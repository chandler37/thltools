package org.thdl.lex.component.child;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SpecificDialect implements Serializable {

	/** identifier field */
	private Short id;

	/** nullable persistent field */
	private String specificDialect;

	/** full constructor */
	public SpecificDialect(String specificDialect) {
		this.specificDialect = specificDialect;
	}

	/** default constructor */
	public SpecificDialect() {
	}

	public Short getId() {
		return this.id;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public String getSpecificDialect() {
		return this.specificDialect;
	}

	public void setSpecificDialect(String specificDialect) {
		this.specificDialect = specificDialect;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

	public boolean equals(Object other) {
		if (!(other instanceof SpecificDialect))
			return false;
		SpecificDialect castOther = (SpecificDialect) other;
		return new EqualsBuilder().append(this.getId(), castOther.getId())
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

}