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
import javax.swing.*; 
import javax.swing.text.*;
import javax.swing.text.rtf.RTFEditorKit;
import java.io.*;

import org.thdl.util.ThdlDebug;
import org.thdl.util.ThdlOptions;

/** Represents a character meant to be rendered in a certain font.
 *  @author David Chandler
 */
class CharacterInAGivenFont {
    private char character;
    private String fontName;
    public CharacterInAGivenFont(char ch, String font) {
        character = ch;
        fontName = font;
    }
    public CharacterInAGivenFont(String s, String font) {
        if (s.length() != 1)
            throw new Error("character in a given font was given a string "
                            + s + " in a given font");
        character = s.charAt(0);
        fontName = font;
    }
    public boolean equals(Object x) {
        return ((x instanceof CharacterInAGivenFont)
                && ((CharacterInAGivenFont)x).character == character
                && ((CharacterInAGivenFont)x).fontName.equals(fontName));
    }
    public int hashCode() {
        return (int)character + fontName.hashCode();
    }
    public String toString() {
        String characterRepresentation
            = "'" + (('\'' == character)
                     ? "\\'"
                     : new Character(character).toString())
            + "' [decimal " + (int)character + "]";
        if ('\n' == character)
            characterRepresentation
                = "newline [decimal " + (int)character + "]";
        if ('\r' == character)
            characterRepresentation
                = "carriage return" + (int)character + "]";
        return characterRepresentation + " in the font "
            + ((null == fontName)
               ? "_ERROR_FINDING_FONT_"
               : fontName);
    }
}

/**
* A TibetanDocument is a styled document that knows about Tibetan and
* will respect line breaks and the like.  It allows you to insert
* Tibetan also.
* @author Edward Garrett, Tibetan and Himalayan Digital Library
* @version 1.0 */
public class TibetanDocument extends DefaultStyledDocument {
	private int tibetanFontSize = 36;

    /** Creates a new TibetanDocument with default styles. */
    public TibetanDocument() { super(); }

    /** Do not use this contructor. */
    private TibetanDocument(AbstractDocument.Content c, StyleContext styles ) {
        super(c, styles);
    }

/**
* Creates a TibetanDocument.
* @param styles a StyleContext, which is simply passed on
* to DefaultStyledDocument's constructor
*/
	public TibetanDocument(StyleContext styles) {
		super(styles);
	} 

/**
* Sets the point size used by default for Tibetan text.
* @param size the point size for Tibetan text
*/
	public void setTibetanFontSize(int size) {
		tibetanFontSize = size;
	}

/**
* Gets the point size for Tibetan text.
* @return the point size used for Tibetan text
*/
	public int getTibetanFontSize() {
		return tibetanFontSize;
	}

/**
* Writes the document to an OutputStream as Rich Text Format (.rtf).
* @param out the OutputStream to write to
*/
	public void writeRTFOutputStream(OutputStream out) throws IOException {
		RTFEditorKit rtf = new RTFEditorKit();

		try {
			rtf.write(out, this, 0, getLength());
		}
		catch (BadLocationException ble) {
            throw new Error("Cannot write RTF output; [0, " + getLength() + ") constitutes a bad position.");
		}
	}

/**
* Inserts Tibetan text into the document. The font size is applied automatically,
* according to the current Tibetan font size.
* @param offset the position at which you want to insert text
* @param s the string you want to insert
* @param attr the attributes to apply, normally a particular TibetanMachineWeb font
* @see #setTibetanFontSize(int size)
*/
	public void appendDuff(int offset, String s, MutableAttributeSet attr) {
        appendDuff(tibetanFontSize, offset, s, attr);
    }

    private void appendDuff(int fontSize, int offset, String s, MutableAttributeSet attr) {
		try {
			StyleConstants.setFontSize(attr, fontSize);
			insertString(offset, s, attr);
		}
		catch (BadLocationException ble) {
            ThdlDebug.noteIffyCode();
		}
	}

/**
* Inserts a stretch of TibetanMachineWeb data into the document.
* @param glyphs the array of Tibetan data you want to insert
* @param pos the position at which you want to insert text
*/
	public int insertDuff(int pos, DuffData[] glyphs) {
        return insertDuff(tibetanFontSize, pos, glyphs, true);
	}


	/** Replacing can be more efficient than inserting and then
        removing. This replaces the glyph at position pos with glyph,
        which is interpreted as a TMW glyph if asTMW is true and a TM
        glyph otherwise.  The font size for the new glyph is
        fontSize. */
    private void replaceDuff(int fontSize, int pos,
                             DuffData glyph, boolean asTMW) {
        replaceDuffs(fontSize, pos, pos + 1, glyph.text,
                     glyph.font, asTMW);
    }

	/** Replacing can be more efficient than inserting and then
        removing. This replaces the glyphs at position [startOffset,
        endOffset) with data, which is interpreted as TMW glyphs if
        asTMW is true and as TM glyphs otherwise.  The font size for
        the new glyph is fontSize; the particular TM or TMW font is
        specified by newFontIndex, which is one-based, not
        zero-based. */
    private void replaceDuffs(int fontSize, int startOffset,
                              int endOffset, String data,
                              int newFontIndex, boolean asTMW) {
		MutableAttributeSet mas
            = ((asTMW)
               ? TibetanMachineWeb.getAttributeSet(newFontIndex)
               : TibetanMachineWeb.getAttributeSetTM(newFontIndex));
        StyleConstants.setFontSize(mas, fontSize);
		try {
            replace(startOffset, endOffset - startOffset, data, mas);
        } catch (BadLocationException ble) {
            ThdlDebug.noteIffyCode();
		}
    }

