package org.thdl.roster.pages.forms;

import org.apache.tapestry.*;
import org.thdl.roster.*;
import org.thdl.roster.om.*;
import org.thdl.roster.pages.*;
import org.apache.torque.*;

public abstract class SecureRosterPage extends RosterPage
{
    /* DO NOT DELETE:  THIS IS A HANDY TESTING HACK TO SEE SOME OF THE OBJECTS IN VISIT
	 public String getMessage()
    {
        Visit visit = (Visit)getVisit();
		  String snapshot=null;
		  try {
			  snapshot = visit.getSnapshot();
		  }
		  catch (TorqueException te )
		  {
			  throw new ApplicationRuntimeException( te );
		  }
        return snapshot; 
    } */

    public void validate(IRequestCycle cycle)
    {
        Visit visit = (Visit) getVisit();
        if ( ! visit.isAuthenticated() )
		  {
			  IPage page = cycle.getPage( "Login" );
			  Login loginPage = (Login)page;
			  loginPage.setForward( getPageName() );
			  throw new PageRedirectException( loginPage );
		  }
		  super.validate( cycle );
    }
	 public SecureRosterPage()
	 {
		 super();
	 }
}
