package org.thdl.lex.commands;

import org.thdl.lex.*;
import org.thdl.lex.component.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class GetInsertFormCommand extends LexCommand implements Command
{
//contract methods	
	public String execute(HttpServletRequest req, ILexComponent component) throws CommandException
	{
	/* 	try
		{	
			setComponent( (LexComponent)component );
			getComponent().scrapeRequest( req );
			String msg="You have reached the New Entry form.";
			req.setAttribute(LexConstants.MESSAGE_REQ_ATTR, msg);
			if ( req.getParameter( LexConstants.COMMAND_REQ_PARAM ).equals( "annotate" ) )
			{
				setComponent( (LexComponent)component );
				getComponent().scrapeRequest( req );
				getComponent().query();

				AnalyticalNote note = new AnalyticalNote();
				note.setParentMeta( getComponent().getMetaId() );
				req.setAttribute( LexConstants.COMPONENT_REQ_ATTR, component );
			}
			getSessionMgr().setDisplayMode( req.getSession( true ), "addNewComponent" );
 */			return getNext();
/* 		}
		catch (LexComponentException e)
		{
			throw new CommandException("Lex Action  Exception: " + e.getMessage());
		}
 */	}
//helper methods			
 	public HashMap initForwards()
	{
		HashMap map = new HashMap();
		map.put( LexConstants.TERMLABEL_VALUE, "displayEntry.jsp?formMode=insert" );
		map.put( LexConstants.SUBDEFINITIONLABEL_VALUE, "displayEntry.jsp?formMode=insert" );
		map.put( LexConstants.TRANSLATIONLABEL_VALUE, "displayEntry.jsp?formMode=insert" );
		// map.put( LexConstants.DEFINITIONLABEL_VALUE, "displayEntry.jsp" );
		// map.put( LexConstants.PASSAGELABEL_VALUE, "displayEntry.jsp" );
		return map;
	}

//constructors
	public GetInsertFormCommand()
	{
		super();
		setForwards( initForwards() );
	}
	public GetInsertFormCommand( String next )
	{
		super();
		setNext( next );
	}
}