	/** Replacing can be more efficient than inserting and then
        removing. This replaces the glyphs at position [startOffset,
        endOffset) with unicode.  The font size for the new unicode is
        fontSize.  Which particular Unicode font is used depends on
        TibetanMachineWeb.getUnicodeAttributeSet().
    
        @see TibetanMachineWeb#getUnicodeAttributeSet()
    */
    private void replaceDuffsWithUnicode(int fontSize, int startOffset,
                                         int endOffset, String unicode) {
		MutableAttributeSet mas
            = TibetanMachineWeb.getUnicodeAttributeSet();
        StyleConstants.setFontSize(mas, fontSize);
		try {
            replace(startOffset, endOffset - startOffset, unicode, mas);
        } catch (BadLocationException ble) {
            throw new Error("TMW->Unicode failed because the following constitute a bad position: startOffset " + startOffset + ", endOffset " + endOffset);
		}
    }

	private int insertDuff(int fontSize, int pos, DuffData[] glyphs, boolean asTMW) {
		if (glyphs == null)
			return pos;

		MutableAttributeSet mas;
		for (int i=0; i<glyphs.length; i++) {
            mas = ((asTMW)
                   ? TibetanMachineWeb.getAttributeSet(glyphs[i].font)
                   : TibetanMachineWeb.getAttributeSetTM(glyphs[i].font));
			appendDuff(fontSize, pos, glyphs[i].text, mas);
			pos += glyphs[i].text.length();
		}
		return pos;
	}

/**
* Converts the entire document into Extended Wylie.
* If the document consists of both Tibetan and
* non-Tibetan fonts, however, the conversion stops
* at the first non-Tibetan font.
* @return the string of Wylie corresponding to this document
*/
	public String getWylie() {
		return getWylie(0, getLength());
	}

/**
* Converts a portion of the document into Extended Wylie.
* If the document consists of both Tibetan and
* non-Tibetan fonts, however, the conversion stops
* at the first non-Tibetan font.
* @param begin the beginning of the region to convert
* @param end the end of the region to convert
* @return the string of Wylie corresponding to this document
*/
	public String getWylie(int begin, int end) {
		AttributeSet attr;
		String fontName;
		int fontNum;
		DuffCode dc;
		char ch;

		if (begin >= end)
			return "";

		java.util.List dcs = new ArrayList();
		int i = begin;
		StringBuffer wylieBuffer = new StringBuffer();

		try {
			while (i < end) {
				attr = getCharacterElement(i).getAttributes();
				fontName = StyleConstants.getFontFamily(attr);

				ch = getText(i,1).charAt(0);

				//current character is formatting
				if (ch == '\n' || ch == '\t') {
					if (dcs.size() > 0) {
						DuffCode[] dc_array = new DuffCode[0];
						dc_array = (DuffCode[])dcs.toArray(dc_array);
						wylieBuffer.append(TibTextUtils.getWylie(dc_array));
						dcs.clear();
					}
					wylieBuffer.append(ch);
				}

				//current character isn't TMW
				else if ((0 == (fontNum = TibetanMachineWeb.getTMWFontNumber(fontName)))) {
					if (dcs.size() > 0) {
						DuffCode[] dc_array = new DuffCode[0];
						dc_array = (DuffCode[])dcs.toArray(dc_array);
						wylieBuffer.append(TibTextUtils.getWylie(dc_array));
						dcs.clear();
					}
				}

				//current character is convertable
				else {
					dc = new DuffCode(fontNum, ch);
					dcs.add(dc);
				}
				i++;
			}
			if (dcs.size() > 0) {
				DuffCode[] dc_array = new DuffCode[0];
				dc_array = (DuffCode[])dcs.toArray(dc_array);
				wylieBuffer.append(TibTextUtils.getWylie(dc_array));
			}
			return wylieBuffer.toString();
		}
		catch (BadLocationException ble) {
			ble.printStackTrace();
			ThdlDebug.noteIffyCode();
		}

		return "";
	}

    /** Prints to standard output a list of all the indices of
        characters that are not in a TMW font within the range [start,
        end).  Using a negative number for end means that this will
        run to the end of the document.  SPEED_FIXME: might be faster
        to run over the elements, if they are one per font.
        @return 1 if at least one non-TMW character was found in
        the specified range, zero if none were, -1 on error. */
    public int findAllNonTMWCharacters(int begin, int end) {
        return findAllNonTMWCharacters(begin, end, System.out);
    }

    /** Configurable so that System.out isn't necessarily used. */
    public int findAllNonTMWCharacters(int begin, int end, PrintStream out) {
        return findCharacters(begin, end, out, "Non-TMW", true);
    }

