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

import java.util.HashSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.thdl.tib.text.DuffCode;
import org.thdl.tib.text.THDLWylieConstants;
import org.thdl.tib.text.TibetanMachineWeb;
import org.thdl.tib.text.TibTextUtils;

/** Canonizes some facts regarding the ACIP transcription system.
 *  @author David Chandler */
class ACIPRules {
    /** {Ksh}, the longest consonant, has 3 characters, so this is
     *  three. */
    public static int MAX_CONSONANT_LENGTH = 3;

    /** {'EEm:}, the longest "vowel", has 5 characters, so this is
     *  five. */
    public static int MAX_VOWEL_LENGTH = 5;

    /** For O(1) {@link #isVowel(String)} calls. */
    private static HashSet acipVowels = null;

    private static String[][] baseVowels = new String[][] {
        // { ACIP, EWTS, EWTS for ACIP {'\'' + baseVowels[][0]}, vowel
        // numbers (see TibetanMachineWeb's VOWEL_A, VOWEL_o, etc.) 
        // for ACIP, vowel numbers for ACIP {'\'' + baseVowels[][0]}
        { "A", "a", "A" },
        { "I", "i", "I" },
        { "U", "u", "U" },
        { "E", "e", "Ae" },
        { "O", "o", "Ao" },
        { "EE", "ai", "Aai" },
        { "OO", "au", "Aau" },
        { "i", "-i", "A-i" }
    };

    /** Returns true if and only if s is an ACIP "vowel".  You can't
     *  just call this any time -- A is a consonant and a vowel in
     *  ACIP, so you have to call this in the right context. */
    public static boolean isVowel(String s) {
        if (null == acipVowels) {
            acipVowels = new HashSet(baseVowels.length * 8);
            for (int i = 0; i < baseVowels.length; i++) {
                // DLC I'm on my own with 'O and 'E and 'OO and 'EE, but
                // GANG'O appears and I wonder... so here they are.  It's
                // consistent with 'I and 'A and 'U, at least: all the vowels
                // may appear as K'vowel.

                acipVowels.add(baseVowels[i][0]);
                acipVowels.add('\'' + baseVowels[i][0]);
                acipVowels.add(baseVowels[i][0] + 'm');
                acipVowels.add('\'' + baseVowels[i][0] + 'm');
                acipVowels.add(baseVowels[i][0] + ':');
                acipVowels.add('\'' + baseVowels[i][0] + ':');
                acipVowels.add(baseVowels[i][0] + "m:");
                acipVowels.add('\'' + baseVowels[i][0] + "m:");
                // DLC keep this code in sync with getUnicodeFor.
                // DLC keep this code in sync with getWylieForACIPVowel

                // DLC '\' for virama? how shall we do \ the virama? like a vowel or not?
            }
        }
        return (acipVowels.contains(s));
    }

    /** For O(1) {@link #isConsonant(String)} calls. */
    private static HashSet consonants = null;

    /** Returns true if and only if acip is an ACIP consonant (without
     *  a vowel). For example, returns true for "K", but not for
     *  "KA" or "X". */
    public static boolean isConsonant(String acip) {
        if (consonants == null) {
            consonants = new HashSet();
            consonants.add("V");
            consonants.add("K");
            consonants.add("KH");
            consonants.add("G");
            consonants.add("NG");
            consonants.add("C");
            consonants.add("CH");
            consonants.add("J");
            consonants.add("NY");
            consonants.add("T");
            consonants.add("TH");
            consonants.add("D");
            consonants.add("N");
            consonants.add("P");
            consonants.add("PH");
            consonants.add("B");
            consonants.add("M");
            consonants.add("TZ");
            consonants.add("TS");
            consonants.add("DZ");
            consonants.add("W");
            consonants.add("ZH");
            consonants.add("Z");
            consonants.add("Y");
            consonants.add("R");
            consonants.add("L");
            consonants.add("SH");
            consonants.add("S");
            consonants.add("H");
            consonants.add("t");
            consonants.add("th");
            consonants.add("d");
            consonants.add("n");
            consonants.add("sh");
            consonants.add("dH");
            consonants.add("DH");
            consonants.add("BH");
            consonants.add("DZH"); // longest, MAX_CONSONANT_LENGTH characters
            consonants.add("Ksh"); // longest, MAX_CONSONANT_LENGTH characters
            consonants.add("GH");
            consonants.add("'");
            consonants.add("A");
        }
        return consonants.contains(acip);
    }

