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
public class SmartApplet extends Applet implements AnnotationPlayer {

		static public String	FIC_SOUND;
		private SmartJMFPlayer	myJMFplayer;

/*-----------------------------------------------------------------------*/
	public void init() {
		FIC_SOUND  	= getParameter("Sound");
		String TAB_STARTS  = getParameter("STARTS");
		String TAB_ENDS  	= getParameter("ENDS");
		String TAB_IDS  	= getParameter("IDS");

		myJMFplayer = new SmartJMFPlayer();
		myJMFplayer.setParentContainer(this);
		myJMFplayer.initForSavant(convertTimesForSmartMoviePanel(TAB_STARTS), convertTimesForSmartMoviePanel(TAB_ENDS), TAB_IDS);
		myJMFplayer.addAnnotationPlayer(this);
		setLayout(new BorderLayout());
		add("Center", myJMFplayer);
	}
/*  public void stop() {
		//player.close();
		player.stop();
		player.deallocate();
	}*/
	public void start() {
		try {
			myJMFplayer.loadMovie(new URL(FIC_SOUND));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
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
	public boolean cmd_isID(String theID) {
		return myJMFplayer.cmd_isID(theID);
	}
	public boolean cmd_playFrom(String fromID) {
		return myJMFplayer.cmd_playFrom(fromID);
	}
	public boolean cmd_playS(String fromID) {
		return myJMFplayer.cmd_playS(fromID);
	}
/*-----------------------------------------------------------------------*/
	public void startAnnotation(String id) {
		sendMessage("startplay", id);
	}
	public void stopAnnotation(String id) {
		sendMessage("endplay", id);
	}
	private void sendMessage(String method, String mess) {
		Object args[] = { mess };
		try {
			JSObject.getWindow(this).call(method, args);
		} catch (Exception e) {
			System.out.println("Erreur appel javascript: "+e+" "+mess);
		}
	}
/*-----------------------------------------------------------------------*/
	private String convertTimesForSmartMoviePanel(String s) {
		StringBuffer sBuff = new StringBuffer();
		StringTokenizer sTok = new StringTokenizer(s, ",");
		while (sTok.hasMoreTokens()) {
			sBuff.append(String.valueOf(new Float(Float.parseFloat(sTok.nextToken()) * 1000).intValue()));
			sBuff.append(',');
		}
		return sBuff.toString();
	}
};