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

public class English implements TranscriptView
{
	private JTextPane text = null;
	private Document xmlDoc = null;
	private StringBuffer idBuffer = null;
	private StringBuffer t1Buffer = null;
	private StringBuffer t2Buffer = null;
	private StringBuffer startBuffer = null;
	private StringBuffer endBuffer = null;

	public English(Document xml)
	{
		process(xml);
		xmlDoc = xml;
	}

	public English(Reader source)
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
		return "English";
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
			else
				doc.insertString(endPos.getOffset(), current.getAttributeValue("eng"), mas0);
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
				else
					doc.insertString(endPos.getOffset(), current.getAttributeValue("eng"), mas0);
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