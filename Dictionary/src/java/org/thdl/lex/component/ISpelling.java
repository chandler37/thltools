package org.thdl.lex.component;

public interface ISpelling  extends ILexComponent
{	public ILexComponent getParent();
	public void setParent( ILexComponent comp );
	public java.lang.Integer getParentId();
	public void setParentId( java.lang.Integer parentId );
	public java.lang.String getSpelling();
	public void setSpelling(java.lang.String spelling);
	public java.lang.Short getSpellingType();
	public void setSpellingType(java.lang.Short spellingType);
}

