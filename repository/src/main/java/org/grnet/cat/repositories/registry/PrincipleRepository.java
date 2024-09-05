package org.grnet.cat.repositories.registry;

import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.Principle;
import org.grnet.cat.repositories.Repository;

import java.util.HashMap;

import java.util.StringJoiner;
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

        var joiner = new StringJoiner(" ");
        joiner.add("from Principle p");

        var params = new HashMap<String, Object>();

        joiner.add("order by p.populatedBy desc");

        var panacheQuery = find(joiner.toString(), params).page(page, size);

        var pageable = new PageQueryImpl<Principle>();
        pageable.list = panacheQuery.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panacheQuery.count();
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

