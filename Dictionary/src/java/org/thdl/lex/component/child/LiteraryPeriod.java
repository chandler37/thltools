package org.thdl.lex.component.child;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class LiteraryPeriod implements Serializable {

    /** identifier field */
    private Integer id;

    /** persistent field */
    private String literaryPeriod;

    /** full constructor */
    public LiteraryPeriod(String literaryPeriod) {
        this.literaryPeriod = literaryPeriod;
    }

    /** default constructor */
    public LiteraryPeriod() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLiteraryPeriod() {
        return this.literaryPeriod;
    }

    public void setLiteraryPeriod(String literaryPeriod) {
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
