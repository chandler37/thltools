package org.thdl.lex;

import java.util.List;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 20, 2003
 */
public class Global
{
	private static long refreshDelay;
	private static int recentUpdatesCount;
	private static long lastRefresh;
	private int entryCount;
	private List recentUpdates;
	private boolean requiresRefresh;


	/**
	 *  Sets the requiresRefresh attribute of the Global object
	 *
	 * @param  requiresRefresh  The new requiresRefresh value
	 */
	public void setRequiresRefresh( boolean requiresRefresh )
	{
		this.requiresRefresh = requiresRefresh;
	}


	/**
	 *  Gets the requiresRefresh attribute of the Global object
	 *
	 * @return    The requiresRefresh value
	 */
	public boolean getRequiresRefresh()
	{
		return requiresRefresh;
	}


	/**
	 *  Sets the recentUpdatesCount attribute of the Global object
	 *
	 * @param  recentUpdatesCount  The new recentUpdatesCount value
	 */
	public void setRecentUpdatesCount( int recentUpdatesCount )
	{
		this.recentUpdatesCount = recentUpdatesCount;
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
	 *  Gets the recentUpdatesCount attribute of the Global object
	 *
	 * @return    The recentUpdatesCount value
	 */
	public int getRecentUpdatesCount()
	{
		return recentUpdatesCount;
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
	 *  Sets the recentUpdates attribute of the Global object
	 *
	 * @param  recentUpdates  The new recentUpdates value
	 */
	public void setRecentUpdates( List recentUpdates )
	{
		this.recentUpdates = recentUpdates;
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
	 *  Gets the recentUpdates attribute of the Global object
	 *
	 * @return                             The recentUpdates value
	 * @exception  LexRepositoryException  Description of the Exception
	 */
	public List getRecentUpdates() throws LexRepositoryException
	{
		if ( null == recentUpdates )
		{
			refresh( getRecentUpdatesCount() );
		}
		return recentUpdates;
	}
//helpers

	/**
	 *  Description of the Method
	 *
	 * @param  limit                       Description of the Parameter
	 * @exception  LexRepositoryException  Description of the Exception
	 */
	public void refresh( int limit ) throws LexRepositoryException
	{
		long now = System.currentTimeMillis();
		if ( now - getLastRefresh() > getRefreshDelay() )
		{
			setRecentUpdates( LexComponentRepository.getRecentTerms( limit ) );
		}
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
		setRecentUpdatesCount( recentItems );
		setRefreshDelay( refreshDelay );
	}
}

