package org.thdl.lex.component;

import java.io.Serializable;
import java.util.*;
import org.thdl.lex.LexConstants;
import org.thdl.lex.LexLogger;


/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 3, 2003
 */
public class Term extends BaseTerm implements Serializable, LexComponentNode
{
	private HashMap childMap;


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
		if ( null == childMap )
		{
			initChildMap();
		}
		return childMap;
	}


	/**
	 *  Description of the Method
	 *
	 * @param  component                  Description of the Parameter
	 * @return                            Description of the Return Value
	 * @exception  LexComponentException  Description of the Exception
	 */
	public List findSiblings( ILexComponent component ) throws LexComponentException
	{
		List list = null;
		if ( null != component.getParent() )
		{
			LexComponentNode node = (LexComponentNode) component.getParent();
			list = (List) node.getChildMap().get( component.getLabel() );
			LexLogger.debug( "List derived from " + node + ": " + list );
		}
		else
		{
			throw new LexComponentException( "Failed to locate a set of siblings in the Term object graph for: " + component );
		}
		return list;
	}


	/**
	 *  Gets the persistentChild attribute of the Term object
	 *
	 * @param  child                      Description of the Parameter
	 * @return                            The persistentChild value
	 * @exception  LexComponentException  Description of the Exception
	 */
	public ILexComponent findChild( ILexComponent child ) throws LexComponentException
	{
		ILexComponent persistentChild = null;
		List list = findSiblings( child );
		for ( Iterator it = list.iterator(); it.hasNext();  )
		{
			ILexComponent lc = (ILexComponent) it.next();
			if ( lc.getMetaId().equals( child.getMetaId() ) )
			{
				persistentChild = lc;
			}
		}
		return persistentChild;
	}


	/**
	 *  Adds a feature to the Child attribute of the Term object
	 *
	 * @param  component                  The feature to be added to the Child attribute
	 * @exception  LexComponentException  Description of the Exception
	 */
	public void addChild( ILexComponent component ) throws LexComponentException
	{
		List list = findSiblings( component );
		list.add( component );
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
	private void initChildMap()
	{
		setChildMap( new HashMap() );
		getChildMap().put( LexConstants.PRONUNCIATIONLABEL_VALUE, getPronunciations() );
		getChildMap().put( LexConstants.SPELLINGLABEL_VALUE, getSpellings() );
		getChildMap().put( LexConstants.ETYMOLOGYLABEL_VALUE, getEtymologies() );
		getChildMap().put( LexConstants.FUNCTIONLABEL_VALUE, getFunctions() );
		getChildMap().put( LexConstants.ENCYCLOPEDIA_ARTICLE_LABEL_VALUE, getEncyclopediaArticles() );
		getChildMap().put( LexConstants.DEFINITIONLABEL_VALUE, getDefinitions() );
		getChildMap().put( LexConstants.MODELSENTENCELABEL_VALUE, getModelSentences() );
		getChildMap().put( LexConstants.PASSAGELABEL_VALUE, getPassages() );
		getChildMap().put( LexConstants.RELATEDTERMLABEL_VALUE, getRelatedTerms() );
		getChildMap().put( LexConstants.REGISTERLABEL_VALUE, getRegisters() );
		getChildMap().put( LexConstants.KEYWORDLABEL_VALUE, getKeywords() );
		getChildMap().put( LexConstants.ANALYTICALNOTELABEL_VALUE, getAnalyticalNotes() );
		getChildMap().put( LexConstants.TRANSLATIONLABEL_VALUE, getTranslationEquivalents() );
		getChildMap().put( LexConstants.TRANSITIONALDATALABEL_VALUE, getTransitionalData() );
	}



	/**
	 *Constructor for the Term object
	 */
	public Term()
	{
		super();
	}

}

