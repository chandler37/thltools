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
Library (THDL). Portions created by the THDL are Copyright 2003 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.input;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;

import org.thdl.util.*;

/** A GUI widget used to convert Tibetan documents from one encoding
    to another.

    @author Nathaniel Garson, Tibetan and Himalayan Digital Library */
class ConvertDialog extends JDialog
    implements FontConverterConstants
{
    private static final boolean debug = false;

    // Attributes
    FontConversion controller;

    Box fileBox, buttonBox;

    JPanel content, choicePanel;

    JComboBox choices;

    JTextField oldTextField, newTextField;

    JButton browseOld, browseNew, convert, cancel, openDoc;

    JLabel type, oldLabel, newLabel;

    String[] choiceNames;

    boolean oldFieldChanged, newFieldChanged;

    JFileChooser jfc;
    File oldFile, newFile;
    String default_directory;

    final String BROWSENEW     = "Browse";
    final String BROWSEOLD     = BROWSENEW;
    final String CONVERT     = "Convert";
    final String CANCEL       = "Cancel";

    private final ThdlActionListener tal = new ThdlActionListener() {
            public void theRealActionPerformed(ActionEvent e) {
                ConvertDialog.this.theRealActionPerformed(e);
            }};
    public void init()
    {
        default_directory = controller.getDefaultDirectory();
        jfc = new JFileChooser(default_directory);
        jfc.setFileFilter(new RTFFileFilter());

        content = new JPanel(new GridLayout(0,1));
        JPanel temp = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        type = new JLabel("Type of Conversion: ");
        temp.add(type);
        temp.add(choices);
        content.add(temp);

        temp = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        oldLabel = new JLabel("Original File: ");
        temp.add(oldLabel);

        oldTextField = new JTextField(25);
        oldFieldChanged = false;
        oldTextField.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent ce) {
                oldFieldChanged = true;
            }
        });
        JPanel tfTemp = new JPanel();
        tfTemp.add(oldTextField);
        temp.add(tfTemp);

        browseOld = new JButton(BROWSEOLD);
        browseOld.addActionListener(tal);
        temp.add(browseOld);
        content.add(temp);

        temp = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        newLabel = new JLabel("Converted File: ");
        temp.add(newLabel);

        newTextField = new JTextField(25);
        newFieldChanged = false;
        newTextField.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent ce) {
                newFieldChanged = true;
            }
        });
        tfTemp = new JPanel();
        tfTemp.add(newTextField);
        temp.add(tfTemp);

        if (true) { // DLC
            browseNew = new JButton(BROWSENEW);
            browseNew.addActionListener(tal);
        }
        temp.add(browseNew);
        // DLC        temp.add(new JLabel("      "));
        content.add(temp);

        buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalGlue());
        convert = new JButton(CONVERT);
        convert.addActionListener(tal);
        buttonBox.add(convert);
        buttonBox.add(Box.createHorizontalGlue());

        cancel = new JButton(CANCEL);
        cancel.addActionListener(tal);
        buttonBox.add(cancel);
        buttonBox.add(Box.createHorizontalGlue());

        openDoc = new JButton("Open Document");
        openDoc.addActionListener(tal);
        buttonBox.add(openDoc);
        buttonBox.add(Box.createHorizontalGlue());
        openDoc.setVisible(false);

        content.add(buttonBox);
        setContentPane(content);
        pack();
        setSize(new Dimension(500,200));
    }

    public void setChoices(String[] choices)
    {
        choiceNames = choices;
        this.choices = new JComboBox(choiceNames);
        this.choices.addActionListener(tal);
    }

    // Accessors
    public void setController(FontConversion fc)
    {
        controller = fc;
    }

    public FontConversion getController()
    {
        return controller;
    }

    public String getType()
    {
        return (String)choices.getSelectedItem();
    }

    public void setCurrentDirectory(String dir)
    {
        jfc.setCurrentDirectory(new File(dir));
    }

    public void setOldFile(File f)
    {
        oldFile = f;
    }

    public void setNewFile(File f)
    {
        newFile = f;
    }

    public File getOldFile()
    {
        if(debug && oldFile == null) {System.out.println("Old file is null!");}
        return oldFile;
    }

    public File getNewFile()
    {
        if(debug && newFile == null) {System.out.println("New file is null!");}
        return newFile;
    }

    public ConvertDialog(FontConversion controller, String[] choices, boolean modal)
    {
        super(new JDialog(),PROGRAM_TITLE,modal);
        setController(controller);
        setChoices(choices);
        init();
        if (debug)
            System.out.println("Default close operation: "
                               + getDefaultCloseOperation());
    }

    void theRealActionPerformed(ActionEvent ae)
    {
        String cmd = ae.getActionCommand();
        if (cmd.equals(BROWSEOLD)
            || cmd.equals(BROWSENEW))
        {
            JButton src = (JButton)ae.getSource();
            jfc.showOpenDialog(this);
            File chosenFile = jfc.getSelectedFile();
            if(chosenFile == null) { return; }
            if(src.equals(browseOld)) {
                String fileName = chosenFile.getPath();
                oldTextField.setText(fileName);
                updateNewFileGuess();
                oldFieldChanged = false;
                oldFile = jfc.getSelectedFile();
            } else if(src.equals(browseNew)) {
                newTextField.setText(chosenFile.getPath());
                newFieldChanged = false;
                newFile = jfc.getSelectedFile();
                openDoc.setVisible(false);
            }
        } else if(cmd.equals(CONVERT)) {
            if (debug)
                System.out.println("Need to write checks for complete info...");

            if(oldFieldChanged || getOldFile() == null) {
                if (debug)
                    System.out.println("DLC: old field changed");
                setOldFile(updateFile(oldFile,oldTextField));
            }
            if(newFieldChanged || getNewFile() == null) {
                if (debug)
                    System.out.println("DLC: new field changed");
                setNewFile(updateFile(newFile,newTextField));
            }

            if(null == oldFile || !oldFile.exists()) {
                JOptionPane.showMessageDialog(this,
                                              "The original file does not exist.  Choose again.",
                                              "No such file",
                                              JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(null == newFile) {
                JOptionPane.showMessageDialog(this,
                                              "Please name the new file before proceeding.",
                                              "No output file named",
                                              JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                if(getNewFile().getCanonicalPath().equals(getOldFile().getCanonicalPath())) {
                    JOptionPane.showMessageDialog(this,
                                                  "Please name the new file something different from the old file.",
                                                  "Input and output are the same",
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (IOException e) {
                // allow it.
            }

            // Success or failure is immaterial; we still want to bust
            // out the "Open Document" button.
            controller.doConversion(this,
                                    getOldFile(),
                                    getNewFile(),
                                    (String)choices.getSelectedItem());
            oldFieldChanged = false;
            newFieldChanged = false;
            openDoc.setVisible(true);
        } else if(cmd.equals("Open Document")) {
            try {
                if(newFile == null) {return;}
                boolean done = false;
                File prog = new File(ThdlOptions.getStringOption("thdl.external.rtf.reader", "C:\\Program Files\\Microsoft Office\\Office\\WINWORD.EXE"));
                while (!done) {
                    String[] cmdArray = {prog.getPath(),newFile.getPath()};
                    Runtime rtime = Runtime.getRuntime();
                    try {
                        Process proc = rtime.exec(cmdArray);
                        proc = null;
                        done = true;
                    } catch (IOException ioe) {
                        JFileChooser jfc = new JFileChooser("C:\\Program Files\\");
                        jfc.setDialogTitle("Locate Program to Read RTF");
                        int returnValue = jfc.showOpenDialog(this);
                        if(returnValue != jfc.CANCEL_OPTION) {
                            prog = jfc.getSelectedFile();
                            ThdlOptions.setUserPreference("thdl.external.rtf.reader",
                                                          prog.getAbsolutePath());
                        } else {
                            done = true;
                        }
                    }
                }
            } catch (SecurityException se) {
                JOptionPane.showMessageDialog(this,
                                              "Cannot proceed because your security policy interfered.",
                                              "Access denied",
                                              JOptionPane.ERROR_MESSAGE);
            }
        } else if(cmd.equals(CANCEL)) {
            System.runFinalization();
            this.dispose();
            System.exit(0);
        } else if (cmd.equals("comboBoxChanged")) {
            updateNewFileGuess();
        }
    }
    private void updateNewFileGuess() {
        String oldFileName = oldTextField.getText();
        if (oldFileName == null || oldFileName.equals(""))
            return;

        String newFileNamePrefix;

        
        File of = new File(oldFileName);
        String oldFileDirName = of.getParent();
        if (oldFileDirName == null)
            oldFileDirName = "";
        else
            oldFileDirName = oldFileDirName + File.separator;
        String oldFileNameSansThingy = of.getName();
        if (oldFileNameSansThingy.startsWith("TMW_")) {
            oldFileNameSansThingy
                = oldFileNameSansThingy.substring("TMW_".length(),
                                                  oldFileNameSansThingy.length());
        } else if (oldFileNameSansThingy.startsWith("TM_")) {
            oldFileNameSansThingy
                = oldFileNameSansThingy.substring("TM_".length(),
                                                  oldFileNameSansThingy.length());
        } else if (oldFileNameSansThingy.startsWith("TMW")) {
            oldFileNameSansThingy
                = oldFileNameSansThingy.substring("TMW".length(),
                                                  oldFileNameSansThingy.length());
        } else if (oldFileNameSansThingy.startsWith("TM")) {
            oldFileNameSansThingy
                = oldFileNameSansThingy.substring("TM".length(),
                                                  oldFileNameSansThingy.length());
        }

        String ct = (String)choices.getSelectedItem();
        if ("Find all non-TMW" == ct) {
            newFileNamePrefix = "FindAllNonTMW__";
        } else if ("Find some non-TMW" == ct) {
            newFileNamePrefix = "FindSomeNonTMW__";
        } else if ("Find some non-TM" == ct) {
            newFileNamePrefix = "FindSomeNonTM__";
        } else if ("Find all non-TM" == ct) {
            newFileNamePrefix = "FindAllNonTM__";
        } else { // conversion {to Wylie or TM} mode
            if ("TMW to Wylie" == ct) {
                newFileNamePrefix = "THDL_Wylie_";
            } else if ("TMW to Unicode" == ct) {
                newFileNamePrefix = "Uni_";
            } else if ("TM to TMW" == ct) {
                newFileNamePrefix = "TMW_";
            } else {
                ThdlDebug.verify("TMW to TM" == ct);
                newFileNamePrefix = "TM_";
            }
        }
        newTextField.setText(oldFileDirName
                             + newFileNamePrefix
                             + oldFileNameSansThingy);
    }

    public File updateFile(File setFile, JTextField textField)
    {
        if(textField.equals(newTextField)) {openDoc.setVisible(false);}
        String txt = textField.getText();
        if (txt.equals(""))
            return null;
        if(txt.indexOf(".rtf")==-1) { txt += ".rtf"; }
        if(setFile == null) {return new File(txt); }
        String fileName = setFile.getPath();
        String filePath = setFile.getPath();
        if(txt.equals(fileName) || txt.equals(filePath)) { return setFile; }
        if(txt.indexOf("\\")>-1) { return new File(txt); }
        return new File(setFile.getParent() + "\\" + txt);
    }

    public class RTFFileFilter extends javax.swing.filechooser.FileFilter
    {
        public boolean accept(File f)
        {
            if(f.isDirectory() || f.getName().indexOf(".rtf")>-1) { return true; }
            return false;
        }

        public String getDescription()
        {
            return "RTF files only";
        }
    }
}

