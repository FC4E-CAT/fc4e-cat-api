package org.grnet.cat.repositories.registry;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.RegistryActor;
import org.grnet.cat.repositories.Repository;

@ApplicationScoped
public class RegistryActorRepository implements Repository<RegistryActor, String> {

    /**
     * Retrieves a page of Actor List.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Actor list to include in a page.
     * @return A list of Actor  objects representing the Actor list in the requested page.
     */
    public PageQuery<RegistryActor> fetchActorsByPage(int page, int size) {

        var panache = find("from RegistryActor", Sort.by("lastTouch", Sort.Direction.Descending)).page(page, size);

        var pageable = new PageQueryImpl<RegistryActor>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }
}
