package org.thdl.lex.commands;

import org.thdl.lex.*;
import org.thdl.lex.component.*;

import org.thdl.users.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class UpdateCommand extends LexCommand implements Command
{
//helper methods	
	public String execute(HttpServletRequest req, ILexComponent component) throws CommandException
	{
	/* 	try
		{	
			if ( CommandToken.isValid( req ) )
			{
				Preferences preferences=getSessionMgr().getPreferences( req.getSession(true) );
				ThdlUser user = getSessionMgr().getSessionUser( req.getSession(true) );
				String msg="Successful Update";
				
				setComponent( component );
				getComponent().query( Integer.parseInt( req.getParameter("id") ) );
				getComponent().scrapeRequest( req );
				getComponent().update( user, preferences );
	
				req.setAttribute(LexConstants.MESSAGE_REQ_ATTR, msg);
				req.setAttribute( "jumpToLocation", getComponent().getLabel() );
				getSessionMgr().getEntry( req.getSession( true ) ).rebuild(); 
				getSessionMgr().setDisplayMode( req.getSession( true ), "edit" );
			}
			else
			{
				String msg="Invalid Reload Attempted";
				req.setAttribute(LexConstants.MESSAGE_REQ_ATTR, msg);
			}
 */			return getNext();
/* 		}
		catch (LexComponentException e)
		{
			throw new CommandException("Lex Action  Exception: " + e.getMessage());
		}
		catch (LexEntryException e)
		{
			throw new CommandException("Lex Entry  Exception: " + e.getMessage());
		}
 */	}
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
	public UpdateCommand()
	{
		super();
		//setForwards( initForwards() );
	}
	public UpdateCommand( String next )
	{
		this();
		setNext( next );
	}
}

