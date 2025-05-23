package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.UriInfo;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.ValidationRequest;
import org.grnet.cat.dtos.ValidationResponse;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.Validation;
import org.grnet.cat.entities.projections.UserRegistryAssessmentEligibility;
import org.grnet.cat.enums.MailType;
import org.grnet.cat.enums.Source;
import org.grnet.cat.enums.ValidationStatus;
import org.grnet.cat.exceptions.ConflictException;
import org.grnet.cat.mappers.ValidationMapper;
import org.grnet.cat.repositories.ValidationRepository;
import org.grnet.cat.repositories.registry.RegistryActorRepository;
import org.grnet.cat.validators.ValidationRequestValidator;

import org.jboss.logging.Logger;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * The ValidationService provides operations for managing Validation entities.
 */

@ApplicationScoped
public class ValidationService {

    @Inject
    MailerService mailerService;
    @Inject
    ValidationRepository validationRepository;
    @Inject
    @Named("keycloak-service")
    RoleService roleService;

    @Inject
    RegistryActorRepository registryActorRepository;

    private static final Logger LOG = Logger.getLogger(ValidationService.class);

    BiConsumer<String, ValidationStatus> handleValidationStatus = (userId, status) -> {

        switch (status) {
            case APPROVED: {
                roleService.assignRolesToUser(userId, List.of("validated"));
                break;
            }
            default: {

            }
        }
    };

    /**
     * Checks if there is a promotion request for a specific user, organization and registry actor.
     *
     * @param userId  The ID of the user.
     * @param organisationId The organisation id.
     * @param organisationSource The organisation source.
     * @param registryActorId The actor id.
     * @throws ConflictException if a promotion request exists for the user, organization and registry actor.
     */
    public void hasPromotionRequestWithRegistryActor(String userId, String organisationId, String organisationSource, String registryActorId) {

        // Call the repository method to check if a promotion request exists
        var exists = validationRepository.hasPromotionRequestWithRegistryActor(userId, organisationId, organisationSource, registryActorId);

        if (exists) {
            throw new ConflictException("There is a promotion request for this user and organisation.");
        }
    }

    /**
     * Stores a new promotion request in database.
     *
     * @param validation The promotion request to be persisted in database.
     */
    @Transactional
    public void store(Validation validation) {

        validationRepository.persist(validation);
    }

    /**
     * Retrieves a page of validation requests submitted by the specified user.
     *
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of validation requests to include in a page.
     * @param uriInfo The Uri Info.
     * @param userID  The ID of the user.
     * @return A list of ValidationResponse objects representing the submitted promotion requests in the requested page.
     */
    public PageResource<ValidationResponse> getValidationsByUserAndPage(int page, int size, String status, UriInfo uriInfo, String userID) {

        PageQuery<Validation> validations = null;

        if (StringUtils.isNotEmpty(status)) {

            validations = validationRepository.fetchValidationsByUserAndStatusAndPage(ValidationStatus.valueOf(status), page, size, userID);
        } else {

            validations = validationRepository.fetchValidationsByUserAndPage(page, size, userID);
        }

        return new PageResource<>(validations, ValidationMapper.INSTANCE.validationsToDto(validations.list()), uriInfo);
    }

    /**
     * Retrieves a page of validation requests submitted by users.
     *
     * @param search  Enables clients to specify a text string for searching specific fields within Validation entity.
     * @param sort Specifies the field by which the results to be sorted.
     * @param order Specifies the order in which the sorted results should be returned.
     * @param type Filters the results based on the type of actor.
     * @param status  Validation status to search for.
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of validation requests to include in a page.
     * @param uriInfo The Uri Info.
     * @return A list of ValidationResponse objects representing the submitted promotion requests in the requested page.
     */
    public PageResource<ValidationResponse> getValidationsByPage(String search, String sort, String order, String type, String status, int page, int size, UriInfo uriInfo) {

        var validations = validationRepository.fetchValidationsByPage(search, sort, order, type, status, page, size);

        return new PageResource<>(validations, ValidationMapper.INSTANCE.validationsToDto(validations.list()), uriInfo);
    }

