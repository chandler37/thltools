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

package org.thdl.tib.input;

import java.util.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.font.*;
import java.awt.event.*;
import javax.swing.*; 
import javax.swing.text.*;
import java.lang.*;
import org.thdl.tib.text.*;

/**
* Enables input of Tibetan text
* using Tibetan Computer Company's free cross-platform TibetanMachineWeb fonts.
* Two modes of text entry are allowed. In Tibetan mode, keystrokes are intercepted
* and reinterpreted according to the Tibetan keyboard installed. The result, of
* course, is Tibetan text, in the TibetanMachineWeb encoding. In Roman mode, 
* keystrokes are not intercepted, and the font defaults to a Roman or user-defined font.
* @author Edward Garrett, Tibetan and Himalayan Digital Library
* @version 1.0
*/
public class DuffPane extends JTextPane implements KeyListener, FocusListener {
/*
* A central part of the Tibetan keyboard. As keys are typed, they are
* added to charList if they constitute a valid Wylie character. charList
* is added to in this manner until the user types punctuation, a vowel,
* or some action or function key. Later, when glyphs are printed to the
* screen, the {@link #newGlyphList glyphList}) is computed on the basis 
* of charList.
*/
	private java.util.LinkedList charList;
/* 
* This field holds a copy of the last {@link #newGlyphList}.
* Then, when a key is pressed, {@link #charList} is updated, a new
* newGlyphList is computed, and the newGlyphList is compared against
* this field. The text on the screen is then modified to reflect
* the new newGlyphList.
*/
	private java.util.List oldGlyphList;
/*
* A central component of the Tibetan input method. While {@link #charList charList}
* keeps track of the characters that have been entered, it does not organize them
* correctly into the proper glyphs. For example, charList might have four characters
* in it, 'b', 's', 'g', and 'r', but it does not know that they should be drawn as
* two glyphs, 'b' and 's-g-r'. newGlyphList is a list of glyphs
* ({@link thdl.tibetan.text.DuffCode DuffCodes}) which is formed by
* @link #recomputeGlyphs(boolean areStacksOnRight, boolean definitelyTibetan, boolean definitelySanskrit) recomputeGlyphs},
* which constructs an optimal arrangement of glyphs from the charList.
*/
	private java.util.List newGlyphList;
/* 
* This field keeps track of what is currently being typed, to account
* for characters (such as Wylie 'tsh') which correspond to more than one
* keystroke in the keyboard. It is cleared or readjusted when it is clear
* that the user has moved on to a different character. For example, after the
* user types 'khr', holdCurrent will contain only 'r', since 'khr' is not
* a valid character.
*/
	private StringBuffer holdCurrent;
/*
* This field says whether or not the character atop {@link #charList} has
* been finalized, and therefore whether subsequent keystrokes are
* allowed to displace this character. For example, if 'k' is at the top
* of charList, and isTopHypothesis is true, then typing 'h' would replace
* 'k' with 'kh'. On the other hand, were isTopHypothesis false, then typing
* 'h' would add 'h' to the top of charList instead.
*/
	private boolean isTopHypothesis;
/*
* Is the user in the process of typing a vowel?
*/
	private boolean isTypingVowel;
/*
* Is it definitely the case that the user is typing Tibetan, rather than
* Sanskrit?
*/
	private boolean isDefinitelyTibetan;
/*
* According to the active keyboard, what value is
* {@link #isDefinitelyTibetan} assigned by default when the 
* keyboard is initialized by {@link #initKeyboard() initKeyboard}?
*/
	private boolean isDefinitelyTibetan_default;
/*
* According to the active keyboard, what value should
* be assigned to {@link #isDefinitelyTibetan} if the
* user has initiated a stack by typing a stack key?
* For example, in the Wylie keyboard, there is a Sanskrit
* stacking key ('+'), but no Tibetan stacking key.
* Therefore, if the user is stacking with '+', this field
* should be false, since what the user is typing must
* be Sanskrit, not Tibetan.
*/
	private boolean isDefinitelyTibetan_withStackKey;
/*
* Is it definitely the case that the user is typing Sanskrit
* (e.g. a Sanskrit stack), rather than Tibetan?
*/
	private boolean isDefinitelySanskrit;
/*
* According to the active keyboard, what value is
* {@link #isDefinitelySanskrit} assigned by default when the 
* keyboard is initialized by {@link #initKeyboard() initKeyboard}?
*/
	private boolean isDefinitelySanskrit_default;
/*
* According to the active keyboard, what value should
* be assigned to {@link #isDefinitelySanskrit} if the
* user has initiated a stack by typing a stack key?
* For example, in the Wylie keyboard, there is a Sanskrit
* stacking key ('+'), but no Tibetan stacking key.
* Therefore, if the user is stacking with '+', this field
* should be true, since what the user is typing must
* be Sanskrit, not Tibetan.
*/
	private boolean isDefinitelySanskrit_withStackKey;
/*
* Is consonant stacking allowed at the moment? In the Wylie
* keyboard, consonant stacking is usually on, since stacking
* is automatic. However, in the TCC and Sambhota keyboards,
* stacking is off by default, since you can only stack when
* you've pressed a stacking key.
*/
	private boolean isStackingOn;
/*
* According to the active keyboard, is stacking on by
* default or not, assuming no stack key has been pressed?
*/
	private boolean isStackingOn_default;
/*
* Automatic stacking in Wylie is from right to left. For
* example, if the user types 'brg', the resulting glyph
* sequence is 'b' plus 'rg', not 'br' plus 'g'. If
* stacking results from the use of a stack key,
* it is from left to right.
*/
	private boolean isStackingRightToLeft;
/*
* If the character last displayed was a vowel,
* how many glyphs is the vowel composed of?
* (Some vowels, such as Wylie 'I', consist of
* two glyphs.)
*/
	private int numberOfGlyphsForLastVowel;
/*
* used for tracking changes in Wylie to TMW conversion
*/
	private int lastStart;
/*
* is the user in Tibetan typing mode? this is true
* by default
*/
	private boolean isTibetan = true;
/*
* is the user allowed to type non-Tibetan? this is true
* by default
*/
	private boolean isRomanEnabled = true;
/*
* The document displayed by this object.
*/
	private TibetanDocument doc;
/*
* The caret of {@link #doc}, used to keep track of the
* current entry/edit/deletion position.
*/
	private Caret caret;
	private StyledEditorKit editorKit;
	private StyleContext styleContext;
	private Style rootStyle;
	private boolean skipUpdate = false;
	private boolean isCutAndPasteEnabled = true;

