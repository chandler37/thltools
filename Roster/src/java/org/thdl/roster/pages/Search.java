package org.thdl.roster.pages;

import java.util.*;
import org.apache.tapestry.*;
import org.apache.tapestry.form.*;
import org.apache.torque.*;
import org.apache.torque.util.*;
import org.thdl.roster.*;
import org.thdl.roster.om.*;
import org.apache.log4j.*;

public class Search extends RosterPage
{
//attributes
	private RosterQuery rosterQuery;
	private IPropertySelectionModel disciplineModel;
	private IPropertySelectionModel culturalAreaModel;
	private IPropertySelectionModel languageModel;
	private IPropertySelectionModel personTypeModel;
	private IPropertySelectionModel projectTypeModel;
	private IPropertySelectionModel organizationTypeModel;

//accessors
	public void setCulturalAreaModel( IPropertySelectionModel culturalAreaModel )
	{
		this.culturalAreaModel = culturalAreaModel;
	}

	public IPropertySelectionModel getCulturalAreaModel()
	{
		if ( culturalAreaModel == null )
		{
			setCulturalAreaModel( buildCulturalAreaModel() );
		}
		return culturalAreaModel;
	}

	public void setLanguageModel( IPropertySelectionModel languageModel )
	{
		this.languageModel = languageModel;
	}

	public IPropertySelectionModel getLanguageModel()
	{
		if ( languageModel == null )
		{
			setLanguageModel( buildLanguageModel() );
		}
		return languageModel;
	}

	public void setDisciplineModel( IPropertySelectionModel disciplineModel )
	{
		this.disciplineModel = disciplineModel;
	}

