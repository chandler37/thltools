package org.thdl.lex;

import org.thdl.users.*;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Enumeration;

public class GuestFilter implements Filter
{
//attributes
	private String loginPage;
	private UserSessionManager sessionMgr;
	
//accessors
	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}
	public String getLoginPage() {
		return loginPage;
	}
	public void setSessionMgr() {
		this.sessionMgr = UserSessionManager.getInstance();
	}
	public UserSessionManager getSessionMgr() {
		return sessionMgr;
	}

//contract methods
	public void init(FilterConfig config) throws ServletException
	{
		setSessionMgr();
	}
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		if ( request instanceof HttpServletRequest && response instanceof HttpServletResponse)
		{
			HttpServletRequest req = (HttpServletRequest) request;
			HttpSession session = req.getSession(true);
			ThdlUser user = getSessionMgr().getSessionUser(session);
			if (null == user )
			{
				try
				{
					user = new LexUser();
				}
				catch ( Exception e )
				{
					throw new ServletException( e );
				}
				user.setRoles( "guest" );
				getSessionMgr().setSessionUser( session, user );
				getSessionMgr().setDisplayMode( session, "full" );
			}
			chain.doFilter(request, response);
		}
		else
		{
			throw new ServletException("Filter only applicable to HTTP and HTTPS requests");
		}
	}
	public void destroy() {}
//helper methods
}
