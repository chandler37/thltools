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
		String delay = config.getInitParameter( "globalDataRefreshDelay" );
		long refreshDelay = Long.parseLong( delay ) * 1000 * 60;
		String recent = config.getInitParameter( "recentItems" );
		int recentItems = Integer.parseInt( recent );
		Global global = new Global( recentItems, refreshDelay );
		config.getServletContext().setAttribute( LexConstants.GLOBAL_CONTEXT_ATTR, global );
		LexLogger.debugComponent( global );
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
		res.setContentType( "text/html; charset=UTF-8" );
		/*
		    LexLogger.debug( "Checking Request state at start of LexActionServlet.service()" );
		    LexLogger.logRequestState( req );
		    LexLogger.logSessionState( req );
		  */
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
		LexLogger.logContextState( getServletContext() );

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

		commands.put( "find", new FindCommand( "displayEntry.jsp" ) );

		commands.put( "getInsertForm", new GetInsertFormCommand( "displayForm.jsp?formMode=insert", Boolean.FALSE ) );
		commands.put( "getInsertTermForm", new GetInsertFormCommand( "displayForm.jsp?formMode=insert", Boolean.TRUE ) );
		commands.put( "getTranslationForm", new GetInsertFormCommand( "displayForm.jsp?formMode=insert", Boolean.FALSE ) );
		commands.put( "getAnnotationForm", new GetInsertFormCommand( "displayForm.jsp?formMode=insert", Boolean.FALSE ) );

		commands.put( "getUpdateForm", new GetUpdateFormCommand( "displayForm.jsp?formMode=update", Boolean.FALSE ) );
		commands.put( "getUpdateTermForm", new GetUpdateFormCommand( "displayForm.jsp?formMode=update", Boolean.TRUE ) );

		/*
		    commands.put( "getAnnotationForm", new AnnotateCommand( "displayForm.jsp?formMode=insert" ) );
		    commands.put( "addAnnotation", new AddAnnotationCommand( "displayEntry.jsp", Boolean.TRUE ) );
		  */
		commands.put( "insert", new InsertCommand( "displayEntry.jsp", Boolean.FALSE ) );
		commands.put( "addAnnotation", new InsertCommand( "displayEntry.jsp", Boolean.FALSE ) );
		commands.put( "insertTerm", new InsertCommand( "displayEntry.jsp", Boolean.TRUE ) );

		commands.put( "update", new UpdateCommand( "displayEntry.jsp", Boolean.FALSE ) );
		commands.put( "updateTerm", new UpdateCommand( "displayEntry.jsp", Boolean.TRUE ) );

		commands.put( "display", new DisplayCommand( "displayEntry.jsp" ) );
		commands.put( "displayFull", new DisplayCommand( "displayEntry.jsp" ) );
		commands.put( "editEntry", new DisplayCommand( "displayEntry.jsp" ) );

		commands.put( "remove", new RemoveCommand( "displayEntry.jsp" ) );

		commands.put( "setMetaPrefs", new PreferencesCommand( "menu.jsp" ) );
		commands.put( "setMetaDefaults", new PreferencesCommand( "menu.jsp" ) );

		commands.put( "abort", new AbortCommand( "menu.jsp" ) );

		setCommands( commands );
	}
}

