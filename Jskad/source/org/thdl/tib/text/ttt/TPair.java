/*
The contents of this file are subject to the THDL Open Community License
Version 1.0 (the "License"); you may not use this file except in compliance
with the License. You may obtain a copy of the License on the THDL web site 
(http://www.thdl.org/).

Software distributed under the License is distributed on an "AS IS" basis, 
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the 
License for the specific terms governing rights and limitations under the 
License. 

The Initial Developer of this software is the Tibetan and Himalayan Digital
Library (THDL). Portions created by the THDL are Copyright 2003 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text.ttt;

import org.thdl.util.ThdlDebug;
import org.thdl.tib.text.TibetanMachineWeb;
import org.thdl.tib.text.DuffCode;

import java.util.ArrayList;

/** An ordered pair used in ACIP-to-TMW conversion.  The left side is
 *  the consonant or empty; the right side is the vowel, '+', or '-'.
 *  @author David Chandler */
/* BIG FIXME: make this package work for EWTS, not just ACIP. */
class TPair {
    /** The left side, or null if there is no left side.  That is, the
     *  non-vowel, non-'m', non-':', non-'-', non-'+' guy. */
    private String l;
    String getLeft() {
        ThdlDebug.verify(!"".equals(l));
        return l;
    }

    /** The right side. That is, the vowel, with 'm' or ':' "vowel"
     *  after it if appropriate, or "-" (disambiguator), or "+"
     *  (stacking), or null otherwise. */
    private String r;
    String getRight() {
        ThdlDebug.verify(!"".equals(r));
        return r;
    }

    /** Constructs a new TPair with left side l and right side r.
     *  Use null or the empty string to represent an absence. */
    TPair(String l, String r) {
        // Normalize:
        if (null != l && l.equals("")) l = null;
        if (null != r && r.equals("")) r = null;

        this.l = l;
        this.r = r;
    }

    /** Returns a nice String representation.  Returns "(D . E)" for
     *  ACIP {DE}, e.g., and (l . r) in general. */
    public String toString() {
        return "("
            + ((null == l) ? "" : l) + " . "
            + ((null == r) ? "" : r) + ")";
    }

    /** Returns the number of ACIP characters that make up this
     *  TPair. */
    int size() {
        return (((l == null) ? 0 : l.length())
                + ((r == null) ? 0 : r.length()));
    }

    /** Returns an TPair that is like this one except that it is
     *  missing N characters.  The characters are taken from r, the
     *  right side, first and from l, the left side, second.  The pair
     *  returned may be illegal, such as the (A . ') you can get from
     *  ACIP {A'AAMA}.
     *  @throws IllegalArgumentException if N is out of range */
    TPair minusNRightmostACIPCharacters(int N)
        throws IllegalArgumentException
    {
        int sz;
        String newL = l, newR = r;
        if (N > size())
            throw new IllegalArgumentException("Don't have that many to remove.");
        if (N < 1)
            throw new IllegalArgumentException("You shouldn't call this if you don't want to remove any.");
        if (null != r && (sz = r.length()) > 0) {
            int min = Math.min(sz, N);
            newR = r.substring(0, sz - min);
            N -= min;
        }
        if (N > 0) {
            sz = l.length();
            newL = l.substring(0, sz - N);
        }
        return new TPair(newL, newR);
    }

    /** Returns true if and only if this is nonempty and is l, if
     *  present, is a legal ACIP consonant, and is r, if present, is a
     *  legal ACIP vowel. */
    boolean isLegal() {
        if (size() < 1)
            return false;
        if (null != l && !ACIPRules.isConsonant(l))
            return false;
        if (null != r && !ACIPRules.isVowel(r))
            return false;
        return true;
    }

    /** Returns true if and only if this pair could be a Tibetan
     *  prefix. */
    boolean isPrefix() {
        return (null != l
                && ((null == r || "".equals(r))
                    || "-".equals(r)
                    || "A".equals(r)) // FIXME: though check for BASKYABS and warn because BSKYABS is more common
                && ACIPRules.isACIPPrefix(l));
    }

    /** Returns true if and only if this pair could be a Tibetan
     *  secondary suffix. */
    boolean isPostSuffix() {
        return (null != l
                && ((null == r || "".equals(r))
                    || "-".equals(r)
                    || "A".equals(r)) // FIXME: though warn about GAMASA vs. GAMS
                && ACIPRules.isACIPPostsuffix(l));
    }

