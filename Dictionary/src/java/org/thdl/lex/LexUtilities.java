package org.thdl.lex;
import java.util.HashMap;

import java.util.StringTokenizer;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 6, 2003
 */
public class LexUtilities
{
	/**
	 *  Gets the resultSetSize attribute of the LexUtilities class
	 *
	 * @param  rs                         Description of the Parameter
	 * @return                            The resultSetSize value
	 * @exception  java.sql.SQLException  Description of the Exception
	 */
	public static int getResultSetSize( java.sql.ResultSet rs ) throws java.sql.SQLException
	{
		rs.last();
		int i = rs.getRow();
		rs.beforeFirst();
		return i;
	}


	/*
	    public static String formatTimestamp( Timestamp time )
	    {
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd 'at' hh:mm:ss a zzz");
	    Date coDate = new Date( getCreatedOn().getTime() );
	    String dateString = formatter.format( coDate );
	    }
	  */
	/**
	 *  Description of the Method
	 *
	 * @param  fromString  Description of the Parameter
	 * @return             Description of the Return Value
	 */
	public static String escape( String fromString )
	{
		HashMap map = new HashMap();
		map.put( "'", "\\'" );
		/*
		    map.put("%", "\\%");
		    map.put("_", "\\_");
		    map.put("\"", "\\\"");
		  */
		StringBuffer targetString = new StringBuffer( "" );
		if ( null != fromString )
		{
			StringTokenizer tokens = new StringTokenizer( fromString, "'%_\"", true );
			while ( tokens.hasMoreTokens() )
			{
				String temp = tokens.nextToken();
				if ( map.containsKey( temp ) )
				{
					temp = (String) map.get( temp );
				}
				targetString.append( temp );
			}
		}
		return targetString.toString();
	}


	/**
	 *  Description of the Method
	 *
	 * @param  fromString  Description of the Parameter
	 * @return             Description of the Return Value
	 */
	public static String hqlEscape( String fromString )
	{
		HashMap map = new HashMap();
		map.put( "'", "''" );
		StringBuffer targetString = new StringBuffer( "" );
		if ( null != fromString )
		{
			StringTokenizer tokens = new StringTokenizer( fromString, "'%_\"", true );
			while ( tokens.hasMoreTokens() )
			{
				String temp = tokens.nextToken();
				if ( map.containsKey( temp ) )
				{
					temp = (String) map.get( temp );
				}
				targetString.append( temp );
			}
		}
		return targetString.toString();
	}


	/**
	 *  Description of the Method
	 *
	 * @param  source  Description of the Parameter
	 * @return         Description of the Return Value
	 */
	public static Integer[] convertToIntegerArray( String[] source )
	{
		if ( null == source )
		{
			return null;
		}
		Integer[] target = new Integer[source.length];
		for ( int i = 0; i < target.length; i++ )
		{
			target[i] = new Integer( source[i] );
		}
		return target;
	}


	/**
	 *  Description of the Method
	 *
	 * @param  source  Description of the Parameter
	 * @return         Description of the Return Value
	 */
	public static Integer[] convertTokensToIntegerArray( String source )
	{
		if ( null == source || "null".equals( source ) )
		{
			return null;
		}
		StringTokenizer sourceTokens = new StringTokenizer( source, ":" );
		Integer[] target = new Integer[sourceTokens.countTokens()];
		for ( int i = 0; sourceTokens.hasMoreTokens(); i++ )
		{
			target[i] = new Integer( sourceTokens.nextToken() );
		}
		return target;
	}


	/**
	 *  Description of the Method
	 *
	 * @param  source  Description of the Parameter
	 * @return         Description of the Return Value
	 */
	public static String convertIntegerArrayToTokens( Integer[] source )
	{
		if ( null == source )
		{
			return null;
		}
		StringBuffer target = new StringBuffer( "" );
		for ( int i = 0; i < source.length; i++ )
		{
			target.append( source[i].toString() );
			if ( i < ( source.length - 1 ) )
			{
				target.append( ":" );
			}
		}
		return target.toString();
	}


	/**
	 *  The main program for the LexUtilities class
	 *
	 * @param  args  The command line arguments
	 */
	public static void main( String[] args )
	{
		String s = "It's stupid to use a % or a _ in a SQL Statement";
		System.out.println( LexUtilities.escape( s ) );

		Integer[] ia = {new Integer( 1 ), new Integer( 3 )};
		System.out.println( LexUtilities.convertIntegerArrayToTokens( ia ) );

		String str = "9:8:7:6:5";
		Integer[] ia2 = LexUtilities.convertTokensToIntegerArray( str );
		String newStr = "";
		for ( int i = 0; i < 5; i++ )
		{
			newStr = newStr + ia2[i] + " ";
		}
		System.out.println( newStr );

	}
}

