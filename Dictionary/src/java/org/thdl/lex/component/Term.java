package org.thdl.lex.component;

import java.io.Serializable;
import java.util.*;
import org.thdl.lex.LexConstants;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 3, 2003
 */
public class Term extends BaseTerm implements Serializable
{
	private HashMap childMap;
	//helper methods

	/**
	 *  Gets the parentId attribute of the Term object
	 *
	 * @return    The parentId value
	 */
	public java.lang.Integer getParentId()
	{
		return null;
	}


	/**
	 *  Sets the parentId attribute of the Term object
	 *
	 * @param  parentId  The new parentId value
	 */
	public void setParentId( java.lang.Integer parentId )
	{
		return;
	}


	/**
	 *  Gets the parent attribute of the Term object
	 *
	 * @return    The parent value
	 */
	public org.thdl.lex.component.ILexComponent getParent()
	{
		return null;
	}


	/**
	 *  Sets the parent attribute of the Term object
	 *
	 * @param  parent  The new parent value
	 */
	public void setParent( org.thdl.lex.component.ILexComponent parent )
	{
		return;
	}


	/**
	 *  Sets the childMap attribute of the Term object
	 *
	 * @param  childMap  The new childMap value
	 */
	public void setChildMap( HashMap childMap )
	{
		this.childMap = childMap;
	}


	/**
	 *  Gets the childMap attribute of the Term object
	 *
	 * @return    The childMap value
	 */
	public HashMap getChildMap()
	{
		return childMap;
	}


	/**
	 *  Gets the childList attribute of the Term object
	 *
	 * @param  component  Description of the Parameter
	 * @return            The childList value
	 */
	/*
	    public List getChildList( String label )
	    {
	    List list = null;
	    Iterator it = getChildMap().keySet().iterator();
	    while ( it.hasNext() )
	    {
	    String key = (String) it.next();
	    if ( key.equals( label ) )
	    {
	    list = (List) getChildMap().get( key );
	    }
	    }
	    return list;
	    }
	  */
	/**
	 *  Gets the persistentChild attribute of the Term object
	 *
	 * @param  component  Description of the Parameter
	 * @return            The persistentChild value
	 */
	public ILexComponent findChild( ILexComponent component )
	{
		ILexComponent persistentChild = null;
		if ( component.getParent() instanceof Term )
		{
			//String label =
			//List list = getChildList( label );
			List list = (List) getChildMap().get( component.getLabel() );
			for ( Iterator it = list.iterator(); it.hasNext();  )
			{
				ILexComponent termChild = (ILexComponent) it.next();
				if ( termChild.getMetaId().equals( component.getMetaId() ) )
				{
					persistentChild = termChild;
				}
			}
		}
		return persistentChild;
	}


	/**
	 *  Adds a feature to the Child attribute of the Term object
	 *
	 * @param  component  The feature to be added to the Child attribute
	 * @return            Description of the Return Value
	 */
	public ILexComponent addChild( ILexComponent component )
	{
		return null;
	}


	/**
	 *  The main program for the Term class
	 *
	 * @param  args  The command line arguments
	 */
	public static void main( String[] args )
	{
		String[] labels = new String[16];

		labels[0] = new Etymology().getLabel();
		labels[1] = new GrammaticalFunction().getLabel();
		labels[2] = new Pronunciation().getLabel();
		labels[3] = new Definition().getLabel();
		labels[4] = new EncyclopediaArticle().getLabel();
		labels[5] = new Spelling().getLabel();
		labels[6] = new AnalyticalNote().getLabel();
		labels[7] = new TransitionalData().getLabel();
		labels[8] = new Subdefinition().getLabel();
		labels[9] = new Gloss().getLabel();
		labels[10] = new Keyword().getLabel();
		labels[11] = new ModelSentence().getLabel();
		labels[12] = new Passage().getLabel();
		labels[13] = new SpeechRegister().getLabel();
		labels[14] = new RelatedTerm().getLabel();
		labels[15] = new TranslationEquivalent().getLabel();
		for ( int i = 0; i < labels.length; i++ )
		{
			System.out.println( labels[i] );
		}
	}


