package org.thdl.lex;
import java.io.*;
import java.util.*;
import org.thdl.lex.component.*;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 20, 2003
 */
public class Global
{
	private static long refreshDelay;
	private static int recentTermsCount;
	private static long lastRefresh;
	private int entryCount;
	private List recentTerms;


	/**
	 *  Gets the lastUpdateAsDate attribute of the LexComponentRepository class
	 *
	 * @return                             The lastUpdateAsDate value
	 * @exception  LexRepositoryException  Description of the Exception
	 */
	public Date getLastUpdate() throws LexRepositoryException
	{
		Date date = null;
		if ( null != getRecentTerms() && getRecentTerms().size() > 0 )
		{
			ITerm term = (ITerm) getRecentTerms().get( 0 );
			date = term.getMeta().getModifiedOn();
		}
		return date;
	}


	/**
	 *  Sets the recentTermsCount attribute of the Global object
	 *
	 * @param  recentTermsCount  The new recentTermsCount value
	 */
	public void setRecentTermsCount( int recentTermsCount )
	{
		this.recentTermsCount = recentTermsCount;
	}


	/**
	 *  Sets the refreshDelay attribute of the Global object
	 *
	 * @param  refreshDelay  The new refreshDelay value
	 */
	public void setRefreshDelay( long refreshDelay )
	{
		this.refreshDelay = refreshDelay;
	}


	/**
	 *  Gets the recentTermsCount attribute of the Global object
	 *
	 * @return    The recentTermsCount value
	 */
	public int getRecentTermsCount()
	{
		return recentTermsCount;
	}


	/**
	 *  Gets the refreshDelay attribute of the Global object
	 *
	 * @return    The refreshDelay value
	 */
	public long getRefreshDelay()
	{
		return refreshDelay;
	}


	/**
	 *  Sets the entryCount attribute of the Global object
	 *
	 * @param  entryCount  The new entryCount value
	 */
	public void setEntryCount( int entryCount )
	{
		this.entryCount = entryCount;
	}


	/**
	 *  Sets the lastRefresh attribute of the Global object
	 *
	 * @param  lastRefresh  The new lastRefresh value
	 */
	public void setLastRefresh( long lastRefresh )
	{
		this.lastRefresh = lastRefresh;
	}


	/**
	 *  Sets the recentTerms attribute of the Global object
	 *
	 * @param  recentTerms  The new recentTerms value
	 */
	public void setRecentTerms( List recentTerms )
	{
		this.recentTerms = recentTerms;
		setLastRefresh( System.currentTimeMillis() );
	}


	/**
	 *  Gets the lastRefresh attribute of the Global object
	 *
	 * @return    The lastRefresh value
	 */
	public long getLastRefresh()
	{
		return lastRefresh;
	}


	/**
	 *  Gets the recentTerms attribute of the Global object
	 *
	 * @return                             The recentTerms value
	 * @exception  LexRepositoryException  Description of the Exception
	 */
	public List getRecentTerms() throws LexRepositoryException
	{
		if ( null == recentTerms )
		{
			doRefresh();
		}
		return recentTerms;
	}
//helpers

	/**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
	public boolean requiresRefresh()
	{
		boolean requiresRefresh = false;
		long now = System.currentTimeMillis();
		long lastUpdate = LexComponentRepository.getLastUpdate();
		long sinceLastRefresh = now - getLastRefresh();
		if ( sinceLastRefresh > getRefreshDelay() && lastUpdate > getLastRefresh() )
		{
			requiresRefresh = true;
		}
		return requiresRefresh;
	}


	/**
	 *  Description of the Method
	 *
	 * @exception  LexRepositoryException  Description of the Exception
	 */
	public void refresh() throws LexRepositoryException
	{
		if ( requiresRefresh() )
		{
			doRefresh();
		}
	}


	/**
	 *  Description of the Method
	 *
	 * @exception  LexRepositoryException  Description of the Exception
	 */
	public void doRefresh() throws LexRepositoryException
	{
		GlobalRefresher gr = new GlobalRefresher();
		new Thread( gr ).start();
	}


	/**
	 *Constructor for the Global object
	 */
	public Global() { }


	/**
	 *Constructor for the Global object
	 *
	 * @param  refreshDelay  Description of the Parameter
	 * @param  recentItems   Description of the Parameter
	 */
	public Global( int recentItems, long refreshDelay )
	{
		setRecentTermsCount( recentItems );
		setRefreshDelay( refreshDelay );
	}


	/**
	 *  Description of the Class
	 *
	 * @author     travis
	 * @created    October 21, 2003
	 */
	class GlobalRefresher implements Runnable
	{
		/**
		 *  Main processing method for the GlobalRefresher object
		 */
		public void run()
		{
			int limit = getRecentTermsCount();
			try
			{
				setRecentTerms( LexComponentRepository.getRecentTerms( limit ) );
			}
			catch ( Exception e )
			{
				StringWriter writer = new StringWriter();
				e.printStackTrace( new PrintWriter( writer ) );
				String stackTrace = writer.getBuffer().toString();
				LexLogger.error( "GlobalRefresher Thread caught an Exception: " + stackTrace );
			}

		}


		/**
		 *Constructor for the GlobalRefresher object
		 */
		GlobalRefresher() { }
	}
}

