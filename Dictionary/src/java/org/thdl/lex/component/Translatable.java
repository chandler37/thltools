package org.thdl.lex.component;


/**
 *  Description of the Interface
 *
 * @author     travis
 * @created    October 13, 2003
 */
public interface Translatable extends ILexComponent
{
	/**
	 *  Gets the translationOf attribute of the Translatable object
	 *
	 * @return    The translationOf value
	 */
	public Integer getTranslationOf();


	/**
	 *  Lists the translationOf attribute of the Translatable object
	 *
	 * @param  pkReference  The new translationOf value
	 */
	public void setTranslationOf( Integer pkReference );


	/**
	 *  Gets the translations attribute of the Translatable object
	 *
	 * @return    The translations value
	 */
	public java.util.List getTranslations();


	/**
	 *  Lists the translations attribute of the Translatable object
	 *
	 * @param  translations  The new translations value
	 */
	public void setTranslations( java.util.List translations );

}

