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

/**
 *  TMW_RTF_TO_THDL_WYLIE is a command-line utility for converting TMW
 *  to Wylie.  It is a TibetanMachineWeb-in-RichTextFormat to THDL
 *  Extended Wylie converter, more specifically.  Invoke it with no
 *  parameters for usage information.
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
            boolean findSomeNonTMWMode = false;
            boolean findAllNonTMWMode = false;
            // Process arguments:
            if ((args.length != 1 && args.length != 2)
                || (args.length == 1
                    && (args[0].equals("-h")
                        || args[0].equals("--help")))
                || (args.length == 2
                    && !((findAllNonTMWMode
                          = args[0].equals("--find-all-non-tmw"))
                         || (findSomeNonTMWMode
                             = args[0].equals("--find-some-non-tmw"))))) {
                out.println("TMW_RTF_TO_THDL_WYLIE [--find-all-non-tmw | --find-some-non-tmw] RTF_file |");
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
                out.println(" Otherwise, needs one argument, the name of the TibetanMachineWeb RTF file.");
                out.println(" Writes the Wylie transliteration of that file to standard output after");
                out.println(" dealing with the curly brace problem.  Exit code is zero on success,");
                out.println(" nonzero otherwise.");
                out.println("");
                out.println(" You may find it helpful to use `--find-some-non-tmw' mode before doing a");
                out.println(" conversion so that you have confidence in the conversion's correctness.");
                return 77;
            }
            if (args[0].equals("--version") || args[0].equals("-v")) {
                out.println("TMW_RTF_TO_THDL_WYLIE version 0.8");
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
            } else { // conversion mode
                // Fix curly braces in the entire document:
                ((TibetanDocument)dp.getDocument()).replaceTahomaCurlyBracesAndBackslashes(0, -1);

                // Convert to THDL Wylie:
                dp.toWylie(0, dp.getDocument().getLength());

                // Write to standard output the result:
                ((TibetanDocument)dp.getDocument()).writeRTFOutputStream(out);

                // Exit normally:
                return 0;
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
