package org.thdl.lex.component.child;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class LiteraryPeriod implements Serializable {

    /** identifier field */
    private java.lang.Integer id;

    /** persistent field */
    private java.lang.String literaryPeriod;

    /** full constructor */
    public LiteraryPeriod(java.lang.String literaryPeriod) {
        this.literaryPeriod = literaryPeriod;
    }

    /** default constructor */
    public LiteraryPeriod() {
    }

    public java.lang.Integer getId() {
        return this.id;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    public java.lang.String getLiteraryPeriod() {
        return this.literaryPeriod;
    }

    public void setLiteraryPeriod(java.lang.String literaryPeriod) {
        this.literaryPeriod = literaryPeriod;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof LiteraryPeriod) ) return false;
        LiteraryPeriod castOther = (LiteraryPeriod) other;
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
