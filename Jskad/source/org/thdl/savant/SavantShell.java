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
import java.awt.event.*;
import javax.swing.text.*;
import javax.swing.text.rtf.*;

import org.thdl.savant.ucuchi.*;
import org.thdl.savant.tib.*;

public class SavantShell extends JFrame
{
	private static int numberOfSavantsOpen = 0;
	private static JScrollPane helpPane;
	private static JScrollPane aboutPane;
	private static String mediaPath = null;

	private JFileChooser fileChooser;
	private javax.swing.filechooser.FileFilter savantFilter;
	private Savant savant = null;

	public static void main(String[] args) {
		try {
			java.io.PrintStream ps = new java.io.PrintStream(new java.io.FileOutputStream("savant.log"));
			System.setErr(ps);
			System.setOut(ps);
		}
		catch (Exception e) {
		}

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}

		RTFEditorKit rtf = new RTFEditorKit();
		InputStream in1 = SavantShell.class.getResourceAsStream("savanthelp.rtf");
		InputStream in2 = SavantShell.class.getResourceAsStream("aboutsavant.rtf");
		DefaultStyledDocument doc1 = new DefaultStyledDocument();
		DefaultStyledDocument doc2 = new DefaultStyledDocument();
		try {
			rtf.read(in1, doc1, 0);
			rtf.read(in2, doc2, 0);
		} catch (BadLocationException ioe) {
			return;
		} catch (IOException ioe) {
			System.out.println("can't find savanthelp or aboutsavant");
			return;
		}

		JTextPane pane1 = new JTextPane(doc1);
		JTextPane pane2 = new JTextPane(doc2);
		pane1.setEditable(false);
		pane2.setEditable(false);

		helpPane = new JScrollPane(pane1);
		aboutPane = new JScrollPane(pane2);

		SavantShell ssh = new SavantShell();
		ssh.setVisible(true);
		ssh.initFileChooser();
	}

	public void initFileChooser() {
		fileChooser = new JFileChooser();
		fileChooser.setFileView(new SavantFileView());
		savantFilter = new SavantFilter();
		fileChooser.addChoosableFileFilter(savantFilter);
	}

	public void setFileChooser(JFileChooser fc) {
		fileChooser = fc;
	}

	private class SavantFilter extends javax.swing.filechooser.FileFilter {
		// accepts all directories and all savant files

		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			}

			String fName = f.getName();
			int i = fName.lastIndexOf('.');

			if (i < 0)
				return false;

			else {
				String ext = fName.substring(i+1).toLowerCase();

				if (ext.equals("savant"))
					return true;
				else
					return false;
			}
		}
    
		//the description of this filter
		public String getDescription() {
			return "Savant data (.savant)";
		}
	}

	public SavantShell()
	{
		setTitle("Savant");

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");

		JMenuItem openItem = new JMenuItem("Open");
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,2));
		openItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
				} catch (FileNotFoundException fnfe) {
					fnfe.printStackTrace();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		});

		JMenuItem closeItem = new JMenuItem("Close");
		closeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,2));
		closeItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (savant != null)
				{
					if (numberOfSavantsOpen < 2)
					{
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
				}		
			}
		});	

		fileMenu.add(openItem);
		fileMenu.add(closeItem);

		JMenu infoMenu = new JMenu("Information");
		JMenuItem helpItem = new JMenuItem("Help");
		helpItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JFrame h = new JFrame("Help");
				h.setSize(500,400);
				Container c = h.getContentPane();
				c.addComponentListener(new ComponentAdapter()
				{
					public void componentResized(ComponentEvent e)
					{
						helpPane.setSize(e.getComponent().getSize());
					}
				});
				helpPane.setSize(c.getSize());
				c.add(helpPane);
				h.setLocation(100,100);
				h.setVisible(true);
			}
		});
		JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JFrame h = new JFrame("About");
				h.setSize(500,400);
				Container c = h.getContentPane();
				c.addComponentListener(new ComponentAdapter()
				{
					public void componentResized(ComponentEvent e)
					{
						aboutPane.setSize(e.getComponent().getSize());
					}
				});
				aboutPane.setSize(c.getSize());
				c.add(aboutPane);
				h.setLocation(100,100);
				h.setVisible(true);
			}
		});
		infoMenu.add(helpItem);
		infoMenu.add(aboutItem);

		//make heavyweight to mix better with jmf video player
		
		fileMenu.getPopupMenu().setLightWeightPopupEnabled(false);
		infoMenu.getPopupMenu().setLightWeightPopupEnabled(false);

		menuBar.add(fileMenu);
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

		/* Code for JDK 1.3, including Mac OS X
			Dimension gs = g.getToolkit().getScreenSize();
			g.setLocation(0,0);
			g.setSize(new Dimension(gs.width, gs.height));
		*/
	}

	public void newSavantWindow(String project, String titleName, URL trn, URL vid, URL abt)
	{
		if (numberOfSavantsOpen == 0)
			openSavant(project, titleName, trn, vid, abt);
		else {
			SavantShell scp = new SavantShell();
			scp.setVisible(true);
			scp.openSavant(project, titleName, trn, vid, abt);
			scp.setFileChooser(fileChooser);
		}
		numberOfSavantsOpen++;
	}

	public void openSavant(String project, String titleName, URL trn, URL vid, URL abt)
	{
		setTitle(titleName);
		savant = new Savant();
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
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			String fonts[] = ge.getAvailableFontFamilyNames();
			int i=0;
			for (; i<fonts.length; i++)
				if (fonts[i].equals("TibetanMachineWeb"))
				{
					i=-1;
					break;
				}

			if (i!=-1)
			{
				JOptionPane.showMessageDialog(this, 
					"If you want to see text in Tibetan script, "+
					"please visit www.thdl.org to download and "+
					"install the Tibetan Machine Web fonts.",
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
