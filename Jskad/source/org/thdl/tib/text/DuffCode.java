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

import java.util.StringTokenizer;

import org.thdl.util.ThdlDebug;

/**
* An immutable representation of a Tibetan glyph in the
* TibetanMachineWeb or TibetanMachine families of fonts.
*
* A DuffCode consists of a font number, a character, and a character
* number. A font identification and a character are sufficient to
* uniquely identify any TibetanMachineWeb or TibetanMachine glyph.
*
* @author Edward Garrett, Tibetan and Himalayan Digital Library
* @version 1.0 */

public final class DuffCode {
/**
* the font number in which this glyph can be found,
* from 1 (TibetanMachineWeb) to 10 (TibetanMachineWeb9).
*/
	private int fontNum;
/**
* the character value of this glyph, as an integer (that is, ordinal)
*/
	private int charNum;

/**
* Called by {@link TibetanMachineWeb} to generate
* DuffCodes from the 'tibwn.ini' initialization file.
* This constructor expects to receive a string such as "1,33" or "33,1",
* i.e. a sequence of two numbers separated by a comma. These numbers
* represent a character: one number is its identifying font number, 
* and the other is the ASCII code of the character.
* 
* @param s the string to parse
* @param leftToRight should be true if the first number is the font number,
* false if the second number is the font number
*/
	public DuffCode(String s, boolean leftToRight) {
		StringTokenizer st = new StringTokenizer(s,",");

		try {
			String val1 = st.nextToken();
			String val2 = st.nextToken();

			Integer num1 = new Integer(val1);
			Integer num2 = new Integer(val2);

			if (leftToRight) {
                setFontNum(num1.intValue());
				charNum = num2.intValue();
			}
			else {
                setFontNum(num2.intValue());
				charNum = num1.intValue();
			}						
		}
		catch (NumberFormatException e) {
            ThdlDebug.noteIffyCode();
		}
	}

/**
* Called to create DuffCodes on the fly
* from an identifying font number and an ASCII character.
* 
* @param font the identifying number of the font
* @param ch a character
*/
	public DuffCode(int font, char ch) {
		setFontNum(font);
		charNum = (int)ch;
	}

    private void setFontNum(int font) {
        if (!(font >= 1 && font <= 10))
            throw new IllegalArgumentException("DuffCodes work with font numbers in the range [1, 5] or [1, 10].  This isn't in the range [1, 10]: " + font);
        fontNum = font;
    }

/**
* Gets the font number of this glyph.
* @return the identifying font number for this DuffCode
*/
	public int getFontNum() {
		return fontNum;
	}

/**
* Gets the character for this glyph, as an integer.
* @return the identifying character, converted to an
* integer, for this DuffCode
*/
	public int getCharNum() {
		return charNum;
	}

/**
* Gets the character for this glyph.
* @return the identifying character for this DuffCode
*/
	public char getCharacter() {
		return (char)charNum;
	}

/**
* Assigns a hashcode based on the font number and character for this
* glyph.
*
* @return the hash code for this object */
	public int hashCode() {
        return fontNum*256 + charNum;
	}

/**
* Evaluates two DuffCodes as equal iff their
* font numbers and characters are identical.
*
* @param o the object (DuffCode) you want to compare
* @return true if this object is equal to o, false if not
*/
	public boolean equals(Object o) {
		if (o instanceof DuffCode) {
			DuffCode dc = (DuffCode)o;

			if (fontNum == dc.fontNum && charNum == dc.charNum)
				return true;
		}
		return false;
	}

/**
* @return a string representation of this object */
	public String toString() {
        boolean[] err = new boolean[] { false };
        String wylie = TibetanMachineWeb.getWylieForGlyph(this, err);
        if (err[0]) wylie = "undefined";
		return "<duffcode wylie="
            + wylie + " font=" + fontNum
            + " charNum=" + charNum + " character="
            + new Character(getCharacter()).toString() + "/>";
	}
/**
 * @param TMW if this DuffCode represents a TMW glyph, not a TM glyph
 * @return a string representation of this object that does not refer
 * to its Wylie representation (because the TMW->Wylie error messages
 * call upon this when there is none, and you don't want an infinite
 * recursion (manifesting as a StackOverflowError)) */
	public String toString(boolean TMW) {
        boolean[] err = new boolean[] { false };
		return "<duffcode font="
            + (TMW
               ? TibetanMachineWeb.tmwFontNames
               : TibetanMachineWeb.tmFontNames)[fontNum]
            + " charNum=" + charNum + " character="
            + new Character(getCharacter()).toString() + "/>";
	}
}
