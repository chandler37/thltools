package org.thdl.lex.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.thdl.lex.*;
import org.thdl.lex.component.*;
import org.thdl.users.*;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 6, 2003
 */
public class UpdateCommand extends LexCommand implements Command
{

	private boolean termMode;


	/**
	 *  Sets the termMode attribute of the GetFormCommand object
	 *
	 * @param  termMode  The new termMode value
	 */
	public void setTermMode( boolean termMode )
	{
		this.termMode = termMode;
	}


	/**
	 *  Gets the termMode attribute of the GetFormCommand object
	 *
	 * @return    The termMode value
	 */
	public boolean isTermMode()
	{
		return termMode;
	}



//helper methods
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
		String msg = null;
		String next = getNext();
		DisplayHelper displayHelper = getSessionManager().getDisplayHelper( req.getSession( true ) );
		Global global = (Global) req.getServletContext().getAttribute( "global" );
		try
		{
			HttpSession ses = req.getSession( false );
			if ( null == ses )
			{
				throw new CommandException( "Could not update component, user's session has expired" );
			}

			ThdlUser user = getSessionManager().getSessionUser( ses );
			LexQuery query = getSessionManager().getQuery( ses );
			ITerm term = query.getEntry();

			if ( CommandToken.isValid( req ) && validate( user, component ) )
			{
				LexComponentRepository.update( term );

				LexLogger.debug( "Checking component state from updateCommand BEFORE component assignment" );
				LexLogger.debugComponent( component );
				if ( isTermMode() )
				{
					term.populate( req.getParameterMap() );
					term.getMeta().populate( req.getParameterMap() );
					component = term;
				}
				else
				{
					ILexComponent ilc = term.findChild( component.getMetaId() );
					ilc.populate( req.getParameterMap() );
					ilc.getMeta().populate( req.getParameterMap() );
					component = ilc;
				}
				LexLogger.debug( "Checking component state from updateCommand AFTER component assignment" );
				LexLogger.debugComponent( component );

				Date now = new Date( System.currentTimeMillis() );
				component.getMeta().setModifiedOn( now );
				component.getMeta().setModifiedBy( user.getId() );
				term.getMeta().setModifiedOn( now );
				term.getMeta().setModifiedBy( user.getId() );

				LexLogger.debugComponent( component );
				LexLogger.debugComponent( term );

				LexComponentRepository.update( term );
				global.setRequiresRefresh( true );
				msg = "Successful Update";
				getSessionManager().setDisplayMode( req.getSession( true ), "edit" );
			}
			else
			{
				msg = CommandToken.isValid( req ) ? "Unauthorized update attempted" : "Invalid reload attempted.";
			}
			return next;
		}
		catch ( LexComponentException e )
		{
			throw new CommandException( e );
		}
		catch ( LexRepositoryException e )
		{
			throw new CommandException( "Command had trouble processing " + component, e );
		}
		finally
		{
			req.setAttribute( LexConstants.MESSAGE_REQ_ATTR, msg );
		}
	}

//constructors
	/**
	 *Constructor for the GetFormCommand object
	 *
	 * @param  next      Description of the Parameter
	 * @param  termMode  Description of the Parameter
	 */
	public UpdateCommand( String next, Boolean termMode )
	{
		super( next );
		setTermMode( termMode.booleanValue() );
	}

}

