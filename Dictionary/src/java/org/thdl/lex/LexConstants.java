package org.thdl.lex;


/**
 *  Description of the Class
 *
 *@author     travis
 *@created    October 1, 2003
 */
public class LexConstants
{
	/*
	 *  REQUEST PARAM/ATTR NAMES AND VALUES USED BY LexComponentFilter.java
	 */
	//form field req params
	public final static String LABEL_REQ_PARAM = "comp";
	public final static String TERMLABEL_VALUE = "term";
	public final static String PRONUNCIATIONLABEL_VALUE = "pronunciation";
	public final static String SPELLINGLABEL_VALUE = "spelling";
	public final static String ETYMOLOGYLABEL_VALUE = "etymology";
	public final static String FUNCTIONLABEL_VALUE = "function";
	public final static String ENCYCLOPEDIA_ARTICLE_LABEL_VALUE = "encyclopediaArticle";
	public final static String DEFINITIONLABEL_VALUE = "definition";
	public final static String SUBDEFINITIONLABEL_VALUE = "subdefinition";
	public final static String MODELSENTENCELABEL_VALUE = "modelSentence";
	public final static String PASSAGELABEL_VALUE = "passage";
	public final static String TRANSLATIONLABEL_VALUE = "translationEquivalent";
	public final static String RELATEDTERMLABEL_VALUE = "relatedTerm";
	public final static String REGISTERLABEL_VALUE = "register";
	public final static String KEYWORDLABEL_VALUE = "keyword";
	public final static String PREFERENCESLABEL_VALUE = "preferences";
	public final static String ANALYTICALNOTELABEL_VALUE = "analyticalNote";
	/*
	 *  REQUEST PARAM NAMES AND VALUES USED BY LexActionServlet.java
	 */
	public final static String COMMAND_REQ_PARAM = "cmd";
	/*
	 *  REQUEST PARAM NAMES AND VALUES USED BY LoginServlet.java
	 */
	public final static String USERNAME_REQ_PARAM = "username";
	public final static String PASSWORD_REQ_PARAM = "password";

	/*
	 *  --------------------------
	 *  REQUEST PARAM NAMES USED BY LexComponent.scrapeRequest(req) METHODS
	 *  --------------------------
	 */
	//Meta Data Request Params
	public final static String NOTE_REQ_PARAM = "note";
	public final static String LANGUAGE_REQ_PARAM = "language";
	public final static String TRANSLATIONOF_REQ_PARAM = "translationOf";
	public final static String DIALECT_REQ_PARAM = "dialect";
	public final static String SOURCE_REQ_PARAM = "source";
	public final static String PROJSUB_REQ_PARAM = "projectSubject";
	public final static String SCRIPT_REQ_PARAM = "script";
	//Term Data Request Params
	public final static String TERM_REQ_PARAM = "term";
	public final static String TERMID_REQ_PARAM = "termId";
	// public static final String SPELLINGNOTE_REQ_PARAM = "spellingNote";
	public final static String PRECEDENCE_REQ_PARAM = "precedence";
	//Definition Data Request Params
	public final static String SUBDEFINITION_REQ_PARAM = "subdefinition";

//outgoing request attributes to jsp
	public final static String COMPONENT_REQ_ATTR = "component";
	public final static String ORIGINALBEAN_REQ_ATTR = "original";
	public final static String MESSAGE_REQ_ATTR = "message";

//session attributes used by filters, servlet, commands and jsp
	public final static String USER_SESS_ATTR = "user";
	public final static String PREFERENCES_SESS_ATTR = "preferences";
	public final static String LOGINTARGET_SESS_PARAM = "loginTarget";

	public final static String DISPLAYMODE_SESS_ATTR = "displayMode";
	public final static String QUERY_SESS_ATTR = "query";
	public final static String TERMENTRYBEAN_SESS_ATTR = "termEntry";

//used by Servlet
	public final static String JSP_DIR = "/jsp/";
	public final static String WELCOME_PAGE = "action?cmd=menu";
	public final static String ERROR_PAGE = "error.jsp";
//used by Repository
	public final static String DRIVER = "com.mysql.jdbc.Driver";

	public final static String HIBERNATE_SESSION_KEY = "hib";

	//public final static String URL = "jdbc:mysql://localhost/LexTorque";
}

