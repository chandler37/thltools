package org.thdl.lex.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.ResourceBundle;

import org.thdl.lex.LexComponentRepository;
import org.thdl.lex.component.ITerm;
import org.thdl.lex.component.Meta;
import org.thdl.lex.component.Term;
import org.thdl.lex.component.TransitionalData;
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

	private static Integer creator;

	private static String note;

	private static Integer proj;

	private static String publicCons;

	private static Integer label;

	private static Statement sqlStatement;

	private static Connection conn;

	private static Connection conn2;

	private static Connection conn3;

	private static Connection conn4;

	private static Connection conn5;

	private static PreparedStatement insertMetaStmt;

	private static PreparedStatement selectMetaStmt;

	private static PreparedStatement insertTermStmt;

	private static PreparedStatement updateTransStmt;

	private static PreparedStatement insertTransStmt;

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
		if (conn != null)
			conn.close();
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
						+ proj.toString());

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
			insertTransStmt.setInt(4, label.intValue());
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
		else if (sqlStatement != null)
			addRecordManually(term, definition);
		else
			addRecordViaHibernate(term, definition);

	}

	/**
	 * Main class to map the term and its definition to the Lex Component object
	 * model. Works but painfully slow!
	 */
	public void addRecordViaHibernate(String term, String definition)
			throws Exception {
		LinkedList ll;
		ListIterator li;
		ITerm lexTerm;
		TransitionalData trans = null;
		boolean found, newTerm = false;
		String existingDef;

		// displaying for debugging purposes only
		// System.out.println(term);

		//check to see if the term already exists
		lexTerm = LexComponentRepository.loadTermByTerm(term);

		found = false;

		//if it doesn't create a new term.
		if (null == lexTerm) {
			// System.out.println("New term");
			lexTerm = new Term();
			lexTerm.setTerm(term);
			lexTerm.setMeta(defaultMeta());
			//save the Term to the database. This step is necessary here to
			// generate a unique id
			LexComponentRepository.save(lexTerm);
			newTerm = true;
		} else {
			// System.out.println("Old term");
			li = lexTerm.getTransitionalData().listIterator();

			while (li.hasNext()) {
				trans = (TransitionalData) li.next();
				if (trans.getMeta().getCreatedByProjSub().equals(proj)) {
					found = true;
					break;
				}
			}
			newTerm = false;
		}

		//OLD CODE
		/*
		 * TransitionalData trans = getTransData( lexTerm.getId() );
		 * 
		 * trans.setTransitionalDataLabel( new Short( "1" ) );
		 * trans.setForPublicConsumption( "true" ); trans.setTermId(
		 * termParent.getId() );
		 * 
		 * String newText = null; if (null != trans.getTransitionalDataText() )
		 * newText = trans.getTransitionalDataText() + ". " +
		 * def.toString().trim(); else newText = def.toString().trim();
		 * trans.setTransitionalDataText( newText ); trans.save();
		 * 
		 * out.print( termParent.getTerm() + " ");
		 * 
		 * int subTarget = 30; if ( trans.getTransitionalDataText().length() <
		 * 40 ) subTarget = trans.getTransitionalDataText().length(); out.print(
		 * trans.getTransitionalDataText().substring( 0, subTarget ) );
		 */

		//NEW CODE
		// check if there is already a defition for this project
		if (found) {
			existingDef = trans.getTransitionalDataText().trim();
			if (existingDef.equals(""))
				found = false;
			else if (existingDef.indexOf(definition) < 0) {
				definition = existingDef + ". " + definition;
				found = false;
			}
		} else {
			// System.out.println("New definition");
			trans = new TransitionalData();
			trans.setMeta(defaultMeta());
			trans.setTransitionalDataLabel(label);
			trans.setParentId(lexTerm.getMetaId());
			trans.setForPublicConsumption(publicCons);
			if (newTerm) {
				ll = new LinkedList();
				ll.add(trans);
				lexTerm.setTransitionalData(ll);
			} else
				lexTerm.getTransitionalData().add(trans);

		}

		if (!found) {
			trans.setTransitionalDataText(definition);
			//save the Term to the database.
			LexComponentRepository.save(lexTerm);
		}
	}

	public Meta defaultMeta() {
		//use the file src/sql/import-updates.sql to add new metadata for use
		// in this method.
		Meta meta = new Meta();
		meta.setCreatedBy(creator);
		meta.setModifiedBy(creator);
		meta.setCreatedByProjSub(proj);
		meta.setModifiedByProjSub(proj);
		meta.setSource(new Integer(0));
		// meta.setTranslationOf( new Integer( 0 ) );
		meta.setLanguage(new Integer("0"));
		meta.setDialect(new Integer("0"));
		meta.setScript(new Integer("1"));
		meta.setNote(note);
		return meta;
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
			conn = DriverManager.getConnection(rb
					.getString("dictionaryimporter.url"));
			conn2 = DriverManager.getConnection(rb
					.getString("dictionaryimporter.url"));
			conn3 = DriverManager.getConnection(rb
					.getString("dictionaryimporter.url"));
			conn4 = DriverManager.getConnection(rb
					.getString("dictionaryimporter.url"));
			conn5 = DriverManager.getConnection(rb
					.getString("dictionaryimporter.url"));
		} catch (Exception ex) {
			// handle any errors
			System.out.println("Could not connect to database!");
			ex.printStackTrace();
			System.exit(0);
		}
	}

	private static void initStatements() {
		try {
			sqlStatement = conn.createStatement();
			insertMetaStmt = conn.prepareStatement(INSERT_META);
			selectMetaStmt = conn.prepareStatement(SELECT_META);
			insertTermStmt = conn.prepareStatement(INSERT_TERM);
			updateTransStmt = conn.prepareStatement(UPDATE_TRANS);
			insertTransStmt = conn.prepareStatement(INSERT_TRANS);
		} catch (Exception ex) {
			// handle any errors
			System.out.println("Could not create statement!");
			ex.printStackTrace();
			System.exit(0);
		}
	}

	public DictionaryImporter() {
	}

	public static void main(String[] args) throws Exception {
		String format = null;
		InputStream is;

		int argNum = args.length, currentArg = 0;
		String option;
		boolean file = false;
		boolean manual = false;
		out = null;

		delimiterType = delimiterDash;
		delimiter = "-";
		creator = new Integer(80);
		proj = new Integer(18);
		note = "This entry comes from The Rangjung Yeshe Tibetan-English Dictionary of Buddhist Culture (www.rangjung.com).";
		publicCons = "true";
		label = new Integer(6);
		sqlStatement = null;
		conn = null;

		if (argNum <= currentArg) {
			System.out
					.println("Syntax: DictionaryImporter [-manual] [-format format] [-tab | -delim delimiter] "
							+ "[-creator creator-id] [-proj project-id] [-label label] [-note note] "
							+ "[-pub-cons public-consumption-marker] [input-file] [output-error-file]");
			return;
		}

		while (args[currentArg].charAt(0) == '-') {
			option = args[currentArg].substring(1);
			currentArg++;
			if (option.equals("manual")) {
				initConnections();
				initStatements();
				manual = true;
			} else if (option.equals("format")) {
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
			} else if (option.equals("creator")) {
				if (argNum <= currentArg) {
					System.out.println("Syntax error: creator expected.");
					return;
				}
				creator = new Integer(args[currentArg]);
				currentArg++;
			} else if (option.equals("proj")) {
				if (argNum <= currentArg) {
					System.out.println("Syntax error: project expected.");
					return;
				}
				proj = new Integer(args[currentArg]);
				currentArg++;
			} else if (option.equals("label")) {
				if (argNum <= currentArg) {
					System.out.println("Syntax error: label expected.");
					return;
				}
				label = new Integer(args[currentArg]);
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
				out = new PrintWriter(new OutputStreamWriter(
						new FileOutputStream(args[currentArg + 1]), format));
			else
				out = new PrintWriter(
						new FileOutputStream(args[currentArg + 1]));
			file = true;
		}

		if (file) {
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

		if (manual) {
			insertMetaStmt.setString(1, creator.toString());
			insertMetaStmt.setString(2, creator.toString());
			insertMetaStmt.setString(3, proj.toString());
			insertMetaStmt.setString(4, proj.toString());
			insertMetaStmt.setString(5, note);
		}

		new DictionaryImporter().doImport();
	}
}