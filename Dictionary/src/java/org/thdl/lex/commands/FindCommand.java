package org.thdl.lex.commands;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.thdl.lex.DisplayHelper;
import org.thdl.lex.LexComponentRepository;
import org.thdl.lex.LexConstants;
import org.thdl.lex.LexQuery;
import org.thdl.lex.LexRepositoryException;
import org.thdl.lex.UserSessionManager;
import org.thdl.lex.Visit;
import org.thdl.lex.component.ILexComponent;
import org.thdl.lex.component.ITerm;

/**
 * Description of the Class
 * 
 * @author travis
 * @created October 1, 2003
 */
public class FindCommand extends LexCommand implements Command {
	//helper methods
	/**
	 * Description of the Method
	 * 
	 * @param req
	 *            Description of Parameter
	 * @param component
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 * @exception CommandException
	 *                Description of Exception
	 * @since
	 */
	public String execute(HttpServletRequest req, ILexComponent component)
			throws CommandException {
		try {
			String msg = null;
			String next = getNext();
			Visit visit = UserSessionManager.getInstance().getVisit(
					req.getSession(true));
			DisplayHelper displayHelper = visit.getHelper();
			LexQuery query = visit.getQuery();
			query.populate(req.getParameterMap());

			if (component instanceof ITerm) {
				ITerm term = (ITerm) component;

				query.setQueryComponent(term);
				LexComponentRepository.findTermsByTerm(query);
				Iterator iterator = query.getResults().keySet().iterator();
				if (iterator.hasNext()) {
					visit.setQuery(query);
					msg = "There are " + query.getResults().size()
							+ " terms matching " + term.getTerm();
				} else {
					next = "menu.jsp";
					msg = "There were no terms matching " + term.getTerm();
				}
			}
			req.setAttribute(LexConstants.MESSAGE_REQ_ATTR, msg);
			query.setQueryComponent(null);
			return next;
		} catch (LexRepositoryException e) {
			throw new CommandException(e);
		}
	}

	//constructors
	/**
	 * Constructor for the FindCommand object
	 * 
	 * @param next
	 *            Description of the Parameter
	 * @since
	 */
	public FindCommand(String next) {
		super(next);
	}
}

