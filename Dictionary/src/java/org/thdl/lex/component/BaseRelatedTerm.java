package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseRelatedTerm extends LexComponent implements
		Serializable, org.thdl.lex.component.IRelatedTerm {

	/** nullable persistent field */
	private Integer parentId;

	/** nullable persistent field */
	private String relatedTerm;

	/** persistent field */
	private Integer relatedTermType;

	/** nullable persistent field */
	private org.thdl.lex.component.ILexComponent parent;

	/** full constructor */
	public BaseRelatedTerm(Boolean deleted, List analyticalNotes,
			org.thdl.lex.component.Meta meta, Integer parentId,
			String relatedTerm, Integer relatedTermType,
			org.thdl.lex.component.ILexComponent parent) {
		super(deleted, analyticalNotes, meta);
		this.parentId = parentId;
		this.relatedTerm = relatedTerm;
		this.relatedTermType = relatedTermType;
		this.parent = parent;
	}

	/** default constructor */
	public BaseRelatedTerm() {
	}

	/** minimal constructor */
	public BaseRelatedTerm(Boolean deleted, List analyticalNotes,
			org.thdl.lex.component.Meta meta, Integer relatedTermType) {
		super(deleted, analyticalNotes, meta);
		this.relatedTermType = relatedTermType;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getRelatedTerm() {
		return this.relatedTerm;
	}

	public void setRelatedTerm(String relatedTerm) {
		this.relatedTerm = relatedTerm;
	}

	public Integer getRelatedTermType() {
		return this.relatedTermType;
	}

	public void setRelatedTermType(Integer relatedTermType) {
		this.relatedTermType = relatedTermType;
	}

	public org.thdl.lex.component.ILexComponent getParent() {
		return this.parent;
	}

	public void setParent(org.thdl.lex.component.ILexComponent parent) {
		this.parent = parent;
	}

	public String toString() {
		return new ToStringBuilder(this).append("metaId", getMetaId())
				.toString();
	}

}