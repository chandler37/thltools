package org.thdl.users;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 6, 2003
 */
public class ThdlUserRepositoryException extends Exception
{
	/**
	 *Constructor for the ThdlUserRepositoryException object
	 */
	public ThdlUserRepositoryException()
	{
		super();
	}


	/**
	 *Constructor for the ThdlUserRepositoryException object
	 *
	 * @param  msg  Description of the Parameter
	 */
	public ThdlUserRepositoryException( String msg )
	{
		super( msg );
	}


	/**
	 *Constructor for the ThdlUserRepositoryException object
	 *
	 * @param  e  Description of the Parameter
	 */
	public ThdlUserRepositoryException( Exception e )
	{
		super( e );
	}


	/**
	 *Constructor for the ThdlUserRepositoryException object
	 *
	 * @param  msg  Description of the Parameter
	 * @param  e    Description of the Parameter
	 */
	public ThdlUserRepositoryException( String msg, Exception e )
	{
		super( msg, e );
	}
}

