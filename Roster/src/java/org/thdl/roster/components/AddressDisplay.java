package org.thdl.roster.components;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IComponent;
import org.thdl.roster.Visit;
import org.thdl.roster.om.Address;

public class AddressDisplay extends BaseComponent
{
//attributes
    private Address addressBean;
//accessors
	public void setAddressBean(Address addressBean) {
		this.addressBean = addressBean;
	}
	public Address getAddressBean() throws org.apache.torque.TorqueException
	{
		return addressBean;
	}
//synthetic attribute accessors
}