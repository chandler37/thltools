package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseLexComponent implements org.thdl.lex.component.ILexComponent,Serializable {

    /** identifier field */
    private java.lang.Integer metaId;

    /** persistent field */
    private java.lang.Boolean deleted;

    /** persistent field */
    private List analyticalNotes;

    /** persistent field */
    private org.thdl.lex.component.Meta meta;

    /** full constructor */
    public BaseLexComponent(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta) {
        this.deleted = deleted;
        this.analyticalNotes = analyticalNotes;
        this.meta = meta;
    }

    /** default constructor */
    public BaseLexComponent() {
    }

    public java.lang.Integer getMetaId() {
        return this.metaId;
    }

    public void setMetaId(java.lang.Integer metaId) {
        this.metaId = metaId;
    }

    public java.lang.Boolean getDeleted() {
        return this.deleted;
    }

    public void setDeleted(java.lang.Boolean deleted) {
        this.deleted = deleted;
    }

    public java.util.List getAnalyticalNotes() {
        return this.analyticalNotes;
    }

    public void setAnalyticalNotes(java.util.List analyticalNotes) {
        this.analyticalNotes = analyticalNotes;
    }

    public org.thdl.lex.component.Meta getMeta() {
        return this.meta;
    }

    public void setMeta(org.thdl.lex.component.Meta meta) {
        this.meta = meta;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("metaId", getMetaId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof BaseLexComponent) ) return false;
        BaseLexComponent castOther = (BaseLexComponent) other;
        return new EqualsBuilder()
            .append(this.getMetaId(), castOther.getMetaId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getMetaId())
            .toHashCode();
    }

}
