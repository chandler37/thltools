package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * @author     Hibernate CodeGenerator
 * @created    December 20, 2003
 */
public abstract class BaseSubdefinition extends LexComponent implements org.thdl.lex.component.ISubdefinition, org.thdl.lex.component.Translatable, org.thdl.lex.component.LexComponentNode, Serializable
{

	/**
	 * nullable persistent field
	 */
	private java.lang.Integer parentId;

	/**
	 * nullable persistent field
	 */
	private java.lang.Integer precedence;

	/**
	 * nullable persistent field
	 */
	private java.lang.String subdefinition;

	/**
	 * nullable persistent field
	 */
	private java.lang.Integer translationOf;

	/**
	 * nullable persistent field
	 */
	private org.thdl.lex.component.ILexComponent parent;

	/**
	 * persistent field
	 */
	private List translations;

	/**
	 * persistent field
	 */
	private List glosses;

	/**
	 * persistent field
	 */
	private List keywords;

	/**
	 * persistent field
	 */
	private List modelSentences;

	/**
	 * persistent field
	 */
	private List translationEquivalents;

	/**
	 * persistent field
	 */
	private List relatedTerms;

	/**
	 * persistent field
	 */
	private List passages;

	/**
	 * persistent field
	 */
	private List registers;


	/**
	 * full constructor
	 *
	 * @param  deleted                 Description of the Parameter
	 * @param  analyticalNotes         Description of the Parameter
	 * @param  meta                    Description of the Parameter
	 * @param  parentId                Description of the Parameter
	 * @param  precedence              Description of the Parameter
	 * @param  subdefinition           Description of the Parameter
	 * @param  translationOf           Description of the Parameter
	 * @param  parent                  Description of the Parameter
	 * @param  translations            Description of the Parameter
	 * @param  glosses                 Description of the Parameter
	 * @param  keywords                Description of the Parameter
	 * @param  modelSentences          Description of the Parameter
	 * @param  translationEquivalents  Description of the Parameter
	 * @param  relatedTerms            Description of the Parameter
	 * @param  passages                Description of the Parameter
	 * @param  registers               Description of the Parameter
	 */
	public BaseSubdefinition( java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.Integer parentId, java.lang.Integer precedence, java.lang.String subdefinition, java.lang.Integer translationOf, org.thdl.lex.component.ILexComponent parent, List translations, List glosses, List keywords, List modelSentences, List translationEquivalents, List relatedTerms, List passages, List registers )
	{
		super( deleted, analyticalNotes, meta );
		this.parentId = parentId;
		this.precedence = precedence;
		this.subdefinition = subdefinition;
		this.translationOf = translationOf;
		this.parent = parent;
		this.translations = translations;
		this.glosses = glosses;
		this.keywords = keywords;
		this.modelSentences = modelSentences;
		this.translationEquivalents = translationEquivalents;
		this.relatedTerms = relatedTerms;
		this.passages = passages;
		this.registers = registers;
	}


	/**
	 * default constructor
	 */
	public BaseSubdefinition() { }


	/**
	 * minimal constructor
	 *
	 * @param  deleted                 Description of the Parameter
	 * @param  analyticalNotes         Description of the Parameter
	 * @param  meta                    Description of the Parameter
	 * @param  translations            Description of the Parameter
	 * @param  glosses                 Description of the Parameter
	 * @param  keywords                Description of the Parameter
	 * @param  modelSentences          Description of the Parameter
	 * @param  translationEquivalents  Description of the Parameter
	 * @param  relatedTerms            Description of the Parameter
	 * @param  passages                Description of the Parameter
	 * @param  registers               Description of the Parameter
	 */
	public BaseSubdefinition( java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, List translations, List glosses, List keywords, List modelSentences, List translationEquivalents, List relatedTerms, List passages, List registers )
	{
		super( deleted, analyticalNotes, meta );
		this.translations = translations;
		this.glosses = glosses;
		this.keywords = keywords;
		this.modelSentences = modelSentences;
		this.translationEquivalents = translationEquivalents;
		this.relatedTerms = relatedTerms;
		this.passages = passages;
		this.registers = registers;
	}


	/**
	 *  Gets the parentId attribute of the BaseSubdefinition object
	 *
	 * @return    The parentId value
	 */
	public java.lang.Integer getParentId()
	{
		return this.parentId;
	}


	/**
	 *  Sets the parentId attribute of the BaseSubdefinition object
	 *
	 * @param  parentId  The new parentId value
	 */
	public void setParentId( java.lang.Integer parentId )
	{
		this.parentId = parentId;
	}


	/**
	 *  Gets the precedence attribute of the BaseSubdefinition object
	 *
	 * @return    The precedence value
	 */
	public java.lang.Integer getPrecedence()
	{
		return this.precedence;
	}


	/**
	 *  Sets the precedence attribute of the BaseSubdefinition object
	 *
	 * @param  precedence  The new precedence value
	 */
	public void setPrecedence( java.lang.Integer precedence )
	{
		if ( null == precedence )
		{
			int i = getGlosses().size();
		}
		if ( ( new Integer( -1 ) ).equals( precedence ) )
		{
			Object o = new String();
			Integer i = (Integer) o;
		}
		this.precedence = precedence;
	}


