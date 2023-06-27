package org.grnet.cat.repositories;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Actor;


/**
 * The ActorRepository interface provides data access methods for the Actor entity.
 */
@ApplicationScoped
public class ActorRepository implements PanacheRepositoryBase<Actor, Long> {

    /**
     * Retrieves a page of from the database.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Actors to include in a page.
     * @return A list of Actor objects representing the Actors in the requested page.
     */
    public PanacheQuery<Actor> fetchActorsByPage(int page, int size){

        return findAll().page(page, size);
    }
}
