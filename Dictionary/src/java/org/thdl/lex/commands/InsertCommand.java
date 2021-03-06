package org.thdl.lex.commands;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.thdl.lex.DisplayHelper;
import org.thdl.lex.LexComponentRepository;
import org.thdl.lex.LexConstants;
import org.thdl.lex.LexLogger;
import org.thdl.lex.LexQuery;
import org.thdl.lex.LexRepositoryException;
import org.thdl.lex.LexUser;
import org.thdl.lex.Preferences;
import org.thdl.lex.UserSessionManager;
import org.thdl.lex.Visit;
import org.thdl.lex.component.AnalyticalNote;
import org.thdl.lex.component.ILexComponent;
import org.thdl.lex.component.ITerm;
import org.thdl.lex.component.LexComponentException;
import org.thdl.lex.component.Meta;
import org.thdl.lex.component.Translatable;

/**
 * Description of the Class
 * 
 * @author travis
 * @created October 6, 2003
 */
public class InsertCommand extends LexCommand implements Command {

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

			LexQuery query = visit.getQuery();
			ITerm term = query.getEntry();
			LexUser user = (LexUser) visit.getUser();
			Preferences prefs = visit.getPreferences();

			if (CommandToken.isValid(req) && validate(user, component)) {
				if (!isTermMode()) {
					LexComponentRepository.update(term);
				}

				if (isTermMode()) {
					query.setEntry((ITerm) component);
					term = query.getEntry();
				} else if (component instanceof AnalyticalNote) {
					LexLogger
							.debug("Debugging Component before inserting analytical note");
					LexLogger.debugComponent(component);
					ILexComponent parent = term.findParent(component
							.getParentId());
					List list = parent.getAnalyticalNotes();
					if (null == list) {
						list = new LinkedList();
					}
					list.add(component);
					parent.setAnalyticalNotes(list);
					//term.addSiblingList( parent, component, list );
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
					List list = source.getTranslations();
					if (null == list) {
						list = new LinkedList();
					}
					list.add(translation);
					source.setTranslations(list);
				} else {
					term.addChild(component);
				}

				Meta meta = new Meta(user, prefs);
				meta.populate(req.getParameterMap());
				component.setMeta(meta);

				Date now = new Date(System.currentTimeMillis());
				component.getMeta().setCreatedOn(now);
				component.getMeta().setModifiedOn(now);
				component.getMeta().setCreatedBy(user.getId());
				component.getMeta().setModifiedBy(user.getId());

				LexComponentRepository.save(term);

				if (!isTermMode()) {
					term.getMeta().setModifiedOn(now);
					term.getMeta().setModifiedBy(user.getId());
				}

				if (!(component instanceof AnalyticalNote)
						&& null != req.getParameter("analyticalNote")
						&& req.getParameter("analyticalNote").length() > 0) {
					AnalyticalNote note = new AnalyticalNote();
					note.setAnalyticalNote(req.getParameter("analyticalNote"));
					note.setParentId(component.getMetaId());
					//note.setPrecedence( new Integer( 0 ) );
					component.setAnalyticalNotes(new LinkedList());
					component.getAnalyticalNotes().add(note);
					meta = new Meta(user, prefs);
					meta.populate(req.getParameterMap());
					note.setMeta(meta);
					LexLogger.debug("Adding analytical note to "
							+ component.getLabel());
				}

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
			LexLogger
					.debug("Showing Term Map at end of InsertCommand.execute()");
			LexLogger.debugTerm(((Visit) req.getSession(false).getAttribute(
					"visit")).getQuery().getEntry());
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
	public InsertCommand(String next, Boolean termMode) {
		super(next);
		setTermMode(termMode.booleanValue());
	}
}

