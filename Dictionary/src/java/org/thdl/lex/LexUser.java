package org.thdl.lex;

public class LexUser extends org.thdl.users.ThdlUser
{
	public boolean isGuest()
	{
		boolean bool = false;
		if ( hasRole( "guest" ) )
		{
			bool = true;
		}
		return bool;
	}	
	public boolean isDeveloper()
	{
		boolean bool = false;
		if ( hasRole( "dev" ) )
		{
			bool = true;
		}
		return bool;
	}
}
