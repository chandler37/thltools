package org.thdl.roster.components.forms;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IComponent;
import org.thdl.roster.Visit;
import org.thdl.roster.om.Phone;

public class PhoneFields extends BaseComponent
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
//synthetic attribute accessors
}