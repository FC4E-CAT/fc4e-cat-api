package org.grnet.cat.repositories.registry;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.TestDefinition;
import org.grnet.cat.repositories.Repository;

@ApplicationScoped
public class TestDefinitionRepository implements Repository<TestDefinition, String> {

    /**
     * Retrieves a page of Metric.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Metric to include in a page.
     * @return A list of TestDefinition objects representing the Metrics in the requested page.
     */
    public PageQuery<TestDefinition> fetchTestDefinitionByPage(int page, int size){

        var panache = find("from TestDefinition", Sort.by("lastTouch", Sort.Direction.Descending).and("id", Sort.Direction.Ascending)).page(page, size);

        var pageable = new PageQueryImpl<TestDefinition>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }
}
