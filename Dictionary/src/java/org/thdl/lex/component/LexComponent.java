package org.thdl.lex.component;

import org.apache.commons.beanutils.BeanUtils;
import java.io.Serializable;
import java.util.*;


/**
 *  Description of the Class
 *
 *@author     travis
 *@created    October 1, 2003
 */
// public abstract class LexComponent extends BaseLexComponent implements Serializable
public abstract class LexComponent extends BaseLexComponent implements Serializable
{

	private String label;


	/**
	 *  Sets the label attribute of the LexComponent object
	 *
	 *@param  label  The new label value
	 *@since
	 */
	public void setLabel( String label )
	{
		this.label = label;
	}


	/**
	 *  Gets the label attribute of the LexComponent object
	 *
	 *@return    The label value
	 *@since
	 */
	public String getLabel()
	{
		if ( null == label )
		{
			String labelHex = this.toString();
			int cutoff = labelHex.indexOf( "@" );
			labelHex = labelHex.substring( 0, cutoff );
			labelHex = labelHex + "." + getMetaId();
			labelHex = labelHex.replace( ".".toCharArray()[0], ":".toCharArray()[0] );
			setLabel( labelHex );
		}
		return label;
	}


	//helper methods
	/**
	 *  Description of the Method
	 *
	 *@param  properties                 Description of Parameter
	 *@exception  LexComponentException  Description of Exception
	 *@since
	 */
	public void populate( Map properties ) throws LexComponentException
	{
		try
		{
			BeanUtils.populate( this, properties );
		}
		catch ( IllegalAccessException iae )
		{
			throw new LexComponentException( iae );
		}
		catch ( java.lang.reflect.InvocationTargetException ite )
		{
			throw new LexComponentException( ite );
		}

	}


//constructors
	/**
	 *  Constructor for the LexComponent object
	 *
	 *@param  translationOf    Description of Parameter
	 *@param  deleted          Description of Parameter
	 *@param  analyticalNotes  Description of Parameter
	 *@param  meta             Description of Parameter
	 *@param  translations     Description of Parameter
	 *@since
	 */
	public LexComponent( Integer translationOf, Boolean deleted, List analyticalNotes, Set translations, Meta meta )
	{
		super( translationOf, deleted, analyticalNotes, translations, meta );
	}


	/**
	 *  Constructor for the LexComponent object
	 *
	 *@param  deleted          Description of Parameter
	 *@param  analyticalNotes  Description of Parameter
	 *@param  meta             Description of Parameter
	 *@param  translations     Description of Parameter
	 *@since
	 */
	public LexComponent( Boolean deleted, List analyticalNotes, Set translations, Meta meta )
	{
		super( deleted, analyticalNotes, translations, meta );
	}


	/**
	 *  Constructor for the LexComponent object
	 *
	 *@since
	 */
	public LexComponent()
	{
		super();
	}
}

