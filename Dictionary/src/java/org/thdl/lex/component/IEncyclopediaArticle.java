package org.thdl.lex.component;

public interface IEncyclopediaArticle  extends ILexComponent
{	public ILexComponent getParent();
	public void setParent( ILexComponent comp );
	public java.lang.Integer getParentId();
	public void setParentId( java.lang.Integer parentId );
	public java.lang.String getArticle();
	public void setArticle(java.lang.String article);
	public java.lang.String getArticleTitle();
	public void setArticleTitle(java.lang.String articleTitle);
}

