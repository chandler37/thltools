package org.thdl.roster;

import java.util.*;
import 	org.apache.commons.configuration.*;

import org.apache.tapestry.*;
import org.apache.tapestry.form.*;
import org.apache.torque.*;
import org.apache.torque.util.*;
import org.thdl.roster.*;
import org.thdl.roster.om.*;

public class RosterQueryAgent
	{
		public  String esc( String rawText ) throws TorqueException
		{
			return SqlExpression.quoteAndEscapeText( rawText, Torque.getDB( Torque.getDefaultDB() ) );
		}
	
		// Custom Query helpers
	
		public  String buildQuery( RosterQuery query) throws TorqueException
		{
			StringBuffer sql = new StringBuffer() ;
			sql.append( " \n\nSELECT DISTINCT Member.* FROM Member " );
			sql.append( " \nLEFT JOIN PersonData ON Member.person_data_id = PersonData.id " );  
			sql.append( " \nLEFT JOIN ProjectData ON Member.project_data_id = ProjectData.id   " );
			sql.append( " \nLEFT JOIN OrganizationData ON Member.organization_data_id = OrganizationData.id " );
			sql.append( " \nLEFT JOIN ContactInfo ON Member.contact_info_id = ContactInfo.id " );
			sql.append( " \nLEFT JOIN Address ON ContactInfo.address_id = Address.id " );
			sql.append( " \nLEFT JOIN Publication ON Member.publication_id = Publication.id " );
			sql.append( " \nLEFT JOIN ResearchInterest ON Member.research_interest_id = ResearchInterest.id " );
			sql.append( " \nLEFT JOIN ResearchInterestDiscipline ON ResearchInterest.id = ResearchInterestDiscipline.research_interest_id " );
			sql.append( " \nLEFT JOIN ResearchInterestCulturalArea ON ResearchInterest.id = ResearchInterestCulturalArea.research_interest_id " );  
			sql.append( " \nLEFT JOIN ResearchInterestLanguage  ON ResearchInterest.id = ResearchInterestLanguage.research_interest_id   " );
			sql.append( " \nLEFT JOIN PersonPersonType  ON PersonData.id = PersonPersonType.person_data_id   " );
			sql.append( " \nLEFT JOIN ProjectProjectType  ON ProjectData.id = ProjectProjectType.project_data_id   " );
			sql.append( " \nLEFT JOIN OrganizationOrganizationType  ON OrganizationData.id = OrganizationOrganizationType.organization_data_id   " );
			sql.append( " \nWHERE Member.deleted = 'false'   " );
			appendNames( sql, query );
			appendOrganizationalBase( sql, query );
			appendAnywhere( sql, query );
			appendMemberTypes( sql, query );
			appendCountries( sql, query );
			appendDiscipline( sql, query );
			appendLanguage( sql, query );
			appendCulturalArea( sql, query );
			sql.append( " \nORDER BY OrganizationData.name, ProjectData.name, PersonData.lastname" );
			return sql.toString();
		}
		
		public  List executeQuery( String sql ) throws TorqueException
		{
			List villageRecords = MemberPeer.executeQuery( sql );
			List members = MemberPeer.populateObjects( villageRecords );
			return members;
		}
		
		public  void appendCulturalArea( StringBuffer sql, RosterQuery query )
		{
			if ( null != query.getSelectedCulturalArea() )
			{
				sql.append( "  \nAND ResearchInterestCulturalArea.cultural_area_id = " );
				sql.append( query.getSelectedCulturalArea() );
			}
		}
	
		public  void appendLanguage( StringBuffer sql, RosterQuery query )
		{
			if ( null != query.getSelectedLanguage() )
			{
				sql.append( "  \nAND ResearchInterestLanguage.language_id = " );
				sql.append( query.getSelectedLanguage() );
			}
		}
	
		public  void appendDiscipline( StringBuffer sql, RosterQuery query )
		{
			if ( null != query.getSelectedDiscipline() )
			{
				sql.append( "  \nAND ResearchInterestDiscipline.discipline_id = " );
				sql.append( query.getSelectedDiscipline() );
			}
		}
	
		public  void appendCountries( StringBuffer sql, RosterQuery query ) throws TorqueException
		{
			Iterator countries = query.getCountries().keySet().iterator();
			while ( countries.hasNext() )
			{
				Country key = (Country)countries.next();
				Boolean value = (Boolean) query.getCountries().get( key );
				if ( value.equals( Boolean.FALSE ) )
				{
					sql.append( "  \nAND " );
					sql.append( AddressPeer.COUNTRY_ID );
					sql.append( " <> " );
					sql.append( key.getId() );
				}
			}
		}
	
		public  void appendNames( StringBuffer sql, RosterQuery query ) throws TorqueException
		{
			if ( null != query.getName() && ! query.getName().equals( "" ) )
			{
				String name = "%" + query.getName() + "%";
				name = esc( name );
				sql.append( " \nAND " );
				sql.append( " ( " );
				sql.append( " ( " );
				sql.append( " PersonData.firstname LIKE " );
				sql.append( name );
				sql.append( " \nOR" );
				sql.append( " PersonData.lastname LIKE " );
				sql.append( name );
				sql.append( " ) " );
				sql.append( " \nOR" );
				sql.append( " ProjectData.name LIKE " );
				sql.append( name );
				sql.append( " \nOR" );
				sql.append( " OrganizationData.name LIKE " );
				sql.append( name );
				sql.append( " )" );
			}
		}

		public  void appendOrganizationalBase( StringBuffer sql, RosterQuery query ) throws TorqueException
		{
			if ( null != query.getOrganizationalBase() && ! query.getOrganizationalBase().equals( "" ) )
			{
				String orgBase = "%" + query.getOrganizationalBase() + "%";
				orgBase = esc( orgBase );
				sql.append( " \nAND " );
				sql.append( " ( " );
				sql.append( " PersonData.parent_organization LIKE " );
				sql.append( orgBase );
				sql.append( " \nOR" );
				sql.append( " ProjectData.parent_organization LIKE " );
				sql.append( orgBase );
				sql.append( " \nOR" );
				sql.append( " OrganizationData.parent_organization LIKE " );
				sql.append( orgBase );
				sql.append( " )" );
			}
		}

		public  void appendAnywhere( StringBuffer sql, RosterQuery query ) throws TorqueException
		{
			if ( null != query.getAnywhere() && ! query.getAnywhere().equals( "" ) )
			{
				String param = "%" + query.getAnywhere() + "%";
				param = esc( param );
				sql.append( " \nAND " );
				sql.append( " ( " );
				sql.append( " PersonData.firstname LIKE " + param);
				sql.append( " \nOR PersonData.middlename LIKE " + param );
				sql.append( " \nOR PersonData.lastname LIKE " + param );
				sql.append( " \nOR PersonData.bio LIKE " + param );
				sql.append( " \nOR PersonData.history LIKE " + param );
				sql.append( " \nOR PersonData.parent_organization LIKE " + param );
				sql.append( " \nOR PersonData.school LIKE " + param );
				sql.append( " \nOR PersonData.department LIKE " + param );
				sql.append( " \nOR PersonData.program LIKE " + param );
				sql.append( " \nOR PersonData.advisor LIKE " + param );
				sql.append( " \nOR PersonData.other_backgrounds LIKE " + param );
				sql.append( " \nOR PersonData.organization LIKE " + param );
				sql.append( " \nOR PersonData.division LIKE " + param );
				sql.append( " \nOR PersonData.title LIKE " + param );
				sql.append( " \nOR PersonData.job_description LIKE " + param );

				sql.append( " \nOR ProjectData.name LIKE " + param );
				sql.append( " \nOR ProjectData.parent_organization LIKE " + param );
				sql.append( " \nOR ProjectData.divisions LIKE " + param );
				sql.append( " \nOR ProjectData.people LIKE " + param );
				sql.append( " \nOR ProjectData.description LIKE " + param );
				sql.append( " \nOR ProjectData.history LIKE " + param );
				sql.append( " \nOR ProjectData.resources LIKE " + param );
				sql.append( " \nOR ProjectData.education_programs LIKE " + param );

				sql.append( " \nOR OrganizationData.name LIKE " + param );
				sql.append( " \nOR OrganizationData.parent_organization LIKE " + param );
				sql.append( " \nOR OrganizationData.divisions LIKE " + param );
				sql.append( " \nOR OrganizationData.people LIKE " + param );
				sql.append( " \nOR OrganizationData.description LIKE " + param );
				sql.append( " \nOR OrganizationData.history LIKE " + param );
				sql.append( " \nOR OrganizationData.resources LIKE " + param );
				sql.append( " \nOR OrganizationData.education_programs LIKE " + param );

				sql.append( " \nOR Publication.formal_publications LIKE " + param );
				sql.append( " \nOR Publication.works_in_progress LIKE " + param );
				sql.append( " \nOR Publication.projects LIKE " + param );

				sql.append( " \nOR ResearchInterest.interests LIKE " + param );
				sql.append( " \nOR ResearchInterest.activities LIKE " + param );
				sql.append( " \nOR ResearchInterest.collaboration_interests LIKE " + param );
				
				sql.append( " ) " );
			}
		}

		public  void appendMemberTypes( StringBuffer sql, RosterQuery query ) throws TorqueException
		{
			Iterator memTypes = query.getMemberTypes().keySet().iterator();
			LinkedList sqlParts = new LinkedList();
			StringBuffer sqlPartsConcat = new StringBuffer();
			while ( memTypes.hasNext() )
			{
				String key = (String)memTypes.next();
				Boolean value = (Boolean) query.getMemberTypes().get( key );
				if ( value.equals( Boolean.TRUE ) )
				{
					StringBuffer tempSql = new StringBuffer();
					tempSql.append( MemberPeer.MEMBER_TYPE );
					tempSql.append( " LIKE " );
					tempSql.append( esc( key ) );
					if ( key.equals( MemberPeer.CLASSKEY_PERSON ) && null != query.getSelectedPersonType() )
					{
						tempSql.append( " AND " );
						tempSql.append( PersonPersonTypePeer.PERSON_TYPE_ID );
						tempSql.append( " = " );
						tempSql.append( query.getSelectedPersonType() );
					}
					else if ( key.equals( MemberPeer.CLASSKEY_PROJECT ) && null != query.getSelectedProjectType() )
					{
						tempSql.append( " AND " );
						tempSql.append( ProjectProjectTypePeer.PROJECT_TYPE_ID );
						tempSql.append( " = " );
						tempSql.append( query.getSelectedProjectType() );
					}
					else if ( key.equals( MemberPeer.CLASSKEY_ORGANIZATION ) && null != query.getSelectedOrganizationType() )
					{
						tempSql.append( " AND " );
						tempSql.append( OrganizationOrganizationTypePeer.ORGANIZATION_TYPE_ID );
						tempSql.append( " = " );
						tempSql.append( query.getSelectedOrganizationType() );
					}
					sqlParts.add( tempSql.toString() );
				}
			}

			ListIterator iterator = sqlParts.listIterator();
			int index = 0;
			String pieceOfSql;
			while ( iterator.hasNext() )
			{
				Object object = iterator.next();
				pieceOfSql = (String) object;
				if ( index > 0 )
				{
					sqlPartsConcat.append( " \nOR " );
				}
				else
				{
					sqlPartsConcat.append( "\n" );
				}
				sqlPartsConcat.append( " ( " );
				sqlPartsConcat.append( pieceOfSql );
				sqlPartsConcat.append( " ) " );
				index++;
			}
			sql.append( " \nAND ( " );
			sql.append( sqlPartsConcat.toString() );
			sql.append( " \n) " ); 
		}

	public RosterQueryAgent() throws Exception
	{
			java.io.InputStream stream = Torque.class.getClassLoader().getResourceAsStream("org/thdl/roster/roster-torque.properties");
			PropertiesConfiguration config = new PropertiesConfiguration();
			config.load( stream );
			if ( ! Torque.isInit() )
			{
				Torque.init( config );
			}
	}
	public RosterQueryAgent( Configuration config ) throws Exception
	{
			if ( ! Torque.isInit() )
			{
				Torque.init( config );
			}
	}
//main
	public static void main(String[] args)
	{
		try 
		{
			RosterQuery query = new RosterQuery();
			RosterQueryAgent agent = new RosterQueryAgent();
			String sql = agent.buildQuery( query );
			System.out.println( sql );
			System.out.println( agent.executeQuery( sql ) );
		}
		catch (Exception te )
		{
			te.printStackTrace();
		}
	}
}		

