package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseSubdefinition extends LexComponent implements org.thdl.lex.component.ISubdefinition,org.thdl.lex.component.Translatable, org.thdl.lex.component.LexComponentNode,Serializable {

    /** nullable persistent field */
    private java.lang.Integer parentId;

    /** nullable persistent field */
    private java.lang.Integer precedence;

    /** nullable persistent field */
    private java.lang.String subdefinition;

    /** nullable persistent field */
    private java.lang.Integer translationOf;

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
    public BaseSubdefinition(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.Integer parentId, java.lang.Integer precedence, java.lang.String subdefinition, java.lang.Integer translationOf, org.thdl.lex.component.ILexComponent parent, List translations, List glosses, List keywords, List modelSentences, List translationEquivalents, List relatedTerms, List passages, List registers) {
        super(deleted, analyticalNotes, meta);
        this.parentId = parentId;
        this.precedence = precedence;
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
    public BaseSubdefinition(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, List translations, List glosses, List keywords, List modelSentences, List translationEquivalents, List relatedTerms, List passages, List registers) {
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

    public java.lang.Integer getParentId() {
        return this.parentId;
    }

    public void setParentId(java.lang.Integer parentId) {
        this.parentId = parentId;
    }

    public java.lang.Integer getPrecedence() {
        return this.precedence;
    }

    public void setPrecedence(java.lang.Integer precedence) {
        this.precedence = precedence;
    }

    public java.lang.String getSubdefinition() {
        return this.subdefinition;
    }

    public void setSubdefinition(java.lang.String subdefinition) {
        this.subdefinition = subdefinition;
    }

    public java.lang.Integer getTranslationOf() {
        return this.translationOf;
    }

    public void setTranslationOf(java.lang.Integer translationOf) {
        this.translationOf = translationOf;
    }

    public org.thdl.lex.component.ILexComponent getParent() {
        return this.parent;
    }

    public void setParent(org.thdl.lex.component.ILexComponent parent) {
        this.parent = parent;
    }

    public java.util.List getTranslations() {
        return this.translations;
    }

    public void setTranslations(java.util.List translations) {
        this.translations = translations;
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
