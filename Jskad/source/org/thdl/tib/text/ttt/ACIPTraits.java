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
Library (THDL). Portions created by the THDL are Copyright 2003-2004 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text.ttt;

/** A singleton class that should contain (but due to laziness and
 *  ignorance probably does not contain) all the traits that make ACIP
 *  transliteration different from other (say, EWTS)
 *  transliterations. */
final class ACIPTraits implements TTraits {
    /** sole instance of this class */
    private static ACIPTraits singleton = null;

    /** Just a constructor. */
    private ACIPTraits() { }

    /** Returns the singleton instance of this class. */
    public static ACIPTraits instance() {
        if (null == singleton) {
            singleton = new ACIPTraits();
        }
        return singleton;
    }

    /** Returns "-". */
    public String disambiguator() { return "-"; }

    /** Returns '-'. */
    public char disambiguatorChar() { return '-'; }

    public int maxConsonantLength() { return ACIPRules.MAX_CONSONANT_LENGTH; }

    public int maxWowelLength() { return ACIPRules.MAX_WOWEL_LENGTH; }

    public boolean isConsonant(String s) { return ACIPRules.isConsonant(s); }

    public boolean isWowel(String s) { return ACIPRules.isWowel(s); }

    public boolean hasSimpleError(TPair p) {
        return ("A".equals(p.getLeft()) && null == p.getRight());
    }
}
