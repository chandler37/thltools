package org.thdl.lex.component;


/**
 *  Description of the Interface
 *
 * @author     travis
 * @created    October 1, 2003
 */
public interface ITerm extends LexComponentNode
{
	/**
	 *  Gets the term attribute of the ITerm object
	 *
	 * @return    The term value
	 * @since
	 */
	public java.lang.String getTerm();


	/**
	 *  Sets the term attribute of the ITerm object
	 *
	 * @param  term  The new term value
	 * @since
	 */
	public void setTerm( java.lang.String term );


	/**
	 *  Gets the precedence attribute of the ITerm object
	 *
	 * @return    The precedence value
	 * @since
	 */
	public java.lang.Integer getPrecedence();


	/**
	 *  Sets the precedence attribute of the ITerm object
	 *
	 * @param  precedence  The new precedence value
	 * @since
	 */
	public void setPrecedence( java.lang.Integer precedence );


	/**
	 *  Gets the pronunciations attribute of the ITerm object
	 *
	 * @return    The pronunciations value
	 * @since
	 */
	public java.util.List getPronunciations();


	/**
	 *  Sets the pronunciations attribute of the ITerm object
	 *
	 * @param  pronunciations  The new pronunciations value
	 * @since
	 */
	public void setPronunciations( java.util.List pronunciations );


	/**
	 *  Gets the etymologies attribute of the ITerm object
	 *
	 * @return    The etymologies value
	 * @since
	 */
	public java.util.List getEtymologies();


	/**
	 *  Sets the etymologies attribute of the ITerm object
	 *
	 * @param  etymologies  The new etymologies value
	 * @since
	 */
	public void setEtymologies( java.util.List etymologies );


	/**
	 *  Gets the spellings attribute of the ITerm object
	 *
	 * @return    The spellings value
	 * @since
	 */
	public java.util.List getSpellings();


	/**
	 *  Sets the spellings attribute of the ITerm object
	 *
	 * @param  spellings  The new spellings value
	 * @since
	 */
	public void setSpellings( java.util.List spellings );


	/**
	 *  Gets the functions attribute of the ITerm object
	 *
	 * @return    The functions value
	 * @since
	 */
	public java.util.List getFunctions();


	/**
	 *  Sets the functions attribute of the ITerm object
	 *
	 * @param  functions  The new functions value
	 * @since
	 */
	public void setFunctions( java.util.List functions );


	/**
	 *  Gets the encyclopediaArticles attribute of the ITerm object
	 *
	 * @return    The encyclopediaArticles value
	 * @since
	 */
	public java.util.List getEncyclopediaArticles();


	/**
	 *  Sets the encyclopediaArticles attribute of the ITerm object
	 *
	 * @param  encyclopediaArticles  The new encyclopediaArticles value
	 * @since
	 */
	public void setEncyclopediaArticles( java.util.List encyclopediaArticles );


	/**
	 *  Gets the transitionalData attribute of the ITerm object
	 *
	 * @return    The transitionalData value
	 * @since
	 */
	public java.util.List getTransitionalData();


	/**
	 *  Sets the transitionalData attribute of the ITerm object
	 *
	 * @param  transitionalData  The new transitionalData value
	 * @since
	 */
	public void setTransitionalData( java.util.List transitionalData );


	/**
	 *  Gets the definitions attribute of the ITerm object
	 *
	 * @return    The definitions value
	 * @since
	 */
	public java.util.List getDefinitions();


	/**
	 *  Sets the definitions attribute of the ITerm object
	 *
	 * @param  definitions  The new definitions value
	 * @since
	 */
	public void setDefinitions( java.util.List definitions );


	/**
	 *  Gets the glosses attribute of the ITerm object
	 *
	 * @return    The glosses value
	 * @since
	 */
	public java.util.List getGlosses();


	/**
	 *  Sets the glosses attribute of the ITerm object
	 *
	 * @param  glosses  The new glosses value
	 * @since
	 */
	public void setGlosses( java.util.List glosses );


