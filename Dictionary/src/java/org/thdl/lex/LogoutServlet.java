package org.thdl.lex;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Description of the Class
 * 
 * @author travis
 * @created October 21, 2003
 */
public class LogoutServlet extends HttpServlet {
	private String goodbyePage;

	/**
	 * Sets the goodbyePage attribute of the LogoutServlet object
	 * 
	 * @param goodbyePage
	 *            The new goodbyePage value
	 */
	public void setGoodbyePage(String goodbyePage) {
		this.goodbyePage = goodbyePage;
	}

	/**
	 * Gets the goodbyePage attribute of the LogoutServlet object
	 * 
	 * @return The goodbyePage value
	 */
	public String getGoodbyePage() {
		return goodbyePage;
	}

	/**
	 * Description of the Method
	 * 
	 * @param config
	 *            Description of the Parameter
	 * @exception ServletException
	 *                Description of the Exception
	 */
	public void init(ServletConfig config) throws ServletException {
		setGoodbyePage(config.getInitParameter("goodbyePage"));
		if (goodbyePage == null) {
			throw new ServletException(
					"The goodbyePage init parameter must be specified.");
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param request
	 *            Description of the Parameter
	 * @param response
	 *            Description of the Parameter
	 * @exception ServletException
	 *                Description of the Exception
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		HttpSession session = request.getSession(true);
		UserSessionManager.getInstance().removeVisit(session);
		try {
			String redirect = response.encodeRedirectURL(getGoodbyePage());
			response.sendRedirect(redirect);
		} catch (IOException e) {
			throw new ServletException("could not redirect to goodbye page");
		}
	}
}

