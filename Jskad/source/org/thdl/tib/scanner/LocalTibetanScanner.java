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
import java.util.*;
import java.io.*;

class LocalTibetanScanner implements TibetanScanner
{
	public static String archivo;
	private SyllableListTree raiz, silActual, lastCompSil, silAnterior;
	private String wordActual, lastCompWord;
	private Vector floatingSil;
	private LinkedList wordList;
	private static String endOfParagraphMarks = "/;|!:[]^@#$%=<>(){}";
	private static String endOfSyllableMarks = " _\t";

	static
	{
		archivo = null;
	}

	public void clearTokens()
	{
		wordList = new LinkedList();
	}

	public DictionarySource getDictionarySource()
	{
		return raiz.getDictionarySource();
	}


	public LocalTibetanScanner(String arch) throws Exception
	{
		archivo = arch;
		// raiz = new MemorySyllableListTree(archivo);
		// raiz = new FileSyllableListTree(archivo);
		raiz = new CachedSyllableListTree(archivo);
		floatingSil = new Vector();
		wordList = new LinkedList();
		resetAll();
	}

	private void resetAll()
	{
		silAnterior = silActual = lastCompSil = null;
		wordActual = lastCompWord = null;
	}

	private void scanSyllable(String sil)
	{
		SyllableListTree resultado=null;
		Enumeration enum;
		Word w;
		String silSinDec;

		if (silActual==null)
			silActual = raiz;

		silAnterior = silActual;
		silActual = silActual.lookUp(sil);

		if (silActual != null)
		{
			if (silActual.hasDef())
			{
				lastCompWord = concatWithSpace(wordActual, sil);
				lastCompSil = silActual;
				floatingSil.removeAllElements();
			}
			else
			{
				silSinDec = withOutDec(sil);
				if (silSinDec!=null)
				{
					resultado = silAnterior.lookUp(silSinDec);
					if (resultado == null)
					{
						silSinDec += "\'";
						resultado = silAnterior.lookUp(silSinDec);
					}
					
					if (resultado!=null && resultado.hasDef())
					{
						lastCompWord = concatWithSpace(wordActual, silSinDec);
						lastCompSil = resultado;
						wordActual = concatWithSpace(wordActual, sil);
					}
					else resultado = null;
				}
				if (resultado!=null) return;
				
				if (lastCompSil!=null)
					floatingSil.addElement(sil);
			}
			wordActual = concatWithSpace(wordActual, sil);
		}
		else
		{
			silSinDec = withOutDec(sil);
			if (silSinDec!=null)
			{
				resultado = silAnterior.lookUp(silSinDec);
				if (resultado == null)
				{
					silSinDec += "\'";
					resultado = silAnterior.lookUp(silSinDec);
				}
				// si funciona sin declension arreglado problema
				if (resultado!=null && resultado.hasDef())
				{
					wordList.addLast(new Word(concatWithSpace(wordActual, silSinDec), concatWithSpace(wordActual,sil), resultado.getDefs()));
					resetAll();
					floatingSil.removeAllElements();
				}
				else resultado = null;
			}
			if (resultado!=null) return;
			
			if (lastCompSil!=null)
			{
				w = new Word(lastCompWord, lastCompSil.getDefs());
				wordList.addLast(w);
				this.resetAll();

				enum = floatingSil.elements();
				floatingSil = new Vector();
				while (enum.hasMoreElements())
					scanSyllable((String)enum.nextElement());

				scanSyllable(sil);
			}
			else
			{
				if (silAnterior!=raiz)
				{
					w = new Word(wordActual, "[incomplete word]");
					wordList.addLast(w);
					this.resetAll();
					scanSyllable(sil);
				}
				else
				{
					w = new Word(sil, "[not found]");
					wordList.addLast(w);
					this.resetAll();
				}
			}
		}
	}

	public void finishUp()
	{
		Enumeration enum;
		Word w;

		while (lastCompSil!=null)
		{
			w = new Word(lastCompWord, lastCompSil.getDefs());
			wordList.addLast(w);
			this.resetAll();

			enum = floatingSil.elements();
			floatingSil = new Vector();
			while (enum.hasMoreElements())
				scanSyllable((String)enum.nextElement());
		}

		if (silActual!=null)
		{
			wordList.addLast(new Word(wordActual, "[incomplete word]"));
			this.resetAll();
		}
	}

	private static String concatWithSpace(String s1, String s2)
	{
		if (s1==null || s1.equals(""))
			return s2;
		else
			return s1 + ' ' + s2;
	}