	/**
	 *  Gets the subdefinition attribute of the BaseSubdefinition object
	 *
	 * @return    The subdefinition value
	 */
	public java.lang.String getSubdefinition()
	{
		return this.subdefinition;
	}


	/**
	 *  Sets the subdefinition attribute of the BaseSubdefinition object
	 *
	 * @param  subdefinition  The new subdefinition value
	 */
	public void setSubdefinition( java.lang.String subdefinition )
	{
		this.subdefinition = subdefinition;
	}


	/**
	 *  Gets the translationOf attribute of the BaseSubdefinition object
	 *
	 * @return    The translationOf value
	 */
	public java.lang.Integer getTranslationOf()
	{
		return this.translationOf;
	}


	/**
	 *  Sets the translationOf attribute of the BaseSubdefinition object
	 *
	 * @param  translationOf  The new translationOf value
	 */
	public void setTranslationOf( java.lang.Integer translationOf )
	{
		this.translationOf = translationOf;
	}


	/**
	 *  Gets the parent attribute of the BaseSubdefinition object
	 *
	 * @return    The parent value
	 */
	public org.thdl.lex.component.ILexComponent getParent()
	{
		return this.parent;
	}


	/**
	 *  Sets the parent attribute of the BaseSubdefinition object
	 *
	 * @param  parent  The new parent value
	 */
	public void setParent( org.thdl.lex.component.ILexComponent parent )
	{
		this.parent = parent;
	}


	/**
	 *  Gets the translations attribute of the BaseSubdefinition object
	 *
	 * @return    The translations value
	 */
	public java.util.List getTranslations()
	{
		return this.translations;
	}


	/**
	 *  Sets the translations attribute of the BaseSubdefinition object
	 *
	 * @param  translations  The new translations value
	 */
	public void setTranslations( java.util.List translations )
	{
		this.translations = translations;
	}


	/**
	 *  Gets the glosses attribute of the BaseSubdefinition object
	 *
	 * @return    The glosses value
	 */
	public java.util.List getGlosses()
	{
		return this.glosses;
	}


	/**
	 *  Sets the glosses attribute of the BaseSubdefinition object
	 *
	 * @param  glosses  The new glosses value
	 */
	public void setGlosses( java.util.List glosses )
	{
		this.glosses = glosses;
	}


	/**
	 *  Gets the keywords attribute of the BaseSubdefinition object
	 *
	 * @return    The keywords value
	 */
	public java.util.List getKeywords()
	{
		return this.keywords;
	}


	/**
	 *  Sets the keywords attribute of the BaseSubdefinition object
	 *
	 * @param  keywords  The new keywords value
	 */
	public void setKeywords( java.util.List keywords )
	{
		this.keywords = keywords;
	}


	/**
	 *  Gets the modelSentences attribute of the BaseSubdefinition object
	 *
	 * @return    The modelSentences value
	 */
	public java.util.List getModelSentences()
	{
		return this.modelSentences;
	}


	/**
	 *  Sets the modelSentences attribute of the BaseSubdefinition object
	 *
	 * @param  modelSentences  The new modelSentences value
	 */
	public void setModelSentences( java.util.List modelSentences )
	{
		this.modelSentences = modelSentences;
	}


	/**
	 *  Gets the translationEquivalents attribute of the BaseSubdefinition object
	 *
	 * @return    The translationEquivalents value
	 */
	public java.util.List getTranslationEquivalents()
	{
		return this.translationEquivalents;
	}


	/**
	 *  Sets the translationEquivalents attribute of the BaseSubdefinition object
	 *
	 * @param  translationEquivalents  The new translationEquivalents value
	 */
	public void setTranslationEquivalents( java.util.List translationEquivalents )
	{
		this.translationEquivalents = translationEquivalents;
	}


	/**
	 *  Gets the relatedTerms attribute of the BaseSubdefinition object
	 *
	 * @return    The relatedTerms value
	 */
	public java.util.List getRelatedTerms()
	{
		return this.relatedTerms;
	}


	/**
	 *  Sets the relatedTerms attribute of the BaseSubdefinition object
	 *
	 * @param  relatedTerms  The new relatedTerms value
	 */
	public void setRelatedTerms( java.util.List relatedTerms )
	{
		this.relatedTerms = relatedTerms;
	}


	/**
	 *  Gets the passages attribute of the BaseSubdefinition object
	 *
	 * @return    The passages value
	 */
	public java.util.List getPassages()
	{
		return this.passages;
	}


	/**
	 *  Sets the passages attribute of the BaseSubdefinition object
	 *
	 * @param  passages  The new passages value
	 */
	public void setPassages( java.util.List passages )
	{
		this.passages = passages;
	}


	/**
	 *  Gets the registers attribute of the BaseSubdefinition object
	 *
	 * @return    The registers value
	 */
	public java.util.List getRegisters()
	{
		return this.registers;
	}


	/**
	 *  Sets the registers attribute of the BaseSubdefinition object
	 *
	 * @param  registers  The new registers value
	 */
	public void setRegisters( java.util.List registers )
	{
		this.registers = registers;
	}


	/**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
	public String toString()
	{
		return new ToStringBuilder( this )
				.append( "metaId", getMetaId() )
				.toString();
	}

}

