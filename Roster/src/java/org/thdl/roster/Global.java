package org.thdl.roster;

import java.util.*;
import java.text.*;
import org.apache.tapestry.*;
import org.apache.torque.*;
import org.apache.torque.util.*;
import org.apache.torque.om.*;
import org.thdl.roster.om.*;
import 	org.apache.commons.configuration.Configuration;
import 	org.apache.commons.configuration.BaseConfiguration;
import 	org.apache.commons.configuration.PropertiesConfiguration;

public class Global
{
//attributes
	private List allPeople;
	private List allOrganizations;
	private List allProjects;
	private List representedCountries;
	private Configuration torqueConfig;
//accessors
	public void setRepresentedCountries(List representedCountries) {
		this.representedCountries = representedCountries;
	}
	public List getRepresentedCountries() {
		if ( null == representedCountries )
		{
			setRepresentedCountries( refreshRepresentedCountries() );
		}
		return representedCountries;
	}
	public void setAllPeople(List allPeople) {
		this.allPeople = allPeople;
	}
	public void setAllOrganizations(List allOrganizations) {
		this.allOrganizations = allOrganizations;
	}
	public void setAllProjects(List allProjects) {
		this.allProjects = allProjects;
	}
	public List getAllPeople() {
		if (null == allPeople)
		{
			setAllPeople( refreshPeople() );
		}
		return allPeople;
	}
	public List getAllOrganizations() {
		if (null == allOrganizations)
		{
			setAllOrganizations( refreshOrganizations() );
		}
		return allOrganizations;
	}
	public List getAllProjects() {
		if (null == allProjects)
		{
			setAllProjects( refreshProjects() );
		}
		return allProjects;
	}
	private void setTorqueConfig(Configuration torqueConfig) {
		this.torqueConfig = torqueConfig;
	}
	public Configuration getTorqueConfig() throws ApplicationRuntimeException {
		if (null== torqueConfig)
		{
			setTorqueConfig( fileConfig() );
		}
		return torqueConfig;
	}
//helpers
	public List refreshPeople()
	{
		try
		{
			Criteria crit = new Criteria();
			crit.add(  MemberPeer.MEMBER_TYPE, MemberPeer.CLASSKEY_PERSON );
			crit.addJoin(  MemberPeer.PERSON_DATA_ID, PersonDataPeer.ID );
			crit.addAscendingOrderByColumn( PersonDataPeer.LASTNAME );
			List people = MemberPeer.doSelect( crit );
			return java.util.Collections.synchronizedList( people );
		}
		catch ( TorqueException te )
		{
			throw new ApplicationRuntimeException( te );
		}
	}

	public List refreshOrganizations()
	{
		try
		{
			Criteria crit = new Criteria();
			crit.add(  MemberPeer.MEMBER_TYPE, MemberPeer.CLASSKEY_ORGANIZATION );
			crit.addJoin(  MemberPeer.ORGANIZATION_DATA_ID, OrganizationDataPeer.ID );
			crit.addAscendingOrderByColumn( OrganizationDataPeer.NAME );
			List organizations = MemberPeer.doSelect( crit );
			return java.util.Collections.synchronizedList( organizations );
		}
		catch ( TorqueException te )
		{
			throw new ApplicationRuntimeException( te );
		}
	}

	public List refreshProjects()
	{
		try
		{
			Criteria crit = new Criteria();
			crit.add(  MemberPeer.MEMBER_TYPE, MemberPeer.CLASSKEY_PROJECT );
			crit.addJoin(  MemberPeer.PROJECT_DATA_ID, ProjectDataPeer.ID );
			crit.addAscendingOrderByColumn( ProjectDataPeer.NAME );
			List projects = MemberPeer.doSelect( crit );
			return java.util.Collections.synchronizedList( projects );
		}
		catch ( TorqueException te )
		{
			throw new ApplicationRuntimeException( te );
		}
	}
	public List refreshRepresentedCountries()
	{
		try
		{
			String sql="SELECT DISTINCT Country.* FROM Country, Address	WHERE Address.country_id = Country.id";
			List villageRecords = CountryPeer.executeQuery( sql );
			List countries = CountryPeer.populateObjects( villageRecords );
			return java.util.Collections.synchronizedList( countries );
		}
		catch ( TorqueException te )
		{
			throw new ApplicationRuntimeException( te );
		}
	}

	private Configuration fileConfig()
	{
		try {
			java.io.InputStream stream = Torque.class.getClassLoader().getResourceAsStream("org/thdl/roster/roster-torque.properties");
			PropertiesConfiguration config = new PropertiesConfiguration();
			config.load( stream );
			return config;
		}
		catch ( Exception e )
		{
			throw new ApplicationRuntimeException( e.toString(), e );
		}
	}

//constructors
	public Global() throws Exception
	{
		try {
			if (! Torque.isInit() )
			{
				Torque.init( getTorqueConfig() );
			}
		}
		catch ( Exception e )
		{
			throw new ApplicationRuntimeException( e.toString(), e );
		}
	}

	//main
	public static void main( String[] args )
	{
		try {
			Global glob = new Global();
			glob.setTorqueConfig( glob.fileConfig() );
			if (! Torque.isInit() )
			{
				Torque.init( glob.getTorqueConfig() );
			}
		} 
		catch( Exception e ) { e.printStackTrace(); }
	}
}

