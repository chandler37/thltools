package org.thdl.lex.component;

public interface ISubdefinition  extends ILexComponent
{	public ILexComponent getParent();
	public void setParent( ILexComponent comp );
	public java.lang.Integer getParentId();
	public void setParentId( java.lang.Integer parentId );
	public java.lang.String getSubdefinition();
	public void setSubdefinition(java.lang.String subdefinition);
	public java.util.List getGlosses();
	public void setGlosses(java.util.List glosses);
	public java.util.List getKeywords();
	public void setKeywords(java.util.List keywords);
	public java.util.List getModelSentences();
	public void setModelSentences(java.util.List modelSentences);
	public java.util.List getTranslationEquivalents();
	public void setTranslationEquivalents(java.util.List translationEquivalents);
	public java.util.List getRelatedTerms();
	public void setRelatedTerms(java.util.List relatedTerms);
	public java.util.List getPassages();
	public void setPassages(java.util.List passages);
	public java.util.List getRegisters();
	public void setRegisters(java.util.List registers);
}

