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
import java.util.*;
import java.io.*;
import java.awt.event.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.datatransfer.*;
import javax.swing.text.JTextComponent;
import javax.swing.JOptionPane;
import org.thdl.tib.input.DuffPane;

/** Provides a graphical interfase to input Tibetan text (Roman or
    Tibetan script) and displays the words (Roman or Tibetan script)
    with their definitions. Works without Tibetan script in
    platforms that don't support Swing. Can access dictionaries stored
    locally or remotely.

    @author Andr&eacute;s Montano Pellegrini
*/
public class WindowScannerFilter implements WindowListener, FocusListener, ActionListener, ItemListener
{
	private ScannerPanel sp;
	private MenuItem mnuExit, mnuCut, mnuCopy, mnuPaste, mnuDelete, mnuSelectAll, mnuAbout, mnuClear;
	private CheckboxMenuItem tibScript;
	private Object objModified;
	private Frame frame;
	private Dialog diagAbout;
	private boolean usingSwing;

	public WindowScannerFilter(String file)
	{
		this (file, false);
	}

	public WindowScannerFilter(String file, boolean ipaq)
	{
		frame = new Frame("Tibetan Scanner");
		frame.setLayout(new GridLayout(1,1));
		frame.setBackground(Color.white);
	    diagAbout = null;
	    usingSwing=!ipaq;

		if (usingSwing)
			sp = new DuffScannerPanel(file);
		else
			sp = new SimpleScannerPanel(file);
		
		MenuBar mb = new MenuBar();
		Menu m = new Menu ("File");		
		mnuExit = new MenuItem("Exit");
		mnuExit.addActionListener(this);
		m.add(mnuExit);
		mb.add(m);

		m = new Menu ("Edit");		
		mnuCut = new MenuItem("Cut");
		//mnuCut.setShortcut(new MenuShortcut(KeyEvent.VK_X));
		m.add(mnuCut);
		mnuCut.addActionListener(this);
		mnuCopy = new MenuItem("Copy");
		//mnuCopy.setShortcut(new MenuShortcut(KeyEvent.VK_C));
		m.add(mnuCopy);
		mnuCopy.addActionListener(this);
		mnuPaste = new MenuItem("Paste");
		//mnuPaste.setShortcut(new MenuShortcut(KeyEvent.VK_V));
		m.add(mnuPaste);
		mnuPaste.addActionListener(this);		
		mnuDelete = new MenuItem("Delete");
		m.add(mnuDelete);
		mnuDelete.addActionListener(this);		
		m.add("-");
		mnuSelectAll = new MenuItem("Select all");
		//mnuSelectAll.setShortcut(new MenuShortcut(KeyEvent.VK_A));
		m.add(mnuSelectAll);
		mnuSelectAll.addActionListener(this);
		mnuClear = new MenuItem("Clear all");
		m.add(mnuClear);
		mnuClear.addActionListener(this);
		mb.add(m);
		if (usingSwing)
		{
    		m = new Menu("View");
	    	tibScript = new CheckboxMenuItem("Tibetan Script", true);
		    m.add(tibScript);
    		tibScript.addItemListener(this);
    		mb.add(m);
    	}
    	else tibScript=null;
    	
		m = new Menu("Help");
		mnuAbout = new MenuItem("About...");
		m.add(mnuAbout);
		mnuAbout.addActionListener(this);
		mb.add(m);
		
		// disable menus
        focusLost(null);	
		sp.addFocusListener(this);
		
		frame.setMenuBar(mb);
		frame.add(sp);
		frame.addWindowListener(this);
		frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		// frame.setSize(240,320);
		//else frame.setSize(500,600);
		frame.show();
	}

	public static void main(String[] args)
	{
		if (args.length!=1 && args.length!=2)
		{
			System.out.println("Sintaxis: java WindowScannerFilter [options] arch-dict");
			System.out.println("Options:");
			System.out.println("  -simple: runs the non-swing version.");
			System.out.println(TibetanScanner.copyrightASCII);
			return;
		}
		new WindowScannerFilter(args[args.length-1], args.length == 2);
	}


	/**
	 * Cierra la ventana. Invocado unicamente cuando el programa corre
	 * como aplicacion, para que el programa pare su ejecucion cuando
	 * el usuario cierre la ventana principal.
	 */
	 public void windowClosing(WindowEvent e)
	 {
   	 	sp.closingRemarks();
 	    System.exit(0);
	 }

	/**
	 * Sin cuerpo, no hace nada. Esta incluido solo para satisfacer
	 * la interfase <code>WindowListener</code>, la cual es implementada
	 * para tener el metodo <code>windowClosing</code>.
	 *
	 * @see #windowClosing
	 */
	public void windowActivated(WindowEvent e)
	{
	}

