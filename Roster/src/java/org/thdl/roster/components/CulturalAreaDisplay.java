package org.thdl.roster.components;

import java.util.*;

import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.*;
import org.apache.torque.util.Criteria;
import org.apache.torque.*;

import org.thdl.roster.om.*;
import org.thdl.roster.*;

public class CulturalAreaDisplay extends BaseComponent
{
//attributes
	private ResearchInterest researchInterest;
	private String CulturalArea;
	private int index;
//accessors
	public void setResearchInterest(ResearchInterest researchInterest) {
		this.researchInterest = researchInterest;
	}
	public ResearchInterest getResearchInterest() {
		return researchInterest;
	}
	public void setCulturalArea(String CulturalArea) {
		this.CulturalArea = CulturalArea;
	}
	public String getCulturalArea() {
		return CulturalArea;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getIndex() {
			return index;
	}
//synthetic properties
	public List getCulturalAreaList()
	{
		LinkedList culturalAreaStrings = new LinkedList();
		try
		{
			Criteria crit = new Criteria();
			crit.add(  ResearchInterestCulturalAreaPeer.RESEARCH_INTEREST_ID, getResearchInterest().getId() );
			crit.addAscendingOrderByColumn( ResearchInterestCulturalAreaPeer.RELEVANCE );
			List ridList = ResearchInterestCulturalAreaPeer.doSelect( crit );
			ListIterator looper = ridList.listIterator();
			while( looper.hasNext() )
			{
				ResearchInterestCulturalArea rid = (ResearchInterestCulturalArea) looper.next();
				String culturalArea = rid.getCulturalArea().getCulturalArea();
				culturalAreaStrings.add( culturalArea );
			}
		}
		catch ( TorqueException te )
		{
			throw new ApplicationRuntimeException( te );
		}
		return culturalAreaStrings;
	}
//constructors
	public CulturalAreaDisplay()
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