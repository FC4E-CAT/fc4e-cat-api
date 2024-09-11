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

        var panache = find("from Metric", Sort.by("lastTouch", Sort.Direction.Descending)).page(page, size);

        var pageable = new PageQueryImpl<Metric>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }
}
