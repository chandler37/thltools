package org.thdl.roster.components;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IComponent;
import org.thdl.roster.Visit;

public class Border extends BaseComponent
{
//attributes
    private String pageName;
//accessors
    public void setPageName(String value) {
        pageName = value;
    }
    public String getPageName() {
        return pageName;
    }
//synthetic attribute accessors
    public boolean getDisablePageLink() {
        return pageName.equals( getPage().getPageName() );
    }
}