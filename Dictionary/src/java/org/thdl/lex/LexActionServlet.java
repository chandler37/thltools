package org.thdl.lex;

import java.io.IOException;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.*;
import org.thdl.lex.commands.*;

import org.thdl.lex.component.*;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 1, 2003
 */
public class LexActionServlet extends HttpServlet
{
//attributes
	private HashMap commands;
	private String cmd;


//accessors

	/**
	 *  Sets the commands attribute of the LexActionServlet object
	 *
	 * @param  commands  The new commands value
	 * @since
	 */
	public void setCommands( HashMap commands )
	{
		this.commands = commands;
	}


	/**
	 *  Sets the cmd attribute of the LexActionServlet object
	 *
	 * @param  cmd  The new cmd value
	 * @since
	 */
	public void setCmd( String cmd )
	{
		this.cmd = cmd;
	}


	/**
	 *  Gets the commands attribute of the LexActionServlet object
	 *
	 * @return    The commands value
	 * @since
	 */
	public HashMap getCommands()
	{
		return commands;
	}


	/**
	 *  Gets the cmd attribute of the LexActionServlet object
	 *
	 * @return    The cmd value
	 * @since
	 */
	public String getCmd()
	{
		return cmd;
	}


//helper methods

	/**
	 *  Description of the Method
	 *
	 * @param  config                Description of Parameter
	 * @exception  ServletException  Description of Exception
	 * @since
	 */
	public void init( ServletConfig config ) throws ServletException
	{
		super.init( config );
		initCommands();
		config.getServletContext().setAttribute( "flatData", new LexFlatDataRepository() );
	}


	/**
	 *  Description of the Method
	 *
	 * @param  req                   Description of Parameter
	 * @param  res                   Description of Parameter
	 * @exception  ServletException  Description of Exception
	 * @exception  IOException       Description of Exception
	 * @since
	 */
	public void service( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException
	{
		LexLogger.debug( "Checking Request state at start of LexActionServlet.service()" );
		LexLogger.logRequestState( req );
		LexLogger.logSessionState( req );

		String next;
		try
		{
			setCmd( req.getParameter( LexConstants.COMMAND_REQ_PARAM ) );
			Command command = lookupCommand( getCmd() );
			LexComponent component = (LexComponent) req.getAttribute( LexConstants.COMPONENT_REQ_ATTR );
			next = command.execute( req, component );
			CommandToken.set( req );
		}
		catch ( CommandException e )
		{
			req.setAttribute( "javax.servlet.jsp.jspException", e );
			next = LexConstants.ERROR_PAGE;
			try
			{
				LexComponentRepository.cleanup();
			}
			catch ( LexRepositoryException lre )
			{
				Exception ex = new Exception( "LexComponentRepository couldn't clean up after Exception because: " + lre.getMessage(), e );
				req.setAttribute( "javax.servlet.jsp.jspException", ex );
			}
		}
		catch ( Exception e )
		{
			req.setAttribute( "javax.servlet.jsp.jspException", e );
			next = LexConstants.ERROR_PAGE;
			try
			{
				LexComponentRepository.cleanup();
			}
			catch ( LexRepositoryException lre )
			{
				Exception ex = new Exception( "LexComponentRepository couldn't clean up after Exception because: " + lre.getMessage(), e );
				req.setAttribute( "javax.servlet.jsp.jspException", ex );
			}
		}
		RequestDispatcher rd;
		rd = getServletContext().getRequestDispatcher( LexConstants.JSP_DIR + next );
		rd.forward( req, res );
		LexLogger.debug( "Checking Request state at end of LexActionServlet.service()" );
		LexLogger.logRequestState( req );
		LexLogger.logSessionState( req );

	}


	/**
	 *  Description of the Method
	 *
	 * @param  cmdKey                Description of Parameter
	 * @return                       Description of the Returned Value
	 * @exception  CommandException  Description of Exception
	 * @since
	 */
	private Command lookupCommand( String cmdKey ) throws CommandException
	{
		if ( cmdKey == null )
		{
			cmdKey = "menu";
		}
		if ( getCommands().containsKey( cmdKey ) )
		{
			return (Command) getCommands().get( cmdKey );
		}
		else
		{
			throw new CommandException( "Invalid Command Identifier: '" + getCmd() + "'" );
		}
	}



	/**
	 *  Description of the Method
	 *
	 * @since
	 */
	private void initCommands()
	{
		HashMap commands = new HashMap();

		commands.put( "menu", new NullCommand( "menu.jsp" ) );
		commands.put( "logout", new NullCommand( "logout.jsp" ) );
		commands.put( "getMetaPrefsForm", new NullCommand( "metaPrefsForm.jsp" ) );
		commands.put( "getMetaDefaultsForm", new NullCommand( "metaDefaultsForm.jsp" ) );
		commands.put( "find", new FindCommand() );
		commands.put( "getInsertForm", new GetFormCommand( "displayForm.jsp?formMode=insert", Boolean.TRUE ) );
		commands.put( "getUpdateForm", new GetFormCommand( "displayForm.jsp?formMode=update", Boolean.FALSE ) );
		commands.put( "getInsertTermForm", new GetFormCommand( "displayForm.jsp?formMode=insert", Boolean.TRUE, Boolean.TRUE ) );
		commands.put( "getUpdateTermForm", new GetFormCommand( "displayForm.jsp?formMode=update", Boolean.FALSE, Boolean.TRUE ) );
		commands.put( "getTranslationForm", new GetFormCommand( "displayForm.jsp?formMode=insert", Boolean.TRUE ) );
		commands.put( "annotate", new GetFormCommand( "displayForm.jsp?formMode=insert", Boolean.FALSE ) );
		commands.put( "insert", new UpdateCommand( "displayEntry.jsp", Boolean.TRUE ) );
		commands.put( "update", new UpdateCommand( "displayEntry.jsp", Boolean.FALSE ) );
		commands.put( "display", new DisplayCommand() );
		commands.put( "displayFull", new DisplayCommand() );
		commands.put( "editEntry", new DisplayCommand() );
		commands.put( "remove", new RemoveCommand() );
		commands.put( "setMetaPrefs", new PreferencesCommand( "menu.jsp" ) );
		commands.put( "setMetaDefaults", new PreferencesCommand( "menu.jsp" ) );
		commands.put( "abort", new AbortCommand( "menu.jsp" ) );
		commands.put( "testing", new TestingCommand( "displayEntry.jsp" ) );

		setCommands( commands );
	}
}

