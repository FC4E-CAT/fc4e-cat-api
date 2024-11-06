package org.grnet.cat.repositories.registry;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.*;
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
    public PageQuery<Principle> fetchPrincipleByPage(String search, String sort, String order, int page, int size) {

        var joiner = new StringJoiner(" ");
        joiner.add("from Principle p");

        var map = new HashMap<String, Object>();

        if (StringUtils.isNotEmpty(search)) {
            joiner.add("where (p.id like :search")
                    .add("or p.label like :search")
                    .add("or p.pri like :search)");

            map.put("search", "%" + search + "%");
        }

        joiner.add("order by p." + sort + " " + order + ", p.id ASC");

        var panache = find(joiner.toString(), map).page(page, size);

        var pageable = new PageQueryImpl<Principle>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    @Transactional
    public PageQuery<Principle> fetchPrincipleByMotivation(String motivationId, String search, String sort, String order, int page, int size) {

        var joiner = new StringJoiner(StringUtils.SPACE);

        joiner.add("select DISTINCT pri FROM PrincipleCriterionJunction cm")
                .add("join cm.principle pri")
                .add("where cm.motivation.id = :motivationId");

        var map = new HashMap<String, Object>();
        map.put("motivationId", motivationId);


        if (StringUtils.isNotEmpty(search)) {
            joiner.add("and (pri.label LIKE :search")
                    .add("or pri.description LIKE :search")
                    .add("or pri.pri LIKE :search)");
            map.put("search", "%" + search + "%");
        }

        joiner.add("ORDER BY pri." + sort + " " + order);


        var panache = find(joiner.toString(), map).page(page, size);

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

