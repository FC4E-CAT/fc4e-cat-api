package org.grnet.cat.repositories.registry;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.registry.Criterion;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.repositories.Repository;

@ApplicationScoped
public class CriterionRepository implements Repository<Criterion, String> {

    /**
     * Fetches criteria by page.
     *
     * @param page   The index of the page to retrieve (starting from 0).
     * @param size   The maximum number of criteria to include in a page.
     * @return A PageQuery of criteria items.
     */
    public PageQuery<Criterion> fetchCriteriaByPage(int page, int size) {

        var panache = find("from Criterion", Sort.by("lastTouch", Sort.Direction.Descending)).page(page, size);

        var pageable = new PageQueryImpl<Criterion>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    public boolean notUnique(String fieldName, String value) {
        String query = "select count(c) from Criterion c where lower(c." + fieldName + ") = lower(?1)";
        long count = getEntityManager().createQuery(query, Long.class)
                .setParameter(1, value)
                .getSingleResult();
        return count > 0;
    }
}
