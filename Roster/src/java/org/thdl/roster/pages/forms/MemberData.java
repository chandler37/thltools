package org.thdl.roster.pages.forms;

import java.util.*;

import org.apache.tapestry.*;
import org.apache.tapestry.html.*;
import org.apache.tapestry.form.*;
import org.apache.tapestry.contrib.palette.SortMode;

import org.apache.torque.*;
import org.apache.torque.om.*;
import org.apache.torque.util.*;

import org.thdl.roster.*;
import org.thdl.roster.om.*;
import org.thdl.roster.pages.*;

public class MemberData extends MemberFormSeries
{
//attributes
	// private List selectedMemberTypes;
	private IPropertySelectionModel personTypeModel;
	private IPropertySelectionModel projectTypeModel;
	private IPropertySelectionModel organizationTypeModel;
//accessors
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

/*     public List getSelectedMemberTypes()
    {
        return selectedMemberTypes;
    }

    public void setSelectedMemberTypes(List selectedMemberTypes)
    {
        this.selectedMemberTypes = selectedMemberTypes;
    }
 */
//helpers
//tapestry listeners
	public void processForm(IRequestCycle cycle)
    {
		 Visit visit = (Visit) getVisit();
       Member member = (Member) visit.getMember();
		 if ( member instanceof Person )
		 {
			 try
			 {
					Person person = (Person) member;
								
					//don't move this line!
					List flatDataIds = person.getPersonData().getPersonTypeIdList();
									
					Integer userId = new Integer( visit.getThdlUser().getId() );
					person.getPersonData().setThdlUserId( userId );
					person.getPersonData().save();
					person.setPersonDataKey( member.getPersonData().getPrimaryKey() );
					person.save();
					
					Criteria crit = new Criteria();
					crit.add( PersonPersonTypePeer.PERSON_DATA_ID, person.getPersonData().getId() );
					List torqueObjects = PersonPersonTypePeer.doSelect( crit );
					
					Integer memberDataId =  person.getPersonData().getId();
					
					PersonPersonType template = new PersonPersonType();
					
					PaletteMergeTableProcessor.processPalette( flatDataIds, torqueObjects, memberDataId, template );
			 }
			 catch ( RosterMemberTypeException rmte )
			 {
				 throw new ApplicationRuntimeException( rmte );
			 }
			 catch (Exception e)
			 {
				 throw new ApplicationRuntimeException( e );
			 }
		 }
		 if ( member instanceof Organization )
		 {
			 try
			 {
					Organization organization = (Organization) member;

					//don't move this line!
					List flatDataIds = organization.getOrganizationData().getOrganizationTypeIdList();

					organization.getOrganizationData().save();
					organization.setOrganizationDataKey( member.getOrganizationData().getPrimaryKey() );
					organization.save();
	
					Criteria crit = new Criteria();
					crit.add( OrganizationOrganizationTypePeer.ORGANIZATION_DATA_ID, organization.getOrganizationData().getId() );
					List torqueObjects = OrganizationOrganizationTypePeer.doSelect( crit );
					Integer memberDataId =  organization.getOrganizationData().getId();
					OrganizationOrganizationType template = new OrganizationOrganizationType();
					PaletteMergeTableProcessor.processPalette( flatDataIds, torqueObjects, memberDataId, template );
			 }
			 catch ( RosterMemberTypeException rmte )
			 {
				 throw new ApplicationRuntimeException( rmte );
			 }
			 catch (Exception e)
			 {
				 throw new ApplicationRuntimeException( e );
			 }
		 }
		 if ( member instanceof Project )
		 {
			 try
			 {
					Project project = (Project) member;

					//don't move this line!
					List flatDataIds = project.getProjectData().getProjectTypeIdList();

					project.getProjectData().save();
					project.setProjectDataKey( member.getProjectData().getPrimaryKey() );
					project.save();
	
					Criteria crit = new Criteria();
					crit.add( ProjectProjectTypePeer.PROJECT_DATA_ID, project.getProjectData().getId() );
					List torqueObjects = ProjectProjectTypePeer.doSelect( crit );
					Integer memberDataId =  project.getProjectData().getId();
					ProjectProjectType template = new ProjectProjectType();
					PaletteMergeTableProcessor.processPalette( flatDataIds, torqueObjects, memberDataId, template );
			 }
			 catch ( RosterMemberTypeException rmte )
			 {
				 throw new ApplicationRuntimeException( rmte );
			 }
			 catch (Exception e)
			 {
				 throw new ApplicationRuntimeException( e );
			 }
		 }
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
    public void detach()
    {
        setNextPage( "Activities" );
        super.detach();
    }

//constructors
	public MemberData()
	{
		try
		{
			if ( !Torque.isInit() )
			{
				Global global = (Global) getGlobal();
				Torque.init( global.getTorqueConfig() );
			}
		}
		catch ( TorqueException te )
		{
			throw new ApplicationRuntimeException( "Torque Exception says: " + te.getMessage(), te );
		}
		setNextPage( "Activities" );
	}

}