package org.grnet.cat.repositories.registry;

import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.MetricTestJunction;
import org.grnet.cat.entities.registry.TestDefinition;
import org.grnet.cat.repositories.Repository;

import java.util.List;

import java.util.Optional;

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

    public Optional<TestDefinition> fetchTestDefinitionByTest(String testId){
            return find("FROM TestDefinition t WHERE t.lodTES = ?1",testId)
                    .firstResultOptional();
        }


    public TestDefinition fetchTestDefinitionByTestId(String testId){

        return find("from TestDefinition td where td.lodTES = ?1", testId).firstResult();
    }

    /**
     * Fetches all TestDefinition entities associated with the provided Test IDs.
     *
     * @param testIds The list of Test IDs.
     * @return A list of TestDefinition entities.
     */
    public List<TestDefinition> fetchTestDefinitionsByTestIds(List<String> testIds){
        return list("from TestDefinition td where td.lodTES in :testIds", Parameters.with("testIds", testIds));
    }
}


