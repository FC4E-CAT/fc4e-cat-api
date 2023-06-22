package org.grnet.cat.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Validation;
import org.grnet.cat.enums.Source;
import org.grnet.cat.enums.ValidationStatus;

import java.util.Arrays;
import java.util.List;


/**
 * The ValidationRepository interface provides data access methods for the Validation entity.
 */
@ApplicationScoped
public class ValidationRepository implements PanacheRepositoryBase<Validation, Long> {

    /**
     * It executes a query in database to check if there is a promotion request for a specific user and organisation.
     * @param id The ID of the user.
     * @param organisationId The ID of the organisation.
     * @param organisationSource The source of the organisation.
     * @return {@code true} if a promotion request exists for the user and organization, {@code false} otherwise
     */
    public boolean hasPromotionRequest(String id, String organisationId, String organisationSource){

        List<ValidationStatus> statuses = Arrays.asList(ValidationStatus.REVIEW, ValidationStatus.APPROVED, ValidationStatus.PENDING);

        return find("from Validation v where v.user.id = ?1 and v.organisationId = ?2 and v.organisationSource = ?3 and v.status IN (?4)", id, organisationId, Source.valueOf(organisationSource), statuses).stream().findAny().isPresent();
    }
}
