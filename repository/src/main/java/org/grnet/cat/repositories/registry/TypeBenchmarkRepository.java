package org.grnet.cat.repositories.registry;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.Imperative;
import org.grnet.cat.entities.registry.TypeBenchmark;
import org.grnet.cat.repositories.Repository;

@ApplicationScoped
public class TypeBenchmarkRepository implements Repository<TypeBenchmark, String> {

    /**
     * Retrieves a page of TypeBenchmark List.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Imperative list to include in a page.
     * @return A list of Imperative  objects representing the TypeBenchmark list in the requested page.
     */
    public PageQuery<TypeBenchmark> fetchTypeBenchmarksByPage(int page, int size){

        var panache = find("from TypeBenchmark", Sort.by("lastTouch", Sort.Direction.Descending).and("id", Sort.Direction.Ascending)).page(page, size);

        var pageable = new PageQueryImpl<TypeBenchmark>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }
}
