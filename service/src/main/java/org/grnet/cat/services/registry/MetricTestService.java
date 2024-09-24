package org.grnet.cat.services.registry;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.MetricTestResponseDto;
import org.grnet.cat.entities.registry.MetricTestId;
import org.grnet.cat.mappers.registry.MetricTestMapper;
import org.grnet.cat.repositories.registry.MetricTestRepository;

@ApplicationScoped
public class MetricTestService {

    @Inject
    MetricTestRepository metricTestRepository;

    /**
     * Retrieves a page of Metric-Test relation items.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Metric-Test relation items to include in a page.
     * @param uriInfo The Uri Info for pagination links.
     * @return A PageResource containing the Metric-Test Relation items in the requested page.
     */
    public PageResource<MetricTestResponseDto> getMetricTestWithSearch(String search, String order, String sort, int page, int size, UriInfo uriInfo) {

        var metricTestPage = metricTestRepository.fetchMetricTestWithSearch(search, order, sort, page, size);
        var metricTestDtos = MetricTestMapper.INSTANCE.metricTestToDtos(metricTestPage.list());

        return new PageResource<>(metricTestPage, metricTestDtos, uriInfo);
    }
}
