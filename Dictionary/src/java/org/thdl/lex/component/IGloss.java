package org.thdl.lex.component;

/**
 * Description of the Interface
 * 
 * @author travis
 * @created October 16, 2003
 */
public interface IGloss extends ILexComponent {
	/**
	 * Sets the parent attribute of the IGloss object
	 * 
	 * @param comp
	 *            The new parent value
	 */

	/**
	 * Gets the parentId attribute of the IGloss object
	 * 
	 * @return The parentId value
	 */
	public java.lang.Integer getParentId();

	/**
	 * Sets the parentId attribute of the IGloss object
	 * 
	 * @param parentId
	 *            The new parentId value
	 */
	public void setParentId(java.lang.Integer parentId);

	/**
	 * Gets the gloss attribute of the IGloss object
	 * 
	 * @return The gloss value
	 */
	public java.lang.String getGloss();

	/**
	 * Sets the gloss attribute of the IGloss object
	 * 
	 * @param gloss
	 *            The new gloss value
	 */
	public void setGloss(java.lang.String gloss);

	/**
	 * Gets the translation attribute of the IGloss object
	 * 
	 * @return The translation value
	 */
	public java.lang.String getTranslation();

	/**
	 * Sets the translation attribute of the IGloss object
	 * 
	 * @param translation
	 *            The new translation value
	 */
	public void setTranslation(java.lang.String translation);
}

