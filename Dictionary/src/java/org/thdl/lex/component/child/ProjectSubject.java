package org.thdl.lex.component.child;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ProjectSubject implements Serializable {

    /** identifier field */
    private Integer id;

    /** nullable persistent field */
    private String projectSubject;

    /** persistent field */
    private Integer leader;

    /** nullable persistent field */
    private String participantList;

    /** full constructor */
    public ProjectSubject(String projectSubject, Integer leader, String participantList) {
        this.projectSubject = projectSubject;
        this.leader = leader;
        this.participantList = participantList;
    }

    /** default constructor */
    public ProjectSubject() {
    }

    /** minimal constructor */
    public ProjectSubject(Integer leader) {
        this.leader = leader;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectSubject() {
        return this.projectSubject;
    }

    public void setProjectSubject(String projectSubject) {
        this.projectSubject = projectSubject;
    }

    public Integer getLeader() {
        return this.leader;
    }

    public void setLeader(Integer leader) {
        this.leader = leader;
    }

    public String getParticipantList() {
        return this.participantList;
    }

    public void setParticipantList(String participantList) {
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
