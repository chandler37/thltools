package org.thdl.lex.commands;

import org.thdl.lex.*;
import org.thdl.lex.component.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class GetUpdateFormCommand extends LexCommand implements Command
{
//helper methods	
	public String execute(HttpServletRequest req, ILexComponent component) throws CommandException
	{
		/* try
		{	
			String msg = null;
			setComponent( (LexComponent)component );
			getComponent().scrapeRequest( req );
			getComponent().query();
			if ( getComponent().getTranslationOf() > 0 )
			{
				try
				{
					LexComponent lab = (LexComponent) getComponent().getClass().newInstance();
					lab.query( getComponent().getTranslationOf() );
					req.setAttribute( LexConstants.ORIGINALBEAN_REQ_ATTR , lab );
				}
				catch (InstantiationException ie)
				{
					throw new LexComponentException("InstantiationException Says: " +ie.getMessage());
				}
				catch (IllegalAccessException iae)
				{
					throw new LexComponentException("IllegalAccessException Says: " +iae.getMessage());
				}
			}
			Meta meta = getComponent().getMeta();
			if ( getSessionMgr().getSessionUser( req.getSession( true ) ).getId() == meta.getCreatedBy() )
			{
				msg="You have reached the Update Form";
				getSessionMgr().setDisplayMode( req.getSession( true ) , "addEditForm" );
				setNext( "displayForm.jsp?formMode=update" );
			}
			else
			{
				msg="A dictionary component can only be edited by the person who created it";
				setNext( "displayEntry.jsp" );
			}
			req.setAttribute(LexConstants.MESSAGE_REQ_ATTR, msg);
 */			return getNext();
/* 		}
		catch (LexComponentException e)
		{
			throw new CommandException("Lex Action  Exception: " + e.getMessage());
		}
 */
	}
 	public HashMap initForwards()
	{
		HashMap map = new HashMap();
		//map.put( LexConstants.TERMLABEL_VALUE, "displayEntry.jsp?formMode=update" );
		//map.put( LexConstants.SUBDEFINITIONLABEL_VALUE, "displayEntry.jsp?formMode=update" );
		//map.put( LexConstants.TRANSLATIONLABEL_VALUE, "displayEntry.jsp?formMode=update" );
		// map.put( LexConstants.DEFINITIONLABEL_VALUE, "displayEntry.jsp" );
		// map.put( LexConstants.PASSAGELABEL_VALUE, "displayEntry.jsp" );
		return map;
	}

//constructors
	public GetUpdateFormCommand()
	{
		super();
		//setForwards( initForwards() );
	}
	public GetUpdateFormCommand( String next )
	{
		super();
		setNext( next );
	}
}

