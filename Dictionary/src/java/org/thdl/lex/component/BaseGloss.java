package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseGloss extends LexComponent implements
		org.thdl.lex.component.IGloss, Serializable {

	/** nullable persistent field */
	private Integer parentId;

	/** nullable persistent field */
	private String gloss;

	/** nullable persistent field */
	private String translation;

	/** nullable persistent field */
	private org.thdl.lex.component.ILexComponent parent;

	/** full constructor */
	public BaseGloss(Boolean deleted, List analyticalNotes,
			org.thdl.lex.component.Meta meta, Integer parentId, String gloss,
			String translation, org.thdl.lex.component.ILexComponent parent) {
		super(deleted, analyticalNotes, meta);
		this.parentId = parentId;
		this.gloss = gloss;
		this.translation = translation;
		this.parent = parent;
	}

	/** default constructor */
	public BaseGloss() {
	}

	/** minimal constructor */
	public BaseGloss(Boolean deleted, List analyticalNotes,
			org.thdl.lex.component.Meta meta) {
		super(deleted, analyticalNotes, meta);
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getGloss() {
		return this.gloss;
	}

	public void setGloss(String gloss) {
		this.gloss = gloss;
	}

	public String getTranslation() {
		return this.translation;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
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