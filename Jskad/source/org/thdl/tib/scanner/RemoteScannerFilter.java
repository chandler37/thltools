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
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class RemoteScannerFilter extends GenericServlet
{
	private TibetanScanner scanner;
	private DictionarySource ds;
	
	public RemoteScannerFilter() throws Exception
	{
		ResourceBundle rb = ResourceBundle.getBundle("dictionary");
		scanner = new LocalTibetanScanner(rb.getString("onlinescannerfilter.dict-file-name"));
		ds = scanner.getDictionarySource();
	}

  	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException
  	{
  		BufferedReader br;
  		res.setContentType ("text/plain");
  		LinkedList words;
  		Word word;
	    PrintWriter out = res.getWriter();
	    int i;
  		String linea, dicts = req.getParameter("dicts"), dicDescrip[];
  		if (dicts!=null)
  		{
  			if (dicts.equals("names"))
  			{
				dicDescrip = scanner.getDictionaryDescriptions();
				if (dicDescrip==null)
				{
					out.close();
					return;
				}
				
				for (i=0; i<dicDescrip.length; i++)
				{
					out.println(dicDescrip[i] + "," + Definitions.defTags[i]);
				}
				out.close();
				return;
  			}
  			else
  			{
  				ds.setDicts(Integer.parseInt(dicts));
  			}
  		}
  		br = new BufferedReader(new InputStreamReader(req.getInputStream()));
  		
  		while((linea = br.readLine())!= null)
  			scanner.scanLine(linea);
  		
  		br.close();
  		
		scanner.finishUp();
		words = scanner.getTokenLinkedList();
		Token token;
		
		ListIterator li = words.listIterator();
		
		while (li.hasNext())
		{
			token = (Token)li.next();
			if (!(token instanceof Word)) continue;
			word = (Word) token;
			out.println(word.getWylie());
			out.println(word.getDef());
			out.println();
		}
		scanner.clearTokens();
	    out.close();
	}
}