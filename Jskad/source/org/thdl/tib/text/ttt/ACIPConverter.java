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
import java.awt.Color;

import org.thdl.util.ThdlDebug;
import org.thdl.util.ThdlOptions;
import org.thdl.tib.text.TibetanDocument;
import org.thdl.tib.text.TibetanMachineWeb;
import org.thdl.tib.text.DuffCode;

/**
* This class is able to convert an ACIP file into Tibetan Machine Web
* and an ACIP file into TMW.  ACIP->Unicode should yield the same
* results as ACIP->TMW followed by TMW->Unicode (FIXME: test it!)
* @author David Chandler
*/
public class ACIPConverter {
    // DLC NOW: (KA)'s info is lost when you convert to Unicode text instead of Unicode RTF.  Give an ERROR.

    /** Command-line converter.  Gives error messages on standard
     *  output about why we can't convert the document perfectly and
     *  exits with non-zero return code, or is silent otherwise and
     *  exits with code zero.  <p>FIXME: not so efficient; copies the
     *  whole file into memory first. */
    public static void main(String[] args)
        throws IOException
    {
        // We don't want to load the TM or TMW font files ourselves:
        ThdlOptions.setUserPreference("thdl.rely.on.system.tmw.fonts", true);
        ThdlOptions.setUserPreference("thdl.rely.on.system.tm.fonts", true);
        ThdlOptions.setUserPreference("thdl.debug", true);

        TibetanDocument.enableColors();

        boolean verbose = true;
        if (args.length != 1) {
            System.out.println("Bad args!  Need just the name of the ACIP text file.");
        }
        StringBuffer errors = new StringBuffer();
        int maxErrors = 250;
        ArrayList al = ACIPTshegBarScanner.scanFile(args[0], errors, maxErrors - 1);

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
        final boolean abortUponScanningError = false;
        // DLC NOW: BAo isn't converting.
        if (errors.length() > 0) {
            System.err.println("Errors scanning ACIP input file: ");
            System.err.println(errors);
            if (abortUponScanningError) {
                System.err.println("Exiting; please fix input file and try again.");
                System.exit(1);
            }
        }

        String warningLevel = "Most"; // DLC make me configurable.
        StringBuffer warnings = null;
        boolean putWarningsInOutput = false;
        if ("None" != warningLevel) {
            warnings = new StringBuffer();
            putWarningsInOutput = true;
        }
        convertToTMW(al, System.out, errors, warnings,
                     putWarningsInOutput, warningLevel);
        int retCode = 0;
        if (errors.length() > 0) {
            System.err.println("Errors converting ACIP input file: ");
            System.err.println(errors);
            System.err.println("The output contains these errors.");
            System.err.println("Exiting; please fix input file and try again.");
            retCode = 2;
        }
        if (null != warnings && warnings.length() > 0) {
            System.err.println("Warnings converting ACIP input file: ");
            System.err.println(warnings);
            if (putWarningsInOutput)
                System.err.println("The output contains these warnings.");
            retCode = 2;
        }
        if (0 == retCode) {
            if (verbose) System.err.println("Converted " + args[0] + " perfectly.");
        }
        System.exit(retCode);
        // DLC NOW: tRAStA is not converter correctly to Unicode, and
        // no warning is given when converting to TMW.
    }

    /** Writes TMW/Latin to out.  If errors occur in converting a
     *  tsheg bar, then they are written into the output, and also
     *  appended to errors if errors is non-null.  If warnings occur
     *  in converting a tsheg bar, then they are written into the
     *  output if writeWarningsToResult is true, and also appended to
     *  warnings if warnings is non-null.  Returns true upon perfect
     *  success or if there were merely warnings, false if errors
     *  occurred.
     *  @throws IOException if we cannot write to out
     */
    public static boolean convertToTMW(ArrayList scan,
                                       OutputStream out,
                                       StringBuffer errors,
                                       StringBuffer warnings,
                                       boolean writeWarningsToResult,
                                       String warningLevel)
        throws IOException
    {
        TibetanDocument tdoc = new TibetanDocument();
        tdoc.setRomanAttributeSet(ThdlOptions.getStringOption("thdl.acip.to.x.latin.font",
                                                              "Courier New"),
                                  ThdlOptions.getIntegerOption("thdl.acip.to.x.latin.font.size",
                                                               20));
        boolean rv
            = convertToTMW(scan, tdoc, errors, warnings,
                           writeWarningsToResult, warningLevel);
        tdoc.writeRTFOutputStream(out);
        return rv;
    }

