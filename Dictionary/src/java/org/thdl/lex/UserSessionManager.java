package org.thdl.lex;
import java.io.IOException;

import javax.servlet.http.*;
import org.thdl.lex.component.*;

import org.thdl.users.*;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 1, 2003
 */
public class UserSessionManager
{
//attributes

	private static UserSessionManager INSTANCE = new UserSessionManager();


	/**
	 *  Gets the displayHelper attribute of the UserSessionManager object
	 *
	 * @param  session  Description of the Parameter
	 * @return          The displayHelper value
	 */
	public DisplayHelper getDisplayHelper( HttpSession session )
	{
		if ( null == session.getAttribute( LexConstants.DISPLAY_HELPER_SESSION_ATT ) )
		{
			setDisplayHelper( session, new DisplayHelper() );
		}
		return (DisplayHelper) session.getAttribute( LexConstants.DISPLAY_HELPER_SESSION_ATT );
	}


	/**
	 *  Sets the displayHelper attribute of the UserSessionManager object
	 *
	 * @param  session  The new displayHelper value
	 * @param  helper   The new displayHelper value
	 */
	public void setDisplayHelper( HttpSession session, DisplayHelper helper )
	{
		session.setAttribute( LexConstants.DISPLAY_HELPER_SESSION_ATT, helper );
	}


	/**
	 *  Sets the preferences attribute of the UserSessionManager object
	 *
	 * @param  session      The new preferences value
	 * @param  preferences  The new preferences value
	 * @since
	 */
	public void setPreferences( HttpSession session, Preferences preferences )
	{
		session.setAttribute( LexConstants.PREFERENCES_SESS_ATTR, preferences );
	}


	/**
	 *  Sets the query attribute of the UserSessionManager object
	 *
	 * @param  session  The new query value
	 * @param  terms    The new query value
	 * @since
	 */
	public void setQuery( HttpSession session, LexQuery terms )
	{
		session.setAttribute( LexConstants.QUERY_SESS_ATTR, terms );
	}


	/**
	 *  Sets the sessionUser attribute of the UserSessionManager object
	 *
	 * @param  session  The new sessionUser value
	 * @param  user     The new sessionUser value
	 * @since
	 */
	public void setSessionUser( HttpSession session, ThdlUser user )
	{
		session.setAttribute( LexConstants.USER_SESS_ATTR, user );
		String roleParam = "administrator";
		if ( user.hasRole( roleParam ) )
		{
			//roles from Lex.Users.userRoleList (references Lex.UserRoles)
			session.setMaxInactiveInterval( 60 * 60 * 8 );
		}
		else
		{
			session.setMaxInactiveInterval( 60 * 45 );
		}
	}


	/**
	 *  Sets the sessionLoginTarget attribute of the UserSessionManager object
	 *
	 * @param  session      The new sessionLoginTarget value
	 * @param  loginTarget  The new sessionLoginTarget value
	 * @since
	 */
	public void setSessionLoginTarget( HttpSession session, String loginTarget )
	{
		session.setAttribute( LexConstants.LOGINTARGET_SESS_PARAM, loginTarget );
	}


	/**
	 *  Sets the displayMode attribute of the UserSessionManager object
	 *
	 * @param  session      The new displayMode value
	 * @param  displayMode  The new displayMode value
	 * @since
	 */
	public void setDisplayMode( HttpSession session, String displayMode )
	{
		session.setAttribute( LexConstants.DISPLAYMODE_SESS_ATTR, displayMode );
	}


	/**
	 *  Sets the entry attribute of the UserSessionManager object
	 *
	 * @return    The instance value
	 * @since
	 */
	/*
	    public void setEntry( HttpSession session, ITerm entry )
	    {
	    session.setAttribute( LexConstants.TERMENTRYBEAN_SESS_ATTR, entry );
	    }
	  */
//helper methods
	/**
	 *  Gets the instance attribute of the UserSessionManager class
	 *
	 * @return    The instance value
	 * @since
	 */
	public static UserSessionManager getInstance()
	{
		return INSTANCE;
	}


	/**
	 *  Gets the query attribute of the UserSessionManager object
	 *
	 * @param  session  Description of Parameter
	 * @return          The query value
	 * @since
	 */
	public LexQuery getQuery( HttpSession session )
	{
		Object query = session.getAttribute( LexConstants.QUERY_SESS_ATTR );
		if ( null == query || !( query instanceof LexQuery ) )
		{
			query = new LexQuery();
			session.setAttribute( LexConstants.QUERY_SESS_ATTR, query );
		}
		return (LexQuery) query;
	}


	/**
	 *  Gets the preferences attribute of the UserSessionManager object
	 *
	 * @param  session                     Description of Parameter
	 * @return                             The preferences value
	 * @exception  LexRepositoryException  Description of the Exception
	 * @exception  LexComponentException   Description of the Exception
	 * @since
	 */
	public Preferences getPreferences( HttpSession session ) throws LexRepositoryException, LexComponentException
	{
		Object sesAtt = session.getAttribute( LexConstants.PREFERENCES_SESS_ATTR );
		if ( null == sesAtt )
		{
			ThdlUser user = getSessionUser( session );
			setPreferences( session, new Preferences( user ) );
		}
		return (Preferences) session.getAttribute( LexConstants.PREFERENCES_SESS_ATTR );
	}


	/**
	 *  Gets the sessionUser attribute of the UserSessionManager object
	 *
	 * @param  session  Description of Parameter
	 * @return          The sessionUser value
	 * @since
	 */
	public ThdlUser getSessionUser( HttpSession session )
	{
		return (ThdlUser) session.getAttribute( LexConstants.USER_SESS_ATTR );
	}


	/**
	 *  Gets the sessionLoginTarget attribute of the UserSessionManager object
	 *
	 * @param  session  Description of Parameter
	 * @param  clear    Description of Parameter
	 * @return          The sessionLoginTarget value
	 * @since
	 */
	public String getSessionLoginTarget( HttpSession session, boolean clear )
	{
		String target = (String) session.getAttribute( LexConstants.LOGINTARGET_SESS_PARAM );
		if ( clear )
		{
			session.removeAttribute( LexConstants.LOGINTARGET_SESS_PARAM );
		}
		return target;
	}



	/**
	 *  Description of the Method
	 *
	 * @param  session  Description of Parameter
	 * @since
	 */
	public void removeSessionUser( HttpSession session )
	{
		session.removeAttribute( LexConstants.USER_SESS_ATTR );
		session.removeAttribute( LexConstants.PREFERENCES_SESS_ATTR );
	}



	/**
	 *  Description of the Method
	 *
	 * @param  request          Description of Parameter
	 * @param  response         Description of Parameter
	 * @param  url              Description of Parameter
	 * @exception  IOException  Description of Exception
	 * @since
	 */
	public static void doRedirect( HttpServletRequest request, HttpServletResponse response, String url ) throws IOException
	{
		String redirect = response.encodeRedirectURL( request.getContextPath() + url );
		response.sendRedirect( redirect );
	}


	//constructor
	/**
	 *  Constructor for the UserSessionManager object
	 *
	 * @since
	 */
	private UserSessionManager() { }
}

