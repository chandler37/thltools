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

import java.io.PrintWriter;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thdl.util.ThdlOptions;

/** Interfase to provide access to an on-line dictionary through a form in html; 
    Inputs Tibetan text (Roman script only) and displays the
    words (Roman or Tibetan script) with their definitions.
    Runs on the server and is called upon through an HTTP request directly
    by the browser.  Requires no additional software installed on the client.

    @author Andr&eacute;s Montano Pellegrini
*/
public class OnLineScannerFilter extends HttpServlet
{
    private final static String propertyFile = "dictionary";
    private final static String dictNameProperty = "onlinescannerfilter.dict-file-name";
    private final static String otherLinksProperty = "onlinescannerfilter.links-to-other-stuff";
    private final static String clearStr = "Clear"; 
    private final static String buttonStr = "button";
    private final static String scriptStr = "script";
    private final static String tibetanStr = "tibetan";
    
    ResourceBundle rb;
	private TibetanScanner scanner;
	private String dictionaries[];
	private ScannerLogger sl;
	
	public OnLineScannerFilter() //throws Exception
	{
		rb = ResourceBundle.getBundle(propertyFile);
                sl = new ScannerLogger();
	    
		try
		{
		    scanner = new LocalTibetanScanner(rb.getString(dictNameProperty), false);
		}
		catch (Exception e)
		{
		    sl.writeLog("Crash\tOnLineScannerFilter");
		    sl.writeException(e);
		}
		
		dictionaries = scanner.getDictionaryDescriptions();
		sl.writeLog("Creation\tOnLineScannerFilter");
	}

    synchronized public void doGet(HttpServletRequest request,
                      HttpServletResponse response) //throws IOException, ServletException
    {
		String answer, parrafo = null, checkboxName;
        
        // if this line is included in the constructor, it works on the iris server but not on wyllie!
        ThdlOptions.setUserPreference("thdl.rely.on.system.tmw.fonts", true);	    
        ThdlOptions.setUserPreference("thdl.rely.on.system.tm.fonts", true);
        
        response.setContentType("text/html");
        PrintWriter out;
        sl.setUserIP(request.getRemoteAddr());
        
        try
        {
            out = response.getWriter();
        }
		catch (Exception e)
		{
		    sl.writeLog("Crash\tOnLineScannerFilter");
		    sl.writeException(e);
		    return;
		}
				
		BitDictionarySource ds=null;
		boolean checkedDicts[], allUnchecked, wantsTibetan, useTHDLBanner = (request.getParameter("thdlBanner")!=null);
		// int percent=100;
		
		if (useTHDLBanner)
		{
		    out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">");
		}
		else out.println("<html>");
		
