package org.thdl.lex.util;

import junit.framework.*;
import org.thdl.lex.*;
import org.thdl.lex.component.*;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import java.util.*;
import javax.naming.InitialContext;
import javax.naming.Name;
import net.sf.hibernate.*;
import java.io.File;

/**
 *  Description of the Class
 *
 * @author     travis
 * @created    February 16, 2004
 */
public class ConvertDataToNewMapping
{
	public static File file = null;
	
	public static void convertLexComponents()
	{		try
		{
			
			if ( file.exists() )
			{
				System.out.println( "Config File exists!" );
			}
			else
			{
				System.out.println( "Config File DOES NOT exist!" );
			}

			HibernateSessionDataTransfer.setConfig( file );
			HibernateSessionDataTransfer.setConfigResource( args[0] );

			Iterator it;
			ILexComponent lc;

			LexComponentRepository.beginTransaction();

			String queryString = " FROM org.thdl.lex.component.LexComponent comp where metaId";
			Query query = LexComponentRepository.getSession().createQuery( queryString );
			it = query.iterate();
			while ( it.hasNext() )
			{
				lc = (ILexComponent) it.next();
				
				System.out.println( "Saving: " + lc.toString() );

				try
				{
					LexComponentRepositoryDataTransfer.beginTransaction();
					LexComponentRepositoryDataTransfer.getSession().save( lc );
					LexComponentRepositoryDataTransfer.endTransaction( true );
					LexComponentRepositoryDataTransfer.getSession().evict( lc );
					LexComponentRepository.getSession().evict( lc );
				}
				catch ( HibernateException he )
				{
					LexComponentRepositoryDataTransfer.endTransaction( false );
					throw he;
				}

			}

			LexComponentRepository.endTransaction( false );

		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

	
	/**
	 *  The main program for the ConvertDataToNewMapping class
	 *
	 * @param  args  The command line arguments
	 */
	public static void main( String[] args )
	{
		file = new java.io.File( args[0] );
		//ConvertDataToNewMapping.convertLexComponents();
		//ConvertDataToNewMapping.convertTerms();
		ConvertDataToNewMapping.writeCredits();
	}
}

