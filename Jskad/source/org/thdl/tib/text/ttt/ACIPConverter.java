// DLC NOW: 'US etc. -- do we handle them all?
// DLC NOW WARN ON NNYA and DBA
// DLC NOW: implement Robert Chilton-supplied prefix rules
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
* and an ACIP file into Unicode.  ACIP->Unicode should yield the same
* results as ACIP->TMW followed by TMW->Unicode (FIXME: test it!)
* @author David Chandler
*/
public class ACIPConverter {
    // DLC NOW: (KA)'s info is lost when you convert to Unicode text instead of Unicode RTF.  Give an ERROR.

    // DLC NOW: IMPLEMENT (KA) font shrinking

    // DLC NOW: BAo isn't converting.

    // DLC NOW: tRAStA is not converter correctly to Unicode, and no
    // warning is given when converting to TMW (Wait!  isn't the "a
    // stack occurs w/o a vowel" warning given?)

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
        if (errors.length() > 0) {
            System.err.println("Errors scanning ACIP input file: ");
            System.err.println(errors);
            if (abortUponScanningError) {
                System.err.println("Exiting; please fix input file and try again.");
                System.exit(1);
            }
        }

        String warningLevel = "Most";
        boolean colors = true;
        StringBuffer warnings = null;
        boolean putWarningsInOutput = false;
        if ("None" != warningLevel) {
            warnings = new StringBuffer();
            putWarningsInOutput = true;
        }
        convertToTMW(al, System.out, errors, warnings,
                     putWarningsInOutput, warningLevel, colors);
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
    }

    /** Writes TMW/Latin to out.  If errors occur in converting a
     *  tsheg bar, then they are written into the output, and also
     *  appended to errors if errors is non-null.  If warnings occur
     *  in converting a tsheg bar, then they are written into the
     *  output if writeWarningsToResult is true, and also appended to
     *  warnings if warnings is non-null.  Returns true upon perfect
     *  success or if there were merely warnings, false if errors
     *  occurred.
     *  @param colors true if and only if you want Sanskrit in one
     *  color, errors/warnings in another, and tsheg-bars affected by
     *  prefix rules in another
     *  @throws IOException if we cannot write to out
     */
    public static boolean convertToTMW(ArrayList scan,
                                       OutputStream out,
                                       StringBuffer errors,
                                       StringBuffer warnings,
                                       boolean writeWarningsToResult,
                                       String warningLevel,
                                       boolean colors)
        throws IOException
    {
        TibetanDocument tdoc = new TibetanDocument();
        boolean rv
            = convertToTMW(scan, tdoc, errors, warnings,
                           writeWarningsToResult, warningLevel, colors);
        tdoc.writeRTFOutputStream(out);
        return rv;
    }

    private static boolean convertToTMW(ArrayList scan,
                                        TibetanDocument tdoc,
                                        StringBuffer errors,
                                        StringBuffer warnings,
                                        boolean writeWarningsToResult,
                                        String warningLevel,
                                        boolean colors)
        throws IOException
    {
        return convertTo(false, scan, null, tdoc, errors, warnings,
                         writeWarningsToResult, warningLevel, colors);
    }

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
            if (null != al) {
                convertToUnicode(al, sw, errors,
                                 warnings, writeWarningsToResult,
                                 warningLevel);
                return sw.toString("UTF-8");
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new Error(e.toString());
        }
    }

    /** Writes Unicode text (not RTF) to out.  <em>NOTE WELL: This
     *  inherently cannot describe the ACIP {(KA) KHA} properly, as
     *  that requires showing KA in a smaller font than KHA, which is
     *  not possible in plain text.</em> If errors occur in converting
     *  a tsheg bar, then they are appended to errors if errors is
     *  non-null.  Furthermore, errors are written to out.  If
     *  writeWarningsToOut is true, then warnings also will be written
     *  to out.
     *  @return true upon perfect success, false if errors occurred.
     *  @param scan result of ACIPTshegBarScanner.scan(..)
     *  @param out stream to which to write converted text
     *  @param errors if non-null, all error messages are appended
     *  @param warnings if non-null, all warning messages appropriate
     *  to warningLevel are appended
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
                         writeWarningsToOut, warningLevel, false);
    }

    private static boolean peekaheadFindsSpacesAndComma(ArrayList /* of TString */ scan,
                                                        int pos) {
        int sz = scan.size();
        while (pos < sz) {
            TString s = (TString)scan.get(pos++);
            if (s.getType() == TString.TIBETAN_PUNCTUATION && s.getText().equals(" ")) {
                // keep going
            } else {
                if (s.getType() == TString.TIBETAN_PUNCTUATION && s.getText().equals(",")) {
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
                                     String warningLevel,
                                     boolean colors)
        throws IOException
    {
        int smallFontSize = -1;
        int regularFontSize = -1;
        if (null != tdoc) {
            String latinFont
                = ThdlOptions.getStringOption("thdl.acip.to.x.latin.font",
                                              "Times New Roman");
            int latinFontSize
                = ThdlOptions.getIntegerOption("thdl.acip.to.x.latin.font.size",
                                               18);
            tdoc.setRomanAttributeSet(latinFont, latinFontSize);

            regularFontSize = tdoc.getTibetanFontSize();
            smallFontSize = (int)(0.75*regularFontSize);
            if (smallFontSize >= regularFontSize)
                smallFontSize = regularFontSize - 1;
        }

        if (colors)
            tdoc.enableColors();
        else
            tdoc.disableColors();
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
            TString s = (TString)scan.get(i);
            int stype = s.getType();
            if (stype == TString.ERROR) {
                lastGuyWasNonPunct = false;
                lastGuy = null;
                hasErrors = true;
                String text = "[#ERROR CONVERTING ACIP DOCUMENT: Lexical error: " + s.getText() + "]";
                if (null != writer) writer.write(text);
                if (null != tdoc) tdoc.appendRoman(text, Color.RED);
            } else if (stype == TString.TSHEG_BAR_ADORNMENT) {
                if (lastGuyWasNonPunct) {
                    String err = "[#ERROR CONVERTING ACIP DOCUMENT: This converter cannot yet convert " + s.getText() + " because the converter's author is unclear what the result should be.]";
                    if (null != writer) {
                        String uni = ACIPRules.getUnicodeFor(s.getText(), false);
                        if (null == uni) {
                            hasErrors = true;
                            uni = err;
                        }
                        if (null != writer) writer.write(uni);
                    }
                    if (null != tdoc) {
                        String wylie
                            = ACIPRules.getWylieForACIPOther(s.getText());
                        if (null == wylie) {
                            hasErrors = true;
                            tdoc.appendRoman(err, Color.RED);
                        } else {
                            tdoc.appendDuffCodes(new DuffCode[] { TibetanMachineWeb.getGlyph(wylie) },
                                                 Color.BLACK);
                        }
                    }
                } else {
                    hasErrors = true;
                }
                lastGuyWasNonPunct = true; // this stuff is not really punctuation
                lastGuy = null;
            } else if (stype == TString.WARNING) {
                lastGuyWasNonPunct = false;
                lastGuy = null;
                if (writeWarningsToOut) {
                    String text = "[#WARNING CONVERTING ACIP DOCUMENT: Lexical warning: " + s.getText() + "]";
                    if (null != writer) writer.write(text);
                    if (null != tdoc) tdoc.appendRoman(text, Color.RED);
                }

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
                        = (((stype == TString.FOLIO_MARKER) ? "{" : "")
                           + s.getText()
                           + ((stype == TString.FOLIO_MARKER) ? "}" : ""));
                    if (null != writer) writer.write(text);
                    if (null != tdoc) tdoc.appendRoman(text, Color.BLACK);
                } else {
                    String unicode = null;
                    Object[] duff = null;
                    if (stype == TString.TIBETAN_NON_PUNCTUATION) {
                        lastGuyWasNonPunct = true;
                        TPairList pls[] = TPairListFactory.breakACIPIntoChunks(s.getText());
                        String acipError;

                        if ((acipError = pls[0].getACIPError()) != null
                            && (null == pls[1] || pls[1].getACIPError() != null)) {
                            hasErrors = true;
                            String errorMessage = "[#ERROR CONVERTING ACIP DOCUMENT: THE TSHEG BAR (\"SYLLABLE\") " + s.getText() + " HAS THESE ERRORS: " + acipError + "]";
                            if (null != writer) writer.write(errorMessage);
                            if (null != tdoc) tdoc.appendRoman(errorMessage, Color.RED);
                            if (null != errors)
                                errors.append(errorMessage + "\n");
                        } else {
                            TParseTree pt0 = pls[0].getParseTree();
                            TParseTree pt1 = ((null == pls[1])
                                              ? null : pls[1].getParseTree());
                            if (null == pt0 && null == pt1) {
                                hasErrors = true;
                                String errorMessage = "[#ERROR CONVERTING ACIP DOCUMENT: THE TSHEG BAR (\"SYLLABLE\") " + s.getText() + " IS ESSENTIALLY NOTHING.]";
                                if (null != writer) writer.write(errorMessage);
                                if (null != tdoc) tdoc.appendRoman(errorMessage, Color.RED);
                                if (null != errors)
                                    errors.append(errorMessage + "\n");
                            } else {
                                TStackList sl0 = pt0.getBestParse();
                                TStackList sl1 = ((null == pt1)
                                                  ? null : pt1.getBestParse());
                                if (null == sl0 && null == sl1) {
                                    hasErrors = true;
                                    String errorMessage = "[#ERROR CONVERTING ACIP DOCUMENT: THE TSHEG BAR (\"SYLLABLE\") " + s.getText() + " HAS NO LEGAL PARSES.]";
                                    if (null != writer) writer.write(errorMessage);
                                    if (null != tdoc) tdoc.appendRoman(errorMessage, Color.RED);
                                    if (null != errors)
                                        errors.append(errorMessage + "\n");
                                } else {
                                    TStackList sl = sl0;
                                    TPairList pl = pls[0];
                                    TParseTree pt = pt0;
                                    // set sl equal to the best choice of sl0 and sl1.
                                    if (null != sl1) {
                                        BoolTriple sl0bt = sl0.isLegalTshegBar(false);
                                        BoolTriple sl1bt = sl1.isLegalTshegBar(false);
                                        int ct;
                                        if ((ct = sl0bt.compareTo(sl1bt)) < 0) {
                                            sl = sl1;
                                            pl = pls[1];
                                            pt = pt1;
                                        } else if (0 == ct) {
                                            // sl remains sl0 -- '* is
                                            // a vowel unless it's
                                            // clearly part of an
                                            // appendage like 'AM.
                                        }
                                    }
                                    lastGuy = sl;
                                    String warning = null;
                                    if ("None" != warningLevel) {
                                        warning = pt.getWarning(warningLevel,
                                                                pl,
                                                                s.getText());
                                    }
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
                                        if (colors && sl.isLegalTshegBar(true).isLegal && !sl.isLegalTshegBar(false).isLegal) {
                                            color = Color.YELLOW;
                                        } else if (colors && sl.isLegalTshegBar(false).isLegal) {
                                            color = Color.BLACK;
                                        } else {
                                            // Sanskrit

                                            // DLC FIXME: a funny vowel, the presence of a sanskrit-only stack, and a funny mark like ACIP ':' should cause green too.
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
                        if (stype == TString.START_SLASH) {
                            if (null != writer) unicode = "\u0F3C";
                            if (null != tdoc) duff = new Object[] { TibetanMachineWeb.getGlyph("(") };
                        } else if (stype == TString.END_SLASH) {
                            if (null != writer) unicode = "\u0F3D";
                            if (null != tdoc) duff = new Object[] { TibetanMachineWeb.getGlyph(")") };
                        } else if (stype == TString.TIBETAN_PUNCTUATION) {
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
                                        // "GU ," and "KU ," each have
                                        // tshegs, but "GI ," and "KI
                                        // ," each have a Tibetan
                                        // space.
                                        && ((lpl.get(0).getLeft().equals("G")
                                             || lpl.get(0).getLeft().equals("K"))
                                            && (null == lpl.get(0).getRight()
                                                || lpl.get(0).getRight().indexOf('U') < 0))
                                        &&
                                        // it's (G . anything)
                                        // followed by some number of
                                        // spaces (at least one, this
                                        // one) and then a comma:
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
                                        duff = new Object[] { TibetanMachineWeb.getGlyph(wy) };
                                    }
                                }
                            }
                        } else if (stype == TString.START_PAREN) {
                            if (null != tdoc) {
                                tdoc.setTibetanFontSize(smallFontSize);
                            }
                            continue;
                        } else if (stype == TString.END_PAREN) {
                            if (null != tdoc) {
                                tdoc.setTibetanFontSize(regularFontSize);
                            }
                            continue;
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
                            for (int j = 0; j < duff.length; j++) {
                                if (duff[j] instanceof DuffCode)
                                    tdoc.appendDuffCode((DuffCode)duff[j],
                                                        color);
                                else {
                                    hasErrors = true;
                                    if (null != errors)
                                        errors.append((String)duff[j] + "\n");
                                    tdoc.appendRoman((String)duff[j],
                                                     Color.RED);
                                }
                            }
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
