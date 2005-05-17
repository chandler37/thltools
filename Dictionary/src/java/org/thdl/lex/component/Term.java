package org.thdl.lex.component;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.thdl.lex.LexConstants;
import org.thdl.lex.LexLogger;

/**
 * Description of the Class
 * 
 * @author travis
 * @created October 3, 2003
 */
public class Term extends BaseTerm implements Serializable, LexComponentNode {
	private HashMap childMap;

	/**
	 * Gets the parentId attribute of the Term object
	 * 
	 * @return The parentId value
	 */
	public java.lang.Integer getParentId() {
		return null;
	}

	/**
	 * Sets the parentId attribute of the Term object
	 * 
	 * @param parentId
	 *            The new parentId value
	 */
	public void setParentId(java.lang.Integer parentId) {
		return;
	}

	/**
	 * Gets the parent attribute of the Term object
	 * 
	 * @return The parent value
	 */
	public org.thdl.lex.component.ILexComponent getParent() {
		return null;
	}

	/**
	 * Sets the parent attribute of the Term object
	 * 
	 * @param parent
	 *            The new parent value
	 */
	public void setParent(org.thdl.lex.component.ILexComponent parent) {
		return;
	}

	/**
	 * Sets the childMap attribute of the Term object
	 * 
	 * @param childMap
	 *            The new childMap value
	 */
	public void setChildMap(HashMap childMap) {
		this.childMap = childMap;
	}

	/**
	 * Gets the childMap attribute of the Term object
	 * 
	 * @return The childMap value
	 */
	public HashMap getChildMap() {
		if (null == childMap) {
			initChildMap();
		}
		return childMap;
	}

	/**
	 * Description of the Method
	 * 
	 * @param component
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception LexComponentException
	 *                Description of the Exception
	 */
	public List findSiblings(ILexComponent component)
			throws LexComponentException {
		List list = null;
		if (null == component.getParent()) {
			component.setParent(findParent(component.getParentId()));
		}
		LexComponentNode node = (LexComponentNode) component.getParent();
		list = (List) node.getChildMap().get(component.getLabel());
		LexLogger.debug("[Term] List derived from " + node + ": " + list);

		if (null == list) {
			LexLogger.debug("findSiblings returned a null list");
			LexLogger.debugComponent(component);

			if (null != getDefinitions()) {
				Iterator it = getDefinitions().iterator();
				while (it.hasNext()) {
					IDefinition def = (IDefinition) it.next();
					list = def.findSiblings(component);
				}
			}

		}

		return list;
	}

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
			throws LexComponentException {
		LexLogger.debug("Finding Parent...");
		ILexComponent parent = null;
		if (parentPk.equals(this.getMetaId())) {
			parent = this;
		} else {
			parent = findChild(parentPk);
		}
		return parent;
	}

	/**
	 * Gets the persistentChild attribute of the Term object
	 * 
	 * @param child
	 *            Description of the Parameter
	 * @return The persistentChild value
	 * @exception LexComponentException
	 *                Description of the Exception
	 */
	public ILexComponent findChild(ILexComponent child)
			throws LexComponentException {
		List list = findSiblings(child);
		child = findChild(list.iterator(), child.getMetaId());
		return child;
	}

	/**
	 * Description of the Method
	 * 
	 * @param pk
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception LexComponentException
	 *                Description of the Exception
	 */
	public ILexComponent findChild(Integer pk) throws LexComponentException {
		ILexComponent child = null;
		Iterator childMapValues = getChildMap().values().iterator();
		while (childMapValues.hasNext() && null == child) {
			Object obj = childMapValues.next();
			Collection list = (Collection) obj;
			child = findChild(list.iterator(), pk);
		}
		if (null != getDefinitions()) {
			Iterator definitions = getDefinitions().iterator();
			while (definitions.hasNext() && null == child) {
				IDefinition def = (IDefinition) definitions.next();
				child = def.findChild(pk);
			}
		}
		return child;
	}

	/**
	 * Description of the Method
	 * 
	 * @param list
	 *            Description of the Parameter
	 * @param pk
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public ILexComponent findChild(Iterator list, Integer pk) {
		ILexComponent child = null;
		if (list != null) {
			while (list.hasNext()) {
				ILexComponent lc = (LexComponent) list.next();
				if (lc.getMetaId().equals(pk)) {
					child = lc;
					break;
				}
			}
		}
		return child;
	}

	/**
	 * Adds a feature to the Child attribute of the Term object
	 * 
	 * @param component
	 *            The feature to be added to the Child attribute
	 * @exception LexComponentException
	 *                Description of the Exception
	 */
	public void addChild(ILexComponent component) throws LexComponentException {
		List list = findSiblings(component);
		if (list == null) {
			list = new LinkedList();
			LexComponentNode parent = (LexComponentNode) component.getParent();
			parent.addSiblingList(parent, component, list);
			parent.getChildMap().put(component.getLabel(), list);
		}
		list.add(component);
	}

	/**
	 * Description of the Method
	 * 
	 * @param child
	 *            Description of the Parameter
	 * @exception LexComponentException
	 *                Description of the Exception
	 */
	public void removeChild(ILexComponent child) throws LexComponentException {
		List list = findSiblings(child);
		int index = list.indexOf(child);
		list.remove(index);
	}

	/**
	 * Description of the Method
	 */
	private void initChildMap() {
		setChildMap(new HashMap());
		getChildMap().put(LexConstants.PRONUNCIATIONLABEL_VALUE,
				getPronunciations());
		getChildMap().put(LexConstants.SPELLINGLABEL_VALUE, getSpellings());
		getChildMap().put(LexConstants.ETYMOLOGYLABEL_VALUE, getEtymologies());
		getChildMap().put(LexConstants.FUNCTIONLABEL_VALUE, getFunctions());
		getChildMap().put(LexConstants.ENCYCLOPEDIA_ARTICLE_LABEL_VALUE,
				getEncyclopediaArticles());
		getChildMap().put(LexConstants.DEFINITIONLABEL_VALUE, getDefinitions());
		getChildMap().put(LexConstants.MODELSENTENCELABEL_VALUE,
				getModelSentences());
		getChildMap().put(LexConstants.PASSAGELABEL_VALUE, getPassages());
		getChildMap().put(LexConstants.RELATEDTERMLABEL_VALUE,
				getRelatedTerms());
		getChildMap().put(LexConstants.REGISTERLABEL_VALUE, getRegisters());
		getChildMap().put(LexConstants.KEYWORDLABEL_VALUE, getKeywords());
		getChildMap().put(LexConstants.ANALYTICALNOTELABEL_VALUE,
				getAnalyticalNotes());
		getChildMap().put(LexConstants.TRANSLATIONLABEL_VALUE,
				getTranslationEquivalents());
		getChildMap().put(LexConstants.TRANSITIONALDATALABEL_VALUE,
				getTransitionalData());
	}

	/**
	 * Constructor for the Term object
	 */
	public Term() {
		super();
	}

}