        out.println("<head>");
		if (useTHDLBanner)
		{
            out.println(" <title>Tibetan and Himalayan Digital Library - The Online Tibetan to English Translation/Dictionary Tool</title>");
            out.println(" <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
            out.println(" <script type=\"text/javascript\" src=\"/tibet/scripts/thdl_scripts.js\"></script>");
            out.println(" <link rel=\"stylesheet\" type=\"text/css\" href=\"/tibet/style/thdl-styles.css\"/>");
        }
        else
            out.println(" <title>The Online Tibetan to English Translation/Dictionary Tool</title>");
            
        out.println(" <meta name=\"keywords\" content=\"tibetan, english, dictionary, jim valby, rangjung yeshe, jeffrey hopkins, tsig mdzod chen mo, online, translation, scanner, parser, buddhism, language, processing, font, dharma, chos, tibet\">");
        out.println(" <meta name=\"Description\" content=\"This Java tool takes Tibetan language passages and divides the passages up into their component phrases and words, and displays corresponding dictionary definitions.\">");
        out.println(" <meta name=\"MSSmartTagsPreventParsing\" content=\"TRUE\">");
		
        answer = request.getParameter(scriptStr);
        
        /* script==null || makes default tibetan
           script!=null && makes default roman
        */
        wantsTibetan = (answer==null || answer.equals(tibetanStr));
        if (wantsTibetan)
        {
	    	out.println("<style>.tmw {font: 28pt TibetanMachineWeb}");
		    out.println(".tmw1 {font: 28pt TibetanMachineWeb1}");
    		out.println(".tmw2 {font: 28pt TibetanMachineWeb2}");
	    	out.println(".tmw3 {font: 28pt TibetanMachineWeb3}");
		    out.println(".tmw4 {font: 28pt TibetanMachineWeb4}");
    		out.println(".tmw5 {font: 28pt TibetanMachineWeb5}");
	    	out.println(".tmw6 {font: 28pt TibetanMachineWeb6}");
		    out.println(".tmw7 {font: 28pt TibetanMachineWeb7}");
    		out.println(".tmw8 {font: 28pt TibetanMachineWeb8}");
	    	out.println(".tmw9 {font: 28pt TibetanMachineWeb9}");
		    out.println("</style>");
        }
        out.println("</head>");
        out.println("<body>");
        
		if (useTHDLBanner)
		{
            out.println("<div id=\"banner\">");
            out.println(" <a id=\"logo\" href=\"/tibet/index.html\"><img id=\"test\" alt=\"THDL Logo\" src=\"/tibet/images/logo.png\"/></a>");
            out.println(" <h1>Tibetan and Himalayan Digital Library</h1>");
            out.println("  <div id=\"menubar\">");
            out.println(" <script type=\'text/javascript\'>function Go(){return}</script>");
            out.println(" <script type=\'text/javascript\' src=\'/tibet/scripts/new/thdl_menu_config.js\'></script>");
            out.println(" <script type=\'text/javascript\' src=\'/tibet/scripts/new/menu_new.js\'></script>");
            out.println(" <script type=\'text/javascript\' src=\'/tibet/scripts/new/menu9_com.js\'></script>");
            out.println(" <noscript><p>Your browser does not support javascript.</p></noscript>");
            out.println(" <div id=\'MenuPos\' >Menu Loading... </div>");
            
            out.println(" </div><!--END menubar-->");
            out.println("</div><!--END banner-->");

            out.println("<div id=\"sub_banner\">");
            out.println("<div id=\"search\">");
            out.println("  <form method=\"get\" action=\"http://www.google.com/u/thdl\">");
            out.println("   <p>");
            out.println("    <input type=\"text\" name=\"q\" id=\"q\" size=\"15\" maxlength=\"255\" value=\"\" />");
            out.println("    <input type=\"submit\" name=\"sa\" id=\"sa\" value=\"Search\"/>");
            out.println("    <input type=\"hidden\" name=\"hq\" id=\"hq\" value=\"inurl:orion.lib.virginia.edu\"/>");
            out.println("   </p>");
            out.println("  </form>");
            out.println(" </div>");
            out.println(" <div id=\"breadcrumbs\">");
            out.println("  <a href=\"/tibet/index.html\">Home</a> &gt; <a href=\"/tibet/reference/index.html\">Reference</a> &gt; Translation Tool");
            out.println(" </div>");
            out.println("</div><!--END sub_banner-->");
            out.println("<div id=\"main\">");
        }
        
        out.println("<h3 align=\"center\">The Online Tibetan to English Translation/Dictionary Tool</h3>");
        
        try
        {
            out.println(rb.getString(otherLinksProperty));
        }
        catch (MissingResourceException e)
        {
            // do nothing
        }
        
		if (useTHDLBanner)
		{
            out.println("<form action=\"org.thdl.tib.scanner.OnLineScannerFilter?thdlBanner=on\" method=POST>");
        }
        else
        {
            out.println("<form action=\"org.thdl.tib.scanner.OnLineScannerFilter\" method=POST>");
        }
        out.println("<table border=\"0\" width=\"100%\">");
        out.println("  <tr>");
        out.println("    <td width=\"25%\">");
        out.println("      <p>Display results in:</td>");
        out.println("    <td width=\"75%\">");
        out.println("      <p><input type=\"radio\" value=\"" + tibetanStr + "\" ");
        if (wantsTibetan) out.println("checked ");
        out.println("name=\"" + scriptStr + "\">Tibetan script (using <a href=\"http://orion.lib.virginia.edu/thdl/tools/tmw.html\" target=\"_blank\">Tibetan Machine Web font</a>)<br/>");
        out.println("      <input type=\"radio\" value=\"roman\" ");
        if (!wantsTibetan) out.println("checked ");
        out.println("name=\"" + scriptStr + "\">Roman script</td>");
        out.println("  </tr>");
        out.println("</table>");

		if (dictionaries!=null)
		{
			int i;
			ds = scanner.getDictionarySource();
			ds.reset();
			checkedDicts = new boolean[dictionaries.length];
/*			out.println("  <tr>");
			out.println("<td width=\""+ percent +"%\">Search in dictionaries:</td>");*/
			out.println("<p>Search in dictionaries: ");
			allUnchecked=true;
			for (i=0; i<dictionaries.length; i++)
			{
				checkboxName = "dict"+ i;
				checkedDicts[i] = (request.getParameter(checkboxName)!=null);
			}
			allUnchecked=true;
			for (i=0; i<dictionaries.length; i++)
			{
				if(checkedDicts[i])
				{
					allUnchecked=false;
					break;
				}
			}
			
			if (allUnchecked)
			{
				for (i=0; i<dictionaries.length; i++)
					checkedDicts[i] = true;
			}
			
			for (i=0; i<dictionaries.length; i++)
			{
				checkboxName = "dict"+ i;
//				out.print("  <td width=\"" + percent + "%\">");
				out.print("<input type=\"checkbox\" name=\"" + checkboxName +"\" value=\""+ checkboxName +"\"");
				if (checkedDicts[i])
				{
					out.print(" checked");
					ds.add(i);
				}
				if (dictionaries[i]!=null)
					out.print(">" + dictionaries[i] + " (" + DictionarySource.defTags[i] + ")&nbsp;&nbsp;&nbsp;");
				else
					out.print(">" + DictionarySource.defTags[i] + "&nbsp;&nbsp;&nbsp;");				
//				out.println(" + "</td>");
			}
//			out.println("  </tr>");
		}
		// fix for updates
		else ds = new BitDictionarySource().getAllDictionaries();
//		out.println("</table>");
		out.println("</p>");
        out.println("<table border=\"0\" width=\"100%\">");
        out.println("  <tr>");
        out.println("    <td width=\"35%\">");		
		out.println("      <p><strong>Input text:</strong></p>");
        out.println("    </td>");
        out.println("    <td width=\"65%\">");
        out.println("      <p> <input type=\"submit\" name=\"" + buttonStr + "\" value=\"Translate\"> <input type=\"submit\" name=\"" + buttonStr + "\" value=\"" + clearStr + "\"></p>");
        out.println("    </td>");
        out.println("  </tr>");
        out.println("</table>");
		
        out.println("<textarea rows=\"12\" name=\"parrafo\" cols=\"60\">");

        // Paragraph should be empty if the user just clicked the clear button
		answer = request.getParameter(buttonStr);
        if (answer == null || answer != null && !answer.equals(clearStr))
        {
		    parrafo = request.getParameter("parrafo");
		    if (parrafo!=null) out.print(parrafo);            
        }
        
		out.println("</textarea>");
        out.println("</form>");
		
        if (parrafo != null)
        {
            sl.writeLog("Translation\tOnLineScannerFilter");
        	if (ds!=null && !ds.isEmpty())
        		desglosar(parrafo, out, wantsTibetan);
        }
        else sl.writeLog("Invocation\tOnLineScannerFilter");
		
		out.println(TibetanScanner.copyrightHTML);
		if (useTHDLBanner) out.println("</div><!--END main-->");
        out.println("</body>");
        out.println("</html>");
    }
	
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        //throws IOException, ServletException
    {
        doGet(request, response);
    }
	
