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

/*-----------------------------------------------------------------------*/
package org.thdl.savant;

import java.util.*;
import java.net.*;
import javax.media.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

/*-----------------------------------------------------------------------*/
public class SoundPanel extends Panel implements ControllerListener
{
		private EventListenerList listenerList = new EventListenerList();

		public URL 				mediaURL;

		private Vector			orderStartID, orderEndID;
		private Stack 			pileStart, pileEnd;
		private Hashtable			hashStart, hashEnd;

		private Player			player = null;
		private Component 		visualComponent = null;
		private Component			controlComponent = null;
		private Panel 			panel = null;
		private JPanel 			vPanel = null;

		private Container			parent = null;

		private java.util.Timer		timer = null;
		private Time 			stopTime = null;
		private Time			pauseTime = null;
		private boolean			stillLoadingVideo = false;
		private boolean 			isMediaAudio = false;
		private boolean			isSized = false;

		private Float			to = null;
/*-----------------------------------------------------------------------*/
	public SoundPanel(Container p, URL sound, String starts, String ends, String ids) {
		parent = p;

System.out.println("soundpanel - "+sound.toString());

		mediaURL = sound;
		String TAB_STARTS  = starts;
		String TAB_ENDS  	= ends;
		String TAB_IDS  	= ids;

		hashStart = new Hashtable();
		hashEnd = new Hashtable();
		pileStart = new Stack();
		pileEnd   = new Stack();

		StringTokenizer	stIDS    = new StringTokenizer(TAB_IDS, ",");
		StringTokenizer	stSTARTS = new StringTokenizer(TAB_STARTS, ",");
		StringTokenizer	stENDS   = new StringTokenizer(TAB_ENDS, ",");
		while ((stIDS.hasMoreTokens()) && (stSTARTS.hasMoreTokens()) && (stENDS.hasMoreTokens())) {
			String sID    = stIDS.nextToken();
			String sStart = stSTARTS.nextToken();
			String sEnd   = stENDS.nextToken();
			try {
				Float start = new Float(sStart);
				hashStart.put(sID, start);
			} catch (NumberFormatException err) {
				hashStart.put(sID, new Float(0));
			}
			try {
				Float end = new Float(sEnd);
				hashEnd.put(sID, end);
			} catch (NumberFormatException err) {
				hashEnd.put(sID, new Float(0));
			}
		}

		Vector saveOrder = new Vector();
		for (Enumeration e = hashStart.keys() ; e.hasMoreElements() ;) {
			Object o = e.nextElement();
			saveOrder.addElement(o);
	     	}
		orderStartID = new Vector();
		while (saveOrder.size() > 0) {
			int num = getMinusStart(saveOrder);
			orderStartID.addElement(saveOrder.elementAt(num));
			saveOrder.removeElementAt(num);
		}
		saveOrder = new Vector();
		for (Enumeration e = hashEnd.keys() ; e.hasMoreElements() ;) {
			Object o = e.nextElement();
			saveOrder.addElement(o);
	     	}
		orderEndID = new Vector();
		while (saveOrder.size() > 0) {
			int num = getMinusEnd(saveOrder);
			orderEndID.addElement(saveOrder.elementAt(num));
			saveOrder.removeElementAt(num);
		}
	}
	public void destroy() {
		player.close();
	}
	public void stop() {
		player.stop();
		player.deallocate();
	}
	public void start() {
		openPlayer();
	}
	private int getMinusStart(Vector v) {
		int index = 0;
		String first = (String)v.elementAt(index);
		Float minus = (Float)hashStart.get(first);
		for (int i=0;i<v.size();i++) {
			String s = (String)v.elementAt(i);
			Float f = (Float)hashStart.get(s);
			if (minus.floatValue() > f.floatValue()) {
				minus = f;
				index = i;
			}
		}
		return index;
	}
	private int getMinusEnd(Vector v) {
		int index = 0;
		String first = (String)v.elementAt(index);
		Float minus = (Float)hashEnd.get(first);
		for (int i=0;i<v.size();i++) {
			String s = (String)v.elementAt(i);
			Float f = (Float)hashEnd.get(s);
			if (minus.floatValue() > f.floatValue()) {
				minus = f;
				index = i;
			}
		}
		return index;
	}
	public void addAnnotationPlayer(AnnotationPlayer ap)
	{
		listenerList.add(AnnotationPlayer.class, ap);
	}
	public void removeAnnotationPlayer(AnnotationPlayer ap)
	{
		listenerList.remove(AnnotationPlayer.class, ap);
	}
	public Component popVisualComponent()
	{
		vPanel.remove(visualComponent);
		invalidate();
		validate();
		repaint();
		return visualComponent;
	}
	public void restoreVisualComponent()
	{
		vPanel.add("Center", visualComponent);
		invalidate();
		validate();
		repaint();
	}
	public Component getVisualComponent()
	{
		return visualComponent;
	}
	public Component getControlComponent()
	{
		return controlComponent;
	}
/*-----------------------------------------------------------------------*/
	public boolean cmd_isSized() {
		return isSized;
	}
	public boolean cmd_isRealized() {
		return player.getState() == Controller.Realized;
	}
	public String cmd_firstS() {
		return (String)orderStartID.elementAt(0);
	}
	public boolean cmd_stop() {
		if (player == null)
			return false;
		try {
			player.stop();
			return true;
		} catch (NotRealizedError err) {
			System.out.println("NotRealizedError");
			return false;
		}
 	}
	public boolean cmd_isID(String theID) {
		System.out.println(hashStart.containsKey(theID));
		return hashStart.containsKey(theID);
	}
	public boolean cmd_playFrom(String fromID) {
		if (stillLoadingVideo)
			return false;

		Float from  = (Float)hashStart.get(fromID);
//		String toID = (String)orderEndID.elementAt(orderEndID.size()-1);
//		Float to    = (Float)hashEnd.get(toID);

		if (play(from, null))
			return true;
		else
			return false;
	}

