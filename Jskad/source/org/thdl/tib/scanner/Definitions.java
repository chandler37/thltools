/*
The contents of this file are subject to the AMP Open Community License
Version 1.0 (the "License"); you may not use this file except in compliance
with the License. You may obtain a copy of the License on the AMP web site 
(http://www.tibet.iteso.mx/Guatemala/).

Software distributed under the License is distributed on an "AS IS" basis, 
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the 
License for the specific terms governing rights and limitations under the 
License. 

The Initial Developer of this software is Andres Montano Pellegrini. Portions
created by Andres Montano Pellegrini are Copyright 2001 Andres Montano
Pellegrini. All Rights Reserved. 

Contributor(s): ______________________________________.
*/

/** Stores the multiple definitions (corresponding to
	various dictionaries) for a single Tibetan word.

    @author Andr&eacute;s Montano Pellegrini
*/
package org.thdl.tib.scanner;

public class Definitions
{
	public String[] def;
	public int[] source;
	public static String[] defTags;
	
	static
	{
		defTags=null;
	}
	
	public static void setTags(String tags[])
	{
		defTags = tags;
	}
	
	public Definitions(String[] def, int[] source)
	{
		this.def = def;
		this.source = source;
	}
	
	public Definitions(String def)
	{
		source = null;
		this.def = new String[1];
		this.def[0] = def;
	}
		
	public String getTag(int i)
	{
		if (source==null) return null;
		if (defTags==null) return Integer.toString(source[i]+1);
		return defTags[source[i]];
	}
	
	public String toString()
	{
		int i;
		String s;
		if (def==null) return null;
		if (source==null) return def[0];
		
		s = "(" + getTag(0) + ") " + def[0];
		for (i=1; i<def.length; i++)
			s += "\n" + "(" + getTag(i) + ") " + def[i];

		return s;
	}
}