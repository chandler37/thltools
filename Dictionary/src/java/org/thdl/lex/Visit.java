package org.thdl.lex;

import javax.servlet.http.HttpSession;

import org.thdl.users.ThdlUser;

/**
 * Description of the Class
 * 
 * @author travis
 * @created October 1, 2003
 */
public class Visit {
	//attributes
	HttpSession session;

	String token;

	DisplayHelper helper;

	Preferences preferences;

	LexQuery query;

	ThdlUser user;

	String displayMode;

	/**
	 * Sets the token attribute of the Visit object
	 * 
	 * @param token
	 *            The new token value
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Gets the token attribute of the Visit object
	 * 
	 * @return The token value
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Sets the session attribute of the Visit object
	 * 
	 * @param session
	 *            The new session value
	 */
	public void setSession(HttpSession session) {
		this.session = session;
	}

	/**
	 * Gets the session attribute of the Visit object
	 * 
	 * @return The session value
	 */
	public HttpSession getSession() {
		return session;
	}

	/**
	 * Sets the helper attribute of the Visit object
	 * 
	 * @param helper
	 *            The new helper value
	 */
	public void setHelper(DisplayHelper helper) {
		this.helper = helper;
	}

	/**
	 * Sets the preferences attribute of the Visit object
	 * 
	 * @param preferences
	 *            The new preferences value
	 */
	public void setPreferences(Preferences preferences) {
		this.preferences = preferences;
	}

	/**
	 * Sets the query attribute of the Visit object
	 * 
	 * @param query
	 *            The new query value
	 */
	public void setQuery(LexQuery query) {
		this.query = query;
	}

	/**
	 * Sets the user attribute of the Visit object
	 * 
	 * @param user
	 *            The new user value
	 */
	public void setUser(ThdlUser user) {
		if (user.hasRole("guest")) {
			getSession().setMaxInactiveInterval(60 * 5);
		} else {
			getSession().setMaxInactiveInterval(60 * 60 * 8);
		}
		this.user = user;
	}

	/**
	 * Sets the displayMode attribute of the Visit object
	 * 
	 * @param displayMode
	 *            The new displayMode value
	 */
	public void setDisplayMode(String displayMode) {
		this.displayMode = displayMode;
	}

	/**
	 * Gets the helper attribute of the Visit object
	 * 
	 * @return The helper value
	 */
	public DisplayHelper getHelper() {
		if (null == helper) {
			setHelper(new DisplayHelper());
		}
		return helper;
	}

	/**
	 * Gets the preferences attribute of the Visit object
	 * 
	 * @return The preferences value
	 */
	public Preferences getPreferences() {
		if (null == preferences) {
			try {
				setPreferences(new Preferences(getUser()));
			} catch (Exception e) {
				setPreferences(new Preferences());
			}
		}
		return preferences;
	}

	/**
	 * Gets the query attribute of the Visit object
	 * 
	 * @return The query value
	 */
	public LexQuery getQuery() {
		if (null == query) {
			setQuery(new LexQuery());
		}
		return query;
	}

	/**
	 * Gets the user attribute of the Visit object
	 * 
	 * @return The user value
	 */
	public ThdlUser getUser() {
		return user;
	}

	/**
	 * Gets the displayMode attribute of the Visit object
	 * 
	 * @return The displayMode value
	 */
	public String getDisplayMode() {
		return displayMode;
	}

	//constructor
	/**
	 * Constructor for the Visit object
	 */
	public Visit() {
	}

	/**
	 * Constructor for the Visit object
	 * 
	 * @param session
	 *            Description of the Parameter
	 */
	public Visit(HttpSession session) {
		this();
		setSession(session);
	}

	/**
	 * Constructor for the Visit object
	 * 
	 * @param session
	 *            Description of the Parameter
	 * @param user
	 *            Description of the Parameter
	 */
	public Visit(HttpSession session, ThdlUser user) {
		this(session);
		setUser(user);
	}

}

