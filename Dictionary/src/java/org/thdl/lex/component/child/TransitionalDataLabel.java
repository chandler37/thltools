package org.thdl.lex.component.child;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class TransitionalDataLabel implements Serializable {

    /** identifier field */
    private java.lang.Short id;

    /** nullable persistent field */
    private java.lang.String transitionalDataLabel;

    /** full constructor */
    public TransitionalDataLabel(java.lang.String transitionalDataLabel) {
        this.transitionalDataLabel = transitionalDataLabel;
    }

    /** default constructor */
    public TransitionalDataLabel() {
    }

    public java.lang.Short getId() {
        return this.id;
    }

    public void setId(java.lang.Short id) {
        this.id = id;
    }

    public java.lang.String getTransitionalDataLabel() {
        return this.transitionalDataLabel;
    }

    public void setTransitionalDataLabel(java.lang.String transitionalDataLabel) {
        this.transitionalDataLabel = transitionalDataLabel;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof TransitionalDataLabel) ) return false;
        TransitionalDataLabel castOther = (TransitionalDataLabel) other;
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
