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

/** Provides recommended implementation of the SyllableListTree
	(currently most efficient memory-speed combination) loading
	into memory only the &quot;trunk&quot; of the tree, and
	searching the rest of the tree directly in the file.
		
	<p>The words must be stored in a binary file tree structure format.
	This can be done using the BinaryFileGenerator.</p>

    @author Andr&eacute;s Montano Pellegrini
    @see TibetanScanner
    @see BinaryFileGenerator
*/
public class CachedSyllableListTree implements SyllableListTree
{
	SyllableListTree syllables[];

	public CachedSyllableListTree(String archivo) throws Exception
	{
		String sil;
		long pos, defSources[];
		DictionarySource sourceDef;
		int i;
		FileSyllableListTree.openFiles(archivo);
		long posLista = FileSyllableListTree.wordRaf.length() - 4;
		LinkedList syllables = new LinkedList();

		FileSyllableListTree.wordRaf.seek(posLista);
		posLista = (long) FileSyllableListTree.wordRaf.readInt();
		FileSyllableListTree.wordRaf.seek(posLista);
		do
		{
			pos = (long) FileSyllableListTree.wordRaf.readInt();
			sil = FileSyllableListTree.wordRaf.readUTF();
			sourceDef = DictionarySource.read(FileSyllableListTree.wordRaf);
			if (sourceDef.isEmpty()) defSources = null;
			else
			{
				defSources = new long[sourceDef.countDefs()];
				for (i=0; i<defSources.length; i++)
				{
					defSources[i] = (long) FileSyllableListTree.wordRaf.readInt();
				}
			}
			syllables.addLast(new FileSyllableListTree(sil, defSources, sourceDef, pos));
		}while(sourceDef.hasBrothers());

		int n = syllables.size();
		this.syllables = new SyllableListTree[n];
		ListIterator li = syllables.listIterator();
		while (li.hasNext())
		{
			n--;
			this.syllables[n] = (SyllableListTree) li.next();
		}
	}

	public String getDef()
	{
		return null;
	}
	
	public Definitions getDefs()
	{
		return null;
	}
	
	public DictionarySource getDictionarySource()
	{
		return FileSyllableListTree.defSourcesWanted;
	}


	public boolean hasDef()
	{
		return false;
	}

	public SyllableListTree lookUp(String silStr)
	{
		int principio=0, medio, fin=syllables.length-1, comp;
		if (silStr==null) return null;
		while (principio<=fin)
		{
			medio = (principio+fin)/2;
			comp = syllables[medio].toString().compareTo(silStr);
			if (comp==0) return syllables[medio];
			else
				if (comp<0) principio = medio+1;
				else fin = medio-1;
		}
		return null;
	}
}
