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

/**
* This class is able to break up Strings of ACIP text (for example, an
* entire sutra file) into tsheg bars, comments, etc. Folio markers,
* comments, and the like are segregated (so that consumers can ensure
* that they remain in Latin), and Tibetan passages are broken up into
* tsheg bars.
* @author David Chandler
*/
public class ACIPTshegBarScanner {
    /** Useful for testing.  Gives error messages on standard output
     *  about why we can't scan the document perfectly and exits with
     *  non-zero return code, or says "Good scan!" otherwise and exits
     *  with code zero.  <p>FIXME: not so efficient; copies the whole
     *  file into memory first. */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Bad args!  Need just the name of the ACIP text file.");
            System.exit(1);
        }
        StringBuffer errors = new StringBuffer();
        int maxErrors = 250;
        ArrayList al = scanFile(args[0], errors, maxErrors - 1);

        if (null == al) {
            System.out.println(maxErrors + " or more errors occurred while scanning ACIP input file; is this");
            System.out.println("Tibetan or English input?");
            System.out.println("");
            System.out.println("First " + maxErrors + " errors scanning ACIP input file: ");
            System.out.println(errors);
            System.out.println("Exiting with " + maxErrors + " or more errors; please fix input file and try again.");
            System.exit(1);
        }
        if (errors.length() > 0) {
            System.out.println("Errors scanning ACIP input file: ");
            System.out.println(errors);
            System.out.println("Exiting; please fix input file and try again.");
            System.exit(1);
        }

        System.out.println("Good scan!");
        System.exit(0);
    }

    /** Scans an ACIP file with path fname into tsheg bars.  If errors
     *  is non-null, error messages will be appended to it.  Returns a
     *  list of TStrings that is the scan. <p>FIXME: not so
     *  efficient; copies the whole file into memory first.
     *  @throws IOException if we cannot read in the ACIP input file */
    public static ArrayList scanFile(String fname, StringBuffer errors, int maxErrors)
        throws IOException
    {
        return scanStream(new FileInputStream(fname),
                          errors, maxErrors);
    }

    /** Scans a stream of ACIP into tsheg bars.  If errors is
     *  non-null, error messages will be appended to it.  You can
     *  recover both errors and warnings (modulo offset information)
     *  from the result, though.  Returns a list of TStrings that
     *  is the scan, or null if more than maxErrors occur. <p>FIXME:
     *  not so efficient; copies the whole file into memory first.
     *  @throws IOException if we cannot read the whole ACIP stream */
    public static ArrayList scanStream(InputStream stream, StringBuffer errors,
                                       int maxErrors)
        throws IOException
    {
        StringBuffer s = new StringBuffer();
        char ch[] = new char[8192];
        BufferedReader in
            = new BufferedReader(new InputStreamReader(stream, "US-ASCII"));

        int amt;
        while (-1 != (amt = in.read(ch))) {
            s.append(ch, 0, amt);
        }
        in.close();
        return scan(s.toString(), errors, maxErrors);
    }

    /** Returns a list of {@link TString TStrings} corresponding
     *  to s, possibly the empty list (when the empty string is the
     *  input).  Each String is either a Latin comment, some Latin
     *  text, a tsheg bar (minus the tsheg or shad or whatever), a
     *  String of inter-tsheg-bar punctuation, etc.
     *
     *  <p>This not only scans; it finds all the errors and warnings a
     *  parser would too, like "NYA x" and "(" and ")" and "/NYA" etc.
     *  It puts those in as TStrings with type {@link
     *  TString#ERROR} or {@link TString#WARNING}, and also, if
     *  errors is non-null, appends helpful messages to errors, each
     *  followed by a '\n'.
     *  @param s the ACIP text
     *  @param errors if non-null, the buffer to which to append error
     *  messages (FIXME: kludge, just get this info by scanning
     *  the result for TString.ERROR (and maybe TString.WARNING,
     *  if you care about warnings), but then we'd have to put the
     *  Offset info in the TString)
     *  @param maxErrors if nonnegative, then scanning will stop when
     *  more than maxErrors errors occur.  In this event, null is
     *  returned.
     *  @return null if more than maxErrors errors occur, or the scan
     *  otherwise
    */
    public static ArrayList scan(String s, StringBuffer errors, int maxErrors) {

        // the size depends on whether it's mostly Tibetan or mostly
        // Latin and a number of other factors.  This is meant to be
        // an underestimate, but not too much of an underestimate.
        int numErrors = 0;
        ArrayList al = new ArrayList(s.length() / 10);
        
        boolean waitingForMatchingIllegalClose = false;
        int sl = s.length();
        int currentType = TString.ERROR;
        int startOfString = 0;
        Stack bracketTypeStack = new Stack();
        int startSlashIndex = -1;
        int startParenIndex = -1;
        int numNewlines = 0;
        for (int i = 0; i < sl; i++) {
            if (i < startOfString) throw new Error("bad reset");
            char ch;
            ch = s.charAt(i);
            if (ch == '\n') ++numNewlines;
            if (TString.COMMENT == currentType && ch != ']') {
                if ('[' == ch) {
                    al.add(new TString("Found an open bracket within a [#COMMENT]-style comment.  Brackets may not appear in comments.\n",
                                       TString.ERROR));
                    if (null != errors)
                        errors.append("Offset " + i + ((numNewlines == 0) ? "" : (" or maybe " + (i-numNewlines))) + ": "
                                      + "Found an open bracket within a [#COMMENT]-style comment.  Brackets may not appear in comments.\n");
                    if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
                }
                continue;
            }
            switch (ch) {
            case '}': // fall through...
            case ']':
                if (bracketTypeStack.empty()) {
                    // Error.
                    if (startOfString < i) {
                        al.add(new TString(s.substring(startOfString, i),
                                           currentType));
                    }
                    if (!waitingForMatchingIllegalClose) {
                        al.add(new TString("Found a truly unmatched close bracket, " + s.substring(i, i+1),
                                           TString.ERROR));
                        if (null != errors) {
                            errors.append("Offset " + i + ((numNewlines == 0) ? "" : (" or maybe " + (i-numNewlines))) + ": "
                                          + "Found a truly unmatched close bracket, ] or }.\n");
                        }
                        if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
                    }
                    waitingForMatchingIllegalClose = false;
                    al.add(new TString("Found a closing bracket without a matching open bracket.  Perhaps a [#COMMENT] incorrectly written as [COMMENT], or a [*CORRECTION] written incorrectly as [CORRECTION], caused this.",
                                       TString.ERROR));
                    if (null != errors)
                        errors.append("Offset " + i + ((numNewlines == 0) ? "" : (" or maybe " + (i-numNewlines))) + ": "
                                      + "Found a closing bracket without a matching open bracket.  Perhaps a [#COMMENT] incorrectly written as [COMMENT], or a [*CORRECTION] written incorrectly as [CORRECTION], caused this.\n");
                    if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
                    startOfString = i+1;
                    currentType = TString.ERROR;
                } else {
                    int stackTop = ((Integer)bracketTypeStack.pop()).intValue();

                    int end = startOfString;
                    if (TString.CORRECTION_START == stackTop) {

                        // This definitely indicates a new token.
                        char prevCh = s.charAt(i-1);
                        if (prevCh == '?')
                            end = i - 1;
                        else
                            end = i;
                        if (startOfString < end) {
                            al.add(new TString(s.substring(startOfString, end),
                                               currentType));
                        }

                        if ('?' != prevCh) {
                            currentType = TString.PROBABLE_CORRECTION;
                        } else {
                            currentType = TString.POSSIBLE_CORRECTION;
                        }
                    }
                    al.add(new TString(s.substring(end, i+1), currentType));
                    startOfString = i+1;
                    currentType = TString.ERROR;
                }
                break; // end ']','}' case

            case '{': // NOTE WELL: KX0016I.ACT, KD0095M.ACT, and a
                      // host of other ACIP files use {} brackets like
                      // [] brackets.  I treat both the same.
                
                // fall through...
            case '[':
                // This definitely indicates a new token.
                if (startOfString < i) {
                    al.add(new TString(s.substring(startOfString, i),
                                       currentType));
                    startOfString = i;
                    currentType = TString.ERROR;
                }
                String thingy = null;

                if (i + "[DD]".length() <= sl
                    && (s.substring(i, i + "[DD]".length()).equals("[DD]")
                        || s.substring(i, i + "[DD]".length()).equals("{DD}"))) {
                    thingy = "[DD]";
                    currentType = TString.DD;
                } else if (i + "[DD1]".length() <= sl
                           && (s.substring(i, i + "[DD1]".length()).equals("[DD1]")
                               || s.substring(i, i + "[DD1]".length()).equals("{DD1}"))) {
                    thingy = "[DD1]";
                    currentType = TString.DD;
                } else if (i + "[DD2]".length() <= sl
                           && (s.substring(i, i + "[DD2]".length()).equals("[DD2]")
                               || s.substring(i, i + "[DD2]".length()).equals("{DD2}"))) {
                    thingy = "[DD2]";
                    currentType = TString.DD;
                } else if (i + "[DDD]".length() <= sl
                           && (s.substring(i, i + "[DDD]".length()).equals("[DDD]")
                               || s.substring(i, i + "[DDD]".length()).equals("{DDD}"))) {
                    thingy = "[DDD]";
                    currentType = TString.DD;
                } else if (i + "[DR]".length() <= sl
                           && (s.substring(i, i + "[DR]".length()).equals("[DR]")
                               || s.substring(i, i + "[DR]".length()).equals("{DR}"))) {
                    thingy = "[DR]";
                    currentType = TString.DR;
                } else if (i + "[LS]".length() <= sl
                           && (s.substring(i, i + "[LS]".length()).equals("[LS]")
                               || s.substring(i, i + "[LS]".length()).equals("{LS}"))) {
                    thingy = "[LS]";
                    currentType = TString.LS;
                } else if (i + "[BP]".length() <= sl
                           && (s.substring(i, i + "[BP]".length()).equals("[BP]")
                               || s.substring(i, i + "[BP]".length()).equals("{BP}"))) {
                    thingy = "[BP]";
                    currentType = TString.BP;
                } else if (i + "[BLANK PAGE]".length() <= sl
                           && (s.substring(i, i + "[BLANK PAGE]".length()).equals("[BLANK PAGE]")
                               || s.substring(i, i + "[BLANK PAGE]".length()).equals("{BLANK PAGE}"))) {
                    thingy = "[BLANK PAGE]";
                    currentType = TString.BP;
                } else if (i + "[ BP ]".length() <= sl
                           && (s.substring(i, i + "[ BP ]".length()).equals("[ BP ]")
                               || s.substring(i, i + "[ BP ]".length()).equals("{ BP }"))) {
                    thingy = "{ BP }"; // found in TD3790E2.ACT
                    currentType = TString.BP;
                } else if (i + "[ DD ]".length() <= sl
                           && (s.substring(i, i + "[ DD ]".length()).equals("[ DD ]")
                               || s.substring(i, i + "[ DD ]".length()).equals("{ DD }"))) {
                    thingy = "{ DD }"; // found in TD3790E2.ACT
                    currentType = TString.DD;
                } else if (i + "[?]".length() <= sl
                           && (s.substring(i, i + "[?]".length()).equals("[?]")
                               || s.substring(i, i + "[?]".length()).equals("{?}"))) {
                    thingy = "[?]";
                    currentType = TString.QUESTION;
                } else {
                    //  We see comments appear not as [#COMMENT], but
                    //  as [COMMENT] sometimes.  We make special cases
                    //  for some English comments.  There's no need to
                    //  make this mechanism extensible, because you
                    //  can easily edit the ACIP text so that it uses
                    //  [#COMMENT] notation instead of [COMMENT].

                    String[] englishComments = new String[] {
                        "FIRST", "SECOND", // S5274I.ACT
                        "Additional verses added by Khen Rinpoche here are", // S0216M.ACT
                        "ADDENDUM: The text of", // S0216M.ACT
                        "END OF ADDENDUM", // S0216M.ACT
                        "Some of the verses added here by Khen Rinpoche include:", // S0216M.ACT
                        "Note that, in the second verse, the {YUL LJONG} was orignally {GANG LJONG},\nand is now recited this way since the ceremony is not only taking place in Tibet.", // S0216M.ACT
                        "Note that, in the second verse, the {YUL LJONG} was orignally {GANG LJONG},\r\nand is now recited this way since the ceremony is not only taking place in Tibet.", // S0216M.ACT
                        "text missing", // S6954E1.ACT
                        "INCOMPLETE", // TD3817I.INC
                        "MISSING PAGE", // S0935m.act
                        "MISSING FOLIO", // S0975I.INC
                        "UNCLEAR LINE", // S0839D1I.INC
                        "THE FOLLOWING TEXT HAS INCOMPLETE SECTIONS, WHICH ARE ON ORDER", // SE6260A.INC
                        "@DATA INCOMPLETE HERE", // SE6260A.INC
                        "@DATA MISSING HERE", // SE6260A.INC
                        "LINE APPARENTLY MISSING THIS PAGE", // TD4035I.INC
                        "DATA INCOMPLETE HERE", // TD4226I2.INC
                        "DATA MISSING HERE", // just being consistent
                        "FOLLOWING SECTION WAS NOT AVAILABLE WHEN THIS EDITION WAS\nPRINTED, AND IS SUPPLIED FROM ANOTHER, PROBABLY THE ORIGINAL:", // S0018N.ACT
                        "FOLLOWING SECTION WAS NOT AVAILABLE WHEN THIS EDITION WAS\r\nPRINTED, AND IS SUPPLIED FROM ANOTHER, PROBABLY THE ORIGINAL:", // S0018N.ACT
                        "THESE PAGE NUMBERS RESERVED IN THIS EDITION FOR PAGES\nMISSING FROM ORIGINAL ON WHICH IT WAS BASED", // S0018N.ACT
                        "THESE PAGE NUMBERS RESERVED IN THIS EDITION FOR PAGES\r\nMISSING FROM ORIGINAL ON WHICH IT WAS BASED", // S0018N.ACT
                        "PAGE NUMBERS RESERVED FROM THIS EDITION FOR MISSING\nSECTION SUPPLIED BY PRECEDING", // S0018N.ACT
                        "PAGE NUMBERS RESERVED FROM THIS EDITION FOR MISSING\r\nSECTION SUPPLIED BY PRECEDING", // S0018N.ACT
                        "SW: OK", // S0057M.ACT
                        "m:ok", // S0057M.ACT
                        "A FIRST ONE\nMISSING HERE?", // S0057M.ACT
                        "A FIRST ONE\r\nMISSING HERE?", // S0057M.ACT
                        "THE INITIAL PART OF THIS TEXT WAS INPUT BY THE SERA MEY LIBRARY IN\nTIBETAN FONT AND NEEDS TO BE REDONE BY DOUBLE INPUT", // S0195A1.INC
                        "THE INITIAL PART OF THIS TEXT WAS INPUT BY THE SERA MEY LIBRARY IN\r\nTIBETAN FONT AND NEEDS TO BE REDONE BY DOUBLE INPUT", // S0195A1.INC
                    };
                    boolean foundOne = false;
                    for (int ec = 0; ec < englishComments.length; ec++) {
                        if (i + 2 + englishComments[ec].length() <= sl
                            && (s.substring(i, i + 2 + englishComments[ec].length()).equals("[" + englishComments[ec] + "]")
                                || s.substring(i, i + 2 + englishComments[ec].length()).equals("[" + englishComments[ec] + "]"))) {
                            al.add(new TString("[#" + englishComments[ec] + "]",
                                               TString.COMMENT));
                            startOfString = i + 2 + englishComments[ec].length();
                            i = startOfString - 1;
                            foundOne = true;
                            break;
                        }
                    }
                    if (!foundOne && i+1 < sl && s.charAt(i+1) == '*') {
                        // Identify [*LINE BREAK?] as an English
                        // correction.  Every correction not on this
                        // list is considered to be Tibetan.
                        // FIXME: make this extensible via a config
                        // file or at least a System property (which
                        // could be a comma-separated list of these
                        // creatures.
                        
                        // If "LINE" is in the list below, then [*
                        // LINE], [* LINE?], [*LINE], [*LINE?], [*
                        // LINE OUT ?], etc. will be considered
                        // English corrections.  I.e., whitespace
                        // before and anything after doesn't prevent a
                        // match.
                        String[] englishCorrections = new String[] {
                            "LINE", // KD0001I1.ACT
                            "DATA", // KL0009I2.INC
                            "BLANK", // KL0009I2.INC
                            "NOTE", // R0001F.ACM
                            "alternate", // R0018F.ACE
                            "02101-02150 missing", // R1003A3.INC
                            "51501-51550 missing", // R1003A52.ACT
                            "BRTAGS ETC", // S0002N.ACT
                            "TSAN, ETC", // S0015N.ACT
                            "SNYOMS, THROUGHOUT", // S0016N.ACT
                            "KYIS ETC", // S0019N.ACT
                            "MISSING", // S0455M.ACT
                            "this", // S6850I1B.ALT
                            "THIS", // S0057M.ACT
                        };
                        int begin;
                        for (begin = i+2; begin < sl; begin++) {
                            if (!isWhitespace(s.charAt(begin)))
                                break;
                        }
                        int end;
                        for (end = i+2; end < sl; end++) {
                            if (s.charAt(end) == ']')
                                break;
                        }
                        int realEnd = end;
                        if (end < sl && s.charAt(end-1) == '?')
                            --realEnd;
                        if (end < sl && begin < realEnd) {
                            String interestingSubstring
                                = s.substring(begin, realEnd);
                            for (int ec = 0; ec < englishCorrections.length; ec++) {
                                if (interestingSubstring.startsWith(englishCorrections[ec])) {
                                    al.add(new TString(s.substring(i, i+2),
                                                       TString.CORRECTION_START));
                                    al.add(new TString(s.substring(i+2, realEnd),
                                                       TString.LATIN));
                                    if (s.charAt(end - 1) == '?') {
                                        al.add(new TString(s.substring(end-1, end+1),
                                                           TString.POSSIBLE_CORRECTION));
                                    } else {
                                        al.add(new TString(s.substring(end, end+1),
                                                           TString.PROBABLE_CORRECTION));
                                    }
                                    foundOne = true;
                                    startOfString = end+1;
                                    i = startOfString - 1;
                                    break;
                                }
                            }
                        }
                    }
                    if (foundOne)
                        break;
                }
                if (null != thingy) {
                    al.add(new TString(thingy,
                                       currentType));
                    startOfString = i + thingy.length();
                    i = startOfString - 1;
                } else {
                    if (i + 1 < sl) {
                        char nextCh = s.charAt(i+1);
                        if ('*' == nextCh) {
                            currentType = TString.CORRECTION_START;
                            bracketTypeStack.push(new Integer(currentType));
                            al.add(new TString(s.substring(i, i+2),
                                               TString.CORRECTION_START));
                            currentType = TString.ERROR;
                            startOfString = i+2;
                            i = startOfString - 1;
                            break;
                        } else if ('#' == nextCh) {
                            currentType = TString.COMMENT;
                            bracketTypeStack.push(new Integer(currentType));
                            break;
                        }
                    }
                    // This is an error.  Sometimes [COMMENTS APPEAR
                    // WITHOUT # MARKS].  Though "... [" could cause
                    // this too.
                    if (waitingForMatchingIllegalClose) {
                        al.add(new TString("Found a truly unmatched open bracket, [ or {, prior to this current illegal open bracket.",
                                           TString.ERROR));
                        if (null != errors) {
                            errors.append("Offset " + i + ((numNewlines == 0) ? "" : (" or maybe " + (i-numNewlines))) + ": "
                                          + "Found a truly unmatched open bracket, [ or {, prior to this current illegal open bracket.\n");
                        }
                        if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
                    }
                    waitingForMatchingIllegalClose = true;
                    if (null != errors) {
                        String inContext = s.substring(i, i+Math.min(sl-i, 10));
                        if (inContext.indexOf("\r") >= 0) {
                            inContext = inContext.substring(0, inContext.indexOf("\r"));
                        } else if (inContext.indexOf("\n") >= 0) {
                            inContext = inContext.substring(0, inContext.indexOf("\n"));
                        } else {
                            if (sl-i > 10) {
                                inContext = inContext + "...";
                            }
                        }
                        al.add(new TString("Found an illegal open bracket (in context, this is " + inContext + ").  Perhaps there is a [#COMMENT] written incorrectly as [COMMENT], or a [*CORRECTION] written incorrectly as [CORRECTION], or an unmatched open bracket?",
                                           TString.ERROR));
                        errors.append("Offset " + i + ((numNewlines == 0) ? "" : (" or maybe " + (i-numNewlines))) + ": "
                                      + "Found an illegal open bracket (in context, this is " + inContext + ").  Perhaps there is a [#COMMENT] written incorrectly as [COMMENT], or a [*CORRECTION] written incorrectly as [CORRECTION], or an unmatched open bracket?\n");
                        if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
                    }
                    startOfString = i + 1;
                    currentType = TString.ERROR;
                }
                break; // end '[','{' case

            case '@':
                // This definitely indicates a new token.
                if (startOfString < i) {
                    al.add(new TString(s.substring(startOfString, i),
                                       currentType));
                    startOfString = i;
                    currentType = TString.ERROR;
                }

                // We look for {@N{AB}, @NN{AB}, ..., @NNNNNN{AB}},
                // {@[N{AB}], @[NN{AB}], ..., @[NNNNNN{AB}]},
                // {@N{AB}.N, @NN{AB}.N, ..., @NNNNNN{AB}.N}, {@N,
                // @NN, ..., @NNNNNN}, and {@{AB}N, @{AB}NN,
                // ... @{AB}NNNNNN} only, that is from one to six
                // digits.  Each of these folio marker format occurs
                // in practice.
                for (int numdigits = 6; numdigits >= 1; numdigits--) {
                    // @NNN{AB} and @NNN{AB}.N cases:
                    if (i+numdigits+1 < sl
                        && (s.charAt(i+numdigits+1) == 'A' || s.charAt(i+numdigits+1) == 'B')) {
                        boolean allAreNumeric = true;
                        for (int k = 1; k <= numdigits; k++) {
                            if (!isNumeric(s.charAt(i+k))) {
                                allAreNumeric = false;
                                break;
                            }
                        }
                        if (allAreNumeric) {
                            // Is this "@012B " or "@012B.3 "?
                            int extra;
                            if (i+numdigits+2 < sl && s.charAt(i+numdigits+2) == '.') {
                                if (!(i+numdigits+4 < sl && isNumeric(s.charAt(i+numdigits+3))
                                      && !isNumeric(s.charAt(i+numdigits+4)))) {
                                    String inContext = s.substring(i, i+Math.min(sl-i, 10));
                                    if (inContext.indexOf("\r") >= 0) {
                                        inContext = inContext.substring(0, inContext.indexOf("\r"));
                                    } else if (inContext.indexOf("\n") >= 0) {
                                        inContext = inContext.substring(0, inContext.indexOf("\n"));
                                    } else {
                                        if (sl-i > 10) {
                                            inContext = inContext + "...";
                                        }
                                    }
                                    al.add(new TString("Found an illegal at sign, @ (in context, this is " + inContext + ").  This folio marker has a period, '.', at the end of it, which is illegal.",
                                                       TString.ERROR));
                                    if (null != errors)
                                        errors.append("Offset " + i + ((numNewlines == 0) ? "" : (" or maybe " + (i-numNewlines))) + ": "
                                                      + "Found an illegal at sign, @ (in context, this is " + inContext + ").  This folio marker has a period, '.', at the end of it, which is illegal.\n");
                                    if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
                                    startOfString = i+numdigits+3;
                                    i = startOfString - 1;
                                    currentType = TString.ERROR;
                                    break;
                                }
                                if (i+numdigits+4 < sl && (s.charAt(i+numdigits+4) == '.' || s.charAt(i+numdigits+4) == 'A' || s.charAt(i+numdigits+4) == 'B' || s.charAt(i+numdigits+4) == 'a' || s.charAt(i+numdigits+4) == 'b' || isNumeric(s.charAt(i+numdigits+4)))) {
                                    String inContext = s.substring(i, i+Math.min(sl-i, 10));
                                    if (inContext.indexOf("\r") >= 0) {
                                        inContext = inContext.substring(0, inContext.indexOf("\r"));
                                    } else if (inContext.indexOf("\n") >= 0) {
                                        inContext = inContext.substring(0, inContext.indexOf("\n"));
                                    } else {
                                        if (sl-i > 10) {
                                            inContext = inContext + "...";
                                        }
                                    }
                                    al.add(new TString("Found an illegal at sign, @ (in context, this is " + inContext + ").  This folio marker is not followed by whitespace, as is expected.",
                                                       TString.ERROR));
                                    if (null != errors)
                                        errors.append("Offset " + i + ((numNewlines == 0) ? "" : (" or maybe " + (i-numNewlines))) + ": "
                                                      + "Found an illegal at sign, @ (in context, this is " + inContext + ").  This folio marker is not followed by whitespace, as is expected.\n");
                                    if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
                                    startOfString = i+1; // FIXME: skip over more?  test this code.
                                    currentType = TString.ERROR;
                                    break;
                                }
                                extra = 4;
                            } else {
                                extra = 2;
                            }
                            al.add(new TString(s.substring(i, i+numdigits+extra),
                                               TString.FOLIO_MARKER));
                            startOfString = i+numdigits+extra;
                            i = startOfString - 1;
                            currentType = TString.ERROR;
                            break;
                        }
                    }
                    
                    // @{AB}NNN case:
                    if (i+numdigits+1 < sl
                        && (s.charAt(i+1) == 'A' || s.charAt(i+1) == 'B')) {
                        boolean allAreNumeric = true;
                        for (int k = 1; k <= numdigits; k++) {
                            if (!isNumeric(s.charAt(i+1+k))) {
                                allAreNumeric = false;
                                break;
                            }
                        }
                        if (allAreNumeric) {
                            al.add(new TString(s.substring(i, i+numdigits+2),
                                               TString.FOLIO_MARKER));
                            startOfString = i+numdigits+2;
                            i = startOfString - 1;
                            currentType = TString.ERROR;
                            break;
                        }
                    }
                    
                    // @[NNN{AB}] case:
                    if (i+numdigits+3 < sl
                        && s.charAt(i+1) == '[' && s.charAt(i+numdigits+3) == ']'
                        && (s.charAt(i+numdigits+2) == 'A' || s.charAt(i+numdigits+2) == 'B')) {
                        boolean allAreNumeric = true;
                        for (int k = 1; k <= numdigits; k++) {
                            if (!isNumeric(s.charAt(i+1+k))) {
                                allAreNumeric = false;
                                break;
                            }
                        }
                        if (allAreNumeric) {
                            al.add(new TString(s.substring(i, i+numdigits+4),
                                               TString.FOLIO_MARKER));
                            startOfString = i+numdigits+4;
                            i = startOfString - 1;
                            currentType = TString.ERROR;
                            break;
                        }
                    }
                    
                    // This case, @NNN, must come after the @NNN{AB} case.
                    if (i+numdigits+1 < sl && (s.charAt(i+numdigits+1) == ' '
                                               || s.charAt(i+numdigits+1) == '\n'
                                               || s.charAt(i+numdigits+1) == '\r')) {
                        boolean allAreNumeric = true;
                        for (int k = 1; k <= numdigits; k++) {
                            if (!isNumeric(s.charAt(i+k))) {
                                allAreNumeric = false;
                                break;
                            }
                        }
                        if (allAreNumeric) {
                            al.add(new TString(s.substring(i, i+numdigits+1),
                                               TString.FOLIO_MARKER));
                            startOfString = i+numdigits+1;
                            i = startOfString - 1;
                            currentType = TString.ERROR;
                            break;
                        }
                    }
                }
                if (startOfString == i) {
                    String inContext = s.substring(i, i+Math.min(sl-i, 10));
                    if (inContext.indexOf("\r") >= 0) {
                        inContext = inContext.substring(0, inContext.indexOf("\r"));
                    } else if (inContext.indexOf("\n") >= 0) {
                        inContext = inContext.substring(0, inContext.indexOf("\n"));
                    } else {
                        if (sl-i > 10) {
                            inContext = inContext + "...";
                        }
                    }
                    al.add(new TString("Found an illegal at sign, @ (in context, this is " + inContext + ").  @012B is an example of a legal folio marker.",
                                       TString.ERROR));
                    if (null != errors)
                        errors.append("Offset " + i + ((numNewlines == 0) ? "" : (" or maybe " + (i-numNewlines))) + ": "
                                      + "Found an illegal at sign, @ (in context, this is " + inContext + ").  @012B is an example of a legal folio marker.\n");
                    if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
                    startOfString = i+1;
                    currentType = TString.ERROR;
                }
                break; // end '@' case

            case '/':
                // This definitely indicates a new token.
                if (startOfString < i) {
                    al.add(new TString(s.substring(startOfString, i),
                                       currentType));
                    startOfString = i;
                    currentType = TString.ERROR;
                }

                if (startSlashIndex >= 0) {
                    if (startSlashIndex + 1 == i) {
                        /* //NYA\\ appears in ACIP input, and I think
                         * it means /NYA/.  We warn about // for this
                         * reason.  \\ causes a tsheg-bar error. */
                        al.add(new TString("Found //, which could be legal (the Unicode would be \\u0F3C\\u0F3D), but is likely in an illegal construct like //NYA\\\\.",
                                           TString.ERROR));
                        if (errors != null) {
                            errors.append("Offset " + i + ((numNewlines == 0) ? "" : (" or maybe " + (i-numNewlines))) + ": "
                                          + "Found //, which could be legal (the Unicode would be \\u0F3C\\u0F3D), but is likely in an illegal construct like //NYA\\\\.\n");
                        }
                        if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
                    }
                    al.add(new TString(s.substring(i, i+1),
                                       TString.END_SLASH));
                    startOfString = i+1;
                    currentType = TString.ERROR;
                    startSlashIndex = -1;
                } else {
                    startSlashIndex = i;
                    al.add(new TString(s.substring(i, i+1),
                                       TString.START_SLASH));
                    startOfString = i+1;
                    currentType = TString.ERROR;
                }
                break; // end '/' case

            case '(':
            case ')':
                // This definitely indicates a new token.
                if (startOfString < i) {
                    al.add(new TString(s.substring(startOfString, i),
                                       currentType));
                    startOfString = i;
                    currentType = TString.ERROR;
                }

                // We do not support nesting like (NYA (BA)).

                if (startParenIndex >= 0) {
                    if (ch == '(') {
                        al.add(new TString("Found an illegal open parenthesis, (.  Nesting of parentheses is not allowed.",
                                           TString.ERROR));
                        if (null != errors)
                            errors.append("Offset " + i + ((numNewlines == 0) ? "" : (" or maybe " + (i-numNewlines))) + ": "
                                          + "Found an illegal open parenthesis, (.  Nesting of parentheses is not allowed.\n");
                        if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
                    } else {
                        al.add(new TString(s.substring(i, i+1), TString.END_PAREN));
                        startParenIndex = -1;
                    }
                    startOfString = i+1;
                    currentType = TString.ERROR;
                } else {
                    if (ch == ')') {
                        al.add(new TString("Unexpected closing parenthesis, ), found.",
                                           TString.ERROR));
                        if (null != errors)
                            errors.append("Offset " + i + ((numNewlines == 0) ? "" : (" or maybe " + (i-numNewlines))) + ": "
                                          + "Unexpected closing parenthesis, ), found.\n");
                        if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
                    } else {
                        startParenIndex = i;
                        al.add(new TString(s.substring(i, i+1), TString.START_PAREN));
                    }
                    startOfString = i+1;
                    currentType = TString.ERROR;
                }
                break; // end '(',')' case

            case '?':
                if (bracketTypeStack.empty() || i+1>=sl
                    || (s.charAt(i+1) != ']' && s.charAt(i+1) != '}')) {
                    // The tsheg bar ends here; new token.
                    if (startOfString < i) {
                        al.add(new TString(s.substring(startOfString, i),
                                           currentType));
                    }
                    al.add(new TString(s.substring(i, i+1),
                                       TString.QUESTION));
                    startOfString = i+1;
                    currentType = TString.ERROR;
                } // else this is [*TR'A ?] or the like.
                break; // end '?' case


            case '.':
                // This definitely indicates a new token.
                if (startOfString < i) {
                    al.add(new TString(s.substring(startOfString, i),
                                       currentType));
                    startOfString = i;
                    currentType = TString.ERROR;
                }
                // . is used for a non-breaking tsheg, such as in
                // {NGO.,} and {....,DAM}.  We give a warning unless ,
                // or ., or [A-Za-z] follows '.'.
                al.add(new TString(s.substring(i, i+1),
                                   TString.TIBETAN_PUNCTUATION));
                if (!(i + 1 < sl
                      && (s.charAt(i+1) == '.' || s.charAt(i+1) == ','
                          || (s.charAt(i+1) == '\r' || s.charAt(i+1) == '\n')
                          || (s.charAt(i+1) >= 'a' && s.charAt(i+1) <= 'z')
                          || (s.charAt(i+1) >= 'A' && s.charAt(i+1) <= 'Z')))) {
                    al.add(new TString("A non-breaking tsheg, '.', appeared, but not like \"...,\" or \".,\" or \".dA\" or \".DA\".",
                                       TString.WARNING));
                }
                startOfString = i+1;
                break; // end '.' case

            // Classic tsheg bar enders:
            case ' ':
            case '\t':
            case '\r':
            case '\n':
            case ',':
            case '*':
            case ';':
            case '`':
            case '#':
            case '%':
            case 'x':
            case 'o':

                boolean legalTshegBarAdornment = false;
                // The tsheg bar ends here; new token.
                if (startOfString < i) {
                    if (currentType == TString.TIBETAN_NON_PUNCTUATION
                        && isTshegBarAdornment(ch))
                        legalTshegBarAdornment = true;
                    al.add(new TString(s.substring(startOfString, i),
                                       currentType));
                }

                // Insert a tsheg if necessary.  ACIP files aren't
                // careful, so "KA\r\n" and "GA\n" appear where "KA
                // \r\n" and "GA \n" should appear.
                if (('\r' == ch
                     || ('\n' == ch && i > 0 && s.charAt(i - 1) != '\r'))
                    && !al.isEmpty()
                    && (((TString)al.get(al.size() - 1)).getType() == TString.TIBETAN_NON_PUNCTUATION
                        || ((TString)al.get(al.size() - 1)).getType() == TString.TSHEG_BAR_ADORNMENT)) {
                    al.add(new TString(" ", TString.TIBETAN_PUNCTUATION));
                }

                // "DANG,\nLHAG" is really "DANG, LHAG".  But always?  Not if you have "MDO,\n\nKA...".
                if (('\r' == ch
                     || ('\n' == ch && i > 0 && s.charAt(i - 1) != '\r'))
                    && !al.isEmpty()
                    && (((TString)al.get(al.size() - 1)).getType() == TString.TIBETAN_PUNCTUATION
                        || ((TString)al.get(al.size() - 1)).getType() == TString.TSHEG_BAR_ADORNMENT)
                    && ((TString)al.get(al.size() - 1)).getText().equals(",")
                    && s.charAt(i-1) == ','
                    && (i + (('\r' == ch) ? 2 : 1) < sl
                        && (s.charAt(i+(('\r' == ch) ? 2 : 1)) != ch))) {
                    al.add(new TString(" ", TString.TIBETAN_PUNCTUATION));
                }

                // Don't add in a "\r\n" or "\n" unless there's a
                // blank line.
                boolean rn = false;
                boolean realNewline = false;
                if (('\n' != ch && '\r' != ch)
                    || (realNewline
                        = ((rn = ('\n' == ch && i >= 3 && s.charAt(i-3) == '\r' && s.charAt(i-2) == '\n' && s.charAt(i-1) == '\r'))
                           || ('\n' == ch && i >= 1 && s.charAt(i-1) == '\n')))) {
                    for (int h = 0; h < (realNewline ? 2 : 1); h++) {
                        if (isTshegBarAdornment(ch) && !legalTshegBarAdornment) {
                            al.add(new TString("The ACIP " + ch + " must be glued to the end of a tsheg bar, but this one was not",
                                               TString.ERROR));
                        } else {
                            al.add(new TString(rn ? s.substring(i - 1, i+1) : s.substring(i, i+1),
                                               (legalTshegBarAdornment
                                                ? TString.TSHEG_BAR_ADORNMENT
                                                : TString.TIBETAN_PUNCTUATION)));
                        }
                    }
                }
                if ('%' == ch) {
                    al.add(new TString("The ACIP {%} is treated by this converter as U+0F35, but sometimes might represent U+0F14 in practice",
                                       TString.WARNING));
                }
                startOfString = i+1;
                currentType = TString.ERROR;
                break; // end TIBETAN_PUNCTUATION case

            default:
                if (!bracketTypeStack.empty()) {
                    int stackTop = ((Integer)bracketTypeStack.peek()).intValue();
                    if (TString.CORRECTION_START == stackTop && '?' == ch) {
                        // allow it through...
                        break;
                    }
                }
                if (i+1 == sl && 26 == (int)ch)
                    // Silently allow the last character to be ^Z,
                    // which just marks end of file.
                    break;
                if (!(isNumeric(ch) || isAlpha(ch))) {
                    if (startOfString < i) {
                        al.add(new TString(s.substring(startOfString, i),
                                           currentType));
                    }
                    if ((int)ch == 65533) {
                        al.add(new TString("Found an illegal, unprintable character.",
                                           TString.ERROR));
                        if (null != errors)
                            errors.append("Offset " + i + ((numNewlines == 0) ? "" : (" or maybe " + (i-numNewlines))) + ": "
                                          + "Found an illegal, unprintable character.\n");
                    } else if ('\\' == ch) {
                        al.add(new TString("Found a Sanskrit virama, \\, but the converter currently doesn't treat these properly.  Sorry!  Please do complain to the maintainers.",
                                           TString.ERROR));
                        if (null != errors)
                            errors.append("Offset " + i + ((numNewlines == 0) ? "" : (" or maybe " + (i-numNewlines))) + ": "
                                          + "Found a Sanskrit virama, \\, but the converter currently doesn't treat these properly.  Sorry!  Please do complain to the maintainers.\n");
                    } else {
                        al.add(new TString("Found an illegal character, " + ch + ", with ordinal " + (int)ch + ".",
                                           TString.ERROR));
                        if (null != errors)
                            errors.append("Offset " + i + ((numNewlines == 0) ? "" : (" or maybe " + (i-numNewlines))) + ": "
                                          + "Found an illegal character, " + ch + ", with ordinal " + (int)ch + ".\n");
                    }
                    if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
                    startOfString = i+1;
                    currentType = TString.ERROR;
                } else {
                    // Continue through the loop.
                    if (TString.ERROR == currentType)
                        currentType = TString.TIBETAN_NON_PUNCTUATION;
                }
                break; // end default case
            }
        }
        if (startOfString < sl) {
            al.add(new TString(s.substring(startOfString, sl),
                               currentType));
        }
        if (waitingForMatchingIllegalClose) {
            al.add(new TString("UNEXPECTED END OF INPUT",
                               TString.ERROR));
            if (null != errors) {
                errors.append("Offset END: "
                              + "Truly unmatched open bracket found.\n");
            }
            if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
        }
        if (!bracketTypeStack.empty()) {
            al.add(new TString("Unmatched open bracket found.  A " + ((TString.COMMENT == currentType) ? "comment" : "correction") + " does not terminate.",
                               TString.ERROR));
            if (null != errors) {
                errors.append("Offset END: "
                              + "Unmatched open bracket found.  A " + ((TString.COMMENT == currentType) ? "comment" : "correction") + " does not terminate.\n");
            }
            if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
        }
        if (startSlashIndex >= 0) {
            al.add(new TString("Slashes are supposed to occur in pairs, but the input had an unmatched '/' character.",
                               TString.ERROR));
            if (null != errors)
                errors.append("Offset END: "
                              + "Slashes are supposed to occur in pairs, but the input had an unmatched '/' character.\n");
            if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
        }
        if (startParenIndex >= 0) {
            al.add(new TString("Parentheses are supposed to occur in pairs, but the input had an unmatched parenthesis.",
                               TString.ERROR));
            if (null != errors)
                errors.append("Offset END: "
                              + "Unmatched open parenthesis, (, found.\n");
            if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
        }
        return al;
    }

    /** See implementation. */
    private static boolean isNumeric(char ch) {
        return ch >= '0' && ch <= '9';
    }

    /** See implementation. */
    private static boolean isWhitespace(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
    }

    /** See implementation. */
    private static boolean isTshegBarAdornment(char ch) {
        return (ch == '%' || ch == 'o' || ch == 'x');
    }

    /** See implementation. */
    private static boolean isAlpha(char ch) {
        return ch == '\'' // 23rd consonant

            // combining punctuation, vowels:
            || ch == 'm'
            || ch == ':'
            || ch == '^'
            // FIXME: we must treat this guy like a vowel, a special vowel that numerals can take on.  Until then, warn.  See bug 838588          || ch == '\\'

            || ch == '-'
            || ch == '+'
            || ((ch >= 'A' && ch <= 'Z') && ch != 'X' && ch != 'Q' && ch != 'F')
            || ch == 'i'
            || ch == 't'
            || ch == 'h'
            || ch == 'd'
            || ch == 'n'
            || ch == 's'
            || ch == 'h';
    }
}
