package org.grnet.cat.repositories.registry;

import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.MetricTestId;
import org.grnet.cat.entities.registry.MetricTestJunction;
import org.grnet.cat.repositories.Repository;

@ApplicationScoped
public class MetricTestRepository  implements Repository<MetricTestJunction, String> {

    /**
     * Retrieves a page of Metric-Test Relations.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Metric-Test relations to include in a page.
     * @return A list of Metric-Test Relations objects representing the Metric-Test relations in the requested page.
     */
    public PageQuery<MetricTestJunction> fetchMetricTestAll(int page, int size){

        var panache = find("from MetricTestJunction", Sort.by("lastTouch", Sort.Direction.Descending).and("id", Sort.Direction.Ascending)).page(page, size);

        var pageable = new PageQueryImpl<MetricTestJunction>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }
    
}
