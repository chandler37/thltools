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
import quicktime.*;
import quicktime.app.*;
import quicktime.app.display.*;
import quicktime.app.players.*;
import quicktime.app.image.*; //required for the QT4JAVA test
import quicktime.std.*;
import quicktime.std.movies.*;
import quicktime.std.movies.media.DataRef;
import quicktime.io.*;
import quicktime.app.time.*;
import quicktime.std.clocks.*;
import quicktime.app.QTFactory;

public class SmartQT4JPlayer extends SmartMoviePanel {

	private myJumpCallBack               theJumpper = null;
	private myRateCallBack               theRater = null;
	private URL                          mediaUrl = null;
	private TimeBase                     myMoviesTimeBase;
	private QTCanvas                     canvas;
	private QTPlayer                     player;

//constructor
	public SmartQT4JPlayer(Container cont, URL mediaURL) {
		super( new GridLayout() );
		try {
			loadMovie(mediaURL);
		} catch (SmartMoviePanelException smpe) {
			smpe.printStackTrace();
		}
	}
//destructor
	public void destroy() {
		if (theJumpper != null)
			theJumpper.cancelAndCleanup();
		if(theRater != null)
			theRater.cancelAndCleanup();

		removeAllAnnotationPlayers();
		QTSession.close();
		removeAll();
		mediaUrl = null;
		System.out.println("Clean up performed.");
	}

//accessors
	public String getIdentifyingName() {
		return "Quicktime for Java";
	}
	public URL getMediaURL() {
		return mediaUrl;
	}
	public void setParentContainer(Container c) {
	}
	public void setPlayer(QTDrawable player) {
		this.player = (QTPlayer)player;
	}
	public QTPlayer getPlayer() {
		return player;
	}
	public void setCanvas(QTCanvas canvas) {
		this.canvas = canvas;
	}
	public QTCanvas getCanvas() {
		return canvas;
	}


//contract methods - initialize
	public void displayBorders(boolean borders) throws SmartMoviePanelException {
	}
	public void displayController(boolean controller) throws SmartMoviePanelException {
	}

