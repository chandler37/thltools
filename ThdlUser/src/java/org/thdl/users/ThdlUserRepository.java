package org.thdl.users;
import java.sql.*;

import java.util.*;
import javax.naming.*;
import javax.sql.*;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 6, 2003
 */
public class ThdlUserRepository
{
//attributes
	private static ThdlUserRepository instance;
	private Connection connection;


	/**
	 *  Sets the connection attribute of the ThdlUserRepository object
	 *
	 * @param  connection  The new connection value
	 */
	private void setConnection( Connection connection )
	{
		this.connection = connection;
	}
//accessors

	/**
	 *  Gets the instance attribute of the ThdlUserRepository class
	 *
	 * @return                                  The instance value
	 * @exception  ThdlUserRepositoryException  Description of the Exception
	 */
	public static ThdlUserRepository getInstance() throws ThdlUserRepositoryException
	{
		if ( instance == null )
		{
			instance = new ThdlUserRepository();
		}
		return instance;
	}


	/**
	 *  Gets the connection attribute of the ThdlUserRepository object
	 *
	 * @return    The connection value
	 */
	private Connection getConnection()
	{
		return connection;
	}
//helper methods

	/**
	 *  Description of the Method
	 *
	 * @param  user                             Description of the Parameter
	 * @return                                  Description of the Return Value
	 * @exception  ThdlUserRepositoryException  Description of the Exception
	 */
	public ThdlUser validate( ThdlUser user ) throws ThdlUserRepositoryException
	{
		try
		{
			ThdlUser thdlUser = null;
			String sql = "SELECT id, firstname, lastname, middlename, email, username, creditAttributionTag, password, passwordHint "
					 + "FROM ThdlUsers "
					 + "WHERE ( email = '" + user.getUsername() + "' OR username = '" + user.getUsername() + "' ) "
					 + "AND password = PASSWORD('" + user.getPassword() + "')";
			Statement stmt = getConnection().createStatement();
			ResultSet rs = stmt.executeQuery( sql );
			if ( ThdlUtilities.getResultSetSize( rs ) < 1 )
			{
				throw new ThdlUserRepositoryException( "Invalid login combination" );
			}
			else if ( ThdlUtilities.getResultSetSize( rs ) > 1 )
			{
				throw new ThdlUserRepositoryException( "Login combination returned multiple records." );
			}
			else if ( ThdlUtilities.getResultSetSize( rs ) == 1 )
			{
				rs.next();
				thdlUser = user;
				thdlUser.setId( new Integer( rs.getInt( "id" ) ) );
				thdlUser.setFirstname( rs.getString( "firstname" ) );
				thdlUser.setLastname( rs.getString( "lastname" ) );
				thdlUser.setMiddlename( rs.getString( "middlename" ) );
				thdlUser.setEmail( rs.getString( "email" ) );
				thdlUser.setUsername( rs.getString( "username" ) );
				thdlUser.setCreditAttributionTag( rs.getString( "creditAttributionTag" ) );
				thdlUser.setPassword( rs.getString( "password" ) );
				thdlUser.setPasswordHint( rs.getString( "passwordHint" ) );
			}
			return thdlUser;
		}
		catch ( SQLException sqle )
		{
			throw new ThdlUserRepositoryException( sqle );
		}
	}


	/**
	 *  Description of the Method
	 *
	 * @param  user                             Description of the Parameter
	 * @param  application                      Description of the Parameter
	 * @return                                  Description of the Return Value
	 * @exception  ThdlUserRepositoryException  Description of the Exception
	 */
	public ThdlUser validate( ThdlUser user, String application ) throws ThdlUserRepositoryException
	{
		ThdlUser thdlUser = validate( user );

		String sql = "SELECT urfa.roles "
				 + "FROM UserRolesForApplication AS urfa, Applications AS apps "
				 + "WHERE urfa.applicationId = apps.id "
				 + "AND apps.application =  '" + application + "' "
				 + "AND urfa.userId = " + thdlUser.getId() + " ";
		try
		{
			Statement stmt = getConnection().createStatement();
			ResultSet rs = stmt.executeQuery( sql );

			if ( ThdlUtilities.getResultSetSize( rs ) == 1 )
			{
				rs.next();
				thdlUser.setRoles( rs.getString( 1 ) );
			}
		}
		catch ( SQLException sqle )
		{
			throw new ThdlUserRepositoryException( sqle );
		}
		return thdlUser;
	}


