package org.thdl.lex;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thdl.lex.commands.AbortCommand;
import org.thdl.lex.commands.Command;
import org.thdl.lex.commands.CommandException;
import org.thdl.lex.commands.CommandToken;
import org.thdl.lex.commands.DisplayCommand;
import org.thdl.lex.commands.FindCommand;
import org.thdl.lex.commands.GetInsertFormCommand;
import org.thdl.lex.commands.GetRemoveFormCommand;
import org.thdl.lex.commands.GetUpdateFormCommand;
import org.thdl.lex.commands.InsertCommand;
import org.thdl.lex.commands.NullCommand;
import org.thdl.lex.commands.PreferencesCommand;
import org.thdl.lex.commands.RemoveCommand;
import org.thdl.lex.commands.UpdateCommand;
import org.thdl.lex.component.LexComponent;

/**
 * Description of the Class
 * 
 * @author travis
 * @created October 1, 2003
 */
public class LexActionServlet extends HttpServlet {
	//attributes
	private HashMap commands;

	//accessors

	/**
	 * Sets the commands attribute of the LexActionServlet object
	 * 
	 * @param commands
	 *            The new commands value
	 * @since
	 */
	public void setCommands(HashMap commands) {
		this.commands = commands;
	}

	/**
	 * Gets the commands attribute of the LexActionServlet object
	 * 
	 * @return The commands value
	 * @since
	 */
	public HashMap getCommands() {
		return commands;
	}

	//helper methods

