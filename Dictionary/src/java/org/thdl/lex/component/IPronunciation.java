package org.thdl.lex.component;

public interface IPronunciation  extends ILexComponent
{	public ILexComponent getParent();
	public void setParent( ILexComponent comp );
	public java.lang.Integer getParentId();
	public void setParentId( java.lang.Integer parentId );
	public java.lang.String getPhonetics();
	public void setPhonetics(java.lang.String phonetics);
	public java.lang.Short getPhoneticsType();
	public void setPhoneticsType(java.lang.Short phoneticsType);
}

