package org.thdl.lex.component.child;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ProjectSubject implements Serializable {

    /** identifier field */
    private java.lang.Integer id;

    /** nullable persistent field */
    private java.lang.String projectSubject;

    /** persistent field */
    private java.lang.Integer leader;

    /** nullable persistent field */
    private java.lang.String participantList;

    /** full constructor */
    public ProjectSubject(java.lang.String projectSubject, java.lang.Integer leader, java.lang.String participantList) {
        this.projectSubject = projectSubject;
        this.leader = leader;
        this.participantList = participantList;
    }

    /** default constructor */
    public ProjectSubject() {
    }

    /** minimal constructor */
    public ProjectSubject(java.lang.Integer leader) {
        this.leader = leader;
    }

    public java.lang.Integer getId() {
        return this.id;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    public java.lang.String getProjectSubject() {
        return this.projectSubject;
    }

    public void setProjectSubject(java.lang.String projectSubject) {
        this.projectSubject = projectSubject;
    }

    public java.lang.Integer getLeader() {
        return this.leader;
    }

    public void setLeader(java.lang.Integer leader) {
        this.leader = leader;
    }

    public java.lang.String getParticipantList() {
        return this.participantList;
    }

    public void setParticipantList(java.lang.String participantList) {
        this.participantList = participantList;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof ProjectSubject) ) return false;
        ProjectSubject castOther = (ProjectSubject) other;
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
