package org.thdl.lex.commands;

import org.thdl.lex.*;
import org.thdl.lex.component.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


/**
 *  Description of the Class
 *
 *@author     travis
 *@created    October 1, 2003
 */
public class FindCommand extends LexCommand implements Command
{
//helper methods
	/**
	 *  Description of the Method
	 *
	 *@param  req                   Description of Parameter
	 *@param  component             Description of Parameter
	 *@return                       Description of the Returned Value
	 *@exception  CommandException  Description of Exception
	 *@since
	 */
	public String execute( HttpServletRequest req, ILexComponent component ) throws CommandException
	{
		try
		{
			String msg = null;
			String forward = (String) getForwards().get( req.getParameter( LexConstants.LABEL_REQ_PARAM ) );

			component.populate( req.getParameterMap() );
			setNext( forward );

			if ( component instanceof ITerm )
			{
				ITerm term = (ITerm) component;

				LexQuery query = getSessionManager().getQuery( req.getSession( true ) );

				query.setQueryComponent( term );
				LexComponentRepository.findTermsByTerm( query );
				Iterator iterator = query.getResults().keySet().iterator();
				if ( iterator.hasNext() )
				{
					setNext( "displayEntry.jsp" );
					getSessionManager().setQuery( req.getSession( true ), query );
					msg = "There are " + query.getResults().size() + " terms matching " + term.getTerm();
				}
				else
				{
					setNext( "menu.jsp" );
					msg = "There were no terms matching " + term.getTerm();
				}
			}
			req.setAttribute( LexConstants.MESSAGE_REQ_ATTR, msg );
			return getNext();
		}
		catch ( LexComponentException e )
		{
			throw new CommandException( e );
		}
		catch ( LexRepositoryException e )
		{
			throw new CommandException( e );
		}
	}


	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Returned Value
	 *@since
	 */
	public HashMap initForwards()
	{
		HashMap map = new HashMap();
		map.put( LexConstants.TERMLABEL_VALUE, "displayEntry.jsp" );
		// map.put( LexConstants.DEFINITIONLABEL_VALUE, "displayEntry.jsp" );
		// map.put( LexConstants.PASSAGELABEL_VALUE, "displayEntry.jsp" );
		return map;
	}


//constructors
	/**
	 *  Constructor for the FindCommand object
	 *
	 *@since
	 */
	public FindCommand()
	{
		super();
		setForwards( initForwards() );
	}
}

