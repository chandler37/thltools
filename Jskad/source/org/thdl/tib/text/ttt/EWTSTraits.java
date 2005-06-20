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
Library (THDL). Portions created by the THDL are Copyright 2004 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

    // TODO(DLC)[EWTS->Tibetan]: TibetanMachineWeb has duplication of much of this!

package org.thdl.tib.text.ttt;

import java.util.ArrayList;

import org.thdl.tib.text.DuffCode;
import org.thdl.tib.text.THDLWylieConstants;
import org.thdl.tib.text.TibTextUtils;
import org.thdl.tib.text.TibetanMachineWeb;
import org.thdl.util.ThdlDebug;

/** A singleton class that should contain (but due to laziness and
 *  ignorance probably does not contain) all the traits that make EWTS
 *  transliteration different from other (say, ACIP) transliteration
 *  schemes. */
public final class EWTSTraits implements TTraits {
    /** sole instance of this class */
    private static EWTSTraits singleton = null;

    /** Just a constructor. */
    private EWTSTraits() { }

    /** */
    public static synchronized EWTSTraits instance() {
        if (null == singleton) {
            singleton = new EWTSTraits();
        }
        return singleton;
    }

    /** Returns ".". */
    public String disambiguator() { return "."; }

    /** Returns '.'. */
    public char disambiguatorChar() { return '.'; }

    // TODO(DLC)[EWTS->Tibetan]: isClearlyIllegal and hasSimpleError are different why?
    public boolean hasSimpleError(TPair p) {
        if (pairHasBadWowel(p)) return true;
        return (("a".equals(p.getLeft()) && null == p.getRight())
                || ("a".equals(p.getLeft())
                    && null != p.getRight()
                    && TibetanMachineWeb.isWylieVowel(p.getRight()))); // TODO(DLC)[EWTS->Tibetan]: or Unicode wowels?  test "a\u0f74" and "a\u0f7e"
        // TODO(DLC)[EWTS->Tibetan]: (a.e) is bad, one of (.a) or (a.) is bad
    }

    /** {tsh}, the longest consonant, has 3 characters, so this is
     *  three. */
    public int maxConsonantLength() { return 3; }

    /** {-i~M`}, in a tie for the longest wowel, has 5 characters, so
     *  this is five.  (No, 'l-i' and 'r-i' are not wowels (but '-i'
     *  is).  (TODO(DLC)[EWTS->Tibetan]: this is crap!  you can put arbitrary wowels
     *  together using plus signs or Unicode escapes) */
    public int maxWowelLength() { return 3; /* a~M`  (TODO(DLC)[EWTS->Tibetan]:!  why the 'a'?) */}
    
    public boolean isUnicodeConsonant(char ch) {
    	return ((ch != '\u0f48' && ch >= '\u0f40' && ch <= '\u0f6a')
				|| (ch != '\u0f98' && ch >= '\u0f90' && ch <= '\u0fbc'));
    }
    
    public boolean isUnicodeWowel(char ch) {
    	// TODO(DLC)[EWTS->Tibetan]: what about combiners that combine only with digits?  TEST
        return ((ch >= '\u0f71' && ch <= '\u0f84')
                || isUnicodeWowelThatRequiresAChen(ch));
    }

// TODO(DLC)[EWTS->Tibetan]: u,e,i,o?  If not, document the special treatment in this function's comment
    public boolean isConsonant(String s) {
    	if (s.length() == 1 && isUnicodeConsonant(s.charAt(0))) return true;
    	if (aVowel().equals(s)) return false; // In EWTS, "a" is both a consonant and a vowel, but we treat it as just a vowel and insert the implied a-chen if you have a TPair ( . a) (TODO(DLC)[EWTS->Tibetan]: right?)

 // TODO(DLC)[EWTS->Tibetan]: numbers are consonants?

        // TODO(DLC)[EWTS->Tibetan]: just g for now
        return TibetanMachineWeb.isWylieChar(s);
    }

