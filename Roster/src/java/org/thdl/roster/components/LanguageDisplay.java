package org.thdl.roster.components;

import java.util.*;

import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.*;
import org.apache.torque.util.Criteria;
import org.apache.torque.*;

import org.thdl.roster.om.*;
import org.thdl.roster.*;

public class LanguageDisplay extends BaseComponent
{
//attributes
	private ResearchInterest researchInterest;
	private String Language;
	private int index;
//accessors
	public void setResearchInterest(ResearchInterest researchInterest) {
		this.researchInterest = researchInterest;
	}
	public ResearchInterest getResearchInterest() {
		return researchInterest;
	}
	public void setLanguage(String Language) {
		this.Language = Language;
	}
	public String getLanguage() {
		return Language;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getIndex() {
			return index;
	}
//synthetic properties
	public List getLanguageList()
	{
		LinkedList languageStrings = new LinkedList();
		try
		{
			Criteria crit = new Criteria();
			crit.add(  ResearchInterestLanguagePeer.RESEARCH_INTEREST_ID, getResearchInterest().getId() );
			crit.addAscendingOrderByColumn( ResearchInterestLanguagePeer.RELEVANCE );
			List ridList = ResearchInterestLanguagePeer.doSelect( crit );
			ListIterator looper = ridList.listIterator();
			while( looper.hasNext() )
			{
				ResearchInterestLanguage rid = (ResearchInterestLanguage) looper.next();
				String language = rid.getLanguage().getLanguage();
				languageStrings.add( language );
			}
		}
		catch ( TorqueException te )
		{
			throw new ApplicationRuntimeException( te );
		}
		return languageStrings;
	}
//constructors
	public LanguageDisplay()
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