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
    when this is a vowel or U+0F7F alone.

    @author David Chandler */
public class TGCPair {
    public static final int OTHER = 1;
    // a standalone achen would fall into this category:
    public static final int CONSONANTAL_WITHOUT_VOWEL = 2;
    public static final int CONSONANTAL_WITH_VOWEL = 3;
    public static final int LONE_VOWEL = 4;
    public static final int SANSKRIT_WITHOUT_VOWEL = 5;
    public static final int SANSKRIT_WITH_VOWEL = 6;

    public String wylie;
    public int classification;
    public TGCPair(String wylie, int classification) {
        this.wylie = wylie;
        this.classification = classification;
    }
    public String toString() {
        return "<TGCPair wylie=" + wylie + " classification="
            + classification + "/>";
    }
}
