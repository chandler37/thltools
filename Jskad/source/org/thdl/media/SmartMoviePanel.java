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

package org.thdl.media;

import java.awt.*;
import java.net.*;
import java.util.*;
import javax.swing.event.EventListenerList;

import org.thdl.savant.AnnotationPlayer; //should move AP to org.thdl.annotation


public abstract class SmartMoviePanel extends Panel
{
//fields
	private EventListenerList listenerList = new EventListenerList();

	private Vector orderStartID = null, orderEndID = null;
	private Stack pileStart = null, pileEnd = null;
	private Hashtable	hashStart = null, hashEnd = null;

	private Timer annTimer = null;

/*-----------------------------------------------------------------------*/
	public void addAnnotationPlayer(AnnotationPlayer ap)
	{
		listenerList.add(AnnotationPlayer.class, ap);
	}
	public void removeAnnotationPlayer(AnnotationPlayer ap)
	{
		listenerList.remove(AnnotationPlayer.class, ap);
	}
	public void removeAllAnnotationPlayers() {
		listenerList = new EventListenerList();
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
	public void initForSavant(String starts, String ends, String ids) {
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
	public String cmd_firstS() {
		return (String)orderStartID.elementAt(0);
	}
	private int getMinusStart(Vector v) {
		int index = 0;
		String first = (String)v.elementAt(index);
		Integer minus = (Integer)hashStart.get(first);
		for (int i=0;i<v.size();i++) {
			String s = (String)v.elementAt(i);
			Integer f = (Integer)hashStart.get(s);
			if (minus.intValue() > f.intValue()) {
				minus = f;
				index = i;
			}
		}
		return index;
	}
	private int getMinusEnd(Vector v) {
		int index = 0;
		String first = (String)v.elementAt(index);
		Integer minus = (Integer)hashEnd.get(first);
		for (int i=0;i<v.size();i++) {
			String s = (String)v.elementAt(i);
			Integer f = (Integer)hashEnd.get(s);
			if (minus.intValue() > f.intValue()) {
				minus = f;
				index = i;
			}
		}
		return index;
	}
	public boolean cmd_isID(String theID) {
		System.out.println(hashStart.containsKey(theID));
		return hashStart.containsKey(theID);
	}
	public void cmd_playFrom(String fromID) {
		Integer from  = (Integer)hashStart.get(fromID);
		try {
			cmd_playSegment(from, null);
		} catch (SmartMoviePanelException smpe) {
			smpe.printStackTrace();
		}
	}
	public void cmd_playS(String fromID) {
		Integer from = (Integer)hashStart.get(fromID);
		Integer to   = (Integer)hashEnd.get(fromID);
		try {
			cmd_playSegment(from, to);
		} catch (SmartMoviePanelException smpe) {
			smpe.printStackTrace();
		}
	}
	public void launchAnnotationTimer() { //FIXME: should have upper limit - stop time else end time
		if (listenerList.getListenerCount() == 0) //no annotation listeners
			return;

		int i = getCurrentTime();
		Integer from = new Integer(i);
		remplisPileStart(from, new Integer(getEndTime()));
		if (annTimer != null) {
			annTimer.cancel();
			annTimer = null;
		}
		annTimer = new java.util.Timer(true);
		annTimer.schedule(new TimerTask() {
			public void run() {
				cmd_nextEvent();
			}}, 0, 15);
	}
	public void cancelAnnotationTimer() {
		if (listenerList.getListenerCount() == 0) //no annotation listeners
			return;

		if (annTimer != null) {
			annTimer.cancel();
			annTimer = null;
		}
	}
	private void cmd_nextEvent() {
		Integer when = new Integer(getCurrentTime());
		if (!pileStart.empty()) {
			String id = (String)pileStart.peek();
			Integer f   = (Integer)hashStart.get(id);
			if (when.intValue() >= f.intValue()) {
				id = (String)pileStart.pop();
				fireStartAnnotation(id);
			}
		}
		if (!pileEnd.empty()) {
			String id = (String)pileEnd.peek();
			Integer f   = (Integer)hashEnd.get(id);
			if (when.intValue() >= f.intValue()) {
				id = (String)pileEnd.pop();
				fireStopAnnotation(id);
			}
		}
	}
	private void vide_Pile() {
		while (!pileEnd.empty()) {				//vider la pile des items qui ne sont pas
			String id = (String)pileEnd.pop();	//encore fini
			if (pileStart.search(id) == -1) {
				fireStopAnnotation(id);
			}
		}
	}
/* empties the pile, and then reconstructs it to consist of all ids
   whose start time or end time is included between start and end. */

	private void remplisPileStart(Integer start, Integer end) {
		vide_Pile();
		pileStart.removeAllElements();
		pileEnd.removeAllElements();
		for (int i=orderEndID.size()-1; i!=-1; i--) {
			String id = (String)orderEndID.elementAt(i);
			Integer f   = (Integer)hashEnd.get(id);
			if ((f.intValue() > start.intValue()) && (f.intValue() <= end.intValue())) {
				pileEnd.push(id);
			}
		}

/* note: we are also interested in ids that begin before start,
   provided they overlap with the interval start-end. */

		for (int i=orderStartID.size()-1; i!=-1; i--) {
			String id = (String)orderStartID.elementAt(i);
			Integer f   = (Integer)hashStart.get(id);
			Integer f2 = (Integer)hashEnd.get(id);
			if (  (f.intValue() >= start.intValue() && f.intValue() < end.intValue()) ||
				(f.intValue() < start.intValue() && f2.intValue() > start.intValue())) {
				pileStart.push(id);
			}
		}
	}
/*-----------------------------------------------------------------------*/


//constructor
	public SmartMoviePanel(GridLayout layout)
	{
		super(layout);
	}


/*-----------------------------------------------------------------------*/
//abstract methods


	public abstract String getIdentifyingName();
	public abstract URL getMediaURL();
	public abstract void setParentContainer(Container c);

//helper methods - initialize
	public abstract void displayBorders(boolean borders) throws SmartMoviePanelException;
	public abstract void displayController(boolean controller) throws SmartMoviePanelException;
	public abstract void loadMovie(URL mediaUrl) throws SmartMoviePanelException;

//helper methods - control media
	public abstract void cmd_playOn() throws SmartMoviePanelException;
	public abstract void cmd_playSegment(Integer startTime, Integer stopTime) throws SmartMoviePanelException;
	public abstract void cmd_stop() throws SmartMoviePanelException;

//helper methods - media status
	public abstract boolean isInitialized();
	public abstract boolean isPlaying();
	public abstract int getCurrentTime();
	public abstract int getEndTime();

//helper methods - cleanup
	public abstract void destroy() throws SmartMoviePanelException;
}
