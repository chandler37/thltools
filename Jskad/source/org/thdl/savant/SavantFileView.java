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

public class SavantFileView extends FileView {
	public String getName(File f) {
		if (f.isDirectory())
			return null;
		Properties p = new Properties();
		try {
			p.load(new FileInputStream(f));
			return p.getProperty("TITLE");
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}
	public String getDescription(File f) {
		return null;
	}
	public Boolean isTraversable(File f) {
		return null; // let the L&F FileView figure this out
	}
	public String getTypeDescription(File f) {
		return null;
	}
/*
	public Icon getIcon(File f) {
	}
*/
}
