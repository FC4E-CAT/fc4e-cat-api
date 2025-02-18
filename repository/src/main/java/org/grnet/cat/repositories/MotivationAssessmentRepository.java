package org.grnet.cat.repositories;

import io.quarkus.hibernate.orm.panache.Panache;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.entities.AssessmentPerActor;
import org.grnet.cat.entities.MotivationAssessment;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

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
        joiner.add("select a.* from MotivationAssessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN CatUser u ON v.user_id = u.id INNER JOIN t_Actor actor ON v.registry_actor_id = actor.lodActor where (u.id = :userID or a.id IN :shareableIds)");

        var map = new HashMap<String, Object>();
        map.put("userID", userID);
        map.put("shareableIds", shareableIds);

        var counter = new StringJoiner(StringUtils.SPACE);
        counter.add("select count(a) from MotivationAssessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN CatUser u ON v.user_id = u.id INNER JOIN t_Actor actor ON v.registry_actor_id = actor.lodActor where (u.id = :userID or a.id IN :shareableIds)");

        if (StringUtils.isNotEmpty(subjectName)) {

            joiner.add("AND a.assessment_doc->'subject'->>'name' = :name");
            counter.add("AND a.assessment_doc->'subject'->>'name' = :name");

            map.put("name", subjectName);
        }

        if (StringUtils.isNotEmpty(subjectType)) {

            joiner.add("AND a.assessment_doc->'subject'->>'type' = :type");
            counter.add("AND a.assessment_doc->'subject'->>'type' = :type");
            map.put("type", subjectType);
        }

        if (!Objects.isNull(actorID)) {

            joiner.add("AND actor.lodActor = :actorID");
            counter.add("AND actor.lodActor = :actorID");
            map.put("actorID", actorID);
        }

        joiner.add("order by a.created_on desc");

        var em = Panache.getEntityManager();

        var query = em.createNativeQuery(joiner.toString(), MotivationAssessment.class);

        var countQuery = em.createNativeQuery(counter.toString(), Long.class);


        for (Map.Entry<String, Object> entry : map.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
            countQuery.setParameter(entry.getKey(), entry.getValue());
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

        var query = em.createNativeQuery("SELECT DISTINCT (a.assessment_doc->>'subject') FROM MotivationAssessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN t_Motivation m ON a.motivation_id = m.lodMTV where v.registry_actor_id = :actorId and m.lodMTV = :motivationId AND a.published = :published")
                .setParameter("actorId", actorId)
                .setParameter("published", true)
                .setParameter("motivationId", motivationId);

        var countQuery = em.createNativeQuery("SELECT count(DISTINCT (a.assessment_doc->>'subject')) FROM MotivationAssessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN t_Motivation m ON a.motivation_id = m.lodMTV where v.registry_actor_id = :actorId and m.lodMTV = :motivationId AND a.published = :published")
                .setParameter("actorId", actorId)
                .setParameter("published", true)
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

        var query = em.createNativeQuery("SELECT a.id, a.assessment_doc, a.validation_id, a.created_on, a.updated_on, a.subject_id, a.updated_by, a.shared, a.motivation_id , a.published FROM MotivationAssessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN t_Motivation m ON a.motivation_id = m.lodMTV where v.registry_actor_id = :actorId and m.lodMTV = :motivationId AND a.published = :published ORDER BY a.created_on DESC", MotivationAssessment.class)
                .setParameter("actorId", actorId)
                .setParameter("published", true)
                .setParameter("motivationId", motivationId);

        var countQuery = em.createNativeQuery("SELECT count(a.id) FROM MotivationAssessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN t_Motivation m ON a.motivation_id = m.lodMTV where v.registry_actor_id = :actorId and m.lodMTV = :motivationId AND a.published = :published", Long.class)
                .setParameter("actorId", actorId)
                .setParameter("published", true)
                .setParameter("motivationId", motivationId);

        if (StringUtils.isNotEmpty(subjectName) && StringUtils.isNotEmpty(subjectType)) {

            query = em.createNativeQuery("SELECT a.id, a.assessment_doc, a.validation_id, a.created_on, a.updated_on, a.subject_id, a.updated_by, a.shared, a.motivation_id , a.published FROM MotivationAssessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN t_Motivation m ON a.motivation_id = m.lodMTV where v.registry_actor_id = :actorId and m.lodMTV = :motivationId AND a.published = :published AND a.assessment_doc->'subject'->>'name' = :name AND a.assessment_doc->'subject'->>'type' = :type ORDER BY a.created_on DESC", MotivationAssessment.class)
                    .setParameter("actorId", actorId)
                    .setParameter("published", true)
                    .setParameter("motivationId", motivationId)
                    .setParameter("name", subjectName)
                    .setParameter("type", subjectType);

            countQuery = em.createNativeQuery("SELECT count(a.id)  FROM MotivationAssessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN t_Motivation m ON a.motivation_id = m.lodMTV where v.registry_actor_id = :actorId and m.lodMTV = :motivationId AND a.published = :published AND assessment_doc->'subject'->>'name' = :name AND a.assessment_doc->'subject'->>'type' = :type", Long.class)
                    .setParameter("actorId", actorId)
                    .setParameter("published", true)
                    .setParameter("motivationId", motivationId)
                    .setParameter("name", subjectName)
                    .setParameter("type", subjectType);


        } else if (StringUtils.isNotEmpty(subjectName)) {

            query = em.createNativeQuery("SELECT a.id, a.assessment_doc, a.validation_id, a.created_on, a.updated_on, a.subject_id, a.updated_by, a.shared, a.motivation_id , a.published FROM MotivationAssessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN t_Motivation m ON a.motivation_id = m.lodMTV where v.registry_actor_id = :actorId and m.lodMTV = :motivationId AND a.published :published AND a.assessment_doc->'subject'->>'name' = :name ORDER BY a.created_on DESC", MotivationAssessment.class)
                    .setParameter("actorId", actorId)
                    .setParameter("published", true)
                    .setParameter("motivationId", motivationId)
                    .setParameter("name", subjectName);

            countQuery = em.createNativeQuery("SELECT count(a.id) FROM MotivationAssessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN t_Motivation m ON a.motivation_id = m.lodMTV where v.registry_actor_id = :actorId and m.lodMTV = :motivationId AND a.published = :published AND a.assessment_doc->'subject'->>'name' = :name", Long.class)
                    .setParameter("actorId", actorId)
                    .setParameter("published", true)
                    .setParameter("motivationId", motivationId)
                    .setParameter("name", subjectName);

        } else if (StringUtils.isNotEmpty(subjectType)) {

            query = em.createNativeQuery("SELECT a.id, a.assessment_doc, a.validation_id, a.created_on, a.updated_on, a.subject_id, a.updated_by, a.shared, a.motivation_id , a.published FROM MotivationAssessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN t_Motivation m ON a.motivation_id = m.lodMTV where v.registry_actor_id = :actorId and m.lodMTV = :motivationId AND (a.published = :published AND a.assessment_doc->'subject'->>'type' = :type ORDER BY a.created_on DESC", MotivationAssessment.class)
                    .setParameter("actorId", actorId)
                    .setParameter("published", true)
                    .setParameter("motivationId", motivationId)
                    .setParameter("type", subjectType);

            countQuery = em.createNativeQuery("SELECT count(a.id) FROM MotivationAssessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN t_Motivation m ON a.motivation_id = m.lodMTV where v.registry_actor_id = :actorId and m.lodMTV = :motivationId AND a.published = :published AND a.assessment_doc->'subject'->>'type' = :type", Long.class)
                    .setParameter("actorId", actorId)
                    .setParameter("published", true)
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
    public PageQuery<String> fetchAssessmentObjects(int page, int size, String userID) {

        var em = Panache.getEntityManager();

        var query = em.createNativeQuery("SELECT DISTINCT (a.assessment_doc->>'subject') FROM MotivationAssessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN CatUser u ON u.id = v.user_id WHERE u.id = :userId")
                .setParameter("userId", userID);

        var list = (List<String>) query
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();

        var countQuery = em.createNativeQuery("SELECT count(DISTINCT (a.assessment_doc->>'subject')) FROM MotivationAssessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN CatUser u ON u.id = v.user_id WHERE u.id = :userId")
                .setParameter("userId", userID);

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
    public PageQuery<String> fetchAssessmentsObjectsByUserAndActor(int page, int size, String userID, String actorID) {


        var query = Panache.getEntityManager().createNativeQuery(
                        "SELECT DISTINCT (a.assessment_doc->>'subject') " +
                                "FROM MotivationAssessment a " +
                                "INNER JOIN Validation v ON a.validation_id = v.id " +
                                "INNER JOIN t_Actor ma ON v.registry_actor_id = ma.lodActor " +
                                "WHERE ma.lodActor = :actorId AND v.user_id = :userId"
                )
                .setParameter("actorId", actorID)
                .setParameter("userId", userID);

        var countQuery = Panache.getEntityManager().createNativeQuery(
                        "SELECT COUNT(DISTINCT (a.assessment_doc->>'subject')) " +
                                "FROM MotivationAssessment a " +
                                "INNER JOIN Validation v ON a.validation_id = v.id " +
                                "INNER JOIN t_Actor ma ON v.registry_actor_id = ma.lodActor " +
                                "WHERE ma.lodActor = :actorId AND v.user_id = :userId"
                )
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
     * Retrieves a page of assessments.
     *
     * @param page   The index of the page to retrieve (starting from 0).
     * @param size   The maximum number of assessments to include in a page.
     * @return A list of Assessment objects representing the assessments in the requested page.
     */
    public PageQuery<MotivationAssessment> fetchAllAssessmentsByPage(int page, int size, String search) {

        var joiner = new StringJoiner(StringUtils.SPACE);
        joiner.add("select a.* from MotivationAssessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN CatUser u ON v.user_id = u.id");

        var counter = new StringJoiner(StringUtils.SPACE);
        counter.add("select count(a) from MotivationAssessment a INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN CatUser u ON v.user_id = u.id");

        var params = new HashMap<String, Object>();

        if (search != null && !search.isEmpty()) {
            joiner.add("WHERE a.assessment_doc->>'name' LIKE :search OR u.email LIKE :search OR u.name LIKE :search OR u.surname LIKE :search");
            counter.add("WHERE a.assessment_doc->>'name' LIKE :search OR u.email LIKE :search OR u.name LIKE :search OR u.surname LIKE :search");
            params.put("search", "%" + search + "%");
        }

        joiner.add("order by a.created_on desc");

        var em = Panache.getEntityManager();

        var query = em.createNativeQuery(joiner.toString(), MotivationAssessment.class);

        var countQuery = em.createNativeQuery(counter.toString(), Long.class);

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
            countQuery.setParameter(entry.getKey(), entry.getValue());
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
     * Retrieves a page of assessment objects for a specific subject.
     *
     * @param page      The index of the page to retrieve (starting from 0).
     * @param size      The maximum number of assessment objects to include in a page.
     * @param subjectId The ID of the Subject.
     * @return A list of Assessment objects representing the assessments in the requested page.
     */
    public PageQuery<MotivationAssessment> fetchAssessmentsPerSubjectAndPage(int page, int size, Long subjectId) {

        var panache = find("from MotivationAssessment a where a.subject.id =?1 ", subjectId).page(page, size);

        var pageable = new PageQueryImpl<MotivationAssessment>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);
        return pageable;
    }

    public List<AssessmentPerActor> getStatistics() {

        return find(" SELECT DISTINCT act.labelActor as actor_name, COUNT(*) as total FROM MotivationAssessment a inner join Validation v on v.id = a.validation.id inner join RegistryActor act on v.registryActor.id = act.id GROUP BY actor_name").project(AssessmentPerActor.class).stream().collect(Collectors.toList());
    }

    public Long countAllAssessmentsByUser(String userId) {

        return count("from MotivationAssessment a where a.validation.user.id = ?1", userId);
    }

}