	/**
	 * Description of the Method
	 * 
	 * @param config
	 *            Description of Parameter
	 * @exception ServletException
	 *                Description of Exception
	 * @since
	 */
	public void init(ServletConfig config) throws ServletException {
		try {
			super.init(config);
			initCommands();

			config.getServletContext().setAttribute("flatData",
					new LexFlatDataRepository());

			/*
			 * LexSourceRepository sources = LexSourceRepository.getInstance();
			 * sources.setOaiServer(
			 * config.getServletContext().getInitParameter( "oaiServer" ) );
			 * sources.setOaiMetadataPrefix(
			 * config.getServletContext().getInitParameter( "oaiMetadataPrefix" ) );
			 * sources.setOaiHome( config.getServletContext().getInitParameter(
			 * "oaiHome" ) ); sources.setOaiLocalCopy(
			 * config.getServletContext().getInitParameter( "oaiLocalCopy" ) );
			 * //int oaiDelay = Integer.parseInt(
			 * config.getServletContext().getInitParameter( "oaiRefreshDelay" ) );
			 * sources.setOaiRefreshDelay( 24 );
			 * config.getServletContext().setAttribute( "sources", sources );
			 */
			String delay = config.getInitParameter("globalDataRefreshDelay");
			long refreshDelay = Long.parseLong(delay) * 1000;
			String recent = config.getInitParameter("recentItems");
			int recentItems = Integer.parseInt(recent);
			Global global = new Global(recentItems, refreshDelay);
			config.getServletContext().setAttribute(
					LexConstants.GLOBAL_CONTEXT_ATTR, global);
			LexLogger.debugComponent(global);
			System.setProperty("java.awt.headless", "true");
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param req
	 *            Description of Parameter
	 * @param res
	 *            Description of Parameter
	 * @exception ServletException
	 *                Description of Exception
	 * @exception IOException
	 *                Description of Exception
	 * @since
	 */
	public void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		/*
		 * LexLogger.debug( "Checking Request state at start of
		 * LexActionServlet.service()" ); LexLogger.logRequestState( req );
		 * LexLogger.logSessionState( req );
		 */
		String next;
		try {
			String cmd = req.getParameter(LexConstants.COMMAND_REQ_PARAM);
			Command command = lookupCommand(cmd);
			LexComponent component = (LexComponent) req
					.getAttribute(LexConstants.COMPONENT_REQ_ATTR);
			next = command.execute(req, component);
			CommandToken.set(req);
		} catch (CommandException e) {
			req.setAttribute("javax.servlet.jsp.jspException", e);
			next = LexConstants.ERROR_PAGE;
			try {
				LexComponentRepository.cleanup();
			} catch (LexRepositoryException lre) {
				Exception ex = new Exception(
						"LexComponentRepository couldn't clean up after Exception because: "
								+ lre.getMessage(), e);
				req.setAttribute("javax.servlet.jsp.jspException", ex);
			}
		} catch (Exception e) {
			req.setAttribute("javax.servlet.jsp.jspException", e);
			next = LexConstants.ERROR_PAGE;
			try {
				LexComponentRepository.cleanup();
			} catch (LexRepositoryException lre) {
				Exception ex = new Exception(
						"LexComponentRepository couldn't clean up after Exception because: "
								+ lre.getMessage(), e);
				req.setAttribute("javax.servlet.jsp.jspException", ex);
			}
		}
		RequestDispatcher rd;
		rd = getServletContext().getRequestDispatcher(
				LexConstants.JSP_DIR + next);
		rd.forward(req, res);
		LexLogger
				.debug("Checking Request state at end of LexActionServlet.service()");
		LexLogger.logRequestState(req);
		LexLogger
				.debug("Checking Session state at end of LexActionServlet.service()");
		LexLogger.logSessionState(req);
		LexLogger
				.debug("Checking Context state at end of LexActionServlet.service()");
		LexLogger.logContextState(getServletContext());
	}

	/**
	 * Description of the Method
	 * 
	 * @param cmdKey
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 * @exception CommandException
	 *                Description of Exception
	 * @since
	 */
	private Command lookupCommand(String cmdKey) throws CommandException {
		if (cmdKey == null) {
			cmdKey = "menu";
		}
		if (getCommands().containsKey(cmdKey)) {
			return (Command) getCommands().get(cmdKey);
		} else {
			throw new CommandException("Invalid Command Identifier: '" + cmdKey
					+ "'");
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @since
	 */
	private void initCommands() {
		HashMap commands = new HashMap();

		commands.put("menu", new NullCommand("menu.jsp"));
		commands.put("search", new NullCommand("search.jsp"));
		commands.put("logout", new NullCommand("logout.jsp"));
		commands.put("getMetaPrefsForm", new NullCommand("metaPrefsForm.jsp"));
		commands.put("getMetaDefaultsForm", new NullCommand(
				"metaDefaultsForm.jsp"));

		commands.put("find", new FindCommand("displayEntry.jsp"));

		commands.put("getInsertForm", new GetInsertFormCommand(
				"displayForm.jsp?formMode=insert", Boolean.FALSE));
		commands.put("getInsertTermForm", new GetInsertFormCommand(
				"displayForm.jsp?formMode=insert", Boolean.TRUE));
		commands.put("getTranslationForm", new GetInsertFormCommand(
				"displayForm.jsp?formMode=insert", Boolean.FALSE));
		commands.put("getAnnotationForm", new GetInsertFormCommand(
				"displayForm.jsp?formMode=insert", Boolean.FALSE));

		commands.put("getUpdateForm", new GetUpdateFormCommand(
				"displayForm.jsp?formMode=update", Boolean.FALSE));
		commands.put("getUpdateTermForm", new GetUpdateFormCommand(
				"displayForm.jsp?formMode=update", Boolean.TRUE));

		/*
		 * commands.put( "getAnnotationForm", new AnnotateCommand(
		 * "displayForm.jsp?formMode=insert" ) ); commands.put( "addAnnotation",
		 * new AddAnnotationCommand( "displayEntry.jsp", Boolean.TRUE ) );
		 */
		commands.put("insert", new InsertCommand("displayEntry.jsp",
				Boolean.FALSE));
		commands.put("insertTerm", new InsertCommand("displayEntry.jsp",
				Boolean.TRUE));

		commands.put("update", new UpdateCommand("displayEntry.jsp",
				Boolean.FALSE));
		commands.put("updateTerm", new UpdateCommand("displayEntry.jsp",
				Boolean.TRUE));

		commands.put("display", new DisplayCommand("displayEntry.jsp"));
		commands.put("displayFull", new DisplayCommand("displayEntry.jsp"));
		commands.put("editEntry", new DisplayCommand("displayEntry.jsp"));

		commands.put("getRemoveForm", new GetRemoveFormCommand(
				"displayForm.jsp?formMode=remove", Boolean.FALSE));
		commands.put("getRemoveTermForm", new GetRemoveFormCommand(
				"displayForm.jsp?formMode=remove", Boolean.TRUE));
		commands.put("remove", new RemoveCommand("displayEntry.jsp",
				Boolean.FALSE));
		commands.put("removeTerm", new RemoveCommand("menu.jsp", Boolean.TRUE));

		commands.put("setMetaPrefs", new PreferencesCommand("menu.jsp"));
		commands.put("setMetaDefaults", new PreferencesCommand("menu.jsp"));
		//		commands.put( "refreshSources", new RefreshSourcesCommand( "test.jsp"
		// ) );

		commands.put("abort", new AbortCommand("menu.jsp"));

		setCommands(commands);
	}
}

