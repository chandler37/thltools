package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BasePronunciation extends LexComponent implements org.thdl.lex.component.IPronunciation,Serializable {

    /** nullable persistent field */
    private java.lang.Integer parentId;

    /** nullable persistent field */
    private java.lang.Integer precedence;

    /** persistent field */
    private java.lang.String phonetics;

    /** persistent field */
    private java.lang.Integer phoneticsType;

    /** nullable persistent field */
    private org.thdl.lex.component.ILexComponent parent;

    /** full constructor */
    public BasePronunciation(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.Integer parentId, java.lang.Integer precedence, java.lang.String phonetics, java.lang.Integer phoneticsType, org.thdl.lex.component.ILexComponent parent) {
        super(deleted, analyticalNotes, meta);
        this.parentId = parentId;
        this.precedence = precedence;
        this.phonetics = phonetics;
        this.phoneticsType = phoneticsType;
        this.parent = parent;
    }

    /** default constructor */
    public BasePronunciation() {
    }

    /** minimal constructor */
    public BasePronunciation(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.String phonetics, java.lang.Integer phoneticsType) {
      super(deleted, analyticalNotes, meta);
        this.phonetics = phonetics;
        this.phoneticsType = phoneticsType;
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

    public java.lang.String getPhonetics() {
        return this.phonetics;
    }

    public void setPhonetics(java.lang.String phonetics) {
        this.phonetics = phonetics;
    }

    public java.lang.Integer getPhoneticsType() {
        return this.phoneticsType;
    }

    public void setPhoneticsType(java.lang.Integer phoneticsType) {
        this.phoneticsType = phoneticsType;
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
