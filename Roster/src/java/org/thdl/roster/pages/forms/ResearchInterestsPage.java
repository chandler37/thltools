package org.thdl.roster.pages.forms;

import java.util.*;
import org.thdl.roster.*;
import org.thdl.roster.om.*;
import org.thdl.roster.pages.*;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.commons.lang.exception.NestableException;
import org.apache.torque.Torque;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

/**
 *  Description of the Class
 *
 *@author     travis
 *@created    March 11, 2003
 */
public class ResearchInterestsPage extends MemberFormSeries
{

	private IPropertySelectionModel disciplineModel;
	private IPropertySelectionModel culturalAreaModel;
	private IPropertySelectionModel languageModel;
	private IPropertySelectionModel centuries;


	/**
	 *  Sets the centuries attribute of the ResearchInterestsPage object
	 *
	 *@param  centuries  The new centuries value
	 */
	public void setCenturies( IPropertySelectionModel centuries )
	{
		this.centuries = centuries;
	}


	/**
	 *  Gets the centuries attribute of the ResearchInterestsPage object
	 *
	 *@return    The centuries value
	 */
	public IPropertySelectionModel getCenturies()
	{
		if ( null == centuries )
		{
			setCenturies( buildCenturiesModel() );
		}
		return centuries;
	}


	/**
	 *  Sets the disciplineModel attribute of the ResearchInterestsPage object
	 *
	 *@param  disciplineModel  The new disciplineModel value
	 */
	public void setDisciplineModel( IPropertySelectionModel disciplineModel )
	{
		this.disciplineModel = disciplineModel;
	}


	/**
	 *  Gets the disciplineModel attribute of the ResearchInterestsPage object
	 *
	 *@return    The disciplineModel value
	 */
	public IPropertySelectionModel getDisciplineModel()
	{
		if ( disciplineModel == null )
		{
			setDisciplineModel( buildDisciplineModel() );
		}
		return disciplineModel;
	}

	/**
	 *  Sets the culturalAreaModel attribute of the ResearchInterestsPage object
	 *
	 *@param  culturalAreaModel  The new culturalAreaModel value
	 */
	public void setCulturalAreaModel( IPropertySelectionModel culturalAreaModel )
	{
		this.culturalAreaModel = culturalAreaModel;
	}


	/**
	 *  Gets the culturalAreaModel attribute of the ResearchInterestsPage object
	 *
	 *@return    The culturalAreaModel value
	 */
	public IPropertySelectionModel getCulturalAreaModel()
	{
		if ( culturalAreaModel == null )
		{
			setCulturalAreaModel( buildCulturalAreaModel() );
		}
		return culturalAreaModel;
	}

	/**
	 *  Sets the languageModel attribute of the ResearchInterestsPage object
	 *
	 *@param  languageModel  The new languageModel value
	 */
	public void setLanguageModel( IPropertySelectionModel languageModel )
	{
		this.languageModel = languageModel;
	}


