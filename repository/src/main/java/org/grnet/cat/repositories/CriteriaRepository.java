package org.grnet.cat.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Criteria;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;

import java.util.HashMap;
import java.util.StringJoiner;

@ApplicationScoped
public class CriteriaRepository implements Repository<Criteria, Long> {

    /**
     * Fetches criteria items by page.
     *
     * @param page   The index of the page to retrieve (starting from 0).
     * @param size   The maximum number of criteria items to include in a page.
     * @return A PageQuery of criteria items.
     */
    public PageQuery<Criteria> fetchCriteriaByPage(int page, int size) {
        var joiner = new StringJoiner(" ");
        joiner.add("from Criteria c");

        var params = new HashMap<String, Object>();

        joiner.add("order by c.createdOn desc");

        var panacheQuery = find(joiner.toString(), params).page(page, size);

        var pageable = new PageQueryImpl<Criteria>();
        pageable.list = panacheQuery.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panacheQuery.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    public boolean notUnique(String fieldName, String value) {
        String query = "select count(c) from Criteria c where lower(c." + fieldName + ") = lower(?1)";
        long count = getEntityManager().createQuery(query, Long.class)
                .setParameter(1, value)
                .getSingleResult();
        return count > 0;
    }
}