    public boolean isWowel(String s) {
        return (getUnicodeForWowel(s) != null);
        /* TODO(DLC)[EWTS->Tibetan]: test ko+m+e etc.
        // TODO(DLC)[EWTS->Tibetan]: all non-consonant combiners? 0f71 0f87 etc.?
    	if (s.length() == 1 && isUnicodeWowel(s.charAt(0))) return true;
        return ("a".equals(s)
                || "e".equals(s)
                || "i".equals(s)
                || "o".equals(s)
                || "u".equals(s)
                || "U".equals(s)
                || "I".equals(s)
                || "A".equals(s)
                || "-i".equals(s)
                || "-I".equals(s)
                || "au".equals(s)
                || "ai".equals(s)
                || isWowelThatRequiresAChen(s));
                 // TODO(DLC)[EWTS->Tibetan]:???
        */
    }

    public String aVowel() { return "a"; }

    public boolean isPostsuffix(String s) {
        return ("s".equals(s) || "d".equals(s));
    }

    public boolean isPrefix(String l) {
        return ("'".equals(l)
                || "m".equals(l)
                || "b".equals(l)
                || "d".equals(l)
                || "g".equals(l));
    }

    public boolean isSuffix(String l) {
        return ("s".equals(l)
                || "g".equals(l)
                || "d".equals(l)
                || "m".equals(l)
                || "'".equals(l)
                || "b".equals(l)
                || "ng".equals(l)
                || "n".equals(l)
                || "l".equals(l)
                || "r".equals(l));
    }

    /** Returns l, since this is EWTS's traits class. */
    public String getEwtsForConsonant(String l) { return l; }

    /** Returns l, since this is EWTS's traits class. */
    public String getEwtsForOther(String l) { return l; }

    /** Returns l, since this is EWTS's traits class. */
    public String getEwtsForWowel(String l) { return l; }

    public TTshegBarScanner scanner() { return EWTSTshegBarScanner.instance(); }

    public void getDuffForWowel(ArrayList duff, DuffCode preceding, String wowel) {

        // TODO(DLC)[EWTS->Tibetan]: I have no confidence in this! test, test, test.

        // Order matters here.
        boolean context_added[] = new boolean[] { false };
        if (wowel.equals(THDLWylieConstants.WYLIE_aVOWEL)) {
            TibTextUtils.getVowel(duff, preceding, THDLWylieConstants.WYLIE_aVOWEL, context_added);
        } else {
            // TODO(DLC)[EWTS->Tibetan]: test vowel stacking
            if (wowel.indexOf(THDLWylieConstants.U_VOWEL) >= 0) {
                TibTextUtils.getVowel(duff, preceding, THDLWylieConstants.U_VOWEL, context_added);
            }
            if (wowel.indexOf(THDLWylieConstants.reverse_I_VOWEL) >= 0) {
                TibTextUtils.getVowel(duff, preceding, THDLWylieConstants.reverse_I_VOWEL, context_added);
            } else if (wowel.indexOf(THDLWylieConstants.I_VOWEL) >= 0) {
                TibTextUtils.getVowel(duff, preceding, THDLWylieConstants.I_VOWEL, context_added);
            }
            if (wowel.indexOf(THDLWylieConstants.A_VOWEL) >= 0) {
                TibTextUtils.getVowel(duff, preceding, THDLWylieConstants.A_VOWEL, context_added);
            }
            if (wowel.indexOf(THDLWylieConstants.ai_VOWEL) >= 0) {
                TibTextUtils.getVowel(duff, preceding, THDLWylieConstants.ai_VOWEL, context_added);
            }
            if (wowel.indexOf(THDLWylieConstants.au_VOWEL) >= 0) {
                TibTextUtils.getVowel(duff, preceding, THDLWylieConstants.au_VOWEL, context_added);
            }
            if (wowel.indexOf(THDLWylieConstants.reverse_i_VOWEL) >= 0) {
                TibTextUtils.getVowel(duff, preceding, THDLWylieConstants.reverse_i_VOWEL, context_added);
            } else if (wowel.indexOf(THDLWylieConstants.i_VOWEL) >= 0) {
                TibTextUtils.getVowel(duff, preceding, THDLWylieConstants.i_VOWEL, context_added);
            }
            if (wowel.indexOf(THDLWylieConstants.e_VOWEL) >= 0) {
                TibTextUtils.getVowel(duff, preceding, THDLWylieConstants.e_VOWEL, context_added);
            }
            if (wowel.indexOf(THDLWylieConstants.o_VOWEL) >= 0) {
                TibTextUtils.getVowel(duff, preceding, THDLWylieConstants.o_VOWEL, context_added);
            }
            if (wowel.indexOf(THDLWylieConstants.u_VOWEL) >= 0) {
                TibTextUtils.getVowel(duff, preceding, THDLWylieConstants.u_VOWEL, context_added);
            }
            if (wowel.indexOf("~X") >= 0) {  // TODO(DLC)[EWTS->Tibetan]: introduce THDLWylieConstants.blah
                duff.add(TibetanMachineWeb.getGlyph("~X"));
            } else if (wowel.indexOf("X") >= 0) {  // TODO(DLC)[EWTS->Tibetan]: introduce THDLWylieConstants.blah
                duff.add(TibetanMachineWeb.getGlyph("X"));
            }
        }
        // FIXME: Use TMW9.61, the "o'i" special combination, when appropriate.

        if (wowel.indexOf('M') >= 0) {
            DuffCode last = null;
            if (duff.size() > 0) {
                last = (DuffCode)duff.get(duff.size() - 1);
                duff.remove(duff.size() - 1); // getBindu will add it back...
                // TODO(DLC)[EWTS->Tibetan]: is this okay????  when is a bindu okay to be alone???
            }
            TibTextUtils.getBindu(duff, last);
        }
        if (wowel.indexOf('H') >= 0)
            duff.add(TibetanMachineWeb.getGlyph("H"));

        
        // TODO(DLC)[EWTS->Tibetan]: verify that no part of wowel is discarded!  acip does that.  'jam~X I think we screw up, e.g.

        // TODO(DLC)[EWTS->Tibetan]:: are bindus are screwed up in the unicode output?  i see (with tmuni font) lone bindus without glyphs to stack on
    }

