package org.thdl.lex.component.child;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class LiteraryForm implements Serializable {

    /** identifier field */
    private java.lang.Integer id;

    /** persistent field */
    private java.lang.String literaryForm;

    /** full constructor */
    public LiteraryForm(java.lang.String literaryForm) {
        this.literaryForm = literaryForm;
    }

    /** default constructor */
    public LiteraryForm() {
    }

    public java.lang.Integer getId() {
        return this.id;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    public java.lang.String getLiteraryForm() {
        return this.literaryForm;
    }

    public void setLiteraryForm(java.lang.String literaryForm) {
        this.literaryForm = literaryForm;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof LiteraryForm) ) return false;
        LiteraryForm castOther = (LiteraryForm) other;
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
