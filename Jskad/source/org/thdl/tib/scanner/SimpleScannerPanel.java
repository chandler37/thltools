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

public class SimpleScannerPanel extends ScannerPanel implements ItemListener
{
	private TextArea txtInput, txtOutput;
	private Panel cardPanel;
	private List listDefs;
	private Word wordArray[];
	private int lenPreview;
	private static int WIDTH_PORTRAIT = 36;
	private static int WIDTH_LANDSCAPE = 48;
	
	public SimpleScannerPanel(String file, boolean landscape)
	{
		super(file);
		Panel panel1, panel2;
		Font f;
		cardPanel = new Panel(new CardLayout());
		
		// FIXME values shouldn't be hardwired
		if (landscape) lenPreview = WIDTH_LANDSCAPE;
		else lenPreview = WIDTH_PORTRAIT;
		wordArray=null;

		// panel1 = new Panel(new GridLayout(3, 1));
		
		// txtInput = new TextArea("",1,1,TextArea.SCROLLBARS_VERTICAL_ONLY);
		
    	panel1 = new Panel(new BorderLayout());
		panel2 = new Panel(new GridLayout(2, 1));
		
    	listDefs = new List();
    	
		if (landscape) 
		{
		    txtInput = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
    		txtOutput = new TextArea("", 4, WIDTH_LANDSCAPE,TextArea.SCROLLBARS_VERTICAL_ONLY);

    		panel2.add(txtInput);
    		panel2.add(listDefs);
    		
		    panel1.add(panel2, BorderLayout.CENTER);
		    panel1.add(txtOutput, BorderLayout.SOUTH);		    
		}
		else
		{
		    txtInput = new TextArea("", 4, WIDTH_PORTRAIT, TextArea.SCROLLBARS_VERTICAL_ONLY);
    		txtOutput = new TextArea("",0, 0,TextArea.SCROLLBARS_VERTICAL_ONLY);
    		
    		panel2.add(listDefs);
    		panel2.add(txtOutput);
    		    		
    		panel1.add(txtInput, BorderLayout.NORTH);
    		panel1.add(panel2, BorderLayout.CENTER);
		}
		listDefs.setMultipleMode(false);
		listDefs.addItemListener(this);
		
		txtOutput.setEditable(false);

		/*f = new Font(null, Font.PLAIN, 10);
		txtOutput.setFont(f);
		txtInput.setFont(f);*/		
		
		cardPanel.add(panel1, "1");
		
		// FIXME: values shouldn't be hardwired
		if (landscape) panel2 = getDictPanel(2);
		else panel2 = getDictPanel(1);
		if (panel2!=null)
		{
		    cardPanel.add(panel2, "2");
		}
		
    	add(cardPanel, BorderLayout.CENTER);
	}
	
    public void itemStateChanged(ItemEvent e)
    {
        int n = listDefs.getSelectedIndex();
        if (n>-1) txtOutput.setText(wordArray[n].toString());
    }
    
	
	public void setWylieInput(boolean enabled)
	{
	    CardLayout cl = (CardLayout) cardPanel.getLayout();
	    if (enabled) cl.first(cardPanel);
	    else cl.last(cardPanel);
	}
	

	public void addFocusListener(FocusListener fl)
	{
	    txtInput.addFocusListener(fl);
	    txtOutput.addFocusListener(fl);
	}

	public void printAllDefs()
	{
		int i;
		wordArray = scanner.getWordArray();
		String preview;
		
		listDefs.removeAll();
		
		for(i=0; i<wordArray.length; i++)
		{
		    preview = wordArray[i].getWordDefPreview();
		    if (preview.length()>lenPreview) preview = preview.substring(0,lenPreview);
		    listDefs.add(preview);
		}
	}
	
	public void clear()
	{
    	txtInput.setText("");
		txtOutput.setText("");
		listDefs.removeAll();
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
