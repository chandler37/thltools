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

import org.thdl.tib.text.TibetanMachineWeb;
import org.thdl.tib.text.DuffCode;
import org.thdl.tib.text.TGCPair;
import org.thdl.util.ThdlDebug;

import java.util.HashMap;
import java.util.ArrayList;

/** A list of {@link TPair TPairs}, typically corresponding to
 *  one tsheg bar.  <i>l</i>' in the design doc is an TPairList.
 *
 *  @author David Chandler */
class TPairList {
    /** FIXME: change me and see if performance improves. */
    private static final int INITIAL_SIZE = 1;

    /** a fast, non-thread-safe, random-access list implementation: */
    private ArrayList al;

    /** Creates a new list containing just p. */
    public TPairList(TPair p) {
        al = new ArrayList(1);
        add(p);
    }

    /** Creates an empty list. */
    public TPairList() {
        al = new ArrayList(INITIAL_SIZE);
    }

    /** Creates an empty list with the capacity to hold N items. */
    public TPairList(int N) {
        al = new ArrayList(N);
    }

    /** Returns the ith pair in this list. */
    public TPair get(int i) { return (TPair)al.get(i); }

    /** Returns the ith non-disambiguator pair in this list. This is
     *  O(size()). */
    public TPair getNthNonDisambiguatorPair(int n) {
        TPair p;
        int count = 0;
        for (int i = 0; i < size(); i++) {
            p = get(i);
            if (!p.isDisambiguator())
                if (count++ == n)
                    return p;
        }
        throw new IllegalArgumentException("n, " + n + " is too big for this list of pairs, " + toString());
    }

    /** Returns the number of pairs in this list that are not entirely
     *  disambiguators. */
    public int sizeMinusDisambiguators() {
        int count = 0;
        for (int i = 0; i < size(); i++) {
            if (!get(i).isDisambiguator())
                ++count;
        }
        return count;
    }

    /** Adds p to the end of this list. */
    public void add(TPair p) {
        if (p == null || (p.getLeft() == null && p.getRight() == null))
            throw new IllegalArgumentException("p is weird");
        al.add(p);
    }

    /** Prepends p to the current list of TPairs. */
    public void prepend(TPair p) {
        al.add(0, p);
    }

    /** Returns the number of TPairs in this list. */
    public int size() { return al.size(); }

    /** Returns a human-readable representation.
     *  @return something like [(R . ), (D . O)] */
    public String toString2() {
        return al.toString();
    }

