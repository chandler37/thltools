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

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.font.*;
import java.awt.event.*;
import javax.swing.*; 
import javax.swing.text.*;
import javax.swing.text.rtf.*;

import org.thdl.tib.text.*;
import org.thdl.util.ThdlDebug;
import org.thdl.util.ThdlOptions;
import org.thdl.util.StatusBar;

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
public class DuffPane extends TibetanPane implements FocusListener, KeyListener {
/** 
* The status bar to update with messages about the current input mode.
* Are we expecting a vowel? a subscript? et cetera.
*/
    private StatusBar statBar = null;


/**
* A central part of the Tibetan keyboard. As keys are typed, they are
* added to charList if they constitute a valid Wylie character. charList
* is added to in this manner until the user types punctuation, a vowel,
* or some action or function key. Later, when glyphs are printed to the
* screen, the {@link #newGlyphList} is computed on the basis of charList.
*/
	private java.util.ArrayList charList;
/**
* This field holds a copy of the last {@link #newGlyphList}.
* Then, when a key is pressed, {@link #charList} is updated, a new
* newGlyphList is computed, and the newGlyphList is compared against
* this field. The text on the screen is then modified to reflect
* the new newGlyphList.
*/
	private java.util.List oldGlyphList;
/**
* A central component of the Tibetan input method. While {@link #charList charList}
* keeps track of the characters that have been entered, it does not organize them
* correctly into the proper glyphs. For example, charList might have four characters
* in it, 'b', 's', 'g', and 'r', but it does not know that they should be drawn as
* two glyphs, 'b' and 's-g-r'. newGlyphList is a list of glyphs
* ({@link org.thdl.tib.text.DuffCode DuffCodes}) which is formed by
* @link #recomputeGlyphs(boolean areStacksOnRight, boolean definitelyTibetan, boolean definitelySanskrit) recomputeGlyphs},
* which constructs an optimal arrangement of glyphs from the charList.
*/
	private java.util.List newGlyphList;
/** 
* This field keeps track of what is currently being typed, to account
* for characters (such as Wylie 'tsh') which correspond to more than one
* keystroke in the keyboard. It is cleared or readjusted when it is clear
* that the user has moved on to a different character. For example, after the
* user types 'khr', holdCurrent will contain only 'r', since 'khr' is not
* a valid character.
*/
	private StringBuffer holdCurrent;
/**
* This field says whether or not the character atop {@link #charList}
* has been finalized, and therefore whether subsequent keystrokes are
* allowed to displace this character. For example, if 'k' is at the
* top of charList, and isTopHypothesis is true, then typing 'h' would
* replace 'k' with 'kh'. On the other hand, were isTopHypothesis
* false, then typing 'h' would add 'h' to the top of charList instead.
* In short, is the top character on {@link #charList} a fact or just a
* hypothesis?
*/
	private boolean isTopHypothesis;
/**
* Is the user in the process of typing a vowel?
*/
	private boolean isTypingVowel;
/**
* Is it definitely the case that the user is typing Tibetan, rather than
* Sanskrit?
*/
	private boolean isDefinitelyTibetan;
/**
* According to the active keyboard, what value
* {@link #isDefinitelyTibetan} should be assigned by default when the 
* keyboard is initialized by {@link #initKeyboard() initKeyboard}
*/
	private boolean isDefinitelyTibetan_default;
/**
* According to the active keyboard, what value {@link
* #isDefinitelyTibetan} should be assigned if the user has initiated a
* stack by typing a stack key.  For example, in the Wylie keyboard,
* there is a Sanskrit stacking key ('+'), but no Tibetan stacking key.
* Therefore, if the user is stacking with '+', this field should be
* false, since what the user is typing must be Sanskrit, not Tibetan.  */
	private boolean isDefinitelyTibetan_withStackKey;
/**
* True iff it is definitely the case that the user is typing Sanskrit
* (for example a Sanskrit stack), rather than Tibetan 
*/
	private boolean isDefinitelySanskrit;
/**
* According to the active keyboard, the value {@link
* #isDefinitelySanskrit} should be assigned by default when the
* keyboard is initialized by {@link #initKeyboard() initKeyboard} 
*/
	private boolean isDefinitelySanskrit_default;
/**
* According to the active keyboard, the value that should
* be assigned to {@link #isDefinitelySanskrit} if the
* user has initiated a stack by typing a stack key.
* For example, in the Wylie keyboard, there is a Sanskrit
* stacking key ('+'), but no Tibetan stacking key.
* Therefore, if the user is stacking with '+', this field
* should be true, since what the user is typing must
* be Sanskrit, not Tibetan.
*/
	private boolean isDefinitelySanskrit_withStackKey;
/**
* True iff consonant stacking is allowed at the moment. In the Wylie
* keyboard, consonant stacking is usually on, since stacking
* is automatic. However, in the TCC and Sambhota keyboards,
* stacking is off by default, since you can only stack when
* you've pressed a stacking key.
*/
	private boolean isStackingOn;
/**
* True iff, according to the active keyboard, stacking is on by
* default assuming no stack key has been pressed.
*/
	private boolean isStackingOn_default;
/**
* Automatic stacking in Wylie is from right to left. For
* example, if the user types 'brg', the resulting glyph
* sequence is 'b' plus 'rg', not 'br' plus 'g'. If
* stacking results from the use of a stack key,
* it is from left to right.
*/
	private boolean isStackingRightToLeft;
/**
* used for tracking changes in Wylie to TMW conversion
*/
	private int lastStart;
/**
* true iff the user is in Tibetan typing mode.  This is true
* by default.
*/
	private boolean isTibetan = true;
/**
* true iff the user is allowed to type non-Tibetan.  This is true
* by default
*/
	private boolean isRomanEnabled = true;
/**
* The caret of {@link #doc}, used to keep track of the
* current entry/edit/deletion position.
*/
	private Caret caret;
	private Style rootStyle;
	private boolean skipUpdate = false;
	private boolean isCutAndPasteEnabled = true;

