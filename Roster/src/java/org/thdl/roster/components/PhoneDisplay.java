package org.thdl.roster.components;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IComponent;
import org.thdl.roster.Visit;
import org.thdl.roster.om.Phone;

public class PhoneDisplay extends BaseComponent
{
//attributes
    private Phone phoneBean;
//accessors
	public void setPhoneBean(Phone phoneBean) {
		this.phoneBean = phoneBean;
	}
	public Phone getPhoneBean() {
		return phoneBean;
	}
	public boolean isDisplayWorthy()
	{
		//return ( null != getPhoneBean().getCountryCode() && null != getPhoneBean().getAreaCode() && null != getPhoneBean().getNumber() );
		return ( null != getPhoneBean().getNumber() );
	}
//synthetic attribute accessors
}