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
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.thdl.tib.input.DuffPane;
import org.thdl.tib.text.TibetanDocument;

/** Graphical interfase to be used by applications and applets
	to input a Tibetan text (in Roman or Tibetan script) and
	displays the words (in Roman or Tibetan script) with their
	definitions (in Roman script). Uses the THDL inputting system.
	
    @author Andr&eacute;s Montano Pellegrini
*/
public class DuffScannerPanel extends ScannerPanel
{
	private TextArea fullDef, txtInput;
	private DuffPane duffInput;
	private JPanel inputPanel;
	
	private JScrollPane listDef;
//	private Font tibetanFont;
	private DictionaryTable table;

	private DictionaryTableModel model;
	
	boolean showingTibetan;

	public DuffScannerPanel(String file)
	{
		super(file);
		Panel panel1;
		panel1 = new Panel(new GridLayout(3,1));
		/* Looks up tibcodes in directory of applet. In order
		to work through a proxy store all the applet classes toghether
		with tibcodes.ini in a jar file. */
		duffInput = new DuffPane();
		duffInput.disableRoman();
		
		JPanel jpanel = new JPanel(new GridLayout(1,1));
		JScrollPane jsp = new JScrollPane(duffInput, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jpanel.add(jsp);
		inputPanel = new JPanel(new CardLayout());
		inputPanel.add(jpanel, "1");

		txtInput = new TextArea("",0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);		
		inputPanel.add(txtInput, "2");
		panel1.add(inputPanel);

		fullDef = new TextArea("",0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
		fullDef.setEditable(false);

		model = new DictionaryTableModel(null);
		table = new DictionaryTable(model, fullDef);
		table.activateTibetan(true);
		listDef = new JScrollPane(table);

		panel1.add(listDef);
		panel1.add(fullDef);
		add(panel1, BorderLayout.CENTER);
		showingTibetan = true;
		
//		tibetanFont = new Font("TibetanMachine",Font.PLAIN,36);
	}

	public void addFocusListener(FocusListener fl)
	{
	    txtInput.addFocusListener(fl);
	    duffInput.addFocusListener(fl);
	    fullDef.addFocusListener(fl);
	}

/*	public void printAllDefs()
	{

		Word word;
		int i;
		GridBagConstraints gb1 = new GridBagConstraints(), gb2 = new GridBagConstraints();
		JTextArea jtext;
		TextArea text;
		GridBagLayout grid = new GridBagLayout();

		if (panelOutput!=null)
			defPane.remove(panelOutput);

		panelOutput = new Panel(grid);
		gb1.weightx = 1;
		gb2.weightx = 4;
		gb1.gridwidth = GridBagConstraints.RELATIVE;
		gb2.gridwidth = GridBagConstraints.REMAINDER;

		for (i=0; i<array.length; i++)
		{
			word = (Word) array[i];
			jtext = new JTextArea(wd.getDuff(new TibetanString(word.getWylie())));
			jtext.setFont(tibetanFont);
			grid.setConstraints(jtext, gb1);
			panelOutput.add(jtext);

			text = new TextArea(word.getDef());
			text.
			grid.setConstraints(text, gb2);
			panelOutput.add(text);
		}

		defPane.add(panelOutput);
	}*/

    public void clear()
    {
		txtInput.setText("");
		duffInput.setText("");
		fullDef.setText("");
		model.newSearch(null);
   		table.tableChanged(new TableModelEvent(model));
    	table.repaint();
    }

    public void translate()
    {
		String in;
		setDicts(scanner.getDictionarySource());

		in = "";
		if (showingTibetan)
		    in = ((TibetanDocument)duffInput.getDocument()).getWylie();
		else
    		in = txtInput.getText();

		if (!in.equals(""))
		{
			doingStatus("Translating...");
			scanner.scanBody(in);
			scanner.finishUp();
			model.newSearch(scanner.getTokenArray());
//			printAllDefs();
			scanner.clearTokens();
			returnStatusToNorm();
			fullDef.setText("");
/*			ListSelectionModel lsm = (ListSelectionModel)table.getSelectionModel();
			if (!lsm.isSelectionEmpty())
			{
				int selectedRow = lsm.getMinSelectionIndex();
				//TableModel tm = table.getModel();
				if (selectedRow<model.getRowCount())
					fullDef.setText(model.getValueAt(selectedRow, 1).toString());
			}*/
		}
		else
		{
			model.newSearch(null);
			fullDef.setText("");
		}
   		table.tableChanged(new TableModelEvent(model));
    	table.repaint();
    }

	public void setEnableTibetanScript(boolean enabled)
	{
	    CardLayout cl = (CardLayout) inputPanel.getLayout();
	    if (enabled && !showingTibetan)
	    {
			String s = txtInput.getText();
/*			int posEnter = s.indexOf('\n');
			if (posEnter > 0)
				s = s.substring(0,posEnter);*/
			duffInput.newDocument();
			if (!s.equals(""))
			    duffInput.toTibetanMachineWeb(s, 0);
			table.activateTibetan(true);
			cl.first(inputPanel);
			showingTibetan = true;
	    }
	    if (!enabled && showingTibetan)
	    {
			txtInput.setText(((TibetanDocument)duffInput.getDocument()).getWylie());
			table.activateTibetan(false);
			cl.last(inputPanel);
			showingTibetan = false;
	    }
		table.repaint();
	}
}
