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

public abstract class SmartMoviePanel extends Panel
{
	public abstract static String getName();
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
//constructor
	public SmartMoviePanel(GridLayout layout)
	{
		super(layout);
	}
}
