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
            System.err.println(maxErrors + " or more errors occurred while scanning ACIP input file; is this");
            System.err.println("Tibetan or English input?");
            System.err.println("");
            System.err.println("First " + maxErrors + " errors scanning ACIP input file: ");
            System.err.println(errors);
            System.err.println("Exiting with " + maxErrors + " or more errors; please fix input file and try again.");
            System.exit(1);
        }
        if (errors.length() > 0) {
            System.err.println("Errors scanning ACIP input file: ");
            System.err.println(errors);
            System.err.println("Exiting; please fix input file and try again.");
            System.exit(1);
        }

        convertToUnicode(al, System.out, errors);
        if (errors.length() > 0) {
            System.err.println("Errors converting ACIP input file: ");
            System.err.println(errors);
            System.err.println("Exiting; please fix input file and try again.");
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

    /** Returns UTF-8 encoded Unicode.  A bit indirect, so use this
     *  for testing only if performance is a concern.  If errors occur
     *  in scanning the ACIP or in converting a tsheg bar, then they
     *  are appended to errors if errors is non-null.  Returns the
     *  conversion upon perfect success, null if errors occurred.
     */
    public static String convertToUnicode(String acip,
                                          StringBuffer errors) {
        ByteArrayOutputStream sw = new ByteArrayOutputStream();
        ArrayList al = ACIPTshegBarScanner.scan(acip, errors, true /* DLC FIXME */, -1);
        try {
            if (null != al && convertToUnicode(al, sw, errors)) {
                return sw.toString("UTF-8");
            } else {
                System.out.println("DLC al is " + al + " and convertToUnicode returned null.");
                return null;
            }
        } catch (Exception e) {
            throw new Error(e.toString());
        }
    }

    /** Writes Unicode to out.  If errors occur in converting a
     *  tsheg bar, then they are appended to errors if errors is
     *  non-null.  Returns true upon perfect success, false if errors
     *  occurred.
     *  @throws IOException if we cannot write to out
     */
    public static boolean convertToUnicode(ArrayList scan,
                                           OutputStream out,
                                           StringBuffer errors)
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
                writer.write("[#ERROR CONVERTING ACIP DOCUMENT: ");
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
