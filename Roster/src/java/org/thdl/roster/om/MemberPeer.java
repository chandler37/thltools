package org.thdl.roster.om;

import org.thdl.roster.RosterQuery;
import org.apache.torque.*;
import org.apache.torque.util.*;
import java.util.*;
/** 
 * The skeleton for this class was autogenerated by Torque on:
 *
 * [Thu Feb 27 15:11:05 EST 2003]
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class MemberPeer extends org.thdl.roster.om.BaseMemberPeer
{
	public static String esc( String rawText ) throws TorqueException
	{
		return SqlExpression.quoteAndEscapeText( rawText, Torque.getDB( Torque.getDefaultDB() ) );
	}

// Custom Query helpers

/* 	public static List executeQuery( RosterQuery query) throws TorqueException
	{
		StringBuffer sql = new StringBuffer() ;
		sql.append( " SELECT DISTINCT Member.* FROM Member, PersonData, ProjectData, OrganizationData, ContactInfo, Address, ResearchInterest, ResearchInterestDiscipline, ResearchInterestCulturalArea, ResearchInterestLanguage " );
		sql.append( "\nWHERE Member.deleted = 'false' ");
		appendMemberTypes( sql, query );
		appendNames( sql, query );
		appendCountries( sql, query );
		appendDiscipline( sql, query );
		appendLanguage( sql, query );
		appendCulturalArea( sql, query );
		query.setPeerGeneratedSql( sql.toString() );
		//List villageRecords = MemberPeer.executeQuery( sql.toString() );
		//List villageRecords = MemberPeer.executeQuery( "SELECT * FROM Member" );
		return null;//MemberPeer.populateObjects( villageRecords );
	}
	
	public static void appendCulturalArea( StringBuffer sql, RosterQuery query )
	{
		if ( null != query.getSelectedCulturalArea() )
		{
			sql.append( " \nAND Member.research_interest_id = ResearchInterest.id " );
			sql.append( " \nAND ResearchInterest.id = ResearchInterestCulturalArea.research_interest_id " );
			sql.append( " \nAND ResearchInterestCulturalArea.cultural_area_id = " );
			sql.append( query.getSelectedCulturalArea() );
		}
	}

	public static void appendLanguage( StringBuffer sql, RosterQuery query )
	{
		if ( null != query.getSelectedLanguage() )
		{
			sql.append( " \nAND Member.research_interest_id = ResearchInterest.id " );
			sql.append( " \nAND ResearchInterest.id = ResearchInterestLanguage.research_interest_id " );
			sql.append( " \nAND ResearchInterestLanguage.language_id = " );
			sql.append( query.getSelectedLanguage() );
		}
	}

	public static void appendDiscipline( StringBuffer sql, RosterQuery query )
	{
		if ( null != query.getSelectedDiscipline() )
		{
			sql.append( " \nAND Member.research_interest_id = ResearchInterest.id " );
			sql.append( " \nAND ResearchInterest.id = ResearchInterestDiscipline.research_interest_id " );
			sql.append( " \nAND ResearchInterestDiscipline.discipline_id = " );
			sql.append( query.getSelectedDiscipline() );
		}
	}

	public static void appendCountries( StringBuffer sql, RosterQuery query ) throws TorqueException
	{
		Iterator countries = query.getCountries().keySet().iterator();
		while ( countries.hasNext() )
		{
			Country key = (Country)countries.next();
			Boolean value = (Boolean) query.getCountries().get( key );
			if ( value.equals( Boolean.FALSE ) )
			{
				sql.append( " \nAND Member.contact_info_id = ContactInfo.id " );
				sql.append( " \nAND ContactInfo.address_id = Address.id " );
				sql.append( " \nAND " );
				sql.append( AddressPeer.COUNTRY_ID );
				sql.append( " <> " );
				sql.append( key.getId() );
			}
		}
	}

	public static void appendNames( StringBuffer sql, RosterQuery query ) throws TorqueException
	{
		if ( null != query.getName() && ! query.getName().equals( "" ) )
		{
			String name = "%" + query.getName() + "%";
			name = esc( name );
			sql.append( "\nAND " );
			sql.append( "\n( " );
			sql.append( "\n	( " );
			sql.append( "\n		Member.person_data_id = PersonData.id " );
			sql.append( "\n		AND " );
			sql.append( "\n		( " );
			sql.append( "\n			PersonData.firstname LIKE " );
			sql.append( name );
			sql.append( "\n			OR" );
			sql.append( "\n			PersonData.lastname LIKE " );
			sql.append( name );
			sql.append( "\n		) " );
			sql.append( "\n	)" );
			sql.append( "\n	OR" );
			sql.append( "\n	(" );
			sql.append( "\n		Member.project_data_id = ProjectData.id" );
			sql.append( "\n		AND" );
			sql.append( "\n		ProjectData.name LIKE " );
			sql.append( name );
			sql.append( "\n	)" );
			sql.append( "\n	OR" );
			sql.append( "\n	(" );
			sql.append( "\n		Member.organization_data_id = OrganizationData.id" );
			sql.append( "\n		AND" );
			sql.append( "\n		OrganizationData.name LIKE " );
			sql.append( name );
			sql.append( "\n	)" );
			sql.append( "\n)" );
		}
	}

	public static void appendMemberTypes( StringBuffer sql, RosterQuery query ) throws TorqueException
	{
		Iterator memTypes = query.getMemberTypes().keySet().iterator();
		while ( memTypes.hasNext() )
		{
			String key = (String)memTypes.next();
			Boolean value = (Boolean) query.getMemberTypes().get( key );
			if ( value.equals( Boolean.FALSE ) )
			{
				sql.append( " \nAND " );
				sql.append( MemberPeer.MEMBER_TYPE );
				sql.append( " NOT LIKE " );
				sql.append( esc( key ) );
			}
		}
	} */
}