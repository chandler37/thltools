package org.thdl.lex;

import org.thdl.users.ThdlUserRepository;

import java.util.*;
import java.io.*;

import org.dlese.dpc.oai.harvester.*;
import org.dlese.dpc.datamgr.*;
import org.dlese.dpc.index.*;

import org.dlese.dpc.oai.harvester.structs.ScheduledHarvest;

import org.openarchives.oai.x20.oaiDc.DcDocument;
import org.openarchives.oai.x20.oaiDc.OaiDcType;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    December 15, 2003
 */
public class LexSourceRepository
{
//attributes
	private static LexSourceRepository instance;
	private ScheduledHarvestManager harvestManager;
	private ScheduledHarvest harvest;

	private String oaiServer;
	private String oaiMetadataPrefix;
	private String oaiHome;
	private String oaiLocalCopy;
	private int oaiRefreshDelay;


	/**
	 *  Sets the oaiServer attribute of the LexSourceRepository object
	 *
	 * @param  oaiServer  The new oaiServer value
	 */
	public void setOaiServer( String oaiServer )
	{
		this.oaiServer = oaiServer;
	}


	/**
	 *  Sets the oaiMetadataPrefix attribute of the LexSourceRepository object
	 *
	 * @param  oaiMetadataPrefix  The new oaiMetadataPrefix value
	 */
	public void setOaiMetadataPrefix( String oaiMetadataPrefix )
	{
		this.oaiMetadataPrefix = oaiMetadataPrefix;
	}


	/**
	 *  Sets the oaiHome attribute of the LexSourceRepository object
	 *
	 * @param  oaiHome  The new oaiHome value
	 */
	public void setOaiHome( String oaiHome )
	{
		this.oaiHome = oaiHome;
	}


	/**
	 *  Sets the oaiLocalCopy attribute of the LexSourceRepository object
	 *
	 * @param  oaiLocalCopy  The new oaiLocalCopy value
	 */
	public void setOaiLocalCopy( String oaiLocalCopy )
	{
		this.oaiLocalCopy = oaiLocalCopy;
	}


	/**
	 *  Sets the oaiRefreshDelay attribute of the LexSourceRepository object
	 *
	 * @param  oaiRefreshDelay  The new oaiRefreshDelay value
	 */
	public void setOaiRefreshDelay( int oaiRefreshDelay )
	{
		this.oaiRefreshDelay = oaiRefreshDelay;
	}


	/**
	 *  Gets the oaiServer attribute of the LexSourceRepository object
	 *
	 * @return    The oaiServer value
	 */
	public String getOaiServer()
	{
		return oaiServer;
	}


	/**
	 *  Gets the oaiMetadataPrefix attribute of the LexSourceRepository object
	 *
	 * @return    The oaiMetadataPrefix value
	 */
	public String getOaiMetadataPrefix()
	{
		return oaiMetadataPrefix;
	}


	/**
	 *  Gets the oaiHome attribute of the LexSourceRepository object
	 *
	 * @return    The oaiHome value
	 */
	public String getOaiHome()
	{
		return oaiHome;
	}


	/**
	 *  Gets the oaiLocalCopy attribute of the LexSourceRepository object
	 *
	 * @return    The oaiLocalCopy value
	 */
	public String getOaiLocalCopy()
	{
		return oaiLocalCopy;
	}


	/**
	 *  Gets the oaiRefreshDelay attribute of the LexSourceRepository object
	 *
	 * @return    The oaiRefreshDelay value
	 */
	public int getOaiRefreshDelay()
	{
		return oaiRefreshDelay;
	}


	/**
	 *  Sets the harvest attribute of the LexSourceRepository object
	 *
	 * @param  harvest  The new harvest value
	 */
	public void setHarvest( ScheduledHarvest harvest )
	{
		this.harvest = harvest;
	}


	/**
	 *  Gets the harvest attribute of the LexSourceRepository object
	 *
	 * @return    The harvest value
	 */
	public ScheduledHarvest getHarvest()
	{
		return harvest;
	}



