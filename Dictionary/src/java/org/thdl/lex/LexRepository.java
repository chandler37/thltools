package org.thdl.lex;
import java.sql.*;

import java.util.*;
import javax.naming.*;
import javax.sql.*;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    September 26, 2002
 */
public class LexRepository
{
//attributes
	private static LexRepository instance;

	private Connection connection;

	private Statement queryStatement;
	private Statement updateStatement;


//accessors
	/**
	 *  Sets the connection attribute of the LexRepository object
	 *
	 * @param  connection  The new connection value
	 * @since
	 */
	private void setConnection( Connection connection )
	{
		this.connection = connection;
	}


	/**
	 *  Sets the queryStatement attribute of the LexRepository object
	 *
	 * @param  queryStatement  The new queryStatement value
	 * @since
	 */
	public void setQueryStatement( Statement queryStatement )
	{
		this.queryStatement = queryStatement;
	}


	/**
	 *  Sets the updateStatement attribute of the LexRepository object
	 *
	 * @param  updateStatement  The new updateStatement value
	 * @since
	 */
	public void setUpdateStatement( Statement updateStatement )
	{
		this.updateStatement = updateStatement;
	}


	/**
	 *  Gets the instance attribute of the LexRepository class
	 *
	 * @return                             The instance value
	 * @exception  LexRepositoryException  Description of the Exception
	 * @since
	 */
	public static LexRepository getInstance() throws LexRepositoryException
	{
		if ( instance == null )
		{
			instance = new LexRepository();
		}
		return instance;
	}


	/**
	 *  Gets the connection attribute of the LexRepository object
	 *
	 * @return    The connection value
	 * @since
	 */
	private Connection getConnection()
	{
		if ( null == connection || connection.isClosed() )
		{
			Context context = new InitialContext();
			DataSource source = (DataSource) context.lookup( LexConstants.DATASOURCE_NAME );
			setConnection( source.getConnection() );
		}
		return connection;
	}


	/**
	 *  Gets the queryStatement attribute of the LexRepository object
	 *
	 * @return    The queryStatement value
	 * @since
	 */
	public Statement getQueryStatement()
	{
		if ( getConnection().isClosed() )
		{
			Context context = new InitialContext();
			DataSource source = (DataSource) context.lookup( LexConstants.DATASOURCE_NAME );
			setConnection( source.getConnection() );
		}
		return queryStatement;
	}


	/**
	 *  Gets the updateStatement attribute of the LexRepository object
	 *
	 * @return    The updateStatement value
	 * @since
	 */
	public Statement getUpdateStatement()
	{
		return updateStatement;
	}


//helper methods
	/**
	 *  doQuery() performs a SELECT query on the database.
	 *
	 * @param  sql                         This is a SQL String passed in from
	 *      outside.
	 * @return                             ResultSet representing query results
	 * @exception  LexRepositoryException  Description of the Exception
	 * @since
	 */
	public ResultSet doQuery( String sql ) throws LexRepositoryException
	{
		try
		{
			return getQueryStatement().executeQuery( sql );
		}
		catch ( SQLException sqle )
		{
			throw new LexRepositoryException( sqle );
		}
	}


	/**
	 *  doUpdate() performs an INSERT/UPDATE/DROP action
	 *
	 * @param  sql                         Description of the Parameter
	 * @return                             Description of the Return Value
	 * @exception  LexRepositoryException  Description of the Exception
	 * @since
	 */
	public int doUpdate( String sql ) throws LexRepositoryException
	{
		try
		{
			return getQueryStatement().executeUpdate( sql );
		}
		catch ( SQLException sqle )
		{
			throw new LexRepositoryException( sqle );
		}
	}


