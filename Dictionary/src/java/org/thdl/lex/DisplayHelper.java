package org.thdl.lex;

import java.util.*;
import org.apache.commons.beanutils.*;
import org.thdl.lex.component.*;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 14, 2003
 */
public class DisplayHelper
{
	private Collection collection;
	private ILexComponent component;
	private ILexComponent note;

	private boolean showNotes;
	private boolean showMeta;
	private boolean showEditOptions;
	private boolean showTranslations;


	/**
	 *  Sets the note attribute of the DisplayHelper object
	 *
	 * @param  note  The new note value
	 */
	public void setNote( ILexComponent note )
	{
		this.note = note;
	}


	/**
	 *  Gets the note attribute of the DisplayHelper object
	 *
	 * @return    The note value
	 */
	public ILexComponent getNote()
	{
		return note;
	}


	/**
	 *  Sets the component attribute of the DisplayHelper object
	 *
	 * @param  component  The new component value
	 */
	public void setComponent( ILexComponent component )
	{
		this.component = component;
	}


	/**
	 *  Gets the component attribute of the DisplayHelper object
	 *
	 * @return    The component value
	 */
	public ILexComponent getComponent()
	{
		return component;
	}


	/**
	 *  Sets the showEditOptions attribute of the DisplayHelper object
	 *
	 * @param  showEditOptions  The new showEditOptions value
	 */
	public void setShowEditOptions( boolean showEditOptions )
	{
		this.showEditOptions = showEditOptions;
	}


	/**
	 *  Gets the showEditOptions attribute of the DisplayHelper object
	 *
	 * @return    The showEditOptions value
	 */
	public boolean getShowEditOptions()
	{
		return showEditOptions;
	}


	/**
	 *  Sets the showTranslations attribute of the DisplayHelper object
	 *
	 * @param  showTranslations  The new showTranslations value
	 */
	public void setShowTranslations( boolean showTranslations )
	{
		this.showTranslations = showTranslations;
	}


	/**
	 *  Gets the showTranslations attribute of the DisplayHelper object
	 *
	 * @return    The showTranslations value
	 */
	public boolean getShowTranslations()
	{
		return showTranslations;
	}


	/**
	 *  Sets the showNotes attribute of the DisplayHelper object
	 *
	 * @param  showNotes  The new showNotes value
	 */
	public void setShowNotes( boolean showNotes )
	{
		this.showNotes = showNotes;
	}


	/**
	 *  Sets the showMeta attribute of the DisplayHelper object
	 *
	 * @param  showMeta  The new showMeta value
	 */
	public void setShowMeta( boolean showMeta )
	{
		this.showMeta = showMeta;
	}


	/**
	 *  Gets the showNotes attribute of the DisplayHelper object
	 *
	 * @return    The showNotes value
	 */
	public boolean getShowNotes()
	{
		return showNotes;
	}


	/**
	 *  Gets the showMeta attribute of the DisplayHelper object
	 *
	 * @return    The showMeta value
	 */
	public boolean getShowMeta()
	{
		return showMeta;
	}



	/**
	 *  Sets the collection attribute of the DisplayHelper object
	 *
	 * @param  collection  The new collection value
	 */
	public void setCollection( Collection collection )
	{
		this.collection = collection;
	}


	/**
	 *  Gets the collection attribute of the DisplayHelper object
	 *
	 * @return    The collection value
	 */
	public Collection getCollection()
	{
		return collection;
	}


	/**
	 *  Gets the collectionSize attribute of the DisplayHelper object
	 *
	 * @return    The collectionSize value
	 */
	public int getCollectionSize()
	{
		int size = 0;
		if ( null != getCollection() )
		{
			size = getCollection().size();
		}
		return size;
	}


	/**
	 *  Gets the componentIsTranslation attribute of the DisplayHelper object
	 *
	 * @return    The componentIsTranslation value
	 */
	public boolean getComponentIsTranslation()
	{

		boolean b = false;
		if ( null != getComponent() && getComponent() instanceof Translatable )
		{
			Translatable t = (Translatable) getComponent();
			b = t.getTranslationOf() != null ? true : false;
		}
		return b;
	}

// helpers
	/**
	 *  Description of the Method
	 */
	public void clear()
	{
		setShowEditOptions( false );
		setShowMeta( false );
		setShowTranslations( false );
		setShowNotes( false );
	}


	/**
	 *  Description of the Method
	 *
	 * @param  map            Description of the Parameter
	 * @exception  Exception  Description of the Exception
	 */
	public void populate( Map map ) throws Exception
	{
		clear();
		BeanUtils.populate( this, map );
	}


	/**
	 *Constructor for the DisplayHelper object
	 */
	public DisplayHelper() { }
}

