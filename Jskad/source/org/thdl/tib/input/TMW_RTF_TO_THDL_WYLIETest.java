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

package org.thdl.tib.input;

import junit.framework.TestCase;

import java.io.File;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import org.thdl.util.ThdlOptions;
import org.thdl.util.javaxdelta.Delta;

/**
 * @author David Chandler
 *
 * Tests {@link org.thdl.tib.input.TMW_RTF_TO_THDL_WYLIE} at the unit level.
 */
public class TMW_RTF_TO_THDL_WYLIETest extends TestCase {
	/**
	 * Plain vanilla constructor for TMW_RTF_TO_THDL_WYLIETest.
	 * @param arg0
	 */
	public TMW_RTF_TO_THDL_WYLIETest(String arg0) {
		super(arg0);
	}

    protected void setUp() {
        // We don't want to use options.txt:
        ThdlOptions.forTestingOnlyInitializeWithoutDefaultOptionsFile();
        // We do want debugging assertions:
        ThdlOptions.setUserPreference("thdl.debug", true);
    }


    /** Invokes a text UI and runs all this class's tests. */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TMW_RTF_TO_THDL_WYLIETest.class);
	}

    private static void testActualAndExpected(String testName) {
        // Now ensure that the expected result and the actual result
        // coincide.
        int rc;
        String actualFile
            = "bin" + File.separator
            + "for-junit" + File.separator
            + "TMW_RTF_TO_THDL_WYLIE" + testName + ".out";
        String expectedFile
            = "source" + File.separator
            + "org" + File.separator
            + "thdl" + File.separator
            + "tib" + File.separator
            + "input" + File.separator
            + "TMW_RTF_TO_THDL_WYLIE" + testName + ".expected";
        assertTrue(new File(actualFile).exists());
        assertTrue(new File(expectedFile).exists());
        rc = Delta.areFilesDifferent(actualFile, expectedFile);
        if (0 != rc) System.out.println("DLC NOW: rc is " + rc);
        assertTrue(0 == rc);
    }

    /** Tests the --find-some-non-tmw mode of {@link
     *  org.thdl.tib.input.TMW_RTF_TO_THDL_WYLIE}. */
    public void testFindSomeNonTMWMode() {
        String[] args = new String[] {
            "--find-some-non-tmw",
            "source" + File.separator
            + "org" + File.separator
            + "thdl" + File.separator
            + "tib" + File.separator
            + "input" + File.separator
            + "TMW_RTF_TO_THDL_WYLIETest1.rtf"
        };
        boolean fileNotFound = false;
        try {
            int rc = TMW_RTF_TO_THDL_WYLIE.realMain(args, new PrintStream(new FileOutputStream("bin/for-junit/TMW_RTF_TO_THDL_WYLIETest1ResultFindSome.out")));
            assertTrue(rc == 1);
        } catch (FileNotFoundException e) {
            fileNotFound = true;
        }
        assertTrue(!fileNotFound);

        testActualAndExpected("Test1ResultFindSome");
    }

    /** Tests the --find-all-non-tmw mode of {@link
     *  org.thdl.tib.input.TMW_RTF_TO_THDL_WYLIE}. */
    public void testFindAllNonTMWMode() {
        String[] args = new String[] {
            "--find-all-non-tmw",
            "source" + File.separator
            + "org" + File.separator
            + "thdl" + File.separator
            + "tib" + File.separator
            + "input" + File.separator
            + "TMW_RTF_TO_THDL_WYLIETest1.rtf"
        };
        boolean fileNotFound = false;
        try {
            int rc = TMW_RTF_TO_THDL_WYLIE.realMain(args, new PrintStream(new FileOutputStream("bin/for-junit/TMW_RTF_TO_THDL_WYLIETest1ResultFindAll.out")));
            assertTrue(rc == 1);
        } catch (FileNotFoundException e) {
            fileNotFound = true;
        }
        assertTrue(!fileNotFound);

        testActualAndExpected("Test1ResultFindAll");
    }

    /** Tests the converter mode of {@link
     *  org.thdl.tib.input.TMW_RTF_TO_THDL_WYLIE}. */
    public void testConverterMode() {
        String[] args = new String[] {
            "source" + File.separator
            + "org" + File.separator
            + "thdl" + File.separator
            + "tib" + File.separator
            + "input" + File.separator
            + "TMW_RTF_TO_THDL_WYLIETest1.rtf"
        };
        boolean fileNotFound = false;
        try {
            int rc = TMW_RTF_TO_THDL_WYLIE.realMain(args, new PrintStream(new FileOutputStream("bin/for-junit/TMW_RTF_TO_THDL_WYLIETest1ResultConversion.out")));
            assertTrue(rc == 0);
        } catch (FileNotFoundException e) {
            fileNotFound = true;
        }
        assertTrue(!fileNotFound);

        testActualAndExpected("Test1ResultConversion");
    }
}
