package org.grnet.cat.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Guidance;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;

import java.util.HashMap;
import java.util.StringJoiner;

@ApplicationScoped
public class GuidanceRepository implements Repository<Guidance, Long> {

    /**
     * Fetches guidance items by page.
     *
     * @param page   The index of the page to retrieve (starting from 0).
     * @param size   The maximum number of guidance items to include in a page.
     * @return A PageQuery of guidance items.
     */
    public PageQuery<Guidance> fetchGuidanceByPage(int page, int size) {
        var joiner = new StringJoiner(" ");
        joiner.add("from Guidance g");

        var params = new HashMap<String, Object>();

        joiner.add("order by g.createdOn desc");

        var panacheQuery = find(joiner.toString(), params).page(page, size);

        var pageable = new PageQueryImpl<Guidance>();
        pageable.list = panacheQuery.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panacheQuery.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    public boolean notUnique(String name, String value) {
        String query = "select count(g) from Guidance g where lower(g." + name + ") = lower(?1)";
        long count = getEntityManager().createQuery(query, Long.class)
                .setParameter(1, value)
                .getSingleResult();
        return count > 0;
    }

}

