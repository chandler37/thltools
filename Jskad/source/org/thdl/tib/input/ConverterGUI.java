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

import java.io.*;

import org.thdl.util.*;
import org.thdl.tib.text.*;
import javax.swing.JOptionPane;

/** DLC FIXMEDOC
 *  @author David Chandler */
public class ConverterGUI implements FontConversion {
    /** Default constructor; does nothing */
    ConverterGUI() { }

    static {
        // No need for the TM or TMW fonts.
        System.setProperty("thdl.rely.on.system.tmw.fonts", "true");
        System.setProperty("thdl.do.not.rely.on.system.tmw.fonts", "false");
    }

    /**
     *  Runs the converter. */
	public static void main(String[] args) {
        System.exit(realMain(args, System.out));
    }

    // DLC FIXMEDOC returns true on success
    public boolean doConversion(ConvertDialog cd, File oldFile, File newFile,
                             String whichConversion) {
        System.err.println("DLC NOW " + oldFile + " " + newFile + " " + whichConversion);
        PrintStream ps;
        try {
            returnCode
                = TibetanConverter.reallyConvert(new FileInputStream(oldFile),
                                                 ps = new PrintStream(new FileOutputStream(newFile),
                                                                      false),
                                                 whichConversion);
            ps.close();
        } catch (FileNotFoundException e) {
            returnCode = 39;
        }
        if (0 != returnCode) {
            JOptionPane.showMessageDialog(cd,
                                          "The conversion failed with code " + returnCode + "; please e-mail\ndchandler@users.sourceforge.net to learn what that means if\nyou can't find out from the output.",
                                          "Conversion failed",
                                          JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            if (!ThdlOptions.getBooleanOption("thdl.skip.conversion.success.message"))
                JOptionPane.showMessageDialog(cd,
                                              "The conversion went perfectly.",
                                              "Conversion succeeded",
                                              JOptionPane.PLAIN_MESSAGE);
            return true;
        }
    }

    // DLC FIXMEDOC
    public String getDefaultDirectory() {
        return ThdlOptions.getStringOption("thdl.Jskad.working.directory",
                                           null);
    }

    private static int returnCode = 0;

    /** Runs the converter without exiting the program.
     *  @return the exit code. */
    public static int realMain(String[] args, PrintStream out) {
        try {

            final ConvertDialog convDialog
                = new ConvertDialog(new ConverterGUI(),
                                    new String[]{
                                        // DLC FIXME: use variables
                                        // for these, because they're
                                        // used in
                                        // TibetanConverter.java too.
                                        "TM to TMW",
                                        "TMW to Unicode",
                                        "TMW to Wylie",
                                        "TMW to TM",
                                        "Find some non-TMW",
                                        "Find some non-TM",
                                        "Find all non-TMW",
                                        "Find all non-TM"
                                    },
                                    true);

            /* Make it so that any time the user exits this program by
             * (almost) any means, the user's preferences are saved if
             * the SecurityManager allows it and the path is correct.
             * This means that the program used to "Open Document"
             * will be remembered. */
            Runtime.getRuntime().addShutdownHook(new Thread() {
                    public void run() {
                        try {
                            if (!ThdlOptions.saveUserPreferences()) {
                                JOptionPane.showMessageDialog(convDialog,
                                                              "You previously cleared preferences,\nso you cannot now save them.",
                                                              "Cannot Save User Preferences",
                                                              JOptionPane.PLAIN_MESSAGE);
                            }
                        } catch (IOException ioe) {
                            System.out.println("IO Exception saving user preferences to " + ThdlOptions.getUserPreferencesPath());
                            ioe.printStackTrace();
                            ThdlDebug.noteIffyCode();
                        }
                        
                    }
                });

			convDialog.setVisible(true);
            return returnCode;
        } catch (ThdlLazyException e) {
            out.println("ConverterGUI has a BUG:");
            e.getRealException().printStackTrace(out);
            return 7;
        }
	}
}
