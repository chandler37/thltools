package org.thdl.lex.commands;

import org.thdl.lex.*;
import org.thdl.lex.component.*;

import org.thdl.users.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class InsertCommand extends LexCommand implements Command
{
//helper methods	
	public String execute(HttpServletRequest req, ILexComponent component) throws CommandException
	{
		/* try
		{	
			if ( CommandToken.isValid( req ) )
			{
				ThdlUser user = getSessionMgr().getSessionUser( req.getSession( true ) );
				Preferences preferences = getSessionMgr().getPreferences( req.getSession( true ) );
				if ( null == user )
					throw new CommandException("No ThdlUser available in this Session");
				if	( null == preferences )
					throw new CommandException("No Preferences available in this Session");
				Meta meta = new Meta( user, preferences );
				meta.scrapeRequest( req );
				meta.insert();
	
				setComponent( component );		
				getComponent().setMeta( meta );
				getComponent().scrapeRequest( req );
				getComponent().insert();
				getComponent().query();
				req.setAttribute( "jumpToLocation", getComponent().getLabel() );
				if (  null != req.getParameter( "analyticalNote" )  )
				{
					if (  ! LexConstants.LABEL_REQ_PARAM.equals( LexConstants.ANALYTICALNOTELABEL_VALUE ) 
							&& ! "".equals( req.getParameter( "analyticalNote" ) )  )
					{
						AnalyticalNote note = new AnalyticalNote();
						Meta mb = new Meta( meta );
						mb.insert();
						note.setMeta( mb );
						note.scrapeRequest( req );
						note.setParentMeta( getComponent().getMetaId() );
						note.insert();
					}
				}
	
				String msg="Successful new " +getComponent().getLabel() +" Entry.";
				req.setAttribute(LexConstants.MESSAGE_REQ_ATTR, msg);
				if ( getComponent() instanceof Term )
					getSessionMgr().setEntry( req.getSession( true ) , new TermEntry( getComponent() ) ); 
				else
					getSessionMgr().getEntry( req.getSession( true ) ).rebuild(); 
				if (null == getSessionMgr().getResults( req.getSession( true ) ) )
				{
					LexComponent[] results = {component};
					getSessionMgr().setResults( req.getSession( true ), results );
				}
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
		// map.put( LexConstants.TERMLABEL_VALUE, "displayEntry.jsp" );
		// map.put( LexConstants.DEFINITIONLABEL_VALUE, "displayEntry.jsp" );
		// map.put( LexConstants.PASSAGELABEL_VALUE, "displayEntry.jsp" );
		return map;
	}
	public InsertCommand( String next)
	{
		super( next );
		setForwards( initForwards() );
	}
}

