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
Library (THDL). Portions created by the THDL are Copyright 2001-2003 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text;

import java.util.*;
import java.net.URL;
import java.io.*;
import java.lang.*;
import java.awt.Font;
import java.awt.event.KeyEvent;
import javax.swing.text.*;
import java.awt.font.*;

import org.thdl.util.ThdlDebug;
import org.thdl.util.ThdlLazyException;
import org.thdl.util.Trie;
import org.thdl.util.ThdlOptions;

/**
* Interfaces between Extended Wylie and the TibetanMachineWeb fonts.
* To do this this must first read the code table, which lives in
* "tibwn.ini", and which must be found in the same directory as this
* class.  Note that WylieWord has its own copy of this file, so edit
* both or neither.
*
* <p>In addition, this class optionally loads the TibetanMachineWeb
* fonts manually via {@link #readInFontFiles()}.
* @author Edward Garrett, Tibetan and Himalayan Digital Library
* @version 1.0
*/
// FIXME: for speed, make either this class, its methods, or both, final?
public class TibetanMachineWeb implements THDLWylieConstants {
    /** This addresses bug 624133, "Input freezes after impossible
     *  character".  The input sequences that are valid in Extended
     *  Wylie.  For example, "Sh" will be in this container, but "S"
     *  will not be. */
    private static Trie validInputSequences = new Trie();

    /** needed because a Trie cannot have a null value associated with
     *  a key */
    private final static String anyOldObjectWillDo
        = "this placeholder is useful for debugging; we need a nonnull Object anyway";

	private static boolean hasReadData = false;
	private static TibetanKeyboard keyboard = null;
	private static Set charSet = null;
	private static Set vowelSet = null;
	private static Set puncSet = null;
	private static Set topSet = null;
	private static Set leftSet = null;
	private static Set rightSet = null;
	private static Set farRightSet = null;
	private static Map tibHash = new HashMap();
	private static Map binduMap = new HashMap();
	private static String[][] toHashKey = new String[11][95]; //note: toHashKey[0][..] is not used
	private static DuffCode[][] TMtoTMW = new DuffCode[5][255-32];
	private static String fileName = "tibwn.ini";
	private static final String DELIMITER = "~";
	private static Set top_vowels;
	private static SimpleAttributeSet[] webFontAttributeSet = new SimpleAttributeSet[11];
	private static boolean hasDisambiguatingKey; //to disambiguate gy and g.y=
	private static char disambiguating_key;
	private static boolean hasSanskritStackingKey; //for stacking Sanskrit
	private static boolean hasTibetanStackingKey; //for stacking Tibetan
	private static boolean isStackingMedial; //ie g+y, not +gy
	private static char stacking_key;
	private static boolean isAChenRequiredBeforeVowel;
	private static boolean isAChungConsonant;
	private static boolean hasAVowel;
	private static String aVowel;

    // We use .intern() explicitly here so the code is easier to
    // understand, but all string literals are interned.
	public static final String[] tmFontNames = {
		null,
		"TibetanMachine".intern(),
		"TibetanMachineSkt1".intern(),
		"TibetanMachineSkt2".intern(),
		"TibetanMachineSkt3".intern(),
		"TibetanMachineSkt4".intern()
	};
	public static final String[] tmwFontNames = {
		null,
		"TibetanMachineWeb".intern(),
		"TibetanMachineWeb1".intern(),
		"TibetanMachineWeb2".intern(),
		"TibetanMachineWeb3".intern(),
		"TibetanMachineWeb4".intern(),
		"TibetanMachineWeb5".intern(),
		"TibetanMachineWeb6".intern(),
		"TibetanMachineWeb7".intern(),
		"TibetanMachineWeb8".intern(),
		"TibetanMachineWeb9".intern()
	};
/**
* represents where in an array of DuffCodes you
* find the TibetanMachine equivalence of a glyph
*/
	public static final int TM = 0;
/**
* represents where in an array of DuffCodes you
* find the reduced character equivalent of a TMW glyph
*/
	public static final int REDUCED_C = 1;
/**
* represents where in an array of DuffCodes you
* find the TibetanMachineWeb glyph
*/
	public static final int TMW = 2;
/**
* represents where in an array of DuffCodes you
* find the gigu value for a given glyph
*/
	public static final int VOWEL_i = 3;
/**
* represents where in an array of DuffCodes you
* find the zhebju value for a given glyph
*/
	public static final int VOWEL_u = 4;
/**
* represents where in an array of DuffCodes you
* find the drengbu value for a given glyph
*/
	public static final int VOWEL_e = 5;
/**
* represents where in an array of DuffCodes you
* find the naro value for a given glyph
*/
	public static final int VOWEL_o = 6;
/**
* represents where in an array of DuffCodes you
* find the achung value for a given glyph
*/
	public static final int VOWEL_A = 7;
/**
* represents where in an array of DuffCodes you
* find the achung + zhebju value for a given glyph
*/
	public static final int VOWEL_U = 8;
/**
* represents where in an array of DuffCodes you
* find the Unicode equivalence of a given glyph
*/
	public static final int UNICODE = 9;
/**
* represents where in an array of DuffCodes you
* find the half height equivalence of a given glyph
*/
	public static final int HALF_C = 10;

