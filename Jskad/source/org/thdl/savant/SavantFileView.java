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
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.*;

import org.thdl.util.ThdlDebug;

/**
 * The SavantFileView "sees through" a <code>*.savant</code> file and
 * returns the title associated with it.  A <code>*.savant</code> file
 * is a properties file like the following:
 * <pre>
 * #Tue May 14 16:07:15 EDT 2002
 * TRANSCRIPT=MST4.xml
 * PROJECT=THDL
 * MEDIA=MST4.mpg
 * TITLE=MST Chapter 4 (video)
 * </pre>
 * @author Edward Garrett */
public class SavantFileView extends FileView {
    /** When opening a file, this is the only extension Savant cares
        about.  This is case-insensitive. */
	public final static String dotSavant = ".savant";

	/** This loads <code>*.savant</code> files as properties files and
        returns an associated TITLE attribute.  For any other type of
        file, or for a properties file that does not specify a
        non-empty-string TITLE attribute, this returns null. */
	public String getName(File f) {
		if (f.isDirectory())
			return null;

		/* Unless a developer has chosen to do otherwise, examine only
           *.savant files.  If you don't do this, you waste a lot of
           time, making any file chooser that uses this class
           unresponsive.  In addition, you'll cause the floppy drive
           to spin up every time you refresh or cd in the file
           chooser. */
		if (!Boolean.getBoolean("THDL_TREAT_ALL_FILES_AS_DOT_SAVANT_FILES_REGARDLESS_OF_EXTENSION")) { /* FIXME */
			if (!f.getName().toLowerCase().endsWith(dotSavant))
				return null;
		}
		Properties p = new Properties();
		try {
			p.load(new FileInputStream(f));
			String ttl = p.getProperty("TITLE");
			if (ttl.equals(""))
				return null; /* We never want the user to see nothing
                                at all, so let the L&F file view
                                handle this. */
			else
				return ttl;
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			ThdlDebug.noteIffyCode();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			ThdlDebug.noteIffyCode();
		}
		return null;
	}

	/** Returns null always, which lets the default look-and-feel's
	 *  FileView take over.
	 *  @return null */
	public String getDescription(File f) {
		return null; // let the L&F FileView figure this out
	}

	/** Returns null always, which lets the default look-and-feel's
	 *  FileView take over.
	 *  @return null */
	public Boolean isTraversable(File f) {
		return null; // let the L&F FileView figure this out
	}

	/** Returns null always, which lets the default look-and-feel's
	 *  FileView take over.
	 *  @return null */
	public String getTypeDescription(File f) {
		return null; // let the L&F FileView figure this out
	}

	/* FIXME: why not same as above? */
/*
	public Icon getIcon(File f) {
	}
*/
}
