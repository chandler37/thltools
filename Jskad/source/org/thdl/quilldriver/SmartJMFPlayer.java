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

package org.thdl.quilldriver;

import java.util.*;
import java.net.*;
import javax.media.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

import org.thdl.util.ThdlDebug;

/*-----------------------------------------------------------------------*/
public class SmartJMFPlayer extends SmartMoviePanel implements ControllerListener
{
		private EventListenerList listenerList = new EventListenerList();

		public URL 				mediaURL;

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
	public SmartJMFPlayer(Container p, URL sound) throws SmartMoviePanelException {
		super( new GridLayout() );
		parent = p;
		loadMovie(sound);
	}
	public void loadMovie(URL sound) throws SmartMoviePanelException {
		if (mediaURL != null) {
			cmd_stop();
			destroy();
		}
		mediaURL = sound;
		start();
	}
	public void destroy() throws SmartMoviePanelException {
		if (false)
			throw new SmartMoviePanelException();
		player.close();
	}
/*-----------------------------------------------------------------------*/
	private void start() {
		try {
			player = Manager.createPlayer(mediaURL);
			player.addControllerListener(this);
		} catch (javax.media.NoPlayerException e) {
			System.err.println("noplayer exception");
			e.printStackTrace();
			ThdlDebug.noteIffyCode();
			return;
		} catch (java.io.IOException ex) {
			System.err.println("IO exception");
			ex.printStackTrace();
			ThdlDebug.noteIffyCode();
			return;
		}
		if (player != null)
			player.realize();
	}
/*-----------------------------------------------------------------------*/
	public void displayBorders(boolean borders) throws SmartMoviePanelException
	{
		if (false)
			throw new SmartMoviePanelException();
	}
	public void displayController(boolean controller) throws SmartMoviePanelException
	{
		if (false)
			throw new SmartMoviePanelException();

	}
	public boolean isInitialized() {
		return isSized;
	}
/*-----------------------------------------------------------------------*/
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

//			player.setStopTime(Clock.RESET);
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
/*-----------------------------------------------------------------------*/
	public void cmd_stop() throws SmartMoviePanelException {
		if (player == null)
			throw new SmartMoviePanelException("no player");
		try {
			player.stop();
		} catch (NotRealizedError err) {
			throw new SmartMoviePanelException("JMF player not realized");
		}
 	}
	public void cmd_playOn() throws SmartMoviePanelException {
		if (stillLoadingVideo || player == null) {
			throw new SmartMoviePanelException("no player or video still loading");
		}
		if (player.getState() == Controller.Started)
			return;

		if (pauseTime == null)
			player.setMediaTime(new Time(0.0));
		else
			player.setMediaTime(pauseTime);
		if (player.getTargetState() < Player.Started) {
			player.setStopTime(Clock.RESET);
			player.prefetch();
		}
		player.start();
	}
	public void cmd_playSegment(Integer from, Integer to) throws SmartMoviePanelException {
		if (from == null || player == null || stillLoadingVideo)
			throw new SmartMoviePanelException("no player or video still loading");

		final Time startTime = new Time(from.longValue() * 1000000);
		try {
			if (player.getState() == Controller.Started)
				player.stop();
			while (player.getState() == Controller.Unrealized)
				;
			if (to == null) {
				stopTime = null;
				player.setStopTime(Clock.RESET);
			} else {
				stopTime = new Time(to.longValue() * 1000000);
				player.setStopTime(stopTime);
			}
			player.setMediaTime(startTime);
			player.prefetch();
			player.start();
		} catch(NotRealizedError err) {
			throw new SmartMoviePanelException("JMF player not realized");
		}
	}
/*-----------------------------------------------------------------------*/
	public int getCurrentTime() {
		if (player == null)
			return -1;
		if (player.getState() < Controller.Realized)
			return -1;
		long currTime = player.getMediaNanoseconds();
		return new Long(currTime / 1000000).intValue();
	}
	public int getEndTime() {
		Time t = player.getDuration();
		long l = t.getNanoseconds();
		return new Long(l / 1000000).intValue();
	}
/*-----------------------------------------------------------------------*/
}

/*

After pause the MPEG video and playing it again it gets faster
Author: vladshtr
In Reply To: After pause the MPEG video and playing it again it gets faster
Mar 1, 2001 6:25 PM

Reply 1 of 1


The problem is in the setting the Media time.

The safety way is to always set new media time with the
following method: setMediaTime(Time time); .... if you want to
use it after
-player.stop(); used as real stop you can use setMediaTime(new
Time(0.0));
-player.stop(); used as real pause you have to use the
combination:
player.stop();
Time currentTime = player.getMediaTime();
//........
player.setMediaTime(currentTime);
player.start();


Re: (urgent) when you pause and resume, video plays at rate > 1
Author: seertenedos
In Reply To: (urgent) when you pause and resume, video plays at rate > 1
Aug 11, 2002 11:36 PM

Reply 1 of 1


I found a solution for this problem for those that are interested.

what you have to do is store the time just before you pause and then set the
time just before you play. here is a copy of my pause and play methods

// Start the player
private void play() {
if (player != null)
{
if (pauseTime != null)
{
player.setMediaTime(pauseTime);
}
if (player.getTargetState() < Player.Started)
player.prefetch();
player.start();
}
}

// Pause the player
private void pause() {
if (player != null)
pauseTime = player.getMediaTime();
player.stop();
}


that should solve your pause play problems!

> The problem is below. It seems quite a few people are
> having this problem but i have not seen a solution
> around. I really need a solution to this problem as
> the whole point of my application is that it plays
> divx and mpeg videos. At the moment i have divx
> movies playing through the mpeg demuxer as the avi one
> stuffed up the audio. I think that is the reason it
> affects both divx and mpeg. My application is due in
> one week and my client is not going to be very happy
> if this problem happens every time they pause then
> play the video.
> The player is for divx movies. If anyone knows how to
> solve this problem or how to make it so you can pause
> and resume divx movies please respond.
>
> Pause and Resume playback.
> The video plays at a high rate and there is no audio.
> Problem doesn't appear while seeking.
>
>
>
>

*/