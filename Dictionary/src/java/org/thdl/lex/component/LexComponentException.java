package org.thdl.lex.component;

/**
 * Description of the Class
 * 
 * @author travis
 * @created October 1, 2003
 */
public class LexComponentException extends Exception {
	/**
	 * Constructor for the LexComponentException object
	 * 
	 * @since
	 */
	public LexComponentException() {
		super();
	}

	/**
	 * Constructor for the LexComponentException object
	 * 
	 * @param msg
	 *            Description of Parameter
	 * @since
	 */
	public LexComponentException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for the LexComponentException object
	 * 
	 * @param e
	 *            Description of Parameter
	 * @since
	 */
	public LexComponentException(Exception e) {
		super(e);
	}

	/**
	 * Constructor for the LexComponentException object
	 * 
	 * @param msg
	 *            Description of Parameter
	 * @param e
	 *            Description of Parameter
	 * @since
	 */
	public LexComponentException(String msg, Exception e) {
		super(msg, e);
	}
}

