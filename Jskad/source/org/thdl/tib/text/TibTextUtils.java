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
    /** Change to true to see various things on System.out and
        System.err. */
    private static final boolean debug = false;

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

    /** An array containing one boolean value.  Pass this to
        TibetanMachineWeb.getWylieForGlyph(..) if you don't care if a
        certain glyph has corresponding Wylie or not. */
    public static final boolean[] weDoNotCareIfThereIsCorrespondingWylieOrNot
        = new boolean[] { false };

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
                    // FIXME: we're printing to stdout!
					if (start+5 <= wylie.length()) {
						System.out.println("Bad wylie: "
                                           + wylie.substring(start,
                                                             start + 5));
                    } else {
						System.out.println("Bad wylie: "+wylie.substring(start));
                    }
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
							if (!TibetanMachineWeb.isWyliePunc(TibetanMachineWeb.getWylieForGlyph(dc, weDoNotCareIfThereIsCorrespondingWylieOrNot))) {
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
            disambiguator = WYLIE_DISAMBIGUATING_KEY_STRING;
        return wylie1 + disambiguator + wylie2;
    }

/**
* Gets the Extended Wylie for a sequence of glyphs.
* @param dcs an array of glyphs
* @param noSuchWylie an array which will not be touched if this is
* successful; however, if there is no THDL Extended Wylie
* corresponding to these glyphs, then noSuchWylie[0] will be set to
* true
* @return the Extended Wylie corresponding to these glyphs, or null */
    public static String getWylie(DuffCode[] dcs, boolean noSuchWylie[]) {
        StringBuffer warnings = (debug ? new StringBuffer() : null);
        String ans = getWylieImplementation(dcs, noSuchWylie, warnings);
        if (debug && warnings.length() > 0)
            System.out.println("DEBUG: warnings in TMW->Wylie: " + warnings);
        return ans;
    }

    /** True for and only for ma and nga because 'am and 'ang are
        appendages. */
    private static final boolean isAppendageNonVowelWylie(String wylie) {
        return (MA.equals(wylie) || NGA.equals(wylie));
    }

    /** Scans the glyphs in glyphList and creates the returned list of
        grapheme clusters based on them.  A grapheme cluster is a
        consonant or consonant stack with optional adornment or a
        number (possibly super- or subscribed) or some other glyph
        alone. */
    private static ArrayList breakTshegBarIntoGraphemeClusters(java.util.List glyphList,
                                                               boolean noSuchWylie[]) {

        // Definition: adornment means vowels and achungs and bindus.

        int sz = glyphList.size();
        ThdlDebug.verify(sz > 0);

        // A list of grapheme clusters (see UnicodeGraphemeCluster).
        // sz is an overestimate (speeds us up, wastes some memory).
        ArrayList gcs = new ArrayList(sz);

        StringBuffer buildingUpGc = new StringBuffer();

        boolean consonantal_with_vowel = false;
        boolean buildingUpSanskrit = false;
        for (int i = 0; i < sz; i++) {
            DuffCode dc = (DuffCode)glyphList.get(i);
            String wylie = TibetanMachineWeb.getWylieForGlyph(dc, noSuchWylie);
            boolean containsWylieVowel = false;
            boolean buildingUpSanskritNext = false;
            if ((buildingUpSanskritNext
                 = TibetanMachineWeb.isWylieSanskritConsonantStack(wylie))
                || TibetanMachineWeb.isWylieTibetanConsonantOrConsonantStack(wylie)) {
                if (buildingUpGc.length() > 0) {
                    gcs.add(new TGCPair(buildingUpGc.toString(),
                                        consonantal_with_vowel
                                        ? (buildingUpSanskrit
                                           ? TGCPair.SANSKRIT_WITH_VOWEL
                                           : TGCPair.CONSONANTAL_WITH_VOWEL)
                                        : (buildingUpSanskrit
                                           ? TGCPair.SANSKRIT_WITHOUT_VOWEL
                                           : TGCPair.CONSONANTAL_WITHOUT_VOWEL)));
                    buildingUpGc.delete(0, buildingUpGc.length());
                }
                buildingUpGc.append(wylie);
                consonantal_with_vowel = false;
                buildingUpSanskrit = buildingUpSanskritNext;
            } else if ((containsWylieVowel
                        = TibetanMachineWeb.isWylieAdornmentAndContainsVowel(wylie))
                       || TibetanMachineWeb.isWylieAdornment(wylie)) {

                if (buildingUpGc.length() > 0) {
                    buildingUpGc.append(wylie);
                    if (containsWylieVowel) {
                        if (debug)
                            System.out.println("DEBUG: with_vowel is true thanks to " + wylie);
                        consonantal_with_vowel = true;
                    }
                    // do not clear; we might have {cui} or {hUM}, e.g.
                } else {
                    gcs.add(new TGCPair(wylie,
                                        TGCPair.LONE_VOWEL));
                    consonantal_with_vowel = false;
                }
            } else {
                // number or weird thing:

                if (buildingUpGc.length() > 0) {
                    gcs.add(new TGCPair(buildingUpGc.toString(),
                                        consonantal_with_vowel
                                        ? (buildingUpSanskrit
                                           ? TGCPair.SANSKRIT_WITH_VOWEL
                                           : TGCPair.CONSONANTAL_WITH_VOWEL)
                                        : (buildingUpSanskrit
                                           ? TGCPair.SANSKRIT_WITHOUT_VOWEL
                                           : TGCPair.CONSONANTAL_WITHOUT_VOWEL)));
                    buildingUpGc.delete(0, buildingUpGc.length());
                }
                gcs.add(new TGCPair(wylie, TGCPair.OTHER));
                consonantal_with_vowel = false;
                buildingUpSanskrit = false;
            }
        }
        if (buildingUpGc.length() > 0) {
            gcs.add(new TGCPair(buildingUpGc.toString(),
                                consonantal_with_vowel
                                ? (buildingUpSanskrit
                                   ? TGCPair.SANSKRIT_WITH_VOWEL
                                   : TGCPair.CONSONANTAL_WITH_VOWEL)
                                : (buildingUpSanskrit
                                   ? TGCPair.SANSKRIT_WITHOUT_VOWEL
                                   : TGCPair.CONSONANTAL_WITHOUT_VOWEL)));
        }
        buildingUpGc = null;
        return gcs;
    }


    private static String getClassificationOfTshegBar(ArrayList gcs,
                                                      StringBuffer warnings) {
        String candidateType = null;
        // Now that we have grapheme clusters, see if they match any
        // of the "legal tsheg bars":
        int sz = gcs.size();
        for (int i = 0; i < sz; i++) {
            TGCPair tp = (TGCPair)gcs.get(i);
            int cls = tp.classification;
            String wylie = tp.wylie;
            if (TGCPair.OTHER == cls) {
                if (TibetanMachineWeb.isWylieNumber(wylie)) {
                    if (null == candidateType) {
                        candidateType = "number";
                    } else {
                        if ("number" != candidateType) {
                            if (null != warnings)
                                warnings.append("Found something odd; the wylie is " + wylie + "\n");
                            candidateType = "invalid";
                            break;
                        }
                    }
                } else {
                    if (null != warnings)
                        warnings.append("Found something odd; the wylie is " + wylie + "\n");
                    candidateType = "invalid";
                    break;
                }
            } else if (TGCPair.SANSKRIT_WITHOUT_VOWEL == cls
                       || TGCPair.SANSKRIT_WITH_VOWEL == cls) {
                candidateType = "invalid";
            } else if (TGCPair.CONSONANTAL_WITHOUT_VOWEL == cls
                       || TGCPair.CONSONANTAL_WITH_VOWEL == cls) {
                if (null == candidateType) {
                    if (TibetanMachineWeb.isWylieLeft(wylie)) {
                        candidateType = "prefix/root";
                    } else {
                        candidateType = "root";
                    }
                } else {
                    if ("prefix/root" == candidateType) {
                        if (ACHUNG.equals(wylie)) {
                            // peek ahead to distinguish between ba's,
                            // ba'ala and ba'am:
                            TGCPair nexttp = (i+1 < sz) ? (TGCPair)gcs.get(i+1) : null;
                            String nextwylie = (nexttp == null) ? "" : nexttp.wylie;
                            if (isAppendageNonVowelWylie(nextwylie)) {
                                candidateType = "maybe-appendaged-prefix/root";
                            } else {
                                candidateType = "prefix/root-root/suffix";
                            }
                        } else if (TibetanMachineWeb.isWylieRight(wylie)) {
                            candidateType = "prefix/root-root/suffix";
                        } else if (TibetanMachineWeb.isWylieAchungAppendage(wylie)) {
                            candidateType = "appendaged-prefix/root";
                        } else {
                            candidateType = "prefix-root";
                        }
                    } else if ("root" == candidateType) {
                        if (ACHUNG.equals(wylie)) {
                            // peek ahead to distinguish between pa's,
                            // pa'ala and pa'am:
                            TGCPair nexttp = (i+1 < sz) ? (TGCPair)gcs.get(i+1) : null;
                            String nextwylie = (nexttp == null) ? "" : nexttp.wylie;
                            if (isAppendageNonVowelWylie(nextwylie)) {
                                candidateType = "maybe-appendaged-root";
                            } else {
                                candidateType = "root-suffix";
                            }
                        } else if (TibetanMachineWeb.isWylieRight(wylie)) {
                            candidateType = "root-suffix";
                        } else if (TibetanMachineWeb.isWylieAchungAppendage(wylie)) {
                            candidateType = "appendaged-root";
                        } else {
                            if (null != warnings)
                                warnings.append("Found a non-prefix consonant or consonant stack followed by a consonant or consonant stack that is not simply a suffix; that thing's wylie is " + wylie + "\n");
                            candidateType = "invalid";
                            break;
                        }
                    } else if ("prefix-root" == candidateType) {
                        if (ACHUNG.equals(wylie)) {
                            // peek ahead to distinguish between bpa's,
                            // bpa'ala and bpa'am:
                            TGCPair nexttp = (i+1 < sz) ? (TGCPair)gcs.get(i+1) : null;
                            String nextwylie = (nexttp == null) ? "" : nexttp.wylie;
                            if (isAppendageNonVowelWylie(nextwylie)) {
                                candidateType = "maybe-appendaged-prefix-root";
                            } else {
                                candidateType = "prefix-root-suffix";
                            }
                        } else if (TibetanMachineWeb.isWylieRight(wylie)) {
                            candidateType = "prefix-root-suffix";
                        } else if (TibetanMachineWeb.isWylieAchungAppendage(wylie)) {
                            candidateType = "appendaged-prefix-root";
                        } else {
                            if (null != warnings)
                                warnings.append("Found a prefix plus a root stack plus a non-suffix consonant or consonant stack whose wylie is " + wylie + "\n");
                            candidateType = "invalid";
                            break;
                        }
                    } else if ("prefix/root-root/suffix" == candidateType) {
                        // this has no peekahead, gag'am works.
                        if (ACHUNG.equals(wylie)) {
                            // peek ahead to distinguish between
                            // gga'am and gaga'ala:
                            TGCPair nexttp = (i+1 < sz) ? (TGCPair)gcs.get(i+1) : null;
                            String nextwylie = (nexttp == null) ? "" : nexttp.wylie;
                            if (isAppendageNonVowelWylie(nextwylie)) {
                                candidateType = "maybe-appendaged-prefix/root-root/suffix";
                            } else {
                                candidateType = "prefix-root-suffix";
                            }
                        } else if (TibetanMachineWeb.isWylieFarRight(wylie)) {
                            candidateType = "prefix/root-root/suffix-suffix/postsuffix";
                        } else if (TibetanMachineWeb.isWylieRight(wylie)) {
                            candidateType = "prefix-root-suffix";
                        } else if (TibetanMachineWeb.isWylieAchungAppendage(wylie)) {
                            candidateType = "appendaged-prefix/root-root/suffix";
                        } else {
                            if (null != warnings)
                                warnings.append("Found a prefix/root stack plus a suffix/root stack plus a non-suffix, non-postsuffix consonant or consonant stack whose wylie is " + wylie + "\n");
                            candidateType = "invalid";
                            break;
                        }
                    } else if ("root-suffix" == candidateType) {
                        // This has no peekahead w.r.t. 'am and 'ang,
                        // but it needs none because we peeked to be
                        // sure that this was root-suffix and not
                        // maybe-appendaged-root.
                        if (TibetanMachineWeb.isWylieFarRight(wylie)) {
                            candidateType = "root-suffix-postsuffix";
                        } else if (TibetanMachineWeb.isWylieAchungAppendage(wylie)) {
                            candidateType = "appendaged-root-suffix";
                        } else if (ACHUNG.equals(wylie)) {
                            candidateType = "maybe-appendaged-root-suffix";
                        } else {
                            if (null != warnings)
                                warnings.append("Found a root stack plus a suffix plus a non-postsuffix consonant or consonant stack whose wylie is " + wylie + "\n");
                            candidateType = "invalid";
                            break;
                        }
                    } else if ("prefix/root-root/suffix-suffix/postsuffix" == candidateType
                               || "prefix-root-suffix" == candidateType) {
                        // this has no peekahead and needs none.
                        if (TibetanMachineWeb.isWylieFarRight(wylie)) {
                            candidateType = "prefix-root-suffix-postsuffix";
                        } else if (TibetanMachineWeb.isWylieAchungAppendage(wylie)) {
                            // if we simply prepended to
                            // candidateType, we wouldn't get interned
                            // strings.
                            candidateType = ("appendaged-" + candidateType).intern();
                        } else if (ACHUNG.equals(wylie)) {
                            candidateType = ("maybe-appendaged-" + candidateType).intern();
                        } else {
                            if (null != warnings)
                                warnings.append("Found a prefix/root stack plus a suffix/root stack plus a suffix/postsuffix plus a non-postsuffix consonant or consonant stack whose wylie is " + wylie + "\n");
                            candidateType = "invalid";
                            break;
                        }
                    } else if ("prefix-root-suffix-postsuffix" == candidateType) {
                        // this has no peekahead and needs none.
                        if (TibetanMachineWeb.isWylieAchungAppendage(wylie)) {
                            candidateType = "appendaged-prefix-root-suffix-postsuffix";
                        } else if (ACHUNG.equals(wylie)) {
                            candidateType = "maybe-appendaged-prefix-root-suffix-postsuffix";
                        } else {
                            if (null != warnings)
                                warnings.append("Found a prefix plus root stack plus suffix plus postsuffix; then found yet another consonant or consonant stack whose wylie is " + wylie + "\n");
                            candidateType = "invalid";
                            break;
                        }
                    } else if ("root-suffix-postsuffix" == candidateType) {
                        // this has no peekahead and needs none.
                        if (TibetanMachineWeb.isWylieAchungAppendage(wylie)) {
                            candidateType = "appendaged-root-suffix-postsuffix";
                        } else if (ACHUNG.equals(wylie)) {
                            candidateType = "maybe-appendaged-root-suffix-postsuffix";
                        } else {
                            if (null != warnings)
                                warnings.append("Found a root stack plus suffix plus postsuffix; then found yet another consonant or consonant stack whose wylie is " + wylie + "\n");
                            candidateType = "invalid";
                            break;
                        }
                    } else if (candidateType.startsWith("maybe-appendaged-")) {
                        if (isAppendageNonVowelWylie(wylie)) {
                            candidateType
                                = candidateType.substring("maybe-".length()).intern();
                            // So that we get 'am, not 'm; 'ang, not 'ng:
                            tp.wylie = WYLIE_aVOWEL + tp.wylie;
                        } else {
                            if (null != warnings)
                                warnings.append("Found a tsheg bar that has an achung (" + ACHUNG + ") tacked on, followed by some other thing whose wylie is " + wylie + "\n");
                            candidateType = "invalid";
                            break;
                        }
                    } else if (candidateType.startsWith("appendaged-")) {
                        if (TibetanMachineWeb.isWylieAchungAppendage(wylie)) {
                            // candidateType stays what it is.
                        } else if (ACHUNG.equals(wylie)) {
                            candidateType = ("maybe-" + candidateType).intern();
                        } else {
                            if (null != warnings)
                                warnings.append("Found a tsheg bar that has a 'i, 'e, 'o, 'u, or 'ang 'am appendage already and then found yet another consonant or consonant stack whose wylie is " + wylie + "\n");
                            candidateType = "invalid";
                            break;
                        }
                    } else {
                        if ("number" != candidateType)
                            throw new Error("missed a case");
                        if (null != warnings)
                            warnings.append("Found a consonant or consonant stack after something odd; the consonantish thing has wylie " + wylie + "\n");
                        candidateType = "invalid";
                        break;
                    }
                }
            } else if (TGCPair.LONE_VOWEL == cls) {
                if (null != warnings)
                    warnings.append("Found a vowel that did not follow either a Tibetan consonant or consonant stack or another vowel.");
                candidateType = "invalid";
                break;
            } else {
                throw new Error("bad cls");
            }
        }
        if (candidateType.startsWith("maybe-appendaged-")) {
            if (null != warnings)
                warnings.append("Found a tsheg bar that has an extra achung (" + ACHUNG + ") tacked on\n");
            candidateType = "invalid";
        }
        return candidateType;
    }

    /** Appends to wylieBuffer the wylie for the glyph list glyphList
        (which should be an ArrayList for speed).  This will be very
        user-friendly for "legal tsheg bars" and will be valid, but
        possibly ugly (interspersed with disambiguators or extra
        vowels, etc.) Wylie for other things, such as Sanskrit
        transliteration.  Updates warnings and noSuchWylie like the
        caller does.

        <p>What constitutes a legal, non-punctuation, non-whitespace
        tsheg bar?  The following are the only such:</p>
        <ul>
          <li>one or more numbers</li>

          <li>a single, possibly adorned consonant stack</li>

          <li>a legal "tyllable" appended with zero or more particles
              from the set { 'i, 'o, 'u, 'e, 'ang, 'am }</li>
        </ul>

        <p>A "tyllable" is, by definition, one of the following:</p>

        <ul>
          <li>a single, possibly adorned consonant stack</li>

          <li>two consonant stacks where one is a single,
              unadorned consonant (and is a prefix it it is first and
              a suffix if it is last) and the other is possibly
              adorned</li>

          <li>three consonant stacks where at most one has adornment.
              If the second has adornment, then the first must be an
              unadorned prefix consonant and the last must be an
              unadorned suffix consonant.  If the first has adornment,
              then the second must be an unadorned suffix consonant
              and the third must be an unadorned secondary suffix
              consonant.</li>

          <li>four consonant stacks where either none is adorned or
              only the second consonant stack is adorned, the first is
              an unadorned prefix consonant, the third is an unadorned
              suffix consonant, and the fourth is an unadorned
              secondary suffix consonant.</li>

        </ul>
        
        <p>When there are three unadorned consonant stacks in a
           tyllable, a hard-coded list of valid Tibetan tsheg bars is
           relied upon to determine if the 'a' vowel comes after the
           first or the second consonant.</p> */
    private static void getTshegBarWylie(java.util.List glyphList,
                                         boolean noSuchWylie[],
                                         StringBuffer warnings,
                                         StringBuffer wylieBuffer) {
        ArrayList gcs
            = breakTshegBarIntoGraphemeClusters(glyphList, noSuchWylie);
        String candidateType = getClassificationOfTshegBar(gcs, warnings);
        int sz = gcs.size();
        if (candidateType == "invalid") {
            // Forget beauty and succintness -- just be sure to
            // generate Wylie that can be converted unambiguously into
            // Tibetan.  Use a disambiguator or vowel after each
            // grapheme cluster.
            //
            // If we truly didn't care about beauty, we'd just lump
            // SANSKRIT_WITHOUT_VOWEL and SANSKRIT_WITH_VOWEL into
            // OTHER.

            for (int i = 0; i < sz; i++) {
                TGCPair tp = (TGCPair)gcs.get(i);
                int cls = tp.classification;
                String wylie = tp.wylie;
                wylieBuffer.append(wylie);
                if (TibetanMachineWeb.isWylieTibetanConsonantOrConsonantStack(wylie)
                    || TibetanMachineWeb.isWylieSanskritConsonantStack(wylie)) {
                    wylieBuffer.append(aVowelToUseAfter(wylie));
                } else {
                    if (TGCPair.CONSONANTAL_WITH_VOWEL != cls
                        && TGCPair.SANSKRIT_WITH_VOWEL != cls)
                        wylieBuffer.append(WYLIE_DISAMBIGUATING_KEY);
                }
            }
        } else {
            // Generate perfect, beautiful, Wylie, using the minimum
            // number of vowels and disambiguators.

            int leftover = sz + 1;

            // Appendaged vs. not appendaged?  it affects nothing at
            // this stage.
            if (candidateType.startsWith("appendaged-")) {
                candidateType
                    = candidateType.substring("appendaged-".length()).intern();
            }

            if ("prefix/root-root/suffix-suffix/postsuffix" == candidateType) {
                /* Yes, this is ambiguous. How do we handle it?  See
                 * this from Andres:
                 *
                 * I'm posting this upon David Chandler's
                 * request. According to Lobsang Thonden in Modern
                 * Tibetan Grammar Language (page 42), with regards to
                 * identifying the root letter in 3 lettered words
                 * there are only 23 ambiguous cases. He writes:
                 *
                 * If the last letter is 'sa' and the first two
                 * letters are affixes, then the SECOND ONE is the
                 * root letter in the following 9 WORDS ONLY:
                 *
                 * gdas gnas gsas dgas dmas bdas mdas 'gas 'das
                 *
                 * And the FIRST is the root letter in the following
                 * 14 WORDS ONLY:
                 *
                 * rags lags nags bags bangs gangs rangs langs nangs
                 * sangs babs rabs rams nams
                 *
                 * As I mentioned before, I think that the best
                 * solution for now is to hard-wire these cases. Even
                 * if the list is not exhaustive, at least we'll have
                 * most cases covered.  */

                leftover = 3;
                /* FIXME: these constants are hard-wired here, rather
                 * than in TibetanMachineWeb, because I'm lazy. */
                String wylie1 = ((TGCPair)gcs.get(0)).wylie;
                String wylie2 = ((TGCPair)gcs.get(1)).wylie;
                String wylie3 = ((TGCPair)gcs.get(2)).wylie;
                if ((wylie1.equals("g") && (wylie2.equals("d") || wylie2.equals("n") || wylie2.equals("s")))
                    || (wylie1.equals("d") && (wylie2.equals("g") || wylie2.equals("m")))
                    || (wylie1.equals("b") && wylie2.equals("d"))
                    || (wylie1.equals("m") && wylie2.equals("d"))
                    || (wylie1.equals("'") && (wylie2.equals("g") || wylie2.equals("d")))) {
                    if (TibetanMachineWeb.isAmbiguousWylie(wylie1, wylie2))
                        wylieBuffer.append(wylie1 + WYLIE_DISAMBIGUATING_KEY + wylie2);
                    else
                        wylieBuffer.append(wylie1 + wylie2);

                    wylieBuffer.append(aVowelToUseAfter(wylie2)
                                       + wylie3);
                } else {
                    wylieBuffer.append(wylie1
                                       + aVowelToUseAfter(wylie1)
                                       + unambiguousPostAVowelWylie(wylie2,
                                                                    wylie3));
                }
            } else if ("root" == candidateType
                       || "prefix/root-root/suffix" == candidateType
                       || "prefix/root" == candidateType
                       || "root-suffix-postsuffix" == candidateType
                       || "root-suffix" == candidateType) {
                String wylie1 = ((TGCPair)gcs.get(0)).wylie;
                leftover = 1;
                wylieBuffer.append(wylie1);
                if (((TGCPair)gcs.get(0)).classification
                    != TGCPair.CONSONANTAL_WITH_VOWEL) {
                    ThdlDebug.verify(TGCPair.CONSONANTAL_WITHOUT_VOWEL
                                     == ((TGCPair)gcs.get(0)).classification);
                    wylieBuffer.append(aVowelToUseAfter(wylie1));
                    if (debug) System.out.println("DEBUG: appending vowel");
                } else {
                    if (debug) System.out.println("DEBUG: already has vowel 2");
                }
                if ("root-suffix-postsuffix" == candidateType) {
                    leftover = 3;
                    String wylie2 = ((TGCPair)gcs.get(1)).wylie;
                    String wylie3 = ((TGCPair)gcs.get(2)).wylie;
                    wylieBuffer.append(unambiguousPostAVowelWylie(wylie2,
                                                                  wylie3));
                }
            } else if ("prefix-root-suffix" == candidateType
                       || "prefix-root" == candidateType
                       || "prefix-root-suffix-postsuffix" == candidateType) {
                String wylie1 = ((TGCPair)gcs.get(0)).wylie;
                String wylie2 = ((TGCPair)gcs.get(1)).wylie;
                leftover = 2;
                if (TibetanMachineWeb.isAmbiguousWylie(wylie1, wylie2))
                    wylieBuffer.append(wylie1 + WYLIE_DISAMBIGUATING_KEY + wylie2);
                else
                    wylieBuffer.append(wylie1 + wylie2);

                if (((TGCPair)gcs.get(1)).classification
                    != TGCPair.CONSONANTAL_WITH_VOWEL) {
                    ThdlDebug.verify(TGCPair.CONSONANTAL_WITHOUT_VOWEL
                                     == ((TGCPair)gcs.get(1)).classification);
                    if (debug) System.out.println("DEBUG: appending vowel");
                    wylieBuffer.append(aVowelToUseAfter(wylie2));
                } else {
                    if (debug) System.out.println("DEBUG: already has vowel 1");
                }
                if ("prefix-root-suffix-postsuffix" == candidateType) {
                    leftover = 4;
                    String wylie3 = ((TGCPair)gcs.get(2)).wylie;
                    String wylie4 = ((TGCPair)gcs.get(3)).wylie;
                    wylieBuffer.append(unambiguousPostAVowelWylie(wylie3,
                                                                  wylie4));
                }
            } else if ("number" == candidateType) {
                leftover = 0;
            } else {
                throw new Error("missed a case down here");
            }

            // append the wylie left over:
            for (int i = leftover; i < sz; i++) {
                TGCPair tp = (TGCPair)gcs.get(i);
                String wylie = tp.wylie;
                wylieBuffer.append(wylie);
            }
        }
    }

