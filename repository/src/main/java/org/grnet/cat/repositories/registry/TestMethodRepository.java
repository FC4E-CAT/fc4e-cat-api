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
    public PageQuery<TestMethod> fetchTestMethodByPage(String search, int page, int size, Boolean enabled) {

        var filterParams = new HashMap<String, Object>();

        // --- DATA QUERY: fetch sorted test methods by usage ---
        var dataQuery = new StringJoiner(" ");
        dataQuery.add("select tm from TestMethod tm");
        dataQuery.add("left join Test t on t.testMethod.id = tm.id");
        dataQuery.add("left join MetricTestJunction mtj on mtj.test.id = t.id");
        dataQuery.add("where 1=1");

        if (StringUtils.isNotEmpty(search)) {
            dataQuery.add("and tm.labelTestMethod ilike :search");
            filterParams.put("search", "%" + search + "%");
        }

        if (enabled != null) {
            dataQuery.add("and tm.enabled = :enabled");
            filterParams.put("enabled", enabled);
        }

        dataQuery.add("group by tm.id");
        dataQuery.add("order by count(mtj.id) DESC, tm.labelTestMethod ASC");

        var panache = find(dataQuery.toString(), filterParams).page(page, size);


        // --- COUNT QUERY: count total unique TestMethods ---
        var countQuery = new StringJoiner(" ");
        countQuery.add("select count(distinct tm.id) from TestMethod tm");
        countQuery.add("left join Test t on t.testMethod.id = tm.id");
        countQuery.add("left join MetricTestJunction mtj on mtj.test.id = t.id");
        countQuery.add("where 1=1");

        if (StringUtils.isNotEmpty(search)) {
            countQuery.add("and tm.labelTestMethod ilike :search");
        }

        if (enabled != null) {
            countQuery.add("and tm.enabled = :enabled");
        }

        var em = getEntityManager();
        var query = em.createQuery(countQuery.toString());
        filterParams.forEach(query::setParameter);
        long total = (long) query.getSingleResult();


        // --- Build PageQuery Response ---
        var pageable = new PageQueryImpl<TestMethod>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = total;
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
