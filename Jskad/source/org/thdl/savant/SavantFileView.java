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
