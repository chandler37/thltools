
package org.thdl.roster.om;


import org.apache.torque.om.Persistent;
import org.apache.torque.*;

/** 
 * The skeleton for this class was autogenerated by Torque on:
 *
 * [Thu Feb 27 15:11:05 EST 2003]
 *
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public  class Address 
    extends org.thdl.roster.om.BaseAddress
    implements java.io.Serializable, Persistent
{
	public Address() throws TorqueException
	{
		super();
		setCountry( new Country() );
	}
}
