package org.thdl.lex.component;

/**
 * Description of the Interface
 * 
 * @author travis
 * @created October 16, 2003
 */
public interface ISpelling extends ILexComponent {
	/**
	 * Sets the parent attribute of the ISpelling object
	 * 
	 * @param comp
	 *            The new parent value
	 */

	/**
	 * Gets the parentId attribute of the ISpelling object
	 * 
	 * @return The parentId value
	 */
	public java.lang.Integer getParentId();

	/**
	 * Sets the parentId attribute of the ISpelling object
	 * 
	 * @param parentId
	 *            The new parentId value
	 */
	public void setParentId(java.lang.Integer parentId);

	/**
	 * Gets the spelling attribute of the ISpelling object
	 * 
	 * @return The spelling value
	 */
	public java.lang.String getSpelling();

	/**
	 * Sets the spelling attribute of the ISpelling object
	 * 
	 * @param spelling
	 *            The new spelling value
	 */
	public void setSpelling(java.lang.String spelling);

	/**
	 * Gets the spellingType attribute of the ISpelling object
	 * 
	 * @return The spellingType value
	 */
	public java.lang.Integer getSpellingType();

	/**
	 * Sets the spellingType attribute of the ISpelling object
	 * 
	 * @param spellingType
	 *            The new spellingType value
	 */
	public void setSpellingType(java.lang.Integer spellingType);
}

