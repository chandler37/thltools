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

package org.thdl.tib.text.ttt;

import java.io.*;
import java.util.ArrayList;
import java.util.Stack;

import org.thdl.util.ThdlDebug;
import org.thdl.util.ThdlOptions;

/**
* This class is able to convert an ACIP file into Tibetan Machine Web.
* From there, TMW->Unicode takes you to Unicode.
* @author David Chandler
*/
public class ACIPConverter {
    static {
        // We don't want to load the TM or TMW font files ourselves:
        ThdlOptions.setUserPreference("thdl.rely.on.system.tmw.fonts", true);
        ThdlOptions.setUserPreference("thdl.rely.on.system.tm.fonts", true);
        ThdlOptions.setUserPreference("thdl.debug", true);
    }

    /** Command-line converter.  Gives error messages on standard
     *  output about why we can't convert the document perfectly and
     *  exits with non-zero return code, or is silent otherwise and
     *  exits with code zero.  <p>FIXME: not so efficient; copies the
     *  whole file into memory first. */
    public static void main(String[] args)
        throws IOException // DLC FIXME: give nice error messages
    {
        boolean verbose = true;
        boolean strict = true;
        if (args.length != 2
            || (!(strict = "--strict".equals(args[0])) && !"--lenient".equals(args[0]))) {
            System.err.println("Bad args!  Need '--strict filename' or '--lenient filename'.");
            System.exit(1);
        }
        StringBuffer errors = new StringBuffer();
        int maxErrors = 250;
        ArrayList al = ACIPTshegBarScanner.scanFile(args[1], errors, strict, maxErrors - 1);

        if (null == al) {
            System.err.println(maxErrors + " or more lexical errors occurred while scanning ACIP input file; is this");
            System.err.println("Tibetan or English input?");
            System.err.println("");
            if (false) {
                // Nobody wants to see this.  FIXME: maybe somebody; have an option.
                System.err.println("First " + maxErrors + " lexical errors scanning ACIP input file: ");
                System.err.println(errors);
            }
            System.err.println("Exiting with " + maxErrors + " or more lexical errors; please fix input file and try again.");
            System.exit(1);
        }
        final boolean abortUponScanningError = false; // DLC MAKE ME CONFIGURABLE
        // DLC NOW: BAo isn't converting.
        if (errors.length() > 0) {
            System.err.println("Errors scanning ACIP input file: ");
            System.err.println(errors);
            if (abortUponScanningError) {
                System.err.println("Exiting; please fix input file and try again.");
                System.exit(1);
            }
        }

        StringBuffer warnings = new StringBuffer();
        boolean putWarningsInOutput = true; // DLC make me configurable.
        convertToUnicode(al, System.out, errors, warnings,
                         putWarningsInOutput);
        if (errors.length() > 0) {
            System.err.println("Errors converting ACIP input file: ");
            System.err.println(errors);
            System.err.println("The output contains these errors.");
            System.err.println("Exiting; please fix input file and try again.");
            System.exit(2);
        }
        if (warnings.length() > 0) {
            System.err.println("Warnings converting ACIP input file: ");
            System.err.println(warnings);
            if (putWarningsInOutput)
                System.err.println("The output contains these warnings.");
            System.exit(2);
        }
        if (verbose) System.err.println("Converted " + args[1] + " perfectly.");
        System.exit(0);
    }

    /** Writes TMW/Latin to out.  If errors occur in converting a
     *  tsheg bar, then they are appended to errors if errors is
     *  non-null.  Returns true upon perfect success, false if errors
     *  occurred.
     *  @throws IOException if we cannot write to out
     */
    public static boolean convertToTMW(ArrayList scan, String latinFont,
                                       OutputStream out, StringBuffer errors)
        throws IOException
    {
        throw new Error("DLC UNIMPLEMENTED");
    }
    // DLC FIXME: sometimes { } is \u0F0B, and sometimes it is a
    // space.  Treat it as a tsheg only when it appears after a
    // syllable or another tsheg.

    /** Returns UTF-8 encoded Unicode.  A bit indirect, so use this
     *  for testing only if performance is a concern.  If errors occur
     *  in scanning the ACIP or in converting a tsheg bar, then they
     *  are appended to errors if errors is non-null, as well as
     *  written to the result.  If warnings occur in scanning the ACIP
     *  or in converting a tsheg bar, then they are appended to
     *  warnings if warnings is non-null, and they are written to the
     *  result if writeWarningsToResult is true.  Returns the
     *  conversion upon perfect success, null if errors occurred.
     */
    public static String convertToUnicode(String acip,
                                          StringBuffer errors,
                                          StringBuffer warnings,
                                          boolean writeWarningsToResult) {
        ByteArrayOutputStream sw = new ByteArrayOutputStream();
        ArrayList al = ACIPTshegBarScanner.scan(acip, errors, true /* DLC FIXME */, -1);
        try {
            if (null != al
                && convertToUnicode(al, sw, errors,
                                    warnings, writeWarningsToResult)) {
                return sw.toString("UTF-8");
            } else {
                System.out.println("DLC al is " + al + " and convertToUnicode returned null.");
                return null;
            }
        } catch (Exception e) {
            throw new Error(e.toString());
        }
    }