	/**
	 *  Description of the Method
	 *
	 * @param  thdlUser                         Description of the Parameter
	 * @exception  ThdlUserRepositoryException  Description of the Exception
	 */
	public void updateUser( ThdlUser thdlUser ) throws ThdlUserRepositoryException
	{
		try
		{
			StringBuffer buffer = new StringBuffer();
			buffer.append( "UPDATE ThdlUsers SET firstname = '" );
			buffer.append( thdlUser.getFirstname() );
			buffer.append( "',  lastname = '" );
			buffer.append( thdlUser.getLastname() );
			buffer.append( "',  middlename = '" );
			buffer.append( thdlUser.getMiddlename() );
			buffer.append( "',  email = '" );
			buffer.append( thdlUser.getEmail() );
			buffer.append( "',  username = '" );
			buffer.append( thdlUser.getUsername() );
			buffer.append( "',  creditAttributionTag = '" );
			buffer.append( thdlUser.getCreditAttributionTag() );
			buffer.append( "',  password = PASSWORD('" );
			buffer.append( thdlUser.getPassword() );
			buffer.append( "'),  passwordHint = '" );
			buffer.append( thdlUser.getPasswordHint() );
			buffer.append( "' WHERE id = " );
			buffer.append( thdlUser.getId() );
			Statement stmt = getConnection().createStatement();
			int updatedRowCount = stmt.executeUpdate( buffer.toString() );
		}
		catch ( SQLException sqle )
		{
			throw new ThdlUserRepositoryException( sqle );
		}
	}


	/**
	 *  Description of the Method
	 *
	 * @param  user                             Description of the Parameter
	 * @return                                  Description of the Return Value
	 * @exception  ThdlUserRepositoryException  Description of the Exception
	 */
	public boolean doesNotAlreadyExist( ThdlUser user ) throws ThdlUserRepositoryException
	{
		boolean insertable = false;
		try
		{
			String sql = "SELECT id FROM ThdlUsers WHERE email = '" + user.getEmail() + "'";
			Statement stmt = getConnection().createStatement();
			ResultSet rs = stmt.executeQuery( sql );
			if ( ThdlUtilities.getResultSetSize( rs ) > 0 )
			{
				throw new UserEmailAlreadyExistsException( "A user with this e-mail already exists. " );
			}
			else
			{
				insertable = true;
			}
			sql = "SELECT id FROM ThdlUsers WHERE  username = '" + user.getUsername() + "'";
			rs = stmt.executeQuery( sql );
			if ( ThdlUtilities.getResultSetSize( rs ) > 0 )
			{
				insertable = true;
				throw new UsernameAlreadyExistsException( "This username is already taken. Please choose again." );
			}
			else
			{
				insertable = true;
			}
		}
		catch ( SQLException sqle )
		{
			throw new ThdlUserRepositoryException( sqle );
		}
		return insertable;
	}