	synchronized public void desglosar(String in, PrintWriter pw, boolean tibetan)
	{
		//boolean hayMasLineas=true;
		//int init = 0, fin;
		//String linea;
		Object words[];
		
		if (!in.equals(""))
		{
		/*	while (hayMasLineas)
			{
				fin = in.indexOf("\n",init);
				if (fin<0)
				{
					linea = in.substring(init).trim();
					hayMasLineas=false;
				}
				else
					linea = in.substring(init, fin).trim();
				
				scanner.scanBody(linea);
				
				init = fin+1;
			}	*/
			scanner.clearTokens();
			scanner.scanBody(in);
			scanner.finishUp();
			printText(pw, tibetan);
			printAllDefs(pw, tibetan);
			scanner.clearTokens();			
		}
	}
	
	public void printText(PrintWriter pw, boolean tibetan)
	{
		Token words[] = scanner.getTokenArray();
		SwingWord word;
		char pm;
		int i;
		
		if (words==null) return;
		
		pw.print("<p>");
		for (i=0; i < words.length; i++)
		{
			
                    if (words[i] instanceof Word)
                    {
                        word = new SwingWord((Word)words[i]);
                        // if (word.getDefs().getDictionarySource()!=null)
                        pw.print(word.getLink());
                        // else pw.print(word.getWylie() + " ");
                    }
                    else
                    {
                        if (words[i] instanceof PunctuationMark)
                        {
                            pm = words[i].toString().charAt(0);
                            switch (pm)
                            {
                                case '\n':
                                    pw.println("</p>");
                                    pw.print("<p>");
                                break;
                                case '<':
                                    pw.print("&lt; ");
                                break;
                                case '>':
                                    pw.print("&gt; ");
                                break;
                               default:
                                    pw.print(pm + " ");
                            }
                        }      
                    }
		}
		pw.println("</p>");
	}
	
