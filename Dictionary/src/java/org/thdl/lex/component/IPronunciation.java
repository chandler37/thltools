package org.thdl.lex.component;


/**
 *  Description of the Interface
 *
 * @author     travis
 * @created    October 16, 2003
 */
public interface IPronunciation extends ILexComponent
{
	/**
	 *  Sets the parent attribute of the IPronunciation object
	 *
	 * @param  comp  The new parent value
	 */
	


	/**
	 *  Gets the parentId attribute of the IPronunciation object
	 *
	 * @return    The parentId value
	 */
	public java.lang.Integer getParentId();


	/**
	 *  Sets the parentId attribute of the IPronunciation object
	 *
	 * @param  parentId  The new parentId value
	 */
	public void setParentId( java.lang.Integer parentId );


	/**
	 *  Gets the phonetics attribute of the IPronunciation object
	 *
	 * @return    The phonetics value
	 */
	public java.lang.String getPhonetics();


	/**
	 *  Sets the phonetics attribute of the IPronunciation object
	 *
	 * @param  phonetics  The new phonetics value
	 */
	public void setPhonetics( java.lang.String phonetics );


	/**
	 *  Gets the phoneticsType attribute of the IPronunciation object
	 *
	 * @return    The phoneticsType value
	 */
	public java.lang.Integer getPhoneticsType();


	/**
	 *  Sets the phoneticsType attribute of the IPronunciation object
	 *
	 * @param  phoneticsType  The new phoneticsType value
	 */
	public void setPhoneticsType( java.lang.Integer phoneticsType );
}

