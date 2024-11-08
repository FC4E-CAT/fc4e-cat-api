package org.grnet.cat.services.registry;

import io.quarkus.hibernate.orm.panache.Panache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriInfo;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.criterion.CriterionRequest;
import org.grnet.cat.dtos.registry.criterion.CriterionResponse;
import org.grnet.cat.dtos.registry.criterion.CriterionUpdate;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.criterion.DetailedCriterionDto;
import org.grnet.cat.dtos.registry.criterion.PrincipleCriterionResponse;
import org.grnet.cat.dtos.registry.template.MetricNode;
import org.grnet.cat.dtos.registry.template.Node;
import org.grnet.cat.dtos.registry.template.TestNode;
import org.grnet.cat.entities.registry.Criterion;
import org.grnet.cat.entities.registry.Imperative;
import org.grnet.cat.entities.registry.TypeCriterion;
import org.grnet.cat.exceptions.UniqueConstraintViolationException;
import org.grnet.cat.mappers.registry.CriteriaMapper;
import org.grnet.cat.mappers.registry.MotivationMapper;
import org.grnet.cat.mappers.registry.PrincipleCriterionMapper;
import org.grnet.cat.repositories.registry.*;
import org.jboss.logging.Logger;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class CriterionService {

    @Inject
    CriterionRepository criteriaRepository;

    @Inject
    ImperativeRepository imperativeRepository;

    @Inject
    TypeCriterionRepository typeCriterionRepository;

    @Inject
    CriterionMetricRepository criterionMetricRepository;

    @Inject
    PrincipleCriterionRepository principleCriterionRepository;

    private static final Logger LOG = Logger.getLogger(CriterionService.class);

    /**
     * Retrieves a page of criteria items.
     *
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of criteria items to include in a page.
     * @param uriInfo The Uri Info.
     * @return A PageResource containing the criteria items in the requested page.
     */
    public PageResource<CriterionResponse> listAll(String search, String sort, String order, int page, int size, UriInfo uriInfo) {

        var criteriaPage = criteriaRepository.fetchCriteriaByPage(search, sort, order, page, size);

        var criteriaDTOs = criteriaPage.list().stream()
                .map(this::criterionResponseWithMotivations)
                .collect(Collectors.toList());

        return new PageResource<>(criteriaPage, criteriaDTOs, uriInfo);
    }

    /**
     * Retrieves a specific Criterion by ID.
     *
     * @param id The ID of the Criterion to retrieve.
     * @return The requested Criterion.
     */
    public CriterionResponse findById(String id) {

        var criterion = criteriaRepository.findById(id);

        return criterionResponseWithMotivations(criterion);
    }

    /**
     * Creates a new criteria item.
     *
     * @param criteriaRequestDto The criteria item to create.
     * @param userId The ID of the user creating the criteria.
     * @return The created criteria item.
     */
    @Transactional
    public CriterionResponse create(CriterionRequest criteriaRequestDto, String userId) {

        if (criteriaRepository.notUnique("cri", criteriaRequestDto.cri.toUpperCase())) {
            throw new UniqueConstraintViolationException("cri", criteriaRequestDto.cri.toUpperCase());
        }
        
        var criteria = CriteriaMapper.INSTANCE.criteriaToEntity(criteriaRequestDto);

        criteria.setPopulatedBy(userId);
        criteria.setImperative(Panache.getEntityManager().getReference(Imperative.class, criteriaRequestDto.imperative));
        criteria.setTypeCriterion(Panache.getEntityManager().getReference(TypeCriterion.class, criteriaRequestDto.typeCriterion));

        criteriaRepository.persist(criteria);

        return CriteriaMapper.INSTANCE.criteriaToDto(criteria);
    }

    /**
     * Updates an existing criteria item.
     *
     * @param id                  The ID of the criteria item to update.
     * @param criteriaUpdateDto  The updated criteria item.
     * @param userId              The ID of the user updating the criteria.
     * @return The updated criteria item.
     */
    @Transactional
    public CriterionResponse update(String id, CriterionUpdate criteriaUpdateDto, String userId) {

        var criteria = criteriaRepository.findById(id);

        var currentCri = criteria.getCri();
        var updateCri = StringUtils.isNotEmpty(criteriaUpdateDto.cri) ? criteriaUpdateDto.cri.toUpperCase() : currentCri;

        if(StringUtils.isNotEmpty(updateCri) && !updateCri.equals(currentCri)) {

            if (criteriaRepository.notUnique("cri", updateCri)) {
                throw new UniqueConstraintViolationException("cri", updateCri);
            }
        }

        CriteriaMapper.INSTANCE.updateCriteria(criteriaUpdateDto, criteria);

        if(!Objects.isNull(criteriaUpdateDto.imperative)){

            imperativeRepository.findByIdOptional(criteriaUpdateDto.imperative).orElseThrow(()-> new NotFoundException("There is no Imperative Type with the following id: "+criteriaUpdateDto.imperative));
            criteria.setImperative(Panache.getEntityManager().getReference(Imperative.class, criteriaUpdateDto.imperative));
        }

        if(!Objects.isNull(criteriaUpdateDto.typeCriterion)){

            typeCriterionRepository.findByIdOptional(criteriaUpdateDto.typeCriterion).orElseThrow(()-> new NotFoundException("There is no Type Criterion with the following id: "+criteriaUpdateDto.typeCriterion));
            criteria.setTypeCriterion(Panache.getEntityManager().getReference(TypeCriterion.class, criteriaUpdateDto.typeCriterion));
        }

        criteria.setPopulatedBy(userId);

        return CriteriaMapper.INSTANCE.criteriaToDto(criteria);
    }

    /**
     * Deletes a specific criteria item by ID.
     * @param id The ID of the criteria item to delete.
     * @return True if the criteria item was deleted, false otherwise.
     */
    @Transactional
    public boolean delete(String id) {

        if(principleCriterionRepository.existsByCriterion(id)){

            throw new ForbiddenException("This Criterion cannot be deleted because it is linked to a Motivation.");
        }

        return criteriaRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {

        criteriaRepository.deleteAll();
    }

    /**
     * Retrieves a page of criteria items.
     * @param motivationId The motivation id
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of criteria items to include in a page.
     * @param uriInfo The Uri Info.
     * @return A PageResource containing the criteria items in the requested page.
     */
    public PageResource<PrincipleCriterionResponse> listCriteriaByMotivation(String motivationId, int page, int size, UriInfo uriInfo) {

        var criteriaPage = criteriaRepository.fetchCriteriaByMotivationAndPage(motivationId, page, size);
        var criteriaDTOs = PrincipleCriterionMapper.INSTANCE.criteriaToDtos(criteriaPage.list());
        return new PageResource<>(criteriaPage, criteriaDTOs, uriInfo);
    }

    /**
     * Retrieves a detailed Motivation Criterion, including the Metric and associated Metric Tests, by its Motivation ID and Criterion ID.
     *
     * @param motivationId the ID of the Motivation.
     * @param criterionId the ID of the Motivation Criterion to retrieve.
     * @return the Motivation Criterion if found.
     * @throws NotFoundException if the Motivation Criterion is not found.
     */
    public DetailedCriterionDto getMotivationCriterion(String motivationId, String criterionId) {

        criterionMetricRepository.fetchCriterionMetricByMotivationAndCriterion(motivationId, criterionId).orElseThrow(()-> new NotFoundException("Not Found Criterion."));

        var cri = criteriaRepository.fetchMotivationCriterion(motivationId, criterionId);

        var mtrMap = new HashMap<String, MetricNode>();
        var testMap = new HashMap<String, TestNode>();

        for (var row : cri) {

            Node mtrNode = mtrMap.computeIfAbsent(row.getMTR(), k -> new MetricNode(k, row.getLabelMetric().trim(), row.getLabelBenchmarkType(), Double.parseDouble(row.getValueBenchmark()), row.getLabelAlgorithmType(), row.getLabelTypeMetric()));
            Node testNode = testMap.computeIfAbsent(row.getTES(), k -> new TestNode(k, row.getLabelTest().trim(), row.getDescTest().trim(), row.getLabelTestMethod().trim(), row.getTestQuestion(), row.getTestParams(), row.getToolTip()));

            if (!mtrNode.getChildren().contains(testNode)) {
                mtrNode.addChild(testNode);
            }
        }

        var detailed = new DetailedCriterionDto();

        var mtr = mtrMap.values().stream().findFirst();

        detailed.metric = mtr.orElse(null);

        return detailed;
    }


    /**
     * This method takes a Criterion entity, converts it to a CriterionResponse, retrieves and maps
     * any associated motivations, and then sets the motivations in the response.
     *
     * @param criterion The Criterion entity to be converted and enhanced.
     * @return A CriterionResponse with associated motivations.
     */
    private CriterionResponse criterionResponseWithMotivations(Criterion criterion) {

        var criterionResponse = CriteriaMapper.INSTANCE.criteriaToDto(criterion);

        var motivations = principleCriterionRepository.getMotivationIdsByCriterion(criterion.getId());
        var motivationResponses = motivations.stream()
                .map(MotivationMapper.INSTANCE::mapPartialMotivation)
                .collect(Collectors.toList());

        criterionResponse.setMotivations(motivationResponses);

        var metrics = criterionMetricRepository.findMetricsByCriterionId(criterion.getId());

        Map<String, MetricNode> metricNodeMap = new HashMap<>();

        for (var row : metrics) {
            var metricNode = metricNodeMap.computeIfAbsent(
                    row.getMTR(),
                    key -> new MetricNode(
                            row.getMTR(),
                            row.getLabelMetric().trim(),
                            row.getLabelBenchmarkType(),
                            Double.parseDouble(row.getValueBenchmark()),
                            row.getLabelAlgorithmType(),
                            row.getLabelTypeMetric()
                    )
            );

            var uniqueTestIds = metricNode.getChildren().stream()
                    .map(child -> ((TestNode) child).getId())
                    .collect(Collectors.toSet());

            if (!uniqueTestIds.contains(row.getTES())) {
                var testNode = new TestNode(
                        row.getTES(),
                        row.getLabelTest().trim(),
                        row.getDescTest().trim(),
                        row.getLabelTestMethod(),
                        row.getTestQuestion(),
                        row.getTestParams(),
                        row.getToolTip()
                );
                metricNode.addChild(testNode);
            }
        }

        criterionResponse.setMetrics(new ArrayList<>(metricNodeMap.values()));

        return criterionResponse;
    }

}
