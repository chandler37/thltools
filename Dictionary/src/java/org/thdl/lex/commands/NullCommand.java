package org.thdl.lex.commands;

import org.thdl.lex.*;
import org.thdl.lex.component.*;

import javax.servlet.http.HttpServletRequest;

public class NullCommand extends LexCommand implements Command
{
	public String execute(HttpServletRequest req, ILexComponent component) throws CommandException
	{
		if( null == req.getParameter( LexConstants.COMMAND_REQ_PARAM ) )
			req.setAttribute( LexConstants.MESSAGE_REQ_ATTR, "Start from here." );
		if( "login" == req.getParameter( LexConstants.COMMAND_REQ_PARAM ) 
			&& null != getSessionManager().getSessionUser( req.getSession(true) ) )
			setNext("menu.jsp");
		return getNext();
	}
	public NullCommand(String next)
	{
		super(next);
	}
}
