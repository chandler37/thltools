package org.thdl.lex.test;

import net.sourceforge.jwebunit.WebTestCase;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    November 10, 2003
 */
public class LexTestCase extends WebTestCase
{

	/**
	 *  Description of the Method
	 */
	public void begin()
	{
		beginAt( "/action" );
		System.out.println( "Starting at /action" );
	}


	/**
	 *  A unit test for JUnit
	 *
	 * @param  user  Description of the Parameter
	 * @param  pass  Description of the Parameter
	 */
	public void testLoginPageInterceptThenProceed( String user, String pass )
	{
		System.out.println( "Verifying Login Form intercept" );
		assertFormPresent( "loginForm" );
		assertFormElementPresent( "username" );
		assertFormElementPresent( "password" );
		setFormElement( "username", user );
		setFormElement( "password", pass );
		submit();
	}


	/**
	 *  A unit test for JUnit
	 */
	public void testLex()
	{
		assertFormPresent( "quickSearch" );
		assertFormPresent( "newTerm" );
		assertFormPresent( "defults" );
		assertFormPresent( "preferences" );
	}


	/**
	 *Constructor for the LexTestCase object
	 *
	 * @param  name  Description of the Parameter
	 */
	public LexTestCase( String name )
	{
		super( name );
		getTestContext().setBaseUrl( "http://haley.local:2020/lex" );
	}


	/**
	 *  The main program for the LexTestCase class
	 *
	 * @param  args  The command line arguments
	 */
	public static void main( String[] args )
	{
		LexTestCase tc = new LexTestCase( "menu" );
		tc.begin();
		tc.testLoginPageInterceptThenProceed( args[0], args[1] );
		tc.testLex();
	}
}

