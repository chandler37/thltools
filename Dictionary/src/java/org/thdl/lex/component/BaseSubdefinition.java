package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseSubdefinition extends LexComponent implements
		org.thdl.lex.component.ISubdefinition,
		org.thdl.lex.component.Translatable,
		org.thdl.lex.component.LexComponentNode, Serializable {

	/** nullable persistent field */
	private Integer parentId;

	/** nullable persistent field */
	private String subdefinition;

	/** nullable persistent field */
	private Integer translationOf;

	/** nullable persistent field */
	private org.thdl.lex.component.ILexComponent parent;

	/** persistent field */
	private List translations;

	/** persistent field */
	private List glosses;

	/** persistent field */
	private List keywords;

	/** persistent field */
	private List modelSentences;

	/** persistent field */
	private List translationEquivalents;

	/** persistent field */
	private List relatedTerms;

	/** persistent field */
	private List passages;

	/** persistent field */
	private List registers;

	/** full constructor */
	public BaseSubdefinition(Boolean deleted, List analyticalNotes,
			org.thdl.lex.component.Meta meta, Integer parentId,
			String subdefinition, Integer translationOf,
			org.thdl.lex.component.ILexComponent parent, List translations,
			List glosses, List keywords, List modelSentences,
			List translationEquivalents, List relatedTerms, List passages,
			List registers) {
		super(deleted, analyticalNotes, meta);
		this.parentId = parentId;
		this.subdefinition = subdefinition;
		this.translationOf = translationOf;
		this.parent = parent;
		this.translations = translations;
		this.glosses = glosses;
		this.keywords = keywords;
		this.modelSentences = modelSentences;
		this.translationEquivalents = translationEquivalents;
		this.relatedTerms = relatedTerms;
		this.passages = passages;
		this.registers = registers;
	}

	/** default constructor */
	public BaseSubdefinition() {
	}

	/** minimal constructor */
	public BaseSubdefinition(Boolean deleted, List analyticalNotes,
			org.thdl.lex.component.Meta meta, List translations, List glosses,
			List keywords, List modelSentences, List translationEquivalents,
			List relatedTerms, List passages, List registers) {
		super(deleted, analyticalNotes, meta);
		this.translations = translations;
		this.glosses = glosses;
		this.keywords = keywords;
		this.modelSentences = modelSentences;
		this.translationEquivalents = translationEquivalents;
		this.relatedTerms = relatedTerms;
		this.passages = passages;
		this.registers = registers;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getSubdefinition() {
		return this.subdefinition;
	}

	public void setSubdefinition(String subdefinition) {
		this.subdefinition = subdefinition;
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

	public List getGlosses() {
		return this.glosses;
	}

	public void setGlosses(List glosses) {
		this.glosses = glosses;
	}

	public List getKeywords() {
		return this.keywords;
	}

	public void setKeywords(List keywords) {
		this.keywords = keywords;
	}

	public List getModelSentences() {
		return this.modelSentences;
	}

	public void setModelSentences(List modelSentences) {
		this.modelSentences = modelSentences;
	}

	public List getTranslationEquivalents() {
		return this.translationEquivalents;
	}

	public void setTranslationEquivalents(List translationEquivalents) {
		this.translationEquivalents = translationEquivalents;
	}

	public List getRelatedTerms() {
		return this.relatedTerms;
	}

	public void setRelatedTerms(List relatedTerms) {
		this.relatedTerms = relatedTerms;
	}

	public List getPassages() {
		return this.passages;
	}

	public void setPassages(List passages) {
		this.passages = passages;
	}

	public List getRegisters() {
		return this.registers;
	}

	public void setRegisters(List registers) {
		this.registers = registers;
	}

	public String toString() {
		return new ToStringBuilder(this).append("metaId", getMetaId())
				.toString();
	}

}