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
	private Meta meta;
	private Integer metaId;
	private Integer parentId;


	/*
	    public List getTranslations()
	    {
	    List nullNullNullNull = null;
	    return nullNullNullNull;
	    }
	  */
	/**
	 *  Gets the meta attribute of the LexComponent object
	 *
	 * @return    The meta value
	 */
	public Meta getMeta()
	{
		if ( null == this.meta )
		{
			setMeta( new Meta() );
		}
		return this.meta;
	}


	/**
	 *  Sets the meta attribute of the LexComponent object
	 *
	 * @param  meta  The new meta value
	 */
	public void setMeta( Meta meta )
	{
		this.meta = meta;
	}


	/**
	 *  Sets the metaId attribute of the LexComponent object
	 *
	 * @param  metaId  The new metaId value
	 */
	public void setMetaId( Integer metaId )
	{
		if ( metaId.intValue() == 0 )
		{
			this.metaId = null;
		}
		this.metaId = metaId;
	}


	/**
	 *  Gets the metaId attribute of the LexComponent object
	 *
	 * @return    The metaId value
	 */
	public Integer getMetaId()
	{
		return metaId;
	}


	/**
	 *  Gets the precedence attribute of the LexComponent object
	 *
	 * @return    The precedence value
	 */
	public abstract java.lang.Integer getPrecedence();


	/**
	 *  Sets the precedence attribute of the LexComponent object
	 *
	 * @param  precedence  The new precedence value
	 */
	public abstract void setPrecedence( java.lang.Integer precedence );


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
	public void setParentId( java.lang.Integer parentId )
	{
		if ( parentId.intValue() == 0 )
		{
			this.parentId = null;
		}
		this.parentId = parentId;
	}


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
	public java.lang.Integer getParentId()
	{
		return this.parentId;
	}


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


	/**
	 *  Description of the Method
	 *
	 * @param  component                  Description of the Parameter
	 * @exception  LexComponentException  Description of the Exception
	 */
	public void populate( ILexComponent component ) throws LexComponentException
	{
		try
		{
			BeanUtils.copyProperties( this, component );
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
	 *Constructor for the LexComponent object
	 *
	 * @param  deleted          Description of the Parameter
	 * @param  analyticalNotes  Description of the Parameter
	 * @param  meta             Description of the Parameter
	 */
	public LexComponent( java.lang.Boolean deleted, java.util.List analyticalNotes, org.thdl.lex.component.Meta meta )
	{
		super( deleted, analyticalNotes, meta );
	}


	/**
	 *Constructor for the LexComponent object
	 */
	public LexComponent()
	{
		super();
		setDeleted( Boolean.FALSE );
	}
}

