package org.grnet.cat.services.registry;

import io.quarkus.hibernate.orm.panache.Panache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.CriterionMetricResponseDto;
import org.grnet.cat.dtos.registry.PrincipleCriterionResponseDto;
import org.grnet.cat.dtos.registry.motivation.CriterionMetricRequest;
import org.grnet.cat.entities.registry.Criterion;
import org.grnet.cat.entities.registry.CriterionMetricJunction;
import org.grnet.cat.entities.registry.Motivation;
import org.grnet.cat.entities.registry.Relation;
import org.grnet.cat.entities.registry.metric.Metric;
import org.grnet.cat.mappers.registry.CriterionMetricMapper;
import org.grnet.cat.mappers.registry.PrincipleCriterionMapper;
import org.grnet.cat.repositories.registry.CriterionMetricRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class CriterionMetricService {

    @Inject
    CriterionMetricRepository criterionMetricRepository;

    public PageResource<CriterionMetricResponseDto> getCriterionMetricsWithSearch(String search, String sort, String order, int page, int size, UriInfo uriInfo) {

        var criterionMetrics = criterionMetricRepository.fetchCriterionMetricWithSearch(search, sort, order, page, size);
        var criterionMetricDtos = CriterionMetricMapper.INSTANCE.criterionMetricToResponseDtos(criterionMetrics.list());

        return new PageResource<>(criterionMetrics, criterionMetricDtos, uriInfo);

    }

    /**
     * Creates a new relationship between criterion, metric and motivation.
     *
     * @param motivationId the ID of the motivation.
     * @param request      the list of relationships containing criterion and metric IDs.
     * @param userId       the ID of the user performing the action.
     */
    @Transactional
    public List<String> createNewCriteriaMetricsRelationship(String motivationId, Set<CriterionMetricRequest> request, String userId) {

        var resultMessages = new ArrayList<String>();

        request.stream().iterator().forEachRemaining(req -> {

            if (!criterionMetricRepository.existsByMotivationAndCriterionAndMetricAndVersion(motivationId, req.criterionId, req.metricId, 1)) {

                var criMet = new CriterionMetricJunction(Panache.getEntityManager().getReference(Motivation.class, motivationId),
                        Panache.getEntityManager().getReference(Criterion.class, req.criterionId),
                        Panache.getEntityManager().getReference(Metric.class, req.metricId),
                        Panache.getEntityManager().getReference(Relation.class, req.relation),
                        motivationId,1);

                criMet.setPopulatedBy(userId);
                criMet.setLastTouch(Timestamp.from(Instant.now()));

                criterionMetricRepository.persist(criMet);
                resultMessages.add("criterion-metric with ids :: " + req.criterionId + " - " + req.metricId + " successfully added to Motivation.");
            } else {
                resultMessages.add("criterion-metric with ids :: " + req.criterionId + " - " + req.metricId + " already exists to Motivation.");
            }
        });

        return resultMessages;
    }

    /**
     * Updates an existing relationship between metric, criterion, and motivation.
     *
     * @param motivationId the ID of the motivation.
     * @param request      the list of relationships containing metric and criterion IDs.
     * @param userId       the ID of the user performing the action.
     */
    @Transactional
    public List<String> updateCriteriaMetricsRelationship(String motivationId, Set<CriterionMetricRequest> request, String userId) {

        var resultMessages = new ArrayList<String>();

        removeCriterionMetricRelationship(motivationId, request, resultMessages);

        request.stream().iterator().forEachRemaining(req -> {

            var junction = criterionMetricRepository.findByMotivationCriterionAndMetricAndVersion(motivationId, req.criterionId, req.metricId, 1);

            if (junction.isPresent()) {

                var existingJunction = junction.get();
                existingJunction.setLastTouch(Timestamp.from(Instant.now()));
                existingJunction.setPopulatedBy(userId);
                existingJunction.setRelation(Panache.getEntityManager().getReference(Relation.class, req.relation));

                resultMessages.add("criterion-metric with ids :: " + req.criterionId + " - " + req.metricId + " successfully updated.");
            } else {

                var criMe = new CriterionMetricJunction(Panache.getEntityManager().getReference(Motivation.class, motivationId),
                        Panache.getEntityManager().getReference(Criterion.class, req.criterionId),
                        Panache.getEntityManager().getReference(Metric.class, req.metricId),
                        Panache.getEntityManager().getReference(Relation.class, req.relation),
                        motivationId,
                        1);

                criMe.setPopulatedBy(userId);
                criMe.setLastTouch(Timestamp.from(Instant.now()));

                criterionMetricRepository.persist(criMe);
                resultMessages.add("criterion-metric with ids :: " + req.criterionId + " - " + req.metricId + " successfully added to Motivation.");
            }
        });

        return resultMessages;
    }

    public PageResource<CriterionMetricResponseDto> getCriteriaMetricsRelationship(String motivationId, int page, int size, UriInfo uriInfo) {

        var criteriaMetrics = criterionMetricRepository.fetchCriterionMetricByMotivation(motivationId, page, size);

        var criteriaMetricsResponse = CriterionMetricMapper.INSTANCE.criterionMetricToResponseDtos(criteriaMetrics.list());

        return new PageResource<>(criteriaMetrics, criteriaMetricsResponse, uriInfo);
    }

    private void removeCriterionMetricRelationship(String motivationId, Set<CriterionMetricRequest> request, List<String> resultMessages) {

        var criMe = criterionMetricRepository.fetchCriterionMetricByMotivation(motivationId);

        criMe
                .iterator()
                .forEachRemaining(crime -> {

                    var temp = new CriterionMetricRequest();
                    temp.criterionId = crime.getId().getCriterionId();
                    temp.metricId = crime.getId().getMetricId();

                    if (!request.contains(temp)) {

                        criterionMetricRepository.delete(crime);
                        resultMessages.add("criterion-metric with ids :: " + temp.criterionId + " - " + temp.metricId + " removed from Motivation.");
                    }
                });
    }
}