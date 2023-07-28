package org.grnet.cat.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Assessment;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;

@ApplicationScoped

public class AssessmentRepository implements Repository<Assessment, Long> {

    /**
     * Retrieves a page of assessments submitted by the specified user.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of assessments to include in a page.
     * @param userID The ID of the user.
     * @return A list of Assessment objects representing the assessments in the requested page.
     */
    public PageQuery<Assessment> fetchAssessmentsByUserAndPage(int page, int size, String userID){

        var panache = find("from Assessment a where a.validation.user.id = ?1", userID).page(page, size);

        var pageable = new PageQueryImpl<Assessment>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }
}
