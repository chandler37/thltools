package org.thdl.lex.component.child;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class PhoneticsType implements Serializable {

    /** identifier field */
    private java.lang.Short id;

    /** persistent field */
    private java.lang.String phoneticsType;

    /** full constructor */
    public PhoneticsType(java.lang.String phoneticsType) {
        this.phoneticsType = phoneticsType;
    }

    /** default constructor */
    public PhoneticsType() {
    }

    public java.lang.Short getId() {
        return this.id;
    }

    public void setId(java.lang.Short id) {
        this.id = id;
    }

    public java.lang.String getPhoneticsType() {
        return this.phoneticsType;
    }

    public void setPhoneticsType(java.lang.String phoneticsType) {
        this.phoneticsType = phoneticsType;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof PhoneticsType) ) return false;
        PhoneticsType castOther = (PhoneticsType) other;
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
