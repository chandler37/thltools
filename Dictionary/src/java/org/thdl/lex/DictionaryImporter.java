package org.thdl.lex;
import java.net.*;
import java.io.*;
import java.util.*;
import org.thdl.lex.*;
import org.thdl.tib.scanner.Manipulate;

//import org.apache.torque.Torque;
//import org.apache.torque.util.Criteria;
import org.thdl.lex.component.*;

public class DictionaryImporter
{
    private PrintWriter out;
    private BufferedReader in;
    private String delim;
    private int delimiterType;
    
    public final static int delimiterGeneric=0;
    public final static int delimiterAcip=1;
    public final static int delimiterDash=2;

//helpers	
	public void doImport() throws Exception
	{
	    String entrada, s1, s2, alternateWords[];
	    int marker, marker2, len, currentLine=1;
	    long start = System.currentTimeMillis();
	    
        while ((entrada = in.readLine())!=null)
	    {
		    entrada = entrada.trim();
			if (!entrada.equals(""))
        	{
        		switch(delimiterType)
        		{
        			/* this is needed to make sure that the dash used in reverse vowels with extended
        			wylie is not confused with the dash that separates definiendum and definition. */
        			case delimiterDash:
        			    marker=entrada.indexOf('-');
        			    len = entrada.length(); 
        			    while (marker>=0 && marker<len-1 && Manipulate.isVowel(entrada.charAt(marker+1)) && !Character.isWhitespace(entrada.charAt(marker-1)))
        			    {
        			        marker = entrada.indexOf('-', marker+1);
        			    }
        			break;
        			default:
	        		marker = entrada.indexOf(delim);
	        	}
		        if (marker<0)
		        {
		            out.println("Error loading line " + currentLine);
		            out.println(entrada);
        		}
	        	else
		        {
		            s1 = Manipulate.deleteQuotes(entrada.substring(0,marker).trim());
		            s2 = Manipulate.deleteQuotes(entrada.substring(marker+delim.length()).trim());
		            if (!s2.equals(""))
		            {
		                // check if there are multiple entries for a single definition
		                marker2 = s1.indexOf(';');
            		    if (marker2>0)
            		    {
		                    alternateWords = s1.split(";");
    		                for (marker2=0; marker2<alternateWords.length; marker2++)
	    	                {
	    	                    addRecord(alternateWords[marker2],s2);
		                    }
		                }
		                out.println(currentLine);
		                addRecord (s1, s2);
		            }
    		    }
    		}
		    currentLine++;
	    }			
	
		out.println( "Duration: " + ( System.currentTimeMillis() - start )/60000 + " minutes");
		out.flush();
	}
	
	/** Main class to map the term and its definition to the Lex Component 
	   object model. Doesn't work yet! */
	public void addRecord(String term, String definition)
	{
	    // displaying for debugging purposes only
	    out.println(term + " - " + definition);
	    
	    /* Previous code starts here
	    
	    Term termParent = getTermParent(term);
		TransitionalData trans = getTransData( termParent.getId() );
		
		trans.setTransitionalDataLabel( new Short( "1" ) );
		trans.setForPublicConsumption( "false" );
		trans.setTermId( termParent.getId() );
				
		String newText = null;
		if (null != trans.getTransitionalDataText() )
			newText = trans.getTransitionalDataText() + ". " + def.toString().trim();
		else
			newText = def.toString().trim();
		trans.setTransitionalDataText( newText );
		trans.save();
				
		out.print( termParent.getTerm() + " ");
		
		int subTarget = 30;
		if ( trans.getTransitionalDataText().length() < 40 )
			subTarget = trans.getTransitionalDataText().length();
		out.print( trans.getTransitionalDataText().substring( 0, subTarget ) );
		*/
	}
	
