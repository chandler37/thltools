package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseModelSentence extends LexComponent implements org.thdl.lex.component.IModelSentence,Serializable {

    /** nullable persistent field */
    private java.lang.Integer parentId;

    /** nullable persistent field */
    private java.lang.Short precedence;

    /** persistent field */
    private java.lang.Integer subdefinitionId;

    /** nullable persistent field */
    private java.lang.String modelSentence;

    /** nullable persistent field */
    private org.thdl.lex.component.ILexComponent parent;

    /** full constructor */
    public BaseModelSentence(java.lang.Integer translationOf, java.lang.Boolean deleted, List analyticalNotes, Set translations, org.thdl.lex.component.Meta meta, java.lang.Integer parentId, java.lang.Short precedence, java.lang.Integer subdefinitionId, java.lang.String modelSentence, org.thdl.lex.component.ILexComponent parent) {
        super(translationOf, deleted, analyticalNotes, translations, meta);
        this.parentId = parentId;
        this.precedence = precedence;
        this.subdefinitionId = subdefinitionId;
        this.modelSentence = modelSentence;
        this.parent = parent;
    }

    /** default constructor */
    public BaseModelSentence() {
    }

    /** minimal constructor */
    public BaseModelSentence(java.lang.Boolean deleted, List analyticalNotes, Set translations, org.thdl.lex.component.Meta meta, java.lang.Integer subdefinitionId) {
      super(deleted, analyticalNotes, translations, meta);
        this.subdefinitionId = subdefinitionId;
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