	/**
	 *  Gets the keywords attribute of the ITerm object
	 *
	 * @return    The keywords value
	 * @since
	 */
	public java.util.List getKeywords();


	/**
	 *  Sets the keywords attribute of the ITerm object
	 *
	 * @param  keywords  The new keywords value
	 * @since
	 */
	public void setKeywords( java.util.List keywords );


	/**
	 *  Gets the modelSentences attribute of the ITerm object
	 *
	 * @return    The modelSentences value
	 * @since
	 */
	public java.util.List getModelSentences();


	/**
	 *  Sets the modelSentences attribute of the ITerm object
	 *
	 * @param  modelSentences  The new modelSentences value
	 * @since
	 */
	public void setModelSentences( java.util.List modelSentences );


	/**
	 *  Gets the translationEquivalents attribute of the ITerm object
	 *
	 * @return    The translationEquivalents value
	 * @since
	 */
	public java.util.List getTranslationEquivalents();


	/**
	 *  Sets the translationEquivalents attribute of the ITerm object
	 *
	 * @param  translationEquivalents  The new translationEquivalents value
	 * @since
	 */
	public void setTranslationEquivalents( java.util.List translationEquivalents );


	/**
	 *  Gets the relatedTerms attribute of the ITerm object
	 *
	 * @return    The relatedTerms value
	 * @since
	 */
	public java.util.List getRelatedTerms();


	/**
	 *  Sets the relatedTerms attribute of the ITerm object
	 *
	 * @param  relatedTerms  The new relatedTerms value
	 * @since
	 */
	public void setRelatedTerms( java.util.List relatedTerms );


	/**
	 *  Gets the passages attribute of the ITerm object
	 *
	 * @return    The passages value
	 * @since
	 */
	public java.util.List getPassages();


	/**
	 *  Sets the passages attribute of the ITerm object
	 *
	 * @param  passages  The new passages value
	 * @since
	 */
	public void setPassages( java.util.List passages );


	/**
	 *  Gets the registers attribute of the ITerm object
	 *
	 * @return    The registers value
	 * @since
	 */
	public java.util.List getRegisters();


	/**
	 *  Sets the registers attribute of the ITerm object
	 *
	 * @param  registers  The new registers value
	 * @since
	 */
	public void setRegisters( java.util.List registers );


	/**
	 *  Description of the Method
	 *
	 * @param  component                  Description of the Parameter
	 * @return                            Description of the Return Value
	 * @exception  LexComponentException  Description of the Exception
	 */
	public ILexComponent findChild( ILexComponent component ) throws LexComponentException;


	/**
	 *  Description of the Method
	 *
	 * @param  list                       Description of the Parameter
	 * @param  pk                         Description of the Parameter
	 * @return                            Description of the Return Value
	 * @exception  LexComponentException  Description of the Exception
	 */
	public ILexComponent findChild( java.util.List list, Integer pk ) throws LexComponentException;


	/**
	 *  Description of the Method
	 *
	 * @param  pk                         Description of the Parameter
	 * @return                            Description of the Return Value
	 * @exception  LexComponentException  Description of the Exception
	 */
	public ILexComponent findChild( Integer pk ) throws LexComponentException;


	/**
	 *  Description of the Method
	 *
	 * @param  parentPk                   Description of the Parameter
	 * @return                            Description of the Return Value
	 * @exception  LexComponentException  Description of the Exception
	 */
	public ILexComponent findParent( Integer parentPk ) throws LexComponentException;


	/**
	 *  Adds a feature to the Child attribute of the ITerm object
	 *
	 * @param  component                  The feature to be added to the Child attribute
	 * @exception  LexComponentException  Description of the Exception
	 */
	public void addChild( ILexComponent component ) throws LexComponentException;


	/**
	 *  Description of the Method
	 *
	 * @param  component                  Description of the Parameter
	 * @exception  LexComponentException  Description of the Exception
	 */
	public void removeChild( ILexComponent component ) throws LexComponentException;
}