/**
* Gets the Extended Wylie for a sequence of glyphs using Chandler's
* experimental method.  This works as follows:
*
* <p>We run along until we hit whitespace or punctuation.  We take
* everything before that and we see if it's a legal Tibetan tsheg bar,
* either a number or a word fragment.  If it is, we insert only one
* vowel in the correct place.  If not, then we throw a disambiguating
* key or a vowel after each stack.
*
* @param dcs an array of glyphs
* @param noSuchWylie an array which will not be touched if this is
* successful; however, if there is no THDL Extended Wylie
* corresponding to these glyphs, then noSuchWylie[0] will be set to
* true
* @param warnings either null or a buffer to which will be appended
* warnings about illegal tsheg bars
* @return the Extended Wylie corresponding to these glyphs, or null */
    public static String getWylieImplementation(DuffCode[] dcs,
                                                boolean noSuchWylie[],
                                                StringBuffer warnings) {
        if (dcs.length == 0)
            return null;

        ArrayList glyphList = new ArrayList();
        StringBuffer wylieBuffer = new StringBuffer();

        for (int i=0; i<dcs.length; i++) {
            char ch = dcs[i].getCharacter();
            int k = dcs[i].getCharNum();
            // int fontNum = dcs[i].getFontNum();

            if (k < 32) {
                if (!glyphList.isEmpty()) {
                    getTshegBarWylie(glyphList, noSuchWylie,
                                     warnings, wylieBuffer);
                    glyphList.clear();
                    if (null != warnings)
                        warnings.append("Some glyphs came right before a newline; they did not have a tsheg or shad come first.");
                }

                wylieBuffer.append(ch);
            } else {
                String wylie = TibetanMachineWeb.getWylieForGlyph(dcs[i], noSuchWylie);
                if (TibetanMachineWeb.isWyliePunc(wylie)
                    && !TibetanMachineWeb.isWylieAdornment(wylie)) {
                    if (!glyphList.isEmpty()) {
                        getTshegBarWylie(glyphList, noSuchWylie,
                                         warnings, wylieBuffer);
                        glyphList.clear();
                    }
                    wylieBuffer.append(wylie); //append the punctuation
                } else {
                    glyphList.add(dcs[i]);
                }
            }
        }

        // replace remaining TMW with Wylie

        if (!glyphList.isEmpty()) {
            getTshegBarWylie(glyphList, noSuchWylie, warnings, wylieBuffer);
            // glyphList.clear() if we weren't about to exit...
            if (null != warnings)
                warnings.append("The stretch of Tibetan ended without final punctuation.");
        }

        if (wylieBuffer.length() > 0)
            return wylieBuffer.toString();
        else
            return null;
    }
}

/** An ordered pair consisting of a Tibetan grapheme cluster's (see
    {@link org.thdl.tib.text.tshegbar.UnicodeGraphemeCluster} for a
    definition of the term}) classification and its
    context-insensitive THDL Extended Wylie representation. */
class TGCPair {
    static final int OTHER = 1;
    // a standalone achen would fall into this category:
    static final int CONSONANTAL_WITHOUT_VOWEL = 2;
    static final int CONSONANTAL_WITH_VOWEL = 3;
    static final int LONE_VOWEL = 4;
    static final int SANSKRIT_WITHOUT_VOWEL = 5;
    static final int SANSKRIT_WITH_VOWEL = 6;

    String wylie;
    int classification;
    TGCPair(String wylie, int classification) {
        this.wylie = wylie;
        this.classification = classification;
    }
    public String toString() {
        return "<TGCPair wylie=" + wylie + " classification="
            + classification + "/>";
    }
}
