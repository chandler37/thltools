package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseTranslationEquivalent extends LexComponent implements
		org.thdl.lex.component.ITranslationEquivalent, Serializable {

	/** nullable persistent field */
	private Integer parentId;

	/** nullable persistent field */
	private String translationEquivalent;

	/** nullable persistent field */
	private org.thdl.lex.component.ILexComponent parent;

	/** full constructor */
	public BaseTranslationEquivalent(Boolean deleted, List analyticalNotes,
			org.thdl.lex.component.Meta meta, Integer parentId,
			String translationEquivalent,
			org.thdl.lex.component.ILexComponent parent) {
		super(deleted, analyticalNotes, meta);
		this.parentId = parentId;
		this.translationEquivalent = translationEquivalent;
		this.parent = parent;
	}

	/** default constructor */
	public BaseTranslationEquivalent() {
	}

	/** minimal constructor */
	public BaseTranslationEquivalent(Boolean deleted, List analyticalNotes,
			org.thdl.lex.component.Meta meta) {
		super(deleted, analyticalNotes, meta);
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getTranslationEquivalent() {
		return this.translationEquivalent;
	}

	public void setTranslationEquivalent(String translationEquivalent) {
		this.translationEquivalent = translationEquivalent;
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