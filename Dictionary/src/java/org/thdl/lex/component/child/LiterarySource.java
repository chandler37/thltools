package org.thdl.lex.component.child;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class LiterarySource implements Serializable {

	/** identifier field */
	private Integer id;

	/** nullable persistent field */
	private Integer literaryPeriod;

	/** nullable persistent field */
	private Integer literaryGenre;

	/** nullable persistent field */
	private Integer literaryForm;

	/** nullable persistent field */
	private Integer author;

	/** nullable persistent field */
	private String sourceNormalizedTitle;

	/** nullable persistent field */
	private String tibetanDate;

	/** nullable persistent field */
	private String internationalDate;

	/** nullable persistent field */
	private String edition;

	/** nullable persistent field */
	private String publisher;

	/** nullable persistent field */
	private String isbn;

	/** nullable persistent field */
	private Date yearPublished;

	/** persistent field */
	private Integer volumeNumber;

	/** nullable persistent field */
	private String pagination;

	/** full constructor */
	public LiterarySource(Integer literaryPeriod, Integer literaryGenre,
			Integer literaryForm, Integer author, String sourceNormalizedTitle,
			String tibetanDate, String internationalDate, String edition,
			String publisher, String isbn, Date yearPublished,
			Integer volumeNumber, String pagination) {
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
	public LiterarySource(Integer volumeNumber) {
		this.volumeNumber = volumeNumber;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLiteraryPeriod() {
		return this.literaryPeriod;
	}

	public void setLiteraryPeriod(Integer literaryPeriod) {
		this.literaryPeriod = literaryPeriod;
	}

	public Integer getLiteraryGenre() {
		return this.literaryGenre;
	}

	public void setLiteraryGenre(Integer literaryGenre) {
		this.literaryGenre = literaryGenre;
	}

	public Integer getLiteraryForm() {
		return this.literaryForm;
	}

	public void setLiteraryForm(Integer literaryForm) {
		this.literaryForm = literaryForm;
	}

	public Integer getAuthor() {
		return this.author;
	}

	public void setAuthor(Integer author) {
		this.author = author;
	}

	public String getSourceNormalizedTitle() {
		return this.sourceNormalizedTitle;
	}

	public void setSourceNormalizedTitle(String sourceNormalizedTitle) {
		this.sourceNormalizedTitle = sourceNormalizedTitle;
	}

	public String getTibetanDate() {
		return this.tibetanDate;
	}

	public void setTibetanDate(String tibetanDate) {
		this.tibetanDate = tibetanDate;
	}

	public String getInternationalDate() {
		return this.internationalDate;
	}

	public void setInternationalDate(String internationalDate) {
		this.internationalDate = internationalDate;
	}

	public String getEdition() {
		return this.edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getPublisher() {
		return this.publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getIsbn() {
		return this.isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Date getYearPublished() {
		return this.yearPublished;
	}

	public void setYearPublished(Date yearPublished) {
		this.yearPublished = yearPublished;
	}

	public Integer getVolumeNumber() {
		return this.volumeNumber;
	}

	public void setVolumeNumber(Integer volumeNumber) {
		this.volumeNumber = volumeNumber;
	}

	public String getPagination() {
		return this.pagination;
	}

	public void setPagination(String pagination) {
		this.pagination = pagination;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

	public boolean equals(Object other) {
		if (!(other instanceof LiterarySource))
			return false;
		LiterarySource castOther = (LiterarySource) other;
		return new EqualsBuilder().append(this.getId(), castOther.getId())
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

}