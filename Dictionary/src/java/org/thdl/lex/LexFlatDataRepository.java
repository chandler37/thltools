package org.thdl.lex;

import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class LexFlatDataRepository
{
//attributes
	int displayLanguage=1; //this should default to the id for english from Languages table
	private static LexFlatDataRepository instance;

	HashMap users;
	HashMap userRoles;
	HashMap projectSubjects;
	HashMap sources;
	HashMap languages;
	HashMap scripts;
	HashMap literarySources;
	HashMap transitionalDataLabels;

	HashMap dialects; //represents merge table
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
	public static LexFlatDataRepository getInstance()
	{
		if (null == instance)
			instance = new LexFlatDataRepository();
		return instance;
	}
	public void setTransitionalDataLabels()
	{
		String sql = "SELECT id, transitionalDataLabel FROM TransitionalDataLabels";
		setTransitionalDataLabels( createMap( sql ) );
	}
	public void setTransitionalDataLabels(HashMap transitionalDataLabels) {
		this.transitionalDataLabels = transitionalDataLabels;
	}
	public HashMap getTransitionalDataLabels() {
		return transitionalDataLabels;
	}
	public void setLiterarySources(HashMap literarySources) {
		this.literarySources = literarySources;
	}
	public void setLiterarySources()
	{
		String sql="SELECT id, sourceNormalizedTitle FROM LiterarySources";
		setLiterarySources( createMap( sql ) );
	}
	public HashMap getLiterarySources() {
		return literarySources;
	}
	public void setUsers(HashMap users) {
		this.users = users;
	}
	public void setUsers() {
		String sql="SELECT id, firstName, lastName FROM Users";
		setUsers( createMap(sql) );
	}	
	public void setProjectSubjects(HashMap projectSubjects) {
		this.projectSubjects = projectSubjects;
	}
	public void setProjectSubjects() {
		String sql="SELECT id, projectSubject FROM projectSubjects";
		setProjectSubjects( createMap(sql) );
	}
	public void setSources(HashMap sources) {
		this.sources = sources;
	}
	public void setSources() {
		String sql ="SELECT id, sourceTitle FROM Sources";
		setSources( createMap(sql) );
	}
	public void setLanguages(HashMap languages) {
		this.languages = languages;
	}
	public void setLanguages() {
		String sql="SELECT id,Language FROM Languages";
		setLanguages( createMap(sql) );
	}
	public void setScripts(HashMap scripts) {
		this.scripts = scripts;
	}
	public void setScripts() {
		String sql="SELECT id,script FROM scripts";
		setScripts( createMap(sql) );
	}
	public HashMap getUsers() {
		return users;
	}
	public HashMap getProjectSubjects() {
		return projectSubjects;
	}
	public HashMap getSources() {
		return sources;
	}
	public HashMap getLanguages() {
		return languages;
	}
	public HashMap getScripts() {
		return scripts;
	}
	public void setEtymologyTypes(HashMap etymologyTypes) {
		this.etymologyTypes = etymologyTypes;
	}
	public void setEtymologyTypes() {
		String sql="SELECT id, etymologyType FROM EtymologyTypes";
		setEtymologyTypes( createMap( sql ) );	
	}
	public HashMap getEtymologyTypes() {
		return etymologyTypes;
	}
	public void setSpellingTypes() {
		String sql="SELECT id, spellingType FROM SpellingTypes";
		setSpellingTypes( createMap( sql ) );	
	}
	public void setSpellingTypes(HashMap spellingTypes) {
		this.spellingTypes = spellingTypes;
	}
	public HashMap getSpellingTypes() {
		return spellingTypes;
	}
	public void setPhoneticsTypes() {
		String sql="SELECT id, phoneticsType FROM PhoneticsTypes";
		setPhoneticsTypes( createMap( sql ) );	
	}
	public void setPhoneticsTypes(HashMap phoneticsTypes) {
		this.phoneticsTypes = phoneticsTypes;
	}
	public HashMap getPhoneticsTypes() {
		return phoneticsTypes;
	}
	public void setRelatedTermTypes() {
		String sql="SELECT id, relatedTermType FROM RelatedTermTypes";
		setRelatedTermTypes( createMap( sql ) );	
	}
	public void setRelatedTermTypes(HashMap relatedTermTypes) {
		this.relatedTermTypes = relatedTermTypes;
	}
	public HashMap getRelatedTermTypes() {
		return relatedTermTypes;
	}
	public void setRegisters() {
		String sql="SELECT id, register FROM Registers";
		setRegisters( createMap( sql ) );		
	}
	public void setRegisters(HashMap registers) {
		this.registers = registers;
	}
	public HashMap getRegisters() {
		return registers;
	}
	public void setFunctionsGeneral() {
		String sql="SELECT id, functionGeneral FROM FunctionsGeneral";
		setFunctionsGeneral( createMap( sql ) );
	}
	public void setFunctionsGeneral(HashMap functionsGeneral) {
		this.functionsGeneral = functionsGeneral;
	}
	public HashMap getFunctionsGeneral() {
		return functionsGeneral;
	}
	public void setMajorDialectFamilies()
	{
		String sql = "SELECT id, majorDialectFamily FROM MajorDialectFamilies";
		setMajorDialectFamilies( createMap( sql ) );
	}
	public void setMajorDialectFamilies(HashMap majorDialectFamilies) {
		this.majorDialectFamilies = majorDialectFamilies;
	}
	public HashMap getMajorDialectFamilies() {
		return majorDialectFamilies;
	}
// accessor methods for HashMap items
	public String getUser(int userId)
	{
		return (String) getUsers().get( new Integer( userId ) );
	}
	public String getProjectSubject(int projSubId)
	{
		return (String) getProjectSubjects().get( new Integer( projSubId ) );
	}
	public String getSource(int sourceId)
	{
		return (String) getSources().get( new Integer( sourceId ) );
	}
	public String getLanguage(int langId)
	{
		return (String) getLanguages().get( new Integer( langId ) );
	}
	public String getScript(int scriptId)
	{
		return (String) getScripts().get( new Integer( scriptId ) );
	}
	public String getLiterarySource(int litSourceId)
	{
		return (String) getLiterarySources().get( new Integer( litSourceId ) );
	}
	public String getEtymologyType(int etymType) 
	{
		return (String) getEtymologyTypes().get( new Integer( etymType ) );
	}
	public String getSpellingType(int varType) 
	{
		return (String) getSpellingTypes().get( new Integer( varType ) );
	}
	public String getRegister( int reg ) 
	{
		return (String) getRegisters().get( new Integer( reg ) );
	}
	public String getFunctionGeneral( int funcGen ) 
	{
		return (String) getFunctionsGeneral().get( new Integer( funcGen ) );
	}
	public String getMajorDialectFamily( int dial )
	{
		return (String)getMajorDialectFamilies().get( new Integer( dial ) );
	}
	public String getTransitionalDataLabel( int label )
	{
		return (String)getTransitionalDataLabels().get( new Integer( label ) );
	}
//helpers
	public HashMap createMap(String sql)
	{
		ResultSet rs = null;
		HashMap map = new HashMap();
		try
		{
			LexRepository lr = LexRepository.getInstance();
			rs = lr.getQueryStatement().executeQuery( sql );
			if (null != rs)
			{
				int i = 0;
				Integer key=null;
				String value="";
				while ( rs.next() )
				{
					i = rs.getInt(1);
					key = new Integer( i );
					value = rs.getString(2);
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnCount = rsmd.getColumnCount();
					for (int x=3; x <= columnCount; x++)
						value = value + " " + rs.getString(x);
					map.put( key , value );
				}
			}
		} catch (LexRepositoryException lre)
		{
			lre.printStackTrace();
		} catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}
		return map;
	}
	
//constructors
	public LexFlatDataRepository()
	{
		setUsers();
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
	public static void main(String[] args)
	{
		LexFlatDataRepository lfdr = new LexFlatDataRepository();
		System.out.println( lfdr.getUser(1) );
		System.out.println( lfdr.getProjectSubject(6) );
		System.out.println( lfdr.getSource(1) );
		System.out.println( lfdr.getLanguage(1) );
		System.out.println( lfdr.getEtymologyType(1) );
	}
}
