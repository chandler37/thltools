
package org.thdl.roster.om;


import org.apache.torque.om.Persistent;
import org.apache.torque.*;
import org.apache.torque.util.*;
import java.util.*;

/** 
 * The skeleton for this class was autogenerated by Torque on:
 *
 * [Thu Feb 27 15:11:05 EST 2003]
 *
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public  class ResearchInterest extends org.thdl.roster.om.BaseResearchInterest implements java.io.Serializable, Persistent
{
//attributes
	private List disciplineIdList;
	private List languageIdList;
	private List culturalAreaIdList;
//accessors
	public void setId(Integer v) throws TorqueException
	{
		super.setId( v );
		setDisciplineIdList( buildDisciplineIdList() );
		setLanguageIdList( buildLanguageIdList() );
		setCulturalAreaIdList( buildCulturalAreaIdList() );
	}
	public void setDisciplineIdList(List disciplineIdList) throws TorqueException {
		//if (disciplineIdList == null) throw new TorqueException( "attempt to set discipline IdList to null" );
		this.disciplineIdList = disciplineIdList;
	}
	public void setLanguageIdList(List languageIdList) {
		this.languageIdList = languageIdList;
	}
	public void setCulturalAreaIdList(List culturalAreaIdList) {
		this.culturalAreaIdList = culturalAreaIdList;
	}
	public List getDisciplineIdList() throws TorqueException {
		return disciplineIdList;
	}
	public List getLanguageIdList() throws TorqueException {
		return languageIdList;
	}
	public List getCulturalAreaIdList() throws TorqueException {
		return culturalAreaIdList;
	}
// helpers
	public List buildDisciplineIdList() throws TorqueException
	{
			Criteria crit = new Criteria();
			crit.add(  ResearchInterestDisciplinePeer.RESEARCH_INTEREST_ID, getId() );
			crit.addAscendingOrderByColumn( ResearchInterestDisciplinePeer.RELEVANCE );
			List idSourceList = ResearchInterestDisciplinePeer.doSelect( crit );
			LinkedList newList = new LinkedList();
			ListIterator iterator = idSourceList.listIterator( 0 );
			while ( iterator.hasNext() )
			{
				ResearchInterestDiscipline rid = (ResearchInterestDiscipline) iterator.next();
				Integer id = rid.getDiscipline().getId();
				newList.add( id );
			}
			return newList;
	}
	public List buildLanguageIdList() throws TorqueException
	{
			Criteria crit = new Criteria();
			crit.add(  ResearchInterestLanguagePeer.RESEARCH_INTEREST_ID, getId() );
			crit.addAscendingOrderByColumn( ResearchInterestLanguagePeer.RELEVANCE );
			List idSourceList = ResearchInterestLanguagePeer.doSelect( crit );
			LinkedList newList = new LinkedList();
			ListIterator iterator = idSourceList.listIterator( 0 );
			while ( iterator.hasNext() )
			{
				ResearchInterestLanguage rid = (ResearchInterestLanguage) iterator.next();
				Integer id = rid.getLanguage().getId();
				newList.add( id );
			}
			return newList;
	}
	public List buildCulturalAreaIdList() throws TorqueException
	{
			Criteria crit = new Criteria();
			crit.add(  ResearchInterestCulturalAreaPeer.RESEARCH_INTEREST_ID, getId() );
			crit.addAscendingOrderByColumn( ResearchInterestCulturalAreaPeer.RELEVANCE );
			List idSourceList = ResearchInterestCulturalAreaPeer.doSelect( crit );
			LinkedList newList = new LinkedList();
			ListIterator iterator = idSourceList.listIterator( 0 );
			while ( iterator.hasNext() )
			{
				ResearchInterestCulturalArea rid = (ResearchInterestCulturalArea) iterator.next();
				Integer id = rid.getCulturalArea().getId();
				newList.add( id );
			}
			return newList;
	}
	public ResearchInterest()
	{
	}
		
}