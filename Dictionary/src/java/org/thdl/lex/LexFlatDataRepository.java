package org.thdl.lex;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

import org.thdl.users.ThdlUserRepository;

/**
 * Description of the Class
 * 
 * @author travis
 * @created December 15, 2003
 */
public class LexFlatDataRepository {
	//attributes
	int displayLanguage = 1;//this should default to the id for english from
							// Languages table

	private static LexFlatDataRepository instance;

	HashMap users;

	HashMap userRoles;

	HashMap projectSubjects;

	HashMap sources;

	HashMap languages;

	HashMap scripts;

	HashMap literarySources;

	HashMap transitionalDataLabels;

	HashMap dialects;//represents merge table

	HashMap majorDialectFamilies;

	HashMap specificDialects;

	HashMap functionsGeneral;

	HashMap functionsSpecific;

	HashMap spellingTypes;

	HashMap phoneticsTypes;

	HashMap relatedTermTypes;

	HashMap etymologyTypes;

	HashMap registers;

	HashMap commentTypes;

	//accessors
	/**
	 * Gets the instance attribute of the LexFlatDataRepository class
	 * 
	 * @return The instance value
	 */
	public static LexFlatDataRepository getInstance() {
		if (null == instance) {
			instance = new LexFlatDataRepository();
		}
		return instance;
	}

	/**
	 * Sets the transitionalDataLabels attribute of the LexFlatDataRepository
	 * object
	 */
	public void setTransitionalDataLabels() {
		String sql = "SELECT id, transitionalDataLabel FROM TransitionalDataLabels";
		setTransitionalDataLabels(createMap(sql));
	}

	/**
	 * Sets the transitionalDataLabels attribute of the LexFlatDataRepository
	 * object
	 * 
	 * @param transitionalDataLabels
	 *            The new transitionalDataLabels value
	 */
	public void setTransitionalDataLabels(HashMap transitionalDataLabels) {
		this.transitionalDataLabels = transitionalDataLabels;
	}

	/**
	 * Gets the transitionalDataLabels attribute of the LexFlatDataRepository
	 * object
	 * 
	 * @return The transitionalDataLabels value
	 */
	public HashMap getTransitionalDataLabels() {
		return transitionalDataLabels;
	}

	/**
	 * Sets the literarySources attribute of the LexFlatDataRepository object
	 * 
	 * @param literarySources
	 *            The new literarySources value
	 */
	public void setLiterarySources(HashMap literarySources) {
		this.literarySources = literarySources;
	}

	/**
	 * Sets the literarySources attribute of the LexFlatDataRepository object
	 */
	public void setLiterarySources() {
		String sql = "SELECT id, sourceNormalizedTitle FROM LiterarySources";
		setLiterarySources(createMap(sql));
	}

	/**
	 * Gets the literarySources attribute of the LexFlatDataRepository object
	 * 
	 * @return The literarySources value
	 */
	public HashMap getLiterarySources() {
		return literarySources;
	}

	/**
	 * Sets the users attribute of the LexFlatDataRepository object
	 * 
	 * @param users
	 *            The new users value
	 */
	public void setUsers(HashMap users) {
		this.users = users;
	}

	/**
	 * Sets the users attribute of the LexFlatDataRepository object
	 * 
	 * @exception LexRepositoryException
	 *                Description of the Exception
	 */
	public void setUsers() throws LexRepositoryException {
		try {
			ThdlUserRepository tr = ThdlUserRepository.getInstance();
			setUsers(tr.getUsernameMap());
		} catch (Exception e) {
			throw new LexRepositoryException(e);
		}
	}

	/**
	 * Sets the projectSubjects attribute of the LexFlatDataRepository object
	 * 
	 * @param projectSubjects
	 *            The new projectSubjects value
	 */
	public void setProjectSubjects(HashMap projectSubjects) {
		this.projectSubjects = projectSubjects;
	}

	/**
	 * Sets the projectSubjects attribute of the LexFlatDataRepository object
	 */
	public void setProjectSubjects() {
		String sql = "SELECT id, projectSubject FROM ProjectSubjects";
		setProjectSubjects(createMap(sql));
	}

	/**
	 * Sets the sources attribute of the LexFlatDataRepository object
	 * 
	 * @param sources
	 *            The new sources value
	 */
	public void setSources(HashMap sources) {
		this.sources = sources;
	}