    private static HashMap acipConsonant2wylie = null;
    /** Returns the EWTS corresponding to the given ACIP consonant
     *  (without the "A" vowel).  Returns null if there is no such
     *  EWTS. */
    static final String getWylieForACIPConsonant(String acip) {
        if (acipConsonant2wylie == null) {
            acipConsonant2wylie = new HashMap(37);

            // oddball:
            acipConsonant2wylie.put("V", "w");

            // more oddballs:
            acipConsonant2wylie.put("DH", "d+h");
            acipConsonant2wylie.put("BH", "b+h");
            acipConsonant2wylie.put("dH", "D+h");
            acipConsonant2wylie.put("DZH", "dz+h");
            acipConsonant2wylie.put("Ksh", "k+Sh");
            acipConsonant2wylie.put("GH", "g+h");


            acipConsonant2wylie.put("K", "k");
            acipConsonant2wylie.put("KH", "kh");
            acipConsonant2wylie.put("G", "g");
            acipConsonant2wylie.put("NG", "ng");
            acipConsonant2wylie.put("C", "c");
            acipConsonant2wylie.put("CH", "ch");
            acipConsonant2wylie.put("J", "j");
            acipConsonant2wylie.put("NY", "ny");
            acipConsonant2wylie.put("T", "t");
            acipConsonant2wylie.put("TH", "th");
            acipConsonant2wylie.put("D", "d");
            acipConsonant2wylie.put("N", "n");
            acipConsonant2wylie.put("P", "p");
            acipConsonant2wylie.put("PH", "ph");
            acipConsonant2wylie.put("B", "b");
            acipConsonant2wylie.put("M", "m");
            acipConsonant2wylie.put("TZ", "ts");
            acipConsonant2wylie.put("TS", "tsh");
            acipConsonant2wylie.put("DZ", "dz");
            acipConsonant2wylie.put("W", "w");
            acipConsonant2wylie.put("ZH", "zh");
            acipConsonant2wylie.put("Z", "z");
            acipConsonant2wylie.put("'", "'");
            acipConsonant2wylie.put("Y", "y");
            acipConsonant2wylie.put("R", "r");
            acipConsonant2wylie.put("L", "l");
            acipConsonant2wylie.put("SH", "sh");
            acipConsonant2wylie.put("S", "s");
            acipConsonant2wylie.put("H", "h");
            acipConsonant2wylie.put("A", "a");
            acipConsonant2wylie.put("t", "T");
            acipConsonant2wylie.put("th", "Th");
            acipConsonant2wylie.put("d", "D");
            acipConsonant2wylie.put("n", "N");
            acipConsonant2wylie.put("sh", "Sh");
        }
        return (String)acipConsonant2wylie.get(acip);
    }

    private static HashMap acipVowel2wylie = null;
    /** Returns the EWTS corresponding to the given ACIP "vowel".
     *  Returns null if there is no such EWTS. */
    static final String getWylieForACIPVowel(String acip) {
        if (acipVowel2wylie == null) {
            acipVowel2wylie = new HashMap(baseVowels.length * 4);

            for (int i = 0; i < baseVowels.length; i++) {
                acipVowel2wylie.put(baseVowels[i][0], baseVowels[i][1]);
                acipVowel2wylie.put('\'' + baseVowels[i][0], baseVowels[i][2]);
                acipVowel2wylie.put(baseVowels[i][0] + 'm', baseVowels[i][1] + 'M');
                acipVowel2wylie.put('\'' + baseVowels[i][0] + 'm', baseVowels[i][2] + 'M');
                acipVowel2wylie.put(baseVowels[i][0] + ':', baseVowels[i][1] + 'H');
                acipVowel2wylie.put('\'' + baseVowels[i][0] + ':', baseVowels[i][2] + 'H');
                acipVowel2wylie.put(baseVowels[i][0] + "m:", baseVowels[i][1] + "MH");
                acipVowel2wylie.put('\'' + baseVowels[i][0] + "m:", baseVowels[i][2] + "MH");
            }
        }
        return (String)acipVowel2wylie.get(acip);
    }

