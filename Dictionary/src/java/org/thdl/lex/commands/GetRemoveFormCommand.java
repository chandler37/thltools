package org.thdl.lex.commands;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.thdl.lex.LexComponentRepository;
import org.thdl.lex.LexConstants;
import org.thdl.lex.LexLogger;
import org.thdl.lex.LexQuery;
import org.thdl.lex.LexRepositoryException;
import org.thdl.lex.LexUser;
import org.thdl.lex.UserSessionManager;
import org.thdl.lex.Visit;
import org.thdl.lex.component.IAnalyticalNote;
import org.thdl.lex.component.ILexComponent;
import org.thdl.lex.component.ITerm;
import org.thdl.lex.component.LexComponentException;
import org.thdl.lex.component.Translatable;

/**
 * Description of the Class
 * 
 * @author travis
 * @created October 6, 2003
 */
public class GetRemoveFormCommand extends LexCommand implements Command {
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
		String next = getNext();
		Visit visit = UserSessionManager.getInstance().getVisit(
				req.getSession(true));
		LexQuery query = visit.getQuery();
		ITerm term = query.getEntry();
		String msg = null;
		LexUser user = visit.getUser();
		if (validate(user, component)) {

			try {

				LexLogger
						.debug("Checking component state from GetRemoveFormCommand BEFORE component assignment");
				LexLogger.debugComponent(component);
				if (isTermMode()) {
					component = query.getEntry();
				} else if (component instanceof IAnalyticalNote) {
					ILexComponent parent = term.findParent(component
							.getParentId());
					List notes = parent.getAnalyticalNotes();
					int index = notes.indexOf(component);
					component = (ILexComponent) notes.get(index);
				} else if (component instanceof Translatable
						&& null != ((Translatable) component)
								.getTranslationOf()) {
					LexComponentRepository.update(term);
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
					req
							.setAttribute(LexConstants.ORIGINALBEAN_REQ_ATTR,
									source);
				} else {
					LexComponentRepository.update(term);
					component = term.findChild(component.getMetaId());
				}
				req.setAttribute(LexConstants.COMPONENT_REQ_ATTR, component);
				LexLogger
						.debug("Checking component state from GetRemoveFormCommand AFTER component assignment");
				LexLogger.debugComponent(component);
			} catch (LexRepositoryException e) {
				throw new CommandException(e);
			} catch (LexComponentException e) {
				throw new CommandException(e);
			}

			msg = "You have reached the Remove Form";
			visit.setDisplayMode("addEditForm");
		} else {
			msg = "A dictionary component can only be removed by the person who created it or an administrator.";
			next = "displayEntry.jsp";
		}

		req.setAttribute(LexConstants.MESSAGE_REQ_ATTR, msg);

		return next;
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
	public GetRemoveFormCommand(String next, Boolean termMode) {
		super(next);
		setTermMode(termMode.booleanValue());
	}

}

