package org.thdl.quilldriver;

import java.awt.*;
import java.net.*;

public abstract class SmartMoviePanel extends Panel
{
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
	public abstract int getCurrentTime();
	public abstract int getEndTime();

//helper methods - cleanup
	public abstract void destroy() throws SmartMoviePanelException;
//constructor
	public SmartMoviePanel(GridLayout layout)
	{
		super(layout);
	}
}
