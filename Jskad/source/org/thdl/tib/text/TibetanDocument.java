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
            ThdlDebug.noteIffyCode();
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
		try {
			StyleConstants.setFontSize(attr, tibetanFontSize);
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
		if (glyphs == null)
			return pos;

		MutableAttributeSet mas;
		for (int i=0; i<glyphs.length; i++) {
			mas = TibetanMachineWeb.getAttributeSet(glyphs[i].font);
			appendDuff(pos, glyphs[i].text, mas);
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
}