    private static HashMap acipOther2wylie = null;
    /** Returns the EWTS corresponding to the given ACIP puncuation or
     *  mark.  Returns null if there is no such EWTS. */
    static final String getWylieForACIPOther(String acip) {
        if (acipOther2wylie == null) {
            acipOther2wylie = new HashMap(20);

            // DLC FIXME: check all these again.
            acipOther2wylie.put(",", "/");
            acipOther2wylie.put(" ", " ");
            acipOther2wylie.put(".", "*");
            acipOther2wylie.put("|", "|");
            acipOther2wylie.put("`", "!");
            acipOther2wylie.put(";", ";");
            acipOther2wylie.put("*", "@");
            acipOther2wylie.put("#", "@#");
            acipOther2wylie.put("%", "~X");
            acipOther2wylie.put("&", "&");

            acipOther2wylie.put("0", "0");
            acipOther2wylie.put("1", "1");
            acipOther2wylie.put("2", "2");
            acipOther2wylie.put("3", "3");
            acipOther2wylie.put("4", "4");
            acipOther2wylie.put("5", "5");
            acipOther2wylie.put("6", "6");
            acipOther2wylie.put("7", "7");
            acipOther2wylie.put("8", "8");
            acipOther2wylie.put("9", "9");
        }
        return (String)acipOther2wylie.get(acip);
    }

