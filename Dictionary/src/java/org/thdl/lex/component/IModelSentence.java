package org.thdl.lex.component;

public interface IModelSentence  extends ILexComponent
{	public ILexComponent getParent();
	public void setParent( ILexComponent comp );
	public java.lang.Integer getParentId();
	public void setParentId( java.lang.Integer parentId );
	public java.lang.Integer getSubdefinitionId();
	public void setSubdefinitionId(java.lang.Integer subdefinitionId);
	public java.lang.String getModelSentence();
	public void setModelSentence(java.lang.String modelSentence);
}

