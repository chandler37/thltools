package org.thdl.lex.component;

public interface IGloss  extends ILexComponent
{	public ILexComponent getParent();
	public void setParent( ILexComponent comp );
	public java.lang.Integer getParentId();
	public void setParentId( java.lang.Integer parentId );
	public java.lang.String getGloss();
	public void setGloss(java.lang.String gloss);
	public java.lang.String getTranslation();
	public void setTranslation(java.lang.String translation);
}

