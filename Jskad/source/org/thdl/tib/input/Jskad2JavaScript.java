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
import netscape.javascript.JSObject;

/**
* A version of Jskad which can be embedded into
* a web page as an applet. It includes all of the functionality
* of the application version of Jskad, minus the File menu.
* In other words, it is not possible to open, save, and print
* from this applet.
* @author Edward Garrett, Tibetan and Himalayan Digital Library
* @version 1.0
*/
public class Jskad2JavaScript extends JApplet {
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
	}

	private Container makeContentPane() {
		JPanel panel = new JPanel(new BorderLayout());
		jskad = new Jskad(this);
		panel.add("Center", jskad);
		JButton submit = new JButton("Submit");
		submit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try {
					TibetanDocument t_doc = (TibetanDocument)jskad.dp.getDocument();
					String wylie = t_doc.getWylie();
					Object[] args = {wylie};
					JSObject.getWindow(Jskad2JavaScript.this).call("settext", args);
				} catch (Exception ex) {
				}
			}
		});
		panel.add("South", submit);
		return panel;
	}

	public void start() {
	}

	public void stop() {
	}

	public void destroy() {
	}
}