	private String romanFontFamily;
	private int romanFontSize;
	private MutableAttributeSet romanAttributeSet;

	public DuffPane() {
		this(new StyledEditorKit());
	}

	public DuffPane(TibetanKeyboard keyboard) {
		this(new StyledEditorKit(), keyboard);
	}

	public DuffPane(java.net.URL keyboardURL) {
		this(new StyledEditorKit(), keyboardURL);
	}

	public DuffPane(StyledEditorKit styledEditorKit) {
		setupKeyboard();
		setupEditor(styledEditorKit);
	}

	public DuffPane(StyledEditorKit styledEditorKit, TibetanKeyboard keyboard) {
		TibetanMachineWeb.setKeyboard(keyboard);
		setupKeyboard();
		setupEditor(styledEditorKit);
	}

	public DuffPane(StyledEditorKit styledEditorKit, java.net.URL keyboardURL) {
		TibetanMachineWeb.setKeyboard(keyboardURL);
		setupKeyboard();
		setupEditor(styledEditorKit);
	}

/**
* This method sets up the editor, assigns fonts and point sizes,
* sets the document, the caret, and adds key and focus listeners.
*
* @param sek the StyledEditorKit for the editing window
*/
	private void setupEditor(StyledEditorKit sek) {
		romanFontFamily = "Serif";
		romanFontSize = 14;
		setRomanAttributeSet(romanFontFamily, romanFontSize);

		editorKit = sek;
		setEditorKit(editorKit);
		styleContext = new StyleContext();

		Style defaultStyle = styleContext.getStyle(StyleContext.DEFAULT_STYLE);
		rootStyle = styleContext.addStyle("RootStyle", defaultStyle);
		StyleConstants.setFontFamily(rootStyle, "TibetanMachineWeb");
		StyleConstants.setFontSize(rootStyle, 36);
		setLogicalStyle(rootStyle);

		newDocument();		
		caret = getCaret();

		addKeyListener(this);
		addFocusListener(this);
	}

/**
* This method sets up the Tibetan keyboard. Initially it is called
* by the constructor, but it is also called internally whenever
* the active keyboard is changed. It first sets various values with
* respect to stacking, and concerning the differences between
* Tibetan and Sanskrit; and then it initializes the input method.
*/
	private void setupKeyboard() {
		if (TibetanMachineWeb.hasTibetanStackingKey()) {
			if (TibetanMachineWeb.hasSanskritStackingKey()) {
				isDefinitelyTibetan_default = false;
				isDefinitelySanskrit_default = false;
				isStackingOn_default = false;
				isDefinitelyTibetan_withStackKey = false;
				isDefinitelySanskrit_withStackKey = false;
			}
			else {
				isDefinitelyTibetan_default = false;
				isDefinitelySanskrit_default = true;
				isStackingOn_default = true;
				isDefinitelyTibetan_withStackKey = true;
				isDefinitelySanskrit_withStackKey = false;
			}
		}
		else {
			if (TibetanMachineWeb.hasSanskritStackingKey()) {
				isDefinitelyTibetan_default = true;
				isDefinitelySanskrit_default = false;
				isStackingOn_default = true;
				isDefinitelyTibetan_withStackKey = false;
				isDefinitelySanskrit_withStackKey = true;
			}
			else { //no stacking key at all
				isDefinitelyTibetan_default = false;
				isDefinitelySanskrit_default = false;
				isStackingOn_default = true;
			}
		}
		charList = new LinkedList();
		oldGlyphList = new ArrayList();
		newGlyphList = new ArrayList();
		initKeyboard();
	}

/**
* Registers the Extended Wylie keyboard, and sets it as
* the active keyboard. 
* Unpredictable behavior will result
* if you set the keyboard in {@link org.thdl.tib.text.TibetanMachineWeb TibetanMachineWeb}
* but don't register it here.
*/
	public void registerKeyboard() {
		TibetanKeyboard tk = null;
		registerKeyboard(tk);
	}

/**
* Registers a keyboard, and sets it as
* the active keyboard. * Unpredictable behavior will result
* if you set the keyboard in {@link org.thdl.tib.text.TibetanMachineWeb TibetanMachineWeb}
* but don't register it in here.
* @param keyboardURL the URL of the keyboard you want to install
*/
	public void registerKeyboard(java.net.URL keyboardURL) {
		TibetanMachineWeb.setKeyboard(keyboardURL);
		setupKeyboard();
	}

/**
* Registers a keyboard, and sets it as
* the active keyboard.
* Unpredictable behavior will result
* if you set the keyboard in {@link org.thdl.tib.text.TibetanMachineWeb TibetanMachineWeb}
* but don't register it in here.
* @param keyboard the keyboard you want to install
*/
	public void registerKeyboard(TibetanKeyboard keyboard) {
		TibetanMachineWeb.setKeyboard(keyboard);
		setupKeyboard();
	}

/**
* Clears the current document.
*/
	public void newDocument() {
		doc = new TibetanDocument(styleContext);
		doc.setTibetanFontSize(36);
		setDocument(doc);
		setLogicalStyle(rootStyle);
	}

/**
* Initializes the keyboard input interpreter, setting all properties
* back to their default states. This method is called whenever an action
* or function key is pressed, and also whenever the user types punctuation
* or a vowel. It is not called when the user is typing characters that
* could be part of a stack, or require manipulation of glyphs - e.g.
* backspacing, redrawing, etc.
*/
	public void initKeyboard() {
		charList.clear();
		oldGlyphList.clear();
		holdCurrent = new StringBuffer();
		isTopHypothesis = false;
		isTypingVowel = false;
		numberOfGlyphsForLastVowel = 0;

		//for keyboard
		isStackingOn = isStackingOn_default;
		isStackingRightToLeft = true;
		isDefinitelyTibetan = isDefinitelyTibetan_default;
		isDefinitelySanskrit = isDefinitelySanskrit_default;
	}

/**
* Enables typing of Roman (non-Tibetan) text along
* with Tibetan.
*/
	public void enableRoman() {
		isRomanEnabled = true;
	}

/**
* Disables typing of Roman (non-Tibetan) text.
*/
	public void disableRoman() {
		isRomanEnabled = false;
	}

/**
* Checks to see if Roman input is enabled.
* @return true if so, false if not
*/
	public boolean isRomanEnabled() {
		return isRomanEnabled;
	}

/**
* Checks to see if currently in Roman input mode.
* @return true if so, false if not
*/
	public boolean isRomanMode() {
		return !isTibetan;
	}

/**
* Toggles between Tibetan and Roman input modes.
* Does nothing if Roman input is disabled.
* @see #enableRoman()
* @see #disableRoman()
*/
	public void toggleLanguage() {
		if (isTibetan && isRomanEnabled)
			isTibetan = false;
		else
			isTibetan = true;
	}

/**
* Inserts Roman text into this object's document,
* at the position of the caret.
* @param attr the attributes for the text to insert
* @param s the string of text to insert
*/
	public void append(String s, MutableAttributeSet attr) {
		append(caret.getDot(), s, attr);
	}

/**
* Inserts Roman text into this object's document.
* @param offset the position at which to insert text
* @param attr the attributes for the text to insert
* @param s the string of text to insert
*/
	public void append(int offset, String s, MutableAttributeSet attr) {
		try {
			doc.insertString(offset, s, attr);
		}
		catch (BadLocationException ble) {
		}
	}

/**
* Changes the default font size for Tibetan text entry mode.
*
* @param size a point size
*/
	public void setTibetanFontSize(int size) {
		if (size > 0)
			doc.setTibetanFontSize(size);
	}

/**
* Gets the current point size for Tibetan text.
* @return the current default font size for Tibetan
* text entry mode
*/
	public int getTibetanFontSize() {
		return doc.getTibetanFontSize();
	}

/**
* Changes the default font and font size for 
* non-Tibetan (Roman) text entry mode.
*
* @param font a font name
* @param size a point size
*/
	public void setRomanAttributeSet(String font, int size) {
		romanAttributeSet = new SimpleAttributeSet();
		StyleConstants.setFontFamily(romanAttributeSet, font);
		StyleConstants.setFontSize(romanAttributeSet, size);
	}

/**
* Gets the current point size for non-Tibetan text.
* @return the current default font size for non-Tibetan
* (Roman) text entry mode
*/
	public int getRomanFontSize() {
		return romanFontSize;
	}

/**
* Gets the current font used for non-Tibetan text.
* @return the current default font for non-Tibetan
* (Roman) text entry mode
*/
	public String getRomanFontFamily() {
		return romanFontFamily;
	}

/**
* Backspace and remove k elements from the current caret position.
*
* @param k the number of glyphs to remove by backspace
*/
	private void backSpace(int k) {
		try {
			doc.remove(caret.getDot()-k, k);
		}
		catch (BadLocationException ble) {
		}
	}

/**
* Takes an old glyph list, which should be the currently visible set of glyphs preceding the cursor, and
* then tries to redraw the glyphs in light of the newly acquired keyboard input (which led to a revised
* 'new' glyph list). For example, the old glyph list might contain 'l' and 'n', because the user had typed
* 'ln' in Extended Wylie mode. This is what you'd see on the screen. But assume that the new glyph list
* contains the stacked glyph 'l-ng', because the user has just finished typing 'lng'. This method
* compares the glyphs, then figures out whether or not backspacing is necessary, and draws whatever characters
* need to be drawn.
* For example, suppose that oldGlyphList contains the two glyphs 'l' and 'n', and newGlyphList contains a single glyph, 'lng'.
* In this case, redrawGlyphs will be instructed to backspace over both 'l' and 'n', and then insert 'lng'.
*/
	private java.util.List redrawGlyphs(java.util.List oldGlyphList, java.util.List newGlyphList) {
		if (newGlyphList.isEmpty())
			return newGlyphList;

		Iterator newIter = newGlyphList.iterator();
		DuffCode newDc = (DuffCode)newIter.next();

		int oldGlyphCount = oldGlyphList.size();
		int newGlyphCount = newGlyphList.size();
		int beginDifference = -1; //at what point does the new glyph list begin to differ from the old one?
		int k=0;

		if (oldGlyphCount!=0) {
			int smallerGlyphCount;
			DuffCode oldDc;

			if (oldGlyphCount < newGlyphCount)
				smallerGlyphCount = oldGlyphCount;
			else
				smallerGlyphCount = newGlyphCount;

			Iterator oldIter = oldGlyphList.iterator();

			for (; k<smallerGlyphCount; k++) {
				oldDc = (DuffCode)oldIter.next();

				if (!oldDc.equals(newDc)) {
					beginDifference = k;
					break;
				}
				if (newIter.hasNext())
					newDc = (DuffCode)newIter.next();
			}

			if (beginDifference == -1)
				beginDifference = smallerGlyphCount;

			if (beginDifference == newGlyphCount) {
				if (oldGlyphCount > newGlyphCount)
					backSpace(oldGlyphCount-newGlyphCount); //deals with 'pd+m' problem

				return newGlyphList; //there is no difference between new and old glyph lists
			}
		}

		if (beginDifference != -1)
			backSpace(oldGlyphCount - beginDifference);

		java.util.List sublist = newGlyphList.subList(k, newGlyphCount);
		TibetanDocument.DuffData[] dd = TibetanDocument.convertGlyphs(sublist);
		doc.insertDuff(caret.getDot(), dd);
		return newGlyphList;
	}

/**
* Tries to insert the vowel v at the position of the caret.
* This method must deal with various issues, such as: can the preceding
* glyph take a vowel? If not, then what? If the preceding glyph can be
* followed by a vowel, then the method has to figure out what vowel
* glyph to affix, which may depend on the immediately preceding glyph,
* but may depend on other factors as well. For example, when affixing
* gigu to the consonantal stack "k'" (ie k plus achung), the value of
* the gigu will depend on "k", not "'".
* 
* @param v the vowel (in Wylie) you want to insert
*/
	private void putVowel(String v) {
		if (caret.getDot()==0) {
			if (!TibetanMachineWeb.isAChenRequiredBeforeVowel())
				printAChenWithVowel(v);

			return;
		}

		AttributeSet attr = doc.getCharacterElement(caret.getDot()-1).getAttributes();
		String fontName = StyleConstants.getFontFamily(attr);
		int fontNum;

		if (0 != (fontNum = TibetanMachineWeb.getTMWFontNumber(fontName))) {
			try {
				char c2 = doc.getText(caret.getDot()-1, 1).charAt(0);
				int k = (int)c2;
				if (k<32 || k>126) { //if previous character is formatting or some other non-character
					if (!TibetanMachineWeb.isAChenRequiredBeforeVowel())
						printAChenWithVowel(v);

					return;
				}

				String wylie = TibetanMachineWeb.getWylieForGlyph(fontNum, k);
				if (TibetanMachineWeb.isWyliePunc(wylie)) {
					if (charList.isEmpty() && !TibetanMachineWeb.isAChenRequiredBeforeVowel()) {
						printAChenWithVowel(v);
						return;
					}
				}

				DuffCode dc_1 = null;
				DuffCode dc_2 = new DuffCode(fontNum, c2);

				if (caret.getDot() > 2) {
					attr = doc.getCharacterElement(caret.getDot()-2).getAttributes();
					fontName = StyleConstants.getFontFamily(attr);
					if (0 != (fontNum = TibetanMachineWeb.getTMWFontNumber(fontName))) {
						c2 = doc.getText(caret.getDot()-2, 1).charAt(0);
						dc_1 = new DuffCode(fontNum, c2);
					}
				}

				java.util.List before_vowel = new ArrayList();
				if (null != dc_1)
					before_vowel.add(dc_1);

				before_vowel.add(dc_2);
				java.util.List after_vowel = TibetanDocument.getVowel(dc_1, dc_2, v);
				redrawGlyphs(before_vowel, after_vowel);
			}
			catch(BadLocationException ble) {
				System.out.println("no--can't insert here");
			}
		}
		else { //0 font means not Tibetan font, so begin new Tibetan font section
			if (!TibetanMachineWeb.isAChenRequiredBeforeVowel())
				printAChenWithVowel(v);
		}
	}

/**
* Prints ACHEN together with the vowel v. When using the Wylie
* keyboard, or any other keyboard in which {@link thdl.tibetan.text.TibetanMachineWeb#isAChenRequiredBeforeVowel() isAChenRequiredBeforeVowel()}
* is false, this method is called frequently. 
*
* @param v the vowel (in Wylie) which you want to print with ACHEN
*/
	private void printAChenWithVowel(String v) {
		DuffCode[] dc_array = (DuffCode[])TibetanMachineWeb.getTibHash().get(TibetanMachineWeb.ACHEN);
		DuffCode dc = dc_array[TibetanMachineWeb.TMW];
		java.util.List achenlist = TibetanDocument.getVowel(dc,v);
		TibetanDocument.DuffData[] dd = TibetanDocument.convertGlyphs(achenlist);
		doc.insertDuff(caret.getDot(), dd);		
	}

/**
* Puts a bindu/anusvara at the current caret position.
* In the TibetanMachineWeb font, top vowels (like gigu,
* drengbu, etc.) are merged together with bindu in a
* single glyph. This method deals with the problem of
* correctly displaying a bindu given this complication.
*/
	private void putBindu() {
		special_bindu_block: {
			if (caret.getDot()==0)
				break special_bindu_block;

			AttributeSet attr = doc.getCharacterElement(caret.getDot()-1).getAttributes();
			String fontName = StyleConstants.getFontFamily(attr);
			int fontNum;

			if (0 == (fontNum = TibetanMachineWeb.getTMWFontNumber(fontName)))
				break special_bindu_block;

			try {
				char c2 = doc.getText(caret.getDot()-1, 1).charAt(0);
				int k = (int)c2;
				if (k<32 || k>126) //if previous character is formatting or some other non-character
					break special_bindu_block;

				String wylie = TibetanMachineWeb.getWylieForGlyph(fontNum, k);
				if (!TibetanMachineWeb.isWylieVowel(wylie))
					break special_bindu_block;

				DuffCode dc = new DuffCode(fontNum, c2);
				java.util.List beforecaret = new ArrayList();
				beforecaret.add(dc);
				java.util.List bindulist = TibetanDocument.getBindu(dc);
				redrawGlyphs(beforecaret, bindulist);
				initKeyboard();
				return;
			}
			catch(BadLocationException ble) {
				System.out.println("no--can't do this bindu maneuver");
			}
		}

		TibetanDocument.DuffData[] dd = TibetanDocument.convertGlyphs(TibetanDocument.getBindu(null));
		doc.insertDuff(caret.getDot(), dd);
		initKeyboard();
	}

/**
* Required by implementations of the
* {@link java.awt.event.FocusListener FocusListener} interface,
* this method simply initializes the keyboard whenever this
* object gains focus.
*
* @param e a FocusEvent
*/
	public void focusGained(FocusEvent e) {
	}

/**
* Required by implementations of the
* {@link java.awt.event.FocusListener FocusListener} interface,
* this method does nothing.
*
* @param e a FocusEvent
*/
	public void focusLost(FocusEvent e) {
		initKeyboard();
	}

/**
* Cuts or copies the specified portion of this object's document
* to the system clipboard. What is cut/copied is Wylie text -
* so, if you cut/copy a region of TibetanMachineWeb, it first converts
* it to Wylie before putting it on the clipboard. If the user
* tries to cut/copy non-TibetanMachineWeb, only the text preceding the 
* first non-TibetanMachineWeb character is cut/copied.
* @param start the begin offset of the copy
* @param end the end offset of the copy
* @param remove this should be true if the operation is 'cut',
* false if it is 'copy'
*/
	public void copy(int start, int end, boolean remove) {
		if (start < end) {
			java.util.List dcs = new ArrayList();

			try {
				AttributeSet attr;
				String fontName;
				int fontNum;
				DuffCode dc;
				char ch;

				int i = start;

				while (i < end+1) {
					attr = doc.getCharacterElement(i).getAttributes();
					fontName = StyleConstants.getFontFamily(attr);

					if ((0 == (fontNum = TibetanMachineWeb.getTMWFontNumber(fontName))) || i==end) {
						if (i != start) {
							DuffCode[] dc_array = new DuffCode[0];
							dc_array = (DuffCode[])dcs.toArray(dc_array);
							StringSelection data = new StringSelection(TibetanDocument.getWylie(dc_array));
							Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
							clipboard.setContents(data, data);
							if (remove) {
								doc.remove(start, i-start);
								setSelectionEnd(start);
							}
							else {
								setSelectionStart(start);
								setSelectionEnd(i);
							}
							return;
						}
						else {
							setSelectionEnd(start);
							return;
						}
					}
					else {
						ch = doc.getText(i,1).charAt(0);
						dc = new DuffCode(fontNum, ch);
						dcs.add(dc);
					}
					i++;
				}
			}
			catch (BadLocationException ble) {
				ble.printStackTrace();
			}
		}
	}

/**
* Pastes the contents of the system clipboard into this object's
* document, at the specified position. The only kind of
* text accepted from the clipboard is Wylie text.
* This Wylie is converted and pasted into the document as
* TibetanMachineWeb. If the text to paste is invalid Wylie,
* then it will not be pasted, and instead an error message will
* appear.
* @param offset the position in the document you want to paste to
*/
	public void paste(int offset) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable data = clipboard.getContents(this);

