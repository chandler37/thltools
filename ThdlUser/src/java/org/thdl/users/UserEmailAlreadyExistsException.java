package org.thdl.users;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 6, 2003
 */
public class UserEmailAlreadyExistsException extends ThdlUserRepositoryException
{
	/**
	 *Constructor for the UserEmailAlreadyExistsException object
	 */
	public UserEmailAlreadyExistsException()
	{
		super();
	}


	/**
	 *Constructor for the UserEmailAlreadyExistsException object
	 *
	 * @param  msg  Description of the Parameter
	 */
	public UserEmailAlreadyExistsException( String msg )
	{
		super( msg );
	}
}

