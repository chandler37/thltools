package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BasePassage extends LexComponent implements org.thdl.lex.component.IPassage,Serializable {

    /** nullable persistent field */
    private java.lang.Integer parentId;

    /** nullable persistent field */
    private java.lang.Short precedence;

    /** nullable persistent field */
    private java.lang.String literarySource;

    /** nullable persistent field */
    private java.lang.String spelling;

    /** nullable persistent field */
    private java.lang.String pagination;

    /** nullable persistent field */
    private java.lang.String passage;

    /** nullable persistent field */
    private org.thdl.lex.component.ILexComponent parent;

    /** full constructor */
    public BasePassage(java.lang.Integer translationOf, java.lang.Boolean deleted, List analyticalNotes, Set translations, org.thdl.lex.component.Meta meta, java.lang.Integer parentId, java.lang.Short precedence, java.lang.String literarySource, java.lang.String spelling, java.lang.String pagination, java.lang.String passage, org.thdl.lex.component.ILexComponent parent) {
        super(translationOf, deleted, analyticalNotes, translations, meta);
        this.parentId = parentId;
        this.precedence = precedence;
        this.literarySource = literarySource;
        this.spelling = spelling;
        this.pagination = pagination;
        this.passage = passage;
        this.parent = parent;
    }

    /** default constructor */
    public BasePassage() {
    }

    /** minimal constructor */
    public BasePassage(java.lang.Boolean deleted, List analyticalNotes, Set translations, org.thdl.lex.component.Meta meta) {
      super(deleted, analyticalNotes, translations, meta);
    }

    public java.lang.Integer getParentId() {
        return this.parentId;
    }

    public void setParentId(java.lang.Integer parentId) {
        this.parentId = parentId;
    }

    public java.lang.Short getPrecedence() {
        return this.precedence;
    }

    public void setPrecedence(java.lang.Short precedence) {
        this.precedence = precedence;
    }

    public java.lang.String getLiterarySource() {
        return this.literarySource;
    }

    public void setLiterarySource(java.lang.String literarySource) {
        this.literarySource = literarySource;
    }

    public java.lang.String getSpelling() {
        return this.spelling;
    }

    public void setSpelling(java.lang.String spelling) {
        this.spelling = spelling;
    }

    public java.lang.String getPagination() {
        return this.pagination;
    }

    public void setPagination(java.lang.String pagination) {
        this.pagination = pagination;
    }

    public java.lang.String getPassage() {
        return this.passage;
    }

    public void setPassage(java.lang.String passage) {
        this.passage = passage;
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
