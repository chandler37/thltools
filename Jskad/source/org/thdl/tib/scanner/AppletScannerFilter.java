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
import java.applet.Applet;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.*;
import java.awt.datatransfer.*;
import org.thdl.tib.input.DuffPane;
import org.thdl.tib.text.TibetanDocument;

/** Inputs a Tibetan text and displays the words with
	their definitions through through a graphical interfase using a
	Browser over the Internet. The graphical interfase is provided
	by implementations of the ScannerPanel.
	
	<p>Parameter URL should contain the URL of the
	servlet which is going to handle to the
	looking up of the words in the server.</p>
	<p>Since the applet uses Swing for the THDL inputting
	system, run the HTML file through Sun's Java Plug-in
	HTML Converter to ensure that the browser will
	use a JVM to run the applet.</p>

    @author Andr&eacute;s Montano Pellegrini
    @see RemoteScannerFilter
    @see ScannerPannel
*/
public class AppletScannerFilter extends JApplet implements ActionListener, FocusListener, ItemListener
{
	private JMenuItem mnuSelectAll, aboutItem, mnuClear; // mnuCut, mnuCopy, mnuPaste, mnuDelete
	private JCheckBoxMenuItem tibScript;
	
	// private JMenu mnuEdit;
	private Object objModified;
	private Dialog diagAbout;
	ScannerPanel sp;
	
	public void init()
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
		}
		String url = getParameter("url");
		if (url.indexOf("http://")<0)
		{
			url = getCodeBase() + url;
		}

		// panel = new SimpleScannerPanel(url);
		sp = new DuffScannerPanel(url);
		sp.addFocusListener(this);
		
		setContentPane(sp);
		
		// setup the menu. Almost identical to WindowScannerFilter, but using swing.
		JMenuBar mb = new JMenuBar();
		// mnuEdit
		JMenu m = new JMenu ("Edit");		
/*		mnuCut = new JMenuItem("Cut");		
		mnuEdit.add(mnuCut);
		mnuCut.addActionListener(this);		
		mnuCopy = new JMenuItem("Copy");
		mnuEdit.add(mnuCopy);
		mnuCopy.addActionListener(this);
		mnuPaste = new JMenuItem("Paste");
		mnuEdit.add(mnuPaste);
		mnuPaste.addActionListener(this);		
		mnuDelete = new JMenuItem("Delete");
		mnuEdit.add(mnuDelete);
		mnuDelete.addActionListener(this);		
		mnuEdit.addSeparator();*/
		mnuSelectAll = new JMenuItem("Select all");
		m.add(mnuSelectAll);
		mnuSelectAll.addActionListener(this);
		mnuClear = new JMenuItem("Clear all");
		m.add(mnuClear);
		mnuClear.addActionListener(this);		
		mb.add(m);
		
   		m = new JMenu("View");
    	tibScript = new JCheckBoxMenuItem("Tibetan Script", true);
	    m.add(tibScript);
   		tibScript.addItemListener(this);
   		mb.add(m);
		
		//JMenuItem 
		aboutItem = new JMenuItem("About...");
		aboutItem.addActionListener(this);
		
		m = new JMenu("Help");
		m.add(aboutItem);
		mb.add(m);
		setJMenuBar(mb);
        //mnuEdit.setEnabled(false);
	}
	
	/** Added to update the Edit menu in dependence upon
	    which textbox the keyboard focus is at.
	*/
	public void focusGained(FocusEvent e)
	{
	    objModified = e.getSource();
/*	    boolean isEditable=false;
	    
	    if (objModified instanceof TextComponent)
	    {
	        TextComponent t = (TextComponent) objModified;
	        isEditable = t.isEditable();
	    }
	    else if (objModified instanceof JTextComponent)
	    {
	        JTextComponent j = (JTextComponent) objModified;
	        isEditable = j.isEditable();
	    }
	    
	    //mnuEdit.setEnabled(true);
        mnuCut.setEnabled(isEditable);
        if (isEditable)
        {
            if (Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this)!=null)
                mnuPaste.setEnabled(true);
        }
        else mnuPaste.setEnabled(false);
        mnuDelete.setEnabled(isEditable);*/
	}
	
	/** Added to update the Edit menu in dependence upon
	    which textbox the keyboard focus is at.
	*/
	public void focusLost(FocusEvent e)
	{
	    /* mnuEdit.setEnabled(false);
	    objModified=null;*/
	}
    public void actionPerformed(ActionEvent e)	
    {
		Object clicked = e.getSource();
/*		StringSelection ss;
		String s = null;
		int start, end;*/
		
		if (clicked==aboutItem)
		{
		    JOptionPane.showMessageDialog(AppletScannerFilter.this, TibetanScanner.aboutUnicode, "About", JOptionPane.PLAIN_MESSAGE);
		}
		else if (clicked == mnuClear)
		{
		    sp.clear();
		}
		else
		{
    		if (objModified==null) return;
		
	    	if (objModified instanceof TextArea)
		    {
		        TextArea t = (TextArea) objModified;
		    
        		/*if (clicked == mnuCut)
	        	{
	        	    ss = new StringSelection(t.getSelectedText());
	    	        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss,ss);
	    	        t.replaceRange("", t.getSelectionStart(), t.getSelectionEnd());
                }
                else if (clicked == mnuCopy)
                {                    
	    	        ss = new StringSelection(t.getSelectedText());
	    	        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss,ss);
                }
                else if (clicked == mnuPaste)
                {
		            Transferable data = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this);
        		    try 
            		{
            		    s = (String)(data.getTransferData(DataFlavor.stringFlavor));
            		}
        	    	catch (Exception ex)
        		    {
    			        s = data.toString();
	    	        }

        		    JOptionPane.showMessageDialog(AppletScannerFilter.this, s, "About", JOptionPane.PLAIN_MESSAGE);
                    t.insert(s, t.getCaretPosition());
                }
                else if (clicked == mnuDelete)
                {
                    t.replaceRange("", t.getSelectionStart(), t.getSelectionEnd());
                }
                else*/ if (clicked == mnuSelectAll)
                {
                    t.selectAll();
                }
            }   
            else if (objModified instanceof DuffPane)
            {
                DuffPane t = (DuffPane) objModified;
            
        		/*if (clicked == mnuCut)
	        	{
		    		t.copy(t.getSelectionStart(), t.getSelectionEnd(), true);
                }
                else if (clicked == mnuCopy)
                {
		    		t.copy(t.getSelectionStart(), t.getSelectionEnd(), false);
                }
                else if (clicked == mnuPaste)
                {
            		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            		Transferable data = clipboard.getContents(this);

            		s = null;
            		try {
            			s = (String)(data.getTransferData(DataFlavor.stringFlavor));
		            }
            		catch (Exception ex) {
        			    s = data.toString();
		            }
        		    JOptionPane.showMessageDialog(AppletScannerFilter.this, s, "About", JOptionPane.PLAIN_MESSAGE);
                    t.paste(t.getCaret().getDot());
                }
                else if (clicked == mnuDelete)
                {
                    try
                    {
                        ((TibetanDocument)t.getDocument()).remove(t.getSelectionStart(), t.getSelectionEnd());
                    }
                    catch (Exception ex)
                    {
                        System.out.println(ex);
                    }
                }
                else*/ if (clicked == mnuSelectAll)
                {
                    t.selectAll();
                }    
            }
        }
    }	
    public void itemStateChanged(ItemEvent e)
    {
        sp.setEnableTibetanScript(e.getStateChange()==ItemEvent.SELECTED);
    }
}
