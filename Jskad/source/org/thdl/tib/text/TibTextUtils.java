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
import javax.swing.*; 
import javax.swing.text.*;
import javax.swing.text.rtf.RTFEditorKit;
import java.io.*;

import org.thdl.util.ThdlDebug;

/**
* Provides methods for converting back and forth between Extended
* Wylie and Tibetan represented in TibetanMachineWeb glyphs.  This
* class is not instantiable.
*
* <p>
* The class provides a variety of static methods for converting
* back and forth between Extended Wylie and TibetanMachineWeb.  The
* Wylie can be accessed as a String, while the TibetanMachineWeb can
* be exported as Rich Text Format.
*
* @author Edward Garrett, Tibetan and Himalayan Digital Library */
public class TibTextUtils implements THDLWylieConstants {
    /** Do not use this contructor. */
    private TibTextUtils() { super(); }


/**
* Converts a list of glyphs into an array of {@link DuffData DuffData}.
* The motivation for this is that most processes - for example using
* TibetanMachineWeb in HTML - only need to know what
* text to output, and when to change fonts. In general, they don't
* need to have an explicit indication for each glyph of the font
* for that glyph.
* @param glyphs the list of TibetanMachineWeb glyphs
* you want to convert
* @return an array of DuffData corresponding to this
* list of glyphs
*/
	public static DuffData[] convertGlyphs(List glyphs) {
		if (glyphs.size() == 0)
			return null;
		List data = new ArrayList();
		StringBuffer sb = new StringBuffer();
		Iterator iter = glyphs.iterator();
		DuffCode dc = (DuffCode)iter.next();
		int lastfont = dc.getFontNum();
		sb.append(dc.getCharacter());

		while (iter.hasNext()) {
			dc = (DuffCode)iter.next();
			if (dc.getFontNum() == lastfont)
				sb.append(dc.getCharacter());
			else {
				data.add(new DuffData(sb.toString(), lastfont));
				lastfont = dc.getFontNum();
				sb = new StringBuffer();
				sb.append(dc.getCharacter());
			}
		}

		data.add(new DuffData(sb.toString(), lastfont));

		DuffData[] dd = new DuffData[0];
		dd = (DuffData[])data.toArray(dd);
		return dd;
	}

/**
* Figures out how to arrange a list of characters into glyphs. For
* example, if the user types 'bsgr' using the Extended Wylie keyboard,
* this method figures out that this should be represented as a 'b'
* glyph followed by a 's-g-r' glyph. If you know that the characters
* do not contain Sanskrit stacks, or do not contain Tibetan stacks,
* then you can specify this to speed the process up. Otherwise, the
* method will first check to see if the characters correspond to any
* Tibetan stacks, and if not, then it will check for Sanskrit stacks.
* @param chars the list of Tibetan characters you want to find glyphs
* for
* @param areStacksOnRight whether stacking should try to maximize from
* right to left (true) or from left to right (false). In the Extended
* Wylie keyboard, you try to stack from right to left. Thus, the
* character sequence r-g-r would be stacked as r followed by gr,
* rather than rg followed by r. In the Sambhota and TCC keyboards, the
* stack direction is reversed.
* @param definitelyTibetan should be true if the characters are known
* to be Tibetan and not Sanskrit
* @param definitelySanskrit should be true if the characters are known
* to be Sanskrit and not Tibetan
*/
	public static List getGlyphs(List chars, boolean areStacksOnRight, boolean definitelyTibetan, boolean definitelySanskrit) {
		StringBuffer tibBuffer, sanBuffer;
		String tibCluster, sanCluster;

		boolean checkTibetan, checkSanskrit;

		if (!(definitelyTibetan || definitelySanskrit)) {
			checkTibetan = true;
			checkSanskrit = true;
		}
		else {
			checkTibetan = definitelyTibetan;
			checkSanskrit = definitelySanskrit;
		}

		int length = chars.size();

		List glyphs = new ArrayList();
		glyphs.clear();

		if (areStacksOnRight) {
			for (int i=0; i<length; i++) {
				tibBuffer = new StringBuffer();
				tibCluster = null;
		
				sanBuffer = new StringBuffer();
				sanCluster = null;

				for (int k=i; k<length; k++) {
					String s = (String)chars.get(k);

					if (checkTibetan)
						tibBuffer.append(s);

					if (checkSanskrit)
						sanBuffer.append(s);

					if (k!=length-1) {
						if (checkTibetan)
							tibBuffer.append("-");

						if (checkSanskrit)
							sanBuffer.append("+");
					}
				}

				if (checkTibetan) {
					tibCluster = tibBuffer.toString();

					if (TibetanMachineWeb.hasGlyph(tibCluster)) {
						Iterator iter = chars.iterator();
						for (int k=0; k<i; k++) //should really check here to make sure glyphs exist FIXME
							glyphs.add(TibetanMachineWeb.getGlyph((String)iter.next()));

						glyphs.add(TibetanMachineWeb.getGlyph(tibCluster));
						return glyphs;
					}
				}

				if (checkSanskrit) {
					sanCluster = sanBuffer.toString();

					if (TibetanMachineWeb.hasGlyph(sanCluster)) {
						Iterator iter = chars.iterator();
						for (int k=0; k<i; k++) //should really check here to make sure glyphs exist FIXME
							glyphs.add(TibetanMachineWeb.getGlyph((String)iter.next()));

						glyphs.add(TibetanMachineWeb.getGlyph(sanCluster));
						return glyphs;
					}
				}
			}
		}
		else {
			for (int i=length-1; i>-1; i--) {
				tibBuffer = new StringBuffer();
				tibCluster = null;

				sanBuffer = new StringBuffer();
				sanCluster = null;

				Iterator iter = chars.iterator();

				for (int k=0; k<i+1; k++) {
					String s = (String)iter.next();

					if (checkTibetan)
						tibBuffer.append(s);

					if (checkSanskrit)
						sanBuffer.append(s);

					if (k!=i) {
						if (checkTibetan)
							tibBuffer.append("-");

						if (checkSanskrit)
							sanBuffer.append("+");
					}
				}

				if (checkTibetan) {
					tibCluster = tibBuffer.toString();

					if (TibetanMachineWeb.hasGlyph(tibCluster)) {
						glyphs.add(TibetanMachineWeb.getGlyph(tibCluster));
						for (int k=i+1; k<length; k++)
							glyphs.add(TibetanMachineWeb.getGlyph((String)iter.next()));

						return glyphs;
					}
				}

				if (checkSanskrit) {
					sanCluster = sanBuffer.toString();

					if (TibetanMachineWeb.hasGlyph(sanCluster)) {
						glyphs.add(TibetanMachineWeb.getGlyph(sanCluster));
						for (int k=i+1; k<length; k++)
							glyphs.add(TibetanMachineWeb.getGlyph((String)iter.next()));

						return glyphs;
					}
				}
			}
		}

		return null;
	}

/**
* Finds the first meaningful element to occur within a string of
* Extended Wylie.  This could be a character, a vowel, punctuation, or
* formatting. For example, passed the string 'tshapo', this method
* will return 'tsh'.
* @param wylie the String of wylie you want to scan
* @return the next meaningful subpart of this string, or null if
* no meaningful subpart can be found (for example 'x' has no equivalent
* in Extended Wylie)
*/
	public static String getNext(String wylie) {
		boolean hasThereBeenValidity = false;
		boolean isThereValidity = false;
	
		String s;
		int i;
		int offset = 0;

		char c = wylie.charAt(offset);
		int k = (int)c;

		if (k < 32) //return null if character is just formatting
			return String.valueOf(c);

		if (c == WYLIE_DISAMBIGUATING_KEY)
			return String.valueOf(WYLIE_DISAMBIGUATING_KEY);
	
		if (c == WYLIE_SANSKRIT_STACKING_KEY)
			return String.valueOf(WYLIE_SANSKRIT_STACKING_KEY);

		for (i=offset+1; i<wylie.length()+1; i++) {
			s = wylie.substring(offset, i);

			if (!isThereValidity) {
				if (TibetanMachineWeb.isWyliePunc(s) || TibetanMachineWeb.isWylieVowel(s) || TibetanMachineWeb.isWylieChar(s)) {
					isThereValidity = true;
					hasThereBeenValidity = true;
				}
			}
			else {
				if (!TibetanMachineWeb.isWyliePunc(s) && !TibetanMachineWeb.isWylieVowel(s) && !TibetanMachineWeb.isWylieChar(s)) {
					isThereValidity = false;
					break;
				}
			}
		}

		if (!hasThereBeenValidity)
			s = null;

		else {
			if (isThereValidity) //the whole text region is valid
				s = wylie.substring(offset, wylie.length());

			else //the loop was broken out of
				s = wylie.substring(offset, i-1);
		}

		return s;
	}

/**
* Converts a string of Extended Wylie into {@link DuffData DuffData}.
* @param wylie the Wylie you want to convert
* @return an array of TibetanMachineWeb data
* corresponding to the Wylie text
* @throws InvalidWylieException if the Wylie is deemed invalid,
* i.e. if it does not conform to the Extended Wylie standard
*/
	public static DuffData[] getTibetanMachineWeb(String wylie) throws InvalidWylieException {
        List chars = new ArrayList();
		DuffCode dc;
		int start = 0;
		boolean isSanskrit = false;
		boolean wasLastSanskritStackingKey = false;
		LinkedList glyphs = new LinkedList();

		while (start < wylie.length()) {
			String next = getNext(wylie.substring(start));

			if (next == null) {
				if (!chars.isEmpty()) {
					glyphs.addAll(getGlyphs(chars, true, !isSanskrit, isSanskrit));
					chars.clear();
					isSanskrit = false;
				}
				else { //could not convert - throw exception
					if (start+5 < wylie.length())
						System.out.println("Bad wylie: "+wylie.substring(start,5)); // FIXME: we're printing to stdout!
					else
						System.out.println("Bad wylie: "+wylie.substring(start));
					throw new InvalidWylieException(wylie, start);
				}
			}

			else if (TibetanMachineWeb.isWyliePunc(next)) {
				if (!chars.isEmpty())
					glyphs.addAll(getGlyphs(chars, true, !isSanskrit, isSanskrit));

				chars.clear();

				if (next.equals(String.valueOf(BINDU))) {
					if (glyphs.isEmpty())
						dc = null;
					else 
						dc = (DuffCode)glyphs.removeLast(); //LinkedList implementation

					glyphs.addAll(getBindu(dc));
				}					

				else {
					dc = TibetanMachineWeb.getGlyph(next);
					glyphs.add(dc);
				}

				isSanskrit = false;
			}

			else if (TibetanMachineWeb.isWylieVowel(next)) {
				if (!chars.isEmpty()) {
					glyphs.addAll(getGlyphs(chars, true, !isSanskrit, isSanskrit));
					dc = (DuffCode)glyphs.removeLast(); //LinkedList implementation
					glyphs.addAll(getVowel(dc, next));
					chars.clear();
				}
				else { //if previous is punctuation or null, then achen plus vowel - otherwise, previous could be vowel
					int size = glyphs.size();

					vowel_block: {
						if (size > 1) {
							dc = (DuffCode)glyphs.get(glyphs.size()-1);
							if (!TibetanMachineWeb.isWyliePunc(TibetanMachineWeb.getWylieForGlyph(dc))) {
								DuffCode dc_2 = (DuffCode)glyphs.removeLast();
								DuffCode dc_1 = (DuffCode)glyphs.removeLast();
								glyphs.addAll(getVowel(dc_1, dc_2, next));
								break vowel_block;
							}
						}
						DuffCode[] dc_array = (DuffCode[])TibetanMachineWeb.getTibHash().get(ACHEN);
						dc = dc_array[TibetanMachineWeb.TMW];
						glyphs.addAll(getVowel(dc, next));
					}

					chars.clear();
				}

				isSanskrit = false;
			}

			else if (TibetanMachineWeb.isWylieChar(next)) {
				if (!isSanskrit) //add char to list - it is not sanskrit
					chars.add(next);

				else if (wasLastSanskritStackingKey) { //add char to list - it is still part of sanskrit stack
					chars.add(next);
					wasLastSanskritStackingKey = false;
				}

				else { //char is no longer part of sanskrit stack, therefore compute and add previous stack
					glyphs.addAll(getGlyphs(chars, true, !isSanskrit, isSanskrit));
					chars.clear();
					chars.add(next);
					isSanskrit = false;
					wasLastSanskritStackingKey = false;
				}
			}

			else if (next.equals(String.valueOf(WYLIE_DISAMBIGUATING_KEY))) {
				if (!chars.isEmpty())
					glyphs.addAll(getGlyphs(chars, true, !isSanskrit, isSanskrit));

				chars.clear();
				isSanskrit = false;
			}

			else if (next.equals(String.valueOf(WYLIE_SANSKRIT_STACKING_KEY))) {
				if (!isSanskrit) { //begin sanskrit stack
					switch (chars.size()) {
						case 0:
							break; //'+' is not "pre-stacking" key

						case 1:
							isSanskrit = true;
							wasLastSanskritStackingKey = true;
							break;

						default:
							String top_char = (String)chars.get(chars.size()-1);
							chars.remove(chars.size()-1);
							glyphs.addAll(getGlyphs(chars, true, !isSanskrit, isSanskrit));
							chars.clear();
							chars.add(top_char);
							isSanskrit = true;
							wasLastSanskritStackingKey = true;
							break;
					}
				}
			}

			else if (TibetanMachineWeb.isFormatting(next.charAt(0))) {
				if (!chars.isEmpty())
					glyphs.addAll(getGlyphs(chars, true, !isSanskrit, isSanskrit));

				dc = new DuffCode(1,next.charAt(0));
				glyphs.add(dc);
				chars.clear();
				isSanskrit = false;
			}

			if (next != null)
				start += next.length();
		}

		if (!chars.isEmpty()) {
			glyphs.addAll(getGlyphs(chars, true, !isSanskrit, isSanskrit));
			chars.clear();
		}

		DuffData[] dd = convertGlyphs(glyphs);
		return dd;
	}

/**
* Gets the bindu sequence for a given context.
* In the TibetanMachineWeb fonts, bindu (anusvara) is realized
* differently depending on which vowel it attaches to. Although
* the default bindu glyph is affixed to consonants and subscript vowels,
* for superscript vowels (i, e, o, etc), there is a single glyph
* which merges the bindu and that vowel together. When you pass this
* method a glyph context, it will return a List of glyphs which
* will either consist of the original glyph followed by the default
* bindu glyph, or a composite vowel+bindu glyph.
* Note that there is only one glyph in the context. This means that
* bindus will not affix properly if superscript vowels are allowed to directly
* precede subscript vowels (e.g. pou).
* @param dc the DuffCode of the glyph you
* want to attach a bindu to
* @return a List of DuffCode glyphs that include the
* original dc, as well as a bindu
*/
	public static List getBindu(DuffCode dc) {
		List bindus = new ArrayList();

		if (null == dc) {
			bindus.add(TibetanMachineWeb.getGlyph(String.valueOf(BINDU)));
			return bindus;
		}

		if (!TibetanMachineWeb.getBinduMap().containsKey(dc)) {
			bindus.add(dc);
			bindus.add(TibetanMachineWeb.getGlyph(String.valueOf(BINDU)));
			return bindus;
		}

		bindus.add((DuffCode)TibetanMachineWeb.getBinduMap().get(dc));
		return bindus;
	}

/**
* Gets the vowel sequence for a given vowel in a given context.
* Given a context, this method affixes a vowel and returns the
* context plus the vowel. Generally, it is enough to provide just
* one glyph for context.
* @param context the glyph preceding the vowel you want to affix
* @param vowel the vowel you want to affix, in Wylie
* @return a List of glyphs equal to the vowel in context
*/
	public static List getVowel(DuffCode context, String vowel) {
		return getVowel(null, context, vowel);
	}

/**
* Gets the vowel sequence for a given vowel in a given context.
* Given a context, this method affixes a vowel and returns the context plus the vowel.
* Since the choice of vowel glyph depends on the consonant to which it is attached,
* generally it is enough to provide just the immediately preceding context. However,
* in some cases, double vowels are allowed - for example 'buo'. To find the correct
* glyph for 'o', we need 'b' in this case, not 'u'. Note also that some Extended
* Wylie vowels correspond to multiple glyphs in TibetanMachineWeb. For example,
* the vowel I consists of both an achung and a reverse gigu. All required glyphs
* are part of the returned List.
* @param context_1 the glyph occurring two glyphs before the vowel you want to affix
* @param context_2 the glyph immediately before the vowel you want to affix
* @param vowel the vowel you want to affix, in Wylie
* @return a List of glyphs equal to the vowel in context
*/

