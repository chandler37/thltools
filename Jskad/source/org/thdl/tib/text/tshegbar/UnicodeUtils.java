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
Library (THDL). Portions created by the THDL are Copyright 2001 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text.tshegbar;

/** <p>This non-instantiable class contains utility routines for
 *  dealing with Tibetan Unicode codepoints and strings of such
 *  codepoints.</p>
 *
 *  @author David Chandler */
public class UnicodeUtils implements UnicodeConstants {
    /** Do not use this, as this class is not instantiable. */
    private UnicodeUtils() { super(); }

    /** Returns true iff x is a Unicode codepoint that represents a
        consonant or two-consonant stack that has a Unicode code
        point.  Returns true only for the usual suspects (like
        <code>&#92;u0F40</code>) and for Sanskrit consonants (like
        <code>&#92;u0F71</code>) and the simple two-consonant stacks in
        Unicode (like <code>&#92;u0F43</code>).  Returns false for, among
        other things, subjoined consonants like
        <code>&#92;u0F90</code>. */
    public static boolean isNonSubjoinedConsonant(char x) {
        return ((x != '\u0F48' /* reserved in Unicode 3.2, but not in use */)
                && (x >= '\u0F40' && x <= '\u0F6A'));
    }

    /** Returns true iff x is a Unicode codepoint that represents a
        subjoined consonant or subjoined two-consonant stack that has
        a Unicode code point.  Returns true only for the usual
        suspects (like <code>&#92;u0F90</code>) and for Sanskrit
        consonants (like <code>&#92;u0F9C</code>) and the simple
        two-consonant stacks in Unicode (like <code>&#92;u0FAC</code>).
        Returns false for, among other things, non-subjoined
        consonants like <code>&#92;u0F40</code>. */
    public static boolean isSubjoinedConsonant(char x) {
        return ((x != '\u0F98' /* reserved in Unicode 3.2, but not in use */)
                && (x >= '\u0F90' && x <= '\u0FBC'));
    }

    /** Returns true iff x is the preferred representation of a
        Tibetan or Sanskrit consonant and cannot be broken down any
        further.  Returns false for, among other things, subjoined
        consonants like <code>&#92;u0F90</code>, two-component consonants
        like <code>&#92;u0F43</code>, and fixed-form consonants like
        '&#92;u0F6A'.  The new consonants (for transcribing Chinese, I
        believe) "&#92;u0F55&#92;u0F39" (which EWTS calls "fa"),
        "&#92;u0F56&#92;u0F39" ("va"), and "&#92;u0F5F&#92;u0F39" ("Dza") are
        two-codepoint sequences, but you should be aware of them
        also. */
    public static boolean isPreferredFormOfConsonant(char x) {
        return ((x != '\u0F48' /* reserved in Unicode 3.2, but not in use */)
                && (x >= '\u0F40' && x <= '\u0F68')
                && (x != '\u0F43')
                && (x != '\u0F4D')
                && (x != '\u0F52')
                && (x != '\u0F57')
                && (x != '\u0F5C'));
    }

    /** Returns true iff unicodeCP is a codepoint from the Unicode
        range U+0F00-U+0FFF.
        @see #isEntirelyTibetanUnicode(String) */
    public static boolean isInTibetanRange(char unicodeCP) {
        return (unicodeCP >= '\u0F00' && unicodeCP <= '\u0FFF');
    }

    /** Returns true iff unicodeString consists only of codepoints
        from the Unicode range U+0F00-U+0FFF.  (Note that these
        codepoints are typically not enough to represent a Tibetan
        text, you may need ZWSP (zero-width space) and various
        whitespace from other ranges.) */
    public static boolean isEntirelyTibetanUnicode(String unicodeString) {
        for (int i = 0; i < unicodeString.length(); i++) {
            if (!isInTibetanRange(unicodeString.charAt(i)))
                return false;
        }
        return true;
    }

