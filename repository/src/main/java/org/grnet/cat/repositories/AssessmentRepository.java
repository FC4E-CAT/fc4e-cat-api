package org.grnet.cat.repositories;

import io.quarkus.hibernate.orm.panache.Panache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.entities.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class AssessmentRepository implements Repository<Assessment, String> {

    /**
     * Retrieves a page of assessments submitted by the specified user.
     *
     * @param page   The index of the page to retrieve (starting from 0).
     * @param size   The maximum number of assessments to include in a page.
     * @param userID The ID of the user.
     * @param shareableIds The IDs shared to the User.
     * @return A list of Assessment objects representing the assessments in the requested page.
     */
    public PageQuery<Assessment> fetchAssessmentsByUserAndPage(int page, int size, String userID, String subjectName, String subjectType, Long actorID, List<String> shareableIds) {

        var joiner = new StringJoiner(StringUtils.SPACE);
        joiner.add("from Assessment a where (a.validation.user.id = :userID or a.id IN :shareableIds)");

        var map = new HashMap<String, Object>();
        map.put("userID", userID);
        map.put("shareableIds", shareableIds);

        if (StringUtils.isNotEmpty(subjectName)) {

            joiner.add("AND FUNCTION('JSON_EXTRACT', a.assessmentDoc, '$.subject.name') = :name");
            map.put("name", subjectName);
        }

        if (StringUtils.isNotEmpty(subjectType)) {

            joiner.add("AND FUNCTION('JSON_EXTRACT', a.assessmentDoc, '$.subject.type') = :type");
            map.put("type", subjectType);
        }

        if (!Objects.isNull(actorID)) {

            joiner.add("AND a.validation.actor.id = :actorID");
            map.put("actorID", actorID);
        }

        joiner.add("order by a.createdOn desc");

        var panache = find(joiner.toString(), map).page(page, size);

        var pageable = new PageQueryImpl<Assessment>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    public Long countAllAssessmentsByUser(String userId) {

        return count("from Assessment a where a.validation.user.id = ?1", userId);
    }

    /**
     * Retrieves a page of assessments.
     *
     * @param page   The index of the page to retrieve (starting from 0).
     * @param size   The maximum number of assessments to include in a page.
     * @return A list of Assessment objects representing the assessments in the requested page.
     */
    public PageQuery<Assessment> fetchAllAssessmentsByPage(int page, int size, String search) {
        var joiner = new StringJoiner(" ");
        joiner.add("from Assessment a");

        var params = new HashMap<String, Object>();

        if (search != null && !search.isEmpty()) {
            joiner.add("WHERE (FUNCTION('JSON_EXTRACT', a.assessmentDoc, '$.name') LIKE :search OR a.validation.user.email LIKE :search OR a.validation.user.name LIKE :search OR a.validation.user.surname LIKE :search)");
            params.put("search", "%" + search + "%");
        }

        joiner.add("order by a.createdOn desc");

        var panache = find(joiner.toString(), params).page(page, size);

        var pageable = new PageQueryImpl<Assessment>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    /**
     * Retrieves a page of published assessments categorized by type and actor, created by all users.
     *
     * @param page        The index of the page to retrieve (starting from 0).
     * @param size        The maximum number of assessments to include in a page.
     * @param typeId      The ID of the Assessment Type.
     * @param actorId     The Actor's id.
     * @param subjectName Subject name to search for.
     * @param subjectType Subject Type to search for.
     * @return A list of Assessment objects representing the assessments in the requested page.
     */
    @SuppressWarnings("unchecked")
    public PageQuery<Assessment> fetchPublishedAssessmentsByTypeAndActorAndPage(int page, int size, Long typeId, Long actorId, String subjectName, String subjectType) {

        var em = Panache.getEntityManager();

        var query = em.createNativeQuery("SELECT a.id, a.assessment_doc, a.template_id, a.validation_id, a.created_on, a.updated_on, a.subject_id, a.updated_by , a.shared FROM Assessment a INNER JOIN Template t ON a.template_id = t.id INNER JOIN Actor actor ON t.actor_id = actor.id INNER JOIN AssessmentType at ON t.assessment_type_id = at.id WHERE actor.id = :actorId AND at.id = :typeId AND JSON_EXTRACT(a.assessment_doc, '$.published') = true ORDER BY a.created_on DESC", Assessment.class)
                .setParameter("actorId", actorId)
                .setParameter("typeId", typeId);

        var countQuery = em.createNativeQuery("SELECT count(a.id) FROM Assessment a INNER JOIN Template t ON a.template_id = t.id INNER JOIN Actor actor ON t.actor_id = actor.id INNER JOIN AssessmentType at ON t.assessment_type_id = at.id WHERE actor.id = :actorId AND at.id = :typeId AND JSON_EXTRACT(a.assessment_doc, '$.published') = true", Long.class)
                .setParameter("actorId", actorId)
                .setParameter("typeId", typeId);


        if (StringUtils.isNotEmpty(subjectName) && StringUtils.isNotEmpty(subjectType)) {

            query = em.createNativeQuery("SELECT a.id, a.assessment_doc, a.template_id, a.validation_id, a.created_on, a.updated_on, a.subject_id, a.updated_by , a.shared FROM Assessment a INNER JOIN Template t ON a.template_id = t.id INNER JOIN Actor actor ON t.actor_id = actor.id INNER JOIN AssessmentType at ON t.assessment_type_id = at.id WHERE actor.id = :actorId AND at.id = :typeId AND JSON_EXTRACT(a.assessment_doc, '$.published') = true AND JSON_EXTRACT(a.assessment_doc, '$.subject.name') = :name AND JSON_EXTRACT(a.assessment_doc, '$.subject.type') = :type ORDER BY a.created_on DESC", Assessment.class)
                    .setParameter("actorId", actorId)
                    .setParameter("typeId", typeId)
                    .setParameter("name", subjectName)
                    .setParameter("type", subjectType);

            countQuery = em.createNativeQuery("SELECT count(a.id) FROM Assessment a INNER JOIN Template t ON a.template_id = t.id INNER JOIN Actor actor ON t.actor_id = actor.id INNER JOIN AssessmentType at ON t.assessment_type_id = at.id WHERE actor.id = :actorId AND at.id = :typeId AND JSON_EXTRACT(a.assessment_doc, '$.published') = true AND JSON_EXTRACT(a.assessment_doc, '$.subject.name') = :name AND JSON_EXTRACT(a.assessment_doc, '$.subject.type') = :type", Long.class)
                    .setParameter("actorId", actorId)
                    .setParameter("typeId", typeId)
                    .setParameter("name", subjectName)
                    .setParameter("type", subjectType);


        } else if (StringUtils.isNotEmpty(subjectName)) {

            query = em.createNativeQuery("SELECT a.id, a.assessment_doc, a.template_id, a.validation_id, a.created_on, a.updated_on, a.subject_id, a.updated_by, a.shared FROM Assessment a INNER JOIN Template t ON a.template_id = t.id INNER JOIN Actor actor ON t.actor_id = actor.id INNER JOIN AssessmentType at ON t.assessment_type_id = at.id WHERE actor.id = :actorId AND at.id = :typeId AND JSON_EXTRACT(a.assessment_doc, '$.published') = true AND JSON_EXTRACT(a.assessment_doc, '$.subject.name') = :name ORDER BY a.created_on DESC", Assessment.class)
                    .setParameter("actorId", actorId)
                    .setParameter("typeId", typeId)
                    .setParameter("name", subjectName);

            countQuery = em.createNativeQuery("SELECT count(a.id) FROM Assessment a INNER JOIN Template t ON a.template_id = t.id INNER JOIN Actor actor ON t.actor_id = actor.id INNER JOIN AssessmentType at ON t.assessment_type_id = at.id WHERE actor.id = :actorId AND at.id = :typeId AND JSON_EXTRACT(a.assessment_doc, '$.published') = true AND JSON_EXTRACT(a.assessment_doc, '$.subject.name') = :name", Long.class)
                    .setParameter("actorId", actorId)
                    .setParameter("typeId", typeId)
                    .setParameter("name", subjectName);

        } else if (StringUtils.isNotEmpty(subjectType)) {

            query = em.createNativeQuery("SELECT a.id, a.assessment_doc, a.template_id, a.validation_id, a.created_on, a.updated_on, a.subject_id, a.updated_by, a.shared FROM Assessment a INNER JOIN Template t ON a.template_id = t.id INNER JOIN Actor actor ON t.actor_id = actor.id INNER JOIN AssessmentType at ON t.assessment_type_id = at.id WHERE actor.id = :actorId AND at.id = :typeId AND JSON_EXTRACT(a.assessment_doc, '$.published') = true AND JSON_EXTRACT(a.assessment_doc, '$.subject.type') = :type ORDER BY a.created_on DESC", Assessment.class)
                    .setParameter("actorId", actorId)
                    .setParameter("typeId", typeId)
                    .setParameter("type", subjectType);

            countQuery = em.createNativeQuery("SELECT count(a.id) FROM Assessment a INNER JOIN Template t ON a.template_id = t.id INNER JOIN Actor actor ON t.actor_id = actor.id INNER JOIN AssessmentType at ON t.assessment_type_id = at.id WHERE actor.id = :actorId AND at.id = :typeId AND JSON_EXTRACT(a.assessment_doc, '$.published') = true AND JSON_EXTRACT(a.assessment_doc, '$.subject.type') = :type", Long.class)
                    .setParameter("actorId", actorId)
                    .setParameter("typeId", typeId)
                    .setParameter("type", subjectType);
        }

        var list = (List<Assessment>) query
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();

        var pageable = new PageQueryImpl<Assessment>();
        pageable.list = list;
        pageable.index = page;
        pageable.size = size;
        pageable.count = (Long) countQuery.getSingleResult();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    /**
     * Retrieves a page of public assessment objects by type and actor.
     *
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of assessment objects to include in a page.
     * @param typeId  The ID of the Assessment Type.
     * @param actorId The Actor's id.
     * @return A list of string objects representing the public assessment objects in the requested page.
     */
    @SuppressWarnings("unchecked")
    public PageQuery<String> fetchPublishedAssessmentObjectsByTypeAndActorAndPage(int page, int size, Long typeId, Long actorId) {

        var em = Panache.getEntityManager();

        var query = em.createNativeQuery("SELECT DISTINCT JSON_EXTRACT(a.assessment_doc, '$.subject') FROM Assessment a INNER JOIN Template t ON a.template_id = t.id INNER JOIN Actor actor ON t.actor_id = actor.id INNER JOIN AssessmentType at ON t.assessment_type_id = at.id WHERE actor.id = :actorId AND at.id = :typeId AND JSON_EXTRACT(a.assessment_doc, '$.published') = true")
                .setParameter("actorId", actorId)
                .setParameter("typeId", typeId);

        var countQuery = em.createNativeQuery("SELECT count(DISTINCT JSON_EXTRACT(a.assessment_doc, '$.subject')) FROM Assessment a INNER JOIN Template t ON a.template_id = t.id INNER JOIN Actor actor ON t.actor_id = actor.id INNER JOIN AssessmentType at ON t.assessment_type_id = at.id WHERE actor.id = :actorId AND at.id = :typeId AND JSON_EXTRACT(a.assessment_doc, '$.published') = true")
                .setParameter("actorId", actorId)
                .setParameter("typeId", typeId);

        var list = (List<String>) query
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();

        var pageable = new PageQueryImpl<String>();
        pageable.list = list;
        pageable.index = page;
        pageable.size = size;
        pageable.count = (Long) countQuery.getSingleResult();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    /**
     * Retrieves a page of assessment objects submitted by the specified user by the specified actor.
     *
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of assessment objects to include in a page.
     * @param userID  The ID of the user.
     * @param actorID The Actor id.
     * @return A list of Assessment objects representing the assessments in the requested page.
     */
    public PageQuery<String> fetchAssessmentsObjectsByUserAndActor(int page, int size, String userID, Long actorID) {


        var query = Panache.getEntityManager().createNativeQuery("SELECT DISTINCT JSON_EXTRACT(a.assessment_doc, '$.subject') FROM Assessment a INNER JOIN Template t ON a.template_id = t.id INNER JOIN Actor actor ON t.actor_id = actor.id INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN User u ON u.id = v.user_id WHERE actor.id = :actorId AND u.id = :userId")
                .setParameter("actorId", actorID)
                .setParameter("userId", userID);

        var countQuery = Panache.getEntityManager().createNativeQuery("SELECT count(DISTINCT JSON_EXTRACT(a.assessment_doc, '$.subject')) FROM Assessment a INNER JOIN Template t ON a.template_id = t.id INNER JOIN Actor actor ON t.actor_id = actor.id INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN User u ON u.id = v.user_id WHERE actor.id = :actorId AND u.id = :userId")
                .setParameter("actorId", actorID)
                .setParameter("userId", userID);

        var list = (List<String>) query
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();

        var pageable = new PageQueryImpl<String>();
        pageable.list = list;
        pageable.index = page;
        pageable.size = size;
        pageable.count = (Long) countQuery.getSingleResult();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    /**
     * Retrieves a page of assessment objects submitted by the specified user.
     *
     * @param page   The index of the page to retrieve (starting from 0).
     * @param size   The maximum number of assessment objects to include in a page.
     * @param userID The ID of the user.
     * @return A list of Assessment objects representing the assessments in the requested page.
     */
    public PageQuery<String> fetchAssessmentsObjectsByUser(int page, int size, String userID) {


        var query = Panache.getEntityManager().createNativeQuery("SELECT DISTINCT JSON_EXTRACT(a.assessment_doc, '$.subject') FROM Assessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN User u ON u.id = v.user_id WHERE u.id = :userId")
                .setParameter("userId", userID);

        var countQuery = Panache.getEntityManager().createNativeQuery("SELECT count(DISTINCT JSON_EXTRACT(a.assessment_doc, '$.subject')) FROM Assessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN User u ON u.id = v.user_id WHERE u.id = :userId")
                .setParameter("userId", userID);

        var list = (List<String>) query
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();

        var pageable = new PageQueryImpl<String>();
        pageable.list = list;
        pageable.index = page;
        pageable.size = size;
        pageable.count = (Long) countQuery.getSingleResult();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    @Transactional
    public void deleteAssessmentById(String assessmentId) {

        var assessment = findById(assessmentId);

        delete(assessment);
    }

    @Transactional
    public Assessment updateAssessmentDocById(String assessmentId, String updatedBy, String assessmentDoc) {

        update("assessmentDoc = ?1 , updatedOn = ?2 , updatedBy = ?3  where id = ?4", assessmentDoc, Timestamp.from(Instant.now()), updatedBy, assessmentId);

        return findById(assessmentId);
    }

    /**
     * Retrieves a page of assessment objects for a specific subject.
     *
     * @param page      The index of the page to retrieve (starting from 0).
     * @param size      The maximum number of assessment objects to include in a page.
     * @param subjectId The ID of the Subject.
     * @return A list of Assessment objects representing the assessments in the requested page.
     */
    public PageQuery<Assessment> fetchAssessmentsPerSubjectAndPage(int page, int size, Long subjectId) {

        var panache = find("from Assessment a where a.subject.id =?1 ", subjectId).page(page, size);

        var pageable = new PageQueryImpl<Assessment>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);
        return pageable;
    }

    public List<AssessmentPerActor> getStatistics() {

        return find(" SELECT DISTINCT act.name as actor_name, COUNT(*) as total FROM Assessment a inner join Validation v on v.id=a.validation.id inner join Actor act on v.actor.id=act.id GROUP BY actor_name").project(AssessmentPerActor.class).stream().collect(Collectors.toList());
    }


}
