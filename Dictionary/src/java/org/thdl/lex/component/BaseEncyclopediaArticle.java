package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseEncyclopediaArticle extends LexComponent implements org.thdl.lex.component.IEncyclopediaArticle,Serializable {

    /** nullable persistent field */
    private java.lang.Integer parentId;

    /** nullable persistent field */
    private java.lang.Integer precedence;

    /** persistent field */
    private java.lang.String article;

    /** persistent field */
    private java.lang.String articleTitle;

    /** nullable persistent field */
    private org.thdl.lex.component.ILexComponent parent;

    /** full constructor */
    public BaseEncyclopediaArticle(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.Integer parentId, java.lang.Integer precedence, java.lang.String article, java.lang.String articleTitle, org.thdl.lex.component.ILexComponent parent) {
        super(deleted, analyticalNotes, meta);
        this.parentId = parentId;
        this.precedence = precedence;
        this.article = article;
        this.articleTitle = articleTitle;
        this.parent = parent;
    }

    /** default constructor */
    public BaseEncyclopediaArticle() {
    }

    /** minimal constructor */
    public BaseEncyclopediaArticle(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.String article, java.lang.String articleTitle) {
      super(deleted, analyticalNotes, meta);
        this.article = article;
        this.articleTitle = articleTitle;
    }

    public java.lang.Integer getParentId() {
        return this.parentId;
    }

    public void setParentId(java.lang.Integer parentId) {
        this.parentId = parentId;
    }

    public java.lang.Integer getPrecedence() {
        return this.precedence;
    }

    public void setPrecedence(java.lang.Integer precedence) {
        this.precedence = precedence;
    }

    public java.lang.String getArticle() {
        return this.article;
    }

    public void setArticle(java.lang.String article) {
        this.article = article;
    }

    public java.lang.String getArticleTitle() {
        return this.articleTitle;
    }

    public void setArticleTitle(java.lang.String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public org.thdl.lex.component.ILexComponent getParent() {
        return this.parent;
    }

    public void setParent(org.thdl.lex.component.ILexComponent parent) {
        this.parent = parent;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("metaId", getMetaId())
            .toString();
    }

}
