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

import org.thdl.media.*;
import org.thdl.util.ThdlDebug;
import org.thdl.util.ThdlActionListener;
import org.thdl.util.ThdlOptions;
import org.thdl.tib.input.JskadKeyboardManager;
import org.thdl.tib.input.JskadKeyboardFactory;
import org.thdl.tib.input.JskadKeyboard;
import org.thdl.util.ThdlI18n;

import org.thdl.savant.JdkVersionHacks;


public class QDShell extends JFrame {

    /** the middleman that keeps code regarding Tibetan keyboards
     *  clean */
    private final static JskadKeyboardManager keybdMgr
		= new JskadKeyboardManager(JskadKeyboardFactory.getAllAvailableJskadKeyboards());

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
				ThdlI18n.setLocale(locale);
			}

			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			catch (Exception e) {
			}

			QDShell qdsh = new QDShell();
			qdsh.setVisible(true);
		} catch (NoClassDefFoundError err) {
			ThdlDebug.handleClasspathError("QuillDriver's CLASSPATH", err);
		}
	}

	public QDShell() {
		setTitle("QuillDriver");
		messages = ThdlI18n.getResourceBundle();

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

		qd = new QD();
		getContentPane().add(qd);
		setJMenuBar(getQDShellMenu());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter () {
			public void windowClosing (WindowEvent e) {
				if (getWantSave())
					getSave();
				System.exit(0);
			}
		});
	}

	public JMenuBar getQDShellMenu() {
		JMenu projectMenu = new JMenu(messages.getString("Project"));

		JMenuItem newItem = new JMenuItem(messages.getString("New"));
		newItem.addActionListener(new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e) {
				if (getWantSave())
					getSave();
				qd.clearProject();
			}
		});

		JMenuItem openVideoItem = new JMenuItem(messages.getString("OpenMediaFile"));
		openVideoItem.addActionListener(new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e) {
				if (getWantSave())
					getSave();

				JFileChooser fc = new JFileChooser();
				if (fc.showDialog(QDShell.this, messages.getString("SelectMedia")) == JFileChooser.APPROVE_OPTION) {
					File mediaFile = fc.getSelectedFile();
					try {
						qd.newProject(mediaFile.toURL());
					} catch (MalformedURLException murle) {
						murle.printStackTrace();
						ThdlDebug.noteIffyCode();
					}
				}
			}
		});

		JMenuItem openURLItem = new JMenuItem(messages.getString("OpenMediaURL"));
		openURLItem.addActionListener(new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e) {
				String url = JOptionPane.showInputDialog(QDShell.this,
					messages.getString("TypeMediaURL"));
				if (url != null) {
					if (getWantSave())
						getSave();

					try {
						qd.newProject(new URL(url));
					} catch (MalformedURLException murle) {
						murle.printStackTrace();
						ThdlDebug.noteIffyCode();
					}
				}
			}
		});

		JMenuItem openItem = new JMenuItem(messages.getString("OpenTranscript"));
		openItem.addActionListener(new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e) {
				if (getWantSave())
					getSave();

				JFileChooser fc = new JFileChooser();
				fc.addChoosableFileFilter(new QDFileFilter());
				if (fc.showDialog(QDShell.this, messages.getString("OpenTranscript")) == JFileChooser.APPROVE_OPTION) {
					File transcriptFile = fc.getSelectedFile();
					qd.loadTranscript(transcriptFile);
				}
			}
		});

/*
		JMenuItem closeItem = new JMenuItem(messages.getString("Close"));
		closeItem.addActionListener(new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e) {
				if (getSave())
					qd.clearProject();
			}
		});
*/

		JMenuItem saveItem = new JMenuItem(messages.getString("Save"));
		saveItem.addActionListener(new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e) {
				getSave();
			}
		});
		JMenuItem quitItem = new JMenuItem(messages.getString("Quit"));
		quitItem.addActionListener(new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e) {
				if (getWantSave())
					getSave();
				System.exit(0);
			}
		});
		projectMenu.add(newItem);
		projectMenu.addSeparator();
		projectMenu.add(openVideoItem);
		projectMenu.add(openURLItem);
		projectMenu.add(openItem);
		projectMenu.addSeparator();
//		projectMenu.add(closeItem);
//		projectMenu.addSeparator();
		projectMenu.add(saveItem);
		projectMenu.addSeparator();
		projectMenu.add(quitItem);

	//Tibetan-specific value: remove in non-Tibetan version
	//non-Tibetan specific version would have Transcription Language option here instead


/* 	Note that on the Mac, you radio buttons don't appear possible as
	submenus on menu bars. They just don't show up.

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

*/

		JMenu keyboardMenu = new JMenu(messages.getString("Keyboard"));

        for (int i = 0; i < keybdMgr.size(); i++) {
            final JskadKeyboard kbd = keybdMgr.elementAt(i);
            if (kbd.hasQuickRefFile()) {
                JMenuItem keybdItem = new JMenuItem(kbd.getIdentifyingString());
                keybdItem.addActionListener(new ThdlActionListener() {
                        public void theRealActionPerformed(ActionEvent e) {
                            qd.changeKeyboard(kbd);
                        }
                    });
                keyboardMenu.add(keybdItem);
            }
        }

		JMenu mediaPlayerMenu = new JMenu(messages.getString("MediaPlayer"));
		java.util.List moviePlayers = SmartPlayerFactory.getAllAvailableSmartPlayers();
		for (int i=0; i<moviePlayers.size(); i++) {
			final SmartMoviePanel mPlayer = (SmartMoviePanel)moviePlayers.get(i);
			JMenuItem mItem = new JMenuItem(mPlayer.getIdentifyingName());
			mItem.addActionListener(new ThdlActionListener() {
				public void theRealActionPerformed(ActionEvent e) {
					qd.setMediaPlayer(mPlayer);
				}
			});
			mediaPlayerMenu.add(mItem);
		}

		JMenu preferencesMenu = new JMenu(messages.getString("Preferences"));
		preferencesMenu.add(keyboardMenu);
		if (moviePlayers.size() > 0) {
			SmartMoviePanel defaultPlayer = (SmartMoviePanel)moviePlayers.get(0);
			qd.setMediaPlayer(defaultPlayer); //set qd media player to default
			if (moviePlayers.size() > 1)
				preferencesMenu.add(mediaPlayerMenu);
		}

		JMenuBar bar = new JMenuBar();
		bar.add(projectMenu);
		bar.add(preferencesMenu);
		return bar;
	}
	private boolean getWantSave() {
		if (qd.pane.getDocument().getLength() == 0 && qd.speakerTable.getRowCount() == 0)
			return false; //nothing to save

		if (JOptionPane.showConfirmDialog(QDShell.this, messages.getString("WantSave"), null, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.YES_OPTION)
			return true;
		else
			return false;
	}
	private void getSave() {
		if (qd.pane.getDocument().getLength() == 0 && qd.speakerTable.getRowCount() == 0)
			return; //nothing to save

		JFileChooser fc = new JFileChooser();
		if (qd.project.getTranscript() == null) {
			if (qd.project.getMedia() == null)
				fc.setSelectedFile(null);
			else {
				String path = qd.project.getMedia().getPath();
				path = path.substring(0, path.lastIndexOf('.')) + QDShell.dotQuillDriver;
				fc.setSelectedFile(new File(path));
			}
		} else
			fc.setSelectedFile(qd.project.getTranscript());
		
		int op = fc.showSaveDialog(QDShell.this);
		if (op == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			qd.project.setTranscript(f);
			qd.saveTranscript();
		}
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
