package org.thdl.lex.commands;

import org.thdl.lex.LexUser;
import org.thdl.lex.UserSessionManager;
import org.thdl.lex.component.ILexComponent;

/**
 * Description of the Class
 * 
 * @author travis
 * @created October 3, 2003
 */
public abstract class LexCommand implements Command {
	// attributes
	private String next;

	private UserSessionManager sessionManager;

	// accessors
	/**
	 * Sets the sessionManager attribute of the LexCommand object
	 * 
	 * @param sessionManager
	 *            The new sessionManager value
	 * @since
	 */
	public void setSessionManager(UserSessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	/**
	 * Sets the next attribute of the LexCommand object
	 * 
	 * @param next
	 *            The new next value
	 * @since
	 */
	public void setNext(String next) {
		this.next = next;
	}

	/**
	 * Gets the sessionManager attribute of the LexCommand object
	 * 
	 * @return The sessionManager value
	 * @since
	 */
	public UserSessionManager getSessionManager() {
		if (null == sessionManager) {
			setSessionManager(UserSessionManager.getInstance());
		}
		return sessionManager;
	}

	/**
	 * Gets the next attribute of the LexCommand object
	 * 
	 * @return The next value
	 * @since
	 */
	public String getNext() {
		return next;
	}

	/**
	 * Gets the component attribute of the LexCommand object
	 * 
	 * @param user
	 *            Description of the Parameter
	 * @param component
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @since
	 */
	/*
	 * public ILexComponent getComponent() { return component; }
	 */
	// helpers
public boolean validate(LexUser user, ILexComponent component) {
		boolean valid = false;
		Integer creator = component.getMeta().getCreatedBy();

		if (user.getId().equals(creator) || user.isCanEdit()) {
			valid = true;
		}
		return valid;
	}
	// constructors

	/**
	 * Constructor for the LexCommand object
	 * 
	 * @param next
	 *            Description of Parameter
	 * @since
	 */
	public LexCommand(String next) {
		setNext(next);
	}

	/**
	 * Constructor for the LexCommand object
	 * 
	 * @since
	 */
	public LexCommand() {
	}
}
