package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseRelatedTerm extends LexComponent implements Serializable,org.thdl.lex.component.IRelatedTerm {

    /** nullable persistent field */
    private java.lang.Integer parentId;

    /** nullable persistent field */
    private java.lang.Integer precedence;

    /** nullable persistent field */
    private java.lang.String relatedTerm;

    /** persistent field */
    private java.lang.Integer relatedTermType;

    /** nullable persistent field */
    private org.thdl.lex.component.ILexComponent parent;

    /** full constructor */
    public BaseRelatedTerm(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.Integer parentId, java.lang.Integer precedence, java.lang.String relatedTerm, java.lang.Integer relatedTermType, org.thdl.lex.component.ILexComponent parent) {
        super(deleted, analyticalNotes, meta);
        this.parentId = parentId;
        this.precedence = precedence;
        this.relatedTerm = relatedTerm;
        this.relatedTermType = relatedTermType;
        this.parent = parent;
    }

    /** default constructor */
    public BaseRelatedTerm() {
    }

    /** minimal constructor */
    public BaseRelatedTerm(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.Integer relatedTermType) {
      super(deleted, analyticalNotes, meta);
        this.relatedTermType = relatedTermType;
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

    public java.lang.String getRelatedTerm() {
        return this.relatedTerm;
    }

    public void setRelatedTerm(java.lang.String relatedTerm) {
        this.relatedTerm = relatedTerm;
    }

    public java.lang.Integer getRelatedTermType() {
        return this.relatedTermType;
    }

    public void setRelatedTermType(java.lang.Integer relatedTermType) {
        this.relatedTermType = relatedTermType;
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
