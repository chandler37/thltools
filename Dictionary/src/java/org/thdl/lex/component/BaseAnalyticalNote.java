package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseAnalyticalNote extends LexComponent implements Serializable,org.thdl.lex.component.IAnalyticalNote {

    /** nullable persistent field */
    private java.lang.Integer parentId;

    /** nullable persistent field */
    private java.lang.Short precedence;

    /** nullable persistent field */
    private java.lang.String analyticalNote;

    /** nullable persistent field */
    private org.thdl.lex.component.ILexComponent parent;

    /** full constructor */
    public BaseAnalyticalNote(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.Integer parentId, java.lang.Short precedence, java.lang.String analyticalNote, org.thdl.lex.component.ILexComponent parent) {
        super(deleted, analyticalNotes, meta);
        this.parentId = parentId;
        this.precedence = precedence;
        this.analyticalNote = analyticalNote;
        this.parent = parent;
    }

    /** default constructor */
    public BaseAnalyticalNote() {
    }

    /** minimal constructor */
    public BaseAnalyticalNote(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta) {
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

    public java.lang.String getAnalyticalNote() {
        return this.analyticalNote;
    }

    public void setAnalyticalNote(java.lang.String analyticalNote) {
        this.analyticalNote = analyticalNote;
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