		String s = null;
		try {
			s = (String)(data.getTransferData(DataFlavor.stringFlavor));
		}
		catch (Exception e) {
			s = data.toString();
		}

		if (s != null)
			toTibetanMachineWeb(s, offset);
	}

/**
* Enables cutting and pasting of Tibetan text.
*/
	public void enableCutAndPaste() {
		isCutAndPasteEnabled = true;
	}

/**
* Disables cutting and pasting of Tibetan text.
* Cut and paste must be disabled if Jskad's
* parent is an applet, because it violates the
* Java security sandbox to cut and paste from an
* applet to the system clipboard.
*/
	public void disableCutAndPaste() {
		isCutAndPasteEnabled = false;
	}

/**
* This method is required as part of the
* implementation of the KeyListener
* interface.
*
* Basically this method only handles action keys - 
* Escape, Ctrl-c, Ctrl-x, etc, and TAB and ENTER.
* Other keystrokes are handled by keyTyped.
*/
	public void keyPressed(KeyEvent e) {
		if (e.isActionKey())
			initKeyboard();

		int code = e.getKeyCode();

		switch (code) {
			case KeyEvent.VK_ESCAPE:
				e.consume();
				initKeyboard();
//				toggleLanguage();
				break;

			case KeyEvent.VK_A:
				if (e.isControlDown() && isCutAndPasteEnabled) {
					e.consume();
					setSelectionStart(0);
					setSelectionEnd(doc.getLength());
				}
				break;

			case KeyEvent.VK_C:
				if (e.isControlDown() && isCutAndPasteEnabled) {
					e.consume();
					copy(getSelectionStart(), getSelectionEnd(), false);
				}
				break;

			case KeyEvent.VK_X:
				if (e.isControlDown() && isCutAndPasteEnabled) {
					e.consume();
					copy(getSelectionStart(), getSelectionEnd(), true);
				}
				break;

			case KeyEvent.VK_V:
				if (e.isControlDown() && isCutAndPasteEnabled) {
					e.consume();
					paste(caret.getDot());
				}
				break;

			case KeyEvent.VK_TAB:
				e.consume();
				initKeyboard();
				if (isTibetan)
					doc.appendDuff(caret.getDot(),"	",TibetanMachineWeb.getAttributeSet(1));
				else
					append("	", romanAttributeSet);
				break;

			case KeyEvent.VK_ENTER:
				e.consume();
				initKeyboard();
				if (isTibetan)
					doc.appendDuff(caret.getDot(),"\n",TibetanMachineWeb.getAttributeSet(1));
				else
					append("\n", romanAttributeSet);
				break;
		}
	}

