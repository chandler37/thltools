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
	private DataSource dataSource;


	/**
	 *  Sets the dataSource attribute of the ThdlUserRepository object
	 *
	 * @param  dataSource  The new dataSource value
	 */
	public void setDataSource( DataSource dataSource )
	{
		this.dataSource = dataSource;
	}


	/**
	 *  Gets the dataSource attribute of the ThdlUserRepository object
	 *
	 * @return    The dataSource value
	 */
	public DataSource getDataSource()
	{
		return dataSource;
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

//helper methods

	/**
	 *  Description of the Method
	 *
	 * @param  user                             Description of the Parameter
	 * @return                                  Description of the Return Value
	 * @exception  ThdlUserRepositoryException  Description of the Exception
	 */
	public ThdlUser validate( ThdlUser user ) throws ThdlUserRepositoryException, SQLException
	{
			ThdlUser thdlUser = null;
			String sql = "SELECT id, firstname, lastname, middlename, email, username, creditAttributionTag, password, passwordHint "
					 + "FROM ThdlUsers "
					 + "WHERE ( email = '" + user.getUsername() + "' OR username = '" + user.getUsername() + "' ) "
					 + "AND password = PASSWORD('" + user.getPassword() + "')";
			Connection con = getDataSource().getConnection();
			ResultSet rs = con.createStatement().executeQuery( sql );
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
			con.close();
			return thdlUser;
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
			Connection con = getDataSource().getConnection();
			ResultSet rs = con.createStatement().executeQuery( sql );

			if ( ThdlUtilities.getResultSetSize( rs ) == 1 )
			{
				rs.next();
				thdlUser.setRoles( rs.getString( 1 ) );
			}
			con.close();
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

			Connection con = getDataSource().getConnection();
			int updatedRowCount = con.createStatement().executeUpdate( buffer.toString() );
			con.close();
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
			Connection con = getDataSource().getConnection();
			Statement stmt = con.createStatement();
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

			Connection con = getDataSource().getConnection();
			Statement stmt = con.createStatement();
			int insertedRowCount = stmt.executeUpdate( buffer.toString() );

			boolean returnVal = false;
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
			con.close();
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

		Connection con = getDataSource().getConnection();
		ResultSet rs = con.createStatement().executeQuery( sql );
		int count = ThdlUtilities.getResultSetSize( rs );
		con.close();
		if ( count > 0 )
		{
			tag.append( count + 1 );
		}
		return tag.toString();
	}


	/**
	 *  Gets the usernameMap attribute of the ThdlUserRepository object
	 *
	 * @return                                  The usernameMap value
	 * @exception  ThdlUserRepositoryException  Description of the Exception
	 */
	public HashMap getUsernameMap() throws ThdlUserRepositoryException
	{
		ResultSet rs = null;
		HashMap map = new HashMap();
		try
		{
			Connection con = getDataSource().getConnection();
			Statement stmt = con.createStatement();
			String sql = "SELECT id, firstname, lastname FROM ThdlUsers";
			rs = stmt.executeQuery( sql );
			if ( null != rs )
			{
				int i = 0;
				Integer key = null;
				String value = "";
				while ( rs.next() )
				{
					i = rs.getInt( 1 );
					key = new Integer( i );
					value = rs.getString( 2 );
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnCount = rsmd.getColumnCount();
					for ( int x = 3; x <= columnCount; x++ )
					{
						value = value + " " + rs.getString( x );
					}
					map.put( key, value );
				}
			}
			con.close();
		}
		catch ( SQLException sqle )
		{
			throw new ThdlUserRepositoryException( sqle );
		}
		catch ( Exception e )
		{
			throw new ThdlUserRepositoryException( e );
		}
		return map;
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
		try
		{
			Context context = new InitialContext();
			DataSource source = (DataSource) context.lookup( ThdlUserConstants.DATASOURCE_NAME );
			setDataSource( source );
		}
		catch ( NamingException ne )
		{
			throw new ThdlUserRepositoryException( ne );
		}
	}
}

