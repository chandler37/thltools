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
Library (THDL). Portions created by the THDL are Copyright 2001-2003 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text.tshegbar;

/** Constants and static routines (DLC still?) useful in writing state
 *  machines for transforming Unicode input into other forms.
 *
 *  @author David Chandler
 */
interface UnicodeReadingStateMachineConstants {

    /** Returns the codepoint class for cp, e.g. {@link #CC_SJC}.
     *  @param cp a Unicode codepoint, which MUST be nondecomposable
     *  if it is in the Tibetan range but can be from outside the
     *  Tibetan range of Unicode */
    static int getCCForCP(char cp) {
        assert(getNFTHDL(cp) == null);
        if ('\u0F82' == cp) {
            return CC_0F82;
        } else if ('\u0F8A' == cp) {
            return CC_0F8A;
        } else if ('\u0F39' == cp) {
            return CC_0F39;
        } else if ('\u0F71' == cp) {
            return CC_ACHUNG;
        } else if ('\u0F40' <= cp && cp <= '\u0F6A') {
            assert(cp != '\u0F48');
            return CC_CON;
        } else if ('\u0F90' <= cp && cp <= '\u0FBC') {
            assert(cp != '\u0F98');
            return CC_SJC;
        } else if ('\u0F20' <= cp && cp <= '\u0F33') {
            return CC_DIGIT;
        } else if (/* DLC NOW do these combine ONLY with digits, or do CC_CM just NOT combine with digits? */
                   '\u0F3E' == cp
                   || '\u0F3F' == cp
                   || '\u0F18' == cp
                   || '\u0F19' == cp) {
            return CC_MCWD;
        } else if ('\u0FC6' == cp
                   || '\u0F87' == cp
                   || '\u0F86' == cp
                   || '\u0F84' == cp
                   || '\u0F83' == cp
                   || '\u0F82' == cp
                   || '\u0F7F' == cp
                   || '\u0F7E' == cp
                   || '\u0F37' == cp /* DLC NOW NORMALIZATION OF 0F10, 11 to 0F0F ??? */
                   || '\u0F35' == cp) {
            return CC_CM;
        } else if ('\u0F72' == cp
                   || '\u0F74' == cp
                   || '\u0F7A' == cp
                   || '\u0F7B' == cp
                   || '\u0F7C' == cp
                   || '\u0F7D' == cp
                   || '\u0F80' == cp) {
            // DLC what about U+0F84 ??? CC_V or CC_CM ?
            return CC_V;
        } else {
            return CC_SIN;
        }
    }

    // codepoint classes (CC_...) follow.  These are mutually
    // exclusive, and their union is the whole of Unicode.

    /** for everything else, i.e. non-Tibetan characters like U+0E00
     *  and also Tibetan characters like U+0FCF and U+0F05 (DLC rename
     *  SIN[GLETON] to OTHER as combining marks from outside the
     *  Tibetan range count as this) but not U+0F8A */
    static final int CC_SIN = 0;

    /** for combining marks in the Tibetan range of Unicode that
     *  combine with digits alone */
    static final int CC_MCWD = 1;

    /** for combining marks in the Tibetan range of Unicode, minus
     *  CC_MCWD, U+0F82, and U+0F39 */
    static final int CC_CM = 2;

    /** for combining consonants, i.e. U+0F90-U+0FBC minus U+0F98
     *  minus the decomposable entries like U+0F93, U+0F9D, U+0FA2,
     *  etc. */
    static final int CC_SJC = 3;

    /** for noncombining consonants, i.e. U+0F40-U+0F6A minus U+0F48
     *  minus the decomposable entries like U+0F43, U+0F4D, U+0F52,
     *  etc. */
    static final int CC_CON = 4;

    /** for simple, nondecomposable vowels, i.e. U+0F72, U+0F74,
     *  U+0F7A, U+0F7B, U+0F7C, U+0F7D, U+0F80 */
    static final int CC_V = 5;

    /** for U+0F8A */
    static final int CC_0F8A = 6;

    /** for U+0F82, which is treated like {@link #CC_CM} except after
     *  U+0F8A */
    static final int CC_0F82 = 7;

    /** for U+0F39, an integral part of a consonant when it directly
     *  follows a member of CM_CONS or CM_SJC */
    static final int CC_0F39 = 8;

    /** for U+0F71 */
    static final int CC_ACHUNG = 9;

    /** for digits, i.e. U+0F20-U+0F33 */
    static final int CC_DIGIT = 10;



    // states STATE_...:

    /** initial state */
    static final int STATE_START = 0;

    /** ready state, i.e. the state in which some non-empty Unicode
     *  String is in the holding area, <i>ready</i> to receive
     *  combining marks like U+0F35 */
    static final int STATE_READY = 1;

    /** digit state, i.e. the state in which some non-empty Unicode
     *  String consisting entirely of digits is in the holding area,
     *  ready to receive marks that combine only with digits */
    static final int STATE_DIGIT = 2;

    /** state in which CC_SJC are welcomed and treated as consonants
     *  to be subscribed to the GraphemeCluster in holding. */
    static final int STATE_STACKING = 3;

    /** state in which one or more consonants have been seen and also
     *  an achung (U+0F71) has been seen */
    static final int STATE_STACKPLUSACHUNG = 4;

    /** state that seeing U+0F8A (when that's not an error) puts you
     *  in.  Needed because U+0F8A is always followed by U+0F82, and
     *  we check for the exceptional case that U+0F8A is followed by
     *  something else. */
    static final int STATE_PARTIALMARK = 5;

    /* DLC we should have many error states or none. */


    /** the present codepoint marks the start of a new
     *  GraphemeCluster */
    static final int ACTION_BEGINS_NEW_GRAPHEME_CLUSTER = 0;
    /** the present codepoint is a continuation of the current
     *  GraphemeCluster */
    static final int ACTION_CONTINUES_GRAPHEME_CLUSTER = 1;
    /** there is an error in the input stream, which we are correcting
     *  (as we are in error-correcting mode) by starting a new
     *  GraphemeCluster with U+0F68 as the first codepoint and the
     *  current codepoint as the second */
    static final int ACTION_PREPEND_WITH_0F68 = 2;
}
