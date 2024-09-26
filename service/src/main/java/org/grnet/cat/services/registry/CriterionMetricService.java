package org.grnet.cat.services.registry;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.CriterionMetricResponseDto;
import org.grnet.cat.mappers.registry.CriterionMetricMapper;
import org.grnet.cat.repositories.registry.CriterionMetricRepository;

@ApplicationScoped
public class CriterionMetricService {

    @Inject
    CriterionMetricRepository criterionMetricRepository;

    public PageResource<CriterionMetricResponseDto> getCriterionMetricsWithSearch(String search, String sort, String order, int page, int size, UriInfo uriInfo) {

        var criterionMetrics = criterionMetricRepository.fetchCriterionMetricWithSearch(search, sort, order, page, size);
        var criterionMetricDtos = CriterionMetricMapper.INSTANCE.criterionMetricToResponseDtos(criterionMetrics.list());

        return new PageResource<>(criterionMetrics, criterionMetricDtos, uriInfo);

    }
}
