package org.thdl.lex.commands;

import java.security.MessageDigest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.thdl.lex.UserSessionManager;
import org.thdl.lex.Visit;

/**
 * Description of the Class
 * 
 * @author travis
 * @created October 22, 2003
 */
public class CommandToken {
	/**
	 * Description of the Method
	 * 
	 * @param req
	 *            Description of the Parameter
	 */
	public static void set(HttpServletRequest req) {
		HttpSession session = req.getSession(true);
		Visit visit = UserSessionManager.getInstance().getVisit(session);
		long systime = System.currentTimeMillis();
		byte[] time = new Long(systime).toString().getBytes();
		byte[] id = session.getId().getBytes();
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(id);
			md5.update(time);
			String token = toHex(md5.digest());
			req.setAttribute("token", token);
			visit.setToken(token);
		} catch (Exception e) {
			System.err
					.println("Unable to calculate MD5 Digests.\nCould not create unique token");
		}
	}

	/**
	 * Gets the valid attribute of the CommandToken class
	 * 
	 * @param req
	 *            Description of the Parameter
	 * @return The valid value
	 */
	public static boolean isValid(HttpServletRequest req) {
		boolean valid;
		HttpSession session = req.getSession(true);
		Visit visit = UserSessionManager.getInstance().getVisit(session);
		String requestToken = req.getParameter("token");
		String sessionToken = visit.getToken();
		if (requestToken == null && sessionToken == null) {
			valid = false;
		} else {
			valid = requestToken.equals(sessionToken);
		}
		return valid;
	}

	/**
	 * Description of the Method
	 * 
	 * @param digest
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static String toHex(byte[] digest) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < digest.length; i++) {
			buf.append(Integer.toHexString((int) digest[i] & 0x00ff));
		}//param=BITWISE operation
		return buf.toString();
	}
}

