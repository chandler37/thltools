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

import java.util.ArrayList;
import org.thdl.tib.text.DuffCode;

/** A TTraits object encapsulates all the things that make a
 *  particular Roman transliteration scheme unique.  For the most
 *  part, this difference is expressed at the finest granularity
 *  possible -- often single characters of Roman transliteration.
 *
 *  <p>If both EWTS and ACIP transliterations have a property in
 *  common, then it's likely encoded in a manner that's hard to
 *  modify.  But if they differ in some respect, then that difference
 *  should be encoded in a TTraits object.
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
    boolean isWowel(String s); // TODO(DLC)[EWTS->Tibetan]: what about "m:" as opposed to "m" or ":"

    /** Returns true if and only if the pair given has a simple error
     *  other than being a mere disambiguator. */
    boolean hasSimpleError(TPair p);

    /** The implicit 'ahhh' vowel, the one you see when you write the
        human-friendly transliteration for "\u0f40\u0f0b". */
    String aVowel();

    /** Returns true if s is a valid postsuffix.  s must not have a
        wowel on it. */
    boolean isPostsuffix(String s);

    /** Returns true if and only if l is the representation of a
        letter that can be a suffix.  Note that all postsuffixes are
        also suffixes.  l should not have a wowel. */
    boolean isSuffix(String l);

    /** Returns true if and only if l is the representation of a
        letter that can be a prefix.  l should not have a wowel. */
    boolean isPrefix(String l);

    /** Returns the EWTS transliteration corresponding to the
     *  consonant l, which should not have a vowel.  Returns null if
     *  there is no such EWTS.
     *
     *  <p>May return "W" instead of "w", "r" instead of "R", and "y"
     *  instead of "Y" because we sometimes don't have enough context
     *  to decide.
     *
     *  <p>The reasoning for "W" instead of "w" is that r-w and r+w
     *  are both known hash keys (as {@link
     *  org.thdl.tib.text#TibetanMachineWeb} would call them).  We
     *  sort 'em out this way.  (They are the only things like this
     *  according to bug report #800166.) */
    String getEwtsForConsonant(String l);

    /** Returns the EWTS corresponding to the given punctuation or
     *  mark.  Returns null if there is no such EWTS. */
    String getEwtsForOther(String l);

    /** Returns the EWTS corresponding to the given "wowel".  Returns
     *  null if there is no such EWTS. */
    String getEwtsForWowel(String l);

    /** If l is a consonant or vowel or punctuation mark, then this
     *  returns the Unicode for it.  The Unicode for the subscribed
     *  form of the glyph is returned if subscribed is true.  Returns
     *  null if l is unknown. */
    String getUnicodeFor(String l, boolean subscribed);

    /** Returns a scanner that can break up a string of
        transliteration. */
    TTshegBarScanner scanner();

    /** Gets the duffcodes for wowel, such that they look good with
     *  the preceding glyph, and appends them to duff. */
    void getDuffForWowel(ArrayList duff, DuffCode preceding, String wowel);
}
