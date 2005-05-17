package org.thdl.lex;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.thdl.lex.component.ILexComponent;
import org.thdl.lex.component.ITerm;

/**
 * Description of the Class
 * 
 * @author travis
 * @created October 1, 2003
 */
public class LexQuery {
	private ILexComponent queryComponent;

	private ILexComponent updateComponent;

	private ITerm entry;

	private Map results;

	private String findMode;

	private long duration;

	/**
	 * Sets the updateComponent attribute of the LexQuery object
	 * 
	 * @param updateComponent
	 *            The new updateComponent value
	 */
	public void setUpdateComponent(ILexComponent updateComponent) {
		this.updateComponent = updateComponent;
	}

	/**
	 * Sets the duration attribute of the LexQuery object
	 * 
	 * @param duration
	 *            The new duration value
	 * @since
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}

	/**
	 * Sets the queryComponent attribute of the LexQuery object
	 * 
	 * @param queryComponent
	 *            The new queryComponent value
	 * @since
	 */
	public void setQueryComponent(ILexComponent queryComponent) {
		this.queryComponent = queryComponent;
	}

	/**
	 * Sets the entry attribute of the LexQuery object
	 * 
	 * @param entry
	 *            The new entry value
	 * @since
	 */
	public void setEntry(ITerm entry) {
		this.entry = entry;
	}

	/**
	 * Sets the findMode attribute of the LexQuery object
	 * 
	 * @param findMode
	 *            The new findMode value
	 * @since
	 */
	public void setFindMode(String findMode) {
		this.findMode = findMode;
	}

	/**
	 * Sets the results attribute of the LexQuery object
	 * 
	 * @param results
	 *            The new results value
	 * @since
	 */
	public void setResults(Map results) {
		this.results = results;
	}

	/**
	 * Gets the updateComponent attribute of the LexQuery object
	 * 
	 * @return The updateComponent value
	 */
	public ILexComponent getUpdateComponent() {
		return updateComponent;
	}

	/**
	 * Gets the duration attribute of the LexQuery object
	 * 
	 * @return The duration value
	 * @since
	 */
	public long getDuration() {
		return duration / 1000;
	}

	/**
	 * Gets the queryComponent attribute of the LexQuery object
	 * 
	 * @return The queryComponent value
	 * @since
	 */
	public ILexComponent getQueryComponent() {
		return queryComponent;
	}

	/**
	 * Gets the entry attribute of the LexQuery object
	 * 
	 * @return The entry value
	 * @since
	 */
	public ITerm getEntry() {
		return entry;
	}

	/**
	 * Gets the findMode attribute of the LexQuery object
	 * 
	 * @return The findMode value
	 * @since
	 */
	public String getFindMode() {
		return findMode;
	}

	/**
	 * Gets the results attribute of the LexQuery object
	 * 
	 * @return The results value
	 * @since
	 */
	public Map getResults() {
		if (null == results) {
			setResults(new HashMap());
		}
		return results;
	}

	//helper methods
	/**
	 * Description of the Method
	 * 
	 * @param parameters
	 *            Description of Parameter
	 * @exception LexRepositoryException
	 *                Description of Exception
	 * @since
	 */
	public void populate(Map parameters) throws LexRepositoryException {
		try {
			BeanUtils.populate(this, parameters);
		} catch (IllegalAccessException iae) {
			throw new LexRepositoryException(iae);
		} catch (java.lang.reflect.InvocationTargetException ite) {
			throw new LexRepositoryException(ite);
		}
	}

	//constructors

	/**
	 * Constructor for the LexQuery object
	 * 
	 * @param findMode
	 *            Description of the Parameter
	 * @since
	 */
	public LexQuery(String findMode) {
		setFindMode(findMode);
	}

	/**
	 * Constructor for the LexQuery object
	 */
	public LexQuery() {
		this(LexComponentRepository.STARTS_WITH);
	}

	/**
	 * Constructor for the LexQuery object
	 * 
	 * @param component
	 *            Description of the Parameter
	 */
	public LexQuery(ILexComponent component) {
		this();
		setQueryComponent(component);
	}

	/**
	 * Constructor for the LexQuery object
	 * 
	 * @param component
	 *            Description of the Parameter
	 * @param findMode
	 *            Description of the Parameter
	 */
	public LexQuery(ILexComponent component, String findMode) {
		this(findMode);
		setQueryComponent(component);
	}

}

