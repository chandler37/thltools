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
package org.thdl.tib.scanner;

import java.io.*;

/** Specifies a subset of dictionaries among a set of
	dictionaries. Supports a maximum of 30 dictionaries.

    @author Andr&eacute;s Montano Pellegrini
*/
public class DictionarySource
{
	private int dicts;

	/** Last bit of word; 1 if there are more brothers.*/
	private static final int lastBit=1073741824;
	private static final int allDicts=lastBit-1;

	public DictionarySource()
	{
		dicts = 0;
	}

	public static DictionarySource getAllDictionaries()
	{
		DictionarySource ds = new DictionarySource();
		ds.setDicts(allDicts);
		return ds;
	}

	public void setAllDictionaries()
	{
		dicts = allDicts;
	}

	public void setDicts(int dicts)
	{
		this.dicts = dicts;
	}

	public int getDicts()
	{
		return dicts;
	}

	private int getBits(int n)
	{
		return 1 << n;
	}

	public boolean contains(int dict)
	{
		return (dicts & getBits(dict))>0;
	}

	public void add(int dict)
	{
		dicts|= getBits(dict);
	}

	/** Write to file using BinaryFileGenerator	*/
	public void print(boolean hasNext, DataOutput raf) throws IOException
	{
		int numDict;
		if (hasNext) numDict = lastBit | dicts;
		else numDict = dicts;
		raf.writeInt(numDict);
	}

	public static DictionarySource read(DataInput raf) throws IOException
	{
		DictionarySource ds = new DictionarySource();
		ds.setDicts(raf.readInt());
		return ds;
	}

	public boolean hasBrothers()
	{
		return (dicts & lastBit)>0;
	}

	public int countDefs()
	{
		int n, source;
		for (n=0, source = dicts & allDicts; source>0; source>>=1)
			if (source%2==1) n++;
		return n;
	}

	public DictionarySource intersection(DictionarySource dsO)
	{
		DictionarySource ds = new DictionarySource();
		ds.setDicts(this.dicts & dsO.dicts);
		return ds;
	}

	public int[] untangleDefs(int n)
	{
		int arr[], i, pos, source;
		arr = new int[n];
		for (i=0, pos=0, source=dicts & allDicts; pos<n; i++, source>>=1)
			if (source%2==1)
				arr[pos++]=i;
		return arr;
	}

	public int[] untangleDefs()
	{
		return untangleDefs(countDefs());
	}

	public boolean isEmpty()
	{
		return (dicts & allDicts)<=0;
	}

	public void reset()
	{
		dicts = 0;
	}
}
