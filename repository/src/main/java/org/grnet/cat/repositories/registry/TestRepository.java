package org.grnet.cat.repositories.registry;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.Motivation;
import org.grnet.cat.entities.registry.Test;
import org.grnet.cat.repositories.Repository;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

import static io.quarkus.hibernate.orm.panache.Panache.getEntityManager;

@ApplicationScoped
public class TestRepository implements Repository<Test, String> {

    /**
     * Retrieves a page of Metric.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Metric to include in a page.
     * @return A list of Metric objects representing the Metrics in the requested page.
     */
    public PageQuery<Test> fetchTestByPage(String search, String sort, String order, int page, int size){

        var joiner = new StringJoiner(" ");
        joiner.add("from Test t");

        var map = new HashMap<String, Object>();

        if (StringUtils.isNotEmpty(search)) {
            joiner.add("where (t.id ilike :search")
                    .add("or t.labelTest ilike :search")
                    .add("or t.TES ilike :search")
                    .add("or t.descTest ilike :search)");

            map.put("search", "%" + search + "%");
        }

        joiner.add("order by t." + sort + " " + order + ", t.id ASC");

        var panache = find(joiner.toString(), map).page(page, size);

        var pageable = new PageQueryImpl<Test>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    @Transactional
    public List<Motivation> getMotivationIdsByTest(String testId) {

        var db = "SELECT DISTINCT m FROM Motivation m " +
                "LEFT JOIN MetricTestJunction mt ON mt.motivation.id = m.id " +
                "WHERE mt.test.id = :testId";

        return getEntityManager().createQuery(db, Motivation.class)
                .setParameter("testId", testId)
                .getResultList();
    }


    public boolean notUnique(String fieldName, String value) {
        String query = "select count(c) from Test c where lower(c." + fieldName + ") = lower(?1)";
        long count = getEntityManager().createQuery(query, Long.class)
                .setParameter(1, value)
                .getSingleResult();
        return count > 0;
    }

    public List<Test> getUpdatedTests(Timestamp assessmentCreatedTime) {
        return find("lastTouch > ?1", assessmentCreatedTime).list();
    }
}
