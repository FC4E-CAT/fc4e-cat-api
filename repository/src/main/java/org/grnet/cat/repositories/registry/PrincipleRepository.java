package org.grnet.cat.repositories.registry;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.Principle;
import org.grnet.cat.repositories.Repository;

@ApplicationScoped
public class PrincipleRepository implements Repository<Principle, String> {

    /**
     * Fetches guidance items by page.
     *
     * @param page   The index of the page to retrieve (starting from 0).
     * @param size   The maximum number of guidance items to include in a page.
     * @return A PageQuery of guidance items.
     */
    public PageQuery<Principle> fetchPrincipleByPage(int page, int size) {

        var panache = find("from Principle", Sort.by("lastTouch", Sort.Direction.Descending)).page(page, size);

        var pageable = new PageQueryImpl<Principle>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    public boolean notUnique(String name, String value) {
        String query = "select count(p) from Principle p where lower(p." + name + ") = lower(?1)";
        long count = getEntityManager().createQuery(query, Long.class)
                .setParameter(1, value)
                .getSingleResult();
        return count > 0;
    }
}

