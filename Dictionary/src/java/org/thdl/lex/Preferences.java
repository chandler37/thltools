package org.thdl.lex;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.thdl.lex.component.LexComponentException;
import org.thdl.users.ThdlUser;

/**
 * Description of the Class
 * 
 * @author travis
 * @created October 6, 2003
 */
public class Preferences {
	//attributes
	private Integer id;

	private Integer userId;

	private String preferencesName;

	private Integer projectSubject;

	private Integer source;

	private Integer language;

	private Integer script;

	private Integer dialect;

	private String note;

	private boolean useDefaultProjSub;

	private boolean useDefaultSource;

	private boolean useDefaultLanguage;

	private boolean useDefaultScript;

	private boolean useDefaultDialect;

	private boolean useDefaultNote;

	private Integer[] projectSubjectSet;

	private Integer[] sourceSet;

	private Integer[] languageSet;

	private Integer[] scriptSet;

	private Integer[] dialectSet;

	//accessors

	/**
	 * Sets the id attribute of the Preferences object
	 * 
	 * @param id
	 *            The new id value
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Sets the userId attribute of the Preferences object
	 * 
	 * @param userId
	 *            The new userId value
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * Sets the preferencesName attribute of the Preferences object
	 * 
	 * @param preferencesName
	 *            The new preferencesName value
	 */
	public void setPreferencesName(String preferencesName) {
		this.preferencesName = preferencesName;
	}

	/**
	 * Sets the useDefaultNote attribute of the Preferences object
	 * 
	 * @param useDefaultNote
	 *            The new useDefaultNote value
	 */
	public void setUseDefaultNote(boolean useDefaultNote) {
		this.useDefaultNote = useDefaultNote;
	}

	/**
	 * Sets the useDefaultProjSub attribute of the Preferences object
	 * 
	 * @param useDefaultProjSub
	 *            The new useDefaultProjSub value
	 */
	public void setUseDefaultProjSub(boolean useDefaultProjSub) {
		this.useDefaultProjSub = useDefaultProjSub;
	}

	/**
	 * Sets the useDefaultSource attribute of the Preferences object
	 * 
	 * @param useDefaultSource
	 *            The new useDefaultSource value
	 */
	public void setUseDefaultSource(boolean useDefaultSource) {
		this.useDefaultSource = useDefaultSource;
	}

	/**
	 * Sets the useDefaultLanguage attribute of the Preferences object
	 * 
	 * @param useDefaultLanguage
	 *            The new useDefaultLanguage value
	 */
	public void setUseDefaultLanguage(boolean useDefaultLanguage) {
		this.useDefaultLanguage = useDefaultLanguage;
	}

	/**
	 * Sets the useDefaultScript attribute of the Preferences object
	 * 
	 * @param useDefaultScript
	 *            The new useDefaultScript value
	 */
	public void setUseDefaultScript(boolean useDefaultScript) {
		this.useDefaultScript = useDefaultScript;
	}

	/**
	 * Sets the useDefaultDialect attribute of the Preferences object
	 * 
	 * @param useDefaultDialect
	 *            The new useDefaultDialect value
	 */
	public void setUseDefaultDialect(boolean useDefaultDialect) {
		this.useDefaultDialect = useDefaultDialect;
	}

	/**
	 * Sets the projectSubject attribute of the Preferences object
	 * 
	 * @param projectSubject
	 *            The new projectSubject value
	 */
	public void setProjectSubject(Integer projectSubject) {
		this.projectSubject = projectSubject;
	}

	/**
	 * Sets the source attribute of the Preferences object
	 * 
	 * @param source
	 *            The new source value
	 */
	public void setSource(Integer source) {
		this.source = source;
	}

	/**
	 * Sets the language attribute of the Preferences object
	 * 
	 * @param language
	 *            The new language value
	 */
	public void setLanguage(Integer language) {
		this.language = language;
	}

	/**
	 * Sets the script attribute of the Preferences object
	 * 
	 * @param script
	 *            The new script value
	 */
	public void setScript(Integer script) {
		this.script = script;
	}

	/**
	 * Sets the dialect attribute of the Preferences object
	 * 
	 * @param dialect
	 *            The new dialect value
	 */
	public void setDialect(Integer dialect) {
		this.dialect = dialect;
	}

