package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseModelSentence extends LexComponent implements org.thdl.lex.component.IModelSentence,Serializable,org.thdl.lex.component.Translatable {

    /** nullable persistent field */
    private java.lang.Integer parentId;

    /** nullable persistent field */
    private java.lang.Integer precedence;

    /** nullable persistent field */
    private java.lang.String modelSentence;

    /** nullable persistent field */
    private java.lang.Integer translationOf;

    /** nullable persistent field */
    private org.thdl.lex.component.ILexComponent parent;

    /** persistent field */
    private List translations;

    /** full constructor */
    public BaseModelSentence(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.Integer parentId, java.lang.Integer precedence, java.lang.String modelSentence, java.lang.Integer translationOf, org.thdl.lex.component.ILexComponent parent, List translations) {
        super(deleted, analyticalNotes, meta);
        this.parentId = parentId;
        this.precedence = precedence;
        this.modelSentence = modelSentence;
        this.translationOf = translationOf;
        this.parent = parent;
        this.translations = translations;
    }

    /** default constructor */
    public BaseModelSentence() {
    }

    /** minimal constructor */
    public BaseModelSentence(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, List translations) {
      super(deleted, analyticalNotes, meta);
        this.translations = translations;
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

    public java.lang.String getModelSentence() {
        return this.modelSentence;
    }

    public void setModelSentence(java.lang.String modelSentence) {
        this.modelSentence = modelSentence;
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

    public java.util.List getTranslations() {
        return this.translations;
    }

    public void setTranslations(java.util.List translations) {
        this.translations = translations;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("metaId", getMetaId())
            .toString();
    }

}
