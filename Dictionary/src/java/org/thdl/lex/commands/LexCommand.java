package org.thdl.lex.commands;

import org.thdl.lex.*;
import org.thdl.lex.component.*;
//import org.apache.commons.beanutils.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;


/**
 *  Description of the Class
 *
 *@author     travis
 *@created    October 3, 2003
 */
public abstract class LexCommand implements Command
{
//attributes
	private String defaultNext;
	private String next;
	private ILexComponent component;
	private LinkedList resultsList;
	private HashMap forwards;
	private UserSessionManager sessionMgr;


//accessors
	/**
	 *  Sets the sessionMgr attribute of the LexCommand object
	 *
	 *@param  sessionMgr  The new sessionMgr value
	 *@since
	 */
	public void setSessionManager( UserSessionManager sessionMgr )
	{
		this.sessionMgr = sessionMgr;
	}


	/**
	 *  Sets the defaultNext attribute of the LexCommand object
	 *
	 *@param  defaultNext  The new defaultNext value
	 *@since
	 */
	public void setDefaultNext( String defaultNext )
	{
		this.defaultNext = defaultNext;
	}


	/**
	 *  Sets the forwards attribute of the LexCommand object
	 *
	 *@param  forwards  The new forwards value
	 *@since
	 */
	public void setForwards( HashMap forwards )
	{
		this.forwards = forwards;
	}


	/**
	 *  Sets the resultsList attribute of the LexCommand object
	 *
	 *@param  resultsList  The new resultsList value
	 *@since
	 */
	public void setResultsList( LinkedList resultsList )
	{
		this.resultsList = resultsList;
	}


	/**
	 *  Sets the next attribute of the LexCommand object
	 *
	 *@param  next  The new next value
	 *@since
	 */
	public void setNext( String next )
	{
		this.next = next;
	}


	/**
	 *  Sets the component attribute of the LexCommand object
	 *
	 *@return            The sessionMgr value
	 *@since
	 */
	/*
	 *  public void setComponent( ILexComponent component )
	 *  {
	 *  this.component = component;
	 *  }
	 */

	/**
	 *  Gets the sessionMgr attribute of the LexCommand object
	 *
	 *@return    The sessionMgr value
	 *@since
	 */
	public UserSessionManager getSessionManager()
	{
		if ( null == sessionMgr )
		{
			setSessionManager( UserSessionManager.getInstance() );
		}
		return sessionMgr;
	}


	/**
	 *  Gets the defaultNext attribute of the LexCommand object
	 *
	 *@return    The defaultNext value
	 *@since
	 */
	public String getDefaultNext()
	{
		return defaultNext;
	}


	/**
	 *  Gets the forwards attribute of the LexCommand object
	 *
	 *@return    The forwards value
	 *@since
	 */
	public HashMap getForwards()
	{
		return forwards;
	}


	/**
	 *  Gets the resultsList attribute of the LexCommand object
	 *
	 *@return    The resultsList value
	 *@since
	 */
	public LinkedList getResultsList()
	{
		return resultsList;
	}


	/**
	 *  Gets the next attribute of the LexCommand object
	 *
	 *@return    The next value
	 *@since
	 */
	public String getNext()
	{
		return next;
	}


	/**
	 *  Gets the component attribute of the LexCommand object
	 *
	 *@param  next  Description of Parameter
	 *@since
	 */
	/*
	 *  public ILexComponent getComponent()
	 *  {
	 *  return component;
	 *  }
	 */
//helpers

//constructors

	/**
	 *  Constructor for the LexCommand object
	 *
	 *@param  next  Description of Parameter
	 *@since
	 */
	public LexCommand( String next )
	{
		setDefaultNext( next );
		setNext( getDefaultNext() );
	}


	/**
	 *  Constructor for the LexCommand object
	 *
	 *@since
	 */
	public LexCommand() { }
}

