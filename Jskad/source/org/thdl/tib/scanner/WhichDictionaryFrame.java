package org.thdl.tib.scanner;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;

/** provides a friendly user interface when the command-line
    is not used for choosing a dictionary or connection to open.
*/
abstract class WhichDictionaryFrame extends Dialog implements ActionListener, ItemListener
{
    protected String response;
    protected int dictType;
    protected Button ok, cancel;
    protected Checkbox useOnline, useOffline;
    protected Choice availDictsOnline;
    protected Label localDict;
    protected Button browse;
    protected Frame owner;
    protected String dictsOnline[], dictTypes[];
    protected Checkbox ckbDefault;
    
    public WhichDictionaryFrame(Frame owner)
    {
        super(owner, "Welcome to the Tibetan to English Translation Tool", true);
        
        this.owner = owner;
        response="";
        
        // FIXME: values should not be hardwired
        dictsOnline = new String[2];
        dictsOnline[0] = "http://iris.lib.virginia.edu/tibetan/servlet/org.thdl.tib.scanner.RemoteScannerFilter";
        dictsOnline[1] = "http://wyllie.lib.virginia.edu/tibetan/servlet/org.thdl.tib.scanner.RemoteScannerFilter";
        
        dictTypes = new String[3];        
        dictTypes[0] = "public";
        dictTypes[1] = "private";
        dictTypes[2] = "local";
        //dictsOnline[2] = "http://localhost:8080/tibetan/servlet/org.thdl.tib.scanner.RemoteScannerFilter";
        
        availDictsOnline = new Choice();
        //availDictsOnline.add("Local (only on my computer!)");
        
        availDictsOnline.addItemListener(this);
        availDictsOnline.setEnabled(false);
        
        localDict = new Label();
        localDict.setEnabled(true);
        
        dictType = 2;

        browse = new Button("Browse...");
        browse.setEnabled(true);
        browse.addActionListener(this);

        ok = new Button("Ok");
        ok.setEnabled(false);
        ok.addActionListener(this);

        cancel = new Button("Cancel");
        cancel.addActionListener(this);
        
        useOnline=null;
        useOffline=null;
        ckbDefault=null;   
    }

    public abstract void actionPerformed(ActionEvent e);
	
	/** Implement the disabling of other guys here
	*/
	public abstract void itemStateChanged(ItemEvent e);
	
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