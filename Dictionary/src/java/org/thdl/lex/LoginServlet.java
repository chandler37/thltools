package org.thdl.lex;
import java.io.IOException;
import javax.servlet.RequestDispatcher;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thdl.lex.component.*;
import org.thdl.users.*;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 2, 2003
 */
public class LoginServlet extends HttpServlet
{
//attributes

	private String welcomePage;
	private String loginPage;
	private UserSessionManager sessionManager;


//accessors
	/**
	 *  Sets the welcomePage attribute of the LoginServlet object
	 *
	 * @param  welcomePage  The new welcomePage value
	 * @since
	 */
	public void setWelcomePage( String welcomePage )
	{
		this.welcomePage = welcomePage;
	}


	/**
	 *  Sets the loginPage attribute of the LoginServlet object
	 *
	 * @param  loginPage  The new loginPage value
	 * @since
	 */
	public void setLoginPage( String loginPage )
	{
		this.loginPage = loginPage;
	}


	/**
	 *  Gets the welcomePage attribute of the LoginServlet object
	 *
	 * @return    The welcomePage value
	 * @since
	 */
	public String getWelcomePage()
	{
		return welcomePage;
	}


	/**
	 *  Gets the loginPage attribute of the LoginServlet object
	 *
	 * @return    The loginPage value
	 * @since
	 */
	public String getLoginPage()
	{
		return loginPage;
	}


	/**
	 *  Gets the sessionManager attribute of the LoginServlet object
	 *
	 * @return    The sessionManager value
	 * @since
	 */
	public UserSessionManager getSessionManager()
	{
		if ( null == sessionManager )
		{
			return UserSessionManager.getInstance();
		}
		else
		{
			return sessionManager;
		}
	}


//helper methods
	/**
	 *  Description of the Method
	 *
	 * @param  config                Description of Parameter
	 * @exception  ServletException  Description of Exception
	 * @since
	 */
	public void init( ServletConfig config ) throws ServletException
	{
		setWelcomePage( "/action?cmd=menu" );

		if ( getWelcomePage() == null )
		{
			throw new ServletException( "The welcomePage init parameter must be specified." );
		}

		setLoginPage( config.getInitParameter( "loginPage" ) );

		if ( getLoginPage() == null )
		{
			throw new ServletException( "The loginPage init parameter must be specified." );
		}

	}


	/**
	 *  Description of the Method
	 *
	 * @param  request               Description of Parameter
	 * @param  response              Description of Parameter
	 * @exception  ServletException  Description of Exception
	 * @since
	 */
	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException
	{
		String username = request.getParameter( LexConstants.USERNAME_REQ_PARAM );

		if ( username == null )
		{
			throw new ServletException( "No Username Specified" );
		}

		String password = request.getParameter( LexConstants.PASSWORD_REQ_PARAM );

		if ( password == null )
		{
			throw new ServletException( "No Password Specified" );
		}

		ThdlUser thdlUser = null;
		try
		{
			ThdlUser lexUser = new LexUser();
			lexUser.setUsername( username );
			lexUser.setPassword( password );
			lexUser = ThdlUserRepository.getInstance().validate( lexUser, "dictionary" );
			doLoginSuccess( request, response, lexUser );
		}
		catch ( ThdlUserRepositoryException ture )
		{
//doLoginFailure( request, response, username );
			throw new ServletException( ture );
		}
		catch ( Exception e )
		{
			throw new ServletException( e );
		}
	}


	/**
	 *  Description of the Method
	 *
	 * @param  response                    Description of Parameter
	 * @param  user                        Description of Parameter
	 * @param  req                         Description of the Parameter
	 * @exception  IOException             Description of Exception
	 * @exception  LexRepositoryException  Description of Exception
	 * @exception  LexComponentException   Description of Exception
	 * @since
	 */
	private void doLoginSuccess( HttpServletRequest req, HttpServletResponse response, ThdlUser user ) throws IOException, LexRepositoryException, LexComponentException
	{
		Visit visit = UserSessionManager.getInstance().getVisit( req.getSession( true ) );

		visit.setUser( user );

		Preferences preferences = new Preferences( user );
		visit.setPreferences( preferences );

		visit.setDisplayMode( "brief" );
		String targetPage = UserSessionManager.getInstance().getSessionLoginTarget( req.getSession( true ), true );

		if ( targetPage == null )
		{
			UserSessionManager.doRedirect( req, response, getWelcomePage() );
		}
		else
		{
			targetPage = response.encodeRedirectURL( targetPage );
			response.sendRedirect( targetPage );
		}
	}


	/**
	 *  Description of the Method
	 *
	 * @param  request               Description of Parameter
	 * @param  response              Description of Parameter
	 * @param  username              Description of Parameter
	 * @exception  ServletException  Description of Exception
	 * @since
	 */
	private void doLoginFailure( HttpServletRequest request, HttpServletResponse response, String username ) throws ServletException
	{

		String loginURL = getLoginPage() + "?retry=true&username=" + username;
		try
		{
			UserSessionManager.doRedirect( request, response, loginURL );
		}
		catch ( IOException ioe )
		{
			throw new ServletException( ioe );
		}
	}
}