	/**
	 * Sets the sources attribute of the LexFlatDataRepository object
	 */
	public void setSources() {
		String sql = "SELECT id, sourceTitle FROM Sources";
		setSources(createMap(sql));
	}

	/**
	 * Sets the languages attribute of the LexFlatDataRepository object
	 * 
	 * @param languages
	 *            The new languages value
	 */
	public void setLanguages(HashMap languages) {
		this.languages = languages;
	}

	/**
	 * Sets the languages attribute of the LexFlatDataRepository object
	 */
	public void setLanguages() {
		String sql = "SELECT id,Language FROM Languages";
		setLanguages(createMap(sql));
	}

	/**
	 * Sets the scripts attribute of the LexFlatDataRepository object
	 * 
	 * @param scripts
	 *            The new scripts value
	 */
	public void setScripts(HashMap scripts) {
		this.scripts = scripts;
	}

	/**
	 * Sets the scripts attribute of the LexFlatDataRepository object
	 */
	public void setScripts() {
		String sql = "SELECT id,script FROM Scripts";
		setScripts(createMap(sql));
	}

	/**
	 * Gets the users attribute of the LexFlatDataRepository object
	 * 
	 * @return The users value
	 * @exception LexRepositoryException
	 *                Description of the Exception
	 */
	public HashMap getUsers() throws LexRepositoryException {
		if (null == users) {
			setUsers();
		}
		return users;
	}

	/**
	 * Gets the projectSubjects attribute of the LexFlatDataRepository object
	 * 
	 * @return The projectSubjects value
	 */
	public HashMap getProjectSubjects() {
		return projectSubjects;
	}

	/**
	 * Gets the sources attribute of the LexFlatDataRepository object
	 * 
	 * @return The sources value
	 */
	public HashMap getSources() {
		return sources;
	}

	/**
	 * Gets the languages attribute of the LexFlatDataRepository object
	 * 
	 * @return The languages value
	 */
	public HashMap getLanguages() {
		return languages;
	}

	/**
	 * Gets the scripts attribute of the LexFlatDataRepository object
	 * 
	 * @return The scripts value
	 */
	public HashMap getScripts() {
		return scripts;
	}

	/**
	 * Sets the etymologyTypes attribute of the LexFlatDataRepository object
	 * 
	 * @param etymologyTypes
	 *            The new etymologyTypes value
	 */
	public void setEtymologyTypes(HashMap etymologyTypes) {
		this.etymologyTypes = etymologyTypes;
	}

	/**
	 * Sets the etymologyTypes attribute of the LexFlatDataRepository object
	 */
	public void setEtymologyTypes() {
		String sql = "SELECT id, etymologyType FROM EtymologyTypes";
		setEtymologyTypes(createMap(sql));
	}

	/**
	 * Gets the etymologyTypes attribute of the LexFlatDataRepository object
	 * 
	 * @return The etymologyTypes value
	 */
	public HashMap getEtymologyTypes() {
		return etymologyTypes;
	}

	/**
	 * Sets the spellingTypes attribute of the LexFlatDataRepository object
	 */
	public void setSpellingTypes() {
		String sql = "SELECT id, spellingType FROM SpellingTypes";
		setSpellingTypes(createMap(sql));
	}

	/**
	 * Sets the spellingTypes attribute of the LexFlatDataRepository object
	 * 
	 * @param spellingTypes
	 *            The new spellingTypes value
	 */
	public void setSpellingTypes(HashMap spellingTypes) {
		this.spellingTypes = spellingTypes;
	}

	/**
	 * Gets the spellingTypes attribute of the LexFlatDataRepository object
	 * 
	 * @return The spellingTypes value
	 */
	public HashMap getSpellingTypes() {
		return spellingTypes;
	}

	/**
	 * Sets the phoneticsTypes attribute of the LexFlatDataRepository object
	 */
	public void setPhoneticsTypes() {
		String sql = "SELECT id, phoneticsType FROM PhoneticsTypes";
		setPhoneticsTypes(createMap(sql));
	}

	/**
	 * Sets the phoneticsTypes attribute of the LexFlatDataRepository object
	 * 
	 * @param phoneticsTypes
	 *            The new phoneticsTypes value
	 */
	public void setPhoneticsTypes(HashMap phoneticsTypes) {
		this.phoneticsTypes = phoneticsTypes;
	}

	/**
	 * Gets the phoneticsTypes attribute of the LexFlatDataRepository object
	 * 
	 * @return The phoneticsTypes value
	 */
	public HashMap getPhoneticsTypes() {
		return phoneticsTypes;
	}

