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
Library (THDL). Portions created by the THDL are Copyright 2001 THDL.
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

/**
* Interfaces between Extended Wylie and the TibetanMachineWeb fonts.
* To do this this must first read the code table, which lives in "tibwn.ini",
* and which must be found in the same directory as this class.
* @author Edward Garrett, Tibetan and Himalayan Digital Library
* @version 1.0
*/
public class TibetanMachineWeb {
	private static boolean hasReadData = false;
	private static TibetanKeyboard keyboard = null;
	private static final String DEFAULT_KEYBOARD = "default_keyboard.ini";
	private static Set charSet = null;
	private static Set vowelSet = null;
	private static Set puncSet = null;
	private static Set leftSet = null;
	private static Set rightSet = null;
	private static Set farRightSet = null;
	private static Map tibHash = new HashMap();
	private static Map binduMap = new HashMap();
	private static String[][] toHashKey = new String[11][95]; //note: 0 slot is not used
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

	public static final String[] tmFontNames = {
		null,
		"TibetanMachine",
		"TibetanMachineSkt1",
		"TibetanMachineSkt2",
		"TibetanMachineSkt3",
		"TibetanMachineSkt4"
	};
	public static final String[] tmwFontNames = {
		null,
		"TibetanMachineWeb",
		"TibetanMachineWeb1",
		"TibetanMachineWeb2",
		"TibetanMachineWeb3",
		"TibetanMachineWeb4",
		"TibetanMachineWeb5",
		"TibetanMachineWeb6",
		"TibetanMachineWeb7",
		"TibetanMachineWeb8",
		"TibetanMachineWeb9"
	};
/**
* the Wylie for bindu/anusvara
*/
	public static final char BINDU = 'M';
/**
* the Wylie for tsheg
*/
	public static final char TSHEG = ' '; //this character occurs in all ten TMW fonts
/**
* the Wylie for whitespace
*/
	public static final char SPACE = '_'; //this character occurs in all ten TMW fonts
/**
* the Sanskrit stacking separator used in Extended Wylie
*/
	public static final char WYLIE_SANSKRIT_STACKING_KEY = '+';
/**
* the Wylie disambiguating key, as a char
*/
	public static final char WYLIE_DISAMBIGUATING_KEY = '.';
/**
* the Wylie for the invisible 'a' vowel
*/
	public static final String WYLIE_aVOWEL = "a";
/**
* the Wylie for achung
*/
	public static final String ACHUNG = "'";
/**
* the Wylie for achen
*/
	public static final String ACHEN = "a";
/**
* the Wylie for gigu
*/
	public static final String i_VOWEL = "i";
/**
* the Wylie for zhebju
*/
	public static final String u_VOWEL = "u";
/**
* the Wylie for drengbu
*/
	public static final String e_VOWEL = "e";
/**
* the Wylie for naro
*/
	public static final String o_VOWEL = "o";
/**
* the Wylie for double drengbu
*/
	public static final String ai_VOWEL = "ai";
/**
* the Wylie for double naro
*/
	public static final String au_VOWEL = "au";
/**
* the Wylie for the subscript achung vowel
*/
	public static final String A_VOWEL = "A";
/**
* the Wylie for log yig gigu
*/
	public static final String reverse_i_VOWEL = "-i";
/**
* the Wylie for the vowel achung + gigu
*/
	public static final String I_VOWEL = "I";
/**
* the Wylie for the vowel achung + zhebju
*/
	public static final String U_VOWEL = "U";
/**
* the Wylie for the vowel achung + log yig gigu
*/
	public static final String reverse_I_VOWEL = "-I";
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

	private static final String lefts = "g,d,b,m,'";
	private static final String rights = "g,ng,d,n,b,m,r,l,s,',T";
	private static final String farrights = "d,s,ng";

