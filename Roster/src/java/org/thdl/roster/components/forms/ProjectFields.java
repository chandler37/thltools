package org.thdl.roster.components.forms;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IComponent;
import org.thdl.roster.Visit;
import org.thdl.roster.om.*;

public class ProjectFields extends BaseComponent
{
//attributes
    private ProjectData projectDataBean;
//accessors
	public void setProjectDataBean(ProjectData projectDataBean) {
		this.projectDataBean = projectDataBean;
	}
	public ProjectData getProjectDataBean() {
		return projectDataBean;
	}
//synthetic attribute accessors
}