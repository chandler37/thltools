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

/** Converts Tibetan dictionaries stored in text files
	into a binary file tree structure format, to be used
	by some implementations of the SyllableListTree.

	<p>The text files must be in the format used by the
	The Rangjung Yeshe Tibetan-English Dictionary of Buddhist Culture.</p>

    @author Andr&eacute;s Montano Pellegrini
    @see SyllableListTree
    @see FileSyllableListTree
    @see CachedSyllableListTree
*/
public class BinaryFileGenerator extends LinkedList
{
	private long posHijos;
	private String sil, def[];
        private static char delimiter;

	/** Number of dictionary. If 0, partial word (no definition).
	*/
	private DictionarySource sourceDef;
	public static RandomAccessFile wordRaf;
	private static RandomAccessFile defRaf;

	static
	{
		wordRaf = null;
		defRaf = null;
                delimiter = '-';
	}

	public BinaryFileGenerator()
	{
		super();
		sil = null;
		def = null;
		posHijos=-1;
		sourceDef = null;
	}

	public BinaryFileGenerator(String sil, String def, int numDef)
	{
		super();
		int marker = sil.indexOf(" ");
		this.sourceDef = new DictionarySource();

		if (marker<0)
		{
			this.sil = sil;
			this.def = new String[1];
			this.def[0] = def;
			this.sourceDef.add(numDef);
		}
		else
		{
			this.sil = sil.substring(0, marker);
			this.def = null;
			addLast(new BinaryFileGenerator(sil.substring(marker+1).trim(), def, numDef));
		}
		posHijos=-1;
	}

	public String toString()
	{
		return sil;
	}

        private static String deleteQuotes(String s)
        {
          int length = s.length();
          if (length>2)
          {
            if ((s.charAt(0)=='\"') && (s.charAt(length-1)=='\"'))
              return s.substring(1,length-2);
          }
          return s;
        }

