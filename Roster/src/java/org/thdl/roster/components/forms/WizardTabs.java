package org.thdl.roster.components.forms;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IComponent;
import org.thdl.roster.Visit;

public class WizardTabs extends BaseComponent
{
//attributes
	private String pageName;
//accessors
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public String getPageName() {
		return pageName;
	}
	public String getHref() {
		return "javascript:tabbedSubmit( '" + getPageName() + "' )";
	}

/*     private String[] pageNames;
    private String[] pageLabels;
	 private int index;
//accessors
	public void setPageNames(String[] pageNames) {
		this.pageNames = pageNames;
	}
	public void setPageLabels(String[] pageLabels) {
		this.pageLabels = pageLabels;
	}
	public String[] getPageNames() {
		return pageNames;
	}
	public String[] getPageLabels() {
		return pageLabels;
	}
	public String getPageName( int index ) {
		return pageNames[ index ];
	}
	public String getPageLabels( int index ) {
		return pageLabels[ index ];
	}
	public String getPageName() {
		return pageNames[ getIndex() ];
	}
	public String getPageLabels() {
		return pageLabels[ getIndex() ];
	}

	public void setIndex(int index) {
		this.index = index;
	}
	public int getIndex() {
		return index;
	} */
//synthetic attribute accessors


    public boolean getDisablePageLink() {
        return getPageName().equals( getPage().getPageName() );
    }
    public String getClassAttribute() {
        String classAttribute = null;
		  if ( getDisablePageLink() )
		  {
			  classAttribute="activeTab";
		  }
		  else
		  {
			  classAttribute="tab";
		  }
		  return classAttribute;
    }
}