    /** Returns true if and only if this pair could be a Tibetan
     *  suffix. FIXME: ACIP specific, just like isPostSuffix() and isPrefix() */
    boolean isSuffix() {
        return (null != l
                && ((null == r || "".equals(r))
                    || "-".equals(r)
                    || "A".equals(r))
                && ACIPRules.isACIPSuffix(l));
    }

    /** Returns true if and only if this pair is merely a
     *  disambiguator. */
    boolean isDisambiguator() {
        return ("-".equals(r) && getLeft() == null);
    }

    /** Yep, this works for TPairs. */
    public boolean equals(Object x) {
        if (x instanceof TPair) {
            TPair p = (TPair)x;
            return ((getLeft() == p.getLeft() || (getLeft() != null && getLeft().equals(p.getLeft())))
                    || (getRight() == p.getRight() || (getRight() != null && getRight().equals(p.getRight()))));
        }
        return false;
    }

    /** Returns an TPair that is like this pair except that it has
     *  a "+" on the right if this pair is empty on the right and is
     *  empty on the right if this pair has a disambiguator (i.e., a
     *  '-') on the right.  May return itself (but never mutates this
     *  instance). */
    TPair insideStack() {
        if (null == getRight())
            return new TPair(getLeft(), "+");
        else if ("-".equals(getRight()))
            return new TPair(getLeft(), null);
        else
            return this;
    }

    /** Returns true if this pair contains a Tibetan number. */
    boolean isNumeric() {
        char ch;
        return (l != null && l.length() == 1 && (ch = l.charAt(0)) >= '0' && ch <= '9');
    }

    String getWylie() {
        return getWylie(false);
    }

    /** Returns the EWTS Wylie that corresponds to this pair if
     *  justLeft is false, or the EWTS Wylie that corresponds to just
     *  {@link #getLeft()} if justLeft is true.
     *
     *  <p>Returns "W" for ACIP "W", "r" for ACIP "R", y for ACIP "Y",
     *  even though sometimes the EWTS for those is "w", "R", or "Y".
     *  Handle that in the caller. */
    String getWylie(boolean justLeft) {
        String leftWylie = null;
        if (getLeft() != null) {
            leftWylie = ACIPRules.getWylieForACIPConsonant(getLeft());
            if (leftWylie == null) {
                if (isNumeric())
                    leftWylie = getLeft();
            }
        }
        if (null == leftWylie) leftWylie = "";
        if (justLeft) return leftWylie;
        String rightWylie = null;
        if ("-".equals(getRight()))
            rightWylie = ".";
        else if ("+".equals(getRight()))
            rightWylie = "+";
        else if (getRight() != null)
            rightWylie = ACIPRules.getWylieForACIPVowel(getRight());
        if (null == rightWylie) rightWylie = "";
        return leftWylie + rightWylie;
    }

    /** Appends legal Unicode corresponding to this (possible
     *  subscribed) pair to sb.  FIXME: which normalization form,
     *  if any? */
    void getUnicode(StringBuffer sb, boolean subscribed) {
        getUnicode(sb, sb, subscribed);
    }

    /** Appends legal Unicode corresponding to this (possible
     *  subscribed) pair to consonantSB (for the non-vowel part) and
     *  vowelSB (for the vowelish part ({'EEm:}, e.g.).  FIXME: which
     *  normalization form, if any? */
    void getUnicode(StringBuffer consonantSB, StringBuffer vowelSB,
                    boolean subscribed) {
        if (null != getLeft()) {
            String x = ACIPRules.getUnicodeFor(getLeft(), subscribed);
            if (null == x) throw new Error("TPair: " + getLeft() + " has no Uni");
            consonantSB.append(x);
        }
        if (null != getRight()
            && !("-".equals(getRight()) || "+".equals(getRight()) || "A".equals(getRight()))) {
            String x = ACIPRules.getUnicodeFor(getRight(), subscribed);
            if (null == x) throw new Error("TPair: " + getRight() + " has no Uni");
            vowelSB.append(x);
        }
    }

    /** Returns true if this pair is surely the last pair in an ACIP
     *  stack. Stacking continues through (* . ) and (* . +), but
     *  stops anywhere else. */
    boolean endsACIPStack() {
        return (getRight() != null && !"+".equals(getRight()));
    }
}
