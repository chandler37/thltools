/*
The contents of this file are subject to the AMP Open Community License
Version 1.0 (the "License"); you may not use this file except in compliance
with the License. You may obtain a copy of the License on the AMP web site 
(http://www.tibet.iteso.mx/Guatemala/).

Software distributed under the License is distributed on an "AS IS" basis, 
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the 
License for the specific terms governing rights and limitations under the 
License. 

The Initial Developer of this software is Andres Montano Pellegrini. Portions
created by Andres Montano Pellegrini are Copyright 2001 Andres Montano
Pellegrini. All Rights Reserved. 

Contributor(s): ______________________________________.
*/
package org.thdl.tib.scanner;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

/** Graphical interfase to be used by applications and
	applets to input a Tibetan text and displays the words with
	their definitions.

    @author Andr&eacute;s Montano Pellegrini	
	@see AppletScannerFilter
	@see WindowScannerFilter
*/
public abstract class ScannerPanel extends Panel implements ActionListener
{
	private Label status;
	protected Checkbox chkDicts[];
	Button cmdTranslate;
	protected TibetanScanner scanner;

	/** Individual components that are to be shown or
	    hidden through the menu.
	*/

	public ScannerPanel(String file, boolean ipaq)
	{
		this(file, ipaq, 4);
	}
	
	public ScannerPanel(String file)
	{
		this(file, false, 4);
	}
	
	public ScannerPanel(String file, int cols)
	{
	    this(file, false, 4);
	}

	public ScannerPanel(String file, boolean ipaq, int cols)

	{
		boolean exito=true;
		int rows, n;

		setLayout(new BorderLayout());
		status = new Label();
		Panel panel1;
		panel1 = new Panel(new BorderLayout());
		panel1.add(status, BorderLayout.CENTER);
		cmdTranslate = new Button("Translate");
		cmdTranslate.addActionListener(this);
		panel1.add(cmdTranslate, BorderLayout.EAST);
		chkDicts=null;
//		Label copyright = new Label(TibetanScanner.copyrightUnicode);

		doingStatus("Loading dictionary...");

		try
		{
			if (file==null || file.equals(""))
				scanner = null;
			else if (file.toLowerCase().startsWith("http://"))
					scanner = new RemoteTibetanScanner(file);
				else
					scanner = new LocalTibetanScanner(file);
						
			String dictionaries[] = scanner.getDictionaryDescriptions();
			if (dictionaries!=null)
			{
				n = dictionaries.length;
				chkDicts = new Checkbox[n];
				if (n>cols)
				{
					rows = n/cols;
					if (n%cols>0) rows++;
				}
				else
				{
					cols = n;
					rows = 1;
				}
				Panel panel2 = new Panel(new GridLayout(rows,cols));
				// panel2 = new Panel();
				int i;

				for (i=0; i<dictionaries.length; i++)
				{
					if (dictionaries[i]!=null)
						chkDicts[i] = new Checkbox(dictionaries[i] + " (" + Definitions.defTags[i] + ")", true);
					else
						chkDicts[i] = new Checkbox(Definitions.defTags[i], true);
					panel2.add(chkDicts[i]);
				}
				panel1.add(panel2, BorderLayout.SOUTH);
			}			
		}
		catch (Exception e)
		{
			status.setText("Dictionary could no be loaded!");
			exito=false;
		}
		if (ipaq)
		{
/*		    panel2 = new Panel(new BorderLayout());
		    panel2.add(panel1, BorderLayout.CENTER);
		    panel2.add(copyright, BorderLayout.SOUTH);
		    add(panel2, BorderLayout.SOUTH);*/
		    add(panel1, BorderLayout.SOUTH);
		}
		else
		{
    		add(panel1, BorderLayout.NORTH);
//   	    	add(copyright, BorderLayout.SOUTH);
		}
		if (exito)
			returnStatusToNorm();
	}

	protected void doingStatus(String s)
	{
		status.setText(s);
	}

	protected void returnStatusToNorm()
	{
		status.setText("Input text:");
	}
	
	public void closingRemarks()
	{
		status.setText("Finishing...");
	}

	protected void setDicts(DictionarySource ds)
	{
		if (chkDicts==null)
		{
			if (ds!=null) ds.setAllDictionaries();
		}
		else
		{
			int i;
			ds.reset();
			for (i=0; i<chkDicts.length; i++)
			{
				if (chkDicts[i].getState()) ds.add(i);
			}
		}
	}
	
	/* FIXME: what happens if this throws an exception?  We'll just
       see it on the console--it won't terminate the program.  And the
       user may not see the console! See ThdlActionListener. -DC */
    public void actionPerformed(ActionEvent e)	
    {
        translate();
    }
	
	public abstract void translate();
	public abstract void clear();
	public void setEnableTibetanScript(boolean enabled) {}
	public void addFocusListener(FocusListener fl) {}
}
