package org.thdl.lex.component;

public interface IPassage  extends ILexComponent
{	public ILexComponent getParent();
	public void setParent( ILexComponent comp );
	public java.lang.Integer getParentId();
	public void setParentId( java.lang.Integer parentId );
	public java.lang.String getLiterarySource();
	public void setLiterarySource(java.lang.String literarySource);
	public java.lang.String getSpelling();
	public void setSpelling(java.lang.String spelling);
	public java.lang.String getPagination();
	public void setPagination(java.lang.String pagination);
	public java.lang.String getPassage();
	public void setPassage(java.lang.String passage);
}