	private String romanFontFamily;
	private int romanFontSize;
	private MutableAttributeSet romanAttributeSet;

    private Clipboard rtfBoard;
    
    private Hashtable actions;


    /** Initializes this object.  All constructors should call
        this. */
    private void initialize(StatusBar sb,
                            TibetanKeyboard keyboard,
                            java.net.URL keyboardURL)
    {
		if (null != keyboard)
            TibetanMachineWeb.setKeyboard(keyboard);
		if (null != keyboardURL)
            TibetanMachineWeb.setKeyboard(keyboardURL);
		setupKeyboard();
		setupEditor();
        if (null != sb)
            setStatusBar(sb);
//		this(new StyledEditorKit(), keyboardURL);
    }

    /** Creates a new DuffPane that updates sb, if sb is not null,
        with messages about how the users' keypresses are being
        interpreted. */
	public DuffPane(StatusBar sb) {
        super();
        initialize(sb, null, null);
    }

	public DuffPane() {
        super();
        initialize(null, null, null);
	}

	public DuffPane(TibetanKeyboard keyboard) {
        super();
        initialize(null, keyboard, null);
	}

	public DuffPane(java.net.URL keyboardURL) {
        super();
        initialize(null, null, keyboardURL);
	}

    /** Sets the status bar to update with mode information.  If sb is
        null, no status bar will be updated. */
    public void setStatusBar(StatusBar sb) {
        statBar = sb;
    }

    /** If we have a status bar, update it. */
    protected void updateStatus(String newStatus) {
        if (statBar != null) {
            /* If we've seen this message just before, append " x2" to
               the end of it. The third message will cause a toggle,
               which you can tell is different, so that's fine. */
            if (newStatus.equals(statBar.currentStatus())) {
                newStatus = newStatus + " x2";
            }
            statBar.replaceStatus((isTopHypothesis ? "Guess: " : "Fact: ")
                                  + "[holding \"" + holdCurrent.toString() + "\"] "
                                  + newStatus);
        }
    }

    /** If we have a status bar, append msg to its current status. */
    protected void appendStatus(String msg) {
        if (statBar != null) {
            statBar.replaceStatus(statBar.currentStatus() + msg);
        }
    }

    private static int defaultTibFontSize() {
        // FIXME: at program exit, or when the user selects "Save
        // preferences", or somehow, save the value the users chooses:
        return ThdlOptions.getIntegerOption("thdl.default.tibetan.font.size",
                                            36);
    }

    private static int defaultRomanFontSize() {
        // FIXME: at program exit, or when the user selects "Save
        // preferences", or somehow, save the value the users chooses:
        return ThdlOptions.getIntegerOption("thdl.default.roman.font.size",
                                            14);
    }

/**
* This method sets up the editor, assigns fonts and point sizes,
* sets the document, the caret, and adds key and focus listeners.
*/
	private void setupEditor() {
		rtfBoard = getToolkit().getSystemClipboard();

        newDocument();

		romanFontFamily = ThdlOptions.getStringOption("thdl.default.roman.font.face",
                                                      "Serif"); // FIXME write out this preference.
		romanFontSize = defaultRomanFontSize();
		setRomanAttributeSet(romanFontFamily, romanFontSize);

		caret = getCaret();

		addKeyListener(this);
		addFocusListener(this);
		
		setupKeymap(); 
	}

/**
* This method sets up the keymap used by DuffPane editors.
* The keymap defines a default behavior for key presses
* in both Tibetan and Roman mode.
*/
	private void setupKeymap() {
		Action defaultAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (!DuffPane.this.isEditable()) return;
				if (	((e.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK) || 
						((e.getModifiers() & ActionEvent.ALT_MASK) == ActionEvent.ALT_MASK) ||
						((e.getModifiers() & ActionEvent.META_MASK) == ActionEvent.META_MASK)) {
							DuffPane.this.initKeyboard();
							return;
				}
				if (e.getActionCommand() != null) { 
					String key = e.getActionCommand();
					if (DuffPane.this.getSelectionStart() < DuffPane.this.getSelectionEnd())
						DuffPane.this.replaceSelection("");
					if (key != null) {
						if (isTibetan) processTibetanChar(key.charAt(0), true);
						else {
							MutableAttributeSet inputAtts = DuffPane.this.getInputAttributes();
							inputAtts.addAttributes(romanAttributeSet);
							processRomanChar(key, inputAtts);
						}
					}
				}
			}
		};
		createActionTable(this);
		Keymap keymap = addKeymap("DuffBindings", getKeymap());				
		keymap.setDefaultAction(defaultAction);
		setKeymap(keymap);
	}
	
	private void createActionTable(JTextComponent textComponent) {
	    actions = new Hashtable();

	    Action[] actionsArray = textComponent.getActions();

	    for (int i = 0; i < actionsArray.length; i++) {

		Action a = actionsArray[i];

		actions.put(a.getValue(Action.NAME), a);

		//System.out.println(a.getValue(Action.NAME));

	    }

	}

	

	private Action getActionByName(String name) {

	    return (Action)(actions.get(name));

	}
	