	/**
	 * Sets the relatedTermTypes attribute of the LexFlatDataRepository object
	 */
	public void setRelatedTermTypes() {
		String sql = "SELECT id, relatedTermType FROM RelatedTermTypes";
		setRelatedTermTypes(createMap(sql));
	}

	/**
	 * Sets the relatedTermTypes attribute of the LexFlatDataRepository object
	 * 
	 * @param relatedTermTypes
	 *            The new relatedTermTypes value
	 */
	public void setRelatedTermTypes(HashMap relatedTermTypes) {
		this.relatedTermTypes = relatedTermTypes;
	}

	/**
	 * Gets the relatedTermTypes attribute of the LexFlatDataRepository object
	 * 
	 * @return The relatedTermTypes value
	 */
	public HashMap getRelatedTermTypes() {
		return relatedTermTypes;
	}

	/**
	 * Sets the registers attribute of the LexFlatDataRepository object
	 */
	public void setRegisters() {
		String sql = "SELECT id, register FROM Registers";
		setRegisters(createMap(sql));
	}

	/**
	 * Sets the registers attribute of the LexFlatDataRepository object
	 * 
	 * @param registers
	 *            The new registers value
	 */
	public void setRegisters(HashMap registers) {
		this.registers = registers;
	}

	/**
	 * Gets the registers attribute of the LexFlatDataRepository object
	 * 
	 * @return The registers value
	 */
	public HashMap getRegisters() {
		return registers;
	}

	/**
	 * Sets the functionsGeneral attribute of the LexFlatDataRepository object
	 */
	public void setFunctionsGeneral() {
		String sql = "SELECT id, functionGeneral FROM FunctionsGeneral";
		setFunctionsGeneral(createMap(sql));
	}

	/**
	 * Sets the functionsGeneral attribute of the LexFlatDataRepository object
	 * 
	 * @param functionsGeneral
	 *            The new functionsGeneral value
	 */
	public void setFunctionsGeneral(HashMap functionsGeneral) {
		this.functionsGeneral = functionsGeneral;
	}

	/**
	 * Gets the functionsGeneral attribute of the LexFlatDataRepository object
	 * 
	 * @return The functionsGeneral value
	 */
	public HashMap getFunctionsGeneral() {
		return functionsGeneral;
	}

	/**
	 * Sets the majorDialectFamilies attribute of the LexFlatDataRepository
	 * object
	 */
	public void setMajorDialectFamilies() {
		String sql = "SELECT id, majorDialectFamily FROM MajorDialectFamilies";
		setMajorDialectFamilies(createMap(sql));
	}

	/**
	 * Sets the majorDialectFamilies attribute of the LexFlatDataRepository
	 * object
	 * 
	 * @param majorDialectFamilies
	 *            The new majorDialectFamilies value
	 */
	public void setMajorDialectFamilies(HashMap majorDialectFamilies) {
		this.majorDialectFamilies = majorDialectFamilies;
	}

	/**
	 * Gets the majorDialectFamilies attribute of the LexFlatDataRepository
	 * object
	 * 
	 * @return The majorDialectFamilies value
	 */
	public HashMap getMajorDialectFamilies() {
		return majorDialectFamilies;
	}

	// accessor methods for HashMap items

	/**
	 * Gets the user attribute of the LexFlatDataRepository object
	 * 
	 * @param userId
	 *            Description of the Parameter
	 * @return The user value
	 * @exception LexRepositoryException
	 *                Description of the Exception
	 */
	public String getUser(int userId) throws LexRepositoryException {
		return (String) getUsers().get(new Integer(userId));
	}

	/**
	 * Gets the projectSubject attribute of the LexFlatDataRepository object
	 * 
	 * @param projSubId
	 *            Description of the Parameter
	 * @return The projectSubject value
	 */
	public String getProjectSubject(int projSubId) {
		return (String) getProjectSubjects().get(new Integer(projSubId));
	}

	/**
	 * Gets the source attribute of the LexFlatDataRepository object
	 * 
	 * @param sourceId
	 *            Description of the Parameter
	 * @return The source value
	 */
	public String getSource(int sourceId) {
		return (String) getSources().get(new Integer(sourceId));
	}

	/**
	 * Gets the language attribute of the LexFlatDataRepository object
	 * 
	 * @param langId
	 *            Description of the Parameter
	 * @return The language value
	 */
	public String getLanguage(int langId) {
		return (String) getLanguages().get(new Integer(langId));
	}

