package org.thdl.roster;

import java.util.*;
import org.thdl.roster.om.*;
import org.apache.torque.*;
import org.apache.torque.util.*;
import java.text.*;
public class RosterTest
{
	public void doStuff()
	{
		try 
		{
			String sql = "SELECT DISTINCT Member.* FROM Member, PersonData, ProjectData, OrganizationData, ContactInfo, Address, ResearchInterest, ResearchInterestDiscipline, ResearchInterestCulturalArea, ResearchInterestLanguage WHERE Member.deleted = 'false' AND ( ( Member.person_data_id = PersonData.id AND ( PersonData.firstname LIKE '%travis%' OR PersonData.lastname LIKE '%travis%' ) ) OR ( Member.project_data_id = ProjectData.id AND ProjectData.name LIKE '%travis%' ) OR ( Member.organization_data_id = OrganizationData.id AND OrganizationData.name LIKE '%travis%' ) ) AND Member.research_interest_id = ResearchInterest.id AND ResearchInterest.id = ResearchInterestDiscipline.research_interest_id AND ResearchInterestDiscipline.discipline_id = 5 AND Member.research_interest_id = ResearchInterest.id AND ResearchInterest.id = ResearchInterestLanguage.research_interest_id AND ResearchInterestLanguage.language_id = 2 AND Member.research_interest_id = ResearchInterest.id AND ResearchInterest.id = ResearchInterestCulturalArea.research_interest_id AND ResearchInterestCulturalArea.cultural_area_id = 1";
			List villageRecords = MemberPeer.executeQuery( sql.toString() );
			System.out.println ( villageRecords );
			List members = MemberPeer.populateObjects( villageRecords );
			System.out.println ( members );
		}
		catch (TorqueException te) {
			 te.printStackTrace( );
		}
	}
	public static void main( String[] srgs )
	{
		try {
		if ( ! Torque.isInit() )
		{
			Torque.init( "./roster-torque.properties" );
		}
		RosterTest rt = new RosterTest();
		rt.doStuff();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
