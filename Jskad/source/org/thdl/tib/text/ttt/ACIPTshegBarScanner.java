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
     *  list of ACIPStrings that is the scan. <p>FIXME: not so
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
     *  from the result, though.  Returns a list of ACIPStrings that
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

    /** Returns a list of {@link ACIPString ACIPStrings} corresponding
     *  to s, possibly the empty list (when the empty string is the
     *  input).  Each String is either a Latin comment, some Latin
     *  text, a tsheg bar (minus the tsheg or shad or whatever), a
     *  String of inter-tsheg-bar punctuation, etc.
     *
     *  <p>This not only scans; it finds all the errors and warnings a
     *  parser would too, like "NYA x" and "(" and ")" and "/NYA" etc.
     *  It puts those in as ACIPStrings with type {@link
     *  ACIPString#ERROR} or {@link ACIPString#WARNING}, and also, if
     *  errors is non-null, appends helpful messages to errors, each
     *  followed by a '\n'.
     *  @param s the ACIP text
     *  @param errors if non-null, the buffer to which to append error
     *  messages (DLC FIXME: cludge, just get this info by scanning
     *  the result for ACIPString.ERROR (and maybe ACIPString.WARNING,
     *  if you care about warnings), but then we'd have to put the
     *  Offset info in the ACIPString)
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
        int currentType = ACIPString.ERROR;
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
            if (ACIPString.COMMENT == currentType && ch != ']') {
                if ('[' == ch) {
                    al.add(new ACIPString("Found an open bracket within a [#COMMENT]-style comment.  Brackets may not appear in comments.\n",
                                          ACIPString.ERROR));
                    if (null != errors)
                        errors.append("Offset " + i + " or maybe " + (i-numNewlines) + ": "
                                      + "Found an open bracket within a [#COMMENT]-style comment.  Brackets may not appear in comments.\n");
                    if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
                }
                continue;
            }
            switch (ch) {
            case '}':
            case ']':
                if (bracketTypeStack.empty()) {
                    // Error.
                    if (startOfString < i) {
                        al.add(new ACIPString(s.substring(startOfString, i),
                                              currentType));
                    }
                    if (!waitingForMatchingIllegalClose) {
                        al.add(new ACIPString("Found a truly unmatched close bracket, " + s.substring(i, i+1),
                                              ACIPString.ERROR));
                        if (null != errors) {
                            errors.append("Offset " + i + " or maybe " + (i-numNewlines) + ": "
                                          + "Found a truly unmatched close bracket, ] or }.\n");
                        }
                        if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
                    }
                    waitingForMatchingIllegalClose = false;
                    al.add(new ACIPString("Found a closing bracket without a matching open bracket.  Perhaps a [#COMMENT] incorrectly written as [COMMENT], or a [*CORRECTION] written incorrectly as [CORRECTION], caused this.",
                                          ACIPString.ERROR));
                    if (null != errors)
                        errors.append("Offset " + i + " or maybe " + (i-numNewlines) + ": "
                                      + "Found a closing bracket without a matching open bracket.  Perhaps a [#COMMENT] incorrectly written as [COMMENT], or a [*CORRECTION] written incorrectly as [CORRECTION], caused this.\n");
                    if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
                    startOfString = i+1;
                    currentType = ACIPString.ERROR;
                } else {
                    int stackTop = ((Integer)bracketTypeStack.pop()).intValue();

                    int end = startOfString;
                    if (ACIPString.CORRECTION_START == stackTop) {

                        // This definitely indicates a new token.
                        char prevCh = s.charAt(i-1);
                        if (prevCh == '?')
                            end = i - 1;
                        else
                            end = i;
                        if (startOfString < end) {
                            al.add(new ACIPString(s.substring(startOfString, end),
                                                  currentType));
                        }

                        if ('?' != prevCh) {
                            currentType = ACIPString.PROBABLE_CORRECTION;
                        } else {
                            currentType = ACIPString.POSSIBLE_CORRECTION;
                        }
                    }
                    al.add(new ACIPString(s.substring(end, i+1), currentType));
                    startOfString = i+1;
                    currentType = ACIPString.ERROR;
                }
                break; // end ']','}' case

            case '{': // NOTE WELL: KX0016I.ACT, KD0095M.ACT, and a
                      // host of other ACIP files use {} brackets like
                      // [] brackets.  I treat both the same.
            case '[':
                // This definitely indicates a new token.
                if (startOfString < i) {
                    al.add(new ACIPString(s.substring(startOfString, i),
                                          currentType));
                    startOfString = i;
                    currentType = ACIPString.ERROR;
                }
                String thingy = null;

                if (i + "[DD]".length() <= sl
                    && (s.substring(i, i + "[DD]".length()).equals("[DD]")
                        || s.substring(i, i + "[DD]".length()).equals("{DD}"))) {
                    thingy = "[DD]";
                    currentType = ACIPString.DD;
                } else if (i + "[DD1]".length() <= sl
                           && (s.substring(i, i + "[DD1]".length()).equals("[DD1]")
                               || s.substring(i, i + "[DD1]".length()).equals("{DD1}"))) {
                    thingy = "[DD1]";
                    currentType = ACIPString.DD;
                } else if (i + "[DD2]".length() <= sl
                           && (s.substring(i, i + "[DD2]".length()).equals("[DD2]")
                               || s.substring(i, i + "[DD2]".length()).equals("{DD2}"))) {
                    thingy = "[DD2]";
                    currentType = ACIPString.DD;
                } else if (i + "[DDD]".length() <= sl
                           && (s.substring(i, i + "[DDD]".length()).equals("[DDD]")
                               || s.substring(i, i + "[DDD]".length()).equals("{DDD}"))) {
                    thingy = "[DDD]";
                    currentType = ACIPString.DD;
                } else if (i + "[DR]".length() <= sl
                           && (s.substring(i, i + "[DR]".length()).equals("[DR]")
                               || s.substring(i, i + "[DR]".length()).equals("{DR}"))) {
                    thingy = "[DR]";
                    currentType = ACIPString.DR;
                } else if (i + "[LS]".length() <= sl
                           && (s.substring(i, i + "[LS]".length()).equals("[LS]")
                               || s.substring(i, i + "[LS]".length()).equals("{LS}"))) {
                    thingy = "[LS]";
                    currentType = ACIPString.LS;
                } else if (i + "[BP]".length() <= sl
                           && (s.substring(i, i + "[BP]".length()).equals("[BP]")
                               || s.substring(i, i + "[BP]".length()).equals("{BP}"))) {
                    thingy = "[BP]";
                    currentType = ACIPString.BP;
                } else if (i + "[BLANK PAGE]".length() <= sl
                           && (s.substring(i, i + "[BLANK PAGE]".length()).equals("[BLANK PAGE]")
                               || s.substring(i, i + "[BLANK PAGE]".length()).equals("{BLANK PAGE}"))) {
                    thingy = "[BLANK PAGE]";
                    currentType = ACIPString.BP;
                } else if (i + "[ BP ]".length() <= sl
                           && (s.substring(i, i + "[ BP ]".length()).equals("[ BP ]")
                               || s.substring(i, i + "[ BP ]".length()).equals("{ BP }"))) {
                    thingy = "{ BP }"; // found in TD3790E2.ACT
                    currentType = ACIPString.BP;
                } else if (i + "[ DD ]".length() <= sl
                           && (s.substring(i, i + "[ DD ]".length()).equals("[ DD ]")
                               || s.substring(i, i + "[ DD ]".length()).equals("{ DD }"))) {
                    thingy = "{ DD }"; // found in TD3790E2.ACT
                    currentType = ACIPString.DD;
                } else if (i + "[?]".length() <= sl
                           && (s.substring(i, i + "[?]".length()).equals("[?]")
                               || s.substring(i, i + "[?]".length()).equals("{?}"))) {
                    thingy = "[?]";
                    currentType = ACIPString.QUESTION;
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
                            al.add(new ACIPString("[#" + englishComments[ec] + "]",
                                                  ACIPString.COMMENT));
                            startOfString = i + 2 + englishComments[ec].length();
                            i = startOfString - 1;
                            foundOne = true;
                            break;
                        }
                    }
                    if (!foundOne && i+1 < sl && s.charAt(i+1) == '*') {
                        // Identify [*LINE BREAK?] as an English
                        // correction.  Every correction not on this
                        // list is considered to be Tibetan.  DLC
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
                                    al.add(new ACIPString(s.substring(i, i+2),
                                                          ACIPString.CORRECTION_START));
                                    al.add(new ACIPString(s.substring(i+2, realEnd),
                                                          ACIPString.LATIN));
                                    if (s.charAt(end - 1) == '?') {
                                        al.add(new ACIPString(s.substring(end-1, end+1),
                                                              ACIPString.POSSIBLE_CORRECTION));
                                    } else {
                                        al.add(new ACIPString(s.substring(end, end+1),
                                                              ACIPString.PROBABLE_CORRECTION));
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
                    al.add(new ACIPString(thingy,
                                          currentType));
                    startOfString = i + thingy.length();
                    i = startOfString - 1;
                } else {
                    if (i + 1 < sl) {
                        char nextCh = s.charAt(i+1);
                        if ('*' == nextCh) {
                            currentType = ACIPString.CORRECTION_START;
                            bracketTypeStack.push(new Integer(currentType));
                            al.add(new ACIPString(s.substring(i, i+2),
                                                  ACIPString.CORRECTION_START));
                            currentType = ACIPString.ERROR;
                            startOfString = i+2;
                            i = startOfString - 1;
                            break;
                        } else if ('#' == nextCh) {
                            currentType = ACIPString.COMMENT;
                            bracketTypeStack.push(new Integer(currentType));
                            break;
                        }
                    }
                    // This is an error.  Sometimes [COMMENTS APPEAR
                    // WITHOUT # MARKS].  Though "... [" could cause
                    // this too.
                    if (waitingForMatchingIllegalClose) {
                        al.add(new ACIPString("Found a truly unmatched open bracket, [ or {, prior to this current illegal open bracket.",
                                              ACIPString.ERROR));
                        if (null != errors) {
                            errors.append("Offset " + i + " or maybe " + (i-numNewlines) + ": "
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
                        al.add(new ACIPString("Found an illegal open bracket (in context, this is " + inContext + ").  Perhaps there is a [#COMMENT] written incorrectly as [COMMENT], or a [*CORRECTION] written incorrectly as [CORRECTION], or an unmatched open bracket?",
                                              ACIPString.ERROR));
                        errors.append("Offset " + i + " or maybe " + (i-numNewlines) + ": "
                                      + "Found an illegal open bracket (in context, this is " + inContext + ").  Perhaps there is a [#COMMENT] written incorrectly as [COMMENT], or a [*CORRECTION] written incorrectly as [CORRECTION], or an unmatched open bracket?\n");
                        if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
                    }
                    startOfString = i + 1;
                    currentType = ACIPString.ERROR;
                }
                break; // end '[','{' case

            case '@':
                // This definitely indicates a new token.
                if (startOfString < i) {
                    al.add(new ACIPString(s.substring(startOfString, i),
                                          currentType));
                    startOfString = i;
                    currentType = ACIPString.ERROR;
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
                                    al.add(new ACIPString("Found an illegal at sign, @ (in context, this is " + inContext + ").  This folio marker has a period, '.', at the end of it, which is illegal.",
                                                          ACIPString.ERROR));
                                    if (null != errors)
                                        errors.append("Offset " + i + " or maybe " + (i-numNewlines) + ": "
                                                      + "Found an illegal at sign, @ (in context, this is " + inContext + ").  This folio marker has a period, '.', at the end of it, which is illegal.\n");
                                    if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
                                    startOfString = i+numdigits+3;
                                    i = startOfString - 1;
                                    currentType = ACIPString.ERROR;
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
                                    al.add(new ACIPString("Found an illegal at sign, @ (in context, this is " + inContext + ").  This folio marker is not followed by whitespace, as is expected.",
                                                          ACIPString.ERROR));
                                    if (null != errors)
                                        errors.append("Offset " + i + " or maybe " + (i-numNewlines) + ": "
                                                      + "Found an illegal at sign, @ (in context, this is " + inContext + ").  This folio marker is not followed by whitespace, as is expected.\n");
                                    if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
                                    startOfString = i+1; // DLC FIXME: skip over more?
                                    currentType = ACIPString.ERROR;
                                    break;
                                }
                                extra = 4;
                            } else {
                                extra = 2;
                            }
                            al.add(new ACIPString(s.substring(i, i+numdigits+extra),
                                                  ACIPString.FOLIO_MARKER));
                            startOfString = i+numdigits+extra;
                            i = startOfString - 1;
                            currentType = ACIPString.ERROR;
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
                            al.add(new ACIPString(s.substring(i, i+numdigits+2),
                                                  ACIPString.FOLIO_MARKER));
                            startOfString = i+numdigits+2;
                            i = startOfString - 1;
                            currentType = ACIPString.ERROR;
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
                            al.add(new ACIPString(s.substring(i, i+numdigits+4),
                                                  ACIPString.FOLIO_MARKER));
                            startOfString = i+numdigits+4;
                            i = startOfString - 1;
                            currentType = ACIPString.ERROR;
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
                            al.add(new ACIPString(s.substring(i, i+numdigits+1),
                                                  ACIPString.FOLIO_MARKER));
                            startOfString = i+numdigits+1;
                            i = startOfString - 1;
                            currentType = ACIPString.ERROR;
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
                    al.add(new ACIPString("Found an illegal at sign, @ (in context, this is " + inContext + ").  @012B is an example of a legal folio marker.",
                                          ACIPString.ERROR));
                    if (null != errors)
                        errors.append("Offset " + i + " or maybe " + (i-numNewlines) + ": "
                                      + "Found an illegal at sign, @ (in context, this is " + inContext + ").  @012B is an example of a legal folio marker.\n");
                    if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
                    startOfString = i+1;
                    currentType = ACIPString.ERROR;
                }
                break; // end '@' case

            case '/':
                // This definitely indicates a new token.
                if (startOfString < i) {
                    al.add(new ACIPString(s.substring(startOfString, i),
                                          currentType));
                    startOfString = i;
                    currentType = ACIPString.ERROR;
                }

                if (startSlashIndex >= 0) {
                    if (startSlashIndex + 1 == i) {
                        /* //NYA\\ appears in ACIP input, and I think
                         * it means /NYA/.  We warn about // for this
                         * reason.  \\ causes a tsheg-bar error (DLC
                         * FIXME: verify this is so). */
                        al.add(new ACIPString("Found //, which could be legal (the Unicode would be \\u0F3C\\u0F3D), but is likely in an illegal construct like //NYA\\\\.",
                                              ACIPString.ERROR));
                        if (errors != null) {
                            errors.append("Offset " + i + " or maybe " + (i-numNewlines) + ": "
                                          + "Found //, which could be legal (the Unicode would be \\u0F3C\\u0F3D), but is likely in an illegal construct like //NYA\\\\.\n");
                        }
                        if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
                    }
                    al.add(new ACIPString(s.substring(i, i+1),
                                          ACIPString.END_SLASH));
                    startOfString = i+1;
                    currentType = ACIPString.ERROR;
                    startSlashIndex = -1;
                } else {
                    startSlashIndex = i;
                    al.add(new ACIPString(s.substring(i, i+1),
                                          ACIPString.START_SLASH));
                    startOfString = i+1;
                    currentType = ACIPString.ERROR;
                }
                break; // end '/' case

            case '(':
            case ')':
                // This definitely indicates a new token.
                if (startOfString < i) {
                    al.add(new ACIPString(s.substring(startOfString, i),
                                          currentType));
                    startOfString = i;
                    currentType = ACIPString.ERROR;
                }

                // We do not support nesting like (NYA (BA)).

                if (startParenIndex >= 0) {
                    if (ch == '(') {
                        al.add(new ACIPString("Found an illegal open parenthesis, (.  Nesting of parentheses is not allowed.",
                                              ACIPString.ERROR));
                        if (null != errors)
                            errors.append("Offset " + i + " or maybe " + (i-numNewlines) + ": "
                                          + "Found an illegal open parenthesis, (.  Nesting of parentheses is not allowed.\n");
                        if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
                    } else {
                        al.add(new ACIPString(s.substring(i, i+1), ACIPString.END_PAREN));
                        startParenIndex = -1;
                    }
                    startOfString = i+1;
                    currentType = ACIPString.ERROR;
                } else {
                    if (ch == ')') {
                        al.add(new ACIPString("Unexpected closing parenthesis, ), found.",
                                              ACIPString.ERROR));
                        if (null != errors)
                            errors.append("Offset " + i + " or maybe " + (i-numNewlines) + ": "
                                          + "Unexpected closing parenthesis, ), found.\n");
                        if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
                    } else {
                        startParenIndex = i;
                        al.add(new ACIPString(s.substring(i, i+1), ACIPString.START_PAREN));
                    }
                    startOfString = i+1;
                    currentType = ACIPString.ERROR;
                }
                break; // end '(',')' case

            case '?':
                if (bracketTypeStack.empty() || i+1>=sl
                    || (s.charAt(i+1) != ']' && s.charAt(i+1) != '}')) {
                    // The tsheg bar ends here; new token.
                    if (startOfString < i) {
                        al.add(new ACIPString(s.substring(startOfString, i),
                                              currentType));
                    }
                    al.add(new ACIPString(s.substring(i, i+1),
                                          ACIPString.QUESTION));
                    startOfString = i+1;
                    currentType = ACIPString.ERROR;
                } // else this is [*TR'A ?] or the like.
                break; // end '?' case


            case '.':
                // This definitely indicates a new token.
                if (startOfString < i) {
                    al.add(new ACIPString(s.substring(startOfString, i),
                                          currentType));
                    startOfString = i;
                    currentType = ACIPString.ERROR;
                }
                // . is used for a non-breaking tsheg, such as in
                // {NGO.,} and {....,DAM}.  We give a warning unless ,
                // or ., or [A-Za-z] follows '.'.
                al.add(new ACIPString(s.substring(i, i+1),
                                      ACIPString.TIBETAN_PUNCTUATION));
                if (!(i + 1 < sl
                      && (s.charAt(i+1) == '.' || s.charAt(i+1) == ','
                          || (s.charAt(i+1) == '\r' || s.charAt(i+1) == '\n')
                          || (s.charAt(i+1) >= 'a' && s.charAt(i+1) <= 'z')
                          || (s.charAt(i+1) >= 'A' && s.charAt(i+1) <= 'Z')))) {
                    al.add(new ACIPString("A non-breaking tsheg, '.', appeared, but not like \"...,\" or \".,\" or \".dA\" or \".DA\".",
                                          ACIPString.WARNING));
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

                // The tsheg bar ends here; new token.
                if (startOfString < i) {
                    al.add(new ACIPString(s.substring(startOfString, i),
                                          currentType));
                }

                // Insert a tsheg if necessary.  ACIP files aren't
                // careful, so "KA\r\n" and "GA\n" appear where "KA
                // \r\n" and "GA \n" should appear.
                if (('\r' == ch
                     || ('\n' == ch && i > 0 && s.charAt(i - 1) != '\r'))
                    && !al.isEmpty()
                    && ((ACIPString)al.get(al.size() - 1)).getType() == ACIPString.TIBETAN_NON_PUNCTUATION) {
                    al.add(new ACIPString(" ", ACIPString.TIBETAN_PUNCTUATION));
                }

                // "DANG,\nLHAG" is really "DANG, LHAG".  But always?  Not if you have "MDO,\n\nKA...".
                if (('\r' == ch
                     || ('\n' == ch && i > 0 && s.charAt(i - 1) != '\r'))
                    && !al.isEmpty()
                    && ((ACIPString)al.get(al.size() - 1)).getType() == ACIPString.TIBETAN_PUNCTUATION
                    && ((ACIPString)al.get(al.size() - 1)).getText().equals(",")
                    && s.charAt(i-1) == ','
                    && (i + (('\r' == ch) ? 2 : 1) < sl
                        && (s.charAt(i+(('\r' == ch) ? 2 : 1)) != ch))) {
                    al.add(new ACIPString(" ", ACIPString.TIBETAN_PUNCTUATION));
                }

                // Don't add in a "\r\n" or "\n" unless there's a
                // blank line.
                boolean rn = false;
                boolean realNewline = false;
                if (('\n' != ch && '\r' != ch)
                    || (realNewline
                        = ((rn = ('\n' == ch && i >= 3 && s.charAt(i-3) == '\r' && s.charAt(i-2) == '\n' && s.charAt(i-1) == '\r'))
                           || ('\n' == ch && i >= 1 && s.charAt(i-1) == '\n')))) {
                    for (int h = 0; h < (realNewline ? 2 : 1); h++)
                        al.add(new ACIPString(rn ? s.substring(i - 1, i+1) : s.substring(i, i+1),
                                              ACIPString.TIBETAN_PUNCTUATION));
                }
                startOfString = i+1;
                currentType = ACIPString.ERROR;
                break; // end TIBETAN_PUNCTUATION case

            default:
                if (!bracketTypeStack.empty()) {
                    int stackTop = ((Integer)bracketTypeStack.peek()).intValue();
                    if (ACIPString.CORRECTION_START == stackTop && '?' == ch) {
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
                        al.add(new ACIPString(s.substring(startOfString, i),
                                              currentType));
                    }
                    if ((int)ch == 65533) {
                        al.add(new ACIPString("Found an illegal, unprintable character.",
                                              ACIPString.ERROR));
                        if (null != errors)
                            errors.append("Offset " + i + " or maybe " + (i-numNewlines) + ": "
                                          + "Found an illegal, unprintable character.\n");
                    } else if ('\\' == ch) {
                        al.add(new ACIPString("Found a Sanskrit virama, \\, but the converter currently doesn't treat these properly.  Sorry!  Please do complain to the maintainers.",
                                              ACIPString.ERROR));
                        if (null != errors)
                            errors.append("Offset " + i + " or maybe " + (i-numNewlines) + ": "
                                          + "Found a Sanskrit virama, \\, but the converter currently doesn't treat these properly.  Sorry!  Please do complain to the maintainers.\n");
                    } else {
                        al.add(new ACIPString("Found an illegal character, " + ch + ", with ordinal " + (int)ch + ".",
                                              ACIPString.ERROR));
                        if (null != errors)
                            errors.append("Offset " + i + " or maybe " + (i-numNewlines) + ": "
                                          + "Found an illegal character, " + ch + ", with ordinal " + (int)ch + ".\n");
                    }
                    if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
                    startOfString = i+1;
                    currentType = ACIPString.ERROR;
                } else {
                    // Continue through the loop.
                    if (ACIPString.ERROR == currentType)
                        currentType = ACIPString.TIBETAN_NON_PUNCTUATION;
                }
                break; // end default case
            }
        }
        if (startOfString < sl) {
            al.add(new ACIPString(s.substring(startOfString, sl),
                                  currentType));
        }
        if (waitingForMatchingIllegalClose) {
            al.add(new ACIPString("UNEXPECTED END OF INPUT",
                                  ACIPString.ERROR));
            if (null != errors) {
                errors.append("Offset END: "
                              + "Truly unmatched open bracket found.\n");
            }
            if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
        }
        if (!bracketTypeStack.empty()) {
            al.add(new ACIPString("Unmatched open bracket found.  A " + ((ACIPString.COMMENT == currentType) ? "comment" : "correction") + " does not terminate.",
                                  ACIPString.ERROR));
            if (null != errors) {
                errors.append("Offset END: "
                              + "Unmatched open bracket found.  A " + ((ACIPString.COMMENT == currentType) ? "comment" : "correction") + " does not terminate.\n");
            }
            if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
        }
        if (startSlashIndex >= 0) {
            al.add(new ACIPString("Slashes are supposed to occur in pairs, but the input had an unmatched '/' character.",
                                  ACIPString.ERROR));
            if (null != errors)
                errors.append("Offset END: "
                              + "Slashes are supposed to occur in pairs, but the input had an unmatched '/' character.\n");
            if (maxErrors >= 0 && ++numErrors >= maxErrors) return null;
        }
        if (startParenIndex >= 0) {
            al.add(new ACIPString("Parentheses are supposed to occur in pairs, but the input had an unmatched parenthesis.",
                                  ACIPString.ERROR));
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
    private static boolean isAlpha(char ch) {
        return ch == '\'' // 23rd consonant

            // combining punctuation, vowels:
            || ch == '%'
            || ch == 'o'
            || ch == 'm'
            || ch == 'x'
            || ch == ':'
            || ch == '^'
            // DLC FIXME: we must treat this guy like a vowel, a special vowel that numerals can take on.  Until then, warn.            || ch == '\\'

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