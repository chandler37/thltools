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
public class SmartQT4JApplet extends Applet implements AnnotationPlayer {

		static public String	FIC_SOUND;
		private SmartQT4JPlayer	myPlayer;

/*-----------------------------------------------------------------------*/
	public void init() {
		FIC_SOUND  	= getParameter("Sound");
		String TAB_STARTS  = getParameter("STARTS");
		String TAB_ENDS  	= getParameter("ENDS");
		String TAB_IDS  	= getParameter("IDS");

		myPlayer = new SmartQT4JPlayer();
		myPlayer.setParentContainer(this);
		myPlayer.initForSavant(convertTimesForSmartMoviePanel(TAB_STARTS), convertTimesForSmartMoviePanel(TAB_ENDS), TAB_IDS);
		myPlayer.addAnnotationPlayer(this);
		setLayout(new BorderLayout());
		add("Center", myPlayer);
	}
    public void destroy() {
		//why don't you send an exception
		//try {
			myPlayer.destroy();
		//} catch (SmartMoviePanelException e) {
		//	System.out.println(e.getMessage());
		//}
	}
	public void start() {
		try {
			myPlayer.loadMovie(new URL(FIC_SOUND));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
/*-----------------------------------------------------------------------*/
	public boolean cmd_isRealized() {
		return myPlayer.isInitialized();
	}
	public String cmd_firstS() {
		return myPlayer.cmd_firstS();
	}
	public boolean cmd_stop() {
		try {
			myPlayer.cmd_stop();
			return true;
		} catch (SmartMoviePanelException err) {
			System.out.println(err.getMessage());
			return false;
		}
 	}
	public boolean cmd_isID(String theID) {
		return myPlayer.cmd_isID(theID);
	}
	public void cmd_playFrom(String fromID) {
		myPlayer.cmd_playFrom(fromID);
	}
	public void cmd_playS(String fromID) {
		myPlayer.cmd_playS(fromID);
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