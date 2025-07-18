package org.grnet.cat.services.registry.metric;

import io.quarkus.hibernate.orm.panache.Panache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.UriInfo;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.metric.MetricRequestDto;
import org.grnet.cat.dtos.registry.metric.MetricResponseDto;
import org.grnet.cat.dtos.registry.metric.MetricUpdateDto;
import org.grnet.cat.dtos.registry.metric.MetricVersionRequestDto;
import org.grnet.cat.dtos.registry.metric.TypeCombinationDto;
import org.grnet.cat.entities.registry.*;
import org.grnet.cat.entities.registry.metric.Metric;
import org.grnet.cat.entities.registry.metric.TypeAlgorithm;
import org.grnet.cat.entities.registry.metric.TypeMetric;
import org.grnet.cat.exceptions.UniqueConstraintViolationException;
import org.grnet.cat.mappers.registry.MotivationMapper;
import org.grnet.cat.mappers.registry.metric.MetricMapper;

import org.grnet.cat.repositories.registry.CriterionMetricRepository;
import org.grnet.cat.repositories.registry.CriterionRepository;
import org.grnet.cat.repositories.registry.TypeBenchmarkRepository;
import org.grnet.cat.repositories.registry.metric.MetricRepository;
import org.grnet.cat.repositories.registry.metric.TypeAlgorithmRepository;
import org.grnet.cat.repositories.registry.metric.TypeMetricRepository;
import org.jboss.logging.Logger;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class MetricService {

    @Inject
    MetricRepository metricRepository;

    @Inject
    TypeAlgorithmRepository typeAlgorithmRepository;

    @Inject
    TypeMetricRepository typeMetricRepository;

    @Inject
    TypeBenchmarkRepository typeBenchmarkRepository;
    @Inject
    CriterionMetricRepository criterionMetricRepository;

    @Inject
    CriterionRepository criterionRepository;

    private static final Logger LOG = Logger.getLogger(MetricService.class);

    /**
     * Retrieves a specific Metric item by its ID.
     *
     * @param id The unique ID of the Metric item.
     * @return The corresponding Metric DTO.
     */
    public MetricResponseDto getMetricById(String id) {

        var metric = metricRepository.findById(id);

        return metricResponseWithMotivations(metric);
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

        if (metricRepository.notUnique("MTR", metricRequestDto.MTR.toUpperCase())) {
            throw new UniqueConstraintViolationException("MTR", metricRequestDto.MTR.toUpperCase());
        }

        var metric = MetricMapper.INSTANCE.metricToEntity(metricRequestDto);

        metric.setPopulatedBy(userId);
        metric.setTypeAlgorithm(Panache.getEntityManager().getReference(TypeAlgorithm.class, metricRequestDto.typeAlgorithmId));
        metric.setTypeMetric(Panache.getEntityManager().getReference(TypeMetric.class, metricRequestDto.typeMetricId));
        metric.setTypeBenchmark(Panache.getEntityManager().getReference(TypeBenchmark.class, metricRequestDto.typeBenchmarkId));
        metric.setVersion(1);

        metricRepository.persist(metric);

        metric.setLodMTRV(metric.getId());

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
    //@CheckPublishedRelation(type = PublishEntityType.METRIC,permittedStatus = false)
    @Transactional
    public MetricResponseDto updateMetric(String id, String userId, MetricUpdateDto request) {

        var metric = metricRepository.findById(id);

        if(StringUtils.isNotEmpty(request.MTR) && !metric.getMTR().equalsIgnoreCase(request.MTR)){

            if (metricRepository.notUnique("MTR", request.MTR)) {
                throw new UniqueConstraintViolationException("MTR", request.MTR);
            }
        }

        MetricMapper.INSTANCE.updateMetricFromDto(request, metric);
        metric.setPopulatedBy(userId);

        if(!Objects.isNull(request.typeAlgorithmId)){

            typeAlgorithmRepository.findById(request.typeAlgorithmId);
            metric.setTypeAlgorithm(Panache.getEntityManager().getReference(TypeAlgorithm.class, request.typeAlgorithmId));
        }

        if(!Objects.isNull(request.typeMetricId)){

            typeMetricRepository.findById(request.typeMetricId);
            metric.setTypeMetric(Panache.getEntityManager().getReference(TypeMetric.class, request.typeMetricId));
        }

        if(!Objects.isNull(request.typeBenchmarkId)){

            typeBenchmarkRepository.findById(request.typeBenchmarkId);
            metric.setTypeMetric(Panache.getEntityManager().getReference(TypeMetric.class, request.typeMetricId));
        }

        return MetricMapper.INSTANCE.metricToDto(metric);
    }

    /**
     * Deletes a Metric item by its ID.
     *
     * @param id The unique ID of the Metric item.
     */
    //@CheckPublishedRelation(type = PublishEntityType.METRIC,permittedStatus = false)
    @Transactional
    public boolean deleteMetric(String id) {
        if(criterionMetricRepository.existMetricInStatus(id,Boolean.TRUE)){
            throw new ForbiddenException("No action permitted, metric exists in a published motivation");
        }

        var metric = metricRepository.findById(id);

        if (metric != null && metric.getVersion() == 1 && metricRepository.countVersion(metric.getId()) > 1) {
            throw new ForbiddenException("Cannot delete version 1 of the metric as more versions exist");
        }

        return metricRepository.deleteById(id);
    }

    /**
     * Retrieves a paginated list of Metric items.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Metric items to include in a page.
     * @param uriInfo The UriInfo object containing request URI details for generating pagination links.
     * @return A PageResource containing the MetricResponseDto items in the requested page.
     */
    public PageResource<MetricResponseDto> getMetricListAll(String search, String sort, String order, int page, int size, UriInfo uriInfo) {

        var metricPage = metricRepository.fetchMetricByPage(search, sort, order, page, size);
        var metrics = metricPage.list();

        if (metrics.isEmpty()) {
            return new PageResource<>(metricPage, List.of(), uriInfo);
        }

        var dtoList = metrics.stream()
                .map(metric -> {
                    var latestVersion = metric.getVersion();
                    var versions = getMetricVersions(metric.getLodMTRV(), latestVersion);

                    var metricResponse = metricResponseWithMotivations(metric);

                    metricResponse.setVersions(versions);

                    return metricResponse;
                })
                .collect(Collectors.toList());

        return new PageResource<>(metricPage, dtoList, uriInfo);
    }

    public List<MetricResponseDto> getMetricVersions(String metricParent, Integer latestVersion) {

        var metricVersions = metricRepository.fetchMetricAllVersions(metricParent);

        var metricVersionsWithoutLatest = metricVersions.stream()
                .filter(metric -> !metric.getVersion().equals(latestVersion)) // Filter out the latest version
                .collect(Collectors.toList());

        if (metricVersionsWithoutLatest.isEmpty()) {
            return List.of();
        }
        var dtoList = metricVersionsWithoutLatest.stream()
                .map(this::metricResponseWithMotivations)
                .collect(Collectors.toList());

        return dtoList;
    }


    @Transactional
    public MetricResponseDto versionMetric(String id, String userId, MetricVersionRequestDto request) {

        var parentId = metricRepository.findById(id).getLodMTRV();
        var parentTestVersion = metricRepository.countVersion(parentId);

        var childMetric = MetricMapper.INSTANCE.versionMetricToEntity(request);

        childMetric.setMTR(metricRepository.findById(id).getMTR());
        childMetric.setPopulatedBy(userId);
        childMetric.setLodMTRV(metricRepository.findById(id).getLodMTRV());
        childMetric.setPopulatedBy(userId);
        childMetric.setTypeAlgorithm(Panache.getEntityManager().getReference(TypeAlgorithm.class, request.typeAlgorithmId));
        childMetric.setTypeMetric(Panache.getEntityManager().getReference(TypeMetric.class, request.typeMetricId));
        childMetric.setTypeBenchmark(Panache.getEntityManager().getReference(TypeBenchmark.class, request.typeBenchmarkId));

        var newVersion = (int) parentTestVersion + 1 ;

        childMetric.setVersion(newVersion);
        metricRepository.persist(childMetric);

        return MetricMapper.INSTANCE.metricToDto(childMetric);
    }


    /**
     * This method takes a Metric entity, converts it to a MetricResponseDto, retrieves and maps
     * any associated motivations, and then sets the motivations in the response.
     *
     * @param metric The Test entity to be converted and enhanced.
     * @return A PrincipleResponseDto with associated motivations.
     */
    public MetricResponseDto metricResponseWithMotivations(Metric metric) {

        var metricResponseToDto = MetricMapper.INSTANCE.metricToDto(metric);

        var motivations = metricRepository.getMotivationIdsByMetric(metric.getId());
        var motivationResponses = motivations.stream()
                .map(MotivationMapper.INSTANCE::mapPartialMotivation)
                .collect(Collectors.toList());

        metricResponseToDto.setMotivations(motivationResponses);

        return metricResponseToDto;
    }

    public List<TypeCombinationDto> getMetricTypeStatistics() {
        List<Object[]> results = metricRepository.fetchMetricTypeAlgorithmCombinations();

        return results.stream()
                .map(result -> {
                    var dto = new TypeCombinationDto();
                    dto.setTypeMetric((String) result[0]);
                    dto.setTypeAlgorithm((String) result[1]);
                    dto.setTypeBenchmark((String) result[2]);
                    dto.setUsageCount(((Number) result[3]).intValue());

                    return dto;
                })
                .collect(Collectors.toList());
    }
}