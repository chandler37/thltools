package org.thdl.lex;

import org.thdl.lex.component.*;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 1, 2003
 */
public class LexConstants
{
	/*
	    REQUEST PARAM/ATTR NAMES AND VALUES USED BY LexComponentFilter.java
	  */
//form field req params
	/**
	 *  Description of the Field
	 */
	public final static String LABEL_REQ_PARAM = "comp";
	/**
	 *  Description of the Field
	 */
	public final static String TERMLABEL_VALUE = "term";
	/**
	 *  Description of the Field
	 */
	public final static String PRONUNCIATIONLABEL_VALUE = new Pronunciation().getLabel();
	/**
	 *  Description of the Field
	 */
	public final static String SPELLINGLABEL_VALUE = new Spelling().getLabel();
	/**
	 *  Description of the Field
	 */
	public final static String ETYMOLOGYLABEL_VALUE = new Etymology().getLabel();
	/**
	 *  Description of the Field
	 */
	public final static String FUNCTIONLABEL_VALUE = new GrammaticalFunction().getLabel();
	/**
	 *  Description of the Field
	 */
	public final static String ENCYCLOPEDIA_ARTICLE_LABEL_VALUE = new EncyclopediaArticle().getLabel();
	/**
	 *  Description of the Field
	 */
	public final static String DEFINITIONLABEL_VALUE = new Definition().getLabel();
	/**
	 *  Description of the Field
	 */
	public final static String SUBDEFINITIONLABEL_VALUE = new Subdefinition().getLabel();
	/**
	 *  Description of the Field
	 */
	public final static String MODELSENTENCELABEL_VALUE = new ModelSentence().getLabel();
	/**
	 *  Description of the Field
	 */
	public final static String PASSAGELABEL_VALUE = new Passage().getLabel();
	/**
	 *  Description of the Field
	 */
	public final static String TRANSLATIONLABEL_VALUE = new TranslationEquivalent().getLabel();
	/**
	 *  Description of the Field
	 */
	public final static String RELATEDTERMLABEL_VALUE = new RelatedTerm().getLabel();
	/**
	 *  Description of the Field
	 */
	public final static String REGISTERLABEL_VALUE = new SpeechRegister().getLabel();
	/**
	 *  Description of the Field
	 */
	public final static String KEYWORDLABEL_VALUE = new Keyword().getLabel();
	/**
	 *  Description of the Field
	 */
	public final static String PREFERENCESLABEL_VALUE = "preferences";
	/**
	 *  Description of the Field
	 */
	public final static String ANALYTICALNOTELABEL_VALUE = new AnalyticalNote().getLabel();
	/**
	 *  Description of the Field
	 */
	public final static String TRANSITIONALDATALABEL_VALUE = new TransitionalData().getLabel();
	/**
	 *  Description of the Field
	 */
	public final static String GLOSSLABEL_VALUE = new Gloss().getLabel();

	/*
	    REQUEST PARAM NAMES AND VALUES USED BY LexActionServlet.java
	  */
	/**
	 *  Description of the Field
	 */
	public final static String COMMAND_REQ_PARAM = "cmd";
	/*
	    REQUEST PARAM NAMES AND VALUES USED BY LoginServlet.java
	  */
	/**
	 *  Description of the Field
	 */
	public final static String USERNAME_REQ_PARAM = "username";
	/**
	 *  Description of the Field
	 */
	public final static String PASSWORD_REQ_PARAM = "password";

	/*
	    --------------------------
	    REQUEST PARAM NAMES USED BY LexComponent.scrapeRequest(req) METHODS
	    --------------------------
	  */
//Meta Data Request Params
	/**
	 *  Description of the Field
	 */
	public final static String NOTE_REQ_PARAM = "note";
	/**
	 *  Description of the Field
	 */
	public final static String LANGUAGE_REQ_PARAM = "language";
	/**
	 *  Description of the Field
	 */
	public final static String TRANSLATIONOF_REQ_PARAM = "translationOf";
	/**
	 *  Description of the Field
	 */
	public final static String DIALECT_REQ_PARAM = "dialect";
	/**
	 *  Description of the Field
	 */
	public final static String SOURCE_REQ_PARAM = "source";
	/**
	 *  Description of the Field
	 */
	public final static String PROJSUB_REQ_PARAM = "projectSubject";
	/**
	 *  Description of the Field
	 */
	public final static String SCRIPT_REQ_PARAM = "script";
//Term Data Request Params
	/**
	 *  Description of the Field
	 */
	public final static String TERM_REQ_PARAM = "term";
	/**
	 *  Description of the Field
	 */
	public final static String TERMID_REQ_PARAM = "termId";
// public static final String SPELLINGNOTE_REQ_PARAM = "spellingNote";
	/**
	 *  Description of the Field
	 */
	public final static String PRECEDENCE_REQ_PARAM = "precedence";
//Definition Data Request Params
	/**
	 *  Description of the Field
	 */
	public final static String SUBDEFINITION_REQ_PARAM = "subdefinition";

//outgoing request attributes to jsp
	/**
	 *  Description of the Field
	 */
	public final static String COMPONENT_REQ_ATTR = "component";
	/**
	 *  Description of the Field
	 */
	public final static String ORIGINALBEAN_REQ_ATTR = "original";
	/**
	 *  Description of the Field
	 */
	public final static String MESSAGE_REQ_ATTR = "message";

//session attributes used by filters, servlet, commands and jsp
	/**
	 *  Description of the Field
	 */
	public final static String USER_SESS_ATTR = "user";
	/**
	 *  Description of the Field
	 */
	public final static String PREFERENCES_SESS_ATTR = "preferences";
	/**
	 *  Description of the Field
	 */
	public final static String LOGINTARGET_SESS_PARAM = "loginTarget";

	/**
	 *  Description of the Field
	 */
	public final static String DISPLAYMODE_SESS_ATTR = "displayMode";
	/**
	 *  Description of the Field
	 */
	public final static String QUERY_SESS_ATTR = "query";
	/**
	 *  Description of the Field
	 */
	public final static String TERMENTRYBEAN_SESS_ATTR = "termEntry";

//used by Servlet
	/**
	 *  Description of the Field
	 */
	public final static String JSP_DIR = "/jsp/";
	/**
	 *  Description of the Field
	 */
	public final static String WELCOME_PAGE = "action?cmd=menu";
	/**
	 *  Description of the Field
	 */
	public final static String ERROR_PAGE = "error.jsp";
//used by Repository
	/**
	 *  Description of the Field
	 */
	public final static String DRIVER = "com.mysql.jdbc.Driver";
	/**
	 *  Description of the Field
	 */
	public final static String DATASOURCE_NAME = "java:comp/env/jdbc/lex-datasource";

	/**
	 *  Description of the Field
	 */
	public final static String HIBERNATE_SESSION_KEY = "hib";

//public final static String URL = "jdbc:mysql://localhost/LexTorque";

}

