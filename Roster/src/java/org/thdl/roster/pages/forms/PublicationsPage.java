package org.thdl.roster.pages.forms;

import java.io.*;
import java.util.*;

import org.thdl.roster.*;
import org.thdl.roster.om.*;
import org.thdl.roster.pages.*;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IPage;
import org.apache.tapestry.request.IUploadFile;
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
public class PublicationsPage extends MemberFormSeries {

	/**
	 *  Description of the Method
	 *
	 *@param  cycle  Description of the Parameter
	 */
	public void processForm(IRequestCycle cycle) 
	{
		 Visit visit = (Visit) getVisit();
       Member member = (Member) visit.getMember();
		 try
		 {
			 member.getPublication().save();
			 member.setPublicationKey( member.getPublication().getPrimaryKey() );
			 member.save();
		 }
		 catch (Exception e)
		 {
			 throw new ApplicationRuntimeException( e );
		 }
	}

    public void detach()
    {
        setNextPage( "Uploads" );
        super.detach();
    }

	 public PublicationsPage()
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
		  setNextPage( "Uploads" );
	 }
}