    public String getUnicodeForWowel(String wowel) {
        if ("a".equals(wowel))
            return "";
        return helpGetUnicodeForWowel(wowel);
    }

    private String helpGetUnicodeForWowel(String wowel) {
        if ("a".equals(wowel))
            return null; // ko+a+e is invalid, e.g.
        if (wowel.length() == 1 && isUnicodeWowel(wowel.charAt(0)))
            return wowel;
        // handle o+u, etc.
        int i;
        if ((i = wowel.indexOf("+")) >= 0) {
            // recurse.

            // Chris Fynn says \u0f7c\u0f7c is different from \u0f7d.
            // So o+o is not the same as au.  e+e is not the same as
            // ai.
            String left = helpGetUnicodeForWowel(wowel.substring(0, i));
            String right = helpGetUnicodeForWowel(wowel.substring(i + 1));
            if (null != left && null != right)
                return left + right;
            else
                return null;
        } else {
            // Handle vowels.  (TODO(dchandler): tibwn.ini has this
            // info, use that instead of duplicating it in this code.)
            if ("i".equals(wowel)) return "\u0f72";
            if ("u".equals(wowel)) return "\u0f74";
            if ("A".equals(wowel)) return "\u0f71";
            if ("U".equals(wowel)) return "\u0f71\u0f74"; // \u0f75 is discouraged
            if ("e".equals(wowel)) return "\u0f7a";
            if ("o".equals(wowel)) return "\u0f7c";
            if ("-i".equals(wowel)) return "\u0f80";
            if ("ai".equals(wowel)) return "\u0f7b";
            if ("au".equals(wowel)) return "\u0f7d";
            if ("-I".equals(wowel)) return "\u0f81";
            if ("I".equals(wowel)) return "\u0f71\u0f72"; // \u0f73 is discouraged

            // TODO(DLC)[EWTS->Tibetan]: fix me!
                // DLC say ah        if ("aM".equals(wowel)) return "\u0f7e";
            if ("M".equals(wowel)) return "\u0f7e";
            // DLC say ah        if ("aH".equals(wowel)) return "\u0f7f";
            if ("H".equals(wowel)) return "\u0f7f";
            // DLC say ah        if ("a?".equals(wowel)) return "\u0f84";
            if ("?".equals(wowel)) return "\u0f84";
            // DLC say ah        if ("a~M".equals(wowel)) return "\u0f83";
            if ("~M".equals(wowel)) return "\u0f83";
            // DLC say ah        if ("a~M`".equals(wowel)) return "\u0f82";
            if ("~M`".equals(wowel)) return "\u0f82";
            // DLC say ah        if ("aX".equals(wowel)) return "\u0f37";
            if ("X".equals(wowel)) return "\u0f37";
            // DLC say ah        if ("a~X".equals(wowel)) return "\u0f35";
            if ("~X".equals(wowel)) return "\u0f35";

            return null;
        }
    }

