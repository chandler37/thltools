package org.thdl.lex.component.child;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class EtymologyType implements Serializable {

	/** identifier field */
	private Short id;

	/** persistent field */
	private String etymologyType;

	/** full constructor */
	public EtymologyType(String etymologyType) {
		this.etymologyType = etymologyType;
	}

	/** default constructor */
	public EtymologyType() {
	}

	public Short getId() {
		return this.id;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public String getEtymologyType() {
		return this.etymologyType;
	}

	public void setEtymologyType(String etymologyType) {
		this.etymologyType = etymologyType;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

	public boolean equals(Object other) {
		if (!(other instanceof EtymologyType))
			return false;
		EtymologyType castOther = (EtymologyType) other;
		return new EqualsBuilder().append(this.getId(), castOther.getId())
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

}