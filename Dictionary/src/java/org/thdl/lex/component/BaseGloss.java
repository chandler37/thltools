package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseGloss extends LexComponent implements org.thdl.lex.component.IGloss,Serializable {

    /** nullable persistent field */
    private java.lang.Integer parentId;

    /** nullable persistent field */
    private java.lang.Short precedence;

    /** nullable persistent field */
    private java.lang.String gloss;

    /** nullable persistent field */
    private java.lang.String translation;

    /** nullable persistent field */
    private org.thdl.lex.component.ILexComponent parent;

    /** full constructor */
    public BaseGloss(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.Integer parentId, java.lang.Short precedence, java.lang.String gloss, java.lang.String translation, org.thdl.lex.component.ILexComponent parent) {
        super(deleted, analyticalNotes, meta);
        this.parentId = parentId;
        this.precedence = precedence;
        this.gloss = gloss;
        this.translation = translation;
        this.parent = parent;
    }

    /** default constructor */
    public BaseGloss() {
    }

    /** minimal constructor */
    public BaseGloss(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta) {
      super(deleted, analyticalNotes, meta);
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

    public java.lang.String getGloss() {
        return this.gloss;
    }

    public void setGloss(java.lang.String gloss) {
        this.gloss = gloss;
    }

    public java.lang.String getTranslation() {
        return this.translation;
    }

    public void setTranslation(java.lang.String translation) {
        this.translation = translation;
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
