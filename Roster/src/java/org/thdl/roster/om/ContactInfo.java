
package org.thdl.roster.om;


import org.apache.torque.om.Persistent;
import org.apache.torque.TorqueException;

/** 
 * The skeleton for this class was autogenerated by Torque on:
 *
 * [Thu Feb 27 15:11:05 EST 2003]
 *
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public  class ContactInfo 
    extends org.thdl.roster.om.BaseContactInfo
    implements java.io.Serializable, Persistent
{
	public Address getAddress() throws TorqueException
	{
		if ( null == super.getAddress() )
		{
			super.setAddress( new Address() );
		}
		return super.getAddress();
	}
	public ContactInfo() throws TorqueException
	{
		super();
		setAddress( new Address() );
		setPhoneRelatedByPhone( new Phone() );
		setPhoneRelatedByFax( new Phone() );
	}
}
