package org.thdl.lex.component;

/**
 * Description of the Interface
 * 
 * @author travis
 * @created October 16, 2003
 */
public interface IRelatedTerm extends ILexComponent {
	/**
	 * Sets the parent attribute of the IRelatedTerm object
	 * 
	 * @param comp
	 *            The new parent value
	 */

	/**
	 * Gets the parentId attribute of the IRelatedTerm object
	 * 
	 * @return The parentId value
	 */
	public java.lang.Integer getParentId();

	/**
	 * Sets the parentId attribute of the IRelatedTerm object
	 * 
	 * @param parentId
	 *            The new parentId value
	 */
	public void setParentId(java.lang.Integer parentId);

	/**
	 * Gets the relatedTerm attribute of the IRelatedTerm object
	 * 
	 * @return The relatedTerm value
	 */
	public java.lang.String getRelatedTerm();

	/**
	 * Sets the relatedTerm attribute of the IRelatedTerm object
	 * 
	 * @param relatedTerm
	 *            The new relatedTerm value
	 */
	public void setRelatedTerm(java.lang.String relatedTerm);

	/**
	 * Gets the relatedTermType attribute of the IRelatedTerm object
	 * 
	 * @return The relatedTermType value
	 */
	public java.lang.Integer getRelatedTermType();

	/**
	 * Sets the relatedTermType attribute of the IRelatedTerm object
	 * 
	 * @param relatedTermType
	 *            The new relatedTermType value
	 */
	public void setRelatedTermType(java.lang.Integer relatedTermType);
}

