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

    /** Returns the ith list of stack lists in this parse tree. */
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
            if (sl.isLegalTshegBar(false).isLegal) {
                sll.add(sl);
            }
        }
        return sll;
    }

    /** Returns a list (never null) containing the parses of this
     *  parse tree that are not clearly illegal. */
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
        TStackListList up = getUniqueParse(false);
        if (up.size() == 1)
            return up.get(0);

        up = getNonIllegalParses();
        int sz = up.size();
        if (sz == 1) {
            return up.get(0);
        } else if (sz > 1) {
            // {PADMA}, for example.  Our technique is to go from the
            // left and stack as much as we can.  So {PA}{D}{MA} is
            // inferior to {PA}{D+MA}, and {PA}{D+MA}{D}{MA} is
            // inferior to {PA}{D+MA}{D+MA}.  We do not look for the
            // minimum number of glyphs, though -- {PA}{N+D}{B+H+R}
            // and {PA}{N}{D+B+H+R} tie by that score, but the former
            // is the clear winner.

            // We give a warning about these, optionally, so that
            // users can produce output that even a dumb ACIP reader
            // can understand.  See getWarning("All", ..).

            // if j is in this list, then up.get(j) is still a
            // potential winner.
            ArrayList candidates = new ArrayList(sz);
            for (int i = 0; i < sz; i++)
                candidates.add(new Integer(i));
            boolean keepGoing = true;
            int stackNumber = 0;
            boolean someoneHasThisStack = true;
            while (someoneHasThisStack && candidates.size() > 1) {
                // maybe none of the candidates have stackNumber+1
                // stacks.  If none do, we'll quit.
                someoneHasThisStack = false;
                int maxGlyphsInThisStack = 0;
                for (int k = 0; k < candidates.size(); k++) {
                    TStackList sl = up.get(((Integer)candidates.get(k)).intValue());
                    if (sl.size() > stackNumber) {
                        int ng;
                        if ((ng = sl.get(stackNumber).size()) > maxGlyphsInThisStack)
                            maxGlyphsInThisStack = ng;
                        someoneHasThisStack = true;
                    }
                }
                // Remove all candidates that aren't keeping up.
                if (someoneHasThisStack) {
                    for (int k = 0; k < candidates.size(); k++) {
                        TStackList sl = up.get(((Integer)candidates.get(k)).intValue());
                        if (sl.size() > stackNumber) {
                            if (sl.get(stackNumber).size() != maxGlyphsInThisStack)
                                candidates.remove(k--);
                        } else throw new Error("impossible!");
                    }
                }
                ++stackNumber;
            }
            if (candidates.size() == 1)
                return up.get(((Integer)candidates.get(0)).intValue());
            else
                return null;
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
     *  spelling).
     *  @param noPrefixTests true if you want to pretend that every
     *  stack can take every prefix, which is not the case in
     *  reality */
    public TStackListList getUniqueParse(boolean noPrefixTests) {
        TStackListList allLegalParses = new TStackListList(2); // save memory
        TStackListList legalParsesWithVowelOnRoot = new TStackListList(1);
        ParseIterator pi = getParseIterator();
        while (pi.hasNext()) {
            TStackList sl = pi.next();
            BoolPair bpa = sl.isLegalTshegBar(noPrefixTests);
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
                if (legalParsesWithVowelOnRoot.get(0).size() != 1 + legalParsesWithVowelOnRoot.get(1).size())
                    throw new Error("Something other than the G-YA vs. GYA case appeared.  Sorry for your trouble! " + legalParsesWithVowelOnRoot.get(0) + " ;; " + legalParsesWithVowelOnRoot.get(1));
                return new TStackListList(legalParsesWithVowelOnRoot.get(1));
            }
            if (allLegalParses.size() == 2) {
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

    /** Returns null if this parse tree is perfectly legal and valid.
     *  Returns a warning for users otherwise.  If and only if
     *  warningLevel is "All", then even unambiguous ACIP like PADMA,
     *  which could be improved by being written as PAD+MA, will cause
     *  a warning.
     *  @param warningLevel "All" if you're paranoid, "Most" to see
     *  warnings about lacking vowels on final stacks, "Some" to see
     *  warnings about lacking vowels on non-final stacks and also
     *  warnings about when prefix rules affect you, "None" if you
     *  like to see IllegalArgumentExceptions.
     *  @param pl the pair list from which this parse tree originated
     *  @param originalACIP the original ACIP, or null if you want
     *  this parse tree to make a best guess. */
    public String getWarning(String warningLevel,
                             TPairList pl,
                             String originalACIP) {
        if (warningLevel != "Some"
            && warningLevel != "Most"
            && warningLevel != "All")
            throw new IllegalArgumentException("warning level bad: is it interned?");

        {
            TStackList bestParse = getBestParse();
            TStackListList noPrefixTestsUniqueParse = getUniqueParse(true);
            if (noPrefixTestsUniqueParse.size() == 1
                && !noPrefixTestsUniqueParse.get(0).equals(bestParse)) {
                return "Warning: We're going with " + bestParse + ", but only because our knowledge of prefix rules says that " + noPrefixTestsUniqueParse.get(0) + " is not a legal Tibetan tsheg bar (\"syllable\")";
            }
        }

        TStackListList up = getUniqueParse(false);
        if (null == up || up.size() != 1) {
            boolean isLastStack[] = new boolean[1];
            TStackListList nip = getNonIllegalParses();
            if (nip.size() != 1) {
                if (null == getBestParse()) {
                    return "Warning: There's not even a unique, non-illegal parse for ACIP {" + ((null != originalACIP) ? originalACIP : recoverACIP()) + "}";
                } else {
                    if (getBestParse().hasStackWithoutVowel(pl, isLastStack)) {
                        if (isLastStack[0]) {
                            if (warningLevel == "All" || warningLevel == "Most")
                                return "Warning: The last stack does not have a vowel in the ACIP {" + ((null != originalACIP) ? originalACIP : recoverACIP()) + "}";
                        } else {
                            return "Warning: There is a stack, before the last stack, without a vowel in the ACIP {" + ((null != originalACIP) ? originalACIP : recoverACIP()) + "}";
                        }
                    }
                    if ("All" == warningLevel) {
                        return "Warning: Though the ACIP {" + ((null != originalACIP) ? originalACIP : recoverACIP()) + "} is unambiguous, it would be more computer-friendly if + signs were used to stack things because there are two (or more) ways to interpret this ACIP if you're not careful.";
                    }
                }
            } else {
                if (nip.get(0).hasStackWithoutVowel(pl, isLastStack)) {
                    if (isLastStack[0]) {
                        if (warningLevel == "All" || warningLevel == "Most")
                            return "Warning: The last stack does not have a vowel in the ACIP {" + ((null != originalACIP) ? originalACIP : recoverACIP()) + "}";
                    } else {
                        return "Warning: There is a stack, before the last stack, without a vowel in the ACIP {" + ((null != originalACIP) ? originalACIP : recoverACIP()) + "}";
                    }
                }
            }
        }
        return null;
    }
    
    /** Returns something akin to the ACIP input (okay, maybe 1-2-3-4
     *  instead of 1234, and maybe AUTPA instead of AUT-PA)
     *  corresponding to this parse tree. */
    public String recoverACIP() {
        ParseIterator pi = getParseIterator();
        if (pi.hasNext()) {
            return pi.next().recoverACIP();
        }
        return null;
    }

    /** Returns a hashCode appropriate for use with our {@link
     *  #equals(Object)} method. */
    public int hashCode() { return al.hashCode(); }

    /** Returns true if and only if this parse tree is empty. */
    public boolean isEmpty() { return al.isEmpty(); }
}