	/**
	 * Gets the script attribute of the LexFlatDataRepository object
	 * 
	 * @param scriptId
	 *            Description of the Parameter
	 * @return The script value
	 */
	public String getScript(int scriptId) {
		return (String) getScripts().get(new Integer(scriptId));
	}

	/**
	 * Gets the literarySource attribute of the LexFlatDataRepository object
	 * 
	 * @param litSourceId
	 *            Description of the Parameter
	 * @return The literarySource value
	 */
	public String getLiterarySource(int litSourceId) {
		return (String) getLiterarySources().get(new Integer(litSourceId));
	}

	/**
	 * Gets the etymologyType attribute of the LexFlatDataRepository object
	 * 
	 * @param etymType
	 *            Description of the Parameter
	 * @return The etymologyType value
	 */
	public String getEtymologyType(int etymType) {
		return (String) getEtymologyTypes().get(new Integer(etymType));
	}

	/**
	 * Gets the spellingType attribute of the LexFlatDataRepository object
	 * 
	 * @param varType
	 *            Description of the Parameter
	 * @return The spellingType value
	 */
	public String getSpellingType(int varType) {
		return (String) getSpellingTypes().get(new Integer(varType));
	}

	/**
	 * Gets the register attribute of the LexFlatDataRepository object
	 * 
	 * @param reg
	 *            Description of the Parameter
	 * @return The register value
	 */
	public String getRegister(int reg) {
		return (String) getRegisters().get(new Integer(reg));
	}

	/**
	 * Gets the functionGeneral attribute of the LexFlatDataRepository object
	 * 
	 * @param funcGen
	 *            Description of the Parameter
	 * @return The functionGeneral value
	 */
	public String getFunctionGeneral(int funcGen) {
		return (String) getFunctionsGeneral().get(new Integer(funcGen));
	}

	/**
	 * Gets the majorDialectFamily attribute of the LexFlatDataRepository object
	 * 
	 * @param dial
	 *            Description of the Parameter
	 * @return The majorDialectFamily value
	 */
	public String getMajorDialectFamily(int dial) {
		return (String) getMajorDialectFamilies().get(new Integer(dial));
	}

	/**
	 * Gets the transitionalDataLabel attribute of the LexFlatDataRepository
	 * object
	 * 
	 * @param label
	 *            Description of the Parameter
	 * @return The transitionalDataLabel value
	 */
	public String getTransitionalDataLabel(int label) {
		return (String) getTransitionalDataLabels().get(new Integer(label));
	}

	//helpers

	/**
	 * Description of the Method
	 * 
	 * @param sql
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public HashMap createMap(String sql) {
		ResultSet rs = null;
		HashMap map = new HashMap();
		try {
			LexRepository lr = LexRepository.getInstance();
			Connection con = lr.getDataSource().getConnection();
			rs = con.createStatement().executeQuery(sql);
			if (null != rs) {
				int i = 0;
				Integer key = null;
				String value = "";
				while (rs.next()) {
					i = rs.getInt(1);
					key = new Integer(i);
					value = rs.getString(2);
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnCount = rsmd.getColumnCount();
					for (int x = 3; x <= columnCount; x++) {
						value = value + " " + rs.getString(x);
					}
					map.put(key, value);
				}
			}
			con.close();
		} catch (LexRepositoryException lre) {
			lre.printStackTrace();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return map;
	}

	//constructors
	/**
	 * Constructor for the LexFlatDataRepository object
	 */
	public LexFlatDataRepository() {
		setProjectSubjects();
		setSources();
		setLanguages();
		setScripts();
		setLiterarySources();
		setEtymologyTypes();
		setSpellingTypes();
		setPhoneticsTypes();
		setRelatedTermTypes();
		setRegisters();
		setFunctionsGeneral();
		setMajorDialectFamilies();
		setTransitionalDataLabels();
	}

	//main

	/**
	 * The main program for the LexFlatDataRepository class
	 * 
	 * @param args
	 *            The command line arguments
	 */
	public static void main(String[] args) {
		LexFlatDataRepository lfdr = new LexFlatDataRepository();
		//System.out.println( lfdr.getUser( 1 ) );
		System.out.println(lfdr.getProjectSubject(6));
		System.out.println(lfdr.getSource(1));
		System.out.println(lfdr.getLanguage(1));
		System.out.println(lfdr.getEtymologyType(1));
	}
}

