package org.thdl.lex.component;


/**
 *  Description of the Interface
 *
 * @author     travis
 * @created    October 13, 2003
 */
public interface LexComponentNode extends ILexComponent
{
	/*
	    public ILexComponent findChild( ILexComponent component ) throws LexComponentException;
	    public void addChild( ILexComponent component ) throws LexComponentException;
	  */
	/**
	 *  Gets the childMap attribute of the ISubdefinition object
	 *
	 * @return    The childMap value
	 */
	public java.util.HashMap getChildMap();


	/**
	 *  Description of the Method
	 *
	 * @param  component                  Description of the Parameter
	 * @return                            Description of the Return Value
	 * @exception  LexComponentException  Description of the Exception
	 */
	public java.util.List findSiblings( ILexComponent component ) throws LexComponentException;
}

