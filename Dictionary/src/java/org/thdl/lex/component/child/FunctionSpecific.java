package org.thdl.lex.component.child;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FunctionSpecific implements Serializable {

    /** identifier field */
    private java.lang.Short id;

    /** persistent field */
    private java.lang.String functionSpecific;

    /** full constructor */
    public FunctionSpecific(java.lang.String functionSpecific) {
        this.functionSpecific = functionSpecific;
    }

    /** default constructor */
    public FunctionSpecific() {
    }

    public java.lang.Short getId() {
        return this.id;
    }

    public void setId(java.lang.Short id) {
        this.id = id;
    }

    public java.lang.String getFunctionSpecific() {
        return this.functionSpecific;
    }

    public void setFunctionSpecific(java.lang.String functionSpecific) {
        this.functionSpecific = functionSpecific;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof FunctionSpecific) ) return false;
        FunctionSpecific castOther = (FunctionSpecific) other;
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
