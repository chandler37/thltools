package org.thdl.lex.component;


/**
 *  Description of the Interface
 *
 *@author     travis
 *@created    October 3, 2003
 */
public interface ITranslationEquivalent extends ILexComponent
{	public ILexComponent getParent();
	public void setParent( ILexComponent comp );
	/**
	 *  Gets the translationEquivalent attribute of the ITranslationEquivalent
	 *  object
	 *
	 *@return    The translationEquivalent value
	 *@since
	 */
	public java.lang.String getTranslationEquivalent();


	/**
	 *  Sets the translationEquivalent attribute of the ITranslationEquivalent
	 *  object
	 *
	 *@param  translationEquivalent  The new translationEquivalent value
	 *@since
	 */
	public void setTranslationEquivalent( java.lang.String translationEquivalent );
}

