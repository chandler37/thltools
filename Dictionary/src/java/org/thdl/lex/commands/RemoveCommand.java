package org.thdl.lex.commands;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.thdl.lex.*;
import org.thdl.lex.component.*;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 14, 2003
 */
public class RemoveCommand extends LexCommand implements Command
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
		Visit visit = UserSessionManager.getInstance().getVisit( req.getSession( true ) );

		DisplayHelper displayHelper = visit.getHelper();

		/*
		    try
		    {
		    setComponent( (LexComponent)component );
		    getComponent().query( Integer.parseInt( req.getParameter("id") ) );
		    String msg=null;
		    int successCode = getComponent().remove();
		    msg=null;
		    String label = req.getParameter( LexConstants.LABEL_REQ_PARAM );
		    if (successCode > 0)
		    { msg = "The " + label +" was successfully removed."; }
		    else
		    { msg = "Failure: The " + getComponent().getLabel() +" was not removed."; }
		    req.setAttribute(LexConstants.MESSAGE_REQ_ATTR, msg);
		  */
		return getNext();
		/*
		    }
		    catch (LexComponentException e)
		    {
		    throw new CommandException("Lex Action  Exception: " + e.getMessage());
		    }
		  */
	}



//constructors
	/**
	 *Constructor for the RemoveCommand object
	 *
	 * @param  next  Description of the Parameter
	 */
	public RemoveCommand( String next )
	{
		super( next );
	}
}

