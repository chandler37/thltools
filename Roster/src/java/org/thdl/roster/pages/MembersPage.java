package org.thdl.roster.pages;

import java.util.*;
import org.apache.torque.*;
import org.apache.torque.util.*;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.IRequestCycle;

import org.apache.tapestry.IPage;
import org.apache.tapestry.ApplicationRuntimeException;
import org.thdl.roster.*;
import org.thdl.roster.om.*;

public class MembersPage extends RosterPage
{
//attributes
	private HashMap memberTypes;
	private List members;
	private Member member;
	private boolean memberTypeChanged;
//accessors	
	public void setMemberTypeChanged(boolean memberTypeChanged) {
		this.memberTypeChanged = memberTypeChanged;
	}
	public boolean getMemberTypeChanged() {
		return memberTypeChanged;
	}
	public void setMembers(List members) {
		this.members = members;
	}
	public void setMember(Member member) {
		setMemberTypeChanged( false );
		if ( null != member && !  member.getClass().isInstance( getMember() ) )
		{
			setMemberTypeChanged( true );
		}
		this.member = member;
	}
	public List getMembers() {
		return members;
	}
	public Member getMember() {
		return member;
	}
	public void setMemberTypes(HashMap memberTypes) {
		this.memberTypes = memberTypes;
	}
	public HashMap getMemberTypes() {
		return memberTypes;
	}
//synthetic accessors
	public String getMemberTypeHeader()
	{
		String memType = getMember().getMemberType();
		if ( memType.equals( "person" ) )
			memType = "People";
		else if ( memType.equals( "organization" ) ) 
			memType = "Organizations";
		else if ( memType.equals( "project" ) )
			memType = "Projects";
		return memType;
	}
	public List getAllGivenTypeOfMembers()
	{
		List members = null;
		Global global = (Global) getGlobal();
		if ( getPageName().equals( "People" ) )
		{
			members  = global.getAllPeople();
		}
		if ( getPageName().equals( "Projects" ) )
		{
			members = global.getAllProjects();
		}
		if ( getPageName().equals( "Organizations" ) )
		{
			members  = global.getAllOrganizations();
		}
		return members;
	}
	
//listeners

	public void addNewMember( IRequestCycle cycle ) 
	{
		Object[] params = cycle.getServiceParameters();
		String memberType = (String) params[0];
		RosterMember member = (RosterMember) getMemberTypes().get(  memberType );
		try
		{
			Visit visit = (Visit) getVisit();
			visit.setMember( produceMember( member ) ); 
		}
		catch ( TorqueException e )
		{
			throw new ApplicationRuntimeException( e );
		}
		catch ( RosterMemberTypeException e )
		{
			throw new ApplicationRuntimeException( e );
		}
		IPage page = cycle.getPage( "Contact" );
		page.validate( cycle );
		cycle.activate( page );
	}

	public void viewMember( IRequestCycle cycle )
	{
		Integer memberPK = (Integer)cycle.getServiceParameters()[0];
		Member member = null;
		try
		{
			member = MemberPeer.retrieveByPK( memberPK );
		}
		catch ( TorqueException te )
		{
			throw new ApplicationRuntimeException( te );
		}
		
		MemberPage page = null;
		if ( member instanceof Person )
		{
			page = (MemberPage) cycle.getPage( "Person" );
		}
		else if ( member instanceof Project )
		{
			page = (MemberPage) cycle.getPage( "Project" );
		}
		else if ( member instanceof Organization )
		{
			page = (MemberPage) cycle.getPage( "Organization" );
		}
		page.setMember( member );
		cycle.activate( page );
	}
	public void editMember( IRequestCycle cycle ) 
	{
		Visit visit = (Visit) getVisit();
		Object[] objArray = cycle.getServiceParameters();
		Object obj = objArray[0];
		Integer memberPK = (Integer) obj;

		try
		{
			visit.setMember( MemberPeer.retrieveByPK( memberPK  ) );
		}
		catch ( TorqueException te )
		{
			throw new ApplicationRuntimeException( te );
		}
		
		IPage page = cycle.getPage( "Contact" );
		page.validate( cycle );
		cycle.activate( page );
	}

//helpers
	public RosterMember produceMember( RosterMember rosterMember ) throws TorqueException, RosterMemberTypeException
	{
			if ( rosterMember instanceof Person )
			{
				Person pers = (Person) rosterMember;
				rosterMember = (Person) pers.copy();
			}
			else if ( rosterMember instanceof Project )
			{
				Project proj = (Project) rosterMember;
				rosterMember = (Project) proj.copy();
			}
			else if ( rosterMember instanceof Organization )
			{
				Organization org = (Organization) rosterMember;
				rosterMember = (Organization) org.copy();
			}
			return rosterMember;
	}

	public void detach()
	{
		super.detach();
	}

//constructors
	public MembersPage()
	{
		try
		{
			if ( ! Torque.isInit() )
			{
				Global global = (Global) getGlobal();
				Torque.init(  global.getTorqueConfig() );
			}
			Person person = new Person();
			Project project = new Project();
			Organization org = new Organization();
			setMemberTypes( new HashMap() );
			getMemberTypes().put( "person" , person );
			getMemberTypes().put( "project" , project );
			getMemberTypes().put( "organization" , org );
		}
		catch ( TorqueException te )
		{
			throw new ApplicationRuntimeException( te.getMessage() );
		}
	}
}