package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseModelSentence extends LexComponent implements
		org.thdl.lex.component.IModelSentence, Serializable,
		org.thdl.lex.component.Translatable {

	/** nullable persistent field */
	private Integer parentId;

	/** nullable persistent field */
	private String modelSentence;

	/** nullable persistent field */
	private Integer translationOf;

	/** nullable persistent field */
	private org.thdl.lex.component.ILexComponent parent;

	/** persistent field */
	private List translations;

	/** full constructor */
	public BaseModelSentence(Boolean deleted, List analyticalNotes,
			org.thdl.lex.component.Meta meta, Integer parentId,
			String modelSentence, Integer translationOf,
			org.thdl.lex.component.ILexComponent parent, List translations) {
		super(deleted, analyticalNotes, meta);
		this.parentId = parentId;
		this.modelSentence = modelSentence;
		this.translationOf = translationOf;
		this.parent = parent;
		this.translations = translations;
	}

	/** default constructor */
	public BaseModelSentence() {
	}

	/** minimal constructor */
	public BaseModelSentence(Boolean deleted, List analyticalNotes,
			org.thdl.lex.component.Meta meta, List translations) {
		super(deleted, analyticalNotes, meta);
		this.translations = translations;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getModelSentence() {
		return this.modelSentence;
	}

	public void setModelSentence(String modelSentence) {
		this.modelSentence = modelSentence;
	}

	public Integer getTranslationOf() {
		return this.translationOf;
	}

	public void setTranslationOf(Integer translationOf) {
		this.translationOf = translationOf;
	}

	public org.thdl.lex.component.ILexComponent getParent() {
		return this.parent;
	}

	public void setParent(org.thdl.lex.component.ILexComponent parent) {
		this.parent = parent;
	}

	public List getTranslations() {
		return this.translations;
	}

	public void setTranslations(List translations) {
		this.translations = translations;
	}

	public String toString() {
		return new ToStringBuilder(this).append("metaId", getMetaId())
				.toString();
	}

}