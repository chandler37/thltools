package org.thdl.roster;

import java.io.Serializable;
import org.thdl.roster.om.*;
import org.thdl.users.*;
import org.apache.torque.Torque;
import org.apache.torque.TorqueException;

public class Visit implements Serializable
{
//attributes
	private ThdlUser thdlUser;
	private boolean authenticated = false;
	private RosterMember member;
	private String test;
	private String token;
//accessors
	public void setToken(String token) {
		this.token = token;
	}
	public String getToken() {
		return token;
	}
	public void setTest(String test) {
		this.test = test;
	}
	public String getTest() {
		return test;
	}
	public void setMember(RosterMember member) {
		this.member = member;
	}
	public RosterMember getMember() {
		return member;
	}
	public void setThdlUser(ThdlUser thdlUser) {
		this.thdlUser = thdlUser;
	}

	public ThdlUser getThdlUser() {
		return thdlUser;
	}
	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}
	public boolean isAuthenticated() {
		return authenticated;
	}

//helpers
	public String getSnapshot() throws TorqueException
	{
		StringBuffer snapshot = new StringBuffer();
		snapshot.append( getThdlUser() + " name: '" + getThdlUser().getFirstname() +"'");
		snapshot.append( "<br />" );
		snapshot.append( getMember() );
		snapshot.append( "<br />" );
		snapshot.append( isAuthenticated() );
		snapshot.append( "<br />" );
		Person p = (Person) getMember();
		if (null != p)
		{
			snapshot.append( p.getPersonData().getPersonTypeIdList() );
			snapshot.append( "<br />" );
		}
		
		return snapshot.toString();
	}
//constructors
 	public Visit()
	{
		setThdlUser( new ThdlUser() );
		setAuthenticated( false );
	}
}