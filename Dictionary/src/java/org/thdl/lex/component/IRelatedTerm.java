package org.thdl.lex.component;

public interface IRelatedTerm  extends ILexComponent
{	public ILexComponent getParent();
	public void setParent( ILexComponent comp );
	public java.lang.Integer getParentId();
	public void setParentId( java.lang.Integer parentId );
	public java.lang.String getRelatedTerm();
	public void setRelatedTerm(java.lang.String relatedTerm);
	public java.lang.Short getRelatedTermType();
	public void setRelatedTermType(java.lang.Short relatedTermType);
}

