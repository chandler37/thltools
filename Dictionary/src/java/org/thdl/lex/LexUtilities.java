package org.thdl.lex;

import java.util.StringTokenizer;
import java.util.HashMap;

public class LexUtilities
{
/* 	public static String formatTimestamp( Timestamp time )
	{
			// SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd 'at' hh:mm:ss a zzz");
			// Date coDate = new Date( getCreatedOn().getTime() );
			// String dateString = formatter.format( coDate );
	} */
	public static String escape( String fromString )
	{
		HashMap map = new HashMap();
		map.put("'", "\\'");
		/* 
		map.put("%", "\\%");
		map.put("_", "\\_");
		map.put("\"", "\\\"");
		 */
		StringBuffer targetString = new StringBuffer("");
		if ( null != fromString )
		{
			StringTokenizer tokens = new StringTokenizer( fromString, "'%_\"", true );
			while ( tokens.hasMoreTokens() )
			{
				String temp = tokens.nextToken();
				if ( map.containsKey( temp ) )
				{
					temp = (String)map.get( temp );
				}
				targetString.append( temp );
			}
		}
		return targetString.toString();
	}
	public static int getResultSetSize( java.sql.ResultSet rs ) throws java.sql.SQLException
	{
			rs.last();
			int i = rs.getRow();
			rs.beforeFirst();
			return i;
	}
	public static int[] convertToIntArray( String[] source )
	{
		if (null == source)
		{
			return null;
		}
		int[] target = new int[ source.length ];
		for ( int i=0; i<target.length; i++ )
		{
			target[ i ] = Integer.parseInt( source[ i ] );
		}
		return target;
	}
	public static int[] convertTokensToIntArray( String source )
	{
		if (null == source || "null".equals( source ) )
		{
			return null;
		}
		StringTokenizer sourceTokens = new StringTokenizer( source, ":" );
		int[] target = new int[ sourceTokens.countTokens() ]; 
		for ( int i = 0; sourceTokens.hasMoreTokens(); i++)
		{
			target[ i ] = Integer.parseInt( sourceTokens.nextToken() );
		}
		return target;
	}
	public static String convertIntArrayToTokens( int[] source )
	{
		if (null == source)
		{
			return null;
		}
		StringBuffer target = new StringBuffer("");
		for ( int i=0; i<source.length; i++ )
		{
			target.append( Integer.toString( source[ i ] ) );
			if ( i < (source.length - 1) )
				target.append(":");
		}
		return target.toString();
	}
	
	public static void main(String[] args)
	{
		String s = "It's stupid to use a % or a _ in a SQL Statement";
		System.out.println( LexUtilities.escape( s ) );
		
		int[] ia = {1,2,3,4,5,6,7,8,9};
		System.out.println( LexUtilities.convertIntArrayToTokens( ia ) );
		
		String str = "9:8:7:6:5";
		int[] ia2 = LexUtilities.convertTokensToIntArray( str );
		String newStr="";
		for (int i=0; i<5; i++)
		{
			newStr=newStr + ia2[i] +" ";
		}
		System.out.println( newStr );
		
	}	
}
