package org.thdl.lex.component.child;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Source implements Serializable {

	/** identifier field */
	private Integer id;

	/** persistent field */
	private String sourceTitle;

	/** nullable persistent field */
	private String sourceDescription;

	/** full constructor */
	public Source(String sourceTitle, String sourceDescription) {
		this.sourceTitle = sourceTitle;
		this.sourceDescription = sourceDescription;
	}

	/** default constructor */
	public Source() {
	}

	/** minimal constructor */
	public Source(String sourceTitle) {
		this.sourceTitle = sourceTitle;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSourceTitle() {
		return this.sourceTitle;
	}

	public void setSourceTitle(String sourceTitle) {
		this.sourceTitle = sourceTitle;
	}

	public String getSourceDescription() {
		return this.sourceDescription;
	}

	public void setSourceDescription(String sourceDescription) {
		this.sourceDescription = sourceDescription;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

	public boolean equals(Object other) {
		if (!(other instanceof Source))
			return false;
		Source castOther = (Source) other;
		return new EqualsBuilder().append(this.getId(), castOther.getId())
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

}