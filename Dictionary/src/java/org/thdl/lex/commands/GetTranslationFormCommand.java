package org.thdl.lex.commands;

import org.thdl.lex.*;
import org.thdl.lex.component.*;

import org.thdl.users.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class GetTranslationFormCommand extends LexCommand implements Command
{
//helper methods	
	public String execute(HttpServletRequest req, ILexComponent component) throws CommandException
	{
	/* 	try
		{	
			ThdlUser user = getSessionMgr().getSessionUser( req.getSession( true ) );
			Preferences preferences = getSessionMgr().getPreferences( req.getSession( true ) );
			Meta meta = new Meta( user, preferences );
			meta.scrapeRequest( req );
			component.setTranslationOf( Integer.parseInt( req.getParameter("id") ) );
			meta.insert();
			setComponent( component );
			getComponent().query( Integer.parseInt( req.getParameter("id") ) );
			req.setAttribute( LexConstants.ORIGINALBEAN_REQ_ATTR , getComponent() );
			getComponent().setMeta( meta );
			//getComponent().insert();
			String msg="You have reached the Translation Form";
			req.setAttribute(LexConstants.MESSAGE_REQ_ATTR, msg);
			getSessionMgr().setDisplayMode( req.getSession( true ) , "addEditForm" );
 */			return getNext();
/* 		}
		catch (LexComponentException e)
		{
			throw new CommandException("Lex Action  Exception: " + e.getMessage());
		}
 */	}
	public HashMap initForwards()
	{
		return null;
	}
//constructors
	public GetTranslationFormCommand()
	{
		super();
	}
	public GetTranslationFormCommand( String next )
	{
		super();
		setNext( next );
	}
}

