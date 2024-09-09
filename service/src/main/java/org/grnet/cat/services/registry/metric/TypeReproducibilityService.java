package org.grnet.cat.services.registry.metric;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.metric.TypeReproducibilityResponseDto;
import org.grnet.cat.mappers.registry.metric.TypeReproducibilityMapper;
import org.grnet.cat.repositories.registry.metric.TypeReproducibilityRepository;
import org.jboss.logging.Logger;

@ApplicationScoped
public class TypeReproducibilityService {

    @Inject
    TypeReproducibilityRepository typeReproducibilityRepository;

    private static final Logger LOG = Logger.getLogger(TypeReproducibilityService.class);

    /**
     * Retrieves a specific Type Reproducibility item by its ID.
     *
     * @param id The unique ID of the Type Reproducibility item.
     * @return The corresponding Type Reproducibility DTO.
     */
    public TypeReproducibilityResponseDto getTypeReproducibilityById(String id) {

        var typeReproducibility = typeReproducibilityRepository.findById(id);

        if (typeReproducibility == null) {
            throw new NotFoundException("Type Reproducibility not found.");
        }

        return TypeReproducibilityMapper.INSTANCE.typeReproducibilityToDto(typeReproducibility);
    }

    /**
     * Retrieves a page of Type Reproducibility items.
     *
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of criteria items to include in a page.
     * @param uriInfo The Uri Info.
     * @return A PageResource containing the Type Reproducibility items in the requested page.
     */
    public PageResource<TypeReproducibilityResponseDto> listAll(int page, int size, UriInfo uriInfo) {

        var typeReproducibilityPage = typeReproducibilityRepository.fetchTypeReproducibilityByPage(page, size);
        var typeReproducibilityDTOs = TypeReproducibilityMapper.INSTANCE.typeReproducibilityToDtos(typeReproducibilityPage.list());

        return new PageResource<>(typeReproducibilityPage, typeReproducibilityDTOs, uriInfo);
    }
}
