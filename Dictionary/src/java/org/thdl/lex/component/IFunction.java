package org.thdl.lex.component;

public interface IFunction  extends ILexComponent
{	public ILexComponent getParent();
	public void setParent( ILexComponent comp );
	public java.lang.Integer getParentId();
	public void setParentId( java.lang.Integer parentId );
	public java.lang.Short getFunction();
	public void setFunction(java.lang.Short function);
}