/**
* Required of implementations of the Key Listener interface,
* this method does (almost) nothing.
*
* @param e the KeyEvent
*/
	public void keyReleased(KeyEvent e) {
/*
* Apparently it works best to check for backspace
* and init the keyboard here in key released
* though i don't really know why...
*/
		int code = e.getKeyCode();

		if (code == KeyEvent.VK_BACK_SPACE)
			initKeyboard();
	}

/**
* Required of implementations of the KeyListener interface,
* this method handles the pressing of non-control and non-action keys. If the user
* is in Tibetan typing mode, then the KeyEvent
* e is consumed, and passed on to {@link #processTibetan(KeyEvent e)}, which
* contains the meat of the keyboard logic. If the user is in English mode, then
* {@link #append(String s, MutableAttributeSet attr) append} is called.
*
* @param e a KeyEvent
*/
	public void keyTyped(KeyEvent e) {
		e.consume();

		if (isTibetan)
			processTibetan(e);
		else {
			if (e.isControlDown() || e.isAltDown())
				return;

			char c = e.getKeyChar();

			switch (c) {
				case KeyEvent.VK_TAB:
				case KeyEvent.VK_ENTER:
				case KeyEvent.VK_ESCAPE:
					break;

				case KeyEvent.VK_BACK_SPACE:
					backSpace(1);
					break;

				default:
					append(String.valueOf(c), romanAttributeSet);
			}
		}
	}

