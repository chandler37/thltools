package org.thdl.lex.component.child;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class RelatedTermType implements Serializable {

    /** identifier field */
    private java.lang.Short id;

    /** persistent field */
    private java.lang.String relatedTermType;

    /** full constructor */
    public RelatedTermType(java.lang.String relatedTermType) {
        this.relatedTermType = relatedTermType;
    }

    /** default constructor */
    public RelatedTermType() {
    }

    public java.lang.Short getId() {
        return this.id;
    }

    public void setId(java.lang.Short id) {
        this.id = id;
    }

    public java.lang.String getRelatedTermType() {
        return this.relatedTermType;
    }

    public void setRelatedTermType(java.lang.String relatedTermType) {
        this.relatedTermType = relatedTermType;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof RelatedTermType) ) return false;
        RelatedTermType castOther = (RelatedTermType) other;
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
