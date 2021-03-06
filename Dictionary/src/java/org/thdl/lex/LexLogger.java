package org.thdl.lex;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.thdl.lex.component.ILexComponent;
import org.thdl.lex.component.ITerm;

/**
 * Description of the Class
 * 
 * @author travis
 * @created October 9, 2003
 */
public class LexLogger {
	private final static Logger LOGGER = Logger.getLogger("org.thdl.lex");

	private final static List EXCLUDED_PARAMS = Arrays.asList(new String[] {
			"org.apache.catalina.jsp_classpath",
			"javax.servlet.context.tempdir",
			"org.apache.catalina.WELCOME_FILES" });

	/**
	 * Description of the Method
	 * 
	 * @param req
	 *            Description of the Parameter
	 */
	public static void logRequestState(HttpServletRequest req) {

		Iterator it;
		LOGGER.debug("authType: " + req.getAuthType());
		LOGGER.debug("characterEncoding: " + req.getCharacterEncoding());
		LOGGER.debug("contentLength: " + req.getContentLength());
		LOGGER.debug("contentType: " + req.getContentType());
		LOGGER.debug("method: " + req.getMethod());
		LOGGER.debug("pathInfo: " + req.getPathInfo());
		LOGGER.debug("pathTranslated: " + req.getPathTranslated());
		LOGGER.debug("protocol: " + req.getProtocol());
		LOGGER.debug("queryString: " + req.getQueryString());
		LOGGER.debug("remoteAddr: " + req.getRemoteAddr());
		LOGGER.debug("remoteHost: " + req.getRemoteHost());
		LOGGER.debug("remoteUser: " + req.getRemoteUser());
		LOGGER.debug("requestedSessionId: " + req.getRequestedSessionId());
		LOGGER.debug("requestedSessionIdFromCookie: "
				+ req.isRequestedSessionIdFromCookie());
		LOGGER.debug("requestedSessionIdFromURL: "
				+ req.isRequestedSessionIdFromURL());
		LOGGER.debug("requestedSessionIdValid: "
				+ req.isRequestedSessionIdValid());
		LOGGER.debug("requestURI: " + req.getRequestURI());
		LOGGER.debug("scheme: " + req.getScheme());
		LOGGER.debug("serverName: " + req.getServerName());
		LOGGER.debug("serverPort: " + req.getServerPort());
		LOGGER.debug("contextPath: " + req.getContextPath());
		LOGGER.debug("servletPath: " + req.getServletPath());
		Enumeration enumeration = req.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String parm = (String) enumeration.nextElement();
			LOGGER.debug("Request Parameter: " + parm + " = '"
					+ req.getParameter(parm) + "'");
		}
		enumeration = req.getAttributeNames();
		while (enumeration.hasMoreElements()) {
			String att = (String) enumeration.nextElement();
			LOGGER.debug("Request Attribute: " + att + " =: "
					+ req.getAttribute(att));
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param resp
	 *            Description of the Parameter
	 */
	public static void logResponseState(HttpServletResponse resp) {
		LOGGER.debug("RESPONSE STATE");
		LOGGER.debug("characterEncoding: " + resp.getCharacterEncoding());
	}

	/**
	 * Description of the Method
	 * 
	 * @param req
	 *            Description of the Parameter
	 */
	public static void logSessionState(HttpServletRequest req) {
		HttpSession ses = req.getSession(false);
		if (null == ses) {
			LOGGER.debug("Session was null");
			return;
		}
		Visit visit = UserSessionManager.getInstance().getVisit(
				req.getSession(true));
		Enumeration enumeration = ses.getAttributeNames();
		while (enumeration.hasMoreElements()) {
			String att = (String) enumeration.nextElement();
			LOGGER.debug("Session Attribute: " + att + " =: "
					+ ses.getAttribute(att));
		}

		if (null == visit) {
			LOGGER.debug("Visit was null");
			return;
		}
		debugComponent(visit);
		debugComponent(visit.getQuery());
		debugComponent(visit.getUser());
	}

	/**
	 * Description of the Method
	 * 
	 * @param context
	 *            Description of the Parameter
	 */
	public static void logContextState(ServletContext context) {
		Enumeration enumeration = context.getAttributeNames();
		while (enumeration.hasMoreElements()) {
			String att = (String) enumeration.nextElement();
			if (!EXCLUDED_PARAMS.contains(att)) {
				LOGGER.debug("Context Attribute: " + att + " =: "
						+ context.getAttribute(att));
			}
		}
		debugComponent(context.getAttribute(LexConstants.GLOBAL_CONTEXT_ATTR));
	}

	/**
	 * Description of the Method
	 * 
	 * @param msg
	 *            Description of the Parameter
	 */
	public static void info(String msg) {
		LOGGER.info(msg);
	}

	/**
	 * Description of the Method
	 * 
	 * @param msg
	 *            Description of the Parameter
	 */
	public static void debug(String msg) {
		LOGGER.debug(msg);
	}

	/**
	 * Description of the Method
	 * 
	 * @param msg
	 *            Description of the Parameter
	 */
	public static void warn(String msg) {
		LOGGER.warn(msg);
	}

	/**
	 * Description of the Method
	 * 
	 * @param msg
	 *            Description of the Parameter
	 */
	public static void error(String msg) {
		LOGGER.error(msg);
	}

	/**
	 * Constructor for the debugComponent object
	 * 
	 * @param component
	 *            Description of the Parameter
	 */
	public static void debugComponent(Object component) {
		if (null == component) {
			debug("debugComponent was just handed a null component");
			return;
		}
		try {
			LOGGER.debug("Describing: " + component);
			String label = component instanceof ILexComponent ? ((ILexComponent) component)
					.getLabel()
					: component.toString();
			Iterator it = BeanUtils.describe(component).entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				LOGGER.debug(label + " property: " + entry.getKey() + " = '"
						+ entry.getValue() + "'");
			}
		} catch (Exception e) {
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));
			String stackTrace = writer.getBuffer().toString();
			LOGGER.debug("LexLogger caught an Exception: " + stackTrace);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param term
	 *            Description of the Parameter
	 */
	public static void debugTerm(ITerm term) {
		debugComponent(term);
	}

}

