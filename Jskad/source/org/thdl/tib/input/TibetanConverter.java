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
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.thdl.util.*;
import org.thdl.tib.text.*;

import org.thdl.tib.text.ttt.ACIPConverter;
import org.thdl.tib.text.ttt.ACIPTshegBarScanner;
import java.util.ArrayList;

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

    /**
     *  Runs the converter. */
	public static void main(String[] args) {
        // No need for the TM or TMW fonts.
        System.setProperty("thdl.rely.on.system.tmw.fonts", "true");
        System.setProperty("thdl.rely.on.system.tm.fonts", "true");

        // Runs on Linux/Unix boxes without X11 servers:
        System.setProperty("java.awt.headless", "true");

        System.exit(realMain(args, System.out));
    }

    /** Runs the converter without exiting the program.
     *  @return the exit code. */
    public static int realMain(String[] args, PrintStream out) {
        try {
            boolean convertToUnicodeMode = false;
            boolean convertToTMMode = false;
            boolean convertACIPToUniMode = false;
            boolean convertACIPToTMWMode = false;
            boolean convertToTMWMode = false;
            boolean convertToWylieRTFMode = false;
            boolean convertToWylieTextMode = false;
            boolean convertToACIPRTFMode = false;
            boolean convertToACIPTextMode = false;
            boolean findSomeNonTMWMode = false;
            boolean findAllNonTMWMode = false;
            boolean findSomeNonTMMode = false;
            boolean findAllNonTMMode = false;

            boolean colors = false;
            
            // Process arguments:
            final int numArgs = 6;
            if ((args.length != 1 && args.length != numArgs)
                || (args.length == 1
                    && !(args[0].equals("-v")
                         || args[0].equals("--version")))
                || (args.length == numArgs
                    && (!(args[numArgs - 6].equals("--colors"))
                        || !((colors = args[numArgs - 5].equals("yes"))
                             || args[numArgs - 5].equals("no"))
                        || !(args[numArgs - 4].equals("--warning-level"))
                        || !(args[numArgs - 3].equals("Most")
                             || args[numArgs - 3].equals("Some")
                             || args[numArgs - 3].equals("All")
                             || args[numArgs - 3].equals("None"))
                        || !((findAllNonTMWMode
                              = args[numArgs - 2].equals("--find-all-non-tmw"))
                             || (convertToTMMode
                                 = args[numArgs - 2].equals("--to-tibetan-machine"))
                             || (convertToTMWMode
                                 = args[numArgs - 2].equals("--to-tibetan-machine-web"))
                             || (convertACIPToUniMode
                                 = args[numArgs - 2].equals("--acip-to-unicode"))
                             || (convertACIPToTMWMode
                                 = args[numArgs - 2].equals("--acip-to-tmw"))
                             || (convertToUnicodeMode
                                 = args[numArgs - 2].equals("--to-unicode"))
                             || (convertToWylieRTFMode
                                 = args[numArgs - 2].equals("--to-wylie"))
                             || (convertToWylieTextMode
                                 = args[numArgs - 2].equals("--to-wylie-text"))
                             || (convertToACIPRTFMode
                                 = args[numArgs - 2].equals("--to-acip"))
                             || (convertToACIPTextMode
                                 = args[numArgs - 2].equals("--to-acip-text"))
                             || (findSomeNonTMWMode
                                 = args[numArgs - 2].equals("--find-some-non-tmw"))
                             || (findSomeNonTMMode
                                 = args[numArgs - 2].equals("--find-some-non-tm"))
                             || (findAllNonTMMode
                                 = args[numArgs - 2].equals("--find-all-non-tm"))
                             )))) {
                out.println("TibetanConverter --colors yes|no");
                out.println("                 --warning-level None|Some|Most|All");
                out.println("                 --find-all-non-tmw | --find-some-non-tmw");
                out.println("                   | --to-tibetan-machine | --to-tibetan-machine-web");
                out.println("                   | --to-unicode | --to-wylie | --to-acip");
                out.println("                   | --to-wylie-text | --to-acip-text  RTF_file");
                out.println(" | TibetanConverter --acip-to-unicode | --acip-to-tmw TXT_file");
                out.println(" | TibetanConverter [--version | -v | --help | -h]");
                out.println("");
                out.println("Distributed under the terms of the THDL Open Community License Version 1.0.");
                out.println("");
                out.println("Usage:");
                out.println(" -v | --version for version info");
                out.println("");
                out.println(" -h | --help for this message");
                out.println("");
                out.println(" --to-tibetan-machine to convert TibetanMachineWeb to TibetanMachine");
                out.println("");
                out.println(" --to-unicode to convert TibetanMachineWeb to Unicode");
                out.println("");
                out.println(" --to-tibetan-machine-web to convert TibetanMachine to TibetanMachineWeb");
                out.println("");
                out.println(" --to-wylie to convert TibetanMachineWeb to THDL Extended Wylie in RTF");
                out.println("");
                out.println(" --to-wylie-text to convert TibetanMachineWeb to THDL Extended Wylie in text");
                out.println("");
                out.println(" --to-acip to convert TibetanMachineWeb to ACIP in RTF");
                out.println("");
                out.println(" --to-acip-text to convert TibetanMachineWeb to ACIP in text");
                out.println("");
                out.println(" --acip-to-unicode to convert ACIP text file to Unicode text file");
                out.println("");
                out.println(" --acip-to-tmw to convert ACIP text file to Tibetan Machine Web RTF File.");
                out.println("");
                out.println(" --find-all-non-tmw to locate all characters in the input document that are");
                out.println("   not in Tibetan Machine Web fonts, exit zero if and only if none found");
                out.println("");
                out.println(" --find-some-non-tmw to locate all distinct characters in the input document");
                out.println("   not in Tibetan Machine Web fonts, exit zero if and only if none found");
                out.println("");
                out.println(" --find-all-non-tm to locate all characters in the input document that are");
                out.println("   not in Tibetan Machine fonts, exit zero if and only if none found");
                out.println("");
                out.println(" --find-some-non-tm to locate all distinct characters in the input document");
                out.println("   not in Tibetan Machine fonts, exit zero if and only if none found");
                out.println("");
                out.println("");
                out.println(" In --to... and --acip-to... modes, needs one argument, the name of the");
                out.println(" TibetanMachineWeb RTF");
                out.println(" file (for --to-wylie, --to-unicode, and --to-tibetan-machine) or the name of");
                out.println(" the TibetanMachine RTF file (for --to-tibetan-machine-web) or the name of the");
                out.println(" ACIP text file (for --acip-to-unicode).  Writes the");
                out.println(" result to standard output (after dealing with the curly brace problem if");
                out.println(" the input is TibetanMachineWeb).  Exit code is zero on success, 42 if some");
                out.println(" glyphs couldn't be converted (in which case the output is just those glyphs),");
                out.println(" 44 if a TMW->Wylie conversion ran into some glyphs that couldn't be");
                out.println(" converted, in which case ugly error messages like");
                out.println("    \"<<[[JSKAD_TMW_TO_WYLIE_ERROR_NO_SUCH_WYLIE: Cannot convert DuffCode...\"");
                out.println(" are in your document waiting for your personal attention,");
                out.println(" 43 if not even one glyph found was eligible for this conversion, which means");
                out.println(" that you probably selected the wrong conversion or the wrong document, or ");
                out.println(" nonzero otherwise.");
                out.println("");
                out.println(" You may find it helpful to use `--find-some-non-tmw' mode (or");
                out.println(" `--find-some-non-tm' mode for Tibetan Machine input) before doing a");
                out.println(" conversion so that you have confidence in the conversion's correctness.");
                return 77;
            }
            if (args[0].equals("--version") || args[0].equals("-v")) {
                out.println("TibetanConverter version 0.83");
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
                if (convertToWylieRTFMode) {
                    conversionTag = TMW_TO_WYLIE;
                } else if (convertToWylieTextMode) {
                    conversionTag = TMW_TO_WYLIE_TEXT;
                } else if (convertToACIPRTFMode) {
                    conversionTag = TMW_TO_ACIP;
                } else if (convertToACIPTextMode) {
                    conversionTag = TMW_TO_ACIP_TEXT;
                } else if (convertToUnicodeMode) {
                    conversionTag = TMW_TO_UNI;
                } else if (convertToTMWMode) {
                    conversionTag = TM_TO_TMW;
                } else if (convertACIPToUniMode) {
                    conversionTag = ACIP_TO_UNI_TEXT;
                } else if (convertACIPToTMWMode) {
                    conversionTag = ACIP_TO_TMW;
                } else {
                    ThdlDebug.verify(convertToTMMode);
                    conversionTag = TMW_TO_TM;
                }
            }
            return reallyConvert(in, out, conversionTag,
                                 args[numArgs - 3].intern(), colors);
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
    static int reallyConvert(InputStream in, PrintStream out, String ct,
                             String warningLevel, boolean colors) {
        if (ACIP_TO_UNI_TEXT == ct || ACIP_TO_TMW == ct) {
            try {
                ArrayList al = ACIPTshegBarScanner.scanStream(in, null,
                                                              ThdlOptions.getIntegerOption("thdl.most.errors.a.tibetan.acip.document.can.have",
                                                                                           250 - 1)
                                                              );
                if (null == al)
                    return 47;
                StringBuffer warnings = new StringBuffer();
                boolean embeddedWarnings = (warningLevel != "None");
                if (ACIP_TO_UNI_TEXT == ct) {
                    if (!ACIPConverter.convertToUnicode(al, out, null, warnings,
                                                        embeddedWarnings,
                                                        warningLevel))
                        return 46;
                } else {
                    if (ct != ACIP_TO_TMW) throw new Error("badness");
                    if (!ACIPConverter.convertToTMW(al, out, null, warnings,
                                                    embeddedWarnings,
                                                    warningLevel, colors))
                        return 46;
                }
                if (embeddedWarnings && warnings.length() > 0)
                    return 45;
                else
                    return 0;
            } catch (IOException e) {
                return 48;
            }
        } else {
            TibetanDocument tdoc = new TibetanDocument();
            {
                SimpleAttributeSet ras = new SimpleAttributeSet();
                StyleConstants.setFontFamily(ras,
                                             ThdlOptions.getStringOption("thdl.default.roman.font.face",
                                                                         "Serif"));
                StyleConstants.setFontSize(ras,
                                           ThdlOptions.getIntegerOption("thdl.default.roman.font.size",
                                                                        14));
                tdoc.setRomanAttributeSet(ras);
            }
            try {
                // Read in the rtf file.
                if (debug) System.err.println("Start: reading in old RTF file");
                if (!ThdlOptions.getBooleanOption("thdl.do.not.fix.rtf.hex.escapes"))
                    in = new RTFFixerInputStream(in);
                (new RTFEditorKit()).read(in, tdoc, 0);
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
                    = tdoc.findAllNonTMWCharacters(0, -1, out);
                if (out.checkError())
                    exitCode = 41;
                return exitCode;
            } else if (FIND_SOME_NON_TMW == ct) {
                // 0, -1 is the entire document.
                int exitCode
                    = tdoc.findSomeNonTMWCharacters(0, -1, out);
                if (out.checkError())
                    exitCode = 41;
                return exitCode;
            } else if (FIND_SOME_NON_TM == ct) {
                // 0, -1 is the entire document.
                int exitCode
                    = tdoc.findSomeNonTMCharacters(0, -1, out);
                if (out.checkError())
                    exitCode = 41;
                return exitCode;
            } else if (FIND_ALL_NON_TM == ct) {
                // 0, -1 is the entire document.
                int exitCode
                    = tdoc.findAllNonTMCharacters(0, -1, out);
                if (out.checkError())
                    exitCode = 41;
                return exitCode;
            } else { // conversion {to Wylie or TM} mode
                // Fix curly braces in the entire document if the input is TMW:
                if (TM_TO_TMW != ct) {
                    // DLC make me optional
                    if (debug) System.err.println("Start: solving curly brace problem");
                    tdoc.replaceTahomaCurlyBracesAndBackslashes(0, -1);
                    if (debug) System.err.println("End  : solving curly brace problem");
                }

                int exitCode = 0;
                ThdlDebug.verify(((TMW_TO_TM == ct) ? 1 : 0)
                                 + ((TMW_TO_UNI == ct) ? 1 : 0)
                                 + ((TM_TO_TMW == ct) ? 1 : 0)
                                 + ((TMW_TO_ACIP == ct) ? 1 : 0)
                                 + ((TMW_TO_ACIP_TEXT == ct) ? 1 : 0)
                                 + ((TMW_TO_WYLIE == ct) ? 1 : 0)
                                 + ((TMW_TO_WYLIE_TEXT == ct) ? 1 : 0)
                                 == 1);
                long numAttemptedReplacements[] = new long[] { 0 };
                if (TMW_TO_WYLIE == ct) {
                    // Convert to THDL Wylie:
                    if (!tdoc.toWylie(0,
                                      tdoc.getLength(),
                                      numAttemptedReplacements)) {
                        exitCode = 44;
                    }
                } else if (TMW_TO_ACIP == ct) {
                    // Convert to ACIP:
                    if (!tdoc.toACIP(0,
                                     tdoc.getLength(),
                                     numAttemptedReplacements)) {
                        exitCode = 49;
                    }
                } else if (TMW_TO_UNI == ct) {
                    StringBuffer errors = new StringBuffer();
                    // Convert to Unicode:
                    if (tdoc.convertToUnicode(0,
                                              tdoc.getLength(),
                                              errors,
                                              ThdlOptions.getStringOption("thdl.tmw.to.unicode.font").intern(),
                                              numAttemptedReplacements)) {
                        System.err.println(errors);
                        exitCode = 42;
                    }
                } else if (TM_TO_TMW == ct) {
                    StringBuffer errors = new StringBuffer();
                    // Convert to TibetanMachineWeb:
                    if (tdoc.convertToTMW(0, tdoc.getLength(), errors,
                                          numAttemptedReplacements)) {
                        System.err.println(errors);
                        exitCode = 42;
                    }
                } else {
                    ThdlDebug.verify(TMW_TO_TM == ct);
                    StringBuffer errors = new StringBuffer();
                    // Convert to TibetanMachine:
                    if (tdoc.convertToTM(0, tdoc.getLength(), errors,
                                         numAttemptedReplacements)) {
                        System.err.println(errors);
                        exitCode = 42;
                    }
                }

                // Write to standard output the result:
                if (TMW_TO_WYLIE_TEXT == ct || TMW_TO_ACIP_TEXT == ct) {
                    try {
                        tdoc.writeTextOutput(new BufferedWriter(new OutputStreamWriter(out)));
                    } catch (IOException e) {
                        exitCode = 40;
                    }
                } else {
                    try {
                        tdoc.writeRTFOutputStream(out);
                    } catch (IOException e) {
                        exitCode = 40;
                    }
                }
                if (out.checkError())
                    exitCode = 41;
                if (numAttemptedReplacements[0] < 1)
                    exitCode = 43;

                return exitCode;
            }
        }
    }
}
