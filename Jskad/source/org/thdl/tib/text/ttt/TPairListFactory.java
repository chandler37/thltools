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

/** A factory for creating {@link TPairList TPairLists} from
 *  Strings of ACIP.
 *  @author David Chandler */
class TPairListFactory {
    /** This class is not instantiable. */
    private TPairListFactory() { }

    /** Returns a new TPairList instance.  Breaks an ACIP tsheg bar
     *  (roughly a &quot;syllable&quot;) into chunks; this computes l'
     *  (for you design doc enthusiasts).
     *
     *  <p>Here's a rough sketch of the algorithm: run along getting
     *  the current TPair as big as you can.  If you get it very
     *  big, but there's something illegal afterward that wouldn't
     *  otherwise be illegal, undo as little as possible to correct.
     *  For example, G'A'I becomes [(G . 'A), (' . I)], and TAA
     *  becomes [(T . A)] in a first pass but then we see that the
     *  rest would be suboptimal, so we backtrack to [(T . )] and then
     *  finally become [(T . ), (A . A)].  We look for (A . ) and (
     *  . <vowel>) in the rest in order to say "the rest would be
     *  suboptimal", i.e. we use TPairList.hasSimpleError()
     *  @param acip a string of ACIP with no punctuation in it */
    static TPairList breakACIPIntoChunks(String acip) {

        // base case for our recursion:
        if ("".equals(acip))
            return new TPairList();

        StringBuffer acipBuf = new StringBuffer(acip);
        int howMuchBuf[] = new int[1];
        TPair head = getFirstConsonantAndVowel(acipBuf, howMuchBuf);
        int howMuch = howMuchBuf[0];
        TPairList tail;
        if ((tail
             = breakACIPIntoChunks(acipBuf.substring(howMuch))).hasSimpleError()) {
            for (int i = 1; i < howMuch; i++) {
                // try giving i characters back if that leaves us with
                // a legal head and makes the rest free of simple
                // errors.
                TPairList newTail = null;
                TPair newHead;
                if ((newHead = head.minusNRightmostACIPCharacters(i)).isLegal()
                    && !(newTail
                         = breakACIPIntoChunks(acipBuf.substring(howMuch - i))).hasSimpleError()) {
                    newTail.prepend(newHead);
                    return newTail;
                }
            }
            // It didn't work.  Return the first thing we'd thought
            // of: head appended with tail.  (I.e., fall through.)
        }
        tail.prepend(head);
        return tail;
    }

    /** Returns the largest TPair we can make from the acip
     *  starting from the left. This will return a size zero pair if
     *  and only if acip is the empty string; otherwise, it may return
     *  a pair with either the left or right component empty.  This
     *  mutates acip when we run into {NA+YA}; it mutates acip into
     *  {N+YA}.  For {NE+YA}, it doesn not mutate acip or behave
     *  intelligently.  A later phase will need to turn that into
     *  {N+YE} (DLC).  howMuch[0] will be set to the number of
     *  characters of acip that this call has consumed. */
    private static TPair getFirstConsonantAndVowel(StringBuffer acip,
                                                      int howMuch[]) {
        // Note that it is *not* the case that if acip.substring(0, N)
        // is legal (according to TPair.isLegal()), then
        // acip.substring(0, N-1) is legal for all N.  For example,
        // think of {shA} and {KshA}.  However, 's' is the only tricky
        // fellow, so it is true that acip.substring(0, N-1) is either
        // legal or ends with 's' if acip.substring(0, N) is legal.
        //
        // We don't, however, use this approach.  We just try to find
        // a consonant of length 3, and then, failing that, of length
        // 2, etc.  Likewise with vowels.  This avoids the issue.

        int i, xl = acip.length();
        if (0 == xl) {
            howMuch[0] = 0;
            return new TPair(null, null);
        }
        if (acip.charAt(0) == '-') {
            howMuch[0] = 1;
            return new TPair(null, "-");
        }
        char ch = acip.charAt(0);

        // Numbers never appear in stacks, so if you see 1234, that's
        // like seeing 1-2-3-4.
        if (ch >= '0' && ch <= '9') {
            howMuch[0] = 1; // not 2...
            return new TPair(acip.substring(0, 1), (xl == 1) ? null : "-");
        }

        String l = null, r = null;
        for (i = Math.min(ACIPRules.MAX_CONSONANT_LENGTH, xl); i >= 1; i--) {
            String t = null;
            if (ACIPRules.isConsonant(t = acip.substring(0, i))) {
                l = t;
                break;
            }
        }
        int ll = (null == l) ? 0 : l.length();
        if (null != l && xl > ll && acip.charAt(ll) == '-') {
            howMuch[0] = l.length() + 1;
            return new TPair(l, "-");
        }
        if (null != l && xl > ll && acip.charAt(ll) == '+') {
            howMuch[0] = l.length() + 1;
            return new TPair(l, "+");
        }
        for (i = Math.min(ACIPRules.MAX_VOWEL_LENGTH, xl - ll); i >= 1; i--) {
            String t = null;
            if (ACIPRules.isVowel(t = acip.substring(ll, ll + i))) {
                r = t;
                break;
            }
        }

        // Treat {BATA+SA'I} like {BAT+SA'I}:
        int z;
        if (null != l && "A".equals(r) && ((z = ll + "A".length()) < xl)
            && acip.charAt(z) == '+') {
            acip.deleteCharAt(z-1);
            howMuch[0] = l.length() + 1;
            return new TPair(l, "+");
        }

        // what if we see a character that's not part of any vowel or
        // consonant?  We return it.
        if (null == l && null == r) {
            howMuch[0] = 1; // not 2...
            // add a '-' to avoid exponentials:
            return new TPair(acip.substring(0, 1), (xl == 1) ? null : "-");
        }

        howMuch[0] = (((l == null) ? 0 : l.length())
                      + ((r == null) ? 0 : r.length()));
        return new TPair(l, r);
    }
}


// DLC strip out [#...] comments; test for nested comments

// DLC see Translit directory on ACIP v4 CD-ROM