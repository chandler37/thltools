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

import org.thdl.util.*;
import java.util.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/** Running on the server, receives the tibetan text from applet/applications running on
    the client and sends them the words with their definitions through the Internet.
    Requests are made through {@link RemoteTibetanScanner}.
    
    @author Andr&eacute;s Montano Pellegrini
    @see RemoteTibetanScanner
*/
public class RemoteScannerFilter extends GenericServlet
{
	private TibetanScanner scanner;
	private BitDictionarySource ds;
	
	public RemoteScannerFilter() throws Exception
	{
		ResourceBundle rb = ResourceBundle.getBundle("dictionary");
		scanner = new LocalTibetanScanner(rb.getString("onlinescannerfilter.dict-file-name"));
		ds = scanner.getDictionarySource();

    		
	    String fileName = rb.getString("remotescannerfilter.log-file-name");
	    Calendar rightNow = Calendar.getInstance();
	    	
	    PrintStream pw = new PrintStream(new FileOutputStream(fileName, true));
	    pw.println("Testing: " + rightNow.toString());
	    pw.flush();
	    pw.close();
	}

  	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException
  	{
  		BufferedReader br;
  		res.setContentType ("text/plain");
  		Token token[] = null;  		
  		Word word = null;
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
					out.println(dicDescrip[i] + "," + DictionarySource.defTags[i]);
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
  		
  		/*  FIXME: sometimes getDef returns raises a NullPointerException.
  		    In the meantime, I'll just keep it from crashing
  		*/
  		try
  		{
  		    while((linea = br.readLine())!= null)
  			    scanner.scanLine(linea);
      		
  		    br.close();
      		
		    scanner.finishUp();
		    token = scanner.getTokenArray();
    		
		    for (i=0; i<token.length; i++)
		    {
			    if (!(token[i] instanceof Word)) continue;
			    word = (Word) token[i];
			    out.println(word.getWylie());
			    out.println(word.getDef());
			    out.println();
		    }
		}
		catch (Exception e)
		{
    		ResourceBundle rb = ResourceBundle.getBundle("dictionary");
	    	String fileName = rb.getString("remotescannerfilter.log-file-name");
	    	Calendar rightNow = Calendar.getInstance();
	    	
	    	PrintStream pw = new PrintStream(new FileOutputStream(fileName, true));
	    	pw.println("Translation tool crashed on: " + rightNow.toString());
	    	e.printStackTrace(pw);
	    	pw.println();
	    	pw.println("Word that crashed: " + word.getWylie());
	    	pw.println();
	    	pw.println("All words:");
		    for (i=0; i<token.length; i++)
		    {
			    if (!(token[i] instanceof Word)) continue;
			    word = (Word) token[i];
			    out.println(word.getWylie());
			}
			pw.println();
			pw.flush();
			pw.close();
		}

		scanner.clearTokens();
	    out.close();
	}
}