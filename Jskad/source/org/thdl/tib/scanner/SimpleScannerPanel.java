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

public class SimpleScannerPanel extends ScannerPanel
{
	private TextArea txtInput, txtOutput;
	
	public SimpleScannerPanel(String file)
	{
		super(file, true);
		Panel panel1;	
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
		panel1 = new Panel(gridbag);
		c.weightx=1;
		c.weighty=1;
		c.gridheight=1;
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = GridBagConstraints.REMAINDER;
		txtInput = new TextArea("",0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
		txtOutput = new TextArea("",0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
		txtOutput.setEditable(false);
		gridbag.setConstraints(txtInput, c);
		panel1.add(txtInput);
		c.gridheight = GridBagConstraints.REMAINDER;
		c.ipady = 70;
		gridbag.setConstraints(txtOutput, c);		
		panel1.add(txtOutput);
		add(panel1, BorderLayout.CENTER);		
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
