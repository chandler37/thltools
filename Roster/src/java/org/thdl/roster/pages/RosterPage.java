package org.thdl.roster.pages;

import org.apache.torque.*;
import org.apache.tapestry.*;
import org.apache.tapestry.html.BasePage;
import org.thdl.roster.*;

public class RosterPage extends BasePage
{
//attributes
    private String message;
    private String warning;
	 private String nextPage;
//accessors
	 public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}
	public String getNextPage() {
		return nextPage;
	}

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public void setWarning(String warning)
    {
        this.warning = warning;
    }

    public String getWarning()
    {
        return warning;
    }
//helpers
	public void torqueInit()
	{
		try
		{
			if ( ! Torque.isInit() )
			{
				Global global = (Global) getGlobal();
				Torque.init(  global.getTorqueConfig() );
			}
		}
		catch (TorqueException te )
		{
			throw new ApplicationRuntimeException( te );
		}
	}
	
    public boolean isLoggedIn()
    {
        Visit visit = (Visit)getPage().getEngine().getVisit();
        boolean b = false;
        if (null != visit && visit.isAuthenticated() )
            b = true;
        return b;
    }

    public void logout(IRequestCycle cycle)
    {
        getEngine().setVisit(null);
        RosterPage home = (RosterPage)cycle.getPage("Home");
        home.setMessage("You have successfully logged out of the Thdl Community Roster.");
        cycle.activate(home);
    }

    public void detach()
    {
        setMessage(null);
        setWarning(null);
        super.detach();
    }
//constructors
    public RosterPage()
    {
		super();
		try
		{
			if ( !Torque.isInit() )
			{
				Global global = (Global) getGlobal();
				Torque.init( global.getTorqueConfig() );
			}
		}
		catch ( TorqueException te )
		{
			throw new ApplicationRuntimeException( "Torque Exception says: " + te.getMessage(), te );
		}
    }

}
