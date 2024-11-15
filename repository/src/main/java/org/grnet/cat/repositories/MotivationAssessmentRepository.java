package org.grnet.cat.repositories;

import io.quarkus.hibernate.orm.panache.Panache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.entities.Assessment;
import org.grnet.cat.entities.MotivationAssessment;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@ApplicationScoped
public class MotivationAssessmentRepository implements Repository<MotivationAssessment, String> {

    /**
     * Retrieves a page of assessments submitted by the specified user.
     *
     * @param page   The index of the page to retrieve (starting from 0).
     * @param size   The maximum number of assessments to include in a page.
     * @param userID The ID of the user.
     * @param shareableIds The IDs shared to the User.
     * @return A list of Assessment objects representing the assessments in the requested page.
     */
    public PageQuery<MotivationAssessment> fetchRegistryAssessmentsByUserAndPage(int page, int size, String userID, String subjectName, String subjectType, String actorID, List<String> shareableIds) {

        var joiner = new StringJoiner(StringUtils.SPACE);
        joiner.add("from MotivationAssessment a where (a.validation.user.id = :userID or a.id IN :shareableIds)");

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

            joiner.add("AND a.validation.registryActor.id = :actorID");
            map.put("actorID", actorID);
        }

        joiner.add("order by a.createdOn desc");

        var panache = find(joiner.toString(), map).page(page, size);

