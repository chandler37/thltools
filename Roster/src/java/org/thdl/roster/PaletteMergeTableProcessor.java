package org.thdl.roster;

import java.util.*;
import java.text.*;
import org.apache.tapestry.*;
import org.apache.torque.*;
import org.apache.torque.util.*;
import org.apache.torque.om.*;
import org.thdl.roster.om.*;

/**
 *		This is a utility class for processing input from Tapestry Palette Components. 
 *		Palettes are user-sortable multiple-select javascript widgets.
 *		The data collected are stored in Torque OR Objects that represent rows of a basic merge table. 
 *		This class works in conjunction with torque objects that implement the RosterMergeData interface.
 *		The merge table should also include field called "relevance" to store the user-specified sort order  
 *
 *@author     travis
 *@created    June 17, 2003
 */
public class PaletteMergeTableProcessor
{
	/**
	 *  This is the utility method that processes data from the Palette. The examples here all assume that merge data relationship is PersonData::PersonPersonTypes.
	 *
	 *@param  flatDataIds                      This is a list of Integers which are primary keys of two-column flat data tables ( PersonType.id, PersonType.personType ) that are used in palette select boxes.
	 *@param  torqueObjects                    This is a list of Torque Objects that represent rows of the Merge Table (e.g. PersonPersonType )
	 *@param  memberDataId                     This is an Integer primary key of the single MemberData (e.g. PersonData) parent for the multiple rows of merge data.
	 *@param  template                         This is a prototype Torque merge data object passed in to make copies from. (e.g. PersonPersonType, or ProjectProjectType ).
	 *@exception  TorqueException              Description of the Exception
	 *@exception  ApplicationRuntimeException  Description of the Exception
	 */
	public static void processPalette( List flatDataIds, List torqueObjects, Integer memberDataId, RosterMergeData template ) throws TorqueException, ApplicationRuntimeException
	{
		//creative loop
		ListIterator flatDataIdsIterator = null;
		if ( null != flatDataIds )
		{
			flatDataIdsIterator = flatDataIds.listIterator( 0 );
		}
	
		while ( null != flatDataIdsIterator && flatDataIdsIterator.hasNext() )
		{
			Integer flatDataId = (Integer) flatDataIdsIterator.next();
			RosterMergeData torqueObject = null;

			ListIterator torqueObjectsIterator = torqueObjects.listIterator( 0 );
			while ( torqueObjectsIterator.hasNext() )
			{
				RosterMergeData mergeData = (RosterMergeData) torqueObjectsIterator.next();
				Integer flat = (Integer) mergeData.getByPosition( 2 );
				if ( flatDataId.equals( flat ) )
				{
					torqueObject = mergeData;
				}
			}

			if ( null == torqueObject )
			{
				try
				{
					torqueObject = (RosterMergeData) template.getClass().newInstance();
					torqueObject.setByPosition( 2, flatDataId );
					torqueObject.setByPosition( 3, memberDataId );
				}
				catch ( Exception e )
				{
					throw new ApplicationRuntimeException( e.getMessage(), e );
				}
			}
			try
			{
				Integer relevance = new Integer( flatDataIdsIterator.previousIndex() );
				torqueObject.setByPosition( 4, relevance );
				torqueObject.save();
			}
			catch ( Exception e )
			{
				throw new ApplicationRuntimeException( e.getMessage(), e );
			}
		}
		//destructive loop
		ListIterator torqueObjectsIterator = torqueObjects.listIterator( 0 );
		while ( torqueObjectsIterator.hasNext() )
		{
			RosterMergeData mergeObject = (RosterMergeData) torqueObjectsIterator.next();
			boolean match = false;

			if ( null != flatDataIds )
			{
				flatDataIdsIterator = flatDataIds.listIterator( 0 );
				while ( flatDataIdsIterator.hasNext() )
				{
					Integer flatDataId = (Integer) flatDataIdsIterator.next();
					if ( flatDataId.equals( mergeObject.getByPosition( 2 ) ) )
					{
						match = true;
						break;
					}
				}
				if ( match == false )
				{
					try
					{
						mergeObject.remove();
					}
					catch ( Exception e )
					{
						throw new ApplicationRuntimeException( "destruction loop says: " + e.getMessage(), e );
					}
				}
			}
			else
			{
				try
				{
					mergeObject.remove();
				}
				catch ( Exception e )
				{
					throw new ApplicationRuntimeException( "destruction loop says: " + e.getMessage(), e );
				}
			}
		}
	}

//main

	/**
	 *  The main program for the PaletteMergeTableProcessor class
	 *
	 *@param  args  The command line arguments
	 */
	public static void main( String[] args )
	{
		try
		{
			/*
			 *  java.io.InputStream stream = Torque.class.getClassLoader().getResourceAsStream("org/thdl/roster/roster-torque.properties");
			 *  org.apache.commons.configuration.PropertiesConfiguration config = new org.apache.commons.configuration.PropertiesConfiguration();
			 *  config.load( stream );
			 */
			Torque.init( "./roster-torque.properties" );

			Member person = MemberPeer.retrieveByPK( new Integer( 1020 ) );
			List flatDataIds = new LinkedList();
			flatDataIds.add( new Integer( 1 ) );
			flatDataIds.add( new Integer( 2 ) );
			flatDataIds.add( new Integer( 3 ) );
			List torqueObjects = person.getPersonData().getPersonPersonTypes();
			Integer memberDataId = person.getPersonData().getId();
			PersonPersonType template = new PersonPersonType();
			//PaletteMergeTableProcessor.processPalette( flatDataIds, torqueObjects, memberDataId, template );

			System.out.println( MemberPeer.executeQuery( "select count( id ) from PersonPersonType" ) );

			/*
			 *  flatDataIds = new LinkedList();
			 *  flatDataIds.add( new Integer( 4 ) );
			 *  flatDataIds.add( new Integer( 5 ) );
			 *  flatDataIds.add( new Integer( 6 ) );
			 *  torqueObjects = person.getPersonData().getPersonPersonTypes();
			 *  PaletteMergeTableProcessor.processPalette( flatDataIds, torqueObjects, memberDataId, template );
			 *  System.out.println( MemberPeer.executeQuery( "select count( id ) from PersonPersonType" ) );
			 */
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

}

