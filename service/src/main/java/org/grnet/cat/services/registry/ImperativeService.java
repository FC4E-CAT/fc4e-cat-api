package org.grnet.cat.services.registry;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.codelist.ImperativeResponse;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.registry.Imperative;
import org.grnet.cat.mappers.registry.ImperativeMapper;
import org.grnet.cat.repositories.registry.ImperativeRepository;

@ApplicationScoped
public class ImperativeService {
    @Inject
    ImperativeRepository imperativeRepository;

    /**
     * Retrieves a specific Imperative.
     *
     * @param id The ID of the Imperative to retrieve.
     * @return The corresponding Imperative.
     */
    public ImperativeResponse getImperativeById(String id) {

        var imperative = imperativeRepository.findById(id);

        return ImperativeMapper.INSTANCE.imperativeToDto(imperative);
    }

    /**
     * Retrieves a page of Imperative.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Imperative to include in a page.
     * @param uriInfo The Uri Info.
     * @return A list of ImperativeResponseDto objects representing the submitted Imperative list in the requested page.
     */
    public PageResource<ImperativeResponse> getImperativeListByPage(int page, int size, UriInfo uriInfo){

        PageQuery<Imperative> imperativeList = imperativeRepository.fetchImperativesByPage(page, size);

        return new PageResource<>(imperativeList, ImperativeMapper.INSTANCE.imperativeToDtos(imperativeList.list()), uriInfo);
    }

}
