package org.thdl.lex;

import net.sf.hibernate.*;
import net.sf.hibernate.cfg.*;


/**
 *  Description of the Class
 *
 * @author     Hibernate WIKI
 * @created    October 1, 2003
 */
public class HibernateSession
{

	private static SessionFactory sessionFactory;
	/**
	 *  Description of the Field
	 */
	public final static ThreadLocal session = new ThreadLocal();


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
				sessionFactory = new Configuration().configure().buildSessionFactory();

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

