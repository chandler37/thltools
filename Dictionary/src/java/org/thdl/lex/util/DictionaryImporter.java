package org.thdl.lex.util;

import java.net.*;
import java.io.*;
import java.util.*;
import org.thdl.lex.*;
import org.thdl.lex.component.*;
import org.thdl.tib.scanner.Manipulate;
import java.sql.*;

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
    private static Statement sqlStatement;
    private static Connection conn;
    
    public final static int delimiterGeneric=0;
    public final static int delimiterAcip=1;
    public final static int delimiterDash=2;

//helpers	
	public void doImport() throws Exception
	{
	    String entrada, s1, s2, alternateWords[];
	    int marker, marker2, len, currentLine=1;
	    //long start = System.currentTimeMillis();
	    
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
	
		//System.out.println( "Duration: " + ( System.currentTimeMillis() - start )/60000 + " minutes");
		System.out.flush();
		if (out!=null) out.flush();
		if (sqlStatement!=null) sqlStatement.close();
		if (conn!=null) conn.close();
	}
	
	public void addRecordManually(String term, String definition) throws Exception
	{
	    Boolean result;
	    ResultSet set;
	    int metaID, metaIDTrans, prec;
	    String currentDef, insertMeta = "INSERT INTO meta (createdby, modifiedby, createdbyprojsub, modifiedbyprojsub, createdon, modifiedon, source, language, dialect, script, note) VALUES (" + creator.toString() + ", " + creator.toString() + ", " + proj.toString() + ", " + proj.toString() + ", NOW(), NOW(), 0, 0, 0, 1, \"" + note + "\")";

	    definition = Manipulate.replace(definition, "\\", "@@@@");
	    definition = Manipulate.replace(definition, "@@@@", "\\\\");
	    
	    definition = Manipulate.replace(definition, "\"", "@@@@");
	    definition = Manipulate.replace(definition, "@@@@", "\\\"");

        // displaying for debugging purposes only
        // System.out.println(term);
        
	    // Check to see if term is already there
	    sqlStatement.execute("SELECT metaid FROM terms WHERE term = \"" + term + "\"");
	    set = sqlStatement.getResultSet();
	    
	    // if it is get its metaID, else add it
	    if (!set.first())
	    {
	        sqlStatement.execute(insertMeta);
	        sqlStatement.execute("SELECT MAX(metaid) FROM META");
	        set = sqlStatement.getResultSet();
	        set.first();
	        metaID = set.getInt(1);
	        sqlStatement.execute("INSERT INTO terms (metaid, term) VALUES (" + metaID + ", \"" + term + "\")");
	    }
	    else metaID = set.getInt(1);
	    
	    // See if there is an associated TransitionalData with this term and project
	    sqlStatement.execute("SELECT transitionaldatatext, transitionaldata.metaid FROM transitionaldata, meta where transitionaldata.parentid = " + metaID + " and transitionaldata.metaid = meta.metaid and createdbyprojsub = " + proj.toString());
	    set = sqlStatement.getResultSet();
	    
	    // if there is, append the definition if it is different. If not add it.
	    if (set.first())
	    {
	        currentDef = set.getString(1).trim();

    	    currentDef = Manipulate.replace(currentDef, "\\", "@@@@");
	        currentDef = Manipulate.replace(currentDef, "@@@@", "\\\\");
    	    
    	    currentDef = Manipulate.replace(currentDef, "\"", "@@@@");
	        currentDef = Manipulate.replace(currentDef, "@@@@", "\\\"");
	        
	        if (currentDef.indexOf(definition)<0)
	        {
	            if (!currentDef.equals("")) definition = currentDef + ". " + definition;
	            metaIDTrans = set.getInt(2);
   	            sqlStatement.execute("UPDATE transitionaldata SET transitionaldatatext = \"" + definition + "\" WHERE metaid = " + metaIDTrans);
	        }
	    }
	    else
	    {
	        sqlStatement.execute(insertMeta);
	        sqlStatement.execute("SELECT MAX(metaid) FROM META");
	        set = sqlStatement.getResultSet();
	        set.first();
	        metaIDTrans = set.getInt(1);
	        sqlStatement.execute("SELECT precedence FROM transitionaldata WHERE parentid = " + metaID + " ORDER BY precedence DESC");
	        set = sqlStatement.getResultSet();
	        if (set.first()) prec = set.getInt(1)+1;
	        else prec = 0;
	        sqlStatement.execute("INSERT INTO transitionaldata (metaid, parentid, precedence, transitionaldatalabel, forpublicconsumption, transitionaldatatext) VALUES ("+ metaIDTrans +", " + metaID +", " + prec + ", " + label + ", \"" + publicCons + "\", \"" + definition + "\")");
	    }
	}
	
	private void addRecord(String term, String definition) throws Exception
	{
	    term = Manipulate.replace(term, "  ", " ");
	    if (out!=null) out.println(term + " - " + definition);
	    else if (sqlStatement!=null) addRecordManually(term, definition);
        else addRecordViaHibernate(term, definition);
	    
	}
	
	/** Main class to map the term and its definition to the Lex Component 
	   object model. Works but painfully slow! */
	public void addRecordViaHibernate(String term, String definition) throws Exception
	{
	    LinkedList ll;
	    ListIterator li;
	    ITerm lexTerm;
	    TransitionalData trans=null;
	    boolean found, newTerm = false;
	    String existingDef;
	    
	    // displaying for debugging purposes only
	    // System.out.println(term);
	    
		//check to see if the term already exists
	    lexTerm = LexComponentRepository.loadTermByTerm( term );
	    
	    found = false;
		
		//if it doesn't create a new term. 
		if ( null == lexTerm )
		{
		    // System.out.println("New term");
			lexTerm = new Term();
			lexTerm.setTerm( term );
			lexTerm.setMeta( defaultMeta() );
			//save the Term to the database. This step is necessary here to generate a unique id
			LexComponentRepository.save( lexTerm );
			newTerm = true;
		}
		else
		{
		    // System.out.println("Old term");
		    li = lexTerm.getTransitionalData().listIterator();
    		
		    while (li.hasNext())
		    {
		        trans = (TransitionalData) li.next();
		        if (trans.getMeta().getCreatedByProjSub().equals(proj)) 
		        {
		            found = true;
		            break;
		        }
		    }
		    newTerm = false;
		}
				
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
		
		if (found)
		{
		    existingDef = trans.getTransitionalDataText().trim(); 
		    if (existingDef.equals("")) found = false;
		    else if (existingDef.indexOf(definition)<0)
		    {
		        definition = existingDef + ". " + definition;
		        found = false;
		    }
		}
		else
		{
		    // System.out.println("New definition");
		    trans = new TransitionalData( );
		    trans.setMeta( defaultMeta() );
		    trans.setTransitionalDataLabel(label);
		    trans.setParentId( lexTerm.getMetaId() );
		    trans.setForPublicConsumption(publicCons);
		    if (newTerm)
		    {
		        ll = new LinkedList();
		        ll.add(trans);
		        lexTerm.setTransitionalData(ll);
		    }
    		else lexTerm.getTransitionalData().add( trans );
    		
		}
		
		if (!found)
		{
	        trans.setTransitionalDataText(definition);		
		    //save the Term to the database.
		    LexComponentRepository.save( lexTerm );
		}
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
	
	/** Used only if the database is being accessed manually instead of through Hibernate */
	private static Statement getStatement()
	{
		ResourceBundle rb = ResourceBundle.getBundle("dictionary-importer");
		Statement s = null;
		
		// Loading driver
		try { 
            // The newInstance() call is a work around for some 
            // broken Java implementations

            Class.forName(rb.getString("dictionaryimporter.driverclassname")).newInstance(); 
        } catch (Exception ex) { 
            System.out.println("Mysql driver couldn't be loaded!");
            System.exit(0);
        }

	    // Connecting to database
        try {
            conn = DriverManager.getConnection(rb.getString("dictionaryimporter.url"));
            s = conn.createStatement();
          
            // Do something with the Connection 
          
        } catch (Exception ex) {
            // handle any errors 
            System.out.println("Could not connect to database!"); 
            System.exit(0);
        }
        
        return s;
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
		sqlStatement = null;
		conn = null;
		
		if (argNum<=currentArg)
		{
		    System.out.println("Syntax: DictionaryImporter [-manual] [-format format] [-tab | -delim delimiter] " +
		                       "[-creator creator-id] [-proj project-id] [-label label] [-note note] " +
		                       "[-pub-cons public-consumption-marker] [input-file] [output-error-file]");
		    return;
		}
		
		while (args[currentArg].charAt(0)=='-')
		{
		    option = args[currentArg].substring(1);		        
		    currentArg++;
		    if (option.equals("manual"))
		    {
		        sqlStatement = getStatement();
		    } else if (option.equals("format"))
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
