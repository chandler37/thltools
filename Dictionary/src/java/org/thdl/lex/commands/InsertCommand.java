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
public class InsertCommand extends LexCommand implements Command
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
		Visit visit = UserSessionManager.getInstance().getVisit( req.getSession( true ) );
		DisplayHelper displayHelper = visit.getHelper();
		try
		{
			HttpSession ses = req.getSession( false );
			if ( null == ses )
			{
				throw new CommandException( "Could not update component, user's session has expired" );
			}

			LexQuery query = visit.getQuery();
			ITerm term = query.getEntry();
			LexUser user = (LexUser) visit.getUser();
			Preferences prefs = visit.getPreferences();

			if ( CommandToken.isValid( req ) && validate( user, component ) )
			{
				if ( !isTermMode() )
				{
					LexComponentRepository.update( term );
				}

				if ( isTermMode() )
				{
					term = (ITerm) component;
					//term.populate( req.getParameterMap() );
				}
				else if ( component instanceof AnalyticalNote )
				{
					ILexComponent parent = term.findParent( component.getParentId() );
					parent.getAnalyticalNotes().add( component );
				}
				else
				{
					term.addChild( component );
				}

				Meta meta = new Meta( user, prefs );
				meta.populate( req.getParameterMap() );
				component.setMeta( meta );

				Date now = new Date( System.currentTimeMillis() );
				component.getMeta().setCreatedOn( now );
				component.getMeta().setModifiedOn( now );
				component.getMeta().setCreatedBy( user.getId() );
				component.getMeta().setModifiedBy( user.getId() );

				if ( !isTermMode() )
				{
					term.getMeta().setModifiedOn( now );
					term.getMeta().setModifiedBy( user.getId() );
				}

				if ( !( component instanceof AnalyticalNote ) && null != req.getParameter( "analyticalNote" ) && req.getParameter( "analyticalNote" ).length() > 0 )
				{
					AnalyticalNote note = new AnalyticalNote();
					note.setAnalyticalNote( req.getParameter( "analyticalNote" ) );
					note.setPrecedence( new Integer( 0 ) );
					component.setAnalyticalNotes( new LinkedList() );
					component.getAnalyticalNotes().add( note );
					meta = new Meta( user, prefs );
					meta.populate( req.getParameterMap() );
					note.setMeta( meta );
					LexLogger.debug( "Adding analytical note to " + component.getLabel() );
				}

				LexLogger.debugComponent( component );
				LexLogger.debugComponent( term );

				LexComponentRepository.save( term );
				msg = "Successful Update";
				visit.setDisplayMode( "edit" );
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
	public InsertCommand( String next, Boolean termMode )
	{
		super( next );
		setTermMode( termMode.booleanValue() );
	}
}