    public String getUnicodeFor(String l, boolean subscribed) {

        // First, handle "\u0f71\u0f84\u0f86", "", "\u0f74", etc.
        {
            boolean already_done = true;
            for (int i = 0; i < l.length(); i++) {
                char ch = l.charAt(i);
                if ((ch < '\u0f00' || ch > '\u0fff')
                    && '\n' != ch
                    && '\r' != ch) {
                    // TODO(DLC)[EWTS->Tibetan]: Is this the place
                    // where we want to interpret how newlines work???
                    already_done = false;
                    break;
                }
            }
            if (already_done)
                return l; // TODO(dchandler): \u0fff etc. are not valid code points, though.  Do we handle that well?
        }

 // TODO(DLC)[EWTS->Tibetan]:: vowels !subscribed could mean (a . i)???? I doubt it but test "i"->"\u0f68\u0f72" etc.

        if (subscribed) {
            if ("R".equals(l)) return "\u0fbc";
            if ("Y".equals(l)) return "\u0fbb";
            if ("W".equals(l)) return "\u0fba";

            // g+h etc. should not be inputs to this function, but for
            // completeness they're here.
            if ("k".equals(l)) return "\u0F90";
            if ("kh".equals(l)) return "\u0F91";
            if ("g".equals(l)) return "\u0F92";
            if ("g+h".equals(l)) return "\u0F93";
            if ("ng".equals(l)) return "\u0F94";
            if ("c".equals(l)) return "\u0F95";
            if ("ch".equals(l)) return "\u0F96";
            if ("j".equals(l)) return "\u0F97";
            if ("ny".equals(l)) return "\u0F99";
            if ("T".equals(l)) return "\u0F9A";
            if ("Th".equals(l)) return "\u0F9B";
            if ("D".equals(l)) return "\u0F9C";
            if ("D+h".equals(l)) return "\u0F9D";
            if ("N".equals(l)) return "\u0F9E";
            if ("t".equals(l)) return "\u0F9F";
            if ("th".equals(l)) return "\u0FA0";
            if ("d".equals(l)) return "\u0FA1";
            if ("d+h".equals(l)) return "\u0FA2";
            if ("n".equals(l)) return "\u0FA3";
            if ("p".equals(l)) return "\u0FA4";
            if ("ph".equals(l)) return "\u0FA5";
            if ("b".equals(l)) return "\u0FA6";
            if ("b+h".equals(l)) return "\u0FA7";
            if ("m".equals(l)) return "\u0FA8";
            if ("ts".equals(l)) return "\u0FA9";
            if ("tsh".equals(l)) return "\u0FAA";
            if ("dz".equals(l)) return "\u0FAB";
            if ("dz+h".equals(l)) return "\u0FAC";
            if ("w".equals(l)) return "\u0FAD"; // TODO(DLC)[EWTS->Tibetan]:: ???
            if ("zh".equals(l)) return "\u0FAE";
            if ("z".equals(l)) return "\u0FAF";
            if ("'".equals(l)) return "\u0FB0";
            if ("y".equals(l)) return "\u0FB1";
            if ("r".equals(l)) return "\u0FB2";
            if ("l".equals(l)) return "\u0FB3";
            if ("sh".equals(l)) return "\u0FB4";
            if ("Sh".equals(l)) return "\u0FB5";
            if ("s".equals(l)) return "\u0FB6";
            if ("h".equals(l)) return "\u0FB7";
            if ("a".equals(l)) return "\u0FB8";
            if ("k+Sh".equals(l)) return "\u0FB9";
            if (false) throw new Error("TODO(DLC)[EWTS->Tibetan]:: subscribed for " + l);
            return null;
        } else {
            if ("R".equals(l)) return "\u0f6a";
            if ("Y".equals(l)) return "\u0f61";
            if ("W".equals(l)) return "\u0f5d";
            
            if (!TibetanMachineWeb.isKnownHashKey(l)) {
                ThdlDebug.noteIffyCode();
                return null;
            }
            String s = TibetanMachineWeb.getUnicodeForWylieForGlyph(l);
            if (null == s)
                ThdlDebug.noteIffyCode();
            return s;
        }
    }