    /** head letters, superscribed letters */
	private static final String tops = "r,s,l";
    /** prefixes */
	private static final String lefts = "g,d,b,m,'";
    /** suffixes */
	private static final String rights = "g,ng,d,n,b,m,r,l,s,',T";
    /** postsuffixes.  nga was here in the past, according to Edward,
     *  to handle cases like ya'ng.  pa'am wasn't considered, but had
     *  it been, ma probably would've gone here too.  We now handle
     *  'am, 'ang, etc. specially, so now this set is now just the
     *  postsuffixes.
     */
	private static final String farrights = "d,s"; 

	static {
		readData();

        /* Initialize to Extended Wylie keyboard.  The preferences
         * mechanism will switch this to the preferred keyboard. */
        setKeyboard(keyboard);
	}

    /** Assumes that the TMW font files are resources associated with
     *  this class and loads those font files.
     *  @throws Error if that assumption does not hold */
    private static void readInFontFiles() {
        /* Note the leading slashes on these paths: */
        readInFontFile("/Fonts/TibetanMachineWeb/timwn.ttf");
        readInFontFile("/Fonts/TibetanMachineWeb/timwn1.ttf");
        readInFontFile("/Fonts/TibetanMachineWeb/timwn2.ttf");
        readInFontFile("/Fonts/TibetanMachineWeb/timwn3.ttf");
        readInFontFile("/Fonts/TibetanMachineWeb/timwn4.ttf");
        readInFontFile("/Fonts/TibetanMachineWeb/timwn5.ttf");
        readInFontFile("/Fonts/TibetanMachineWeb/timwn6.ttf");
        readInFontFile("/Fonts/TibetanMachineWeb/timwn7.ttf");
        readInFontFile("/Fonts/TibetanMachineWeb/timwn8.ttf");
        readInFontFile("/Fonts/TibetanMachineWeb/timwn9.ttf");
    }