    /** Returns a human-readable representation like {G}{YA} or
     *  {G-}{YA}. */
    public String toString() {
        int sz = size();
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < sz; i++) {
            b.append('{');
            if (null != get(i).getLeft())
                b.append(get(i).getLeft());
            if (null != get(i).getRight())
                b.append(get(i).getRight());
            b.append('}');
        }
        return b.toString();
    }

    /** Returns the ACIP corresponding to this TPairList.  It will
     *  be as ambiguous as the input.  It may have more disambiguators
     *  than the original, such as in the case of the ACIP {1234}. */
    String recoverACIP() {
        StringBuffer original = new StringBuffer();
        int sz = size();
        for (int i = 0; i < sz; i++) {
            TPair p = get(i);
            if (p.getLeft() != null)
                original.append(p.getLeft());
            if (p.getRight() != null)
                original.append(p.getRight());
        }
        return original.toString();
    }

    /** Returns true if this list contains ( . <vowel>) or (A . ),
     *  which are two simple errors you encounter if you interpret DAA
     *  or TAA or DAI or DAE the wrong way. */
    boolean hasSimpleError() {
        int sz = size();
        for (int i = 0; i < sz; i++) {
            TPair p = get(i);
            if ((null == p.getLeft() && !"-".equals(p.getRight()))
                || ("A".equals(p.getLeft()) && null == p.getRight()))
                return true;
        }
        return false;
    }

    /** Finds errors so simple that they can be detected without using
     *  the rules of Tibetan spelling (i.e., tsheg bar syntax).
     *  Returns an error message, or null if there is no error that
     *  you can find without the help of tsheg bar syntax rules. */
    // FIXME: This is needlessly ACIP specific -- rename and change text of messages
    String getACIPError() {
        int sz = size();
        if (0 == sz)
            return "Warning, empty tsheg bar found while converting from ACIP!";
        boolean first = true;
        StringBuffer rv = null;
        boolean mustBeEntirelyNumeric = get(0).isNumeric();
        for (int i = 0; i < sz; i++) {
            TPair p = get(i);
            if (mustBeEntirelyNumeric != p.isNumeric())
                return "Cannot convert ACIP " + recoverACIP() + " because it contains a number but also a non-number.";

            if ((i == 0 && "V".equals(p.getLeft()))
                || (i > 0 && "V".equals(p.getLeft())
                    && (null != get(i - 1).getRight()
                        && !"+".equals(get(i - 1).getRight())))) {
                if (first) {
                    first = false;
                    rv = new StringBuffer("Cannot convert ACIP ");
                    rv.append(recoverACIP());
                    rv.append(" because {V}, wa-zur, appears without being subscribed to a consonant.");
                } else {
                    rv.append("; also, {V}, wa-zur, appears without being subscribed to a consonant");
                }
            } else if ("A".equals(p.getLeft()) && (null == p.getRight() || "".equals(p.getRight()))) {
                if (first) {
                    first = false;
                    rv = new StringBuffer("Cannot convert ACIP ");
                    rv.append(recoverACIP());
                    rv.append(" because we would be required to assume that {A} is a consonant, when it is not clear if it is a consonant or a vowel.");
                } else {
                    rv.append("; also, we would be required to assume that {A} is a consonant, when it is not clear if it is a consonant or a vowel.");
                }
            } else if ((null == p.getLeft() && !"-".equals(p.getRight()))
                       || (null != p.getLeft()
                           && !ACIPRules.isConsonant(p.getLeft())
                           && !p.isNumeric())) {
                if (first) {
                    first = false;
                    rv = new StringBuffer("Cannot convert ACIP ");
                    rv.append(recoverACIP());
                    rv.append(" because ");
                    if (null == p.getLeft()) {
                        rv.append(p.getRight());
                        rv.append(" is a \"vowel\" without an associated consonant");
                    } else {
                        rv.append(p.getLeft());
                        rv.append(" is not an ACIP consonant");
                    }
                } else {
                    if (null == p.getLeft()) {
                        rv.append("; also, ");
                        rv.append(p.getRight());
                        rv.append(" is an ACIP \"vowel\" without an associated consonant");
                    } else {
                        rv.append("; also, ");
                        rv.append(p.getLeft());
                        rv.append(" is not an ACIP consonant");
                    }
                }
            }
        }
        if ("+".equals(get(sz - 1).getRight())) {
            if (first) {
                first = false;
                rv = new StringBuffer("Cannot convert ACIP ");
                rv.append(recoverACIP());
                rv.append(" because it ends with a {+}.");
            } else {
                rv.append("; also, it ends with a {+}.");
            }
        }

        // FIXME: really this is a warning, not an error:
        if ("-".equals(get(sz - 1).getRight())) {
            if (first) {
                first = false;
                rv = new StringBuffer("Cannot convert ACIP ");
                rv.append(recoverACIP());
                rv.append(" because it ends with a {-}.");
            } else {
                rv.append("; also, it ends with a {-}.");
            }
        }

        return (rv == null) ? null : rv.toString();
    }

    /** Returns true if and only if either x is an TPairList object
     *  representing the same TPairs in the same order or x is a
     *  String that is equals to the result of {@link #toString()}. */
    public boolean equals(Object x) {
        if (x instanceof TPairList) {
            return al.equals(((TPairList)x).al);
        } else if (x instanceof String) {
            return toString().equals(x) || toString2().equals(x);
        }
        return false;
    }

    /** Returns true if and only if this list is empty. */
    public boolean isEmpty() { return al.isEmpty(); }

    /** Returns a hashCode appropriate for use with our {@link
     *  #equals(Object)} method. */
    public int hashCode() { return al.hashCode(); }

    private static final int STOP_STACK = 0;
    private static final int KEEP_STACKING = 1;
    private static final int ALWAYS_KEEP_STACKING = 2;
    private static final int ALWAYS_STOP_STACKING = 3;

    /** Returns a set (as as ArrayList) of all possible TStackLists.
     *  Uses knowledge of Tibetan spelling rules (i.e., tsheg bar
     *  syntax) to do so.  If this list of pairs has something clearly
     *  illegal in it, or is empty, or is merely a list of
     *  disambiguators etc., then this returns null.  Never returns an
     *  empty parse tree.
     */
    public TParseTree getParseTree() {
        // We treat [(B . ), (G . +), (K . ), (T . A)] as if it could
        // be {B+G+K+T} or {B}{G+K+T}; we handle prefixes specially
        // this way.  [(T . ), (G . +), (K . ), (T . A)] is clearly
        // {T+G+K+TA}
        //
        // We don't care if T+G+K+T is in TMW or not -- there is no
        // master list of stacks.

        int sz = size();
        for (int i = 0; i < sz; i++) {
            TPair p = get(i);
            if (p.getLeft() == null && !"-".equals(p.getRight()))
                return null; // clearly illegal.
            if ("+".equals(p.getLeft()))
                return null; // clearly illegal.
            if (":".equals(p.getLeft()))
                return null; // clearly illegal.
            if ("m".equals(p.getLeft()))
                return null; // clearly illegal.
            if ("m:".equals(p.getLeft()))
                return null; // clearly illegal.
        }


        TParseTree pt = new TParseTree();
        if (sz < 1) return null;

        // When we see a stretch of ACIP without a disambiguator or a
        // vowel, that stretch is taken to be one stack unless it may
        // be prefix-root or suffix-postsuffix or suffix/postsuffix-'
        // -- the latter necessary because GAMS'I is GAM-S-'I, not
        // GAM-S+'I.  'UR, 'US, 'ANG, 'AM, 'I, 'O, 'U -- all begin
        // with '.  So we can have zero, one, two, or three special
        // break locations.  (The kind that aren't special are the
        // break after G in G-DAMS, or the break after G in GADAMS or
        // GEDAMS.)
        //
        // If a nonnegative number appears in breakLocations[i], it
        // means that pair i may or may not be stacked with pair i+1.
        int nextBreakLoc = 0;
        int breakLocations[] = { -1, -1, -1 };

        boolean mayHavePrefix;

        // Handle the first pair specially -- it could be a prefix.
        if (ddebug) System.out.println("i is " + 0);
        if ((mayHavePrefix = get(0).isPrefix())
            && sz > 1
            && null == get(0).getRight()) {
            // special case: we must have a branch in the parse tree
            // for the initial part of this pair list.  For example,
            // is DKHYA D+KH+YA or D-KH+YA?  It depends on prefix
            // rules (can KH+YA take a DA prefix?), so the parse tree
            // includes both.
            breakLocations[nextBreakLoc++] = 0;
        }

        // stack numbers start at 1.
        int stackNumber = (get(0).endsACIPStack()) ? 2 : 1;
        // this starts at 0.
        int stackStart = (get(0).endsACIPStack()) ? 1 : 0;

        int numeric = get(0).isNumeric() ? 1 : (get(0).isDisambiguator() ? 0 : -1);

        for (int i = 1; i < sz; i++) {
            if (ddebug) System.out.println("i is " + i);
            TPair p = get(i);

            // GA-YOGS should be treated like GAYOGS or G-YOGS:
            if (p.isDisambiguator()) continue;

            boolean nn;
            if ((nn = p.isNumeric()) && ("+".equals(get(i-1).getRight())
                                         || "+".equals(p.getRight())))
                return null; // clearly illegal.  You can't stack numbers.
            if (nn) {
                if (-1 == numeric)
                    return null; // you can't mix numbers and letters.
                else if (0 == numeric)
                    numeric = 1;
            } else if (!p.isDisambiguator()) {
                if (numeric == 1)
                    return null; // you can't mix numbers and letters.
                else if (0 == numeric)
                    numeric = -1;
            }

            if (i+1==sz || p.endsACIPStack()) {
                if (/* the stack ending here might really be
                       suffix-postsuffix or
                       suffix-appendage or
                       suffix-postsuffix-appendage */
                    (mayHavePrefix && (stackNumber == 2 || stackNumber == 3))
                    || (!mayHavePrefix && (stackNumber == 2))) {
                    if (i > stackStart) {
                        if (get(stackStart).isSuffix()
                            && (get(stackStart+1).isPostSuffix() // suffix-postsuffix
                                || "'".equals(get(stackStart+1).getLeft()))) // suffix-appendage
                            breakLocations[nextBreakLoc++] = stackStart;
                        if (i > stackStart + 1) {
                            // three to play with, maybe it's
                            // suffix-postsuffix-appendage.
                            if (get(stackStart).isSuffix()
                                && get(stackStart+1).isPostSuffix()
                                && "'".equals(get(stackStart+2).getLeft()))
                                breakLocations[nextBreakLoc++] = stackStart+1;
                        }
                    }
                    // else no need to insert a breakLocation, we're
                    // breaking hard.
                }
                if (/* the stack ending here might really be
                       postsuffix-appendage (e.g., GDAM-S'O) */
                    (mayHavePrefix && (stackNumber == 3 || stackNumber == 4))
                    || (!mayHavePrefix && (stackNumber == 3))) {
                    if (i == stackStart+1) { // because GDAM--S'O is illegal, and because it's 'ANG, not 'NG, 'AM, not 'M -- ' always ends the stack
                        if (get(stackStart).isPostSuffix()
                            && "'".equals(get(stackStart+1).getLeft()))
                            breakLocations[nextBreakLoc++] = stackStart;
                    }
                }
                ++stackNumber;
                stackStart = i+1;
            }
        }
        // FIXME: we no longer need all these breakLocations -- we can handle SAM'AM'ANG without them.

        // Now go from hard break (i.e., (* . VOWEL or -)) to hard
        // break (and there's a hard break after the last pair, of
        // course, even if it is (G . ) or (G . +) [the latter being
        // hideously illegal]).  Between the hard breaks, there will
        // be 1, 2, or 4 (can you see why 8 isn't possible, though
        // numBreaks can be 3?) possible parses.  There are two of DGA
        // in DGAMS'O -- D-GA and D+GA.  There are 4 of MS'O in
        // DGAMS'O -- M-S-'O, M-S+'O, M+S-'O, and M+S+'O.  Add one
        // TStackListList per hard break to pt, the parse tree.
        int startLoc = 0; // which pair starts this hard break?

        // FIXME: assert this
        if ((breakLocations[1] >= 0 && breakLocations[1] <= breakLocations[0])
            || (breakLocations[2] >= 0 && breakLocations[2] <= breakLocations[1]))
            throw new Error("breakLocations is monotonically increasing, ain't it?");

        for (int i = 0; i < sz; i++) {
            if (i+1 == sz || get(i).endsACIPStack()) {
                TStackListList sll = new TStackListList(4); // maximum is 4.

                int numBreaks = 0;
                int breakStart = -1;
                for (int jj = 0; jj < breakLocations.length; jj++) {
                    if (breakLocations[jj] >= startLoc
                        && breakLocations[jj] <= i) {
                        if (breakStart < 0)
                            breakStart = jj;
                        ++numBreaks;
                    }
                }

                // Count from [0, 1<<numBreaks).  At each point,
                // counter equals b2b1b0 in binary.  1<<numBreaks is
                // the number of stack lists there are in this stack
                // list list of the parse tree.  Break at location
                // breakLocations[breakStart+0] if and only if b0 is
                // one, at location breakLocations[breakStart+1] if
                // and only if b1 is one, etc.
                for (int counter = 0; counter < (1<<numBreaks); counter++) {
                    TStackList sl = new TStackList();
                    TPairList currentStack = new TPairList();
                    for (int k = startLoc; k <= i; k++) {
                        if (!get(k).isDisambiguator()) {
                            if (get(k).isNumeric()
                                || (get(k).getLeft() != null
                                    && ACIPRules.isConsonant(get(k).getLeft())))
                                currentStack.add(get(k).insideStack());
                            else
                                return null; // sA, for example, is illegal.
                        }
                        if (k == i || get(k).endsACIPStack()) {
                            if (!currentStack.isEmpty())
                                sl.add(currentStack.asStack());
                            currentStack = new TPairList();
                        } else {
                            if (numBreaks > 0) {
                                for (int j = 0; breakStart+j < 3; j++) {
                                    if (k == breakLocations[breakStart+j]
                                        && 1 == ((counter >> j) & 1)) {
                                        if (!currentStack.isEmpty())
                                            sl.add(currentStack.asStack());
                                        currentStack = new TPairList();
                                        break; // shouldn't matter, but you never know
                                    }
                                }
                            }
                        }
                    }
                    if (!sl.isEmpty()) {
                        sll.add(sl);
                    }
                }

                if (!sll.isEmpty())
                    pt.add(sll);
                startLoc = i+1;
            }
        }


        if (pt.isEmpty()) return null;
        return pt;
    }

    private static final boolean ddebug = false;

    /** Mutates this TPairList object such that the last pair is
     *  empty or is a vowel, but is never the stacking operator ('+')
     *  or a disambiguator (i.e., a '-' on the right).
     *  @return this instance */
    private TPairList asStack() {
        if (!isEmpty()) {
            TPair lastPair = get(size() - 1);
            if ("+".equals(lastPair.getRight()))
                al.set(size() - 1, new TPair(lastPair.getLeft(), null));
            else if ("-".equals(lastPair.getRight()))
                al.set(size() - 1, new TPair(lastPair.getLeft(), null));
        }
        return this;
    }

    /** Adds the TGCPairs corresponding to this list to the end of
     *  pl. Some TPairs correspond to more than one TGCPair
     *  ({AA:}); some TGCPairs correspond to more than one TPair
     *  ({G+YA}).  To keep track, indexList will be appended to in
     *  lockstep with pl.  index (wrapped as an {@link
     *  java.lang#Integer}) will be appended to indexList once each
     *  time we append to pl.  This assumes that this TPairList
     *  corresponds to exactly one Tibetan grapheme cluster (i.e.,
     *  stack).  Note that U+0F7F (ACIP {:}) is part of a stack, not a
     *  stack all on its own. */
    void populateWithTGCPairs(ArrayList pl,
                              ArrayList indexList, int index) {
        int sz = size();
        if (sz == 0) {
            return;
        } else {
            // drop the disambiguator, if there is one.

            boolean isNumeric = false;
            StringBuffer lWylie = new StringBuffer();
            int i;
            // All pairs but the last:
            for (i = 0; i + 1 < sz; i++) {
                lWylie.append(get(i).getWylie());
                if (get(i).isNumeric())
                    isNumeric = true;
            }

            // The last pair:
            TPair p = get(i);
            ThdlDebug.verify(!"+".equals(p.getRight()));
            boolean add_U0F7F = false;
            int where;
            if (p.getRight() != null
                && (where = p.getRight().indexOf(':')) >= 0) {
                // this ':' guy is his own TGCPair.
                add_U0F7F = true;
                StringBuffer rr = new StringBuffer(p.getRight());
                rr.deleteCharAt(where);
                p = new TPair(p.getLeft(), rr.toString());
            }
            boolean hasNonAVowel = (!"A".equals(p.getRight()) && null != p.getRight());
            String thislWylie = ACIPRules.getWylieForACIPConsonant(p.getLeft());
            if (thislWylie == null) {
                char ch;
                if (p.isNumeric()) {
                    thislWylie = p.getLeft();
                    isNumeric = true;
                }
            }

            if (null == thislWylie)
                throw new Error("BADNESS AT MAXIMUM: p is " + p + " and thislWylie is " + thislWylie);
            lWylie.append(thislWylie);
            StringBuffer ll = new StringBuffer(lWylie.toString());
            int ww;
            while ((ww = ll.indexOf("+")) >= 0)
                ll.deleteCharAt(ww);
            boolean isTibetan = TibetanMachineWeb.isWylieTibetanConsonantOrConsonantStack(ll.toString());
            boolean isSanskrit = TibetanMachineWeb.isWylieSanskritConsonantStack(lWylie.toString());
            if (ddebug && !isTibetan && !isSanskrit && !isNumeric) {
                System.out.println("OTHER for " + lWylie + " with vowel " + ACIPRules.getWylieForACIPVowel(p.getRight()) + " and p.getRight()=" + p.getRight());
            }
            if (isTibetan && isSanskrit) {
                 // RVA, e.g.  It must be Tibetan because RWA is what
                 // you'd use for RA over fixed-form WA.
                isSanskrit = false;
            }
            if (ddebug && hasNonAVowel && ACIPRules.getWylieForACIPVowel(p.getRight()) == null) {
                System.out.println("vowel " + ACIPRules.getWylieForACIPVowel(p.getRight()) + " and p.getRight()=" + p.getRight());
            }
            TGCPair tp;
            indexList.add(new Integer(index));
            tp = new TGCPair(lWylie.toString(),
                             (hasNonAVowel
                              ? ACIPRules.getWylieForACIPVowel(p.getRight())
                              : ""),
                             (isNumeric
                              ? TGCPair.TYPE_OTHER
                              : (isSanskrit
                                 ? TGCPair.TYPE_SANSKRIT
                                 : (isTibetan
                                    ? TGCPair.TYPE_TIBETAN
                                    : TGCPair.TYPE_OTHER))));
            pl.add(tp);
            if (add_U0F7F) {
                indexList.add(new Integer(index));
                pl.add(new TGCPair("H", null, TGCPair.TYPE_OTHER));
            }
        }
    }

    private static HashMap unicodeExceptionsMap = null;

    /** Appends legal Unicode corresponding to this stack to sb.
     *  FIXME: which normalization form, if any? */
    void getUnicode(StringBuffer sb) {
        // The question is this: U+0FB1 or U+0FBB?  U+0FB2 or U+0FBC?
        // The answer: always the usual form, not the full form,
        // except for a few known stacks (all the ones with full-form,
        // non-WA subjoined consonants in TMW: [in EWTS, they are:]
        // r+Y, N+D+Y, N+D+R+y, k+Sh+R).  Note that wa-zur, U+0FAD, is
        // never confused for U+0FBA because "V" and "W" are different
        // transliterations.  EWTS {r+W} thus needs no special
        // treatment during ACIP->Unicode.

        StringBuffer nonVowelSB = new StringBuffer();
        int beginningIndex = sb.length();
        boolean subscribed = false;
        int szz = size();
        int i;
        for (i = 0; i + ((1 == szz) ? 0 : 1) < szz; i++) {
            TPair p = get(i);

            // FIXME: change this to an assertion:
            if ((1 != szz) && null != p.getRight() && !"+".equals(p.getRight()))
                throw new Error("Oops -- this stack (i.e., " + toString() + ") is funny, so we can't generate proper Unicode for it.  i is " + i + " and size is " + szz);

            p.getUnicode(nonVowelSB, subscribed);
            subscribed = true;
        }
        if (szz > 1) {
            TPair p = get(i);
            StringBuffer vowelSB = new StringBuffer();
            p.getUnicode(nonVowelSB, vowelSB, subscribed /* which is true */);

            if (null == unicodeExceptionsMap) {
                unicodeExceptionsMap = new HashMap();
                unicodeExceptionsMap.put("\u0f69\u0fb2", "\u0f69\u0fbc"); // KshR (variety 1)
                unicodeExceptionsMap.put("\u0f40\u0fb5\u0fb2", "\u0f40\u0fb5\u0fbc"); // KshR (variety 2)
                unicodeExceptionsMap.put("\u0f4e\u0f9c\u0fb2\u0fb1", "\u0f4e\u0f9c\u0fbc\u0fb1"); // ndRY
                unicodeExceptionsMap.put("\u0f4e\u0f9c\u0fb1", "\u0f4e\u0f9c\u0fbb"); // ndY
                unicodeExceptionsMap.put("\u0f61\u0fb1", "\u0f61\u0fbb"); // YY
                unicodeExceptionsMap.put("\u0f62\u0fb1", "\u0f62\u0fbb"); // RY
            }
            String mapEntry = (String)unicodeExceptionsMap.get(nonVowelSB.toString());
            if (null != mapEntry)
                sb.append(mapEntry);
            else
                sb.append(nonVowelSB);
            sb.append(vowelSB);
        } else {
            sb.append(nonVowelSB);
        }
    }

    /** Appends the DuffCodes that correspond to this grapheme cluster
     *  to duffsAndErrors, or appends a String that is an error
     *  message saying that TMW cannot represent this grapheme
     *  cluster. */
    void getDuff(ArrayList duffsAndErrors) {
        int previousSize = duffsAndErrors.size();
        StringBuffer wylieForConsonant = new StringBuffer();
        for (int x = 0; x + 1 < size(); x++) {
            wylieForConsonant.append(get(x).getWylie(false));
        }
        TPair lastPair = get(size() - 1);
        wylieForConsonant.append(lastPair.getWylie(true));
        String hashKey = wylieForConsonant.toString();

        // Because EWTS has special handling for full-formed
        // subjoined consonants, we have special handling here.
        if ("r+y".equals(hashKey))
            hashKey = "r+Y";
        else if ("y+y".equals(hashKey))
            hashKey = "y+Y";
        else if ("N+D+y".equals(hashKey))
            hashKey = "N+D+Y";
        else if ("N+D+r+y".equals(hashKey))
            hashKey = "N+D+R+y";
        else if ("k+Sh+r".equals(hashKey))
            hashKey = "k+Sh+R";
        
        // TPair.getWylie(..) returns "W" sometimes when "w" is what
        // really should be returned.  ("V" always causes "w" to be
        // returned, which is fine.)  We'll change "W" to "w" here if
        // we need to.  We do it only for a few known stacks (the ones
        // in TMW).
        if ("W".equals(hashKey))
            hashKey = "w";
        else if ("W+y".equals(hashKey))
            hashKey = "w+y";
        else if ("W+r".equals(hashKey))
            hashKey = "w+r";
        else if ("W+n".equals(hashKey))
            hashKey = "w+n";
        else if ("W+W".equals(hashKey))
            hashKey = "w+W";
        // We're NOT doing it for r+W etc., on purpose.

        if (!TibetanMachineWeb.isKnownHashKey(hashKey)) {
            hashKey = hashKey.replace('+', '-');
            if (!TibetanMachineWeb.isKnownHashKey(hashKey)) {
                duffsAndErrors.add("The ACIP {" + recoverACIP() + "} cannot be represented with the TibetanMachine or TibetanMachineWeb fonts because no such glyph exists in these fonts.");
                return;
            }
        }
        if (lastPair.getRight() == null || lastPair.equals("-")) {
            duffsAndErrors.add(TibetanMachineWeb.getGlyph(hashKey));
        } else {
            ACIPRules.getDuffForACIPVowel(duffsAndErrors,
                                          TibetanMachineWeb.getGlyph(hashKey),
                                          lastPair.getRight());
        }
        if (previousSize == duffsAndErrors.size())
            throw new Error("TPairList with no duffs? " + toString()); // FIXME: change to assertion.
    }
}

