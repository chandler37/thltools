package org.thdl.lex.component;
import java.io.Serializable;
import java.util.*;

import org.apache.commons.beanutils.BeanUtils;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 1, 2003
 */
// public abstract class LexComponent extends BaseLexComponent implements Serializable
public abstract class LexComponent extends BaseLexComponent implements Serializable
{

	/**
	 *  Gets the precedence attribute of the LexComponent object
	 *
	 * @return    The precedence value
	 */
	public abstract java.lang.Short getPrecedence();


	/**
	 *  Sets the precedence attribute of the LexComponent object
	 *
	 * @param  precedence  The new precedence value
	 */
	public abstract void setPrecedence( java.lang.Short precedence );


	/**
	 *  Sets the parent attribute of the LexComponent object
	 *
	 * @param  comp  The new parent value
	 */
	public abstract void setParent( ILexComponent comp );


	/**
	 *  Sets the parentId attribute of the LexComponent object
	 *
	 * @param  parentId  The new parentId value
	 */
	public abstract void setParentId( java.lang.Integer parentId );


	/**
	 *  Gets the parent attribute of the LexComponent object
	 *
	 * @return    The parent value
	 */
	public abstract ILexComponent getParent();


	/**
	 *  Gets the parentId attribute of the LexComponent object
	 *
	 * @return    The parentId value
	 */
	public abstract java.lang.Integer getParentId();


	/**
	 *  Gets the label attribute of the LexComponent object
	 *
	 * @return    The label value
	 * @since
	 */
	public String getLabel()
	{
		String label = null;
		StringTokenizer tokens = new StringTokenizer( this.getClass().getName(), "." );
		while ( tokens.hasMoreTokens() )
		{
			label = tokens.nextToken();
		}
		String first = label.substring( 0, 1 ).toLowerCase();
		String rest = label.substring( 1, label.length() );
		return first + rest;
	}


	//helper methods
	/**
	 *  Description of the Method
	 *
	 * @param  properties                 Description of Parameter
	 * @exception  LexComponentException  Description of Exception
	 * @since
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
	 * @param  translationOf    Description of Parameter
	 * @param  deleted          Description of Parameter
	 * @param  analyticalNotes  Description of Parameter
	 * @param  meta             Description of Parameter
	 * @param  translations     Description of Parameter
	 * @since
	 */
	public LexComponent( Integer translationOf, Boolean deleted, List analyticalNotes, Set translations, Meta meta )
	{
		super( translationOf, deleted, analyticalNotes, translations, meta );
	}


	/**
	 *  Constructor for the LexComponent object
	 *
	 * @param  deleted          Description of Parameter
	 * @param  analyticalNotes  Description of Parameter
	 * @param  meta             Description of Parameter
	 * @param  translations     Description of Parameter
	 * @since
	 */
	public LexComponent( Boolean deleted, List analyticalNotes, Set translations, Meta meta )
	{
		super( deleted, analyticalNotes, translations, meta );
	}


	/**
	 *  Constructor for the LexComponent object
	 *
	 * @since
	 */
	public LexComponent()
	{
		super();
	}
}

