package org.thdl.lex;

import net.sf.hibernate.*;
import net.sf.hibernate.cfg.*;


/**
 *  Description of the Class
 *
 * @author     Hibernate WIKI
 * @created    October 1, 2003
 */
public class HibernateTransaction
{

	/**
	 *  Description of the Field
	 */
	public final static ThreadLocal transaction = new ThreadLocal();


	/**
	 *  Description of the Method
	 *
	 * @exception  HibernateException  Description of Exception
	 * @since
	 */
	public static void beginTransaction() throws HibernateException
	{

		Transaction t = (Transaction) transaction.get();
		if ( t == null )
		{
			t = HibernateSession.currentSession().beginTransaction();
			transaction.set( t );
		}
	}


	/**
	 *  Description of the Method
	 *
	 * @param  commit                  Description of the Parameter
	 * @exception  HibernateException  Description of Exception
	 * @since
	 */
	public static void endTransaction( boolean commit ) throws HibernateException
	{
		Transaction t = (Transaction) transaction.get();
		transaction.set( null );
		if ( t != null && commit )
		{
			t.commit();
		}
		else if ( t != null )
		{
			t.rollback();
		}
	}

}