    /** Assumes that the TMW font file at the given path is a resource
     *  associated with this class and loads that font file.
     *  @param path a path within the JAR containing this class file
     *  @throws Error if that assumption does not hold */
    private static void readInFontFile(String path) {

        // Note that the TM and TMW fonts do not have hanging
        // baselines.  They have Roman baselines.  Tony Duff said this
        // is subtly necessary and that only an OpenType font can
        // support baselines properly.

        try {
            InputStream is = TibetanMachineWeb.class.getResourceAsStream(path);
            if (null == is) {
                throw new Error("You selected the optional behavior of loading the TibetanMachineWeb font family manually, but the resource "
                                + path + " could not be found.");
            }
            Font.createFont(Font.TRUETYPE_FONT, is);
        } catch( Exception e ) {
            e.printStackTrace();
            ThdlDebug.noteIffyCode();
        }
    }

/**
* This method reads the data file ("tibwn.ini"), constructs
* the character, punctuation, and vowel lists, as well as
* performing other acts of initialization.
*/
	private static void readData() {
        if (!ThdlOptions.getBooleanOption("thdl.rely.on.system.tmw.fonts")) {
            readInFontFiles();
        }

		webFontAttributeSet[0] = null;
		for (int i=1; i<webFontAttributeSet.length; i++) {
			webFontAttributeSet[i] = new SimpleAttributeSet();
			StyleConstants.setFontFamily(webFontAttributeSet[i],tmwFontNames[i]);
		}

		StringTokenizer sTok;
		topSet = new HashSet();
		leftSet = new HashSet();
		rightSet = new HashSet();
		farRightSet = new HashSet();

		sTok = new StringTokenizer(tops, ",");
		while (sTok.hasMoreTokens())
			topSet.add(sTok.nextToken());

		sTok = new StringTokenizer(lefts, ",");
		while (sTok.hasMoreTokens())
			leftSet.add(sTok.nextToken());

		sTok = new StringTokenizer(rights, ",");
		while (sTok.hasMoreTokens())
			rightSet.add(sTok.nextToken());

		sTok = new StringTokenizer(farrights, ",");
		while (sTok.hasMoreTokens())
			farRightSet.add(sTok.nextToken());

		top_vowels = new HashSet();
		top_vowels.add(TibetanMachineWeb.i_VOWEL);
		top_vowels.add(TibetanMachineWeb.e_VOWEL);
		top_vowels.add(TibetanMachineWeb.o_VOWEL);
		top_vowels.add(TibetanMachineWeb.ai_VOWEL);
		top_vowels.add(TibetanMachineWeb.au_VOWEL);
		top_vowels.add(TibetanMachineWeb.reverse_i_VOWEL);

		try {
			URL url = TibetanMachineWeb.class.getResource(fileName);
			if (url == null) {
				System.err.println("Cannot find " + fileName + "; aborting.");
				System.exit(1);
			}
			InputStreamReader isr = new InputStreamReader(url.openStream());
			BufferedReader in = new BufferedReader(isr);

            if (ThdlOptions.getBooleanOption("thdl.verbose")) {
                System.out.println("Reading Tibetan Machine Web code table "
                                   + fileName);
            }
			String line;
			boolean hashOn = false;
			boolean isSanskrit = false; //FIXME: this is never read.
			boolean ignore = false;

			while ((line = in.readLine()) != null) {
				if (line.startsWith("<?")) { //line is command
					if (line.equalsIgnoreCase("<?Consonants?>")) {
						isSanskrit = false;
						hashOn = false;
						line = in.readLine();
						charSet = new HashSet();
						StringTokenizer st = new StringTokenizer(line,",");
						while (st.hasMoreTokens()) {
                            String ntk;
							charSet.add(ntk = st.nextToken());
                            validInputSequences.put(ntk, anyOldObjectWillDo);
                        }
					}
					else if (line.equalsIgnoreCase("<?Vowels?>")) {
						isSanskrit = false;
						hashOn = false;
						line = in.readLine();
						vowelSet = new HashSet();
						StringTokenizer st = new StringTokenizer(line,",");
						while (st.hasMoreTokens()) {
                            String ntk;
							vowelSet.add(ntk = st.nextToken());
                            validInputSequences.put(ntk, anyOldObjectWillDo);
                        }
					}
					else if (line.equalsIgnoreCase("<?Other?>")) {
						isSanskrit = false;
						hashOn = false;
						line = in.readLine();
						puncSet = new HashSet();
						StringTokenizer st = new StringTokenizer(line,",");
						while (st.hasMoreTokens()) {
                            String ntk;
							puncSet.add(ntk = st.nextToken());
                            validInputSequences.put(ntk, anyOldObjectWillDo);
                        }
					}

					else if (line.equalsIgnoreCase("<?Input:Punctuation?>")
						|| line.equalsIgnoreCase("<?Input:Vowels?>")
						|| line.equalsIgnoreCase("<?Input:Tibetan?>")) {
						isSanskrit = false;
						hashOn = true;
						ignore = false;
					}
					else if (line.equalsIgnoreCase("<?Input:Sanskrit?>")) {
						isSanskrit = true;
						hashOn = true;
						ignore = false;
					}
					else if (line.equalsIgnoreCase("<?ToWylie?>")) {
						isSanskrit = false;
						hashOn = false;
						ignore = false;
					}
					else if (line.equalsIgnoreCase("<?Ignore?>"))
						ignore = true;
				}
				else if (line.startsWith("//")) //comment
					;
				else if (line.equals("")) //empty string
					;
				else if (!ignore) {
					StringTokenizer st = new StringTokenizer(line,DELIMITER,true);

					String wylie = new String();
					DuffCode[] duffCodes = new DuffCode[11];

					int k = 0;

					while (st.hasMoreTokens()) {
						String val = st.nextToken();

						if (val.equals(DELIMITER))
							k++;

						else if (!val.equals("")) {							
							switch (k) {
								case 0: //wylie key
									wylie = val;
									break;

								case 1:
									duffCodes[TM] = new DuffCode(val,false);
									break;

								case 2: //reduced-size character if there is one
									duffCodes[REDUCED_C] = new DuffCode(val,true);
									break;

								case 3: //TibetanMachineWeb code
									duffCodes[k-1] = new DuffCode(val,true);
									TMtoTMW[duffCodes[TM].fontNum-1][duffCodes[TM].charNum-32] = duffCodes[TMW];
									break;
								case 4:
								case 5:
								case 6:
								case 7:
								case 8:
								case 9:
									duffCodes[k-1] = new DuffCode(val,true);
									break;

								case 10: //Unicode: ignore for now
									break;

								case 11: //half-height character if there is one
									duffCodes[HALF_C] = new DuffCode(val,true);
									break;

								case 12: //special bindu-value if vowel+bindu are one glyph
									DuffCode binduCode = new DuffCode(val,true);
									binduMap.put(duffCodes[TMW],binduCode);
									break;
							}
						}
					}

					if (hashOn)
						tibHash.put(wylie,duffCodes);

					int font = duffCodes[2].fontNum;
					int code = duffCodes[2].charNum-32;
					toHashKey[font][code] = wylie;
				}
			}
		}
		catch (IOException e) {
			System.out.println("file Disappeared");
            ThdlDebug.noteIffyCode();
		}

		hasReadData = true;
	}

/**
* (Re-)sets the keyboard.
* @param kb the keyboard to be installed. If null, then the
* Extended Wylie keyboard is installed
* @return true if the keyboard was successfully set, false
* if there was an error
*/
public static boolean setKeyboard(TibetanKeyboard kb) {
	keyboard = kb;

	if (currentKeyboardIsExtendedWylie()) { //wylie keyboard
		hasDisambiguatingKey = true;
		disambiguating_key = WYLIE_DISAMBIGUATING_KEY;
		hasSanskritStackingKey = true;
		hasTibetanStackingKey = false;
		isStackingMedial = true;
		stacking_key = WYLIE_SANSKRIT_STACKING_KEY;
		isAChenRequiredBeforeVowel = false;
		isAChungConsonant = false;
		hasAVowel = true;
		aVowel = WYLIE_aVOWEL;
		if (!vowelSet.contains(WYLIE_aVOWEL)) {
			vowelSet.add(WYLIE_aVOWEL);
            validInputSequences.put(WYLIE_aVOWEL, anyOldObjectWillDo);
        }
	}
	else {
		hasDisambiguatingKey = keyboard.hasDisambiguatingKey();
		if (hasDisambiguatingKey)
			disambiguating_key = keyboard.getDisambiguatingKey();

		hasSanskritStackingKey = keyboard.hasSanskritStackingKey();
		hasTibetanStackingKey = keyboard.hasTibetanStackingKey();
		if (hasSanskritStackingKey || hasTibetanStackingKey) {
			isStackingMedial = keyboard.isStackingMedial();
			stacking_key = keyboard.getStackingKey();
		}

		isAChenRequiredBeforeVowel = keyboard.isAChenRequiredBeforeVowel();
		isAChungConsonant = keyboard.isAChungConsonant();
		hasAVowel = keyboard.hasAVowel();
	}
	return true;
}

/**
* (Re-)sets the keyboard.
* @param url the URL of the keyboard to be installed.
* If null, then the Extended Wylie keyboard is 
* installed
* @return true if the keyboard was successfully set, false
* if there was an error
*/
public static boolean setKeyboard(URL url) {
	try {
        TibetanKeyboard kb = new TibetanKeyboard(url);
		if (setKeyboard(kb))
			return true;
		else
			return false;
	}
	catch (TibetanKeyboard.InvalidKeyboardException ike) {
		System.out.println("can't create the keyboard associated with " + url);
        ThdlDebug.noteIffyCode();
		return false;
	}
}

/**
* Gets the AttributeSet for the given TibetanMachineWeb font.
* This information is required in order to be able to put styled
* text into {@link TibetanDocument TibetanDocument}.
* @param font the number of the TibetanMachineWeb font for which
* you want the SimpleAttributeSet: TibetanMachineWeb = 1, 
* TibetanMachineWeb1 = 2, TibetanMachineWeb = 3, etc. up to 10
* @return a SimpleAttributeSet for the given font - that is,
* a way of encoding the font itself
*/
public static SimpleAttributeSet getAttributeSet(int font) {
	if (font > -1 && font < webFontAttributeSet.length)
		return webFontAttributeSet[font];
	else
		return null;
}

/**
* Says whether or not the character is formatting.
* @param c the character to be checked
* @return true if c is formatting (TAB or
* ENTER), false if not
*/
public static boolean isFormatting(char c) {
	if (c < 32 || c > 126)
		return true;
	else
		return false;
/*
	if (		c == KeyEvent.VK_TAB
		|| 	c == KeyEvent.VK_ENTER)

		return true;
	else
		return false;
*/
}

/**
* Checks to see if the passed string
* is a character in the installed keyboard.
*
* @param s the string you want to check
* @return true if s is a character in the current keyboard,
* false if not
*/
public static boolean isChar(String s) {
	if (currentKeyboardIsExtendedWylie())
		return charSet.contains(s);
	else
		return keyboard.isChar(s);
}

/**
* Checks to see if the passed string
* is a character in Extended Wylie.
* @param s the string to be checked
* @return true if s is a character in
* Extended Wylie transliteration, false if not
*/
public static boolean isWylieChar(String s) {
	return charSet.contains(s);
}

/**
* Checks to see if the passed string
* is punctuation in the installed keyboard.
* @param s the string you want to check
* @return true if s is punctuation in the current
* keyboard, false if not
*/
public static boolean isPunc(String s) {
	if (currentKeyboardIsExtendedWylie())
		return puncSet.contains(s);
	else
		return keyboard.isPunc(s);
}

/**
* This method checks to see if the passed string
* is punctuation in Extended Wylie.
* @param s the string to be checked
* @return true if s is punctuation in
* Extended Wylie transliteration, false if not
*/
public static boolean isWyliePunc(String s) {
	return puncSet.contains(s);
}

/**
* Checks to see if the passed string
* is a vowel in the installed keyboard.
* @param s the string you want to check
* @return true if s is a vowel in the current
* keyboard, false if not
*/
public static boolean isVowel(String s) {
	if (currentKeyboardIsExtendedWylie())
		return vowelSet.contains(s);
	else
		return keyboard.isVowel(s);
}

/**
* Checks to see if the concatenation of x and y is ambiguous in
* Extended Wylie.  gya and g.ya, bla and b.la, and bra and b.ra are
* the only syntactically legal ambigous fellows, as stacks like blha,
* blda, brla, brkya, brgya, brka, etc. are unambiguous.
* @param x the prefix
* @param y the root stack
* @return true if x + y is ambiguous in the Extended Wylie
* transliteration, false if not
*/
public static boolean isAmbiguousWylie(String x, String y) {
    // What about ambiguity between wa-zur and wa? dwa vs. d.wa, e.g.?
    // Some would say it doesn't matter, because that's illegal.  wa
    // doesn't take any prefixes.  But I want even illegal stuff to
    // work well end-to-end (i.e., converting tibetan X to wylie Y to
    // tibetan Z should get you X==Z in a perfect world), and it
    // doesn't confuse the legal stuff.

	return (("g".equals(x) && "y".equals(y))
            || ("g".equals(x) && "w".equals(y))
            || ("d".equals(x) && "w".equals(y))
            || ("b".equals(x) && "l".equals(y))
            || ("b".equals(x) && "r".equals(y)));
}

/**
* Checks to see if the passed string
* is a vowel in Extended Wylie.
* @param s the string to be checked
* @return true if s is a vowel in
* Extended Wylie transliteration, false if not
*/
public static boolean isWylieVowel(String s) {
	return vowelSet.contains(s);
}

/**
* Returns true iff this Wylie is valid as a leftmost character in a
* Tibetan syllable.  For example, in the syllable 'brgyad', 'b' is the
* leftmost character. Valid leftmost characters include g, d, b, ',
* and m.
* @param s the (Wylie) string to be checked
* @return true if s is a possible leftmost character in a Tibetan
* syllable, false if not.  */
public static boolean isWylieLeft(String s) {
	return leftSet.contains(s);
}

/**
* Returns true iff this Wylie is valid as a right (post-vowel)
* character in a Tibetan syllable.  For example, in the syllable
* 'lags', 'g' is in the right character position. Valid right
* characters include g, ng, d, n, b, m, r, l, s, ', and T.
* @param s the (Wylie) string to be checked
* @return true if s is a possible right character in a Tibetan
* syllable, false if not.  */
public static boolean isWylieRight(String s) {
	return rightSet.contains(s);
}

/**
* Returns true iff this Wylie is valid as a postsuffix in a
* Tibetan syllable.
* @param s the string to be checked
* @return true if s is a possible postsuffix in a Tibetan
* syllable, false if not.  */
public static boolean isWylieFarRight(String s) {
	return farRightSet.contains(s);
}

/**
* Returns true iff this Wylie is valid as a head letter in a Tibetan
* syllable.
* @param s the string to be checked
* @return true if s is a possible superscribed letter in a Tibetan
* syllable, false if not.  */
public static boolean isWylieTop(String s) {
	return topSet.contains(s);
}

/**
* Converts character to its Extended Wylie correspondence.
* This assumes that the passed string is a character
* in the current keyboard.
* @param s the string to be converted
* @return the Wylie character corresponding to
* s, or null if there is no such character
* @see TibetanKeyboard
*/
public static String getWylieForChar(String s) {
	if (currentKeyboardIsExtendedWylie())
		return s;

	return keyboard.getWylieForChar(s);
}

