package org.thdl.roster.components;

import java.util.*;

import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.*;
import org.apache.torque.util.Criteria;
import org.apache.torque.*;

import org.thdl.roster.om.*;
import org.thdl.roster.*;

public class MemberTypeDisplay extends BaseComponent
{
//attributes
	private Member member;
	private String memberType;
	private int index;
//accessors
	public void setMember(Member member) {
		this.member = member;
	}
	public Member getMember() {
		return member;
	}
	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}
	public String getMemberType() {
		return memberType;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getIndex() {
		return index;
	}
//synthetic properties
	public List getMemberTypeList()
	{
		LinkedList memberTypes = new LinkedList();
		try
		{
			List mmTypes = null;
			Criteria crit = new Criteria();
			if ( member instanceof Person )
			{
				crit.add(  PersonPersonTypePeer.PERSON_DATA_ID, getMember().getMemberData().getId() );
				crit.addAscendingOrderByColumn( PersonPersonTypePeer.RELEVANCE );
				mmTypes = PersonPersonTypePeer.doSelect( crit );
			}
			else if ( member instanceof Project )
			{
				crit.add(  ProjectProjectTypePeer.PROJECT_DATA_ID, getMember().getMemberData().getId() );
				crit.addAscendingOrderByColumn( ProjectProjectTypePeer.RELEVANCE );
				mmTypes = ProjectProjectTypePeer.doSelect( crit );
			}
			else if ( member instanceof Organization )
			{
				crit.add(  OrganizationOrganizationTypePeer.ORGANIZATION_DATA_ID, getMember().getMemberData().getId() );
				crit.addAscendingOrderByColumn( OrganizationOrganizationTypePeer.RELEVANCE );
				mmTypes = OrganizationOrganizationTypePeer.doSelect( crit );
			}

			ListIterator looper = mmTypes.listIterator();
			while( looper.hasNext() )
			{
				String displayText = null;
				if ( member instanceof Person )
				{
					PersonPersonType pt = (PersonPersonType) looper.next();
					displayText= pt.getPersonType().getPersonType();
				}
				else if ( member instanceof Project )
				{
					ProjectProjectType pt = (ProjectProjectType) looper.next();
					displayText= pt.getProjectType().getProjectType();
				}
				else if ( member instanceof Organization )
				{
					OrganizationOrganizationType ot = (OrganizationOrganizationType) looper.next();
					displayText= ot.getOrganizationType().getOrganizationType();
				}
				memberTypes.add( displayText );
			}
		}
		catch ( TorqueException te )
		{
			throw new ApplicationRuntimeException( te );
		}
		catch ( RosterMemberTypeException rmte )
		{
			throw new ApplicationRuntimeException( rmte );
		}
		return memberTypes;
	}
//constructors
	 public MemberTypeDisplay()
	{
		super();
		try
		{
			if ( ! Torque.isInit() )
			{
				Global global = (Global) getPage().getGlobal();
				Torque.init( global.getTorqueConfig() );
			}
		}
		catch ( TorqueException te )
		{
			throw new ApplicationRuntimeException( te );
		}

	}
}