	static {

        // FIXME: we have it so that you can select the default
        // keyboard via the preferences mechanism.  We can remove this
        // DEFAULT_KEYBOARD stuff, can't we?

		readData();

		URL keyboard_url = TibetanMachineWeb.class.getResource(DEFAULT_KEYBOARD);
		if (null != keyboard_url) {
			try {
                TibetanKeyboard kb = new TibetanKeyboard(keyboard_url);
                setKeyboard(kb); // this can't throw the InvalidKeyboardException
			}
			catch (TibetanKeyboard.InvalidKeyboardException ike) {
				System.out.println("invalid keyboard file or file not found: " + keyboard_url.toString());
                ThdlDebug.noteIffyCode();
				setKeyboard(keyboard);
			}
		}
		else
			setKeyboard(keyboard);
	}

/**
* This method reads the data file ("tibwn.ini"), constructs
* the character, punctuation, and vowel lists, as well as
* performing other acts of initialization.
*/
	private static void readData() {
		webFontAttributeSet[0] = null;
		for (int i=1; i<webFontAttributeSet.length; i++) {
			webFontAttributeSet[i] = new SimpleAttributeSet();
			StyleConstants.setFontFamily(webFontAttributeSet[i],tmwFontNames[i]);
		}

		StringTokenizer sTok;
		leftSet = new HashSet();
		rightSet = new HashSet();
		farRightSet = new HashSet();

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

			System.out.println("reading "+fileName);
			String line;
			boolean hashOn = false;
			boolean isSanskrit = false;
			boolean ignore = false;

			while ((line = in.readLine()) != null) {
				if (line.startsWith("<?")) { //line is command
					if (line.equalsIgnoreCase("<?Consonants?>")) {
						isSanskrit = false;
						hashOn = false;
						line = in.readLine();
						charSet = new HashSet();
						StringTokenizer st = new StringTokenizer(line,",");
						while (st.hasMoreTokens())
							charSet.add(st.nextToken());
					}
					else if (line.equalsIgnoreCase("<?Vowels?>")) {
						isSanskrit = false;
						hashOn = false;
						line = in.readLine();
						vowelSet = new HashSet();
						StringTokenizer st = new StringTokenizer(line,",");
						while (st.hasMoreTokens())
							vowelSet.add(st.nextToken());
					}
					else if (line.equalsIgnoreCase("<?Other?>")) {
						isSanskrit = false;
						hashOn = false;
						line = in.readLine();
						puncSet = new HashSet();
						StringTokenizer st = new StringTokenizer(line,",");
						while (st.hasMoreTokens())
							puncSet.add(st.nextToken());
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

	if (keyboard == null) { //wylie keyboard
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
		if (!vowelSet.contains(WYLIE_aVOWEL))
			vowelSet.add(WYLIE_aVOWEL);
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
	if (keyboard == null) {
		if (charSet.contains(s))
			return true;
		else
			return false;	
	}
	else
		if (keyboard.isChar(s))
			return true;
		else
			return false;
}

/**
* Checks to see if the passed string
* is a character in Extended Wylie.
* @param s the string to be checked
* @return true if s is a character in
* Extended Wylie transliteration, false if not
*/
public static boolean isWylieChar(String s) {
	if (charSet.contains(s))
		return true;

	return false;
}

/**
* Checks to see if the passed string
* is punctuation in the installed keyboard.
* @param s the string you want to check
* @return true if s is punctuation in the current
* keyboard, false if not
*/
public static boolean isPunc(String s) {
	if (keyboard == null) {
		if (puncSet.contains(s))
			return true;
		else
			return false;
	}
	else
		if (keyboard.isPunc(s))
			return true;
		else
			return false;
}

/**
* This method checks to see if the passed string
* is punctuation in Extended Wylie.
* @param s the string to be checked
* @return true if s is punctuation in
* Extended Wylie transliteration, false if not
*/
public static boolean isWyliePunc(String s) {
	if (puncSet.contains(s))
		return true;

	return false;
}

/**
* Checks to see if the passed string
* is a vowel in the installed keyboard.
* @param s the string you want to check
* @return true if s is a vowel in the current
* keyboard, false if not
*/
public static boolean isVowel(String s) {
	if (keyboard == null) {
		if (vowelSet.contains(s))
			return true;
		else
			return false;
	}
	else
		if (keyboard.isVowel(s))
			return true;
		else
			return false;
}

/**
* Checks to see if the passed string
* is a vowel in Extended Wylie.
* @param s the string to be checked
* @return true if s is a vowel in
* Extended Wylie transliteration, false if not
*/
public static boolean isWylieVowel(String s) {
	if (vowelSet.contains(s))
		return true;

	return false;
}

/**
* Returns true iff this Wylie is valid as a leftmost character in a
* Tibetan syllable.  For example, in the syllable 'brgyad', 'b' is the
* leftmost character. Valid leftmost characters include g, d, b, and
* m.
* @param s the (Wylie) string to be checked
* @return true if s is a possible leftmost character in a Tibetan
* syllable, false if not.  */
public static boolean isWylieLeft(String s) {
	if (keyboard != null)
		s = keyboard.getWylieForChar(s);

	if (leftSet.contains(s))
		return true;
	else
		return false;
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
	if (keyboard != null)
		s = keyboard.getWylieForChar(s);

	if (rightSet.contains(s))
		return true;
	else
		return false;
}

/**
* Returns true iff this Wylie is valid as a leftmost character in a
* Tibetan syllable.
* @param s the string to be checked
* @return true if s is a possible leftmost character in a Tibetan
* syllable, false if not.  */
public static boolean isWylieFarRight(String s) {
	if (keyboard != null)
		s = keyboard.getWylieForChar(s);

	if (farRightSet.contains(s))
		return true;
	else
		return false;
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
	if (keyboard == null)
		return s;

	return keyboard.getWylieForChar(s);
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
	if (keyboard == null)
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
	if (keyboard == null)
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
	for (int i=1; i<tmFontNames.length; i++) {
		if (name.equals(tmFontNames[i]))
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
	for (int i=1; i<tmwFontNames.length; i++) {
		if (name.equals(tmwFontNames[i]))
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
	return wylieForGlyph(hashKey);
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
}
