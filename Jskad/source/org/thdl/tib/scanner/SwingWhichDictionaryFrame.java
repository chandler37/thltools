package org.thdl.tib.scanner;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;

/** provides a friendly user interface when the command-line
    is not used for choosing a dictionary or connection to open.
*/
class SwingWhichDictionaryFrame extends WhichDictionaryFrame
{
    private CreateDatabaseWizard cdw;
    private DictionaryFileFilter dicFilter;
    private Checkbox createNewDictDB;
    
    public SwingWhichDictionaryFrame(Frame owner)
    {
        super(owner);
        Panel p;
        CheckboxGroup cbg = new CheckboxGroup();

        dicFilter = null;
        
        this.setLayout(new GridLayout(6, 1));
        
        this.setSize(400, 200);
        this.add(new Label("Would you like to:"));
        
        p = new Panel(new BorderLayout());
        
        useOnline = new Checkbox("Access an on-line dictionary", false, cbg);
        useOnline.addItemListener(this);
        p.add(useOnline, BorderLayout.WEST);

        availDictsOnline.add("Public version");
        availDictsOnline.add("Private version (only within UVa)");
        
        p.add(availDictsOnline, BorderLayout.EAST);
        this.add(p);
        
        p = new Panel(new BorderLayout());
        
        useOffline = new Checkbox("Access a local dictionary: ", true, cbg);
        useOffline.addItemListener(this);
        p.add(useOffline, BorderLayout.WEST);
        
        p.add(localDict, BorderLayout.CENTER);
        
        p.add(browse, BorderLayout.EAST);
        this.add(p);
        
        createNewDictDB = new Checkbox("Create a new dictionary...", false, cbg);
        createNewDictDB.addItemListener(this);
        this.add(createNewDictDB);
        
        p = new Panel(new FlowLayout());

        p.add(ok);
        p.add(cancel);
        this.add(p);
        
        p = new Panel(new FlowLayout(FlowLayout.RIGHT));
        ckbDefault = new Checkbox("Use these settings as default", true);
        p.add(ckbDefault);
        this.add(p);
    }

    public void actionPerformed(ActionEvent e)	
    {
		Object obj = e.getSource();
		FileDialog fd;
		String fileName;
		int pos;
		
		if (obj == ok)
		{
		    if (response.equals(""))
		    {
		        if (cdw==null) cdw = new CreateDatabaseWizard(owner);
		        cdw.show();
		        response = cdw.getResponse();
		    }
		    if (!response.equals("")) this.setVisible(false);
		}
		else if (obj == cancel)
		{
		    response = "";
		    this.setVisible(false);
		}
		else if (obj == browse)
		{
		    if (dicFilter == null) dicFilter = new DictionaryFileFilter();
		    JFileChooser fileChooser = new JFileChooser("Select dictionary to open");
		    fileChooser.addChoosableFileFilter(dicFilter);
		    /*setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
            {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                return;
            }*/
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            {
                response = fileChooser.getSelectedFile().getPath();
                response = response.substring(0, response.length()-4);
                localDict.setText(response);
		        ok.setEnabled(true);
            }
		}
	}
	
	/** Implement the disabling of other guys here
	*/
	public void itemStateChanged(ItemEvent e)
	{
	    Object obj = e.getSource();
	    if (obj instanceof Checkbox)
	    {
	        Checkbox chx = (Checkbox) obj;
	        //System.out.println(obj);
	        if (chx == useOnline)
	        {
	            localDict.setEnabled(false);
	            browse.setEnabled(false);
	            availDictsOnline.setEnabled(true);
	            ok.setEnabled(true);
	            dictType = availDictsOnline.getSelectedIndex();
	            response = dictsOnline[dictType];
	        }
	        else if (chx == useOffline)
	        {
	            localDict.setEnabled(true);
	            browse.setEnabled(true);
	            if (availDictsOnline!=null)  availDictsOnline.setEnabled(false);
	            ok.setEnabled(!localDict.getText().equals(""));
	            response = localDict.getText();
	            dictType = 2;
	        }
	        else if (chx == createNewDictDB)
	        {
	            localDict.setEnabled(false);
	            browse.setEnabled(false);
	            if (availDictsOnline!=null) availDictsOnline.setEnabled(false);
	            ok.setEnabled(true);
	            response="";
	            dictType = 2;
	        }
	    }
	    else if (obj instanceof Choice)
	    {
	        Choice ch = (Choice) obj;
	        dictType = ch.getSelectedIndex();
	        response = dictsOnline[dictType];
	    }
	}

}