    public String shortTranslitName() { return "EWTS"; }

    private boolean pairHasBadWowel(TPair p) {
        return (null != p.getRight()
                && !disambiguator().equals(p.getRight())
                && !"+".equals(p.getRight())
                && null == getUnicodeForWowel(p.getRight()));
    }
    public boolean isClearlyIllegal(TPair p) {
        if (pairHasBadWowel(p)) return true;
        if (p.getLeft() == null
        	&& (p.getRight() == null ||
        		(!disambiguator().equals(p.getRight())
        		 && !isWowel(p.getRight()))))
            return true;
        if ("+".equals(p.getLeft()))
            return true;
        if (p.getLeft() != null && isWowel(p.getLeft())
            && !aVowel().equals(p.getLeft())) // achen
            return true;
        return false;
    }

    public TPairList[] breakTshegBarIntoChunks(String tt, boolean sh) {
        if (sh) throw new IllegalArgumentException("Don't do that, silly!");
        try {
            return TPairListFactory.breakEWTSIntoChunks(tt);
        } catch (StackOverflowError e) {
            throw new IllegalArgumentException("Input too large[1]: " + tt);
        } catch (OutOfMemoryError e) {
            throw new IllegalArgumentException("Input too large[2]: " + tt);
        }
    }
    
    public boolean isACIP() { return false; }
    
    public boolean vowelAloneImpliesAChen() { return true; }
    
    public boolean vowelsMayStack() { return true; }

    public boolean isWowelThatRequiresAChen(String s) {
        // TODO(DLC)[EWTS->Tibetan]: fix me!
        return ((s.length() == 1 && (isUnicodeWowelThatRequiresAChen(s.charAt(0))
                                     || "?MHX".indexOf(s.charAt(0)) >= 0))
                // DLC say ah                || "aM".equals(s) // DLC funny...  (DLC NOW too funny! affects longest wowel length!)
                // DLC say ah                || "a?".equals(s) // DLC funny...
                // DLC say ah                || "aH".equals(s) // DLC funny...
                // DLC say ah                || "aX".equals(s) // DLC funny...
                || "~X".equals(s)
                // DLC say ah                || "a~X".equals(s) // DLC funny...
                || "~M".equals(s)
                // DLC say ah                || "a~M".equals(s) // DLC funny...
                || "~M`".equals(s)
                // DLC say ah                || "a~M`".equals(s) // DLC funny...
                );
    }

    public boolean isUnicodeWowelThatRequiresAChen(char ch) {
        // TODO(DLC)[EWTS->Tibetan]: ask if 18 19 3e 3f combine only with digits
        return "\u0f35\u0f37\u0f18\u0f19\u0f3e\u0f3f\u0f86\u0f87\u0fc6".indexOf(ch) >= 0;
    }

    public boolean couldBeValidStack(TPairList pl) {
        StringBuffer hashKey = new StringBuffer();
        boolean allHavePlus = true;
        for (int i = 0; i < pl.size(); i++) {
            if (i + 1 < pl.size() && !"+".equals(pl.get(i).getRight()))
                allHavePlus = false;
            if (0 != hashKey.length())
                hashKey.append('-');
            hashKey.append(pl.get(i).getLeft());
        }
        return (allHavePlus
                || TibetanMachineWeb.hasGlyph(hashKey.toString())); // TODO(DLC)[EWTS->Tibetan]: test with smra and tsma and bdgya
    }
}
