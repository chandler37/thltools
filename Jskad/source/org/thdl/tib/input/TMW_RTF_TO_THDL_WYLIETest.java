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
import org.apache.commons.jrcs.tools.JDiff;
import org.apache.commons.jrcs.diff.Revision;

/**
 * @author David Chandler
 *
 * Tests {@link org.thdl.tib.input.TibetanConverter} at the unit level.
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

        // Runs on Linux/Unix boxes without X11 servers:
        System.setProperty("java.awt.headless", "true");
        // FIXME: ant check still fails because it doesn't see the
        // above property early enough.

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
        Revision rev = JDiff.getDiff(expectedFile, actualFile);
        assertTrue(null != rev);
        String lineSep = System.getProperty("line.separator");
        boolean foundExpectedDiff = false;
        String expectedDiff
            = ("3c3" + lineSep
               + "< {\\stylesheet{\\s1\\li0\\ri0\\fi0\\ql\\sbasedon2\\snext1 Body Text;}{\\s2 default;}}\n"
               + "---" + lineSep
               + "> {\\stylesheet{\\s2 default;}{\\s1\\li0\\ri0\\fi0\\ql\\sbasedon2\\snext1 Body Text;}}\n");
        if (0 != rev.size()
            && !(foundExpectedDiff = expectedDiff.equals(rev.toString()))) {
            System.out.println("Oops! the diff is this:");
            System.out.print(rev.toString());
            assertTrue(false);
        }
    }


    private void helper(String testName, String mode, String extension, int erc) {
        String[] args = new String[] {
            "--colors",
            "no",
            "--warning-level",
            "All",
            mode,
            getTestFileName(testName)
        };
        boolean fileNotFound = false;
        try {
            int rc = TibetanConverter.realMain(args, new PrintStream(new FileOutputStream("bin/for-junit/TMW_RTF_TO_THDL_WYLIE" + testName + "Result" + extension + ".out")));
            if (erc != rc) System.out.println("erc: rc is " + rc);
            assertTrue(rc == erc);
        } catch (FileNotFoundException e) {
            fileNotFound = true;
        }
        assertTrue(!fileNotFound);

        testActualAndExpected(testName + "Result" + extension);
    }

    private static String getTestFileName(String testName) {
        return "source" + File.separator
            + "org" + File.separator
            + "thdl" + File.separator
            + "tib" + File.separator
            + "input" + File.separator
            + "TMW_RTF_TO_THDL_WYLIE" + testName + ".rtf";
    }


    /** Tests the --find-some-non-tmw mode of {@link
     *  org.thdl.tib.input.TibetanConverter}. */
    public void testFindSomeNonTMWMode() {
        helper("Test1", "--find-some-non-tmw", "FindSome", 1);
    }

    /** Tests the --find-all-non-tmw mode of {@link
     *  org.thdl.tib.input.TibetanConverter}. */
    public void testFindAllNonTMWMode() {
        helper("Test1", "--find-all-non-tmw", "FindAll", 1);
    }

    /** Tests the --to-wylie converter mode of {@link
     *  org.thdl.tib.input.TibetanConverter}. */
    public void testConverterMode() {
        helper("Test1", "--to-wylie", "Conversion", 0);
        helper("Test2", "--to-wylie", "Conversion", 44);
    }

    /** Tests the --to-tibetan-machine converter mode of {@link
     *  org.thdl.tib.input.TibetanConverter}. */
    public void testTMConverterMode() {
        helper("Test1", "--to-tibetan-machine", "TM", 0);
        helper("Test2", "--to-tibetan-machine", "TM", 0);
        helper("Test2", "--to-tibetan-machine-web", "TMW", 0);
        helper("Test3", "--acip-to-tmw", "TMW", 0);
    }
}