	public static List getVowel(DuffCode context_1, DuffCode context_2, String vowel) {
		List vowels = new ArrayList();

//this vowel doesn't correspond to a glyph -
//so you just return the original context

		if (	vowel.equals(WYLIE_aVOWEL) ||
			TibetanMachineWeb.isTopVowel(context_2)) {
			if (context_1 != null)
				vowels.add(context_1);

			vowels.add(context_2);
			return vowels;
		}

//first, the three easiest cases: ai, au, and <i
//these vowels have one invariant form - therefore,
//dc_context is just returned along with that form

		if (vowel.equals(ai_VOWEL)) {
			if (context_1 != null)
				vowels.add(context_1);

			vowels.add(context_2);
			DuffCode[] dc_v = (DuffCode[])TibetanMachineWeb.getTibHash().get(ai_VOWEL);
			vowels.add(dc_v[TibetanMachineWeb.TMW]);
			return vowels;
		}

		if (vowel.equals(au_VOWEL)) {
			if (context_1 != null)
				vowels.add(context_1);

			vowels.add(context_2);
			DuffCode[] dc_v = (DuffCode[])TibetanMachineWeb.getTibHash().get(au_VOWEL);
			vowels.add(dc_v[TibetanMachineWeb.TMW]);
			return vowels;
		}

		if (vowel.equals(reverse_i_VOWEL)) {
			if (context_1 != null)
				vowels.add(context_1);

			vowels.add(context_2);

			if (!TibetanMachineWeb.isTopVowel(context_2)) {
				DuffCode[] dc_v = (DuffCode[])TibetanMachineWeb.getTibHash().get(reverse_i_VOWEL);
				vowels.add(dc_v[TibetanMachineWeb.TMW]);
			}

			return vowels;
		}

//second, the vowels i, e, and o
//these vowels have many different glyphs each,
//whose correct selection depends on the
//preceding context. therefore, dc_context is
//returned along with the vowel appropriate to
//that context

		if (vowel.equals(i_VOWEL)) {
			String hashKey_context = TibetanMachineWeb.getHashKeyForGlyph(context_2);
			DuffCode dc_v = TibetanMachineWeb.getVowel(hashKey_context, TibetanMachineWeb.VOWEL_i);
			if (null == dc_v && null != context_1) {
				hashKey_context = TibetanMachineWeb.getHashKeyForGlyph(context_1);
				dc_v = TibetanMachineWeb.getVowel(hashKey_context, TibetanMachineWeb.VOWEL_i);
			}

			if (context_1 != null)
				vowels.add(context_1);

			vowels.add(context_2);

			if (null != dc_v)
				vowels.add(dc_v);

			return vowels;
		}

		if (vowel.equals(e_VOWEL)) {
			String hashKey_context = TibetanMachineWeb.getHashKeyForGlyph(context_2);
			DuffCode dc_v = TibetanMachineWeb.getVowel(hashKey_context, TibetanMachineWeb.VOWEL_e);
			if (null == dc_v && null != context_1) {
				hashKey_context = TibetanMachineWeb.getHashKeyForGlyph(context_1);
				dc_v = TibetanMachineWeb.getVowel(hashKey_context, TibetanMachineWeb.VOWEL_e);
			}

			if (context_1 != null)
				vowels.add(context_1);

			vowels.add(context_2);

			if (null != dc_v)
				vowels.add(dc_v);

			return vowels;
		}

		if (vowel.equals(o_VOWEL)) {
			String hashKey_context = TibetanMachineWeb.getHashKeyForGlyph(context_2);
			DuffCode dc_v = TibetanMachineWeb.getVowel(hashKey_context, TibetanMachineWeb.VOWEL_o);
			if (null == dc_v && null != context_1) {
				hashKey_context = TibetanMachineWeb.getHashKeyForGlyph(context_1);
				dc_v = TibetanMachineWeb.getVowel(hashKey_context, TibetanMachineWeb.VOWEL_o);
			}

			if (context_1 != null)
				vowels.add(context_1);

			vowels.add(context_2);

			if (null != dc_v)
				vowels.add(dc_v);

			return vowels;
		}

//next come the vowels u, A, and U
//these three vowels are grouped together because they all
//can cause the preceding context to change. in particular,
//both u and A cannot be affixed to ordinary k or g, but
//rather the shortened versions of k and g - therefore,

		if (vowel.equals(u_VOWEL)) {
			String hashKey_context = TibetanMachineWeb.getHashKeyForGlyph(context_2);
			DuffCode halfHeight = TibetanMachineWeb.getHalfHeightGlyph(hashKey_context);
			DuffCode dc_v = TibetanMachineWeb.getVowel(hashKey_context, TibetanMachineWeb.VOWEL_u);

			if (null != context_1)
				vowels.add(context_1);

			if (null == halfHeight)
				vowels.add(context_2);
			else
				vowels.add(halfHeight);

			if (null != dc_v)
				vowels.add(dc_v);

			return vowels;
		}

		if (vowel.equals(A_VOWEL)) {
			String hashKey_context = TibetanMachineWeb.getHashKeyForGlyph(context_2);
			DuffCode halfHeight = TibetanMachineWeb.getHalfHeightGlyph(hashKey_context);
			DuffCode dc_v = TibetanMachineWeb.getVowel(hashKey_context, TibetanMachineWeb.VOWEL_A);

			if (null != context_1)
				vowels.add(context_1);

			if (null == halfHeight)
				vowels.add(context_2);
			else
				vowels.add(halfHeight);

			if (null != dc_v)

				vowels.add(dc_v);

			return vowels;
		}

		if (vowel.equals(U_VOWEL)) {
			String hashKey_context = TibetanMachineWeb.getHashKeyForGlyph(context_2);
			DuffCode halfHeight = TibetanMachineWeb.getHalfHeightGlyph(hashKey_context);
			DuffCode dc_v = TibetanMachineWeb.getVowel(hashKey_context, TibetanMachineWeb.VOWEL_U);

			if (null != context_1)
				vowels.add(context_1);

			if (null == halfHeight)
				vowels.add(context_2);
			else
				vowels.add(halfHeight);

			if (null != dc_v && !TibetanMachineWeb.isTopVowel(context_2))
				vowels.add(dc_v);

			return vowels;
		}

//finally, the vowels I and <I
//these vowels are unique in that they both
//require a change from the previous character,
//and consist of two glyphs themselves

		if (vowel.equals(I_VOWEL)) {
			String hashKey_context = TibetanMachineWeb.getHashKeyForGlyph(context_2);
			DuffCode halfHeight = TibetanMachineWeb.getHalfHeightGlyph(hashKey_context);
			DuffCode dc_v_sub = TibetanMachineWeb.getVowel(hashKey_context, TibetanMachineWeb.VOWEL_A);
			DuffCode dc_v_sup = TibetanMachineWeb.getVowel(hashKey_context, TibetanMachineWeb.VOWEL_i);

			if (null != context_1)
				vowels.add(context_1);

			if (null == halfHeight)
				vowels.add(context_2);
			else
				vowels.add(halfHeight);

			if (null != dc_v_sub && null != dc_v_sup) {
				vowels.add(dc_v_sub);
				vowels.add(dc_v_sup);
			}

			return vowels;
		}

		if (vowel.equals(reverse_I_VOWEL)) {
			String hashKey_context = TibetanMachineWeb.getHashKeyForGlyph(context_2);
			DuffCode halfHeight = TibetanMachineWeb.getHalfHeightGlyph(hashKey_context);
			DuffCode dc_v_sub = TibetanMachineWeb.getVowel(hashKey_context, TibetanMachineWeb.VOWEL_A);
			DuffCode[] tv_array = (DuffCode[])TibetanMachineWeb.getTibHash().get(reverse_i_VOWEL);
			DuffCode dc_v_sup = tv_array[TibetanMachineWeb.TMW];

			if (null != context_1)
				vowels.add(context_1);

			if (null == halfHeight)
				vowels.add(context_2);
			else
				vowels.add(halfHeight);

			if (null != dc_v_sub && null != dc_v_sup) {
				vowels.add(dc_v_sub);
				vowels.add(dc_v_sup);
			}

			return vowels;
		}

		return null;
	}

