package org.grnet.cat.services.registry;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.codelist.ImperativeResponse;
import org.grnet.cat.dtos.registry.codelist.RegistryActorResponse;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.registry.Imperative;
import org.grnet.cat.entities.registry.RegistryActor;
import org.grnet.cat.mappers.registry.ImperativeMapper;
import org.grnet.cat.mappers.registry.RegistryActorMapper;
import org.grnet.cat.repositories.registry.ImperativeRepository;
import org.grnet.cat.repositories.registry.RegistryActorRepository;

@ApplicationScoped
public class RegistryActorService {
    @Inject
    RegistryActorRepository registryActorRepository;

    /**
     * Retrieves a specific RegistryActor.
     *
     * @param id The ID of the RegistryActor to retrieve.
     * @return The corresponding RegistryActor.
     */
    public RegistryActorResponse getActorById(String id) {

        var actor = registryActorRepository.findById(id);

        return RegistryActorMapper.INSTANCE.actorToDto(actor);
    }

    /**
     * Retrieves a page of RegistryActor.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of RegistryActor to include in a page.
     * @param uriInfo The Uri Info.
     * @return A list of RegistryActorResponseDto objects representing the submitted RegistryActor list in the requested page.
     */
    public PageResource<RegistryActorResponse> getActorListByPage(int page, int size, UriInfo uriInfo){

        PageQuery<RegistryActor> actorList = registryActorRepository.fetchActorsByPage(page, size);

        return new PageResource<>(actorList, RegistryActorMapper.INSTANCE.actorToDtos(actorList.list()), uriInfo);
    }

}
