package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseDefinition extends LexComponent implements org.thdl.lex.component.IDefinition,Serializable,org.thdl.lex.component.Translatable {

    /** nullable persistent field */
    private java.lang.Integer parentId;

    /** persistent field */
    private java.lang.Short precedence;

    /** nullable persistent field */
    private java.lang.String definition;

    /** nullable persistent field */
    private java.lang.Integer translationOf;

    /** nullable persistent field */
    private org.thdl.lex.component.ILexComponent parent;

    /** persistent field */
    private List subdefinitions;

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

    /** persistent field */
    private Set translations;

    /** full constructor */
    public BaseDefinition(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.Integer parentId, java.lang.Short precedence, java.lang.String definition, java.lang.Integer translationOf, org.thdl.lex.component.ILexComponent parent, List subdefinitions, List glosses, List keywords, List modelSentences, List translationEquivalents, List relatedTerms, List passages, List registers, Set translations) {
        super(deleted, analyticalNotes, meta);
        this.parentId = parentId;
        this.precedence = precedence;
        this.definition = definition;
        this.translationOf = translationOf;
        this.parent = parent;
        this.subdefinitions = subdefinitions;
        this.glosses = glosses;
        this.keywords = keywords;
        this.modelSentences = modelSentences;
        this.translationEquivalents = translationEquivalents;
        this.relatedTerms = relatedTerms;
        this.passages = passages;
        this.registers = registers;
        this.translations = translations;
    }

    /** default constructor */
    public BaseDefinition() {
    }

    /** minimal constructor */
    public BaseDefinition(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.Short precedence, List subdefinitions, List glosses, List keywords, List modelSentences, List translationEquivalents, List relatedTerms, List passages, List registers, Set translations) {
      super(deleted, analyticalNotes, meta);
        this.precedence = precedence;
        this.subdefinitions = subdefinitions;
        this.glosses = glosses;
        this.keywords = keywords;
        this.modelSentences = modelSentences;
        this.translationEquivalents = translationEquivalents;
        this.relatedTerms = relatedTerms;
        this.passages = passages;
        this.registers = registers;
        this.translations = translations;
    }

    public java.lang.Integer getParentId() {
        return this.parentId;
    }

    public void setParentId(java.lang.Integer parentId) {
        this.parentId = parentId;
    }

    public java.lang.Short getPrecedence() {
        return this.precedence;
    }

    public void setPrecedence(java.lang.Short precedence) {
        this.precedence = precedence;
    }

    public java.lang.String getDefinition() {
        return this.definition;
    }

    public void setDefinition(java.lang.String definition) {
        this.definition = definition;
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

    public java.util.List getSubdefinitions() {
        return this.subdefinitions;
    }

    public void setSubdefinitions(java.util.List subdefinitions) {
        this.subdefinitions = subdefinitions;
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

    public java.util.Set getTranslations() {
        return this.translations;
    }

    public void setTranslations(java.util.Set translations) {
        this.translations = translations;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("metaId", getMetaId())
            .toString();
    }

}
