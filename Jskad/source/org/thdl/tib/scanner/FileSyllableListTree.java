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

/** Searches the words directly in a file; not the preferred
	implementation. The search is too slow!
	The preferred implementation is the {@link CachedSyllableListTree}.
		
	<p>The words must be stored in a binary file tree structure format.
	This can be done using the {@link BinaryFileGenerator}.</p>

    @author Andr&eacute;s Montano Pellegrini
    @see TibetanScanner
    @see CachedSyllableListTree
    @see BinaryFileGenerator
*/

public class FileSyllableListTree implements SyllableListTree
{
	private String sil;
	private long def[];
	private long posLista;
	private DictionarySource defSource;
	public static BitDictionarySource defSourcesWanted;
	public static RandomAccessFile wordRaf=null;
	private static RandomAccessFile defRaf=null;
	public static int versionNumber;
	
	/** Creates the root */
	public FileSyllableListTree(String archivo, int defSourcesWanted) throws Exception
	{
		sil = null;
		def = null;
		defSource = null;
		
		this.openFiles(archivo);
		posLista = this.wordRaf.getFilePointer();

		/* if versionNumber is 2 use BitDictionarySource
		else use ByteDictionarySource. */		
		this.defSourcesWanted.setDicts(defSourcesWanted);
	}
	
	/** Used to create each node (except the root)
	*/
	public FileSyllableListTree(String sil, long []def, DictionarySource defSource, long posLista)
	{
		this.sil=sil;
		this.def=def;
		this.defSource = defSource;
		this.posLista=posLista;
	}

	public String toString()
	{
		return sil;
	}

	public DictionarySource getDictionarySource()
	{
		return defSource;
	}
	
	public BitDictionarySource getDictionarySourcesWanted()
	{
	    return this.defSourcesWanted;
	}

	public static void openFiles(String archivo) throws Exception
	{
	    long fileSize;
	    int pos;
		
		wordRaf = new RandomAccessFile(archivo + ".wrd", "r");
		defRaf = new RandomAccessFile(archivo + ".def", "r");

		fileSize = wordRaf.length();
		wordRaf.seek(fileSize-4L);
		pos = wordRaf.readInt();
		
		if (pos >> 8 == -1)
		{
		    versionNumber = pos & 255;
		    
		    // for now, only version 2 & 3 should be expected
		    if (versionNumber != 3) versionNumber=2;
		    wordRaf.seek(fileSize-8L);
		    pos = wordRaf.readInt();
		}
		else
		{
		    // Updates the dictionary for backward compatibility.		    
		    try
		    {
    		    wordRaf.close();
	    	    wordRaf = new RandomAccessFile(archivo + ".wrd", "rw");
    		    wordRaf.seek(fileSize);    	    
		        wordRaf.writeShort(-1);
		        wordRaf.writeByte(-1);
		        
		        // Because it didn't have a version number, must be version 2.
		        versionNumber = 2;
		        wordRaf.writeByte(versionNumber);
		        wordRaf.close();
		        wordRaf = new RandomAccessFile(archivo + ".wrd", "r");
		    }
		    catch (Exception e)
		    {
		        // dictionary is stored on a non-writable media. Do nothing.
		    }
		}
		
		/* if versionNumber is 2 use BitDictionarySource else use 
		ByteDictionarySource. */
		defSourcesWanted = new BitDictionarySource();
		
	    wordRaf.seek(pos);		
	}

	public String getDef()
	{
		return getDefs().toString();
	}

	public Definitions getDefs()
	{
		if (def==null) return null;
        DictionarySource defSourceAvail = defSource.intersection(defSourcesWanted);
        String defs[];
		int i, n=0;
        
		if (versionNumber==2)
		{
		    int defsAvail[] = ((BitDictionarySource) defSourceAvail).untangleDefs(), defsFound[] = ((BitDictionarySource) defSource).untangleDefs(def.length);

		    defs = new String[defsAvail.length];
		    try
		    {
			    for (i=0; i<defsAvail.length; i++)
			    {
				    while(defsAvail[i]!=defsFound[n]) n++;
				    defRaf.seek(def[n]);
				    defs[i] = defRaf.readUTF();
			    }
		    }
		    catch (Exception e)
		    {
			    System.out.println(e);
			    return null;
		    }
		}
		else
		{
		    ByteDictionarySource defSourceAvailBy = (ByteDictionarySource) defSourceAvail;
		    defs = new String [defSourceAvailBy.countDefs()];
		    
		    try
		    {
			    for (i=0; i < def.length; i++)
			    {
				    if (!defSourceAvailBy.isEmpty(i))
				    {
				        defRaf.seek(def[i]);
				        defs[n] = defRaf.readUTF();
				        n++;
				    }
				}
		    }
		    catch (Exception e)
		    {
			    System.out.println(e);
			    return null;
		    }
		}
		return new Definitions(defs, defSourceAvail);
	}

	public boolean hasDef()
	{
		if (def==null) return false;
		DictionarySource defSourceAvail = defSource.intersection(defSourcesWanted);
		return !defSourceAvail.isEmpty();
	}

	public SyllableListTree lookUp(String silStr)
	{
		String sil;
		long pos, defSource[];
		DictionarySource sourceDef;
		
		int i;

		if (silStr==null) return null;
		try
		{
			wordRaf.seek(posLista);
			do
			{
				pos = (long) wordRaf.readInt();
				sil = wordRaf.readUTF();
				
				if (versionNumber==2) sourceDef = new BitDictionarySource();
				else sourceDef = new ByteDictionarySource();
				sourceDef.read(wordRaf);
				
				if (sourceDef.isEmpty()) defSource = null;
				else
				{
					defSource = new long[sourceDef.countDefs()];
					for (i=0; i<defSource.length; i++)
					{
						defSource[i] = (long) wordRaf.readInt();
					}
				}

				if (sil.compareTo(silStr)>0)
					return null;
				if (sil.equals(silStr))
					return new FileSyllableListTree(sil, defSource, sourceDef, pos);

			}while(sourceDef.hasBrothers());
		}
		catch (Exception e)
		{
		}
		return null;
	}
}
