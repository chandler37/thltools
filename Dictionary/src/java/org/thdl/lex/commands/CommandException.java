package org.thdl.lex.commands;

import org.thdl.lex.*;
import org.thdl.lex.component.*;


/**
 *  Description of the Class
 *
 *@author     travis
 *@created    October 1, 2003
 */
public class CommandException extends Exception
{
	/**
	 *  Constructor for the CommandException object
	 *
	 *@since
	 */
	public CommandException()
	{
		super();
	}


	/**
	 *  Constructor for the CommandException object
	 *
	 *@param  msg  Description of Parameter
	 *@since
	 */
	public CommandException( String msg )
	{
		super( msg );
	}


	/**
	 *  Constructor for the CommandException object
	 *
	 *@param  msg  Description of Parameter
	 *@param  e    Description of Parameter
	 *@since
	 */
	public CommandException( String msg, Exception e )
	{
		super( msg, e );
	}


	/**
	 *  Constructor for the CommandException object
	 *
	 *@param  e  Description of Parameter
	 *@since
	 */
	public CommandException( Exception e )
	{
		super( e );
	}
}