	/**
	 * Sin cuerpo, no hace nada. Esta incluido solo para satisfacer
	 * la interfase <code>WindowListener</code>, la cual es implementada
	 * para tener el metodo <code>windowClosing</code>.
	 *
	 * @see #windowClosing
	 */
	public void windowClosed(WindowEvent e)
	{
	}

	/**
	 * Sin cuerpo, no hace nada. Esta incluido solo para satisfacer
	 * la interfase <code>WindowListener</code>, la cual es implementada
	 * para tener el metodo <code>windowClosing</code>.
	 *
	 * @see #windowClosing
	 */
	public void windowDeactivated(WindowEvent e)
	{
	}

	/**
	 * Sin cuerpo, no hace nada. Esta incluido solo para satisfacer
	 * la interfase <code>WindowListener</code>, la cual es implementada
	 * para tener el metodo <code>windowClosing</code>.
	 *
	 * @see #windowClosing
	 */
	public void windowDeiconified(WindowEvent e)
	{
	}

	/**
	 * Sin cuerpo, no hace nada. Esta incluido solo para satisfacer
	 * la interfase <code>WindowListener</code>, la cual es implementada
	 * para tener el metodo <code>windowClosing</code>.
	 *
	 * @see #windowClosing
	 */
	public void windowIconified(WindowEvent e)
	{
	}

	/**
	 * Sin cuerpo, no hace nada. Esta incluido solo para satisfacer
	 * la interfase <code>WindowListener</code>, la cual es implementada
	 * para tener el metodo <code>windowClosing</code>.
	 *
	 * @see #windowClosing
	 */
	public void windowOpened(WindowEvent e)
	{
	}
	
	/** Added to update the Edit menu in dependence upon
	    which textbox the keyboard focus is at.
	*/
	public void focusGained(FocusEvent e)
	{
	    objModified = e.getSource();
	    boolean isEditable=false;
	    
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
	    
        mnuCut.setEnabled(isEditable);
        if (isEditable)
        {
            if (Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this)!=null)
                mnuPaste.setEnabled(true);
        }
        else mnuPaste.setEnabled(false);
        mnuDelete.setEnabled(isEditable);
		mnuCopy.setEnabled(true);
		mnuSelectAll.setEnabled(true);
	}
	
	/** Added to update the Edit menu in dependence upon
	    which textbox the keyboard focus is at.
	*/
	public void focusLost(FocusEvent e)
	{
	    objModified=null;
		mnuCut.setEnabled(false);
		mnuCopy.setEnabled(false);
		mnuPaste.setEnabled(false);
		mnuDelete.setEnabled(false);
		mnuSelectAll.setEnabled(false);
	}

	/* FIXME: what happens if this throws an exception?  We'll just
       see it on the console--it won't terminate the program.  And the
       user may not see the console! See ThdlActionListener. -DC */
    public void actionPerformed(ActionEvent e)	
    {
		Object clicked = e.getSource();
		StringSelection ss;
		String s = null;
		
		if (clicked == mnuAbout)
		{
		    if (usingSwing)
		    {
		        JOptionPane.showMessageDialog(frame, TibetanScanner.aboutUnicode, "About", JOptionPane.PLAIN_MESSAGE);
		    }
		    else if (diagAbout==null)
		    {
    	        diagAbout = new AboutDialog(frame);
		    }
		    diagAbout.show();
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
		        
		        if (clicked == mnuExit)
		        {
		            System.exit(0);
		        }
		        else if (clicked == mnuCut)
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
	    	        t.replaceRange(s, t.getSelectionStart(), t.getSelectionEnd());                
                    //t.insert(s, t.getCaretPosition());
                }
                else if (clicked == mnuDelete)
                {
                    t.replaceRange("", t.getSelectionStart(), t.getSelectionEnd());
                }
                else if (clicked == mnuSelectAll)
                {
                    t.selectAll();
                }
            }   
            else if (objModified instanceof DuffPane)
            {
                DuffPane t = (DuffPane) objModified;
            
        		if (clicked == mnuCut)
	        	{
		    		t.cutCurrentSelection();
                }
                else if (clicked == mnuCopy)
                {
		    		t.copyCurrentSelection();
                }
                else if (clicked == mnuPaste)
                {
                    t.paste(t.getCaret().getDot());
                }
                else if (clicked == mnuDelete)
                {
                    try
                    {
                        t.getDocument().remove(t.getSelectionStart(), t.getSelectionEnd());
                    }
                    catch (Exception ex)
                    {
                        System.out.println(ex);
                    }
                }
                else if (clicked == mnuSelectAll)
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
