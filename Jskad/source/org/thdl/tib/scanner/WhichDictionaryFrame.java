package org.thdl.tib.scanner;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;

/** provides a friendly user interface when the command-line
    is not used for choosing a dictionary or connection to open.
*/
class WhichDictionaryFrame extends Dialog implements ActionListener, ItemListener
{
    private String response;
    private int dictType;
    private Button ok, cancel;
    private Checkbox useOnline, useOffline, createNewDictDB;
    private Choice availDictsOnline;
    private Label localDict;
    private Button browse;
    private Frame owner;
    private boolean pocketpc;
    private DictionaryFileFilter dicFilter;
    private String dictsOnline[], dictTypes[];
    private Checkbox ckbDefault;
    private CreateDatabaseWizard cdw;
    
    public WhichDictionaryFrame(Frame owner, boolean pocketpc)
    {
        super(owner, "Welcome to the Tibetan to English Translation Tool", true);
        Panel p;
        CheckboxGroup cbg = new CheckboxGroup();

        this.owner = owner;
        this.pocketpc = pocketpc;
        dicFilter = null;
        response="";
        
        this.setLayout(new GridLayout(6, 1));
        if (pocketpc)
        {
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            this.setSize(d);
        } else this.setSize(400, 200);
        if (pocketpc) this.add(new Label("Would you like open:"));
        else this.add(new Label("Would you like to:"));
        
        // FIXME: values should not be hardwired
        dictsOnline = new String[2];
        dictTypes = new String[3];        
        
        availDictsOnline = null;
        p = new Panel(new BorderLayout());
        if (pocketpc) useOnline = new Checkbox("An on-line dict", false, cbg);
        else useOnline = new Checkbox("Access an on-line dictionary", false, cbg);
        useOnline.addItemListener(this);
        p.add(useOnline, BorderLayout.WEST);

        availDictsOnline = new Choice();
        
        if (pocketpc) availDictsOnline.add("Public");
        else availDictsOnline.add("Public version");
        dictsOnline[0] = "http://iris.lib.virginia.edu/tibetan/servlet/org.thdl.tib.scanner.RemoteScannerFilter";
        dictTypes[0] = "public";
        
        if (pocketpc) availDictsOnline.add("Private version");
        else availDictsOnline.add("Private version (only within UVa)");
        dictsOnline[1] = "http://wyllie.lib.virginia.edu/tibetan/servlet/org.thdl.tib.scanner.RemoteScannerFilter";
        dictTypes[1] = "private";
        
        //availDictsOnline.add("Local (only on my computer!)");
        //dictsOnline[2] = "http://localhost:8080/tibetan/servlet/org.thdl.tib.scanner.RemoteScannerFilter";        
        
        availDictsOnline.addItemListener(this);
        availDictsOnline.setEnabled(false);
        p.add(availDictsOnline, BorderLayout.EAST);
        this.add(p);
        
        p = new Panel(new BorderLayout());
        if (pocketpc) useOffline = new Checkbox("Local dict: ", true, cbg);
        else useOffline = new Checkbox("Access a local dictionary: ", true, cbg);
        useOffline.addItemListener(this);
        p.add(useOffline, BorderLayout.WEST);
        
        localDict = new Label();
        localDict.setEnabled(true);
        p.add(localDict, BorderLayout.CENTER);
        dictTypes[2] = "local";
        dictType = 2;
        
        browse = new Button("Browse...");
        browse.setEnabled(true);
        browse.addActionListener(this);
        p.add(browse, BorderLayout.EAST);
        this.add(p);
        if (pocketpc) createNewDictDB = new Checkbox("New dict...", false, cbg);
        else createNewDictDB = new Checkbox("Create a new dictionary...", false, cbg);
        createNewDictDB.addItemListener(this);
        this.add(createNewDictDB);
        
        p = new Panel(new FlowLayout());
        ok = new Button("Ok");
        ok.setEnabled(false);
        ok.addActionListener(this);
        cancel = new Button("Cancel");
        cancel.addActionListener(this);
        p.add(ok);
        p.add(cancel);
        this.add(p);
        p = new Panel(new FlowLayout(FlowLayout.RIGHT));
        if (pocketpc) ckbDefault = new Checkbox("Set as default?", true);
        else ckbDefault = new Checkbox("Use these settings as default", true);
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
		        if (cdw==null) cdw = new CreateDatabaseWizard(owner, pocketpc);
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
		    if (pocketpc)
		    {
    		    fd = new FileDialog(owner, "Select dictionary to open", FileDialog.LOAD);
		        fd.show();
		        fileName = fd.getFile();
		        if (fileName!= null)
		        {
		            // dropping the extension
    	            pos = fileName.lastIndexOf('.');
	                if (pos>0) fileName = fileName.substring(0, pos);
		            response = fd.getDirectory() + fileName;
		            localDict.setText(response);
		            ok.setEnabled(true);
		        }
		    }
		    else
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
	        response = dictsOnline[ch.getSelectedIndex()];
	    }
	}

    public String getResponse()
    {
        return response;
    }
    
    public boolean getDefaultOption()
    {
        return ckbDefault.getState();
    }
    
    public String getDictionaryType()
    {
        return dictTypes[dictType];
    }
}