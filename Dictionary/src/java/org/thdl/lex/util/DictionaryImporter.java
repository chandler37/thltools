package org.thdl.lex.util;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import org.thdl.tib.scanner.Manipulate;

public class DictionaryImporter {
	private static final String INSERT_META = "INSERT INTO Meta (createdby, modifiedby, createdbyprojsub, modifiedbyprojsub, createdon,"
			+ "modifiedon, source, language, dialect, script, note) VALUES ( ?, ? , ?, ?, NOW(), NOW(), 0, 0, 0, 1, ?)";

	private static final String SELECT_META = "SELECT metaid FROM Terms WHERE term = ?";

	private static final String INSERT_TERM = "INSERT INTO Terms (metaid, term) VALUES (?,?)";

	private static final String UPDATE_TRANS = "UPDATE TransitionalData SET TransitionalDataText = ? WHERE metaid = ?";

	private static final String INSERT_TRANS = "INSERT INTO TransitionalData (metaid, parentid, precedence, transitionaldatalabel, forpublicconsumption, "
			+ "transitionaldatatext) VALUES (?, ?, ?, ?, ?, ? )";

	private static PrintWriter out;

	private static BufferedReader in;

	private static String delimiter;

	private static int delimiterType;

	private static String note;

	private static int proj;

	private static String publicCons;

	private static int label;

	private static Statement sqlStatement;

	private static Connection connLex, connUsers;

	private static PreparedStatement insertMetaStmt, selectMetaStmt, insertTermStmt, updateTransStmt, insertTransStmt;

	public final static int delimiterGeneric = 0;

	public final static int delimiterAcip = 1;

	public final static int delimiterDash = 2;

	private static int counter = 0;

	//helpers
	public void doImport() throws Exception {
		String entrada, s1, s2, alternateWords[];
		int marker, marker2, len, currentLine = 1;
		//long start = System.currentTimeMillis();

		while ((entrada = in.readLine()) != null) {
			entrada = entrada.trim();
			if (!entrada.equals("")) {
				switch (delimiterType) {
				/*
				 * this is needed to make sure that the dash used in reverse
				 * vowels with extended wylie is not confused with the dash that
				 * separates definiendum and definition.
				 */
				case delimiterDash:
					marker = entrada.indexOf('-');
					len = entrada.length();
					while (marker >= 0
							&& marker < len - 1
							&& Manipulate.isVowel(entrada.charAt(marker + 1))
							&& !Character.isWhitespace(entrada
									.charAt(marker - 1))) {
						marker = entrada.indexOf('-', marker + 1);
					}
					break;
				default:
					marker = entrada.indexOf(delimiter);
				}
				if (marker < 0) {
					System.out.println("Error loading line " + currentLine);
					System.out.println(entrada);
				} else {
					s1 = Manipulate.deleteQuotes(entrada.substring(0, marker)
							.trim());
					s2 = Manipulate.deleteQuotes(entrada.substring(
							marker + delimiter.length()).trim());
					if (!s2.equals("")) {
						// check if there are multiple entries for a single
						// definition
						marker2 = s1.indexOf(';');
						if (marker2 > 0) {
							alternateWords = s1.split(";");
							for (marker2 = 0; marker2 < alternateWords.length; marker2++) {
								addRecord(alternateWords[marker2], s2);
							}
						}
						addRecord(s1, s2);
					}
				}
			}
			currentLine++;
		}

		//System.out.println( "Duration: " + ( System.currentTimeMillis() -
		// start )/60000 + " minutes");
		System.out.flush();
		if (out != null)
			out.flush();
		if (sqlStatement != null)
			sqlStatement.close();
		if (connLex != null)
			connLex.close();
		if (connUsers != null)
			connUsers.close();
	}

