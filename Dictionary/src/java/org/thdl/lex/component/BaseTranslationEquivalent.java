package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseTranslationEquivalent extends LexComponent implements org.thdl.lex.component.ITranslationEquivalent,Serializable {

    /** nullable persistent field */
    private java.lang.Integer parentId;

    /** nullable persistent field */
    private java.lang.Integer precedence;

    /** nullable persistent field */
    private java.lang.String translationEquivalent;

    /** nullable persistent field */
    private org.thdl.lex.component.ILexComponent parent;

    /** full constructor */
    public BaseTranslationEquivalent(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.Integer parentId, java.lang.Integer precedence, java.lang.String translationEquivalent, org.thdl.lex.component.ILexComponent parent) {
        super(deleted, analyticalNotes, meta);
        this.parentId = parentId;
        this.precedence = precedence;
        this.translationEquivalent = translationEquivalent;
        this.parent = parent;
    }

    /** default constructor */
    public BaseTranslationEquivalent() {
    }

    /** minimal constructor */
    public BaseTranslationEquivalent(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta) {
      super(deleted, analyticalNotes, meta);
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

    public java.lang.String getTranslationEquivalent() {
        return this.translationEquivalent;
    }

    public void setTranslationEquivalent(java.lang.String translationEquivalent) {
        this.translationEquivalent = translationEquivalent;
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
