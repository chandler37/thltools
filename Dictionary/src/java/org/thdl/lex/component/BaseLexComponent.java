package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * @author     Hibernate CodeGenerator
 * @created    October 7, 2003
 */
public abstract class BaseLexComponent implements org.thdl.lex.component.ILexComponent, Serializable
{

	/**
	 * identifier field
	 */
	private java.lang.Integer metaId;

	/**
	 * nullable persistent field
	 */
	private java.lang.Integer translationOf;

	/**
	 * persistent field
	 */
	private java.lang.Boolean deleted;

	/**
	 * persistent field
	 */
	private List analyticalNotes;

	/**
	 * persistent field
	 */
	private Set translations;

	/**
	 * persistent field
	 */
	private org.thdl.lex.component.Meta meta;


	/**
	 *  Sets the metaId attribute of the BaseLexComponent object
	 *
	 * @param  metaId  The new metaId value
	 */
	public void setMetaId( java.lang.Integer metaId )
	{
		this.metaId = metaId;
	}


	/**
	 *  Sets the translationOf attribute of the BaseLexComponent object
	 *
	 * @param  translationOf  The new translationOf value
	 */
	public void setTranslationOf( java.lang.Integer translationOf )
	{
		this.translationOf = translationOf;
	}


	/**
	 *  Sets the deleted attribute of the BaseLexComponent object
	 *
	 * @param  deleted  The new deleted value
	 */
	public void setDeleted( java.lang.Boolean deleted )
	{
		this.deleted = deleted;
	}


	/**
	 *  Sets the analyticalNotes attribute of the BaseLexComponent object
	 *
	 * @param  analyticalNotes  The new analyticalNotes value
	 */
	public void setAnalyticalNotes( java.util.List analyticalNotes )
	{
		this.analyticalNotes = analyticalNotes;
	}


	/**
	 *  Sets the translations attribute of the BaseLexComponent object
	 *
	 * @param  translations  The new translations value
	 */
	public void setTranslations( java.util.Set translations )
	{
		this.translations = translations;
	}


	/**
	 *  Sets the meta attribute of the BaseLexComponent object
	 *
	 * @param  meta  The new meta value
	 */
	public void setMeta( org.thdl.lex.component.Meta meta )
	{
		this.meta = meta;
	}


	/**
	 *  Gets the metaId attribute of the BaseLexComponent object
	 *
	 * @return    The metaId value
	 */
	public java.lang.Integer getMetaId()
	{
		return this.metaId;
	}


	/**
	 *  Gets the translationOf attribute of the BaseLexComponent object
	 *
	 * @return    The translationOf value
	 */
	public java.lang.Integer getTranslationOf()
	{
		return this.translationOf;
	}


	/**
	 *  Gets the deleted attribute of the BaseLexComponent object
	 *
	 * @return    The deleted value
	 */
	public java.lang.Boolean getDeleted()
	{
		return this.deleted;
	}


	/**
	 *  Gets the analyticalNotes attribute of the BaseLexComponent object
	 *
	 * @return    The analyticalNotes value
	 */
	public java.util.List getAnalyticalNotes()
	{
		return this.analyticalNotes;
	}


	/**
	 *  Gets the translations attribute of the BaseLexComponent object
	 *
	 * @return    The translations value
	 */
	public java.util.Set getTranslations()
	{
		return this.translations;
	}


	/**
	 *  Gets the meta attribute of the BaseLexComponent object
	 *
	 * @return    The meta value
	 */
	public org.thdl.lex.component.Meta getMeta()
	{
		return this.meta;
	}


	/**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
	public String toString()
	{
		return new ToStringBuilder( this )
				.append( "metaId", getMetaId() )
				.toString();
	}


	/**
	 *  Description of the Method
	 *
	 * @param  other  Description of the Parameter
	 * @return        Description of the Return Value
	 */
	public boolean equals( Object other )
	{
		if ( !( other instanceof BaseLexComponent ) )
		{
			return false;
		}
		BaseLexComponent castOther = (BaseLexComponent) other;
		return new EqualsBuilder()
				.append( this.getMetaId(), castOther.getMetaId() )
				.isEquals();
	}


	/**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
	public int hashCode()
	{
		return new HashCodeBuilder()
				.append( getMetaId() )
				.toHashCode();
	}


	/**
	 * full constructor
	 *
	 * @param  translationOf    Description of the Parameter
	 * @param  deleted          Description of the Parameter
	 * @param  analyticalNotes  Description of the Parameter
	 * @param  translations     Description of the Parameter
	 * @param  meta             Description of the Parameter
	 */
	public BaseLexComponent( java.lang.Integer translationOf, java.lang.Boolean deleted, List analyticalNotes, Set translations, org.thdl.lex.component.Meta meta )
	{
		this.translationOf = translationOf;
		this.deleted = deleted;
		this.analyticalNotes = analyticalNotes;
		this.translations = translations;
		this.meta = meta;
	}


	/**
	 * default constructor
	 */
	public BaseLexComponent() { }


	/**
	 * minimal constructor
	 *
	 * @param  deleted          Description of the Parameter
	 * @param  analyticalNotes  Description of the Parameter
	 * @param  translations     Description of the Parameter
	 * @param  meta             Description of the Parameter
	 */
	public BaseLexComponent( java.lang.Boolean deleted, List analyticalNotes, Set translations, org.thdl.lex.component.Meta meta )
	{
		this.deleted = deleted;
		this.analyticalNotes = analyticalNotes;
		this.translations = translations;
		this.meta = meta;
	}

}

