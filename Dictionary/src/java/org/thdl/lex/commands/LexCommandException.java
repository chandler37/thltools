package org.thdl.lex.commands;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 1, 2003
 */
public class LexCommandException extends Exception
{
	/**
	 *  Constructor for the LexCommandException object
	 *
	 * @since
	 */
	public LexCommandException()
	{
		super();
	}


	/**
	 *  Constructor for the LexCommandException object
	 *
	 * @param  msg  Description of Parameter
	 * @since
	 */
	public LexCommandException( String msg )
	{
		super( msg );
	}


	/**
	 *  Constructor for the LexCommandException object
	 *
	 * @param  e  Description of Parameter
	 * @since
	 */
	public LexCommandException( Exception e )
	{
		super( e );
	}


	/**
	 *  Constructor for the LexCommandException object
	 *
	 * @param  msg  Description of Parameter
	 * @param  e    Description of Parameter
	 * @since
	 */
	public LexCommandException( String msg, Exception e )
	{
		super( msg, e );
	}
}

