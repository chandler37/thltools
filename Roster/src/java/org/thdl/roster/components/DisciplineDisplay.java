package org.thdl.roster.components;

import java.util.*;

import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.*;
import org.apache.torque.util.Criteria;
import org.apache.torque.*;

import org.thdl.roster.om.*;
import org.thdl.roster.*;

public class DisciplineDisplay extends BaseComponent
{
//attributes
	private ResearchInterest researchInterest;
	private String Discipline;
	private int index;
//accessors
	public void setResearchInterest(ResearchInterest researchInterest) {
		this.researchInterest = researchInterest;
	}
	public ResearchInterest getResearchInterest() {
		return researchInterest;
	}
	public void setDiscipline(String Discipline) {
		this.Discipline = Discipline;
	}
	public String getDiscipline() {
		return Discipline;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getIndex() {
			return index;
	}
//synthetic properties
	public List getDisciplineList()
	{
		LinkedList disciplineStrings = new LinkedList();
		try
		{
			Criteria crit = new Criteria();
			crit.add(  ResearchInterestDisciplinePeer.RESEARCH_INTEREST_ID, getResearchInterest().getId() );
			crit.addAscendingOrderByColumn( ResearchInterestDisciplinePeer.RELEVANCE );
			List ridList = ResearchInterestDisciplinePeer.doSelect( crit );
			ListIterator looper = ridList.listIterator();
			while( looper.hasNext() )
			{
				ResearchInterestDiscipline rid = (ResearchInterestDiscipline) looper.next();
				String discipline = rid.getDiscipline().getDiscipline();
				disciplineStrings.add( discipline );
			}
		}
		catch ( TorqueException te )
		{
			throw new ApplicationRuntimeException( te );
		}
		return disciplineStrings;
	}
//constructors
	public DisciplineDisplay()
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