package org.thdl.quilldriver;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Attribute;
import org.jdom.Text;
import java.awt.Color;
import java.util.List;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import javax.swing.JTextPane;
import javax.swing.text.Position;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.BadLocationException;

public class TranscriptRenderer {
	private Document xml;
	private JTextPane pane;
	private StyledDocument doc;
	private Map startOffsets, endOffsets;
	private final float indentIncrement = 15.0F;
	private final Color tagColor = Color.magenta;
	private final Color attColor = Color.pink;
	private final Color textColor = Color.gray;
	
	public TranscriptRenderer(Document xmlDoc, JTextPane textPane) {
		xml = xmlDoc;
		pane = textPane;
		render();
	}
	
	public void render() {
		doc = pane.getStyledDocument();
		int len = doc.getLength();
		if (len > 0) {
			try {
				doc.remove(0, len);
			} catch (BadLocationException ble) {
				ble.printStackTrace();
			}
		}
		startOffsets = new HashMap();
		endOffsets = new HashMap();
		Element root = xml.getRootElement();
		renderElement(root, 0.0F);

		//replace Integer values in startOffsets and endOffsets with Positions
		Set startKeys = startOffsets.keySet();
		Iterator iter = startKeys.iterator();
		while (iter.hasNext()) {
			Object key = iter.next();
			Integer val = (Integer)startOffsets.get(key);
			try {
				startOffsets.put(key, doc.createPosition(val.intValue()));
			} catch (BadLocationException ble) {
				ble.printStackTrace();
			}
		}
		Set endKeys = endOffsets.keySet();
		iter = endKeys.iterator();
		while (iter.hasNext()) {
			Object key = iter.next();
			Integer val = (Integer)endOffsets.get(key);
			try {
				endOffsets.put(key, doc.createPosition(val.intValue()));
			} catch (BadLocationException ble) {
				ble.printStackTrace();
			}
		}
	}
	
	private void renderElement(Element e, float indent) {
		SimpleAttributeSet eAttributes = new SimpleAttributeSet();
		StyleConstants.setLeftIndent(eAttributes, indent);
		SimpleAttributeSet eColor = new SimpleAttributeSet();
		StyleConstants.setForeground(eColor, tagColor);
		eColor.addAttribute("xmlnode", e);
		try {
			int start = doc.getLength();
			startOffsets.put(e, new Integer(start));
			doc.insertString(doc.getLength(), e.getQualifiedName(), eColor); //insert element begin tag
			List attributes = e.getAttributes();
			Iterator iter = attributes.iterator();
			while (iter.hasNext()) {
				Attribute att = (Attribute)iter.next();
				renderAttribute(att);
			}
			doc.insertString(doc.getLength(), " {", eColor);
			doc.setParagraphAttributes(start, doc.getLength(), eAttributes, false);
			doc.insertString(doc.getLength(), "\n", null);
			List list = e.getContent();
			iter = list.iterator();
			while (iter.hasNext()) {
				Object next = iter.next();
				if (next instanceof Element)
					renderElement((Element)next, indent + indentIncrement);
				else if (next instanceof Text) {
					Text t = (Text)next;
					if (t.getTextTrim().length() > 0)
						renderText(t, indent + indentIncrement);
				}
				// Also: Comment ProcessingInstruction CDATA EntityRef
			}
			start = doc.getLength();
			doc.insertString(start, "}", eColor); //insert element end tag
			doc.setParagraphAttributes(start, doc.getLength(), eAttributes, false);
			endOffsets.put(e, new Integer(doc.getLength()));
			doc.insertString(doc.getLength(), "\n", null);
		} catch (BadLocationException ble) {
			ble.printStackTrace();
		}
	}
	
	private void renderAttribute(Attribute att) {
		SimpleAttributeSet aColor = new SimpleAttributeSet();
		StyleConstants.setForeground(aColor, attColor);
		SimpleAttributeSet tColor = new SimpleAttributeSet();
		StyleConstants.setForeground(tColor, textColor);
		tColor.addAttribute("xmlnode", att);
		String name = att.getQualifiedName();
		String value = att.getValue();
		try {
			doc.insertString(doc.getLength(), " "+att.getQualifiedName()+"=", aColor);
			startOffsets.put(att, new Integer(doc.getLength()));		
			doc.insertString(doc.getLength(), "\"" +att.getValue()+"\"", tColor);
			endOffsets.put(att, new Integer(doc.getLength()));
		} catch (BadLocationException ble) {
			ble.printStackTrace();
		}
	}
	
	private void renderText(Text t, float indent) {
		SimpleAttributeSet tAttributes = new SimpleAttributeSet();
		StyleConstants.setLeftIndent(tAttributes, indent);
		StyleConstants.setForeground(tAttributes, textColor);
		tAttributes.addAttribute("xmlnode", t);
		try {
			String s = t.getTextTrim();
			int start = doc.getLength()-1;
			startOffsets.put(t, new Integer(start));
			doc.insertString(doc.getLength(), s, null); //insert text
			int end = doc.getLength();
			endOffsets.put(t, new Integer(end));
			doc.setParagraphAttributes(start+1, end-start, tAttributes, false);
			doc.insertString(doc.getLength(), "\n", null);
		} catch (BadLocationException ble) {
			ble.printStackTrace();
		}
	}
	
	public Object getNodeForOffset(int offset) {
		AttributeSet attSet = doc.getCharacterElement(offset).getAttributes();
		return attSet.getAttribute("xmlnode");
	}
	
	public int getStartOffsetForNode(Object node) {
		Position pos = (Position)startOffsets.get(node);
		if (pos == null) return -1;
		else return pos.getOffset();
	}
	
	public int getEndOffsetForNode(Object node) {
		Position pos = (Position)endOffsets.get(node);
		if (pos == null) return -1;
		else return pos.getOffset();
	}
	
	public boolean isEditable(Object node) {
		if (node == null) return false;
		else if (node instanceof Element) return false;
		else if (node instanceof Text) return true;
		else if (node instanceof Attribute) return true;
		else return false;
	}
}