    /** Returns true iff the currently active keyboard is the
     *  built-in, extended Wylie keyboard. */
    public static boolean currentKeyboardIsExtendedWylie() {
        return (null == getKeyboard());
    }

/**
 *  Returns the current keyboard, or, if the current keyboard is the
 *  Extended Wylie keyboard, null.  */
    public static TibetanKeyboard getKeyboard() {
        return keyboard;
    }

/**
* Converts punctuation to its Extended Wylie correspondence.
* This assumes that the passed string is punctuation
* in the current keyboard.
* @param s the string to be converted
* @return the Wylie punctuation corresponding to
* s, or null if there is no such punctuation
* @see TibetanKeyboard
*/
public static String getWylieForPunc(String s) {
	if (currentKeyboardIsExtendedWylie())
		return s;

	return keyboard.getWylieForPunc(s);
}

/**
* Converts vowel to its Extended Wylie correspondence.
* This assumes that the passed string is a vowel
* in the current keyboard.
* @param s the string to be converted
* @return the Wylie vowel corresponding to
* s, or null if there is no such vowel
* @see TibetanKeyboard
*/
public static String getWylieForVowel(String s) {
	if (currentKeyboardIsExtendedWylie())
		return s;

	return keyboard.getWylieForVowel(s);
}

/**
* Gets the DuffCode required for a vowel, if
* affixed to the given hashKey.
* @param hashKey the key for the character the
* vowel is to be affixed to
* @param vowel the vowel you want the DuffCode for
* @return the DuffCode for the vowel in the given
* context, or null if there is no such vowel in
* the context
* @see DuffCode
*/
public static DuffCode getVowel(String hashKey, int vowel) {
	DuffCode[] dc = (DuffCode[])tibHash.get(hashKey);
	
	if (null == dc)
		return null;

	return dc[vowel]; //either a vowel or null
}

/**
* Checks to see if a glyph exists for this hash key.
* @param hashKey the key to be checked
* @return true if there is a glyph corresponding to
* hashKey, false if not
*/
public static boolean hasGlyph(String hashKey) {
	if (tibHash.get(hashKey)==null)
		return false;
	else
		return true;
}

/**
* Gets a glyph for this hash key. Hash keys are not identical to Extended
* Wylie. The hash key for a Tibetan stack separates the members of the stack
* with '-', for example, 's-g-r'. In Sanskrit stacks, '+' is used, e.g. 'g+h+g+h'.
* @param hashKey the key for which you want a DuffCode
* @return the TibetanMachineWeb DuffCode value for hashKey
* @see DuffCode
*/
public static DuffCode getGlyph(String hashKey) {
	DuffCode[] dc = (DuffCode[])tibHash.get(hashKey);
	return dc[TMW];
}

/**
* Gets the half height character for this hash key.
* @param hashKey the key you want a half height glyph for
* @return the TibetanMachineWeb DuffCode of hashKey's
* reduced height glyph, or null if there is no such glyph
* @see DuffCode
*/
public static DuffCode getHalfHeightGlyph(String hashKey) {
	DuffCode[] dc = (DuffCode[])tibHash.get(hashKey);
	if (dc == null)
		return null;

	return dc[REDUCED_C];
}

private static DuffCode getTMtoTMW(int font, int code) {
	if (code > 255-32) {
		switch (code) {
			case 8218-32: //sby
				code = 130-32;
				break;

			case 8230-32: //sgr
				code = 133-32;
				break;

			case 8225-32: //spr
				code = 135-32;
				break;

			case 8117-32: //tshw
				code = 146-32;
				break;

			case 8126-32: //rw
				code = 149-32;
				break;

			case 8482-32: //grw
				code = 153-32;
				break;

			default:
				return null;
		}
	}

	return TMtoTMW[font][code];
}

private static int getTMFontNumber(String name) {
    String internedName = name.intern();
	for (int i=1; i<tmFontNames.length; i++) {
		if (internedName == tmFontNames[i])
			return i;
	}
	return 0;
}

/**
* Gets the TibetanMachineWeb font number for this font name.
* @param name a font name
* @return between 1 and 10 if the font is one
* of the TibetanMachineWeb fonts, otherwise 0
*/
public static int getTMWFontNumber(String name) {
    String internedName = name.intern();
	for (int i=1; i<tmwFontNames.length; i++) {
        // Thanks to interning, we can use == rather than .equals().
		if (internedName == tmwFontNames[i])
			return i;
	}
	return 0;
}

/**
* Gets the hash key associated with this glyph.
* @param font a TibetanMachineWeb font number
* @param code an ASCII character code
* @return the hashKey corresponding to the character
* at font, code
*/
public static String getHashKeyForGlyph(int font, int code) {
	code = code - 32;
	return toHashKey[font][code];
}

/**
* Gets the hash key associated with this glyph.
* @param dc a DuffCode denoting a TibetanMachineWeb glyph
* @return the hashKey corresponding to the character at dc
*/
public static String getHashKeyForGlyph(DuffCode dc) {
	int font = dc.fontNum;
	int code = dc.charNum-32;
	return toHashKey[font][code];
}

/**
* Gets the Extended Wylie for a given hash key.
* The hash keys in charList and so on may include separating
* characters. For example, Wylie 'sgr' has the hash key 's-g-r'.
* This method takes a hash key and converts it its correct
* Wylie value, and therefore is useful in conversions from
* TibetanMachineWeb to Wylie.
* @param hashKey the hash key for a glyph
* @return the Wylie value of that hash key
*/
public static String wylieForGlyph(String hashKey) {
	if (hashKey.indexOf(WYLIE_SANSKRIT_STACKING_KEY) > -1)
		return hashKey; //because '+' remains part of Extended Wylie for Sanskrit stacks

	if (hashKey.charAt(0) == '-')
		return hashKey; //because must be '-i' or '-I' vowels

	StringTokenizer st = new StringTokenizer(hashKey, "-");
	StringBuffer sb = new StringBuffer();

	while (st.hasMoreTokens())
		sb.append(st.nextToken());

	return sb.toString();
}

/**
* Gets the Extended Wylie value for this glyph.
* @param font the font of the TibetanMachineWeb
* glyph you want the Wylie of
* @param code the TibetanMachineWeb glyph
* you want the Wylie of
* @return the Wylie value corresponding to the
* glyph denoted by font, code
*/
public static String getWylieForGlyph(int font, int code) {
	String hashKey = getHashKeyForGlyph(font, code);
	return wylieForGlyph(hashKey);
}

/**
* Gets the Extended Wylie value for this glyph.
* @param dc the DuffCode of the glyph you want
* the Wylie of
* @return the Wylie value corresponding to the
* glyph denoted by dc
*/
public static String getWylieForGlyph(DuffCode dc) {
	String hashKey = getHashKeyForGlyph(dc);
    if (hashKey == null) {
        // This error message is documented in
        // www/htdocs/TMW_RTF_TO_THDL_WYLIE.html, so change them both
        // when you change this.
        return "<<[[JSKAD_TMW_TO_WYLIE_ERROR_NO_SUCH_WYLIE: Cannot convert DuffCode " + dc + " to THDL Extended Wylie.  Please see the documentation for the TMW font and transcribe this yourself.]]>>";
    }
	return wylieForGlyph(hashKey);
}

