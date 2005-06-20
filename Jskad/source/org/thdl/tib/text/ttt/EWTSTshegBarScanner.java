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

import java.math.BigInteger;
import java.util.ArrayList;

/**
* This singleton class is able to break up Strings of EWTS text (for
* example, an entire sutra file) into tsheg bars, comments, etc.
* Non-Tibetan parts are segregated (so that consumers can ensure that
* they remain non-Tibetan), and Tibetan passages are broken up into
* tsheg bars.
*
* This is not public because you should use {@link EWTSTraits#scanner()}.
*
* @author David Chandler */
class EWTSTshegBarScanner extends TTshegBarScanner {

    /** Returns true iff ch can appear within an EWTS tsheg bar. */
    protected static boolean isValidInsideTshegBar(char ch) {
        // '\\' is absent, but should it be?  TODO(DLC)[EWTS->Tibetan]
        return ((ch >= '0' && ch <= '9')
                || (ch >= '\u0f71' && ch <= '\u0f84')
                || EWTSTraits.instance().isUnicodeConsonant(ch)
                || EWTSTraits.instance().isUnicodeWowel(ch)
                || (ch >= '\u0f20' && ch <= '\u0f33')
                || "khgncjytdpbmtstdzwzz'rlafvTDNSWYReuioIAUMHX?^\u0f39\u0f35\u0f37.+~'`-\u0f19\u0f18\u0f3f\u0f3e\u0f86\u0f87\u0f88".indexOf(ch) >= 0);
    }

    /** See the comment in TTshegBarScanner.  This does not find
        errors and warnings that you'd think of a parser finding (TODO(DLC)[EWTS->Tibetan]:
        DOES IT?). */
    public ArrayList scan(String s, StringBuffer errors, int maxErrors, // TODO(DLC)[EWTS->Tibetan]: ignored
                          boolean shortMessages, String warningLevel) {
        // the size depends on whether it's mostly Tibetan or mostly
        // Latin and a number of other factors.  This is meant to be
        // an underestimate, but not too much of an underestimate.
        ArrayList al = new ArrayList(s.length() / 10);

        // TODO(DLC)[EWTS->Tibetan]: use jflex, javacc or something similar

        // TODO(DLC)[EWTS->Tibetan]: what about Unicode escapes like \u0f20?  When do you do that?  Immediately like Java source files?  I think so and then we can say that oddballs like \u0f19 are valid within tsheg bars.

        StringBuffer sb = new StringBuffer(s);
        ExpandEscapeSequences(sb);
        int sl = sb.length();
        // TODO(DLC)[EWTS->Tibetan]:: '@#', in ewts->tmw, is not working
        // TODO(DLC)[EWTS->Tibetan]:: 'jamX 'jam~X one is not working in ->tmw mode
        // TODO(DLC)[EWTS->Tibetan]:: dzaHsogs is not working
        for (int i = 0; i < sl; i++) {
        	if (isValidInsideTshegBar(sb.charAt(i))) {
        		StringBuffer tbsb = new StringBuffer();
        		for (; i < sl; i++) {
        			if (isValidInsideTshegBar(sb.charAt(i)))
        				tbsb.append(sb.charAt(i));
        			else {
        				--i;
        				break;
        			}
        		}
        		al.add(new TString("EWTS", tbsb.toString(),
        				TString.TIBETAN_NON_PUNCTUATION));
        	} else {
        		if (" /;|!:=_@#$%<>()\r\n\t*".indexOf(sb.charAt(i)) >= 0)
        			al.add(new TString("EWTS", sb.substring(i, i+1),
        					TString.TIBETAN_PUNCTUATION));
        		else
        			al.add(new TString("EWTS", "ERROR TODO(DLC)[EWTS->Tibetan]: this character is illegal in EWTS: " + sb.substring(i, i+1),
        					TString.ERROR));
        	}
        }
        return al;
    }
    
    /** Modifies the EWTS in sb such that Unicode escape sequences are
     *  expanded. */
    public static void ExpandEscapeSequences(StringBuffer sb) {
    	int sl;
        for (int i = 0; i < (sl = sb.length()); i++) {
        	if (i + "\\u00000000".length() <= sl) {
                if (sb.charAt(i) == '\\' && sb.charAt(i + 1) == 'u' || sb.charAt(i + 1) == 'U') {
                    boolean isEscape = true;
                    for (int j = 0; j < "00000000".length(); j++) {
                        char ch =  sb.charAt(i + "\\u".length() + j);
                        if (!((ch <= '9' && ch >= '0')
                              || (ch <= 'F' && ch >= 'A')
                              || (ch <= 'f' && ch >= 'a'))) {
                            isEscape = false;
                            break;
                        }
                    }
                    if (isEscape) {
                    	long x = -1;
                    	try {
                    		BigInteger bigx = new java.math.BigInteger(sb.substring(i+2, i+10), 16);
                    		x = bigx.longValue();
							if (!(bigx.compareTo(new BigInteger("0", 16)) >= 0
								  && bigx.compareTo(new BigInteger("FFFFFFFF", 16)) <= 0))
								x = -1;
                    	} catch (NumberFormatException e) {
                    		// leave x == -1
                    	}
                        if (x >= 0 && x <= 0xFFFF) {
                            sb.replace(i, i + "\\uXXXXyyyy".length(), new String(new char[] { (char)x }));
                            continue;
                        } else if (x >= 0x00000000L
								   && x <= 0xFFFFFFFFL) {
// TODO(DLC)[EWTS->Tibetan]: do nothing?  test errors                        	al.add(new TString("EWTS", "Sorry, we don't yet support Unicode escape sequences above 0x0000FFFF!  File a bug.",
                        		   //TString.ERROR));
                        	i += "uXXXXYYYY".length();
                            continue;
                        }
                    }
                }
            }
            if (i + "\\u0000".length() <= sl) {
                if (sb.charAt(i) == '\\' && sb.charAt(i + 1) == 'u' || sb.charAt(i + 1) == 'U') {
                    boolean isEscape = true;
                    for (int j = 0; j < "0000".length(); j++) {
                        char ch =  sb.charAt(i + "\\u".length() + j);
                        if (!((ch <= '9' && ch >= '0')
                              || (ch <= 'F' && ch >= 'A')
                              || (ch <= 'f' && ch >= 'a'))) {
                            isEscape = false;
                            break;
                        }
                    }
                    if (isEscape) {
                        int x = -1;
                        try {
                            if (!((x = Integer.parseInt(sb.substring(i+2, i+6), 16)) >= 0x0000
                                  && x <= 0xFFFF))
                                x = -1;
                        } catch (NumberFormatException e) {
                            // leave x == -1
                        }
                        if (x >= 0) {
                            sb.replace(i, i + "\\uXXXX".length(), new String(new char[] { (char)x }));
                            continue;
                        }
                    }
                }
            }
        }
    }

    /** non-public because this is a singleton */
    protected EWTSTshegBarScanner() { }
    private static EWTSTshegBarScanner singleton = null;
    /** Returns the sole instance of this class. */
    public synchronized static EWTSTshegBarScanner instance() {
        if (null == singleton) {
            singleton = new EWTSTshegBarScanner();
        }
        return singleton;
    }
}
