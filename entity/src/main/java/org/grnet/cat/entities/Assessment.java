package org.grnet.cat.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

/**
 * This entity represents the Assessment table in database.
 *
 */
@Entity
public class Assessment {

    /**
     * As id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assessment_doc", columnDefinition = "json")
    private String assessmentDoc;

    @ManyToOne
    @JoinColumn(name = "template_id", referencedColumnName = "id")
    private Template template;

    @ManyToOne
    @JoinColumn(name = "validation_id", referencedColumnName = "id")
    private Validation validation;

    @Column(name = "created_on")
    @NotNull
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssessmentDoc() {
        return assessmentDoc;
    }

    public void setAssessmentDoc(String assessmentDoc) {
        this.assessmentDoc = assessmentDoc;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public Timestamp getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Timestamp updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Validation getValidation() {
        return validation;
    }

    public void setValidation(Validation validation) {
        this.validation = validation;
    }
}