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

public class SmartQT4JPlayer extends SmartMoviePanel
{
//attributes
	private QTCanvas canvas;  //this hands over screen real estate to
									 //native graphics that quicktime can control
	
	private QTPlayer player;   //this is the the client for the canvas
									  //In my app it will get set to a
									 // quicktime.app.player object
			
	private Movie movie; //this is the content used in 
							  //QTDrawable player's quicktime.app.player constructor

	private MovieController controller;
							  
	private RateCallBack rateCallBack = null;
	private TimeJumpCallBack jumpCallBack = null;

	private TimeBaseTimeCallBack theMoviesTimeCallback;
	private TimeBaseTimeCallBackStopper theStopper;

	private URL mediaUrl = null;

//accessors
	public String getIdentifyingName() {
		return "Quicktime for Java";
	}
	public URL getMediaURL() {
		return mediaUrl;
	}
	public void setParentContainer(Container c) {
	}
	public void setPlayer(QTPlayer player)
	{
		this.player = player;
	}
	public QTPlayer getPlayer()
	{
		return player;
	}
	
	public void setMovie(Movie movie)
	{
		this.movie = movie;
	}
	public void setMovie(URL movieURL)
	{
		try
		{
			DataRef movieDataRef = new DataRef( "file://" + movieURL.getFile() );
			Movie m = new Movie();
			m = m.fromDataRef( movieDataRef, StdQTConstants.newMovieActive );
			setMovie(m);
			getMovie().setTimeScale(1000);
			mediaUrl = movieURL;
		}
		catch(QTException qte)
		{
			qte.printStackTrace();
			System.out.println( movieURL.toString() );
		}
	}
	public Movie getMovie()
	{
		return movie;
	}
	
	public void setController(MovieController controller)
	{
		this.controller = controller;
	}
	public MovieController getController()
	{
		return controller;
	}
	
	public void setCanvas(QTCanvas canvas)
	{
		this.canvas = canvas;
	}
	public QTCanvas getCanvas()
	{
		return canvas;
	}
	
	
//contract methods - initialize
	public void displayBorders(boolean borders) throws SmartMoviePanelException
	{
	}
	public void displayController(boolean controller) throws SmartMoviePanelException
	{
	}
	public void loadMovie(URL mediaURL) throws SmartMoviePanelException
	{
		//Initialize a QT session and add a test image
	
		//These three try/catch blocks come from PlayMovie.java copyright
		// Apple Co. I'm using it to test that QT and QT4Java exist
		try 
		{
			if (QTSession.isInitialized() == false) {
				QTSession.open();
				try
				{
					setCanvas( new QTCanvas(QTCanvas.kInitialSize, 0.5F, 0.5F) );
					this.add( getCanvas() );
					getCanvas().setClient(ImageDrawer.getQTLogo(), true);
				}
				catch(QTException qte)
				{
					qte.printStackTrace();
				}
			}
		} 
		catch (NoClassDefFoundError er) 
		{
			add (new Label ("Can't Find QTJava classes"), "North");
			add (new Label ("Check install and try again"), "South");
		} 
		catch (SecurityException se) 
		{
			// this is thrown by MRJ trying to find QTSession class
			add (new Label ("Can't Find QTJava classes"), "North");
			add (new Label ("Check install and try again"), "South");
		} 
		catch (Exception e) 
		{
			// do a dynamic test for QTException 
			//so the QTException class is not loaded unless
			// an unknown exception is thrown by the runtime
			if (e instanceof ClassNotFoundException || e instanceof java.io.FileNotFoundException) 
			{
				add (new Label ("Can't Find QTJava classes"), "North");
				add (new Label ("Check install and try again"), "South");
			} 
			else if (e instanceof QTException) 
			{
				add (new Label ("Problem with QuickTime install"), "North");
				if (((QTException)e).errorCode() == -2093)
					add (new Label ("QuickTime must be installed"), "South");			
				else
					add (new Label (e.getMessage()), "South");
			}
		}

		setMovie(mediaURL);
		try
		{	
			getCanvas().removeClient();
			setController( new MovieController( getMovie() ));			
			getController().setKeysEnabled(true);
			setPlayer(new QTPlayer( getController() ));
			getCanvas().setClient( getPlayer(), true );
			this.add( getCanvas() );
			TimeBase theMoviesTimeBase = getMovie().getTimeBase();
	
			// this callback is triggered when the rate of the movie changes (0 is stopped, >0 is playing)
			rateCallBack = new RateCallBack(theMoviesTimeBase, 0, StdQTConstants.triggerRateChange) {
				public void execute() {
System.out.println("Rate changed to: " + String.valueOf(rateWhenCalled));
					if (rateWhenCalled > 0)
						launchAnnotationTimer();
					else
						cancelAnnotationTimer();
					schedule(this);
				}		 
			};
			schedule(rateCallBack);

			// this callback is triggered when there is a jump in the timebase, ie when the slider control is adjusted
			jumpCallBack = new TimeJumpCallBack(theMoviesTimeBase) {
				public void execute() {
System.out.println("Time Jump. New rate: " + String.valueOf(rateWhenCalled));
					if (rateWhenCalled > 0)
						launchAnnotationTimer();
					schedule(this);
				}
			};
			schedule(jumpCallBack);
		}
		catch(QTException qte)
		{
			qte.printStackTrace();
		}
	}

