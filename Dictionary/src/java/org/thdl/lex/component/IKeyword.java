package org.thdl.lex.component;

public interface IKeyword  extends ILexComponent
{	public ILexComponent getParent();
	public void setParent( ILexComponent comp );
	public java.lang.Integer getParentId();
	public void setParentId( java.lang.Integer parentId );
	public java.lang.String getKeyword();
	public void setKeyword(java.lang.String keyword);
}


