package org.grnet.cat.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Comment;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;

import java.util.HashMap;
import java.util.StringJoiner;

@ApplicationScoped
public class CommentRepository implements Repository<Comment, Long> {

    /**
     * Fetches assessment comments by page.
     *
     * @param assessmentId The ID of the assessment to retrieve comments for.
     * @param page         The index of the page to retrieve (starting from 0).
     * @param size         The maximum number of comments to include in a page.
     * @return A PageQuery of assessment comments.
     */
    public PageQuery<Comment> fetchCommentsByAssessmentId(String assessmentId, int page, int size) {
        var joiner = new StringJoiner(" ");
        joiner.add("from Comment c where c.assessment.id = :assessmentId");

        var params = new HashMap<String, Object>();
        params.put("assessmentId", assessmentId);

        joiner.add("order by c.createdOn desc");

        var panacheQuery = find(joiner.toString(), params).page(page, size);

        var pageable = new PageQueryImpl<Comment>();
        pageable.list = panacheQuery.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panacheQuery.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }
}
