package org.thdl.lex;
import java.net.*;
import java.io.*;
import java.util.*;
import org.thdl.lex.*;
import org.thdl.lex.component.*;
import org.thdl.tib.scanner.Manipulate;

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
	    
		//check to see if the term already exists
	    ITerm lexTerm = LexComponentRepository.loadTermByTerm( term );
		
		//if it doesn't create a new term. 
		if ( null == lexTerm )
		{
			lexTerm = new Term();
			lexTerm.setTerm( term );
			lexTerm.setMeta( defaultMeta() );
			//save the Term to the database. This step is necessary here to generate a unique id
			LexComponentRepository.save( lexTerm );

		}
		
	    //Andres, each term has a List object that holds all TransData components. 
		// If you need to get a specific one, you'll need to iterate through the list and find it by means of criteria in the Meta object..
		//	The RY dictionary had multiple entries for a single term which this dictionary does not.
		//so I sometimes had to append transitional data text to an already created Trans Data object.
		//If you don't need to do this, you can simply add a new TransitionalData component to each term and you should be fine.
		
		//OLD CODE
		/* TransitionalData trans = getTransData( lexTerm.getId() );
		
		trans.setTransitionalDataLabel( new Short( "1" ) );
		trans.setForPublicConsumption( "true" );
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
		out.print( trans.getTransitionalDataText().substring( 0, subTarget ) ); */

		//NEW CODE
		// create a new transData object to add the term if we're not using one that's already there.
		TransitionalData trans = new TransitionalData( );
		trans.setMeta( defaultMeta );
		trans.setParentId( lexTerm.getMetaId() );
		
		//ADD THE REST OF YOUR DATA HERE
		
		//add the new trans Data obj to the term.
		lexTerm.getTransitionalData().add( trans );
		
		//save the Term to the database.
		LexComponentRepository.save( lexTerm );
	}
	
	public Meta defaultMeta() {
		//use the file src/sql/import-updates.sql to add new metadata for use in this method.
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
