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

package org.thdl.tib.text;


/** An ordered pair consisting of a Tibetan grapheme cluster's (see
    {@link org.thdl.tib.text.tshegbar.UnicodeGraphemeCluster} for a
    definition of the term}) classification and its
    context-insensitive THDL Extended Wylie representation.  NOTE
    WELL: this is not a real grapheme cluster; I'm misusing the term
    (FIXME).  It's actually whole or part of one.  It's part of one
    when this is U+0F7F alone.

    @author David Chandler */
public class TGCPair {
    public static final int OTHER = 1;
    // a standalone achen would fall into this category:
    public static final int CONSONANTAL_WITHOUT_VOWEL = 2;
    public static final int CONSONANTAL_WITH_VOWEL = 3;
    public static final int LONE_VOWEL = 4;
    public static final int SANSKRIT_WITHOUT_VOWEL = 5;
    public static final int SANSKRIT_WITH_VOWEL = 6;

    public static final int TYPE_OTHER = 31;
    public static final int TYPE_SANSKRIT = 32;
    public static final int TYPE_TIBETAN = 33;

    // Sanskrit or Tibetan consonant, or number, or oddball:
    private String consonantWylie;
    private String vowelWylie;
    public String getConsonantWylie() {
        return consonantWylie;
    }
    public String getVowelWylie() {
        return vowelWylie;
    }
    /** Cludge. */
    public void setWylie(String x) {
        consonantWylie = x;
        vowelWylie = null;
    }
    public String getWylie() {
        StringBuffer b = new StringBuffer();
        if (consonantWylie != null) {
            // we may have {p-y}, but the user wants to see {py}.
            for (int i = 0; i < consonantWylie.length(); i++) {
                char ch = consonantWylie.charAt(i);
                if ('-' != ch)
                    b.append(ch);
            }
        }
        if (vowelWylie != null)
            b.append(vowelWylie);
        return b.toString();
    }
    public int classification;
    /** Constructs a new TGCPair with (Tibetan or Sanskrit) consonant
     *  consonantWylie and vowel vowelWylie.  Use
     *  classification==TYPE_OTHER for numbers, lone vowels, marks,
     *  etc.  Use classification==TYPE_TIBETAN for Tibetan (not
     *  Tibetanized Sanskrit) and classification=TYPE_SANSKRIT for
     *  Tibetanized Sanskrit. */
    public TGCPair(String consonantWylie, String vowelWylie, int classification) {
        if ("".equals(vowelWylie))
            vowelWylie = null;
        // Technically, we don't need the following check, but it's
        // nice for consistency's sake.
        if ("".equals(consonantWylie))
            consonantWylie = null;

        // DLC FIXME: for speed, make these assertions:
        if (classification != TYPE_OTHER
            && classification != TYPE_TIBETAN
            && classification != TYPE_SANSKRIT) {
            throw new IllegalArgumentException("Bad classification " + classification + ".");
        }
        int realClassification = -37;
        if (vowelWylie == null && classification == TYPE_TIBETAN)
            realClassification = CONSONANTAL_WITHOUT_VOWEL;
        if (vowelWylie != null && classification == TYPE_TIBETAN)
            realClassification = CONSONANTAL_WITH_VOWEL;
        if (vowelWylie == null && classification == TYPE_SANSKRIT)
            realClassification = SANSKRIT_WITHOUT_VOWEL;
        if (vowelWylie != null && classification == TYPE_SANSKRIT)
            realClassification = SANSKRIT_WITH_VOWEL;
        if (consonantWylie == null) {
            if (classification != TYPE_OTHER)
                throw new IllegalArgumentException("That's the very definition of a lone vowel.");
            realClassification = LONE_VOWEL;
        } else {
            if (classification == TYPE_OTHER)
                realClassification = OTHER;
        }

        this.consonantWylie = consonantWylie;
        this.vowelWylie = vowelWylie;
        this.classification = realClassification;
    }

    public String toString() {
        return "<TGCPair wylie=" + getWylie() + " classification="
            + classification + "/>";
    }
}