    /** This addresses bug 624133, "Input freezes after impossible
     *  character".  Returns true iff s is a proper prefix of some
     *  legal input for this keyboard.  In the extended Wylie
     *  keyboard, hasInputPrefix("S") is true because "Sh" is legal
     *  input.  hasInputPrefix("Sh") is false because though "Sh" is
     *  legal input, ("Sh" + y) is not valid input for any non-empty
     *  String y. */
    public static boolean hasInputPrefix(String s) {
        if (!currentKeyboardIsExtendedWylie()) {
            return keyboard.hasInputPrefix(s);
        } else {
            return validInputSequences.hasPrefix(s);
        }
    }


/**
* Says whether or not this glyph involves a Sanskrit stack.
* @param font the font of a TibetanMachineWeb glyph
* @param code the ASCII value of a TibetanMachineWeb glyph
* @return true if this glyph is a Sanskrit stack,
* false if not
*/
public static boolean isSanskritStack(int font, int code) {
	String val = toHashKey[font][code];
	if (val.indexOf(WYLIE_SANSKRIT_STACKING_KEY) == -1)
		return false;
	else
		return true;
}

/**
* Says whether or not this glyph involves a Sanskrit stack.
* @param dc the DuffCode of a TibetanMachineWeb glyph
* @return true if this glyph is a Sanskrit stack,
* false if not
*/
public static boolean isSanskritStack(DuffCode dc) {
	int font = dc.fontNum;
	int code = dc.charNum-32;

	if (isSanskritStack(font, code))
		return true;
	else
		return false;
}

/**
* Says whether or not this glyph involves a Tibetan stack.
* @param font the font of a TibetanMachineWeb glyph
* @param code the ASCII value of a TibetanMachineWeb glyph
* @return true if this glyph is a Tibetan stack,
* false if not
*/
public static boolean isStack(int font, int code) {
	String val = toHashKey[font][code];
	if (val.indexOf('-') < 1) //we allow '-i' and '-I' in as vowels
		return false;
	else
		return true;
}

/**
* Says whether or not this glyph involves a Tibetan stack.
* @param dc the DuffCode of a TibetanMachineWeb glyph
* @return true if this glyph is a Tibetan stack,
* false if not
*/
public static boolean isStack(DuffCode dc) {
	int font = dc.fontNum;
	int code = dc.charNum-32;

	if (isStack(font, code))
		return true;
	else
		return false;
}

/**
* Gets the hash with information about each character and stack.
* @return a hash containing a key for each
* entity defined in Wylie, whose object is the
* DuffCode for that key
*/
public static Map getTibHash() {
	return tibHash;
}

/**
* Gets the hash for characters that require special bindus.
* @return a hash whose keys are all vowel glyphs (DuffCodes)
* that require a special bindu, and whose objects
* are the vowel+bindu glyph (DuffCode) corresponding to each
* such vowel glyph
*/
public static Map getBinduMap() {
	return binduMap;
}

/**
* Returns true iff the keyboard has a disambiguating key.
* @return true if the installed keyboard has a disambiguating key,
* false if not
* @see TibetanKeyboard */
public static boolean hasDisambiguatingKey() {
	return hasDisambiguatingKey;
}

/**
* Gets the disambiguating key.
* @return the disambiguating key for the installed
* keyboard, or ' ' if there is no such key
* @see TibetanKeyboard
*/
public static char getDisambiguatingKey() {
	return disambiguating_key;
}

/**
* Returns true iff the keyboard has a Sanksrit stacking key.
* @return true if a stacking key is required to type Sanskrit stacks,
* false if not
* @see TibetanKeyboard */
public static boolean hasSanskritStackingKey() {
	return hasSanskritStackingKey;
}

/**
* Returns true iff the keyboard has a Tibetan stacking key.
* @return true if a stacking key is required to type Tibetan stacks,
* false if not
* @see TibetanKeyboard */
public static boolean hasTibetanStackingKey() {
	return hasTibetanStackingKey;
}

/**
* Returns true iff stacking is medial.
* @return true if the stacking key is medial, false if not, or if
* there is no stacking key
* @see TibetanKeyboard */
public static boolean isStackingMedial() {
	return isStackingMedial;
}

/**
* Gets the stacking key.
* @return the stacking key, or ' ' if there
* isn't one
* @see TibetanKeyboard
*/
public static char getStackingKey() {
	return stacking_key;
}

/**
* Returns true iff achen is required before vowels.
* @return true if you have to type achen first before you can get a
* vowel with achen, false if you can just type the vowel by itself (as
* in Wylie)
* @see TibetanKeyboard */
public static boolean isAChenRequiredBeforeVowel() {
	return isAChenRequiredBeforeVowel;
}

/**
* Returns true iff achung is treated as a consonant.
* @return true if a-chung is considered a consonant for the purposes
* of stacking, false if not (as in Wylie)
* @see TibetanKeyboard */
public static boolean isAChungConsonant() {
	return isAChungConsonant;
}

/**
* Returns true iff there is a key for the invisible 'a' vowel in this
* keyboard.
* @return true if the installed keyboard has a dummy a vowel, false if
* not
* @see TibetanKeyboard */
public static boolean hasAVowel() {
	return hasAVowel;
}

/**
* Gets the invisible 'a' vowel.
* @return the dummy 'a'-vowel for the installed
* keyboard, or "" if there is no such vowel
* @see TibetanKeyboard
*/
public static String getAVowel() {
	return aVowel;
}

/**
* Returns true iff this glyph is a top (superscript) vowel.
* @param a DuffCode representing a TibetanMachineWeb glyph
* @return true if the glyph is a top-hanging (superscript) vowel (i,
* u, e, o, ai, or ao) and false if not */
public static boolean isTopVowel(DuffCode dc) {
	String wylie = getWylieForGlyph(dc);
	if (top_vowels.contains(wylie))
		return true;

	return false;
}

