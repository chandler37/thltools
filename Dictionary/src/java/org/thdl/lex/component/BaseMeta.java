package org.thdl.lex.component;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseMeta implements Serializable {

	/** persistent field */
	private Integer createdBy;

	/** persistent field */
	private Integer modifiedBy;

	/** persistent field */
	private Integer createdByProjSub;

	/** persistent field */
	private Integer modifiedByProjSub;

	/** nullable persistent field */
	private Date createdOn;

	/** nullable persistent field */
	private Date modifiedOn;

	/** persistent field */
	private Integer source;

	/** persistent field */
	private Integer language;

	/** persistent field */
	private Integer script;

	/** persistent field */
	private Integer dialect;

	/** nullable persistent field */
	private String note;

	/** full constructor */
	public BaseMeta(Integer createdBy, Integer modifiedBy,
			Integer createdByProjSub, Integer modifiedByProjSub,
			Date createdOn, Date modifiedOn, Integer source, Integer language,
			Integer script, Integer dialect, String note) {
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
	public BaseMeta(Integer createdBy, Integer modifiedBy,
			Integer createdByProjSub, Integer modifiedByProjSub,
			Integer source, Integer language, Integer script, Integer dialect) {
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.createdByProjSub = createdByProjSub;
		this.modifiedByProjSub = modifiedByProjSub;
		this.source = source;
		this.language = language;
		this.script = script;
		this.dialect = dialect;
	}

	public Integer getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Integer getCreatedByProjSub() {
		return this.createdByProjSub;
	}

	public void setCreatedByProjSub(Integer createdByProjSub) {
		this.createdByProjSub = createdByProjSub;
	}

	public Integer getModifiedByProjSub() {
		return this.modifiedByProjSub;
	}

	public void setModifiedByProjSub(Integer modifiedByProjSub) {
		this.modifiedByProjSub = modifiedByProjSub;
	}

	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getModifiedOn() {
		return this.modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public Integer getSource() {
		return this.source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Integer getLanguage() {
		return this.language;
	}

	public void setLanguage(Integer language) {
		this.language = language;
	}

	public Integer getScript() {
		return this.script;
	}

	public void setScript(Integer script) {
		this.script = script;
	}

	public Integer getDialect() {
		return this.dialect;
	}

	public void setDialect(Integer dialect) {
		this.dialect = dialect;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String toString() {
		return new ToStringBuilder(this).toString();
	}

}