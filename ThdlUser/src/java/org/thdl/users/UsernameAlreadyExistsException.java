package org.thdl.users;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 6, 2003
 */
public class UsernameAlreadyExistsException extends ThdlUserRepositoryException
{
	/**
	 *Constructor for the UsernameAlreadyExistsException object
	 */
	public UsernameAlreadyExistsException()
	{
		super();
	}


	/**
	 *Constructor for the UsernameAlreadyExistsException object
	 *
	 * @param  msg  Description of the Parameter
	 */
	public UsernameAlreadyExistsException( String msg )
	{
		super( msg );
	}
}

