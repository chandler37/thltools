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

public class WylieEnglish implements TranscriptView
{
	private JTextPane text = null;
	private Document xmlDoc = null;
	private StringBuffer idBuffer = null;
	private StringBuffer t1Buffer = null;
	private StringBuffer t2Buffer = null;
	private StringBuffer startBuffer = null;
	private StringBuffer endBuffer = null;

	public WylieEnglish(Document xml)
	{
		process(xml);
		xmlDoc = xml;
	}

	public WylieEnglish(Reader source)
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
		return "Wylie and English";
	}

	public void process(Document xml)
	{
		try {
			Element root = xml.getRootElement();
			List elements = root.getChildren();
			Iterator iter = elements.iterator();
			Element current = null;

			TibetanDocument.DuffData[] dd;
			MutableAttributeSet mas = new SimpleAttributeSet();
			StyleConstants.setForeground(mas, Color.blue);
			MutableAttributeSet mas2 = new SimpleAttributeSet();
			StyleConstants.setItalic(mas2, true);
			Position endPos = null;
			int wherestart;
			String wylie;
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
			if (endPos == null)
			{
				thisStart = "0";
				doc.insertString(0, wylie+"\n", null);
				endPos = doc.createPosition(doc.getLength());		
			} else {
				thisStart = String.valueOf(endPos.getOffset());
				doc.insertString(endPos.getOffset(), wylie+"\n", mas2);
			}
			doc.insertString(endPos.getOffset(), current.getAttributeValue("eng"), null);

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
				doc.insertString(endPos.getOffset(), wylie+"\n", mas2);
				doc.insertString(endPos.getOffset(), current.getAttributeValue("eng"), null);
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