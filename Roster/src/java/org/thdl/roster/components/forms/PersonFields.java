package org.thdl.roster.components.forms;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IComponent;
import org.thdl.roster.Visit;
import org.thdl.roster.om.*;

public class PersonFields extends BaseComponent
{
//attributes
    private PersonData personDataBean;
//accessors
	public void setPersonDataBean(PersonData personDataBean) {
		this.personDataBean = personDataBean;
	}
	public PersonData getPersonDataBean() {
		return personDataBean;
	}
//synthetic attribute accessors
}