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
    /** Returns a list of {@link ACIPString ACIPStrings} corresponding
     *  to s, possibly the empty list (when the empty string is the
     *  input).  Each String is either a Latin comment, some Latin
     *  text, a tsheg bar (minus the tsheg or shad or whatever), a
     *  String of inter-tsheg-bar punctuation, etc.
     *
     *  <p>This not only scans; it finds all the errors a parser would
     *  too, like "NYA x" and "(" and ")" and "/NYA" etc.  It puts
     *  those in as ACIPStrings with type {@link ACIPString#ERROR}.
    */
    public static ArrayList scan(String s) {

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
            if (ACIPString.COMMENT == currentType && ch != ']')
                continue;
            switch (ch) {
            case ']':
                if (bracketTypeStack.empty()) {
                    // Error.
                    if (startOfString < i) {
                        al.add(new ACIPString(s.substring(startOfString, i),
                                              currentType));
                    }
                    al.add(new ACIPString(s.substring(i, i+1), ACIPString.ERROR));
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
                break;

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
                    && s.substring(i, i + "[DD]".length()).equals("[DD]")) {
                    thingy = "[DD]";
                    currentType = ACIPString.DD;
                } else if (i + "[DD1]".length() <= sl
                           && s.substring(i, i + "[DD1]".length()).equals("[DD1]")) {
                    thingy = "[DD1]";
                    currentType = ACIPString.DD;
                } else if (i + "[DD2]".length() <= sl
                           && s.substring(i, i + "[DD2]".length()).equals("[DD2]")) {
                    thingy = "[DD2]";
                    currentType = ACIPString.DD;
                } else if (i + "[DDD]".length() <= sl
                           && s.substring(i, i + "[DDD]".length()).equals("[DDD]")) {
                    thingy = "[DDD]";
                    currentType = ACIPString.DD;
                } else if (i + "[DR]".length() <= sl
                           && s.substring(i, i + "[DR]".length()).equals("[DR]")) {
                    thingy = "[DR]";
                    currentType = ACIPString.DR;
                } else if (i + "[LS]".length() <= sl
                           && s.substring(i, i + "[LS]".length()).equals("[LS]")) {
                    thingy = "[LS]";
                    currentType = ACIPString.LS;
                } else if (i + "[BP]".length() <= sl
                           && s.substring(i, i + "[BP]".length()).equals("[BP]")) {
                    thingy = "[BP]";
                    currentType = ACIPString.BP;
                } else if (i + "[?]".length() <= sl
                           && s.substring(i, i + "[?]".length()).equals("[?]")) {
                    thingy = "[?]";
                    currentType = ACIPString.QUESTION;
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
                    startOfString = i + 1;
                    currentType = ACIPString.ERROR;
                }
                break; // end '[' case

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
                            al.add(new ACIPString(s.substring(i, i+numdigits+2), ACIPString.FOLIO_MARKER));
                            startOfString = i+numdigits+2;
                            currentType = ACIPString.ERROR;
                            break;
                        }
                    }
                }
                if (startOfString == i) {
                    al.add(new ACIPString(s.substring(i, i+1), ACIPString.ERROR));
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
                    al.add(new ACIPString(s.substring(i, i+1), ACIPString.END_SLASH));
                    startOfString = i+1;
                    currentType = ACIPString.ERROR;
                    startSlashIndex = -1;
                } else {
                    startSlashIndex = i;
                    al.add(new ACIPString(s.substring(i, i+1), ACIPString.START_SLASH));
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
                    if (ch == '(')
                        al.add(new ACIPString("Nesting of parentheses () is not allowed", ACIPString.ERROR));
                    else {
                        al.add(new ACIPString(s.substring(i, i+1), ACIPString.END_PAREN));
                        startParenIndex = -1;
                    }
                    startOfString = i+1;
                    currentType = ACIPString.ERROR;
                } else {
                    if (ch == ')')
                        al.add(new ACIPString("Unexpected closing parenthesis )", ACIPString.ERROR));
                    else {
                        startParenIndex = i;
                        al.add(new ACIPString(s.substring(i, i+1), ACIPString.START_PAREN));
                    }
                    startOfString = i+1;
                    currentType = ACIPString.ERROR;
                }
                break; // end '/' case


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
                if (!(isNumeric(ch) || isAlpha(ch))) {
                    if (startOfString < i) {
                        al.add(new ACIPString(s.substring(startOfString, i),
                                              currentType));
                    }
                    al.add(new ACIPString(s.substring(i, i+1),
                                          ACIPString.ERROR));
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
            if (!bracketTypeStack.empty()) {
                al.add(new ACIPString("UNEXPECTED END OF INPUT",
                                      ACIPString.ERROR));
            }
            if (startSlashIndex >= 0) {
                al.add(new ACIPString("Slashes are supposed to occur in pairs, but the input had an unmatched '/' character.",
                                      ACIPString.ERROR));
            }
            if (startParenIndex >= 0) {
                al.add(new ACIPString("Parentheses are supposed to occur in pairs, but the input had an unmatched parenthesis.",
                                      ACIPString.ERROR));
            }
        }
        return al;
    }
    
    /** See implementation. */
    private static boolean isNumeric(char ch) {
        return ch >= '0' && ch <= '9';
    }

    /** See implementation. */
    private static boolean isAlpha(char ch) {
        return ch == '\''

            // combining punctuation:
            || ch == '%'
            || ch == 'o'
            || ch == 'x'
            
            || (ch >= 'A' && ch <= 'Z')
            || (ch >= 'a' && ch <= 'z');
    }
}
