package org.thdl.lex.commands;

import org.thdl.lex.*;
import org.thdl.lex.component.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class RemoveCommand extends LexCommand implements Command
{
//helper methods	
	public String execute(HttpServletRequest req, ILexComponent component) throws CommandException
	{
	/* 	try
		{	
			setComponent( (LexComponent)component );
			getComponent().query( Integer.parseInt( req.getParameter("id") ) );
			String msg=null;
			String forward = (String)getForwards().get( req.getParameter( LexConstants.LABEL_REQ_PARAM ) );
			setNext( forward );
			int successCode = getComponent().remove();
			msg=null;
			String label = req.getParameter( LexConstants.LABEL_REQ_PARAM );
			if (successCode > 0)
			{ msg = "The " + label +" was successfully removed."; }
			else
			{ msg = "Failure: The " + getComponent().getLabel() +" was not removed."; }

			req.setAttribute(LexConstants.MESSAGE_REQ_ATTR, msg);
 */			return getNext();
	/* 	}
		catch (LexComponentException e)
		{
			throw new CommandException("Lex Action  Exception: " + e.getMessage());
		}
 */	}
 	public HashMap initForwards()
	{
		HashMap map = new HashMap();
		map.put( LexConstants.TERMLABEL_VALUE, "menu.jsp" );
		// map.put( LexConstants.DEFINITIONLABEL_VALUE, "displayEntry.jsp" );
		// map.put( LexConstants.PASSAGELABEL_VALUE, "displayEntry.jsp" );
		return map;
	}

//constructors
	public RemoveCommand()
	{
		super();
		setForwards( initForwards() );
	}
}

