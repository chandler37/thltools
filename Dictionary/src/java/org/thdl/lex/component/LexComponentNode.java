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

	// public java.util.List getSiblings();
}

