package org.thdl.lex;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Transaction;

/**
 * Description of the Class
 * 
 * @author Hibernate WIKI
 * @created October 1, 2003
 */
public class HibernateTransactionDataTransfer {

	/**
	 * Description of the Field
	 */
	public final static ThreadLocal transaction = new ThreadLocal();

	/**
	 * Description of the Method
	 * 
	 * @exception HibernateException
	 *                Description of Exception
	 * @since
	 */
	public static void beginTransaction() throws HibernateException {

		Transaction t = (Transaction) transaction.get();
		if (t == null) {
			t = HibernateSessionDataTransfer.currentSession()
					.beginTransaction();
			transaction.set(t);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param commit
	 *            Description of the Parameter
	 * @exception HibernateException
	 *                Description of Exception
	 * @since
	 */
	public static void endTransaction(boolean commit) throws HibernateException {
		Transaction t = (Transaction) transaction.get();
		transaction.set(null);
		if (t != null) {
			if (commit) {
				t.commit();
			} else {
				t.rollback();
			}
		}
	}

}

