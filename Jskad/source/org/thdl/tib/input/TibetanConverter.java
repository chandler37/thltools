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

/** TibetanConverter is a command-line utility for converting to
 *  and from Tibetan Machine Web (TMW).  It converts TMW to Wylie, to
 *  Unicode, or to Tibetan Machine (TM).  It also converts TM to TMW.
 *  It is a TibetanMachineWeb-in-RichTextFormat to your choice of
 *  TibetanMachine-in-RichTextFormat, THDL Extended
 *  Wylie-in-RichTextFormat, or Unicode-in-RichTextFormat converter,
 *  more specifically, as well as converting from TM to TMW.  Invoke
 *  it with no parameters for usage information.
 *  @author David Chandler */
public class TibetanConverter implements FontConverterConstants {
    private static final boolean debug = false;

    /** Default constructor; does nothing */
    TibetanConverter() { }

    static final String rtfErrorMessage
        = "The Rich Text Format (RTF) file selected contains constructs that\nJskad cannot handle.  If you got the RTF file from saving a Word\ndocument as RTF, try saving that same document as RTF in\nWord 2000 instead of Word XP or in Word 97 instead of\nWord 2000.  Older versions of Word produce RTF that Jskad\ncan more easily deal with.  OpenOffice and StarOffice may also\nproduce better-behaved RTF.";