	/**
	 *  Description of the Method
	 *
	 * @param  thdlUser                         Description of the Parameter
	 * @return                                  Description of the Return Value
	 * @exception  ThdlUserRepositoryException  Description of the Exception
	 */
	public boolean insertUser( ThdlUser thdlUser ) throws ThdlUserRepositoryException
	{
		try
		{
			StringBuffer buffer = new StringBuffer();
			buffer.append( "Insert INTO ThdlUsers VALUES ( NULL, '" );
			buffer.append( thdlUser.getFirstname() );
			buffer.append( "', '" );
			buffer.append( thdlUser.getLastname() );
			buffer.append( "', '" );
			buffer.append( thdlUser.getMiddlename() );
			buffer.append( "', '" );
			buffer.append( thdlUser.getEmail() );
			buffer.append( "', '" );
			buffer.append( thdlUser.getUsername() );
			buffer.append( "', '" );
			thdlUser.setCreditAttributionTag( makeCreditTag( thdlUser ) );
			buffer.append( thdlUser.getCreditAttributionTag() );
			buffer.append( "', PASSWORD('" );
			buffer.append( thdlUser.getPassword() );
			buffer.append( "'), '" );
			buffer.append( thdlUser.getPasswordHint() );
			buffer.append( "' ) " );
			Statement stmt = getConnection().createStatement();

			boolean returnVal = false;
			int insertedRowCount = stmt.executeUpdate( buffer.toString() );

			if ( insertedRowCount > 0 )
			{
				ResultSet rs = stmt.executeQuery( "SELECT LAST_INSERT_ID()" );
				rs.next();
				thdlUser.setId( new Integer( rs.getInt( 1 ) ) );
				returnVal = true;
			}
			else
			{
				throw new ThdlUserRepositoryException( "Insert affected 0 rows. Sql String was '" + buffer.toString() + "'" );
			}
			return returnVal;
		}
		catch ( SQLException sqle )
		{
			throw new ThdlUserRepositoryException( sqle );
		}
	}


	/**
	 *  Description of the Method
	 *
	 * @param  user              Description of the Parameter
	 * @return                   Description of the Return Value
	 * @exception  SQLException  Description of the Exception
	 */
	public String makeCreditTag( ThdlUser user ) throws SQLException
	{
		StringBuffer tag = new StringBuffer();
		if ( user.getFirstname().length() > 0 )
		{
			tag.append( user.getFirstname().substring( 0, 1 ) );
		}
		if ( user.getMiddlename().length() > 0 )
		{
			tag.append( user.getMiddlename().substring( 0, 1 ) );
		}
		if ( user.getLastname().length() > 0 )
		{
			tag.append( user.getLastname().substring( 0, 1 ) );
		}
		String sql = "SELECT id FROM ThdlUsers WHERE  creditAttributionTag = '" + user.getCreditAttributionTag() + "'";
		ResultSet rs = getConnection().createStatement().executeQuery( sql );
		int count = ThdlUtilities.getResultSetSize( rs );
		if ( count > 0 )
		{
			tag.append( count + 1 );
		}
		return tag.toString();
	}
//main

	/**
	 *  The main program for the ThdlUserRepository class
	 *
	 * @param  args  The command line arguments
	 */
	public static void main( String[] args )
	{
		try
		{
			ThdlUserRepository tur = ThdlUserRepository.getInstance();
			ThdlUser user = new ThdlUser();
			user.setUsername( args[0] );
			user.setPassword( args[1] );
			tur.validate( user );
			System.out.println( user.getFirstname() );
			System.out.println( user.getMiddlename() );
			System.out.println( user.getLastname() );
			System.out.println( user.getCreditAttributionTag() );
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

//constructors
	/**
	 *Constructor for the ThdlUserRepository object
	 *
	 * @exception  ThdlUserRepositoryException  Description of the Exception
	 */
	private ThdlUserRepository() throws ThdlUserRepositoryException
	{
		/*
		    try
		    {
		    Class.forName( ThdlUserConstants.DRIVER );
		    }
		    catch ( ClassNotFoundException cnfe )
		    {
		    throw new ThdlUserRepositoryException( "No Driver Available for: " + ThdlUserConstants.DRIVER );
		    }
		    Properties props = new Properties();
		    props.setProperty( "user", ThdlUserConstants.USER );
		    props.setProperty( "password", ThdlUserConstants.PASSWORD );
		    props.setProperty( "useUnicode", "true" );
		    props.setProperty( "characterEncoding", "UTF-8" );
		  */
		try
		{
			/*
			    setConnection( DriverManager.getConnection( ThdlUserConstants.URL, props ) );
			  */
			Context context = new InitialContext();
			DataSource source = (DataSource) context.lookup( ThdlUserConstants.DATASOURCE_NAME );
			setConnection( source.getConnection() );
		}
		catch ( NamingException ne )
		{
			throw new ThdlUserRepositoryException( ne );
		}
		catch ( SQLException se )
		{
			throw new ThdlUserRepositoryException( se );
		}
	}
}

