package org.thdl.lex.component.child;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class LiteraryGenre implements Serializable {

    /** identifier field */
    private Integer id;

    /** persistent field */
    private String literaryGenre;

    /** full constructor */
    public LiteraryGenre(String literaryGenre) {
        this.literaryGenre = literaryGenre;
    }

    /** default constructor */
    public LiteraryGenre() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLiteraryGenre() {
        return this.literaryGenre;
    }

    public void setLiteraryGenre(String literaryGenre) {
        this.literaryGenre = literaryGenre;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof LiteraryGenre) ) return false;
        LiteraryGenre castOther = (LiteraryGenre) other;
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
