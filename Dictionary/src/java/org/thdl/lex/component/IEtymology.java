package org.thdl.lex.component;


/**
 *  Description of the Interface
 *
 * @author     travis
 * @created    October 16, 2003
 */
public interface IEtymology extends Translatable
{
	/**
	 *  Sets the parent attribute of the IEtymology object
	 *
	 * @param  comp  The new parent value
	 */
	


	/**
	 *  Gets the parentId attribute of the IEtymology object
	 *
	 * @return    The parentId value
	 */
	public java.lang.Integer getParentId();


	/**
	 *  Sets the parentId attribute of the IEtymology object
	 *
	 * @param  parentId  The new parentId value
	 */
	public void setParentId( java.lang.Integer parentId );


	/**
	 *  Gets the loanLanguage attribute of the IEtymology object
	 *
	 * @return    The loanLanguage value
	 */
	public java.lang.Integer getLoanLanguage();


	/**
	 *  Sets the loanLanguage attribute of the IEtymology object
	 *
	 * @param  loanLanguage  The new loanLanguage value
	 */
	public void setLoanLanguage( java.lang.Integer loanLanguage );


	/**
	 *  Gets the etymologyType attribute of the IEtymology object
	 *
	 * @return    The etymologyType value
	 */
	public java.lang.Integer getEtymologyType();


	/**
	 *  Sets the etymologyType attribute of the IEtymology object
	 *
	 * @param  etymologyType  The new etymologyType value
	 */
	public void setEtymologyType( java.lang.Integer etymologyType );


	/**
	 *  Gets the derivation attribute of the IEtymology object
	 *
	 * @return    The derivation value
	 */
	public java.lang.String getDerivation();


	/**
	 *  Sets the derivation attribute of the IEtymology object
	 *
	 * @param  derivation  The new derivation value
	 */
	public void setDerivation( java.lang.String derivation );


	/**
	 *  Gets the etymologyDescription attribute of the IEtymology object
	 *
	 * @return    The etymologyDescription value
	 */
	public java.lang.String getEtymologyDescription();


	/**
	 *  Sets the etymologyDescription attribute of the IEtymology object
	 *
	 * @param  etymologyDescription  The new etymologyDescription value
	 */
	public void setEtymologyDescription( java.lang.String etymologyDescription );
}

