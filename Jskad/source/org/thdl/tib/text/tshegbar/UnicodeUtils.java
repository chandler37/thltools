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
 *  dealing with Tibetan Unicode characters and strings of such
 *  characters.</p>
 *
 *  @author David Chandler */
public class UnicodeUtils {
    /** Do not use this, as this class is not instantiable. */
    private UnicodeUtils() { super(); }

    /** Returns true iff x is a Unicode character that represents a
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

    /** Returns true iff x is a Unicode character that represents a
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
        two-character sequences, but you should be aware of them
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

    /** Returns true iff unicodeChar is a character from the Unicode
        range U+0F00-U+0FFF.
        @see #isEntirelyTibetanUnicode(String) */
    public static boolean isInTibetanRange(char unicodeChar) {
        return (unicodeChar >= '\u0F00' && unicodeChar <= '\u0FFF');
    }

    /** Returns true iff unicodeString consists only of characters
        from the Unicode range U+0F00-U+0FFF.  (Note that these
        characters are typically not enough to represent a Tibetan
        text, you may need ZWSP (zero-width space) and various
        whitespace from other ranges.) */
    public static boolean isEntirelyTibetanUnicode(String unicodeString) {
        for (int i = 0; i < unicodeString.length(); i++) {
            if (!isInTibetanRange(unicodeString.charAt(i)))
                return false;
        }
        return true;
    }

    /** Modifies tibetanUnicode so that it is equivalent, according to
        the Unicode 3.2 standard, to the input buffer.  The Tibetan
        passages of the returned string are in THDL-canonical form,
        however.  This form uses a maximum of characters, in general,
        and never uses characters whose use has been {@link
        #isDiscouraged(char) discouraged}.  If the input contains
        characters for which {@link #isInTibetanRange(char)} is not
        true, then they will not be modified.
    
        <p>Note well that only well-formed input guarantees
        well-formed output.</p> */
    public static void toCanonicalForm(StringBuffer tibetanUnicode) {
        int offset = 0;
        while (offset < tibetanUnicode.length()) {
            String s = toCanonicalForm(tibetanUnicode.charAt(offset));
            if (null == s) {
                ++offset;
            } else {
                // modify tibetanUnicode and update offset.
                tibetanUnicode.deleteCharAt(offset);
                tibetanUnicode.insert(offset, s);
            }
        }
    }

    /** Like {@link #toCanonicalForm(StringBuffer)}, but does not
        modify its input.  Instead, it returns the canonically-formed
        version of tibetanUnicode. */
    public static String toCanonicalForm(String tibetanUnicode) {
        StringBuffer sb = new StringBuffer(tibetanUnicode);
        toCanonicalForm(sb);
        return sb.toString();
    }

    /** There are 19 characters in the Tibetan range of Unicode 3.2
        which can be decomposed into longer strings of characters in
        the Tibetan range of Unicode.  These 19 are said not to be in
        THDL-canonical form.  This routine returns the canonical form
        for such characters, and returns null for characters that are
        already canonical or are not in the Tibetan range of Unicode.
        @param tibetanUnicodeChar the character to canonicalize
        @return null if tibetanUnicodeChar is canonical, or a string
        of two or three characters otherwise */
    public static String toCanonicalForm(char tibetanUnicodeChar) {
        switch (tibetanUnicodeChar) {
        case '\u0F43': return new String(new char[] { '\u0F42', '\u0FB7' });
        case '\u0F4D': return new String(new char[] { '\u0F4C', '\u0FB7' });
        case '\u0F52': return new String(new char[] { '\u0F51', '\u0FB7' });
        case '\u0F57': return new String(new char[] { '\u0F56', '\u0FB7' });
        case '\u0F5C': return new String(new char[] { '\u0F5B', '\u0FB7' });
        case '\u0F69': return new String(new char[] { '\u0F40', '\u0FB5' });
        case '\u0F73': return new String(new char[] { '\u0F71', '\u0F72' });
        case '\u0F75': return new String(new char[] { '\u0F71', '\u0F74' });
        case '\u0F76': return new String(new char[] { '\u0FB2', '\u0F80' });
        case '\u0F77': return new String(new char[] { '\u0FB2', '\u0F71', '\u0F80' });
        case '\u0F78': return new String(new char[] { '\u0FB3', '\u0F80' });
        case '\u0F79': return new String(new char[] { '\u0FB3', '\u0F71', '\u0F80' });
        case '\u0F81': return new String(new char[] { '\u0F71', '\u0F80' });
        case '\u0F93': return new String(new char[] { '\u0F92', '\u0FB7' });
        case '\u0F9D': return new String(new char[] { '\u0F9C', '\u0FB7' });
        case '\u0FA2': return new String(new char[] { '\u0FA1', '\u0FB7' });
        case '\u0FA7': return new String(new char[] { '\u0FA6', '\u0FB7' });
        case '\u0FAC': return new String(new char[] { '\u0FAB', '\u0FB7' });
        case '\u0FB9': return new String(new char[] { '\u0F90', '\u0FB5' });

        default:
            return null;
        }
    }

