package org.thdl.roster.pages;

import java.util.*;
import org.apache.tapestry.*;
import org.apache.tapestry.form.*;
import org.apache.torque.*;
import org.apache.torque.util.*;
import org.thdl.roster.*;
import org.thdl.roster.om.*;
import org.apache.log4j.*;

public class Home extends RosterPage
{
//attributes
	private RosterQuery rosterQuery;
	private String login;
	private String password;
	private Member member;
//accessors
	public void setMember(Member member) {
		this.member = member;
	}
	public Member getMember() {
		return member;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLogin() {
		return login;
	}
	public String getPassword() {
		return password;
	}

	public void setRosterQuery(RosterQuery rosterQuery) {
		this.rosterQuery = rosterQuery;
	}
	public RosterQuery getRosterQuery() {
		if ( null == rosterQuery )
		{
			Global global = (Global) getGlobal();
			setRosterQuery( new RosterQuery( global.getRepresentedCountries() ) );
		}
		return rosterQuery;
	}
//helpers
	public List getPersonalEntries()
	{
		List entries = new LinkedList();
		Visit visit = (Visit)getVisit();
		if ( visit.isAuthenticated() )
		{
			Integer userId =  visit.getThdlUser().getId() ;
			Global global = (Global) getGlobal();
			Iterator iter = global.getAllPeople().iterator();
			while( iter.hasNext() )
			{
				Member mem = (Member)iter.next();
				if ( mem.getCreatedBy().equals( userId ) )
				{
					entries.add( mem );
				}
			}
			iter = global.getAllOrganizations().iterator();
			while( iter.hasNext() )
			{
				Member mem = (Member)iter.next();
				if ( mem.getCreatedBy().equals( userId ) )
				{
					entries.add( mem );
				}
			}
			iter = global.getAllProjects().iterator();
			while( iter.hasNext() )
			{
				Member mem = (Member)iter.next();
				if ( mem.getCreatedBy().equals( userId ) )
				{
					entries.add( mem );
				}
			}
		}
		return entries;
	}
//listeners
	public void loginToEdit( IRequestCycle cycle )
	{
		//test for Login.validateUser then either forward to ContactInfo/Home
		Login loginPage = (Login)cycle.getPage( "Login" );
		loginPage.setLogin( getLogin() );
		loginPage.setPassword( getPassword() );
		if ( loginPage.validateUser() )
		{
			if ( getPersonalEntries().size() == 1 )
			{
				Visit visit = (Visit) getVisit();
				visit.setMember( (Member)getPersonalEntries().get( 0 ) );
				IPage page = cycle.getPage( "Contact" );
				page.validate( cycle );
				cycle.activate( page );
			}
		}
		else
		{
			setWarning( "Invalid Login/Password combination" );
		}
	}
				
	public void editMember( IRequestCycle cycle )
	{
		MembersPage page = (MembersPage)cycle.getPage( "People" );
		page.editMember( cycle );
	}
	public void addNewMember( IRequestCycle cycle )
	{
		MembersPage page = (MembersPage)cycle.getPage( "People" );
		page.addNewMember( cycle );
	}
	public void processForm( IRequestCycle cycle ) throws Exception
	{
		List members = null;
		try
		{
			Global global =(Global)getGlobal();
			RosterQueryAgent agent = new RosterQueryAgent( global.getTorqueConfig() );
			String sql = agent.buildQuery( getRosterQuery() );
			Logger  logger = Logger.getLogger("org.thdl.roster");
			logger.debug( "About to call RosterQueryAgent.executeQuery( sql ) where sql = " +sql );
			members = agent.executeQuery( sql );
		}
		catch ( TorqueException te )
		{
			throw new ApplicationRuntimeException( te );
		}
		MembersPage page = (MembersPage) cycle.getPage( "SearchResults" );
		page.setMembers( members );
		cycle.activate( page );
	}
 	public void detach()
	{
		getRosterQuery().clear();
		setLogin( null );
		setPassword( null );
		super.detach();
	}

//constructors

	public Home() 
	{ 
		super();
	}
}