/**
* This method sets up the Tibetan keyboard. Initially it is called
* by the constructor, but it is also called internally whenever
* the active keyboard is changed. It first sets various values with
* respect to stacking, and concerning the differences between
* Tibetan and Sanskrit; and then it initializes the input method.
*/
	public void setupKeyboard() {
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
		charList = new ArrayList();
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
		registerKeyboard((TibetanKeyboard)null);
	}

/**
* Registers a keyboard, and sets it as the active keyboard.
* Unpredictable behavior will result if you set the keyboard in {@link
* org.thdl.tib.text.TibetanMachineWeb TibetanMachineWeb} but don't
* register it in here.
* @param keyboardURL the URL of the keyboard you want to install */
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
		StyleContext styleContext = new StyleContext();

		doc = new TibetanDocument(styleContext);
		doc.setTibetanFontSize(defaultTibFontSize());

        // Something about the order in which you do this step
        // relative to the others in this method matters.  You'll get
        // a small cursor, though most everything else will be normal,
        // if you call setDocument(doc) at the end of this method.
		setDocument(doc);

		Style defaultStyle = styleContext.getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setFontFamily(defaultStyle, "TibetanMachineWeb");
		StyleConstants.setFontSize(defaultStyle, defaultTibFontSize());
	}

/**
* Initializes the keyboard input interpreter, setting all properties
* back to their default states. This method is called whenever an action
* or function key is pressed, and also whenever the user types punctuation
* or a vowel. It is not called when the user is typing characters that
* could be part of a stack, or require manipulation of glyphs - e.g.
* backspacing, redrawing, etc.
*/
	private void initKeyboard() {
        updateStatus("Jskad is in its basic input mode");
		charList.clear();
		oldGlyphList.clear();
		holdCurrent = new StringBuffer();
		isTopHypothesis = false;
		isTypingVowel = false;

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
			getTibDoc().insertString(offset, s, attr);
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
			getTibDoc().setTibetanFontSize(size);
	}

    /**
     * Like {@link #setTibetanFontSize(int)}, but should be called only
     * when the user has somewhat explicitly chosen the font size.
     * This will set the font size but also record this as a user
     * preference.  Then you can choose to save the user preferences
     * via {@link org.thdl.util.ThdlOptions#saveUserPreferences()}.
     *
     * @param size a point size
     */
	public void setByUserTibetanFontSize(int size) {
        ThdlOptions.setUserPreference("thdl.default.tibetan.font.size", size);
        setTibetanFontSize(size);
	}

/**
* Gets the current point size for Tibetan text.
* @return the current default font size for Tibetan
* text entry mode
*/
	public int getTibetanFontSize() {
		return getTibDoc().getTibetanFontSize();
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

    /** Like {@link #setRomanAttributeSet}, but allows for noting the
     *  (explicit or implicit) choice in the user's preferences
     *  file. */
    public void setByUserRomanAttributeSet(String font, int size) {
        ThdlOptions.setUserPreference("thdl.default.roman.font.face", font);
        ThdlOptions.setUserPreference("thdl.default.roman.font.size", size);
        setRomanAttributeSet(font, size);
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
			getTibDoc().remove(caret.getDot()-k, k);
		}
		catch (BadLocationException ble) {
		}
	}

/**
* Takes an old glyph list, which should be the currently visible set
* of glyphs preceding the cursor, and then tries to redraw the glyphs
* in light of the newly acquired keyboard input (which led to a
* revised 'new' glyph list). For example, the old glyph list might
* contain 'l' and 'n', because the user had typed 'ln' in Extended
* Wylie mode. This is what you'd see on the screen. But assume that
* the new glyph list contains the stacked glyph 'l-ng', because the
* user has just finished typing 'lng'. This method compares the
* glyphs, then figures out whether or not backspacing is necessary,
* and draws whatever characters need to be drawn.
*
* <p> For example, suppose that oldGlyphList contains the two glyphs
* 'l' and 'n', and newGlyphList contains a single glyph, 'lng'.  In
* this case, redrawGlyphs will be instructed to backspace over both
* 'l' and 'n', and then insert 'lng'.  */
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
		DuffData[] dd = TibTextUtils.convertGlyphs(sublist);
		getTibDoc().insertDuff(caret.getDot(), dd);
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

		AttributeSet attr = getTibDoc().getCharacterElement(caret.getDot()-1).getAttributes();
		String fontName = StyleConstants.getFontFamily(attr);
		int fontNum;

		if (0 != (fontNum = TibetanMachineWeb.getTMWFontNumber(fontName))) {
			try {
				char c2 = getTibDoc().getText(caret.getDot()-1, 1).charAt(0);
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
					attr = getTibDoc().getCharacterElement(caret.getDot()-2).getAttributes();
					fontName = StyleConstants.getFontFamily(attr);
					if (0 != (fontNum = TibetanMachineWeb.getTMWFontNumber(fontName))) {
						c2 = getTibDoc().getText(caret.getDot()-2, 1).charAt(0);
						dc_1 = new DuffCode(fontNum, c2);
					}
				}

				java.util.List before_vowel = new ArrayList();
				if (null != dc_1)
					before_vowel.add(dc_1);

				before_vowel.add(dc_2);
				java.util.List after_vowel = TibTextUtils.getVowel(dc_1, dc_2, v);
                if (after_vowel.size() >= before_vowel.size()) {
                    setNumberOfGlyphsForLastVowel(after_vowel.size()
                                                  - before_vowel.size());
                } else {
                    setNumberOfGlyphsForLastVowel(0);
                    ThdlDebug.noteIffyCode(); // I don't think this can ever happen, but...
                }
				redrawGlyphs(before_vowel, after_vowel);
			}
			catch(BadLocationException ble) {
				System.out.println("no--can't insert here");
                ThdlDebug.noteIffyCode();
			}
		}
		else { //0 font means not Tibetan font, so begin new Tibetan font section
			if (!TibetanMachineWeb.isAChenRequiredBeforeVowel())
				printAChenWithVowel(v);
		}
	}