    private static HashMap superACIP2unicode = null;
    private static HashMap subACIP2unicode = null;
    /** If acip is an ACIP consonant or vowel or punctuation mark,
     *  then this returns the Unicode for it.  The Unicode for the
     *  subscribed form of the glyph is returned if subscribed is
     *  true.  Returns null if acip is unknown. */
    static String getUnicodeFor(String acip, boolean subscribed) {
        if (superACIP2unicode == null) {
            superACIP2unicode = new HashMap(144);
            subACIP2unicode = new HashMap(42);

            // oddball:
            subACIP2unicode.put("V", "\u0FAD");

            superACIP2unicode.put("DH", "\u0F52");
            subACIP2unicode.put("DH", "\u0FA2");
            superACIP2unicode.put("BH", "\u0F57");
            subACIP2unicode.put("BH", "\u0FA7");
            superACIP2unicode.put("dH", "\u0F4D");
            subACIP2unicode.put("dH", "\u0F9D");
            superACIP2unicode.put("DZH", "\u0F5C");
            subACIP2unicode.put("DZH", "\u0FAC");
            superACIP2unicode.put("Ksh", "\u0F69");
            subACIP2unicode.put("Ksh", "\u0FB9");
            superACIP2unicode.put("GH", "\u0F43");
            subACIP2unicode.put("GH", "\u0F93");
            superACIP2unicode.put("K", "\u0F40");
            subACIP2unicode.put("K", "\u0F90");
            superACIP2unicode.put("KH", "\u0F41");
            subACIP2unicode.put("KH", "\u0F91");
            superACIP2unicode.put("G", "\u0F42");
            subACIP2unicode.put("G", "\u0F92");
            superACIP2unicode.put("NG", "\u0F44");
            subACIP2unicode.put("NG", "\u0F94");
            superACIP2unicode.put("C", "\u0F45");
            subACIP2unicode.put("C", "\u0F95");
            superACIP2unicode.put("CH", "\u0F46");
            subACIP2unicode.put("CH", "\u0F96");
            superACIP2unicode.put("J", "\u0F47");
            subACIP2unicode.put("J", "\u0F97");
            superACIP2unicode.put("NY", "\u0F49");
            subACIP2unicode.put("NY", "\u0F99");
            superACIP2unicode.put("T", "\u0F4F");
            subACIP2unicode.put("T", "\u0F9F");
            superACIP2unicode.put("TH", "\u0F50");
            subACIP2unicode.put("TH", "\u0FA0");
            superACIP2unicode.put("D", "\u0F51");
            subACIP2unicode.put("D", "\u0FA1");
            superACIP2unicode.put("N", "\u0F53");
            subACIP2unicode.put("N", "\u0FA3");
            superACIP2unicode.put("P", "\u0F54");
            subACIP2unicode.put("P", "\u0FA4");
            superACIP2unicode.put("PH", "\u0F55");
            subACIP2unicode.put("PH", "\u0FA5");
            superACIP2unicode.put("B", "\u0F56");
            subACIP2unicode.put("B", "\u0FA6");
            superACIP2unicode.put("M", "\u0F58");
            subACIP2unicode.put("M", "\u0FA8");
            superACIP2unicode.put("TZ", "\u0F59");
            subACIP2unicode.put("TZ", "\u0FA9");
            superACIP2unicode.put("TS", "\u0F5A");
            subACIP2unicode.put("TS", "\u0FAA");
            superACIP2unicode.put("DZ", "\u0F5B");
            subACIP2unicode.put("DZ", "\u0FAB");
            superACIP2unicode.put("W", "\u0F5D");
            subACIP2unicode.put("W", "\u0FBA"); // oddball
            superACIP2unicode.put("ZH", "\u0F5E");
            subACIP2unicode.put("ZH", "\u0FAE");
            superACIP2unicode.put("Z", "\u0F5F");
            subACIP2unicode.put("Z", "\u0FAF");
            superACIP2unicode.put("'", "\u0F60");
            subACIP2unicode.put("'", "\u0FB0");
            superACIP2unicode.put("Y", "\u0F61");
            subACIP2unicode.put("Y", "\u0FB1");
            superACIP2unicode.put("R", "\u0F62");
            subACIP2unicode.put("R", "\u0FB2");
            superACIP2unicode.put("L", "\u0F63");
            subACIP2unicode.put("L", "\u0FB3");
            superACIP2unicode.put("SH", "\u0F64");
            subACIP2unicode.put("SH", "\u0FB4");
            superACIP2unicode.put("S", "\u0F66");
            subACIP2unicode.put("S", "\u0FB6");
            superACIP2unicode.put("H", "\u0F67");
            subACIP2unicode.put("H", "\u0FB7");
            superACIP2unicode.put("A", "\u0F68");
            subACIP2unicode.put("A", "\u0FB8");
            superACIP2unicode.put("t", "\u0F4A");
            subACIP2unicode.put("t", "\u0F9A");
            superACIP2unicode.put("th", "\u0F4B");
            subACIP2unicode.put("th", "\u0F9B");
            superACIP2unicode.put("d", "\u0F4C");
            subACIP2unicode.put("d", "\u0F9C");
            superACIP2unicode.put("n", "\u0F4E");
            subACIP2unicode.put("n", "\u0F9E");
            superACIP2unicode.put("sh", "\u0F65");
            subACIP2unicode.put("sh", "\u0FB5");

            superACIP2unicode.put("I", "\u0F72");
            superACIP2unicode.put("E", "\u0F7A");
            superACIP2unicode.put("O", "\u0F7C");
            superACIP2unicode.put("U", "\u0F74");
            superACIP2unicode.put("OO", "\u0F7D");
            superACIP2unicode.put("EE", "\u0F7B");
            superACIP2unicode.put("i", "\u0F80");
            superACIP2unicode.put("'A", "\u0F71");
            superACIP2unicode.put("'I", "\u0F71\u0F72");
            superACIP2unicode.put("'E", "\u0F71\u0F7A");
            superACIP2unicode.put("'O", "\u0F71\u0F7C");
            superACIP2unicode.put("'U", "\u0F71\u0F74");
            superACIP2unicode.put("'OO", "\u0F71\u0F7D");
            superACIP2unicode.put("'EE", "\u0F71\u0F7B");
            superACIP2unicode.put("'i", "\u0F71\u0F80");

            superACIP2unicode.put("Im", "\u0F72\u0F7E");
            superACIP2unicode.put("Em", "\u0F7A\u0F7E");
            superACIP2unicode.put("Om", "\u0F7C\u0F7E");
            superACIP2unicode.put("Um", "\u0F74\u0F7E");
            superACIP2unicode.put("OOm", "\u0F7D\u0F7E");
            superACIP2unicode.put("EEm", "\u0F7B\u0F7E");
            superACIP2unicode.put("im", "\u0F80\u0F7E");
            superACIP2unicode.put("'Am", "\u0F71\u0F7E");
            superACIP2unicode.put("'Im", "\u0F71\u0F72\u0F7E");
            superACIP2unicode.put("'Em", "\u0F71\u0F7A\u0F7E");
            superACIP2unicode.put("'Om", "\u0F71\u0F7C\u0F7E");
            superACIP2unicode.put("'Um", "\u0F71\u0F74\u0F7E");
            superACIP2unicode.put("'OOm", "\u0F71\u0F7D\u0F7E");
            superACIP2unicode.put("'EEm", "\u0F71\u0F7B\u0F7E");
            superACIP2unicode.put("'im", "\u0F71\u0F80\u0F7E");

            superACIP2unicode.put("I:", "\u0F72\u0F7F");
            superACIP2unicode.put("E:", "\u0F7A\u0F7F");
            superACIP2unicode.put("O:", "\u0F7C\u0F7F");
            superACIP2unicode.put("U:", "\u0F74\u0F7F");
            superACIP2unicode.put("OO:", "\u0F7D\u0F7F");
            superACIP2unicode.put("EE:", "\u0F7B\u0F7F");
            superACIP2unicode.put("i:", "\u0F80\u0F7F");
            superACIP2unicode.put("'A:", "\u0F71\u0F7F");
            superACIP2unicode.put("'I:", "\u0F71\u0F72\u0F7F");
            superACIP2unicode.put("'E:", "\u0F71\u0F7A\u0F7F");
            superACIP2unicode.put("'O:", "\u0F71\u0F7C\u0F7F");
            superACIP2unicode.put("'U:", "\u0F71\u0F74\u0F7F");
            superACIP2unicode.put("'OO:", "\u0F71\u0F7D\u0F7F");
            superACIP2unicode.put("'EE:", "\u0F71\u0F7B\u0F7F");
            superACIP2unicode.put("'i:", "\u0F71\u0F80\u0F7F");

            superACIP2unicode.put("Im:", "\u0F72\u0F7E\u0F7F");
            superACIP2unicode.put("Em:", "\u0F7A\u0F7E\u0F7F");
            superACIP2unicode.put("Om:", "\u0F7C\u0F7E\u0F7F");
            superACIP2unicode.put("Um:", "\u0F74\u0F7E\u0F7F");
            superACIP2unicode.put("OOm:", "\u0F7D\u0F7E\u0F7F");
            superACIP2unicode.put("EEm:", "\u0F7B\u0F7E\u0F7F");
            superACIP2unicode.put("im:", "\u0F80\u0F7E\u0F7F");
            superACIP2unicode.put("'Am:", "\u0F71\u0F7E\u0F7F");
            superACIP2unicode.put("'Im:", "\u0F71\u0F72\u0F7E\u0F7F");
            superACIP2unicode.put("'Em:", "\u0F71\u0F7A\u0F7E\u0F7F");
            superACIP2unicode.put("'Om:", "\u0F71\u0F7C\u0F7E\u0F7F");
            superACIP2unicode.put("'Um:", "\u0F71\u0F74\u0F7E\u0F7F");
            superACIP2unicode.put("'OOm:", "\u0F71\u0F7D\u0F7E\u0F7F");
            superACIP2unicode.put("'EEm:", "\u0F71\u0F7B\u0F7E\u0F7F");
            superACIP2unicode.put("'im:", "\u0F71\u0F80\u0F7E\u0F7F");
            // :m does not appear, though you'd think it's as valid as m:.

            // I doubt these will occur alone:
            superACIP2unicode.put("m", "\u0F7E");
            superACIP2unicode.put(":", "\u0F7F");

            superACIP2unicode.put("Am", "\u0F7E");
            superACIP2unicode.put("A:", "\u0F7F");

            superACIP2unicode.put("0", "\u0F20");
            superACIP2unicode.put("1", "\u0F21");
            superACIP2unicode.put("2", "\u0F22");
            superACIP2unicode.put("3", "\u0F23");
            superACIP2unicode.put("4", "\u0F24");
            superACIP2unicode.put("5", "\u0F25");
            superACIP2unicode.put("6", "\u0F26");
            superACIP2unicode.put("7", "\u0F27");
            superACIP2unicode.put("8", "\u0F28");
            superACIP2unicode.put("9", "\u0F29");

            // DLC punctuation
            superACIP2unicode.put("&", "\u0F85");
            superACIP2unicode.put(",", "\u0F0D");
            superACIP2unicode.put(" ", "\u0F0B");
            superACIP2unicode.put(".", "\u0F0C");
            superACIP2unicode.put("`", "\u0F08");
            superACIP2unicode.put("`", "\u0F08");
            superACIP2unicode.put("*", "\u0F04\u0F05");
            superACIP2unicode.put("#", "\u0F04\u0F05\u0F05");
            superACIP2unicode.put("%", "\u0F35");
            superACIP2unicode.put(";", "\u0F11");
            superACIP2unicode.put("\r", "\r");
            superACIP2unicode.put("\t", "\t");
            superACIP2unicode.put("\r\n", "\r\n");
            superACIP2unicode.put("\n", "\n");
            superACIP2unicode.put("\\", "\u0F84"); // DLC FIXME: make this like a vowel
            // DLC FIXME: what's the Unicode for caret, ^?
            // DLC FIXME: what's the Unicode for o?
            // DLC FIXME: what's the Unicode for x?

        }
        if (subscribed) {
            String u = (String)subACIP2unicode.get(acip);
            if (null != u) return u;
        }
        return (String)superACIP2unicode.get(acip);
    }



