package org.thdl.lex;
import java.sql.*;
import java.util.*;

import net.sf.hibernate.*;
import org.apache.log4j.*;

import org.thdl.lex.component.*;



/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 1, 2003
 */
public class LexComponentRepository
{

	/**
	 *  Description of the Field
	 */
	public final static String EXACT = "exact";
	/**
	 *  Description of the Field
	 */
	public final static String STARTS_WITH = "startsWith";
	/**
	 *  Description of the Field
	 */
	public final static String ANYWHERE = "anywhere";
	private static long start;

	private static long lastUpdate;


	/**
	 *  Sets the lastUpdate attribute of the LexComponentRepository class
	 *
	 * @param  last  The new lastUpdate value
	 */
	public static void setLastUpdate( long last )
	{
		lastUpdate = last;
	}


	/**
	 *  Gets the lastUpdate attribute of the LexComponentRepository class
	 *
	 * @return    The lastUpdate value
	 */
	public static long getLastUpdate()
	{
		return lastUpdate;
	}




	/**
	 *  Sets the start attribute of the LexComponentRepository object
	 *
	 * @param  startTime  The new start value
	 * @since
	 */
	private static void setStart( long startTime )
	{
		Logger logger = Logger.getLogger( "org.thdl.lex" );
		logger.debug( "Query start time: " + new java.util.Date( startTime ) );
		start = startTime;
	}



	/**
	 *  Gets the start attribute of the LexComponentRepository object
	 *
	 * @return    The start value
	 * @since
	 */
	private static long getStart()
	{
		return start;
	}


	/**
	 *  Gets the duration attribute of the LexComponentRepository class
	 *
	 * @return    The duration value
	 */
	private static long getDuration()
	{
		long duration = now() - getStart();

		Logger logger = Logger.getLogger( "org.thdl.lex" );
		logger.debug( "Query finish: " + new java.util.Date( now() ) );
		logger.debug( "Query duration in ms: " + duration );
		logger.info( "Query duration: " + duration / 1000 + " seconds." );
		return duration;
	}


	/**
	 *  Gets the session attribute of the LexComponentRepository class
	 *
	 * @return                         The session value
	 * @exception  HibernateException  Description of Exception
	 * @since
	 */
	private static Session getSession() throws HibernateException
	{
		Session session = HibernateSession.currentSession();
		if ( !session.isConnected() )
		{
//session.reconnect();
		}
		return session;
	}


	/**
	 *  Description of the Method
	 *
	 * @exception  LexRepositoryException  Description of the Exception
	 */
	private static void beginTransaction() throws LexRepositoryException
	{
		try
		{
			HibernateTransaction.beginTransaction();
		}
		catch ( HibernateException he )
		{
			throw new LexRepositoryException( he );
		}

	}


	/**
	 *  Description of the Method
	 *
	 * @param  commit                      Description of Parameter
	 * @exception  LexRepositoryException  Description of the Exception
	 * @since
	 */
	private static void endTransaction( boolean commit ) throws LexRepositoryException
	{
		try
		{
			HibernateTransaction.endTransaction( commit );
		}
		catch ( HibernateException he )
		{
			throw new LexRepositoryException( he );
		}
	}


	/**
	 *  Description of the Method
	 *
	 * @return    Description of the Returned Value
	 * @since
	 */
	private static long now()
	{
		return System.currentTimeMillis();
	}


	/**
	 *  Description of the Method
	 *
	 * @param  comp                        Description of Parameter
	 * @return                             Description of the Returned Value
	 * @exception  LexRepositoryException  Description of Exception
	 * @since
	 */
	private static ITerm assertTerm( ILexComponent comp ) throws LexRepositoryException
	{
		ITerm term = null;
		try
		{
			term = (ITerm) comp;
		}
		catch ( Exception e )
		{
			throw new LexRepositoryException( "Query Component was not a term." );
		}
		return term;
	}


	/**
	 *  Queries the database for Terms that start with the string in the term
	 *  property of the queryComponent. Sets entry property the first hit returned.
	 *
	 * @param  lexQuery                    Description of Parameter
	 * @exception  LexRepositoryException  Description of Exception
	 * @since
	 */
	public static void findTermsByTerm( LexQuery lexQuery ) throws LexRepositoryException
	{
		setStart( now() );
		beginTransaction();
		ITerm term = assertTerm( lexQuery.getQueryComponent() );
		if ( null == term.getTerm() )
		{
			throw new LexRepositoryException( "Query Component term was null." );
		}

		Query query = null;
		Iterator it = null;
		String termForQuery = null;
		if ( lexQuery.getFindMode().equals( LexComponentRepository.EXACT ) )
		{
			termForQuery = "'" + term.getTerm() + "'";
		}
		else if ( lexQuery.getFindMode().equals( LexComponentRepository.STARTS_WITH ) )
		{
			termForQuery = "'" + term.getTerm() + "%'";
		}
		else if ( lexQuery.getFindMode().equals( LexComponentRepository.ANYWHERE ) )
		{
			termForQuery = "'%" + term.getTerm() + "%'";
		}
		String queryString = " FROM org.thdl.lex.component.ITerm as term WHERE term.term like '" + term.getTerm() + "%' AND term.deleted=0 ORDER BY term.term";
		try
		{
			query = getSession().createQuery( queryString );
		}
		catch ( HibernateException he )
		{
			throw new LexRepositoryException( he );
		}

		try
		{
			it = query.iterate();
		}
		catch ( HibernateException he )
		{
			throw new LexRepositoryException( he );
		}

		if ( it.hasNext() )
		{
			term = (ITerm) it.next();
			lexQuery.setEntry( term );
			lexQuery.getResults().clear();
			lexQuery.getResults().put( term.getMetaId(), term.getTerm() );
		}
		else
		{
			lexQuery.setEntry( null );
			lexQuery.getResults().clear();
		}
		while ( it.hasNext() )
		{
			term = (ITerm) it.next();
			lexQuery.getResults().put( term.getMetaId(), term.getTerm() );
		}
		endTransaction( false );
		lexQuery.setDuration( getDuration() );
	}


