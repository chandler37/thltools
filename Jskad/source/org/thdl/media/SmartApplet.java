package org.thdl.media;

/*-----------------------------------------------------------------------*/
import java.applet.*;
import java.util.*;
import java.net.*;
import javax.media.*;
import netscape.javascript.JSObject;
import java.awt.*;
import org.thdl.savant.AnnotationPlayer;

/*-----------------------------------------------------------------------*/
public class SmartApplet extends Applet implements AnnotationPlayer
{
		static public String	FIC_SOUND;

		/*private Vector			orderStartID, orderEndID;
		private Stack 			pileStart, pileEnd;
		private Hashtable		hashStart, hashEnd;*/

		/*private Player			player = null;
		private Component 		visualComponent = null;
		private Panel 			panel = null;
		private Panel 			vPanel = null;*/

		private SmartJMFPlayer	myJMFplayer;
/*-----------------------------------------------------------------------*/
	public void startAnnotation(String id) {
		eventStart(id);
	}
	public void stopAnnotation(String id) {
		eventEnd(id);
	}
	private String convertTimesForSmartMoviePanel(String s) {
		StringBuffer sBuff = new StringBuffer();
		StringTokenizer sTok = new StringTokenizer(s, ",");
		while (sTok.hasMoreTokens()) {
			sBuff.append(String.valueOf(new Float(Float.parseFloat(sTok.nextToken()) * 1000).intValue()));
			sBuff.append(',');
		}
		return sBuff.toString();
	}
	public void init() {
		FIC_SOUND  	= getParameter("Sound");
		String TAB_STARTS  = getParameter("STARTS");
		String TAB_ENDS  	= getParameter("ENDS");
		String TAB_IDS  	= getParameter("IDS");

		myJMFplayer = new SmartJMFPlayer();
		myJMFplayer.setParentContainer(this);
		myJMFplayer.initForSavant(convertTimesForSmartMoviePanel(TAB_STARTS), convertTimesForSmartMoviePanel(TAB_ENDS), TAB_IDS);
		setLayout(new BorderLayout());
		add("Center", myJMFplayer);
		try {
			myJMFplayer.loadMovie(new URL(FIC_SOUND));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

/*		hashStart = new Hashtable();
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
		}*/
	}
/*    public void destroy() {
		player.close();
    }
	public void stop() {
		//player.close();
		player.stop();
		player.deallocate();
	}
	public void start() {
		openPlayer();
	}*/
	/*private int getMinusStart(Vector v) {
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
	}*/
/*-----------------------------------------------------------------------*/
	public boolean cmd_isRealized() {
		return myJMFplayer.isInitialized();
	}
	public String cmd_firstS() {
		return myJMFplayer.cmd_firstS();
	}
	public boolean cmd_stop() {
		try {
			myJMFplayer.cmd_stop();
			return true;
		} catch (SmartMoviePanelException err) {
			System.out.println(err.getMessage());
			return false;
		}
 	}
	/*public boolean cmd_stop() {
		if (player == null)
			return false;
		try {
			player.stop();
			return true;
		} catch (NotRealizedError err) {
			System.out.println("NotRealizedError");
			return false;
		}
 	}*/
	public boolean cmd_isID(String theID) {
		return myJMFplayer.cmd_isID(theID);
	}
	/*public boolean cmd_isID(String theID) {
		System.out.println(hashStart.containsKey(theID));
		return hashStart.containsKey(theID);
	}*/
	public boolean cmd_playFrom(String fromID) {
		boolean b = myJMFplayer.cmd_playFrom(fromID);
		System.out.println(b);
		return b;
		//return myJMFplayer.cmd_playFrom(fromID);
	}
	/*public boolean cmd_playFrom(String fromID) {
		Float from  = (Float)hashStart.get(fromID);
		String toID = (String)orderEndID.elementAt(orderEndID.size()-1);
		Float to    = (Float)hashEnd.get(toID);
		return cmd_play(from, to);
	}*/
	public boolean cmd_playS(String fromID) {
		return myJMFplayer.cmd_playS(fromID);
	}
	/*public boolean cmd_playS(String fromID) {
		Float from = (Float)hashStart.get(fromID);
		Float to   = (Float)hashEnd.get(fromID);
		return cmd_play(from, to);
	}*/
	/*public boolean cmd_play(Float from, Float to) {
		if (play(from, to)) {
			remplisPileStart(from, to);
			return true;
		} else {
			return false;
		}
	}*/
	/*public void cmd_nextEvent() {
		Float when = new Float(when());
		if (!pileStart.empty()) {
			String id = (String)pileStart.peek();
			Float f   = (Float)hashStart.get(id);
			if (when.floatValue() >= f.floatValue()) {
				id = (String)pileStart.pop();
				eventStart(id);
			}
		}
		if (!pileEnd.empty()) {
			String id = (String)pileEnd.peek();
			Float f   = (Float)hashEnd.get(id);
			if (when.floatValue() >= f.floatValue()) {
				id = (String)pileEnd.pop();
				eventEnd(id);
			}
		}
	}*/
/*-----------------------------------------------------------------------*/
	/*private void vide_Pile() {
		while (!pileEnd.empty()) {				//vider la pile des items qui ne sont pas
			String id = (String)pileEnd.pop();	//encore fini
			if (pileStart.search(id) == -1) {
				eventEnd(id);
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
	}*/
	private void sendMessage(String method, String mess) {
		Object args[] = { mess };
		try {
			JSObject.getWindow(this).call(method, args);
		} catch (Exception e) {
			System.out.println("Erreur appel javascript: "+e+" "+mess);
		}
	}
	private void eventStart(String id) {
		sendMessage("startplay", id);
	}
	private void eventEnd(String id) {
		sendMessage("endplay", id);
	}
	/*private void eventStop() {			//A VOIR PLUS TARD
		vide_Pile();
		sendMessage("stopplay", "");
	}*/
/*-----------------------------------------------------------------------*/
	/*private String when() {
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
		Time startTime = new Time((long)(from.floatValue() * 1000000000));
		Time stopTime = new Time((long)(to.floatValue() * 1000000000));
		try {
			if (player.getState() == Controller.Started)
				player.stop();
			while (player.getState() == Controller.Unrealized)
				;
			player.setStopTime(stopTime);
			player.setMediaTime(startTime);
			player.start();
			return true;
		} catch(NotRealizedError err) {
			sendMessage("errHandler", "NotRealizedError");
			System.out.println("NotRealizedError");
			return false;
		}
	}*/
/*-----------------------------------------------------------------------*/

	/*public void openPlayer() {
		URL mediaURL;
		try {
			String url;
			StringTokenizer	st = new StringTokenizer(FIC_SOUND, ":");
			if (st.countTokens() == 1) {
				mediaURL = new URL(getCodeBase(), FIC_SOUND);
			} else {
				mediaURL = new URL(FIC_SOUND);
			}
			System.out.println("ouvre: "+mediaURL);
		} catch (MalformedURLException e) {
			sendMessage("errHandler", "MalformedURLException");
			System.err.println("Invalid media file URL!");
			return;
		}
		try {
			player = Manager.createPlayer(mediaURL);
			player.addControllerListener(this);
		} catch (javax.media.NoPlayerException e) {
			sendMessage("errHandler", "NoPlayerException");
			System.err.println("noplayer exception");
			return;
		} catch (java.io.IOException ex) {
			sendMessage("errHandler", "IOException");
			System.err.println("IO exception");
			return;
		}
		if (player != null)
			player.realize();
	}*/
    /*public synchronized void controllerUpdate(ControllerEvent event) {
		if (player == null)
			return;
		if (event instanceof RealizeCompleteEvent) {
			System.out.println("received RealizeCompleteEvent event");
			sendMessage("RealizeCompleteEvent", "");
			if (visualComponent == null)
				if ((visualComponent = player.getVisualComponent())!= null) {
					if (panel == null) {
						panel = new Panel();
						panel.setLayout(new BorderLayout());
						add(panel);
					}
					vPanel = new Panel();
					vPanel.setLayout( new BorderLayout() );
					vPanel.add("Center", visualComponent);
					panel.add("Center", vPanel);
				}
			validate();
		} else if (event instanceof StopAtTimeEvent) {
			System.out.println("received StopAtTimeEvent event");
			eventStop();
		} else if ( event instanceof CachingControlEvent) {
			CachingControlEvent  e = (CachingControlEvent) event;
			System.out.println("got CachingControlEvent: " + e);
		} else if (event instanceof EndOfMediaEvent) {
			System.out.println("received endofmedia event");
			player.stop();
			player.deallocate();
			eventStop();
			System.out.println("player deallocated");
		} else if (event instanceof ControllerErrorEvent) {
			player = null;
			sendMessage("errHandler", "ControllerErrorEvent");
			System.err.println("*** ControllerErrorEvent *** " + ((ControllerErrorEvent)event).getMessage());
		} else if (event instanceof PrefetchCompleteEvent) {
			if (panel != null) {
				panel.invalidate();
			}
			invalidate();
			validate();
		}
    }*/
};