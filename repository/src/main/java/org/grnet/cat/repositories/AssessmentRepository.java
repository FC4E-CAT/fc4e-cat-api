package org.grnet.cat.repositories;

import io.quarkus.hibernate.orm.panache.Panache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.grnet.cat.entities.Assessment;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@ApplicationScoped

public class AssessmentRepository implements Repository<Assessment, String> {

    /**
     * Retrieves a page of assessments submitted by the specified user.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of assessments to include in a page.
     * @param userID The ID of the user.
     * @return A list of Assessment objects representing the assessments in the requested page.
     */
    public PageQuery<Assessment> fetchAssessmentsByUserAndPage(int page, int size, String userID){

        var panache = find("from Assessment a where a.validation.user.id = ?1 order by a.createdOn desc", userID).page(page, size);

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
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of assessments to include in a page.
     * @param typeId The ID of the Assessment Type.
     * @param actorId The Actor's id.
     * @return A list of Assessment objects representing the assessments in the requested page.
     */
    @SuppressWarnings("unchecked")
    public PageQuery<Assessment> fetchPublishedAssessmentsByTypeAndActorAndPage(int page, int size, Long typeId, Long actorId){

        var em = Panache.getEntityManager();

        var query =  em.createNativeQuery("SELECT a.id, a.assessment_doc, a.template_id, a.validation_id, a.created_on, a.updated_on FROM Assessment a INNER JOIN Template t ON a.template_id = t.id INNER JOIN Actor actor ON t.actor_id = actor.id INNER JOIN AssessmentType at ON t.assessment_type_id = at.id WHERE actor.id = :actorId AND at.id = :typeId AND JSON_EXTRACT(a.assessment_doc, '$.published') = true ORDER BY a.created_on DESC", Assessment.class)
                .setParameter("actorId", actorId)
                .setParameter("typeId", typeId);

        var countQuery =  em.createNativeQuery("SELECT count(a.id) FROM Assessment a INNER JOIN Template t ON a.template_id = t.id INNER JOIN Actor actor ON t.actor_id = actor.id INNER JOIN AssessmentType at ON t.assessment_type_id = at.id WHERE actor.id = :actorId AND at.id = :typeId AND JSON_EXTRACT(a.assessment_doc, '$.published') = true", Long.class)
                .setParameter("actorId", actorId)
                .setParameter("typeId", typeId);

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
     * Retrieves a page of assessment objects submitted by the specified user.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of assessment objects to include in a page.
     * @param userID The ID of the user.
     * @param actorID The Actor id.
     * @return A list of Assessment objects representing the assessments in the requested page.
     */
    public PageQuery<String> fetchAssessmentsObjectsByUserAndActor(int page, int size, String userID, Long actorID){


        var query =  Panache.getEntityManager().createNativeQuery("SELECT DISTINCT JSON_EXTRACT(a.assessment_doc, '$.subject') FROM Assessment a INNER JOIN Template t ON a.template_id = t.id INNER JOIN Actor actor ON t.actor_id = actor.id INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN User u ON u.id = v.user_id WHERE actor.id = :actorId AND u.id = :userId")
                .setParameter("actorId", actorID)
                .setParameter("userId", userID);

        var countQuery =  Panache.getEntityManager().createNativeQuery("SELECT count(DISTINCT JSON_EXTRACT(a.assessment_doc, '$.subject')) FROM Assessment a INNER JOIN Template t ON a.template_id = t.id INNER JOIN Actor actor ON t.actor_id = actor.id INNER JOIN Validation v ON a.validation_id = v.id INNER JOIN User u ON u.id = v.user_id WHERE actor.id = :actorId AND u.id = :userId")
                .setParameter("actorId", actorID)
                .setParameter("userId", userID);

        var list =  (List<String>) query
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
    public void deleteAssessmentById(String assessmentId){

        delete("from Assessment where id = ?1", assessmentId);
    }

    @Transactional
    public Assessment updateAssessmentDocById(String assessmentId, String assessmentDoc){

        update("assessmentDoc = ?1 , updatedOn = ?2   where id = ?3", assessmentDoc,  Timestamp.from(Instant.now()), assessmentId);

        return findById(assessmentId);
    }
}
