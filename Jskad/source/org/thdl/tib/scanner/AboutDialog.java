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

class AboutDialog extends Dialog implements ActionListener, WindowListener
{
    public AboutDialog(Frame parent)
    {
        super(parent, "About...", true);
        Button close = new Button("Close this window");
        add(close, BorderLayout.NORTH);
        close.addActionListener(this);
        TextArea ta = new TextArea(TibetanScanner.aboutUnicode,0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
        ta.setEditable(false);
        addWindowListener(this);
        add(ta, BorderLayout.CENTER);
        setSize(240,300); // the size ipaq's window.
    }
    
	/* FIXME: what happens if this throws an exception?  We'll just
       see it on the console--it won't terminate the program.  And the
       user may not see the console! See ThdlActionListener. -DC */
    public void actionPerformed(ActionEvent e)
    {
        hide();
    }
    
	/**
	 * Cierra la ventana. Invocado unicamente cuando el programa corre
	 * como aplicacion, para que el programa pare su ejecucion cuando
	 * el usuario cierre la ventana principal.
	 */
	 public void windowClosing(WindowEvent e)
	 {
   	 	hide();
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
}
