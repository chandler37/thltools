package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseTerm extends LexComponent implements
		org.thdl.lex.component.ITerm, Serializable,
		org.thdl.lex.component.LexComponentNode {

	/** persistent field */
	private String term;

	/** persistent field */
	private List pronunciations;

	/** persistent field */
	private List etymologies;

	/** persistent field */
	private List spellings;

	/** persistent field */
	private List functions;

	/** persistent field */
	private List encyclopediaArticles;

	/** persistent field */
	private List transitionalData;

	/** persistent field */
	private List definitions;

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
	public BaseTerm(Boolean deleted, List analyticalNotes,
			org.thdl.lex.component.Meta meta, String term, List pronunciations,
			List etymologies, List spellings, List functions,
			List encyclopediaArticles, List transitionalData, List definitions,
			List glosses, List keywords, List modelSentences,
			List translationEquivalents, List relatedTerms, List passages,
			List registers) {
		super(deleted, analyticalNotes, meta);
		this.term = term;
		this.pronunciations = pronunciations;
		this.etymologies = etymologies;
		this.spellings = spellings;
		this.functions = functions;
		this.encyclopediaArticles = encyclopediaArticles;
		this.transitionalData = transitionalData;
		this.definitions = definitions;
		this.glosses = glosses;
		this.keywords = keywords;
		this.modelSentences = modelSentences;
		this.translationEquivalents = translationEquivalents;
		this.relatedTerms = relatedTerms;
		this.passages = passages;
		this.registers = registers;
	}

	/** default constructor */
	public BaseTerm() {
	}

	public String getTerm() {
		return this.term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public List getPronunciations() {
		return this.pronunciations;
	}

	public void setPronunciations(List pronunciations) {
		this.pronunciations = pronunciations;
	}

	public List getEtymologies() {
		return this.etymologies;
	}

	public void setEtymologies(List etymologies) {
		this.etymologies = etymologies;
	}

	public List getSpellings() {
		return this.spellings;
	}

	public void setSpellings(List spellings) {
		this.spellings = spellings;
	}

	public List getFunctions() {
		return this.functions;
	}

	public void setFunctions(List functions) {
		this.functions = functions;
	}

	public List getEncyclopediaArticles() {
		return this.encyclopediaArticles;
	}

	public void setEncyclopediaArticles(List encyclopediaArticles) {
		this.encyclopediaArticles = encyclopediaArticles;
	}

	public List getTransitionalData() {
		return this.transitionalData;
	}

	public void setTransitionalData(List transitionalData) {
		this.transitionalData = transitionalData;
	}

	public List getDefinitions() {
		return this.definitions;
	}

	public void setDefinitions(List definitions) {
		this.definitions = definitions;
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