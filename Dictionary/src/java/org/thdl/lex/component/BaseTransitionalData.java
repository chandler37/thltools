package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseTransitionalData extends LexComponent implements org.thdl.lex.component.ITransitionalData,Serializable {

    /** nullable persistent field */
    private Integer parentId;

    /** nullable persistent field */
    private Integer transitionalDataLabel;

    /** persistent field */
    private String forPublicConsumption;

    /** nullable persistent field */
    private String transitionalDataText;

    /** nullable persistent field */
    private org.thdl.lex.component.ILexComponent parent;

    /** full constructor */
    public BaseTransitionalData(Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, Integer parentId, Integer transitionalDataLabel, String forPublicConsumption, String transitionalDataText, org.thdl.lex.component.ILexComponent parent) {
        super(deleted, analyticalNotes, meta);
        this.parentId = parentId;
        this.transitionalDataLabel = transitionalDataLabel;
        this.forPublicConsumption = forPublicConsumption;
        this.transitionalDataText = transitionalDataText;
        this.parent = parent;
    }

    /** default constructor */
    public BaseTransitionalData() {
    }

    /** minimal constructor */
    public BaseTransitionalData(Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, String forPublicConsumption) {
      super(deleted, analyticalNotes, meta);
        this.forPublicConsumption = forPublicConsumption;
    }

    public Integer getParentId() {
        return this.parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getTransitionalDataLabel() {
        return this.transitionalDataLabel;
    }

    public void setTransitionalDataLabel(Integer transitionalDataLabel) {
        this.transitionalDataLabel = transitionalDataLabel;
    }

    public String getForPublicConsumption() {
        return this.forPublicConsumption;
    }

    public void setForPublicConsumption(String forPublicConsumption) {
        this.forPublicConsumption = forPublicConsumption;
    }

    public String getTransitionalDataText() {
        return this.transitionalDataText;
    }

    public void setTransitionalDataText(String transitionalDataText) {
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