    /** Prints to standard output a list of all the indices of
        characters that are not in a TM font within the range [start,
        end).  Using a negative number for end means that this will
        run to the end of the document.  SPEED_FIXME: might be faster
        to run over the elements, if they are one per font.
        @return 1 if at least one non-TM character was found in
        the specified range, zero if none were, -1 on error. */
    public int findAllNonTMCharacters(int begin, int end) {
        return findAllNonTMCharacters(begin, end, System.out);
    }

    /** Configurable so that System.out isn't necessarily used. */
    public int findAllNonTMCharacters(int begin, int end, PrintStream out) {
        return findCharacters(begin, end, out, "Non-TM", true);
    }

    /** Finds the first occurrence of a non-TMW character in a given
        font and prints it to System.out.  If you have a Tahoma
        newline and an Arial newline, e.g., the first occurrence of
        each will be reported.
        
        <p>Works within the range [start, end).  Using a negative
        number for end means that this will run to the end of the
        document.  SPEED_FIXME: might be faster to run over the
        elements, if they are one per font.
        @return 1 if at least one non-TMW character was found in
        the specified range, zero if none were, -1 on error. */
    public int findSomeNonTMWCharacters(int begin, int end) {
        return findSomeNonTMWCharacters(begin, end, System.out);
    }

    /** Finds the first occurrence of a non-TM character in a given
        font and prints it to System.out.  If you have a Tahoma
        newline and an Arial newline, e.g., the first occurrence of
        each will be reported.
        
        <p>Works within the range [start, end).  Using a negative
        number for end means that this will run to the end of the
        document.  SPEED_FIXME: might be faster to run over the
        elements, if they are one per font.
        @return 1 if at least one non-TMW character was found in
        the specified range, zero if none were, -1 on error. */
    public int findSomeNonTMCharacters(int begin, int end) {
        return findSomeNonTMCharacters(begin, end, System.out);
    }

    /** Configurable so that System.out isn't necessarily used. */
    public int findSomeNonTMWCharacters(int begin, int end, PrintStream out) {
        return findCharacters(begin, end, out, "Non-TMW", false);
    }

    /** Configurable so that System.out isn't necessarily used. */
    public int findSomeNonTMCharacters(int begin, int end, PrintStream out) {
        return findCharacters(begin, end, out, "Non-TM", false);
    }

    /** Pass in whatKind=="Non-TMW" or whatKind=="Non-TM" for now; see
        callers and the code to understand the semantics.  Pass in all
        == true to find all characters or all == false to report each
        character just once. */
    private int findCharacters(int begin, int end, PrintStream out,
                               String whatKind, boolean all) {
        if (whatKind != "Non-TMW" && whatKind != "Non-TM")
            throw new IllegalArgumentException("You didn't use an interned string.");
        if (end < 0)
            end = getLength();
        if (begin >= end)
            return 0;
        int i = begin;
        int returnValue = 0;
        try {
            HashMap cgfTable = null;
            if (!all) cgfTable = new HashMap();
            while (i < end) {
                AttributeSet attr = getCharacterElement(i).getAttributes();
                String fontName = StyleConstants.getFontFamily(attr);
                if ((whatKind == "Non-TMW"
                     && (0 == TibetanMachineWeb.getTMWFontNumber(fontName)))
                    || (whatKind == "Non-TM"
                        && (0 == TibetanMachineWeb.getTMFontNumber(fontName)))) {
                    returnValue = 1;
                    CharacterInAGivenFont cgf
                        = new CharacterInAGivenFont(getText(i, 1), fontName);
                    boolean doOutput = all;
                    if (!all && !cgfTable.containsKey(cgf)) {
                        cgfTable.put(cgf, "yes this character appears once");
                        doOutput = true;
                    }
                    if (true == doOutput)
                        out.println(whatKind + " character "
                                    + cgf + " appears "
                                    + ((all) ? "" : "first ")
                                    + "at location " + i);
                }
                i++;
            }
        } catch (BadLocationException ble) {
            ble.printStackTrace(out);
            ThdlDebug.noteIffyCode();
            returnValue = -1;
        }
        return returnValue;
    }

    private static final DuffData[] leftCurlyBraceTMW
        = new DuffData[] { new DuffData("{", 1) };
    private static final DuffData[] rightCurlyBraceTMW
        = new DuffData[] { new DuffData("}", 1) };
    private static final DuffData[] backslashTMW
        = new DuffData[] { new DuffData("\\", 2) };
    /** This is a band-aid used to help Jskad fix RTF files that are
        mostly TMW but have some Tahoma characters that should be TMW.
        Replaces '{', '}', and '\\' characters with the correct
        TibetanMachineWeb.  Works within the range [start, end).
        Using a negative number for end means that this will run to
        the end of the document.  Be sure to set the size for Tibetan
        as you like it before using this (well, it usually gets it
        right on its own, but just in case).  SPEED_FIXME: might be
        faster to run over the elements, if they are one per font. */
    public void replaceTahomaCurlyBracesAndBackslashes(int begin, int end) {
        if (end < 0)
            end = getLength();
        if (begin >= end)
            return;
        int i = begin;
        try {
            while (i < end) {
                AttributeSet attr = getCharacterElement(i).getAttributes();
                String fontName = StyleConstants.getFontFamily(attr);
                if (fontName.equals("Tahoma")) {
                    DuffData[] toReplaceWith = null;
                    switch (getText(i, 1).charAt(0)) {
                    case '{':
                        toReplaceWith = leftCurlyBraceTMW;
                        break;
                    case '}':
                        toReplaceWith = rightCurlyBraceTMW;
                        break;
                    case '\\':
                        toReplaceWith = backslashTMW;
                        break;
                    }
                    if (null != toReplaceWith) {
                        // SPEED_FIXME: determining font size might be slow
                        int fontSize = tibetanFontSize;
                        try {
                            fontSize = ((Integer)getCharacterElement(i).getAttributes().getAttribute(StyleConstants.FontSize)).intValue();
                        } catch (Exception e) {
                            // leave it as tibetanFontSize
                        }
                        if (replaceInsteadOfInserting()) {
                            replaceDuff(fontSize, i, toReplaceWith[0], true);
                        } else {
                            if (insertBefore()) {
                                insertDuff(fontSize, i, toReplaceWith, true);
                                remove(i+1, 1);
                            } else {
                                insertDuff(fontSize, i+1, toReplaceWith, true);
                                remove(i, 1);
                            }
                        }
                    }
                }
                i++;
            }
        } catch (BadLocationException ble) {
            ble.printStackTrace();
            ThdlDebug.noteIffyCode();
        }
    }

