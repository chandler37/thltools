package org.thdl.lex.commands;

import javax.servlet.http.HttpServletRequest;

import org.thdl.lex.LexConstants;
import org.thdl.lex.LexUtilities;
import org.thdl.lex.Preferences;
import org.thdl.lex.UserSessionManager;
import org.thdl.lex.Visit;
import org.thdl.lex.component.ILexComponent;
import org.thdl.lex.component.LexComponentException;

/**
 * Description of the Class
 * 
 * @author travis
 * @created October 6, 2003
 */
public class PreferencesCommand extends LexCommand implements Command {
	/**
	 * Description of the Method
	 * 
	 * @param req
	 *            Description of the Parameter
	 * @param component
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception CommandException
	 *                Description of the Exception
	 */
	public String execute(HttpServletRequest req, ILexComponent component)
			throws CommandException {
		try {
			Visit visit = UserSessionManager.getInstance().getVisit(
					req.getSession(true));

			Preferences isb = visit.getPreferences();
			if (req.getParameter(LexConstants.COMMAND_REQ_PARAM).equals(
					"setMetaPrefs")) {
				isb.setLanguageSet(LexUtilities.convertToIntegerArray(req
						.getParameterValues("languages")));
				isb.setDialectSet(LexUtilities.convertToIntegerArray(req
						.getParameterValues("dialects")));
				isb.setSourceSet(LexUtilities.convertToIntegerArray(req
						.getParameterValues("sources")));
				isb.setProjectSubjectSet(LexUtilities.convertToIntegerArray(req
						.getParameterValues("projectSubjects")));
				isb.setScriptSet(LexUtilities.convertToIntegerArray(req
						.getParameterValues("scripts")));
			} else if (req.getParameter(LexConstants.COMMAND_REQ_PARAM).equals(
					"setMetaDefaults")) {
				isb.setLanguage(new Integer(req.getParameter("language")));
				isb.setDialect(new Integer(req.getParameter("dialect")));
				isb.setSource(new Integer(req.getParameter("source")));
				isb.setProjectSubject(new Integer(req
						.getParameter("projectSubject")));
				isb.setScript(new Integer(req.getParameter("script")));
				isb.setNote(req.getParameter("note"));
				if (null != req.getParameter("useDefaultLanguage")
						&& req.getParameter("useDefaultLanguage")
								.equals("true")) {
					isb.setUseDefaultLanguage(true);
				} else {
					isb.setUseDefaultLanguage(false);
				}
				if (null != req.getParameter("useDefaultDialect")
						&& req.getParameter("useDefaultDialect").equals("true")) {
					isb.setUseDefaultDialect(true);
				} else {
					isb.setUseDefaultDialect(false);
				}
				if (null != req.getParameter("useDefaultSource")
						&& req.getParameter("useDefaultSource").equals("true")) {
					isb.setUseDefaultSource(true);
				} else {
					isb.setUseDefaultSource(false);
				}
				if (null != req.getParameter("useDefaultProjSub")
						&& req.getParameter("useDefaultProjSub").equals("true")) {
					isb.setUseDefaultProjSub(true);
				} else {
					isb.setUseDefaultProjSub(false);
				}
				if (null != req.getParameter("useDefaultScript")
						&& req.getParameter("useDefaultScript").equals("true")) {
					isb.setUseDefaultScript(true);
				} else {
					isb.setUseDefaultScript(false);
				}
				if (null != req.getParameter("useDefaultNote")
						&& req.getParameter("useDefaultNote").equals("true")) {
					isb.setUseDefaultNote(true);
				} else {
					isb.setUseDefaultNote(false);
				}
			}
			isb.save();
		} catch (LexComponentException lre) {
			throw new CommandException("LexComponentException says: "
					+ lre.getMessage());
		}

		return getNext();
	}

	/**
	 * Constructor for the PreferencesCommand object
	 * 
	 * @param next
	 *            Description of the Parameter
	 */
	public PreferencesCommand(String next) {
		super(next);
	}
}

