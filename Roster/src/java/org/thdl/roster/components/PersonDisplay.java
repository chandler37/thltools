package org.thdl.roster.components;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IComponent;
import org.thdl.roster.Visit;
import org.thdl.roster.om.*;

public class PersonDisplay extends BaseComponent
{
//attributes
    private Person personBean;
//accessors
	public void setPersonBean(Person personBean) {
		this.personBean = personBean;
	}
	public Person getPersonBean() {
		return personBean;
	}
//synthetic attribute accessors
}