    /** Converts all TibetanMachineWeb glyphs in the document to
        TibetanMachine.  Works within the range [start, end).  Using a
        negative number for end means that this will run to the end of
        the document.  Be sure to set the size for Tibetan as you like
        it before using this (well, it usually gets it right on its
        own, but just in case).  SPEED_FIXME: might be faster to run
        over the elements, if they are one per font.
        @return false on 100% success, true if any exceptional case
        was encountered
        @param errors if non-null, then notes about all exceptional
        cases will be appended to this StringBuffer
    */
    public boolean convertToTM(int begin, int end, StringBuffer errors) {
        return convertHelper(begin, end, true, false, errors);
    }

    /** Converts all TibetanMachine glyphs in the document to
        TibetanMachineWeb.  Works within the range [start, end).
        Using a negative number for end means that this will run to
        the end of the document.  Be sure to set the size for Tibetan
        as you like it before using this (well, it usually gets it
        right on its own, but just in case).  SPEED_FIXME: might be
        faster to run over the elements, if they are one per font.
        @return false on 100% success, true if any exceptional case
        was encountered
        @param errors if non-null, then notes about all exceptional
        cases will be appended to this StringBuffer
    */
    public boolean convertToTMW(int begin, int end, StringBuffer errors) {
        return convertHelper(begin, end, false, false, errors);
    }

    /** Converts all TibetanMachineWeb glyphs in the document to
        Unicode.  Works within the range [start, end).  Using a
        negative number for end means that this will run to the end of
        the document.  Be sure to set the size for Tibetan as you like
        it before using this (well, it usually gets it right on its
        own, but just in case).  SPEED_FIXME: might be faster to run
        over the elements, if they are one per font.
        @return false on 100% success, true if any exceptional case
        was encountered
        @param errors if non-null, then notes about all exceptional
        cases will be appended to this StringBuffer */
    public boolean convertToUnicode(int begin, int end, StringBuffer errors) {
        return convertHelper(begin, end, false, true, errors);
    }

    /** For debugging only.  Start with an empty document, and call
        this on it.  You'll get all the TibetanMachine glyphs
        inserted, in order, into your document. */
    private void insertAllTMGlyphs() {
        int font;
        int ord;
        DuffData[] equivalent = new DuffData[1];
        equivalent[0] = new DuffData();

        int count = 0;
        for (font = 0; font < 5; font++) {
            for (ord = 32; ord < 255; ord++) {
                if (TibetanMachineWeb.mapTMtoTMW(font, ord, 0) != null) {
                    equivalent[0].setData((char)ord, font + 1);
                    try {
                        insertDuff(tibetanFontSize, count++, equivalent, false);
                    } catch (NullPointerException e) {
                        System.err.println("nullpointerexception happened: font is " + font + " ord is " + ord);
                    }
                }
            }
        }
    }

