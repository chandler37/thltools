package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseAnalyticalNote extends LexComponent implements Serializable,org.thdl.lex.component.IAnalyticalNote {

    /** nullable persistent field */
    private Integer parentId;

    /** nullable persistent field */
    private String analyticalNote;

    /** nullable persistent field */
    private org.thdl.lex.component.ILexComponent parent;

    /** full constructor */
    public BaseAnalyticalNote(Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, Integer parentId, String analyticalNote, org.thdl.lex.component.ILexComponent parent) {
        super(deleted, analyticalNotes, meta);
        this.parentId = parentId;
        this.analyticalNote = analyticalNote;
        this.parent = parent;
    }

    /** default constructor */
    public BaseAnalyticalNote() {
    }

    /** minimal constructor */
    public BaseAnalyticalNote(Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta) {
      super(deleted, analyticalNotes, meta);
    }

    public Integer getParentId() {
        return this.parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getAnalyticalNote() {
        return this.analyticalNote;
    }

    public void setAnalyticalNote(String analyticalNote) {
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