    /** Gets the duffcodes for vowel, such that they look good with
     *  the stack with hash key hashKey, and appends them to r. */
    static void getDuffForACIPVowel(ArrayList r, DuffCode preceding, String vowel) {
        if (null == vowel) return;
        if (null == getWylieForACIPVowel(vowel)) // FIXME: expensive assertion!  Use assert.
            throw new IllegalArgumentException("Vowel " + vowel + " isn't in the small set of vowels we handle correctly.");

        // Order matters here.
        if (vowel.startsWith("A")) {
            TibTextUtils.getVowel(r, preceding, THDLWylieConstants.WYLIE_aVOWEL);
        } else if (vowel.indexOf("'U") >= 0) {
            TibTextUtils.getVowel(r, preceding, "U");
        } else {
            if (vowel.indexOf('\'') >= 0)
                TibTextUtils.getVowel(r, preceding, THDLWylieConstants.A_VOWEL);
            if (vowel.indexOf("EE") >= 0)
                TibTextUtils.getVowel(r, preceding, THDLWylieConstants.ai_VOWEL);
            else if (vowel.indexOf('E') >= 0)
                TibTextUtils.getVowel(r, preceding, THDLWylieConstants.e_VOWEL);
            if (vowel.indexOf("OO") >= 0)
                TibTextUtils.getVowel(r, preceding, THDLWylieConstants.au_VOWEL);
            else if (vowel.indexOf('O') >= 0)
                TibTextUtils.getVowel(r, preceding, THDLWylieConstants.o_VOWEL);
            if (vowel.indexOf('I') >= 0)
                TibTextUtils.getVowel(r, preceding, THDLWylieConstants.i_VOWEL);
            if (vowel.indexOf('U') >= 0)
                TibTextUtils.getVowel(r, preceding, THDLWylieConstants.u_VOWEL);
            if (vowel.indexOf('i') >= 0)
                TibTextUtils.getVowel(r, preceding, THDLWylieConstants.reverse_i_VOWEL);
        }

        if (vowel.indexOf('m') >= 0)
            r.add(TibetanMachineWeb.getGlyph("M"));
        if (vowel.indexOf(':') >= 0)
            r.add(TibetanMachineWeb.getGlyph("H"));

    }
}
