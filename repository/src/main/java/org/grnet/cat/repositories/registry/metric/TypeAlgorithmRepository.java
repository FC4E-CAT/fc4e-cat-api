package org.grnet.cat.repositories.registry.metric;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.metric.TypeAlgorithm;
import org.grnet.cat.repositories.Repository;
@ApplicationScoped
public class TypeAlgorithmRepository implements Repository<TypeAlgorithm, String> {

    /**
     * Retrieves a page of Type Algorithm.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Type Algorithm to include in a page.
     * @return A list of Type Algorithm objects representing the Type Algorithm in the requested page.
     */
    public PageQuery<TypeAlgorithm> fetchTypeAlgorithmByPage(int page, int size){

        var panache = find("from TypeAlgorithm", Sort.by("lastTouch", Sort.Direction.Descending).and("id", Sort.Direction.Ascending)).page(page, size);

        var pageable = new PageQueryImpl<TypeAlgorithm>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }
}
