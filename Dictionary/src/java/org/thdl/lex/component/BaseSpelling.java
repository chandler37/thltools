package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseSpelling extends LexComponent implements
		Serializable, org.thdl.lex.component.ISpelling {

	/** nullable persistent field */
	private Integer parentId;

	/** persistent field */
	private String spelling;

	/** persistent field */
	private Integer spellingType;

	/** nullable persistent field */
	private org.thdl.lex.component.ILexComponent parent;

	/** full constructor */
	public BaseSpelling(Boolean deleted, List analyticalNotes,
			org.thdl.lex.component.Meta meta, Integer parentId,
			String spelling, Integer spellingType,
			org.thdl.lex.component.ILexComponent parent) {
		super(deleted, analyticalNotes, meta);
		this.parentId = parentId;
		this.spelling = spelling;
		this.spellingType = spellingType;
		this.parent = parent;
	}

	/** default constructor */
	public BaseSpelling() {
	}

	/** minimal constructor */
	public BaseSpelling(Boolean deleted, List analyticalNotes,
			org.thdl.lex.component.Meta meta, String spelling,
			Integer spellingType) {
		super(deleted, analyticalNotes, meta);
		this.spelling = spelling;
		this.spellingType = spellingType;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getSpelling() {
		return this.spelling;
	}

	public void setSpelling(String spelling) {
		this.spelling = spelling;
	}

	public Integer getSpellingType() {
		return this.spellingType;
	}

	public void setSpellingType(Integer spellingType) {
		this.spellingType = spellingType;
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