package org.thdl.roster.om;

import org.apache.torque.om.Persistent;
import org.apache.torque.*;
import org.apache.torque.util.*;
import java.util.*;

/** 
 * The skeleton for this class was autogenerated by Torque on:
 *
 * [Thu Feb 27 15:11:05 EST 2003]
 *
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */

public  class PersonData 
    extends org.thdl.roster.om.BasePersonData
    implements RosterMemberData,  java.io.Serializable
{
	//attributes
	private List personTypeIdList;
	//accessors
	public void setId(Integer v) throws TorqueException
	{
		super.setId( v );
		setPersonTypeIdList( buildIdList() );
	}
	public void setPersonTypeIdList( List personTypeIdList )
	{
		this.personTypeIdList = personTypeIdList;
	}
	public List getPersonTypeIdList() throws TorqueException
	{
		return personTypeIdList;
	}
	//helpers
	public String getName()
	{
		String name = null;
		if ( null != getLastname() )
		{
			name = getLastname();
		}
		if ( null != getFirstname() )
		{
			if ( null != name )
			{
				name = getFirstname() + " " + name;
			}
			else
			{
				name = getFirstname();
			}
		}
		return name;
	}

	public String getDescription()
	{
		return getBio();
	}

	public List buildIdList() throws TorqueException
	{
			Criteria crit = new Criteria();
			crit.add(  PersonPersonTypePeer.PERSON_DATA_ID, getId() );
			crit.addAscendingOrderByColumn( PersonPersonTypePeer.RELEVANCE );
			List idSourceList = PersonPersonTypePeer.doSelect( crit );
			LinkedList newList = new LinkedList();
			ListIterator iterator = idSourceList.listIterator( 0 );
			while ( iterator.hasNext() )
			{
				PersonPersonType ppt = (PersonPersonType) iterator.next();
				Integer id = ppt.getPersonType().getId();
				newList.add( id );
			}
			return newList;
	}
	public java.util.List getMemberTypes() throws org.apache.torque.TorqueException
	{
		return getPersonPersonTypes();
	}
	public void setMemberTypes( java.util.List memberTypes )
	{
		// WRONG setPersonPersonTypes( memberTypes );
	}
}