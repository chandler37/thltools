package org.thdl.roster.pages.forms;

import java.util.List;
import java.util.LinkedList;
import org.apache.torque.*;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.ApplicationRuntimeException;
import org.thdl.roster.*;
import org.thdl.roster.om.*;

public class ContactInfoPage extends MemberFormSeries
{

//tapestry listeners
	public void processForm(IRequestCycle cycle)
    {
		 Visit visit = (Visit) getVisit();
       Member member = (Member) visit.getMember();
		 member.save(  visit.getThdlUser().getId()  );
		try
		 {
			 if ( member instanceof Person )
			 {
				 Person person = (Person)member;
				 person.getPersonData().setFirstname( visit.getThdlUser().getFirstname() );
				 person.getPersonData().setLastname( visit.getThdlUser().getLastname() );
				 person.getPersonData().setMiddlename( visit.getThdlUser().getMiddlename() );
			}
			member.getContactInfo().getAddress().save();			 
			member.getContactInfo().getPhoneRelatedByPhone().save();
			member.getContactInfo().getPhoneRelatedByFax().save();
			member.getContactInfo().setAddressKey( member.getContactInfo().getAddress().getPrimaryKey() );
			member.getContactInfo().setPhoneRelatedByPhoneKey( member.getContactInfo().getPhoneRelatedByPhone().getPrimaryKey() );
			member.getContactInfo().setPhoneRelatedByFaxKey( member.getContactInfo().getPhoneRelatedByFax().getPrimaryKey() );
			member.getContactInfo().save();
			member.setContactInfoKey( member.getContactInfo().getPrimaryKey() );
			member.save();
		 }
		 catch (Exception e )
		 {
			 throw new ApplicationRuntimeException(  e );
		 }
    }
    public void detach()
    {
        setNextPage( "Background" );
        super.detach();
    }

//constructors
	public ContactInfoPage()
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
		setNextPage("Background");
	}
}