	public void loadMovie(URL mediaURL) throws SmartMoviePanelException {
		//Initialize a QT session and add a test image

		//These three try/catch blocks come from PlayMovie.java copyright
		// Apple Co. I'm using it to test that QT and QT4Java exist
		try {
			if (QTSession.isInitialized() == false) {
				QTSession.open();
				try {
					setCanvas( new QTCanvas(QTCanvas.kInitialSize, 0.5F, 0.5F) );
					this.add( getCanvas() );
					getCanvas().setClient(ImageDrawer.getQTLogo(), true);
				} catch(QTException qte) {
					qte.printStackTrace();
				}
			}
		} catch (NoClassDefFoundError er) {
			add (new Label ("Can't Find QTJava classes"), "North");
			add (new Label ("Check install and try again"), "South");
		} catch (SecurityException se) {
			// this is thrown by MRJ trying to find QTSession class
			add (new Label ("Can't Find QTJava classes"), "North");
			add (new Label ("Check install and try again"), "South");
		} catch (Exception e) {
			// do a dynamic test for QTException
			//so the QTException class is not loaded unless
			// an unknown exception is thrown by the runtime
			if (e instanceof ClassNotFoundException || e instanceof java.io.FileNotFoundException) {
				add (new Label ("Can't Find QTJava classes"), "North");
				add (new Label ("Check install and try again"), "South");
			} else if (e instanceof QTException) {
				add (new Label ("Problem with QuickTime install"), "North");
				if (((QTException)e).errorCode() == -2093)
					add (new Label ("QuickTime must be installed"), "South");
				else
					add (new Label (e.getMessage()), "South");
			}
		}

		try {
			this.remove( getCanvas() );
			setPlayer(QTFactory.makeDrawable(mediaURL.toString()));
			getCanvas().setClient( getPlayer(), true );
			this.add( getCanvas() );
			System.out.println("loadMovie:"+mediaURL.toString());

			myMoviesTimeBase = getPlayer().getTimeBase();
			theJumpper = new myJumpCallBack(myMoviesTimeBase);
			theJumpper.callMeWhen();
			theRater = new myRateCallBack(myMoviesTimeBase, 0, StdQTConstants.triggerRateChange);
			theRater.callMeWhen();
			Timer timer = new Timer(10,1,new Tickler(), getPlayer().getMovieController().getMovie());
			timer.setActive(true);
		} catch(QTException qte) {
			System.out.println("loadMovie failed");
			qte.printStackTrace();
		}
	}

//contract methods - control media
	public void cmd_playOn() throws SmartMoviePanelException {
		try {
			getPlayer().setRate(1.0F);
		} catch(QTException qte) {
			qte.printStackTrace();
		}
	}
	public void cmd_playSegment(Integer startTime, Integer stopTime) throws SmartMoviePanelException {
		try {
			int myScale = getPlayer().getScale();
			cmd_stop();
			getPlayer().setTime( (startTime.intValue() * myScale) / 1000 );

			if (stopTime == null) {
				myMoviesTimeBase.setStopTime(new TimeRecord(myScale, getEndTime()));
				//System.out.println("startTime:"+(startTime.intValue()*myScale)/1000+" stopTime: to the End" );
			} else {
				myMoviesTimeBase.setStopTime(new TimeRecord(myScale, (stopTime.intValue()*myScale)/1000));
				//System.out.println("startTime:"+(startTime.intValue()*myScale)/1000+" stopTime:"+(stopTime.intValue()*myScale)/1000 );
			}
			cmd_playOn();
		}
		catch (SmartMoviePanelException smpe) {
		}
		catch (StdQTException sqte) {
		}
		catch (QTException qte) {
		}
	}
	public void cmd_stop() throws SmartMoviePanelException {
		try {
			getPlayer().setRate(0.0F);
		} catch(QTException qte) {
			qte.printStackTrace();
		}
	}

//contract methods - media status
	public boolean isInitialized() {
		return true; //FIXME what should this do?
	}
	public boolean isPlaying() {
		try {
			if (player.getRate() > 0)
				return true;
		} catch (StdQTException stdqte) {
			stdqte.printStackTrace();
		}
		return false;
	}
	//doit envoyer le temps en sec  fois 1000
	public int getCurrentTime() {
		try {
			int myScale = getPlayer().getScale();
			//System.out.println("getCurrentTime():"+(player.getTime()*1000)/myScale);
			return (player.getTime()*1000)/myScale;
		} catch (StdQTException stqte) {
			stqte.printStackTrace();
			return 0;
		} catch (QTException qte) {
			System.out.println("getCurrentTimeErr");
			return 0;
		}
	}
	public int getEndTime() {
		try {
			int myScale = getPlayer().getScale();
			return (player.getDuration()*1000)/myScale;
		} catch (StdQTException stqte) {
			stqte.printStackTrace();
			return 0;
		} catch (QTException qte) {
			System.out.println("getCurrentTimeErr");
			return 0;
		}
	}

	public SmartQT4JPlayer() {
		super( new GridLayout() );
	}

// inner classes


	class myRateCallBack extends quicktime.std.clocks.RateCallBack {
		public myRateCallBack(TimeBase tb, int scale, int flag) throws QTException {
			super(tb, scale, flag);
		}
		public void execute() {
			try {
				//System.out.println("myRateCallBack: " + String.valueOf(rateWhenCalled));
				if (rateWhenCalled > 0)
					launchAnnotationTimer();
				else
					cancelAnnotationTimer();
				cancel();
				callMeWhen();
			} catch (Exception e) {
				System.out.println("myRateCallBack err: "+e.getMessage());
			}
		}
	}
	class myJumpCallBack extends quicktime.std.clocks.TimeJumpCallBack {
		public myJumpCallBack(TimeBase tb) throws QTException {
			super(tb);
		}
		public void execute() {
			try {
				//System.out.println("myJumpCallBack: " + String.valueOf(rateWhenCalled));
				if (rateWhenCalled > 0)
					launchAnnotationTimer();
				cancel();
				callMeWhen();
			} catch (Exception e) {
				System.out.println("myJumpCallBack err: "+e.getMessage());
			}
		}
	}
	public class Tickler implements Ticklish {
		public void timeChanged(int newTime) throws QTException {
			//System.out.println("*** TimerClass *** timeChanged at:"+newTime);
		}
		public boolean tickle(float er,int time) throws QTException {
			//System.out.println("*** TimerClass *** tickle at:"+time);
			return true;
		}
	}
}