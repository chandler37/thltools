package org.thdl.roster.components;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IComponent;
import org.thdl.roster.Visit;
import org.thdl.roster.om.*;

public class ProjectDisplay extends BaseComponent
{
//attributes
    private Project projectBean;
//accessors
	public void setProjectBean(Project projectBean) {
		this.projectBean = projectBean;
	}
	public Project getProjectBean() {
		return projectBean;
	}
//synthetic attribute accessors
}