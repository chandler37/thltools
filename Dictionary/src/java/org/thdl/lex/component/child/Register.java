package org.thdl.lex.component.child;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Register implements Serializable {

    /** identifier field */
    private java.lang.Short id;

    /** persistent field */
    private java.lang.String register;

    /** full constructor */
    public Register(java.lang.String register) {
        this.register = register;
    }

    /** default constructor */
    public Register() {
    }

    public java.lang.Short getId() {
        return this.id;
    }

    public void setId(java.lang.Short id) {
        this.id = id;
    }

    public java.lang.String getRegister() {
        return this.register;
    }

    public void setRegister(java.lang.String register) {
        this.register = register;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Register) ) return false;
        Register castOther = (Register) other;
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