    /** Puts the Tibetan codepoints in tibetanUnicode, a sequence of
        Unicode codepoints, into Normalization Form KD (NFKD) as
        specified by Unicode 3.2.  The Tibetan passages of the
        returned string are in NFKD, but codepoints outside of the
        range <code>U+0F00</code>-<code>U+0FFF</code> are not
        necessarily put into NFKD.  This form uses a maximum of
        codepoints, and it never uses codepoints whose use has been
        {@link #isDiscouraged(char) discouraged}.  It would be David
        Chandler's very favorite form if not for the fact that
        <code>U+0F0C</code> normalizes to <code>U+0F0B</code> in NFKD.
        NFD is thus David Chandler's favorite, though it does not
        decompose <code>U+0F77</code> and <code>U+0F79</code> (for
        some reason, hopefully a well-thought-out one).

        <p>Recall that NFKD, as it applies to Tibetan codepoints, is
        closed under string concatenation and under substringing.
        Note again that if the input contains codepoints for which
        {@link #isInTibetanRange(char)} is not true, then they will
        not be modified.</p>
    
        <p>Note well that only well-formed input guarantees
        well-formed output.</p>

        @param tibetanUnicode the codepoints to be decomposed
        @param normForm NORM_NFKD or NORM_NFD */
    public static void toMostlyDecomposedUnicode(StringBuffer tibetanUnicode,
                                                 byte normForm)
    {
        if (normForm != NORM_NFD && normForm != NORM_NFKD)
            throw new IllegalArgumentException("normForm must be NORM_NFD or NORM_NFKD for decomposition to work");
        int offset = 0;
        while (offset < tibetanUnicode.length()) {
            String s
                = toNormalizedForm(tibetanUnicode.charAt(offset), normForm);
            if (null == s) {
                ++offset;
            } else {
                // modify tibetanUnicode and update offset.
                tibetanUnicode.deleteCharAt(offset);
                tibetanUnicode.insert(offset, s);
            }
        }
    }

    /** Like {@link #toMostlyDecomposedUnicode(StringBuffer, byte)},
        but does not modify its input.  Instead, it returns the NFKD-
        or NFD-normalized version of tibetanUnicode. */
    public static String toMostlyDecomposedUnicode(String tibetanUnicode,
                                                   byte normForm)
    {
        StringBuffer sb = new StringBuffer(tibetanUnicode);
        toMostlyDecomposedUnicode(sb, normForm);
        return sb.toString();
    }

    /** There are 19 codepoints in the Tibetan range of Unicode 3.2
        which can be decomposed into longer strings of codepoints in
        the Tibetan range of Unicode.  Often one wants to manipulate
        decomposed codepoint strings.  Also, HTML and XML are W3C
        standards that require certain normalization forms.  This
        routine returns a chosen normalized form for such codepoints,
        and returns null for codepoints that are already normalized or
        are not in the Tibetan range of Unicode.
        @param tibetanUnicodeCP the codepoint to normalize
        @param normalizationForm NORM_NFKD or NORM_NFD if you expect
        something nontrivial to happen
        @return null if tibetanUnicodeCP is already in the chosen
        normalized form, or a string of two or three codepoints
        otherwise */
    public static String toNormalizedForm(char tibetanUnicodeCP, byte normalizationForm) {
        if (normalizationForm == NORM_NFKD
            || normalizationForm == NORM_NFD) {
            // Where not specified, the NFKD form is also the NFD form.
            switch (tibetanUnicodeCP) {
            case '\u0F0C': return ((normalizationForm == NORM_NFKD)
                                   ? "\u0F0B" : null);
            case '\u0F43': return "\u0F42\u0FB7";
            case '\u0F4D': return "\u0F4C\u0FB7";
            case '\u0F52': return "\u0F51\u0FB7";
            case '\u0F57': return "\u0F56\u0FB7";
            case '\u0F5C': return "\u0F5B\u0FB7";
            case '\u0F69': return "\u0F40\u0FB5";
            case '\u0F73': return "\u0F71\u0F72";
            case '\u0F75': return "\u0F71\u0F74";
            case '\u0F76': return "\u0FB2\u0F80";
            // I do not understand why NFD does not decompose this codepoint:
            case '\u0F77': return ((normalizationForm == NORM_NFKD)
                                   ? "\u0FB2\u0F71\u0F80" : null);
            case '\u0F78': return "\u0FB3\u0F80";
            // I do not understand why NFD does not decompose this codepoint:
            case '\u0F79': return ((normalizationForm == NORM_NFKD)
                                   ? "\u0FB3\u0F71\u0F80" : null);

            case '\u0F81': return "\u0F71\u0F80";
            case '\u0F93': return "\u0F92\u0FB7";
            case '\u0F9D': return "\u0F9C\u0FB7";
            case '\u0FA2': return "\u0FA1\u0FB7";
            case '\u0FA7': return "\u0FA6\u0FB7";
            case '\u0FAC': return "\u0FAB\u0FB7";
            case '\u0FB9': return "\u0F90\u0FB5";

            default:
                return null;
            }
        }
        return null;
    }

