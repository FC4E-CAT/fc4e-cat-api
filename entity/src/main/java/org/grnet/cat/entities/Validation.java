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
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.converter.StatusAttributeConverter;
import org.grnet.cat.entities.registry.RegistryActor;
import org.grnet.cat.enums.Source;
import org.grnet.cat.enums.ValidationStatus;

import java.sql.Timestamp;

/**
 * This entity represents an application that an identified user can request to be promoted to a validated user.
 */
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
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
    @NotNull
    @JoinColumn(name = "registry_actor_id", referencedColumnName = "lodActor")
    private RegistryActor registryActor;

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
}
