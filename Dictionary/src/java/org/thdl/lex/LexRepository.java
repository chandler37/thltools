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

	private DataSource dataSource;


	/**
	 *  Sets the dataSource attribute of the LexRepository object
	 *
	 * @param  dataSource  The new dataSource value
	 */
	public void setDataSource( DataSource dataSource )
	{
		this.dataSource = dataSource;
	}


	/**
	 *  Gets the dataSource attribute of the LexRepository object
	 *
	 * @return    The dataSource value
	 */
	public DataSource getDataSource()
	{
		return dataSource;
	}



//accessors

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



//helper methods

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
			Connection con = getDataSource().getConnection();
			int i = con.createStatement().executeUpdate( sql );
			con.close();
			return i;
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
				Connection con = getDataSource().getConnection();
				rs = con.createStatement().executeQuery( "SELECT LAST_INSERT_ID()" );
				while ( rs.next() )
				{
					returnVal = rs.getInt( 1 );
				}
				con.close();
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
			Context context = new InitialContext();
			DataSource source = (DataSource) context.lookup( LexConstants.DATASOURCE_NAME );
			setDataSource( source );
		}
		catch ( NamingException ne )
		{
			throw new LexRepositoryException( ne );
		}

	}
}

