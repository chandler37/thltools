package org.thdl.lex.component;


/**
 *  Description of the Interface
 *
 * @author     travis
 * @created    October 16, 2003
 */
public interface IEncyclopediaArticle extends ILexComponent
{
	/**
	 *  Sets the parent attribute of the IEncyclopediaArticle object
	 *
	 * @param  comp  The new parent value
	 */
	


	/**
	 *  Gets the parentId attribute of the IEncyclopediaArticle object
	 *
	 * @return    The parentId value
	 */
	public java.lang.Integer getParentId();


	/**
	 *  Sets the parentId attribute of the IEncyclopediaArticle object
	 *
	 * @param  parentId  The new parentId value
	 */
	public void setParentId( java.lang.Integer parentId );


	/**
	 *  Gets the article attribute of the IEncyclopediaArticle object
	 *
	 * @return    The article value
	 */
	public java.lang.String getArticle();


	/**
	 *  Sets the article attribute of the IEncyclopediaArticle object
	 *
	 * @param  article  The new article value
	 */
	public void setArticle( java.lang.String article );


	/**
	 *  Gets the articleTitle attribute of the IEncyclopediaArticle object
	 *
	 * @return    The articleTitle value
	 */
	public java.lang.String getArticleTitle();


	/**
	 *  Sets the articleTitle attribute of the IEncyclopediaArticle object
	 *
	 * @param  articleTitle  The new articleTitle value
	 */
	public void setArticleTitle( java.lang.String articleTitle );
}

