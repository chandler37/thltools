package org.thdl.roster.components;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IComponent;
import org.thdl.roster.Visit;
import org.thdl.roster.om.*;

public class OrganizationDisplay extends BaseComponent
{
//attributes
    private Organization organizationBean;
//accessors
	public void setOrganizationBean(Organization organizationBean) {
		this.organizationBean = organizationBean;
	}
	public Organization getOrganizationBean() {
		return organizationBean;
	}
//synthetic attribute accessors
}