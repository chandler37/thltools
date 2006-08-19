/**
 *
 *
 */

package org.thdl.tib.input ;

public class DictionarySettings
{
	private boolean dictionaryValid = false ;
	private boolean dictionaryEnabled = false ;
	private boolean dictionaryLocal = false ;
	private String dictionaryPath = "" ;

	public DictionarySettings ()
	{
		boolean dictionaryValid = false ;
		boolean dictionaryEnabled = false ;
		boolean dictionaryLocal = false ;
		String dictionaryPath = "" ;
	}

	public DictionarySettings ( boolean valid, boolean enabled, boolean local, String pathOrUrl )
	{
		boolean dictionaryValid = valid ;
		boolean dictionaryEnabled = enabled ;
		boolean dictionaryLocal = local ;
		String dictionaryPath = pathOrUrl ;
	}

	public DictionarySettings ( boolean enabled, boolean local, String pathOrUrl )
	{
		boolean dictionaryValid = true ;
		boolean dictionaryEnabled = enabled ;
		boolean dictionaryLocal = local ;
		String dictionaryPath = pathOrUrl ;
	}

	public boolean equal ( DictionarySettings ds )
	{
		return ( ds.isEnabled () == this.isEnabled () &&
				 ds.isLocal () != this.isLocal () &&
				 ds.getPathOrUrl ().equals ( this.getPathOrUrl () ) ) ;
	}

	public boolean isValid ()
	{
		return dictionaryValid ;
	}

	public boolean isEnabled ()
	{
		return dictionaryEnabled ;
	}

	public boolean isLocal ()
	{
		return dictionaryLocal ;
	}

	public String getPathOrUrl ()
	{
		return dictionaryPath ;
	}

	public void set ( DictionarySettings ds )
	{
		dictionaryValid = ds.dictionaryValid ;
		dictionaryEnabled = ds.dictionaryEnabled ;
		dictionaryLocal = ds.dictionaryLocal ;
		dictionaryPath = ds.dictionaryPath ;
	}

	public void setValid ( boolean valid )
	{
		dictionaryValid = valid ;
	}

	public void setEnabled ( boolean enabled )
	{
		dictionaryEnabled = enabled ;
	}

	public void setLocal ( boolean local )
	{
		dictionaryLocal = local ;
	}

	public void setPathOrUrl ( String pathOrUrl )
	{
		dictionaryPath = pathOrUrl ;
	}
};
