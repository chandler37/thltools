package org.thdl.savant;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;

public class TextPlayer extends JPanel implements AnnotationPlayer
{
	private JTextComponent text;
	private Hashtable	hashStart, hashEnd, highlights;
	private Highlighter highlighter;
	private Highlighter.HighlightPainter highlightPainter;

	public TextPlayer(JTextComponent textcomponent, Color highlightcolor, String ids, String startoffsets, String endoffsets)
	{
		text = textcomponent;
		text.setEditable(false);
		hashStart = new Hashtable();
		hashEnd = new Hashtable();
		highlights = new Hashtable();

		StringTokenizer	stIDS    = new StringTokenizer(ids, ",");
		StringTokenizer	stSTARTS = new StringTokenizer(startoffsets, ",");
		StringTokenizer	stENDS   = new StringTokenizer(endoffsets, ",");
		while ((stIDS.hasMoreTokens()) && (stSTARTS.hasMoreTokens()) && (stENDS.hasMoreTokens())) {
			String sID    = stIDS.nextToken();
			String sStart = stSTARTS.nextToken();
			String sEnd   = stENDS.nextToken();
			try {
				Integer start = new Integer(sStart);
				hashStart.put(sID, start);
			} catch (NumberFormatException err) {
				hashStart.put(sID, new Integer(0));
			}
			try {
				Integer end = new Integer(sEnd);
				hashEnd.put(sID, end);
			} catch (NumberFormatException err) {
				hashEnd.put(sID, new Integer(0));
			}
		}

		highlighter = text.getHighlighter();
		highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(highlightcolor);

		setLayout(new GridLayout(1,1));
		add(new JScrollPane(text, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
	}

	public boolean isPlayableAnnotation(String id)
	{
		return hashStart.containsKey(id);
	}

	public void startAnnotation(String id)
	{
		if (isPlayableAnnotation(id))
			highlight(id);
	}

	public void stopAnnotation(String id)
	{
		if (isPlayableAnnotation(id))
			unhighlight(id);
	}

	private void highlight(String id)
	{
		try
		{
			Integer startInt = (Integer)hashStart.get(id);
			Integer endInt = (Integer)hashEnd.get(id);
			int start = startInt.intValue();
			int end = endInt.intValue();
			Object tag = highlighter.addHighlight(start, end, highlightPainter);
			highlights.put(id, tag);
		}
		catch (BadLocationException ble)
		{
			ble.printStackTrace();
		}
	}

	private void unhighlight(String id)
	{
		if (highlights.containsKey(id))
		{
			highlighter.removeHighlight(highlights.get(id));
			highlights.remove(id);
		}
	}
}