package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseGrammaticalFunction extends LexComponent implements
		org.thdl.lex.component.IFunction, Serializable {

	/** nullable persistent field */
	private Integer parentId;

	/** persistent field */
	private Integer function;

	/** nullable persistent field */
	private org.thdl.lex.component.ILexComponent parent;

	/** full constructor */
	public BaseGrammaticalFunction(Boolean deleted, List analyticalNotes,
			org.thdl.lex.component.Meta meta, Integer parentId,
			Integer function, org.thdl.lex.component.ILexComponent parent) {
		super(deleted, analyticalNotes, meta);
		this.parentId = parentId;
		this.function = function;
		this.parent = parent;
	}

	/** default constructor */
	public BaseGrammaticalFunction() {
	}

	/** minimal constructor */
	public BaseGrammaticalFunction(Boolean deleted, List analyticalNotes,
			org.thdl.lex.component.Meta meta, Integer function) {
		super(deleted, analyticalNotes, meta);
		this.function = function;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getFunction() {
		return this.function;
	}

	public void setFunction(Integer function) {
		this.function = function;
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