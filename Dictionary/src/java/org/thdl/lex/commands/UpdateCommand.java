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

	private boolean insertMode;
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


	/**
	 *  Sets the insertMode attribute of the GetFormCommand object
	 *
	 * @param  insertMode  The new insertMode value
	 */
	public void setInsertMode( boolean insertMode )
	{
		this.insertMode = insertMode;
	}


	/**
	 *  Gets the insertMode attribute of the GetFormCommand object
	 *
	 * @return    The insertMode value
	 */
	public boolean isInsertMode()
	{
		return insertMode;
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
		try
		{
			if ( CommandToken.isValid( req ) )
			{
				HttpSession ses = req.getSession( false );
				if ( null == ses )
				{
					throw new CommandException( "Could not update component, uses session has expired" );
				}
				ThdlUser user = getSessionManager().getSessionUser( ses );
				LexQuery query = getSessionManager().getQuery( ses );

				Map params = req.getParameterMap();
				query.getUpdateComponent().populate( params );
				query.getUpdateComponent().getMeta().populate( params );
				Date now = new Date( System.currentTimeMillis() );
				query.getUpdateComponent().getMeta().setModifiedOn( now );
				query.getUpdateComponent().getMeta().setModifiedBy( user.getId() );
				if ( isInsertMode() )
				{
					query.getUpdateComponent().getMeta().setCreatedOn( now );
					query.getUpdateComponent().getMeta().setCreatedBy( user.getId() );
				}
				ITerm term = query.getEntry();
				LexComponentRepository.saveOrUpdate( term );
				msg = "Successful Update";
				getSessionManager().setDisplayMode( req.getSession( true ), "edit" );
			}
			else
			{
				msg = "Invalid Reload Attempted.";
			}
			return getNext();
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


	/**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
	public HashMap initForwards()
	{
		HashMap map = new HashMap();
		// map.put( LexConstants.TERMLABEL_VALUE, "displayEntry.jsp" );
		// map.put( LexConstants.SUBDEFINITIONLABEL_VALUE, "displayEntry.jsp" );
		// map.put( LexConstants.TRANSLATIONLABEL_VALUE, "displayEntry.jsp" );
		// map.put( LexConstants.DEFINITIONLABEL_VALUE, "displayEntry.jsp" );
		// map.put( LexConstants.PASSAGELABEL_VALUE, "displayEntry.jsp" );
		return map;
	}

//constructors
	/**
	 *Constructor for the GetFormCommand object
	 *
	 * @param  next        Description of the Parameter
	 * @param  insertMode  Description of the Parameter
	 * @param  termMode    Description of the Parameter
	 */
	public UpdateCommand( String next, Boolean insertMode, Boolean termMode )
	{
		super( next );
		setInsertMode( insertMode.booleanValue() );
		setTermMode( termMode.booleanValue() );
	}


	/**
	 *Constructor for the GetFormCommand object
	 *
	 * @param  next        Description of the Parameter
	 * @param  insertMode  Description of the Parameter
	 */
	public UpdateCommand( String next, Boolean insertMode )
	{
		this( next, insertMode, Boolean.FALSE );
	}
}