    /** Returns true iff tibetanUnicodeChar {@link
        #isInTibetanRange(char)} and if the Unicode 3.2 standard
        discourages the use of tibetanUnicodeChar. */
    public static boolean isDiscouraged(char tibetanUnicodeChar) {
        return ('\u0F73' == tibetanUnicodeChar
                || '\u0F75' == tibetanUnicodeChar
                || '\u0F77' == tibetanUnicodeChar
                || '\u0F81' == tibetanUnicodeChar);
        /* DLC FIXME -- I was using 3.0 p.437-440, check 3.2. */
    }

    /** Returns true iff ch corresponds to the Tibetan letter ra.
        Several Unicode characters correspond to the Tibetan letter ra
        (in its subscribed form or otherwise).  Oftentimes,
        <code>&#92;u0F62</code> is thought of as the nominal
        representation.  Returns false for some characters that
        contain ra but are not merely ra, such as <code>&#92;u0F77</code> */
    public static boolean isRa(char ch) {
        return ('\u0F62' == ch
                || '\u0F6A' == ch
                || '\u0FB2' == ch
                || '\u0FBC' == ch);
    }

    /** Returns true iff ch corresponds to the Tibetan letter wa.
        Several Unicode characters correspond to the Tibetan letter
        wa.  Oftentimes, <code>&#92;u0F5D</code> is thought of as the
        nominal representation. */
    public static boolean isWa(char ch) {
        return ('\u0F5D' == ch
                || '\u0FAD' == ch
                || '\u0FBA' == ch);
    }

    /** Returns true iff ch corresponds to the Tibetan letter ya.
        Several Unicode characters correspond to the Tibetan letter
        ya.  Oftentimes, <code>&#92;u0F61</code> is thought of as the
        nominal representation. */
    public static boolean isYa(char ch) {
        return ('\u0F61' == ch
                || '\u0FB1' == ch
                || '\u0FBB' == ch);
    }

    /** Returns true iff there exists at least one character ch in
        unicodeString such that ch {@link #isRa(char) is ra} or contains
        ra (like <code>&#92;u0F77</code>).  This method is not implemented
        as fast as it could be.  It calls on the canonicalization code
        in order to maximize reuse and minimize the possibility of
        coder error. */
    public static boolean containsRa(String unicodeString) {
        String canonForm = toCanonicalForm(unicodeString);
        for (int i = 0; i < canonForm.length(); i++) {
            if (isRa(canonForm.charAt(i)))
                return true;
        }
        return false;
    }
    /** Inefficient shortcut.
        @see #containsRa(String) */
    public static boolean containsRa(char unicodeChar) {
        return containsRa(new String(new char[] { unicodeChar }));
    }

    public static String unicodeCharToString(char ch) {
        return "U+" + Integer.toHexString((int)ch);
    }
}

