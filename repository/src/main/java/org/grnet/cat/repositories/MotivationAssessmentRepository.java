package org.grnet.cat.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.entities.Assessment;
import org.grnet.cat.entities.MotivationAssessment;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;

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
}
