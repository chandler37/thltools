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

package org.thdl.savant;

import java.io.*;
import java.awt.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.*;
import javax.swing.text.*;
import javax.swing.text.rtf.*;

import org.thdl.savant.ucuchi.*;
import org.thdl.savant.tib.*;
import org.thdl.media.*;
import org.thdl.util.TeeStream;
import org.thdl.util.ThdlDebug;
import org.thdl.util.ThdlActionListener;
import org.thdl.util.RTFPane;
import org.thdl.util.SimpleFrame;
import org.thdl.util.ThdlI18n;
import org.thdl.util.ThdlOptions;


public class SavantShell extends JFrame
{
	private static int numberOfSavantsOpen = 0;
	private static JScrollPane helpPane = null;
	private static JScrollPane aboutPane = null;
	private static String mediaPath = null;
	private static JFileChooser fileChooser;

	private Savant savant = null;

	static Locale locale = null;
	ResourceBundle messages = null;

	public static void main(String[] args) {
		try {
			ThdlDebug.attemptToSetUpLogFile("savant", ".log");

			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) { ThdlDebug.noteIffyCode(); }
          
            try {
                helpPane = new RTFPane(SavantShell.class, "savanthelp.rtf");
                aboutPane = new RTFPane(SavantShell.class, "aboutsavant.rtf");
            } catch (FileNotFoundException e) {
				System.out.println("Can't find " + e.getMessage() + ".");
                System.exit(1);
            } catch (IOException e) {
				System.out.println("Can't find one of savanthelp.rtf or aboutsavant.rtf.");
				System.exit(1);
            } catch (BadLocationException e) {
				System.out.println("Error reading one of savanthelp.rtf or aboutsavant.rtf.");
				System.exit(1);
            } catch (Exception e) {
                /* Carry on. A security exception, probably. */
                ThdlDebug.noteIffyCode();
            }

 			if (args.length == 2) {
				locale = new Locale(new String(args[0]), new String(args[1]));
				ThdlI18n.setLocale(locale);
			}

			initFileChooser();
			SavantShell ssh = new SavantShell();
			ssh.setVisible(true);
		} catch (NoClassDefFoundError err) {
			ThdlDebug.handleClasspathError("Savant's CLASSPATH", err);
		}
	}

	public static void initFileChooser() {
		fileChooser = new JFileChooser();
		fileChooser.setFileView(new SavantFileView());
		fileChooser.addChoosableFileFilter(new FileFilter() {
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}
				return f.getName().toLowerCase().endsWith(SavantFileView.getDotSavant());
			}
    			//the description of this filter
			public String getDescription() {
				return "Savant data (.savant)";
			}
		});
	}

	/** Closes one open title, if there is one open. Returns true if
        one was closed, or false if no titles are open. */
	private boolean closeOneSavantTitle() {
		if (savant != null) {
			ThdlDebug.verify("There should be one or more Savant titles open: ",
							 numberOfSavantsOpen >= 1);
			if (numberOfSavantsOpen < 2) {
				savant.close();
				savant = null;
				getContentPane().removeAll();
				setTitle("Savant");
				invalidate();
				validate();
				repaint();
			} else {
				savant.close();
				dispose();
			}
			numberOfSavantsOpen--;
			return true;
		} else {
			ThdlDebug.verify("There should be zero Savant titles open: ",
							 numberOfSavantsOpen == 0);
			return false;
		}
	}

	public SavantShell()
	{
		setTitle("Savant");
		messages = ThdlI18n.getResourceBundle();
		savant = new Savant();

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu(messages.getString("File"));

		JMenuItem openItem = new JMenuItem(messages.getString("Open"));
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,java.awt.Event.CTRL_MASK));
		openItem.addActionListener(new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e) {
				if (fileChooser.showOpenDialog(SavantShell.this) != JFileChooser.APPROVE_OPTION)
					return;
				Properties p = new Properties();
				try {
					File fileChosen = fileChooser.getSelectedFile();
					p.load(new FileInputStream(fileChosen));
					String path = "file:" + fileChosen.getParent() + File.separatorChar;
					newSavantWindow(p.getProperty("PROJECT"), p.getProperty("TITLE"), new URL(path + p.getProperty("TRANSCRIPT")), new URL(path + p.getProperty("MEDIA")), null);
				} catch (MalformedURLException murle) {
					murle.printStackTrace();
					ThdlDebug.noteIffyCode();
				} catch (FileNotFoundException fnfe) {
					fnfe.printStackTrace();
					ThdlDebug.noteIffyCode();
				} catch (IOException ioe) {
					ioe.printStackTrace();
					ThdlDebug.noteIffyCode();
				}
			}
		});


		JMenuItem closeItem = new JMenuItem(messages.getString("Close"));
		closeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,java.awt.Event.CTRL_MASK));
		closeItem.addActionListener(new ThdlActionListener()
		{
			public void theRealActionPerformed(ActionEvent e)
			{
				// Return value doesn't matter:
				closeOneSavantTitle();
			}
		});

		JMenuItem quitItem = new JMenuItem(messages.getString("Quit"));
		quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,java.awt.Event.CTRL_MASK));
		quitItem.addActionListener(new ThdlActionListener()
		{
			public void theRealActionPerformed(ActionEvent e)
			{
				// Close all Savant titles:
				while (closeOneSavantTitle())
					;

				// Exit normally:
				System.exit(0);
			}
		});	

		/* Hey developers: To test ThdlActionListener, use this:

		   JMenuItem errorThrowerItem = new JMenuItem("ErrorThrower");
		   errorThrowerItem.addActionListener(new ThdlActionListener()
		   {
		   public void theRealActionPerformed(ActionEvent e)
		   {
		   throw new Error("Testing ThdlActionListener");
		   }
		   });
		   fileMenu.add(errorThrowerItem);
		*/

		fileMenu.add(openItem);
		fileMenu.add(closeItem);
		fileMenu.addSeparator();
		fileMenu.add(quitItem);


		JMenu mediaPlayerMenu = new JMenu(messages.getString("MediaPlayer"));
		java.util.List moviePlayers = SmartPlayerFactory.getAllAvailableSmartPlayers();
		for (int i=0; i<moviePlayers.size(); i++) {
			final SmartMoviePanel mPlayer = (SmartMoviePanel)moviePlayers.get(i);
			JMenuItem mItem = new JMenuItem(mPlayer.getIdentifyingName());
			mItem.addActionListener(new ThdlActionListener() {
				public void theRealActionPerformed(ActionEvent e) {
					savant.setMediaPlayer(mPlayer);
				}
			});
			mediaPlayerMenu.add(mItem);
		}

		JMenu preferencesMenu = new JMenu(messages.getString("Preferences"));
		if (moviePlayers.size() > 0) {
			SmartMoviePanel defaultPlayer = (SmartMoviePanel)moviePlayers.get(0);
			savant.setMediaPlayer(defaultPlayer); //set savant media player to default
			if (moviePlayers.size() > 1)
				preferencesMenu.add(mediaPlayerMenu);
		}

		JMenu infoMenu = new JMenu(messages.getString("InfoShort"));
		JMenuItem helpItem = new JMenuItem(messages.getString("Help"));
		helpItem.addActionListener(new ThdlActionListener()
		{
			public void theRealActionPerformed(ActionEvent e)
			{
				new SimpleFrame(messages.getString("Help"), helpPane);
			}
		});
		JMenuItem aboutItem = new JMenuItem(messages.getString("About"));
		aboutItem.addActionListener(new ThdlActionListener()
		{
			public void theRealActionPerformed(ActionEvent e)
			{
				new SimpleFrame(messages.getString("About"), aboutPane);
			}
		});
		infoMenu.add(helpItem);
		infoMenu.add(aboutItem);

		//make heavyweight to mix better with jmf video player
		
		fileMenu.getPopupMenu().setLightWeightPopupEnabled(false);
		infoMenu.getPopupMenu().setLightWeightPopupEnabled(false);

		menuBar.add(fileMenu);
		menuBar.add(preferencesMenu);
		menuBar.add(infoMenu);
		setJMenuBar(menuBar);

		addWindowListener(new WindowAdapter () {
			public void windowClosing (WindowEvent e) {
				if (numberOfSavantsOpen < 2)
					System.exit(0);
				if (savant != null)
					savant.close();
				numberOfSavantsOpen--;
			}
		});

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
	}

	public void newSavantWindow(String project, String titleName, URL trn, URL vid, URL abt)
	{
		try {
			if (numberOfSavantsOpen == 0) {
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				openSavant(project, titleName, trn, vid, abt);
			} else {
				SavantShell scp = new SavantShell();
				scp.setVisible(true);
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				scp.openSavant(project, titleName, trn, vid, abt);
			}
			numberOfSavantsOpen++;
		} catch (NoClassDefFoundError err) {
			ThdlDebug.handleClasspathError("Savant's CLASSPATH", err);
		}
	}

	public void openSavant(String project, String titleName, URL trn, URL vid, URL abt)
	{
		setTitle(titleName);
		setContentPane(savant);
		validate();
		savant.paintImmediately(0,0,savant.getWidth(),savant.getHeight());

		InputStreamReader isr = null;
		try {
			InputStream in = trn.openStream();
			isr = new InputStreamReader(in, "UTF8");
		} catch (IOException ioe) {
			System.out.println("input stream error");
			return;
		}

		if (project.equals("Ucuchi")) {
			TranscriptView[] views = new TranscriptView[5];
			views[0] = new org.thdl.savant.ucuchi.Quechua(isr);
			views[1] = new org.thdl.savant.ucuchi.SegmentedQuechua(views[0].getDocument());
			views[2] = new org.thdl.savant.ucuchi.English(views[0].getDocument());
			views[3] = new org.thdl.savant.ucuchi.QuechuaEnglish(views[0].getDocument());
			views[4] = new org.thdl.savant.ucuchi.All(views[0].getDocument());
			savant.open(views, vid, abt);
			return;
		}

		if (project.equals("THDL")) {
            // See if we have the TMW fonts available.  If we do,
            // allow showing Tibetan as well as Wylie and English.

            int i=0;
            if (!ThdlOptions.getBooleanOption("thdl.rely.on.system.tmw.fonts")) {
                // We do have the TMW fonts available because we
                // manually loaded them.
                i = -1;
            } else {
                // DLC FIXME: scan for this in TibetanMachineWeb.java
                // before manually loading the TMW fonts.
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                String fonts[] = ge.getAvailableFontFamilyNames();
                for (; i<fonts.length; i++) {
                    if (fonts[i].equals("TibetanMachineWeb"))
                        {
                            i=-1;
                            break;
                        }
                }
            }
			if (i!=-1) {
                JOptionPane.showMessageDialog(this,
                                              "If you want to see text in Tibetan script, "+
                                              "please set the option " +
                                              "thdl.rely.on.system.tmw.fonts" +
                                              " to false.",
                                              "Note", JOptionPane.INFORMATION_MESSAGE);

				TranscriptView[] views = new TranscriptView[3];
				views[0] = new org.thdl.savant.tib.Wylie(isr);
				views[1] = new org.thdl.savant.tib.English(views[0].getDocument());
				views[2] = new org.thdl.savant.tib.WylieEnglish(views[0].getDocument());
				savant.open(views, vid, abt);
			} else {
				TranscriptView[] views = new TranscriptView[7];
				views[0] = new org.thdl.savant.tib.Tibetan(isr);
				views[1] = new org.thdl.savant.tib.TibetanWylieEnglish(views[0].getDocument());
				views[2] = new org.thdl.savant.tib.TibetanEnglish(views[0].getDocument());
				views[3] = new org.thdl.savant.tib.TibetanWylie(views[0].getDocument());
				views[4] = new org.thdl.savant.tib.WylieEnglish(views[0].getDocument());
				views[5] = new org.thdl.savant.tib.English(views[0].getDocument());
				views[6] = new org.thdl.savant.tib.Wylie(views[0].getDocument());
				savant.open(views, vid, abt);
			}
		}
	}
}
