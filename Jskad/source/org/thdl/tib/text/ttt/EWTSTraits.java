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

package org.thdl.tib.text.ttt;

/** A singleton class that should contain (but due to laziness and
 *  ignorance probably does not contain) all the traits that make EWTS
 *  transliteration different from other (say, ACIP) transliteration
 *  schemes. */
final class EWTSTraits implements TTraits {
    /** sole instance of this class */
    private static EWTSTraits singleton = null;

    /** Just a constructor. */
    private EWTSTraits() { }

    /** */
    public static EWTSTraits instance() {
        if (null == singleton) {
            singleton = new EWTSTraits();
        }
        return singleton;
    }

    /** Returns ".". */
    public String disambiguator() { return "."; }

    /** Returns '.'. */
    public char disambiguatorChar() { return '.'; }

    public boolean hasSimpleError(TPair p) {
        return ("a".equals(p.getLeft()) && null == p.getRight()); // TODO(DLC)[EWTS->Tibetan]: (a.e) is bad, one of (.a) or (a.) is bad
    }

    /** {tsh}, the longest consonant, has 3 characters, so this is
     *  three. */
    public int maxConsonantLength() { return 3; }

    /** {-i~M`}, in a tie for the longest wowel, has 6 characters, so
     *  this is six.  (No, 'l-i' and 'r-i' are not wowels (but '-i'
     *  is). */
    public int maxWowelLength() { return 5; }

// TODO(DLC)[EWTS->Tibetan]: u,e,i,o?  If not, document the special treatment in this function's comment
    public boolean isConsonant(String s) {
        // TODO(DLC)[EWTS->Tibetan]: just g for now
        return "g".equals(s);
    }

    public boolean isWowel(String s) {
        // TODO(DLC)[EWTS->Tibetan]: all non-consonant combiners? 0f71 0f87 etc.?
        return ("a".equals(s)
                || "e".equals(s)
                || "i".equals(s)
                || "o".equals(s)
                || "u".equals(s)
                || "?".equals(s) // TODO(DLC)[EWTS->Tibetan]: 0f84 virama???
                // TODO(DLC)[EWTS->Tibetan]: & ~M` ~M ???
                || "U".equals(s)
                || "I".equals(s)
                || "A".equals(s)
                || "-i".equals(s)
                || "-I".equals(s)
                || "H".equals(s)
                || "M".equals(s)); // TODO(DLC)[EWTS->Tibetan]:???
    }
}
