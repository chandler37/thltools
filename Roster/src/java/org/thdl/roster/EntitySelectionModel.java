package org.thdl.roster;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.form.IPropertySelectionModel;

/**
 *  This class is used as a property selection model to select a primary key. We
 *  assume that the primary keys are integers, which makes it easy to translate
 *  between the various representations.
 *
 *@author     Howard Lewis Ship
 *@created    March 13, 2003
 *@version    $Id: EntitySelectionModel.java,v 1.5 2003/01/13 03:33:28 hlship
 *      Exp $
 */

public class EntitySelectionModel implements IPropertySelectionModel
{

	private final static int LIST_SIZE = 20;

	private List entries = new ArrayList( LIST_SIZE );


	/**
	 *  Description of the Method
	 *
	 *@param  key    Description of the Parameter
	 *@param  label  Description of the Parameter
	 */
	public void add( Integer key, String label )
	{
		Entry entry;

		entry = new Entry( key, label );
		entries.add( entry );
	}


	/**
	 *  Gets the optionCount attribute of the EntitySelectionModel object
	 *
	 *@return    The optionCount value
	 */
	public int getOptionCount()
	{
		return entries.size();
	}


	/**
	 *  Description of the Method
	 *
	 *@param  index  Description of the Parameter
	 *@return        Description of the Return Value
	 */
	private Entry get( int index )
	{
		return (Entry) entries.get( index );
	}


	/**
	 *  Gets the option attribute of the EntitySelectionModel object
	 *
	 *@param  index  Description of the Parameter
	 *@return        The option value
	 */
	public Object getOption( int index )
	{
		return get( index ).primaryKey;
	}


	/**
	 *  Gets the label attribute of the EntitySelectionModel object
	 *
	 *@param  index  Description of the Parameter
	 *@return        The label value
	 */
	public String getLabel( int index )
	{
		return get( index ).label;
	}


	/**
	 *  Gets the value attribute of the EntitySelectionModel object
	 *
	 *@param  index  Description of the Parameter
	 *@return        The value value
	 */
	public String getValue( int index )
	{
		Integer primaryKey;

		primaryKey = get( index ).primaryKey;

		if ( primaryKey == null )
		{
			return "";
		}

		return primaryKey.toString();
	}


	/**
	 *  Description of the Method
	 *
	 *@param  value  Description of the Parameter
	 *@return        Description of the Return Value
	 */
	public Object translateValue( String value )
	{
		if ( value.equals( "" ) )
		{
			return null;
		}

		try
		{
			return new Integer( value );
		}
		catch ( NumberFormatException e )
		{
			throw new ApplicationRuntimeException( "Could not convert '" + value + "' to an Integer.", e );
		}
	}


	private static class Entry
	{
		Integer primaryKey;
		String label;


		/**
		 *  Constructor for the Entry object
		 *
		 *@param  primaryKey  Description of the Parameter
		 *@param  label       Description of the Parameter
		 */
		Entry( Integer primaryKey, String label )
		{
			this.primaryKey = primaryKey;
			this.label = label;
		}

	}
}