	/**
	 *  Gets the languageModel attribute of the ResearchInterestsPage object
	 *
	 *@return    The languageModel value
	 */
	public IPropertySelectionModel getLanguageModel()
	{
		if ( languageModel == null )
		{
			setLanguageModel( buildLanguageModel() );
		}
		return languageModel;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public IPropertySelectionModel buildDisciplineModel()
	{
		LinkedList list;
		try
		{
			Criteria crit = new Criteria();
			crit.addAscendingOrderByColumn( DisciplinePeer.DISCIPLINE );
			list = new LinkedList( BaseDisciplinePeer.doSelect( crit ) );
		}
		catch ( TorqueException te )
		{
			throw new ApplicationRuntimeException( "Torque Exception says: " + te.getMessage(), te );
		}
		EntitySelectionModel disciplineModel = new EntitySelectionModel();
		ListIterator looper = list.listIterator( 0 );
		while ( looper.hasNext() )
		{
			Discipline discipline = (Discipline) looper.next();
			disciplineModel.add( discipline.getId(), discipline.getDiscipline() );
		}
		return disciplineModel;
	}


	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
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
		while ( looper.hasNext() )
		{
			CulturalArea culturalArea = (CulturalArea) looper.next();
			culturalAreaModel.add( culturalArea.getId(), culturalArea.getCulturalArea() );
		}
		return culturalAreaModel;
	}


	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
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
		while ( looper.hasNext() )
		{
			Language language = (Language) looper.next();
			languageModel.add( language.getId(), language.getLanguage() );
		}
		return languageModel;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public IPropertySelectionModel buildCenturiesModel()
	{
		EntitySelectionModel centuriesModel = new EntitySelectionModel();
		String centuries[] = { "","twenty-first", "twentieth", "nineteenth", "eighteenth", "seventeenth", "sixteenth", "fifteenth", "fourteenth", "thirteenth", "twelfth", "eleventh", "tenth", "ninth", "eighth", "seventh", "pre-seventh" };
		int centIntegers[] = { -1, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 0 };
		for ( int i = 0; i < centuries.length; i++ )
		{
			centuriesModel.add( new Integer( centIntegers[i] ), centuries[i] );
		}
		return centuriesModel;
	}

	/**
	 *  This is the form listener that saves the 
	 *
	 *@param  cycle  Description of the Parameter
	 */
	public void processForm( IRequestCycle cycle )
	{
		 Visit visit = (Visit) getVisit();
       Member member = (Member) visit.getMember();
		 try
		 {
				List disciplineIds = member.getResearchInterest().getDisciplineIdList();
				List languageIds = member.getResearchInterest().getLanguageIdList();
				List cultAreaIds = member.getResearchInterest().getCulturalAreaIdList();

				member.getResearchInterest().save();
				member.setResearchInterestKey( member.getResearchInterest().getPrimaryKey() );
				member.save();
				
				Criteria crit;
				List torqueObjects;
				Integer foreignKey2;
				
				crit = new Criteria();
				crit.add( ResearchInterestDisciplinePeer.RESEARCH_INTEREST_ID, member.getResearchInterest().getId() );
				torqueObjects = ResearchInterestDisciplinePeer.doSelect( crit );
				foreignKey2 =  member.getResearchInterest().getId();
				ResearchInterestDiscipline template = new ResearchInterestDiscipline();
				PaletteMergeTableProcessor.processPalette( disciplineIds, torqueObjects, foreignKey2, template );

				crit = new Criteria();
				crit.add( ResearchInterestLanguagePeer.RESEARCH_INTEREST_ID, member.getResearchInterest().getId() );
				torqueObjects = ResearchInterestLanguagePeer.doSelect( crit );
				foreignKey2 =  member.getResearchInterest().getId();
				ResearchInterestLanguage template2 = new ResearchInterestLanguage();
				PaletteMergeTableProcessor.processPalette( languageIds, torqueObjects, foreignKey2, template2 );

				crit = new Criteria();
				crit.add( ResearchInterestCulturalAreaPeer.RESEARCH_INTEREST_ID, member.getResearchInterest().getId() );
				torqueObjects = ResearchInterestCulturalAreaPeer.doSelect( crit );
				foreignKey2 =  member.getResearchInterest().getId();
				ResearchInterestCulturalArea template3 = new ResearchInterestCulturalArea();
				PaletteMergeTableProcessor.processPalette( cultAreaIds, torqueObjects, foreignKey2, template3 );
		 }
		 catch (Exception e)
		 {
			 throw new ApplicationRuntimeException( e.getMessage(), e );
		 }			 
	}

    public void detach()
    {
        setNextPage( "Works" );
		  super.detach();
    }

	/**  Constructor for the ResearchInterestsPage object */
	public ResearchInterestsPage() 
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
		setNextPage( "Works" );
	}
}

