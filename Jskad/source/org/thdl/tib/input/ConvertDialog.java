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
    private FontConversion controller;

    private Box fileBox, buttonBox;

    private JPanel content;

    JPanel getContentPanel() { return content; }

    private JComboBox choices;

    private JComboBox warningLevels;

    private JTextField oldTextField, newTextField;

    private JButton browseOld, browseNew, convert, cancel, openDocOld, openDocNew, about;

    private JFileChooser jfc;

    private static final String BROWSENEW     = "Browse...";
    private static final String BROWSEOLD     = BROWSENEW;
    private static final String CONVERT     = "Convert";
    private static final String CANCEL       = "Close";
    private static final String ABOUT       = "About";
    private static final String OPEN_WITH       = "Open With...";
    private static final String LOCATE_FILE       = "Locate File";

    private final ThdlActionListener tal = new ThdlActionListener() {
            public void theRealActionPerformed(ActionEvent e) {
                ConvertDialog.this.theRealActionPerformed(e);
            }};
    private void updateWarningLevels() {
        if (choices.getSelectedItem() == ACIP_TO_UNI)
            this.warningLevels.enable();
        else
            this.warningLevels.disable();
    }
    private void init()
    {
        jfc = new JFileChooser(controller.getDefaultDirectory());
        jfc.setDialogTitle(LOCATE_FILE);
        jfc.setFileFilter(new RTFFileFilter());

        content = new JPanel(new GridLayout(0,1));
        JPanel temp = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        temp.add(new JLabel("Type of Conversion: "));
        temp.add(choices);
        temp.add(Box.createHorizontalStrut(20));
        temp.add(new JLabel("Warning Level: "));
        this.warningLevels
            = new JComboBox(new String[] { "None", "Some", "Most", "All" });
        this.warningLevels.setSelectedItem("Most");
        this.warningLevels.addActionListener(tal);
        updateWarningLevels();

        temp.add(warningLevels);
        content.add(temp);

        temp = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        temp.add(new JLabel("Original File: "));

        oldTextField = new JTextField(25);
        JPanel tfTemp = new JPanel();
        tfTemp.add(oldTextField);
        temp.add(tfTemp);

        browseOld = new JButton(BROWSEOLD);
        browseOld.addActionListener(tal);
        temp.add(browseOld);
        openDocOld = new JButton(OPEN_WITH);
        openDocOld.addActionListener(tal);
        temp.add(openDocOld);
        content.add(temp);

        temp = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        temp.add(new JLabel("Converted File: "));

        newTextField = new JTextField(25);
        tfTemp = new JPanel();
        tfTemp.add(newTextField);
        temp.add(tfTemp);

        if (true) {
            browseNew = new JButton(BROWSENEW);
            browseNew.addActionListener(tal);
        }
        temp.add(browseNew);
        openDocNew = new JButton(OPEN_WITH);
        openDocNew.addActionListener(tal);
        temp.add(openDocNew);
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

        about = new JButton(ABOUT);
        about.addActionListener(tal);
        buttonBox.add(about);
        buttonBox.add(Box.createHorizontalGlue());

        content.add(buttonBox);
        setContentPane(content);
        pack();
        setSize(new Dimension(620,200));
    }

    private void setChoices(String[] choices)
    {
        this.choices = new JComboBox(choices);
        this.choices.addActionListener(tal);
    }

    // Accessors
    private void setController(FontConversion fc)
    {
        controller = fc;
    }

    /** This constructor takes an owner; the other doesn't. */
    public ConvertDialog(Frame owner,
                         FontConversion controller,
                         String[] choices,
                         boolean modal)
    {
        super(owner,PROGRAM_TITLE,modal);
        initConvertDialog(controller, choices, modal);
    }

    /** This constructor does not take an owner; the other does. */
    public ConvertDialog(FontConversion controller,
                         String[] choices,
                         boolean modal)
    {
        super(new JDialog(),PROGRAM_TITLE,modal);
        initConvertDialog(controller, choices, modal);
    }

    private void initConvertDialog(FontConversion controller,
                                   String[] choices,
                                   boolean modal) {
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
            if (jfc.showOpenDialog(this) != jfc.APPROVE_OPTION)
                return;
            File chosenFile = jfc.getSelectedFile();
            if(chosenFile == null) { return; }
            if(src == browseOld) {
                String fileName = chosenFile.getPath();
                oldTextField.setText(fileName);
                updateNewFileGuess();
                ThdlOptions.setUserPreference("thdl.Jskad.working.directory",
                                              chosenFile.getParentFile().getAbsolutePath());
            } else if(src == browseNew) {
                newTextField.setText(chosenFile.getPath());
            } else
                throw new Error("New button?");
        } else if(cmd.equals(CONVERT)) {
            File origFile = new File(oldTextField.getText());
            if (!origFile.exists()) {
                JOptionPane.showMessageDialog(this,
                                              "The original file does not exist.  Choose again.",
                                              "No such file",
                                              JOptionPane.ERROR_MESSAGE);
                return;
            }
            File convertedFile = new File(newTextField.getText());
            if(null == convertedFile) {
                JOptionPane.showMessageDialog(this,
                                              "Please name the new file before proceeding.",
                                              "No output file named",
                                              JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                if(convertedFile.getCanonicalPath().equals(origFile.getCanonicalPath())) {
                    JOptionPane.showMessageDialog(this,
                                                  "Please name the new file something different from the old file.",
                                                  "Input and output are the same",
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (IOException e) {
                // allow it.
            }

            if (convertedFile.isDirectory()) {
                JOptionPane.showMessageDialog(this,
                                              "The target file you've chosen is a directory.  Choose a file.",
                                              "Cannot write to directory",
                                              JOptionPane.ERROR_MESSAGE);
                return;
            } else if (convertedFile.exists()) {
                int overwriteExisingFile
                    = JOptionPane.showConfirmDialog(this,
                                                    "Do you want to overwrite "
                                                    + convertedFile.getName()
                                                    + "?",
                                                    "Please select",
                                                    JOptionPane.YES_NO_OPTION);

                switch (overwriteExisingFile) {
                case JOptionPane.YES_OPTION: // continue.
                    break;
                default:
                    return;
                }
            }

            try {
                controller.doConversion(this,
                                        origFile,
                                        convertedFile,
                                        (String)choices.getSelectedItem(),
                                        (String)warningLevels.getSelectedItem());
            } catch (OutOfMemoryError e) {
                JOptionPane.showMessageDialog(this,
                                              "The converter ran out of memory.  Please give the\nJVM more memory by using java -XmxYYYm where YYY\nis the amount of memory your system has, or\nsomething close to it.  E.g., try\n'java -Xmx512m -jar Jskad.jar'.",
                                              "Out of Memory",
                                              JOptionPane.ERROR_MESSAGE);
            }
        } else if(cmd.equals(OPEN_WITH)) {
            try {
                JButton src = (JButton)ae.getSource();
                String fileToOpen;
                if (src == openDocNew) {
                    fileToOpen = newTextField.getText();
                } else {
                    ThdlDebug.verify(src == openDocOld);
                    fileToOpen = oldTextField.getText();
                }
                if ("".equals(fileToOpen)) {
                    JOptionPane.showMessageDialog(this,
                                                  "Please choose a file to open with the external viewer.",
                                                  "No file named",
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }
                File namedFile = new File(fileToOpen);
                if (!namedFile.exists()) {
                    JOptionPane.showMessageDialog(this,
                                                  "No such file exists, so it cannot be opened with the external viewer.",
                                                  "No such file",
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!namedFile.isFile()) {
                    JOptionPane.showMessageDialog(this,
                                                  "You've chosen a directory, not a file.  Only files\ncan be opened with the external viewer.",
                                                  "Not a regular file",
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }

                openWithExternalViewer(this, fileToOpen);
            } catch (SecurityException se) {
                JOptionPane.showMessageDialog(this,
                                              "Cannot proceed because your security policy interfered.",
                                              "Access denied",
                                              JOptionPane.ERROR_MESSAGE);
            }
        } else if(cmd.equals(CANCEL)) {
            this.dispose();
        } else if(cmd.equals(ABOUT)) {
            JOptionPane.showMessageDialog(this,
                                          "This Tibetan Converter is Copyright 2003\nTibetan and Himalayan Digital Library and\nis protected by the THDL Open Community\nLicense Version 1.0.\n\nCompiled "
                                          + ThdlVersion.getTimeOfCompilation(),
                                          "About",
                                          JOptionPane.PLAIN_MESSAGE);
        } else if (cmd.equals("comboBoxChanged")) {
            JComboBox src = (JComboBox)ae.getSource();
            if (src == choices) {
                updateNewFileGuess();
                updateWarningLevels();
            }
        }
    }

    /** Invokes a user-specified external viewer (one that takes a
        single command-line argument, the path to view) on the file
        with path fileToOpen.
        @param parent the owner of any dialogs that come up
        @param fileToOpen the path to open in the external viewer */
    static void openWithExternalViewer(Component parent, String fileToOpen) {

        boolean done = false;
        File prog
            = new File(ThdlOptions.getStringOption("thdl.external.rtf.reader",
                                                   "C:\\Program Files\\Microsoft Office\\Office\\WINWORD.EXE"));
        while (!done) {
            String[] cmdArray = {prog.getPath(),fileToOpen};
            Runtime rtime = Runtime.getRuntime();
            try {
                Process proc = rtime.exec(cmdArray);
                proc = null;
                done = true;
            } catch (IOException ioe) {
                JFileChooser jfc = new JFileChooser("C:\\Program Files\\");
                jfc.setDialogTitle("Locate Program to Read RTF");
                if(jfc.showOpenDialog(parent) == jfc.APPROVE_OPTION) {
                    prog = jfc.getSelectedFile();
                    ThdlOptions.setUserPreference("thdl.external.rtf.reader",
                                                  prog.getAbsolutePath());
                } else {
                    done = true;
                }
                jfc.setDialogTitle(LOCATE_FILE);
            }
        }
    }

    /** Looks at the name of the original file and creates a name for
        the converted file based on that. */
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
        } else if (FIND_SOME_NON_TMW == ct) {
            newFileNamePrefix = "FindSomeNonTMW__";
        } else if (FIND_SOME_NON_TM == ct) {
            newFileNamePrefix = "FindSomeNonTM__";
        } else if (FIND_ALL_NON_TM == ct) {
            newFileNamePrefix = "FindAllNonTM__";
        } else { // conversion {to Wylie or TM} mode
            if (TMW_TO_WYLIE == ct) {
                newFileNamePrefix = suggested_WYLIE_prefix;
            } else if (TMW_TO_UNI == ct || ACIP_TO_UNI == ct) {
                newFileNamePrefix = suggested_TO_UNI_prefix;
            } else if (TM_TO_TMW == ct) {
                newFileNamePrefix = suggested_TO_TMW_prefix;
            } else {
                ThdlDebug.verify(TMW_TO_TM == ct);
                newFileNamePrefix = suggested_TO_TM_prefix;
            }
        }
        newTextField.setText(oldFileDirName
                             + newFileNamePrefix
                             + oldFileNameSansThingy);
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