	public void addRecordManually(String term, String definition)
			throws Exception {
		Boolean result;
		ResultSet set;
		int metaID, metaIDTrans, prec;
		String currentDef;

		// These escapes should not be needed since we're using Prepared
		// Statements
		/*
		 * definition = Manipulate.replace(definition, "\\", "@@@@"); definition =
		 * Manipulate.replace(definition, "@@@@", "\\\\"); definition =
		 * Manipulate.replace(definition, "\"", "@@@@"); definition =
		 * Manipulate.replace(definition, "@@@@", "\\\"");
		 */

		// displaying for debugging purposes only
		// System.out.println(term);
		// Check to see if term is already there
		selectMetaStmt.setString(1, term);
		set = selectMetaStmt.executeQuery();

		// if it is get its metaID, else add it
		if (!set.first()) {
			insertMetaStmt.executeUpdate();
			set = sqlStatement.executeQuery("SELECT MAX(metaid) FROM Meta");
			set.first();
			metaID = set.getInt(1);

			insertTermStmt.setInt(1, metaID);
			insertTermStmt.setString(2, term);
			insertTermStmt.executeUpdate();
		} else
			metaID = set.getInt(1);

		// See if there is an associated TransitionalData with this term and
		// project
		set = sqlStatement
				.executeQuery("SELECT TransitionalDataText, TransitionalData.metaid FROM TransitionalData, Meta where TransitionalData.parentid = "
						+ metaID
						+ " and TransitionalData.metaid = Meta.metaid and createdbyprojsub = "
						+ proj);

		// if there is, append the definition if it is different. If not add it.
		if (set.first()) {
			currentDef = set.getString(1).trim();

			// These escapes should not be needed since we're using Prepared
			// Statements
			/*
			 * currentDef = Manipulate.replace(currentDef, "\\", "@@@@");
			 * currentDef = Manipulate.replace(currentDef, "@@@@", "\\\\");
			 * currentDef = Manipulate.replace(currentDef, "\"", "@@@@");
			 * currentDef = Manipulate.replace(currentDef, "@@@@", "\\\"");
			 */

			if (currentDef.indexOf(definition) < 0) {
				if (!currentDef.equals(""))
					definition = currentDef + ". " + definition;
				metaIDTrans = set.getInt(2);
				updateTransStmt.setString(1, definition);
				updateTransStmt.setInt(2, metaIDTrans);
				updateTransStmt.executeUpdate();
			}
		} else {
			insertMetaStmt.executeUpdate();
			set = sqlStatement.executeQuery("SELECT MAX(metaid) FROM Meta");
			set.first();
			metaIDTrans = set.getInt(1);
			set = sqlStatement
					.executeQuery("SELECT precedence FROM TransitionalData WHERE parentid = "
							+ metaID + " ORDER BY precedence DESC");
			if (set.first())
				prec = set.getInt(1) + 1;
			else
				prec = 0;

			insertTransStmt.setInt(1, metaIDTrans);
			insertTransStmt.setInt(2, metaID);
			insertTransStmt.setInt(3, prec);
			insertTransStmt.setInt(4, label);
			insertTransStmt.setString(5, publicCons);
			insertTransStmt.setString(6, definition);
			insertTransStmt.executeUpdate();
		}
	}

	private void addRecord(String term, String definition) throws Exception {
		if (counter++ % 1000 == 0) {
			System.out.println("Adding term " + counter + " " + term);
		}
		term = Manipulate.replace(term, "  ", " ");
		if (out != null)
			out.println(term + " - " + definition);
		else
			addRecordManually(term, definition);

	}

	/**
	 * Used only if the database is being accessed manually instead of through
	 * Hibernate
	 */
	private static void initConnections() {
		ResourceBundle rb = ResourceBundle.getBundle("dictionary-importer");

		// Loading driver
		try {
			Class.forName(rb.getString("dictionaryimporter.driverclassname"))
					.newInstance();
		} catch (Exception ex) {
			System.out.println("Mysql driver couldn't be loaded!");
			System.exit(0);
		}

		// Connecting to database
		try {
			connLex = DriverManager.getConnection(rb
					.getString("dictionaryimporter.lex.url"));
			connUsers = DriverManager.getConnection(rb
					.getString("dictionaryimporter.thdlusers.url"));
		} catch (Exception ex) {
			// handle any errors
			System.out.println("Could not connect to database!");
			ex.printStackTrace();
			System.exit(0);
		}
	}

	private static void initStatements() {
		try {
			sqlStatement = connLex.createStatement();
			insertMetaStmt = connLex.prepareStatement(INSERT_META);
			selectMetaStmt = connLex.prepareStatement(SELECT_META);
			insertTermStmt = connLex.prepareStatement(INSERT_TERM);
			updateTransStmt = connLex.prepareStatement(UPDATE_TRANS);
			insertTransStmt = connLex.prepareStatement(INSERT_TRANS);
		} catch (Exception ex) {
			// handle any errors
			System.out.println("Could not create statement!");
			ex.printStackTrace();
			System.exit(0);
		}
	}

	public DictionaryImporter() {
	}
	
