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
import org.thdl.tib.text.*;
import java.net.*;
import java.io.*;

public class DictionaryBreakDown
{
    private static final int TOMERAIDER=1;
    private static final int HTML=2;
    private static final int SIMPLEBREAK=3;
    private int mode;
    private BufferedReader in;
    private int numberOfFields;
    private int numberOfMergedFields;
    
    private static final int UNIQUE=0;
    private static final int MERGE_HEAD=1;
    private static final int MERGE_FOLLOWER=2;    
    
    public DictionaryBreakDown(BufferedReader in, int mode)
    {
        this.mode = mode;
        this.in = in;
    }

	public static void main (String[] args) throws Exception
	{
		PrintWriter out;
		BufferedReader in=null;
		int argNum = args.length, currentArg=0, mode = SIMPLEBREAK;
		String option, format=null;
				
		if (argNum<=currentArg)
		{
		    System.out.println("Syntax: DictionaryBreakDown [-format format] [-tomeraider | -html] input-file");
		    return;
		}
		
		while (args[currentArg].charAt(0)=='-')
		{
		    option = args[currentArg].substring(1);		        
		    currentArg++;
		    if (option.equals("format"))
		    {
		        format=args[currentArg];
		        currentArg++;
		    } else if (option.equals("tomeraider"))
		    {
		        mode=TOMERAIDER;
		    } else if (option.equals("html"))
		    {
		        mode=HTML;
		    }
		}
        if (argNum<=currentArg)
        {
            System.out.println("Syntax error. Input file expected.");
            return;
        }
        
		in = getBufferedReader(args[currentArg], format);
		
		new DictionaryBreakDown(in, mode).run(format);
    }
    
	public static BufferedReader getBufferedReader(String s, String format) throws Exception
	{
	    InputStream is;
	    
    	if (s.indexOf("http://") >= 0) 
			is = new BufferedInputStream((new URL(s)).openStream());
		else
		    is = new FileInputStream(s);
		    
		if (format==null)
		    return new BufferedReader(new InputStreamReader(is));
		else
		    return new BufferedReader(new InputStreamReader(is, format));			
		
	}
		
	private int[] buildMergeMatrix(String fields[])
	{
	    int i;
	    int matrix[] = new int[fields.length];
	    boolean sameRoot=false;
	    String root;
	    
	    for (i=0; i<matrix.length; i++)
	        matrix[i] = UNIQUE;
	    
	    i=0;
	    while (i<fields.length)
	    {
	        if (Character.isDigit(fields[i].charAt(fields[i].length()-1)))
	        {
	            matrix[i] = MERGE_HEAD;
	            root = fields[i].substring(0,fields[i].length()-1);
	            i++;
	            while (i<fields.length && (sameRoot = fields[i].substring(0,fields[i].length()-1).equals(root)))
	            {
	                matrix[i] = MERGE_FOLLOWER;
	                i++;
	            }
	            if (!sameRoot) i--;
	        }
	        i++;
	    }
	    return matrix;
	}
	
	private static String deleteComas(String s)
	{
	    if (s.length()>1 && s.charAt(0)=='\"' && s.charAt(s.length()-1)=='\"')
	        return s.substring(1,s.length()-1);
	    else return s;
	}
	
	private String[] getFields(String linea)
	{
	    int i=0, j, pos;
	    String fields[] = new String[this.numberOfFields], tokens[];
	    
	    tokens = linea.split("\t");
	    for (i=0; i<tokens.length; i++)
	    {
	        fields[i] = deleteComas(tokens[i]);
	    }
    
	    for (j=i; j<fields.length; j++)
	        fields[j]="";
	    
	    return fields;
	}
	
	private String[] buildMergedFieldNames(String fieldNames[], int mergeMatrix[])
	{
	    int i;
	    SimplifiedLinkedList ll = new SimplifiedLinkedList();
	    for (i=0; i<mergeMatrix.length; i++)
	    {
	        switch (mergeMatrix[i])
	        {
	            case UNIQUE: ll.addLast(fieldNames[i]);
	            break;
	            case MERGE_HEAD: ll.addLast(fieldNames[i].substring(0,fieldNames[i].length()-1));
	        }
	            
	    }
	    return ll.toStringArray();
	}
	
	public String [] getMergeFields(String fields[], int mergeMatrix[])
	{
	    // I can safely assume that fields contains info and are non null.
	    int i=0, j=0, fieldNum;
	    String mergedFields[] = new String[numberOfMergedFields];
	    
	    outAHere:
	    while(i<fields.length && j<mergedFields.length)
	    {
	        switch(mergeMatrix[i])
	        {
	            case UNIQUE:
	                mergedFields[j] = fields[i];
	                i++; j++;
	            break;
	            case MERGE_HEAD:
	                fieldNum=1;
	                if (!fields[i].equals("") && !fields[i+1].equals(""))
	                    mergedFields[j] = fieldNum + ". " + fields[i];
	                else mergedFields[j]=fields[i];
	                i++; fieldNum++;
	                while (mergeMatrix[i]==MERGE_FOLLOWER)
	                {
	                    if (!fields[i].equals(""))
	                    {
	                        if (!mergedFields[j].equals(""))
	                            mergedFields[j]+="; " + fieldNum + ". " + fields[i];
	                        else mergedFields[j] = fieldNum + ". " + fields[i];
	                    }
	                    i++; fieldNum++;
	                    if (i>=fields.length) break outAHere;
	                }
	                j++;
	        }
	    }
	    return mergedFields;
	}
	
