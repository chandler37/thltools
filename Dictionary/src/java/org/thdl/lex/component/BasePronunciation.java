package org.thdl.lex.component;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
abstract public class BasePronunciation extends LexComponent implements org.thdl.lex.component.IPronunciation,Serializable {

    /** nullable persistent field */
    private Integer parentId;

    /** persistent field */
    private String phonetics;

    /** persistent field */
    private Integer phoneticsType;

    /** nullable persistent field */
    private org.thdl.lex.component.ILexComponent parent;

    /** full constructor */
    public BasePronunciation(Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, Integer parentId, String phonetics, Integer phoneticsType, org.thdl.lex.component.ILexComponent parent) {
        super(deleted, analyticalNotes, meta);
        this.parentId = parentId;
        this.phonetics = phonetics;
        this.phoneticsType = phoneticsType;
        this.parent = parent;
    }

    /** default constructor */
    public BasePronunciation() {
    }

    /** minimal constructor */
    public BasePronunciation(Boolean deleted, List analyticalNotes, org.thdl.lex.component.Meta meta, String phonetics, Integer phoneticsType) {
      super(deleted, analyticalNotes, meta);
        this.phonetics = phonetics;
        this.phoneticsType = phoneticsType;
    }

    public Integer getParentId() {
        return this.parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getPhonetics() {
        return this.phonetics;
    }

    public void setPhonetics(String phonetics) {
        this.phonetics = phonetics;
    }

    public Integer getPhoneticsType() {
        return this.phoneticsType;
    }

    public void setPhoneticsType(Integer phoneticsType) {
        this.phoneticsType = phoneticsType;
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