    /** This setting determines whether the formatting is preserved,
        but with infinite loops in it, or is not preserved, but works
        well.  Inserting + removing must be used rather than replacing
        because you get the same exception otherwise.  FIXME: try Java
        1.5 -- maybe it beats Java 1.4.

     [java] javax.swing.text.StateInvariantError: infinite loop in formatting
     [java] 	at javax.swing.text.FlowView$FlowStrategy.layout(FlowView.java:404)
     [java] 	at javax.swing.text.FlowView.layout(FlowView.java:182)
     [java] 	at javax.swing.text.BoxView.setSize(BoxView.java:379)
     [java] 	at javax.swing.text.BoxView.updateChildSizes(BoxView.java:348)
     [java] 	at javax.swing.text.BoxView.setSpanOnAxis(BoxView.java:330)
     [java] 	at javax.swing.text.BoxView.layout(BoxView.java:682)
     [java] 	at javax.swing.text.BoxView.setSize(BoxView.java:379)
     [java] 	at javax.swing.plaf.basic.BasicTextUI$RootView.setSize(BasicTextUI.java:1598)
     [java] 	at javax.swing.plaf.basic.BasicTextUI.getPreferredSize(BasicTextUI.java:800)
     [java] 	at javax.swing.JComponent.getPreferredSize(JComponent.java:1272)
     [java] 	at javax.swing.JEditorPane.getPreferredSize(JEditorPane.java:1206)
     [java] 	at javax.swing.ScrollPaneLayout.layoutContainer(ScrollPaneLayout.java:769)
     [java] 	at java.awt.Container.layout(Container.java:1017)
     [java] 	at java.awt.Container.doLayout(Container.java:1007)
     [java] 	at java.awt.Container.validateTree(Container.java:1089)
     [java] 	at java.awt.Container.validate(Container.java:1064)
     [java] 	at javax.swing.RepaintManager.validateInvalidComponents(RepaintManager.java:353)
     [java] 	at javax.swing.SystemEventQueueUtilities$ComponentWorkRequest.run(SystemEventQueueUtilities.java:116)
     [java] 	at java.awt.event.InvocationEvent.dispatch(InvocationEvent.java:178)
     [java] 	at java.awt.EventQueue.dispatchEvent(EventQueue.java:448)
     [java] 	at java.awt.EventDispatchThread.pumpOneEventForHierarchy(EventDispatchThread.java:197)
     [java] 	at java.awt.EventDispatchThread.pumpEventsForHierarchy(EventDispatchThread.java:150)
     [java] 	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:144)
     [java] 	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:136)
     [java] 	at java.awt.EventDispatchThread.run(EventDispatchThread.java:99)
     [java] javax.swing.text.StateInvariantError: infinite loop in formatting
     [java] 	at javax.swing.text.FlowView$FlowStrategy.layout(FlowView.java:404)
     [java] 	at javax.swing.text.FlowView.layout(FlowView.java:182)
     [java] 	at javax.swing.text.BoxView.setSize(BoxView.java:379)
     [java] 	at javax.swing.text.BoxView.updateChildSizes(BoxView.java:348)
     [java] 	at javax.swing.text.BoxView.setSpanOnAxis(BoxView.java:316)
     [java] 	at javax.swing.text.BoxView.layout(BoxView.java:683)
     [java] 	at javax.swing.text.BoxView.setSize(BoxView.java:379)
     [java] 	at javax.swing.plaf.basic.BasicTextUI$RootView.setSize(BasicTextUI.java:1598)
     [java] 	at javax.swing.plaf.basic.BasicTextUI.getPreferredSize(BasicTextUI.java:800)
     [java] 	at javax.swing.JComponent.getPreferredSize(JComponent.java:1272)
     [java] 	at javax.swing.JEditorPane.getPreferredSize(JEditorPane.java:1206)
     [java] 	at javax.swing.ScrollPaneLayout.layoutContainer(ScrollPaneLayout.java:769)
     [java] 	at java.awt.Container.layout(Container.java:1017)
     [java] 	at java.awt.Container.doLayout(Container.java:1007)
     [java] 	at java.awt.Container.validateTree(Container.java:1089)
     [java] 	at java.awt.Container.validate(Container.java:1064)
     [java] 	at javax.swing.RepaintManager.validateInvalidComponents(RepaintManager.java:353)
     [java] 	at javax.swing.SystemEventQueueUtilities$ComponentWorkRequest.run(SystemEventQueueUtilities.java:116)
     [java] 	at java.awt.event.InvocationEvent.dispatch(InvocationEvent.java:178)
     [java] 	at java.awt.EventQueue.dispatchEvent(EventQueue.java:448)
     [java] 	at java.awt.EventDispatchThread.pumpOneEventForHierarchy(EventDispatchThread.java:197)
     [java] 	at java.awt.EventDispatchThread.pumpEventsForHierarchy(EventDispatchThread.java:150)
     [java] 	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:144)
     [java] 	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:136)
     [java] 	at java.awt.EventDispatchThread.run(EventDispatchThread.java:99)
     [java] javax.swing.text.StateInvariantError: infinite loop in formatting
     [java] 	at javax.swing.text.FlowView$FlowStrategy.layout(FlowView.java:404)
     [java] 	at javax.swing.text.FlowView.layout(FlowView.java:182)
     [java] 	at javax.swing.text.BoxView.setSize(BoxView.java:379)
     [java] 	at javax.swing.text.BoxView.updateChildSizes(BoxView.java:348)
     [java] 	at javax.swing.text.BoxView.setSpanOnAxis(BoxView.java:316)
     [java] 	at javax.swing.text.BoxView.layout(BoxView.java:683)
     [java] 	at javax.swing.text.BoxView.setSize(BoxView.java:379)
     [java] 	at javax.swing.plaf.basic.BasicTextUI$RootView.setSize(BasicTextUI.java:1598)
     [java] 	at javax.swing.plaf.basic.BasicTextUI.modelToView(BasicTextUI.java:934)
     [java] 	at javax.swing.text.DefaultCaret.repaintNewCaret(DefaultCaret.java:1044)
     [java] 	at javax.swing.text.DefaultCaret$1.run(DefaultCaret.java:1023)
     [java] 	at java.awt.event.InvocationEvent.dispatch(InvocationEvent.java:178)
     [java] 	at java.awt.EventQueue.dispatchEvent(EventQueue.java:448)
     [java] 	at java.awt.EventDispatchThread.pumpOneEventForHierarchy(EventDispatchThread.java:197)
     [java] 	at java.awt.EventDispatchThread.pumpEventsForHierarchy(EventDispatchThread.java:150)
     [java] 	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:144)
     [java] 	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:136)
     [java] 	at java.awt.EventDispatchThread.run(EventDispatchThread.java:99)
     [java] javax.swing.text.StateInvariantError: infinite loop in formatting
     [java] 	at javax.swing.text.FlowView$FlowStrategy.layout(FlowView.java:404)
     [java] 	at javax.swing.text.FlowView.layout(FlowView.java:182)
     [java] 	at javax.swing.text.BoxView.setSize(BoxView.java:379)
     [java] 	at javax.swing.text.BoxView.updateChildSizes(BoxView.java:348)
     [java] 	at javax.swing.text.BoxView.setSpanOnAxis(BoxView.java:316)
     [java] 	at javax.swing.text.BoxView.layout(BoxView.java:683)
     [java] 	at javax.swing.text.BoxView.setSize(BoxView.java:379)
     [java] 	at javax.swing.plaf.basic.BasicTextUI$RootView.setSize(BasicTextUI.java:1598)
     [java] 	at javax.swing.plaf.basic.BasicTextUI.getPreferredSize(BasicTextUI.java:800)
     [java] 	at javax.swing.JComponent.getPreferredSize(JComponent.java:1272)
     [java] 	at javax.swing.JEditorPane.getPreferredSize(JEditorPane.java:1206)
     [java] 	at javax.swing.ScrollPaneLayout.layoutContainer(ScrollPaneLayout.java:769)
     [java] 	at java.awt.Container.layout(Container.java:1017)
     [java] 	at java.awt.Container.doLayout(Container.java:1007)
     [java] 	at java.awt.Container.validateTree(Container.java:1089)
     [java] 	at java.awt.Container.validate(Container.java:1064)
     [java] 	at javax.swing.RepaintManager.validateInvalidComponents(RepaintManager.java:353)
     [java] 	at javax.swing.SystemEventQueueUtilities$ComponentWorkRequest.run(SystemEventQueueUtilities.java:116)
     [java] 	at java.awt.event.InvocationEvent.dispatch(InvocationEvent.java:178)
     [java] 	at java.awt.EventQueue.dispatchEvent(EventQueue.java:448)
     [java] 	at java.awt.EventDispatchThread.pumpOneEventForHierarchy(EventDispatchThread.java:197)
     [java] 	at java.awt.EventDispatchThread.pumpEventsForHierarchy(EventDispatchThread.java:150)
     [java] 	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:144)
     [java] 	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:136)
     [java] at java.awt.EventDispatchThread.run(EventDispatchThread.java:99) */
    private static boolean insertBefore() {
        return !ThdlOptions.getBooleanOption("thdl.insert.rtf.after.not.before");
    }
    private static boolean replaceInsteadOfInserting() {
        return !ThdlOptions.getBooleanOption("thdl.insert.and.remove.instead.of.replacing");
    }

