package org.grnet.cat.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ZenodoAssessmentInfoId implements Serializable {

    @Column(name = "assessment_id")
    private String assessmentId;

    @Column(name = "deposit_id")  // <-- Correct column name mapping
    private String depositId;

    // Constructors
    public ZenodoAssessmentInfoId() {
    }

    public ZenodoAssessmentInfoId(String assessmentId, String depositId) {
        this.assessmentId = assessmentId;
        this.depositId = depositId;
    }

    // Getters and Setters
    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getDepositId() {
        return depositId;
    }

    public void setDepositId(String depositId) {
        this.depositId = depositId;
    }

    // Equals and HashCode (Required for Embedded ID)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZenodoAssessmentInfoId that = (ZenodoAssessmentInfoId) o;
        return Objects.equals(assessmentId, that.assessmentId) &&
                Objects.equals(depositId, that.depositId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assessmentId, depositId);
    }
}
