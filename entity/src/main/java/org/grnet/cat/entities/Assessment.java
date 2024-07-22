package org.grnet.cat.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Timestamp;

/**
 * This entity represents the Assessment table in database.
 *
 */
@Entity
@Getter
@Setter
public class Assessment {

    /**
     * As id
     */
    @Id
    @UuidGenerator
    private String id;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(name = "updated_by")
    private String updatedBy;
}