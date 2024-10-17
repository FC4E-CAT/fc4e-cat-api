package org.grnet.cat.services.registry;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.RelationResponse;
import org.grnet.cat.dtos.registry.codelist.RegistryActorResponse;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.registry.RegistryActor;
import org.grnet.cat.entities.registry.Relation;
import org.grnet.cat.mappers.registry.RegistryActorMapper;
import org.grnet.cat.mappers.registry.RelationMapper;
import org.grnet.cat.repositories.registry.RegistryActorRepository;
import org.grnet.cat.repositories.registry.RelationRepository;

@ApplicationScoped
public class RelationService {
    @Inject
    RelationRepository relationRepository;

    /**
     * Retrieves a specific Relation.
     *
     * @param id The ID of the Relation to retrieve.
     * @return The corresponding Relation.
     */
    public RelationResponse getRelationById(String id) {

        var relation = relationRepository.findById(id);

        return RelationMapper.INSTANCE.relationToDto(relation);
    }

    /**
     * Retrieves a page of Relation.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Relation to include in a page.
     * @param uriInfo The Uri Info.
     * @return A list of RelationResponse objects representing the submitted Relation list in the requested page.
     */
    public PageResource<RelationResponse> getRelationListByPage(int page, int size, UriInfo uriInfo){

        PageQuery<Relation> relationList = relationRepository.fetchRelationsByPage(page, size);

        return new PageResource<>(relationList, RelationMapper.INSTANCE.relationToDtos(relationList.list()), uriInfo);
    }

}

