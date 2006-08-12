package org.thdl.tib.input ;

import javax.swing.* ;
import javax.swing.text.* ;
import javax.swing.text.StyleConstants.FontConstants ;
import java.awt.GridBagLayout ;
import java.awt.GridBagConstraints ;
import java.awt.Insets ;
import java.awt.Rectangle ;
import java.awt.Font ;
import java.awt.Color ;
import java.awt.Component ;
import java.awt.event.KeyEvent ;
import java.awt.event.KeyAdapter ;
import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;
import javax.swing.event.ListSelectionListener ;
import javax.swing.event.ListSelectionEvent ;
import java.util.concurrent.locks.ReentrantLock ;

class DictionaryFrame extends JFrame implements ListSelectionListener
{
	//
	// the fields to display ..
	//
	protected JTextPane original ;			// .. the original text - romanized and tibetan
	protected JTextArea pronounciation ;	// .. the pronounciation
	protected DefaultListModel listModel ;	//
	protected JList entryList ;				// .. list of dictionary keywords
	protected JTextPane description ;		// .. sequence of dictionary entries
    protected JScrollPane descriptionPane ;	//
	protected JButton closeButton ;

	protected Font fontSerif ;
	protected Font fontSansSerif ;

    protected ReentrantLock listLock ;

	//
	// entryList contains MyListElement-s instead of just String-s
	// (we want a number to be associated with each element to store the position within description)
	//
	class MyListElement
	{
		MyListElement ( String theStr, int theData )
		{
			str = theStr ;
			data = theData ;
		}

		public void setIntData ( int theData )
		{
			data = theData ;
		}

		public int getIntData ()
		{
			return data ;
		}

		public String toString ()
		{
			return str ;
		}

		protected String str ;
		protected int data ;
	};

	DictionaryFrame ()
	{
		super ( "Dictionary" ) ;
		init ( null ) ;
	}

	DictionaryFrame ( Component parent )
	{
		super ( "Dictionary" ) ;
		init ( parent ) ;
	}

	void init ( Component parent )
	{	
        listLock = new ReentrantLock () ;

		fontSerif = new Font ( "serif", Font.PLAIN, 12 ) ;
		fontSansSerif = new Font ( "sansserif", Font.PLAIN, 12 ) ;

		//
		// layout
		//
		GridBagLayout gridbag = new GridBagLayout () ;
		GridBagConstraints c = new GridBagConstraints () ;

		getContentPane ().setLayout ( gridbag ) ;

		//
		// children
		//
		original = new JTextPane () ;
		pronounciation = new JTextArea ( "" ) ;

		listModel = new DefaultListModel () ;
		
		entryList = new JList ( listModel ) ;
		entryList.addListSelectionListener ( this ) ;

		description = new JTextPane () ;
		descriptionPane = new JScrollPane ( description, 
											JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
											JScrollPane.HORIZONTAL_SCROLLBAR_NEVER ) ;

        closeButton = new JButton ( "Close" ) ;
        closeButton.setMnemonic ( 'c' ) ;
        closeButton.addActionListener ( new ActionListener () { public void actionPerformed ( ActionEvent e ) { closeWindow () ;  } } ) ;

		original.setEditable ( false ) ;
		pronounciation.setEditable ( false ) ;
		description.setEditable ( false ) ;

		//
		// add them
		//
		c.insets = new Insets ( 1, 1, 1, 1 ) ;
		c.anchor = GridBagConstraints.NORTHWEST ;
		c.weightx = 1.0 ;			
		c.weighty = 0.0 ;			
		c.gridheight = 1 ;
		c.gridwidth = GridBagConstraints.REMAINDER ;
		c.fill = GridBagConstraints.HORIZONTAL ;

		gridbag.setConstraints ( original, c ) ;
		getContentPane ().add ( original ) ;

		c.anchor = GridBagConstraints.NORTHWEST ;
		c.weightx = 1.0 ;			
		c.weighty = 0.0 ;			
		c.gridheight = 1 ;
		c.gridwidth = GridBagConstraints.REMAINDER ;
		c.fill = GridBagConstraints.HORIZONTAL ;

		gridbag.setConstraints ( pronounciation, c ) ;
		getContentPane ().add ( pronounciation ) ;

		c.weighty = 1.0 ;			
		c.fill = GridBagConstraints.BOTH ;
		c.gridheight = 4 ;
		c.weightx = 0.0 ;
		c.gridwidth = 1 ;

		gridbag.setConstraints ( entryList, c ) ;
		getContentPane ().add ( entryList ) ;

		c.fill = GridBagConstraints.BOTH ;
		c.gridheight = 4 ;
		c.gridwidth = GridBagConstraints.REMAINDER ;
		c.weightx = 3.0 ;

		gridbag.setConstraints ( descriptionPane, c ) ;
		getContentPane ().add ( descriptionPane ) ;

		c.anchor = GridBagConstraints.CENTER ;
		c.weightx = 1.0 ;			
		c.weighty = 0.0 ;			
		c.gridx = GridBagConstraints.RELATIVE ;
		c.gridy = GridBagConstraints.RELATIVE ;
		c.gridheight = 1 ;
		c.gridwidth = GridBagConstraints.RELATIVE ;
		c.fill = GridBagConstraints.HORIZONTAL ;

		gridbag.setConstraints ( closeButton, c ) ;
		getContentPane ().add ( closeButton ) ;

		//
		// set fonts
		//
		original.setFont ( fontSansSerif ) ;
		pronounciation.setFont ( fontSansSerif ) ;
		entryList.setFont ( fontSansSerif ) ;
		description.setFont ( fontSansSerif ) ;

		//
		// we need F12 to toggle the visible state of the dictionary window
		//
		KeyAdapter keyAdapter = new KeyAdapter () 
								{
									public void keyPressed ( KeyEvent ev )
									{
										if ( KeyEvent.VK_F12 == ev.getKeyCode () ||
                                             KeyEvent.VK_ESCAPE == ev.getKeyCode () )
											toggleVisible () ;
									}
								};
		original.addKeyListener ( keyAdapter ) ;
		pronounciation.addKeyListener ( keyAdapter ) ;
		entryList.addKeyListener ( keyAdapter ) ;
		description.addKeyListener ( keyAdapter ) ;

		//
		// finish
		//		
		pack () ;

		//
		// place the window on top of the parent frame, moving it slightly
		//
		if ( parent != null )
			setLocation ( parent.getLocation ().x, parent.getLocation ().y ) ;

		setSize ( 500, 500 ) ;		
	}

