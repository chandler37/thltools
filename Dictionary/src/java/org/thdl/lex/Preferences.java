package org.thdl.lex;

import org.thdl.users.*;
import org.thdl.lex.component.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Preferences
{
//attributes
	private int id;
	private int userId;
	private String preferencesName;
	private int projectSubject;
	private int source;
	private int language;
	private int script;
	private int dialect;
	private String note;
	private boolean useDefaultProjSub;
	private boolean useDefaultSource;
	private boolean useDefaultLanguage;
	private boolean useDefaultScript;
	private boolean useDefaultDialect;
	private boolean useDefaultNote;
	private int[] projectSubjectSet;
	private int[] sourceSet;
	private int[] languageSet;
	private int[] scriptSet;
	private int[] dialectSet;
//accessors	
	public void setId(int id) {
		this.id = id;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getId() {
		return id;
	}
	public int getUserId() {
		return userId;
	}
	public void setPreferencesName(String preferencesName) {
		this.preferencesName = preferencesName;
	}
	public String getPreferencesName() {
		return preferencesName;
	}
	public void setUseDefaultNote(boolean useDefaultNote) {
		this.useDefaultNote = useDefaultNote;
	}
	public boolean getUseDefaultNote() {
		return useDefaultNote;
	}
	public void setUseDefaultProjSub(boolean useDefaultProjSub) {
		this.useDefaultProjSub = useDefaultProjSub;
	}
	public boolean getUseDefaultProjSub() {
		return useDefaultProjSub;
	}
	public void setUseDefaultSource(boolean useDefaultSource) {
		this.useDefaultSource = useDefaultSource;
	}
	public void setUseDefaultLanguage(boolean useDefaultLanguage) {
		this.useDefaultLanguage = useDefaultLanguage;
	}
	public void setUseDefaultScript(boolean useDefaultScript) {
		this.useDefaultScript = useDefaultScript;
	}
	public void setUseDefaultDialect(boolean useDefaultDialect) {
		this.useDefaultDialect = useDefaultDialect;
	}
	public boolean getUseDefaultSource() {
		return useDefaultSource;
	}
	public boolean getUseDefaultLanguage() {
		return useDefaultLanguage;
	}
	public boolean getUseDefaultScript() {
		return useDefaultScript;
	}
	public boolean getUseDefaultDialect() {
		return useDefaultDialect;
	}
	public void setProjectSubject(int projectSubject) {
		this.projectSubject = projectSubject;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public void setLanguage(int language) {
		this.language = language;
	}
	public void setScript(int script) {
		this.script = script;
	}
	public void setDialect(int dialect) {
		this.dialect = dialect;
	}
	public void setProjectSubjectSet(int[] projectSubjectSet) {
		this.projectSubjectSet = projectSubjectSet;
	}
	public void setSourceSet(int[] sourceSet) {
		this.sourceSet = sourceSet;
	}
	public void setLanguageSet(int[] languageSet) {
		this.languageSet = languageSet;
	}
	public void setScriptSet(int[] scriptSet) {
		this.scriptSet = scriptSet;
	}
	public void setDialectSet(int[] dialectSet) {
		this.dialectSet = dialectSet;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public int getProjectSubject() {
		return projectSubject;
	}
	public int getSource() {
		return source;
	}
	public int getLanguage() {
		return language;
	}
	public int getScript() {
		return script;
	}
	public int getDialect() {
		return dialect;
	}
	public int[] getProjectSubjectSet() {
		return projectSubjectSet;
	}
	public int[] getSourceSet() {
		return sourceSet;
	}
	public int[] getLanguageSet() {
		return languageSet;
	}
	public int[] getScriptSet() {
		return scriptSet;
	}
	public int[] getDialectSet() {
		return dialectSet;
	}
	public String getNote() {
		return note;
	}
//helpers
	public void populate() throws LexRepositoryException, LexComponentException
	{
		String sql = "SELECT * FROM Preferences WHERE userId = " + getUserId();
		ResultSet results = LexRepository.getInstance().doQuery( sql );
		try {
			if ( LexUtilities.getResultSetSize( results ) > 0 )
			{
				results.next();
				setId( results.getInt( 1 ) );
				setUserId( results.getInt( 2 ) );
				setPreferencesName( results.getString( 3 ) );
				setProjectSubject( results.getInt( 4 ) );
				setSource( results.getInt( 5 ) );
				setLanguage( results.getInt( 6 ) );
				setScript( results.getInt( 7 ) );
				setDialect( results.getInt( 8 ) );
				setNote( results.getString( 9 ) );
				Boolean bool = Boolean.valueOf( results.getString( 10 ) );
				setUseDefaultProjSub( bool.booleanValue() );
				bool = Boolean.valueOf( results.getString( 11 ) );
				setUseDefaultSource( bool.booleanValue() );
				bool = Boolean.valueOf( results.getString( 12 ) );
				setUseDefaultLanguage( bool.booleanValue() );
				bool = Boolean.valueOf( results.getString( 13 ) );
				setUseDefaultScript( bool.booleanValue() );
				bool = Boolean.valueOf( results.getString( 14 ) );
				setUseDefaultDialect( bool.booleanValue() );
				bool = Boolean.valueOf( results.getString( 15 ) );
				setUseDefaultNote( bool.booleanValue() );
				setProjectSubjectSet( LexUtilities.convertTokensToIntArray( results.getString( 16 ) ) );
				setSourceSet( LexUtilities.convertTokensToIntArray( results.getString( 17 ) ) );
				setLanguageSet( LexUtilities.convertTokensToIntArray( results.getString( 18 ) ) );
				setScriptSet( LexUtilities.convertTokensToIntArray( results.getString( 19 ) ) );
				setDialectSet( LexUtilities.convertTokensToIntArray(results.getString( 20 ) ) );
			}
			else { 
				insertNew(); 
			}
		} catch (SQLException sqle)
		{ throw new LexComponentException( sqle.getMessage() ); }
	}

	public void insertNew() throws LexComponentException
	{
		try {
			String sql = "INSERT INTO Preferences ( id, userId ) VALUES ( NULL, " +getUserId() +" )";
			int i = LexRepository.getInstance().doUpdate( sql );
			setId( i );
		}
		catch ( LexRepositoryException lre ) { throw new LexComponentException( lre.getMessage() ); }
	}

	public void save() throws LexComponentException
	{
		try {
		String sql = "SELECT id FROM Preferences WHERE userId = " + getUserId();
		ResultSet results = LexRepository.getInstance().doQuery( sql );
		if ( LexUtilities.getResultSetSize( results ) < 1 )
		{
			insertNew();
		}
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append( "UPDATE Preferences SET userId = ");
		sqlBuffer.append( getUserId() );
		sqlBuffer.append( ", preferencesName =  '");
		sqlBuffer.append( getPreferencesName() );
		sqlBuffer.append( "', projectSubject =  ");
		sqlBuffer.append( getProjectSubject() );
		sqlBuffer.append( ", source =  ");
		sqlBuffer.append( getSource() );
		sqlBuffer.append( ", language =  ");
		sqlBuffer.append( getLanguage() );
		sqlBuffer.append( ", script =  ");
		sqlBuffer.append( getScript() );
		sqlBuffer.append( ", dialect =  ");
		sqlBuffer.append( getDialect() );
		sqlBuffer.append( ", note =  '");
		sqlBuffer.append( getNote() );
		sqlBuffer.append( "', useDefaultProjSub =  '");
		sqlBuffer.append( getUseDefaultProjSub() );
		sqlBuffer.append( "', useDefaultSource =  '");
		sqlBuffer.append( getUseDefaultSource() );
		sqlBuffer.append( "', useDefaultLanguage =  '");
		sqlBuffer.append( getUseDefaultLanguage() );
		sqlBuffer.append( "', useDefaultScript =  '");
		sqlBuffer.append( getUseDefaultScript() );
		sqlBuffer.append( "', useDefaultDialect =  '");
		sqlBuffer.append( getUseDefaultDialect() );
		sqlBuffer.append( "', useDefaultNote =  '");
		sqlBuffer.append( getUseDefaultNote() );
		sqlBuffer.append( "', projectSubjectSet =  '");
		sqlBuffer.append( LexUtilities.convertIntArrayToTokens( getProjectSubjectSet() ) );
		sqlBuffer.append( "', sourceSet =  '");
		sqlBuffer.append( LexUtilities.convertIntArrayToTokens( getSourceSet() ) );
		sqlBuffer.append( "', languageSet =  '");
		sqlBuffer.append( LexUtilities.convertIntArrayToTokens( getLanguageSet() ) );
		sqlBuffer.append( "', scriptSet =  '");
		sqlBuffer.append( LexUtilities.convertIntArrayToTokens( getScriptSet() ) );
		sqlBuffer.append( "', dialectSet =  '");
		sqlBuffer.append( LexUtilities.convertIntArrayToTokens( getDialectSet() ) );			
		sqlBuffer.append( "' WHERE id = ");
		sqlBuffer.append( getId() );
		
		int i = LexRepository.getInstance().doUpdate( sqlBuffer.toString() );
		} 
		catch ( LexRepositoryException lre ) { throw new LexComponentException( lre.getMessage() ); }
		catch ( SQLException sqle )  { throw new LexComponentException( sqle.getMessage() ); }
	}
		
//constructors
	public Preferences() throws LexRepositoryException, LexComponentException
	{
		setId( 0 );
		setUserId( 1 );
		setProjectSubject( 1 );
		setSource( 1 );
		setLanguage( 1 );
		setScript( 1 );
		setDialect( 1 );
		int[] temp = { getProjectSubject() };
		setProjectSubjectSet( temp );
		int[] temp2 = { getSource() };
		setSourceSet( temp2 );
		int[] temp3 = { getLanguage() };
		setLanguageSet( temp3 );
		int[] temp4 = { getDialect() };
		setDialectSet( temp4 );
		int[] temp5 = { getScript() };
		setScriptSet( temp5 );
		setNote( "Default Note" );
	}
	public Preferences(ThdlUser user) throws LexRepositoryException, LexComponentException
	{
		this();
		setUserId( user.getId() );
		populate();
		/* setProjectSubject( user.getDefaultProjSub() );
		setSource(  user.getDefaultSource() );
		setLanguage( user.getDefaultLanguage() );
		setScript( user.getDefaultScript() );
		setDialect( user.getDefaultDialect() ); */
	}
	public static void main(String[] args)
	{
		try {
		Preferences component = new Preferences();
		component.setUserId( 2 );
		component.save();
		System.out.println( component.getId() + " " + component.getUserId() );
		}
		catch(Exception e){ e.printStackTrace(); }
	}
}

