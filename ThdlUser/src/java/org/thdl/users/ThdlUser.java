package org.thdl.users;

import java.io.Serializable;
import java.util.*;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 2, 2003
 */
public class ThdlUser implements Serializable
{
	private Integer id;
	private String firstname;
	private String lastname;
	private String middlename;
	private String email;
	private String username;
	private String creditAttributionTag;
	private String password;
	private String passwordHint;
	private String roles;


	/**
	 *  Sets the roles attribute of the ThdlUser object
	 *
	 * @param  roles  The new roles value
	 * @since
	 */
	public void setRoles( String roles )
	{
		this.roles = roles;
	}


	/**
	 *  Sets the creditAttributionTag attribute of the ThdlUser object
	 *
	 * @param  creditAttributionTag  The new creditAttributionTag value
	 * @since
	 */
	public void setCreditAttributionTag( String creditAttributionTag )
	{
		this.creditAttributionTag = creditAttributionTag;
	}


	/**
	 *  Sets the id attribute of the ThdlUser object
	 *
	 * @param  id  The new id value
	 * @since
	 */
	public void setId( Integer id )
	{
		this.id = id;
	}


	/**
	 *  Sets the firstname attribute of the ThdlUser object
	 *
	 * @param  firstname  The new firstname value
	 * @since
	 */
	public void setFirstname( String firstname )
	{
		this.firstname = firstname;
	}


	/**
	 *  Sets the lastname attribute of the ThdlUser object
	 *
	 * @param  lastname  The new lastname value
	 * @since
	 */
	public void setLastname( String lastname )
	{
		this.lastname = lastname;
	}


	/**
	 *  Sets the middlename attribute of the ThdlUser object
	 *
	 * @param  middlename  The new middlename value
	 * @since
	 */
	public void setMiddlename( String middlename )
	{
		this.middlename = middlename;
	}


	/**
	 *  Sets the email attribute of the ThdlUser object
	 *
	 * @param  email  The new email value
	 * @since
	 */
	public void setEmail( String email )
	{
		this.email = email;
	}


	/**
	 *  Sets the username attribute of the ThdlUser object
	 *
	 * @param  username  The new username value
	 * @since
	 */
	public void setUsername( String username )
	{
		this.username = username;
	}


	/**
	 *  Sets the password attribute of the ThdlUser object
	 *
	 * @param  password  The new password value
	 * @since
	 */
	public void setPassword( String password )
	{
		this.password = password;
	}


	/**
	 *  Sets the passwordHint attribute of the ThdlUser object
	 *
	 * @param  passwordHint  The new passwordHint value
	 * @since
	 */
	public void setPasswordHint( String passwordHint )
	{
		this.passwordHint = passwordHint;
	}


	/**
	 *  Gets the roles attribute of the ThdlUser object
	 *
	 * @return    The roles value
	 * @since
	 */
	public String getRoles()
	{
		if ( null == roles )
		{
			setRoles( "guest" );
		}
		return roles;
	}


	/**
	 *  Gets the creditAttributionTag attribute of the ThdlUser object
	 *
	 * @return    The creditAttributionTag value
	 * @since
	 */
	public String getCreditAttributionTag()
	{
		return creditAttributionTag;
	}


	/**
	 *  Gets the id attribute of the ThdlUser object
	 *
	 * @return    The id value
	 * @since
	 */
	public Integer getId()
	{
		return id;
	}


	/**
	 *  Gets the firstname attribute of the ThdlUser object
	 *
	 * @return    The firstname value
	 * @since
	 */
	public String getFirstname()
	{
		return firstname;
	}


	/**
	 *  Gets the lastname attribute of the ThdlUser object
	 *
	 * @return    The lastname value
	 * @since
	 */
	public String getLastname()
	{
		return lastname;
	}


	/**
	 *  Gets the middlename attribute of the ThdlUser object
	 *
	 * @return    The middlename value
	 * @since
	 */
	public String getMiddlename()
	{
		return middlename;
	}


	/**
	 *  Gets the email attribute of the ThdlUser object
	 *
	 * @return    The email value
	 * @since
	 */
	public String getEmail()
	{
		return email;
	}


	/**
	 *  Gets the username attribute of the ThdlUser object
	 *
	 * @return    The username value
	 * @since
	 */
	public String getUsername()
	{
		return username;
	}


	/**
	 *  Gets the password attribute of the ThdlUser object
	 *
	 * @return    The password value
	 * @since
	 */
	protected String getPassword()
	{
		return password;
	}


	/**
	 *  Gets the passwordHint attribute of the ThdlUser object
	 *
	 * @return    The passwordHint value
	 * @since
	 */
	public String getPasswordHint()
	{
		return passwordHint;
	}

//helpers

	/**
	 *  Description of the Method
	 *
	 * @param  userRole  Description of Parameter
	 * @return           Description of the Returned Value
	 * @since
	 */
	public boolean hasRole( String userRole )
	{
		boolean boo = false;
		StringTokenizer tokens = new StringTokenizer( getRoles(), ":" );
		while ( tokens.hasMoreTokens() )
		{
			if ( userRole.equals( tokens.nextToken() ) )
			{
				boo = true;
			}
		}
		return boo;
	}
}

