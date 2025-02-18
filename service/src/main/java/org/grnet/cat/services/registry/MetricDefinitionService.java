package org.grnet.cat.services.registry;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.MetricDefinitionResponseDto;
import org.grnet.cat.mappers.registry.MetricDefinitionMapper;
import org.grnet.cat.repositories.registry.TypeBenchmarkRepository;
import org.grnet.cat.repositories.registry.MetricDefinitionRepository;
import org.grnet.cat.repositories.registry.metric.MetricRepository;

@ApplicationScoped
public class MetricDefinitionService {

    @Inject
    MetricDefinitionRepository metricDefinitionRepository;

    @Inject
    TypeBenchmarkRepository typeBenchmarkRepository;

    @Inject
    MetricRepository metricRepository;


    /**
     * Retrieves a page of MetricDefinitions.
     *
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of MetricDefinitions to include in a page.
     * @param uriInfo The Uri Info.
     * @return A list of MetricDefinitionResponseDto objects representing the Metric Definitions in the requested page.
     */
    public PageResource<MetricDefinitionResponseDto> getMetricDefinitionsWithSearch(String search, String sort, String order, int page, int size, UriInfo uriInfo) {

        var metricDefinitions = metricDefinitionRepository.fetchMetricAndDefinitionByPage(search, sort, order ,page, size);
        var metricDefinitionsDtos = MetricDefinitionMapper.INSTANCE.metricDefinitionToResponseDtos(metricDefinitions.list());

        return new PageResource<>(metricDefinitions, metricDefinitionsDtos , uriInfo);
    }

}