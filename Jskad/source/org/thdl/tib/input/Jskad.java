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

import java.io.*;
import java.net.URL;
import java.awt.*;
import java.awt.event.*;

import java.awt.print.*;
import javax.swing.plaf.basic.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.rtf.*;

import org.thdl.tib.text.*;
import org.thdl.util.ThdlDebug;
import org.thdl.util.ThdlOptions;
import org.thdl.util.StatusBar;
import org.thdl.util.ThdlActionListener;
import org.thdl.util.RTFPane;
import org.thdl.util.SimpleFrame;
import org.thdl.util.ThdlLazyException;



/**
* A simple Tibetan text editor. Jskad editors lack most of the
* functionality of a word-processor, but they do provide
* multiple keyboard input methods, as well as 
* conversion routines to go back and forth between Extended
* Wylie and TibetanMachineWeb.
* <p>
* Jskad embeds {@link DuffPane DuffPane}, which is the editing
* window where the keyboard logic and most functionality is housed.
* <p>
* Depending on the object passed to the constructor,
* a Jskad object can be used in either an application or an
* applet.
* @author Edward Garrett, Tibetan and Himalayan Digital Library
* @version 1.0
*/
public class Jskad extends JPanel implements DocumentListener {

    /** the name of the property a developer should set to see
        low-level info on how keypresses in "Tibetan" input mode are
        being interpreted */
    private final static String enableKeypressStatusProp
        = "thdl.Jskad.enable.tibetan.mode.status";

    /* The .rtf files named below better be included in the jar in the
       same directory as 'Jskad.class'. */
    private final String keybd1Description = "Extended Wylie Keyboard";
    private final String keybd1HelpFile = "Wylie_keyboard.rtf";

    private final String keybd2Description = "TCC Keyboard #1";
    private final String keybd2HelpFile = "TCC_keyboard_1.rtf";

    private final String keybd3Description = "TCC Keyboard #2";
    private final String keybd3HelpFile = "TCC_keyboard_2.rtf";

    private final String keybd4Description = "Sambhota Keymap One";
    private final String keybd4HelpFile = "Sambhota_keymap_one.rtf";


	private JComboBox fontFamilies, fontSizes;
	private JFileChooser fileChooser;
	private javax.swing.filechooser.FileFilter rtfFilter;
	private javax.swing.filechooser.FileFilter txtFilter;
	private int fontSize = 0;
	private Object parentObject = null;
	private static int numberOfTibsRTFOpen = 0;
	private static int x_size;
	private static int y_size;

/**
* The text editing window which this Jskad object embeds.
*/
	public DuffPane dp;
/**
* Has the document been saved since it was last changed?
*/
	public boolean hasChanged = false;

/**
* The filename, if any, associated with this instance of Jskad.
*/
	public String fileName = null;

