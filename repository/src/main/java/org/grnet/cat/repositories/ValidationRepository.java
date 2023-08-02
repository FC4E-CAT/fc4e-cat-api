package org.grnet.cat.repositories;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.*;
import org.grnet.cat.enums.Source;
import org.grnet.cat.enums.ValidationStatus;

import java.util.Arrays;
import java.util.List;

/**
 * The ValidationRepository interface provides data access methods for the Validation entity.
 */
@ApplicationScoped
public class ValidationRepository implements Repository<Validation, Long> {

    /**
     * It executes a query in database to check if there is a promotion request for a specific user and organisation.
     * @param id The ID of the user.
     * @param organisationId The ID of the organisation.
     * @param organisationSource The source of the organisation.
     * @param actorId The actor id.
     * @return {@code true} if a promotion request exists for the user and organization, {@code false} otherwise
     */
    public boolean hasPromotionRequest(String id, String organisationId, String organisationSource, Long actorId){

        List<ValidationStatus> statuses = Arrays.asList(ValidationStatus.REVIEW, ValidationStatus.APPROVED, ValidationStatus.PENDING);

        return find("from Validation v where v.user.id = ?1 and v.organisationId = ?2 and v.organisationSource = ?3 and v.status IN (?4) and v.actor.id = ?5", id, organisationId, Source.valueOf(organisationSource), statuses, actorId).stream().findAny().isPresent();
    }

    /**
     * Retrieves a page of validation requests submitted by the specified user.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of validation requests to include in a page.
     * @param userID The ID of the user.
     * @return A list of Validation objects representing the validation requests in the requested page.
     */
    public PageQuery<Validation> fetchValidationsByUserAndPage(int page, int size, String userID){

        var panache = find("from Validation v where v.user.id = ?1", Sort.by("status"), userID).page(page, size);

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
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of validation requests to include in a page.
     * @param userID The ID of the user.
     * @param status Validation status to search for.
     * @return A list of Validation objects representing the validation requests in the requested page.
     */
    public PageQuery<Validation> fetchValidationsByUserAndStatusAndPage(ValidationStatus status, int page, int size, String userID){

        var panache = find("from Validation v where v.user.id = ?1 and v.status = ?2", userID, status).page(page, size);

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
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of validation requests to include in a page.
     * @return A list of Validation objects representing the validation requests in the requested page.
     */
    public PageQuery<Validation> fetchValidationsByPage(int page, int size){

        var panache = findAll(Sort.by("status")).page(page, size);

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
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of validation requests to include in a page.
     * @param status Validation status to search for.
     * @return A list of Validation objects representing the validation requests in the requested page.
     */
    public PageQuery<Validation> fetchValidationsByStatusAndPage(ValidationStatus status, int page, int size){

        var panache = find("status", status).page(page, size);

        var pageable = new PageQueryImpl<Validation>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }
}