    /** Writes Unicode to out.  If errors occur in converting a tsheg
     *  bar, then they are appended to errors if errors is non-null.
     *  Furthermore, errors are written to out.  If writeWarningsToOut
     *  is true, then warnings also will be written to out.  Returns
     *  true upon perfect success, false if errors occurred.
     *  @param scan result of ACIPTshegBarScanner.scan(..)
     *  @param out stream to which to write converted text
     *  @param errors if non-null, all error messages are appended
     *  @param warnings if non-null, all warning messages are appended
     *  to this
     *  @param writeWarningsToOut if true, then all warning messages
     *  are written to out in the appropriate places
     *  @throws IOException if we cannot write to out
     */
    public static boolean convertToUnicode(ArrayList scan,
                                           OutputStream out,
                                           StringBuffer errors,
                                           StringBuffer warnings,
                                           boolean writeWarningsToOut)
        throws IOException
    {
        int sz = scan.size();
        boolean hasErrors = false;
        BufferedWriter writer
            = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
        for (int i = 0; i < sz; i++) {
            ACIPString s = (ACIPString)scan.get(i);
            int stype = s.getType();
            if (stype == ACIPString.ERROR) {
                hasErrors = true;
                writer.write("[#ERROR CONVERTING ACIP DOCUMENT: Lexical error: ");
                writer.write(s.getText());
                writer.write("]");
            } else {
                // DLC FIXME: what about 'no A on root stack' and 'no A on such-and-such stack' warnings?
                if (s.isLatin(stype)) {
                    if (stype == ACIPString.FOLIO_MARKER)
                        writer.write("{");
                    writer.write(s.getText());
                    if (stype == ACIPString.FOLIO_MARKER)
                        writer.write("}");
                } else {
                    String unicode = null;
                    if (stype == ACIPString.TIBETAN_NON_PUNCTUATION) {
                        TPairList pl = TPairListFactory.breakACIPIntoChunks(s.getText());
                        String acipError;

                        if ((acipError = pl.getACIPError()) != null) {
                            hasErrors = true;
                            String errorMessage = "[#ERROR CONVERTING ACIP DOCUMENT: THE TSHEG BAR (\"SYLLABLE\") " + s.getText() + " HAS THESE ERRORS: " + acipError + "]";
                            writer.write(errorMessage);
                            if (null != errors)
                                errors.append(errorMessage + "\n");
                        } else {
                            TParseTree pt = pl.getParseTree();
                            if (null == pt) {
                                hasErrors = true;
                                String errorMessage = "[#ERROR CONVERTING ACIP DOCUMENT: THE TSHEG BAR (\"SYLLABLE\") " + s.getText() + " IS ESSENTIALLY NOTHING.]";
                                writer.write(errorMessage);
                                if (null != errors)
                                    errors.append(errorMessage + "\n");
                            } else {
                                TStackList sl = pt.getBestParse();
                                if (null == sl) {
                                    hasErrors = true;
                                    String errorMessage = "[#ERROR CONVERTING ACIP DOCUMENT: THE TSHEG BAR (\"SYLLABLE\") " + s.getText() + " HAS NO LEGAL PARSES.]";
                                    writer.write(errorMessage);
                                    if (null != errors)
                                        errors.append(errorMessage + "\n");
                                } else {
                                    String warning
                                        = pt.getWarning(false, // DLC: make me configurable
                                                        pl,
                                                        s.getText());
                                    if (null != warning) {
                                        if (writeWarningsToOut) {
                                            writer.write("[#WARNING CONVERTING ACIP DOCUMENT: ");
                                            writer.write(warning);
                                            writer.write("]");
                                        }
                                        if (null != warnings) {
                                            warnings.append(warning);
                                            warnings.append('\n');
                                        }
                                    }
                                    unicode = sl.getUnicode();
                                    if (null == unicode) throw new Error("DLC: HOW?");
                                }
                            }
                        }
                    } else {
                        if (stype == ACIPString.START_SLASH)
                            unicode = "\u0F3C";
                        else if (stype == ACIPString.END_SLASH)
                            unicode = "\u0F3D";
                        else
                            unicode = ACIPRules.getUnicodeFor(s.getText(), false);
                        if (null == unicode) throw new Error("DLC: HOW?");
                    }
                    if (null != unicode) {
                        writer.write(unicode);
                    }
                }
            }
        }
        writer.close();
        return !hasErrors;
    }
}
// DLC FIXME: putting Tibetan in black, Sanskrit in green, and Latin
// in yellow would help you quickly decide if ZHIGN maybe should've
// been ZHING.
