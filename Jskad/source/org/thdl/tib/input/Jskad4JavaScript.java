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

package org.thdl.tib.input;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.thdl.tib.text.*;

/**
* A version of Jskad which can be embedded into
* a web page as an applet. It includes all of the functionality
* of the application version of Jskad, minus the File menu.
* In other words, it is not possible to open, save, and print
* from this applet.
* @author Edward Garrett, Tibetan and Himalayan Digital Library
* @version 1.0
*/
public class Jskad4JavaScript extends JApplet {
/**
* the {@link Jskad Jskad} used by this applet
*/
	public Jskad jskad;

	public void init() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {
		}
		setContentPane(makeContentPane());
		String wylie = getParameter("Wylie");
		if (null != wylie)
			setWylie(wylie);
	}

	private Container makeContentPane() {
		jskad = new Jskad(this);
		jskad.dp.disableRoman();
		return jskad;
	}

	public void start() {
	}

	public void stop() {
	}

	public void destroy() {
	}

	public void setWylie(String wylie) {
		try {
			DuffData[] dd = TibTextUtils.getTibetanMachineWeb(wylie);
			TibetanDocument t_doc = (TibetanDocument)jskad.dp.getDocument();
			if (t_doc.getLength() > 0)
				jskad.dp.newDocument(); // DLC FIXME: is it intended that t_doc is the new document?  Because it is the old document at present.
			DuffData[] duffdata = TibTextUtils.getTibetanMachineWeb(wylie);
			t_doc.insertDuff(0, duffdata);
		}
		catch (InvalidWylieException iwe) {
			JOptionPane.showMessageDialog(this,
				"The Wylie you are trying to convert is invalid, " +
				"beginning from:\n     " + iwe.getCulpritInContext() + "\n" +
				"The culprit is probably the character '"+iwe.getCulprit()+"'.");
		}
	}

	public String getWylie() {
		return jskad.dp.getWylie();
	}
}
