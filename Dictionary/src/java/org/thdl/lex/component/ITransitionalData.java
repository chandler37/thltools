package org.thdl.lex.component;

public interface ITransitionalData  extends ILexComponent
{	public ILexComponent getParent();
	public void setParent( ILexComponent comp );
	public java.lang.Integer getParentId();
	public void setParentId( java.lang.Integer parentId );
	public java.lang.Short getTransitionalDataLabel();
	public void setTransitionalDataLabel(java.lang.Short transitionalDataLabel);
	public java.lang.String getForPublicConsumption();
	public void setForPublicConsumption(java.lang.String forPublicConsumption);
	public java.lang.String getTransitionalDataText();
	public void setTransitionalDataText(java.lang.String transitionalDataText);
}

