package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseTerm extends LexComponent implements org.thdl.lex.component.ITerm,Serializable,org.thdl.lex.component.LexComponentNode {

    /** persistent field */
    private java.lang.String term;

    /** nullable persistent field */
    private java.lang.Integer precedence;

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
    public BaseTerm(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.String term, java.lang.Integer precedence, List pronunciations, List etymologies, List spellings, List functions, List encyclopediaArticles, List transitionalData, List definitions, List glosses, List keywords, List modelSentences, List translationEquivalents, List relatedTerms, List passages, List registers) {
        super(deleted, analyticalNotes, meta);
        this.term = term;
        this.precedence = precedence;
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

    /** minimal constructor */
    public BaseTerm(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.String term, List pronunciations, List etymologies, List spellings, List functions, List encyclopediaArticles, List transitionalData, List definitions, List glosses, List keywords, List modelSentences, List translationEquivalents, List relatedTerms, List passages, List registers) {
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

    public java.lang.String getTerm() {
        return this.term;
    }

    public void setTerm(java.lang.String term) {
        this.term = term;
    }

    public java.lang.Integer getPrecedence() {
        return this.precedence;
    }

    public void setPrecedence(java.lang.Integer precedence) {
        this.precedence = precedence;
    }

    public java.util.List getPronunciations() {
        return this.pronunciations;
    }

    public void setPronunciations(java.util.List pronunciations) {
        this.pronunciations = pronunciations;
    }

    public java.util.List getEtymologies() {
        return this.etymologies;
    }

    public void setEtymologies(java.util.List etymologies) {
        this.etymologies = etymologies;
    }

    public java.util.List getSpellings() {
        return this.spellings;
    }

    public void setSpellings(java.util.List spellings) {
        this.spellings = spellings;
    }

    public java.util.List getFunctions() {
        return this.functions;
    }

    public void setFunctions(java.util.List functions) {
        this.functions = functions;
    }

    public java.util.List getEncyclopediaArticles() {
        return this.encyclopediaArticles;
    }

    public void setEncyclopediaArticles(java.util.List encyclopediaArticles) {
        this.encyclopediaArticles = encyclopediaArticles;
    }

    public java.util.List getTransitionalData() {
        return this.transitionalData;
    }

    public void setTransitionalData(java.util.List transitionalData) {
        this.transitionalData = transitionalData;
    }

    public java.util.List getDefinitions() {
        return this.definitions;
    }

    public void setDefinitions(java.util.List definitions) {
        this.definitions = definitions;
    }

    public java.util.List getGlosses() {
        return this.glosses;
    }

    public void setGlosses(java.util.List glosses) {
        this.glosses = glosses;
    }

    public java.util.List getKeywords() {
        return this.keywords;
    }

    public void setKeywords(java.util.List keywords) {
        this.keywords = keywords;
    }

    public java.util.List getModelSentences() {
        return this.modelSentences;
    }

    public void setModelSentences(java.util.List modelSentences) {
        this.modelSentences = modelSentences;
    }

    public java.util.List getTranslationEquivalents() {
        return this.translationEquivalents;
    }

    public void setTranslationEquivalents(java.util.List translationEquivalents) {
        this.translationEquivalents = translationEquivalents;
    }

    public java.util.List getRelatedTerms() {
        return this.relatedTerms;
    }

    public void setRelatedTerms(java.util.List relatedTerms) {
        this.relatedTerms = relatedTerms;
    }

    public java.util.List getPassages() {
        return this.passages;
    }

    public void setPassages(java.util.List passages) {
        this.passages = passages;
    }

    public java.util.List getRegisters() {
        return this.registers;
    }

    public void setRegisters(java.util.List registers) {
        this.registers = registers;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("metaId", getMetaId())
            .toString();
    }

}
