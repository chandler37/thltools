package org.thdl.lex.util;

import java.net.*;
import java.io.*;
import java.util.*;
import org.thdl.lex.*;
import org.thdl.lex.component.*;
import org.thdl.tib.scanner.Manipulate;

public class DictionaryImporter
{
    private static PrintWriter out;
    private static BufferedReader in;
    private static String delimiter;
    private static int delimiterType;
    private static Integer creator;
    private static String note;
    private static Integer proj;
    private static String publicCons;
    private static Integer label;
    
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
	        		marker = entrada.indexOf(delimiter);
	        	}
		        if (marker<0)
		        {
		            System.out.println("Error loading line " + currentLine);
		            System.out.println(entrada);
        		}
	        	else
		        {
		            s1 = Manipulate.deleteQuotes(entrada.substring(0,marker).trim());
		            s2 = Manipulate.deleteQuotes(entrada.substring(marker+delimiter.length()).trim());
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
		                addRecord (s1, s2);
		            }
    		    }
    		}
		    currentLine++;
	    }			
	
		System.out.println( "Duration: " + ( System.currentTimeMillis() - start )/60000 + " minutes");
		System.out.flush();
		out.flush();
	}
	
	/** Main class to map the term and its definition to the Lex Component 
	   object model. Doesn't work yet! */
	public void addRecord(String term, String definition) throws Exception
	{
	    ListIterator li;
	    ITerm lexTerm;
	    TransitionalData trans=null;
	    boolean found;
	    
	    // displaying for debugging purposes only
	    if (out!=null)
	    {
	        out.println(term + " - " + definition);
	        return;
	    }
	    
		//check to see if the term already exists
	    lexTerm = LexComponentRepository.loadTermByTerm( term );
		
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
		
		// check if there is already a defition for this project
		
		li = lexTerm.getTransitionalData().listIterator();
		
		found = false;
		while (li.hasNext())
		{
		    trans = (TransitionalData) li.next();
		    if (trans.getMeta().getCreatedByProjSub().equals(proj)) 
		    {
		        found = true;
		        break;
		    }
		}
		if (found)
		{
		    definition = trans.getTransitionalDataText() + ". " + definition;
		}
		else
		{
		    trans = new TransitionalData( );
		    trans.setMeta( defaultMeta() );
		    trans.setTransitionalDataLabel(label);
		    trans.setParentId( lexTerm.getMetaId() );
		    trans.setForPublicConsumption(publicCons);
    		lexTerm.getTransitionalData().add( trans );
		}

	    trans.setTransitionalDataText(definition);
		
		//add the new trans Data obj to the term.
		
		//save the Term to the database.
		LexComponentRepository.save( lexTerm );
	}
	
	public Meta defaultMeta() {
		//use the file src/sql/import-updates.sql to add new metadata for use in this method.
		Meta meta = new Meta();
		meta.setCreatedBy(creator);
		meta.setModifiedBy(creator);
		meta.setCreatedByProjSub(proj);
		meta.setModifiedByProjSub(proj);
		meta.setSource( new Integer( 0 ) );
		// meta.setTranslationOf( new Integer( 0 ) );
		meta.setLanguage( new Integer( "0" ) );
		meta.setDialect( new Integer( "0" ) );
		meta.setScript( new Integer( "1" ) );
		meta.setNote(note);
		return meta;
	}
	
	public DictionaryImporter()
	{
	}
	
	public static void main( String[] args ) throws Exception
	{
		String format = null;
	    InputStream is;
		
		int argNum = args.length, currentArg=0;
		String option;
		boolean file=false;

		out = null;
		
		delimiterType = delimiterDash;
		delimiter="-";
		creator= new Integer(80);
		proj = new Integer(18);
		note = "This entry comes from The Rangjung Yeshe Tibetan-English Dictionary of Buddhist Culture (www.rangjung.com).";
		publicCons = "true";
		label = new Integer(6);
		
		if (argNum<=currentArg)
		{
		    System.out.println("Syntax: DictionaryImporter [-format format] [-tab | -delim delimiter] " +
		                       "[-creator creator-id] [-proj project-id] [-label label] [-note note] " +
		                       "[-pub-cons public-consumption-marker] [input-file] [output-error-file]");
		    return;
		}
		
		while (args[currentArg].charAt(0)=='-')
		{
		    option = args[currentArg].substring(1);		        
		    currentArg++;
		    if (option.equals("format"))
		    {
		        if (argNum<=currentArg)
		        {
		            System.out.println("Syntax error: format expected.");
		            return;
		        }
		        format = args[currentArg];
		        currentArg++;
		    } else if (option.equals("tab"))
		    {
		        delimiter = "\t";
		        delimiterType = delimiterGeneric;
		    } else if (option.equals("delim"))
		    {
		        if (argNum<=currentArg)
		        {
		            System.out.println("Syntax error: delimiter expected.");
		            return;
		        }
		        delimiter = args[currentArg];
		        currentArg++;		        
		    } else if (option.equals("creator"))
		    {
		        if (argNum<=currentArg)
		        {
		            System.out.println("Syntax error: creator expected.");
		            return;
		        }
		        creator = new Integer(args[currentArg]);
		        currentArg++;		        
		    } else if (option.equals("proj"))
		    {
		        if (argNum<=currentArg)
		        {
		            System.out.println("Syntax error: project expected.");
		            return;
		        }
		        proj = new Integer(args[currentArg]);
		        currentArg++;		        
		    } else if (option.equals("label"))
		    {
		        if (argNum<=currentArg)
		        {
		            System.out.println("Syntax error: label expected.");
		            return;
		        }
		        label = new Integer(args[currentArg]);
		        currentArg++;		        
		    } else if (option.equals("note"))
		    {
		        if (argNum<=currentArg)
		        {
		            System.out.println("Syntax error: note expected.");
		            return;
		        }
		        note = args[currentArg];
		        currentArg++;		        
		    } else if (option.equals("pub-cons"))
		    {
		        if (argNum<=currentArg)
		        {
		            System.out.println("Syntax error: public consumption description expected.");
		            return;
		        }
		        publicCons = args[currentArg];
		        currentArg++;		        
		    }
		}

		switch (args.length-currentArg)
		{
		case 0: out = new PrintWriter(System.out);
		        if (format != null)
		        {
		            System.out.println("Syntax error. Input file expected.");
		            return;
		        }
				break;
		case 1: file = true;
				break;
		default:
		        if (format != null) out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[currentArg + 1]), format));
		        else out = new PrintWriter(new FileOutputStream(args[currentArg + 1]));
    			file = true;		
		}
		
   		if (file)
		{
    	    if (args[currentArg].indexOf("http://") >= 0) 
			    is = new BufferedInputStream((new URL(args[currentArg])).openStream());
		    else
		        is = new FileInputStream(args[currentArg]);
    		    
		    if (format==null)
		        in = new BufferedReader(new InputStreamReader(is));
		    else
		        in = new BufferedReader(new InputStreamReader(is, format));		    
		}

		new DictionaryImporter().doImport();
	}
}