	/*

	// doesn't work yet
	public Term getTermParent( String term )
	{	
		try {
			
			Criteria crit = new Criteria();
			crit.add(TermPeer.TERM, term);
			List v = TermPeer.doSelect(crit);
			Iterator i = v.iterator();
			Term terms;
			if ( v.size() == 1 )
			{
				terms = (Term) i.next();
				return terms;
			}
			else
			{
				Meta meta = defaultMeta();
				meta.save();
				terms = new Term();
				terms.setTerm( term );
				terms.setMetaId( meta.getMetaId() );
				terms.setPrecedence( new Short( "1" ) );
				terms.save();
				return terms;
			}
		}
		catch ( Exception e ) 
		{
			e.printStackTrace();
			return null;
		}			
	}	
	
	//Doesn't work yet.
 	public TransitionalData getTransData( Integer termId )
	{
		try {
			Criteria crit = new Criteria();
			crit.add(TransitionalDataPeer.TERMID, termId);
			List v = TransitionalDataPeer.doSelect(crit);
			Iterator i = v.iterator();
			TransitionalData transData;
			if ( v.size() == 1 )
			{
				transData = (TransitionalData) i.next();
				return transData;
			}
			else
			{
				Meta meta = defaultMeta();
				meta.save();
				TransitionalData trans = new TransitionalData();
				trans.setTransitionalDataLabel( new Short( "1" ) );
				trans.setForPublicConsumption( "false" );
				trans.setMeta (meta);
				return trans;
			}		
		}
		catch ( Exception e ) 
		{
			e.printStackTrace();
			return null;
		}			
	}*/
	
	public Meta defaultMeta() {
		Meta meta = new Meta();
		meta.setCreatedBy( new Integer( 4 ) );
		meta.setModifiedBy( new Integer( 4 ) );
		meta.setCreatedByProjSub( new Integer( 16 ) );
		meta.setModifiedByProjSub( new Integer( 16 ) );
		meta.setSource( new Integer( 0 ) );
		// meta.setTranslationOf( new Integer( 0 ) );
		meta.setLanguage( new Integer( "0" ) );
		meta.setDialect( new Integer( "0" ) );
		meta.setScript( new Integer( "1" ) );
		meta.setNote( "This entry comes from The Rangjung Yeshe Tibetan-English Dictionary of Buddhist Culture (www.rangjung.com)." );
		return meta;
	}
	
	public DictionaryImporter(BufferedReader in, PrintWriter out)
	{
	    this(in, out, delimiterDash);
	}
	
	public DictionaryImporter(BufferedReader in, PrintWriter out, int delimiterType)
	{
	    /* If the delimiter type is acip the dash will be ignored anyway, the delimiter
	    will be ignored anyway. */
	    this (in, out, delimiterType, "-");
	}
	
	public DictionaryImporter(BufferedReader in, PrintWriter out, String delim)
	{
	    this (in, out, delimiterGeneric, delim);
	}
	
	public DictionaryImporter(BufferedReader in, PrintWriter out, int delimiterType, String delim)
	{
	    this.delim = delim;
	    this.in = in;
	    this.out = out;
	    this.delim = delim;
	    
		/* try 
		{
			Torque.init( "./lex-torque.properties" );		
		}
		catch ( Exception e ) 
		{
			e.printStackTrace();
		}*/

	}
	
	public static void main( String[] args )
	{
		BufferedReader in=null;
		PrintWriter out;
		
		int argNum = args.length, currentArg=0;
		String option;
		boolean file=false;
		
		if (argNum<=currentArg)
		{
		    System.out.println("Syntax: DictionaryImporter [input-file] [output-file]");
		    return;
		}
		/*
		while (args[currentArg].charAt(0)=='-')
		{
		    option = args[currentArg].substring(1);		        
		    currentArg++;
		    if (option.equals("xxx"))
		    {
		        if (argNum<=currentArg)
		        {
		        }
		        // use it if needed: args[currentArg];
		        currentArg++;
		    }
		}*/

		switch (args.length-currentArg)
		{
		case 0: out = new PrintWriter(System.out);
				in = new BufferedReader(new InputStreamReader(System.in));
				break;
		case 1: out = new PrintWriter(System.out);
				file = true;
				break;
		default: 
		    try 
		    {
		        out = new PrintWriter(new FileOutputStream(args[currentArg + 1]));
		    }
		    catch ( Exception e )
		    {
			    e.printStackTrace();
			    return;
		    }
		
			file = true;		
		}
			    
		try
		{
   		    if (file)
		    {
			    if (args[currentArg].indexOf("http://") >= 0) 
				    in = new BufferedReader(new InputStreamReader(new BufferedInputStream((new URL(args[currentArg])).openStream())));
			    else 
				    in = new BufferedReader(new InputStreamReader(new FileInputStream(args[currentArg])));
		    }

			new DictionaryImporter(in, out).doImport();
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}
}
