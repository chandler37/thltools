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

package org.thdl.savant.tib;

import java.awt.Color;
import java.io.Reader;
import java.util.List;
import java.util.Iterator;
import javax.swing.JTextPane;
import javax.swing.text.*;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.thdl.tib.text.*;
import org.thdl.tib.input.*;
import org.thdl.savant.*;

public class TibetanWylie implements TranscriptView
{
	private JTextPane text = null;
	private Document xmlDoc = null;
	private StringBuffer idBuffer = null;
	private StringBuffer t1Buffer = null;
	private StringBuffer t2Buffer = null;
	private StringBuffer startBuffer = null;
	private StringBuffer endBuffer = null;

	public TibetanWylie(Document xml)
	{
		process(xml);
		xmlDoc = xml;
	}

	public TibetanWylie(Reader source)
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
		return "Tibetan and Wylie";
	}

	public void process(Document xml)
	{
		try {
			Element root = xml.getRootElement();
			List elements = root.getChildren();
			Iterator iter = elements.iterator();
			Element current = null;

			String wylie;
			TibetanDocument.DuffData[] dd;
			TibetanDocument.DuffData[] space = TibetanDocument.getTibetanMachineWeb("_");
			MutableAttributeSet mas = new SimpleAttributeSet();
			StyleConstants.setForeground(mas, Color.blue);
			Position endPos = null;
			int wherestart;

			TibetanDocument doc = new TibetanDocument(new StyleContext());

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

				doc.insertString(endPos.getOffset(), "\n", null);
				if (iter.hasNext())
					current = (org.jdom.Element)iter.next();
			}

			wylie = current.getText();
			dd = TibetanDocument.getTibetanMachineWeb(wylie+"\n");
			if (endPos == null)
			{
				thisStart = "0";
				doc.insertDuff(0, dd);
				endPos = doc.createPosition(doc.getLength());		
			} else {
				thisStart = String.valueOf(endPos.getOffset());
				doc.insertDuff(endPos.getOffset(), dd);
			}
			doc.insertString(endPos.getOffset(), wylie, null);

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
			doc.insertString(endPos.getOffset(), "\n", null);

			while (iter.hasNext())
			{
				current = (Element)iter.next();

				while (current.getName().equals("spkr"))
				{
					doc.insertString(endPos.getOffset(), "\n", null);
					wylie = current.getAttributeValue("who");
					wherestart = endPos.getOffset();
					doc.insertString(endPos.getOffset(), wylie, mas);
					if (iter.hasNext())
						current = (org.jdom.Element)iter.next();
				}

				doc.insertString(endPos.getOffset(), "\n", null);
				counter++;
				thisStart = String.valueOf(endPos.getOffset());
				startBuffer.append(thisStart);
				startBuffer.append(',');
				wylie = current.getText();
				dd = TibetanDocument.getTibetanMachineWeb(wylie+"\n");
				doc.insertDuff(endPos.getOffset(), dd);
				doc.insertString(endPos.getOffset(), wylie, null);

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
				doc.insertString(endPos.getOffset(), "\n", null);
			}

			text = new JTextPane(doc);
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
		catch (InvalidWylieException iwe)
		{
			iwe.printStackTrace();
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