	public void addFile(String archivo, int defNum) throws Exception
	{
	    int marker, linea, len;
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo)));
		String entrada, s1, s2, previous="";

        linea=1;
        
        /* used for acip dict 
        if (delimiter==' ')
        {
    		while ((entrada = br.readLine())!=null)
	    	{
		    	entrada = entrada.trim();
			    if (!entrada.equals(""))
			    {
			        // skip page numbers
			        if (entrada.charAt(0)=='@')
			        {
			            len = entrada.length();
			            marker = 1;
			            while(marker<len && Character.isDigit(entrada.charAt(marker)))
			                marker++;
			            if (marker<len) entrada = entrada.substring(marker);
			            else continue;
			        }
			        
			        // skip letter headers
			        if (entrada.length()>0 && (entrada.charAt(0)=='(' || entrada.charAt(0)=='{' || entrada.charAt(0)=='?') && previous.trim().equals(""))
			            continue;
			            
			        // get definiendum
    			    marker = entrada.indexOf('/');
	    	        if (marker<0)
		            {
		                System.out.println("Error loading line " + linea + ", in file " + archivo + ":");
		                System.out.println(entrada);
    		        }
	    	        else
		            {
		                s1 = entrada.substring(0,marker).trim();
		                // first part of def
		                s2 = entrada.substring(marker+1).trim();
    		        }
    		        entrada = entrada.substring(marker+1).trim();
    		        if (
    		        add(s1, s2 , defNum);
			    }
			    previous = entrada;
			    linea++;
    		}
        }
        else*/
		while ((entrada = br.readLine())!=null)
		{
			entrada = entrada.trim();
			if (!entrada.equals(""))
			{
			    marker = entrada.indexOf(delimiter);
		        if (marker<0)
		        {
		            System.out.println("Error loading line " + linea + ", in file " + archivo + ":");
		            System.out.println(entrada);
		        }
		        else
		        {
		            s1 = deleteQuotes(entrada.substring(0,marker).trim());
		            s2 = deleteQuotes(entrada.substring(marker+1).trim());
		            add(s1, s2 , defNum);
		        }
			}
			linea++;
		}
	}


	private void add(String word, String def, int defNum)
	{
		Link link, newLink;
		BinaryFileGenerator ultimo;
		String firstSillable;
		int pos, marker = word.indexOf(" "), comp;

		if (marker<0)
			firstSillable = word;
		else firstSillable = word.substring(0,marker);

		/* usa orden alfabetico */
		if (isEmpty() || ((comp = firstSillable.compareTo((ultimo = (BinaryFileGenerator) getLast()).toString()))<0))
		{
			super.addLast(new BinaryFileGenerator(word, def, defNum));
		}
		else
		{
			if (comp==0)
				if (marker<0) ultimo.addMoreDef(def, defNum);
				else ultimo.add(word.substring(marker+1).trim(), def, defNum);
			else
			{
				link = cabeza;
				while(link.siguiente!=null)
				{
					comp = firstSillable.compareTo(link.siguiente.toString());
					if (comp<0)
					{
						newLink = new Link(new BinaryFileGenerator(word, def, defNum));
						newLink.siguiente = link.siguiente;
						link.siguiente = newLink;
						return;
					}
					else
						if (comp==0)
						{
							ultimo = (BinaryFileGenerator) link.siguiente.get();
							if (marker<0) ultimo.addMoreDef(def, defNum);
							else ultimo.add(word.substring(marker+1).trim(), def, defNum);
							return;
						}
					link = link.siguiente;
				}
				newLink = new Link(new BinaryFileGenerator(word, def, defNum));
				link.siguiente = newLink;
			}
		}
	}

	private void addMoreDef(String def, int numDef)
	{
		if (this.def==null)
		{
			this.def = new String[1];
			this.def[0] = def;
			sourceDef.add(numDef);
		}
		else
		{
			// if the word is repeated in the same dictionary
			if (sourceDef.contains(numDef))
				this.def[this.def.length-1] = this.def[this.def.length-1] + ". " + def;
			else
			{
				int i=0;
				String newDef[] = new String[this.def.length+1];
				while(i<this.def.length)
				{
					newDef[i] = this.def[i];
					i++;
				}
				newDef[i] = def;
				this.def = newDef;
				sourceDef.add(numDef);
			}
		}
	}

	public boolean equals (Object o)
	{
		if (o instanceof String)
		{
			return sil.equals((String)o);
		}
		else return false;
	}


	private void printMe(boolean hasNext) throws Exception
	{
		int i;

		wordRaf.writeInt((int) posHijos);
		wordRaf.writeUTF(sil);
		sourceDef.print(hasNext, wordRaf);

		if (def!=null)
			for (i=0; i<def.length; i++)
			{
				wordRaf.writeInt((int)defRaf.getFilePointer());
				defRaf.writeUTF(def[i]);
			}
	}

	private void print() throws Exception
	{
		long pos;
		ListIterator i = listIterator();
		BinaryFileGenerator silHijos;
		boolean hasNext;

		while (i.hasNext())
		{
			silHijos = (BinaryFileGenerator) i.next();
			if (!silHijos.isEmpty()) silHijos.print();
		}
		pos = wordRaf.getFilePointer();
		if (!isEmpty())
		{
			posHijos=pos;
			i = listIterator();
			hasNext = true;
			while (hasNext)
			{
				silHijos = (BinaryFileGenerator) i.next();
				hasNext=i.hasNext();
				silHijos.printMe(hasNext);
			}
		}
	}

        private static void printSintax()
        {
		System.out.println("Stores multiple dictionaries into a binary tree file.");
                System.out.println("Sintaxis:");
		System.out.println("-For multiple dictionary sources:");
		System.out.println("  java BinaryFileGenerator arch-dest [-delimiter1] arch-dict1 [[-delimiter2] arch-dict2 ...]");
		System.out.println("-For one dictionary");
		System.out.println("  java BinaryFileGenerator [-delimiter] arch-dict");
		System.out.println("Dictionary files are assumed to be .txt. Don't include extensions!");
		System.out.println("  -delimiter: default value is \'-\'. -tab takes \'\\t\' as delimiter.");
        }

	public static void main(String args[]) throws Exception
	{
		int i, n=0, a;
		if (args.length==0)
		{
                  printSintax();
		  return;
		}
		BinaryFileGenerator sl = new BinaryFileGenerator();
                if (args[0].charAt(0)=='-')
                {
                  if (args[0].equals("-tab"))
                    delimiter='\t';
                  /*else if (args[0].equals("-acip"))
                    delimiter=' ';*/
                  else
                    delimiter=args[0].charAt(1);
                  if (args.length>2)
                  {
                    printSintax();
                    return;
                  }
                  sl.addFile(args[1] + ".txt",0);
                  a=1;
                }
                else
                {
                  a=0;
		  if (args.length==1)
		  {
                    sl.addFile(args[0] + ".txt",0);
		  }
		  else
                  {
                    i=1;

                    while(i< args.length)
                    {
                      if (args[i].charAt(0)=='-')
                      {
                        if (args[i].equals("-tab"))
                          delimiter='\t';
                        else
                          delimiter=args[i].charAt(1);
                        i++;
                      }
                      else delimiter='-';
                      sl.addFile(args[i] + ".txt", n);
                      n++; i++;
                    }
                  }
		}
                File wordF = new File(args[a] + ".wrd"), defF = new File(args[a] + ".def");
                wordF.delete();
		defF.delete();
		wordRaf = new RandomAccessFile(wordF,"rw");
		defRaf = new RandomAccessFile(defF,"rw");
		sl.print();
		wordRaf.writeInt((int)sl.posHijos);
	}
}