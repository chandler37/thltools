package org.thdl.lex.component.child;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FunctionsGeneral implements Serializable {

    /** identifier field */
    private java.lang.Short id;

    /** persistent field */
    private java.lang.String functionGeneral;

    /** full constructor */
    public FunctionsGeneral(java.lang.String functionGeneral) {
        this.functionGeneral = functionGeneral;
    }

    /** default constructor */
    public FunctionsGeneral() {
    }

    public java.lang.Short getId() {
        return this.id;
    }

    public void setId(java.lang.Short id) {
        this.id = id;
    }

    public java.lang.String getFunctionGeneral() {
        return this.functionGeneral;
    }

    public void setFunctionGeneral(java.lang.String functionGeneral) {
        this.functionGeneral = functionGeneral;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof FunctionsGeneral) ) return false;
        FunctionsGeneral castOther = (FunctionsGeneral) other;
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
