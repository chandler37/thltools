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
import java.util.StringTokenizer;
import javax.swing.JTextPane;
import javax.swing.text.*;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.thdl.savant.*;

import org.thdl.util.ThdlDebug;

public class SegmentedQuechua implements TranscriptView
{
	private JTextPane text = null;
	private Document xmlDoc = null;
	private StringBuffer idBuffer = null;
	private StringBuffer t1Buffer = null;
	private StringBuffer t2Buffer = null;
	private StringBuffer startBuffer = null;
	private StringBuffer endBuffer = null;

	public SegmentedQuechua(Document xml)
	{
		process(xml);
		xmlDoc = xml;
	}

	public SegmentedQuechua(Reader source)
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
			ThdlDebug.noteIffyCode();
		}
	}

	public String getTitle()
	{
		return "Segmented Quechua";
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
			Position endPos = null;

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
				String wylie = current.getAttributeValue("who");
				if (endPos == null)
				{
					doc.insertString(0, wylie, mas);
					endPos = doc.createPosition(doc.getLength());
				}
				else
				{
					doc.insertString(endPos.getOffset(), wylie, mas);
				}

				if (iter.hasNext())
					current = (org.jdom.Element)iter.next();
			}

			doc.insertString(endPos.getOffset(), "\n", mas0);
			thisStart = String.valueOf(endPos.getOffset());
			if (current.getAttributeValue("gls").equals("PAUSE"))
				doc.insertString(endPos.getOffset(), "......", mas0);
			else {
				String segString = current.getAttributeValue("seg");
				StringBuffer segBuffer = new StringBuffer();
				StringTokenizer segTokenizer = new StringTokenizer(segString);
				for (int i=0; segTokenizer.hasMoreTokens(); i++) {
					String next = segTokenizer.nextToken();
					if (i>0 && next.charAt(0)!='-')
						segBuffer.append(' ');
					segBuffer.append(next);
				}
				doc.insertString(endPos.getOffset(), segBuffer.toString(), mas0);
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

				while (current.getName().equals("spkr"))
				{
					doc.insertString(endPos.getOffset(), "\n\n", mas0);
					doc.insertString(endPos.getOffset(), current.getAttributeValue("who"), mas);

					if (iter.hasNext())
						current = (org.jdom.Element)iter.next();
				}

				doc.insertString(endPos.getOffset(), "\n", mas0);
				counter++;
				thisStart = String.valueOf(endPos.getOffset());
				startBuffer.append(thisStart);
				startBuffer.append(',');
				if (current.getAttributeValue("gls").equals("PAUSE"))
					doc.insertString(endPos.getOffset(), "......", mas0);
				else {
					String segString = current.getAttributeValue("seg");
					StringBuffer segBuffer = new StringBuffer();
					StringTokenizer segTokenizer = new StringTokenizer(segString);
					for (int i=0; segTokenizer.hasMoreTokens(); i++) {
						String next = segTokenizer.nextToken();
						if (i>0 && next.charAt(0)!='-')
							segBuffer.append(' ');
						segBuffer.append(next);
					}
					doc.insertString(endPos.getOffset(), segBuffer.toString(), mas0);
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
			ThdlDebug.noteIffyCode();
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
