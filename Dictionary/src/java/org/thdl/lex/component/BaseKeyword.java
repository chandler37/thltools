package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseKeyword extends LexComponent implements
		org.thdl.lex.component.IKeyword, Serializable {

	/** nullable persistent field */
	private Integer parentId;

	/** nullable persistent field */
	private String keyword;

	/** nullable persistent field */
	private org.thdl.lex.component.ILexComponent parent;

	/** full constructor */
	public BaseKeyword(Boolean deleted, List analyticalNotes,
			org.thdl.lex.component.Meta meta, Integer parentId, String keyword,
			org.thdl.lex.component.ILexComponent parent) {
		super(deleted, analyticalNotes, meta);
		this.parentId = parentId;
		this.keyword = keyword;
		this.parent = parent;
	}

	/** default constructor */
	public BaseKeyword() {
	}

	/** minimal constructor */
	public BaseKeyword(Boolean deleted, List analyticalNotes,
			org.thdl.lex.component.Meta meta) {
		super(deleted, analyticalNotes, meta);
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getKeyword() {
		return this.keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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