    /** Helper function.  Converts TMW->TM if !toUnicode&&toTM,
        TM->TMW if !toUnicode&&!toTM, TMW->Unicode if toUnicode.
        @param errors if non-null, then notes about all exceptional
        cases will be appended to this StringBuffer
        @return false on 100% success, true if any exceptional case
        was encountered
        @see convertToUnicode(int,int) 
        @see convertToTMW(int,int) 
        @see convertToTM(int,int) */
    private boolean convertHelper(int begin, int end, boolean toTM,
                                  boolean toUnicode, StringBuffer errors) {
        // To preserve formatting, we go paragraph by paragraph.

        // Use positions, not offsets, because our work on paragraph K
        // will affect the offsets of paragraph K+1.

        Position finalEndPos;
        if (end < 0) {
            end = getLength();
        }
        try {
            finalEndPos = createPosition(end);
        } catch (BadLocationException e) {
            throw new Error("BAD LOCATION DURING CONVERSION");
        }

        ConversionErrorHelper ceh = new ConversionErrorHelper();
        int pl = 0;
        pl = getParagraphs(begin, end).length;
        boolean warn = false;
        int lastTimeWeExamined = -1; // must be -1
        boolean noMore = false;
        while (!noMore
               && lastTimeWeExamined != ceh.lastOffsetExamined) {
            lastTimeWeExamined = ceh.lastOffsetExamined;
            Element thisParagraph
                = getParagraphElement(lastTimeWeExamined + 1);
            int p_end = thisParagraph.getEndOffset();
            if (p_end >= finalEndPos.getOffset()) {
                noMore = true;
                ceh.doErrorWrapup = true;
            }
            convertHelperHelper(thisParagraph.getStartOffset(),
                                ((finalEndPos.getOffset() < p_end)
                                 ? finalEndPos.getOffset()
                                 : p_end),
                                toTM, toUnicode, errors, ceh);
        }
        if (!ceh.errorReturn
            && pl != getParagraphs(begin, finalEndPos.getOffset()).length) {
            System.err.println("Conversion WARNING: the number of paragraphs changed from "
                               + pl + " to " + getParagraphs(begin, end).length
                               + ", indicating that formatting may have been lost.");
            ThdlDebug.noteIffyCode();
        }

        return ceh.errorReturn;
    }

