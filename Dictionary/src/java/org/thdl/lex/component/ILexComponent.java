package org.thdl.lex.component;

import java.util.List;


/**
 *  Description of the Interface
 *
 * @author     travis
 * @created    October 1, 2003
 */
public interface ILexComponent
{

	/**
	 *  Sets the parent attribute of the ILexComponent object
	 *
	 * @param  parent  The new parent value
	 */
	public void setParent( ILexComponent parent );


	/**
	 *  Gets the parent attribute of the ILexComponent object
	 *
	 * @return    The parent value
	 */
	public ILexComponent getParent();


	/**
	 *  Sets the parentId attribute of the LexComponent object
	 *
	 * @param  parentId  The new parentId value
	 */
	public void setParentId( java.lang.Integer parentId );


	/**
	 *  Gets the parent attribute of the LexComponent object
	 *
	 * @return    The parent value
	 */

	/**
	 *  Gets the parentId attribute of the LexComponent object
	 *
	 * @return    The parentId value
	 */
	public java.lang.Integer getParentId();




	/**
	 *  Gets the label attribute of the ILexComponent object
	 *
	 * @return    The label value
	 * @since
	 */
	public java.lang.String getLabel();


	/**
	 *  Gets the metaId attribute of the ILexComponent object
	 *
	 * @return    The metaId value
	 * @since
	 */
	public java.lang.Integer getMetaId();


	/**
	 *  Sets the metaId attribute of the ILexComponent object
	 *
	 * @param  metaId  The new metaId value
	 * @since
	 */
	public void setMetaId( java.lang.Integer metaId );



	/**
	 *  Gets the translations attribute of the ILexComponent object
	 *
	 * @return    The translations value
	 * @since
	 */
	// public java.util.Set getTranslations();


	/**
	 *  Sets the translations attribute of the ILexComponent object
	 *
	 * @return    The deleted value
	 * @since
	 */
	//public void setTranslations( java.util.Set translations );


	/**
	 *  Gets the deleted attribute of the ILexComponent object
	 *
	 * @return    The deleted value
	 * @since
	 */
	public java.lang.Boolean getDeleted();


	/**
	 *  Sets the deleted attribute of the ILexComponent object
	 *
	 * @param  deleted  The new deleted value
	 * @since
	 */
	public void setDeleted( java.lang.Boolean deleted );


	/**
	 *  Gets the analyticalNotes attribute of the ILexComponent object
	 *
	 * @return    The analyticalNotes value
	 * @since
	 */
	public java.util.List getAnalyticalNotes();


	/**
	 *  Sets the analyticalNotes attribute of the ILexComponent object
	 *
	 * @param  analyticalNotes  The new analyticalNotes value
	 * @since
	 */
	public void setAnalyticalNotes( java.util.List analyticalNotes );


	/**
	 *  Gets the meta attribute of the ILexComponent object
	 *
	 * @return    The meta value
	 * @since
	 */
	public org.thdl.lex.component.Meta getMeta();


	/**
	 *  Sets the meta attribute of the ILexComponent object
	 *
	 * @param  meta  The new meta value
	 * @since
	 */
	public void setMeta( org.thdl.lex.component.Meta meta );


	/**
	 *  Description of the Method
	 *
	 * @param  properties                 Description of Parameter
	 * @exception  LexComponentException  Description of Exception
	 * @since
	 */
	public void populate( java.util.Map properties ) throws LexComponentException;


	/**
	 *  Description of the Method
	 *
	 * @param  component                  Description of the Parameter
	 * @exception  LexComponentException  Description of the Exception
	 */
	public void populate( ILexComponent component ) throws LexComponentException;


	/**
	 *  Adds a feature to the SiblingList attribute of the LexComponentNode object
	 *
	 * @param  component                  The feature to be added to the SiblingList attribute
	 * @param  list                       The feature to be added to the SiblingList attribute
	 * @param  parent                     The feature to be added to the SiblingList attribute
	 * @exception  LexComponentException  Description of the Exception
	 */
	public void addSiblingList( ILexComponent parent, ILexComponent component, List list ) throws LexComponentException;
}

