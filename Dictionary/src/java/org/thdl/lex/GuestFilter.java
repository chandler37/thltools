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
 * @created    October 9, 2003
 */
public class GuestFilter implements Filter
{
//attributes
	private String loginPage;
	private UserSessionManager sessionMgr;

//accessors
	/**
	 *  Sets the loginPage attribute of the GuestFilter object
	 *
	 * @param  loginPage  The new loginPage value
	 */
	public void setLoginPage( String loginPage )
	{
		this.loginPage = loginPage;
	}


	/**
	 *  Gets the loginPage attribute of the GuestFilter object
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
				try
				{
					user = new LexUser();
				}
				catch ( Exception e )
				{
					throw new ServletException( e );
				}
				user.setRoles( "guest" );
				visit.setUser( user );
				visit.setDisplayMode( "full" );
			}
			chain.doFilter( request, response );
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
}

