package org.thdl.roster;

import org.apache.tapestry.engine.BaseEngine;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.torque.*;
import org.apache.torque.util.Criteria;
import java.util.*;
import java.io.*;
import javax.servlet.ServletException;
import java.util.ResourceBundle;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.form.EnumPropertySelectionModel;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.contrib.palette.SortMode;
import org.thdl.roster.Visit;
import org.thdl.roster.om.*;

public class RosterEngine extends BaseEngine
{
	private static final String[] PAGE_NAMES = { "Home", "Search", "People", "Organizations", "Projects", "Login" };
	private static final String[] PERSON_WIZARD_PAGES= { "Contact", "Background", "Activities", "Works", "Uploads" };
	
	 public String[] getPageNames()
	 {
		  return PAGE_NAMES;
	 }
	 public String[] getPersonWizardPages()
	 {
		  return PERSON_WIZARD_PAGES;
	 }
	public boolean service(RequestContext context) throws ServletException, IOException
	{
		 context.getRequest().setCharacterEncoding("UTF-8");
	
		 return super.service(context);
	}

}