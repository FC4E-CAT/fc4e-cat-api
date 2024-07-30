package org.grnet.cat.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.grnet.cat.enums.CriteriaImperative;

import java.sql.Timestamp;

@Getter
@Entity
public class Criteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String uuid;

    @Column(name = "criteria_code", unique = true)
    @NotNull
    private String criteriaCode;

    @Column
    @NotNull
    private String label;

    @Column
    @NotNull
    private String description;

    @Column(name = "imperative")
    @NotNull
    @Enumerated(EnumType.STRING)
    private CriteriaImperative imperative;

    @Column(name = "created_on", updatable = false)
    @NotNull
    private Timestamp createdOn;

    @Column(name = "modified_on")
    private Timestamp modifiedOn;

    @Column(name = "created_by")
    @NotNull
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    public void setId(Long id) { this.id = id;}

    public void setUuid(String uuid) { this.uuid = uuid;}

    public void setCriteriaCode(String criteriaCode) { this.criteriaCode = criteriaCode;}

    public void setLabel(String label) { this.label = label;}

    public void setDescription(String description) { this.description = description;}

    public void setImperative(CriteriaImperative imperative) { this.imperative = imperative;}

    public void setCreatedOn(Timestamp createdOn) { this.createdOn = createdOn;}

    public void setModifiedOn(Timestamp modifiedOn) { this.modifiedOn = modifiedOn;}

    public void setCreatedBy(String createdBy) { this.createdBy = createdBy;}

    public void setModifiedBy(String modifiedBy) {this.modifiedBy = modifiedBy;}
}