    /** See the sole caller, convertHelper. */
    private void convertHelperHelper(int begin, int end, boolean toTM,
                                     boolean toUnicode, StringBuffer errors,
                                     ConversionErrorHelper ceh) {
        final boolean debug = false;
        if (debug)
            System.err.println("cHH: [" + begin + ", " + end + ")");
        // DLC FIXME: here's an idea, a compressor -- use the '-' (ord
        // 45) or ' ' (ord 32) glyph from the same font as the
        // preceding glyph, never others.  This reduces the size of a
        // TMW RTF file by a factor of 3 sometimes.  To do it, use
        // this routine, but give it the ability to go from TMW->TMW
        // and TM->TM.

        // toTM is ignored when toUnicode is true:
        ThdlDebug.verify(!toUnicode || !toTM);

        boolean toStdout = ThdlOptions.getBooleanOption("thdl.debug");
        if (end < 0)
            end = getLength();
        if (begin >= end)
            return; // nothing to do

        // For speed, do as few replaces as possible.  To preserve
        // formatting, we'll try to replace one paragraph at a time.
        // But we *must* replace when we hit a different font (TMW3 as
        // opposed to TMW2, e.g.), so we'll likely replace many times
        // per paragraph.  One very important optimization is that we
        // don't have to treat TMW3.45 or TMW3.32 as a different font
        // than TMW.33 -- that's because each of the ten TMW fonts has
        // the same glyph at position 32 (space) and the same glyph at
        // position 45 (tsheg).  Note that we're building up a big
        // StringBuffer; we're trading space for time.
        try {
            int replacementStartIndex = begin;
            StringBuffer replacementQueue = new StringBuffer();
            int replacementFontIndex = 0;
            int replacementFontSize = -1;

            int i = begin;
            Position endPos = createPosition(end);
            DuffData[] equivalent = new DuffData[1];
            equivalent[0] = new DuffData();
            boolean mustReplace = false;
            int mustReplaceUntil = -1;
            while (i < endPos.getOffset()) {
                AttributeSet attr = getCharacterElement(i).getAttributes();
                String fontName = StyleConstants.getFontFamily(attr);
				int fontNum
                    = ((toTM || toUnicode)
                       ? TibetanMachineWeb.getTMWFontNumber(fontName)
                       : TibetanMachineWeb.getTMFontNumber(fontName));

                if (0 != fontNum) {

                    // SPEED_FIXME: determining font size might be slow, allow an override.
                    int fontSize = tibetanFontSize;
                    try {
                        fontSize = ((Integer)getCharacterElement(i).getAttributes().getAttribute(StyleConstants.FontSize)).intValue();
                    } catch (Exception e) { /* leave it as tibetanFontSize */ }

                    DuffCode dc = null;
                    String unicode = null;
                    if (toUnicode) {
                        unicode = TibetanMachineWeb.mapTMWtoUnicode(fontNum - 1,
                                                                    getText(i,1).charAt(0));
                    } else {
                        if (toTM) {
                            dc = TibetanMachineWeb.mapTMWtoTM(fontNum - 1,
                                                              getText(i,1).charAt(0),
                                                              replacementFontIndex);
                        } else {
                            dc = TibetanMachineWeb.mapTMtoTMW(fontNum - 1,
                                                              getText(i,1).charAt(0),
                                                              replacementFontIndex);
                        }
                    }
                    if (replacementQueue.length() > 0
                        && (mustReplace
                            || ((!toUnicode
                                 && null != dc
                                 && dc.getFontNum() != replacementFontIndex)
                                || fontSize != replacementFontSize))) {
                        // We must replace now, because the attribute
                        // set has changed.

                        // We have two choices: replace or
                        // insert-and-remove.  We replace, because
                        // that preserves formatting.

                        // this if-else statement is duplicated below; beware!
                        int endIndex = mustReplace ? mustReplaceUntil : i;
                        if (toUnicode) {
                            replaceDuffsWithUnicode(replacementFontSize,
                                                    replacementStartIndex,
                                                    endIndex,
                                                    replacementQueue.toString());
                        } else {
                            replaceDuffs(replacementFontSize,
                                         replacementStartIndex,
                                         endIndex,
                                         replacementQueue.toString(),
                                         replacementFontIndex,
                                         !toTM);
                        }

                        // i += numnewchars - numoldchars;
                        if (debug)
                            System.err.println("Incrementing i by " + (replacementQueue.length()
                              - (endIndex - replacementStartIndex)) + "; replaced a patch with font size " + replacementFontSize + ", fontindex " + replacementFontIndex);
                        i += (replacementQueue.length()
                              - (endIndex - replacementStartIndex));

                        replacementQueue.delete(0, replacementQueue.length());
                        mustReplace = false;
                    }

                    if (null != dc || null != unicode) {
                        if (0 == replacementQueue.length()) {
                            replacementFontSize = fontSize;
                            replacementStartIndex = i;
                            if (!toUnicode) {
                                replacementFontIndex = dc.getFontNum();
                            }
                        }
                        if (toUnicode) {
                            replacementQueue.append(unicode);
                        } else {
                            replacementQueue.append(dc.getCharacter());
                        }
                    } else {
                        // For now, on error, we insert the alphabet
                        // in a big font in TMW to try and get some
                        // attention.  We also put the oddballs at the
                        // start of the document.  But then we delete
                        // the alphabet usually.
                        
                        ceh.errorReturn = true;
                        CharacterInAGivenFont cgf
                            = new CharacterInAGivenFont(getText(i,1), fontName);
                        if (!ceh.problemGlyphsTable.containsKey(cgf)) {
                            ceh.problemGlyphsTable.put(cgf, "yes this character appears once");
                            if (null != errors) {
                                String err
                                    = (toUnicode
                                       ? "TMW->Unicode"
                                       : (toTM ? "TMW->TM" : "TM->TMW"))
                                    + " conversion failed for a glyph:\nFont is "
                                    + fontName + ", glyph number is "
                                    + (int)getText(i,1).charAt(0)
                                    + "; first position found (from zero) is "
                                    + i + "\n";
                                errors.append(err);
                                if (toStdout) {
                                    System.out.print(err);
                                }

                                // Now also put this problem glyph at
                                // the beginning of the document,
                                // after a 'a' character (i.e.,
                                // \tm0062 or \tmw0063):
                                equivalent[0].setData((toUnicode || toTM) ? (char)63 : (char)62, 1);
                                insertDuff(72, ceh.errorGlyphLocation++,
                                           equivalent, toUnicode || toTM);
                                ++i;
                                equivalent[0].setData(getText(i,1), fontNum);
                                insertDuff(72, ceh.errorGlyphLocation++,
                                           equivalent, toUnicode || toTM);
                                ++i;
                            }
                        }

                        if (ThdlOptions.getBooleanOption("thdl.leave.bad.tm.tmw.conversions.in.place")) {
                            String trickyTMW
                                = "!-\"-#-$-%-&-'-(-)-*-+-,-.-/-0-1-2-3-4-5-6-7-8-9-:-;-<-=->-?-";
                            equivalent[0].setData(trickyTMW, 1);
                            insertDuff(72, i, equivalent, true);
                            i += trickyTMW.length();
                        }
                    }
                } else {
                    if (debug) System.err.println("non-tm/tmw found at offset " + i + "; font=" + fontName + " ord " + (int)getText(i,1).charAt(0));
                    if (replacementQueue.length() > 0) {
                        if (!mustReplace) {
                            mustReplaceUntil = i;
                            mustReplace = true;
                        }
                    }
                }
                i++;
            }
            if (replacementQueue.length() > 0) {
                // this if-else statement is duplicated above; beware!
                int endIndex = mustReplace ? mustReplaceUntil : i;
                if (toUnicode) {
                    replaceDuffsWithUnicode(replacementFontSize,
                                            replacementStartIndex,
                                            endIndex,
                                            replacementQueue.toString());
                } else {
                    replaceDuffs(replacementFontSize,
                                 replacementStartIndex,
                                 endIndex,
                                 replacementQueue.toString(),
                                 replacementFontIndex,
                                 !toTM);
                }
            }
            ceh.lastOffsetExamined = endPos.getOffset() - 1;

            if (!ThdlOptions.getBooleanOption("thdl.leave.bad.tm.tmw.conversions.in.place")) {
                // Remove all characters other than the oddballs:
                if (ceh.doErrorWrapup && ceh.errorGlyphLocation > 0) {
                    remove(ceh.errorGlyphLocation, getLength()-ceh.errorGlyphLocation-1);
                }
            }
        } catch (BadLocationException ble) {
            ble.printStackTrace();
            ThdlDebug.noteIffyCode();
        }
    }

