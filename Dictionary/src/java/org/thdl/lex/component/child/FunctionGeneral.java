package org.thdl.lex.component.child;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FunctionGeneral implements Serializable {

    /** identifier field */
    private Short id;

    /** persistent field */
    private String functionGeneral;

    /** full constructor */
    public FunctionGeneral(String functionGeneral) {
        this.functionGeneral = functionGeneral;
    }

    /** default constructor */
    public FunctionGeneral() {
    }

    public Short getId() {
        return this.id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getFunctionGeneral() {
        return this.functionGeneral;
    }

    public void setFunctionGeneral(String functionGeneral) {
        this.functionGeneral = functionGeneral;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof FunctionGeneral) ) return false;
        FunctionGeneral castOther = (FunctionGeneral) other;
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