/**
* Interprets a key typed during Tibetan input mode.
* This method keeps track of which characters the user has and is typing,
* and also of whether or not the user is in stacking
* mode. Most of the keyboard logic can be found here.
*
* @param e a KeyEvent
*/
	public void processTibetan(KeyEvent e) {
		char c = e.getKeyChar();

		int start = getSelectionStart();
		int end = getSelectionEnd();
		boolean shouldIBackSpace = true;

		if (e.isControlDown() || e.isAltDown())
			return;

		if (start != end) {
			if (e.getKeyCode() != KeyEvent.VK_ESCAPE) {
				try {
					initKeyboard();
					doc.remove(start, end-start);
					shouldIBackSpace = false;
				}
				catch (BadLocationException ble) {
				}
			}
		}

		key_block: 
		{

		if (TibetanMachineWeb.hasDisambiguatingKey())
			if (c == TibetanMachineWeb.getDisambiguatingKey()) {
				initKeyboard();
				break key_block;
			};

		if (TibetanMachineWeb.hasSanskritStackingKey() || TibetanMachineWeb.hasTibetanStackingKey()) {
			if (c == TibetanMachineWeb.getStackingKey()) {
				if (TibetanMachineWeb.isStackingMedial()) {
					int size = charList.size();

					if (size == 0)
						initKeyboard();

					else if (size > 1 && isStackingRightToLeft) {
						String s = (String)charList.removeLast();
						newGlyphList = TibetanDocument.getGlyphs(charList, isStackingRightToLeft, isDefinitelyTibetan, isDefinitelySanskrit);
						oldGlyphList = redrawGlyphs(oldGlyphList, newGlyphList);
						initKeyboard();
						charList.add(s);
						newGlyphList = TibetanDocument.getGlyphs(charList, isStackingRightToLeft, isDefinitelyTibetan, isDefinitelySanskrit);
						oldGlyphList = redrawGlyphs(oldGlyphList, newGlyphList);
						holdCurrent = new StringBuffer();
						isTopHypothesis = false;
						isStackingOn = true;
						isStackingRightToLeft = false;
						isDefinitelyTibetan = isDefinitelyTibetan_withStackKey;
						isDefinitelySanskrit = isDefinitelySanskrit_withStackKey;
					}
					else {
						holdCurrent = new StringBuffer();
						isTopHypothesis = false;
						isStackingOn = true;
						isStackingRightToLeft = false;
						isDefinitelyTibetan = isDefinitelyTibetan_withStackKey;
						isDefinitelySanskrit = isDefinitelySanskrit_withStackKey;
					}
					break key_block;
				}
				else { //stacking must be pre/post
					if (!isStackingOn || (isStackingOn && isDefinitelyTibetan==isDefinitelyTibetan_default)) {
						initKeyboard();
						isStackingOn = true;
						isStackingRightToLeft = false;
						isDefinitelyTibetan = isDefinitelyTibetan_withStackKey;
						isDefinitelySanskrit = isDefinitelySanskrit_withStackKey;
					}
					else {try {
						char ch = doc.getText(caret.getDot()-1, 1).charAt(0);
						AttributeSet attr = doc.getCharacterElement(caret.getDot()-1).getAttributes();
						String fontName = StyleConstants.getFontFamily(attr);
						int fontNum = TibetanMachineWeb.getTMWFontNumber(fontName);

						if (0 == fontNum) {
							initKeyboard();
							isStackingOn = true;
							isStackingRightToLeft = false;
							isDefinitelyTibetan = isDefinitelyTibetan_withStackKey;
							isDefinitelySanskrit = isDefinitelySanskrit_withStackKey;
						}
						else {
							initKeyboard();
							DuffCode dc = new DuffCode(fontNum, ch);

							if (!TibetanMachineWeb.isStack(dc) && !TibetanMachineWeb.isSanskritStack(dc)) {
								isStackingOn = true;
								isStackingRightToLeft = false;
								isDefinitelyTibetan = isDefinitelyTibetan_withStackKey;
								isDefinitelySanskrit = isDefinitelySanskrit_withStackKey;
							}
						}
					}
					catch (BadLocationException ble) {
						initKeyboard();
					}}
					break key_block;
				}
			}
		}

		switch (c) {
			case KeyEvent.VK_TAB:
			case KeyEvent.VK_ENTER:
			case KeyEvent.VK_ESCAPE:
				initKeyboard();
				break;

			case KeyEvent.VK_BACK_SPACE:
				if (shouldIBackSpace) {
					backSpace(1);
					break;
				}
		
			default:
				String val = String.valueOf(c);

				if (TibetanMachineWeb.isPunc(val)) { //punctuation
					val = TibetanMachineWeb.getWylieForPunc(val);

					if (val.charAt(0) == TibetanMachineWeb.BINDU)
						putBindu();

					else {
						DuffCode puncDc = TibetanMachineWeb.getGlyph(val);
						MutableAttributeSet mas = TibetanMachineWeb.getAttributeSet(puncDc.getFontNum());
						doc.appendDuff(caret.getDot(), String.valueOf(puncDc.getCharacter()), mas);
					}

					initKeyboard();
					return;
				}

				if (charList.size()==0) { //add current character to charList if possible
					holdCurrent.append(c);
					String s = holdCurrent.toString();

					if (TibetanMachineWeb.isVowel(s)) {
						s = TibetanMachineWeb.getWylieForVowel(s);

						if (isTypingVowel) //note: this takes care of multiple keystroke vowels like 'ai'
							backSpace(numberOfGlyphsForLastVowel);

						putVowel(s);
						isTypingVowel = true;
					}
					else {
						if (isTypingVowel) {
							isTypingVowel = false;
							s = String.valueOf(c);
							holdCurrent = new StringBuffer(s);
						}							

						if (TibetanMachineWeb.isVowel(s)) {
							s = TibetanMachineWeb.getWylieForVowel(s);
							putVowel(s);
							isTypingVowel = true;
						}

						else if (TibetanMachineWeb.isChar(s)) {
							s = TibetanMachineWeb.getWylieForChar(s);
							charList.add(s);
							isTopHypothesis = true;
							newGlyphList = TibetanDocument.getGlyphs(charList, isStackingRightToLeft, isDefinitelyTibetan, isDefinitelySanskrit);
							oldGlyphList = redrawGlyphs(oldGlyphList, newGlyphList);
						}
						else
							isTopHypothesis = false;
					}
				}
				else { //there is already a character in charList
					holdCurrent.append(c);
					String s = holdCurrent.toString();

					if (TibetanMachineWeb.isVowel(s)) { //the holding string is a vowel
						s = TibetanMachineWeb.getWylieForVowel(s);
						initKeyboard();
						isTypingVowel = true;
						putVowel(s);
					}
					else if (TibetanMachineWeb.isChar(s)) { //the holding string is a character
						String s2 = TibetanMachineWeb.getWylieForChar(s);

						if (isTopHypothesis) {
							if (TibetanMachineWeb.isAChungConsonant() && isStackingOn && charList.size()>1 && s2.equals(TibetanMachineWeb.ACHUNG)) {
								charList.removeLast();
								newGlyphList = TibetanDocument.getGlyphs(charList, isStackingRightToLeft, isDefinitelyTibetan, isDefinitelySanskrit);
								oldGlyphList = redrawGlyphs(oldGlyphList, newGlyphList);
								putVowel(TibetanMachineWeb.A_VOWEL);
								initKeyboard();
								break key_block;
							}
							charList.set(charList.size()-1, s2);
							newGlyphList = TibetanDocument.getGlyphs(charList, isStackingRightToLeft, isDefinitelyTibetan, isDefinitelySanskrit);
							oldGlyphList = redrawGlyphs(oldGlyphList, newGlyphList);
						}
						else {
							if (!isStackingOn) {
								initKeyboard();
								holdCurrent = new StringBuffer(s);
							}
							else if (TibetanMachineWeb.isAChungConsonant() && s2.equals(TibetanMachineWeb.ACHUNG)) {
								putVowel(TibetanMachineWeb.A_VOWEL);
								initKeyboard();
								break key_block;
							}

							charList.add(s2);
							isTopHypothesis = true;
							newGlyphList = TibetanDocument.getGlyphs(charList, isStackingRightToLeft, isDefinitelyTibetan, isDefinitelySanskrit);
							oldGlyphList = redrawGlyphs(oldGlyphList, newGlyphList);
						}
					}
					else { //the holding string is not a character
						if (isTopHypothesis) { //finalize top character and add new hypothesis to top
							holdCurrent = new StringBuffer();
							holdCurrent.append(c);
							s = holdCurrent.toString();

							if (TibetanMachineWeb.isVowel(s)) {
								String s2 = TibetanMachineWeb.getWylieForVowel(s);
								putVowel(s2);
								initKeyboard();
								isTypingVowel = true;
								holdCurrent = new StringBuffer(s);
							}
							else {
								if (TibetanMachineWeb.isStackingMedial() && !isStackingRightToLeft)
									initKeyboard();

								if (TibetanMachineWeb.isChar(s)) {
									String s2 = TibetanMachineWeb.getWylieForChar(s);

									if (!isStackingOn)
										initKeyboard();

									else if (TibetanMachineWeb.isAChungConsonant() && s2.equals(TibetanMachineWeb.ACHUNG)) {
										putVowel(TibetanMachineWeb.A_VOWEL);
										initKeyboard();
										break key_block;
									}

									charList.add(s2);
									newGlyphList = TibetanDocument.getGlyphs(charList, isStackingRightToLeft, isDefinitelyTibetan, isDefinitelySanskrit);
									oldGlyphList = redrawGlyphs(oldGlyphList, newGlyphList);
								}
								else {
									holdCurrent = new StringBuffer(s);
									isTopHypothesis = false;
								}
							}
						}
						else { //top char is just a guess! just keep it in holdCurrent
						}
					}
				}
		} //end switch
		} //end key_block
	}

