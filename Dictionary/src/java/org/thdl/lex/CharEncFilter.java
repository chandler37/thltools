package org.thdl.lex;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description of the Class
 * 
 * @author travis
 * @created October 21, 2003
 */
public class CharEncFilter implements Filter {
	/**
	 * Description of the Method
	 * 
	 * @param config
	 *            Description of the Parameter
	 * @exception ServletException
	 *                Description of the Exception
	 */
	public void init(FilterConfig config) throws ServletException {
	}

	/**
	 * Description of the Method
	 * 
	 * @param request
	 *            Description of the Parameter
	 * @param response
	 *            Description of the Parameter
	 * @param chain
	 *            Description of the Parameter
	 * @exception IOException
	 *                Description of the Exception
	 * @exception ServletException
	 *                Description of the Exception
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest
				&& response instanceof HttpServletResponse) {
			HttpServletResponse res = (HttpServletResponse) response;
			HttpServletRequest req = (HttpServletRequest) request;
			res.setContentType("text/html; charset=UTF-8;");
			req.setCharacterEncoding("UTF-8");

			chain.doFilter(request, response);
		} else {
			throw new ServletException(
					"Filter only applicable to HTTP and HTTPS requests");
		}
	}

	/**
	 * Description of the Method
	 */
	public void destroy() {
	}
	//helper methods

}