	/**
	 * Sets the projectSubjectSet attribute of the Preferences object
	 * 
	 * @param projectSubjectSet
	 *            The new projectSubjectSet value
	 */
	public void setProjectSubjectSet(Integer[] projectSubjectSet) {
		this.projectSubjectSet = projectSubjectSet;
	}

	/**
	 * Sets the sourceSet attribute of the Preferences object
	 * 
	 * @param sourceSet
	 *            The new sourceSet value
	 */
	public void setSourceSet(Integer[] sourceSet) {
		this.sourceSet = sourceSet;
	}

	/**
	 * Sets the languageSet attribute of the Preferences object
	 * 
	 * @param languageSet
	 *            The new languageSet value
	 */
	public void setLanguageSet(Integer[] languageSet) {
		this.languageSet = languageSet;
	}

	/**
	 * Sets the scriptSet attribute of the Preferences object
	 * 
	 * @param scriptSet
	 *            The new scriptSet value
	 */
	public void setScriptSet(Integer[] scriptSet) {
		this.scriptSet = scriptSet;
	}

	/**
	 * Sets the dialectSet attribute of the Preferences object
	 * 
	 * @param dialectSet
	 *            The new dialectSet value
	 */
	public void setDialectSet(Integer[] dialectSet) {
		this.dialectSet = dialectSet;
	}

	/**
	 * Sets the note attribute of the Preferences object
	 * 
	 * @param note
	 *            The new note value
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * Gets the id attribute of the Preferences object
	 * 
	 * @return The id value
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Gets the userId attribute of the Preferences object
	 * 
	 * @return The userId value
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * Gets the preferencesName attribute of the Preferences object
	 * 
	 * @return The preferencesName value
	 */
	public String getPreferencesName() {
		return preferencesName;
	}

	/**
	 * Gets the useDefaultNote attribute of the Preferences object
	 * 
	 * @return The useDefaultNote value
	 */
	public boolean getUseDefaultNote() {
		return useDefaultNote;
	}

	/**
	 * Gets the useDefaultProjSub attribute of the Preferences object
	 * 
	 * @return The useDefaultProjSub value
	 */
	public boolean getUseDefaultProjSub() {
		return useDefaultProjSub;
	}

	/**
	 * Gets the useDefaultSource attribute of the Preferences object
	 * 
	 * @return The useDefaultSource value
	 */
	public boolean getUseDefaultSource() {
		return useDefaultSource;
	}

	/**
	 * Gets the useDefaultLanguage attribute of the Preferences object
	 * 
	 * @return The useDefaultLanguage value
	 */
	public boolean getUseDefaultLanguage() {
		return useDefaultLanguage;
	}

	/**
	 * Gets the useDefaultScript attribute of the Preferences object
	 * 
	 * @return The useDefaultScript value
	 */
	public boolean getUseDefaultScript() {
		return useDefaultScript;
	}

	/**
	 * Gets the useDefaultDialect attribute of the Preferences object
	 * 
	 * @return The useDefaultDialect value
	 */
	public boolean getUseDefaultDialect() {
		return useDefaultDialect;
	}

	/**
	 * Gets the projectSubject attribute of the Preferences object
	 * 
	 * @return The projectSubject value
	 */
	public Integer getProjectSubject() {
		return projectSubject;
	}

	/**
	 * Gets the source attribute of the Preferences object
	 * 
	 * @return The source value
	 */
	public Integer getSource() {
		return source;
	}

	/**
	 * Gets the language attribute of the Preferences object
	 * 
	 * @return The language value
	 */
	public Integer getLanguage() {
		return language;
	}

	/**
	 * Gets the script attribute of the Preferences object
	 * 
	 * @return The script value
	 */
	public Integer getScript() {
		return script;
	}

	/**
	 * Gets the dialect attribute of the Preferences object
	 * 
	 * @return The dialect value
	 */
	public Integer getDialect() {
		return dialect;
	}

	/**
	 * Gets the projectSubjectSet attribute of the Preferences object
	 * 
	 * @return The projectSubjectSet value
	 */
	public Integer[] getProjectSubjectSet() {
		return projectSubjectSet;
	}

	/**
	 * Gets the sourceSet attribute of the Preferences object
	 * 
	 * @return The sourceSet value
	 */
	public Integer[] getSourceSet() {
		return sourceSet;
	}