    /** Returns true iff tibetanUnicodeCP {@link
        #isInTibetanRange(char) is a Tibetan codepoint} and if the
        Unicode 3.2 standard discourages the use of
        tibetanUnicodeCP. */
    public static boolean isDiscouraged(char tibetanUnicodeCP) {
        return ('\u0F73' == tibetanUnicodeCP
                || '\u0F75' == tibetanUnicodeCP
                || '\u0F77' == tibetanUnicodeCP
                || '\u0F79' == tibetanUnicodeCP
                || '\u0F81' == tibetanUnicodeCP);
        /* DLC FIXME -- I was using 3.0 p.437-440, check 3.2. */
    }

    /** Returns true iff ch corresponds to the Tibetan letter ra.
        Several Unicode codepoints correspond to the Tibetan letter ra
        (in its subscribed form or otherwise).  Oftentimes,
        <code>&#92;u0F62</code> is thought of as the nominal
        representation.  Returns false for some codepoints that
        contain ra but are not merely ra, such as <code>&#92;u0F77</code> */
    public static boolean isRa(char ch) {
        return ('\u0F62' == ch
                || '\u0F6A' == ch
                || '\u0FB2' == ch
                || '\u0FBC' == ch);
    }

    /** Returns true iff ch corresponds to the Tibetan letter wa.
        Several Unicode codepoints correspond to the Tibetan letter
        wa.  Oftentimes, <code>&#92;u0F5D</code> is thought of as the
        nominal representation. */
    public static boolean isWa(char ch) {
        return ('\u0F5D' == ch
                || '\u0FAD' == ch
                || '\u0FBA' == ch);
    }

    /** Returns true iff ch corresponds to the Tibetan letter ya.
        Several Unicode codepoints correspond to the Tibetan letter
        ya.  Oftentimes, <code>&#92;u0F61</code> is thought of as the
        nominal representation. */
    public static boolean isYa(char ch) {
        return ('\u0F61' == ch
                || '\u0FB1' == ch
                || '\u0FBB' == ch);
    }

    /** Returns true iff there exists at least one codepoint cp in
        unicodeString such that cp {@link #isRa(char) is ra} or contains
        ra (like <code>&#92;u0F77</code>).  This method is not implemented
        as fast as it could be.  It calls on the canonicalization code
        in order to maximize reuse and minimize the possibility of
        coder error. */
    public static boolean containsRa(String unicodeString) {
        String canonForm = toMostlyDecomposedUnicode(unicodeString, NORM_NFKD);
        for (int i = 0; i < canonForm.length(); i++) {
            if (isRa(canonForm.charAt(i)))
                return true;
        }
        return false;
    }
    /** Inefficient shortcut.
        @see #containsRa(String) */
    public static boolean containsRa(char unicodeCP) {
        return containsRa(new String(new char[] { unicodeCP }));
    }

    /** Returns a human-readable, ASCII form of the Unicode codepoint
        ch. */
    public static String unicodeCPToString(char ch) {
        return "U+" + Integer.toHexString((int)ch);
    }
}

