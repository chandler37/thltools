package org.thdl.lex.component.child;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class LiterarySource implements Serializable {

    /** identifier field */
    private java.lang.Integer id;

    /** nullable persistent field */
    private java.lang.Integer literaryPeriod;

    /** nullable persistent field */
    private java.lang.Integer literaryGenre;

    /** nullable persistent field */
    private java.lang.Integer literaryForm;

    /** nullable persistent field */
    private java.lang.Integer author;

    /** nullable persistent field */
    private java.lang.String sourceNormalizedTitle;

    /** nullable persistent field */
    private java.lang.String tibetanDate;

    /** nullable persistent field */
    private java.lang.String internationalDate;

    /** nullable persistent field */
    private java.lang.String edition;

    /** nullable persistent field */
    private java.lang.String publisher;

    /** nullable persistent field */
    private java.lang.String isbn;

    /** nullable persistent field */
    private java.util.Date yearPublished;

    /** persistent field */
    private java.lang.Integer volumeNumber;

    /** nullable persistent field */
    private java.lang.String pagination;

    /** full constructor */
    public LiterarySource(java.lang.Integer literaryPeriod, java.lang.Integer literaryGenre, java.lang.Integer literaryForm, java.lang.Integer author, java.lang.String sourceNormalizedTitle, java.lang.String tibetanDate, java.lang.String internationalDate, java.lang.String edition, java.lang.String publisher, java.lang.String isbn, java.util.Date yearPublished, java.lang.Integer volumeNumber, java.lang.String pagination) {
        this.literaryPeriod = literaryPeriod;
        this.literaryGenre = literaryGenre;
        this.literaryForm = literaryForm;
        this.author = author;
        this.sourceNormalizedTitle = sourceNormalizedTitle;
        this.tibetanDate = tibetanDate;
        this.internationalDate = internationalDate;
        this.edition = edition;
        this.publisher = publisher;
        this.isbn = isbn;
        this.yearPublished = yearPublished;
        this.volumeNumber = volumeNumber;
        this.pagination = pagination;
    }

    /** default constructor */
    public LiterarySource() {
    }

    /** minimal constructor */
    public LiterarySource(java.lang.Integer volumeNumber) {
        this.volumeNumber = volumeNumber;
    }

    public java.lang.Integer getId() {
        return this.id;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    public java.lang.Integer getLiteraryPeriod() {
        return this.literaryPeriod;
    }

    public void setLiteraryPeriod(java.lang.Integer literaryPeriod) {
        this.literaryPeriod = literaryPeriod;
    }

    public java.lang.Integer getLiteraryGenre() {
        return this.literaryGenre;
    }

    public void setLiteraryGenre(java.lang.Integer literaryGenre) {
        this.literaryGenre = literaryGenre;
    }

    public java.lang.Integer getLiteraryForm() {
        return this.literaryForm;
    }

    public void setLiteraryForm(java.lang.Integer literaryForm) {
        this.literaryForm = literaryForm;
    }

    public java.lang.Integer getAuthor() {
        return this.author;
    }

    public void setAuthor(java.lang.Integer author) {
        this.author = author;
    }

    public java.lang.String getSourceNormalizedTitle() {
        return this.sourceNormalizedTitle;
    }

    public void setSourceNormalizedTitle(java.lang.String sourceNormalizedTitle) {
        this.sourceNormalizedTitle = sourceNormalizedTitle;
    }

    public java.lang.String getTibetanDate() {
        return this.tibetanDate;
    }

    public void setTibetanDate(java.lang.String tibetanDate) {
        this.tibetanDate = tibetanDate;
    }

    public java.lang.String getInternationalDate() {
        return this.internationalDate;
    }

    public void setInternationalDate(java.lang.String internationalDate) {
        this.internationalDate = internationalDate;
    }

    public java.lang.String getEdition() {
        return this.edition;
    }

    public void setEdition(java.lang.String edition) {
        this.edition = edition;
    }

    public java.lang.String getPublisher() {
        return this.publisher;
    }

    public void setPublisher(java.lang.String publisher) {
        this.publisher = publisher;
    }

    public java.lang.String getIsbn() {
        return this.isbn;
    }

    public void setIsbn(java.lang.String isbn) {
        this.isbn = isbn;
    }

    public java.util.Date getYearPublished() {
        return this.yearPublished;
    }

    public void setYearPublished(java.util.Date yearPublished) {
        this.yearPublished = yearPublished;
    }

    public java.lang.Integer getVolumeNumber() {
        return this.volumeNumber;
    }

    public void setVolumeNumber(java.lang.Integer volumeNumber) {
        this.volumeNumber = volumeNumber;
    }

    public java.lang.String getPagination() {
        return this.pagination;
    }

    public void setPagination(java.lang.String pagination) {
        this.pagination = pagination;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof LiterarySource) ) return false;
        LiterarySource castOther = (LiterarySource) other;
        return new EqualsBuilder()
            .append(this.getId(), castOther.getId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getId())
            .toHashCode();
    }

}
