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

import org.thdl.tib.text.TibTextUtils;
import org.thdl.tib.text.TGCList;

import java.util.ArrayList;
import java.util.ListIterator;

/** A list of {@link TPairList TPairLists}, each of which is for
 *  a stack (a grapheme cluster), typically corresponding to one tsheg
 *  bar.
 *
 *  @author David Chandler */
class TStackList {
    /** FIXME: change me and see if performance improves. */
    private static final int INITIAL_SIZE = 1;

    /** a fast, non-thread-safe, random-access list implementation: */
    private ArrayList al;

    /** Creates an empty list. */
    public TStackList() { al = new ArrayList(INITIAL_SIZE); }

    /** Creates a list containing just p. */
    public TStackList(TPairList p) {
        al = new ArrayList(1);
        add(p);
    }

    /** Creates an empty list with the capacity to hold N items. */
    public TStackList(int N) {
        al = new ArrayList(N);
    }

    /** Returns the ith pair in this list. */
    public TPairList get(int i) { return (TPairList)al.get(i); }

    /** Adds p to the end of this list. */
    public void add(TPairList p) { al.add(p); }

    /** Adds all the stacks in c to the end of this list. */
    public void addAll(TStackList c) { al.addAll(c.al); }

    /** Adds all the stacks in c to this list, inserting them at
     *  position k. */
    public void addAll(int k, TStackList c) { al.addAll(k, c.al); }

    /** Returns the number of TPairLists in this list. */
    public int size() { return al.size(); }

    /** Returns true if and only if this list is empty. */
    public boolean isEmpty() { return al.isEmpty(); }

    /** Returns the ACIP input (okay, maybe 1-2-3-4 instead of 1234)
     *  corresponding to this stack list. */
    public String recoverACIP() {
        return toStringHelper(false);
    }

    /** Returns a human-readable representation like {G}{YA} or
     *  {GYA}. */
    public String toString() {
        return toStringHelper(true);
    }

    private String toStringHelper(boolean brackets) {
        int sz = size();
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < sz; i++) {
            if (brackets) b.append('{');
            b.append(get(i).recoverACIP());
            if (brackets) b.append('}');
        }
        return b.toString();
    }

    /** Returns a human-readable representation.
     *  @return something like [[(R . ), (D . O)], [(R . ), (J . E)]] */
    public String toString2() {
        return al.toString();
    }

    /** Returns true if and only if either x is an TStackList
     *  object representing the same TPairLists in the same
     *  order or x is a String that is equals to the result of {@link
     *  #toString()}. */
    public boolean equals(Object x) {
        if (x instanceof TStackList) {
            return al.equals(((TStackList)x).al);
        } else if (x instanceof String) {
            return toString().equals(x) || toString2().equals(x);
        }
        return false;
    }

    /** Returns a hashCode appropriate for use with our {@link
     *  #equals(Object)} method. */
    public int hashCode() { return al.hashCode(); }

    /** Returns an iterator for this list. Mutate this list while
     *  iterating and you'll have to read the code to know what will
     *  happen. */
    public ListIterator listIterator() { return al.listIterator(); }

    /** Returns a pair with {@link BoolPair#isLegal} true if and only
     *  if this list of stacks is a legal tsheg bar by the rules of
     *  Tibetan syntax (sometimes called rules of spelling).  If this
     *  is legal, then {@link BoolPair#isLegalAndHasAVowelOnRoot} will
     *  be true if and only if there is an explicit {A} vowel on the
     *  root stack.
     *  @param noPrefixTests true if you want to pretend that every
     *  stack can take every prefix, which is not the case in
     *  reality */
    public BoolPair isLegalTshegBar(boolean noPrefixTests) {
        // DLC handle PADMA and other Tibetanized Sanskrit fellows consistently.  Right now we only treat single-stack Sanskrit guys as legal.

        TTGCList tgcList = new TTGCList(this);
        StringBuffer warnings = new StringBuffer();
        String candidateType
            = TibTextUtils.getClassificationOfTshegBar(tgcList, warnings, noPrefixTests);

        // preliminary answer:
        boolean isLegal = (candidateType != "invalid");

        if (isLegal) {
            if (isClearlyIllegal())
                isLegal = false;
        }

        boolean isLegalAndHasAVowelOnRoot = false;
        if (isLegal) {
            int rootIndices[]
                = TibTextUtils.getIndicesOfRootForCandidateType(candidateType);
            for (int i = 0; i < 2; i++) {
                if (rootIndices[i] >= 0) {
                    int pairListIndex = tgcList.getTPairListIndex(rootIndices[i]);
                    TPairList pl = get(pairListIndex);
                    TPair p = pl.get(pl.size() - 1);
                    isLegalAndHasAVowelOnRoot
                        = (p.getRight() != null && p.getRight().startsWith("A")); // could be {A:}, e.g.
                    if (isLegalAndHasAVowelOnRoot)
                        break;
                }
            }
        }
        return new BoolPair(isLegal, isLegalAndHasAVowelOnRoot);
    }

    private static final boolean ddebug = false;

    /** Returns true if and only if this stack list contains a clearly
     *  illegal construct, such as an TPair (V . something). */
    boolean isClearlyIllegal() {
        // check for {D}{VA} sorts of things:
        for (int i = 0; i < size(); i++) {
            if (get(i).getACIPError() != null) {
                if (ddebug) System.out.println("ddebug: error is " + get(i).getACIPError());
                return true;
            }
        }
        return false;
    }

    /** Returns true if and only if this stack list contains a stack
     *  that does not end in a vowel or disambiguator.  Note that this
     *  is not erroneous for legal Tibetan like {BRTAN}, where {B} has
     *  no vowel, but it is a warning sign for Sanskrit stacks.
     *  @param opl the pair list from which this stack list
     *  originated
     *  @param isLastStack if non-null, then isLastStack[0] will be
     *  set to true if and only if the very last stack is the only
     *  stack not to have a vowel or disambiguator on it */
    boolean hasStackWithoutVowel(TPairList opl, boolean[] isLastStack) {
        int runningSize = 0;
        for (int i = 0; i < size(); i++) {
            TPairList pl = get(i);
            String l;
            TPair lastPair = opl.getNthNonDisambiguatorPair(runningSize + pl.size() - 1);
            runningSize += pl.size();
            if (null == lastPair.getRight()
                && !((l = lastPair.getLeft()) != null && l.length() == 1
                     && l.charAt(0) >= '0' && l.charAt(0) <= '9')) {
                if (null != isLastStack)
                    isLastStack[0] = (i + 1 == size());
                return true;
            }
        }
        if (runningSize != opl.sizeMinusDisambiguators())
            throw new IllegalArgumentException("opl (" + opl + ") is bad for this stack list (" + toString() + ")");
        return false;
    }

    /** Returns legal Unicode corresponding to this tsheg bar.  DLC FIXME: which normalization form, if any? */
    String getUnicode() {
        StringBuffer u = new StringBuffer(size());
        for (int i = 0; i < size(); i++) {
            get(i).getUnicode(u);
        }
        return u.toString();
    }
}

class BoolPair {
    boolean isLegal;
    boolean isLegalAndHasAVowelOnRoot;
    BoolPair(boolean isLegal, boolean isLegalAndHasAVowelOnRoot) {
        this.isLegal = isLegal;
        this.isLegalAndHasAVowelOnRoot = isLegalAndHasAVowelOnRoot;
    }
}
