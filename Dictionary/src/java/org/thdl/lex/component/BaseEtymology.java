package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseEtymology extends LexComponent implements Serializable,org.thdl.lex.component.IEtymology,org.thdl.lex.component.Translatable {

    /** nullable persistent field */
    private java.lang.Integer parentId;

    /** nullable persistent field */
    private java.lang.Short precedence;

    /** nullable persistent field */
    private java.lang.Short loanLanguage;

    /** persistent field */
    private java.lang.Short etymologyType;

    /** persistent field */
    private java.lang.String derivation;

    /** persistent field */
    private java.lang.String etymologyDescription;

    /** nullable persistent field */
    private java.lang.Integer translationOf;

    /** nullable persistent field */
    private org.thdl.lex.component.ILexComponent parent;

    /** persistent field */
    private Set translations;

    /** full constructor */
    public BaseEtymology(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.Integer parentId, java.lang.Short precedence, java.lang.Short loanLanguage, java.lang.Short etymologyType, java.lang.String derivation, java.lang.String etymologyDescription, java.lang.Integer translationOf, org.thdl.lex.component.ILexComponent parent, Set translations) {
        super(deleted, analyticalNotes, meta);
        this.parentId = parentId;
        this.precedence = precedence;
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
    public BaseEtymology(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.Short etymologyType, java.lang.String derivation, java.lang.String etymologyDescription, Set translations) {
      super(deleted, analyticalNotes, meta);
        this.etymologyType = etymologyType;
        this.derivation = derivation;
        this.etymologyDescription = etymologyDescription;
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

    public java.lang.Short getLoanLanguage() {
        return this.loanLanguage;
    }

    public void setLoanLanguage(java.lang.Short loanLanguage) {
        this.loanLanguage = loanLanguage;
    }

    public java.lang.Short getEtymologyType() {
        return this.etymologyType;
    }

    public void setEtymologyType(java.lang.Short etymologyType) {
        this.etymologyType = etymologyType;
    }

    public java.lang.String getDerivation() {
        return this.derivation;
    }

    public void setDerivation(java.lang.String derivation) {
        this.derivation = derivation;
    }

    public java.lang.String getEtymologyDescription() {
        return this.etymologyDescription;
    }

    public void setEtymologyDescription(java.lang.String etymologyDescription) {
        this.etymologyDescription = etymologyDescription;
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