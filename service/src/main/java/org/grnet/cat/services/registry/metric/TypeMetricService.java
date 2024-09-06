package org.grnet.cat.services.registry.metric;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.metric.TypeMetricResponseDto;
import org.grnet.cat.mappers.registry.metric.TypeMetricMapper;
import org.grnet.cat.repositories.registry.metric.TypeMetricRepository;
import org.jboss.logging.Logger;

@ApplicationScoped
public class TypeMetricService {

    @Inject
    TypeMetricRepository typeMetricRepository;

    private static final Logger LOG = Logger.getLogger(TypeMetricService.class);

    /**
     * Retrieves a specific Type Metric item by its ID.
     *
     * @param id The unique ID of the Type Metric item.
     * @return The corresponding Type Metric DTO.
     */
    public TypeMetricResponseDto getTypeMetricById(String id) {

        var typeMetric = typeMetricRepository.findById(id);

        if (typeMetric == null) {
            throw new NotFoundException("Type Metric not found.");
        }

        return TypeMetricMapper.INSTANCE.typeMetricToDto(typeMetric);
    }

    /**
     * Retrieves a paginated list of Type Metric items.
     *
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of Type Metric items to include in a page.
     * @param uriInfo The Uri Info.
     * @return A PageResource containing the Type Metric items in the requested page.
     */
    public PageResource<TypeMetricResponseDto> listAllTypeMetrics(int page, int size, UriInfo uriInfo) {

        var typeMetricPage = typeMetricRepository.fetchTypeMetricByPage(page, size);
        var typeMetricDTOs = TypeMetricMapper.INSTANCE.typeMetricToDtos(typeMetricPage.list());

        return new PageResource<>(typeMetricPage, typeMetricDTOs, uriInfo);
    }
}
