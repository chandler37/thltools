package org.thdl.lex.component;

/**
 * Description of the Interface
 * 
 * @author travis
 * @created October 16, 2003
 */
public interface IFunction extends ILexComponent {
	/**
	 * Sets the parent attribute of the IFunction object
	 * 
	 * @param comp
	 *            The new parent value
	 */

	/**
	 * Gets the parentId attribute of the IFunction object
	 * 
	 * @return The parentId value
	 */
	public java.lang.Integer getParentId();

	/**
	 * Sets the parentId attribute of the IFunction object
	 * 
	 * @param parentId
	 *            The new parentId value
	 */
	public void setParentId(java.lang.Integer parentId);

	/**
	 * Gets the function attribute of the IFunction object
	 * 
	 * @return The function value
	 */
	public java.lang.Integer getFunction();

	/**
	 * Sets the function attribute of the IFunction object
	 * 
	 * @param function
	 *            The new function value
	 */
	public void setFunction(java.lang.Integer function);
}