    /** the status bar for this frame */
    private StatusBar statusBar;

/**
* @param parent the object that embeds this instance of Jskad.
* Supported objects include JFrames and JApplets. If the parent
* is a JApplet then the File menu is omitted from the menu bar.
*/
	public Jskad(final Object parent) {
        if (ThdlOptions.getBooleanOption("thdl.Jskad.disable.status.bar")) {
            statusBar = null;
        } else {
            statusBar
                = new StatusBar(ThdlOptions.getStringOption("thdl.Jskad.initial.status.message",
                                                            "Welcome to Jskad!"));
        }
		parentObject = parent;
		numberOfTibsRTFOpen++;
		JMenuBar menuBar = new JMenuBar();

		if (parentObject instanceof JFrame || parentObject instanceof JInternalFrame) {
			fileChooser = new JFileChooser();
			rtfFilter = new RTFFilter();
			txtFilter = new TXTFilter();
			fileChooser.addChoosableFileFilter(rtfFilter);

			JMenu fileMenu = new JMenu("File");

			JMenuItem newItem = new JMenuItem("New");
//			newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,2)); //Ctrl-n
			newItem.addActionListener(new ThdlActionListener() {
				public void theRealActionPerformed(ActionEvent e) {
					newFile();
				}
			});
			fileMenu.add(newItem);

			JMenuItem openItem = new JMenuItem("Open");
//			openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,2)); //Ctrl-o
			openItem.addActionListener(new ThdlActionListener() {
				public void theRealActionPerformed(ActionEvent e) {
					openFile();
				}
			});
			fileMenu.add(openItem);

			if (parentObject instanceof JFrame) {
				JMenuItem closeItem = new JMenuItem("Close");	
				closeItem.addActionListener(new ThdlActionListener() {
					public void theRealActionPerformed(ActionEvent e) {
						if (!hasChanged || hasChanged && checkSave()) {
							numberOfTibsRTFOpen--;

							if (numberOfTibsRTFOpen == 0)
								System.exit(0);
							else {
								final JFrame parentFrame = (JFrame)parentObject;
								parentFrame.dispose();
							}
						}
					}
				});
				fileMenu.add(closeItem);
			}

			JMenuItem saveItem = new JMenuItem("Save");
//			saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,2)); //Ctrl-s
			saveItem.addActionListener(new ThdlActionListener() {
				public void theRealActionPerformed(ActionEvent e) {
					if (fileName == null)
						saveAsFile();
					else
						saveFile();
				}

			});
			fileMenu.addSeparator();
			fileMenu.add(saveItem);

			JMenuItem saveAsItem = new JMenuItem("Save as");
			saveAsItem.addActionListener(new ThdlActionListener() {
				public void theRealActionPerformed(ActionEvent e) {
					saveAsFile();
				}
			});
			fileMenu.add(saveAsItem);

			menuBar.add(fileMenu);
		}

		JMenu editMenu = new JMenu("Edit");

		if (parentObject instanceof JFrame || parentObject instanceof JInternalFrame) {
			JMenuItem cutItem = new JMenuItem("Cut");
			cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,2)); //Ctrl-x
			cutItem.addActionListener(new ThdlActionListener() {
				public void theRealActionPerformed(ActionEvent e) {
					cutSelection();					
				}
			});
			editMenu.add(cutItem);