	public boolean cmd_playS(String fromID) {
		if (stillLoadingVideo)
			return false;

		Float from = (Float)hashStart.get(fromID);
		Float to   = (Float)hashEnd.get(fromID);
		if (play(from, to))
			return true;
		else
			return false;
	}
	public void cmd_nextEvent() {
		Float when = new Float(when());
		if (!pileStart.empty()) {
			String id = (String)pileStart.peek();
			Float f   = (Float)hashStart.get(id);
			if (when.floatValue() >= f.floatValue()) {
				id = (String)pileStart.pop();
				fireStartAnnotation(id);
			}
		}
		if (!pileEnd.empty()) {
			String id = (String)pileEnd.peek();
			Float f   = (Float)hashEnd.get(id);
			if (when.floatValue() >= f.floatValue()) {
				id = (String)pileEnd.pop();
				fireStopAnnotation(id);
			}
		}
	}
/*-----------------------------------------------------------------------*/
	private void vide_Pile() {
		while (!pileEnd.empty()) {				//vider la pile des items qui ne sont pas
			String id = (String)pileEnd.pop();	//encore fini
			if (pileStart.search(id) == -1) {
				fireStopAnnotation(id);
			}
		}
	}
	private void remplisPileStart(Float start, Float end) {
		vide_Pile();
		pileStart.removeAllElements();
		pileEnd.removeAllElements();
		for (int i=orderEndID.size()-1; i!=-1; i--) {
			String id = (String)orderEndID.elementAt(i);
			Float f   = (Float)hashEnd.get(id);
			if ((f.floatValue() > start.floatValue()) && (f.floatValue() <= end.floatValue())) {
				pileEnd.push(id);
			}
		}
		for (int i=orderStartID.size()-1; i!=-1; i--) {
			String id = (String)orderStartID.elementAt(i);
			Float f   = (Float)hashStart.get(id);
			if ((f.floatValue() >= start.floatValue()) && (f.floatValue() < end.floatValue())) {
				pileStart.push(id);
			}
		}
	}
	private void fireStartAnnotation(String id)
	{
		//see javadocs on EventListenerList for how following array is structured
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i]==AnnotationPlayer.class)
				((AnnotationPlayer)listeners[i+1]).startAnnotation(id);
		}
	}
	private void fireStopAnnotation(String id)
	{
		//see javadocs on EventListenerList for how following array is structured
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i]==AnnotationPlayer.class)
				((AnnotationPlayer)listeners[i+1]).stopAnnotation(id);
		}
	}
/*-----------------------------------------------------------------------*/
	private String when() {
		if (player == null)
			return "-1";
		if (player.getState() != Controller.Started)
			return "-1";
		long currTime = player.getMediaNanoseconds();
		Float time = new Float(currTime);
		float f = (time.floatValue() / 1000000000);
		return Float.toString(f);
	}
	private boolean play(Float from, Float to) {
		if (player == null)
			return false;
		final Time startTime = new Time((long)(from.floatValue() * 1000000000));
		try {
			if (player.getState() == Controller.Started)
				player.stop();
			while (player.getState() == Controller.Unrealized)
				;
//			player.stop();
			if (to == null) {
				stopTime = null;
				player.setStopTime(Clock.RESET);
			} else {
				stopTime = new Time((long)(to.floatValue() * 1000000000));
				player.setStopTime(stopTime);
			}
			player.setMediaTime(startTime);
			player.prefetch();
			player.start();
			return true;
		} catch(NotRealizedError err) {
			System.out.println("NotRealizedError");
			return false;
		}
	}
