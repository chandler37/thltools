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
import org.thdl.tib.input.*;
import org.thdl.util.*;

/** Provides a graphical interfase to input Tibetan text (Roman or
    Tibetan script) and displays the words (Roman or Tibetan script)
    with their definitions. Works without Tibetan script in
    platforms that don't support Swing. Can access dictionaries stored
    locally or remotely. For example, to access the public dictionary database run the command:</p>
    <pre>java -jar DictionarySearchStandalone.jar http://iris.lib.virginia.edu/tibetan/servlet/org.thdl.tib.scanner.RemoteScannerFilter</pre>
  <p>If the JRE you installed does not support <i> Swing</i> classes but supports
    <i>
    AWT</i> (as the JRE for handhelds), run the command: </p>
    <pre>java -jar DictionarySearchHandheld.jar -simple ry-dic99</pre>

    @author Andr&eacute;s Montano Pellegrini
*/
public class WindowScannerFilter implements WindowListener, FocusListener, ActionListener, ItemListener
{
	private static String defOpenOption = "thdl.scanner.default.open";
	private static String dictOpenType = "thdl.scanner.default.type";
    
	private ScannerPanel sp;
	private MenuItem mnuExit, mnuCut, mnuCopy, mnuPaste, mnuDelete, mnuSelectAll, mnuAbout, mnuClear, mnuOpen, mnuPreferences, mnuSavePref;
	private CheckboxMenuItem mnuDicts;
	private Object objModified;
	private AboutDialog diagAbout;
	private Frame mainWindow;
	private boolean pocketpc;

	public WindowScannerFilter(boolean pocketpc)
	{
		String response, dictType;
		this.pocketpc = pocketpc;
		
		if (!pocketpc) response = ThdlOptions.getStringOption(defOpenOption);
		else response=null;
		
		if (response==null || response.equals(""))
		{
		    mainWindow = new Frame("Tibetan Translation Tool");
		    mainWindow.show();
		    mainWindow.toFront();
		    WhichDictionaryFrame wdf = new WhichDictionaryFrame(mainWindow, pocketpc);

		    wdf.show();
		    response = wdf.getResponse();
		    if (response.equals("")) System.exit(0);
		    else
		    {
		        dictType = wdf.getDictionaryType();
		        mainWindow.setTitle("Tibetan Translation Tool: Connected to " + dictType + " database");
		        if (!pocketpc && wdf.getDefaultOption())
		        {
		            ThdlOptions.setUserPreference(defOpenOption, response);
		            ThdlOptions.setUserPreference(dictOpenType, dictType);		            
		            try
		            {
		                ThdlOptions.saveUserPreferences();
		            }
		            catch (Exception e)
		            {
		            }
		        }
		    }
		}
    	makeWindow (response);
	}
	
	public WindowScannerFilter()
	{
	    this(false);
	}

	public WindowScannerFilter(String file)
	{
	    pocketpc = false;
	    mainWindow = null;
		makeWindow (file);
	}
	
	public WindowScannerFilter(String file, boolean pocketpc)
	{
	    this.pocketpc = pocketpc;
	    mainWindow = null;
		makeWindow (file);
	}	

