package org.thdl.lex.commands;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.thdl.lex.DisplayHelper;
import org.thdl.lex.LexComponentRepository;
import org.thdl.lex.LexConstants;
import org.thdl.lex.LexLogger;
import org.thdl.lex.LexQuery;
import org.thdl.lex.LexRepositoryException;
import org.thdl.lex.UserSessionManager;
import org.thdl.lex.Visit;
import org.thdl.lex.component.AnalyticalNote;
import org.thdl.lex.component.ILexComponent;
import org.thdl.lex.component.ITerm;
import org.thdl.lex.component.LexComponentException;
import org.thdl.lex.component.Translatable;
import org.thdl.users.ThdlUser;

/**
 * Description of the Class
 * 
 * @author travis
 * @created October 6, 2003
 */
public class UpdateCommand extends LexCommand implements Command {

	private boolean termMode;

	/**
	 * Sets the termMode attribute of the GetFormCommand object
	 * 
	 * @param termMode
	 *            The new termMode value
	 */
	public void setTermMode(boolean termMode) {
		this.termMode = termMode;
	}

	/**
	 * Gets the termMode attribute of the GetFormCommand object
	 * 
	 * @return The termMode value
	 */
	public boolean isTermMode() {
		return termMode;
	}

	//helper methods
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
		String msg = null;
		String next = getNext();
		Visit visit = UserSessionManager.getInstance().getVisit(
				req.getSession(true));

		DisplayHelper displayHelper = visit.getHelper();
		try {
			HttpSession ses = req.getSession(false);
			if (null == ses) {
				throw new CommandException(
						"Could not update component, user's session has expired");
			}

			ThdlUser user = visit.getUser();
			LexQuery query = visit.getQuery();
			ITerm term = query.getEntry();

			if (CommandToken.isValid(req) && validate(user, component)) {
				LexComponentRepository.update(term);

				LexLogger
						.debug("Checking component state from updateCommand BEFORE component assignment");
				LexLogger.debugComponent(component);
				if (isTermMode()) {
					term.populate(req.getParameterMap());
					term.getMeta().populate(req.getParameterMap());
					component = term;
				} else if (component instanceof AnalyticalNote) {
					LexLogger
							.debug("Debugging Component before updating analytical note");
					LexLogger.debugComponent(component);
					ILexComponent parent = term.findParent(component
							.getParentId());
					List notes = parent.getAnalyticalNotes();
					ILexComponent ilc = (ILexComponent) notes.get(notes
							.indexOf(component));
					ilc.populate(component);
				} else if (component instanceof Translatable
						&& null != ((Translatable) component)
								.getTranslationOf()) {
					Translatable translation = (Translatable) component;
					Translatable source = null;
					try {
						source = (Translatable) translation.getClass()
								.newInstance();
					} catch (Exception e) {
						throw new CommandException(e);
					}
					source.setMetaId(translation.getTranslationOf());
					source.setParentId(translation.getParentId());
					source = (Translatable) term.findChild(source);
					List translationList = source.getTranslations();
					component = (ILexComponent) translationList
							.get(translationList.indexOf(translation));
					component.populate(req.getParameterMap());
					component.getMeta().populate(req.getParameterMap());
				} else {
					ILexComponent ilc = term.findChild(component.getMetaId());
					ilc.populate(req.getParameterMap());
					ilc.getMeta().populate(req.getParameterMap());
					component = ilc;
				}
				LexLogger
						.debug("Checking component state from updateCommand AFTER component assignment");
				LexLogger.debugComponent(component);

				Date now = new Date(System.currentTimeMillis());
				component.getMeta().setModifiedOn(now);
				component.getMeta().setModifiedBy(user.getId());
				term.getMeta().setModifiedOn(now);
				term.getMeta().setModifiedBy(user.getId());

				LexLogger.debugComponent(component);
				LexLogger.debugComponent(term);

				LexComponentRepository.update(term);
				msg = "Successful Update";
				visit.setDisplayMode("edit");
			} else {
				msg = CommandToken.isValid(req) ? "Unauthorized update attempted"
						: "Invalid reload attempted.";
			}
			return next;
		} catch (LexComponentException e) {
			throw new CommandException(e);
		} catch (LexRepositoryException e) {
			throw new CommandException("Command had trouble processing "
					+ component, e);
		} finally {
			req.setAttribute(LexConstants.MESSAGE_REQ_ATTR, msg);
		}
	}

	//constructors
	/**
	 * Constructor for the GetFormCommand object
	 * 
	 * @param next
	 *            Description of the Parameter
	 * @param termMode
	 *            Description of the Parameter
	 */
	public UpdateCommand(String next, Boolean termMode) {
		super(next);
		setTermMode(termMode.booleanValue());
	}

}

