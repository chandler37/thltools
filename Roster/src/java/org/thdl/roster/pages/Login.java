package org.thdl.roster.pages;

import java.util.*;
import java.text.*;
import org.apache.torque.*;
import org.apache.tapestry.*;
import org.apache.tapestry.html.BasePage;

import org.thdl.users.*;
import org.thdl.roster.*;
import org.thdl.roster.om.*;
import org.thdl.roster.components.Border;
import org.apache.log4j.*;

public class Login extends RosterPage
{
//attributes
	private boolean newUser;
	private String login;
	private String password;
	private String passwordCopy;
	private String forward;
//accessors
	public void setNewUser(boolean newUser) {
		this.newUser = newUser;
	}
	public boolean getNewUser() {
		return newUser;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLogin() {
		return login;
	}
	public String getPassword() {
		return password;
	}
	public void setPasswordCopy(String passwordCopy) {
		this.passwordCopy = passwordCopy;
	}
	public String getPasswordCopy() {
		return passwordCopy;
	}
	public void setForward(String forward) {
		this.forward = forward;
		Tapestry.fireObservedChange( this, "forward", forward );
	}
	public String getForward() {
		if (null == forward)
		{
			setForward( "Home" );
		}
		return forward;
	}
//helpers
	public boolean validateUser()
	{
		boolean rVal = false;
		ThdlUser thdlUser = null;
		try
		{
			thdlUser = ThdlUserRepository.getInstance().validate( getLogin(), getPassword() );
			rVal = true;
			Visit visit = (Visit) getVisit(); 
			visit.setThdlUser( thdlUser );
			visit.setAuthenticated( true );
		}
		catch (ThdlUserRepositoryException ture)
		{
			setMessage( ture.getMessage() );
		}
		
		return rVal;
	}
	//tapestry helpers
	public void detach()
	{
		setLogin( null );
		setPassword( null );
		setPasswordCopy( null );
		setMessage( null );
		setWarning( null );
		setNewUser( false );
		super.detach();
	}
	//tapestry listeners
	public void registerNewUser(IRequestCycle cycle) 
	{
		setNewUser( true );
	}
	public void loginFormSubmit(IRequestCycle cycle) 
	{
		if ( validateUser() )
		{
			proceed( cycle );
		}
	 }
	private void proceed( IRequestCycle cycle )
	{
			Visit visit = (Visit) getVisit(); 
			
			//do some logging
			String name = visit.getThdlUser().getFirstname() + " " + visit.getThdlUser().getLastname() ;
			Date now = new Date( System.currentTimeMillis() );
			String date = new SimpleDateFormat().format( now );
			Logger logger = Logger.getLogger( "org.thdl.roster.pages" );
			logger.info( name + " logged in at " + date );
			Member member = (Member) visit.getMember();
			if ( null != member && member.isNew() )
			{
				try
				{
					member.getContactInfo().setContactName( visit.getThdlUser().getFirstname() + " " + visit.getThdlUser().getLastname() );
					member.getContactInfo().setEmail( visit.getThdlUser().getEmail() );
				}
				catch ( TorqueException te )
				{
					throw new ApplicationRuntimeException( te );
				}
			}
			RosterPage page = (RosterPage) cycle.getPage( getForward() );
			page.validate( cycle );
			cycle.activate( page );		
	}
	public void sendInfoFormSubmit(IRequestCycle cycle) 
	{
		Visit visit = (Visit) getVisit(); 
	}
	public void newUserFormSubmit(IRequestCycle cycle) 
	{
		Visit visit = (Visit) getVisit(); 
		try
		{
			boolean insertUser=false;
			if ( ThdlUserRepository.getInstance().doesNotAlreadyExist( visit.getThdlUser() ) )
			{
				insertUser=true;
			}
			if ( ! getPassword().equals( getPasswordCopy() ) )
			{
				insertUser=false;
				setWarning( "Your two password entries were not the same. Please re-enter your password." );
			}
			else
			{
				visit.getThdlUser().setPassword( getPassword() );
			}

			if ( insertUser )
			{
				ThdlUserRepository.getInstance().insertUser( visit.getThdlUser() );
				setMessage( "Your new user entry was successfully submitted." );
				visit.setAuthenticated( true );
				proceed( cycle );
			}
			else
			{
				setMessage( "Your new user entry was not submitted." );
			}
		}
		catch (UsernameAlreadyExistsException uaee)
		{
			setWarning( uaee.getMessage() );
		}
		catch (UserEmailAlreadyExistsException ueaee)
		{
			setWarning( ueaee.getMessage() );
		}
		catch (ThdlUserRepositoryException ture)
		{
			setWarning( ture.getMessage() );
		}
	}
	 public Login()
	 {
		 setNewUser( false );
	 }
}