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

/** A TTraits object encapsulates all the things that make a
 *  particular Roman transliteration scheme unique.  If both EWTS and
 *  ACIP transliterations have a property in common, then it's likely
 *  encoded in a manner that's hard to modify.  But if they differ in
 *  some respect, then that difference should be encoded in a TTraits
 *  object.
 *
 *  <p>It is very likely that classes that implement this interface
 *  will choose to use the design pattern 'singleton'. */
interface TTraits {
    /** Returns the disambiguator for this transliteration scheme,
     *  which had better be a string containing just one character
     *  lest {@link #disambiguatorChar()} become nonsensical for
     *  certain transliteration systems.  A disambiguator is the
     *  string that separates two bits of transliteration that would
     *  otherwise be interpreted differently.  The canonical example
     *  is EWTS's {gya} versus {g.ya}, which, due to the disambiguator
     *  in the latter, are different sequences of Unicode. */
    String disambiguator();

    /** For convenience, a convenience that is possible because ACIP's
     *  and EWTS's disambiguator strings both have length one, this
     *  method returns the sole character in the string returned by
     *  {@link #disambiguator()}. */
    char disambiguatorChar();

    /** Returns the maximum number of characters of transliteration
     *  required to denote a Tibetan consonant. */
    int maxConsonantLength();

    /** Returns the maximum number of characters of transliteration
     *  required to denote a Tibetan wowel, i.e. a vowel or one or
     *  more hangers-on like U+TODO(DLC)[EWTS->Tibetan]:_NOW or both. */
    int maxWowelLength();
    // TODO(DLC)[EWTS->Tibetan]: use the term 'wowel' everywhere, never "vowel" unless you mean just {e,i,o,u}

    /** Returns true if and only if <em>s</em> is a stretch of
     *  transliteration corresponding to a Tibetan consonant (without
     *  any wowel) */
    boolean isConsonant(String s);

    /** Returns true if and only if <em>s</em> is a stretch of
     *  transliteration corresponding to a Tibetan wowel (without any
     *  [achen or other] consonant) */
    boolean isWowel(String s);

    /** Returns true if and only if the pair given has a simple error
     *  other than being a mere disambiguator. */
    boolean hasSimpleError(TPair p);
}
