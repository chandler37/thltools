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

import org.thdl.util.ThdlDebug;
import org.thdl.util.ThdlLazyException;

/** DLC FIXMEDOC and turn into junit tests.
 *
 *  @author David Chandler
 */
public class test implements UnicodeConstants {
    /** Tests Unicode Normalization form KD for Tibetan codepoints.
        See Unicode, Inc.'s NormalizationTest-3.2.0.txt.  This
        contains all test cases for
        <code>U+0F00</code>-<code>U+0FFF</code> there, and a few
        more. */
    public void testMostlyNFKD() {
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F0B", NORM_NFD).equals("\u0F0B"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F40", NORM_NFD).equals("\u0F40"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F90", NORM_NFD).equals("\u0F90"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F0C", NORM_NFKD).equals("\u0F0B"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F43", NORM_NFKD).equals("\u0F42\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F42\u0FB7", NORM_NFKD).equals("\u0F42\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F4D", NORM_NFKD).equals("\u0F4C\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F4C\u0FB7", NORM_NFKD).equals("\u0F4C\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F52", NORM_NFKD).equals("\u0F51\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F51\u0FB7", NORM_NFKD).equals("\u0F51\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F57", NORM_NFKD).equals("\u0F56\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F56\u0FB7", NORM_NFKD).equals("\u0F56\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F5C", NORM_NFKD).equals("\u0F5B\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F5B\u0FB7", NORM_NFKD).equals("\u0F5B\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F69", NORM_NFKD).equals("\u0F40\u0FB5"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F40\u0FB5", NORM_NFKD).equals("\u0F40\u0FB5"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F73", NORM_NFKD).equals("\u0F71\u0F72"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F72", NORM_NFKD).equals("\u0F71\u0F72"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F75", NORM_NFKD).equals("\u0F71\u0F74"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F74", NORM_NFKD).equals("\u0F71\u0F74"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F76", NORM_NFKD).equals("\u0FB2\u0F80"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F80", NORM_NFKD).equals("\u0FB2\u0F80"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F78", NORM_NFKD).equals("\u0FB3\u0F80"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F80", NORM_NFKD).equals("\u0FB3\u0F80"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F81", NORM_NFKD).equals("\u0F71\u0F80"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F80", NORM_NFKD).equals("\u0F71\u0F80"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F93", NORM_NFKD).equals("\u0F92\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F92\u0FB7", NORM_NFKD).equals("\u0F92\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F9D", NORM_NFKD).equals("\u0F9C\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F9C\u0FB7", NORM_NFKD).equals("\u0F9C\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA2", NORM_NFKD).equals("\u0FA1\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA1\u0FB7", NORM_NFKD).equals("\u0FA1\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA7", NORM_NFKD).equals("\u0FA6\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA6\u0FB7", NORM_NFKD).equals("\u0FA6\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FAC", NORM_NFKD).equals("\u0FAB\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FAB\u0FB7", NORM_NFKD).equals("\u0FAB\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB9", NORM_NFKD).equals("\u0F90\u0FB5"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F90\u0FB5", NORM_NFKD).equals("\u0F90\u0FB5"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F77", NORM_NFKD).equals("\u0FB2\u0F71\u0F80"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F81", NORM_NFKD).equals("\u0FB2\u0F71\u0F80"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F71\u0F80", NORM_NFKD).equals("\u0FB2\u0F71\u0F80"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F79", NORM_NFKD).equals("\u0FB3\u0F71\u0F80"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F81", NORM_NFKD).equals("\u0FB3\u0F71\u0F80"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F71\u0F80", NORM_NFKD).equals("\u0FB3\u0F71\u0F80"));
    }

    /** Tests Unicode Normalization form D for Tibetan codepoints.
        See Unicode, Inc.'s NormalizationTest-3.2.0.txt.  This
        contains all test cases for
        <code>U+0F00</code>-<code>U+0FFF</code> there, and a few
        more. */
    public void testMostlyNFD() {
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F0B", NORM_NFD).equals("\u0F0B"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F40", NORM_NFD).equals("\u0F40"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F90", NORM_NFD).equals("\u0F90"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F0C", NORM_NFD).equals("\u0F0C"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F43", NORM_NFD).equals("\u0F42\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F42\u0FB7", NORM_NFD).equals("\u0F42\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F4D", NORM_NFD).equals("\u0F4C\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F4C\u0FB7", NORM_NFD).equals("\u0F4C\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F52", NORM_NFD).equals("\u0F51\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F51\u0FB7", NORM_NFD).equals("\u0F51\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F57", NORM_NFD).equals("\u0F56\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F56\u0FB7", NORM_NFD).equals("\u0F56\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F5C", NORM_NFD).equals("\u0F5B\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F5B\u0FB7", NORM_NFD).equals("\u0F5B\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F69", NORM_NFD).equals("\u0F40\u0FB5"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F40\u0FB5", NORM_NFD).equals("\u0F40\u0FB5"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F73", NORM_NFD).equals("\u0F71\u0F72"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F72", NORM_NFD).equals("\u0F71\u0F72"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F75", NORM_NFD).equals("\u0F71\u0F74"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F74", NORM_NFD).equals("\u0F71\u0F74"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F76", NORM_NFD).equals("\u0FB2\u0F80"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F80", NORM_NFD).equals("\u0FB2\u0F80"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F78", NORM_NFD).equals("\u0FB3\u0F80"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F80", NORM_NFD).equals("\u0FB3\u0F80"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F81", NORM_NFD).equals("\u0F71\u0F80"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F80", NORM_NFD).equals("\u0F71\u0F80"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F93", NORM_NFD).equals("\u0F92\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F92\u0FB7", NORM_NFD).equals("\u0F92\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F9D", NORM_NFD).equals("\u0F9C\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F9C\u0FB7", NORM_NFD).equals("\u0F9C\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA2", NORM_NFD).equals("\u0FA1\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA1\u0FB7", NORM_NFD).equals("\u0FA1\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA7", NORM_NFD).equals("\u0FA6\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA6\u0FB7", NORM_NFD).equals("\u0FA6\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FAC", NORM_NFD).equals("\u0FAB\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FAB\u0FB7", NORM_NFD).equals("\u0FAB\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB9", NORM_NFD).equals("\u0F90\u0FB5"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F90\u0FB5", NORM_NFD).equals("\u0F90\u0FB5"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F77", NORM_NFD).equals("\u0F77"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F81", NORM_NFD).equals("\u0FB2\u0F71\u0F80"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F71\u0F80", NORM_NFD).equals("\u0FB2\u0F71\u0F80"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F79", NORM_NFD).equals("\u0F79"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F81", NORM_NFD).equals("\u0FB3\u0F71\u0F80"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F71\u0F80", NORM_NFD).equals("\u0FB3\u0F71\u0F80"));
    }

    /** Tests Unicode Normalization form THDL for Tibetan codepoints.
        See Unicode, Inc.'s NormalizationTest-3.2.0.txt.  This
        contains all test cases for
        <code>U+0F00</code>-<code>U+0FFF</code> there, and a few
        more. */
    public void testMostlyNFTHDL() {
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F0B", NORM_NFTHDL).equals("\u0F0B"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F40", NORM_NFTHDL).equals("\u0F40"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F90", NORM_NFTHDL).equals("\u0F90"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F0C", NORM_NFTHDL).equals("\u0F0C"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F43", NORM_NFTHDL).equals("\u0F42\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F42\u0FB7", NORM_NFTHDL).equals("\u0F42\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F4D", NORM_NFTHDL).equals("\u0F4C\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F4C\u0FB7", NORM_NFTHDL).equals("\u0F4C\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F52", NORM_NFTHDL).equals("\u0F51\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F51\u0FB7", NORM_NFTHDL).equals("\u0F51\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F57", NORM_NFTHDL).equals("\u0F56\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F56\u0FB7", NORM_NFTHDL).equals("\u0F56\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F5C", NORM_NFTHDL).equals("\u0F5B\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F5B\u0FB7", NORM_NFTHDL).equals("\u0F5B\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F69", NORM_NFTHDL).equals("\u0F40\u0FB5"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F40\u0FB5", NORM_NFTHDL).equals("\u0F40\u0FB5"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F73", NORM_NFTHDL).equals("\u0F71\u0F72"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F72", NORM_NFTHDL).equals("\u0F71\u0F72"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F75", NORM_NFTHDL).equals("\u0F71\u0F74"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F74", NORM_NFTHDL).equals("\u0F71\u0F74"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F76", NORM_NFTHDL).equals("\u0FB2\u0F80"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F80", NORM_NFTHDL).equals("\u0FB2\u0F80"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F78", NORM_NFTHDL).equals("\u0FB3\u0F80"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F80", NORM_NFTHDL).equals("\u0FB3\u0F80"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F81", NORM_NFTHDL).equals("\u0F71\u0F80"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F71\u0F80", NORM_NFTHDL).equals("\u0F71\u0F80"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F93", NORM_NFTHDL).equals("\u0F92\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F92\u0FB7", NORM_NFTHDL).equals("\u0F92\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F9D", NORM_NFTHDL).equals("\u0F9C\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F9C\u0FB7", NORM_NFTHDL).equals("\u0F9C\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA2", NORM_NFTHDL).equals("\u0FA1\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA1\u0FB7", NORM_NFTHDL).equals("\u0FA1\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA7", NORM_NFTHDL).equals("\u0FA6\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FA6\u0FB7", NORM_NFTHDL).equals("\u0FA6\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FAC", NORM_NFTHDL).equals("\u0FAB\u0FB7"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FAB\u0FB7", NORM_NFTHDL).equals("\u0FAB\u0FB7"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB9", NORM_NFTHDL).equals("\u0F90\u0FB5"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F90\u0FB5", NORM_NFTHDL).equals("\u0F90\u0FB5"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F77", NORM_NFTHDL).equals("\u0FB2\u0F71\u0F80"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F81", NORM_NFTHDL).equals("\u0FB2\u0F71\u0F80"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB2\u0F71\u0F80", NORM_NFTHDL).equals("\u0FB2\u0F71\u0F80"));

        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0F79", NORM_NFTHDL).equals("\u0FB3\u0F71\u0F80"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F81", NORM_NFTHDL).equals("\u0FB3\u0F71\u0F80"));
        ThdlDebug.verify(UnicodeUtils.toMostlyDecomposedUnicode("\u0FB3\u0F71\u0F80", NORM_NFTHDL).equals("\u0FB3\u0F71\u0F80"));
    }

    public void testTopToBottomForLegalGraphemeClusters() {
        // DLC delete:  System.out.println("see this " + UnicodeUtils.unicodeStringToString(new UnicodeGraphemeCluster("\u0F00").getTopToBottomCodepoints()));
        ThdlDebug.verify(new UnicodeGraphemeCluster("\u0F00").getTopToBottomCodepoints().equals("\u0F7E\u0F7C\u0F68"));
        ThdlDebug.verify(new UnicodeGraphemeCluster("\u0F66\u0F93\u0F91\u0FA7\u0F92\u0FAD\u0F77\u0F83\u0F86\u0F84").getTopToBottomCodepoints().equals("\u0F86\u0F83\u0F80\u0F66\u0F92\u0FB7\u0F91\u0FA6\u0FB7\u0F92\u0FB2\u0FAD\u0F71\u0F84"));
    }

    /** tests this package */
    public test() throws Throwable {
        super();

        testMostlyNFKD();
        testMostlyNFD();
        testMostlyNFTHDL();

        testTopToBottomForLegalGraphemeClusters();


        ThdlDebug.verify(UnicodeUtils.containsRa('\u0FB2'));
        ThdlDebug.verify(UnicodeUtils.containsRa('\u0F77'));
        ThdlDebug.verify(UnicodeUtils.containsRa('\u0F76'));
        ThdlDebug.verify(UnicodeUtils.containsRa('\u0F6A'));
        ThdlDebug.verify(UnicodeUtils.containsRa('\u0F62'));
        ThdlDebug.verify(UnicodeUtils.containsRa('\u0FBC'));

        String ew
            = new LegalTshegBar(EWC_ba, EWC_sa, EWC_ga,
                                EWSUB_ra_btags, false, true,
                                EWC_la, EWC_sa, EWV_o).getThdlWylie().toString();
        System.out.println("DLC: t-b 1: " + ew);
        ThdlDebug.verify(ew.equals("bsgrAols"));
        ThdlDebug.verify(ew.equals("bsgrAols"));
        ThdlDebug.verify(new LegalTshegBar(EWC_ba, EWC_sa, EWC_ga,
                                           EWSUB_ra_btags, true, true,
                                           EWC_la, EWC_sa, EWV_o).getThdlWylie().toString().equals("bsgrwAols"));
        ThdlDebug.verify(new LegalTshegBar(EWC_ba, EWC_sa, EWC_ga,
                                           EWSUB_ra_btags, false, false,
                                           EWC_la, EWC_sa, EWV_o).getThdlWylie().toString().equals("bsgrols"));

        // Ensure that EWTS's jskad is not legal:
        ThdlDebug.verify(!LegalTshegBar.formsLegalTshegBar(EWC_ja, EWC_sa,
                                                           EWC_ka, EW_ABSENT,
                                                           false, false,
                                                           EW_ABSENT, EWC_da,
                                                           EW_ABSENT));
    }

    /** Unit tests this package. */
    public static void main(String argv[]) {
        try {
            System.out.println("yo");
            new test();
        } catch (ThdlLazyException ex) {
            System.out.println("Oopsy!");
            ex.getRealException().printStackTrace();
        } catch (Throwable t) {
            System.out.println("Oops!");
            System.out.println(t);
            System.out.println("at:");
            t.printStackTrace();
        }
    }
}
