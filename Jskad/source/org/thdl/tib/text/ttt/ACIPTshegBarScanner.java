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
    // DLC DOC
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Bad args!  Need just the ACIP file's path.");
            System.exit(1);
        }
        StringBuffer errors = new StringBuffer();
        ArrayList al = scanFile(args[0], errors);

        if (errors.length() > 0) {
            System.out.println("Errors scanning ACIP input file: ");
            System.out.println(errors);
            System.out.println("Exiting; please fix input file and try again.");
            System.exit(1);
        }

        System.out.println("Good scan!");
        System.exit(0);
    }
    
    // DLC DOC
    // DLC FIXME: not so efficient; copies the whole file into memory first
    public static ArrayList scanFile(String fname, StringBuffer errors) throws IOException {
        StringBuffer s = new StringBuffer();
        char ch[] = new char[8192];
        BufferedReader in
            = new BufferedReader(new InputStreamReader(new FileInputStream(fname))); // DLC FIXME: specify encoding.

        int amt;
        while (-1 != (amt = in.read(ch))) {
            s.append(ch, 0, amt);
        }
        return scan(s.toString(), errors);
    }

    /** Returns a list of {@link ACIPString ACIPStrings} corresponding
     *  to s, possibly the empty list (when the empty string is the
     *  input).  Each String is either a Latin comment, some Latin
     *  text, a tsheg bar (minus the tsheg or shad or whatever), a
     *  String of inter-tsheg-bar punctuation, etc.
     *
     *  <p>This not only scans; it finds all the errors a parser would
     *  too, like "NYA x" and "(" and ")" and "/NYA" etc.  It puts
     *  those in as ACIPStrings with type {@link ACIPString#ERROR},
     *  and also, if errors is non-null, appends helpful messages to
     *  errors, each followed by a '\n'.  There is at least one case
     *  where no ERROR ACIPString will appear but errors will be
     *  modified.
    */
    public static ArrayList scan(String s, StringBuffer errors) {

        // the size depends on whether it's mostly Tibetan or mostly
        // Latin and a number of other factors.  This is meant to be
        // an underestimate, but not too much of an underestimate.
        ArrayList al = new ArrayList(s.length() / 10);
        
        int sl = s.length();
        int currentType = ACIPString.ERROR;
        int startOfString = 0;
        Stack bracketTypeStack = new Stack();
        int startSlashIndex = -1;
        int startParenIndex = -1;
        for (int i = 0; i < sl; i++) {
            if (i < startOfString) throw new Error("bad reset");
            char ch;
            ch = s.charAt(i);
            if (ACIPString.COMMENT == currentType && ch != ']') {
                if ('[' == ch) {
                    al.add(new ACIPString("Found an open square bracket, [, within a [#COMMENT]-style comment.  Square brackets may not appear in comments.\n",
                                          ACIPString.ERROR));
                    if (null != errors)
                        errors.append("Offset " + i + ": "
                                      + "Found an open square bracket, [, within a [#COMMENT]-style comment.  Square brackets may not appear in comments.\n");
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
                    al.add(new ACIPString(s.substring(i, i+1), ACIPString.ERROR));
                    if (null != errors)
                        errors.append("Offset " + i + ": "
                                      + "Found a closing square bracket, ], without a matching open square bracket, [.  Perhaps a [#COMMENT] incorrectly written as [COMMENT], or a [*CORRECTION] written incorrectly as [CORRECTION], caused this.\n");
                    startOfString = i+1;
                    currentType = ACIPString.ERROR;
                } else {
                    int stackTop = ((Integer)bracketTypeStack.pop()).intValue();

                    String text = s.substring(startOfString, i+1);
                    if (ACIPString.CORRECTION_START == stackTop) {
                        char prevCh = s.charAt(i-1);
                        if ('?' != prevCh) {
                            currentType = ACIPString.PROBABLE_CORRECTION;
                        } else {
                            currentType = ACIPString.POSSIBLE_CORRECTION;
                        }
                    }
                    al.add(new ACIPString(text, currentType));
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
                    //  for some English comments.  DLC FIXME: put
                    //  these in a config file.

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
                            break;
                        } else if ('#' == nextCh) {
                            currentType = ACIPString.COMMENT;
                            bracketTypeStack.push(new Integer(currentType));
                            break;
                        }
                    }
                    // This is an error.  DLC FIXME: in practice
                    // [COMMENTS APPEAR WITHOUT # MARKS].  Though
                    // "... [" could cause this too.
                    al.add(new ACIPString(s.substring(i, i+1),
                                          ACIPString.ERROR));
                    if (null != errors) {
                        String inContext = s.substring(i, i+Math.min(sl-i, 10));
                        if (sl-i > 10) {
                            inContext = inContext + "...";
                        }
                        errors.append("Offset " + i + ": "
                                      + "Found an illegal open square bracket, [ (in context, this is " + inContext + ").  Perhaps there is a [#COMMENT] written incorrectly as [COMMENT], or a [*CORRECTION] written incorrectly as [CORRECTION], or an unmatched open square bracket?\n");
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

                // We look for @N[AB], @NN[AB], @NNN[AB], @NNNN[AB],
                // @NNNNN[AB], and @NNNNNN[AB] only, that is from one
                // to six digits.
                for (int numdigits = 1; numdigits <= 5; numdigits++) {
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
                            al.add(new ACIPString(s.substring(i, i+numdigits+2),
                                                  ACIPString.FOLIO_MARKER));
                            startOfString = i+numdigits+2;
                            i = startOfString - 1;
                            currentType = ACIPString.ERROR;
                            break;
                        }
                    }
                    //                    System.out.println("DLC NOW HERE xxx y:" + (i+numdigits+3 < sl) + " z:" + s.charAt(i+1) + s.charAt(i+numdigits+2) + s.charAt(i+numdigits+3));
                    
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
                }
                if (startOfString == i) {
                    al.add(new ACIPString(s.substring(i, i+1), ACIPString.ERROR));
                    if (null != errors)
                        errors.append("Offset " + i + ": "
                                      + "Found an illegal at sign, @.  @012B is an example of a legal folio marker.\n");
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

                // DLC support nesting like (NYA (BA))?

                if (startParenIndex >= 0) {
                    if (ch == '(') {
                        al.add(new ACIPString("Nesting of parentheses () is not allowed", ACIPString.ERROR));
                        if (null != errors)
                            errors.append("Offset " + i + ": "
                                          + "Found an illegal open parenthesis, (.  Nesting of parentheses is not allowed.\n");
                    } else {
                        al.add(new ACIPString(s.substring(i, i+1), ACIPString.END_PAREN));
                        startParenIndex = -1;
                    }
                    startOfString = i+1;
                    currentType = ACIPString.ERROR;
                } else {
                    if (ch == ')') {
                        al.add(new ACIPString("Unexpected closing parenthesis )", ACIPString.ERROR));
                        if (null != errors)
                            errors.append("Offset " + i + ": "
                                          + "Unexpected closing parenthesis, ), found.\n");
                    } else {
                        startParenIndex = i;
                        al.add(new ACIPString(s.substring(i, i+1), ACIPString.START_PAREN));
                    }
                    startOfString = i+1;
                    currentType = ACIPString.ERROR;
                }
                break; // end '(',')' case

            case '?':
                if (bracketTypeStack.empty()) {
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
                // . is used for a non-breaking tsheg, such as in {NGO.,} and {....,DAM}.  We give an error unless , or . follows '.'.
                if (i + 1 < sl && (s.charAt(i+1) == '.' || s.charAt(i+1) == ',')) {
                    al.add(new ACIPString(s.substring(i, i+1),
                                          ACIPString.TIBETAN_PUNCTUATION));
                } else {
                    al.add(new ACIPString("A non-breaking tsheg, '.', appeared, but not like \"...,\" or \".,\".",
                                          ACIPString.ERROR));
                    if (null != errors)
                        errors.append("Offset " + i + ": "
                                      + "A non-breaking tsheg, '.', appeared, but not like \"...,\" or \".,\".\n");

                }
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
                al.add(new ACIPString(s.substring(i, i+1),
                                      ACIPString.TIBETAN_PUNCTUATION));
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
                if (!(isNumeric(ch) || isAlpha(ch))) {
                    if (startOfString < i) {
                        al.add(new ACIPString(s.substring(startOfString, i),
                                              currentType));
                    }
                    al.add(new ACIPString(s.substring(i, i+1),
                                          ACIPString.ERROR));
                    if (null != errors)
                        errors.append("Offset " + i + ": "
                                      + "Found an illegal character, " + ch + "\n");
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
        if (!bracketTypeStack.empty()) {
            al.add(new ACIPString("UNEXPECTED END OF INPUT",
                                  ACIPString.ERROR));
            if (null != errors) {
                if (ACIPString.COMMENT == currentType) {
                    errors.append("Offset END: "
                                  + "Unmatched open square bracket, [, found.  A comment does not terminate.\n");
                } else {
                    errors.append("Offset END: "
                                  + "Unmatched open square bracket, [, found.  A correction does not terminate.\n");
                }
            }
        }
        if (startSlashIndex >= 0) {
            al.add(new ACIPString("Slashes are supposed to occur in pairs, but the input had an unmatched '/' character.",
                                  ACIPString.ERROR));
            if (null != errors)
                errors.append("Offset END: "
                              + "Slashes are supposed to occur in pairs, but the input had an unmatched '/' character.\n");
        }
        if (startParenIndex >= 0) {
            al.add(new ACIPString("Parentheses are supposed to occur in pairs, but the input had an unmatched parenthesis.",
                                  ACIPString.ERROR));
            if (null != errors)
                errors.append("Offset END: "
                              + "Unmatched open parenthesis, (, found.\n");
        }
        return al;
    }

    /** See implementation. */
    private static boolean isNumeric(char ch) {
        return ch >= '0' && ch <= '9';
    }

    /** See implementation. */
    private static boolean isAlpha(char ch) {
        return ch == '\'' // 23rd consonant

            // combining punctuation, vowels:
            || ch == '%'
            || ch == 'o'
            || ch == 'x'
            || ch == ':'

            || ch == '-'
            || ch == '+'
            
            || (ch >= 'A' && ch <= 'Z')
            || (ch >= 'a' && ch <= 'z');
    }
}
