package org.thdl.roster.pages.forms;

import java.util.List;
import java.util.LinkedList;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IEngine;

import org.apache.tapestry.*;
import org.thdl.roster.*;
import org.thdl.roster.om.*;
import org.thdl.roster.pages.*;

public abstract class MemberFormSeries extends SecureRosterPage
{
//attributes
	private String token;
//accessors
	public void setToken(String token) {
		this.token = token;
	}
	public String getToken() {
		return token;
	}
//helpers
//tapestry listeners
	public abstract void processForm( IRequestCycle cycle );
	
	public boolean tokensValidate()
	{
		return true;
	}
	
	public void forward( IRequestCycle cycle )
	{		
		String warning = null;
		String message = null;
		if ( tokensValidate() )
		{
			processForm( cycle );
			message= "Your " + getPageName() + " data was saved in your profile.";
		}
		else
		{
			warning="Invalid reload attempt; your form submission was not processed.";
		}
		
		String newToken = null;
		try
		{
			newToken = TokenMaker.make();
		}
		catch ( java.security.NoSuchAlgorithmException nsae )
		{
			throw new ApplicationRuntimeException( nsae );
		}

		MemberFormSeries page = (MemberFormSeries)cycle.getPage( getNextPage() );
		page.setToken( newToken );		
		Visit visit = (Visit)getVisit();
		visit.setToken( newToken );
		
		page.setWarning( warning );
		page.setMessage( message );
		page.validate( cycle );
		cycle.activate( page );
	}
	
	public void finish( IRequestCycle cycle )
	{
		Visit visit = (Visit)getVisit();
		Member member = (Member) visit.getMember();
		Global global = (Global) getGlobal();

		if ( tokensValidate() )
		{
			processForm( cycle );
		}

		visit.setToken( null );
		
		MemberPage page = null;
		if ( member instanceof Person )
		{
			page = (MemberPage)cycle.getPage( "Person" );
			global.setAllPeople( global.refreshPeople() );
		}
		else if ( member instanceof Project )
		{
			page = (MemberPage)cycle.getPage( "Project" );
			global.setAllProjects( global.refreshProjects() );
		}
		else if ( member instanceof Organization )
		{
			page = (MemberPage)cycle.getPage( "Organization" );
			global.setAllOrganizations( global.refreshOrganizations() );
		}
		global.setRepresentedCountries( global.refreshRepresentedCountries() );
		page.setMember( member );
		cycle.activate( page );
	}

    public void validate(IRequestCycle cycle)
    {
        Visit visit = (Visit) getVisit();
		  Member member = (Member)visit.getMember();
       if ( null == member )
		  {
			  RosterPage home = (RosterPage) cycle.getPage( "Home" );
			  home.setWarning( "There was no Roster Member in your session. This is possibly due to navigating to an edit screen after your session had timed out." );
			  throw new PageRedirectException( home );
		  }

		  super.validate( cycle );
		  
		  if ( ! member.isNew() )
		  {
			  Integer owner = member.getCreatedBy();
			  Integer user = new Integer( visit.getThdlUser().getId() );
			  if ( ! owner.equals( user ) )
			  {
				  RosterPage home = (RosterPage) cycle.getPage( "Home" );
				  home.setWarning( "You are not logged in as the owner of this entry." );
				  throw new PageRedirectException( home );
			  }
		  }
    }
//synthetic accessors

//constructors
	public MemberFormSeries()
	{
		super();
		/* List list = java.util.Collections.synchronizedList( new LinkedList() );
		setForward( list );
		getForward().add( "Contact" );
		getForward().add( "Background" );
		getForward().add( "Research" );
		getForward().add( "Publications" ); */
	}
}