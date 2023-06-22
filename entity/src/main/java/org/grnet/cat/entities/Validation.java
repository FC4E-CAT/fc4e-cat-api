package org.grnet.cat.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import org.grnet.cat.enums.Source;
import org.grnet.cat.enums.ValidationStatus;

import java.sql.Timestamp;

@Entity
/**
 * This entity represents an application that an identified user can request to be promoted to a validated user.
 */
public class Validation {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
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

    @Column(name = "created_on")
    @NotNull
    private Timestamp createdOn;

    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    @NotNull
    private ValidationStatus status;

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
}