	/**
	 * toggle the visibility of this window
	 */
	void toggleVisible ()
	{
		setVisible ( !isVisible () ) ;
	}

    /**
     *
     */
    void closeWindow ()
    {
        setVisible ( false ) ;
    }

	/**
	 * clear all fields
	 */
	public void reset ()
	{
		//
		// clear all fields
		//
        listLock.lock () ;
        try
        {
		    original.setText ( "" ) ;
		    pronounciation.setText ( "" ) ;

		    description.setText ( "" ) ;
			listModel.clear () ;		
        }
        finally
        {
            listLock.unlock () ;
        }
	}

	/**
	 * isEmpty
	 */
	public boolean isEmpty ()
	{
		return ( original.getText ().length () > 0 ? false : true ) ;
	}

	/**
	 * set the original
	 * 
	 * text - has the Wylie roman text
	 * realDoc - ref to the actual document being edited
	 * startPos, endPos - location of the text in the actual document being edited
	 */
	public void setOriginal ( String text, DefaultStyledDocument realDoc, int startPos, int endPos )
	{
		AbstractDocument doc = (AbstractDocument)original.getDocument () ;

		SimpleAttributeSet as = new SimpleAttributeSet () ;
		as.addAttribute ( FontConstants.Family, "serif" ) ;
		try
		{
			doc.insertString ( doc.getLength (), text + "  ", as ) ;
		}
		catch ( BadLocationException e )
		{
			// this should never happen
		}

		SimpleAttributeSet as1 = new SimpleAttributeSet () ;
		
		for ( int pos = startPos; pos <= endPos; pos++ )
		{
			Element el = realDoc.getCharacterElement ( pos ) ;
			
			try
			{
				doc.insertString ( doc.getLength (), realDoc.getText ( pos, 1 ), el.getAttributes () ) ;
			}
			catch ( BadLocationException e )
			{
				// this should never happen
			}
		}
	}

	/**
	 * set the pronounciation field
	 */
	public void setPronounciation ( String text )
	{
		//
		// set the pronounciation field
		//
		pronounciation.setText ( text ) ;
	}
	
	/**
	 * add a dictionary entry
	 */
	public void addDescription ( String text )
	{
		//
		// add the keyword to entryList. we assume the keyword is anything in the
		// dictionary entry that precedes the first dash
		//

        listLock.lock () ;

        try
        {        
			Document doc = description.getDocument () ;

			try
			{
				int len = doc.getLength() ;
				if ( len > 0 )
				{
					doc.insertString ( len, "\n\n", null ) ;
					len = doc.getLength () ;
				}

				int dashIndex = text.indexOf ( "-" ) ;
				String keyword = text.substring ( 0, dashIndex - 1 ) ;
				String entry = text.substring ( dashIndex + 1 ) ;

				//
				// add keyword to the list box
				//
				listModel.addElement ( new MyListElement ( keyword,	len ) ) ;

				SimpleAttributeSet attrSet = new SimpleAttributeSet () ;
				StyleConstants.setBold ( attrSet, true ) ;
				StyleConstants.setUnderline ( attrSet, true ) ;				

				//
				// add entry to the list box
				//
				doc.insertString ( len, keyword, attrSet ) ;
				len = doc.getLength () ;

				StyleConstants.setBold ( attrSet, true ) ;
				StyleConstants.setUnderline ( attrSet, false ) ;
				doc.insertString ( len, entry, null ) ;
			}
			catch ( Exception e )
			{
			}
        }
        finally
        {
            listLock.unlock () ;
        }
	}

	/**
	 * called when entryList selection changes
	 */
	public void valueChanged ( ListSelectionEvent e ) 
	{
		//
		// get the position of the dictionary entry identified
		// by the selected entryList item.
		//

        listLock.lock () ;

        try
        {
            MyListElement elem = ((MyListElement)entryList.getSelectedValue ()) ;
            if ( null != elem )
            { 
		        int pos = elem.getIntData () ;
        
		        try
		        {
			        //
			        // make sure the part of description associated with the item
			        // is visible;  TODO this doesn't work quite well
			        //
			        Rectangle rect = description.modelToView ( pos ) ;
			        rect.setSize ( (int)rect.getWidth (), descriptionPane.getHeight () - 10 ) ;
			        description.scrollRectToVisible ( rect ) ;
		        }
		        catch ( BadLocationException ble )
		        {
			        //
			        // this should never happen
			        //
			        System.err.println ( "DictionaryFrame.valueChanged .. Incorrect argument passed to modelToView." ) ;
                }
            }
		}	
        finally
        {
            listLock.unlock () ;
        }        
	}

    public void gotoList ()
    {
        entryList.requestFocusInWindow () ;
        entryList.setSelectedIndex ( 0 ) ;
    }

	/**
	 * unit test
	 */
	public static void main ( String [] args )
	{
		new DictionaryFrame () ;
	}
}
