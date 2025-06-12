package org.grnet.cat.repositories.registry;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.Motivation;
import org.grnet.cat.entities.registry.TestMethod;
import org.grnet.cat.repositories.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@ApplicationScoped
public class TestMethodRepository implements Repository<TestMethod, String> {

    /**
     * Retrieves a page of Metric.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Metric to include in a page.
     * @return A list of TestMethod objects representing the Metrics in the requested page.
     */
    public PageQuery<TestMethod> fetchTestMethodByPage(String search, int page, int size, Boolean enabled){

        var joiner = new StringJoiner(" ");
        joiner.add("from TestMethod tm");
        joiner.add("where 1=1");

        var map = new HashMap<String, Object>();

        if (StringUtils.isNotEmpty(search)) {
            joiner.add("and tm.labelTestMethod ilike :search");
            map.put("search", "%" + search + "%");
        }

        if(!Objects.isNull(enabled)){

            joiner.add("and tm.enabled = :enabled");
            map.put("enabled", enabled);
        }

        joiner.add("order by tm.labelTestMethod ASC, tm.id ASC");

        var panache = find(joiner.toString(), map).page(page, size);

        var pageable = new PageQueryImpl<TestMethod>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    @Transactional
    public List<Motivation> getMotivationIdsByTestMethodId(String testMethodId) {

        var db = "SELECT DISTINCT m FROM Motivation m " +
                "LEFT JOIN MetricTestJunction mt ON mt.motivation.id = m.id " +
                "WHERE mt.test.testMethod.id = :testMethodId and m.published = :published";

        return getEntityManager().createQuery(db, Motivation.class)
                .setParameter("testMethodId", testMethodId)
                .setParameter("published", Boolean.TRUE)
                .getResultList();
    }
}
