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

public class AuthenticationFilter implements Filter
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
		setLoginPage( config.getInitParameter("loginPage") );
		if ( null == getLoginPage() )
			throw new ServletException("The loginPage parameter must be specified");
	}
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		if ( request instanceof HttpServletRequest && response instanceof HttpServletResponse)
		{
			HttpServletRequest req = (HttpServletRequest) request;
			HttpSession session = req.getSession(true);
			ThdlUser user = sessionMgr.getSessionUser(session);
			if (null == user )
			{
				requireLogin(req, (HttpServletResponse)response, session);
			}
			else
			{
				chain.doFilter(request, response);
			}
		}
		else
		{
			throw new ServletException("Filter only applicable to HTTP and HTTPS requests");
		}
	}
	public void destroy() {}
//helper methods
	public void requireLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException
	{
		StringBuffer buffer = request.getRequestURL();
		String query = request.getQueryString();
		Enumeration params = request.getParameterNames();
		boolean paramsExist;
		if ( params.hasMoreElements() )
		{
			paramsExist=true;
			buffer.append('?');
			while ( params.hasMoreElements() )
			{
				String temp = (String)params.nextElement();
				buffer.append( temp + "=" +request.getParameter( temp ) );
				if ( params.hasMoreElements() )
					buffer.append("&");
			}
		}
		else
			paramsExist=false;

		sessionMgr.setSessionLoginTarget( session, buffer.toString() );
		UserSessionManager.doRedirect(request, response, loginPage);
	}
}
