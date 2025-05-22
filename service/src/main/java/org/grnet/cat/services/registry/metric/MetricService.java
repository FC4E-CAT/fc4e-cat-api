package org.grnet.cat.services.registry.metric;

import io.quarkus.hibernate.orm.panache.Panache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.UriInfo;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.MetricDefinitionExtendedResponse;
import org.grnet.cat.dtos.registry.metric.MetricRequestDto;
import org.grnet.cat.dtos.registry.metric.MetricResponseDto;
import org.grnet.cat.dtos.registry.metric.MetricUpdateDto;
import org.grnet.cat.entities.registry.*;
import org.grnet.cat.entities.registry.metric.Metric;
import org.grnet.cat.entities.registry.metric.TypeAlgorithm;
import org.grnet.cat.entities.registry.metric.TypeMetric;
import org.grnet.cat.exceptions.UniqueConstraintViolationException;
import org.grnet.cat.mappers.registry.MotivationMapper;
import org.grnet.cat.mappers.registry.metric.MetricMapper;

import org.grnet.cat.repositories.registry.CriterionMetricRepository;
import org.grnet.cat.repositories.registry.MetricDefinitionRepository;
import org.grnet.cat.repositories.registry.metric.MetricRepository;
import org.grnet.cat.repositories.registry.metric.TypeAlgorithmRepository;
import org.grnet.cat.repositories.registry.metric.TypeMetricRepository;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    CriterionMetricRepository criterionMetricRepository;

    @Inject
    MetricDefinitionRepository metricDefinitionRepository;

    private static final Logger LOG = Logger.getLogger(MetricService.class);

    /**
     * Retrieves a specific Metric item by its ID.
     *
     * @param id The unique ID of the Metric item.
     * @return The corresponding Metric DTO.
     */
    public MetricDefinitionExtendedResponse getMetricById(String id) {

        var junction = metricDefinitionRepository.fetchMetricDefinitionByMetricId(id);
        var metric = junction.getMetric();

        return metricResponseWithMotivations(metric, junction);
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
    //@CheckPublishedRelation(type = PublishEntityType.METRIC,permittedStatus = false)
    @Transactional
    public MetricResponseDto updateMetric(String id, String userId, MetricUpdateDto request) {

        var metric = metricRepository.findById(id);

//        if(criterionMetricRepository.existMetricInStatus(id,Boolean.TRUE)){
//            throw new ForbiddenException("No action permitted, metric exists in a published motivation");
//        }

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

    /**
     * Retrieves a paginated list of Metric items with their associated definitions.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Metric items to include in a page.
     * @param uriInfo The UriInfo object containing request URI details for generating pagination links.
     * @return A PageResource containing the MetricDefinitionExtendedResponse items in the requested page.
     */
    public PageResource<MetricDefinitionExtendedResponse> getMetricListAll(String search, String sort, String order, int page, int size, UriInfo uriInfo) {

        var metricDefinitionPage = metricDefinitionRepository.fetchMetricAndDefinitionByPage(search, sort, order, page, size);

        var junctions = metricDefinitionPage.list();

        if (junctions.isEmpty()) {
            return new PageResource<>(metricDefinitionPage, List.of(), uriInfo);
        }

        var metricIds = junctions.stream()
                .map(j -> j.getMetric().getId())
                .distinct()
                .collect(Collectors.toList());

        var motivationsList = metricDefinitionRepository.getMotivationsForMetricIds(metricIds);

        var motivationsMap = motivationsList.stream()
                .collect(Collectors.groupingBy(
                        result -> (String) result[0],
                        Collectors.mapping(result -> (Motivation) result[1], Collectors.toList())
                ));

        var metricParentIds = junctions.stream()
                .map(j -> j.getMetric().getLodMTRV())
                .distinct()
                .collect(Collectors.toList());

        var latestMetrics = metricRepository.fetchLatestVersionMetrics(metricParentIds);

        var dtoList = latestMetrics.stream()
                .map(metric -> {

                    var junction = junctions.stream()
                            .filter(j -> j.getMetric().getLodMTRV().equals(metric.getLodMTRV()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Junction not found"));

                    var dto = metricResponseWithMotivations(metric, junction);

                    var versions = getMetricVersions(metric.getLodMTRV(), metric.getVersion());

                    var motivations = motivationsMap.getOrDefault(junction.getMetric().getId(), List.of());

                    dto.setMotivations(motivations.stream()
                            .map(MotivationMapper.INSTANCE::mapPartialMotivation)
                            .collect(Collectors.toList()));

                    dto.setVersions(versions);  // Set the versions list here


                    return dto;
                })
                .collect(Collectors.toList());

        // Return the paginated result with the list of DTOs
        return new PageResource<>(metricDefinitionPage, dtoList, uriInfo);
    }

    public List<MetricDefinitionExtendedResponse> getMetricVersions(String metricParent, Integer latestVersion) {

        var metricVersions = metricDefinitionRepository.fetchMetricAndDefinitionVersion(metricParent);

        return metricVersions.stream()
                .filter(junction -> !junction.getMetric().getVersion().equals(latestVersion)) // Exclude the latest version
                .map(junction -> {
                    var dto = metricResponseWithMotivations(junction.getMetric(), junction);
                    return dto;
                })
                .collect(Collectors.toList());
    }


    /**
     * This method takes a Metric entity, converts it to a MetricDefinitionResponseDto, retrieves and maps
     * any associated motivations, and then sets the motivations in the response.
     *
     * @param metric The Test entity to be converted and enhanced.
     * @return A PrincipleResponseDto with associated motivations.
     */
    public MetricDefinitionExtendedResponse metricResponseWithMotivations(Metric metric, MetricDefinitionJunction junction) {

        var metricAndDefinitionToDto = MetricMapper.INSTANCE.metricAndDefinitionToDto(metric, junction);

        var motivations = metricDefinitionRepository.getMotivationIdsByMetric(metric.getId());
        var motivationResponses = motivations.stream()
                .map(MotivationMapper.INSTANCE::mapPartialMotivation)
                .collect(Collectors.toList());

        metricAndDefinitionToDto.setMotivations(motivationResponses);

        return metricAndDefinitionToDto;
    }

}