    /** Returns all the paragraph elements in this document that
     *  contain glyphs with offsets in the range [start, end) where
     *  end < 0 is treated as the document's length.  Note that roman,
     *  TM, Arial Unicode MS, and TMW text can all be intermingled
     *  within a paragraph.  It's the correct level of abstraction to
     *  use, however, because the next finer grain is roughly one
     *  Element per glyph. */
    private Element[] getParagraphs(int start, int end) {
        if (end < 0)
            end = getLength();
        Element arrayType[] = new Element[0];
        ArrayList v = new ArrayList();
        int pos = start;
        while (pos <= end) {
            Element pe = getParagraphElement(pos);
            v.add(pe);
            int peo = pe.getEndOffset();
            if (peo == pos) {
                // Avoids an infinite loop I've run into:
                if (getParagraphElement(peo + 1).getEndOffset() > pos)
                    pos = peo + 1;
                break;
            } else
                pos = peo;
        }
        return (Element[])v.toArray(arrayType);
    }

}

/** A helper class used by TibetanDocument.convertHelper(..). */
class ConversionErrorHelper {
    boolean errorReturn;
    /** one more than the location of the last error glyph, or zero if no
     *  error glyphs yet exist */
    int errorGlyphLocation;
    boolean doErrorWrapup;
    int lastOffsetExamined;
    HashMap problemGlyphsTable;
    ConversionErrorHelper() {
        errorReturn = false;
        errorGlyphLocation = 0;
        doErrorWrapup = false;
        lastOffsetExamined = 0;
        problemGlyphsTable = new HashMap();
    }
}
