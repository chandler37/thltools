package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BasePronunciation extends LexComponent implements org.thdl.lex.component.IPronunciation,Serializable {

    /** nullable persistent field */
    private java.lang.Integer parentId;

    /** nullable persistent field */
    private java.lang.Short precedence;

    /** persistent field */
    private java.lang.String phonetics;

    /** persistent field */
    private java.lang.Short phoneticsType;

    /** nullable persistent field */
    private org.thdl.lex.component.ILexComponent parent;

    /** full constructor */
    public BasePronunciation(java.lang.Integer translationOf, java.lang.Boolean deleted, List analyticalNotes, Set translations, org.thdl.lex.component.Meta meta, java.lang.Integer parentId, java.lang.Short precedence, java.lang.String phonetics, java.lang.Short phoneticsType, org.thdl.lex.component.ILexComponent parent) {
        super(translationOf, deleted, analyticalNotes, translations, meta);
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
    public BasePronunciation(java.lang.Boolean deleted, List analyticalNotes, Set translations, org.thdl.lex.component.Meta meta, java.lang.String phonetics, java.lang.Short phoneticsType) {
      super(deleted, analyticalNotes, translations, meta);
        this.phonetics = phonetics;
        this.phoneticsType = phoneticsType;
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

    public java.lang.String getPhonetics() {
        return this.phonetics;
    }

    public void setPhonetics(java.lang.String phonetics) {
        this.phonetics = phonetics;
    }

    public java.lang.Short getPhoneticsType() {
        return this.phoneticsType;
    }

    public void setPhoneticsType(java.lang.Short phoneticsType) {
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