/**
* If the character last displayed was a vowel, this is how many glyphs
* the vowel was composed of.  (Some vowels, such as Wylie 'I', consist
* of two glyphs.)  We use the getter and setter for this variable and
* never the variable itself. */
	private int numberOfGlyphsForLastVowel;
    private int getNumberOfGlyphsForLastVowel() {
        return numberOfGlyphsForLastVowel;
    }
    private void setNumberOfGlyphsForLastVowel(int x) {
        numberOfGlyphsForLastVowel = x;
    }

/**
* Prints ACHEN together with the vowel v. When using the Wylie
* keyboard, or any other keyboard in which {@link org.thdl.tib.text.TibetanMachineWeb#isAChenRequiredBeforeVowel() isAChenRequiredBeforeVowel()}
* is false, this method is called frequently. 
*
* @param v the vowel (in Wylie) which you want to print with ACHEN
*/
	private void printAChenWithVowel(String v) {
		DuffCode[] dc_array = (DuffCode[])TibetanMachineWeb.getTibHash().get(TibetanMachineWeb.ACHEN);
		DuffCode dc = dc_array[TibetanMachineWeb.TMW];
		java.util.List achenlist = TibTextUtils.getVowel(dc,v);
		DuffData[] dd = TibTextUtils.convertGlyphs(achenlist);
		getTibDoc().insertDuff(caret.getDot(), dd);		
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

			AttributeSet attr = getTibDoc().getCharacterElement(caret.getDot()-1).getAttributes();
			String fontName = StyleConstants.getFontFamily(attr);
			int fontNum;

			if (0 == (fontNum = TibetanMachineWeb.getTMWFontNumber(fontName)))
				break special_bindu_block;

			try {
				char c2 = getTibDoc().getText(caret.getDot()-1, 1).charAt(0);
				int k = (int)c2;
				if (k<32 || k>126) //if previous character is formatting or some other non-character
					break special_bindu_block;

				String wylie = TibetanMachineWeb.getWylieForGlyph(fontNum, k);
				if (!TibetanMachineWeb.isWylieVowel(wylie))
					break special_bindu_block;

				DuffCode dc = new DuffCode(fontNum, c2);
				java.util.List beforecaret = new ArrayList();
				beforecaret.add(dc);
				java.util.List bindulist = TibTextUtils.getBindu(dc);
				redrawGlyphs(beforecaret, bindulist);
				initKeyboard();
				return;
			}
			catch(BadLocationException ble) {
				System.out.println("no--can't do this bindu maneuver");
                ThdlDebug.noteIffyCode();
			}
		}

		DuffData[] dd = TibTextUtils.convertGlyphs(TibTextUtils.getBindu(null));
		getTibDoc().insertDuff(caret.getDot(), dd);
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
* this method resets the keyboard.
*
* @param e a FocusEvent
*/
	public void focusLost(FocusEvent e) {
		initKeyboard();
		setNumberOfGlyphsForLastVowel(0);
        appendStatus(" (because the window focus was lost)");
	}

    // FIXMEDOC
class RTFSelection implements ClipboardOwner, Transferable {
	private DataFlavor[] supportedFlavor;
	private ByteArrayOutputStream rtfOut;
	private String plainText;

