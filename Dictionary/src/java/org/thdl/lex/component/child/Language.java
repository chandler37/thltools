package org.thdl.lex.component.child;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Language implements Serializable {

    /** identifier field */
    private java.lang.Short id;

    /** persistent field */
    private java.lang.String language;

    /** full constructor */
    public Language(java.lang.String language) {
        this.language = language;
    }

    /** default constructor */
    public Language() {
    }

    public java.lang.Short getId() {
        return this.id;
    }

    public void setId(java.lang.Short id) {
        this.id = id;
    }

    public java.lang.String getLanguage() {
        return this.language;
    }

    public void setLanguage(java.lang.String language) {
        this.language = language;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Language) ) return false;
        Language castOther = (Language) other;
        return new EqualsBuilder()
            .append(this.getId(), castOther.getId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getId())
            .toHashCode();
    }

}