	public static int getCreator(String firstName, String lastName) throws Exception
	{
		PreparedStatement stmt, stmt2;
		ResultSet set;
		int creator = -1;
		
		stmt = connUsers.prepareStatement("SELECT id FROM thdlusers WHERE firstname LIKE ? AND lastname LIKE ?");
		stmt.setString(1, "%" + firstName + "%");
		stmt.setString(2, "%" + lastName + "%");
		set = stmt.executeQuery();
		
		// check if there is at least one
		if (set.next())
		{
			creator = set.getInt(1);
			
			// if there are more instances of the same user delete them
			if (set.next())
			{
				stmt = connUsers.prepareStatement("DELETE FROM thdlusers WHERE firstname LIKE ? AND lastname LIKE ? AND id != ?");
				stmt.setString(1, "%" + firstName + "%");
				stmt.setString(2, "%" + lastName + "%");
				stmt.setInt(3, creator);
				stmt.executeUpdate();
			}
		}
		else
		{
			stmt2 = connUsers.prepareStatement("INSERT INTO thdlusers(firstname, lastname) VALUES (?, ?)");
			stmt2.setString(1, firstName);
			stmt2.setString(2, lastName);
			stmt2.executeUpdate();
			set = stmt.executeQuery();
			
			if (set.next())
			{
				creator = set.getInt(1);
			}

		}
		
		return creator;
	}
	
	public static int getProject(String projectName, String shortName, int creator) throws Exception
	{
		PreparedStatement stmt, stmt2;
		ResultSet set;
		int projectFirst = -1, project = -1;
		
		stmt = connLex.prepareStatement("SELECT id FROM projectsubjects WHERE projectSubject like ? OR projectSubject like ?");
		stmt.setString(1, "%" + projectName + "%");
		stmt.setString(2, "%" + shortName + "%");
		set = stmt.executeQuery();
		stmt2 = connLex.prepareStatement("DELETE FROM transitionaldata, meta USING transitionaldata, meta WHERE transitionaldata.metaid=meta.metaid AND createdbyprojsub = ?");
		
		while (set.next())
		{
			project = set.getInt(1);
			if (projectFirst==-1) projectFirst = project;
			stmt2.setInt(1, project);
			stmt2.executeUpdate();
		}
		
		if (projectFirst==-1)
		{
			stmt2 = connLex.prepareStatement("INSERT INTO ProjectSubjects(projectSubject, leader, participantList) VALUES (?, ?, ?)");
			stmt2.setString(1, projectName);
			stmt2.setInt(2, creator);
			stmt2.setString(3, "");
			stmt2.executeUpdate();
			
			set = stmt.executeQuery();
			if (set.next())
			{
				projectFirst = set.getInt(1);
			}
		}
		/* if there is more than one project under that name
		 * delete the others
		 * */
		else
		{
			stmt2 = connLex.prepareStatement("UPDATE projectsubjects SET projectSubject = ?, leader = ?, participantList = ? WHERE id = ?");
			stmt2.setString(1, projectName);
			stmt2.setInt(2, creator);
			stmt2.setString(3, "");
			stmt2.setInt(4, projectFirst);
			stmt2.executeUpdate();
			
			if (projectFirst!=project)
			{
				stmt2 = connLex.prepareStatement("DELETE FROM projectsubjects WHERE id != ? AND (projectSubject like ? OR projectSubject like ?)");
				stmt2.setInt(1, projectFirst);
				stmt2.setString(2, "%" + projectName + "%");
				stmt2.setString(3, "%" + shortName + "%");
				stmt2.executeUpdate();
			}
		}
		
		return projectFirst;
	}
	
	public static int getLabel(String longName, String shortName, int priority) throws Exception
	{
		PreparedStatement stmt, stmt2;
		ResultSet set;
		int label=-1;
		
		stmt = connLex.prepareStatement("SELECT id FROM transitionaldatalabels WHERE transitionaldatalabel like ? OR transitionaldatalabel like ?");
		stmt.setString(1, "%" + shortName + "%");
		stmt.setString(2, "%" + longName + "%");
		set = stmt.executeQuery();
		
		if (set.next())
		{
			label = set.getInt(1);
			if (set.next())
			{
				stmt2 = connLex.prepareStatement("DELETE FROM transitionaldatalabels WHERE id != ? AND (transitionaldatalabel like ? OR transitionaldatalabel like ?)");
				stmt2.setInt(1, label);
				stmt2.setString(2, "%" + shortName + "%");
				stmt2.setString(3, "%" + longName + "%");
				stmt2.executeUpdate();
			}
			stmt2 = connLex.prepareStatement("UPDATE transitionaldatalabels SET priority = ?, transitionaldatalabelshort = ?, transitionaldatalabel = ? WHERE id = ?");
			stmt2.setInt(1, priority);
			stmt2.setString(2, shortName);
			stmt2.setString(3, longName);
			stmt2.setInt(4, label);
			stmt2.executeUpdate();
		}
		else
		{
			stmt2 = connLex.prepareStatement("INSERT INTO transitionaldatalabels(priority, transitionaldatalabel, transitionaldatalabelshort) VALUES (?, ?, ?)");
			stmt2.setInt(1, priority);
			stmt2.setString(2, longName);
			stmt2.setString(3, shortName);
			stmt2.executeUpdate();
			
			set = stmt.executeQuery();
			
			if (set.next())
			{
				label = set.getInt(1);
			}
		}
		
		return label;
	}