    private static boolean convertToTMW(ArrayList scan,
                                        TibetanDocument tdoc,
                                        StringBuffer errors,
                                        StringBuffer warnings,
                                        boolean writeWarningsToResult,
                                        String warningLevel)
        throws IOException
    {
        return convertTo(false, scan, null, tdoc, errors, warnings,
                         writeWarningsToResult, warningLevel);
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
     *  conversion upon perfect success or if there were merely
     *  warnings, null if errors occurred.
     */
    public static String convertToUnicode(String acip,
                                          StringBuffer errors,
                                          StringBuffer warnings,
                                          boolean writeWarningsToResult,
                                          String warningLevel) {
        ByteArrayOutputStream sw = new ByteArrayOutputStream();
        ArrayList al = ACIPTshegBarScanner.scan(acip, errors, -1);
        try {
            if (null != al
                && convertToUnicode(al, sw, errors,
                                    warnings, writeWarningsToResult,
                                    warningLevel)) {
                return sw.toString("UTF-8");
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new Error(e.toString());
        }
    }

    /** Writes Unicode to out.  If errors occur in converting a tsheg
     *  bar, then they are appended to errors if errors is non-null.
     *  Furthermore, errors are written to out.  If writeWarningsToOut
     *  is true, then warnings also will be written to out.
     *  @return true upon perfect success, false if errors occurred.
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
                                           boolean writeWarningsToOut,
                                           String warningLevel)
        throws IOException
    {
        return convertTo(true, scan, out, null, errors, warnings,
                         writeWarningsToOut, warningLevel);
    }

    private static boolean peekaheadFindsSpacesAndComma(ArrayList /* of ACIPString */ scan,
                                                        int pos) {
        int sz = scan.size();
        while (pos < sz) {
            ACIPString s = (ACIPString)scan.get(pos++);
            if (s.getType() == ACIPString.TIBETAN_PUNCTUATION && s.getText().equals(" ")) {
                // keep going
            } else {
                if (s.getType() == ACIPString.TIBETAN_PUNCTUATION && s.getText().equals(",")) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    private static boolean convertTo(boolean toUnicode, // else to TMW
                                     ArrayList scan,
                                     OutputStream out, // for toUnicode mode
                                     TibetanDocument tdoc, // for !toUnicode mode
                                     StringBuffer errors,
                                     StringBuffer warnings,
                                     boolean writeWarningsToOut,
                                     String warningLevel)
        throws IOException
    {
        int sz = scan.size();
        boolean hasErrors = false;
        BufferedWriter writer = null;
        if (toUnicode)
            writer
                = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
        boolean lastGuyWasNonPunct = false;
        TStackList lastGuy = null;
        Color lastColor = Color.BLACK;
        Color color = Color.BLACK;
        for (int i = 0; i < sz; i++) {
            ACIPString s = (ACIPString)scan.get(i);
            int stype = s.getType();
            if (stype == ACIPString.ERROR) {
                lastGuyWasNonPunct = false;
                lastGuy = null;
                hasErrors = true;
                String text = "[#ERROR CONVERTING ACIP DOCUMENT: Lexical error: " + s.getText() + "]";
                if (null != writer) writer.write(text);
                if (null != tdoc) tdoc.appendRoman(text, Color.RED);
            } else if (stype == ACIPString.WARNING) {
                lastGuyWasNonPunct = false;
                lastGuy = null;
                if (writeWarningsToOut) {
                    String text = "[#WARNING CONVERTING ACIP DOCUMENT: Lexical warning: " + s.getText() + "]";
                    if (null != writer) writer.write(text);
                    if (null != tdoc) tdoc.appendRoman(text, Color.RED);
                }
                // DLC NOW: Warning: We're going with {'}{R}{DA}, but only because our knowledge of prefix rules says that {'}{R+DA} is not a legal Tibetan tsheg bar ("syllable")

                if (null != warnings) {
                    warnings.append("Warning: Lexical warning: ");
                    warnings.append(s.getText());
                    warnings.append('\n');
                }
            } else {
                if (s.isLatin(stype)) {
                    lastGuyWasNonPunct = false;
                    lastGuy = null;
                    String text
                        = (((stype == ACIPString.FOLIO_MARKER) ? "{" : "")
                           + s.getText()
                           + ((stype == ACIPString.FOLIO_MARKER) ? "}" : ""));
                    if (null != writer) writer.write(text);
                    if (null != tdoc) tdoc.appendRoman(text, Color.BLACK);
                } else {
                    String unicode = null;
                    DuffCode[] duff = null;
                    if (stype == ACIPString.TIBETAN_NON_PUNCTUATION) {
                        lastGuyWasNonPunct = true;
                        TPairList pl = TPairListFactory.breakACIPIntoChunks(s.getText());
                        String acipError;

                        if ((acipError = pl.getACIPError()) != null) {
                            hasErrors = true;
                            String errorMessage = "[#ERROR CONVERTING ACIP DOCUMENT: THE TSHEG BAR (\"SYLLABLE\") " + s.getText() + " HAS THESE ERRORS: " + acipError + "]";
                            if (null != writer) writer.write(errorMessage);
                            if (null != tdoc) tdoc.appendRoman(errorMessage, Color.RED);
                            if (null != errors)
                                errors.append(errorMessage + "\n");
                        } else {
                            TParseTree pt = pl.getParseTree();
                            if (null == pt) {
                                hasErrors = true;
                                String errorMessage = "[#ERROR CONVERTING ACIP DOCUMENT: THE TSHEG BAR (\"SYLLABLE\") " + s.getText() + " IS ESSENTIALLY NOTHING.]";
                                if (null != writer) writer.write(errorMessage);
                                if (null != tdoc) tdoc.appendRoman(errorMessage, Color.RED);
                                if (null != errors)
                                    errors.append(errorMessage + "\n");
                            } else {
                                TStackList sl = pt.getBestParse();
                                if (null == sl) {
                                    hasErrors = true;
                                    String errorMessage = "[#ERROR CONVERTING ACIP DOCUMENT: THE TSHEG BAR (\"SYLLABLE\") " + s.getText() + " HAS NO LEGAL PARSES.]";
                                    if (null != writer) writer.write(errorMessage);
                                    if (null != tdoc) tdoc.appendRoman(errorMessage, Color.RED);
                                    if (null != errors)
                                        errors.append(errorMessage + "\n");
                                } else {
                                    lastGuy = sl;
                                    String warning
                                        = pt.getWarning(warningLevel,
                                                        pl,
                                                        s.getText());
                                    if (null != warning) {
                                        if (writeWarningsToOut) {
                                            String text
                                                = ("[#WARNING CONVERTING ACIP DOCUMENT: "
                                                   + warning + "]");
                                            if (null != writer) writer.write(text);
                                            if (null != tdoc) tdoc.appendRoman(text, Color.RED);
                                        }
                                        if (null != warnings) {
                                            warnings.append(warning);
                                            warnings.append('\n');
                                        }
                                    }
                                    if (null != writer) {
                                        unicode = sl.getUnicode();
                                        if (null == unicode) throw new Error("FIXME: make this an assertion 4");
                                    }
                                    if (null != tdoc) {
                                        duff = sl.getDuff();
                                        if (sl.isLegalTshegBar(true).isLegal && !sl.isLegalTshegBar(false).isLegal) {
                                            color = Color.YELLOW;
                                        } else if (sl.isLegalTshegBar(false).isLegal) {
                                            color = Color.BLACK;
                                        } else {
                                            // Sanskrit
                                            color = Color.GREEN;
                                        }

                                        if (0 == duff.length) {
                                            throw new Error("No DuffCodes for stack list " + sl); // FIXME: make this an assertion
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        color = Color.BLACK;
                        if (stype == ACIPString.START_SLASH) {
                            if (null != writer) unicode = "\u0F3C";
                            if (null != tdoc) duff = new DuffCode[] { TibetanMachineWeb.getGlyph("(") };
                        } else if (stype == ACIPString.END_SLASH) {
                            if (null != writer) unicode = "\u0F3D";
                            if (null != tdoc) duff = new DuffCode[] { TibetanMachineWeb.getGlyph(")") };
                        } else if (stype == ACIPString.TIBETAN_PUNCTUATION) {
                            // For ACIP, tshegs are used as both
                            // tshegs and whitespace.  We treat a
                            // space as a tsheg if and only if it
                            // occurs after TIBETAN_NON_PUNCTUATION.
                            // But "SHIG ,MDO" is an example of a
                            // special case, needed because a tsheg is
                            // not used after a GA in Tibetan
                            // typesetting.
                            boolean done = false;
                            // DLC what about after numbers?  marks?
                            TPairList lpl = null;
                            if (s.getText().equals(" ")) {
                                if (!lastGuyWasNonPunct
                                    || (null != lastGuy
                                        && (lpl = lastGuy.get(lastGuy.size() - 1)).size() == 1
                                        && lpl.get(0).getLeft().equals("G")
                                        && // it's (G . anything)
                                           // followed by some number
                                           // of spaces (at least one,
                                           // this one) and then a
                                           // comma:
                                        peekaheadFindsSpacesAndComma(scan, i+1))) {
                                    if (null != writer) {
                                        unicode = "    ";
                                        done = true;
                                    }
                                    if (null != tdoc) {
                                        tdoc.appendRoman("    ", Color.BLACK);
                                        continue;
                                    }
//  DLC AM I DOING THIS? By normal Tibetan & Dzongkha spelling, writing, and input rules
//  Tibetan script stacks should be entered and written: 1 headline
//  consonant (0F40->0F6A), any  subjoined consonant(s) (0F90->
//  0F9C),  achung (0F71), shabkyu (0F74), any above headline
//  vowel(s) (0F72 0F7A 0F7B 0F7C 0F7D and 0F80) ; any ngaro (0F7E,
//  0F82 and 0F83)
                                }
                            } else if (s.getText().equals(",")
                                       && lastGuyWasNonPunct
                                       && null != lastGuy
                                       && (lpl = lastGuy.get(lastGuy.size() - 1)).size() == 1
                                       && lpl.get(0).getLeft().equals("NG")) {
                                DuffCode tshegDuff = TibetanMachineWeb.getGlyph(" ");
                                if (null == tshegDuff) throw new Error("tsheg duff");
                                tdoc.appendDuffCodes(new DuffCode[] { tshegDuff }, lastColor);
                            }

                            if (!done) {
                                if (null != writer) unicode = ACIPRules.getUnicodeFor(s.getText(), false);
                                if (null != tdoc) {
                                    if (s.getText().equals("\r")
                                        || s.getText().equals("\t")
                                        || s.getText().equals("\n")
                                        || s.getText().equals("\r\n")) {
                                        tdoc.appendRoman(s.getText(), Color.BLACK);
                                        continue;
                                    } else {
                                        String wy = ACIPRules.getWylieForACIPOther(s.getText());
                                        if (null == wy) throw new Error("No wylie for ACIP " + s.getText());
                                        duff = new DuffCode[] { TibetanMachineWeb.getGlyph(wy) };
                                    }
                                }
                            }
                        } else {
                            throw new Error("forgot a case");
                        }
                        if (null != writer && null == unicode)
                            throw new Error("FIXME: make this an assertion 1");
                        if (null != tdoc && (null == duff || 0 == duff.length))
                            throw new Error("FIXME: make this an assertion 2");
                        lastGuyWasNonPunct = false;
                        lastGuy = null;
                    }
                    if (null != writer && null != unicode) writer.write(unicode);
                    if (null != tdoc) {
                        if (null != duff && 0 != duff.length) {
                            tdoc.appendDuffCodes(duff, color);
                            // DLC NOW FIXME: use TibTextUtils.getVowel logic to make the output beautiful.
                        } else {
                            // this happens when you have an
                            // [#ERROR]-producing tsheg bar.
                            
                            // System.err.println("Bad tsheg bar with ACIP {" + s.getText() + "}");
                        }
                    }
                }
            }
            lastColor = color;
        }
        if (null != writer) {
            writer.close();
        }
        return !hasErrors;
    }
}
// DLC FIXME: putting Tibetan in black, Sanskrit in green, and Latin
// in yellow would help you quickly decide if ZHIGN maybe should've
// been ZHING.
