package org.thdl.lex.commands;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.thdl.lex.*;
import org.thdl.lex.component.*;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 1, 2003
 */
public class FindCommand extends LexCommand implements Command
{
//helper methods
	/**
	 *  Description of the Method
	 *
	 * @param  req                   Description of Parameter
	 * @param  component             Description of Parameter
	 * @return                       Description of the Returned Value
	 * @exception  CommandException  Description of Exception
	 * @since
	 */
	public String execute( HttpServletRequest req, ILexComponent component ) throws CommandException
	{
		try
		{
			String msg = null;
			String next = getNext();
			DisplayHelper displayHelper = getSessionManager().getDisplayHelper( req.getSession( true ) );
			LexQuery query = getSessionManager().getQuery( req.getSession( true ) );
			query.populate( req.getParameterMap() );

			if ( component instanceof ITerm )
			{
				ITerm term = (ITerm) component;

				query.setQueryComponent( term );
				LexComponentRepository.findTermsByTerm( query );
				Iterator iterator = query.getResults().keySet().iterator();
				if ( iterator.hasNext() )
				{
					getSessionManager().setQuery( req.getSession( true ), query );
					msg = "There are " + query.getResults().size() + " terms matching " + term.getTerm();
				}
				else
				{
					next = "menu.jsp";
					msg = "There were no terms matching " + term.getTerm();
				}
			}
			req.setAttribute( LexConstants.MESSAGE_REQ_ATTR, msg );
			query.setQueryComponent( null );
			return next;
		}
		catch ( LexRepositoryException e )
		{
			throw new CommandException( e );
		}
	}



//constructors
	/**
	 *  Constructor for the FindCommand object
	 *
	 * @param  next  Description of the Parameter
	 * @since
	 */
	public FindCommand( String next )
	{
		super( next );
	}
}

