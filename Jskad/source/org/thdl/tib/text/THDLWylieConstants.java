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

/** This is where basic, static knowledge of THDL's Extended Wylie is housed.
 *  @see TibetanMachineWeb */
public interface THDLWylieConstants {
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
* the Wylie disambiguating key, as a String
*/
	public static final String WYLIE_DISAMBIGUATING_KEY_STRING
        = new String(new char[] { WYLIE_DISAMBIGUATING_KEY });
/**
* the Wylie for the invisible 'a' vowel
*/
	public static final String WYLIE_aVOWEL = "a";
/**
* the Wylie for achung
*/
	public static final char ACHUNG_character = '\'';
/**
* the Wylie for achung
*/
	public static final String ACHUNG
        = new String(new char[] { ACHUNG_character });
/**
* the Wylie for the 28th of the 30 consonants, sa:
*/
	public static final String SA = "s";
/**
* the Wylie for the consonant ra:
*/
	public static final String RA = "r";
/**
* the Wylie for the 16th of the 30 consonants, ma:
*/
	public static final String MA = "m";
/**
* the Wylie for the 4th of the 30 consonants, nga:
*/
	public static final String NGA = "ng";
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
}
