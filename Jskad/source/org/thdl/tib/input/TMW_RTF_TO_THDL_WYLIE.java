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

import java.io.*;

import org.thdl.util.*;
import org.thdl.tib.text.*;

/** DLC FIXME: this is misnamed and it doesn't allow for TM->TMW conversion.
 *
 *  TMW_RTF_TO_THDL_WYLIE is a command-line utility for converting TMW
 *  to Wylie or to Tibetan Machine.  It is a
 *  TibetanMachineWeb-in-RichTextFormat to your choice of
 *  TibetanMachine-in-RichTextFormat or THDL Extended
 *  Wylie-in-RichTextFormat converter, more specifically.  Invoke it
 *  with no parameters for usage information.
 *  @author David Chandler */
public class TMW_RTF_TO_THDL_WYLIE {
    static final String rtfErrorMessage
        = "The Rich Text Format (RTF) file selected contains constructs that\nJskad cannot handle.  If you got the RTF file from saving a Word\ndocument as RTF, try saving that same document as RTF in\nWord 2000 instead of Word XP or in Word 97 instead of\nWord 2000.  Older versions of Word produce RTF that Jskad\ncan more easily deal with.  OpenOffice and StarOffice may also\nproduce better-behaved RTF.";

    static {
        // No need for the TMW fonts.
        System.setProperty("thdl.rely.on.system.tmw.fonts", "true");
    }

    /**
     *  Runs the converter. */
	public static void main(String[] args) {
        System.exit(realMain(args, System.out));
    }

    /** Runs the converter without exiting the program.
     *  @return the exit code. */
    public static int realMain(String[] args, PrintStream out) {
        try {
            boolean convertToTMMode = false;
            boolean convertToTMWMode = false;
            boolean convertToWylieMode = false;
            boolean findSomeNonTMWMode = false;
            boolean findAllNonTMWMode = false;
            // Process arguments:
            if ((args.length != 1 && args.length != 2)
                || (args.length == 1
                    && !(args[0].equals("-v")
                         || args[0].equals("--version")))
                || (args.length == 2
                    && !((findAllNonTMWMode
                          = args[0].equals("--find-all-non-tmw"))
                         || (convertToTMMode
                             = args[0].equals("--to-tibetan-machine"))
                         || (convertToTMWMode
                             = args[0].equals("--to-tibetan-machine-web"))
                         || (convertToWylieMode
                             = args[0].equals("--to-wylie"))
                         || (findSomeNonTMWMode
                             = args[0].equals("--find-some-non-tmw"))))) {
                out.println("TMW_RTF_TO_THDL_WYLIE [--find-all-non-tmw | --find-some-non-tmw | --to-tibetan-machine | --to-tibetan-machine-web | --to-wylie] RTF_file |");
                out.println("TMW_RTF_TO_THDL_WYLIE [--version | -v | --help | -h]");
                out.println("");
                out.println("Distributed under the terms of the THDL Open Community License Version 1.0.");
                out.println("");
                out.println("Usage:");
                out.println(" -v | --version for version info");
                out.println(" -h | --help for this message");
                out.println(" --find-all-non-tmw to locate all characters in the input document that are");
                out.println("   not in Tibetan Machine Web fonts, exit zero iff none found");
                out.println(" --find-some-non-tmw to locate all distinct characters in the input document");
                out.println("   not in Tibetan Machine Web fonts, exit zero iff none found");
                out.println(" --to-tibetan-machine to convert TibetanMachineWeb to TibetanMachine");
                out.println(" --to-tibetan-machine-web to convert TibetanMachine to TibetanMachineWeb");
                out.println(" --to-wylie to convert TibetanMachineWeb to THDL Extended Wylie");
                out.println(" In --to... modes, needs one argument, the name of the TibetanMachineWeb RTF");
                out.println(" file (for --to-wylie and --to-tibetan-machine) or the name of TibetanMachine");
                out.println(" RTF file (for --to-tibetan-machine-web).  Writes the THDL Extended Wylie");
                out.println(" transliteration of that file [in --to-wylie mode]");
                out.println(" or the TibetanMachine equivalent of that file [in");
                out.println(" --to-tibetan-machine mode] to standard output after dealing with the curly");
                out.println(" brace problem.  Exit code is zero on success, 42 if some TibetanMachine glyphs");
                out.println(" couldn't be understood (though output is still given), nonzero otherwise.");
                out.println("");
                out.println(" You may find it helpful to use `--find-some-non-tmw' mode before doing a");
                out.println(" conversion so that you have confidence in the conversion's correctness.");
                return 77;
            }
            if (args[0].equals("--version") || args[0].equals("-v")) {
                out.println("TMW_RTF_TO_THDL_WYLIE version 0.81");
                out.println("Compiled at "
                            + ThdlVersion.getTimeOfCompilation());
                return 77;
            }
            String tmwRtfPath = args[args.length - 1];

            DuffPane dp = new DuffPane();
            // Read in the rtf file.
            {
                InputStream in = new FileInputStream(tmwRtfPath);
                try {
                    dp.rtfEd.read(in, dp.getDocument(), 0);
                } catch (Exception e) {
                    out.println("TMW_RTF_TO_THDL_WYLIE:\n"
                                       + rtfErrorMessage);
                    return 3;
                }
                in.close();
            }

            if (findAllNonTMWMode) {
                // 0, -1 is the entire document.
                return ((TibetanDocument)dp.getDocument()).findAllNonTMWCharacters(0, -1, out);
            } else if (findSomeNonTMWMode) {
                // 0, -1 is the entire document.
                return ((TibetanDocument)dp.getDocument()).findSomeNonTMWCharacters(0, -1, out);
            } else { // conversion {to Wylie or TM} mode
                // Fix curly braces in the entire document if the input is TMW:
                if (!convertToTMWMode) {
                    ((TibetanDocument)dp.getDocument()).replaceTahomaCurlyBracesAndBackslashes(0, -1);
                }
                
                int exitCode = 0;
                if (convertToWylieMode) {
                    ThdlDebug.verify(!convertToTMMode);
                    ThdlDebug.verify(!convertToTMWMode);
                    // Convert to THDL Wylie:
                    dp.toWylie(0, dp.getDocument().getLength());
                } else if (convertToTMWMode) {
                    ThdlDebug.verify(!convertToTMMode);
                    ThdlDebug.verify(!convertToWylieMode);
                    StringBuffer errors = new StringBuffer();
                    // Convert to TibetanMachine:
                    if (((TibetanDocument)dp.getDocument()).convertToTMW(0, dp.getDocument().getLength(), errors)) {
                        System.err.println(errors);
                        exitCode = 42;
                    }
                } else {
                    ThdlDebug.verify(convertToTMMode);
                    ThdlDebug.verify(!convertToTMWMode);
                    ThdlDebug.verify(!convertToWylieMode);
                    StringBuffer errors = new StringBuffer();
                    // Convert to TibetanMachine:
                    if (((TibetanDocument)dp.getDocument()).convertToTM(0, dp.getDocument().getLength(), errors)) {
                        System.err.println(errors);
                        exitCode = 42;
                    }
                }

                // Write to standard output the result:
                ((TibetanDocument)dp.getDocument()).writeRTFOutputStream(out);

                return exitCode;
            }
        } catch (ThdlLazyException e) {
            out.println("TMW_RTF_TO_THDL_WYLIE has a BUG:");
            e.getRealException().printStackTrace(out);
            return 7;
        } catch (IOException e) {
            e.printStackTrace(out);
            return 4;
        }
	}
}
