package org.thdl.lex;

/**
 * Description of the Class
 * 
 * @author travis
 * @created October 1, 2003
 */
public class LexRepositoryException extends Exception {
	/**
	 * Constructor for the LexRepositoryException object
	 * 
	 * @since
	 */
	public LexRepositoryException() {
		super();
	}

	/**
	 * Constructor for the LexRepositoryException object
	 * 
	 * @param msg
	 *            Description of Parameter
	 * @since
	 */
	public LexRepositoryException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for the LexRepositoryException object
	 * 
	 * @param e
	 *            Description of Parameter
	 * @since
	 */
	public LexRepositoryException(Exception e) {
		super(e);
	}

	/**
	 * Constructor for the LexRepositoryException object
	 * 
	 * @param msg
	 *            Description of Parameter
	 * @param e
	 *            Description of Parameter
	 * @since
	 */
	public LexRepositoryException(String msg, Exception e) {
		super(msg, e);
	}
}

