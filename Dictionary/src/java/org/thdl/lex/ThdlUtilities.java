package org.thdl.lex;
import java.util.HashMap;

import java.util.StringTokenizer;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 5, 2003
 */
public class ThdlUtilities
{
	/**
	 *  Gets the resultSetSize attribute of the ThdlUtilities class
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
	    / SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd 'at' hh:mm:ss a zzz");
	    / Date coDate = new Date( getCreatedOn().getTime() );
	    / String dateString = formatter.format( coDate );
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
	 * @param  source  Description of the Parameter
	 * @return         Description of the Return Value
	 */
	public static int[] convertToIntArray( String[] source )
	{
		if ( null == source )
		{
			return null;
		}
		int[] target = new int[source.length];
		for ( int i = 0; i < target.length; i++ )
		{
			target[i] = Integer.parseInt( source[i] );
		}
		return target;
	}


	/**
	 *  Description of the Method
	 *
	 * @param  source  Description of the Parameter
	 * @return         Description of the Return Value
	 */
	public static int[] convertTokensToIntArray( String source )
	{
		if ( null == source )
		{
			return null;
		}
		StringTokenizer sourceTokens = new StringTokenizer( source, ":" );
		int[] target = new int[sourceTokens.countTokens()];
		for ( int i = 0; sourceTokens.hasMoreTokens(); i++ )
		{
			target[i] = Integer.parseInt( sourceTokens.nextToken() );
		}
		return target;
	}


	/**
	 *  Description of the Method
	 *
	 * @param  source  Description of the Parameter
	 * @return         Description of the Return Value
	 */
	public static String convertIntArrayToTokens( int[] source )
	{
		if ( null == source )
		{
			return null;
		}
		StringBuffer target = new StringBuffer( "" );
		for ( int i = 0; i < source.length; i++ )
		{
			target.append( Integer.toString( source[i] ) );
			if ( i < ( source.length - 1 ) )
			{
				target.append( ":" );
			}
		}
		return target.toString();
	}


	/**
	 *  The main program for the ThdlUtilities class
	 *
	 * @param  args  The command line arguments
	 */
	public static void main( String[] args )
	{
		String s = "It's stupid to use a % or a _ in a SQL Statement";
		System.out.println( ThdlUtilities.escape( s ) );

		int[] ia = {1, 2, 3, 4, 5, 6, 7, 8, 9};
		System.out.println( ThdlUtilities.convertIntArrayToTokens( ia ) );

		String str = "9:8:7:6:5";
		int[] ia2 = ThdlUtilities.convertTokensToIntArray( str );
		String newStr = "";
		for ( int i = 0; i < 5; i++ )
		{
			newStr = newStr + ia2[i] + " ";
		}
		System.out.println( newStr );

	}
}

