package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseSpelling extends LexComponent implements Serializable,org.thdl.lex.component.ISpelling {

    /** nullable persistent field */
    private java.lang.Integer parentId;

    /** nullable persistent field */
    private java.lang.Integer precedence;

    /** persistent field */
    private java.lang.String spelling;

    /** persistent field */
    private java.lang.Integer spellingType;

    /** nullable persistent field */
    private org.thdl.lex.component.ILexComponent parent;

    /** full constructor */
    public BaseSpelling(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.Integer parentId, java.lang.Integer precedence, java.lang.String spelling, java.lang.Integer spellingType, org.thdl.lex.component.ILexComponent parent) {
        super(deleted, analyticalNotes, meta);
        this.parentId = parentId;
        this.precedence = precedence;
        this.spelling = spelling;
        this.spellingType = spellingType;
        this.parent = parent;
    }

    /** default constructor */
    public BaseSpelling() {
    }

    /** minimal constructor */
    public BaseSpelling(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.String spelling, java.lang.Integer spellingType) {
      super(deleted, analyticalNotes, meta);
        this.spelling = spelling;
        this.spellingType = spellingType;
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

    public java.lang.String getSpelling() {
        return this.spelling;
    }

    public void setSpelling(java.lang.String spelling) {
        this.spelling = spelling;
    }

    public java.lang.Integer getSpellingType() {
        return this.spellingType;
    }

    public void setSpellingType(java.lang.Integer spellingType) {
        this.spellingType = spellingType;
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
