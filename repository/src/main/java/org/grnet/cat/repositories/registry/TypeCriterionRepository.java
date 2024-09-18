package org.grnet.cat.repositories.registry;


import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.Motivation;
import org.grnet.cat.entities.registry.TypeCriterion;
import org.grnet.cat.repositories.Repository;

@ApplicationScoped
public class TypeCriterionRepository implements Repository<TypeCriterion, String> {

    /**
     * Retrieves a page of Type Criterion List.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Type Criterion list to include in a page.
     * @return A list of Type Criterion  objects representing the Type Criterion list in the requested page.
     */
    public PageQuery<TypeCriterion> fetchTypeCriterionListByPage(int page, int size){

        var panache = find("from TypeCriterion", Sort.by("lastTouch", Sort.Direction.Descending).and("id", Sort.Direction.Ascending)).page(page, size);

        var pageable = new PageQueryImpl<TypeCriterion>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }
}