	/**
	 *  Description of the Method
	 */
	private void initChild()
	{
		setChildMap( new HashMap() );
		getChildMap().put( LexConstants.PRONUNCIATIONLABEL_VALUE, getPronunciations() );
		getChildMap().put( LexConstants.SPELLINGLABEL_VALUE, getSpellings() );
		getChildMap().put( LexConstants.ETYMOLOGYLABEL_VALUE, getEtymologies() );
		getChildMap().put( LexConstants.FUNCTIONLABEL_VALUE, getFunctions() );
		getChildMap().put( LexConstants.ENCYCLOPEDIA_ARTICLE_LABEL_VALUE, getEncyclopediaArticles() );
		getChildMap().put( LexConstants.DEFINITIONLABEL_VALUE, getDefinitions() );
		// getChildMap().put( LexConstants.SUBDEFINITIONLABEL_VALUE, getSubdefinitions() );
		getChildMap().put( LexConstants.MODELSENTENCELABEL_VALUE, getModelSentences() );
		getChildMap().put( LexConstants.PASSAGELABEL_VALUE, getPassages() );
		getChildMap().put( LexConstants.TRANSLATIONLABEL_VALUE, getTranslations() );
		getChildMap().put( LexConstants.RELATEDTERMLABEL_VALUE, getRelatedTerms() );
		getChildMap().put( LexConstants.REGISTERLABEL_VALUE, getRegisters() );
		getChildMap().put( LexConstants.KEYWORDLABEL_VALUE, getKeywords() );
		getChildMap().put( LexConstants.ANALYTICALNOTELABEL_VALUE, getAnalyticalNotes() );
		getChildMap().put( LexConstants.TRANSLATIONLABEL_VALUE, getTranslations() );
	}



	/**
	 *Constructor for the Term object
	 */
	public Term()
	{
		initChild();
	}


	/**
	 *Constructor for the Term object
	 *
	 * @param  translationOf           Description of the Parameter
	 * @param  deleted                 Description of the Parameter
	 * @param  analyticalNotes         Description of the Parameter
	 * @param  translations            Description of the Parameter
	 * @param  meta                    Description of the Parameter
	 * @param  term                    Description of the Parameter
	 * @param  precedence              Description of the Parameter
	 * @param  pronunciations          Description of the Parameter
	 * @param  etymologies             Description of the Parameter
	 * @param  spellings               Description of the Parameter
	 * @param  functions               Description of the Parameter
	 * @param  encyclopediaArticles    Description of the Parameter
	 * @param  transitionalData        Description of the Parameter
	 * @param  definitions             Description of the Parameter
	 * @param  glosses                 Description of the Parameter
	 * @param  keywords                Description of the Parameter
	 * @param  modelSentences          Description of the Parameter
	 * @param  translationEquivalents  Description of the Parameter
	 * @param  relatedTerms            Description of the Parameter
	 * @param  passages                Description of the Parameter
	 * @param  registers               Description of the Parameter
	 */
	public Term( java.lang.Integer translationOf, java.lang.Boolean deleted, List analyticalNotes, Set translations, org.thdl.lex.component.Meta meta, java.lang.String term, java.lang.Short precedence, List pronunciations, List etymologies, List spellings, List functions, List encyclopediaArticles, List transitionalData, List definitions, List glosses, List keywords, List modelSentences, List translationEquivalents, List relatedTerms, List passages, List registers )
	{
		super( translationOf, deleted, analyticalNotes, translations, meta, term, precedence, pronunciations, etymologies, spellings, functions, encyclopediaArticles, transitionalData, definitions, glosses, keywords, modelSentences, translationEquivalents, relatedTerms, passages, registers );
		initChild();
	}


	/**
	 *Constructor for the Term object
	 *
	 * @param  deleted                 Description of the Parameter
	 * @param  analyticalNotes         Description of the Parameter
	 * @param  translations            Description of the Parameter
	 * @param  meta                    Description of the Parameter
	 * @param  term                    Description of the Parameter
	 * @param  pronunciations          Description of the Parameter
	 * @param  etymologies             Description of the Parameter
	 * @param  spellings               Description of the Parameter
	 * @param  functions               Description of the Parameter
	 * @param  encyclopediaArticles    Description of the Parameter
	 * @param  transitionalData        Description of the Parameter
	 * @param  definitions             Description of the Parameter
	 * @param  glosses                 Description of the Parameter
	 * @param  keywords                Description of the Parameter
	 * @param  modelSentences          Description of the Parameter
	 * @param  translationEquivalents  Description of the Parameter
	 * @param  relatedTerms            Description of the Parameter
	 * @param  passages                Description of the Parameter
	 * @param  registers               Description of the Parameter
	 */
	public Term( java.lang.Boolean deleted, List analyticalNotes, Set translations, org.thdl.lex.component.Meta meta, java.lang.String term, List pronunciations, List etymologies, List spellings, List functions, List encyclopediaArticles, List transitionalData, List definitions, List glosses, List keywords, List modelSentences, List translationEquivalents, List relatedTerms, List passages, List registers )
	{
		super( deleted, analyticalNotes, translations, meta, term, pronunciations, etymologies, spellings, functions, encyclopediaArticles, transitionalData, definitions, glosses, keywords, modelSentences, translationEquivalents, relatedTerms, passages, registers );
		initChild();
	}
}

