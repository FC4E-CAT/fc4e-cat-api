package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.grnet.cat.dtos.PromotionRequest;
import org.grnet.cat.entities.Identified;
import org.grnet.cat.entities.User;
import org.grnet.cat.entities.Validation;
import org.grnet.cat.enums.Source;
import org.grnet.cat.enums.ValidationStatus;
import org.grnet.cat.exceptions.ConflictException;
import org.grnet.cat.repositories.ActorRepository;
import org.grnet.cat.repositories.IdentifiedRepository;
import org.grnet.cat.repositories.ValidationRepository;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * The IdentifiedService provides operations for managing Identified entities.
 */

@ApplicationScoped
public class IdentifiedService {

    /**
     * Injection point for the Identified Repository
     */
    @Inject
    IdentifiedRepository identifiedRepository;

    /**
     * Injection point for the Actor Repository
     */
    @Inject
    ActorRepository actorRepository;

    /**
     * Injection point for the Validation Service
     */
    @Inject
    ValidationService validationService;

    /**
     * This operations registers a user on the CAT service.
     * Typically, it constructs an {@link User Identified} object and stores it in the database.
     * @param id User's voperson_id
     */
    @Transactional
    public void register(String id){

        var optionalUser = identifiedRepository.findByIdOptional(id);

        optionalUser.ifPresent(s -> {throw new ConflictException("User already exists in the database.");});

        var identified = new Identified();
        identified.setId(id);
        identified.setRegisteredOn(Timestamp.from(Instant.now()));

        identifiedRepository.persist(identified);
    }

    /**
     * Requests user promotion to become a validated user.
     *
     * @param id The ID of the identified user requesting promotion.
     * @param promotionRequest The promotion request information.
     */
    @Transactional
    public void validate(String id, PromotionRequest promotionRequest){

        validationService.hasPromotionRequest(id, promotionRequest);

        var user = identifiedRepository.findById(id);

        var actor = actorRepository.findById(promotionRequest.actorId);

        var validation = new Validation();

        validation.setUser(user);
        validation.setActor(actor);
        validation.setCreatedOn(Timestamp.from(Instant.now()));
        validation.setStatus(ValidationStatus.REVIEW);
        validation.setOrganisationId(promotionRequest.organisationId);
        validation.setOrganisationName(promotionRequest.organisationName);
        validation.setOrganisationSource(Source.valueOf(promotionRequest.organisationSource));
        validation.setOrganisationWebsite(promotionRequest.organisationWebsite);
        validation.setOrganisationRole(promotionRequest.organisationRole);

        validationService.store(validation);
    }
}