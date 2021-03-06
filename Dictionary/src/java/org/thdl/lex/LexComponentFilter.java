package org.thdl.lex;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thdl.lex.component.AnalyticalNote;
import org.thdl.lex.component.Definition;
import org.thdl.lex.component.EncyclopediaArticle;
import org.thdl.lex.component.Etymology;
import org.thdl.lex.component.GrammaticalFunction;
import org.thdl.lex.component.ITerm;
import org.thdl.lex.component.Keyword;
import org.thdl.lex.component.LexComponent;
import org.thdl.lex.component.LexComponentException;
import org.thdl.lex.component.ModelSentence;
import org.thdl.lex.component.Passage;
import org.thdl.lex.component.Pronunciation;
import org.thdl.lex.component.RelatedTerm;
import org.thdl.lex.component.SpeechRegister;
import org.thdl.lex.component.Spelling;
import org.thdl.lex.component.Subdefinition;
import org.thdl.lex.component.Term;
import org.thdl.lex.component.TransitionalData;
import org.thdl.lex.component.TranslationEquivalent;

/**
 * Description of the Class
 * 
 * @author travis
 * @created October 1, 2003
 */
public class LexComponentFilter implements Filter {
	//attributes
	private HashMap blanks;

	private UserSessionManager sessionMgr;

	//accessors
	/**
	 * Sets the blanks attribute of the LexComponentFilter object
	 * 
	 * @param blanks
	 *            The new blanks value
	 * @since
	 */
	public void setBlanks(HashMap blanks) {
		this.blanks = blanks;
	}

	/**
	 * Sets the sessionMgr attribute of the LexComponentFilter object
	 * 
	 * @param sessionMgr
	 *            The new sessionMgr value
	 * @since
	 */
	public void setSessionManager(UserSessionManager sessionMgr) {
		this.sessionMgr = sessionMgr;
	}

	/**
	 * Gets the blanks attribute of the LexComponentFilter object
	 * 
	 * @return The blanks value
	 * @since
	 */
	public HashMap getBlanks() {
		return blanks;
	}

	/**
	 * Gets the sessionMgr attribute of the LexComponentFilter object
	 * 
	 * @return The sessionMgr value
	 * @since
	 */
	public UserSessionManager getSessionManager() {
		if (null == sessionMgr) {
			setSessionManager(UserSessionManager.getInstance());
		}
		return sessionMgr;
	}

	//contract methods

	/**
	 * Description of the Method
	 * 
	 * @param config
	 *            Description of Parameter
	 * @exception ServletException
	 *                Description of Exception
	 * @since
	 */
	public void init(FilterConfig config) throws ServletException {
		try {
			setBlanks(new HashMap());
			ITerm term = new Term();
			getBlanks().put(LexConstants.TERMLABEL_VALUE, term);
			getBlanks().put(LexConstants.PRONUNCIATIONLABEL_VALUE,
					new Pronunciation());
			getBlanks().put(LexConstants.ETYMOLOGYLABEL_VALUE, new Etymology());
			getBlanks().put(LexConstants.FUNCTIONLABEL_VALUE,
					new GrammaticalFunction());
			getBlanks().put(LexConstants.SPELLINGLABEL_VALUE, new Spelling());
			getBlanks().put(LexConstants.ENCYCLOPEDIA_ARTICLE_LABEL_VALUE,
					new EncyclopediaArticle());
			getBlanks().put(LexConstants.DEFINITIONLABEL_VALUE,
					new Definition());
			getBlanks().put(LexConstants.PASSAGELABEL_VALUE, new Passage());
			getBlanks().put(LexConstants.SUBDEFINITIONLABEL_VALUE,
					new Subdefinition());
			getBlanks().put(LexConstants.TRANSLATIONLABEL_VALUE,
					new TranslationEquivalent());
			getBlanks().put(LexConstants.KEYWORDLABEL_VALUE, new Keyword());
			getBlanks().put(LexConstants.RELATEDTERMLABEL_VALUE,
					new RelatedTerm());
			getBlanks().put(LexConstants.MODELSENTENCELABEL_VALUE,
					new ModelSentence());
			getBlanks().put(LexConstants.REGISTERLABEL_VALUE,
					new SpeechRegister());
			getBlanks().put("analyticalNote", new AnalyticalNote());
			getBlanks().put("transitionalData", new TransitionalData());
			// getBlanks().put( LexConstants.INPUTSESSIONLABEL_VALUE, new
			// Preferences() );
		}
		// catch (LexComponentException labe)
		catch (Exception labe) {
			throw new ServletException(labe);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param request
	 *            Description of Parameter
	 * @param response
	 *            Description of Parameter
	 * @param chain
	 *            Description of Parameter
	 * @exception IOException
	 *                Description of Exception
	 * @exception ServletException
	 *                Description of Exception
	 * @since
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		long start = System.currentTimeMillis();
		if (request instanceof HttpServletRequest
				&& response instanceof HttpServletResponse) {
			HttpServletResponse res = (HttpServletResponse) response;
			HttpServletRequest req = (HttpServletRequest) request;
			res.setContentType("text/html; charset=UTF-8;");
			req.setCharacterEncoding("UTF-8");

			if (null != req.getParameter(LexConstants.LABEL_REQ_PARAM)) {
				String labelValue = req
						.getParameter(LexConstants.LABEL_REQ_PARAM);
				try {
					if (getBlanks().get(labelValue) != null) {
						Class c = getBlanks().get(labelValue).getClass();
						LexComponent component = (LexComponent) c.newInstance();
						component.populate(req.getParameterMap());
						component.getMeta().populate(req.getParameterMap());
						req.setAttribute(LexConstants.COMPONENT_REQ_ATTR,
								component);
					} else {
						LexLogger
								.error("componentLabel was not in blank components");
					}
				} catch (InstantiationException ie) {
					throw new ServletException(ie);
				} catch (IllegalAccessException iae) {
					throw new ServletException(iae);
				} catch (LexComponentException lce) {
					throw new ServletException(lce);
				}
			} else {
				LexLogger
						.error("Required parameter, '"
								+ LexConstants.LABEL_REQ_PARAM
								+ "' was not specified.");
			}
			chain.doFilter(request, response);

			HttpServletResponse resp = (HttpServletResponse) response;
			resp.setContentType("text/html; charset=UTF-8;");
			LexLogger.logResponseState(resp);
			try {
				LexComponentRepository.cleanup();
			} catch (LexRepositoryException lre) {
				throw new ServletException(lre);
			}
			/*
			 * LexLogger.debug( "Checking Request state at end of
			 * LexComponentFilter.doFilter()" ); LexLogger.logRequestState( req );
			 * LexLogger.logSessionState( req );
			 */
			long dur = System.currentTimeMillis() - start;
			LexLogger.debug("Total Request took: " + dur / 1000
					+ " seconds.\n\n");
		} else {
			throw new ServletException(
					"Filter only applicable to HTTP and HTTPS requests");
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @since
	 */
	public void destroy() {
	}

	//helper methods

	//constructors
	/**
	 * Constructor for the LexComponentFilter object
	 * 
	 * @since
	 */
	public LexComponentFilter() {
	}
}

