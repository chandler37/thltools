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

import java.lang.reflect.*;
import java.util.List;
import java.util.ArrayList;

import org.thdl.util.*;

import org.thdl.util.ThdlDebug;

public class SmartPlayerFactory {
	public static List moviePlayers;

    /** You cannot instantiate this class. */
    private SmartPlayerFactory() { }

	public static List getAllAvailableSmartPlayers() {
		String os;
		try {
			os = System.getProperty("os.name").toLowerCase();
		} catch (SecurityException e) {
			os = "unknown";
		}

		String defaultPlayer;
		if (os.indexOf("mac") != -1) //macs default to org.thdl.media.SmartQT4JPlayer
			defaultPlayer = ThdlOptions.getStringOption("thdl.media.player", "org.thdl.media.SmartQT4JPlayer");
		else if (os.indexOf("windows") != -1) //windows defaults to SmartJMFPlayer
			defaultPlayer = ThdlOptions.getStringOption("thdl.media.player", "org.thdl.media.SmartJMFPlayer");
		else //put linux etc. here
			defaultPlayer = ThdlOptions.getStringOption("thdl.media.player", "org.thdl.media.SmartJMFPlayer");

		String[] possiblePlayers;
		if (defaultPlayer.equals("org.thdl.media.SmartJMFPlayer"))
			possiblePlayers = new String[] {"org.thdl.media.SmartJMFPlayer", "org.thdl.media.SmartQT4JPlayer"};
		else
			possiblePlayers = new String[] {"org.thdl.media.SmartQT4JPlayer", "org.thdl.media.SmartJMFPlayer"};

		moviePlayers = new ArrayList();
		for (int i=0; i<possiblePlayers.length; i++) {
			try {
				Class mediaClass = Class.forName(possiblePlayers[i]);

                //FIXME:				playerClasses.add(mediaClass);

				SmartMoviePanel smp = (SmartMoviePanel)mediaClass.newInstance();
				moviePlayers.add(smp);
			} catch (ClassNotFoundException cnfe) {
				System.out.println("No big deal: class " + possiblePlayers[i] + " not found.");
			} catch (LinkageError lie) {
				System.out.println("No big deal: class " + possiblePlayers[i] + " not found.");
			} catch (InstantiationException ie) {
				ie.printStackTrace();
			} catch (SecurityException se) {
				se.printStackTrace();
			} catch (IllegalAccessException iae) {
				iae.printStackTrace();
				ThdlDebug.noteIffyCode();
			}
		}
		return moviePlayers;
	}
}
