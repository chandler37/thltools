package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseLexComponent implements org.thdl.lex.component.ILexComponent,Serializable {

    /** identifier field */
    private java.lang.Integer metaId;

    /** nullable persistent field */
    private java.lang.Integer translationOf;

    /** persistent field */
    private java.lang.Boolean deleted;

    /** persistent field */
    private List analyticalNotes;

    /** persistent field */
    private Set translations;

    /** persistent field */
    private org.thdl.lex.component.Meta meta;

    /** full constructor */
    public BaseLexComponent(java.lang.Integer translationOf, java.lang.Boolean deleted, List analyticalNotes, Set translations, org.thdl.lex.component.Meta meta) {
        this.translationOf = translationOf;
        this.deleted = deleted;
        this.analyticalNotes = analyticalNotes;
        this.translations = translations;
        this.meta = meta;
    }

    /** default constructor */
    public BaseLexComponent() {
    }

    /** minimal constructor */
    public BaseLexComponent(java.lang.Boolean deleted, List analyticalNotes, Set translations, org.thdl.lex.component.Meta meta) {
        this.deleted = deleted;
        this.analyticalNotes = analyticalNotes;
        this.translations = translations;
        this.meta = meta;
    }

    public java.lang.Integer getMetaId() {
        return this.metaId;
    }

    public void setMetaId(java.lang.Integer metaId) {
        this.metaId = metaId;
    }

    public java.lang.Integer getTranslationOf() {
        return this.translationOf;
    }

    public void setTranslationOf(java.lang.Integer translationOf) {
        this.translationOf = translationOf;
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

    public java.util.Set getTranslations() {
        return this.translations;
    }

    public void setTranslations(java.util.Set translations) {
        this.translations = translations;
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
