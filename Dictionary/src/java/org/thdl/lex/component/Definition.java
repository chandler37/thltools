package org.thdl.lex.component;

import java.io.Serializable;
import java.util.HashMap;
import org.thdl.lex.LexConstants;

/**
 *  Description of the Class
 *
 * @author     travis
 * @created    October 13, 2003
 */
public class Definition extends BaseDefinition implements Serializable, Translatable, LexComponentNode
{
	private HashMap childMap;


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
	 */
	private void initChildMap()
	{
		setChildMap( new HashMap() );
		getChildMap().put( LexConstants.SUBDEFINITIONLABEL_VALUE, getSubdefinitions() );
		getChildMap().put( LexConstants.MODELSENTENCELABEL_VALUE, getModelSentences() );
		getChildMap().put( LexConstants.PASSAGELABEL_VALUE, getPassages() );
		getChildMap().put( LexConstants.TRANSLATIONLABEL_VALUE, getTranslations() );
		getChildMap().put( LexConstants.RELATEDTERMLABEL_VALUE, getRelatedTerms() );
		getChildMap().put( LexConstants.REGISTERLABEL_VALUE, getRegisters() );
		getChildMap().put( LexConstants.KEYWORDLABEL_VALUE, getKeywords() );
		getChildMap().put( LexConstants.ANALYTICALNOTELABEL_VALUE, getAnalyticalNotes() );
		getChildMap().put( LexConstants.TRANSLATIONLABEL_VALUE, getTranslationEquivalents() );
	}

}

