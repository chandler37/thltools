package org.thdl.lex.commands;

import javax.servlet.http.HttpServletRequest;

import org.thdl.lex.*;
import org.thdl.lex.component.*;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 6, 2003
 */
public class RefreshSourcesCommand extends LexCommand implements Command
{
	/**
	 *  Description of the Method
	 *
	 * @param  req                   Description of the Parameter
	 * @param  component             Description of the Parameter
	 * @return                       Description of the Return Value
	 * @exception  CommandException  Description of the Exception
	 */
	public String execute( HttpServletRequest req, ILexComponent component ) throws CommandException
	{
		try
		{
			LexSourceRepository lcr = LexSourceRepository.getInstance();
			req.setAttribute( "testArray", lcr.xmlTesting() );

		}
		catch ( Exception lre )
		{
			throw new CommandException( lre );
		}
		return getNext();
	}



	/**
	 *Constructor for the PreferencesCommand object
	 *
	 * @param  next  Description of the Parameter
	 */
	public RefreshSourcesCommand( String next )
	{
		super( next );
	}
}