/**
* Converts the entire document to Extended Wylie.
*/
	public void toWylie() {
		int start = getSelectionStart();
		int end = getSelectionEnd();

		toWylie(start, end);
	}

/**
* Converts the specified portion
* of this object's document to Extended Wylie.
*
* @param start the point from which to begin converting to Wylie
* @param end the point at which to stop converting to Wylie
*/
	public void toWylie(int start, int end) {
		if (start == end)
			return;

		DuffCode[] dc_array;
		AttributeSet attr;
		String fontName;
		Position endPos;
		int fontNum;
		DuffCode dc;
		char ch;
		int i;

		java.util.List dcs = new ArrayList();

		try {
			endPos = doc.createPosition(end);
			i = start;

			while (i < endPos.getOffset()+1) {
				attr = doc.getCharacterElement(i).getAttributes();
				fontName = StyleConstants.getFontFamily(attr);

				if ((0 == (fontNum = TibetanMachineWeb.getTMWFontNumber(fontName))) || i==endPos.getOffset()) {
					if (i != start) {
						dc_array = new DuffCode[0];
						dc_array = (DuffCode[])dcs.toArray(dc_array);
						doc.remove(start, i-start);
						append(start, TibetanDocument.getWylie(dc_array), romanAttributeSet);
						dcs.clear();
					}
					start = i+1;
				}
				else {
					ch = doc.getText(i,1).charAt(0);
					dc = new DuffCode(fontNum, ch);
					dcs.add(dc);
				}

				i++;
			}
		}
		catch (BadLocationException ble) {
			ble.printStackTrace();
		}
	}

