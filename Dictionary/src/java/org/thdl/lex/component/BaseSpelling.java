package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseSpelling extends LexComponent implements Serializable,org.thdl.lex.component.ISpelling {

    /** nullable persistent field */
    private java.lang.Integer parentId;

    /** nullable persistent field */
    private java.lang.Short precedence;

    /** persistent field */
    private java.lang.String spelling;

    /** persistent field */
    private java.lang.Short spellingType;

    /** nullable persistent field */
    private org.thdl.lex.component.ILexComponent parent;

    /** full constructor */
    public BaseSpelling(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.Integer parentId, java.lang.Short precedence, java.lang.String spelling, java.lang.Short spellingType, org.thdl.lex.component.ILexComponent parent) {
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
    public BaseSpelling(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.String spelling, java.lang.Short spellingType) {
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

    public java.lang.Short getPrecedence() {
        return this.precedence;
    }

    public void setPrecedence(java.lang.Short precedence) {
        this.precedence = precedence;
    }

    public java.lang.String getSpelling() {
        return this.spelling;
    }

    public void setSpelling(java.lang.String spelling) {
        this.spelling = spelling;
    }

    public java.lang.Short getSpellingType() {
        return this.spellingType;
    }

    public void setSpellingType(java.lang.Short spellingType) {
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
