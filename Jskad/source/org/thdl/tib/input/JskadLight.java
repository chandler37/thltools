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

/**
* Another version of Jskad which can be embedded into
* a web page as an applet. It is called 'Light' because it
* has no menu bar.
* @author Edward Garrett, Tibetan and Himalayan Digital Library
* @version 1.0
*/
public class JskadLight extends JApplet {
/**
* the scroll pane that embeds the editor
*/
	public JScrollPane scroll;
/**
* the panel that embeds the scroll pane
*/
	public JPanel panel;
/**
* the editing window
*/
	public DuffPane pane;

	public void init() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {
		}
		setContentPane(makeContentPane());
	}

	private Container makeContentPane() {
		panel = new JPanel(new GridLayout(1,1));
		pane = new DuffPane();
		scroll = new JScrollPane(pane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panel.add(scroll);
		return panel;
	}

	public void start() {
	}

	public void stop() {
	}

	public void destroy() {
	}
}
