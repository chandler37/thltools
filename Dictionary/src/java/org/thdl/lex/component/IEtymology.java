package org.thdl.lex.component;

public interface IEtymology  extends ILexComponent
{	public ILexComponent getParent();
	public void setParent( ILexComponent comp );
	public java.lang.Integer getParentId();
	public void setParentId( java.lang.Integer parentId );
	public java.lang.Short getLoanLanguage();
	public void setLoanLanguage(java.lang.Short loanLanguage);
	public java.lang.Short getEtymologyType();
	public void setEtymologyType(java.lang.Short etymologyType);
	public java.lang.String getDerivation();
	public void setDerivation(java.lang.String derivation);
	public java.lang.String getEtymologyDescription();
	public void setEtymologyDescription(java.lang.String etymologyDescription);
}

