package org.thdl.quilldriver;

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
							  
	private TimeBaseRateCallBack theMoviesRateCallback;
	private TimeBaseExtremesCallBack theMoviesExtremeCallback;
	private TimeBaseTimeJumpCallBack theMoviesTimeJumpCallback;
	private TimeBaseTimeCallBack theMoviesTimeCallback;
	private TimeBaseTimeCallBackStopper theStopper;
	
//accessors
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
	
			// this callback is triggered when the rate of the movie changes
			theMoviesRateCallback = new TimeBaseRateCallBack(theMoviesTimeBase, 1.0F, StdQTConstants.triggerRateChange);
//			theMoviesRateCallback.callMeWhen();
	
			// this callback is triggered when the movie ends
			theMoviesExtremeCallback = new TimeBaseExtremesCallBack(theMoviesTimeBase, StdQTConstants.triggerAtStop);
//			theMoviesExtremeCallback.callMeWhen();
	
			// this callback is triggered when the movie starts
			theMoviesExtremeCallback = new TimeBaseExtremesCallBack(theMoviesTimeBase, StdQTConstants.triggerAtStart);
//			theMoviesExtremeCallback.callMeWhen();
	
			// this callback is triggered when there is a jump in the timebase
			theMoviesTimeJumpCallback = new TimeBaseTimeJumpCallBack(theMoviesTimeBase);
//			theMoviesTimeJumpCallback.callMeWhen();
	
			// this schedules the time callback once every 2 seconds
			// this callback is triggered at a specific time interval
			theMoviesTimeCallback = new TimeBaseTimeCallBack(theMoviesTimeBase, 1, 2, StdQTConstants.triggerTimeEither);
//			theMoviesTimeCallback.callMeWhen();
	
			//Using the Timer class you can get rescheduled properly and get callbacks at the set intervals. It uses the same callback
			//mechanism of internally of the TimeCallback.
			//Its recomended to use this Timer class to do callbacks , which would take care of the time base time changes and
			//recscheduling of the tickle method .
			Timer timer = new Timer(1, 2, new Tickler(), getMovie() );
//			timer.setActive(true);		

		}
		catch(QTException qte)
		{
			qte.printStackTrace();
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
			getPlayer().setTime( startTime.intValue() );

			int value=stopTime.intValue();
			TimeBase theMoviesTimeBase = getPlayer().getTimeBase();
			theStopper = new TimeBaseTimeCallBackStopper(theMoviesTimeBase, 1000, value, StdQTConstants.triggerTimeEither);
			theStopper.callMeWhen();
	
			cmd_playOn();

			System.out.println("Set start time to " +startTime.intValue() );
			System.out.println("Set stop time " +stopTime.intValue() );
			System.out.println("Current time " +getPlayer().getTime() );
			System.out.println("Time Stopper's stop trigger " +theStopper.getCallTime() );
			System.out.println("Player Scale: " +getPlayer().getScale() );
			System.out.println("Movie Scale: " +getMovie().getTimeScale() );
			
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
	public boolean isInitialized()
	{
		return true;
	}
	public int getCurrentTime()
	{
		return 0;
	}
	public int getEndTime() 
	{
		return 0;
	}
	
//helper methods - QT4J
	public void startupQTSession() 
	{
		//Initialize a QT session and add a test image
	
		//These three try/catch blocks come from PlayMovie.java copyright
		// Apple Co. I'm using it to test that QT and QT4Java exist
		try 
		{
			if (QTSession.isInitialized() == false)
			QTSession.open();
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

	public void destroy() 
	{
		QTSession.close();
		System.out.println("Clean up performed.");
	}
		
//constructor
	public SmartQT4JPlayer(Container cont, URL mediaURL)
	{
		super( new GridLayout() );
		startupQTSession();
	}		
	public SmartQT4JPlayer()
	{
		super( new GridLayout() );
		startupQTSession();
	}		
	
// inner classes
	/**
	 *  This class extends the RateCallBack class and provides an execute routine
	 *  that is invoked by the callback when the rate of the movie changes
	 *
	 *@author     travis
	 *@created    September 3, 2002
	 */

	public class TimeBaseRateCallBack extends RateCallBack
	{
		public TimeBaseRateCallBack(TimeBase tb, float rate, int flag) throws QTException
		{
			super(tb, rate, flag);
		}

		public void execute()
		{
			try
			{
				System.out.println("---  RateCallBack@ratechange---");

				cancel();
				//reschedule
				callMeWhen();

			} catch (StdQTException e)
			{}
		}
	}
	
	/**
	 *  This class implements a method that is called when the movie stops
	 *
	 *@author     travis
	 *@created    September 3, 2002
	 */

	class TimeBaseExtremesCallBack extends quicktime.std.clocks.ExtremesCallBack
	{

		public TimeBaseExtremesCallBack(TimeBase tb, int flag) throws QTException
		{
			super(tb, flag);
		}

		public void execute()
		{
			try
			{
				System.out.println("---  ExtremesCallBack@stop---");

				//cancel
				cancel();
				//reschedule
				callMeWhen();
			} 
			catch (StdQTException e)
			{}
		}
	}

	/**
	 *  This class extends the TimeJumpCallBack class to provide a method that is
	 *  called when the timebase of the movie changes (IE, if the user clicks in
	 *  the movie controller)
	 *
	 *@author     travis
	 *@created    September 3, 2002
	 */

	class TimeBaseTimeJumpCallBack extends quicktime.std.clocks.TimeJumpCallBack
	{
		public TimeBaseTimeJumpCallBack(TimeBase tb) throws QTException
		{
			super(tb);
		}

		public void execute()
		{
			try
			{
				System.out.println("---  TimeJumpCallBack---");

				cancel();
				//reschedule
				callMeWhen();
			} 
			catch (StdQTException e)
			{}
		}
	}

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
				System.out.println("---  TimeCallBack@triggerTimeEither--- called at:" + timeWhenCalledMsecs + "msecs");

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
			try
			{
				System.out.println("---TimeCallBackStopper--- called at:" + timeWhenCalledMsecs + "msecs");
				System.out.println("---TimeCallBackStopper--- callTime is: " + getCallTime() + " msecs");
				cmd_stop();
				System.out.println("---TimeCallBackStopper--- Player time is: " + getPlayer().getTime() + " msecs");
				System.out.println("---TimeCallBackStopper--- Movie time is: " + getMovie().getTime() + " msecs");
				
				cancelAndCleanup();
				//reschedule
				//callMeWhen();
		} 
			catch (StdQTException e) {}
			catch (SmartMoviePanelException smpe) {}			
		}
		public TimeBaseTimeCallBackStopper(TimeBase tb, int scale, int value, int flags) throws QTException
		{
			super(tb, scale, value, flags);
		}

	}

	public class Tickler implements Ticklish
	{
		public void timeChanged(int newTime) throws QTException
		{
			System.out.println("* * * * Timer Class * * * timeChanged at:" + newTime);
		}

		public boolean tickle(float er, int time) throws QTException
		{
			System.out.println("* * * * Timer Class  * * * tickle at:" + time);
			return true;
		}
	}	
	
}