	/**
	 * Gets the languageSet attribute of the Preferences object
	 * 
	 * @return The languageSet value
	 */
	public Integer[] getLanguageSet() {
		return languageSet;
	}

	/**
	 * Gets the scriptSet attribute of the Preferences object
	 * 
	 * @return The scriptSet value
	 */
	public Integer[] getScriptSet() {
		return scriptSet;
	}

	/**
	 * Gets the dialectSet attribute of the Preferences object
	 * 
	 * @return The dialectSet value
	 */
	public Integer[] getDialectSet() {
		return dialectSet;
	}

	/**
	 * Gets the note attribute of the Preferences object
	 * 
	 * @return The note value
	 */
	public String getNote() {
		return note;
	}

	//helpers

	/**
	 * Description of the Method
	 * 
	 * @exception LexRepositoryException
	 *                Description of the Exception
	 * @exception LexComponentException
	 *                Description of the Exception
	 */
	public void populate() throws LexRepositoryException, LexComponentException {
		String sql = "SELECT * FROM Preferences WHERE userId = " + getUserId();
		try {
			LexRepository lr = LexRepository.getInstance();
			Connection con = lr.getDataSource().getConnection();
			ResultSet results = con.createStatement().executeQuery(sql);
			if (LexUtilities.getResultSetSize(results) > 0) {
				results.next();
				setId(new Integer(results.getInt(1)));
				setUserId(new Integer(results.getInt(2)));
				setPreferencesName(results.getString(3));
				setProjectSubject(new Integer(results.getInt(4)));
				setSource(new Integer(results.getInt(5)));
				setLanguage(new Integer(results.getInt(6)));
				setScript(new Integer(results.getInt(7)));
				setDialect(new Integer(results.getInt(8)));
				setNote(results.getString(9));
				Boolean bool = Boolean.valueOf(results.getString(10));
				setUseDefaultProjSub(bool.booleanValue());
				bool = Boolean.valueOf(results.getString(11));
				setUseDefaultSource(bool.booleanValue());
				bool = Boolean.valueOf(results.getString(12));
				setUseDefaultLanguage(bool.booleanValue());
				bool = Boolean.valueOf(results.getString(13));
				setUseDefaultScript(bool.booleanValue());
				bool = Boolean.valueOf(results.getString(14));
				setUseDefaultDialect(bool.booleanValue());
				bool = Boolean.valueOf(results.getString(15));
				setUseDefaultNote(bool.booleanValue());
				setProjectSubjectSet(LexUtilities
						.convertTokensToIntegerArray(results.getString(16)));
				setSourceSet(LexUtilities.convertTokensToIntegerArray(results
						.getString(17)));
				setLanguageSet(LexUtilities.convertTokensToIntegerArray(results
						.getString(18)));
				setScriptSet(LexUtilities.convertTokensToIntegerArray(results
						.getString(19)));
				setDialectSet(LexUtilities.convertTokensToIntegerArray(results
						.getString(20)));
			} else {
				insertNew();
			}
			con.close();
		} catch (SQLException sqle) {
			throw new LexComponentException(sqle.getMessage());
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @exception LexComponentException
	 *                Description of the Exception
	 */
	public void insertNew() throws LexComponentException {
		try {
			String sql = "INSERT INTO Preferences ( id, userId ) VALUES ( NULL, "
					+ getUserId() + " )";
			Integer i = new Integer(LexRepository.getInstance().doUpdate(sql));
			setId(i);
		} catch (LexRepositoryException lre) {
			throw new LexComponentException(lre.getMessage());
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @exception LexComponentException
	 *                Description of the Exception
	 */
	public void save() throws LexComponentException {
		try {
			String sql = "SELECT id FROM Preferences WHERE userId = "
					+ getUserId();
			LexRepository lr = LexRepository.getInstance();
			Connection con = lr.getDataSource().getConnection();
			ResultSet results = con.createStatement().executeQuery(sql);

			if (LexUtilities.getResultSetSize(results) < 1) {
				insertNew();
			}
			con.close();
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append("UPDATE Preferences SET userId = ");
			sqlBuffer.append(getUserId());
			sqlBuffer.append(", preferencesName =  '");
			sqlBuffer.append(getPreferencesName());
			sqlBuffer.append("', projectSubject =  ");
			sqlBuffer.append(getProjectSubject());
			sqlBuffer.append(", source =  ");
			sqlBuffer.append(getSource());
			sqlBuffer.append(", language =  ");
			sqlBuffer.append(getLanguage());
			sqlBuffer.append(", script =  ");
			sqlBuffer.append(getScript());
			sqlBuffer.append(", dialect =  ");
			sqlBuffer.append(getDialect());
			sqlBuffer.append(", note =  '");
			sqlBuffer.append(LexUtilities.escape(getNote()));
			sqlBuffer.append("', useDefaultProjSub =  '");
			sqlBuffer.append(getUseDefaultProjSub());
			sqlBuffer.append("', useDefaultSource =  '");
			sqlBuffer.append(getUseDefaultSource());
			sqlBuffer.append("', useDefaultLanguage =  '");
			sqlBuffer.append(getUseDefaultLanguage());
			sqlBuffer.append("', useDefaultScript =  '");
			sqlBuffer.append(getUseDefaultScript());
			sqlBuffer.append("', useDefaultDialect =  '");
			sqlBuffer.append(getUseDefaultDialect());
			sqlBuffer.append("', useDefaultNote =  '");
			sqlBuffer.append(getUseDefaultNote());
			sqlBuffer.append("', projectSubjectSet =  '");
			sqlBuffer.append(LexUtilities
					.convertIntegerArrayToTokens(getProjectSubjectSet()));
			sqlBuffer.append("', sourceSet =  '");
			sqlBuffer.append(LexUtilities
					.convertIntegerArrayToTokens(getSourceSet()));
			sqlBuffer.append("', languageSet =  '");
			sqlBuffer.append(LexUtilities
					.convertIntegerArrayToTokens(getLanguageSet()));
			sqlBuffer.append("', scriptSet =  '");
			sqlBuffer.append(LexUtilities
					.convertIntegerArrayToTokens(getScriptSet()));
			sqlBuffer.append("', dialectSet =  '");
			sqlBuffer.append(LexUtilities
					.convertIntegerArrayToTokens(getDialectSet()));
			sqlBuffer.append("' WHERE id = ");
			sqlBuffer.append(getId());
			LexRepository.getInstance().doUpdate(sqlBuffer.toString());
		} catch (LexRepositoryException lre) {
			throw new LexComponentException(lre.getMessage());
		} catch (SQLException sqle) {
			throw new LexComponentException(sqle.getMessage());
		}
	}

	/**
	 * The main program for the Preferences class
	 * 
	 * @param args
	 *            The command line arguments
	 */
	public static void main(String[] args) {
		try {
			Preferences component = new Preferences();
			component.setUserId(new Integer(2));
			component.save();
			System.out.println(component.getId() + " " + component.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//constructors
	/**
	 * Constructor for the Preferences object
	 */
	public Preferences() {
		setId(new Integer(0));
		setUserId(new Integer(1));
		setProjectSubject(new Integer(1));
		setSource(new Integer(1));
		setLanguage(new Integer(1));
		setScript(new Integer(1));
		setDialect(new Integer(1));
		Integer[] temp = { getProjectSubject() };
		setProjectSubjectSet(temp);
		Integer[] temp2 = { getSource() };
		setSourceSet(temp2);
		Integer[] temp3 = { getLanguage() };
		setLanguageSet(temp3);
		Integer[] temp4 = { getDialect() };
		setDialectSet(temp4);
		Integer[] temp5 = { getScript() };
		setScriptSet(temp5);
		setNote("Default Note");
	}

	/**
	 * Constructor for the Preferences object
	 * 
	 * @param user
	 *            Description of the Parameter
	 * @exception LexRepositoryException
	 *                Description of the Exception
	 * @exception LexComponentException
	 *                Description of the Exception
	 */
	public Preferences(ThdlUser user) throws LexRepositoryException,
			LexComponentException {
		this();
		setUserId(user.getId());
		populate();
		/*
		 * setProjectSubject( user.getDefaultProjSub() ); setSource(
		 * user.getDefaultSource() ); setLanguage( user.getDefaultLanguage() );
		 * setScript( user.getDefaultScript() ); setDialect(
		 * user.getDefaultDialect() );
		 */
	}
}

