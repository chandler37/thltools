package org.thdl.lex.commands;

import org.thdl.lex.component.ILexComponent;

/**
 * Description of the Interface
 * 
 * @author travis
 * @created October 3, 2003
 */
public interface Command {
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
	public String execute(javax.servlet.http.HttpServletRequest req,
			ILexComponent component) throws CommandException;
}

