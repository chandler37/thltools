package org.thdl.lex.component;


/**
 *  Description of the Interface
 *
 * @author     travis
 * @created    October 16, 2003
 */
public interface IKeyword extends ILexComponent
{
	/**
	 *  Sets the parent attribute of the IKeyword object
	 *
	 * @param  comp  The new parent value
	 */
	


	/**
	 *  Gets the parentId attribute of the IKeyword object
	 *
	 * @return    The parentId value
	 */
	public java.lang.Integer getParentId();


	/**
	 *  Sets the parentId attribute of the IKeyword object
	 *
	 * @param  parentId  The new parentId value
	 */
	public void setParentId( java.lang.Integer parentId );


	/**
	 *  Gets the keyword attribute of the IKeyword object
	 *
	 * @return    The keyword value
	 */
	public java.lang.String getKeyword();


	/**
	 *  Sets the keyword attribute of the IKeyword object
	 *
	 * @param  keyword  The new keyword value
	 */
	public void setKeyword( java.lang.String keyword );
}


