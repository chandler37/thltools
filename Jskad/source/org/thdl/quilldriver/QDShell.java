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

import java.io.*;
import java.awt.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.text.*;
import javax.swing.text.rtf.*;

import org.thdl.util.ThdlDebug;
import org.thdl.util.ThdlActionListener;
import org.thdl.util.ThdlOptions;

import org.thdl.savant.JdkVersionHacks;


public class QDShell extends JFrame {
    /** When opening a file, this is the only extension QuillDriver
        cares about.  This is case-insensitive. */
    protected final static String dotQuillDriver = ".xml";

	ResourceBundle messages = null;
	QD qd = null;

	public static void main(String[] args) {
		try {
			ThdlDebug.attemptToSetUpLogFile("qd", ".log");

			Locale locale;

			if (args.length == 2) {
				locale = new Locale(new String(args[0]), new String(args[1]));
				Locale[] locales = Locale.getAvailableLocales();
				for (int k=0; k<locales.length; k++)
					if (locales[k].equals(locale)) {
						JComponent.setDefaultLocale(locale);
						break;
					}
			}
			else
				locale = Locale.getDefault();

			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			catch (Exception e) {
			}

			QDShell qdsh = new QDShell(locale);
			qdsh.setVisible(true);
		} catch (NoClassDefFoundError err) {
			ThdlDebug.handleClasspathError("QuillDriver's CLASSPATH", err);
		}
	}

	public QDShell(Locale locale) {
		setTitle("QuillDriver");

		// Code for Merlin
		if (JdkVersionHacks.maximizedBothSupported(getToolkit())) {
			setLocation(0,0);
			setSize(getToolkit().getScreenSize().width,getToolkit().getScreenSize().height);
			setVisible(true);

			// call setExtendedState(Frame.MAXIMIZED_BOTH) if possible:
			if (!JdkVersionHacks.maximizeJFrameInBothDirections(this)) {
				throw new Error("badness at maximum: the frame state is supported, but setting that state failed.  JdkVersionHacks has a bug.");
			}
		} else {
			Dimension gs = getToolkit().getScreenSize();
			setLocation(0,0);
			setSize(new Dimension(gs.width, gs.height));
			setVisible(true);
		}

		messages = ResourceBundle.getBundle("MessageBundle", locale);

		qd = new QD(messages);
		getContentPane().add(qd);
		setJMenuBar(getQDShellMenu());
		addWindowListener(new WindowAdapter () {
			public void windowClosing (WindowEvent e) {
				if (getSave())
					System.exit(0);
			}
		});
	}

