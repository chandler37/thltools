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
import java.util.HashMap;

/** Canonizes some facts regarding the ACIP transcription system.
 *  @author David Chandler */
class ACIPRules {
    /** {Ksh}, the longest consonant, has 3 characters, so this is
     *  three. */
    public static int MAX_CONSONANT_LENGTH = 3;

    /** {'im:}, the longest "vowel", has 4 characters, so this is
     *  four. */
    public static int MAX_VOWEL_LENGTH = 4;

    /** For O(1) {@link #isVowel(String)} calls. */
    private static HashSet acipVowels = null;

    private static String[][] baseVowels = new String[][] {
        // { ACIP, EWTS }:
        { "A", "a" },
        { "I", "i" },
        { "U", "u" },
        { "E", "e" },
        { "O", "o" },
        { "'I", "I" },
        { "'U", "U" },
        { "EE", "ai" },
        { "OO", "au" },
        { "i", "-i" },
        { "'i", "-I" },
        { "'A", "A" },
        { "'O", "Ao" },
        { "'E", "Ae" }
        // DLC I'm on my own with 'O and 'E, but GANG'O appears
        // and I wonder... so here are 'O and 'E.  It's
        // consistent with 'I and 'A and 'U, at least.
    };

    /** Returns true if and only if s is an ACIP "vowel".  You can't
     *  just call this any time -- A is a consonant and a vowel in
     *  ACIP, so you have to call this in the right context. */
    public static boolean isVowel(String s) {
        if (null == acipVowels) {
            acipVowels = new HashSet();
            for (int i = 0; i < baseVowels.length; i++) {
                acipVowels.add(baseVowels[i][0]);
                acipVowels.add(baseVowels[i][0] + 'm');
                acipVowels.add(baseVowels[i][0] + ':');
                acipVowels.add(baseVowels[i][0] + "m:");
                // DLC '\' for visarga? how shall we do \ the visarga? like a vowel or not?

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
                acipVowel2wylie.put(baseVowels[i][0] + 'm', baseVowels[i][1] + 'M');
                acipVowel2wylie.put(baseVowels[i][0] + ':', baseVowels[i][1] + 'H');
                acipVowel2wylie.put(baseVowels[i][0] + "m:", baseVowels[i][1] + "MH");
            }
        }
        return (String)acipVowel2wylie.get(acip);
    }
}
