package org.thdl.lex;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thdl.users.*;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 21, 2003
 */
public class AuthenticationFilter implements Filter
{
//attributes
	private String loginPage;

//accessors
	/**
	 *  Sets the loginPage attribute of the AuthenticationFilter object
	 *
	 * @param  loginPage  The new loginPage value
	 */
	public void setLoginPage( String loginPage )
	{
		this.loginPage = loginPage;
	}


	/**
	 *  Gets the loginPage attribute of the AuthenticationFilter object
	 *
	 * @return    The loginPage value
	 */
	public String getLoginPage()
	{
		return loginPage;
	}

//contract methods
	/**
	 *  Description of the Method
	 *
	 * @param  config                Description of the Parameter
	 * @exception  ServletException  Description of the Exception
	 */
	public void init( FilterConfig config ) throws ServletException
	{
		setLoginPage( config.getInitParameter( "loginPage" ) );
		if ( null == getLoginPage() )
		{
			throw new ServletException( "The loginPage parameter must be specified" );
		}
	}


	/**
	 *  Description of the Method
	 *
	 * @param  request               Description of the Parameter
	 * @param  response              Description of the Parameter
	 * @param  chain                 Description of the Parameter
	 * @exception  IOException       Description of the Exception
	 * @exception  ServletException  Description of the Exception
	 */
	public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException, ServletException
	{
		if ( request instanceof HttpServletRequest && response instanceof HttpServletResponse )
		{
			HttpServletRequest req = (HttpServletRequest) request;
			Visit visit = UserSessionManager.getInstance().getVisit( req.getSession( true ) );
			ThdlUser user = visit.getUser();
			if ( null == user )
			{
				requireLogin( req, (HttpServletResponse) response, req.getSession( true ) );
			}
			else
			{
				chain.doFilter( request, response );
			}
		}
		else
		{
			throw new ServletException( "Filter only applicable to HTTP and HTTPS requests" );
		}
	}


	/**
	 *  Description of the Method
	 */
	public void destroy() { }
//helper methods

	/**
	 *  Description of the Method
	 *
	 * @param  request          Description of the Parameter
	 * @param  response         Description of the Parameter
	 * @param  session          Description of the Parameter
	 * @exception  IOException  Description of the Exception
	 */
	public void requireLogin( HttpServletRequest request, HttpServletResponse response, HttpSession session ) throws IOException
	{
		StringBuffer buffer = request.getRequestURL();
		String query = request.getQueryString();
		Enumeration params = request.getParameterNames();
		boolean paramsExist;
		if ( params.hasMoreElements() )
		{
			paramsExist = true;
			buffer.append( '?' );
			while ( params.hasMoreElements() )
			{
				String temp = (String) params.nextElement();
				buffer.append( temp + "=" + request.getParameter( temp ) );
				if ( params.hasMoreElements() )
				{
					buffer.append( "&" );
				}
			}
		}
		else
		{
			paramsExist = false;
		}

		UserSessionManager.getInstance().setSessionLoginTarget( session, buffer.toString() );
		UserSessionManager.doRedirect( request, response, loginPage );
	}
}

