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
import java.awt.Frame;
import java.awt.Dialog;

/** DLC FIXMEDOC
 *  @author David Chandler */
public class ConverterGUI implements FontConversion, FontConverterConstants {
    /** Default constructor; does nothing */
    ConverterGUI() { }

    /**
     *  Runs the converter. */
	public static void main(String[] args) {
        // No need for the TM or TMW fonts.
        System.setProperty("thdl.rely.on.system.tmw.fonts", "true");
        System.setProperty("thdl.do.not.rely.on.system.tm.fonts", "false");

        System.exit(realMain(args, System.out, null));
    }

    // DLC FIXMEDOC returns true on success
    public boolean doConversion(ConvertDialog cd, File oldFile, File newFile,
                                String whichConversion) {
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
            JOptionPane.showMessageDialog(cd,
                                          "The conversion failed because either the old\nfile could not be found or the new file could\nnot be written (because it was open\nelsewhere or read-only or what have you).",
                                          "Conversion failed",
                                          JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (3 == returnCode) {
            JOptionPane.showMessageDialog(cd,
                                          TibetanConverter.rtfErrorMessage,
                                          "Conversion failed",
                                          JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (44 == returnCode) {
            JOptionPane.showMessageDialog(cd,
                                          "Though an output file has been created, it contains ugly\nerror messages like\n\"<<[[JSKAD_TMW_TO_WYLIE_ERROR_NO_SUCH_WYLIE:\n    Cannot convert DuffCode...\".\nPlease edit the output by hand to replace all such\ncreatures with the correct EWTS transliteration.",
                                          "Attention required",
                                          JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (43 == returnCode) {
            JOptionPane.showMessageDialog(cd,
                                          "Though an output file has been created, this conversion did nothing.\nDid you choose the correct original file?\nDid you choose the correct type of conversion?",
                                          "Nothing to do",
                                          JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (42 == returnCode) {
            JOptionPane.showMessageDialog(cd,
                                          "Some of the document cannot be converted.  The output\ncontains the problem glyphs.  E-mail David Chandler\nwith your suggestions about the proper way to handle\nsuch a document.",
                                          "Errors in Conversion",
                                          JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (0 != returnCode) {
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
    public static int realMain(String[] args, PrintStream out, Frame owner) {
        returnCode = 0;
        try {
            final ConvertDialog convDialog;
            String[] choices = new String[]{
                TM_TO_TMW,
                TMW_TO_UNI,
                TMW_TO_WYLIE,
                TMW_TO_TM,
                FIND_SOME_NON_TMW,
                FIND_SOME_NON_TM,
                FIND_ALL_NON_TMW,
                FIND_ALL_NON_TM
            };
            if (null == owner) {
                convDialog
                    = new ConvertDialog(new ConverterGUI(),
                                        choices,
                                        true);
            } else {
                convDialog
                    = new ConvertDialog(owner,
                                        new ConverterGUI(),
                                        choices,
                                        true);
            }

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
