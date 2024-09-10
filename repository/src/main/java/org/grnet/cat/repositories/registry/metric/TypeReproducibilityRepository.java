package org.grnet.cat.repositories.registry.metric;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.metric.TypeReproducibility;
import org.grnet.cat.repositories.Repository;

@ApplicationScoped
public class TypeReproducibilityRepository implements Repository<TypeReproducibility, String> {

    /**
     * Fetches TypeReproducibility items by page.
     *
     * @param page   The index of the page to retrieve (starting from 0).
     * @param size   The maximum number of TypeReproducibility items to include in a page.
     * @return A PageQuery of TypeReproducibility items.
     */

    public PageQuery<TypeReproducibility> fetchTypeReproducibilityByPage(int page, int size){

        var panache = find("from TypeReproducibility", Sort.by("lastTouch", Sort.Direction.Descending)).page(page, size);

        var pageable = new PageQueryImpl<TypeReproducibility>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }
}
