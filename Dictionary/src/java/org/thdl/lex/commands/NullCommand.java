package org.thdl.lex.commands;

import javax.servlet.http.HttpServletRequest;

import org.thdl.lex.*;
import org.thdl.lex.component.*;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 21, 2003
 */
public class NullCommand extends LexCommand implements Command
{
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
		/*
		    Visit visit = UserSessionManager.getInstance().getVisit( req.getSession( true ) );
		    if( null == req.getParameter( LexConstants.COMMAND_REQ_PARAM ) )
		    req.setAttribute( LexConstants.MESSAGE_REQ_ATTR, "Start from here." );
		    if( "login" == req.getParameter( LexConstants.COMMAND_REQ_PARAM )
		    && null != visit.getUser())
		    setNext("menu.jsp");
		 */
		return getNext();
	}


	/**
	 *Constructor for the NullCommand object
	 *
	 * @param  next  Description of the Parameter
	 */
	public NullCommand( String next )
	{
		super( next );
	}
}

