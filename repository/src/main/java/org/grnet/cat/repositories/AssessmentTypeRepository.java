package org.grnet.cat.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.AssessmentType;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;

import java.util.Optional;

@ApplicationScoped
public class AssessmentTypeRepository implements PanacheRepositoryBase<AssessmentType, Long>, Repository<AssessmentType, Long> {

    @Override
    public Optional<AssessmentType> searchByIdOptional(Long id) {
        return findByIdOptional(id);
    }

    /**
     * Retrieves from the database a page of assessment types.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of templates to include in a page.
     * @return A list of AssessmentType objects representing the assessment types in the requested page.
     */
    public PageQuery<AssessmentType> getAssessmentTypesByPage(int page, int size){

        var panache = findAll().page(page, size);

        var pageable = new PageQueryImpl<AssessmentType>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }
}
