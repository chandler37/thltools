package org.thdl.roster.components.forms;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IComponent;
import org.thdl.roster.Visit;
import org.thdl.roster.om.*;

public class OrganizationFields extends BaseComponent
{
//attributes
    private OrganizationData organizationDataBean;
//accessors
	public void setOrganizationDataBean(OrganizationData organizationDataBean) {
		this.organizationDataBean = organizationDataBean;
	}
	public OrganizationData getOrganizationDataBean() {
		return organizationDataBean;
	}
//synthetic attribute accessors
}