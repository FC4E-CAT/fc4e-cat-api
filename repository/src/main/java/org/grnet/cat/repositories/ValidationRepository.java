package org.grnet.cat.repositories;

import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.Validation;
import org.grnet.cat.entities.projections.UserAssessmentEligibility;
import org.grnet.cat.entities.projections.UserRegistryAssessmentEligibility;
import org.grnet.cat.enums.Source;
import org.grnet.cat.enums.ValidationStatus;
import org.grnet.cat.repositories.registry.RegistryActorRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * The ValidationRepository interface provides data access methods for the Validation entity.
 */
@ApplicationScoped
public class ValidationRepository implements Repository<Validation, Long> {

    @Inject
    ActorRepository actorRepository;

    @Inject
    RegistryActorRepository registryActorRepository;

    /**
     * It executes a query in database to check if there is a promotion request for a specific user, organisation and actor.
     *
     * @param id                 The ID of the user.
     * @param organisationId     The ID of the organisation.
     * @param organisationSource The source of the organisation.
     * @param actorId            The actor id.
     * @return {@code true} if a promotion request exists for the user, organization and actor, {@code false} otherwise
     */
    public boolean hasPromotionRequestWithActor(String id, String organisationId, String organisationSource, Long actorId) {

        List<ValidationStatus> statuses = Arrays.asList(ValidationStatus.REVIEW, ValidationStatus.APPROVED, ValidationStatus.PENDING);

        return find("from Validation v where v.user.id = ?1 and v.organisationId = ?2 and v.organisationSource = ?3 and v.status IN (?4) and v.actor.id = ?5", id, organisationId, Source.valueOf(organisationSource), statuses, actorId).stream().findAny().isPresent();
    }

    /**
     * It executes a query in database to check if there is a promotion request for a specific user, organisation and registry actor.
     *
     * @param id                 The ID of the user.
     * @param organisationId     The ID of the organisation.
     * @param organisationSource The source of the organisation.
     * @param registryActorId            The actor id.
     * @return {@code true} if a promotion request exists for the user, organization and registry actor, {@code false} otherwise
     */
    public boolean hasPromotionRequestWithRegistryActor(String id, String organisationId, String organisationSource, String registryActorId) {

        List<ValidationStatus> statuses = Arrays.asList(ValidationStatus.REVIEW, ValidationStatus.APPROVED, ValidationStatus.PENDING);

        return find("from Validation v where v.user.id = ?1 and v.organisationId = ?2 and v.organisationSource = ?3 and v.status IN (?4) and v.registryActor.id = ?5", id, organisationId, Source.valueOf(organisationSource), statuses, registryActorId).stream().findAny().isPresent();
    }

    /**
     * Retrieves the total number of validation requests submitted by the specified user.
     *
     * @param userId The ID of the user.
     * @return the number of counts of validation requests for a specified user
     */
    public Long countValidationsByUserId(String userId) {

        return count("from Validation v where v.user.id = ?1", userId);
    }

