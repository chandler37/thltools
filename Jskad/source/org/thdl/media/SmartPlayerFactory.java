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
import java.util.*;

public class SmartPlayerFactory {
    /** You cannot instantiate this class. */
    private SmartPlayerFactory() { }

	static final String[] possiblePlayers
		= {"org.thdl.media.SmartJMFPlayer", "org.thdl.media.SmartQT4JPlayer"};

	static SmartMoviePanel[] getAllAvailableSmartPlayers() {
		List moviePlayers = new ArrayList();
		for (int i=0; i<possiblePlayers.length; i++) {
			try {
				Class mediaClass = Class.forName(possiblePlayers[i]);
				playerClasses.add(mediaClass);
				SmartMoviePanel smp = (SmartMoviePanel)mediaClass.newInstance();
				moviePlayers.add(smp);
			} catch (ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
			} catch (InstantiationException ie) {
				ie.printStackTrace();
				ThdlDebug.noteIffyCode();
			} catch (IllegalAccessException iae) {
				iae.printStackTrace();
				ThdlDebug.noteIffyCode();
			}
		}
		return (SmartMoviePanel[])moviePlayers.toArray();
	}
}