	private static String withOutDec(String sil)
	{
		boolean isDeclined =false;
		int len = sil.length();
		String dev;

		if (len<3) return null;

		char lastCar = sil.charAt(len-1);
		if ((lastCar == 's' || lastCar == 'r') && isVowel(sil.charAt(len-2)))
		{
			isDeclined=true;
			sil = sil.substring(0, len-1);
		}
		else if ((lastCar == 'i' || lastCar == 'o') && sil.charAt(len-2)=='\'')
		{
			isDeclined=true;
			sil = sil.substring(0, len-2);
		}

		if (!isDeclined) return null;
		return sil;
	}
	
	public void scanBody(String in)
	{
		Word word;
		boolean hayMasLineas=true;

		if (in.equals("")) finishUp();
		else
		{
			int init = 0, fin;
			String linea;

			while (hayMasLineas)
			{
				fin = in.indexOf("\n",init);
				if (fin<0)
				{
					linea = in.substring(init).trim();
					hayMasLineas=false;
				}
				else
					linea = in.substring(init, fin).trim();

				if (linea.equals(""))
				{
				    finishUp();
		            wordList.addLast(new PunctuationMark('\n'));
				}
				else
					scanLine(linea);

				init = fin+1;
			}
		}
	}
		
	private boolean isEndOfSyllable(int ch)
	{
	    return (endOfSyllableMarks.indexOf(ch)>-1);
	}
	
	public void scanLine(String linea)
	{
		int init = 0, fin, i;
		char ch;
		String sil;
		boolean doNotFinishUp;

		if (linea.equals(""))
		{
		    finishUp();
		    wordList.addLast(new PunctuationMark('\n'));
			return;
		}

outAHere:
		while(true)
		{
		    doNotFinishUp=true;

		    // Make init skip all punctuation marks
			while (true)
			{
				if (init>=linea.length())
					break outAHere;
			    ch = linea.charAt(init);
			    if (endOfParagraphMarks.indexOf(ch)>=0)
			    {
			        if (doNotFinishUp)
			        {
        			    finishUp();
			            doNotFinishUp=false;
			        }
			        wordList.addLast(new PunctuationMark(ch));
			    }
			    else if (endOfSyllableMarks.indexOf(ch)<0)
			        break;

				init++;
			}
			
		    doNotFinishUp = true;
			
			/* move fin to the end of the next syllable. If finishing
			   up is necessary it is done after scanSyllable
			*/
			
		    fin = init+1;
			while (true)
			{
			    ch = linea.charAt(fin);
			    if (endOfParagraphMarks.indexOf(ch)>=0)
			    {
			        doNotFinishUp = false;
			        break;
			    }
			    else if (endOfSyllableMarks.indexOf(ch)>=0)
			    {
			        break;
			    }
			    else
			    {
				    fin++;
				    if (fin>=linea.length())
					    break;
				}
			}
			
			sil = linea.substring(init, fin);
			scanSyllable(sil);

		    if (!doNotFinishUp)
		    {
			        finishUp();
                    wordList.addLast(new PunctuationMark(ch));
			}
			init = fin+1;
		}
	}

	public LinkedList getTokenLinkedList()
	{
		return wordList;
	}

	public Token[] getTokenArray()
	{
		int i=wordList.size();
		Token token[] = new Token[i];
		Object obj;
		ListIterator li = wordList.listIterator();
		while(li.hasNext())
			token[--i] = (Token)li.next();
		return token;
	}

	private static boolean isVowel(char ch)
	{
		return (ch=='a' || ch=='e' || ch=='i' || ch=='o' || ch=='u');
	}

	/** Looks for .dic file, and returns the dictionary descriptions.
		Also updates the definitionTags in the Definitions class.
	*/
	public String[] getDictionaryDescriptions()
	{
		int n;
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo + ".dic")));
			LinkedList ll1 = new LinkedList(), ll2 = new LinkedList();
			int i;
			String s;
			while ((s=br.readLine())!=null)
			{
				n = s.indexOf(",");
				if (n < 0)
				{
					ll1.addLast(null);
					ll2.addLast(s);
				}
				else
				{
					ll1.addLast(s.substring(0,n).trim());
					ll2.addLast(s.substring(n+1).trim());
				}
			}
			Definitions.defTags = ll2.toStringArray();
			return ll1.toStringArray();
		}
		catch (Exception e)
		{
			return null;
		}
	}
}