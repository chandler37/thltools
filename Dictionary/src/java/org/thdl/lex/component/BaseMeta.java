package org.thdl.lex.component;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseMeta implements Serializable {

    /** persistent field */
    private java.lang.Integer createdBy;

    /** persistent field */
    private java.lang.Integer modifiedBy;

    /** persistent field */
    private java.lang.Integer createdByProjSub;

    /** persistent field */
    private java.lang.Integer modifiedByProjSub;

    /** nullable persistent field */
    private java.util.Date createdOn;

    /** nullable persistent field */
    private java.util.Date modifiedOn;

    /** persistent field */
    private java.lang.Integer source;

    /** persistent field */
    private java.lang.Integer language;

    /** persistent field */
    private java.lang.Integer script;

    /** persistent field */
    private java.lang.Integer dialect;

    /** nullable persistent field */
    private java.lang.String note;

    /** full constructor */
    public BaseMeta(java.lang.Integer createdBy, java.lang.Integer modifiedBy, java.lang.Integer createdByProjSub, java.lang.Integer modifiedByProjSub, java.util.Date createdOn, java.util.Date modifiedOn, java.lang.Integer source, java.lang.Integer language, java.lang.Integer script, java.lang.Integer dialect, java.lang.String note) {
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.createdByProjSub = createdByProjSub;
        this.modifiedByProjSub = modifiedByProjSub;
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
        this.source = source;
        this.language = language;
        this.script = script;
        this.dialect = dialect;
        this.note = note;
    }

    /** default constructor */
    public BaseMeta() {
    }

    /** minimal constructor */
    public BaseMeta(java.lang.Integer createdBy, java.lang.Integer modifiedBy, java.lang.Integer createdByProjSub, java.lang.Integer modifiedByProjSub, java.lang.Integer source, java.lang.Integer language, java.lang.Integer script, java.lang.Integer dialect) {
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.createdByProjSub = createdByProjSub;
        this.modifiedByProjSub = modifiedByProjSub;
        this.source = source;
        this.language = language;
        this.script = script;
        this.dialect = dialect;
    }

    public java.lang.Integer getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(java.lang.Integer createdBy) {
        this.createdBy = createdBy;
    }

    public java.lang.Integer getModifiedBy() {
        return this.modifiedBy;
    }

    public void setModifiedBy(java.lang.Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public java.lang.Integer getCreatedByProjSub() {
        return this.createdByProjSub;
    }

    public void setCreatedByProjSub(java.lang.Integer createdByProjSub) {
        this.createdByProjSub = createdByProjSub;
    }

    public java.lang.Integer getModifiedByProjSub() {
        return this.modifiedByProjSub;
    }

    public void setModifiedByProjSub(java.lang.Integer modifiedByProjSub) {
        this.modifiedByProjSub = modifiedByProjSub;
    }

    public java.util.Date getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(java.util.Date createdOn) {
        this.createdOn = createdOn;
    }

    public java.util.Date getModifiedOn() {
        return this.modifiedOn;
    }

    public void setModifiedOn(java.util.Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public java.lang.Integer getSource() {
        return this.source;
    }

    public void setSource(java.lang.Integer source) {
        this.source = source;
    }

    public java.lang.Integer getLanguage() {
        return this.language;
    }

    public void setLanguage(java.lang.Integer language) {
        this.language = language;
    }

    public java.lang.Integer getScript() {
        return this.script;
    }

    public void setScript(java.lang.Integer script) {
        this.script = script;
    }

    public java.lang.Integer getDialect() {
        return this.dialect;
    }

    public void setDialect(java.lang.Integer dialect) {
        this.dialect = dialect;
    }

    public java.lang.String getNote() {
        return this.note;
    }

    public void setNote(java.lang.String note) {
        this.note = note;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .toString();
    }

}