	private void schedule(QTCallBack callBack) {
		try {
			callBack.callMeWhen();
		} catch (StdQTException stdqte) {
			stdqte.printStackTrace();
		}
	}
	
//contract methods - control media
	public void cmd_playOn() throws SmartMoviePanelException
	{
		try
		{
			getPlayer().setRate(1.0F);
		}
		catch(QTException qte)
		{
			qte.printStackTrace();
		}
	}
	public void cmd_playSegment(Integer startTime, Integer stopTime) throws SmartMoviePanelException
	{
		try
		{
			getPlayer().setRate(0);
			getPlayer().setTime( startTime.intValue() );

			int value;
			if (stopTime == null)
				value=getEndTime();
			else
				value=stopTime.intValue();

			TimeBase theMoviesTimeBase = getPlayer().getTimeBase();
			theStopper = new TimeBaseTimeCallBackStopper(theMoviesTimeBase, 1000, value, StdQTConstants.triggerTimeEither);
			theStopper.callMeWhen();
	
			cmd_playOn();

/*
			System.out.println("Set start time to " +startTime.intValue() );
			if (stopTime != null)
				System.out.println("Set stop time " +stopTime.intValue() );
			System.out.println("Current time " +getPlayer().getTime() );
			if (stopTime != null)
				System.out.println("Time Stopper's stop trigger " +theStopper.getCallTime() );
			System.out.println("Player Scale: " +getPlayer().getScale() );
			System.out.println("Movie Scale: " +getMovie().getTimeScale() );
*/			
		}
		catch (SmartMoviePanelException smpe) {}
		catch (StdQTException sqte) {}
		catch (QTException qte) {}
	}
	public void cmd_stop() throws SmartMoviePanelException
	{
		try
		{
			getPlayer().setRate(0.0F);
		}
		catch(QTException qte)
		{
			qte.printStackTrace();
		}
	}
	
//contract methods - media status
	public boolean isInitialized() {
		return true; //FIXME what should this do?
	}
	public boolean isPlaying() {
		try {
			if (getMovie().getRate() > 0)
				return true;
		} catch (StdQTException stdqte) {
			stdqte.printStackTrace();
		}
		return false;
	}
	public int getCurrentTime()
	{
		try {
			return getMovie().getTime();
		} catch (StdQTException stqte) {
			stqte.printStackTrace();
			return 0;
		}
	}
	public int getEndTime() 
	{
		try {
			return getMovie().getDuration();
		} catch (StdQTException stqte) {
			stqte.printStackTrace();
			return 0;
		}
	}
	
//helper methods - QT4J

	public void destroy() 
	{
		if (rateCallBack != null)
			rateCallBack.cancelAndCleanup();
		if (jumpCallBack != null)
			jumpCallBack.cancelAndCleanup();
		removeAllAnnotationPlayers();
		QTSession.close();
		removeAll();
		mediaUrl = null;
		System.out.println("Clean up performed.");
	}
		
//constructor
	public SmartQT4JPlayer(Container cont, URL mediaURL)
	{
		super( new GridLayout() );
		try {
			loadMovie(mediaURL);
		} catch (SmartMoviePanelException smpe) {
			smpe.printStackTrace();
		}
	}
	public SmartQT4JPlayer()
	{
		super( new GridLayout() );
	}			

// inner classes


	class TimeBaseTimeCallBack extends quicktime.std.clocks.TimeCallBack
	{
		private int callTime;
		private TimeBase theTimeBase;

		public void setCallTime(int time)
		{
			callTime=time;
			value=callTime;
		}
		public int getCallTime()
		{
			return callTime;
		}
		public void execute()
		{
			try
			{
//				System.out.println("---  TimeCallBack@triggerTimeEither--- called at:" + timeWhenCalledMsecs + "msecs");

				cancel();
				//reschedule
				callMeWhen();
			} 
			catch (StdQTException e)
			{}
		}
		public TimeBaseTimeCallBack(TimeBase tb, int scale, int value, int flags) throws QTException
		{
			super(tb, scale, value, flags);
//			setCallTime(value/1000);
			setCallTime(value);
			theTimeBase = tb;
		}
	}
	
	class TimeBaseTimeCallBackStopper extends TimeBaseTimeCallBack
	{
		public void execute()
		{
			try {
/*
				System.out.println("---TimeCallBackStopper--- called at:" + timeWhenCalledMsecs + "msecs");
				System.out.println("---TimeCallBackStopper--- callTime is: " + getCallTime() + " msecs");
*/
				cmd_stop();

/*
				System.out.println("---TimeCallBackStopper--- Player time is: " + getPlayer().getTime() + " msecs");
				System.out.println("---TimeCallBackStopper--- Movie time is: " + getMovie().getTime() + " msecs");
*/
				
				cancelAndCleanup();
				//reschedule
				//callMeWhen();
		} 
			catch (SmartMoviePanelException smpe) {}			
		}
		public TimeBaseTimeCallBackStopper(TimeBase tb, int scale, int value, int flags) throws QTException
		{
			super(tb, scale, value, flags);
		}

	}
}
