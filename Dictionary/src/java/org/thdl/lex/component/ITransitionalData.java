package org.thdl.lex.component;

import org.thdl.lex.component.child.TransitionalDataLabel;

/**
 * Description of the Interface
 * 
 * @author travis
 * @created October 16, 2003
 */
public interface ITransitionalData extends ILexComponent {

	/**
	 * Gets the parentId attribute of the ITransitionalData object
	 * 
	 * @return The parentId value
	 */
	public java.lang.Integer getParentId();

	/**
	 * Sets the parentId attribute of the ITransitionalData object
	 * 
	 * @param parentId
	 *            The new parentId value
	 */
	public void setParentId(java.lang.Integer parentId);

	/**
	 * Gets the transitionalDataLabel attribute of the ITransitionalData object
	 * 
	 * @return The transitionalDataLabel value
	 */
	public TransitionalDataLabel getTransitionalDataLabel();

	/**
	 * Sets the transitionalDataLabel attribute of the ITransitionalData object
	 * 
	 * @param transitionalDataLabel
	 *            The new transitionalDataLabel value
	 */
	public void setTransitionalDataLabel(TransitionalDataLabel transitionalDataLabel);

	/**
	 * Gets the forPublicConsumption attribute of the ITransitionalData object
	 * 
	 * @return The forPublicConsumption value
	 */
	public java.lang.String getForPublicConsumption();

	/**
	 * Sets the forPublicConsumption attribute of the ITransitionalData object
	 * 
	 * @param forPublicConsumption
	 *            The new forPublicConsumption value
	 */
	public void setForPublicConsumption(java.lang.String forPublicConsumption);

	/**
	 * Gets the transitionalDataText attribute of the ITransitionalData object
	 * 
	 * @return The transitionalDataText value
	 */
	public java.lang.String getTransitionalDataText();

	/**
	 * Sets the transitionalDataText attribute of the ITransitionalData object
	 * 
	 * @param transitionalDataText
	 *            The new transitionalDataText value
	 */
	public void setTransitionalDataText(java.lang.String transitionalDataText);
}

