package org.thdl.lex.component;

public interface IRegister  extends ILexComponent
{	public ILexComponent getParent();
	public void setParent( ILexComponent comp );
	public java.lang.Integer getParentId();
	public void setParentId( java.lang.Integer parentId );
	public java.lang.Short getRegister();
	public void setRegister(java.lang.Short register);
}