			JMenuItem copyItem = new JMenuItem("Copy");
			copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,2)); //Ctrl-c
			copyItem.addActionListener(new ThdlActionListener() {
				public void theRealActionPerformed(ActionEvent e) {
					copySelection();
				}
			});
			editMenu.add(copyItem);

			JMenuItem pasteItem = new JMenuItem("Paste");
			pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,2)); //Ctrl-v
			pasteItem.addActionListener(new ThdlActionListener() {
				public void theRealActionPerformed(ActionEvent e) {
					pasteSelection();
				}
			});
			editMenu.add(pasteItem);
			editMenu.addSeparator();

			JMenuItem selectallItem = new JMenuItem("Select All");
			selectallItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,2)); //Ctrl-a
			selectallItem.addActionListener(new ThdlActionListener() {
				public void theRealActionPerformed(ActionEvent e) {
					dp.setSelectionStart(0);
					dp.setSelectionEnd(dp.getDocument().getLength());
				}
			});
			editMenu.add(selectallItem);
		}

		JMenuItem preferencesItem = new JMenuItem("Preferences");
		preferencesItem.addActionListener(new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e) {
				getPreferences();
			}
		});
		editMenu.addSeparator();
		editMenu.add(preferencesItem);

		menuBar.add(editMenu);

		JMenu toolsMenu = new JMenu("Tools");

		JMenuItem TMWWylieItem = new JMenuItem("Convert Tibetan to Wylie");
		TMWWylieItem.addActionListener(new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e) {
				toWylie();
			}
		});
		toolsMenu.add(TMWWylieItem);

		JMenuItem wylieTMWItem = new JMenuItem("Convert Wylie to Tibetan");
		wylieTMWItem.addActionListener(new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e) {
				toTibetan();
			}
		});
		toolsMenu.add(wylieTMWItem);

		if (parentObject instanceof JFrame || parentObject instanceof JInternalFrame) {
			JMenuItem importItem = new JMenuItem("Import Wylie as Tibetan");
			importItem.addActionListener(new ThdlActionListener() {
				public void theRealActionPerformed(ActionEvent e) {
					importWylie();
				}
			});
			toolsMenu.addSeparator();
			toolsMenu.add(importItem);
		}

        if (ThdlOptions.getBooleanOption("thdl.add.developer.options.to.menu")) {
            toolsMenu.addSeparator();
            JMenuItem DevelItem = new JMenuItem("Toggle read-only");
            DevelItem.addActionListener(new ThdlActionListener() {
                    public void theRealActionPerformed(ActionEvent e) {
                        dp.setEditable(!dp.isEditable());
                    }
                });
            toolsMenu.add(DevelItem);
        }

		menuBar.add(toolsMenu);

		JMenu infoMenu = new JMenu("Info");

		JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.addActionListener(new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(Jskad.this,
					"Copyright 2001-2002 Tibetan and Himalayan Digital Library\n"+
					"Programmed by Edward Garrett\n\n"+
					"This software is protected by the terms of the\n"+
					"THDL Open Community License, Version 1.0.\n"+
					"It uses Tibetan Computer Company (http://www.tibet.dk/tcc/)\n"+
					"fonts created by Tony Duff and made available by the\n"+
					"Trace Foundation (http://trace.org/).\n\n"+
					"For more information, or to download the source code\n"+
					"for Jskad, see our web site:\n"+
					"     http://www.thdl.org/",
                    "About Jskad 1.0", /* FIXME HARD-CODED VERSION NUMBER */
					JOptionPane.PLAIN_MESSAGE);
			}
		});

        {
            JMenuItem keybd1Item = new JMenuItem(keybd1Description);
            keybd1Item.addActionListener(new ThdlActionListener() {
                    public void theRealActionPerformed(ActionEvent e) {
                        new SimpleFrame(keybd1Description, getKeybd1RTFPane());
                    }
                });
            infoMenu.add(keybd1Item);
        }

        {
            JMenuItem keybd2Item = new JMenuItem(keybd2Description);
            keybd2Item.addActionListener(new ThdlActionListener() {
                    public void theRealActionPerformed(ActionEvent e) {
                        new SimpleFrame(keybd2Description, getKeybd2RTFPane());
                    }
                });
            infoMenu.add(keybd2Item);
        }

        {
            JMenuItem keybd3Item = new JMenuItem(keybd3Description);
            keybd3Item.addActionListener(new ThdlActionListener() {
                    public void theRealActionPerformed(ActionEvent e) {
                        new SimpleFrame(keybd3Description, getKeybd3RTFPane());
                    }
                });
            infoMenu.add(keybd3Item);
        }

        {
            JMenuItem keybd4Item = new JMenuItem(keybd4Description);
            keybd4Item.addActionListener(new ThdlActionListener() {
                    public void theRealActionPerformed(ActionEvent e) {
                        new SimpleFrame(keybd4Description, getKeybd4RTFPane());
                    }
                });
            infoMenu.add(keybd4Item);
        }

		infoMenu.addSeparator();

		infoMenu.add(aboutItem);

		menuBar.add(infoMenu);

		JToolBar toolBar = new JToolBar();
		toolBar.setBorder(null);
		toolBar.addSeparator();
		toolBar.add(new JLabel("Input mode:"));
		toolBar.addSeparator();

		String[] input_modes = {"Tibetan","Roman"};
		final JComboBox inputmethods = new JComboBox(input_modes);
		inputmethods.addActionListener(new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e) {
				switch (inputmethods.getSelectedIndex()) {
					case 0: //Tibetan
						if (dp.isRomanMode())
							dp.toggleLanguage();
                        statusBar.replaceStatus("Now inputting Tibetan script");
						break;

					case 1: //Roman
						if (!dp.isRomanMode() && dp.isRomanEnabled())
							dp.toggleLanguage();
                        statusBar.replaceStatus("Now inputting Roman script");
						break;
				}
			}
		});


		toolBar.add(inputmethods);
		toolBar.add(Box.createHorizontalGlue());

		toolBar.add(new JLabel("Keyboard:"));
		toolBar.addSeparator();

        /* Initialize dp before calling installKeyboard. */
        if (ThdlOptions.getBooleanOption(Jskad.enableKeypressStatusProp)) {
            dp = new DuffPane(statusBar);
        } else {
            dp = new DuffPane();
        }


		String[] keyboard_options = {keybd1Description,
                                     keybd2Description,
                                     keybd3Description,
                                     keybd4Description};
		final JComboBox keyboards = new JComboBox(keyboard_options);
        int initialKeyboard
            = ThdlOptions.getIntegerOption("thdl.default.tibetan.keyboard", 0);
        try {
            keyboards.setSelectedIndex(initialKeyboard);
        } catch (IllegalArgumentException e) {
            keyboards.setSelectedIndex(0); // good ol' Wylie
        }
        installKeyboard(initialKeyboard);
		keyboards.addActionListener(new ThdlActionListener() {
			public void theRealActionPerformed(ActionEvent e) {
                installKeyboard(keyboards.getSelectedIndex());
			}
		});
		toolBar.add(keyboards);
		toolBar.add(Box.createHorizontalGlue());

		JScrollPane sp = new JScrollPane(dp, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		dp.getDocument().addDocumentListener(this);

		if (parentObject instanceof JFrame) {
			final JFrame parentFrame = (JFrame)parentObject;
			parentFrame.setJMenuBar(menuBar);
			parentFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			parentFrame.addWindowListener(new WindowAdapter () {
				public void windowClosing (WindowEvent e) {
					if (!hasChanged || hasChanged && checkSave()) {
						numberOfTibsRTFOpen--;
						if (numberOfTibsRTFOpen == 0)
							System.exit(0);
						else
							parentFrame.dispose();
					}
				}
			});
		}

		else if (parentObject instanceof JInternalFrame) {
			final JInternalFrame parentFrame = (JInternalFrame)parentObject;
			parentFrame.setJMenuBar(menuBar);
		}

		else if (parentObject instanceof JApplet) {
			JApplet parentApplet = (JApplet)parentObject;
			parentApplet.setJMenuBar(menuBar);
			dp.disableCutAndPaste();
		}

		setLayout(new BorderLayout());
		add("North", toolBar);
		add("Center", sp);
        if (statusBar != null)
            add("South", statusBar);
	}

	private void getPreferences() {
		GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontNames = genv.getAvailableFontFamilyNames();

		JPanel tibetanPanel;
		JComboBox tibetanFontSizes;

		tibetanPanel = new JPanel();
		tibetanPanel.setBorder(BorderFactory.createTitledBorder("Set Tibetan Font Size"));
		tibetanFontSizes = new JComboBox(new String[] {"8","10","12","14","16","18","20","22","24","26","28","30","32","34","36","48","72"});
		tibetanFontSizes.setMaximumSize(tibetanFontSizes.getPreferredSize());
		tibetanFontSizes.setSelectedItem(String.valueOf(dp.getTibetanFontSize()));
		tibetanFontSizes.setEditable(true);
		tibetanPanel.add(tibetanFontSizes);

		JPanel romanPanel;
		JComboBox romanFontFamilies;
		JComboBox romanFontSizes;

		romanPanel = new JPanel();
		romanPanel.setBorder(BorderFactory.createTitledBorder("Set non-Tibetan Font and Size"));
		romanFontFamilies = new JComboBox(fontNames);
		romanFontFamilies.setMaximumSize(romanFontFamilies.getPreferredSize());
		romanFontFamilies.setSelectedItem(dp.getRomanFontFamily());
		romanFontFamilies.setEditable(true);
		romanFontSizes = new JComboBox(new String[] {"8","10","12","14","16","18","20","22","24","26","28","30","32","34","36","48","72"});
		romanFontSizes.setMaximumSize(romanFontSizes.getPreferredSize());
		romanFontSizes.setSelectedItem(String.valueOf(dp.getRomanFontSize()));
		romanFontSizes.setEditable(true);
		romanPanel.setLayout(new GridLayout(1,2));
		romanPanel.add(romanFontFamilies);
		romanPanel.add(romanFontSizes);

		JPanel preferencesPanel = new JPanel();
		preferencesPanel.setLayout(new GridLayout(2,1));
		preferencesPanel.add(tibetanPanel);
		preferencesPanel.add(romanPanel);

		JOptionPane pane = new JOptionPane(preferencesPanel);
		JDialog dialog = pane.createDialog(this, "Preferences");
		dialog.show();

		int size;
		try {
			size = Integer.parseInt(tibetanFontSizes.getSelectedItem().toString());
		}
		catch (NumberFormatException ne) {
			size = dp.getTibetanFontSize();
		}
		dp.setTibetanFontSize(size);

		String font = romanFontFamilies.getSelectedItem().toString();
		try {
			size = Integer.parseInt(romanFontSizes.getSelectedItem().toString());
		}
		catch (NumberFormatException ne) {
			size = dp.getRomanFontSize();
		}
		dp.setRomanAttributeSet(font, size);
	}

	private void newFile() {
		if (dp.getDocument().getLength()>0 && parentObject instanceof JFrame) {
			JFrame parentFrame = (JFrame)parentObject;
			JFrame newFrame = new JFrame("Jskad");
			Point point = parentFrame.getLocationOnScreen();
			newFrame.setSize(parentFrame.getSize().width, parentFrame.getSize().height);
			newFrame.setLocation(point.x+50, point.y+50);
			newFrame.getContentPane().add(new Jskad(newFrame));
			newFrame.setVisible(true);
		}
		else {
			if (parentObject instanceof JFrame) {
				JFrame parentFrame = (JFrame)parentObject;
				parentFrame.setTitle("Jskad");
			}
			else if (parentObject instanceof JInternalFrame) {
				JInternalFrame parentFrame = (JInternalFrame)parentObject;
				parentFrame.setTitle("Jskad");
			}
			dp.newDocument();
			dp.getDocument().addDocumentListener(Jskad.this);
			hasChanged = false;
		}
	}

	private void openFile() {
		fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(rtfFilter);

		if (dp.getDocument().getLength()>0 && parentObject instanceof JFrame) {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				return;
			}

			final File fileChosen = fileChooser.getSelectedFile();
			final String f_name = fileChosen.getAbsolutePath();

			try {
				JFrame parentFrame = (JFrame)parentObject;
				InputStream in = new FileInputStream(fileChosen);
				JFrame newFrame = new JFrame(f_name);
				Point point = parentFrame.getLocationOnScreen();
				newFrame.setSize(x_size, y_size);
				newFrame.setLocation(point.x+50, point.y+50);
				Jskad newRTF = new Jskad(newFrame);
				newFrame.getContentPane().add(newRTF);
				newRTF.dp.rtfEd.read(in, newRTF.dp.getDocument(), 0);
				newRTF.dp.getDocument().addDocumentListener(newRTF);
				in.close();
				newFrame.setTitle(f_name);
				newRTF.fileName = new String(f_name);
				newRTF.hasChanged = false;
				newRTF.dp.getCaret().setDot(0);
				newFrame.setVisible(true);
			}
			catch (FileNotFoundException fnfe) {
			}
			catch (IOException ioe) {
			}
			catch (BadLocationException ble) {
			}
		}
		else {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				return;
			}

			final File fileChosen = fileChooser.getSelectedFile();
			final String f_name = fileChosen.getAbsolutePath();

			try {
				InputStream in = new FileInputStream(fileChosen);
				dp.newDocument();
				dp.rtfEd.read(in, dp.getDocument(), 0);
				in.close();
				dp.getCaret().setDot(0);
				dp.getDocument().addDocumentListener(Jskad.this);
				hasChanged = false;
				fileName = new String(f_name);

				if (parentObject instanceof JFrame) {
					JFrame parentFrame = (JFrame)parentObject;
					parentFrame.setTitle(fileName);
				}
				else if (parentObject instanceof JInternalFrame) {
					JInternalFrame parentFrame = (JInternalFrame)parentObject;
					parentFrame.setTitle(fileName);
				}
			}
			catch (FileNotFoundException fnfe) {
			}
			catch (IOException ioe) {
			}
			catch (BadLocationException ble) {
			}
		}
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	private void saveFile() {
		String s = getSave(fileName);
		if (null != s) {
			if (parentObject instanceof JFrame) {
				JFrame parentFrame = (JFrame)parentObject;
				parentFrame.setTitle(s);
				fileName = new String(s);
			}
			else if (parentObject instanceof JInternalFrame) {
				JInternalFrame parentFrame = (JInternalFrame)parentObject;
				parentFrame.setTitle(s);
				fileName = new String(s);
			}
		}
	}

	private void saveAsFile() {
		String s = getSaveAs();
		if (null != s) {
			if (parentObject instanceof JFrame) {
				JFrame parentFrame = (JFrame)parentObject;
				parentFrame.setTitle(s);
				fileName = new String(s);
			}
			else if (parentObject instanceof JInternalFrame) {
				JInternalFrame parentFrame = (JInternalFrame)parentObject;
				parentFrame.setTitle(s);
				fileName = new String(s);
			}
		}
	}

	private boolean checkSave() {
		int saveFirst = JOptionPane.showConfirmDialog(this, "Do you want to save your changes?", "Please select", JOptionPane.YES_NO_CANCEL_OPTION);

		switch (saveFirst) {
			case JOptionPane.NO_OPTION: //don't save but do continue
				return true;

			case JOptionPane.YES_OPTION: //save and continue
				if (fileName == null) {
					saveAsFile();
}
				else
					saveFile();
				return true;

			default:
				return false;
		}
	}

	private String getSave(String f_name) {
		File fileChosen = new File(f_name);

		try {
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fileChosen));
			dp.rtfEd.write(out, dp.getDocument(), 0, dp.getDocument().getLength());
			out.flush();
			out.close();
			hasChanged = false;
		} catch (IOException exception) {
			exception.printStackTrace();
			ThdlDebug.noteIffyCode();
			return null;
		} catch (BadLocationException ble) {
			ble.printStackTrace();
			ThdlDebug.noteIffyCode();
		}
		return f_name;
	}

	private String getSaveAs() {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		if (fileName == null)
			fileChooser.setSelectedFile(null);
		else
			fileChooser.setSelectedFile(new File(fileName));

		if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			return null;
		}

		File fileChosen = fileChooser.getSelectedFile();
		String fileName = fileChosen.getAbsolutePath();
		int i = fileName.lastIndexOf('.');

		if (i < 0)
			fileName += ".rtf";
		else
			fileName = fileName.substring(0, i) + ".rtf";

		getSave(fileName);

		fileChooser.rescanCurrentDirectory();
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		return fileName;
	}

	private void cutSelection() {
		dp.copy(dp.getSelectionStart(), dp.getSelectionEnd(), true);
	}

	private void copySelection() {
		dp.copy(dp.getSelectionStart(), dp.getSelectionEnd(), false);
	}

	private void pasteSelection() {
		dp.paste(dp.getCaret().getDot());
	}

	private void toTibetan() {
		Jskad.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		dp.toTibetanMachineWeb();
		Jskad.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	private void toWylie() {
		Jskad.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		dp.toWylie(dp.getSelectionStart(), dp.getSelectionEnd());
		Jskad.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	private void importWylie() {
		fileChooser.removeChoosableFileFilter(rtfFilter);
		fileChooser.addChoosableFileFilter(txtFilter);

		if (fileChooser.showDialog(Jskad.this, "Import Wylie") != JFileChooser.APPROVE_OPTION) {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			fileChooser.removeChoosableFileFilter(txtFilter);
			fileChooser.addChoosableFileFilter(rtfFilter);
			return;
		}

		File txt_fileChosen = fileChooser.getSelectedFile();
		fileChooser.rescanCurrentDirectory();
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		fileChooser.removeChoosableFileFilter(txtFilter);
		fileChooser.addChoosableFileFilter(rtfFilter);

		if (fileChooser.showDialog(Jskad.this, "Save as Tibetan") != JFileChooser.APPROVE_OPTION) {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			return;
		}

		File rtf_fileChosen = fileChooser.getSelectedFile();
		fileChooser.rescanCurrentDirectory();
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		String rtf_fileName = rtf_fileChosen.getAbsolutePath();
		int i = rtf_fileName.lastIndexOf('.');

		if (i < 0)
			rtf_fileName += ".rtf";
		else
			rtf_fileName = fileName.substring(0, i) + ".rtf";

		try {
			BufferedReader in = new BufferedReader(new FileReader(txt_fileChosen));
            DuffPane dp2;
            if (ThdlOptions.getBooleanOption(Jskad.enableKeypressStatusProp)) {
                dp2 = new DuffPane(statusBar);
            } else {
                dp2 = new DuffPane();
            }

			try {
				String val = in.readLine();

				while (val != null) {
					dp2.toTibetanMachineWeb(val + "\n", dp2.getCaret().getDot());
					val = in.readLine();
				}

				TibetanDocument t_doc = (TibetanDocument)dp2.getDocument();
				t_doc.writeRTFOutputStream(new FileOutputStream(new File(rtf_fileName)));
			}
			catch (IOException ioe) {
				System.out.println("problem reading or writing file");
			}
		}
		catch (FileNotFoundException fnfe) {
			System.out.println("problem reading file");
		}
	}

	private TibetanKeyboard sambhota1Keyboard = null, duff1Keyboard = null, duff2Keyboard = null;
	private void installKeyboard(int keyboardIndex) {
        switch (keyboardIndex) {
        case 1: //TCC 1
            if (duff1Keyboard == null)
                duff1Keyboard = installKeyboard("tcc1");
            else
                dp.registerKeyboard(duff1Keyboard);
            break;

        case 2: //TCC 2
            if (duff2Keyboard == null)
                duff2Keyboard = installKeyboard("tcc2");
            else
                dp.registerKeyboard(duff2Keyboard);
            break;

        case 3: //Sambhota
            if (sambhota1Keyboard == null)
                sambhota1Keyboard = installKeyboard("sambhota1");
            else
                dp.registerKeyboard(sambhota1Keyboard);
            break;
        default: //Extended Wylie
            if (0 != keyboardIndex
                && ThdlOptions.getBooleanOption("thdl.debug")) {
                throw new Error("You set thdl.default.tibetan.keyboard to an illegal value: "
                                + keyboardIndex);
            }
            installKeyboard("wylie");
            break;
        }
    }

    private TibetanKeyboard installKeyboard(String name) {
		TibetanKeyboard tk = null;

		if (!name.equalsIgnoreCase("wylie")) {
			URL keyboard_url = null;

			if (name.equalsIgnoreCase("sambhota1"))
				keyboard_url = TibetanMachineWeb.class.getResource("sambhota_keyboard_1.ini");

			else if (name.equalsIgnoreCase("tcc1"))
				keyboard_url = TibetanMachineWeb.class.getResource("tcc_keyboard_1.ini");

			else if (name.equalsIgnoreCase("tcc2"))
				keyboard_url = TibetanMachineWeb.class.getResource("tcc_keyboard_2.ini");

			if (null != keyboard_url) {
				try {
					tk = new TibetanKeyboard(keyboard_url);
				}
				catch (TibetanKeyboard.InvalidKeyboardException ike) {
					System.out.println("invalid keyboard file or file not found");
                    ThdlDebug.noteIffyCode();
					return null;
				}
			}
			else
				return null;
		}

        ThdlDebug.verify("dp != null", dp != null);
		dp.registerKeyboard(tk);
		return tk;
	}

/**
* Allows use of Jskad as dependent JFrame.
* Once you've called this method, users will
* be able to close Jskad without shutting
* down your superordinate application.
*/
	public void makeDependent() {
		numberOfTibsRTFOpen++;
	}

/**
* Fills the editing pane with content. If the
* editing pane already has content, this method does nothing.
*
* @param wylie the string of wylie you want to
* put in the editing pane
*/
	public void setContent(String wylie) {
		if (dp.getDocument().getLength() > 0)
			return;

		dp.newDocument();
		dp.toTibetanMachineWeb(wylie, 0);
	}

/**
* Enables typing of Roman (non-Tibetan) text along
* with Tibetan.
*/
	public void enableRoman() {
		dp.enableRoman();
	}

/**
* Disables typing of Roman (non-Tibetan) text.
*/
	public void disableRoman() {
		dp.disableRoman();
	}

/*
	private void showAttributes(int pos) {
		dp.skipUpdate = true;

		AttributeSet attr = dp.getDocument().getCharacterElement(pos).getAttributes();
		String name = StyleConstants.getFontFamily(attr);

		if (!fontName.equals(name)) {
			fontName = name;
			fontFamilies.setSelectedItem(name);
		}
		int size = StyleConstants.getFontSize(attr);
		if (fontSize != size) {
			fontSize = size;
			fontSizes.setSelectedItem(Integer.toString(fontSize));
		}

		dp.skipUpdate = false;
	}
*/

/**
* Required for implementations of DocumentListener.
* Does nothing.
*/
	public void changedUpdate(DocumentEvent de) {
	}

/**
* Required for implementations of DocumentListener.
* Informs the object that a change in the document
* has occurred.
*
* @param de a DocumentEvent
*/
	public void insertUpdate(DocumentEvent de) {
		hasChanged = true;
	}

/**
* Required for implementations of DocumentListener.
* Informs the object that a change in the document
* has occurred.
*
* @param de a DocumentEvent
*/
	public void removeUpdate(DocumentEvent de) {
		hasChanged = true;
	}

	private class RTFFilter extends javax.swing.filechooser.FileFilter {
		// Accept all directories and all RTF files.

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

				if (ext.equals("rtf"))
					return true;
				else
					return false;
			}
		}
    
		//the description of this filter
		public String getDescription() {
			return "Rich Text Format (.rtf)";
		}
	}

	private class TXTFilter extends javax.swing.filechooser.FileFilter {
		// Accept all directories and all TXT files.

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

				if (ext.equals("txt"))
					return true;
				else
					return false;
			}
		}
    
		//the description of this filter
		public String getDescription() {
			return "Text file (.txt)";
		}
	}

    /** Cached RTFPane displaying the contents of the .rtf file
        associated with keyboard 1. */
    private static RTFPane keybd1RTFPane = null;

    /** Returns an RTFPane displaying the contents of the .rtf file
        associated with keyboard 1. */
    private RTFPane getKeybd1RTFPane() {
        if (keybd1RTFPane == null) {
            try {
                keybd1RTFPane = new RTFPane(Jskad.class, keybd1HelpFile);
            } catch (Exception e) {
                e.printStackTrace(System.err);
                throw new ThdlLazyException(e); /* FIXME--handle this better. */
            }
        }
        return keybd1RTFPane;
    }

    /** Cached RTFPane displaying the contents of the .rtf file
        associated with keyboard 2. */
    private static RTFPane keybd2RTFPane = null;

    /** Returns an RTFPane displaying the contents of the .rtf file
        associated with keyboard 2. */
    private RTFPane getKeybd2RTFPane() {
        if (keybd2RTFPane == null) {
            try {
                keybd2RTFPane = new RTFPane(Jskad.class, keybd2HelpFile);
            } catch (Exception e) {
                e.printStackTrace(System.err);
                throw new ThdlLazyException(e); /* FIXME--handle this better. */
            }
        }
        return keybd2RTFPane;
    }

    /** Cached RTFPane displaying the contents of the .rtf file
        associated with keyboard 3. */
    private static RTFPane keybd3RTFPane = null;

    /** Returns an RTFPane displaying the contents of the .rtf file
        associated with keyboard 3. */
    private RTFPane getKeybd3RTFPane() {
        if (keybd3RTFPane == null) {
            try {
                keybd3RTFPane = new RTFPane(Jskad.class, keybd3HelpFile);
            } catch (Exception e) {
                e.printStackTrace(System.err);
                throw new ThdlLazyException(e); /* FIXME--handle this better. */
            }
        }
        return keybd3RTFPane;
    }

    /** Cached RTFPane displaying the contents of the .rtf file
        associated with keyboard 4. */
    private static RTFPane keybd4RTFPane = null;

    /** Returns an RTFPane displaying the contents of the .rtf file
        associated with keyboard 4. */
    private RTFPane getKeybd4RTFPane() {
        if (keybd4RTFPane == null) {
            try {
                keybd4RTFPane = new RTFPane(Jskad.class, keybd4HelpFile);
            } catch (Exception e) {
                e.printStackTrace(System.err);
                throw new ThdlLazyException(e); /* FIXME--handle this better. */
            }
        }
        return keybd4RTFPane;
    }



/**
* Runs Jskad.  System output, including errors, is redirected to
* jskad.log in addition to appearing on the console as per usual.  If
* you discover a bug, please send us an email, making sure to include
* the jskad.log file as an attachment.  */
	public static void main(String[] args) {
        try {
            ThdlDebug.attemptToSetUpLogFile("jskad", ".log");

            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            catch (Exception e) {
            }

            JFrame f = new JFrame("Jskad");
            Dimension d = f.getToolkit().getScreenSize();
            x_size = d.width/4*3;
            y_size = d.height/4*3;
            f.setSize(x_size, y_size);
            f.setLocation(d.width/8, d.height/8);
            f.getContentPane().add(new Jskad(f));
            f.setVisible(true);
        } catch (ThdlLazyException e) {
            System.err.println("Jskad has a BUG:");
            e.getRealException().printStackTrace(System.err);
        }
	}
}

