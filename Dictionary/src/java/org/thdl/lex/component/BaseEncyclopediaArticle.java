package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseEncyclopediaArticle extends LexComponent implements
		org.thdl.lex.component.IEncyclopediaArticle, Serializable {

	/** nullable persistent field */
	private Integer parentId;

	/** persistent field */
	private String article;

	/** persistent field */
	private String articleTitle;

	/** nullable persistent field */
	private org.thdl.lex.component.ILexComponent parent;

	/** full constructor */
	public BaseEncyclopediaArticle(Boolean deleted, List analyticalNotes,
			org.thdl.lex.component.Meta meta, Integer parentId, String article,
			String articleTitle, org.thdl.lex.component.ILexComponent parent) {
		super(deleted, analyticalNotes, meta);
		this.parentId = parentId;
		this.article = article;
		this.articleTitle = articleTitle;
		this.parent = parent;
	}

	/** default constructor */
	public BaseEncyclopediaArticle() {
	}

	/** minimal constructor */
	public BaseEncyclopediaArticle(Boolean deleted, List analyticalNotes,
			org.thdl.lex.component.Meta meta, String article,
			String articleTitle) {
		super(deleted, analyticalNotes, meta);
		this.article = article;
		this.articleTitle = articleTitle;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getArticle() {
		return this.article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public String getArticleTitle() {
		return this.articleTitle;
	}

	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}

	public org.thdl.lex.component.ILexComponent getParent() {
		return this.parent;
	}

	public void setParent(org.thdl.lex.component.ILexComponent parent) {
		this.parent = parent;
	}

	public String toString() {
		return new ToStringBuilder(this).append("metaId", getMetaId())
				.toString();
	}

}