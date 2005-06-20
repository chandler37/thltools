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

// TODO(DLC)[EWTS->Tibetan]: If EWTS still has 'v', warn about it if it looks like someone thinks that ACIP's usage of it for wa-zur is how EWTS does things.

package org.thdl.tib.text.ttt;

/** A factory for creating {@link TPairList TPairLists} from
 *  Strings of ACIP.
 *  @author David Chandler */
// TODO(DLC)[EWTS->Tibetan]: kill this class; put it all in TTraits.
class TPairListFactory {
    /** This class is not instantiable. */
    private TPairListFactory() { }

    /** See {@link TTraits#breakTshegBarIntoChunks}. */
    static TPairList[] breakACIPIntoChunks(String tt,
                                           boolean specialHandlingForAppendages) {
        TTraits ttraits = ACIPTraits.instance();
        TPairList a = breakHelperACIP(tt, true, false, ttraits);
        TPairList b = null;
        if (specialHandlingForAppendages)
            b = breakHelperACIP(tt, false, false, ttraits);
        if (null != b && a.equals(b))
            return new TPairList[] { a, null };
        else
            return new TPairList[] { a, b };
    }

    /** Helps {@link #breakACIPIntoChunks(String,boolean)}.
     *  @param tickIsVowel true if and only if you want to treat the
     *  ACIP {'} as an U+0F71 vowel instead of the full-sized
     *  consonant in special, "this might be an appendage like 'AM or
     *  'ANG" circumstances
     *  @param weHaveSeenVowelAlready true if and only if, in our
     *  recursion, we've already found one vowel (not a disambiguator,
     *  but a vowel like "A", "E", "Um:", "m", "'U", etc.) */
    private static TPairList breakHelperACIP(String acip, boolean tickIsVowel,
                                             boolean weHaveSeenVowelAlready,
                                             TTraits ttraits) {

        // base case for our recursion:
        if ("".equals(acip))
            return new TPairList(ttraits);

        StringBuffer acipBuf = new StringBuffer(acip);
        int howMuchBuf[] = new int[1];
        TPair head = getFirstConsonantAndVowel(acipBuf, howMuchBuf, ttraits);
        int howMuch = howMuchBuf[0];
        if (!tickIsVowel
            && null != head.getLeft()
            && null != head.getRight()
            && weHaveSeenVowelAlready
            && ttraits.isSuffix(head.getLeft()) // DKY'O should be two horizontal units, not three. -- {D}{KY'O}, not {D}{KY}{'O}.
            && head.getRight().startsWith("'")) {
            head = new TPair(ttraits, head.getLeft(),
                             // Without this disambiguator, we are
                             // less efficient (8 parses, not 4) and
                             // we can't handle PA'AM'ANG etc.
                             "-");
            howMuch = head.getLeft().length();
        }

        TPairList tail;
        if ((tail
             = breakHelperACIP(acipBuf.substring(howMuch),
                               tickIsVowel,
                               weHaveSeenVowelAlready
                               || (head.getRight() != null
                                   && !"+".equals(head.getRight())
                                   && !"-".equals(head.getRight())),
                               ttraits)).hasSimpleError()) {
            for (int i = 1; i < howMuch; i++) {
                // try giving i characters back if that leaves us with
                // a legal head and makes the rest free of simple
                // errors.
                TPairList newTail = null;
                TPair newHead;
                if ((newHead = head.minusNRightmostTransliterationCharacters(i)).isLegal()
                    && !(newTail
                         = breakHelperACIP(acipBuf.substring(howMuch - i),
                                           tickIsVowel,
                                           weHaveSeenVowelAlready
                                           || (newHead.getRight() != null
                                               && !"+".equals(newHead.getRight())
                                               && !"-".equals(newHead.getRight())),
                                           ttraits)).hasSimpleError()) {
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

    /** See {@link TTraits#breakTshegBarIntoChunks}. */
    static TPairList[] breakEWTSIntoChunks(String ewts)
        throws IllegalArgumentException
    {
    	EWTSTraits traits = EWTSTraits.instance();
    	TPairList pl = breakHelperEWTS(ewts, traits);
        TPairList npl = pl;

        // TODO(DLC)[EWTS->Tibetan]: this crap ain't workin' for kaHM.  But kaeM and kaMe shouldn't work, right?  Figure out what EWTS really says...

        // TODO(DLC)[EWTS->Tibetan]: for "a\\0f86" e.g.:
        if (pl.size() > 1) {
            npl = new TPairList(traits, pl.size());

            for (int i = pl.size() - 1; i >= 1; i--) {
                TPair left = pl.get(i - 1);
                TPair right = pl.get(i);
                if (traits.aVowel().equals(left.getRight())
                    && left.getLeft() == null
                    && right.getLeft() == null
                    && traits.isWowelThatRequiresAChen(right.getRight())) {
                    npl.prepend(new TPair(traits, traits.aVowel(), right.getRight()));
                    --i;
                } else if (traits.aVowel().equals(left.getRight())
                           && left.getLeft() != null
                           && right.getLeft() == null
                           && traits.isWowelThatRequiresAChen(right.getRight())
                           && false /* TODO(DLC)[EWTS->Tibetan]: ewts kaM is bothersome now */) {
                    npl.prepend(new TPair(traits, left.getLeft(), right.getRight()));
                    --i;
                } else {
                    npl.prepend(right);
                    if (i == 1)
                        npl.prepend(left);
                }
            }
        }

        TPairList nnpl;
        if (true) {
            // Collapse ( . wowel1) ( . wowel2) into (
            // . wowel1+wowel2).  Then collapse (* . a) ( . x) into (*
            // . x).  Also, if an a-chen (\u0f68) is implied, then
            // insert it.
            TPairList xnnpl = new TPairList(traits, pl.size());
            for (int i = 0; i < npl.size(); ) {
                TPair p = npl.get(i);
                int set_i_to = i + 1;
                if (p.getLeft() == null
                    && p.getRight() != null
                    && !traits.disambiguator().equals(p.getRight())
                    && !"+".equals(p.getRight())) {
                    StringBuffer sb = new StringBuffer(p.getRight());
                    for (int j = i + 1; j < npl.size(); j++) {
                        TPair p2 = npl.get(j);
                        if (p2.getLeft() == null
                            && p2.getRight() != null
                            && !traits.disambiguator().equals(p2.getRight())
                            && !"+".equals(p2.getRight()))
                            {
                                sb.append("+" + p2.getRight());
                                set_i_to = j + 1;
                            } else {
                                break;
                            }
                    }
                    p = new TPair(traits, traits.aVowel(), sb.toString());
                }
                // TODO(DLC)[EWTS->Tibetan]: Do we still have "ai" converting to the wrong thing.  "ae"?
                xnnpl.append(p);
                i = set_i_to;
            }

            nnpl = new TPairList(traits, pl.size());
            // (* . a ) ( . x) ... ( . y) -> (* . a+x+...+y)
            for (int i = 0; i < xnnpl.size(); ) {
                TPair p = xnnpl.get(i);
                int set_i_to = i + 1;
                if (traits.aVowel().equals(p.getRight())) {
                    StringBuffer sb = new StringBuffer(p.getRight());
                    for (int j = i + 1; j < xnnpl.size(); j++) {
                        TPair p2 = xnnpl.get(j);
                        if (p2.getLeft() == null
                            && p2.getRight() != null
                            && !traits.disambiguator().equals(p2.getRight())
                            && !"+".equals(p2.getRight()))
                            {
                                // TODO(DLC)[EWTS->Tibetan] a+o+e is what we'll get.. maybe we want just o+e?
                                sb.append("+" + p2.getRight());
                                set_i_to = j + 1;
                            } else {
                                break;
                            }
                    }
                    p = new TPair(traits, p.getLeft(), sb.toString());
                }

                if (false) { // TODO(DLC)[EWTS->Tibetan]: bra is screwed up, do in it stacklist?
                // EWTS does not think that kra is k+ra.  Replace
                // (consonant . ) with (consonant . DISAMBIGUATOR):
                if (p.getRight() == null && p.getLeft() != null
                    && i + 1 < xnnpl.size())
                    p = new TPair(traits, p.getLeft(), traits.disambiguator());
                }

                nnpl.append(p);
                i = set_i_to;
            }
        } else {
            // TODO(DLC)[EWTS->Tibetan]: this block is not executing.  kill it after testing and thinking
            nnpl = new TPairList(traits, pl.size());
        	
            for (int i = npl.size() - 1; i >= 0; i--) {
                TPair p = npl.get(i);
                if (p.getLeft() == null
                    && p.getRight() != null
                    && !traits.disambiguator().equals(p.getRight())
                    && !"+".equals(p.getRight())) /* TODO(DLC)[EWTS->Tibetan] this should be equivalent to isWowel(p.getRight()) but o+o shows that's not true yet */
                    p = new TPair(traits, traits.aVowel(), p.getRight());
                // TODO(DLC)[EWTS->Tibetan]: do you still have "ai" converting to the wrong thing?  ("ae" also?)
                nnpl.prepend(p);
            }
        }

        // TODO(DLC)[EWTS->Tibetan]: this nnpl crap was before getFirstConsonantAndVowel got fixed.  Try killing it!
        return new TPairList[] {
            nnpl, null
        };
    }

    // TODO(DLC)[EWTS->Tibetan]: doc
    private static TPairList breakHelperEWTS(String ewts, TTraits ttraits) {

        // base case for our recursion:
        if ("".equals(ewts))
            return new TPairList(ttraits);

        StringBuffer ewtsBuf = new StringBuffer(ewts);
        int howMuchBuf[] = new int[1];
        TPair head = getFirstConsonantAndVowel(ewtsBuf, howMuchBuf, ttraits);
        int howMuch = howMuchBuf[0];

        TPairList tail;
        if ((tail = breakHelperEWTS(ewtsBuf.substring(howMuch),
                                    ttraits)).hasSimpleError()) {
            for (int i = 1; i < howMuch; i++) {
                // try giving i characters back if that leaves us with
                // a legal head and makes the rest free of simple
                // errors.
                TPairList newTail = null;
                TPair newHead;
                if ((newHead = head.minusNRightmostTransliterationCharacters(i)).isLegal()
                    && !(newTail
                         = breakHelperEWTS(ewtsBuf.substring(howMuch - i), ttraits)).hasSimpleError()) {
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

    private static String GetInitialVowel(TTraits ttraits, String tx,
                                          String startOfVowel) {
    	if (null == startOfVowel) startOfVowel = "";
    	boolean startsWithPlus = false;
    	if (!"".equals(startOfVowel)
            && (!ttraits.vowelsMayStack()
                || (tx.length() < 1 || !(startsWithPlus = tx.substring(0, 1).equals("+")))))
            return ("".equals(startOfVowel) ? null : startOfVowel);
    	if (startsWithPlus)
            tx = tx.substring(1);
    	for (int i = Math.min(ttraits.maxWowelLength(), tx.length()); i >= 1; i--) {
            String t = tx.substring(0, i);
            if (ttraits.isWowel(t)
                || (ttraits.isACIP()
                    // Or these, which we massage into "Am", "Am:", and
                    // "A:" because I didn't think {Pm} should be treated
                    // like {PAm} originally:
                    // TODO(DLC)[EWTS->Tibetan]: NOW NIGHTMARE
                    && ("m".equals(t) || "m:".equals(t) || ":".equals(t)))) {
				// If this is followed by +wowel[+wowel[+wowel... in EWTS then that's part of the vowel also:
                return GetInitialVowel(ttraits,
                                       tx.substring(i),
                                       startOfVowel + (startsWithPlus ? "+" : "") + t);
            }
    	}
    	return null;
    }

    
    /** Returns the largest TPair we can make from the transliteration
     *  starting from the left. This will return a size zero pair if
     *  and only if tx is the empty string; otherwise, it may return a
     *  pair with either the left or right component empty.  [FOR
     *  ACIP:] This mutates tx when we run into {NA+YA}; it mutates tx
     *  into {N+YA}.  For {NE+YA}, it does not mutate tx or behave
     *  intelligently.  A later phase will need to turn that into
     *  {N+YE} or an error or whatever you like.  howMuch[0] will be
     *  set to the number of characters of tx that this call has
     *  consumed. */
    private static TPair getFirstConsonantAndVowel(StringBuffer tx, // TODO(DLC)[EWTS->Tibetan]: function name needs ACIP in it?
                                                   int howMuch[],
                                                   TTraits ttraits) {
        // To handle EWTS "phywa\\u0f84\u0f86" [yes that's two slashes
        // and then one slash], for example, we need to make the wowel
        // (the getRight() field of the returned TPair) contain
        // everything that it should.
        //
        // It can't hurt in ACIP, though I don't recall if ACIP's lexer
        // allows Unicode characters.
        TPair og = helpGetFirstConsonantAndVowel(tx, howMuch, ttraits);
        int len = tx.length();
        StringBuffer x = null;
        while (howMuch[0] < len) {
            if (isUnicodeWowelChar(tx.charAt(howMuch[0]))) {
                if (null == x) x = new StringBuffer(); // rarely happens
                if (x.length() > 0) x.append('+');
                x.append(tx.charAt(howMuch[0]++));
            } else {
                break;
            }
        }
        // In EWTS, deal with M, ~M`, etc.  They're much like
        // UnicodeWowelCharacters.
        if (ttraits instanceof EWTSTraits) {
            EWTSTraits tt = (EWTSTraits)ttraits;
            while (howMuch[0] < len) {
                int howMuchExtra[] = new int[] { 0 };
                TPair p
                    = helpGetFirstConsonantAndVowel(new StringBuffer(tx.substring(howMuch[0])),
                                                    howMuchExtra,
                                                    ttraits);
                if (p.getLeft() == null
                    && p.getRight() != null
                    && tt.isWowelThatRequiresAChen(p.getRight())) {
                    if (null == x) x = new StringBuffer(); // rarely happens
                    String extra;
                    if (x.length() > 0) x.append('+');
                    x.append(extra = tx.substring(howMuch[0], howMuch[0] + howMuchExtra[0]));
                    //  System.out.println("extra is " + extra);  TODO(DLC)[EWTS->Tibetan]
                    howMuch[0] += howMuchExtra[0];
                } else {
                    break;
                }
            }
        }
        if (null != x)
            return new TPair(ttraits, og.getLeft(),
                             (null == og.getRight() || ttraits.aVowel().equals(og.getRight()))
                             ? x.toString()
                             : (og.getRight() + "+" + x.toString()));
        else
            return og;
    }
    private static TPair helpGetFirstConsonantAndVowel(StringBuffer tx, // TODO(DLC)[EWTS->Tibetan]: function name needs ACIP in it?
                                                       int howMuch[],
                                                       TTraits ttraits) {
        // Note that it is *not* the case that if tx.substring(0, N)
        // is legal (according to TPair.isLegal()), then
        // tx.substring(0, N-1) is legal for all N.  For example,
        // think of ACIP's {shA} and {KshA}.  However, 's' is the only
        // tricky fellow in ACIP, so in ACIP it is true that
        // tx.substring(0, N-1) is either legal or ends with 's' if
        // tx.substring(0, N) is legal.
        //
        // We don't, however, use this approach.  We just try to find
        // a consonant of length 3, and then, failing that, of length
        // 2, etc.  Likewise with vowels.  This avoids the issue.

        int i, xl = tx.length();
        // TODO(DLC)[EWTS->Tibetan]: nasty special case!
        if (false && !ttraits.isACIP() /* TODO(DLC)[EWTS->Tibetan]: isEWTS! */
        	&& xl >= 2 && tx.charAt(0) == 'a' && (tx.charAt(1) == 'i' || tx.charAt(1) == 'u')) {
        	howMuch[0] = 2;
        	return new TPair(ttraits, null, tx.substring(0, 2));
        	// TODO(DLC)[EWTS->Tibetan]: test that "au" alone is \u0f68\u0f7d, "ai" alone is \u0f68\u0f7b in EWTS.
        }
        if (0 == xl) {
            howMuch[0] = 0;
            return new TPair(ttraits, null, null);
        }
        if (tx.charAt(0) == ttraits.disambiguatorChar()) {
            howMuch[0] = 1;
            return new TPair(ttraits, null, ttraits.disambiguator());
        }
        char ch = tx.charAt(0);

        // Numbers never appear in stacks, so if you see 1234, that's
        // like seeing 1-2-3-4.  Though in EWTS you can have '0\u0f19'
        if (ch >= '0' && ch <= '9') {
        	// TODO(DLC)[EWTS->Tibetan]: test case: 0e should have a-chen  and 0\u0f74 should go through without errors.
        	if (xl > 1 && ttraits.isUnicodeWowel(tx.charAt(1))) {
        		howMuch[0] = 2;
                return new TPair(ttraits, tx.substring(0, 1), tx.substring(1, 2));
        	}
        		
            howMuch[0] = 1; // not 2...
            return new TPair(ttraits, tx.substring(0, 1), (xl == 1) ? null : ttraits.disambiguator());
        }

        String l = null, r = null;
        for (i = Math.min(ttraits.maxConsonantLength(), xl); i >= 1; i--) {
            String t = null;
            if (ttraits.isConsonant(t = tx.substring(0, i))
            	|| (ttraits.vowelAloneImpliesAChen() // handle EWTS {a+yo}
            			&& ttraits.aVowel().equals(tx.substring(0, i))
						&& i < xl && tx.substring(i, i + i).equals("+"))) {
                l = t;
                break;
            }
        }
        int ll = (null == l) ? 0 : l.length();
        if (null != l && xl > ll && tx.charAt(ll) == ttraits.disambiguatorChar()) {
            howMuch[0] = l.length() + 1;
            return new TPair(ttraits, l, ttraits.disambiguator());
        }
        if (null != l && xl > ll && tx.charAt(ll) == '+') {
            howMuch[0] = l.length() + 1;
            return new TPair(ttraits, l, "+");
        }
        int mod = 0;

        r = GetInitialVowel(ttraits, tx.substring(ll), null);
        if (ttraits.isACIP()) {
            // Treat {BATA+SA'I} like {BAT+SA'I}: // TODO(DLC)[EWTS->Tibetan]: in EWTS???
            int z;
            if (null != l
                && ttraits.aVowel().equals(r)
                && ((z = ll + ttraits.aVowel().length()) < xl)
                && tx.charAt(z) == '+') {
                tx.deleteCharAt(z-1);
                howMuch[0] = l.length() + 1;
                return new TPair(ttraits, l, "+");
            }

            // Allow Pm to mean PAm, P: to mean PA:, Pm: to mean PAm:. /* TODO(DLC)[EWTS->Tibetan]:  in EWTS? */
            if ("m".equals(r)) { r = "Am"; mod = -1; }
            if (":".equals(r)) { r = "A:"; mod = -1; }
            if ("m:".equals(r)) { r = "Am:"; mod = -1; }
            if (":m".equals(r)) { r = "A:m"; mod = -1; } // not seen, though...
        }

        // what if we see a character that's not part of any wowel or
        // consonant?  We return it.
        if (null == l && null == r) {
            howMuch[0] = 1; // not 2...
            // add a disambiguator to avoid exponential running time:
            return new TPair(ttraits, tx.substring(0, 1),
                             (xl == 1) ? null : ttraits.disambiguator());
        }

        howMuch[0] = (((l == null) ? 0 : l.length())
                      + ((r == null) ? 0 : r.length())
                      + mod);
        return new TPair(ttraits, l, r);
    } // TODO(DLC)[EWTS->Tibetan]:

    private static boolean isUnicodeWowelChar(char ch) {
        return ((ch >= '\u0f71' && ch <= '\u0f84')
                || "\u0f35\u0f37\u0f18\u0f19\u0f3e\u0f3f\u0f86\u0f87\u0fc6".indexOf(ch) >= 0);
        // TODO(dchandler): should we really allow "phywa\\u0f18", or
        // does \u0f18 only combine with digits?
    }
}


// FIXME: test for nested comments

// FIXME: see Translit directory on ACIP v4 CD-ROM
