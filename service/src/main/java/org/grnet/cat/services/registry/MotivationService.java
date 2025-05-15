package org.grnet.cat.services.registry;

import io.quarkus.hibernate.orm.panache.Panache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriInfo;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.MetricDefinitionExtendedResponse;
import org.grnet.cat.dtos.registry.MetricDefinitionRequest;
import org.grnet.cat.dtos.registry.PrincipleCriterionResponseDto;
import org.grnet.cat.dtos.registry.actor.MotivationActorRequest;
import org.grnet.cat.dtos.registry.actor.MotivationActorResponse;
import org.grnet.cat.dtos.registry.metric.DetailedMetricDto;
import org.grnet.cat.dtos.registry.metric.MetricRequestDto;
import org.grnet.cat.dtos.registry.metric.MetricUpdateDto;
import org.grnet.cat.dtos.registry.metric.MotivationMetricExtendedRequest;
import org.grnet.cat.dtos.registry.metric.MotivationMetricUpdateRequest;
import org.grnet.cat.dtos.registry.motivation.*;
import org.grnet.cat.dtos.registry.principle.MotivationPrincipleExtendedRequestDto;
import org.grnet.cat.dtos.registry.principle.MotivationPrincipleRequest;
import org.grnet.cat.dtos.registry.principle.PrincipleResponseDto;
import org.grnet.cat.dtos.registry.principle.PrincipleUpdateDto;
import org.grnet.cat.dtos.registry.template.MetricNode;
import org.grnet.cat.dtos.registry.template.MetricTestNode;
import org.grnet.cat.entities.registry.*;
import org.grnet.cat.entities.registry.metric.Metric;
import org.grnet.cat.entities.registry.metric.TypeAlgorithm;
import org.grnet.cat.entities.registry.metric.TypeMetric;
import org.grnet.cat.exceptions.UniqueConstraintViolationException;
import org.grnet.cat.mappers.registry.*;
import org.grnet.cat.mappers.registry.MotivationActorMapper;
import org.grnet.cat.mappers.registry.MotivationMapper;
import org.grnet.cat.mappers.registry.PrincipleCriterionMapper;
import org.grnet.cat.mappers.registry.PrincipleMapper;
import org.grnet.cat.mappers.registry.metric.MetricMapper;
import org.grnet.cat.repositories.registry.*;
import org.grnet.cat.repositories.registry.metric.MetricRepository;
import org.grnet.cat.services.registry.metric.MetricService;
import org.grnet.cat.utils.TestParamsTransformer;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class MotivationService {

    @Inject
    MotivationActorRepository motivationActorRepository;

    @Inject
    RegistryActorRepository registryActorRepository;

    @Inject
    PrincipleRepository principleRepository;

    @Inject
    MotivationRepository motivationRepository;

    @Inject
    MotivationTypeRepository motivationTypeRepository;

    @Inject
    MotivationPrincipleRepository motivationPrincipleRepository;

    @Inject
    RelationsService relationsService;

    @Inject
    PrincipleCriterionRepository principleCriterionRepository;

    @Inject
    CriterionActorRepository criterionActorRepository;

    @Inject
    MetricRepository metricRepository;

    @Inject
    MetricDefinitionRepository metricDefinitionRepository;

    @Inject
    MetricTestRepository metricTestRepository;

    @Inject
    MetricService metricService;

    /**
     * Creates a new Motivation.
     *
     * @param request The Motivation to be created.
     * @param userId  The user who requests to create the Motivation.
     * @return The created Motivation.
     */
    @Transactional
    public MotivationResponse createMotivation(MotivationRequest request, String userId) {

        var motivation = MotivationMapper.INSTANCE.dtoToMotivation(request);

        motivation.setPopulatedBy(userId);
        motivation.setMotivationType(Panache.getEntityManager().getReference(MotivationType.class, request.motivationTypeId));
        motivation.setPublished(Boolean.FALSE);
        motivationRepository.persist(motivation);

        if (request.basedOn != null && !request.basedOn.isEmpty()) {
            relationsService.copyRelationsToNewMotivation(motivation.getId(), request.basedOn);
        }

        return MotivationMapper.INSTANCE.motivationToDto(motivation);
    }

    /**
     * Assign a new Actor to motivation.
     *
     * @param motivationId           The Motivation to assign actors.
     * @param motivationActorRequest The MotivationActorRequest to be added.
     * @param userId                 The user who requests to add actors to  the Motivation.
     * @return The added relation.
     */
    @Transactional
    public List<String> assignActors(String motivationId, Set<MotivationActorRequest> motivationActorRequest, String userId) {

        var resultMessages = new ArrayList<String>();

        var motivation = motivationRepository.findById(motivationId);
        motivationActorRequest.stream().iterator().forEachRemaining(req -> {

            if (!motivationActorRepository.existsByMotivationAndActorAndVersion(motivationId, req.actorId, 1)) {

                var actor = registryActorRepository.findById(req.actorId);

                if (StringUtils.isEmpty(actor.getLodMTV())) {

                    actor.setLodMTV(motivationId);
                }

                motivation.addActor(Panache.getEntityManager().getReference(RegistryActor.class, req.actorId), Panache.getEntityManager().getReference(Relation.class, req.relation), motivation.getId(), 1, userId, Timestamp.from(Instant.now()));
                resultMessages.add("Actor with id :: " + req.actorId + " successfully added to motivation.");
            } else {
                resultMessages.add("Actor with id :: " + req.actorId + " already exists to motivation.");
            }
        });
        return resultMessages;
    }

    @Transactional
    public List<String> deleteActorFromMotivation(String motivationId, String actorId) {

        var resultMessages = new ArrayList<String>();

        if (motivationActorRepository.existsByMotivationAndActorAndVersion(motivationId, actorId, 1)) {

            motivationActorRepository.deleteByMotivationAndActorAndVersion(motivationId, actorId, 1);
            resultMessages.add("Actor with ID: " + actorId + " has been removed from motivation: " + motivationId + ".");

        } else {
            resultMessages.add("Actor with ID: " + actorId + " does not exist in motivation: " + motivationId + ".");
        }

        if (criterionActorRepository.existsByMotivationAndActor(motivationId, actorId, 1)) {

            criterionActorRepository.deleteByActorId(motivationId, actorId);
            resultMessages.add("Actor with ID: " + actorId + " has been removed from all criterion relations.");

        } else {
            resultMessages.add("Actor with ID: " + actorId + " does not exist in any criterion relations.");
        }

        return resultMessages;
    }

    /**
     * Assign multiple principles to a single motivation.
     *
     * @param motivationId The Motivation to assign Principles.
     * @param request      The MotivationPrincipleRequest to be added.
     * @param userId       The user who requests to assign Principles to the Motivation.
     * @return The added relation.
     */
    @Transactional
    public List<String> assignPrinciples(String motivationId, Set<MotivationPrincipleRequest> request, String userId) {

        var resultMessages = new ArrayList<String>();

        var motivation = motivationRepository.findById(motivationId);
        request.stream().iterator().forEachRemaining(req -> {

            if (!motivationPrincipleRepository.existsByMotivationAndPrincipleAndVersion(motivationId, req.principleId, 1)) {

                var principle = principleRepository.findById(req.principleId);

                if (StringUtils.isEmpty(principle.getLodMTV())) {

                    principle.setLodMTV(motivationId);
                }

                motivation.addPrinciple(Panache.getEntityManager().getReference(Principle.class, req.principleId), req.annotationText, req.annotationUrl, Panache.getEntityManager().getReference(Relation.class, req.relation), motivation.getId(), 1, userId, Timestamp.from(Instant.now()));
                resultMessages.add("Principle with id :: " + req.principleId + " successfully added to motivation.");
            } else {
                resultMessages.add("Principle with id :: " + req.principleId + " already exists to motivation.");
            }
        });
        return resultMessages;
    }

    /**
     * Retrieves a specific Motivation.
     *
     * @param id The ID of the Motivation to retrieve.
     * @return The corresponding Motivation.
     */
    public MotivationResponse getMotivationById(String id) {

        var motivation = MotivationMapper.INSTANCE.motivationToDto(motivationRepository.fetchById(id));

        if (motivation.actors != null) {

            motivation.actors.forEach(actorjunction -> {

                var criterionIds = criterionActorRepository.getCriterionIdsByMotivationAndActor(id, actorjunction.id);
                var count = principleCriterionRepository.countPrincipleCriterionByMotivationAndCriterionIds(id, criterionIds);

                actorjunction.setExistsPrincipleCriterion(count > 0);
                actorjunction.setPrincipleCriterionCount((int) count);
            });
        }

        return motivation;
    }


    /**
     * Retrieves a page of Motivations.
     *
     * @param actor   the actor text to search on motivation actor label
     * @param search  the search text to search on mtv or label of a motivation
     * @param sort    the field to sort on the results
     * @param order   the order to retrieve the results, ASC or DESC
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of Motivations to include in a page.
     * @param uriInfo The Uri Info.
     * @return A list of MotivationResponse objects representing the submitted Motivations in the requested page.
     */
    public PageResource<MotivationResponse> getMotivationsByPage(String actor, String search, String status, String sort, String order, int page, int size, UriInfo uriInfo) {

        var motivations = motivationRepository.fetchMotivationsByPage(actor, search, status, sort, order, page, size);
        return new PageResource<>(motivations, MotivationMapper.INSTANCE.motivationsToDto(motivations.list()), uriInfo);
    }

    /**
     * This method updates a Motivation.
     *
     * @param id      The ID of the Motivation to update.
     * @param request The Motivation attributes to be updated.
     * @param userID  The ID of the user who requests to update the Motivation.
     * @return The updated Motivation.
     */
    @Transactional
    public MotivationResponse update(String id, UpdateMotivationRequest request, String userID) {

        var motivation = motivationRepository.findById(id);
        motivation.setPopulatedBy(userID);

        MotivationMapper.INSTANCE.updateMotivationFromDto(request, motivation);

        if (!Objects.isNull(request.motivationTypeId)) {

            motivationTypeRepository.findByIdOptional(request.motivationTypeId).orElseThrow(() -> new NotFoundException("There is no Motivation Type with the following id: " + request.motivationTypeId));
            motivation.setMotivationType(Panache.getEntityManager().getReference(MotivationType.class, request.motivationTypeId));
        }

        return MotivationMapper.INSTANCE.motivationToDto(motivation);
    }

    /**
     * Retrieves a page of the Actors of a Motivations.
     *
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of the Actors of the Motivation to include in a page.
     * @param uriInfo The Uri Info.
     * @return A list of MotivationActorJunctionResponse objects representing the submitted Motivations in the requested page.
     */
    public PageResource<MotivationActorResponse> getActorsByMotivationAndPage(String motivationId, int page, int size, UriInfo uriInfo) {

        var motivationActors = motivationActorRepository.fetchActorsByMotivationAndPage(motivationId, page, size);

        return new PageResource<>(motivationActors, MotivationActorMapper.INSTANCE.fullMotivationActorToDtos(motivationActors.list()), uriInfo);
    }

    /**
     * Creates a new relationship between principle, criterion, and motivation.
     *
     * @param motivationId the ID of the motivation.
     * @param request      the list of relationships containing principle and criterion IDs.
     * @param userId       the ID of the user performing the action.
     */
    @Transactional
    public List<String> createNewPrinciplesCriteriaRelationship(String motivationId, Set<PrincipleCriterionRequest> request, String userId) {

        var resultMessages = new ArrayList<String>();

        request.stream().iterator().forEachRemaining(req -> {

            if (!principleCriterionRepository.existsByMotivationAndPrincipleAndCriterionAndVersion(motivationId, req.principleId, req.criterionId, 1)) {

                var priCri = new PrincipleCriterionJunction(Panache.getEntityManager().getReference(Motivation.class, motivationId),
                        Panache.getEntityManager().getReference(Principle.class, req.principleId),
                        Panache.getEntityManager().getReference(Criterion.class, req.criterionId),
                        req.annotationText,
                        req.annotationUrl,
                        Panache.getEntityManager().getReference(Relation.class, req.relation),
                        motivationId,
                        1);

                priCri.setPopulatedBy(userId);
                priCri.setLastTouch(Timestamp.from(Instant.now()));

                principleCriterionRepository.persist(priCri);
                resultMessages.add("principle-criterion with ids :: " + req.principleId + " - " + req.criterionId + " successfully added to Motivation.");
            } else {
                resultMessages.add("principle-criterion with ids :: " + req.principleId + " - " + req.criterionId + " already exists to Motivation.");
            }
        });

        return resultMessages;
    }

    /**
     * Updates an existing relationship between principle, criterion, and motivation.
     *
     * @param motivationId the ID of the motivation.
     * @param request      the list of relationships containing principle and criterion IDs.
     * @param userId       the ID of the user performing the action.
     */
    @Transactional
    public List<String> updatePrinciplesCriteriaRelationship(String motivationId, Set<PrincipleCriterionRequest> request, String userId) {

        var resultMessages = new ArrayList<String>();

        removePrincipleCriterionRelationship(motivationId, request, resultMessages);

        request.stream().iterator().forEachRemaining(req -> {

            var junction = principleCriterionRepository.findByMotivationAndPrincipleAndCriterionAndVersion(motivationId, req.principleId, req.criterionId, 1);

            if (junction.isPresent()) {

                var existingJunction = junction.get();
                existingJunction.setLastTouch(Timestamp.from(Instant.now()));
                existingJunction.setPopulatedBy(userId);
                existingJunction.setAnnotationURL(req.annotationUrl);
                existingJunction.setAnnotationText(req.annotationText);
                existingJunction.setRelation(Panache.getEntityManager().getReference(Relation.class, req.relation));

                resultMessages.add("principle-criterion with ids :: " + req.principleId + " - " + req.criterionId + " successfully updated.");
            } else {

                var priCri = new PrincipleCriterionJunction(Panache.getEntityManager().getReference(Motivation.class, motivationId),
                        Panache.getEntityManager().getReference(Principle.class, req.principleId),
                        Panache.getEntityManager().getReference(Criterion.class, req.criterionId),
                        req.annotationText,
                        req.annotationUrl,
                        Panache.getEntityManager().getReference(Relation.class, req.relation),
                        motivationId,
                        1);

                priCri.setPopulatedBy(userId);
                priCri.setLastTouch(Timestamp.from(Instant.now()));

                principleCriterionRepository.persist(priCri);
                resultMessages.add("principle-criterion with ids :: " + req.principleId + " - " + req.criterionId + " successfully added to Motivation.");
            }
        });

        return resultMessages;
    }

    public PageResource<PrincipleCriterionResponseDto> getPrinciplesCriteriaRelationship(String motivationId, int page, int size, UriInfo uriInfo) {

        var principleCriteria = principleCriterionRepository.fetchPrincipleCriterionByMotivation(motivationId, page, size);

        var principleCriteriaResponse = PrincipleCriterionMapper.INSTANCE.principleCriterionToResponseDtos(principleCriteria.list());

        return new PageResource<>(principleCriteria, principleCriteriaResponse, uriInfo);
    }

    private void removePrincipleCriterionRelationship(String motivationId, Set<PrincipleCriterionRequest> request, List<String> resultMessages) {

        var pcList = principleCriterionRepository.fetchPrincipleCriterionByMotivation(motivationId);

        pcList
                .iterator()
                .forEachRemaining(pc -> {

                    var temp = new PrincipleCriterionRequest();
                    temp.criterionId = pc.getId().getCriterionId();
                    temp.principleId = pc.getId().getPrincipleId();

                    if (!request.contains(temp)) {

                        principleCriterionRepository.delete(pc);
                        resultMessages.add("principle-criterion with ids :: " + temp.principleId + " - " + temp.criterionId + " removed from Motivation.");
                    }
                });
    }

    /**
     * Publish a  Motivation.
     *
     * @param id The id of the  Motivation  to be published.
     */
    @Transactional
    public void publish(String id) {

        var motivation = motivationRepository.fetchById(id);
        motivation.setPublished(Boolean.TRUE);
    }

    /**
     * Unpublish a  Motivation.
     *
     * @param id The id of the  Motivation  to be unpublished.
     */
    //@CheckPublishedRelation(permittedStatus = true, type = PublishEntityType.ACTOR)
    // Only proceed if `published` is false
    @Transactional
    public void unpublish(String id) {

        var motivation = motivationRepository.fetchById(id);
        motivation.setPublished(Boolean.FALSE);
    }

    /**
     * Publish a  Motivation Actor.
     *
     * @param id      The id of the  Motivation .
     * @param actorId The id of the  Actor .
     */
    //@CheckPublishedRelation(permittedStatus = false,type = PublishEntityType.ACTOR)  // Only proceed if `published` is false
    @Transactional
    public void publishActor(String id, String actorId) {

        Optional<MotivationActorJunction> motivationActorJunctionOpt = motivationActorRepository.fetchByMotivationAndActorAndVersion(id, actorId, 1);
        if (motivationActorJunctionOpt.isPresent()) {
            MotivationActorJunction motivationActorJunction = motivationActorJunctionOpt.get();

            if (motivationActorJunction.getPublished() == Boolean.TRUE) {
                throw new ForbiddenException("Action not permitted, motivation-actor relation is already published");
            }
            motivationActorJunction.setPublished(Boolean.TRUE);
        }
    }

    /**
     * Unpublish a  Motivation Actor.
     *
     * @param id      The id of the  Motivation .
     * @param actorId The id of the  Actor .
     */
    //@CheckPublishedRelation(permittedStatus = false,type = PublishEntityType.ACTOR)
    @Transactional
    public void unpublishActor(String id, String actorId) {

        Optional<MotivationActorJunction> motivationActorJunctionOpt = motivationActorRepository.fetchByMotivationAndActorAndVersion(id, actorId, 1);
        if (motivationActorJunctionOpt.isPresent()) {
            MotivationActorJunction motivationActorJunction = motivationActorJunctionOpt.get();

            if (motivationActorJunction.getPublished() == Boolean.FALSE) {
                throw new ForbiddenException("Action not permitted, motivation-actor relation is already unpublished");
            }
            motivationActorJunction.setPublished(Boolean.FALSE);
        }

    }

    @Transactional
    public InformativeResponse createPrincipleForMotivation(String id, MotivationPrincipleExtendedRequestDto request, String userID) {

        var response = new InformativeResponse();

        if (!principleRepository.notUnique("pri", request.principleRequestDto.pri.toUpperCase())) {

            var principle = PrincipleMapper.INSTANCE.principleToEntity(request.principleRequestDto);

            principle.setLodMTV(id);
            principle.setPopulatedBy(userID);
            principleRepository.persist(principle);

            var motivationPrincipleJunction = new MotivationPrincipleJunction(
                    Panache.getEntityManager().getReference(Motivation.class, id),
                    Panache.getEntityManager().getReference(Principle.class, principle.getId()),
                    request.annotationText,
                    request.annotationUrl,
                    Panache.getEntityManager().getReference(Relation.class, request.relation),
                    id,
                    1,
                    userID,
                    Timestamp.from(Instant.now())
            );

            motivationPrincipleRepository.persist(motivationPrincipleJunction);

            response.code = 200;
            response.message = "Principle successfully created and linked to the specified motivation.";
        } else {
            response.code = 409;
            response.message = "A principle with the identifier '" + request.principleRequestDto.pri.toUpperCase() + "' already exists.";
        }

        return response;
    }

    @Transactional
    public InformativeResponse createMetricDefinitionForMotivation(String id, MotivationMetricExtendedRequest request, String userId) {

        var response = new InformativeResponse();

        if (!metricRepository.notUnique("MTR", request.MTR.toUpperCase())) {

            var metricRequest = new MetricRequestDto();
            metricRequest.MTR = request.MTR;
            metricRequest.urlMetric = request.urlMetric;
            metricRequest.typeMetricId = request.typeMetricId;
            metricRequest.typeAlgorithmId = request.typeAlgorithmId;
            metricRequest.labelMetric = request.labelMetric;
            metricRequest.descrMetric = request.descrMetric;

            var metric = MetricMapper.INSTANCE.metricToEntity(metricRequest);

            metric.setLodMTV(id);
            metric.setPopulatedBy(userId);
            metric.setTypeAlgorithm(Panache.getEntityManager().getReference(TypeAlgorithm.class, metricRequest.typeAlgorithmId));
            metric.setTypeMetric(Panache.getEntityManager().getReference(TypeMetric.class, metricRequest.typeMetricId));
            metric.setPopulatedBy(userId);
            metric.setVersion(1);
            metricRepository.persist(metric);

            metric.setLodMTRV(metric.getId());


            var metricDefinitionJunction = new MetricDefinitionJunction(
                    Panache.getEntityManager().getReference(Motivation.class, id),
                    Panache.getEntityManager().getReference(Metric.class, metric.getId()),
                    Panache.getEntityManager().getReference(TypeBenchmark.class, request.typeBenchmarkId),
                    request.valueBenchmark,
                    id,
                    1,
                    (Timestamp.from(Instant.now())).toLocalDateTime().toLocalDate(),
                    userId,
                    Timestamp.from(Instant.now())
            );

            metricDefinitionRepository.persist(metricDefinitionJunction);

            response.code = 200;
            response.message = "A metric and a Metric Definition successfully created and linked to the specified motivation.";
        } else {
            response.code = 409;
            response.message = "A metric with the identifier '" + request.MTR.toUpperCase() + "' already exists.";
        }

        return response;
    }

    @Transactional
    public InformativeResponse createMetricDefinitionVersionForMotivation(String motivationId, String metricId, MotivationMetricExtendedRequest request, String userId) {

        var response = new InformativeResponse();

        var metricParent = metricRepository.findById(metricId);

        if (Objects.equals(metricParent.getMTR(), request.MTR)) {

            var metricRequest = new MetricRequestDto();
            metricRequest.MTR = request.MTR;
            metricRequest.urlMetric = request.urlMetric;
            metricRequest.typeMetricId = request.typeMetricId;
            metricRequest.typeAlgorithmId = request.typeAlgorithmId;
            metricRequest.labelMetric = request.labelMetric;
            metricRequest.descrMetric = request.descrMetric;

            var metric = MetricMapper.INSTANCE.metricToEntity(metricRequest);

            metric.setLodMTV(motivationId);
            metric.setPopulatedBy(userId);
            metric.setTypeAlgorithm(Panache.getEntityManager().getReference(TypeAlgorithm.class, metricRequest.typeAlgorithmId));
            metric.setTypeMetric(Panache.getEntityManager().getReference(TypeMetric.class, metricRequest.typeMetricId));
            metric.setPopulatedBy(userId);
            metric.setLodMTRV(metricParent.getLodMTRV());

            var parentMetricVersion = metricRepository.countVersion(metricId);
            metric.setVersion((int) (parentMetricVersion + 1));


            metricRepository.persist(metric);

            var metricDefinitionJunction = new MetricDefinitionJunction(
                    Panache.getEntityManager().getReference(Motivation.class, motivationId),
                    Panache.getEntityManager().getReference(Metric.class, metric.getId()),
                    Panache.getEntityManager().getReference(TypeBenchmark.class, request.typeBenchmarkId),
                    request.valueBenchmark,
                    motivationId,
                    1,
                    (Timestamp.from(Instant.now())).toLocalDateTime().toLocalDate(),
                    userId,
                    Timestamp.from(Instant.now())
            );

            metricDefinitionRepository.persist(metricDefinitionJunction);

            var metricDefinition = metricDefinitionRepository.fetchMetricDefinitionByMetricId(metricParent.getLodMTRV());
            metricDefinitionJunction.setMetricDefinition(metricDefinition.getMetricDefinition());

            response.code = 200;
            response.message = "A version of a metric and a Metric Definition successfully created and linked to the specified motivation.";
        } else {
            response.code = 409;
            response.message = "A metric with the identifier '" + request.MTR.toUpperCase() + "' already exists.";
        }

        return response;
    }

    @Transactional
    public InformativeResponse updateMetricDefinitionForMotivation(String id, String metricId, MotivationMetricUpdateRequest request, String userId) {

        var response = new InformativeResponse();

        var updateMetric = new MetricUpdateDto();

        var metricDefinition = metricDefinitionRepository.fetchMetricDefinitionByMetricId(metricId);

        updateMetric.MTR = request.MTR;
        updateMetric.labelMetric = request.labelMetric;
        updateMetric.descrMetric = request.descrMetric;
        updateMetric.typeMetricId = request.typeMetricId;
        updateMetric.urlMetric = request.urlMetric;
        updateMetric.typeAlgorithmId = request.typeAlgorithmId;

        metricService.updateMetric(metricId, userId, updateMetric);

        if(StringUtils.isNotEmpty(request.typeBenchmarkId)){

            metricDefinition.setTypeBenchmark(Panache.getEntityManager().getReference(TypeBenchmark.class, request.typeBenchmarkId));

        }

        if(StringUtils.isNotEmpty(request.valueBenchmark)){

            metricDefinition.setValueBenchmark(request.valueBenchmark);
        }

        response.code = 200;
        response.message = "A metric and a Metric Definition successfully updated.";

        return response;
    }


    @Transactional
    public List<String> updateMetricDefinitionRelation(String motivationId, Set<MetricDefinitionRequest> request, String userId) {

        var resultMessages = new ArrayList<String>();

        removeMetricDefinitionRelationship(motivationId, request, resultMessages);

        request.stream().iterator().forEachRemaining(req -> {

            var junction = principleCriterionRepository.findByMotivationAndPrincipleAndCriterionAndVersion(motivationId, req.metricId, req.typeBenchmarkId, 1);

            if (junction.isPresent()) {

                var existingJunction = junction.get();
                existingJunction.setLastTouch(Timestamp.from(Instant.now()));
                existingJunction.setPopulatedBy(userId);
                resultMessages.add("metric-definition with ids :: " + req.metricId + " - " + req.typeBenchmarkId + " successfully updated.");
            } else {

                var metDef = new MetricDefinitionJunction(Panache.getEntityManager().getReference(Motivation.class, motivationId),
                        Panache.getEntityManager().getReference(Metric.class, req.metricId),
                        Panache.getEntityManager().getReference(TypeBenchmark.class, req.typeBenchmarkId),
                        req.valueBenchmark,
                        motivationId,
                        1,
                        (Timestamp.from(Instant.now())).toLocalDateTime().toLocalDate(),
                        userId,
                        Timestamp.from(Instant.now())
                );

                metricDefinitionRepository.persist(metDef);
                resultMessages.add("metric-definition with ids :: " + req.metricId + " - " + req.typeBenchmarkId + " successfully added to Motivation.");
            }
        });
        return resultMessages;
    }

    public List<String> deletePrincipleFromMotivation(String motivationId, String principleId) {
        var resultMessages = new ArrayList<String>();

        // Start the deletion process asynchronously, within the transaction scope
        CompletableFuture<Void> deletionFuture = CompletableFuture.runAsync(() -> {
            // Ensure these methods are invoked within the transaction scope
            deletePrincipleFromMotivationInTransaction(motivationId, principleId, resultMessages);
        });

        // Wait for the deletion to finish before doing further checks
        deletionFuture.join(); // This ensures the deletions are complete before proceeding

        // Perform the checks after deletion has been completed
        if (principleCriterionRepository.isUsedWithCriterionInOtherMotivation(motivationId, principleId, 1)) {

            resultMessages.add(String.format("Principle with id: %s can not be deleted, as it is used by criterion existing in another motivation.", principleId));
            throw new ForbiddenException(String.join("\n", resultMessages));

        }

        if (motivationPrincipleRepository.isUsedInOtherMotivation(motivationId, principleId, 1)) {
            resultMessages.add(String.format("Principle with id: %s can not be deleted, as it is used in another motivation. Messages: %s", principleId));
            throw new ForbiddenException(String.join("\n", resultMessages));
        }

        // Proceed with final deletion if needed
        principleRepository.deleteById(principleId);

        return resultMessages;
    }

    public PageResource<MetricDefinitionExtendedResponse> getMetricDefinitionRelation(String motivationId, int page, int size, UriInfo uriInfo) {

        var metricDefinition = metricDefinitionRepository.fetchMetricDefinitionByMotivation(motivationId, page, size);

        var metricDefinitionResponse = MetricDefinitionMapper.INSTANCE.metricDefinitionToExtendedResponses(metricDefinition.list());

        return new PageResource<>(metricDefinition, metricDefinitionResponse, uriInfo);
    }

    public MetricDefinitionExtendedResponse getMetricDefinitionRelation(String motivationId, String metricId) {

        var metricDefinition = metricDefinitionRepository.fetchMetricDefinitionByMotivationAndMetricId(motivationId, metricId);

        return MetricDefinitionMapper.INSTANCE.metricDefinitionToExtendedResponse(metricDefinition);
    }

    /**
     * Creates a new relationship between metric, test, and motivation.
     *
     * @param motivationId the ID of the motivation.
     * @param request      the list of relationships containing metric and test IDs.
     * @param userId       the ID of the user performing the action.
     */
    @Transactional
    public List<String> createMetricTestRelation(String motivationId, String metricId, Set<MetricTestRequest> request, String userId) {

        var resultMessages = new ArrayList<String>();
        request.stream().iterator().forEachRemaining(req -> {

            if (!metricTestRepository.existsByMotivationAndMetricAndTestAndVersion(motivationId, metricId, req.testId, 1)) {

                var metricTest = new MetricTestJunction(Panache.getEntityManager().getReference(Motivation.class, motivationId),
                        Panache.getEntityManager().getReference(Metric.class, metricId),
                        Panache.getEntityManager().getReference(Test.class, req.testId),
                        Panache.getEntityManager().getReference(Relation.class, req.relation),
                        motivationId,
                        1);

                    metricTest.setPopulatedBy(userId);
                    metricTest.setLastTouch(Timestamp.from(Instant.now()));

                    metricTestRepository.persist(metricTest);
                    resultMessages.add("metric-test with ids :: " + metricId + " - " + req.testId + " successfully added to Motivation.");
            } else {
                resultMessages.add("metric-test with ids :: " + metricId + " - " + req.testId + " already exists to Motivation.");
            }
        });

        return resultMessages;
    }

    public DetailedMetricDto getMetricTestRelation(String motivationId, String metricId) {

        var metricTest = metricTestRepository.fetchMotivationMetricTests(motivationId, metricId);
        Map<String, MetricNode> metricNodeMap = new HashMap<>();
        if (metricTest.isEmpty()) {
            throw new NotFoundException(String.format("Not found relation of metric-tests for metric: %s and motivation: %s", metricId, motivationId));
        }
        var metric = metricTest.get(0);
        var metricNode = metricNodeMap.computeIfAbsent(
                metric.getMTR(),
                key -> new MetricNode(
                        metric.getMTR(),
                        metric.getLabelMetric().trim(),
                        metric.getLabelBenchmarkType(),
                        Double.parseDouble(metric.getValueBenchmark()),
                        metric.getLabelAlgorithmType(),
                        metric.getLabelTypeMetric()
                )
        );

        metricTest.stream().forEach(mt -> {
                    var transformedParams = TestParamsTransformer.transformTestParams(mt.getTestParams());

                    var testNode = new MetricTestNode(
                            mt.getLodTES(),
                            mt.getTES(),
                            mt.getLabelTest().trim(),
                            mt.getDescTest().trim(),
                            mt.getLabelTestMethod(),
                            mt.getTestQuestion(),
                            transformedParams,
                            mt.getToolTip()
                    );
                    metricNode.addChild(testNode);
                }
        );

        var detailed = new DetailedMetricDto();
        detailed.metric = metricNode;
        return detailed;
    }

    @Transactional
    public void updateTestMetricRelation(String motivationId, String metricId, MetricTestRequest req, String userId) {

        var metricTest = new MetricTestJunction(Panache.getEntityManager().getReference(Motivation.class, motivationId),
                Panache.getEntityManager().getReference(Metric.class, metricId),
                Panache.getEntityManager().getReference(Test.class, req.testId),
                Panache.getEntityManager().getReference(Relation.class, req.relation),
                motivationId,
                1);

        metricTest.setPopulatedBy(userId);
        metricTest.setLastTouch(Timestamp.from(Instant.now()));

        metricTestRepository.persist(metricTest);

    }

    /**
     * Updates an existing relationship between metric, test, and motivation.
     *
     * @param motivationId the ID of the motivation.
     * @param request      the list of relationships containing metric and test IDs.
     * @param userId       the ID of the user performing the action.
     */
    @Transactional
    public List<String> updateTestMetricRelation(String motivationId, String metricId, List<MetricTestRequest> request, String userId) {

        var resultMessages = new ArrayList<String>();

        removeMetricTestRelation(motivationId, metricId, request, resultMessages);

        request.stream().iterator().forEachRemaining(req -> {

            var junction = metricTestRepository.findByMotivationAndMetricAndTestAndVersion(motivationId, metricId, req.testId, 1);

            if (junction.isPresent()) {

                var existingJunction = junction.get();
                existingJunction.setLastTouch(Timestamp.from(Instant.now()));
                existingJunction.setPopulatedBy(userId);
                existingJunction.setRelation(Panache.getEntityManager().getReference(Relation.class, req.relation));

                resultMessages.add("metric-test with ids :: " + metricId + " - " + req.testId + " successfully updated.");
            } else {
                updateTestMetricRelation(motivationId, metricId, req, userId);
                resultMessages.add("metric-test with ids :: " + metricId + " - " + req.testId + " successfully added to Motivation.");
            }
        });
        return resultMessages;
    }

    private void removeMetricDefinitionRelationship(String
                                                            motivationId, Set<MetricDefinitionRequest> request, List<String> resultMessages) {

        var mdList = metricDefinitionRepository.fetchMetricDefinitionByMotivation(motivationId);

        mdList
                .iterator()
                .forEachRemaining(md -> {

                    var temp = new MetricDefinitionRequest();
                    temp.metricId = md.getId().getMetricId();
                    temp.typeBenchmarkId = md.getId().getTypeBenchmarkId();

                    if (!request.contains(temp)) {

                        metricDefinitionRepository.delete(md);
                        resultMessages.add("principle-criterion with ids :: " + temp.metricId + " - " + temp.typeBenchmarkId + " removed from Motivation.");
                    }
                });
    }


    // This method is transactional and encapsulates the delete logic
    @Transactional
    public void deletePrincipleFromMotivationInTransaction(String motivationId, String
            principleId, List<String> resultMessages) {
        motivationPrincipleRepository.deleteByPrincipleId(motivationId, principleId, 1);
        resultMessages.add(String.format("Principle with ID: %s  has been removed from motivation: %s.", principleId, motivationId));

        principleCriterionRepository.deleteByMotivationPrincipleId(motivationId, principleId);
        resultMessages.add(String.format("Principle with ID: %s  has been removed from all criterion relations of motivation %s.", principleId, motivationId));
    }

    /**
     * Deletes an existing relationship between principle, criterion, and motivation.
     *
     * @param motivationId the ID of the motivation.
     * @param request      the list of relationships containing principle and criterion IDs.
     * @param userId       the ID of the user performing the action.
     */
    @Transactional
    public List<String> deletePrinciplesCriteriaRelationship(String motivationId, DeletePrincipleCriterionRequest
            request, String userId) {

        var resultMessages = new ArrayList<String>();

        try {
            principleCriterionRepository.deletePrincipleCriterionMotivationRelation(motivationId, request.principleId, request.criterionId);
            resultMessages.add(String.format("Successfully deleted  relationship for Motivation ID: %s.", motivationId));

        } catch (Exception e) {
            resultMessages.add("Failed to delete relationships due to an unexpected error: " + e.getMessage());
            // Log the
        }
        return resultMessages;
    }

    @Transactional
    public PrincipleResponseDto updatePrincipleFromMotivation(String motivationId, String
            principleId, PrincipleUpdateDto requestDto) {

        if (principleCriterionRepository.isUsedWithCriterionInOtherMotivation(motivationId, principleId, 1)) {
            throw new ForbiddenException(String.format("Principle with id: %s can not be updated, as it is used by criterion existing in another motivation", principleId));
        }

        if (motivationPrincipleRepository.isUsedInOtherMotivation(motivationId, principleId, 1)) {
            throw new ForbiddenException(String.format("Principle with id: %s can not be updated, as it is used in another motivation", principleId));
        }

        var principle = principleRepository.findById(principleId);
        var currentPri = principle.getPri();
        var updatePri = StringUtils.isNotEmpty(requestDto.pri) ? requestDto.pri.toUpperCase() : currentPri;

        if (StringUtils.isNotEmpty(updatePri) && !updatePri.equals(currentPri)) {

            if (principleRepository.notUnique("pri", updatePri)) {
                throw new UniqueConstraintViolationException("pri", updatePri);
            }
        }

        PrincipleMapper.INSTANCE.updatePrinciple(requestDto, principle);
        return PrincipleMapper.INSTANCE.principleToDto(principle);

    }

    private void removeMetricTestRelation(String motivationId, String
            metricId, List<MetricTestRequest> request, List<String> resultMessages) {

        var mtList = metricTestRepository.fetchMetricTestByMotivationAndMetric(motivationId, metricId);

        mtList.iterator().forEachRemaining(mt -> {

            if (mt.getId() != null && mt.getId().getTestId() != null) {
                var temp = new MetricTestRequest();
                temp.testId = mt.getId().getTestId();
                temp.relation = mt.getRelation().getId();

                if (!request.contains(temp)) {
                    // Delete the junction record
                    metricTestRepository.delete(mt);
                    resultMessages.add("metric-test with ids :: " + metricId + " - " + temp.testId + " removed from Motivation.");
                }
            } else {
                // Optionally handle the case where the ID or test-related information is missing
                resultMessages.add("Failed to remove metric-test with ids :: " + metricId + " due to missing information.");
            }
        });
    }

}