	public void makeWindow(String file)
	{
	    if (mainWindow==null)
	    {
	        String dictType=null;
	        if (!pocketpc) dictType = ThdlOptions.getStringOption(dictOpenType);
	        if (dictType!=null && !dictType.equals(""))
	            mainWindow = new Frame("Tibetan Translation Tool: Connected to " + dictType + " database");
	        else
	            mainWindow = new Frame("Tibetan Translation Tool");
	    }
	    else mainWindow.setVisible(false);
		mainWindow.setLayout(new GridLayout(1,1));
		mainWindow.setBackground(Color.white);
		
	    diagAbout = null;
	    mnuAbout = null;
	    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

		if (pocketpc) sp = new SimpleScannerPanel(file, d.width >d.height);
		else sp = new DuffScannerPanel(file);
		
		MenuBar mb = new MenuBar();
		Menu m;
	    m = new Menu ("File");
		mnuOpen = new MenuItem("Open...");
		mnuOpen.addActionListener(this);
		m.add(mnuOpen);
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
		if (!pocketpc)
		{
		    m.add("-");
		    mnuPreferences = new MenuItem("Preferences");
		    m.add(mnuPreferences);
		    mnuPreferences.addActionListener(this);
		    mnuSavePref = new MenuItem("Save preferences to " + ThdlOptions.getUserPreferencesPath());
		    m.add(mnuSavePref);
		    mnuSavePref.addActionListener(this);
		    mnuDicts=null;
		}
		mb.add(m);
		
		if (pocketpc)
		{
       		m = new Menu("View");
	    	mnuDicts = new CheckboxMenuItem("Dictionaries", false);
		    m.add(mnuDicts);
    		mnuDicts.addItemListener(this);
    	}
    	else
   		{
   		    m = new Menu("Help");
            for (int i = 0; i < DuffScannerPanel.keybdMgr.size(); i++)
            {
                final JskadKeyboard kbd = DuffScannerPanel.keybdMgr.elementAt(i);
                if (kbd.hasQuickRefFile())
                {
                    MenuItem keybdItem = new MenuItem(kbd.getIdentifyingString());
                    keybdItem.addActionListener(new ThdlActionListener()
                    {
                        public void theRealActionPerformed(ActionEvent e) 
                        {
                            new SimpleFrame(kbd.getIdentifyingString(),
                                            kbd.getQuickRefPane());
                            /* DLC FIXME -- pressing the "Extended
                            Wylie" menu item (for example) twice
                            causes the first pane to become dead.
                            We should check to see if the first
                            pane exists and raise it rather than
                            creating a second pane. */
                        }
                    });
                    m.add(keybdItem);
                }
            }   		    
   		    m.add("-");
        	mnuAbout = new MenuItem("About...");
	        m.add(mnuAbout);
   		    mnuAbout.addActionListener(this);
        }
       	mb.add(m);
		
		// disable menus
        focusLost(null);        
        
		sp.addFocusListener(this);
		
		mainWindow.setMenuBar(mb);
		mainWindow.add(sp);
		mainWindow.addWindowListener(this);
		mainWindow.setSize(d);
		// mainWindow.setSize(240,320);
		//else mainWindow.setSize(500,600);
		mainWindow.show();
		mainWindow.toFront();

	    if (pocketpc || !ThdlOptions.getBooleanOption(AboutDialog.windowAboutOption))
	    {
   	        diagAbout = new AboutDialog(mainWindow, pocketpc);
	        diagAbout.show();
	        
	        if (!pocketpc && diagAbout.omitNextTime())
	        {
	            ThdlOptions.setUserPreference(AboutDialog.windowAboutOption, true);
	            try
	            { 
	                ThdlOptions.saveUserPreferences();
	            }
	            catch(Exception e)
	            {
	            }
	        }
	    }
	}
	
	public static void main(String[] args)
	{
		switch(args.length)
		{
		    case 0:
		    new WindowScannerFilter();
		    break;
		    case 1:
		    if (args[0].length()>0 && args[0].charAt(0)=='-') new WindowScannerFilter(true);
		    else new WindowScannerFilter(args[0]);
		    break;
		    case 2:
		    new WindowScannerFilter(args[1], true);
		    break;
		    default:
		    System.out.println("Syntax error!");
			System.out.println("Sintaxis: java WindowScannerFilter [options] arch-dict");
			System.out.println("Options:");
			System.out.println("  -simple: runs the non-swing version.");
			System.out.println(TibetanScanner.copyrightASCII);
		}
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
    public void actionPerformed(ActionEvent event)	
    {
		Object clicked = event.getSource();
		StringSelection ss;
		String s = null;
		
		if (clicked == mnuAbout)
		{
		    if (diagAbout==null) diagAbout = new AboutDialog(mainWindow, pocketpc);
		    diagAbout.show();
		    if (!pocketpc)
		    {
		        ThdlOptions.setUserPreference(AboutDialog.windowAboutOption, diagAbout.omitNextTime());
		        try
	            { 
	                ThdlOptions.saveUserPreferences();
	            }
    	        catch(Exception e)
	            {
	            }
	        }
		}
		else if (clicked == mnuClear)
		{
		    sp.clear();
		}
		else if (clicked == mnuOpen)
        {
            mainWindow.setVisible(false);
            mainWindow.dispose();
            if (!pocketpc) ThdlOptions.setUserPreference(defOpenOption, "");
            new WindowScannerFilter(pocketpc);
        }
        else if (clicked == mnuExit)
        {
            System.exit(0);
        }
        else if (clicked == mnuPreferences)
        {
            sp.setPreferences();
        }
        else if (clicked == mnuSavePref)
        {
            try 
            {
                if (!ThdlOptions.saveUserPreferences()) {
                    JOptionPane.showMessageDialog(mainWindow,
                                                  "You previously cleared preferences,\nso you cannot now save them.",
                                                  "Cannot Save User Preferences",
                                                  JOptionPane.PLAIN_MESSAGE);
                }
            } 
            catch (IOException ioe)
            {
                JOptionPane.showMessageDialog(mainWindow,
                                              "Could not save to your preferences file!",
                                              "Error Saving Preferences",
                                              JOptionPane.ERROR_MESSAGE);
            }
            
        }
        else
		{
    		if (objModified==null) return;
		
	    	if (objModified instanceof TextArea)
		    {
		        TextArea t = (TextArea) objModified;
		        
		        if (clicked == mnuCut)
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
	        	    t.cut();
                }
                else if (clicked == mnuCopy)
                {
                    t.copy();
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
        sp.setWylieInput(e.getStateChange()!=ItemEvent.SELECTED);
    }
}