	/**
	 *  Sets the harvestManager attribute of the LexSourceRepository object
	 *
	 * @param  harvestManager  The new harvestManager value
	 */
	public void setHarvestManager( ScheduledHarvestManager harvestManager )
	{
		this.harvestManager = harvestManager;
	}


	/**
	 *  Gets the harvestManager attribute of the LexSourceRepository object
	 *
	 * @return    The harvestManager value
	 */
	public ScheduledHarvestManager getHarvestManager()
	{
		return harvestManager;
	}


//accessors
	/**
	 *  Gets the instance attribute of the LexSourceRepository class
	 *
	 * @return                The instance value
	 * @exception  Exception  Description of the Exception
	 */
	public static LexSourceRepository getInstance() throws Exception
	{
		if ( null == instance )
		{
			instance = new LexSourceRepository();
		}
		return instance;
	}


	/**
	 *  Description of the Method
	 *
	 * @exception  LexRepositoryException  Description of the Exception
	 */
	public void refreshSources() throws LexRepositoryException
	{
		/*
		    try
		    {
		    getHarvest().
		    }
		    catch ( Hexception h )
		    {
		    throw new LexRepositoryException( h );
		    }
		    catch ( OAIErrorException h )
		    {
		    throw new LexRepositoryException( h );
		    }
		  */
	}


	/**
	 *  Description of the Method
	 *
	 * @return                             Description of the Return Value
	 * @exception  LexRepositoryException  Description of the Exception
	 */
	public String[] xmlTesting() throws LexRepositoryException
	{
		String[] sa = null;
		try
		{
			File file = new File( "/Users/travis/webapps/lex/dlese-oai/datastore.lib.virginia.edu/tibet/spt/SPT--OAI.php/oai_dc", "oai%3Alib.virginia.edu%3Athdl-267.xml" );
			DcDocument dcd = DcDocument.Factory.parse( file );
			OaiDcType oaiDc = dcd.getDc();
			sa = new String[oaiDc.getTitleArray().length];
			for ( int i = 0; i < sa.length; i++ )
			{
				sa[i] = oaiDc.getTitleArray()[i].toString();
			}
		}
		catch ( Exception e )
		{
			throw new LexRepositoryException( e );
		}
		return sa;
	}
//constructors

	/**
	 *Constructor for the LexSourceRepository object
	 *
	 * @exception  Exception  Description of the Exception
	 */
	private LexSourceRepository() throws Exception
	{
		SimpleDataStore dataStore = new SimpleDataStore( "/Users/travis/webapps/lex/dlese-oai/scheduled-harvester", true );
		File initialHarvestDir = new File( "/Users/travis/webapps/lex/dlese-oai/datastore.lib.virginia.edu" );
		SimpleLuceneIndex harvestLogIndex = new SimpleLuceneIndex( "/Users/travis/webapps/lex/dlese-oai/lucene-index" );
		ScheduledHarvestManager manager = new ScheduledHarvestManager( dataStore, initialHarvestDir, harvestLogIndex );
		setHarvestManager( manager );

		String repositoryName = "SPT";
		String setSpec = "";
		String baseURL = "http://datastore.lib.virginia.edu/tibet/spt/SPT--OAI.php";
		String metadataPrefix = "oai_dc";
		int seconds = 1000 * 1;
		String harvestingInterval = Integer.toString( seconds );
		String intervalGranularity = "YYYY -MM-DDThh:mm:ssZ";
		String enabledDisabled = "enabled";
		ScheduledHarvest harvest = new ScheduledHarvest( repositoryName, setSpec, baseURL, metadataPrefix, harvestingInterval, intervalGranularity, enabledDisabled );
		setHarvest( harvest );

		getHarvestManager().addScheduledHarvest( getHarvest() );
	}
//main

	/**
	 *  The main program for the LexSourceRepository class
	 *
	 * @param  args  The command line arguments
	 */
	public static void main( String[] args )
	{
	}
}

