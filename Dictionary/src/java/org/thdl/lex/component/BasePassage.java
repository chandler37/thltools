package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BasePassage extends LexComponent implements org.thdl.lex.component.IPassage,Serializable,org.thdl.lex.component.Translatable {

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
    private java.lang.Integer translationOf;

    /** nullable persistent field */
    private org.thdl.lex.component.ILexComponent parent;

    /** persistent field */
    private Set translations;

    /** full constructor */
    public BasePassage(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.Integer parentId, java.lang.Short precedence, java.lang.String literarySource, java.lang.String spelling, java.lang.String pagination, java.lang.String passage, java.lang.Integer translationOf, org.thdl.lex.component.ILexComponent parent, Set translations) {
        super(deleted, analyticalNotes, meta);
        this.parentId = parentId;
        this.precedence = precedence;
        this.literarySource = literarySource;
        this.spelling = spelling;
        this.pagination = pagination;
        this.passage = passage;
        this.translationOf = translationOf;
        this.parent = parent;
        this.translations = translations;
    }

    /** default constructor */
    public BasePassage() {
    }

    /** minimal constructor */
    public BasePassage(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, Set translations) {
      super(deleted, analyticalNotes, meta);
        this.translations = translations;
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

    public java.lang.Integer getTranslationOf() {
        return this.translationOf;
    }

    public void setTranslationOf(java.lang.Integer translationOf) {
        this.translationOf = translationOf;
    }

    public org.thdl.lex.component.ILexComponent getParent() {
        return this.parent;
    }

    public void setParent(org.thdl.lex.component.ILexComponent parent) {
        this.parent = parent;
    }

    public java.util.Set getTranslations() {
        return this.translations;
    }

    public void setTranslations(java.util.Set translations) {
        this.translations = translations;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("metaId", getMetaId())
            .toString();
    }

}
