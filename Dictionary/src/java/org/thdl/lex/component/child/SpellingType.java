package org.thdl.lex.component.child;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SpellingType implements Serializable {

    /** identifier field */
    private Short id;

    /** persistent field */
    private String spellingType;

    /** full constructor */
    public SpellingType(String spellingType) {
        this.spellingType = spellingType;
    }

    /** default constructor */
    public SpellingType() {
    }

    public Short getId() {
        return this.id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getSpellingType() {
        return this.spellingType;
    }

    public void setSpellingType(String spellingType) {
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
