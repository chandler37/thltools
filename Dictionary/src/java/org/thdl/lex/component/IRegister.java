package org.thdl.lex.component;


/**
 *  Description of the Interface
 *
 * @author     travis
 * @created    October 16, 2003
 */
public interface IRegister extends ILexComponent
{
	/**
	 *  Sets the parent attribute of the IRegister object
	 *
	 * @param  comp  The new parent value
	 */
	


	/**
	 *  Gets the parentId attribute of the IRegister object
	 *
	 * @return    The parentId value
	 */
	public java.lang.Integer getParentId();


	/**
	 *  Sets the parentId attribute of the IRegister object
	 *
	 * @param  parentId  The new parentId value
	 */
	public void setParentId( java.lang.Integer parentId );


	/**
	 *  Gets the register attribute of the IRegister object
	 *
	 * @return    The register value
	 */
	public java.lang.Integer getRegister();


	/**
	 *  Sets the register attribute of the IRegister object
	 *
	 * @param  register  The new register value
	 */
	public void setRegister( java.lang.Integer register );
}

