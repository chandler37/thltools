package org.thdl.lex.component;


/**
 *  Description of the Interface
 *
 * @author     travis
 * @created    October 13, 2003
 */
public interface ISubdefinition extends Translatable
{
	/**
	 *  Gets the parent attribute of the ISubdefinition object
	 *
	 * @return    The parent value
	 */
	public ILexComponent getParent();


	/**
	 *  Sets the parent attribute of the ISubdefinition object
	 *
	 * @param  comp  The new parent value
	 */
	public void setParent( ILexComponent comp );


	/**
	 *  Gets the parentId attribute of the ISubdefinition object
	 *
	 * @return    The parentId value
	 */
	public java.lang.Integer getParentId();


	/**
	 *  Sets the parentId attribute of the ISubdefinition object
	 *
	 * @param  parentId  The new parentId value
	 */
	public void setParentId( java.lang.Integer parentId );



	/**
	 *  Gets the subdefinition attribute of the ISubdefinition object
	 *
	 * @return    The subdefinition value
	 */
	public java.lang.String getSubdefinition();


	/**
	 *  Sets the subdefinition attribute of the ISubdefinition object
	 *
	 * @param  subdefinition  The new subdefinition value
	 */
	public void setSubdefinition( java.lang.String subdefinition );


	/**
	 *  Gets the glosses attribute of the ISubdefinition object
	 *
	 * @return    The glosses value
	 */
	public java.util.List getGlosses();


	/**
	 *  Sets the glosses attribute of the ISubdefinition object
	 *
	 * @param  glosses  The new glosses value
	 */
	public void setGlosses( java.util.List glosses );


	/**
	 *  Gets the keywords attribute of the ISubdefinition object
	 *
	 * @return    The keywords value
	 */
	public java.util.List getKeywords();


	/**
	 *  Sets the keywords attribute of the ISubdefinition object
	 *
	 * @param  keywords  The new keywords value
	 */
	public void setKeywords( java.util.List keywords );


	/**
	 *  Gets the modelSentences attribute of the ISubdefinition object
	 *
	 * @return    The modelSentences value
	 */
	public java.util.List getModelSentences();


	/**
	 *  Sets the modelSentences attribute of the ISubdefinition object
	 *
	 * @param  modelSentences  The new modelSentences value
	 */
	public void setModelSentences( java.util.List modelSentences );


	/**
	 *  Gets the translationEquivalents attribute of the ISubdefinition object
	 *
	 * @return    The translationEquivalents value
	 */
	public java.util.List getTranslationEquivalents();


	/**
	 *  Sets the translationEquivalents attribute of the ISubdefinition object
	 *
	 * @param  translationEquivalents  The new translationEquivalents value
	 */
	public void setTranslationEquivalents( java.util.List translationEquivalents );


	/**
	 *  Gets the relatedTerms attribute of the ISubdefinition object
	 *
	 * @return    The relatedTerms value
	 */
	public java.util.List getRelatedTerms();


	/**
	 *  Sets the relatedTerms attribute of the ISubdefinition object
	 *
	 * @param  relatedTerms  The new relatedTerms value
	 */
	public void setRelatedTerms( java.util.List relatedTerms );


	/**
	 *  Gets the passages attribute of the ISubdefinition object
	 *
	 * @return    The passages value
	 */
	public java.util.List getPassages();


	/**
	 *  Sets the passages attribute of the ISubdefinition object
	 *
	 * @param  passages  The new passages value
	 */
	public void setPassages( java.util.List passages );


	/**
	 *  Gets the registers attribute of the ISubdefinition object
	 *
	 * @return    The registers value
	 */
	public java.util.List getRegisters();


	/**
	 *  Sets the registers attribute of the ISubdefinition object
	 *
	 * @param  registers  The new registers value
	 */
	public void setRegisters( java.util.List registers );


}

