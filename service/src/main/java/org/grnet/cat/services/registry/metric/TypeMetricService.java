package org.grnet.cat.services.registry.metric;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.metric.TypeMetricResponseDto;
import org.grnet.cat.dtos.registry.metric.TypeMetricUpdateDto;
import org.grnet.cat.mappers.registry.metric.TypeMetricMapper;
import org.grnet.cat.repositories.registry.MetricTestRepository;
import org.grnet.cat.repositories.registry.metric.TypeMetricRepository;
import org.jboss.logging.Logger;

@ApplicationScoped
public class TypeMetricService {

    @Inject
    TypeMetricRepository typeMetricRepository;

    @Inject
    MetricTestRepository metricTestRepository;


    private static final Logger LOG = Logger.getLogger(TypeMetricService.class);

    /**
     * Retrieves a specific Type Metric item by its ID.
     *
     * @param id The unique ID of the Type Metric item.
     * @return The corresponding Type Metric DTO.
     */
    public TypeMetricResponseDto getTypeMetricById(String id) {

        var typeMetric = typeMetricRepository.findById(id);

        var tm = TypeMetricMapper.INSTANCE.typeMetricToDto(typeMetric);

        tm.usedByPublishedMotivations = metricTestRepository.existTypeMetricInStatus(id, Boolean.TRUE);

        return tm;
    }

    /**
     * Updates an existing TypeAlgorithm item.
     *
     * @param id      The unique ID of the TestMethod to update.
     * @param userId  The user performing the update.
     * @param request The TestMethod update data.
     * @return The updated TestMethod DTO.
     */
    @Transactional
    public TypeMetricResponseDto updateTypeMetric(String id, String userId, TypeMetricUpdateDto request) {
        if (metricTestRepository.existTypeMetricInStatus(id, Boolean.TRUE)) {
            throw new ForbiddenException("No action permitted, type metric exists in a published motivation");
        }

        var typeMetric = typeMetricRepository.findById(id);

        TypeMetricMapper.INSTANCE.updateTypeMetricFromDto(request, typeMetric);
        typeMetric.setPopulatedBy(userId);


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
    public PageResource<TypeMetricResponseDto> listAllTypeMetrics(int page, int size, Boolean enabled, UriInfo uriInfo) {

        var typeMetricPage = typeMetricRepository.fetchTypeMetricByPage(page, size, enabled);
        var typeMetricDTOs = TypeMetricMapper.INSTANCE.typeMetricToDtos(typeMetricPage.list());

        typeMetricDTOs.forEach(tm-> tm.usedByPublishedMotivations = metricTestRepository.existTypeMetricInStatus(tm.id, Boolean.TRUE));

        return new PageResource<>(typeMetricPage, typeMetricDTOs, uriInfo);
    }
}