	public JMenuBar getQDShellMenu() {
		JMenu projectMenu = new JMenu(messages.getString("Project"));
		JMenuItem newItem = new JMenuItem(messages.getString("New"));
		newItem.addActionListener(new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e) {
				if (getSave()) {
					JFileChooser fc = new JFileChooser();
					if (fc.showDialog(QDShell.this, messages.getString("SelectMedia")) == JFileChooser.APPROVE_OPTION) {
						File mediaFile = fc.getSelectedFile();
						qd.newProject(mediaFile);
					}
				}
			}
		});		
		JMenuItem openItem = new JMenuItem(messages.getString("Open"));
		openItem.addActionListener(new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e) {
				if (getSave()) {
					JFileChooser fc = new JFileChooser();
					fc.addChoosableFileFilter(new QDFileFilter());
					if (fc.showDialog(QDShell.this, messages.getString("OpenTranscript")) == JFileChooser.APPROVE_OPTION) {
						File transcriptFile = fc.getSelectedFile();
						qd.loadTranscript(transcriptFile);
					}
				}
			}
		});
		JMenuItem closeItem = new JMenuItem(messages.getString("Close"));
		closeItem.addActionListener(new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e) {
				if (getSave())
					qd.clearProject();
			}
		});

		JMenuItem saveItem = new JMenuItem(messages.getString("Save"));
		saveItem.addActionListener(new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e) {
				getSave();
			}
		});
		JMenuItem quitItem = new JMenuItem(messages.getString("Quit"));
		quitItem.addActionListener(new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e) {
				if (getSave())
					System.exit(0);
			}
		});
		projectMenu.add(newItem);
		projectMenu.add(openItem);
		projectMenu.add(closeItem);
		projectMenu.addSeparator();
		projectMenu.add(saveItem);
		projectMenu.addSeparator();
		projectMenu.add(quitItem);

	//Tibetan-specific value: remove in non-Tibetan version
	//non-Tibetan specific version would have Transcription Language option here instead
		JRadioButton[] buttons = new JRadioButton[4];
		buttons[0] = new JRadioButton("THDL Extended Wylie");
		buttons[1] = new JRadioButton("TCC Keyboard 1");
		buttons[2] = new JRadioButton("TCC Keyboard 2");
		buttons[3] = new JRadioButton("Sambhota Keymap One");
		buttons[0].setActionCommand("wylie");
		buttons[1].setActionCommand("tcc1");
		buttons[2].setActionCommand("tcc2");
		buttons[3].setActionCommand("sambhota1");
		ThdlActionListener l1 = new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e) {
				qd.changeKeyboard(e);
			}
		};

		buttons[0].addActionListener(l1);
		buttons[1].addActionListener(l1);
		buttons[2].addActionListener(l1);
		buttons[3].addActionListener(l1);
		ButtonGroup bg = new ButtonGroup();
		bg.add(buttons[0]);
		bg.add(buttons[1]);
		bg.add(buttons[2]);
		bg.add(buttons[3]);
		JPanel b1 = new JPanel(new GridLayout(0,1));
		b1.add(buttons[0]);
		b1.add(buttons[1]);
		b1.add(buttons[2]);
		b1.add(buttons[3]);

	int initialKeyboard
            = ThdlOptions.getIntegerOption("thdl.default.tibetan.keyboard", 0);

		buttons[initialKeyboard].setSelected(true);

		JMenu keyboardMenu = new JMenu(messages.getString("Keyboard"));
		keyboardMenu.add(b1);


		JRadioButton[] buttons2 = new JRadioButton[2];
		buttons2[0] = new JRadioButton("Java Media Framework");
		buttons2[1] = new JRadioButton("Quicktime for Java");
		buttons2[0].setActionCommand("jmf");
		buttons2[1].setActionCommand("qt4j");
		ThdlActionListener l2 = new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e) {
				qd.setMediaPlayerProperty(e.getActionCommand());
			}
		};

		buttons2[0].addActionListener(l2);
		buttons2[1].addActionListener(l2);
		ButtonGroup bg2 = new ButtonGroup();
		bg2.add(buttons2[0]);
		bg2.add(buttons2[1]);
		JPanel b2 = new JPanel(new GridLayout(0,1));
		b2.add(buttons2[0]);
		b2.add(buttons2[1]);

		String initialMediaPlayer = qd.getMediaPlayerProperty();
		if (initialMediaPlayer.equals("jmf"))
			buttons2[0].setSelected(true);
		else if (initialMediaPlayer.equals("qt4j"))
			buttons2[1].setSelected(true);

		JMenu mediaPlayerMenu = new JMenu(messages.getString("MediaPlayer"));
		mediaPlayerMenu.add(b2);

		JMenu preferencesMenu = new JMenu(messages.getString("Preferences"));
		preferencesMenu.add(keyboardMenu);
		preferencesMenu.add(mediaPlayerMenu);

		JMenuBar bar = new JMenuBar();
		bar.add(projectMenu);
		bar.add(preferencesMenu);
		return bar;
	}
	private boolean getSave() {
		if (qd.pane.getDocument().getLength() == 0 && qd.speakerTable.getRowCount() == 0)
			return true; //nothing to save

		JFileChooser fc = new JFileChooser();
		if (qd.project.getTranscript() == null) {
			if (qd.project.getMedia() == null)
				fc.setSelectedFile(null);
			else {
				String path = qd.project.getMedia().getPath();
				path = path.substring(0, path.lastIndexOf('.')) + QDShell.dotQuillDriver;
				fc.setSelectedFile(new File(path));
			}
			if (fc.showSaveDialog(QDShell.this) == JFileChooser.APPROVE_OPTION) {
				File f = fc.getSelectedFile();
				qd.project.setTranscript(f);
			} else
				return false;
		}
		return qd.saveTranscript();
	}
	private class QDFileFilter extends javax.swing.filechooser.FileFilter {
		// accepts all directories and all savant files

		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			}
			return f.getName().toLowerCase().endsWith(QDShell.dotQuillDriver);
		}
    
		//the description of this filter
		public String getDescription() {
			return "QD File Format (" + QDShell.dotQuillDriver + ")";
		}
	}
}
