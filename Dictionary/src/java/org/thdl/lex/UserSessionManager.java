package org.thdl.lex;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Description of the Class
 * 
 * @author travis
 * @created October 1, 2003
 */
public class UserSessionManager {
	//attributes

	private static UserSessionManager INSTANCE = new UserSessionManager();

	/**
	 * Gets the visit attribute of the UserSessionManager object
	 * 
	 * @param session
	 *            Description of the Parameter
	 * @return The visit value
	 */
	public Visit getVisit(HttpSession session) {
		if (null == session.getAttribute(LexConstants.VISIT_SESSION_ATTR)) {
			setVisit(session, new Visit(session));
		}
		return (Visit) session.getAttribute(LexConstants.VISIT_SESSION_ATTR);
	}

	/**
	 * Sets the visit attribute of the UserSessionManager object
	 * 
	 * @param session
	 *            The new visit value
	 * @param visit
	 *            The new visit value
	 */
	public void setVisit(HttpSession session, Visit visit) {
		session.setAttribute(LexConstants.VISIT_SESSION_ATTR, visit);
	}

	/**
	 * Description of the Method
	 * 
	 * @param session
	 *            Description of the Parameter
	 */
	public void removeVisit(HttpSession session) {
		session.setAttribute(LexConstants.VISIT_SESSION_ATTR, null);
	}

	/**
	 * Sets the sessionLoginTarget attribute of the UserSessionManager object
	 * 
	 * @param session
	 *            The new sessionLoginTarget value
	 * @param loginTarget
	 *            The new sessionLoginTarget value
	 * @since
	 */
	public void setSessionLoginTarget(HttpSession session, String loginTarget) {
		session.setAttribute(LexConstants.LOGINTARGET_SESS_PARAM, loginTarget);
	}

	/**
	 * Gets the instance attribute of the UserSessionManager class
	 * 
	 * @return The instance value
	 * @since
	 */
	public static UserSessionManager getInstance() {
		return INSTANCE;
	}

	/**
	 * Gets the sessionLoginTarget attribute of the UserSessionManager object
	 * 
	 * @param session
	 *            Description of Parameter
	 * @param clear
	 *            Description of Parameter
	 * @return The sessionLoginTarget value
	 * @since
	 */
	public String getSessionLoginTarget(HttpSession session, boolean clear) {
		String target = (String) session
				.getAttribute(LexConstants.LOGINTARGET_SESS_PARAM);
		if (clear) {
			session.removeAttribute(LexConstants.LOGINTARGET_SESS_PARAM);
		}
		return target;
	}

	/**
	 * Description of the Method
	 * 
	 * @param request
	 *            Description of Parameter
	 * @param response
	 *            Description of Parameter
	 * @param url
	 *            Description of Parameter
	 * @exception IOException
	 *                Description of Exception
	 * @since
	 */
	public static void doRedirect(HttpServletRequest request,
			HttpServletResponse response, String url) throws IOException {
		String redirect = response.encodeRedirectURL(request.getContextPath()
				+ url);
		response.sendRedirect(redirect);
	}

	//constructor
	/**
	 * Constructor for the UserSessionManager object
	 * 
	 * @since
	 */
	private UserSessionManager() {
	}
}

