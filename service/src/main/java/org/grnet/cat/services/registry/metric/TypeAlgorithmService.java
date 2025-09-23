package org.grnet.cat.services.registry.metric;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.metric.TypeAlgorithmResponseDto;
import org.grnet.cat.dtos.registry.metric.TypeAlgorithmUpdateDto;
import org.grnet.cat.mappers.registry.metric.TypeAlgorithmMapper;
import org.grnet.cat.repositories.registry.MetricTestRepository;
import org.grnet.cat.repositories.registry.metric.TypeAlgorithmRepository;
import org.jboss.logging.Logger;
@ApplicationScoped
public class TypeAlgorithmService {

    @Inject
    TypeAlgorithmRepository typeAlgorithmRepository;

    @Inject
    MetricTestRepository metricTestRepository;

    private static final Logger LOG = Logger.getLogger(TypeAlgorithmService.class);

    /**
     * Retrieves a specific Type Algorithm by its ID.
     *
     * @param id The unique ID of the Type Algorithm.
     * @return The corresponding Type Algorithm DTO.
     */
    public TypeAlgorithmResponseDto getTypeAlgorithmById(String id) {

        var typeAlgorithm = typeAlgorithmRepository.findById(id);

        var ta = TypeAlgorithmMapper.INSTANCE.typeAlgorithmToDto(typeAlgorithm);

        ta.usedByPublishedMotivations = metricTestRepository.existTypeAlgorithmInStatus(id, Boolean.TRUE);

        return ta;
    }


    /**
     * Updates an existing TypeAlgorithm item.
     *
     * @param id      The unique ID of the TestAlgorithm to update.
     * @param userId  The user performing the update.
     * @param request The TestMethod update data.
     * @return The updated TestMethod DTO.
     */
    @Transactional
    public TypeAlgorithmResponseDto updateTypeAlgorithm(String id, String userId, TypeAlgorithmUpdateDto request) {
        if (metricTestRepository.existTypeAlgorithmInStatus(id, Boolean.TRUE)) {
            throw new ForbiddenException("No action permitted, type algorithm exists in a published motivation");
        }

        var typeAlgorithm = typeAlgorithmRepository.findById(id);

        TypeAlgorithmMapper.INSTANCE.updateTypeAlgorithmFromDto(request, typeAlgorithm);
        typeAlgorithm.setPopulatedBy(userId);


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
    public PageResource<TypeAlgorithmResponseDto> getTypeAlgorithmAll(int page, int size, Boolean enable, UriInfo uriInfo) {

        var typeAlgorithmPage = typeAlgorithmRepository.fetchTypeAlgorithmByPage(page, size, enable);
        var typeAlgorithmDTOs = TypeAlgorithmMapper.INSTANCE.typeAlgorithmToDtos(typeAlgorithmPage.list());

        typeAlgorithmDTOs.forEach(ta-> ta.usedByPublishedMotivations = metricTestRepository.existTypeAlgorithmInStatus(ta.id, Boolean.TRUE));

        return new PageResource<>(typeAlgorithmPage, typeAlgorithmDTOs, uriInfo);
    }
}
