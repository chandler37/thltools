package org.thdl.lex.commands;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.*;

import org.thdl.lex.*;
import org.thdl.lex.component.*;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 3, 2003
 */
public class DisplayCommand extends LexCommand implements Command
{
//helper methods
	/**
	 *  Sets the displayMode attribute of the DisplayCommand object
	 *
	 * @param  req  The new displayMode value
	 * @since
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
	 * @param  req                   Description of Parameter
	 * @param  component             Description of Parameter
	 * @return                       Description of the Returned Value
	 * @exception  CommandException  Description of Exception
	 * @since
	 */
	public String execute( HttpServletRequest req, ILexComponent component ) throws CommandException
	{
		String msg = null;
		String next = getNext();
		Visit visit = UserSessionManager.getInstance().getVisit( req.getSession( true ) );
		DisplayHelper displayHelper = visit.getHelper();
		try
		{
			LexQuery query = visit.getQuery();
			if ( null != component )
			{
				component.populate( req.getParameterMap() );
				query.setQueryComponent( component );
				LexComponentRepository.loadTermByPk( query );
			}
			else
			{
				LexComponentRepository.update( query.getEntry() );
			}

			displayHelper.populate( req.getParameterMap() );
			req.setAttribute( LexConstants.MESSAGE_REQ_ATTR, msg );

			return next;
		}
		catch ( LexComponentException e )
		{
			throw new CommandException( e );
		}
		catch ( LexRepositoryException e )
		{
			throw new CommandException( e );
		}
		catch ( Exception e )
		{
			throw new CommandException( e );
		}
	}


//constructors
	/**
	 *  Constructor for the DisplayCommand object
	 *
	 * @param  next  Description of the Parameter
	 * @since
	 */
	public DisplayCommand( String next )
	{
		super( next );
	}
}

