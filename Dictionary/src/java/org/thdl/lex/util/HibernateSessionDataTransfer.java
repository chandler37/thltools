package org.thdl.lex.util;

import net.sf.hibernate.*;
import net.sf.hibernate.cfg.*;
import java.io.File;

/**
 *  Description of the Class
 *
 * @author     Hibernate WIKI
 * @created    October 1, 2003
 */
public class HibernateSessionTEMP
{

	private static SessionFactory sessionFactory;
	/**
	 *  Description of the Field
	 */
	public final static ThreadLocal session = new ThreadLocal();

	/**
	 *  Description of the Field
	 */
	public static File config;
	public static String configResource;
public static void setConfigResource(String configResource) {
	HibernateSessionTEMP.configResource = configResource;
}
public static String getConfigResource() {
	return configResource;
}


	/**
	 *  Sets the config attribute of the HibernateSessionTEMP object
	 *
	 * @param  config  The new config value
	 */
	public static void setConfig( File config )
	{
		HibernateSessionTEMP.config = config;
	}


	/**
	 *  Gets the config attribute of the HibernateSessionTEMP object
	 *
	 * @return    The config value
	 */
	public static File getConfig()
	{
		return config;
	}


	/**
	 *  Description of the Method
	 *
	 * @return                         Description of the Returned Value
	 * @exception  HibernateException  Description of Exception
	 * @since
	 */
	public static Session currentSession()
			 throws HibernateException
	{

		Session s = (Session) session.get();
		if ( s == null )
		{

			// Don't get from JNDI, use a static SessionFactory
			if ( sessionFactory == null )
			{

				// Use default hibernate.cfg.xml
				sessionFactory = new Configuration().configure( getConfig() ).buildSessionFactory();

			}

			s = sessionFactory.openSession();
			session.set( s );
		}
		return s;
	}


	/**
	 *  Description of the Method
	 *
	 * @exception  HibernateException  Description of Exception
	 * @since
	 */
	public static void closeSession()
			 throws HibernateException
	{

		Session s = (Session) session.get();
		session.set( null );
		if ( s != null )
		{
			s.close();
		}
	}

}