	public static void main(String[] args)
	{
		String format = null, firstName = "", lastName = "", longName = "", shortName = "";
		InputStream is;

		int argNum = args.length, currentArg = 0, creator, priority = -1;
		String option;
		boolean file = false;
		out = null;

		delimiterType = delimiterDash;
		delimiter = "-";
		proj = -1;
		note = "";
		publicCons = "true";
		label = -1;
		sqlStatement = null;
		connLex = null;

		if (argNum <= currentArg) {
			System.out
					.println("Syntax: DictionaryImporter [-format format] [-tab | -delim delimiter] "
							+ "-creatorFirstName first-name -creatorLastName last-name [-proj project-name] [-note note] "
							+ "[-pub-cons public-consumption-marker] [input-file] [output-error-file]");
			return;
		}

		while (args[currentArg].charAt(0) == '-') {
			option = args[currentArg].substring(1);
			currentArg++;
			if (option.equals("format")) {
				if (argNum <= currentArg) {
					System.out.println("Syntax error: format expected.");
					return;
				}
				format = args[currentArg];
				currentArg++;
			} else if (option.equals("tab")) {
				delimiter = "\t";
				delimiterType = delimiterGeneric;
			} else if (option.equals("delim")) {
				if (argNum <= currentArg) {
					System.out.println("Syntax error: delimiter expected.");
					return;
				}
				delimiter = args[currentArg];
				currentArg++;
			} else if (option.equals("creatorFirstName")) {
				if (argNum <= currentArg) {
					System.out.println("Syntax error: creator\'s first name expected.");
					return;
				}
				firstName = args[currentArg];
				currentArg++;
			} else if (option.equals("creatorLastName")) {
				if (argNum <= currentArg) {
					System.out.println("Syntax error: creator\'s last name expected.");
					return;
				}
				lastName = args[currentArg];
				currentArg++;
			} else if (option.equals("longName")) {
				if (argNum <= currentArg) {
					System.out.println("Syntax error: project\'s full name expected.");
					return;
				}
				longName = args[currentArg];
				currentArg++;
			} else if (option.equals("shortName")) {
				if (argNum <= currentArg) {
					System.out.println("Syntax error: project\'s abbreviated name expected.");
					return;
				}
				shortName = args[currentArg];
				currentArg++;
			} else if (option.equals("priority")) {
				if (argNum <= currentArg) {
					System.out.println("Syntax error: project\'s priority expected.");
					return;
				}
				priority = Integer.parseInt(args[currentArg]);
				currentArg++;
			} else if (option.equals("note")) {
				if (argNum <= currentArg) {
					System.out.println("Syntax error: note expected.");
					return;
				}
				note = args[currentArg];
				currentArg++;
			} else if (option.equals("pub-cons")) {
				if (argNum <= currentArg) {
					System.out
							.println("Syntax error: public consumption description expected.");
					return;
				}
				publicCons = args[currentArg];
				currentArg++;
			}
		}

		switch (args.length - currentArg) {
		case 0:
			out = new PrintWriter(System.out);
			if (format != null) {
				System.out.println("Syntax error. Input file expected.");
				return;
			}
			break;
		case 1:
			file = true;
			break;
		default:
			if (format != null)
			{
				try
				{
					out = new PrintWriter(new OutputStreamWriter(
						new FileOutputStream(args[currentArg + 1]), format));
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				try
				{
					out = new PrintWriter(
						new FileOutputStream(args[currentArg + 1]));
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
			}
			file = true;
		}

		if (file)
		{
			try
			{
				if (args[currentArg].indexOf("http://") >= 0)
					is = new BufferedInputStream((new URL(args[currentArg]))
							.openStream());
				else
					is = new FileInputStream(args[currentArg]);
	
				if (format == null)
					in = new BufferedReader(new InputStreamReader(is));
				else
					in = new BufferedReader(new InputStreamReader(is, format));
			}
			catch (Exception e)
			{
				System.out.println("File " + args[currentArg] + " not found.");
				e.printStackTrace();
			}
		}

		initConnections();
		initStatements();
		
		try
		{
			creator = getCreator(firstName, lastName);
			proj = getProject(longName, shortName, creator);
			label = getLabel (longName, shortName, priority);
			
			insertMetaStmt.setInt(1, creator);
			insertMetaStmt.setInt(2, creator);
			insertMetaStmt.setInt(3, proj);
			insertMetaStmt.setInt(4, proj);
			insertMetaStmt.setString(5, note);
	
			new DictionaryImporter().doImport();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}