package org.grnet.cat.services.registry.metric;

import io.quarkus.hibernate.orm.panache.Panache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.metric.MetricRequestDto;
import org.grnet.cat.dtos.registry.metric.MetricResponseDto;
import org.grnet.cat.dtos.registry.metric.MetricUpdateDto;
import org.grnet.cat.entities.registry.metric.TypeAlgorithm;
import org.grnet.cat.entities.registry.metric.TypeMetric;
import org.grnet.cat.mappers.registry.metric.MetricMapper;

import org.grnet.cat.repositories.registry.metric.MetricRepository;
import org.grnet.cat.repositories.registry.metric.TypeAlgorithmRepository;
import org.grnet.cat.repositories.registry.metric.TypeMetricRepository;
import org.jboss.logging.Logger;

import java.util.Objects;

@ApplicationScoped
public class MetricService {

    @Inject
    MetricRepository metricRepository;

    @Inject
    TypeAlgorithmRepository typeAlgorithmRepository;

    @Inject
    TypeMetricRepository typeMetricRepository;

    private static final Logger LOG = Logger.getLogger(MetricService.class);

    /**
     * Retrieves a specific Metric item by its ID.
     *
     * @param id The unique ID of the Metric item.
     * @return The corresponding Metric DTO.
     */
    public MetricResponseDto getMetricById(String id) {

        var metric = metricRepository.findById(id);

        return MetricMapper.INSTANCE.metricToDto(metric);
    }

    /**
     * Creates a new Metric item.
     *
     * @param userId The user creating the Metric.
     * @param metricRequestDto The Metric request data.
     * @return The created Metric DTO.
     */
    @Transactional
    public MetricResponseDto createMetric(String userId, MetricRequestDto metricRequestDto) {

        var metric = MetricMapper.INSTANCE.metricToEntity(metricRequestDto);

        metric.setPopulatedBy(userId);
        metric.setTypeAlgorithm(Panache.getEntityManager().getReference(TypeAlgorithm.class, metricRequestDto.typeAlgorithmId));
        metric.setTypeMetric(Panache.getEntityManager().getReference(TypeMetric.class, metricRequestDto.typeMetricId));

        metricRepository.persist(metric);

        return MetricMapper.INSTANCE.metricToDto(metric);
    }

    /**
     * Updates an existing Metric item.
     *
     * @param id The unique ID of the Metric to update.
     * @param userId The user performing the update.
     * @param request The Metric update data.
     * @return The updated Metric DTO.
     */
    @Transactional
    public MetricResponseDto updateMetric(String id, String userId, MetricUpdateDto request) {

        var metric = metricRepository.findById(id);

        MetricMapper.INSTANCE.updateMetricFromDto(request, metric);
        metric.setPopulatedBy(userId);

        if(!Objects.isNull(request.typeAlgorithmId)){

            typeAlgorithmRepository.findByIdOptional(request.typeAlgorithmId).orElseThrow(()-> new NotFoundException("There is no Algorithm Type with the following id: "+request.typeAlgorithmId));
            metric.setTypeAlgorithm(Panache.getEntityManager().getReference(TypeAlgorithm.class, request.typeAlgorithmId));
        }

        if(!Objects.isNull(request.typeMetricId)){

            typeMetricRepository.findByIdOptional(request.typeMetricId).orElseThrow(()-> new NotFoundException("There is no Metric Type with the following id: "+request.typeMetricId));
            metric.setTypeMetric(Panache.getEntityManager().getReference(TypeMetric.class, request.typeMetricId));
        }

        return MetricMapper.INSTANCE.metricToDto(metric);
    }

    /**
     * Deletes a Metric item by its ID.
     *
     * @param id The unique ID of the Metric item.
     */
    @Transactional
    public boolean deleteMetric(String id) {

        return metricRepository.deleteById(id);
    }

    /**
     * Retrieves a page of Metric items.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Metric items to include in a page.
     * @param uriInfo The Uri Info for pagination links.
     * @return A PageResource containing the Metric items in the requested page.
     */
    public PageResource<MetricResponseDto> getMetriclistAll(int page, int size, UriInfo uriInfo) {

        var metricPage = metricRepository.fetchMetricByPage(page, size);
        var metricDtos = MetricMapper.INSTANCE.metricToDtos(metricPage.list());

        return new PageResource<>(metricPage, metricDtos, uriInfo);
    }
}
