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

    /** Returns one or two new TPairList instances.  Breaks an ACIP
     *  tsheg bar (roughly a &quot;syllable&quot;) into chunks; this
     *  computes l' (for you design doc enthusiasts).
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
     *  suboptimal", i.e. we use TPairList.hasSimpleError().</p>
     *
     *  <p>There is one case where we break things up into two pair
     *  lists if and only if specialHandlingForAppendages is true -- I
     *  thought the converter had a bug because I saw SNYAM'AM in
     *  KD0003I2.ACT.  I asked Robert Chilton, though, and he said
     *  "SNYAM'AM " was likely a typo for "SNYAM 'AM", so leave
     *  specialHandlingForAppendages false.</p>
     *
     *  <p>I found out about (OK, as it turns out, imagined) this case
     *  too late to do anything clean about it.  SNYAM'AM, e.g.,
     *  breaks up into [(S . ), (NY . A), (M . 'A), (M . )], which is
     *  incorrect -- [(S . ), (NY . A), (M . ), (' . A), (M . )] is
     *  correct.  But we don't know which is correct without parsing,
     *  so both are returned.  The clean treatment would be to lex
     *  into a form that didn't insist 'A was either a vowel or a
     *  consonant.  Then the parser would figure it out.  But don't
     *  bother, because specialHandlingForAppendages should be false
     *  always.</p>
     *
     *  @param acip a string of ACIP with no punctuation in it
     *  @param specialHandlingForAppendages true if and only if you
     *  want SNYAM'AM to ultimately parse as {S+NYA}{M}{'A}{M} instead
     *  of {S+NYA}{M'A}{M}
     *  @return an array of one or two pair lists, if the former, then
     *  the second element will be null, if the latter, the second
     *  element will have (* . ), (' . *) instead of (* . '*) which
     *  the former has @throws IllegalArgumentException if acip is too
     *  large for us to break into chunks (we're recursive, not
     *  iterative, so the boundary can be increased a lot if you care,
     *  but you don't) */
    static TPairList[] breakACIPIntoChunks(String acip,
                                           boolean specialHandlingForAppendages)
        throws IllegalArgumentException
    {
        try {
            TPairList a = breakHelper(acip, true, false);
            TPairList b = null;
            if (specialHandlingForAppendages)
                b = breakHelper(acip, false, false);
            if (null != b && a.equals(b))
                return new TPairList[] { a, null };
            else
                return new TPairList[] { a, b };
        } catch (StackOverflowError e) {
            throw new IllegalArgumentException("Input too large[1]: " + acip);
        } catch (OutOfMemoryError e) {
            throw new IllegalArgumentException("Input too large[2]: " + acip);
        }
    }
    /** Helps {@link breakACIPIntoChunks(String)}.
     *  @param tickIsVowel true if and only if you want to treat the
     *  ACIP {'} as an U+0F71 vowel instead of the full-sized
     *  consonant in special, "this might be an appendage like 'AM or
     *  'ANG" circumstances
     *  @param weHaveSeenVowelAlready true if and only if, in our
     *  recursion, we've already found one vowel (not a disambiguator,
     *  but a vowel like "A", "E", "Um:", "'U", etc.) */
    private static TPairList breakHelper(String acip, boolean tickIsVowel, boolean weHaveSeenVowelAlready) {

        // base case for our recursion:
        if ("".equals(acip))
            return new TPairList();

        StringBuffer acipBuf = new StringBuffer(acip);
        int howMuchBuf[] = new int[1];
        TPair head = getFirstConsonantAndVowel(acipBuf, howMuchBuf);
        int howMuch = howMuchBuf[0];
        if (!tickIsVowel
            && null != head.getLeft()
            && null != head.getRight()
            && weHaveSeenVowelAlready
            && ACIPRules.isACIPSuffix(head.getLeft()) // DKY'O should be two horizontal units, not three. -- {D}{KY'O}, not {D}{KY}{'O}.
            && head.getRight().startsWith("'")) {
            head = new TPair(head.getLeft(),
                             // Without this disambiguator, we are
                             // less efficient (8 parses, not 4) and
                             // we can't handle PA'AM'ANG etc.
                             "-");
            howMuch = head.getLeft().length();
        }

        TPairList tail;
        if ((tail
             = breakHelper(acipBuf.substring(howMuch),
                           tickIsVowel,
                           weHaveSeenVowelAlready
                           || (head.getRight() != null
                               && !"+".equals(head.getRight())
                               && !"-".equals(head.getRight())))).hasSimpleError()) {
            for (int i = 1; i < howMuch; i++) {
                // try giving i characters back if that leaves us with
                // a legal head and makes the rest free of simple
                // errors.
                TPairList newTail = null;
                TPair newHead;
                if ((newHead = head.minusNRightmostACIPCharacters(i)).isLegal()
                    && !(newTail
                         = breakHelper(acipBuf.substring(howMuch - i),
                                       tickIsVowel,
                                       weHaveSeenVowelAlready
                                       || (newHead.getRight() != null
                                           && !"+".equals(newHead.getRight())
                                           && !"-".equals(newHead.getRight())))).hasSimpleError()) {
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

    /** Returns the largest TPair we can make from the acip starting
     *  from the left. This will return a size zero pair if and only
     *  if acip is the empty string; otherwise, it may return a pair
     *  with either the left or right component empty.  This mutates
     *  acip when we run into {NA+YA}; it mutates acip into {N+YA}.
     *  For {NE+YA}, it does not mutate acip or behave intelligently.
     *  A later phase will need to turn that into {N+YE} or an error
     *  or whatever you like.  howMuch[0] will be set to the number of
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


// DLC test for nested comments

// DLC see Translit directory on ACIP v4 CD-ROM
