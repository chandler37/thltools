package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BaseSpeechRegister extends LexComponent implements org.thdl.lex.component.IRegister,Serializable {

    /** nullable persistent field */
    private java.lang.Integer parentId;

    /** nullable persistent field */
    private java.lang.Short precedence;

    /** persistent field */
    private java.lang.Short register;

    /** nullable persistent field */
    private org.thdl.lex.component.ILexComponent parent;

    /** full constructor */
    public BaseSpeechRegister(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.Integer parentId, java.lang.Short precedence, java.lang.Short register, org.thdl.lex.component.ILexComponent parent) {
        super(deleted, analyticalNotes, meta);
        this.parentId = parentId;
        this.precedence = precedence;
        this.register = register;
        this.parent = parent;
    }

    /** default constructor */
    public BaseSpeechRegister() {
    }

    /** minimal constructor */
    public BaseSpeechRegister(java.lang.Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, java.lang.Short register) {
      super(deleted, analyticalNotes, meta);
        this.register = register;
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

    public java.lang.Short getRegister() {
        return this.register;
    }

    public void setRegister(java.lang.Short register) {
        this.register = register;
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
