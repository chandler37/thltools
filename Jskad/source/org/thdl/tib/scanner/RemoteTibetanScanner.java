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

import java.net.*; 
import java.io.*; 

public class RemoteTibetanScanner implements TibetanScanner
{
	private String url;
	private LinkedList wordList;
	private DictionarySource defSourcesWanted;
		
	public RemoteTibetanScanner(String url) throws Exception
	{
		defSourcesWanted = DictionarySource.getAllDictionaries();
		this.url = url;
		wordList = new LinkedList();
	}
	
	/** dont use */
	public void scanLine(String linea)
	{
		scanBody(linea);
	}
	
	public void scanBody(String linea)
	{
		try
		{
			URLConnection uC = new URL(url + "?dicts=" + Integer.toString(defSourcesWanted.getDicts())).openConnection();
			// URLConnection uC = new URL(url).openConnection();
			uC.setDoInput(true);
			uC.setDoOutput(true);
			uC.setUseCaches(false);
			uC.setRequestProperty("Content-type", "application/octet-stream");
			OutputStream os = uC.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			
			pw.println(linea);
			pw.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(uC.getInputStream()));
			String word, eachDef, def;
			outside:
			while((word = in.readLine())!=null && (def = in.readLine())!=null)
			{
				do
				{
					eachDef = in.readLine();
					if (eachDef==null)
					{
						wordList.addLast(new Word(word, def));
						break outside;
					}
					if (eachDef.equals("")) break;
					def+='\n' + eachDef;
				}
				while(true);
				wordList.addLast(new Word(word, def));
			}
			in.close();
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
	
	public LinkedList getTokenLinkedList()
	{
		return wordList;
	}
	
	public void clearTokens()
	{
		wordList = new LinkedList();
	}
	
	public Token[] getTokenArray()
	{
		int i=0;
		Token token[] = new Token[wordList.size()];
		ListIterator li = wordList.listIterator();
		while(li.hasNext())
			token[i++] = (Token)li.next();
		return token;
	}

	/** does not do anything */
	public void finishUp()
	{
	}	
	
	public DictionarySource getDictionarySource()
	{
		return defSourcesWanted;
	}
	
	public String[] getDictionaryDescriptions()
	{
		int n;
		try
		{
			URLConnection uC = new URL(url+"?dicts=names").openConnection();
			uC.setDoInput(true);
			uC.setDoOutput(false);
			uC.setUseCaches(false);
			uC.setRequestProperty("Content-type", "text/plain");

			BufferedReader br = new BufferedReader(new InputStreamReader(uC.getInputStream()));
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
			br.close();
			Definitions.defTags = ll2.toStringArray();
			return ll1.toStringArray();
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
