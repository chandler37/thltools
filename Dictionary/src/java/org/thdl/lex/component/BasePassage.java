package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BasePassage extends LexComponent implements
		org.thdl.lex.component.IPassage, org.thdl.lex.component.Translatable,
		Serializable {

	/** nullable persistent field */
	private Integer parentId;

	/** nullable persistent field */
	private String literarySource;

	/** nullable persistent field */
	private String spelling;

	/** nullable persistent field */
	private String pagination;

	/** nullable persistent field */
	private String passage;

	/** nullable persistent field */
	private Integer translationOf;

	/** nullable persistent field */
	private org.thdl.lex.component.ILexComponent parent;

	/** persistent field */
	private List translations;

	/** full constructor */
	public BasePassage(Boolean deleted, List analyticalNotes,
			org.thdl.lex.component.Meta meta, Integer parentId,
			String literarySource, String spelling, String pagination,
			String passage, Integer translationOf,
			org.thdl.lex.component.ILexComponent parent, List translations) {
		super(deleted, analyticalNotes, meta);
		this.parentId = parentId;
		this.literarySource = literarySource;
		this.spelling = spelling;
		this.pagination = pagination;
		this.passage = passage;
		this.translationOf = translationOf;
		this.parent = parent;
		this.translations = translations;
	}

	/** default constructor */
	public BasePassage() {
	}

	/** minimal constructor */
	public BasePassage(Boolean deleted, List analyticalNotes,
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

	public String getLiterarySource() {
		return this.literarySource;
	}

	public void setLiterarySource(String literarySource) {
		this.literarySource = literarySource;
	}

	public String getSpelling() {
		return this.spelling;
	}

	public void setSpelling(String spelling) {
		this.spelling = spelling;
	}

	public String getPagination() {
		return this.pagination;
	}

	public void setPagination(String pagination) {
		this.pagination = pagination;
	}

	public String getPassage() {
		return this.passage;
	}

	public void setPassage(String passage) {
		this.passage = passage;
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