    /**
     * Updates the validation request with the provided details.
     *
     * @param id                The ID of the validation request to update.
     * @param validationRequest The updated validation request object.
     * @return The updated validation request.
     */
    @Transactional
    public ValidationResponse updateValidationRequest(Long id, ValidationRequest validationRequest) {

        var validation = validationRepository.findById(id);

        validation.setOrganisationWebsite(validationRequest.organisationWebsite);
        validation.setOrganisationName(validationRequest.organisationName);
        validation.setOrganisationId(validationRequest.organisationId);
        validation.setOrganisationRole(validationRequest.organisationRole);
        validation.setOrganisationSource(Source.valueOf(validationRequest.organisationSource));

        return ValidationMapper.INSTANCE.validationToDto(validation);
    }

    /**
     * Updates the status of a validation request with the provided status.
     *
     * @param id     The ID of the validation request to update.
     * @param status The new status to set for the validation request.
     * @param userId The user who validates a validation request.
     * @param rejectionReason The reason for rejecting a validation.
     * @return The updated validation request.
     */
    @Transactional
    public ValidationResponse updateValidationRequestStatus(Long id, ValidationStatus status, String userId, String rejectionReason) {

        ValidationRequestValidator.validateRejectionReason(status, rejectionReason);

        validationRepository.update(
                "status = ?1, validatedBy = ?2, validatedOn = ?3, rejectionReason = ?4 WHERE id = ?5",
                status, userId, Timestamp.from(Instant.now()), status == ValidationStatus.REJECTED ? rejectionReason : null, id
        );
        var validation = validationRepository.findById(id);
        handleValidationStatus.accept(validation.getUser().getId(), status);
        MailerService.CustomCompletableFuture.runAsync(() -> mailerService.sendMails(validation, MailType.VALIDATED_ALERT_CHANGE_VALIDATION_STATUS, Arrays.asList(validation.getUser().getEmail())));

        return ValidationMapper.INSTANCE.validationToDto(validation);
    }

    /**
     * Retrieves a specific validation request if it belongs to the user.
     *
     * @param userId       The ID of the user.
     * @param validationId The ID of the validation request to retrieve.
     * @return The validation request if it belongs to the user.
     * @throws ForbiddenException If the user is not authorized to access the validation request.
     */
    public ValidationResponse getValidationRequest(String userId, Long validationId) {

        var validation = validationRepository.findById(validationId);

        if (!validation.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Not Permitted.");
        }

        return ValidationMapper.INSTANCE.validationToDto(validation);
    }

    /**
     * Retrieves a specific validation request.
     *
     * @param validationId The ID of the validation request to retrieve.
     * @return The validation request if it belongs to the user.
     */
    public ValidationResponse getValidationRequest(Long validationId) {

        var validation = validationRepository.findById(validationId);

        return ValidationMapper.INSTANCE.validationToDto(validation);
    }

    /**
     * Retrieves the list of published assessment types and registry actors for which the user is eligible to create assessments.
     *
     * @param page   The index of the page to retrieve (starting from 0).
     * @param size   The maximum number of validation requests to include in a page.
     * @param userID the ID of the user
     * @return a structured list of organizations, assessment types, and registry actors
     */
    public PageQuery<UserRegistryAssessmentEligibility> getUserRegistryAssessmentEligibility( int page, int size, String userID){

        return validationRepository.fetchUserRegistryAssessmentEligibility( page, size, userID);
    }

    /**
     * Retrieves the list of all assessment types and registry actors for which the user is eligible to create assessments.
     *
     * @param page   The index of the page to retrieve (starting from 0).
     * @param size   The maximum number of validation requests to include in a page.
     * @param userID the ID of the user
     * @return a structured list of organizations, assessment types, and registry actors
     */
    public PageQuery<UserRegistryAssessmentEligibility> getUserRegistryAssessmentEligibilityAll( int page, int size, String userID){

        return validationRepository.fetchUserRegistryAssessmentEligibilityAll( page, size, userID);
    }

    @Transactional
    public void deleteAll() {
        validationRepository.deleteAll();
    }
}
