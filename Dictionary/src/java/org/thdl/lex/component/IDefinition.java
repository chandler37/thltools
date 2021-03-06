package org.thdl.lex.component;

/**
 * Description of the Interface
 * 
 * @author travis
 * @created October 1, 2003
 */
public interface IDefinition extends LexComponentNode {
	/**
	 * Gets the parent attribute of the IDefinition object
	 * 
	 * @return The parentId value
	 */

	/**
	 * Sets the parent attribute of the IDefinition object
	 * 
	 * @return The parentId value
	 */

	/**
	 * Gets the parentId attribute of the IDefinition object
	 * 
	 * @return The parentId value
	 */
	public java.lang.Integer getParentId();

	/**
	 * Sets the parentId attribute of the IDefinition object
	 * 
	 * @param parentId
	 *            The new parentId value
	 */
	public void setParentId(java.lang.Integer parentId);

	/**
	 * Gets the definition attribute of the IDefinition object
	 * 
	 * @return The definition value
	 * @since
	 */
	public java.lang.String getDefinition();

	/**
	 * Sets the definition attribute of the IDefinition object
	 * 
	 * @param definition
	 *            The new definition value
	 * @since
	 */
	public void setDefinition(java.lang.String definition);

	/**
	 * Gets the subdefinitions attribute of the IDefinition object
	 * 
	 * @return The subdefinitions value
	 * @since
	 */
	public java.util.List getSubdefinitions();

	/**
	 * Sets the subdefinitions attribute of the IDefinition object
	 * 
	 * @param subdefinitions
	 *            The new subdefinitions value
	 * @since
	 */
	public void setSubdefinitions(java.util.List subdefinitions);

	/**
	 * Gets the glosses attribute of the IDefinition object
	 * 
	 * @return The glosses value
	 * @since
	 */
	public java.util.List getGlosses();

	/**
	 * Sets the glosses attribute of the IDefinition object
	 * 
	 * @param glosses
	 *            The new glosses value
	 * @since
	 */
	public void setGlosses(java.util.List glosses);

	/**
	 * Gets the keywords attribute of the IDefinition object
	 * 
	 * @return The keywords value
	 * @since
	 */
	public java.util.List getKeywords();

	/**
	 * Sets the keywords attribute of the IDefinition object
	 * 
	 * @param keywords
	 *            The new keywords value
	 * @since
	 */
	public void setKeywords(java.util.List keywords);

	/**
	 * Gets the modelSentences attribute of the IDefinition object
	 * 
	 * @return The modelSentences value
	 * @since
	 */
	public java.util.List getModelSentences();

	/**
	 * Sets the modelSentences attribute of the IDefinition object
	 * 
	 * @param modelSentences
	 *            The new modelSentences value
	 * @since
	 */
	public void setModelSentences(java.util.List modelSentences);

	/**
	 * Gets the translationEquivalents attribute of the IDefinition object
	 * 
	 * @return The translationEquivalents value
	 * @since
	 */
	public java.util.List getTranslationEquivalents();

	/**
	 * Sets the translationEquivalents attribute of the IDefinition object
	 * 
	 * @param translationEquivalents
	 *            The new translationEquivalents value
	 * @since
	 */
	public void setTranslationEquivalents(java.util.List translationEquivalents);

	/**
	 * Gets the relatedTerms attribute of the IDefinition object
	 * 
	 * @return The relatedTerms value
	 * @since
	 */
	public java.util.List getRelatedTerms();

	/**
	 * Sets the relatedTerms attribute of the IDefinition object
	 * 
	 * @param relatedTerms
	 *            The new relatedTerms value
	 * @since
	 */
	public void setRelatedTerms(java.util.List relatedTerms);

	/**
	 * Gets the passages attribute of the IDefinition object
	 * 
	 * @return The passages value
	 * @since
	 */
	public java.util.List getPassages();

	/**
	 * Sets the passages attribute of the IDefinition object
	 * 
	 * @param passages
	 *            The new passages value
	 * @since
	 */
	public void setPassages(java.util.List passages);

	/**
	 * Gets the registers attribute of the IDefinition object
	 * 
	 * @return The registers value
	 * @since
	 */
	public java.util.List getRegisters();

	/**
	 * Sets the registers attribute of the IDefinition object
	 * 
	 * @param registers
	 *            The new registers value
	 * @since
	 */
	public void setRegisters(java.util.List registers);

	/**
	 * Description of the Method
	 * 
	 * @param component
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception LexComponentException
	 *                Description of the Exception
	 */
	public ILexComponent findChild(ILexComponent component)
			throws LexComponentException;

	/**
	 * Description of the Method
	 * 
	 * @param list
	 *            Description of the Parameter
	 * @param pk
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception LexComponentException
	 *                Description of the Exception
	 */
	public ILexComponent findChild(java.util.List list, Integer pk)
			throws LexComponentException;

	/**
	 * Description of the Method
	 * 
	 * @param pk
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception LexComponentException
	 *                Description of the Exception
	 */
	public ILexComponent findChild(Integer pk) throws LexComponentException;

	/**
	 * Description of the Method
	 * 
	 * @param parentPk
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception LexComponentException
	 *                Description of the Exception
	 */
	public ILexComponent findParent(Integer parentPk)
			throws LexComponentException;
}

