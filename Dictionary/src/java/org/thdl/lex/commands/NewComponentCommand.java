package org.thdl.lex.commands;

import org.thdl.lex.*;
import org.thdl.lex.component.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class NewComponentCommand extends LexCommand implements Command
{
//helper methods	
	public String execute(HttpServletRequest req, ILexComponent component) throws CommandException
	{
/* 		try
		{	

			setComponent( (LexComponent)component );
			getComponent().scrapeRequest( req );
			String msg="You have reached the form for creating a New " +getComponent().getLabel() +" Entry.";
			String forward = (String)getForwards().get( req.getParameter( LexConstants.LABEL_REQ_PARAM ) );
			setNext( forward );

			if ( getComponent() instanceof Term )
			{
				Term term = (Term)getComponent();
				LexComponent[] results = term.find();
				req.setAttribute( "term", term.getTerm() );
				if ( results.length > 0)
				{
					component=results[0];
					msg="There are already " + results.length + " entries for " + term.getTerm() +".";
					getSessionMgr().setEntry( req.getSession( true), new TermEntry( component ) );
				}
				else
				{
					//REQUEST SHOULD BE FORWARDED TO /lex/action?cmd=getInsertForm HERE
					results = new LexComponent[1];
					results[0] = component;
					msg="Click the 'New Entry' button to add " + term.getTerm() +" to the dictionary.";
					getSessionMgr().setEntry( req.getSession( true), null );
				}
				setNext( "displayEntry.jsp?mode=newTerm");
				getSessionMgr().setResults( req.getSession( true ), results );
			}
			req.setAttribute(LexConstants.MESSAGE_REQ_ATTR, msg);
	 */		return getNext();
	/* 	}
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
		map.put( LexConstants.DEFINITIONLABEL_VALUE, "displayEntry.jsp" );
		map.put( LexConstants.PASSAGELABEL_VALUE, "displayEntry.jsp" );
		return map;
	}

//constructors
	public NewComponentCommand()
	{
		super();
		setForwards( initForwards() );
	}
}

