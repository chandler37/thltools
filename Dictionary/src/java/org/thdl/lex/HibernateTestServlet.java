package org.thdl.lex;

import org.thdl.lex.component.*;
// import org.thdl.lex.component.peers.*;

import net.sf.hibernate.*;
import net.sf.hibernate.cfg.*;
// import net.sf.hibernate.tool.hbm2ddl.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import java.sql.*;
import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;


/**
 *  Description of the Class
 *
 *@author     travis
 *@created    October 1, 2003
 */
public class HibernateTestServlet
		 extends HttpServlet
{

	private SessionFactory sessionFactory;
	private Session session;
	private Transaction transaction;


	/**
	 *  Sets the sessionFactory attribute of the HibernateTestServlet object
	 *
	 *@param  sessionFactory  The new sessionFactory value
	 *@since
	 */
	public void setSessionFactory( SessionFactory sessionFactory )
	{
		this.sessionFactory = sessionFactory;
	}


	/**
	 *  Sets the session attribute of the HibernateTestServlet object
	 *
	 *@param  session  The new session value
	 *@since
	 */
	public void setSession( Session session )
	{
		this.session = session;
	}


	/**
	 *  Sets the transaction attribute of the HibernateTestServlet object
	 *
	 *@param  transaction  The new transaction value
	 *@since
	 */
	public void setTransaction( Transaction transaction )
	{
		this.transaction = transaction;
	}


	/**
	 *  Gets the sessionFactory attribute of the HibernateTestServlet object
	 *
	 *@return    The sessionFactory value
	 *@since
	 */
	public SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}


	/**
	 *  Gets the session attribute of the HibernateTestServlet object
	 *
	 *@return    The session value
	 *@since
	 */
	public Session getSession()
	{
		return session;
	}


	/**
	 *  Gets the transaction attribute of the HibernateTestServlet object
	 *
	 *@return    The transaction value
	 *@since
	 */
	public Transaction getTransaction()
	{
		return transaction;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  request               Description of Parameter
	 *@param  response              Description of Parameter
	 *@exception  ServletException  Description of Exception
	 *@exception  IOException       Description of Exception
	 *@since
	 */
	public void doGet( HttpServletRequest request, HttpServletResponse response )
			 throws ServletException, IOException
	{

		try
		{
			initHibernate();

			response.setContentType( "text/html" );
			PrintWriter out = response.getWriter();
			out.println( "<html><body>" );

			beginTransaction();
			processMods( out );
			//testQuery( out );
			endTransaction( false );

			out.println( "</body></html>" );

		}
		catch ( SQLException e )
		{
			try
			{
				getSession().close();
				throw new ServletException( e );
			}
			catch ( HibernateException he )
			{
				throw new ServletException( he );
			}
		}
		catch ( HibernateException e )
		{
			try
			{
				getSession().close();
				throw new ServletException( e );
			}
			catch ( HibernateException he )
			{
				throw new ServletException( he );
			}
		}

	}


	// Helper Methods
	/**
	 *  Description of the Method
	 *
	 *@exception  HibernateException  Description of Exception
	 *@since
	 */
	private void initHibernate()
			 throws HibernateException
	{

		// Load Configuration and build SessionFactory
		setSessionFactory( new Configuration().configure().buildSessionFactory() );
	}


	/**
	 *  Description of the Method
	 *
	 *@exception  HibernateException  Description of Exception
	 *@since
	 */
	private void beginTransaction()
			 throws HibernateException
	{
		setSession( getSessionFactory().openSession() );
		setTransaction( session.beginTransaction() );
	}


	/**
	 *  Description of the Method
	 *
	 *@param  commit                  Description of Parameter
	 *@exception  HibernateException  Description of Exception
	 *@since
	 */
	private void endTransaction( boolean commit )
			 throws HibernateException
	{

		if ( commit )
		{
			getTransaction().commit();
		}
		else
		{
			// Don't commit the transaction, can be faster for read-only operations
			getTransaction().rollback();
		}
		getSession().close();
	}


	/**
	 *  Description of the Method
	 *
	 *@param  out                     Description of Parameter
	 *@exception  SQLException        Description of Exception
	 *@exception  HibernateException  Description of Exception
	 *@since
	 */
	public void processMods( PrintWriter out ) throws SQLException, HibernateException
	{
		String queryString = "FROM org.thdl.lex.component.LexComponent";
		Query query = getSession().createQuery( queryString );

		out.println( "Starting..." );
		for ( Iterator it = query.iterate(); it.hasNext();  )
		{
			ILexComponent lc = (ILexComponent) it.next();
			out.println( lc.getMetaId() + " </br/> " );
		}
	}


	/**
	 *  A unit test for JUnit
	 *
	 *@param  out                     Description of Parameter
	 *@exception  SQLException        Description of Exception
	 *@exception  HibernateException  Description of Exception
	 *@since
	 */
	public void testQuery( PrintWriter out )
			 throws SQLException, HibernateException
	{
		String queryString = "FROM org.thdl.lex.component.Term as term WHERE term.term = :term";
		Query query = getSession().createQuery( queryString );
		ITerm term = new Term();
		term.setTerm( "thos pa" );
		query.setProperties( term );
		for ( Iterator it = query.iterate(); it.hasNext();  )
		{
			out.println( it.next() );
		}
	}
}