        var pageable = new PageQueryImpl<MotivationAssessment>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    /**
     * Retrieves a page of public assessment objects by motivation and actor.
     *
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of assessment objects to include in a page.
     * @param motivationId  The ID of the Motivation.
     * @param actorId The Actor's id.
     * @return A list of string objects representing the public assessment objects in the requested page.
     */
    @SuppressWarnings("unchecked")
    public PageQuery<String> fetchPublishedAssessmentObjectsByMotivationAndActorAndPage(int page, int size, String motivationId, String actorId) {

        var em = Panache.getEntityManager();

        var query = em.createNativeQuery("SELECT DISTINCT JSON_EXTRACT(a.assessment_doc, '$.subject') FROM MotivationAssessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN t_Motivation m ON a.motivation_id = m.lodMTV where v.registry_actor_id = :actorId and m.lodMTV = :motivationId AND JSON_EXTRACT(a.assessment_doc, '$.published') = true")
                .setParameter("actorId", actorId)
                .setParameter("motivationId", motivationId);

        var countQuery = em.createNativeQuery("SELECT count(DISTINCT JSON_EXTRACT(a.assessment_doc, '$.subject')) FROM MotivationAssessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN t_Motivation m ON a.motivation_id = m.lodMTV where v.registry_actor_id = :actorId and m.lodMTV = :motivationId AND JSON_EXTRACT(a.assessment_doc, '$.published') = true")
                .setParameter("actorId", actorId)
                .setParameter("motivationId", motivationId);

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
     * Retrieves a page of published assessments categorized by type and actor, created by all users.
     *
     * @param page        The index of the page to retrieve (starting from 0).
     * @param size        The maximum number of assessments to include in a page.
     * @param motivationId      The ID of the Assessment Type.
     * @param actorId     The Actor's id.
     * @param subjectName Subject name to search for.
     * @param subjectType Subject Type to search for.
     * @return A list of Assessment objects representing the assessments in the requested page.
     */
    @SuppressWarnings("unchecked")
    public PageQuery<MotivationAssessment> fetchPublishedAssessmentsByMotivationAndActorAndPage(int page, int size, String motivationId, String actorId, String subjectName, String subjectType) {

        var em = Panache.getEntityManager();

        var query = em.createNativeQuery("SELECT a.id, a.assessment_doc, a.validation_id, a.created_on, a.updated_on, a.subject_id, a.updated_by , a.shared, a.motivation_id FROM MotivationAssessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN t_Motivation m ON a.motivation_id = m.lodMTV where v.registry_actor_id = :actorId and m.lodMTV = :motivationId AND JSON_EXTRACT(a.assessment_doc, '$.published') = true ORDER BY a.created_on DESC", MotivationAssessment.class)
                .setParameter("actorId", actorId)
                .setParameter("motivationId", motivationId);

        var countQuery = em.createNativeQuery("SELECT count(a.id) FROM MotivationAssessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN t_Motivation m ON a.motivation_id = m.lodMTV where v.registry_actor_id = :actorId and m.lodMTV = :motivationId AND JSON_EXTRACT(a.assessment_doc, '$.published') = true", Long.class)
                .setParameter("actorId", actorId)
                .setParameter("motivationId", motivationId);


        if (StringUtils.isNotEmpty(subjectName) && StringUtils.isNotEmpty(subjectType)) {

            query = em.createNativeQuery("SELECT a.id, a.assessment_doc, a.validation_id, a.created_on, a.updated_on, a.subject_id, a.updated_by , a.motivation_id FROM MotivationAssessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN t_Motivation m ON a.motivation_id = m.lodMTV where v.registry_actor_id = :actorId and m.lodMTV = :motivationId AND JSON_EXTRACT(a.assessment_doc, '$.published') = true AND JSON_EXTRACT(a.assessment_doc, '$.subject.name') = :name AND JSON_EXTRACT(a.assessment_doc, '$.subject.type') = :type ORDER BY a.created_on DESC", MotivationAssessment.class)
                    .setParameter("actorId", actorId)
                    .setParameter("motivationId", motivationId)
                    .setParameter("name", subjectName)
                    .setParameter("type", subjectType);

            countQuery = em.createNativeQuery("SELECT count(a.id)  FROM MotivationAssessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN t_Motivation m ON a.motivation_id = m.lodMTV where v.registry_actor_id = :actorId and m.lodMTV = :motivationId AND JSON_EXTRACT(a.assessment_doc, '$.published') = true AND JSON_EXTRACT(a.assessment_doc, '$.published') = true AND JSON_EXTRACT(a.assessment_doc, '$.subject.name') = :name AND JSON_EXTRACT(a.assessment_doc, '$.subject.type') = :type", Long.class)
                    .setParameter("actorId", actorId)
                    .setParameter("motivationId", motivationId)
                    .setParameter("name", subjectName)
                    .setParameter("type", subjectType);


        } else if (StringUtils.isNotEmpty(subjectName)) {

            query = em.createNativeQuery("SELECT a.id, a.assessment_doc, a.validation_id, a.created_on, a.updated_on, a.subject_id, a.updated_by, a.shared, a.motivation_id FROM MotivationAssessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN t_Motivation m ON a.motivation_id = m.lodMTV where v.registry_actor_id = :actorId and m.lodMTV = :motivationId AND JSON_EXTRACT(a.assessment_doc, '$.published') = true AND JSON_EXTRACT(a.assessment_doc, '$.subject.name') = :name ORDER BY a.created_on DESC", MotivationAssessment.class)
                    .setParameter("actorId", actorId)
                    .setParameter("motivationId", motivationId)
                    .setParameter("name", subjectName);

            countQuery = em.createNativeQuery("SELECT count(a.id) FROM MotivationAssessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN t_Motivation m ON a.motivation_id = m.lodMTV where v.registry_actor_id = :actorId and m.lodMTV = :motivationId AND JSON_EXTRACT(a.assessment_doc, '$.published') = true AND JSON_EXTRACT(a.assessment_doc, '$.subject.name') = :name", Long.class)
                    .setParameter("actorId", actorId)
                    .setParameter("motivationId", motivationId)
                    .setParameter("name", subjectName);

        } else if (StringUtils.isNotEmpty(subjectType)) {

            query = em.createNativeQuery("SELECT a.id, a.assessment_doc, a.validation_id, a.created_on, a.updated_on, a.subject_id, a.updated_by, a.shared, a.motivation_id FROM MotivationAssessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN t_Motivation m ON a.motivation_id = m.lodMTV where v.registry_actor_id = :actorId and m.lodMTV = :motivationId AND JSON_EXTRACT(a.assessment_doc, '$.published') = true AND JSON_EXTRACT(a.assessment_doc, '$.subject.type') = :type ORDER BY a.created_on DESC", MotivationAssessment.class)
                    .setParameter("actorId", actorId)
                    .setParameter("motivationId", motivationId)
                    .setParameter("type", subjectType);

            countQuery = em.createNativeQuery("SELECT count(a.id) FROM MotivationAssessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN t_Motivation m ON a.motivation_id = m.lodMTV where v.registry_actor_id = :actorId and m.lodMTV = :motivationId AND JSON_EXTRACT(a.assessment_doc, '$.published') = true AND JSON_EXTRACT(a.assessment_doc, '$.subject.type') = :type", Long.class)
                    .setParameter("actorId", actorId)
                    .setParameter("motivationId", motivationId)
                    .setParameter("type", subjectType);
        }

        var list = (List<MotivationAssessment>) query
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();

        var pageable = new PageQueryImpl<MotivationAssessment>();
        pageable.list = list;
        pageable.index = page;
        pageable.size = size;
        pageable.count = (Long) countQuery.getSingleResult();
        pageable.page = Page.of(page, size);

        return pageable;
    }
    /**
     * Retrieves a page of  assessment objects.
     *
     * @return A list of string objects representing the  assessment objects.
     */
    @SuppressWarnings("unchecked")
    public List<String> fetchAssessmentObjects() {

        var em = Panache.getEntityManager();

        var query = em.createNativeQuery("SELECT DISTINCT JSON_EXTRACT(a.assessment_doc, '$.subject') FROM MotivationAssessment a");

        return (List<String>) query.getResultList();

       }

}
