package org.thdl.lex.component;

/**
 * Description of the Interface
 * 
 * @author travis
 * @created October 16, 2003
 */
public interface IModelSentence extends Translatable {
	/**
	 * Sets the parent attribute of the IModelSentence object
	 * 
	 * @return The parentId value
	 */

	/**
	 * Gets the parentId attribute of the IModelSentence object
	 * 
	 * @return The parentId value
	 */
	public java.lang.Integer getParentId();

	/**
	 * Sets the parentId attribute of the IModelSentence object
	 * 
	 * @param parentId
	 *            The new parentId value
	 */
	public void setParentId(java.lang.Integer parentId);

	/**
	 * Gets the modelSentence attribute of the IModelSentence object
	 * 
	 * @return The modelSentence value
	 */
	public java.lang.String getModelSentence();

	/**
	 * Sets the modelSentence attribute of the IModelSentence object
	 * 
	 * @param modelSentence
	 *            The new modelSentence value
	 */
	public void setModelSentence(java.lang.String modelSentence);
}

