package org.thdl.lex.commands;

import javax.servlet.http.HttpServletRequest;

import org.thdl.lex.component.ILexComponent;

/**
 * Description of the Class
 * 
 * @author travis
 * @created October 1, 2003
 */
public class AbortCommand implements Command {
	private String next;

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
		req.setAttribute("termtool.msg", "Operation Aborted");
		return next;
	}

	/**
	 * Constructor for the AbortCommand object
	 * 
	 * @param next
	 *            Description of Parameter
	 * @since
	 */
	public AbortCommand(String next) {
		this.next = next;
	}
}

