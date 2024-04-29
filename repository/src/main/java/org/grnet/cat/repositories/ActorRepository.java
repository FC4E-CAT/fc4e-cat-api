package org.grnet.cat.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Actor;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;

import java.util.Optional;


/**
 * The ActorRepository interface provides data access methods for the Actor entity.
 */
@ApplicationScoped
public class ActorRepository implements Repository<Actor, Long> {

    /**
     * Retrieves a page of from the database.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Actors to include in a page.
     * @return A list of Actor objects representing the Actors in the requested page.
     */
    public PageQuery<Actor> fetchActorsByPage(int page, int size){

        var panache = findAll().page(page, size);

        var pageable = new PageQueryImpl<Actor>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }
    public Optional<Actor> fetchActorByName(String name) {

        return find("from Actor actor where name = ?1", name).stream().findFirst();
    }
}