    static {
        // No need for the TM or TMW fonts.
        System.setProperty("thdl.rely.on.system.tmw.fonts", "true");
        System.setProperty("thdl.do.not.rely.on.system.tmw.fonts", "false");
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
            boolean convertToUnicodeMode = false;
            boolean convertToTMMode = false;
            boolean convertToTMWMode = false;
            boolean convertToWylieMode = false;
            boolean findSomeNonTMWMode = false;
            boolean findAllNonTMWMode = false;
            boolean findSomeNonTMMode = false;
            boolean findAllNonTMMode = false;
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
                         || (convertToUnicodeMode
                             = args[0].equals("--to-unicode"))
                         || (convertToWylieMode
                             = args[0].equals("--to-wylie"))
                         || (findSomeNonTMWMode
                             = args[0].equals("--find-some-non-tmw"))
                         || (findSomeNonTMMode
                             = args[0].equals("--find-some-non-tm"))
                         || (findAllNonTMMode
                             = args[0].equals("--find-all-non-tm"))
                ))) {
                out.println("TibetanConverter [--find-all-non-tmw | --find-some-non-tmw");
                out.println("                  | --to-tibetan-machine | --to-tibetan-machine-web");
                out.println("                  | --to-unicode | --to-wylie] RTF_file");
                out.println(" | TibetanConverter [--version | -v | --help | -h]");
                out.println("");
                out.println("Distributed under the terms of the THDL Open Community License Version 1.0.");
                out.println("");
                out.println("Usage:");
                out.println(" -v | --version for version info");
                out.println(" -h | --help for this message");
                out.println(" --find-all-non-tmw to locate all characters in the input document that are");
                out.println("   not in Tibetan Machine Web fonts, exit zero if and only if none found");
                out.println(" --find-some-non-tmw to locate all distinct characters in the input document");
                out.println("   not in Tibetan Machine Web fonts, exit zero if and only if none found");
                out.println(" --find-all-non-tm to locate all characters in the input document that are");
                out.println("   not in Tibetan Machine fonts, exit zero if and only if none found");
                out.println(" --find-some-non-tm to locate all distinct characters in the input document");
                out.println("   not in Tibetan Machine fonts, exit zero if and only if none found");
                out.println(" --to-tibetan-machine to convert TibetanMachineWeb to TibetanMachine");
                out.println(" --to-unicode to convert TibetanMachineWeb to Unicode");
                out.println(" --to-tibetan-machine-web to convert TibetanMachine to TibetanMachineWeb");
                out.println(" --to-wylie to convert TibetanMachineWeb to THDL Extended Wylie");
                out.println("");
                out.println(" In --to... modes, needs one argument, the name of the TibetanMachineWeb RTF");
                out.println(" file (for --to-wylie, --to-unicode, and --to-tibetan-machine) or the name of");
                out.println(" the TibetanMachine RTF file (for --to-tibetan-machine-web).  Writes the");
                out.println(" result to standard output (after dealing with the curly brace problem if");
                out.println(" the input is TibetanMachineWeb).  Exit code is zero on success, 42 if some");
                out.println(" glyphs couldn't be converted (in which case the output is just those glyphs),");
                out.println(" 43 if not even one glyph found was eligible for this conversion, which means");
                out.println(" that you probably selected the wrong conversion or the wrong document, or ");
                out.println(" nonzero otherwise.");
                out.println("");
                out.println(" You may find it helpful to use `--find-some-non-tmw' mode (or");
                out.println(" `--find-some-non-tm' mode for Tibetan Machine input) before doing a");
                out.println(" conversion so that you have confidence in the conversion's correctness.");
                // DLC add Wylie->TMW mode.
                return 77;
            }
            if (args[0].equals("--version") || args[0].equals("-v")) {
                out.println("TibetanConverter version 0.82");
                out.println("Compiled at "
                            + ThdlVersion.getTimeOfCompilation());
                return 77;
            }
            String inputRtfPath = args[args.length - 1];

            InputStream in;
            if (inputRtfPath.equals("-"))
                in = System.in;
            else
                in = new FileInputStream(inputRtfPath);
            
            String conversionTag = null;
            if (findAllNonTMWMode) {
                conversionTag = FIND_ALL_NON_TMW;
            } else if (findSomeNonTMWMode) {
                conversionTag = FIND_SOME_NON_TMW;
            } else if (findSomeNonTMMode) {
                conversionTag = FIND_SOME_NON_TM;
            } else if (findAllNonTMMode) {
                conversionTag = FIND_ALL_NON_TM;
            } else { // conversion {to Wylie or TM} mode
                if (convertToWylieMode) {
                    conversionTag = TMW_TO_WYLIE;
                } else if (convertToUnicodeMode) {
                    conversionTag = TMW_TO_UNI;
                } else if (convertToTMWMode) {
                    conversionTag = TM_TO_TMW;
                } else {
                    ThdlDebug.verify(convertToTMMode);
                    conversionTag = TMW_TO_TM;
                }
            }
            return reallyConvert(in, out, conversionTag);
        } catch (ThdlLazyException e) {
            out.println("TibetanConverter has a BUG:");
            e.getRealException().printStackTrace(out);
            return 7;
        } catch (IOException e) {
            e.printStackTrace(out);
            return 4;
        }
	}

    /** Reads from in, closes in, converts (or finds some/all
        non-TM/TMW), writes the result to out, does not close out.
        The action taken depends on ct, which must be one of a set
        number of strings -- see the code.  Returns an appropriate
        return code so that TibetanConverter's usage message is
        honored. */
    static int reallyConvert(InputStream in, PrintStream out, String ct) {
        DuffPane dp = new DuffPane();
        try {
            // Read in the rtf file.
            if (debug) System.err.println("Start: reading in old RTF file");
            if (!ThdlOptions.getBooleanOption("thdl.do.not.fix.rtf.hex.escapes"))
                in = new RTFFixerInputStream(in);
            dp.rtfEd.read(in, dp.getDocument(), 0);
            if (debug) System.err.println("End  : reading in old RTF file");
        } catch (Exception e) {
            out.println("TibetanConverter:\n"
                        + rtfErrorMessage);
            return 3;
        }

        try {
            in.close();
        } catch (IOException e) {
            // silently ignore; we don't care about the input so much...
            ThdlDebug.noteIffyCode();
        }


        if (FIND_ALL_NON_TMW == ct) {
            // 0, -1 is the entire document.
            int exitCode
                = ((TibetanDocument)dp.getDocument()).findAllNonTMWCharacters(0, -1, out);
            if (out.checkError())
                exitCode = 41;
            return exitCode;
        } else if (FIND_SOME_NON_TMW == ct) {
            // 0, -1 is the entire document.
            int exitCode
                = ((TibetanDocument)dp.getDocument()).findSomeNonTMWCharacters(0, -1, out);
            if (out.checkError())
                exitCode = 41;
            return exitCode;
        } else if (FIND_SOME_NON_TM == ct) {
            // 0, -1 is the entire document.
            int exitCode
                = ((TibetanDocument)dp.getDocument()).findSomeNonTMCharacters(0, -1, out);
            if (out.checkError())
                exitCode = 41;
            return exitCode;
        } else if (FIND_ALL_NON_TM == ct) {
            // 0, -1 is the entire document.
            int exitCode
                = ((TibetanDocument)dp.getDocument()).findAllNonTMCharacters(0, -1, out);
            if (out.checkError())
                exitCode = 41;
            return exitCode;
        } else { // conversion {to Wylie or TM} mode
            // Fix curly braces in the entire document if the input is TMW:
            if (TM_TO_TMW != ct) {
                // DLC make me optional
                if (debug) System.err.println("Start: solving curly brace problem");
                ((TibetanDocument)dp.getDocument()).replaceTahomaCurlyBracesAndBackslashes(0, -1);
                if (debug) System.err.println("End  : solving curly brace problem");
            }

            int exitCode = 0;
            ThdlDebug.verify(((TMW_TO_TM == ct) ? 1 : 0)
                             + ((TMW_TO_UNI == ct) ? 1 : 0)
                             + ((TM_TO_TMW == ct) ? 1 : 0)
                             + ((TMW_TO_WYLIE == ct) ? 1 : 0)
                             == 1);
            long numAttemptedReplacements[] = new long[] { 0 };
            if (TMW_TO_WYLIE == ct) {
                // Convert to THDL Wylie:
                dp.toWylie(0, dp.getDocument().getLength(),
                           numAttemptedReplacements);
            } else if (TMW_TO_UNI == ct) {
                StringBuffer errors = new StringBuffer();
                // Convert to Unicode:
                if (((TibetanDocument)dp.getDocument()).convertToUnicode(0,
                                                                         dp.getDocument().getLength(),
                                                                         errors,
                                                                         ThdlOptions.getStringOption("thdl.tmw.to.unicode.font").intern(),
                                                                         numAttemptedReplacements)) {
                    System.err.println(errors);
                    exitCode = 42;
                }
            } else if (TM_TO_TMW == ct) {
                StringBuffer errors = new StringBuffer();
                // Convert to TibetanMachineWeb:
                if (((TibetanDocument)dp.getDocument()).convertToTMW(0, dp.getDocument().getLength(), errors,
                                                                     numAttemptedReplacements)) {
                    System.err.println(errors);
                    exitCode = 42;
                }
            } else {
                ThdlDebug.verify(TMW_TO_TM == ct);
                StringBuffer errors = new StringBuffer();
                // Convert to TibetanMachine:
                if (((TibetanDocument)dp.getDocument()).convertToTM(0, dp.getDocument().getLength(), errors,
                                                                    numAttemptedReplacements)) {
                    System.err.println(errors);
                    exitCode = 42;
                }
            }

            // Write to standard output the result:
            try {
                ((TibetanDocument)dp.getDocument()).writeRTFOutputStream(out);
            } catch (IOException e) {
                exitCode = 40;
            }
            if (out.checkError())
                exitCode = 41;
            if (numAttemptedReplacements[0] < 1)
                exitCode = 43;

            return exitCode;
        }
    }
}
