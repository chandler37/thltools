package org.thdl.lex.component;

import java.io.Serializable;
import java.util.*;
import org.thdl.lex.*;


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

//helpers
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
		if ( null == component.getParent() )
		{
			component.setParent( findParent( component.getParentId() ) );
		}
		LexComponentNode node = (LexComponentNode) component.getParent();
		list = (List) node.getChildMap().get( component.getLabel() );

		if ( null == list )
		{
			Iterator it = getSubdefinitions().iterator();
			while ( it.hasNext() )
			{
				ISubdefinition subdef = (ISubdefinition) it.next();
				list = subdef.findSiblings( component );
			}
		}

		return list;
	}


	/**
	 *  Description of the Method
	 *
	 * @param  parentPk                   Description of the Parameter
	 * @return                            Description of the Return Value
	 * @exception  LexComponentException  Description of the Exception
	 */
	public ILexComponent findParent( Integer parentPk ) throws LexComponentException
	{
		LexLogger.debug( "Finding Parent..." );
		ILexComponent parent = null;
		if ( parentPk.equals( this.getMetaId() ) )
		{
			parent = this;
		}
		else
		{
			parent = findChild( parentPk );
		}
		return parent;
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
		List list = findSiblings( child );
		child = findChild( list, child.getMetaId() );
		return child;
	}


	/**
	 *  Description of the Method
	 *
	 * @param  pk                         Description of the Parameter
	 * @return                            Description of the Return Value
	 * @exception  LexComponentException  Description of the Exception
	 */
	public ILexComponent findChild( Integer pk ) throws LexComponentException
	{
		ILexComponent child = null;

		Iterator childMapValues = getChildMap().values().iterator();
		while ( childMapValues.hasNext() && null == child )
		{
			List list = (List) childMapValues.next();
			child = findChild( list, pk );
		}

		Iterator subdefinitions = getSubdefinitions().iterator();
		while ( subdefinitions.hasNext() && null == child )
		{
			ISubdefinition def = (ISubdefinition) subdefinitions.next();
			child = def.findChild( pk );
		}

		return child;
	}


	/**
	 *  Description of the Method
	 *
	 * @param  list  Description of the Parameter
	 * @param  pk    Description of the Parameter
	 * @return       Description of the Return Value
	 */
	public ILexComponent findChild( List list, Integer pk )
	{
		ILexComponent child = null;
		for ( Iterator it = list.iterator(); it.hasNext();  )
		{
			ILexComponent lc = (LexComponent) it.next();
			if ( lc.getMetaId().equals( pk ) )
			{
				child = lc;
				break;
			}
		}
		return child;
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

