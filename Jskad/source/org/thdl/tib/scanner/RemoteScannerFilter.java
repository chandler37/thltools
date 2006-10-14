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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.GenericServlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

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
	private ScannerLogger sl;
	
	public RemoteScannerFilter()
	{
		ResourceBundle rb = ResourceBundle.getBundle("dictionary");
		sl = new ScannerLogger();

	    try
	    {
		    scanner = new LocalTibetanScanner(rb.getString("onlinescannerfilter.dict-file-name"),false);
		}
		catch (Exception e)
		{
		    sl.writeLog("1\t2");
		    sl.writeException(e);
		}		
		ds = scanner.getDictionarySource();
		sl.writeLog("Creation\t2");
	}

  	public void service(ServletRequest req, ServletResponse res) //throws ServletException, IOException
  	{
  		BufferedReader br;
  		res.setContentType ("text/plain");
  		sl.setUserIP(req.getRemoteAddr());
  		
  		Word word = null, words[] = null;
  		PrintWriter out;
  		
  		try
  		{
	        out = res.getWriter();
	    }
		catch (Exception e)
		{
		    sl.writeLog("1\t2");
		    sl.writeException(e);
		    return;
		}		
	    
	    int i;
  		String linea, dicts = req.getParameter("dicts"), dicDescrip[];
  		
  		if (dicts!=null)
  		{
  			if (dicts.equals("names"))
  			{
  			    sl.writeLog("3\t2");
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
  		
  		try
  		{
  		    br = new BufferedReader(new InputStreamReader(req.getInputStream()));
  		}
		catch (Exception e)
		{
		    sl.writeLog("1\t2");
		    sl.writeException(e);
		    return;
		}		
  		
  		
  		/*  FIXME: sometimes getDef raises a NullPointerException.
  		    In the meantime, I'll just keep it from crashing
  		*/
  		sl.writeLog("4\t2");
  		
  		try
  		{
                    scanner.clearTokens();
  		    while((linea = br.readLine())!= null)
  			    scanner.scanLine(linea);
      		
  		    br.close();
      		
		    scanner.finishUp();
		    words = scanner.getWordArray();
    		
		    for (i=0; i<words.length; i++)
		    {
                        linea = words[i].getDef();
                        if (linea == null) continue;
                        out.println(words[i].getWylie());
                        out.println(linea);
                        out.println();
		    }
		}
		catch (Exception e)
		{
		    sl.writeLog("1\t2\t" + word.getWylie());
		    sl.writeException(e);
		}

		scanner.clearTokens();
	    out.close();
	}
	
	public void destroy()
	{
	    super.destroy();
	    sl.setUserIP(null);
	    sl.writeLog("5\t2");
	    scanner.destroy();
	}
}