    /**
     * Retrieves a page of validation requests submitted by the specified user.
     *
     * @param page   The index of the page to retrieve (starting from 0).
     * @param size   The maximum number of validation requests to include in a page.
     * @param userID The ID of the user.
     * @return A list of Validation objects representing the validation requests in the requested page.
     */
    public PageQuery<Validation> fetchValidationsByUserAndPage(int page, int size, String userID) {

        var panache = find("from Validation v where v.user.id = ?1", Sort.by("status").and("createdOn", Sort.Direction.Descending), userID).page(page, size);

        var pageable = new PageQueryImpl<Validation>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    /**
     * Retrieves a page of validation requests submitted by the specified user.
     *
     * @param page   The index of the page to retrieve (starting from 0).
     * @param size   The maximum number of validation requests to include in a page.
     * @param userID The ID of the user.
     * @param status Validation status to search for.
     * @return A list of Validation objects representing the validation requests in the requested page.
     */
    public PageQuery<Validation> fetchValidationsByUserAndStatusAndPage(ValidationStatus status, int page, int size, String userID) {

        var panache = find("from Validation v where v.user.id = ?1 and v.status = ?2", Sort.by("createdOn", Sort.Direction.Descending), userID, status).page(page, size);

        var pageable = new PageQueryImpl<Validation>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    /**
     * Retrieves a page of validation requests submitted by users.
     *
     * @param search  Enables clients to specify a text string for searching specific fields within Validation entity.
     * @param sort Specifies the field by which the results to be sorted.
     * @param order Specifies the order in which the sorted results should be returned.
     * @param type Filters the results based on the type of Actor.
     * @param status  Validation status to search for.
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of validation requests to include in a page.
     * @return A list of Validation objects representing the validation requests in the requested page.
     */
    public PageQuery<Validation> fetchValidationsByPage(String search, String sort, String order, String type, String status, int page, int size) {

        var joiner = new StringJoiner(StringUtils.SPACE);
        joiner.add("from Validation validation");

        var map = new HashMap<String, Object>();

        if(StringUtils.isNotEmpty(status)){

            joiner.add("where validation.status = :status");
            map.put("status", ValidationStatus.valueOf(status));
        } else {

            var list = new ArrayList<>();

            list.add(ValidationStatus.REVIEW);
            list.add(ValidationStatus.PENDING);
            list.add(ValidationStatus.APPROVED);

            joiner.add("where validation.status in (:status)");
            map.put("status", list);
        }

        if(!Objects.isNull(type)){

            var actor = actorRepository.fetchActorByName(type);
            joiner.add("and validation.actor.id = :id");
            map.put("id", actor.get().getId());
        }

        if (StringUtils.isNotEmpty(search)) {

            joiner.add("and validation.organisationName like :search or validation.user.name like :search or validation.user.surname like :search");
            map.put("search", "%" + search + "%");
        }

        joiner.add("order by");
        joiner.add("validation."+sort);
        joiner.add(order);

        var panache = find(joiner.toString(), map).page(page, size);

        var pageable = new PageQueryImpl<Validation>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    /**
     * Retrieves a page of validation requests submitted by users.
     *
     * @param page   The index of the page to retrieve (starting from 0).
     * @param size   The maximum number of validation requests to include in a page.
     * @param status Validation status to search for.
     * @return A list of Validation objects representing the validation requests in the requested page.
     */
    public PageQuery<Validation> fetchValidationsByStatusAndPage(ValidationStatus status, int page, int size) {

        var panache = find("status", Sort.by("createdOn", Sort.Direction.Descending), status).page(page, size);

        var pageable = new PageQueryImpl<Validation>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    /**
     * Retrieves the validation for a specific user's validation request on a specified actor and organization.
     *
     * @param userID         The unique identifier of the user initiating the validation request.
     * @param actorID        The unique identifier of the actor involved in the validation process.
     * @param organisationID The unique identifier of the organization associated with the validation request.
     * @return The validation for the specified user, actor, and organization.
     * @throws NotFoundException If the validation request is not found.
     */
    public Validation fetchValidationByUserAndActorAndOrganisation(String userID, Long actorID, String organisationID) {

        var optional = find("organisationId = :organisationID and user.id = : userID and actor.id = : actorID and status = : status",
                Parameters.with("organisationID", organisationID).and("userID", userID).and("actorID", actorID).and("status", ValidationStatus.APPROVED)).firstResultOptional();

        return optional.orElseThrow(() -> new NotFoundException("There is no approved validation request."));
    }

    /**
     * Retrieves the validation for a specific user's validation request on a specified registry actor and organization.
     *
     * @param userID         The unique identifier of the user initiating the validation request.
     * @param actorID        The unique identifier of the registry actor involved in the validation process.
     * @param organisationID The unique identifier of the organization associated with the validation request.
     * @return The validation for the specified user, registry actor, and organization.
     * @throws NotFoundException If the validation request is not found.
     */
    public Validation fetchValidationByUserAndRegistryActorAndOrganisation(String userID, String actorID, String organisationID) {

        var optional = find("organisationId = :organisationID and user.id = : userID and registryActor.id = : actorID and status = : status",
                Parameters.with("organisationID", organisationID).and("userID", userID).and("actorID", actorID).and("status", ValidationStatus.APPROVED)).firstResultOptional();

        return optional.orElseThrow(() -> new NotFoundException("There is no approved validation request."));
    }

    /**
     * Retrieves the list of assessment types and actors for which the user is eligible to create assessments.
     *
     * @param page   The index of the page to retrieve (starting from 0).
     * @param size   The maximum number of validation requests to include in a page.
     * @param userID the ID of the user
     * @return a structured list of organizations, assessment types, and actors
     */
    public PageQuery<UserAssessmentEligibility> fetchUserAssessmentEligibility(int page, int size, String userID){

        var panache = find("select v.organisationId, v.organisationName, v.organisationSource, v.organisationRole, " +
                "t.actor.id, t.actor.name, t.type.id, t.type.label from Validation v inner join Template t on v.actor.id = t.actor.id " +
                "where v.user.id = : userID and status = : status order by v.createdOn",
                Parameters.with("userID", userID).and("status", ValidationStatus.APPROVED)).project(UserAssessmentEligibility.class).page(page, size);

        var pageable = new PageQueryImpl<UserAssessmentEligibility>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    /**
     * Retrieves the list of assessment types and registry actors for which the user is eligible to create assessments.
     *
     * @param page   The index of the page to retrieve (starting from 0).
     * @param size   The maximum number of validation requests to include in a page.
     * @param userID the ID of the user
     * @return a structured list of organizations, assessment types, and registry actors
     */
    public PageQuery<UserRegistryAssessmentEligibility> fetchUserRegistryAssessmentEligibility(int page, int size, String userID){

        var panache = find("select v.organisationId, v.organisationName, v.organisationSource, v.organisationRole, " +
                        "ra.id as actorId, ra.labelActor as actorName, m.id as assessmentTypeId, m.label as assessmentTypeName from Validation v inner join MotivationActorJunction ma on v.registryActor.id = ma.id.actorId inner join RegistryActor ra on ma.id.actorId = ra.id inner join Motivation m on ma.id.motivationId = m.id " +
                        "where v.user.id = : userID and v.status = : status and ma.published = : published order by v.createdOn ",
                Parameters.with("userID", userID).and("status", ValidationStatus.APPROVED).and("published",Boolean.TRUE)).project(UserRegistryAssessmentEligibility.class).page(page, size);

        var pageable = new PageQueryImpl<UserRegistryAssessmentEligibility>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }
}