package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseTransitionalData extends LexComponent implements org.thdl.lex.component.ITransitionalData,Serializable {

    /** nullable persistent field */
    private java.lang.Integer parentId;

    /** nullable persistent field */
    private java.lang.Short precedence;

    /** nullable persistent field */
    private java.lang.Short transitionalDataLabel;

    /** persistent field */
    private java.lang.String forPublicConsumption;

    /** nullable persistent field */
    private java.lang.String transitionalDataText;

    /** nullable persistent field */
    private org.thdl.lex.component.ILexComponent parent;

    /** full constructor */
    public BaseTransitionalData(java.lang.Integer translationOf, java.lang.Boolean deleted, List analyticalNotes, Set translations, org.thdl.lex.component.Meta meta, java.lang.Integer parentId, java.lang.Short precedence, java.lang.Short transitionalDataLabel, java.lang.String forPublicConsumption, java.lang.String transitionalDataText, org.thdl.lex.component.ILexComponent parent) {
        super(translationOf, deleted, analyticalNotes, translations, meta);
        this.parentId = parentId;
        this.precedence = precedence;
        this.transitionalDataLabel = transitionalDataLabel;
        this.forPublicConsumption = forPublicConsumption;
        this.transitionalDataText = transitionalDataText;
        this.parent = parent;
    }

    /** default constructor */
    public BaseTransitionalData() {
    }

    /** minimal constructor */
    public BaseTransitionalData(java.lang.Boolean deleted, List analyticalNotes, Set translations, org.thdl.lex.component.Meta meta, java.lang.String forPublicConsumption) {
      super(deleted, analyticalNotes, translations, meta);
        this.forPublicConsumption = forPublicConsumption;
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

    public java.lang.Short getTransitionalDataLabel() {
        return this.transitionalDataLabel;
    }

    public void setTransitionalDataLabel(java.lang.Short transitionalDataLabel) {
        this.transitionalDataLabel = transitionalDataLabel;
    }

    public java.lang.String getForPublicConsumption() {
        return this.forPublicConsumption;
    }

    public void setForPublicConsumption(java.lang.String forPublicConsumption) {
        this.forPublicConsumption = forPublicConsumption;
    }

    public java.lang.String getTransitionalDataText() {
        return this.transitionalDataText;
    }

    public void setTransitionalDataText(java.lang.String transitionalDataText) {
        this.transitionalDataText = transitionalDataText;
    }

    public org.thdl.lex.component.ILexComponent getParent() {
        return this.parent;
    }

    public void setParent(org.thdl.lex.component.ILexComponent parent) {
        this.parent = parent;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("metaId", getMetaId())
            .toString();
    }

}