	public IPropertySelectionModel getDisciplineModel()
	{
		if ( disciplineModel == null )
		{
			setDisciplineModel( buildDisciplineModel() );
		}
		return disciplineModel;
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
	public IPropertySelectionModel buildDisciplineModel()
	{
		List disciplines;
		try
		{
			Criteria crit = new Criteria();
			crit.addAscendingOrderByColumn( DisciplinePeer.DISCIPLINE );
			disciplines = new LinkedList( BaseDisciplinePeer.doSelect( crit ) );
		}
		catch ( TorqueException te )
		{
			throw new ApplicationRuntimeException( "Torque Exception says: " + te.getMessage(), te );
		}
		EntitySelectionModel disciplineModel = new EntitySelectionModel();
		ListIterator looper = disciplines.listIterator( 0 );
		disciplineModel.add( null , "All Disciplines" );
		while ( looper.hasNext() )
		{
			Discipline discipline = (Discipline) looper.next();
			disciplineModel.add( discipline.getId(), discipline.getDiscipline() );
		}
		return disciplineModel;
	}
	public IPropertySelectionModel buildCulturalAreaModel()
	{
		LinkedList list;
		try
		{
			Criteria crit = new Criteria();
			crit.addAscendingOrderByColumn( CulturalAreaPeer.CULTURAL_AREA );
			list = new LinkedList( BaseCulturalAreaPeer.doSelect( crit ) );
		}
		catch ( TorqueException te )
		{
			throw new ApplicationRuntimeException( "Torque Exception says: " + te.getMessage(), te );
		}
		EntitySelectionModel culturalAreaModel = new EntitySelectionModel();
		ListIterator looper = list.listIterator( 0 );
		culturalAreaModel.add( null, "All Areas" );
		while ( looper.hasNext() )
		{
			CulturalArea culturalArea = (CulturalArea) looper.next();
			culturalAreaModel.add( culturalArea.getId(), culturalArea.getCulturalArea() );
		}
		return culturalAreaModel;
	}


	public IPropertySelectionModel buildLanguageModel()
	{
		LinkedList list;
		try
		{
			Criteria crit = new Criteria();
			crit.addAscendingOrderByColumn( LanguagePeer.LANGUAGE );
			list = new LinkedList( BaseLanguagePeer.doSelect( crit ) );
		}
		catch ( TorqueException te )
		{
			throw new ApplicationRuntimeException( "Torque Exception says: " + te.getMessage(), te );
		}
		EntitySelectionModel languageModel = new EntitySelectionModel();
		ListIterator looper = list.listIterator( 0 );
		languageModel.add( null, "All Languages" );
		while ( looper.hasNext() )
		{
			Language language = (Language) looper.next();
			languageModel.add( language.getId(), language.getLanguage() );
		}
		return languageModel;
	}


	public void setPersonTypeModel(IPropertySelectionModel personTypeModel) 
	{
		this.personTypeModel = personTypeModel;
	}

    public IPropertySelectionModel getPersonTypeModel()
    {
        if (personTypeModel == null)
		  {
			  setPersonTypeModel( buildPersonTypeModel() );
		  }
        return personTypeModel;
    }

	 public void setProjectTypeModel(IPropertySelectionModel projectTypeModel) 
	{
		this.projectTypeModel = projectTypeModel;
	}

   public IPropertySelectionModel getProjectTypeModel()
    {
        if (projectTypeModel == null)
		  {
			  setProjectTypeModel( buildProjectTypeModel() );
		  }
        return projectTypeModel;
    }

	 public void setOrganizationTypeModel(IPropertySelectionModel organizationTypeModel) 
	{
		this.organizationTypeModel = organizationTypeModel;
	}
    
	 public IPropertySelectionModel getOrganizationTypeModel()
    {
        if (organizationTypeModel == null)
		  {
			  setOrganizationTypeModel( buildOrganizationTypeModel() );
		  }
        return organizationTypeModel;
    }

	public IPropertySelectionModel buildPersonTypeModel()
	{
		try 
		{
			EntitySelectionModel personTypeModel = new EntitySelectionModel();
			Criteria crit = new Criteria();
			crit.addAscendingOrderByColumn( PersonTypePeer.PERSON_TYPE );
			LinkedList list = new LinkedList( PersonTypePeer.doSelect( crit ) );
			PersonType personType;
			ListIterator looper = list.listIterator( 0 );
			personTypeModel.add( null, "All People" );
			while ( looper.hasNext() )
			{
				personType = (PersonType) looper.next();
				personTypeModel.add( personType.getId(), personType.getPersonType() );
			}
			return personTypeModel;
		}
		catch (TorqueException te) {
			throw new ApplicationRuntimeException( "Torque Exception says: " + te.getMessage(), te );
		}
	}

	public IPropertySelectionModel buildProjectTypeModel()
	{
		try 
		{
			EntitySelectionModel projectTypeModel = new EntitySelectionModel();
			Criteria crit = new Criteria();
			crit.addAscendingOrderByColumn( ProjectTypePeer.PROJECT_TYPE );
			LinkedList list = new LinkedList( ProjectTypePeer.doSelect( crit ) );
			ProjectType projectType;
			ListIterator looper = list.listIterator( 0 );
			projectTypeModel.add( null, "All Projects" );
			while ( looper.hasNext() )
			{
				projectType = (ProjectType) looper.next();
				projectTypeModel.add( projectType.getId(), projectType.getProjectType() );
			}
			return projectTypeModel;
		}
		catch (TorqueException te) {
			throw new ApplicationRuntimeException( "Torque Exception says: " + te.getMessage(), te );
		}
	}

	public IPropertySelectionModel buildOrganizationTypeModel()
	{
		try 
		{
			EntitySelectionModel organizationTypeModel = new EntitySelectionModel();
			Criteria crit = new Criteria();
			crit.addAscendingOrderByColumn( OrganizationTypePeer.ORGANIZATION_TYPE );
			LinkedList list = new LinkedList( OrganizationTypePeer.doSelect( crit ) );
			OrganizationType organizationType;
			ListIterator looper = list.listIterator( 0 );
			organizationTypeModel.add( null, "All Organizations");
			while ( looper.hasNext() )
			{
				organizationType = (OrganizationType) looper.next();
				organizationTypeModel.add( organizationType.getId(), organizationType.getOrganizationType() );
			}
			return organizationTypeModel;
		}
		catch (TorqueException te) {
			throw new ApplicationRuntimeException( "Torque Exception says: " + te.getMessage(), te );
		}
	}

//listeners

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
		if ( members != null && members.size() > 0 )
		{
			MembersPage page = (MembersPage) cycle.getPage( "SearchResults" );
			page.setMembers( members );
			cycle.activate( page );
		}
		else
		{
			setWarning( "Search returned zero results. Please try again" );
		}
	}
 	public void detach()
	{
		getRosterQuery().clear();
		super.detach();
	}

//constructors

	public Search() 
	{ 
		super();
	}
}
