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
import org.thdl.tib.text.TGCPair;
import org.thdl.util.ThdlDebug;

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

// DLC [THE FOLLOWIN... appears, so [#comment] or [comment] is possible. [BLANK PAGE] [MISSING PAGE] [FIRST] [SECOND] [DD1] [DD2] [46A.2] [THE ... [FOLLOWING... [PAGE ... [THESE ... @[7B] [SW: OK] [A FIRST... [ADDENDUM... [END ... [Additional [Some [Note [MISSING [DDD] [INCOMPLETE [LINE [DATA 
// [A pair of ... which is part of the text! S0200A.ACE
// [D] is a correction, eh?


// DLC BDE 'BA' ZHIG RGYUN DU BSTEN, ,YENGS KYANG THUB NA [GNYEN PO,)
//     'BYONGS [BLO,) S0375M.ACT


// S0011N.ACT contains [SMON TSIG 'DI'I RTZOM MING MI GSAL,], why the brackets?  IS all this really a correction? DLC?
// DLC: what are () for?

    /** Finds errors so simple that they can be detected without using
     *  the rules of Tibetan spelling (i.e., tsheg bar syntax).
     *  Returns an error message, or null if there is no error that
     *  you can find without the help of tsheg bar syntax rules. */
    // DLC RENAME
        // DLC FIXME: 9BLTA is an error, numbers are all or nothing
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

        // DLC really this is a warning, not an error:
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

    // DLC TEST: BA'I has exactly two syntactically legal parses but just one TStackList.

    /** Returns a set (as as ArrayList) of all possible TStackLists.
     *  Uses knowledge of Tibetan spelling rules (i.e., tsheg bar
     *  syntax) to do so.  If this list of pairs has something clearly
     *  illegal in it, or is empty, or is merely a list of
     *  disambiguators etc., then this returns null.  Never returns an
     *  empty parse tree. */
    public TParseTree getParseTree() {
        TParseTree pt = new TParseTree();
        int sz = size();
        int firstPair = 0;
        for (int i = 0; i < sz; i++) {

            // We treat [(B . ), (G . +), (K . ), (T . A)] as if it
            // could be {B+G+K+T} or {B}{G+K}{T} or {B+G+K}{T} or
            // {B}{G+K+T} (modulo stack legality); we're conservative.
            // (Though some stacks won't be legal.)


            if (ddebug) System.out.println("i is " + i);
            TPair p = get(i);
            if (p.getRight() == null && firstPair + 1 < sz) {
                // Here's the ambiguity.  Let's fill up sl. (B . ) (G
                // . +) (K . A) could be {B+G+KA} or {BA}{G+KA}, so we
                // go until we hit a vowel and then break into
                // TPairLists.
                int start = firstPair;
                int blanks[] = new int[sz - start]; // we may not use all of this.
                int j;
                for (j = start; j < sz; j++) {
                    TPair pj = get(j);
                    boolean isBlank;
                    if (ddebug) System.out.println("right guy is " + pj.getRight());
                    if (pj.isDisambiguator())
                        blanks[j-start] = ALWAYS_STOP_STACKING;
                    else {
                        if (!(isBlank = (pj.getRight() == null)) && !"+".equals(pj.getRight())) {
                            if (ddebug) System.out.println("breaker breaker at j=" + j);
                            break;
                        }
                        blanks[j-start] = isBlank ? STOP_STACK : ALWAYS_KEEP_STACKING;
                    }
                }
                if (j >= sz) j = sz - 1;

                blanks[j-start] = ALWAYS_STOP_STACKING;

                // get(j) [corresponding to blanks[j-i]] is
                // the last pair in the ambiguous stretch; get(i)
                // [corresponding to blanks[0]] is the first.

                // We'll end up doing 2**(j-i+1) (i.e., (1 <<
                // (j-i+1))) iterations.  If that's going to be too
                // many, let's just say there's no legal parse. FIXME:
                // give a nice error message in this case.
                if (ddebug) System.out.println("ddebug: we're going to do 2^" + (j-i+1) + " [or " + (1 << (j-i+1)) + "] wacky iterations!");
                if ((j-i+1) > 13) // if you don't use 13, then change PackageTest.testSlowestTshegBar().
                    return null;

                boolean keepGoing = true;
                TStackListList sll = new TStackListList();
                do {
                    // Add the stack list currently specified by
                    // blanks if all the stacks in it are legal.
// DLC DELETE                    {
//                         ArrayList x = new ArrayList((j-start+1));
//                         for (int ii = 0; ii < (j-start+1); ii++)
//                             x.add(new Integer(blanks[ii]));
//                     }
                    TStackList sl = new TStackList(sz - start);
                    boolean illegal = false;
                    TPairList currentStack = new TPairList();
                    for (int k = 0; k < j-start+1; k++) {
                        TPair pk = get(start + k);
                        if (!pk.isDisambiguator()) {
                            currentStack.add(pk.insideStack());
                            if (blanks[k] == STOP_STACK) {
                                if (currentStack.isLegalTibetanOrSanskritStack())
                                    sl.add(currentStack.asStack());
                                else {
                                    illegal = true;
                                    break;
                                }
                                currentStack = new TPairList();
                            }
                        }
                    }
                    if (!illegal && !currentStack.isEmpty()) {
                        if (currentStack.isLegalTibetanOrSanskritStack()) {
                            TPairList stack = currentStack.asStack();
                            if (ddebug) System.out.println("adding currentStack " + stack + " to sl " + sl);
                            sl.add(stack);
                        } else {
                            illegal = true;
                        }
                    }
                    if (!illegal) {
                        if (ddebug) System.out.println("adding sl " + sl + " to sll " + sll);
                        sll.add(sl);
                    }

                    // Update blanks.  Think of this as doing base 2
                    // arithmetic where STOP_STACK is zero,
                    // KEEP_STACKING is one, and ALWAYS_KEEP_STACKING
                    // and ALWAYS_STOP_STACKING are digits we cannot
                    // modify.  We'll end up doing 2^M iterations,
                    // where M is the number of fields in blanks that
                    // are not equal to ALWAYS_KEEP_STACKING or
                    // ALWAYS_STOP_STACKING.
                    keepGoing = false;
                    for (int k = j-start; k >= 0; k--) {
                        if (blanks[k] == STOP_STACK) {
                            keepGoing = true;
                            blanks[k] = KEEP_STACKING;
                            // reset all digits to the right of k to
                            // "zero":
                            for (int m = k + 1; m < j-start+1; m++) {
                                if (blanks[m] == KEEP_STACKING)
                                    blanks[m] = STOP_STACK;
                            }
                            break;
                        }
                    }
                } while (keepGoing);
                if (sll.isEmpty())
                    return null; // STXAL or shT+ZNAGN, e.g.
                else {
                    if (ddebug) System.out.println("adding sll " + sll + " to parse tree " + pt);
                    pt.add(sll);
                }
                
                if (ddebug) System.out.println("i is " + i + " and j is " + j + " and we are resetting so that i==j+1 next time.");
                i = j;
                firstPair = j + 1;
            } else if ("+".equals(p.getRight())) {
                // Keep firstPair where it is.
            } else {
                // Add all pairs in the range [firstPair, i].  Some
                // pairs are stacks all by themselves, some pairs have
                // '+' on the right and are thus just part of a stack.
                // We'll add a whole number of stacks, though.
                
                // this is initialized to hold the max we might use:
                TStackListList sll
                    = new TStackListList(i - firstPair + 1);

                TPairList currentStack = new TPairList();
                for (int j = firstPair; j <= i; j++) {
                    TPair pj = get(j);
                    if (!pj.isDisambiguator()) {
                        currentStack.add(pj.insideStack());
                        if (!"+".equals(pj.getRight())) {
                            if (currentStack.isLegalTibetanOrSanskritStack())
                                sll.add(new TStackList(currentStack.asStack()));
                            else {
                                return null;
                            }
                            currentStack = new TPairList();
                        }
                    }
                }
                if (!currentStack.isEmpty())
                    throw new Error("how can this happen? currentStack is " + currentStack);

                if (!sll.isEmpty()) {
                    if (ddebug) System.out.println("adding sll " + sll + " to parse tree " + pt);
                    pt.add(sll);
                    firstPair = i + 1;
                } // else you probably have {G--YA} or something as
                  // your tsheg bar.
            }
        }
        if (pt.isEmpty()) return null;
        return pt;
    }

    /** Returns true if and only if this list of TPairs can be
     *  interpreted as a legal Tibetan stack or a legal Tibetanized
     *  Sanskrit stack.  This is private because a precondition is
     *  that no vowels or disambiguators appear except possibly in the
     *  final pair. */
    private boolean isLegalTibetanOrSanskritStack() {
        StringBuffer tibetan = new StringBuffer();
        StringBuffer sanskrit = new StringBuffer();
        int sz = size();

        // Special case because otherwise wa-zur alone would be seen
        // as legal.
        if (sz == 1 && "V".equals(get(0).getLeft()))
            return false;

        for (int i = 0; i < sz; i++) {
            TPair p = get(i);
            String ewts_form
                = ACIPRules.getWylieForACIPConsonant(p.getLeft());
            if (null == ewts_form) {
                if (p.isNumeric())
                    ewts_form = p.getLeft();
            }
            if (null == ewts_form) {
                if (ddebug) System.out.println("testing " + toString2() + " for legality said false. numeric?" + p.isNumeric() + "[1]");
                return false;
            }
            tibetan.append(ewts_form);
            sanskrit.append(ewts_form);
            if (i + 1 < sz) {
                tibetan.append('-');
                sanskrit.append('+');
            }
        }
        boolean ans = 
            (TibetanMachineWeb.hasGlyph(tibetan.toString())
                || TibetanMachineWeb.hasGlyph(sanskrit.toString()));
        if (ddebug) System.out.println("testing " + toString2() + " for legality said " + ans + " [2]; san is " + sanskrit + " tib is " + tibetan + ".");
        return ans;
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
    void populateWithTGCPairs(ArrayList pl, ArrayList indexList, int index) {
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
            int where;
            boolean add_U0F7F = false;
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
            // DLC NOW: what about fixed-form RA on top???  test it.
            while ((ww = ll.indexOf("+")) >= 0)
                ll.deleteCharAt(ww);
            boolean isTibetan = TibetanMachineWeb.isWylieTibetanConsonantOrConsonantStack(ll.toString());
            boolean isSanskrit = TibetanMachineWeb.isWylieSanskritConsonantStack(lWylie.toString());
            if (ddebug && !isTibetan && !isSanskrit && !isNumeric) {
                System.out.println("DLC: OTHER for " + lWylie + " with vowel " + ACIPRules.getWylieForACIPVowel(p.getRight()) + " and p.getRight()=" + p.getRight());
            }
            if (isTibetan && isSanskrit) isSanskrit = false; // RVA, e.g.
            if (ddebug && hasNonAVowel && ACIPRules.getWylieForACIPVowel(p.getRight()) == null) {
                System.out.println("DLC: vowel " + ACIPRules.getWylieForACIPVowel(p.getRight()) + " and p.getRight()=" + p.getRight());
            }
            TGCPair tp;
            indexList.add(new Integer(index));
            tp = new TGCPair(lWylie.toString()
                             + (hasNonAVowel
                                ? ACIPRules.getWylieForACIPVowel(p.getRight())
                                : ""),
                             (isNumeric
                              ? TGCPair.OTHER
                              : (hasNonAVowel
                                 ? (isSanskrit
                                    ? TGCPair.SANSKRIT_WITH_VOWEL
                                    : (isTibetan
                                       ? TGCPair.CONSONANTAL_WITH_VOWEL
                                       : TGCPair.OTHER))
                                 : (isSanskrit
                                    ? TGCPair.SANSKRIT_WITHOUT_VOWEL
                                    : (isTibetan
                                       ? TGCPair.CONSONANTAL_WITHOUT_VOWEL
                                       : TGCPair.OTHER)))));
            pl.add(tp);
            if (add_U0F7F) {
                indexList.add(new Integer(index));
                pl.add(new TGCPair("H", TGCPair.OTHER));
            }
        }
    }

    /** Appends legal Unicode corresponding to this stack to sb.  DLC
     *  FIXME: which normalization form, if any? */
    void getUnicode(StringBuffer sb) {
        boolean subscribed = false;
        for (int i = 0; i < size(); i++) {
            get(i).getUnicode(sb, subscribed);
            subscribed = true;
        }
    }

}
// DLC FIXME: handle 'o' and 'x', e.g. KAo and NYAx.
