package org.grnet.cat.entities;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.entities.registry.Motivation;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * This entity represents the Motivation Assessment table in database.
 *
 */
@Entity
@Getter
@Setter
public class MotivationAssessment {

    /**
     * As id
     */
    @Id
    @UuidGenerator
    private String id;

    @Column(name = "assessment_doc", columnDefinition = "json")
    private String assessmentDoc;

    @ManyToOne
    @JoinColumn(name = "validation_id", referencedColumnName = "id")
    private Validation validation;

    @Column(name = "created_on")
    @NotNull
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(name = "updated_by")
    private String updatedBy;

    @OneToMany(mappedBy = "motivationAssessment",
                cascade = CascadeType.ALL,
                orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    private Boolean shared;

    @ManyToOne
    @JoinColumn(name = "motivation_id", referencedColumnName = "lodMTV")
    @NotNull
    private Motivation motivation;
}