/**
* Converts a string of Extended Wylie to TibetanMachineWeb, and 
* inserts it at the specified position.
*
* @param wylie the string of Wylie to convert
* @param offset the position at which to insert the conversion
*/
	public void toTibetanMachineWeb(String wylie, int offset) {
		try {
			TibetanDocument.DuffData[] dd = TibetanDocument.getTibetanMachineWeb(wylie);
			doc.insertDuff(offset, dd);
		}
		catch (InvalidWylieException iwe) {
			JOptionPane.showMessageDialog(this,
				"The Wylie you are trying to convert is invalid, " +
				"beginning from:\n     " + iwe.getCulpritInContext() + "\n" +
				"The culprit is probably the character '"+iwe.getCulprit()+"'.");
		}

	}

/**
* Converts the currently selected text from Extended Wylie to TibetanMachineWeb.
*/
	public void toTibetanMachineWeb() {
		int start = getSelectionStart();
		int end = getSelectionEnd();

		toTibetanMachineWeb(start, end);
	}

/**
* Converts a stretch of text from Extended Wylie to TibetanMachineWeb.
* @param start the begin point for the conversion
* @param end the end point for the conversion
*/
	public void toTibetanMachineWeb(int start, int end) {
		if (start == end)
			return;

		StringBuffer sb;
		AttributeSet attr;
		String fontName;
		int fontNum;
		Position endPos;
		int i;

		try {
			sb = new StringBuffer();
			endPos = doc.createPosition(end);
			i = start;

			while (i < endPos.getOffset()+1) {
				attr = doc.getCharacterElement(i).getAttributes();
				fontName = StyleConstants.getFontFamily(attr);

				if ((0 != (fontNum = TibetanMachineWeb.getTMWFontNumber(fontName))) || i==endPos.getOffset()) {
					if (i != start) {
						try {
							TibetanDocument.DuffData[] duffdata = TibetanDocument.getTibetanMachineWeb(sb.toString());
							doc.remove(start, i-start);
							doc.insertDuff(start, duffdata);
						}
						catch (InvalidWylieException iwe) {
							JOptionPane.showMessageDialog(this,
								"The Wylie you are trying to convert is invalid, " +
								"beginning from:\n     " + iwe.getCulpritInContext() +
								"\nThe culprit is probably the character '" +
								iwe.getCulprit() + "'.");
							return;
						}
					}
					start = i+1;
				}
				else
					sb.append(doc.getText(i, 1));

				i++;
			}
		}
		catch (BadLocationException ble) {
			ble.printStackTrace();
		}
	}
}
