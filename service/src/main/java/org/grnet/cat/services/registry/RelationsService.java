package org.grnet.cat.services.registry;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.CriterionMetricResponseDto;
import org.grnet.cat.dtos.registry.MetricDefinitionResponseDto;
import org.grnet.cat.dtos.registry.MetricTestResponseDto;
import org.grnet.cat.dtos.registry.PrincipleCriterionResponseDto;
import org.grnet.cat.exceptions.InternalServerErrorException;
import org.grnet.cat.mappers.registry.CriterionMetricMapper;
import org.grnet.cat.mappers.registry.MetricDefinitionMapper;
import org.grnet.cat.mappers.registry.MetricTestMapper;
import org.grnet.cat.mappers.registry.PrincipleCriterionMapper;
import org.grnet.cat.repositories.registry.*;
import jakarta.persistence.PersistenceException;


import java.sql.SQLException;

@ApplicationScoped
public class RelationsService {

    @Inject
    PrincipleCriterionRepository principleCriterionRepository;
    @Inject
    CriterionMetricRepository criterionMetricRepository;
    @Inject
    MetricTestRepository metricTestRepository;
    @Inject
    MetricDefinitionRepository metricDefinitionRepository;
    @Inject
    MotivationRepository motivationRepository;
    @Inject
    EntityManager entityManager;

    public RelationsResponse getRelationsByMotivation(String motivationId, int page, int size, UriInfo uriInfo) {

        var principleCriterions = principleCriterionRepository.fetchPrincipleCriterionByMotivation(motivationId, page, size);
        var criterionMetrics    = criterionMetricRepository.fetchCriterionMetricByMotivation(motivationId, page, size);
        var metricTests         = metricTestRepository.fetchMetricTestByMotivation(motivationId, page, size);
        var metricDefinitions   = metricDefinitionRepository.fetchMetricDefinitionByMotivation(motivationId, page, size);

        var principleCriterionsResponse = PrincipleCriterionMapper.INSTANCE.principleCriterionToResponseDtos(principleCriterions.list());
        var criterionMetricsResponse    = CriterionMetricMapper.INSTANCE.criterionMetricToResponseDtos(criterionMetrics.list());
        var metricTestResponse          = MetricTestMapper.INSTANCE.metricTestToDtos(metricTests.list());
        var metricDefinitionResponse    = MetricDefinitionMapper.INSTANCE.metricDefinitionToResponseDtos(metricDefinitions.list());

        var responseDto = new RelationsResponse();

        responseDto.setPrincipleCriterions(new PageResource<>(principleCriterions, principleCriterionsResponse, uriInfo));
        responseDto.setCriterionMetrics(new PageResource<>(criterionMetrics, criterionMetricsResponse, uriInfo));
        responseDto.setMetricTests(new PageResource<>(metricTests, metricTestResponse, uriInfo));
        responseDto.setMetricDefinitions(new PageResource<>(metricDefinitions, metricDefinitionResponse, uriInfo));

        return responseDto;
    }

    @Transactional
    public void copyRelationsToNewMotivation(String newMotivationId, String sourceMotivationId) {

        try {
            entityManager
                    .createNativeQuery("CALL CopyRelationsToNewMotivation(:newMotivationId, :sourceMotivationId)")
                    .setParameter("newMotivationId", newMotivationId)
                    .setParameter("sourceMotivationId", sourceMotivationId)
                    .executeUpdate();
        } catch (PersistenceException e) {
            if (e.getCause() instanceof SQLException) {
                throw new InternalServerErrorException(e.getCause().getMessage(), 500);
            }
        }
    }

    @Setter
    @Getter
    public static class RelationsResponse {

        private PageResource<PrincipleCriterionResponseDto> principleCriterions;
        private PageResource<CriterionMetricResponseDto> criterionMetrics;
        private PageResource<MetricTestResponseDto> metricTests;
        private PageResource<MetricDefinitionResponseDto> metricDefinitions;

    }
}
