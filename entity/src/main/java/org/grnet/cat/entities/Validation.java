package org.grnet.cat.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import org.grnet.cat.converter.StatusAttributeConverter;
import org.grnet.cat.enums.Source;
import org.grnet.cat.enums.ValidationStatus;

import java.sql.Timestamp;

/**
 * This entity represents an application that an identified user can request to be promoted to a validated user.
 */
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Validation {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "organisation_role")
    @NotNull
    private String organisationRole;

    @Column(name = "organisation_id")
    @NotNull
    private String organisationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "organisation_source", length = 8)
    @NotNull
    private Source organisationSource;

    @Column(name = "organisation_name")
    @NotNull
    private String organisationName;

    @Column(name = "organisation_website")
    private String organisationWebsite;

    @OneToOne
    @JoinColumn(name = "actor_id", referencedColumnName = "id")
    @NotNull
    private Actor actor;

    @Column(name = "created_on")
    @NotNull
    private Timestamp createdOn;

    @Convert(converter = StatusAttributeConverter.class)
    @NotNull
    private ValidationStatus status;

    @Column(name = "validated_on")
    private Timestamp validatedOn;

    @Column(name = "validated_by")
    private String validatedBy;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOrganisationRole() {
        return organisationRole;
    }

    public void setOrganisationRole(String organisationRole) {
        this.organisationRole = organisationRole;
    }

    public String getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(String organisationId) {
        this.organisationId = organisationId;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getOrganisationWebsite() {
        return organisationWebsite;
    }

    public void setOrganisationWebsite(String organisationWebsite) {
        this.organisationWebsite = organisationWebsite;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public ValidationStatus getStatus() {
        return status;
    }

    public void setStatus(ValidationStatus status) {
        this.status = status;
    }

    public Source getOrganisationSource() {
        return organisationSource;
    }

    public void setOrganisationSource(Source organisationSource) {
        this.organisationSource = organisationSource;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Timestamp getValidatedOn() {
        return validatedOn;
    }

    public void setValidatedOn(Timestamp validatedOn) {
        this.validatedOn = validatedOn;
    }

    public String getValidatedBy() {
        return validatedBy;
    }

    public void setValidatedBy(String validatedBy) {
        this.validatedBy = validatedBy;
    }

    public String getRejectionReason() { return rejectionReason; }

    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}
