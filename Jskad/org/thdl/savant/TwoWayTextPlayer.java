package org.thdl.savant;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import javax.swing.text.JTextComponent;

public class TwoWayTextPlayer extends TextHighlightPlayer
{
	protected TreeMap orderedOffsets = null;
	protected SoundPanel sound = null;
	protected long doubleClickDelay = 300L;
	protected long lastClickTime;
	protected Point lastClickPoint = null;

	public TwoWayTextPlayer(SoundPanel sp, TranscriptView view, Color highlightColor)
	{
		super(view, highlightColor);
		sound = sp;

		orderedOffsets = new TreeMap();
		for (Enumeration e = hashEnd.keys() ; e.hasMoreElements() ;)
		{
			String id = (String)e.nextElement();
			orderedOffsets.put(hashEnd.get(id), id);
		}

		for (Enumeration e = hashStart.keys() ; e.hasMoreElements() ;)
		{
			String id = (String)e.nextElement();
			orderedOffsets.put(hashStart.get(id), id);
		}

		view.getTextComponent().addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				//note - can't use e.getClickCount() here
				//because every double click event is
				//preceded by a single click event, so
				//no easy way to distinguish the two
				try
				{
					final long currentClickTime = e.getWhen();
					final Point currentClickPoint = e.getPoint();
					if (lastClickPoint != null && currentClickPoint.equals(lastClickPoint))
					{ //this could be the second click of a double click
						if (currentClickTime - lastClickTime < doubleClickDelay)
						{ //must be a double-click: play only one line
							lastClickPoint = null; //makes sure second click won't be interpreted as single click also
							int pos = text.viewToModel(currentClickPoint)+1;
							Integer i = new Integer(pos);				
							SortedMap map = orderedOffsets.headMap(i);
							final String id = (String)map.get(map.lastKey());
							highlighter.removeAllHighlights();
							sound.cmd_playS(id);
							return;
						}
					}
					//otherwise, this click can only be single click or first click
					lastClickTime = currentClickTime;
					lastClickPoint = new Point(currentClickPoint);
					final Timer clickTimer = new Timer(true);
					clickTimer.schedule(new TimerTask()
					{
						public void run()
						{
							if (lastClickPoint != null)
							{ //must be a single click: play line onwards
								int pos = text.viewToModel(currentClickPoint)+1;
								Integer i = new Integer(pos);				
								SortedMap map = orderedOffsets.headMap(i);
								final String id = (String)map.get(map.lastKey());
								highlighter.removeAllHighlights();
								sound.cmd_playFrom(id);
							}
							clickTimer.cancel();
						}
					}, doubleClickDelay);
				}
				catch (NoSuchElementException nsee)
				{
					nsee.printStackTrace();
				}
			}
		});
	}

	public void reset()
	{
		sound.removeAnnotationPlayer(this);
		highlighter.removeAllHighlights();
	}

	public JTextComponent getTextComponent()
	{
		return text;
	}
}