package org.thdl.lex;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class LogoutServlet extends HttpServlet
{
	private String goodbyePage;
	private UserSessionManager sessionMgr = UserSessionManager.getInstance();

	public void setGoodbyePage(String goodbyePage) {
	this.goodbyePage = goodbyePage;
	}
	public String getGoodbyePage() {
		return goodbyePage;
	}

	public void init(ServletConfig config) throws ServletException
	{
		setGoodbyePage( config.getInitParameter("goodbyePage") );
		if (goodbyePage == null)
			throw new ServletException( "The goodbyePage init parameter must be specified.");
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		HttpSession session = request.getSession(true);
		sessionMgr.removeSessionUser(session);
		try
		{
			UserSessionManager.doRedirect( request, response, goodbyePage);
		}
		catch (IOException e) {throw new ServletException("could not redirect to goodbye page");}
	}
}
