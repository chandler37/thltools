package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseSpeechRegister extends LexComponent implements
		org.thdl.lex.component.IRegister, Serializable {

	/** nullable persistent field */
	private Integer parentId;

	/** persistent field */
	private Integer register;

	/** nullable persistent field */
	private org.thdl.lex.component.ILexComponent parent;

	/** full constructor */
	public BaseSpeechRegister(Boolean deleted, List analyticalNotes,
			org.thdl.lex.component.Meta meta, Integer parentId,
			Integer register, org.thdl.lex.component.ILexComponent parent) {
		super(deleted, analyticalNotes, meta);
		this.parentId = parentId;
		this.register = register;
		this.parent = parent;
	}

	/** default constructor */
	public BaseSpeechRegister() {
	}

	/** minimal constructor */
	public BaseSpeechRegister(Boolean deleted, List analyticalNotes,
			org.thdl.lex.component.Meta meta, Integer register) {
		super(deleted, analyticalNotes, meta);
		this.register = register;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getRegister() {
		return this.register;
	}

	public void setRegister(Integer register) {
		this.register = register;
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