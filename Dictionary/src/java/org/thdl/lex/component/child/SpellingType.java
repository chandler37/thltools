package org.thdl.lex.component.child;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SpellingType implements Serializable {

    /** identifier field */
    private java.lang.Short id;

    /** persistent field */
    private java.lang.String spellingType;

    /** full constructor */
    public SpellingType(java.lang.String spellingType) {
        this.spellingType = spellingType;
    }

    /** default constructor */
    public SpellingType() {
    }

    public java.lang.Short getId() {
        return this.id;
    }

    public void setId(java.lang.Short id) {
        this.id = id;
    }

    public java.lang.String getSpellingType() {
        return this.spellingType;
    }

    public void setSpellingType(java.lang.String spellingType) {
        this.spellingType = spellingType;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof SpellingType) ) return false;
        SpellingType castOther = (SpellingType) other;
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