	/**
	 *  doInsert() is a wrapper for doUpdate() that returns the auto_increment
	 *  primary key value of the newly inserted row
	 *
	 * @param  sql                         Description of the Parameter
	 * @return                             Description of the Return Value
	 * @exception  LexRepositoryException  Description of the Exception
	 * @since
	 */
	public int doInsert( String sql ) throws LexRepositoryException
	{
		if ( null == sql )
		{
			throw new LexRepositoryException( "SQL String was null" );
		}
		if ( sql.equals( "" ) )
		{
			throw new LexRepositoryException( "SQL String was empty" );
		}
		try
		{
			ResultSet rs;
			int returnVal = 0;
			int i;
			i = doUpdate( sql );
			if ( i > 0 )
			{
				rs = doQuery( "SELECT LAST_INSERT_ID()" );
				while ( rs.next() )
				{
					returnVal = rs.getInt( 1 );
				}
				return returnVal;
			}
			else
			{
				returnVal = i;
			}
			throw new LexRepositoryException( "Insert affected 0 rows. Sql String was '" + sql + "'" );
		}
		catch ( SQLException sqle )
		{
			throw new LexRepositoryException( sqle );
		}
	}


//main
	/**
	 *  The main program for the LexRepository class. This method tests all other
	 *  methods in this class
	 *
	 * @param  args  The command line arguments
	 * @since
	 */
	public static void main( String[] args )
	{
		String table = "Testing";
		String msg = "Successful";
		if ( args.length == 1 )
		{
			msg = args[0];
		}
//TEST doInsert() method. Insert a message multiple times using the Testing table
		System.out.println( "TEST ONE\n--------\n" );
		try
		{
			LexRepository lr = LexRepository.getInstance();
			String sqlString = "INSERT INTO Testing Values (NULL, '" + msg + "')";
			int newPK = lr.doInsert( sqlString );
			if ( newPK > 0 )
			{
				System.out.println( "The newly inserted row's primary key equals " + newPK );
			}
			else
			{
				System.out.println( "The row was not inserted" );
			}
		}
		catch ( LexRepositoryException lre )
		{
			System.out.println( lre.getMessage() );
			lre.printStackTrace();
		}

//TEST doQuery() method. Accept a table parameter from the command line and output
//a tab-delimited representation of the table.
		System.out.println( "\nTEST TWO\n--------\n" );
		try
		{
			LexRepository lr = LexRepository.getInstance();
			ResultSet rs = lr.doQuery( "SELECT * FROM " + table );
			ResultSetMetaData rsmd = rs.getMetaData();

			int cc = rsmd.getColumnCount();
			StringBuffer sb = new StringBuffer();
			for ( int i = 1; i <= cc; i++ )
			{
				if ( 1 != i )
				{
					sb.append( "\t" );
				}
				sb.append( rsmd.getColumnLabel( i ) );
			}
			System.out.println( sb.toString() );
			sb.setLength( 0 );
			while ( rs.next() )
			{
				for ( int i = 1; i <= cc; i++ )
				{
					if ( 1 != i )
					{
						sb.append( "\t" );
					}
					sb.append( rs.getString( i ) );
				}

				System.out.println( sb.toString() );
				sb.setLength( 0 );
			}
		}
		catch ( LexRepositoryException lre )
		{
			System.out.println( lre.getMessage() );
			lre.printStackTrace();
		}
		catch ( SQLException sqle )
		{
			System.out.println( sqle.getMessage() );
			sqle.printStackTrace();
		}
	}


//constructors
	/**
	 *  Default Constructor for the LexRepository object
	 *
	 * @exception  LexRepositoryException  Description of the Exception
	 * @since
	 */
	private LexRepository() throws LexRepositoryException
	{
		try
		{
			/*
			    Class.forName( LexConstants.DRIVER );
			    Properties props = new Properties();
			    props.setProperty( "user", LexConstantsSecure.USER );
			    props.setProperty( "password", LexConstantsSecure.PASSWORD );
			    props.setProperty( "useUnicode", "true" );
			    props.setProperty( "characterEncoding", "UTF-8" );
			    setConnection( DriverManager.getConnection( LexConstantsSecure.URL, props ) );
			  */
			Context context = new InitialContext();
			DataSource source = (DataSource) context.lookup( LexConstants.DATASOURCE_NAME );
			setConnection( source.getConnection() );
			setQueryStatement( getConnection().createStatement() );
			setUpdateStatement( getConnection().createStatement() );
		}
		/*
		    catch ( ClassNotFoundException cnfe )
		    {
		    throw new LexRepositoryException( "No Driver Available for: " + LexConstants.DRIVER );
		    }
		  */
		catch ( NamingException ne )
		{
			throw new LexRepositoryException( ne );
		}
		catch ( SQLException se )
		{
			throw new LexRepositoryException( se );
		}
	}
}

