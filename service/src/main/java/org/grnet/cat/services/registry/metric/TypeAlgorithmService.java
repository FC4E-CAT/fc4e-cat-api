package org.grnet.cat.services.registry.metric;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.metric.TypeAlgorithmResponseDto;
import org.grnet.cat.mappers.registry.metric.TypeAlgorithmMapper;
import org.grnet.cat.repositories.registry.metric.TypeAlgorithmRepository;
import org.jboss.logging.Logger;
@ApplicationScoped
public class TypeAlgorithmService {

    @Inject
    TypeAlgorithmRepository typeAlgorithmRepository;

    private static final Logger LOG = Logger.getLogger(TypeAlgorithmService.class);

    /**
     * Retrieves a specific Type Algorithm by its ID.
     *
     * @param id The unique ID of the Type Algorithm.
     * @return The corresponding Type Algorithm DTO.
     */
    public TypeAlgorithmResponseDto getTypeAlgorithmById(String id) {

        var typeAlgorithm = typeAlgorithmRepository.findById(id);

        return TypeAlgorithmMapper.INSTANCE.typeAlgorithmToDto(typeAlgorithm);
    }

    /**
     * Retrieves a page of Type Algorithm items.
     *
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of criteria items to include in a page.
     * @param uriInfo The Uri Info.
     * @return A PageResource containing the Type Algorithm items in the requested page.
     */
    public PageResource<TypeAlgorithmResponseDto> listAll(int page, int size, UriInfo uriInfo) {

        var typeAlgorithmPage = typeAlgorithmRepository.fetchTypeAlgorithmByPage(page, size);
        var typeAlgorithmDTOs = TypeAlgorithmMapper.INSTANCE.typeAlgorithmToDtos(typeAlgorithmPage.list());

        return new PageResource<>(typeAlgorithmPage, typeAlgorithmDTOs, uriInfo);
    }
}
