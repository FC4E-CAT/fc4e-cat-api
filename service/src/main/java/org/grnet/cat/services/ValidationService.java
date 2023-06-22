package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.grnet.cat.dtos.PromotionRequest;
import org.grnet.cat.entities.Validation;
import org.grnet.cat.exceptions.ConflictException;
import org.grnet.cat.repositories.ValidationRepository;

/**
 * The ValidationService provides operations for managing Validation entities.
 */

@ApplicationScoped
public class ValidationService {

    @Inject
    ValidationRepository validationRepository;

    /**
     * Checks if there is a promotion request for a specific user and organization.
     *
     * @param userId  The ID of the user.
     * @param request The promotion request information.
     * @throws ConflictException  if a promotion request exists for the user and organization.
     */
    public void hasPromotionRequest(String userId, PromotionRequest request) {

        // Call the repository method to check if a promotion request exists
        var exists = validationRepository.hasPromotionRequest(userId, request.organisationId, request.organisationSource);

        if(exists){
            throw new ConflictException("There is a promotion request for this user and organisation.");
        }
    }

    /**
     * Stores a new promotion request in database.
     *
     * @param validation The promotion request to be persisted in database.
     */
    @Transactional
    public void store(Validation validation){

        validationRepository.persist(validation);
    }
}
