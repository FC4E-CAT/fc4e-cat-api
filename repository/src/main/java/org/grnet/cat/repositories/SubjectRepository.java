package org.grnet.cat.repositories;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.Subject;

import java.util.Optional;

/**
 * The SubjectRepository interface provides data access methods for the Subject entity.
 */
@ApplicationScoped
public class SubjectRepository implements Repository<Subject, Long> {

    /**
     * Retrieves a page of subjects submitted by the specified user.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of subjects to include in a page.
     * @param userID The ID of the user.
     * @return A list of Subject objects representing the subjects in the requested page.
     */
    public PageQuery<Subject> fetchSubjectsByUserAndPage(int page, int size, String userID){

        var panache = find("from Subject v where v.createdBy = ?1", Sort.by("createdOn", Sort.Direction.Descending), userID).page(page, size);

        var pageable = new PageQueryImpl<Subject>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    public Optional<Subject> fetchSubjectByNameAndTypeAndSubjectId(String name, String type, String subjectId, String userID){

        return find("from Subject v where v.name = ?1 and v.type = ?2 and v.subjectId = ?3 and v.createdBy = ?4", name, type, subjectId, userID).firstResultOptional();
    }
}
