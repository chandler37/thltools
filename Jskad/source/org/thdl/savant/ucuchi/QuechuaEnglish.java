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

package org.thdl.savant.ucuchi;

import java.io.Reader;
import java.util.List;
import java.util.Iterator;
import javax.swing.JTextPane;
import javax.swing.text.*;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.thdl.savant.*;

public class QuechuaEnglish implements TranscriptView
{
	private JTextPane text = null;
	private Document xmlDoc = null;
	private StringBuffer idBuffer = null;
	private StringBuffer t1Buffer = null;
	private StringBuffer t2Buffer = null;
	private StringBuffer startBuffer = null;
	private StringBuffer endBuffer = null;

	public QuechuaEnglish(Document xml)
	{
		process(xml);
		xmlDoc = xml;
	}

	public QuechuaEnglish(Reader source)
	{
		try
		{
			SAXBuilder builder = new SAXBuilder();
			Document xml = builder.build(source);
			process(xml);
			xmlDoc = xml;
		}
		catch (JDOMException jdome)
		{
			jdome.printStackTrace();
		}
	}

	public String getTitle()
	{
		return "Quechua and English";
	}

	public void process(Document xml)
	{
		try {
			Element root = xml.getRootElement();
			List elements = root.getChildren();
			Iterator iter = elements.iterator();
			Element current = null;

			text = new JTextPane();
			javax.swing.text.Document doc = text.getDocument();

			MutableAttributeSet mas0 = new SimpleAttributeSet();
			StyleConstants.setFontSize(mas0, 16);

			MutableAttributeSet mas = new SimpleAttributeSet();
			StyleConstants.setBold(mas, true);
			StyleConstants.setUnderline(mas, true);
			StyleConstants.setFontSize(mas, 16);
			MutableAttributeSet mas2 = new SimpleAttributeSet();
			StyleConstants.setItalic(mas2, true);
			StyleConstants.setFontSize(mas2, 16);
			StyleConstants.setFontSize(mas2, 16);
			Position endPos = null;
			int wherestart;
			String wylie;

			idBuffer = new StringBuffer();
			startBuffer = new StringBuffer();
			endBuffer = new StringBuffer();
			t1Buffer = new StringBuffer();
			t2Buffer = new StringBuffer();

			String thisStart, thisEnd, thisId;

			int counter = 0;

			if (iter.hasNext())
				current = (Element)iter.next();

			while (current.getName().equals("spkr"))
			{
				wylie = current.getAttributeValue("who");
				if (endPos == null)
				{
					doc.insertString(0, wylie, mas);
					endPos = doc.createPosition(doc.getLength());
				}
				else
				{
					wherestart = endPos.getOffset();
					doc.insertString(endPos.getOffset(), wylie, mas);
				}

				doc.insertString(endPos.getOffset(), "\n", mas0);
				if (iter.hasNext())
					current = (org.jdom.Element)iter.next();
			}

			thisStart = "0";
			if (current.getAttributeValue("gls").equals("PAUSE"))
				doc.insertString(endPos.getOffset(), "((pause))", mas0);
			else {
				wylie = current.getText();
				if (endPos == null)
				{
					doc.insertString(0, wylie+"\n", mas0);
					endPos = doc.createPosition(doc.getLength());		
				} else {
					thisStart = String.valueOf(endPos.getOffset());
					doc.insertString(endPos.getOffset(), wylie+"\n", mas0);
				}
				doc.insertString(endPos.getOffset(), current.getAttributeValue("eng"), mas2);
			}
			startBuffer.append(thisStart);
			startBuffer.append(',');
			thisEnd = String.valueOf(endPos.getOffset());
			endBuffer.append(thisEnd);
			endBuffer.append(',');
			idBuffer.append("s0,");
			t1Buffer.append(current.getAttributeValue("start"));
			t1Buffer.append(',');
			t2Buffer.append(current.getAttributeValue("end"));
			t2Buffer.append(',');

			while (iter.hasNext())
			{
				current = (Element)iter.next();
				doc.insertString(endPos.getOffset(), "\n", mas0);
				while (current.getName().equals("spkr"))
				{
					doc.insertString(endPos.getOffset(), "\n", mas0);
					wylie = current.getAttributeValue("who");
					wherestart = endPos.getOffset();
					doc.insertString(endPos.getOffset(), wylie, mas);
					if (iter.hasNext())
						current = (org.jdom.Element)iter.next();
				}
				doc.insertString(endPos.getOffset(), "\n", mas0);
				counter++;
				thisStart = String.valueOf(endPos.getOffset());
				startBuffer.append(thisStart);
				startBuffer.append(',');
				if (current.getAttributeValue("gls").equals("PAUSE"))
					doc.insertString(endPos.getOffset(), "((pause))", mas0);
				else {
					wylie = current.getText();
					doc.insertString(endPos.getOffset(), wylie+"\n", mas0);
					doc.insertString(endPos.getOffset(), current.getAttributeValue("eng"), mas2);
				}
				thisEnd = String.valueOf(endPos.getOffset());
				endBuffer.append(thisEnd);
				endBuffer.append(',');
				thisId = "s"+String.valueOf(counter);
				idBuffer.append(thisId);
				idBuffer.append(',');
				t1Buffer.append(current.getAttributeValue("start"));
				t1Buffer.append(',');
				t2Buffer.append(current.getAttributeValue("end"));
				t2Buffer.append(',');
			}

			idBuffer.toString();
			t1Buffer.toString();
			t2Buffer.toString();
			startBuffer.toString();
			endBuffer.toString();
		}
		catch (BadLocationException ble)
		{
			ble.printStackTrace();
		}
	}

	public JTextComponent getTextComponent()
	{
		return text;
	}

	public Document getDocument()
	{
		return xmlDoc;
	}

	public String getIDs()
	{
		return idBuffer.toString();
	}

	public String getT1s()
	{
		return t1Buffer.toString();
	}

	public String getT2s()
	{
		return t2Buffer.toString();
	}

	public String getStartOffsets()
	{
		return startBuffer.toString();
	}

	public String getEndOffsets()
	{
		return endBuffer.toString();
	}
}
