package org.thdl.lex.component;


/**
 *  Description of the Interface
 *
 *@author     travis
 *@created    October 1, 2003
 */
public interface ILexComponent
{

	/**
	 *  Gets the label attribute of the ILexComponent object
	 *
	 *@return    The label value
	 *@since
	 */
	public java.lang.String getLabel();


	/**
	 *  Sets the label attribute of the ILexComponent object
	 *
	 *@param  label  The new label value
	 *@since
	 */
	public void setLabel( java.lang.String label );


	/**
	 *  Gets the metaId attribute of the ILexComponent object
	 *
	 *@return    The metaId value
	 *@since
	 */
	public java.lang.Integer getMetaId();


	/**
	 *  Sets the metaId attribute of the ILexComponent object
	 *
	 *@param  metaId  The new metaId value
	 *@since
	 */
	public void setMetaId( java.lang.Integer metaId );


	/**
	 *  Gets the translationOf attribute of the ILexComponent object
	 *
	 *@return    The translationOf value
	 *@since
	 */
	public java.lang.Integer getTranslationOf();


	/**
	 *  Sets the translationOf attribute of the ILexComponent object
	 *
	 *@param  translationOf  The new translationOf value
	 *@since
	 */
	public void setTranslationOf( java.lang.Integer translationOf );


	/**
	 *  Gets the translations attribute of the ILexComponent object
	 *
	 *@return    The translations value
	 *@since
	 */
	public java.util.Set getTranslations();


	/**
	 *  Sets the translations attribute of the ILexComponent object
	 *
	 *@param  translations  The new translations value
	 *@since
	 */
	public void setTranslations( java.util.Set translations );


	/**
	 *  Gets the deleted attribute of the ILexComponent object
	 *
	 *@return    The deleted value
	 *@since
	 */
	public java.lang.Boolean getDeleted();


	/**
	 *  Sets the deleted attribute of the ILexComponent object
	 *
	 *@param  deleted  The new deleted value
	 *@since
	 */
	public void setDeleted( java.lang.Boolean deleted );


	/**
	 *  Gets the analyticalNotes attribute of the ILexComponent object
	 *
	 *@return    The analyticalNotes value
	 *@since
	 */
	public java.util.List getAnalyticalNotes();


	/**
	 *  Sets the analyticalNotes attribute of the ILexComponent object
	 *
	 *@param  analyticalNotes  The new analyticalNotes value
	 *@since
	 */
	public void setAnalyticalNotes( java.util.List analyticalNotes );


	/**
	 *  Gets the meta attribute of the ILexComponent object
	 *
	 *@return    The meta value
	 *@since
	 */
	public org.thdl.lex.component.Meta getMeta();


	/**
	 *  Sets the meta attribute of the ILexComponent object
	 *
	 *@param  meta  The new meta value
	 *@since
	 */
	public void setMeta( org.thdl.lex.component.Meta meta );


	/**
	 *  Description of the Method
	 *
	 *@param  properties                 Description of Parameter
	 *@exception  LexComponentException  Description of Exception
	 *@since
	 */
	public void populate( java.util.Map properties ) throws LexComponentException;
}

