package org.thdl.roster.pages;

import org.thdl.roster.om.*;
import org.apache.torque.*;

public class MemberPage extends RosterPage
{
//attributes
	 private Member member;
//accessors
	public void setMember(Member member) {
		this.member = member;
	}
	public Member getMember() {
		return member;
	}
//helpers
	public boolean isHistoricalDisplayOk() throws TorqueException
	{
		ResearchInterest ri = getMember().getResearchInterest();
		Integer neg1 = new Integer( -1 );
		boolean b = ! ( neg1.equals( ri.getFocusFrom() ) || neg1.equals( ri.getFocusTo() ) );
		return b;
	}
    public MemberPage()
    {
    }
}