	// FIXMEDOC
	RTFSelection(StyledDocument sdoc, int offset, int length) {
		supportedFlavor = new DataFlavor[2];
		supportedFlavor[0] = rtfFlavor;
		supportedFlavor[1] = DataFlavor.stringFlavor;
		try {
			//construct new document that contains only portion of text you want to copy
			//this workaround is due to bug 4129911, which will not be fixed (see below after source code)
			StyledDocument newDoc = new DefaultStyledDocument();
			for (int i=offset; i<offset+length; i++) {
				try {
					String s = sdoc.getText(i,1);
					AttributeSet as = sdoc.getCharacterElement(i).getAttributes();
					newDoc.insertString(i-offset, s, as);
				} catch (BadLocationException ble) {
					ble.printStackTrace();
					ThdlDebug.noteIffyCode();
				}
			}
			rtfOut = new ByteArrayOutputStream();
			rtfEd.write(rtfOut, newDoc, 0, newDoc.getLength()); //last two parameters ignored, see bug below
			plainText = getText(offset, length);
		} catch (BadLocationException ble) {
			ble.printStackTrace();
			ThdlDebug.noteIffyCode();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			ThdlDebug.noteIffyCode();
		}
	}
	// FIXMEDOC
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
	}
	// FIXMEDOC
	public Object getTransferData(DataFlavor flavor) {
		if (flavor.equals(rtfFlavor))
			return new ByteArrayInputStream(rtfOut.toByteArray());
		if (flavor.equals(DataFlavor.stringFlavor))
			return plainText;
		return null;
	}
	// FIXMEDOC
	public DataFlavor[] getTransferDataFlavors() {
		return supportedFlavor; 
	}
	// FIXMEDOC
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		for (int i=0; i<supportedFlavor.length; i++)
			if (flavor.equals(supportedFlavor[i]))
				return true;
		return false;
	}
} // class RTFSelection

    /** Copies the current selection to the system clipboard, unless
        cut-and-paste operations are disabled. */
	public void copy() {
        copy(getSelectionStart(), getSelectionEnd(), false);
    }

    /** If this.isEditable(), then this copies the current selection
        to the system clipboard and then deletes it. */
	public void cut() {
        copy(getSelectionStart(), getSelectionEnd(), true);
    }

    /** If this.isEditable(), then this deletes the current selection. */
	public void deleteCurrentSelection()
	{
        int start=getSelectionStart(), end=getSelectionEnd();
        
        if (!this.isEditable())
            return;
        try
        {
            getDocument().remove(start, end-start);
        } catch (BadLocationException ble) {
            ble.printStackTrace();
            ThdlDebug.noteIffyCode();
        }
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
	private void copy(int start, int end, boolean remove) {
        updateStatus("Copied to clipboard");
		int p1 = start;
		int p2 = end;
		if (p1 != p2) {
			RTFSelection rtfSelection = new RTFSelection((StyledDocument)getDocument(), p1, p2-p1);
			try {
				rtfBoard.setContents(rtfSelection, rtfSelection);
			} catch (IllegalStateException ise) {
				ise.printStackTrace();
				ThdlDebug.noteIffyCode();
			}
		}
		if (remove) {
            // Respect setEditable(boolean):
            if (!this.isEditable())
                return;

			try {
				getDocument().remove(p1, p2-p1);
			} catch (BadLocationException ble) {
				ble.printStackTrace();
				ThdlDebug.noteIffyCode();
			}
        }
	}

public void paste() {
	paste(getSelectionStart());
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
    // Respect setEditable(boolean):
    if (!this.isEditable())
        return;
                
	try {
		Transferable contents = rtfBoard.getContents(this);

/*
		if (!isRomanEnabled) {
			if (contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				String data = (String)contents.getTransferData(DataFlavor.stringFlavor);
				toTibetanMachineWeb(data, offset);
			}
		} else if (contents.isDataFlavorSupported(rtfFlavor)) {
*/

		if (contents.isDataFlavorSupported(rtfFlavor)){

			InputStream in = (InputStream)contents.getTransferData(rtfFlavor);
			int p1 = offset;

			//construct new document that contains only portion of text you want to paste
			StyledDocument sd = new DefaultStyledDocument();

            // I swear this happened once when I pasted in some
            // random junk just after Jskad started up.
            ThdlDebug.verify(null != in);

			rtfEd.read(in, sd, 0);
			
			/** Added by AM, to fix copy-paste issues for Translation Tool.
			    Assumes that if roman is disabled and you are pasting something
			    in RTF but is not TibetanMachineWeb it most be wylie.
			*/
			if (!sd.getFont((sd.getCharacterElement(0).getAttributes())).getFamily().equals("TibetanMachineWeb") && !isRomanEnabled && contents.isDataFlavorSupported(DataFlavor.stringFlavor))
			{
				String data = (String)contents.getTransferData(DataFlavor.stringFlavor);
				toTibetanMachineWeb(data, offset);			    
			}
			else
			{
			    for (int i=0; i<sd.getLength()-1; i++) { //getLength()-1 so that final newline is not included in paste
				    try {
					    String s = sd.getText(i,1);
					    AttributeSet as = sd.getCharacterElement(i).getAttributes();
					    getTibDoc().insertString(p1+i, s, as);
				    } catch (BadLocationException ble) {
					    ble.printStackTrace();
					    ThdlDebug.noteIffyCode();
				    }
			    }
			}
		} else if (contents.isDataFlavorSupported(DataFlavor.stringFlavor))
		{
		    if (!isRomanEnabled) {
				String data = (String)contents.getTransferData(DataFlavor.stringFlavor);
				toTibetanMachineWeb(data, offset);
            }
            else
            {
			    String s = (String)contents.getTransferData(DataFlavor.stringFlavor);
			    replaceSelection(s);
			}
		}
	} catch (UnsupportedFlavorException ufe) {
		ufe.printStackTrace();
		ThdlDebug.noteIffyCode();
	} catch (IOException ioe) {
		ioe.printStackTrace();
		ThdlDebug.noteIffyCode();
	} catch (IllegalStateException ise) {
		ise.printStackTrace();
		ThdlDebug.noteIffyCode();
	} catch (BadLocationException ble) {
		ble.printStackTrace();
		ThdlDebug.noteIffyCode();
	}
}

/**
* Enables cutting and pasting of Tibetan text.
*/
	public void enableCutAndPaste() {
		isCutAndPasteEnabled = true;
	}

    /** Returns true iff cut-and-paste operations are enabled. */
	public boolean isCutAndPasteOn() {
		return isCutAndPasteEnabled;
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
* Required by contract with the KeyListener interface,
* this method initializes the keyboard
* when an action key is pressed. It leaves the
* behavior up to the Keymap of this DuffPane.
*/
	public void keyPressed(KeyEvent e) {
        // FIXME: exceptions thrown here do not cause the program to fail, even in development mode.

		if (e.isActionKey())
			initKeyboard();
	}


/**
* Required by contract with the KeyListener interface,
* this method does nothing.
*
* @param e a KeyEvent
*/
	public void keyTyped(KeyEvent e) { }
	

/**
* Required by contract with the Key Listener interface,
* this method initializes the keyboard if the user presses
* backspace.
*
* @param e the KeyEvent
*/
	public void keyReleased(KeyEvent e) {
        // FIXME: exceptions thrown here do not cause the program to fail, even in development mode.

		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
			initKeyboard();
	}

	private void processRomanChar(String key, MutableAttributeSet attSet) {
		switch (key.charAt(0)) {
			case KeyEvent.VK_BACK_SPACE:
				break;
			default:
				append(key, attSet);
		}
	}

    /** Utility method called by DuffPane's Keymap as the 
     	default behavior when the user is in Tibetan typing mode.
        @param c the character the user entered in whatever keyboard
        is in use
        @param shouldIBackSpace false iff a press of the backspace key
        should not backspace, such as when you've selected some text
        and then pressed backspace ACTUALLY I DONT KNOW IF THIS IS 
        NECESSARY ANYMORE SINCE BACKSPACE IS NOW HANDLED BY THE 
        DEFAULT KEYMAP FOR JTEXTCOMPONENT */
	private void processTibetanChar(char c, boolean shouldIBackSpace) {

        // Have we modified the status bar?
		boolean changedStatus = false;

		key_block: 
		{

		if (TibetanMachineWeb.hasDisambiguatingKey())
			if (c == TibetanMachineWeb.getDisambiguatingKey()) {
				initKeyboard();
                changedStatus = true;
                appendStatus(" (because you pressed the disambiguating key)");
				break key_block;
			};

		if (TibetanMachineWeb.hasSanskritStackingKey() || TibetanMachineWeb.hasTibetanStackingKey()) {
			if (c == TibetanMachineWeb.getStackingKey()) {
				if (TibetanMachineWeb.isStackingMedial()) {
					int size = charList.size();

					if (size == 0) {
						initKeyboard();
                        changedStatus = true;
                        appendStatus(" (because you pressed the stacking key with nothing to stack on)");
					} else if (size > 1 && isStackingRightToLeft) {
						String s = (String)charList.remove(charList.size() - 1);
						newGlyphList = TibTextUtils.getGlyphs(charList, isStackingRightToLeft, isDefinitelyTibetan, isDefinitelySanskrit);
						oldGlyphList = redrawGlyphs(oldGlyphList, newGlyphList);
						initKeyboard();
						charList.add(s);
						newGlyphList = TibTextUtils.getGlyphs(charList, isStackingRightToLeft, isDefinitelyTibetan, isDefinitelySanskrit);
						oldGlyphList = redrawGlyphs(oldGlyphList, newGlyphList);
						holdCurrent = new StringBuffer();
						isTopHypothesis = false;
						isStackingOn = true;
						isStackingRightToLeft = false;
						isDefinitelyTibetan = isDefinitelyTibetan_withStackKey;
						isDefinitelySanskrit = isDefinitelySanskrit_withStackKey;
                        changedStatus = true;
                        updateStatus("You have stacked a letter atop another.");
					} else {
						holdCurrent = new StringBuffer();
						isTopHypothesis = false;
						isStackingOn = true;
						isStackingRightToLeft = false;
						isDefinitelyTibetan = isDefinitelyTibetan_withStackKey;
						isDefinitelySanskrit = isDefinitelySanskrit_withStackKey;
                        changedStatus = true;
                        updateStatus("Some sort of stack-fu is happening/has happened.");
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
                        changedStatus = true;
                        updateStatus("!isStackingOn || (isStackingOn && isDefinitelyTibetan==isDefinitelyTibetan_default)");
					}
					else {try {
						char ch = getTibDoc().getText(caret.getDot()-1, 1).charAt(0);
						AttributeSet attr = getTibDoc().getCharacterElement(caret.getDot()-1).getAttributes();
						String fontName = StyleConstants.getFontFamily(attr);
						int fontNum = TibetanMachineWeb.getTMWFontNumber(fontName);

						if (0 == fontNum) {
							initKeyboard();
							isStackingOn = true;
							isStackingRightToLeft = false;
							isDefinitelyTibetan = isDefinitelyTibetan_withStackKey;
							isDefinitelySanskrit = isDefinitelySanskrit_withStackKey;
                            changedStatus = true;
                            appendStatus(" (because 0 == fontNum)");
						} else {
							initKeyboard();
							DuffCode dc = new DuffCode(fontNum, ch);

							if (!TibetanMachineWeb.isStack(dc) && !TibetanMachineWeb.isSanskritStack(dc)) {
								isStackingOn = true;
								isStackingRightToLeft = false;
								isDefinitelyTibetan = isDefinitelyTibetan_withStackKey;
								isDefinitelySanskrit = isDefinitelySanskrit_withStackKey;
							}
                            changedStatus = true;
                            appendStatus(" (because 0 != fontNum)");
						}
					}
					catch (BadLocationException ble) {
						initKeyboard();
                        changedStatus = true;
                        appendStatus(" (because a BadLocationException was thrown)");
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
                changedStatus = true;
                appendStatus(" (because you typed enter, tab, or escape)");
				break;

			default:
				String val = String.valueOf(c);

				if (TibetanMachineWeb.isPunc(val)) { //punctuation
					val = TibetanMachineWeb.getWylieForPunc(val);

					if (val.charAt(0) == TibetanMachineWeb.BINDU)
						putBindu();

					else {
						DuffCode puncDc = TibetanMachineWeb.getGlyph(val);
						MutableAttributeSet mas = TibetanMachineWeb.getAttributeSet(puncDc.getFontNum());
						getTibDoc().appendDuff(caret.getDot(), String.valueOf(puncDc.getCharacter()), mas);
					}

					initKeyboard();
                    changedStatus = true;
                    appendStatus(" (because you typed punctuation)");
					break key_block; /* DLC is this right? */
				}

				if (charList.size()==0) { //add current character to charList if possible
					holdCurrent.append(c);
					String s = holdCurrent.toString();

					if (TibetanMachineWeb.isVowel(s)) {
						s = TibetanMachineWeb.getWylieForVowel(s);

						if (isTypingVowel) {
                            //note: this takes care of multiple keystroke vowels like 'ai'
							backSpace(getNumberOfGlyphsForLastVowel());
                        }

						putVowel(s);
						isTypingVowel = true;
                        changedStatus = true;
                        updateStatus("You typed a vowel (the simple way).");
					} else {
						if (isTypingVowel) {
							isTypingVowel = false;
							s = String.valueOf(c);
							holdCurrent = new StringBuffer(s);
						}

						if (TibetanMachineWeb.isVowel(s)) {
							s = TibetanMachineWeb.getWylieForVowel(s);
							putVowel(s);
							isTypingVowel = true;
                            changedStatus = true;
                            updateStatus("You typed a vowel (the other way).");
						} else if (TibetanMachineWeb.isChar(s)) {
							s = TibetanMachineWeb.getWylieForChar(s);
							charList.add(s);
							isTopHypothesis = true;
							newGlyphList = TibTextUtils.getGlyphs(charList, isStackingRightToLeft, isDefinitelyTibetan, isDefinitelySanskrit);
							oldGlyphList = redrawGlyphs(oldGlyphList, newGlyphList);
                            changedStatus = true;
                            updateStatus("You typed a non-vowel, Tibetan character.");
						} else {
                            if (TibetanMachineWeb.hasInputPrefix(s)) {
                                isTopHypothesis = false;
                                changedStatus = true;
                                updateStatus("incomplete input (like the \"S\" in Extended Wylie's \"Sh\")");
                            } else {
                                // before we call initKeyboard, get info to prevent infinite loops:
                                StringBuffer prevHoldCurrent = new StringBuffer(holdCurrent.toString());

                                // FIXME: ring a bell here so the user knows what's up.
                                initKeyboard();
                                
                                // The recursive call to
                                // processTibetanChar will update the
                                // status bar, so set this to true:
                                changedStatus = true;

                                // This status message is bound to get
                                // overridden, but this is what we
                                // would say if we had a queue of
                                // messages or something like Emacs's
                                // M-x view-lossage to see a history
                                // of status messages:
                                appendStatus(" (because you typed something invalid [1st way])");

                                if (prevHoldCurrent.length() != 0
                                    && !prevHoldCurrent.toString().equals(String.valueOf(c))) {
                                    processTibetanChar(c, false);
                                }
                                // else we'd go into an infinite loop
                            }
                        }
					}
				} else { //there is already a character in charList
					holdCurrent.append(c);
					String s = holdCurrent.toString();

					if (TibetanMachineWeb.isVowel(s)) { //the holding string is a vowel
						s = TibetanMachineWeb.getWylieForVowel(s);
						initKeyboard();
						isTypingVowel = true;
						putVowel(s);
                        changedStatus = true;
                        updateStatus("You typed another vowel, so the first vowel was discarded.");
					} else if (TibetanMachineWeb.isChar(s)) { //the holding string is a character
						String s2 = TibetanMachineWeb.getWylieForChar(s);

						if (isTopHypothesis) {
							if (TibetanMachineWeb.isAChungConsonant() && isStackingOn && charList.size()>1 && s2.equals(TibetanMachineWeb.ACHUNG)) {
								charList.remove(charList.size() - 1);
								newGlyphList = TibTextUtils.getGlyphs(charList, isStackingRightToLeft, isDefinitelyTibetan, isDefinitelySanskrit);
								oldGlyphList = redrawGlyphs(oldGlyphList, newGlyphList);
								putVowel(TibetanMachineWeb.A_VOWEL);
								initKeyboard();
                                changedStatus = true;
                                appendStatus(" (because we put a vowel and there's some achung stuff happening)");
								break key_block;
							}
							charList.set(charList.size()-1, s2);
							newGlyphList = TibTextUtils.getGlyphs(charList, isStackingRightToLeft, isDefinitelyTibetan, isDefinitelySanskrit);
							oldGlyphList = redrawGlyphs(oldGlyphList, newGlyphList);
                            changedStatus = true;
                            updateStatus("we were holding a hypothesis, and we've updated it");
						} else {
							if (!isStackingOn) {
								initKeyboard();
								holdCurrent = new StringBuffer(s);
                                changedStatus = true;
                                appendStatus(" (because you weren't stacking, and there was a character already)");
							} else if (TibetanMachineWeb.isAChungConsonant() && s2.equals(TibetanMachineWeb.ACHUNG)) {
								putVowel(TibetanMachineWeb.A_VOWEL);
								initKeyboard();
                                changedStatus = true;
                                appendStatus(" (because you were stacking, and we put a vowel, and there was some achung business)");
								break key_block;
							}

							charList.add(s2);
							isTopHypothesis = true;
							newGlyphList = TibTextUtils.getGlyphs(charList, isStackingRightToLeft, isDefinitelyTibetan, isDefinitelySanskrit);
							oldGlyphList = redrawGlyphs(oldGlyphList, newGlyphList);
                            changedStatus = true;
                            updateStatus("we weren't holding a hypothesis, but we are now");
						}
					} else { //the holding string is not a character
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
                                changedStatus = true;
                                appendStatus(" (because we put a vowel and the previous...)");
							} else {
								if (TibetanMachineWeb.isStackingMedial() && !isStackingRightToLeft) {
									initKeyboard();
                                    changedStatus = true;
                                    appendStatus(" (because TibetanMachineWeb.isStackingMedial() && !isStackingRightToLeft)");
                                }

								if (TibetanMachineWeb.isChar(s)) {
									String s2 = TibetanMachineWeb.getWylieForChar(s);

									if (!isStackingOn) {
										initKeyboard();
									} else if (TibetanMachineWeb.isAChungConsonant() && s2.equals(TibetanMachineWeb.ACHUNG)) {
										putVowel(TibetanMachineWeb.A_VOWEL);
										initKeyboard();
                                        changedStatus = true;
                                        appendStatus(" (because we put a vowel)");
										break key_block;
									}

									charList.add(s2);
									newGlyphList = TibTextUtils.getGlyphs(charList, isStackingRightToLeft, isDefinitelyTibetan, isDefinitelySanskrit);
									oldGlyphList = redrawGlyphs(oldGlyphList, newGlyphList);
                                    changedStatus = true;
                                    updateStatus("added character to charList");
								} else {
									holdCurrent = new StringBuffer(s);
									isTopHypothesis = false;
                                    changedStatus = true;
                                    // FIXME: ring a bell here so the user knows what's up.
                                    updateStatus("semireset (holdCurrent was reset) because you typed something invalid");
								}
							}
						} else {
                            // top char is just a guess!  Just keep c
                            // in holdCurrent if it may become valid
                            // input, or reset if we've no hope.

                            if (TibetanMachineWeb.hasInputPrefix(s)) {
                                isTopHypothesis = false;
                                changedStatus = true;
                                updateStatus("incomplete input (like the \"S\" in Extended Wylie's \"Sh\")");
                            } else {
                                // before we call initKeyboard, get info to prevent infinite loops:
                                StringBuffer prevHoldCurrent = new StringBuffer(holdCurrent.toString());

                                // FIXME: ring a bell here so the user knows what's up.
                                initKeyboard();
                                
                                // The recursive call to
                                // processTibetanChar will update the
                                // status bar, so set this to true:
                                changedStatus = true;

                                // This status message is bound to get
                                // overridden, but this is what we
                                // would say if we had a queue of
                                // messages or something like Emacs's
                                // M-x view-lossage to see a history
                                // of status messages:
                                appendStatus(" (because you typed something invalid [2nd way])");

                                if (prevHoldCurrent.length() != 0
                                    && !prevHoldCurrent.toString().equals(String.valueOf(c))) {
                                    processTibetanChar(c, false);
                                }
                                // else we'd go into an infinite loop
                            }
						}
					}
				}
		} //end switch
		} //end key_block

        if (changedStatus == false) {
            updateStatus("THAT KEY DID NOTHING BECAUSE OF THE CURRENT INPUT MODE.");
        }
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
			endPos = getTibDoc().createPosition(end);
			i = start;

			while (i < endPos.getOffset()+1) {
				attr = getTibDoc().getCharacterElement(i).getAttributes();
				fontName = StyleConstants.getFontFamily(attr);

				if ((0 == (fontNum = TibetanMachineWeb.getTMWFontNumber(fontName))) || i==endPos.getOffset()) {
					if (i != start) {
						dc_array = new DuffCode[0];
						dc_array = (DuffCode[])dcs.toArray(dc_array);
						getTibDoc().remove(start, i-start);
						append(start, TibTextUtils.getWylie(dc_array), romanAttributeSet);
						dcs.clear();
					}
					start = i+1;
				}
				else {
					ch = getTibDoc().getText(i,1).charAt(0);
					dc = new DuffCode(fontNum, ch);
					dcs.add(dc);
				}

				i++;
			}
		}
		catch (BadLocationException ble) {
			ble.printStackTrace();
			ThdlDebug.noteIffyCode();
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
			StringTokenizer sTok = new StringTokenizer(wylie, "\n\t", true); // FIXME does this work on all platforms?
			while (sTok.hasMoreTokens()) {
				String next = sTok.nextToken();
				if (next.equals("\n") || next.equals("\t")) { // FIXME does this work on all platforms?
					try {
						getTibDoc().insertString(offset, next, null);
						offset++;
					} catch (BadLocationException ble) {
						ble.printStackTrace();
						ThdlDebug.noteIffyCode();
					}
				} else {
					DuffData[] dd = TibTextUtils.getTibetanMachineWeb(next);
					offset = getTibDoc().insertDuff(offset, dd);
				}
			}
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
		Position endPos;
		int i;

		try {
			sb = new StringBuffer();
			endPos = getTibDoc().createPosition(end);
			i = start;

			while (i < endPos.getOffset()+1) {
				attr = getTibDoc().getCharacterElement(i).getAttributes();
				fontName = StyleConstants.getFontFamily(attr);

				if ((0 != TibetanMachineWeb.getTMWFontNumber(fontName)) || i==endPos.getOffset()) {
					if (i != start) {
						try {
							DuffData[] duffdata = TibTextUtils.getTibetanMachineWeb(sb.toString());
							getTibDoc().remove(start, i-start);
							getTibDoc().insertDuff(start, duffdata);
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
					sb.append(getTibDoc().getText(i, 1));

				i++;
			}
		}
		catch (BadLocationException ble) {
			ble.printStackTrace();
			ThdlDebug.noteIffyCode();
		}
	}

/**
* Converts the entire associated document into Extended Wylie.  If the
* document consists of both Tibetan and non-Tibetan fonts, however,
* the conversion stops at the first non-Tibetan font.
* @return the string of Wylie corresponding to the associated document
* @see org.thdl.tib.text.TibetanDocument#getWylie() */
    public String getWylie() {
        return getTibDoc().getWylie();
    }

}
