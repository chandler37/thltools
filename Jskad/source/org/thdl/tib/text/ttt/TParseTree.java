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

import java.util.ArrayList;

/** A list of non-empty list of {@link #TStackListList
 *  TStackListLists} representing all the ways you could break up a
 *  tsheg bar of ACIP into stacks (i.e., grapheme clusters).
 *
 *  @author David Chandler */
class TParseTree {
    /** a fast, non-thread-safe, random-access list implementation: */
    private ArrayList al = new ArrayList();

    /** Creates an empty list. */
    public TParseTree() { }

    /** Returns the ith pair in this list. */
    public TStackListList get(int i) { return (TStackListList)al.get(i); }

    /** Adds p to the end of this list. */
    public void add(TStackListList p)
        throws IllegalArgumentException
    {
        if (p.isEmpty())
            throw new IllegalArgumentException("p is empty");
        al.add(p);
    }

    /** Returns the number of TStackListLists in this list.  See
     * also {@link #numberOfParses()}, which gives a different
     * interpretation of the size of this tree. */
    public int size() { return al.size(); }

    /** Returns the number of different parses one could make from
     *  this parse tree.  Returns zero if this list is empty. */
    public int numberOfParses() {
        if (al.isEmpty()) return 0;
        int k = 1;
        int sz = size();
        for (int i = 0; i < sz; i++) {
            k *= get(i).size();
        }
        return k;
    }
        
    /** Returns the number of {@link #TPair pairs} that are in a
     *  parse of this tree. */
    public int numberOfPairs() {
        if (al.isEmpty()) return 0;
        int k = 1;
        int sz = size();
        for (int i = 0; i < sz; i++) {
            // get(i).get(0) is the same size as get(i).get(1),
            // get(i).get(2), ...
            k += get(i).get(0).size();
        }
        return k;
    }
        
    /** Returns an iterator that will iterate over the {@link
     *  #numberOfParses} available. */
    public ParseIterator getParseIterator() {
        return new ParseIterator(al);
    }

    /** Returns a list containing the legal parses of this parse tree.
     *  By &quot;legal&quot;, we mean a sequence of stacks that is
     *  legal by the rules of Tibetan tsheg bar syntax (sometimes
     *  called spelling).  This will return the {G-YA} parse of {GYA}
     *  as well as the {GYA} parse, so watch yourself. */
    public TStackListList getLegalParses() {
        TStackListList sll = new TStackListList(2); // save memory
        ParseIterator pi = getParseIterator();
        while (pi.hasNext()) {
            TStackList sl = pi.next();
            if (sl.isLegalTshegBar().isLegal) {
                sll.add(sl);
            }
        }
        return sll;
    }

    /** Returns a list containing the parses of this parse tree that
     *  are not clearly illegal. */
    public TStackListList getNonIllegalParses() {
        TStackListList sll = new TStackListList(2); // save memory
        ParseIterator pi = getParseIterator();
        while (pi.hasNext()) {
            TStackList sl = pi.next();
            if (!sl.isClearlyIllegal()) {
                sll.add(sl);
            }
        }
        return sll;
    }

    /** Returns the best parse, if there is a unique parse that is
     *  clearly preferred to other parses.  Basically, if there's a
     *  unique legal parse, you get it.  If there's not, but there is
     *  a unique non-illegal parse, you get it.  If there's not a
     *  unique answer, null is returned. */
    // {TZANDRA} is not solved by this, DLC NOW.  Solve PADMA PROBLEM!

    // DLC by using this we can get rid of single-sanskrit-gc, eh?
    public TStackList getBestParse() {
        TStackListList up = getUniqueParse();
        if (up.size() == 1)
            return up.get(0);
        else if (up.size() == 2) {
        }
        up = getNonIllegalParses();
        int sz = up.size();
        if (up.size() == 1) {
            return up.get(0);
        }
        return null;
    }

    /** Returns a list containing the unique legal parse of this parse
     *  tree if there is a unique legal parse.  Note that {SRAS} has a
     *  unique legal parse, though {SRS} has two equally good parses;
     *  i.e., note that the {A} vowel is treated specially here
     *  (unlike in {@link #getLegalParses()}). Returns an empty list
     *  if there are no legal parses.  Returns a list containing all
     *  legal parses if there two or more equally good parses.  By
     *  &quot;legal&quot;, we mean a sequence of stacks that is legal
     *  by the rules of Tibetan tsheg bar syntax (sometimes called
     *  spelling). */
    public TStackListList getUniqueParse() {
        TStackListList allLegalParses = new TStackListList(2); // save memory
        TStackListList legalParsesWithVowelOnRoot = new TStackListList(1);
        ParseIterator pi = getParseIterator();
        while (pi.hasNext()) {
            TStackList sl = pi.next();
            BoolPair bpa = sl.isLegalTshegBar();
            if (bpa.isLegal) {
                if (bpa.isLegalAndHasAVowelOnRoot)
                    legalParsesWithVowelOnRoot.add(sl);
                allLegalParses.add(sl);
            }
        }
        if (legalParsesWithVowelOnRoot.size() == 1)
            return legalParsesWithVowelOnRoot;
        else {
            if (legalParsesWithVowelOnRoot.size() == 2) {
                // DLC is this even valid?
                if (legalParsesWithVowelOnRoot.get(0).size() != 1 + legalParsesWithVowelOnRoot.get(1).size())
                    throw new Error("Something other than the G-YA vs. GYA case appeared.  Sorry for your trouble! " + legalParsesWithVowelOnRoot.get(0) + " ;; " + legalParsesWithVowelOnRoot.get(1));
                return new TStackListList(legalParsesWithVowelOnRoot.get(1));
            }
            if (allLegalParses.size() == 2) {
                // DLC is this even valid?
                if (allLegalParses.get(0).size() != 1 + allLegalParses.get(1).size())
                    throw new Error("Something other than the G-YA vs. GYA case appeared.  Sorry for your trouble! " + allLegalParses.get(0) + " ;; " + allLegalParses.get(1));
                return new TStackListList(allLegalParses.get(1));
            }
            return allLegalParses;
        }
    }

    /** Returns a human-readable representation. */
    public String toString() {
        return al.toString();
    }

    /** Returns true if and only if either x is an TParseTree
     *  object representing the same TPairLists in the same order
     *  or x is a String that is equals to the result of {@link
     *  #toString()}. */
    public boolean equals(Object x) {
        if (x instanceof TParseTree) {
            return al.equals(((TParseTree)x).al);
        } else if (x instanceof String) {
            return toString().equals(x);
        }
        return false;
    }

    /** Returns a hashCode appropriate for use with our {@link
     *  #equals(Object)} method. */
    public int hashCode() { return al.hashCode(); }
}
