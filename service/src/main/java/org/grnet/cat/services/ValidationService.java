package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.PromotionRequest;
import org.grnet.cat.dtos.ValidationResponse;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.entities.Validation;
import org.grnet.cat.exceptions.ConflictException;
import org.grnet.cat.mappers.ValidationMapper;
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
        var exists = validationRepository.hasPromotionRequest(userId, request.organisationId, request.organisationSource, request.actorId);

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

    /**
     * Retrieves a page of validation requests submitted by the specified user.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of validation requests to include in a page.
     * @param uriInfo The Uri Info.
     * @param userID The ID of the user.
     * @return A list of PromotionResponse objects representing the submitted promotion requests in the requested page.
     */
    public PageResource<ValidationResponse> getValidationsByPage(int page, int size, UriInfo uriInfo, String userID){

        var validations = validationRepository.fetchValidationsByPage(page, size, userID);

        return new PageResource<>(validations, ValidationMapper.INSTANCE.validationsToDto(validations.list()), uriInfo);
    }
}