/*-----------------------------------------------------------------------*/
	public void openPlayer() {
		try {
			player = Manager.createPlayer(mediaURL);
			player.addControllerListener(this);
		} catch (javax.media.NoPlayerException e) {
			System.err.println("noplayer exception");
			e.printStackTrace();
			return;
		} catch (java.io.IOException ex) {
			System.err.println("IO exception");
			ex.printStackTrace();
			return;
		}
		if (player != null)
			player.realize();
	}
	public synchronized void controllerUpdate(ControllerEvent event) {
		if (player == null)
			return;
		if (event instanceof RealizeCompleteEvent) {
			System.out.println("received RealizeCompleteEvent event");
			if (visualComponent == null) {
				if (panel == null) {
					setLayout(new GridLayout(1,1));
					vPanel = new JPanel();
					vPanel.setLayout( new BorderLayout() );
					if ((visualComponent = player.getVisualComponent())!= null)	
						vPanel.add("Center", visualComponent);
					else {
						isMediaAudio = true;
						stillLoadingVideo = false;
					}
					if (!stillLoadingVideo)
					{
						if ((controlComponent = player.getControlPanelComponent()) != null) {
							if (visualComponent == null) //no video
								vPanel.setPreferredSize(new Dimension(400,25));
							vPanel.add("South", controlComponent);
						}
					}
					add(vPanel);
				}
			}
			parent.invalidate();
			parent.validate();
			parent.repaint();
			isSized = true;
			if (stillLoadingVideo)
				player.start();
		} else if (event instanceof StartEvent) {
			StartEvent se = (StartEvent)event;
			Time t = se.getMediaTime();
			long longt = t.getNanoseconds();
			Float from = new Float(longt);
			float f = (from.floatValue() / 1000000000);
			from = new Float(f);
			t = player.getStopTime();
			longt = t.getNanoseconds();
			to = new Float(longt);
			f = (to.floatValue() / 1000000000);
			to = new Float(f);
			remplisPileStart(from, to);
			if (timer != null)
			{
				timer.cancel();
				timer = null;
			}
			timer = new java.util.Timer(true);
			timer.schedule(new TimerTask() {
				public void run() {
					//this is specifically for the MPG stop time bug
					if (stopTime != null)
						if (player.getMediaTime().getNanoseconds() > stopTime.getNanoseconds())
							player.stop();
					cmd_nextEvent();
				}}, 0, 15);
		} else if (event instanceof StopEvent) {	
			pauseTime = player.getMediaTime();

			/*messy problems require messy solutions:
				if the slider is present, dragging it while playing creates
				a RestartingEvent, and if I set the media time here it messes up
				and barely plays at all (maybe because it cancels the previously
				set media time? - I don't know).
			
				but it seems that if you press the play/pause button on the
				control widget, then you need to set the media time upon stop
				(probably because of problem noted below, namely that you get
				weird results if you do player.start() without setting the media
				time.*/

			if (!(event instanceof RestartingEvent))
				player.setMediaTime(pauseTime);

			stopTime = null;

			System.out.println("received StopEvent");

			if (timer != null)
			{
				timer.cancel();
				timer = null;
			}
			if (stillLoadingVideo)
			{
				System.out.println("received EndOfMediaEvent");
				stillLoadingVideo = false;
				player.stop();
				if ((controlComponent = player.getControlPanelComponent()) != null) {
					if (visualComponent == null) //no video
						vPanel.setPreferredSize(new Dimension(400,25));
					vPanel.add("South", controlComponent);
				}
				parent.invalidate();
				parent.validate();
				parent.repaint();
			}
		} else if ( event instanceof CachingControlEvent) {
			CachingControlEvent  e = (CachingControlEvent) event;
			System.out.println("got CachingControlEvent: " + e);
			if (!isMediaAudio)
				stillLoadingVideo = true;
		} else if (event instanceof ControllerErrorEvent) {
			player = null;
			System.err.println("*** ControllerErrorEvent *** " + ((ControllerErrorEvent)event).getMessage());
		} else if (event instanceof PrefetchCompleteEvent) {
			if (panel != null) {
				panel.invalidate();
			}
			parent.invalidate();
			parent.validate();
			parent.repaint();
		}
    }
};
