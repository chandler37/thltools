package org.thdl.roster;

import java.util.*;
import org.thdl.roster.om.*;
import org.apache.torque.*;
import org.apache.torque.util.*;

public class RosterQuery implements java.io.Serializable
{
//attributes
	//private RosterQueryAgent queryAgent;
	private HashMap memberTypes;
	/*private String memberType; */

	private String name;	
	private String organizationalBase;
	private String anywhere;
	private List representedCountries; //stores a reference to Global.representedCountries
	private HashMap countries;
	private Country country;
	private String sql;
	private Integer selectedDiscipline;
	private Integer selectedLanguage;
	private Integer selectedCulturalArea;
	private Integer selectedPersonType;
	private Integer selectedProjectType;
	private Integer selectedOrganizationType;

//accessors

	public void setSelectedPersonType(Integer selectedPersonType) {
		this.selectedPersonType = selectedPersonType;
	}

	public void setSelectedProjectType(Integer selectedProjectType) {
		this.selectedProjectType = selectedProjectType;
	}

	public void setSelectedOrganizationType(Integer selectedOrganizationType) {
		this.selectedOrganizationType = selectedOrganizationType;
	}

	public Integer getSelectedPersonType() {
		return selectedPersonType;
	}

	public Integer getSelectedProjectType() {
		return selectedProjectType;
	}

	public Integer getSelectedOrganizationType() {
		return selectedOrganizationType;
	}

	public void setPeople(Boolean people) {
		getMemberTypes().put( MemberPeer.CLASSKEY_PERSON, people );
	}

	public void setProjects(Boolean projects) {
		getMemberTypes().put( MemberPeer.CLASSKEY_PROJECT, projects );
	}

	public void setOrganizations(Boolean organizations) {
		getMemberTypes().put( MemberPeer.CLASSKEY_ORGANIZATION, organizations );
	}

	public Boolean getPeople() {
		return (Boolean)getMemberTypes().get( MemberPeer.CLASSKEY_PERSON );
	}

	public Boolean getProjects() {
		return (Boolean)getMemberTypes().get( MemberPeer.CLASSKEY_PROJECT );
	}

	public Boolean getOrganizations() {
		return (Boolean)getMemberTypes().get( MemberPeer.CLASSKEY_ORGANIZATION );
	}
	public void setOrganizationalBase(String organizationalBase) {
		this.organizationalBase = organizationalBase;
	}
	
	public String getOrganizationalBase() {
		return organizationalBase;
	}

	public void setAnywhere(String anywhere) {
		this.anywhere = anywhere;
	}
	
	public String getAnywhere() {
		return anywhere;
	}

	public void setSelectedCulturalArea(Integer selectedCulturalArea) {
		this.selectedCulturalArea = selectedCulturalArea;
	}

	public Integer getSelectedCulturalArea() {
		return selectedCulturalArea;
	}

	public void setSelectedLanguage(Integer selectedLanguage) {
		this.selectedLanguage = selectedLanguage;
	}

	public Integer getSelectedLanguage() {
		return selectedLanguage;
	}

	public void setSelectedDiscipline(Integer selectedDiscipline) {
		this.selectedDiscipline = selectedDiscipline;
	}

	public Integer getSelectedDiscipline() {
		return selectedDiscipline;
	}

	public void setRepresentedCountries(List representedCountries) {
		this.representedCountries = representedCountries;
	}

	public List getRepresentedCountries() {
		return representedCountries;
	}

	public void setSelectedCountry( Boolean selectedCountry ) {
		getCountries().put( getCountry(), selectedCountry );	
	}

	public Boolean getSelectedCountry() {
		Country key = getCountry();
		Boolean selected = (Boolean)getCountries().get( key );
		return selected;
	}

	public void setCountries(HashMap countries) {
		this.countries = countries;
	}

	public HashMap getCountries() {
		if ( null == countries )
		{
			refreshCountries();
		}
		return countries;
	}

/* 	public void setSelectedMemberType(Boolean selectedMemberType) {
		getMemberTypes().put( getMemberType(), selectedMemberType );	
	}

	public Boolean getSelectedMemberType() {
		String key = getMemberType();
		Boolean selected = (Boolean)getMemberTypes().get( key );
		return selected;
	}
*/
	public void setMemberTypes(HashMap memberTypes) {
		this.memberTypes = memberTypes;
	}

	public HashMap getMemberTypes() {
		return memberTypes;
	}

/*	public void setMemberType(String memberType) {
		this.memberType = memberType;
	} 
		public String getMemberType() {
		return memberType;
	}

*/

	public void setName(String name) {
		this.name = name;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getName() {
		return name;
	}

	public Country getCountry() {
		return country;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getSql() {
		return sql;
	}
//helpers
	public void clear()
	{
		setCountries( null  );
		setName( null );	
		setOrganizationalBase( null );	
		setAnywhere( null );	
		setSql( null );	
		setSelectedDiscipline( null );
		setSelectedLanguage( null );
		setSelectedCulturalArea( null );
		setSelectedPersonType( null );
		setSelectedProjectType( null );
		setSelectedOrganizationType( null );
	}		
	public void refreshCountries()
	{
		setCountries( new HashMap() );
		Iterator countries = getRepresentedCountries().iterator();
		while (countries.hasNext())
		{
			getCountries().put( countries.next(), Boolean.TRUE );
		}
	}		

// constructors	
	public RosterQuery()
	{
		setCountries( new HashMap() );
		setMemberTypes( new HashMap() );
		/* getMemberTypes().put( MemberPeer.CLASSKEY_PERSON, Boolean.TRUE );
		getMemberTypes().put( MemberPeer.CLASSKEY_PROJECT, Boolean.TRUE );
		getMemberTypes().put( MemberPeer.CLASSKEY_ORGANIZATION, Boolean.TRUE ); */
		setPeople( Boolean.TRUE );
		setProjects( Boolean.TRUE );
		setOrganizations( Boolean.TRUE );
	}
	public RosterQuery( List representedCountries )
	{
		this();
		setRepresentedCountries( representedCountries );
		refreshCountries();
		//setQueryAgent( new RosterQueryAgent() );
	}
	
}
