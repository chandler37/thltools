package org.thdl.lex.component.child;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Function implements Serializable {

	/** identifier field */
	private Short id;

	/** persistent field */
	private short functionGeneral;

	/** persistent field */
	private short functionSpecific;

	/** full constructor */
	public Function(short functionGeneral, short functionSpecific) {
		this.functionGeneral = functionGeneral;
		this.functionSpecific = functionSpecific;
	}

	/** default constructor */
	public Function() {
	}

	public Short getId() {
		return this.id;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public short getFunctionGeneral() {
		return this.functionGeneral;
	}

	public void setFunctionGeneral(short functionGeneral) {
		this.functionGeneral = functionGeneral;
	}

	public short getFunctionSpecific() {
		return this.functionSpecific;
	}

	public void setFunctionSpecific(short functionSpecific) {
		this.functionSpecific = functionSpecific;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

	public boolean equals(Object other) {
		if (!(other instanceof Function))
			return false;
		Function castOther = (Function) other;
		return new EqualsBuilder().append(this.getId(), castOther.getId())
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

}