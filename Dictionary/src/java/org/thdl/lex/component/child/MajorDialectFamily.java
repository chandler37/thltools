package org.thdl.lex.component.child;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MajorDialectFamily implements Serializable {

    /** identifier field */
    private java.lang.Short id;

    /** nullable persistent field */
    private java.lang.String majorDialectFamily;

    /** full constructor */
    public MajorDialectFamily(java.lang.String majorDialectFamily) {
        this.majorDialectFamily = majorDialectFamily;
    }

    /** default constructor */
    public MajorDialectFamily() {
    }

    public java.lang.Short getId() {
        return this.id;
    }

    public void setId(java.lang.Short id) {
        this.id = id;
    }

    public java.lang.String getMajorDialectFamily() {
        return this.majorDialectFamily;
    }

    public void setMajorDialectFamily(java.lang.String majorDialectFamily) {
        this.majorDialectFamily = majorDialectFamily;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MajorDialectFamily) ) return false;
        MajorDialectFamily castOther = (MajorDialectFamily) other;
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
