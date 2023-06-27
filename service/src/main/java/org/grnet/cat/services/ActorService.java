package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.ActorDto;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.mappers.ActorMapper;
import org.grnet.cat.repositories.ActorRepository;

/**
 * The ActorService provides operations for managing Actor entities.
 */

@ApplicationScoped
public class ActorService {

    /**
     * Injection point for the User Repository
     */
    @Inject
    ActorRepository actorRepository;


    /**
     * Retrieves a page of actors from the database.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of actors to include in a page.
     * @param uriInfo The Uri Info.
     * @return A list of ActorDto objects representing the actors in the requested page.
     */
    public PageResource<ActorDto> getActorsByPage(int page, int size, UriInfo uriInfo){

        var actors = actorRepository.fetchActorsByPage(page, size);

        return new PageResource<>(actors, ActorMapper.INSTANCE.actorsToDto(actors.list()), uriInfo);
    }
}