	/**
	 *  Description of the Method
	 *
	 * @param  term                        Description of the Parameter
	 * @exception  LexRepositoryException  Description of Exception
	 * @since
	 */
	public static void loadTermByPk( ITerm term ) throws LexRepositoryException
	{
		try
		{
			beginTransaction();
			getSession().load( term, term.getMetaId() );
			endTransaction( false );
		}
		catch ( HibernateException he )
		{
			throw new LexRepositoryException( he );
		}
	}


	/**
	 *  Description of the Method
	 *
	 * @param  lexQuery                    Description of the Parameter
	 * @exception  LexRepositoryException  Description of the Exception
	 */
	public static void loadTermByPk( LexQuery lexQuery ) throws LexRepositoryException
	{
		beginTransaction();
		ITerm term = assertTerm( lexQuery.getQueryComponent() );
		loadTermByPk( term );
		lexQuery.setEntry( term );
		if ( !lexQuery.getResults().containsKey( term.getMetaId() ) )
		{
			lexQuery.getResults().put( term.getMetaId(), term.getTerm() );
		}
		endTransaction( false );
	}


	/**
	 *  Description of the Method
	 *
	 * @param  component                   Description of the Parameter
	 * @exception  LexRepositoryException  Description of the Exception
	 */
	public static void loadByPk( ILexComponent component ) throws LexRepositoryException
	{

		try
		{
			beginTransaction();
			getSession().load( component, component.getMetaId() );
			endTransaction( false );
		}
		catch ( HibernateException he )
		{
			throw new LexRepositoryException( he );
		}
	}


	/**
	 *  Description of the Method
	 *
	 * @param  component                   Description of the Parameter
	 * @param  pk                          Description of the Parameter
	 * @exception  LexRepositoryException  Description of the Exception
	 */
	public static void loadByPk( ILexComponent component, Integer pk ) throws LexRepositoryException
	{

		try
		{
			beginTransaction();
			getSession().load( component, pk );
			endTransaction( false );
		}
		catch ( HibernateException he )
		{
			throw new LexRepositoryException( he );
		}
	}


	/**
	 *  Gets the recentTerms attribute of the LexComponentRepository class
	 *
	 * @param  limit                       Description of the Parameter
	 * @return                             The recentTerms value
	 * @exception  LexRepositoryException  Description of the Exception
	 */
	public static List getRecentTerms( int limit ) throws LexRepositoryException
	{
		Query query = null;
		List results = null;
		String queryString = " FROM org.thdl.lex.component.ITerm ORDER BY modifiedOn DESC LIMIT " + limit;
		try
		{
			query = getSession().createQuery( queryString );
			results = query.list();
		}
		catch ( HibernateException he )
		{
			throw new LexRepositoryException( he );
		}
		return results;
	}


	/**
	 *  Description of the Method
	 *
	 * @param  component                   Description of the Parameter
	 * @exception  LexRepositoryException  Description of the Exception
	 */
	public static void save( ILexComponent component ) throws LexRepositoryException
	{

		try
		{
			beginTransaction();
			getSession().saveOrUpdate( component );
			endTransaction( true );
			setLastUpdate( now() );
		}
		catch ( HibernateException he )
		{
			throw new LexRepositoryException( he );
		}
	}


	/**
	 *  Description of the Method
	 *
	 * @param  component                   Description of the Parameter
	 * @exception  LexRepositoryException  Description of the Exception
	 */
	public static void update( ILexComponent component ) throws LexRepositoryException
	{

		try
		{
			beginTransaction();
			getSession().update( component );
			endTransaction( true );
		}
		catch ( HibernateException he )
		{
			throw new LexRepositoryException( he );
		}
	}


	/**
	 *  Description of the Method
	 *
	 * @param  component                   Description of the Parameter
	 * @exception  LexRepositoryException  Description of the Exception
	 */
	public static void refresh( ILexComponent component ) throws LexRepositoryException
	{

		try
		{
			beginTransaction();
			getSession().refresh( component );
			endTransaction( true );
		}
		catch ( HibernateException he )
		{
			throw new LexRepositoryException( he );
		}
	}


	/**
	 *  Description of the Method
	 *
	 * @exception  LexRepositoryException  Description of Exception
	 * @since
	 */
	public static void cleanup() throws LexRepositoryException
	{
		try
		{
			endTransaction( false );
			HibernateSession.closeSession();
		}
		catch ( HibernateException he )
		{
			throw new LexRepositoryException( he );
		}
	}
}

