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

/** A non-Swing graphical interfase to be used by applications
    running on platforms that don't support Swing,
    to input a Tibetan text (in Roman script only) and
	display the words (in Roman script only) with their
	definitions (in Roman script).
	
    @author Andr&eacute;s Montano Pellegrini
    @see WindowScannerFilter
*/

public class SimpleScannerPanel extends ScannerPanel
{
	private TextArea txtInput, txtOutput;
	private Panel inputPanel;
	
	public SimpleScannerPanel(String file, boolean landscape)
	{
		super(file);
		Panel panel1, panel2;
		inputPanel = new Panel(new CardLayout());
		txtInput = new TextArea("",1,1,TextArea.SCROLLBARS_VERTICAL_ONLY);
		inputPanel.add(txtInput, "1");

		if (landscape) panel2 = getDictPanel(5);
		else panel2 = getDictPanel(4);
		if (panel2!=null)
		{
		    inputPanel.add(panel2, "2");
		}
		
		txtOutput = new TextArea("",0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
		txtOutput.setEditable(false);
		
		panel1 = new Panel(new BorderLayout());
		panel1.add(inputPanel, BorderLayout.NORTH);
		panel1.add(txtOutput, BorderLayout.CENTER);
		
		/*panel1 = new Panel (new GridLayout(2,1));
		panel1.add(inputPanel);
		panel1.add(txtOutput);*/

        /*GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
		panel1 = new Panel(gridbag);
		c.weightx=1;
		c.gridheight=1;		
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(inputPanel, c);
		panel1.add(inputPanel);
		c.gridheight=2;
		c.weighty=1;
        c.gridheight = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(txtOutput, c);
		panel1.add(txtOutput);*/
		
        /* GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
		panel1 = new Panel(gridbag);
		c.weightx=1;
		c.weighty=1;
		c.gridheight=1;
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(txtInput, c);
		panel1.add(txtInput);
		c.gridheight = GridBagConstraints.REMAINDER;
		c.ipady = 70;
		gridbag.setConstraints(txtOutput, c);*/
		
		add(panel1, BorderLayout.CENTER);		
	}
	
	public void setWylieInput(boolean enabled)
	{
	    CardLayout cl = (CardLayout) inputPanel.getLayout();
	    if (enabled) cl.first(inputPanel);
	    else cl.last(inputPanel);
	}
	

	public void addFocusListener(FocusListener fl)
	{
	    txtInput.addFocusListener(fl);
	    txtOutput.addFocusListener(fl);
	}

	public void printAllDefs()
	{
		int i;
		Object array[] = scanner.getTokenArray();
		
		for(i=0; i<array.length; i++)
		    if (array[i] instanceof Word)
			    txtOutput.append("* " + array[i].toString() + "\n\n");
	}
	
	public void clear()
	{
    	txtInput.setText("");
		txtOutput.setText("");
	}
	
	public void translate()
	{
		String in;
		setDicts(scanner.getDictionarySource());
		in = txtInput.getText();
		doingStatus("Translating...");
		if (!in.equals(""))
		{
			txtOutput.setText("");
			scanner.scanBody(in);
			scanner.finishUp();
			printAllDefs();
			scanner.clearTokens();
			returnStatusToNorm();
		}
    }	
}