	public void printAllDefs(PrintWriter pw, boolean tibetan) {
		int i, j, k=0;
		Word words[];
		SwingWord word = null;
		Definitions defs;
		String tag;
		DictionarySource ds;
		ByteDictionarySource sourceb=null;

		words = scanner.getWordArray(false);

		if (words == null)
			return;

		pw.println("<table border=\"1\" width=\"100%\">");

		for (j = 0; j < words.length; j++) {
			try {

				word = new SwingWord(words[j]);
				defs = word.getDefs();
				ds = defs.getDictionarySource();
				pw.println("  <tr>");
				if (ds == null) {
					tag = "&nbsp;";
				}
				else {
					if (FileSyllableListTree.versionNumber==2) {
						tag = ds.getTag(0);
					}
					else {
						sourceb = (ByteDictionarySource) ds;
					    k=0;
					    while (sourceb.isEmpty(k)) k++;
					    tag = sourceb.getTag(k);
					    k++;
					}
				}
				
				pw.println("    <td width=\"20%\" rowspan=\"" + defs.def.length
						+ "\" valign=\"top\">" + word.getBookmark(tibetan)
						+ "</td>");
				pw.println("    <td width=\"12%\">" + tag + "</td>");
				pw.println("    <td width=\"68%\">" + defs.def[0] + "</td>");

				pw.println("  </tr>");
				for (i = 1; i < defs.def.length; i++) {
					pw.println("  <tr>");
					
					if (FileSyllableListTree.versionNumber==2) {
						tag = ds.getTag(i);
					}
					else {
					    while (sourceb.isEmpty(k)) k++;
					    tag = sourceb.getTag(k);
					    k++;
					}
					
					pw.println("    <td width=\"12%\">" + tag + "</td>");
					pw.println("    <td width=\"68%\">" + defs.def[i] + "</td>");
					//else pw.println("    <td width=\"80%\" colspan=\"2\">" + defs.def[i] + "</td>");
					pw.println("  </tr>");
				}
			} catch (Exception e) {
				sl.writeLog("Crash\tOnLineScannerFilter\t" + word.getWylie());
				sl.writeException(e);
			}

		}
		pw.println("</table>");
	}
	
	public void destroy()
	{
	    super.destroy();
	    sl.setUserIP(null);
	    sl.writeLog("Shutdown\tOnLineScannerFilter");
	    scanner.destroy();
	}
	
}