    /**
     * True if you want TibetanMachineWeb-to-Extended-Wylie conversion
     * to produce Wylie that, if typed, will produce the same sequence
     * of TibetanMachineWeb glyphs.  Without it, converting the glyphs
     * you get from typing jskad, skaska, skaskaska, skaskaskaska,
     * etc. will not give you Wylie, that, if typed in again, will
     * produce the original glyphs.  Hence, if this is true, then you
     * get working, end-to-end Wylie for syntactically illegal
     * sequences of glyphs. */
    private static final boolean makeIllegalTibetanGoEndToEnd = true;


    /** Returns "a", unless wylie is already "a". */
    private static String aVowelToUseAfter(String wylie) {
        if (wylie.equals(ACHEN))
            return "";
        else
            return WYLIE_aVOWEL;
    }

    private static String unambiguousPostAVowelWylie(String wylie1,
                                                     String wylie2) {
        String disambiguator = "";
        // type "lard" vs. "lar.d", and you'll see the need for this
        // disambiguation of suffix and postsuffix.  sa doesn't take
        // any head letters, so only da needs to be considered.
        if (TibetanMachineWeb.isWylieTop(wylie1)
            && wylie2.equals(/* FIXME: hard-coded */ "d"))
            disambiguator
                = new String(new char[] { WYLIE_DISAMBIGUATING_KEY });
        return wylie1 + disambiguator + wylie2;
    }

/**
* Scans a list of glyphs and returns an Extended Wylie string with 'a' inserted.
* Passed a list of TibetanMachineWeb glyphs that constitute a partial
* syllable that is in need of an 'a' vowel, this method scans the list,
* figures out where to put the 'a' vowel, and then returns a string
* of Wylie corresponding to this sequence. This method is used
* heavily during TibetanMachineWeb to Extended Wylie conversion,
* since there is no glyph corresponding to the Extended Wylie 'a' vowel.
* @param glyphList a list of TibetanMachineWeb glyphs, i.e. {@link
* org.thdl.tib.text.DuffCode DuffCodes}.  Pass in an ArrayList if you
* care at all for speed.
* @return the Wylie string corresponding to this glyph list, with 'a' inserted.
*/
	public static String withA(java.util.List glyphList) {
		StringBuffer sb = new StringBuffer();
		int size = glyphList.size();
		String wylie;
		String lastWylie = "";

		switch (size) {
        case 0:
            return "";

        case 1: //only one glyph: 'a' goes after it
            wylie = TibetanMachineWeb.getWylieForGlyph((DuffCode)glyphList.get(0));
            sb.append(wylie);
            sb.append(aVowelToUseAfter(wylie));

            return sb.toString();

        case 2: //two glyphs: 'a' either goes after first or after both
            lastWylie = TibetanMachineWeb.getWylieForGlyph((DuffCode)glyphList.get(0));
            sb.append(lastWylie);
            wylie = TibetanMachineWeb.getWylieForGlyph((DuffCode)glyphList.get(1));
            if (TibetanMachineWeb.isWylieRight(wylie)) {
                sb.append(aVowelToUseAfter(lastWylie));
                sb.append(wylie);
            } else {
                /* handle illegal two-glyph combinations,
                 * e.g., skaska */
                if (makeIllegalTibetanGoEndToEnd
                    && !TibetanMachineWeb.isWylieLeft(lastWylie)) {
                    sb.append(aVowelToUseAfter(lastWylie));
                }

                if (TibetanMachineWeb.isAmbiguousWylie(lastWylie, wylie))
                    sb.append(WYLIE_DISAMBIGUATING_KEY);

                if (!wylie.equals(ACHEN)) {
                    sb.append(wylie);
                    sb.append(WYLIE_aVOWEL);
                } else {
                    sb.append(WYLIE_DISAMBIGUATING_KEY);
                    sb.append(wylie);
                }
            }
            return sb.toString();

        default:
            /* Three or more characters: 'a' goes before last two,
             * between last two, or in final position, unless we have
             * something like pa'am, in which case the vowel comes
             * before the first ACHEN. */

            /* First, allow for pa'am, and even pa'am'ang, and
             * even bskyars'am'ang.  'i, 'o, 'i, 'u, etc. will not
             * occur because this is a call to withA, so vowels
             * aren't in the glyphList.  We will look at the end
             * of the glyphList (and no, with an ArrayList, this
             * is not O(glyphList.size()), it is O(1)) and work
             * our way backward, building up tailEndWylie as we
             * go. */
            {
                StringBuffer tailEndWylie = null;
                int effectiveSize = size - 2;
                while (effectiveSize >= 0
                       && TibetanMachineWeb.getWylieForGlyph((DuffCode)glyphList.get(effectiveSize)).equals(ACHUNG)) {
                    if (null == tailEndWylie) tailEndWylie = new StringBuffer();
                    // prepend:
                    tailEndWylie.insert(0,
                                        ACHUNG
                                        + aVowelToUseAfter(ACHUNG)
                                        + TibetanMachineWeb.getWylieForGlyph((DuffCode)glyphList.get(effectiveSize + 1)));
                    effectiveSize -= 2;
                }
                if (null != tailEndWylie) {
                    return (withA(glyphList.subList(0, effectiveSize + 2))
                            + tailEndWylie.toString());
                }
            }

            if (makeIllegalTibetanGoEndToEnd
                && (size > 4 // this is too many glyphs to be legal
                    // this is illegal because it doesn't begin
                    // with a prefix:
                    || (size == 4
                        && (!TibetanMachineWeb.isWylieLeft(TibetanMachineWeb.getWylieForGlyph((DuffCode)glyphList.get(0)))
                            // this is illegal because it doesn't have a
                            // suffix in the proper place, e.g. mjskad:
                            || !TibetanMachineWeb.isWylieRight(TibetanMachineWeb.getWylieForGlyph((DuffCode)glyphList.get(size - 2)))
                            // this is illegal because it doesn't have a
                            // postsuffix in the proper place,
                            // e.g. 'lan.g, which would otherwise become
                            // 'lang (with nga, not na and then ga):
                            || !TibetanMachineWeb.isWylieFarRight(TibetanMachineWeb.getWylieForGlyph((DuffCode)glyphList.get(size - 1))))))) {
                for (int i = 0; i < size; i++) {
                    wylie = TibetanMachineWeb.getWylieForGlyph((DuffCode)glyphList.get(i));
                    if (TibetanMachineWeb.isAmbiguousWylie(lastWylie, wylie)
                        || (i != 0 && wylie.equals(ACHEN)))
                        sb.append(WYLIE_DISAMBIGUATING_KEY);

                    sb.append(wylie + aVowelToUseAfter(wylie));
                    lastWylie = wylie;
                }
                return sb.toString();
            }

            /* Else, chew up all the glyphs except for the last two.  Then decide. */
            int i = 0;
            while (i+2 < size) {
                wylie = TibetanMachineWeb.getWylieForGlyph((DuffCode)glyphList.get(i));
                if (TibetanMachineWeb.isAmbiguousWylie(lastWylie, wylie)
                    || (i != 0 && wylie.equals(ACHEN)))
                    sb.append(WYLIE_DISAMBIGUATING_KEY);

                sb.append(wylie);
                lastWylie = wylie;
                i++;
            }

            String wylie1
                = TibetanMachineWeb.getWylieForGlyph((DuffCode)glyphList.get(i));
            String wylie2
                = TibetanMachineWeb.getWylieForGlyph((DuffCode)glyphList.get(i + 1));

            if (size == 3) {
                String wylie0 = lastWylie;
                // Let's see if wylie0+wylie1+wylie2 is ambiguous
                // -- if wylie0 could be a prefix and if wylie1
                // could be a suffix, and if wylie2 is "s".  If
                // it's ambigous, let's look up
                // wylie0+wylie1+wylie2 in our magic table.
                // Otherwise, see if we have a prefix, and if we
                // do, the "a" vowel comes after wylie1.  Else the
                // "a" vowel comes after wylie0.
                if (TibetanMachineWeb.isWylieLeft(wylie0)) {
                    /* is it ambiguous? */
                    if (TibetanMachineWeb.isWylieRight(wylie1)
                        && SA.equals(wylie2) /* isWylieFarRight would
                                              * work, but the list of
                                              * 9 words doesn't have
                                              * any ending with d --
                                              * all end with s. */) {
                        /* Yes, this is ambiguous. How do we handle
                         * it?  See this from Andres:
                         *
                         * I'm posting this upon David Chandler's
                         * request. According to Lobsang Thonden in
                         * Modern Tibetan Grammar Language (page 42),
                         * with regards to identifying the root letter
                         * in 3 lettered words there are only 23
                         * ambiguous cases. He writes:
                         *
                         * If the last letter is 'sa' and the first
                         * two letters are affixes, then the SECOND
                         * ONE is the root letter in the following 9
                         * WORDS ONLY:
                         *
                         * gdas gnas gsas dgas dmas bdas mdas 'gas
                         * 'das
                         *
                         * And the FIRST is the root letter in the
                         * following 14 WORDS ONLY:
                         *
                         * rags lags nags bags bangs gangs rangs langs
                         * nangs sangs babs rabs rams nams
                         *
                         * As I mentioned before, I think that the
                         * best solution for now is to hard-wire these
                         * cases. Even if the list is not exhaustive,
                         * at least we'll have most cases covered.
                         */

                        /* FIXME: these constants are hard-wired here,
                         * rather than in TibetanMachineWeb, because
                         * I'm lazy. */
                        if ((wylie0.equals("g") && (wylie1.equals("d") || wylie1.equals("n") || wylie1.equals("s")))
                            || (wylie0.equals("d") && (wylie1.equals("g") || wylie1.equals("m")))
                            || (wylie0.equals("b") && wylie1.equals("d"))
                            || (wylie0.equals("m") && wylie1.equals("d"))
                            || (wylie0.equals("'") && (wylie1.equals("g") || wylie1.equals("d")))) {
                            sb.append(wylie1
                                      + aVowelToUseAfter(wylie1)
                                      + wylie2);
                        } else {
                            sb.append(aVowelToUseAfter(wylie0)
                                      + unambiguousPostAVowelWylie(wylie1,
                                                                   wylie2));
                        }

                    } else {
                        /* no ambiguity. the "a" vowel comes after
                         * wylie1. */
                        if (TibetanMachineWeb.isAmbiguousWylie(wylie0, wylie1))
                            sb.append(WYLIE_DISAMBIGUATING_KEY);
                        sb.append(wylie1
                                  + aVowelToUseAfter(wylie1)
                                  + wylie2);
                    }
                } else {
                    if (makeIllegalTibetanGoEndToEnd
                        && !(TibetanMachineWeb.isWylieRight(wylie1)
                             && TibetanMachineWeb.isWylieFarRight(wylie2))) {
                        /* handle skaskaska, e.g. */
                        sb.append(aVowelToUseAfter(wylie0)
                                  + wylie1
                                  + aVowelToUseAfter(wylie1)
                                  + wylie2
                                  + aVowelToUseAfter(wylie2));
                    } else {
                        /* no ambiguity. the "a" vowel comes after
                         * wylie0. */
                        sb.append(aVowelToUseAfter(wylie0)
                                  + unambiguousPostAVowelWylie(wylie1,
                                                               wylie2));
                    }
                }
            } else {
                /* If size==4, then we assume this is legal.  If
                 * size==5, anything will do!  So assume we have a
                 * prefix, a root letter, a suffix, and a postsuffix.
                 * The "a" vowel comes after the root letter. */
                sb.append(aVowelToUseAfter(lastWylie)
                          + unambiguousPostAVowelWylie(wylie1,
                                                       wylie2));
            }
            return sb.toString();
		}
	}

/**
* Gets the Extended Wylie for a list of glyphs.  Passed a list of
* TibetanMachineWeb glyphs that constitute a partial or complete
* syllable, this method scans the list, and then returns a string of
* Wylie corresponding to this sequence. No 'a' vowel is inserted
* because it is assumed that the glyph list already contains some
* other vowel. If the glyph list does not already contain a vowel,
* then this method should not be called.
*
* @param glyphList a list of TibetanMachineWeb glyphs, i.e. {@link
* org.thdl.tib.text.DuffCode DuffCodes}
* @param isBeforeVowel true if these glyphs occur before a vowel,
* false if these glyphs occur after a vowel
* @return the Wylie string corresponding to this glyph list
*/
	public static String withoutA(java.util.ArrayList glyphList, boolean isBeforeVowel) {
		StringBuffer sb = new StringBuffer();
		Iterator iter = glyphList.iterator();
		DuffCode dc;
		String currWylie;
		String lastWylie = new String();

		while (iter.hasNext()) {
			dc = (DuffCode)iter.next();
			currWylie = TibetanMachineWeb.getWylieForGlyph(dc);

			if (TibetanMachineWeb.isAmbiguousWylie(lastWylie, currWylie)
				|| (!lastWylie.equals("")
                    && currWylie.equals(ACHEN)))
                sb.append(WYLIE_DISAMBIGUATING_KEY);

            /* le'ang, not le'ng, to be consistent w.r.t. pa'am
             * vs. pa'm: */
            if (lastWylie.equals(ACHUNG) && !isBeforeVowel)
                sb.append(WYLIE_aVOWEL);

			sb.append(currWylie);

			lastWylie = currWylie;
		}

        // DLC FIXME: type jeskada, convert Tibetan->Wylie.  You get
        // the wrong thing in makeIllegalTibetanGoEndToEnd mode.  Fix
        // it here.
		return sb.toString();
	}

/**
* Gets the Extended Wylie for a sequence of glyphs.
* @param dcs an array of glyphs
* @return the Extended Wylie corresponding to these glyphs
*/
	public static String getWylie(DuffCode[] dcs) {
		if (dcs.length == 0)
			return null;

		char ch;
		String wylie;

		ArrayList glyphList = new ArrayList();
		boolean needsVowel = true;
		boolean isLastVowel = false;
		int start = 0;
		StringBuffer wylieBuffer = new StringBuffer();

        for (int i=start; i<dcs.length; i++) {
            ch = dcs[i].getCharacter();
            int k = dcs[i].getCharNum();
            // int fontNum = dcs[i].getFontNum();

            if (k < 32) {
                if (wylieBuffer.length() > 0 || !glyphList.isEmpty()) {
                    String thisPart;
                    if (needsVowel)
                        thisPart = withA(glyphList);
                    else
                        thisPart = withoutA(glyphList, false);
                    wylieBuffer.append(thisPart);

                    glyphList.clear();
                    needsVowel = true;
                    isLastVowel = false;
                }

                wylieBuffer.append(ch);
            } else {
                wylie = TibetanMachineWeb.getWylieForGlyph(dcs[i]);

                boolean containsBindu = false;
                if (wylie.length() > 1 && wylie.charAt(wylie.length()-1) == BINDU) {
                    char[] cArray = wylie.toCharArray();
                    wylie = new String(cArray, 0, wylie.length()-1);
                    containsBindu = true;
                }

                process_block: {
                    if (TibetanMachineWeb.isWyliePunc(wylie)) {
                        isLastVowel = false;

                        if (glyphList.isEmpty()) {
                            wylieBuffer.append(wylie);
                        } else {
                            String thisPart;
                            if (needsVowel)
                                thisPart = withA(glyphList);
                            else
                                thisPart = withoutA(glyphList, false);
                            wylieBuffer.append(thisPart);

                            wylieBuffer.append(wylie); //append the punctuation

                            glyphList.clear();
                        }
                        needsVowel = true; //next consonants are syllable onset, so we are awaiting vowel
                    } else if (TibetanMachineWeb.isWylieChar(wylie)) {
						//isChar must come before isVowel because ACHEN has priority over WYLIE_aVOWEL
                        isLastVowel = false;
                        glyphList.add(dcs[i]);
                    } else if (TibetanMachineWeb.isWylieVowel(wylie)) {
                        if (isLastVowel) {
                            int len = wylieBuffer.length();
                            int A_len = A_VOWEL.length();

                            if (wylieBuffer.substring(len-A_len).equals(A_VOWEL)) {
                                try {
                                    if (wylie.equals(i_VOWEL)) {
                                        wylieBuffer.delete(len-A_len, len);
                                        wylieBuffer.append(I_VOWEL);
                                        isLastVowel = false;
                                        break process_block;
                                    } else if (wylie.equals(reverse_i_VOWEL)) {
                                        wylieBuffer.delete(len-A_len, len);
                                        wylieBuffer.append(reverse_I_VOWEL);
                                        isLastVowel = false;
                                        break process_block;
                                    }
                                }
                                catch (StringIndexOutOfBoundsException se) {
                                    ThdlDebug.noteIffyCode();
                                }

                                wylieBuffer.append(wylie); //append current vowel
                                isLastVowel = false;
                            } else
                                wylieBuffer.append(wylie); //append current vowel
                        } else {
                            int glyphCount = glyphList.size();
                            boolean insertDisAmbig = false;

                            if (0 != glyphCount) {
                                DuffCode top_dc = (DuffCode)glyphList.get(glyphCount-1);
                                String top_wylie = TibetanMachineWeb.getWylieForGlyph(top_dc);

                                if (top_wylie.equals(ACHEN)) {
                                    glyphList.remove(glyphCount-1);
										
                                    if (glyphCount-1 == 0) {
                                        top_dc = null;
                                    } else {
                                        insertDisAmbig = true;
                                        top_dc = (DuffCode)glyphList.get(glyphCount-2);
                                    }
                                }

                                if (top_dc == null || !TibetanMachineWeb.getWylieForGlyph(top_dc).equals(ACHUNG)) {
                                    String thisPart = withoutA(glyphList, true);
                                    wylieBuffer.append(thisPart); //append consonants in glyphList
                                } else {
                                    glyphCount = glyphList.size();
                                    glyphList.remove(glyphCount-1);
										
                                    if (glyphCount-1 != 0) {
                                        String thisPart = withA(glyphList);
                                        wylieBuffer.append(thisPart);
                                    }

                                    wylieBuffer.append(ACHUNG);
                                }
                            }

                            if (insertDisAmbig)
                                wylieBuffer.append(WYLIE_DISAMBIGUATING_KEY);

                            wylieBuffer.append(wylie); //append vowel

                            glyphList.clear();
                            isLastVowel = true;
                            needsVowel = false;
                        }
                    } else { //must be a stack
                        isLastVowel = false;
                        glyphList.add(dcs[i]);
                    }
                }

                if (containsBindu) {
                    isLastVowel = false;
                    wylieBuffer.append(withoutA(glyphList, false));
                    wylieBuffer.append(BINDU); //append the bindu
                    glyphList.clear();
                }
            }
        }

        //replace TMW with Wylie

        if (!glyphList.isEmpty()) {
            String thisPart;
            if (needsVowel)
                thisPart = withA(glyphList);
            else
                thisPart = withoutA(glyphList, false);
            wylieBuffer.append(thisPart);
        }

        if (wylieBuffer.length() > 0)
            return wylieBuffer.toString();
        else
            return null;
	}
}
