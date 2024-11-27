package org.grnet.cat.repositories.registry.metric;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.metric.Metric;
import org.grnet.cat.repositories.Repository;

@ApplicationScoped
public class MetricRepository implements Repository<Metric, String> {

    /**
     * Retrieves a page of Metric.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Metric to include in a page.
     * @return A list of Metric objects representing the Metrics in the requested page.
     */
    public PageQuery<Metric> fetchMetricByPage(int page, int size){

        var panache = find("from Metric", Sort.by("lastTouch", Sort.Direction.Descending).and("id", Sort.Direction.Ascending)).page(page, size);

        var pageable = new PageQueryImpl<Metric>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    /**
     * Checks if the specified value for a given field in the Metric entity is not unique.
     *
     * @param name  The name of the field to check (e.g., "mtr").
     * @param value The value to verify for uniqueness.
     * @return true if the value already exists, false otherwise.
     */
    public boolean notUnique(String name, String value) {
        String query = "SELECT COUNT(m) FROM Metric m WHERE LOWER(m." + name + ") = LOWER(?1)";
        long count = getEntityManager().createQuery(query, Long.class)
                .setParameter(1, value)
                .getSingleResult();
        return count > 0;
    }


}