    /** Returns true if and only if ch, which is an ASCII character
        that you can think of as an arbitrary index into one of the
        Tibetan fonts, is a character that is appropriate for ending a
        line of Tibetan.  <code>'-'</code>, for example, represents
        the tsheg (the little dot after a syllable) in (FIXME: Edward,
        is this true?) all of the TMW fonts.  Thus, this would return
        true for <code>'-'</code>.

        Note that ch is <b>not</b> the Wylie transliteration; it is an
        arbitrary character (well, not quite, since ' ', '\t', '\n' et
        cetera seem to have been wisely chosen to represent Tibetan
        whitespace, but pretty arbitrary).  If you open up MS Word,
        select TibetanMachineWeb1, and type a hyphen,
        i.e. <code>'-'</code>, you'll see a tsheg appear.  If you open
        Jskad and type a hyphen, you won't see a tsheg.
                    
        @param ch the ASCII character "index" into the TMW font

        @return true iff this is a tsheg or whitespace or the like */
    public static boolean isTMWFontCharBreakable(char ch) {
        // DLC FIXME: treat whitespace differently than you do
        // punctuation.  And treat "/ka nga/", Tibetan verse,
        // specially in the caller of this method.

        if (false) {
        //<?Input:Punctuation?>
        int ord = (int)ch;

        // FIXME: why did 94 appear twice in tibwn.ini's punctuation section?
        if (32 == ord) return true;
        if (45 == ord) return true;
        if (107 == ord) return true;
        if (103 == ord) return true;
        if (104 == ord) return true;
        if (105 == ord) return true;
        if (43 == ord) return true;
        if (40 == ord) return true;
        if (41 == ord) return true;
        if (38 == ord) return true;
        if (39 == ord) return true;
        if (93 == ord) return true;
        if (94 == ord) return true;
        if (92 == ord) return true;
        if (91 == ord) return true;
        } // DLC FIXME
        return ('-' == ch /* FIXME: this is the tsheg (i.e., the Wylie is ' '), but we have no constant for it. */
                || ' ' == ch /* FIXME: this is space (i.e., the Wylie is '_'), but we have no constant for it. */
                || '\t' == ch /* FIXME: this is some sort of whitespace */
                || '\n' == ch /* FIXME: this is some sort of whitespace */
                || '/' == ch /* a shad */
                );

        // FIXME: am I missing anything?  tabs etc.?
    }
}
