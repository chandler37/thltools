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
Library (THDL). Portions created by the THDL are Copyright 2002-2003 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text.tshegbar;

import junit.framework.TestCase;

/**
 * @author David Chandler
 *
 * Tests {@link org.thdl.tib.text.tshegbar.UnicodeUtils} at the unit level.
 */
public class UnicodeUtilsTest extends TestCase implements UnicodeConstants {
	/**
	 * Plain vanilla constructor for UnicodeUtilsTest.
	 * @param arg0
	 */
	public UnicodeUtilsTest(String arg0) {
		super(arg0);
	}

    /** Invokes a text UI and runs all this class's tests. */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(UnicodeUtilsTest.class);
	}

    /** Tests Unicode Normalization form KD for Tibetan codepoints.
        See Unicode, Inc.'s NormalizationTest-3.2.0.txt.  This
        contains all test cases for
        <code>U+0F00</code>-<code>U+0FFF</code> there, and a few
        more. */
    public void testMostlyNFKD() {
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F0B", NORM_NFKD).equals("\u0F0B"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F40", NORM_NFKD).equals("\u0F40"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F90", NORM_NFKD).equals("\u0F90"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F0C", NORM_NFKD).equals("\u0F0B"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F43", NORM_NFKD).equals("\u0F42\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F42\u0FB7", NORM_NFKD).equals("\u0F42\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F4D", NORM_NFKD).equals("\u0F4C\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F4C\u0FB7", NORM_NFKD).equals("\u0F4C\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F52", NORM_NFKD).equals("\u0F51\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F51\u0FB7", NORM_NFKD).equals("\u0F51\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F57", NORM_NFKD).equals("\u0F56\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F56\u0FB7", NORM_NFKD).equals("\u0F56\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F5C", NORM_NFKD).equals("\u0F5B\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F5B\u0FB7", NORM_NFKD).equals("\u0F5B\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F69", NORM_NFKD).equals("\u0F40\u0FB5"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F40\u0FB5", NORM_NFKD).equals("\u0F40\u0FB5"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F73", NORM_NFKD).equals("\u0F71\u0F72"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F72", NORM_NFKD).equals("\u0F71\u0F72"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F75", NORM_NFKD).equals("\u0F71\u0F74"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F74", NORM_NFKD).equals("\u0F71\u0F74"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F76", NORM_NFKD).equals("\u0FB2\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F80", NORM_NFKD).equals("\u0FB2\u0F80"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F78", NORM_NFKD).equals("\u0FB3\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F80", NORM_NFKD).equals("\u0FB3\u0F80"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F81", NORM_NFKD).equals("\u0F71\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F80", NORM_NFKD).equals("\u0F71\u0F80"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F93", NORM_NFKD).equals("\u0F92\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F92\u0FB7", NORM_NFKD).equals("\u0F92\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F9D", NORM_NFKD).equals("\u0F9C\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F9C\u0FB7", NORM_NFKD).equals("\u0F9C\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA2", NORM_NFKD).equals("\u0FA1\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA1\u0FB7", NORM_NFKD).equals("\u0FA1\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA7", NORM_NFKD).equals("\u0FA6\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA6\u0FB7", NORM_NFKD).equals("\u0FA6\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FAC", NORM_NFKD).equals("\u0FAB\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FAB\u0FB7", NORM_NFKD).equals("\u0FAB\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB9", NORM_NFKD).equals("\u0F90\u0FB5"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F90\u0FB5", NORM_NFKD).equals("\u0F90\u0FB5"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F77", NORM_NFKD).equals("\u0FB2\u0F71\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F81", NORM_NFKD).equals("\u0FB2\u0F71\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F71\u0F80", NORM_NFKD).equals("\u0FB2\u0F71\u0F80"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F79", NORM_NFKD).equals("\u0FB3\u0F71\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F81", NORM_NFKD).equals("\u0FB3\u0F71\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F71\u0F80", NORM_NFKD).equals("\u0FB3\u0F71\u0F80"));
    }

    /** Tests Unicode Normalization form D for Tibetan codepoints.
        See Unicode, Inc.'s NormalizationTest-3.2.0.txt.  This
        contains all test cases for
        <code>U+0F00</code>-<code>U+0FFF</code> there, and a few
        more. */
    public void testMostlyNFD() {
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F0B", NORM_NFD).equals("\u0F0B"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F40", NORM_NFD).equals("\u0F40"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F90", NORM_NFD).equals("\u0F90"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F0C", NORM_NFD).equals("\u0F0C"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F43", NORM_NFD).equals("\u0F42\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F42\u0FB7", NORM_NFD).equals("\u0F42\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F4D", NORM_NFD).equals("\u0F4C\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F4C\u0FB7", NORM_NFD).equals("\u0F4C\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F52", NORM_NFD).equals("\u0F51\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F51\u0FB7", NORM_NFD).equals("\u0F51\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F57", NORM_NFD).equals("\u0F56\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F56\u0FB7", NORM_NFD).equals("\u0F56\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F5C", NORM_NFD).equals("\u0F5B\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F5B\u0FB7", NORM_NFD).equals("\u0F5B\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F69", NORM_NFD).equals("\u0F40\u0FB5"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F40\u0FB5", NORM_NFD).equals("\u0F40\u0FB5"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F73", NORM_NFD).equals("\u0F71\u0F72"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F72", NORM_NFD).equals("\u0F71\u0F72"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F75", NORM_NFD).equals("\u0F71\u0F74"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F74", NORM_NFD).equals("\u0F71\u0F74"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F76", NORM_NFD).equals("\u0FB2\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F80", NORM_NFD).equals("\u0FB2\u0F80"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F78", NORM_NFD).equals("\u0FB3\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F80", NORM_NFD).equals("\u0FB3\u0F80"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F81", NORM_NFD).equals("\u0F71\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F80", NORM_NFD).equals("\u0F71\u0F80"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F93", NORM_NFD).equals("\u0F92\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F92\u0FB7", NORM_NFD).equals("\u0F92\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F9D", NORM_NFD).equals("\u0F9C\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F9C\u0FB7", NORM_NFD).equals("\u0F9C\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA2", NORM_NFD).equals("\u0FA1\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA1\u0FB7", NORM_NFD).equals("\u0FA1\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA7", NORM_NFD).equals("\u0FA6\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA6\u0FB7", NORM_NFD).equals("\u0FA6\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FAC", NORM_NFD).equals("\u0FAB\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FAB\u0FB7", NORM_NFD).equals("\u0FAB\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB9", NORM_NFD).equals("\u0F90\u0FB5"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F90\u0FB5", NORM_NFD).equals("\u0F90\u0FB5"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F77", NORM_NFD).equals("\u0F77"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F81", NORM_NFD).equals("\u0FB2\u0F71\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F71\u0F80", NORM_NFD).equals("\u0FB2\u0F71\u0F80"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F79", NORM_NFD).equals("\u0F79"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F81", NORM_NFD).equals("\u0FB3\u0F71\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F71\u0F80", NORM_NFD).equals("\u0FB3\u0F71\u0F80"));
    }

    /** Tests Unicode Normalization form THDL for Tibetan codepoints.
        See Unicode, Inc.'s NormalizationTest-3.2.0.txt.  This
        contains all test cases for
        <code>U+0F00</code>-<code>U+0FFF</code> there, and a few
        more. */
    public void testMostlyNFTHDL() {
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F0B", NORM_NFTHDL).equals("\u0F0B"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F40", NORM_NFTHDL).equals("\u0F40"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F90", NORM_NFTHDL).equals("\u0F90"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F0C", NORM_NFTHDL).equals("\u0F0C"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F43", NORM_NFTHDL).equals("\u0F42\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F42\u0FB7", NORM_NFTHDL).equals("\u0F42\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F4D", NORM_NFTHDL).equals("\u0F4C\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F4C\u0FB7", NORM_NFTHDL).equals("\u0F4C\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F52", NORM_NFTHDL).equals("\u0F51\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F51\u0FB7", NORM_NFTHDL).equals("\u0F51\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F57", NORM_NFTHDL).equals("\u0F56\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F56\u0FB7", NORM_NFTHDL).equals("\u0F56\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F5C", NORM_NFTHDL).equals("\u0F5B\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F5B\u0FB7", NORM_NFTHDL).equals("\u0F5B\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F69", NORM_NFTHDL).equals("\u0F40\u0FB5"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F40\u0FB5", NORM_NFTHDL).equals("\u0F40\u0FB5"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F73", NORM_NFTHDL).equals("\u0F71\u0F72"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F72", NORM_NFTHDL).equals("\u0F71\u0F72"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F75", NORM_NFTHDL).equals("\u0F71\u0F74"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F74", NORM_NFTHDL).equals("\u0F71\u0F74"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F76", NORM_NFTHDL).equals("\u0FB2\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F80", NORM_NFTHDL).equals("\u0FB2\u0F80"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F78", NORM_NFTHDL).equals("\u0FB3\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F80", NORM_NFTHDL).equals("\u0FB3\u0F80"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F81", NORM_NFTHDL).equals("\u0F71\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F80", NORM_NFTHDL).equals("\u0F71\u0F80"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F93", NORM_NFTHDL).equals("\u0F92\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F92\u0FB7", NORM_NFTHDL).equals("\u0F92\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F9D", NORM_NFTHDL).equals("\u0F9C\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F9C\u0FB7", NORM_NFTHDL).equals("\u0F9C\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA2", NORM_NFTHDL).equals("\u0FA1\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA1\u0FB7", NORM_NFTHDL).equals("\u0FA1\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA7", NORM_NFTHDL).equals("\u0FA6\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA6\u0FB7", NORM_NFTHDL).equals("\u0FA6\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FAC", NORM_NFTHDL).equals("\u0FAB\u0FB7"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FAB\u0FB7", NORM_NFTHDL).equals("\u0FAB\u0FB7"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB9", NORM_NFTHDL).equals("\u0F90\u0FB5"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F90\u0FB5", NORM_NFTHDL).equals("\u0F90\u0FB5"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F77", NORM_NFTHDL).equals("\u0FB2\u0F71\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F81", NORM_NFTHDL).equals("\u0FB2\u0F71\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F71\u0F80", NORM_NFTHDL).equals("\u0FB2\u0F71\u0F80"));

        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0F79", NORM_NFTHDL).equals("\u0FB3\u0F71\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F81", NORM_NFTHDL).equals("\u0FB3\u0F71\u0F80"));
        assertTrue(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F71\u0F80", NORM_NFTHDL).equals("\u0FB3\u0F71\u0F80"));
    }

    /** Tests the containsRa method. */
    public void testContainsRa() {
        assertTrue(UnicodeUtils.containsRa('\u0FB2'));
        assertTrue(UnicodeUtils.containsRa('\u0F77'));
        assertTrue(UnicodeUtils.containsRa('\u0F76'));
        assertTrue(UnicodeUtils.containsRa('\u0F6A'));
        assertTrue(UnicodeUtils.containsRa('\u0F62'));
        assertTrue(UnicodeUtils.containsRa('\u0FBC'));
    }
}