	public void run(String format) throws Exception
	{
		String linea, fieldNames[], fields[], mergedFieldNames[], mergedFields[], tail;
		int i, pos, j;
		int mergeMatrix[];
		PrintWriter out[] = null, outOne = null;
		SimplifiedLinkedList ll = null, llOdd = null;
		SimplifiedListIterator sli;
		char ch;
		
		linea=in.readLine();
		if (linea==null) throw new Exception("File is empty!");
		fieldNames = linea.split("\t");
		numberOfFields = fieldNames.length;
		mergeMatrix = buildMergeMatrix(fieldNames);
		mergedFieldNames = buildMergedFieldNames(fieldNames, mergeMatrix);
		numberOfMergedFields = mergedFieldNames.length;
		
		switch (mode)
		{
		    case TOMERAIDER:
		        if (format!=null) outOne = new PrintWriter(new OutputStreamWriter(new FileOutputStream("dict-in-tomeraider-format.txt"), format));
    		    else outOne = new PrintWriter(new FileOutputStream("dict-in-tomeraider-format.txt"));
	    	    ll = new SimplifiedLinkedList();
		        llOdd = new SimplifiedLinkedList();
		    break;
		    
		    case HTML:
		        if (format != null) outOne = new PrintWriter(new OutputStreamWriter(new FileOutputStream("dict-in-tab-format.txt"), format));
		        else outOne = new PrintWriter(new FileOutputStream("dict-in-tab-format.txt"));
		    break;
		    
		    case SIMPLEBREAK:
    		    out = new PrintWriter[mergedFieldNames.length-1];
		        for (i=0; i<out.length; i++)
		        {
		            if (format!=null) out[i] = new PrintWriter(new OutputStreamWriter(new FileOutputStream(mergedFieldNames[i+1] + ".txt"), format));
		            else out[i] = new PrintWriter(new FileOutputStream(mergedFieldNames[i+1] + ".txt"));
	    	    }		    
		}
		
		while ((linea=in.readLine())!=null)
		{
		    fields = getFields(linea);
		    mergedFields = getMergeFields(fields, mergeMatrix);
		    switch (mode)
		    {
		        case TOMERAIDER:
		            linea = fields[0];
		            
		            forBreak:
		            for (i=0; i<linea.length(); i++)
		            {
		                ch = linea.charAt(i);
		                if (ch==';' || ch=='/')
		                {
		                    linea = linea.substring(0, i);
		                    break forBreak;
		                }
		            }
		            pos = linea.indexOf("...");
		            if (pos>0) linea = linea.substring(0,pos).trim();
		            pos = linea.indexOf('+');
		            if (pos>0)
		            {
		                llOdd.addLast(mergedFields);
		                continue;
		            }
		            try
		            {
    		            ll.addSorted(new SortingTibetanEntry(linea, mergedFields));
		            }
		            catch (Exception e)
		            {
		                llOdd.addLast(mergedFields);
		            }
    		        
		        break;
		        case HTML:
		            tail = null;
    		        
		            pos = mergedFields[0].indexOf("...");
    		        
		            if (pos==0) mergedFields[0] = mergedFields[0].substring(3).trim();
		            else
		            if (pos>0)
		            {
		                tail = mergedFields[0].substring(pos + 3).trim();
		                mergedFields[0] = mergedFields[0].substring(0,pos-1).trim();
		            }
		            outOne.print(mergedFields[0] + "\t");
		            
		            for (i=1; i<=2; i++) // tenses & sanskrit
		            {
		                if (!mergedFields[i].equals("")) 
		                {
	                        if (tail!=null) outOne.print("... " + tail + ": " + mergedFields[i] + "<br>"); 
		                    else outOne.print(mergedFields[i] + "<br>");
		                }
		            }
		            
		            if (!mergedFields[3].equals("")) // english
		            {
	                    if (tail!=null) outOne.print("... " + tail + ": <b>" + mergedFields[3] + "</b><br>"); 
		                else outOne.print("<b>" + mergedFields[3] + "</b><br>");
		            }
		            if (!mergedFields[4].equals("")) // english-others
		            {
	                    if (tail!=null) outOne.print("... " + tail + ": <i>" + mergedFields[4] + "</i><br>"); 
		                else outOne.print("<i>" + mergedFields[4] + "</i><br>");
		            }
		            
		            for (i=5; i<=8; i++)
		            {
		                if (!mergedFields[i].equals("")) 
		                {
	                        if (tail!=null) outOne.print("... " + tail + ": " + mergedFields[i] + "<br>"); 
		                    else outOne.print(mergedFields[i] + "<br>");
		                }
		            }
		            
		            if (!mergedFields[9].equals("")) // synonyms-tibetan
		            {
                        if (tail!=null) outOne.print("... " + tail + ": <i>Synonyms: </i>" + mergedFields[9] + "<br>"); 
	                    else outOne.print("<i>Synonyms: </i>" + mergedFields[9] + "<br>");
		            }

		            for (i=10; i<=13; i++)
		            {
		                if (!mergedFields[i].equals("")) 
		                {
	                        if (tail!=null) outOne.print("... " + tail + ": " + mergedFields[i] + "<br>"); 
		                    else outOne.print(mergedFields[i] + "<br>");
		                }
		            }
		            outOne.println();
		        break;
		        case SIMPLEBREAK:
		            tail = null;
    		        
		            pos = mergedFields[0].indexOf("...");
    		        
		            if (pos==0) mergedFields[0] = mergedFields[0].substring(3).trim();
		            else
		            if (pos>0)
		            {
		                tail = mergedFields[0].substring(pos + 3).trim();
		                mergedFields[0] = mergedFields[0].substring(0,pos-1).trim();
		            }
    		        		        
		            for (i=1; i<mergedFields.length; i++)
		            {
		                if (!mergedFields[i].equals(""))
		                {
		                    if (tail!=null)
		                        out[i-1].println(mergedFields[0] + '\t' + "... " + tail + ": " + mergedFields[i]); 
		                    else out[i-1].println(mergedFields[0] + '\t' + mergedFields[i]);
		                }
		            }
		    }
		}
		
		switch (mode)
		{
		    case TOMERAIDER:
		        outOne.println("<new>\nAbout\n");
		        outOne.println(TibetanScanner.aboutTomeraider);
    		    
		        sli = ll.listIterator();
		        while (sli.hasNext())
		        {
		            mergedFields = ((SortingTibetanEntry)sli.next()).get();
		            outOne.println("\n<new>\n" + mergedFields[0] + "\n");
		            if (!mergedFields[1].equals("")) outOne.println(mergedFields[1] + "<br>"); // tenses
		            if (!mergedFields[2].equals("")) outOne.println(mergedFields[2] + "<br>"); // sanskrit
		            if (!mergedFields[3].equals("")) outOne.println("<b>" + mergedFields[3] + "</b><br>"); // english
		            if (!mergedFields[4].equals("")) outOne.println("<i>" + mergedFields[4] + "</i><br>"); // english-others
		            for (i=5; i<=8; i++)
		            {        
		                if (!mergedFields[i].equals("")) outOne.println(mergedFields[i] + "<br>");
		            }
		            if (!mergedFields[9].equals("")) outOne.println("<i>Synonyms: </i>" + mergedFields[9] + "<br>"); // synonyms-tibetan
    		        
		            for (i=10; i<=13; i++)
		            {
		                if (!mergedFields[i].equals("")) outOne.println(mergedFields[i] + "<br>");
		            }		        
		        }
    		    
		        sli = llOdd.listIterator();
		        if (sli.hasNext())
		        {
		            outOne.println("\n<new>\nUnsorted entries start here.\n");
		            outOne.println("Because of errors in the entries with regards to:");
		            outOne.println("<ol>");
		            outOne.println("<li>Invalid tibetan");
		            outOne.println("<li>Conversion from Tibetan script to extended wylie");
		            outOne.println("<li>or the sorting algorithm");
		            outOne.println("</ol>");
		            outOne.println("the following entries could not be sorted. Hopefully these errors will be corrected in future versions.");
		        }
		        while (sli.hasNext())
		        {
		            mergedFields = (String[])sli.next();
		            outOne.println("\n<new>\n" + mergedFields[0] + "\n");
		            if (!mergedFields[1].equals("")) outOne.println(mergedFields[1] + "<br>"); // tenses
		            if (!mergedFields[2].equals("")) outOne.println(mergedFields[2] + "<br>"); // sanskrit
		            if (!mergedFields[3].equals("")) outOne.println("<b>" + mergedFields[3] + "</b><br>"); // english
		            if (!mergedFields[4].equals("")) outOne.println("<i>" + mergedFields[4] + "</i><br>"); // english-others
		            for (i=5; i<=8; i++)
		            {        
		                if (!mergedFields[i].equals("")) outOne.println(mergedFields[i] + "<br>");
		            }
		            if (!mergedFields[9].equals("")) outOne.println("<i>Synonyms: </i>" + mergedFields[9] + "<br>"); // synonyms-tibetan
    		        
		            for (i=10; i<=13; i++)
		            {
		                if (!mergedFields[i].equals("")) outOne.println(mergedFields[i] + "<br>");
		            }		        
		        }
		    case HTML:
		        outOne.flush(); // no break above so that both flush.
		    break;
		    
		    case SIMPLEBREAK:
    		    for (i=0; i<out.length; i++)
	    	        out[i].flush();
		}		
	}
}