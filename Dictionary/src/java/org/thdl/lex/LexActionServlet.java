package org.thdl.lex;

import org.thdl.lex.component.*;
import org.thdl.lex.commands.*;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;


/**
 *  Description of the Class
 *
 *@author     travis
 *@created    October 1, 2003
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
	 *@param  commands  The new commands value
	 *@since
	 */
	public void setCommands( HashMap commands )
	{
		this.commands = commands;
	}


	/**
	 *  Sets the cmd attribute of the LexActionServlet object
	 *
	 *@param  cmd  The new cmd value
	 *@since
	 */
	public void setCmd( String cmd )
	{
		this.cmd = cmd;
	}


	/**
	 *  Gets the commands attribute of the LexActionServlet object
	 *
	 *@return    The commands value
	 *@since
	 */
	public HashMap getCommands()
	{
		return commands;
	}


	/**
	 *  Gets the cmd attribute of the LexActionServlet object
	 *
	 *@return    The cmd value
	 *@since
	 */
	public String getCmd()
	{
		return cmd;
	}


//helper methods

	/**
	 *  Description of the Method
	 *
	 *@param  config                Description of Parameter
	 *@exception  ServletException  Description of Exception
	 *@since
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
	 *@param  req                   Description of Parameter
	 *@param  res                   Description of Parameter
	 *@exception  ServletException  Description of Exception
	 *@exception  IOException       Description of Exception
	 *@since
	 */
	public void service( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException
	{
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
	}


	/**
	 *  Description of the Method
	 *
	 *@param  cmdKey                Description of Parameter
	 *@return                       Description of the Returned Value
	 *@exception  CommandException  Description of Exception
	 *@since
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
	 *@since
	 */
	private void initCommands()
	{
		HashMap commands = new HashMap();
		commands.put( "menu", new NullCommand( "menu.jsp" ) );
		commands.put( "abort", new AbortCommand( "menu.jsp" ) );
		commands.put( "testing", new TestingCommand( "displayEntry.jsp" ) );
		// commands.put( "login", new NullCommand( "login.jsp" ) );
		commands.put( "logout", new NullCommand( "logout.jsp" ) );
		commands.put( "new", new NewComponentCommand() );
		commands.put( "find", new FindCommand() );
		commands.put( "getInsertForm", new GetInsertFormCommand( "displayForm.jsp?formMode=insert" ) );
		commands.put( "getUpdateForm", new GetUpdateFormCommand( "displayForm.jsp?formMode=update" ) );
		commands.put( "getTranslationForm", new GetTranslationFormCommand( "displayForm.jsp?formMode=insert" ) );
		commands.put( "annotate", new GetInsertFormCommand( "displayForm.jsp?formMode=insert" ) );
		commands.put( "insert", new InsertCommand( "displayEntry.jsp" ) );
		commands.put( "update", new UpdateCommand( "displayEntry.jsp" ) );
		commands.put( "display", new DisplayCommand() );
		commands.put( "displayFull", new DisplayCommand() );
		commands.put( "editEntry", new DisplayCommand() );
		commands.put( "remove", new RemoveCommand() );
		commands.put( "getMetaPrefsForm", new NullCommand( "metaPrefsForm.jsp" ) );
		commands.put( "getMetaDefaultsForm", new NullCommand( "metaDefaultsForm.jsp" ) );
		commands.put( "setMetaPrefs", new PreferencesCommand( "menu.jsp" ) );
		commands.put( "setMetaDefaults", new PreferencesCommand( "menu.jsp" ) );
		setCommands( commands );
	}
}

