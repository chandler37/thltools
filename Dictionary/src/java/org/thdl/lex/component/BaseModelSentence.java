package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseModelSentence extends LexComponent implements org.thdl.lex.component.Translatable,org.thdl.lex.component.IModelSentence,Serializable {

    /** nullable persistent field */
    private java.lang.Integer parentId;

    /** nullable persistent field */
    private java.lang.Short precedence;

    /** persistent field */
    private java.lang.Integer subdefinitionId;

    /** nullable persistent field */
    private java.lang.String modelSentence;

    /** nullable persistent field */
    private java.lang.Integer translationOf;

    /** nullable persistent field */
    private org.thdl.lex.component.ILexComponent parent;

    /** persistent field */
    private Set translations;

    /** full constructor */
    public BaseModelSentence(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.Integer parentId, java.lang.Short precedence, java.lang.Integer subdefinitionId, java.lang.String modelSentence, java.lang.Integer translationOf, org.thdl.lex.component.ILexComponent parent, Set translations) {
        super(deleted, analyticalNotes, meta);
        this.parentId = parentId;
        this.precedence = precedence;
        this.subdefinitionId = subdefinitionId;
        this.modelSentence = modelSentence;
        this.translationOf = translationOf;
        this.parent = parent;
        this.translations = translations;
    }

    /** default constructor */
    public BaseModelSentence() {
    }

    /** minimal constructor */
    public BaseModelSentence(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.Integer subdefinitionId, Set translations) {
      super(deleted, analyticalNotes, meta);
        this.subdefinitionId = subdefinitionId;
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

    public java.lang.Integer getSubdefinitionId() {
        return this.subdefinitionId;
    }

    public void setSubdefinitionId(java.lang.Integer subdefinitionId) {
        this.subdefinitionId = subdefinitionId;
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
