package org.thdl.lex;
import java.util.*;
import javax.servlet.http.*;

import org.apache.log4j.*;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 9, 2003
 */
public class LexLogger
{
	private final static Logger LOGGER = Logger.getLogger( "org.thdl.lex" );


	/**
	 *  Description of the Method
	 *
	 * @param  req  Description of the Parameter
	 */
	public static void logRequestState( HttpServletRequest req )
	{

		Iterator it;
		Enumeration enum = req.getParameterNames();
		while ( enum.hasMoreElements() )
		{
			String parm = (String) enum.nextElement();
			LOGGER.debug( "Request Parameter " + parm + " = " + req.getParameter( parm ) );
		}
		enum = req.getAttributeNames();
		while ( enum.hasMoreElements() )
		{
			String att = (String) enum.nextElement();
			LOGGER.debug( "Request Attribute " + att + " = " + req.getAttribute( att ) );
		}
	}


	/**
	 *  Description of the Method
	 *
	 * @param  req  Description of the Parameter
	 */
	public static void logSessionState( HttpServletRequest req )
	{
		HttpSession ses = req.getSession( false );
		if ( null == ses )
		{
			LOGGER.debug( "Session was null" );
			return;
		}
		Enumeration enum = ses.getAttributeNames();
		while ( enum.hasMoreElements() )
		{
			String att = (String) enum.nextElement();
			LOGGER.debug( "Session Attribute " + att + " = " + ses.getAttribute( att ) );
		}
		LexQuery query = (LexQuery) ses.getAttribute( "query" );
		if ( null == query )
		{
			return;
		}
		LOGGER.debug( "Query Entry: " + query.getEntry() );
		LOGGER.debug( "Query QueryComponent: " + query.getQueryComponent() );
		LOGGER.debug( "Query UpdateComponent: " + query.getUpdateComponent() );
		LOGGER.debug( "Query Results, " + query.getResults() + ", contain: " + query.getResults().values() + "\n" );
	}


	/**
	 *  Description of the Method
	 *
	 * @param  msg  Description of the Parameter
	 */
	public static void info( String msg )
	{
		LOGGER.info( msg );
	}


	/**
	 *  Description of the Method
	 *
	 * @param  msg  Description of the Parameter
	 */
	public static void debug( String msg )
	{
		LOGGER.debug( msg );
	}


	/**
	 *  Description of the Method
	 *
	 * @param  msg  Description of the Parameter
	 */
	public static void warn( String msg )
	{
		LOGGER.warn( msg );
	}

}

