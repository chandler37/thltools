package org.thdl.lex.component.child;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Dialect implements Serializable {

    /** identifier field */
    private java.lang.Integer id;

    /** persistent field */
    private short majorDialectFamily;

    /** persistent field */
    private short specificDialect;

    /** full constructor */
    public Dialect(short majorDialectFamily, short specificDialect) {
        this.majorDialectFamily = majorDialectFamily;
        this.specificDialect = specificDialect;
    }

    /** default constructor */
    public Dialect() {
    }

    public java.lang.Integer getId() {
        return this.id;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    public short getMajorDialectFamily() {
        return this.majorDialectFamily;
    }

    public void setMajorDialectFamily(short majorDialectFamily) {
        this.majorDialectFamily = majorDialectFamily;
    }

    public short getSpecificDialect() {
        return this.specificDialect;
    }

    public void setSpecificDialect(short specificDialect) {
        this.specificDialect = specificDialect;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Dialect) ) return false;
        Dialect castOther = (Dialect) other;
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
