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

import org.thdl.util.ThdlDebug;

public class Tibetan implements TranscriptView
{
	private DuffPane text = null;
	private Document xmlDoc = null;
	private StringBuffer idBuffer = null;
	private StringBuffer t1Buffer = null;
	private StringBuffer t2Buffer = null;
	private StringBuffer startBuffer = null;
	private StringBuffer endBuffer = null;

	public Tibetan(Document xml)
	{
		process(xml);
		xmlDoc = xml;
	}

	public Tibetan(Reader source)
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
		return "Tibetan";
	}

	public void process(Document xml)
	{
		try {
			Element root = xml.getRootElement();
			List elements = root.getChildren();
			Iterator iter = elements.iterator();
			Element current = null;
			idBuffer = new StringBuffer();
			startBuffer = new StringBuffer();
			endBuffer = new StringBuffer();
			t1Buffer = new StringBuffer();
			t2Buffer = new StringBuffer();
			String thisStart, thisEnd, thisId;
			Position endPos = null;
			DuffData[] dd;
			TibetanDocument doc = new TibetanDocument(new StyleContext());
			DuffData[] space = TibTextUtils.getTibetanMachineWeb("_");
			MutableAttributeSet mas = new SimpleAttributeSet();
			StyleConstants.setForeground(mas, Color.blue);

			int counter = 0;
			int wherestart = 0;

			if (iter.hasNext())
				current = (Element)iter.next();

			while (current.getName().equals("spkr"))
			{
				dd = TibTextUtils.getTibetanMachineWeb(current.getAttributeValue("who"));
				if (endPos == null)
				{
					doc.insertDuff(0, dd);
					endPos = doc.createPosition(doc.getLength());
					doc.setCharacterAttributes(0, endPos.getOffset(), mas, false);
				}
				else
				{
					wherestart = endPos.getOffset();
					doc.insertDuff(endPos.getOffset(), dd);
					doc.setCharacterAttributes(wherestart, endPos.getOffset()-wherestart, mas, false);
				}

				if (iter.hasNext())
					current = (org.jdom.Element)iter.next();
			}

			if (endPos == null)
			{
				doc.insertDuff(0, space);
				endPos = doc.createPosition(doc.getLength());
			} else {
				doc.insertDuff(endPos.getOffset(), space);
			}

			wherestart = endPos.getOffset();
			dd = TibTextUtils.getTibetanMachineWeb(current.getText()); //from +"\n"
			doc.insertDuff(endPos.getOffset(), dd);
			startBuffer.append(String.valueOf(wherestart)+",");
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
					doc.insertString(endPos.getOffset(), "\n", null);
					dd = TibTextUtils.getTibetanMachineWeb(current.getAttributeValue("who")); //from +"\n"
					wherestart = endPos.getOffset();
					doc.insertDuff(endPos.getOffset(), dd);
					doc.setCharacterAttributes(wherestart, endPos.getOffset()-wherestart, mas, false);
					if (iter.hasNext())
						current = (org.jdom.Element)iter.next();
				}

				doc.insertDuff(endPos.getOffset(), space);
				counter++;
				dd = TibTextUtils.getTibetanMachineWeb(current.getText()); //from "+\n"
				thisStart = String.valueOf(endPos.getOffset());
				startBuffer.append(thisStart);
				startBuffer.append(',');
				doc.insertDuff(endPos.getOffset(), dd);
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

			text = new DuffPane();
			text.setDocument(doc);
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
		catch (InvalidWylieException iwe)
		{
			iwe.printStackTrace();
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
