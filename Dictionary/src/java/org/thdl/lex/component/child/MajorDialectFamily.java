package org.thdl.lex.component.child;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MajorDialectFamily implements Serializable {

    /** identifier field */
    private Short id;

    /** nullable persistent field */
    private String majorDialectFamily;

    /** full constructor */
    public MajorDialectFamily(String majorDialectFamily) {
        this.majorDialectFamily = majorDialectFamily;
    }

    /** default constructor */
    public MajorDialectFamily() {
    }

    public Short getId() {
        return this.id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getMajorDialectFamily() {
        return this.majorDialectFamily;
    }

    public void setMajorDialectFamily(String majorDialectFamily) {
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
