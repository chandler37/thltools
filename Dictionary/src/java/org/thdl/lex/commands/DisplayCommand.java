package org.thdl.lex.commands;

import org.thdl.lex.*;
import org.thdl.lex.component.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;


/**
 *  Description of the Class
 *
 *@author     travis
 *@created    October 3, 2003
 */
public class DisplayCommand extends LexCommand implements Command
{
//helper methods
	/**
	 *  Sets the displayMode attribute of the DisplayCommand object
	 *
	 *@param  req  The new displayMode value
	 *@since
	 */
	public void setDisplayMode( HttpServletRequest req )
	{
		String cmd = req.getParameter( LexConstants.COMMAND_REQ_PARAM );
		if ( cmd.equals( "display" ) )
		{
			req.getSession( true ).setAttribute( LexConstants.DISPLAYMODE_SESS_ATTR, "brief" );
		}
		else if ( cmd.equals( "displayFull" ) )
		{
			req.getSession( true ).setAttribute( LexConstants.DISPLAYMODE_SESS_ATTR, "full" );
		}
		else if ( cmd.equals( "editEntry" ) )
		{
			req.getSession( true ).setAttribute( LexConstants.DISPLAYMODE_SESS_ATTR, "edit" );
		}
	}


//contract methods

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
			component.populate( req.getParameterMap() );

			LexQuery query = getSessionManager().getQuery( req.getSession( true ) );
			query.setQueryComponent( component );
			LexComponentRepository.loadTermByPk( query );

			String msg = null;
			String forward = (String) getForwards().get( req.getParameter( LexConstants.LABEL_REQ_PARAM ) );
			setNext( forward );

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
		map.put( LexConstants.ENCYCLOPEDIA_ARTICLE_LABEL_VALUE, "displayEntry.jsp" );
		return map;
	}


//constructors
	/**
	 *  Constructor for the DisplayCommand object
	 *
	 *@since
	 */
	public DisplayCommand()
	{
		super();
		setForwards( initForwards() );
	}
}

