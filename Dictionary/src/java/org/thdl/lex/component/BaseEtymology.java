package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseEtymology extends LexComponent implements
		org.thdl.lex.component.Translatable, Serializable,
		org.thdl.lex.component.IEtymology {

	/** nullable persistent field */
	private Integer parentId;

	/** nullable persistent field */
	private Integer loanLanguage;

	/** persistent field */
	private Integer etymologyType;

	/** persistent field */
	private String derivation;

	/** persistent field */
	private String etymologyDescription;

	/** nullable persistent field */
	private Integer translationOf;

	/** nullable persistent field */
	private org.thdl.lex.component.ILexComponent parent;

	/** persistent field */
	private List translations;

	/** full constructor */
	public BaseEtymology(Boolean deleted, List analyticalNotes,
			org.thdl.lex.component.Meta meta, Integer parentId,
			Integer loanLanguage, Integer etymologyType, String derivation,
			String etymologyDescription, Integer translationOf,
			org.thdl.lex.component.ILexComponent parent, List translations) {
		super(deleted, analyticalNotes, meta);
		this.parentId = parentId;
		this.loanLanguage = loanLanguage;
		this.etymologyType = etymologyType;
		this.derivation = derivation;
		this.etymologyDescription = etymologyDescription;
		this.translationOf = translationOf;
		this.parent = parent;
		this.translations = translations;
	}

	/** default constructor */
	public BaseEtymology() {
	}

	/** minimal constructor */
	public BaseEtymology(Boolean deleted, List analyticalNotes,
			org.thdl.lex.component.Meta meta, Integer etymologyType,
			String derivation, String etymologyDescription, List translations) {
		super(deleted, analyticalNotes, meta);
		this.etymologyType = etymologyType;
		this.derivation = derivation;
		this.etymologyDescription = etymologyDescription;
		this.translations = translations;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getLoanLanguage() {
		return this.loanLanguage;
	}

	public void setLoanLanguage(Integer loanLanguage) {
		this.loanLanguage = loanLanguage;
	}

	public Integer getEtymologyType() {
		return this.etymologyType;
	}

	public void setEtymologyType(Integer etymologyType) {
		this.etymologyType = etymologyType;
	}

	public String getDerivation() {
		return this.derivation;
	}

	public void setDerivation(String derivation) {
		this.derivation = derivation;
	}

	public String getEtymologyDescription() {
		return this.etymologyDescription;
	}

	public void setEtymologyDescription(String etymologyDescription) {
		this.etymologyDescription = etymologyDescription;
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