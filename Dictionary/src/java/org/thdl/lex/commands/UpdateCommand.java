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
				ThdlUser user = getSessionMgr().getSessionUser( req.getSession( true ) );
				msg = "Successful Update";

				/*
				    component.setMetaId( new Integer( req.getParameter( "metaId" ) ) );
				    LexComponentRepository.loadByPk( component );
				    component.populate( req.getParameterMap() );
				    Date now = new Date( System.currentTimeMillis() );
				    component.getMeta().setModifiedOn( now );
				    component.getMeta().setModifiedBy( user.getId() );
				    LexComponentRepository.update( component );
				    req.setAttribute( LexConstants.MESSAGE_REQ_ATTR, msg );
				    req.setAttribute( "jumpToLocation", component.getLabel() );
				  */
				ITerm term = getSessionMgr().getQuery( req.getSession( true ) ).getEntry();
				LexComponentRepository.update( term );

				/*
				    term = new Term();
				    term.setMetaId( new Integer( 1 ) );
				    try
				    {
				    LexComponentRepository.loadTermByPk( term );
				    }
				    catch ( LexRepositoryException e )
				    {
				    throw new CommandException( "Command had trouble processing " + term, e );
				    }
				  */
				getSessionMgr().setDisplayMode( req.getSession( true ), "edit" );
			}
			else
			{
				msg = "Invalid Reload Attempted";
			}
			return getNext();
		}
		/*
		    catch ( LexComponentException e )
		    {
		    throw new CommandException( e );
		    }
		 */
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
		map.put( LexConstants.TERMLABEL_VALUE, "displayEntry.jsp" );
		map.put( LexConstants.SUBDEFINITIONLABEL_VALUE, "displayEntry.jsp" );
		map.put( LexConstants.TRANSLATIONLABEL_VALUE, "displayEntry.jsp" );
		// map.put( LexConstants.DEFINITIONLABEL_VALUE, "displayEntry.jsp" );
		// map.put( LexConstants.PASSAGELABEL_VALUE, "displayEntry.jsp" );
		return map;
	}

//constructors
	/**
	 *Constructor for the UpdateCommand object
	 */
	public UpdateCommand()
	{
		super();
		//setForwards( initForwards() );
	}


	/**
	 *Constructor for the UpdateCommand object
	 *
	 * @param  next  Description of the Parameter
	 */
	public UpdateCommand( String next )
	{
		this();
		setNext( next );
	}
}

