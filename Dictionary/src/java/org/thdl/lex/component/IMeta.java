package org.thdl.lex.component;

public interface IMeta extends ILexComponent
{	public ILexComponent getParent();
	public void setParent( ILexComponent comp );
	public java.lang.Integer getParentId();
	public void setParentId( java.lang.Integer parentId );
	public java.lang.Integer getCreatedBy();
	public void setCreatedBy(java.lang.Integer createdBy);
	public java.lang.Integer getModifiedBy();
	public void setModifiedBy(java.lang.Integer modifiedBy);
	public java.lang.Integer getCreatedByProjSub();
	public void setCreatedByProjSub(java.lang.Integer createdByProjSub);
	public java.lang.Integer getModifiedByProjSub();
	public void setModifiedByProjSub(java.lang.Integer modifiedByProjSub);
	public java.util.Date getCreatedOn();
	public void setCreatedOn(java.util.Date createdOn);
	public java.util.Date getModifiedOn();
	public void setModifiedOn(java.util.Date modifiedOn);
	public java.lang.Integer getSource();
	public void setSource(java.lang.Integer source);
	public java.lang.Short getLanguage();
	public void setLanguage(java.lang.Short language);
	public java.lang.Short getScript();
	public void setScript(java.lang.Short script);
	public java.lang.Short getDialect();
	public void setDialect(java.lang.Short dialect);
	public java.lang.String getNote();
	public void setNote(java.lang.String note);
}

