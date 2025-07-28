package org.grnet.cat.services.registry;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.codelist.RegistryActorResponse;
import org.grnet.cat.dtos.registry.criterion.CriterionActorRequest;
import org.grnet.cat.dtos.registry.criterion.CriterionActorResponse;
import org.grnet.cat.dtos.registry.metric.MetricRequestDto;
import org.grnet.cat.dtos.registry.motivation.MultipleCriterionMetricRequest;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.registry.*;
import org.grnet.cat.mappers.registry.CriterionActorMapper;
import org.grnet.cat.mappers.registry.RegistryActorMapper;
import org.grnet.cat.repositories.registry.*;
import org.grnet.cat.repositories.registry.metric.MetricRepository;
import org.mapstruct.Mapping;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class RegistryActorService {
    @Inject
    RegistryActorRepository registryActorRepository;

    @Inject
    MotivationRepository motivationRepository;
    @Inject
    ImperativeRepository imperativeRepository;
    @Inject
    PrincipleCriterionRepository principleCriterionRepository;
    @Inject
    CriterionActorRepository criterionActorRepository;

    @Inject
    MotivationActorRepository motivationActorRepository;

    @Inject
    CriterionMetricService criterionMetricService;

    @Inject
    MotivationService motivationService;

    @Inject
    CriterionMetricRepository criterionMetricRepository;

    @Inject
    MetricRepository metricRepository;

    /**
     * Retrieves a specific RegistryActor.
     *
     * @param id The ID of the RegistryActor to retrieve.
     * @return The corresponding RegistryActor.
     */
    public RegistryActorResponse getActorById(String id) {

        var actor = registryActorRepository.findById(id);

        return RegistryActorMapper.INSTANCE.actorToDto(actor);
    }

    /**
     * Retrieves a page of RegistryActor.
     *
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of RegistryActor to include in a page.
     * @param uriInfo The Uri Info.
     * @return A list of RegistryActorResponseDto objects representing the submitted RegistryActor list in the requested page.
     */
    public PageResource<RegistryActorResponse> getActorListByPage(int page, int size, UriInfo uriInfo) {

        PageQuery<RegistryActor> actorList = registryActorRepository.fetchActorsByPage(page, size);

        return new PageResource<>(actorList, RegistryActorMapper.INSTANCE.actorToDtos(actorList.list()), uriInfo);
    }


    /**
     * Adds  a new Actor to motivation.
     *
     * @param motivationId          The Motivation
     * @param actorId               The Actor.
     * @param criterionActorRequest The CriterionActorRequest to be added.
     * @param userId                The user who requests to add criteria to  the Actor.
     * @return The added relation.
     */
   // @CheckPublishedRelation(permittedStatus = false, type = PublishEntityType.ACTOR)
    // Only proceed if `published` is false
    @Transactional
    public List<String> addCriteria(String motivationId, String actorId, Set<CriterionActorRequest> criterionActorRequest, String userId) {

        var resultMessages = new ArrayList<String>();

        if (!motivationActorRepository.existsByMotivationAndActorAndVersion(motivationId, actorId, 1)) {
            throw new NotFoundException("relation between motivation with id: " + motivationId + " and actor with id: " + actorId + " in version : " + 1 + " does not exist");
        }
        Optional<MotivationActorJunction> motivationActorJunctionOpt =
                motivationActorRepository.fetchByMotivationAndActorAndVersion(motivationId, actorId, 1);

        if (motivationActorJunctionOpt.get().getPublished() == Boolean.TRUE) {
            throw new ForbiddenException("No action is permitted as motivation-actor relation is published");
        }

        Motivation motivation = motivationRepository.findById(motivationId);
        RegistryActor actor = registryActorRepository.findById(actorId);

        criterionActorRequest.stream().iterator().forEachRemaining(req -> {
            var imperative = imperativeRepository.findById(req.imperativeId);
            var principleCriterionJunction = principleCriterionRepository.findCriterion(req.criterionId, motivation.getId());
            if (!principleCriterionJunction.isPresent()) {
                resultMessages.add("criterion with id :: " + req.criterionId + " is not related to principles");
            } else {

                var criterion = principleCriterionJunction.get().getCriterion();
                if (!criterionActorRepository.existsByMotivationAndActorAndCriterion(motivationId, actorId, req.criterionId, 1)) {
                    actor.addCriterion(motivation, criterion, imperative, motivation.getId(), 1, userId, Timestamp.from(Instant.now()));
                    resultMessages.add("criterion with id :: " + criterion.getId() + " successfully added to actor");
                } else {
                    resultMessages.add("criterion with id :: " + criterion.getId() + " already exists to actor");
                }
            }
        });
        return resultMessages;
    }

    /**
     * Retrieves a page of the Criteria of a Motivation Actor.
     *
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of the Criteria of the Motivation Actor to include in a page.
     * @param uriInfo The Uri Info.
     * @return A list of CriteriaActorJunctionResponse objects representing the submitted Criteria in the requested page.
     */
    public PageResource<CriterionActorResponse> getCriteriaByMotivationActorAndPage(String motivationId, String actorId, int page, int size, UriInfo uriInfo) {

        if (!motivationActorRepository.existsByMotivationAndActorAndVersion(motivationId, actorId, 1)) {
            throw new NotFoundException("relation between motivation with id: " + motivationId + " and actor with id: " + actorId + " in version : " + 1 + " does not exist");
        }

        var criteriaActor = criterionActorRepository.fetchCriteriaByMotivationAndActorAndPage(motivationId, actorId, page, size);

        var criAct =  CriterionActorMapper.INSTANCE.toCriterionActorResponseList(criteriaActor.list());

        return new PageResource<>(criteriaActor, criAct, uriInfo);
    }

    /**
     * Adds  a new Actor to motivation.
     *
     * @param motivationId          The Motivation
     * @param actorId               The Actor.
     * @param criterionActorRequest The CriterionActorRequest to be added.
     * @param userId                The user who requests to add criteria to  the Actor.
     * @return The added relation.
     */
    //@CheckPublishedRelation(permittedStatus = false, type = PublishEntityType.ACTOR)
    // Only proceed if `published` is false
    @Transactional
    public List<String> updateCriteria(String motivationId, String actorId, Set<CriterionActorRequest> criterionActorRequest, String userId) {
        var resultMessages = new ArrayList<String>();

        if (!motivationActorRepository.existsByMotivationAndActorAndVersion(motivationId, actorId, 1)) {
            throw new NotFoundException("relation between motivation with id: " + motivationId + " and actor with id: " + actorId + " in version : " + 1 + " does not exist");
        }
        Optional<MotivationActorJunction> motivationActorJunctionOpt =
                motivationActorRepository.fetchByMotivationAndActorAndVersion(motivationId, actorId, 1);

        if (motivationActorJunctionOpt.get().getPublished() == Boolean.TRUE) {
            throw new ForbiddenException("No action is permitted as motivation-actor relation is published");
        }

        var motivation = motivationRepository.findById(motivationId);
        var actor = registryActorRepository.findById(actorId);

        removeCriteria(motivation, actor, criterionActorRequest, resultMessages);

        criterionActorRequest.stream().iterator().forEachRemaining(req -> {
            var imperative = imperativeRepository.findById(req.imperativeId);
            var principleCriterionJunction = principleCriterionRepository.findCriterion(req.criterionId, motivation.getId());

            if (principleCriterionJunction.isEmpty()) {
                resultMessages.add("criterion with id: " + req.criterionId + " is not related to principles");
            } else {

                var criterion = principleCriterionJunction.get().getCriterion();
                var junction = criterionActorRepository.findByMotivationAndActorAndCriterion(motivationId, actorId, req.criterionId, 1);

                if (junction.isPresent()) {

                    var existingJunction = junction.get();

                    if (!existingJunction.getImperative().equals(imperative)) {
                        existingJunction.setImperative(imperative);
                        existingJunction.setLastTouch(Timestamp.from(Instant.now()));
                        existingJunction.setPopulatedBy(userId);
                        resultMessages.add("criterion with id: " + criterion.getId() + " updated with new imperative for actor.");
                    } else {
                        resultMessages.add("criterion with id: " + criterion.getId() + " already exists with the same imperative");
                    }
                } else {
                    actor.addCriterion(motivation, criterion, imperative, motivation.getId(), 1, userId, Timestamp.from(Instant.now()));
                    resultMessages.add("criterion with id: " + criterion.getId() + " successfully added to actor");
                }
            }
        });

        return resultMessages;
    }


    /**
     * Add Criteria to Actor.
     *
     * @param motivationId          The Motivation
     * @param actorId               The Actor.
     * @param criterionActorRequest The CriterionActorRequest to be added.
     * @param userId                The user who requests to add criteria to  the Actor.
     * @return The added relation.
     */
    @Transactional
    public List<String> addCriterionWithAutoMetricToActor(String motivationId, String actorId, Set<CriterionActorRequest> criterionActorRequest, String userId) {

        var resultMessages = new ArrayList<String>();

        if (!motivationActorRepository.existsByMotivationAndActorAndVersion(motivationId, actorId, 1)) {
            throw new NotFoundException("relation between motivation with id: " + motivationId + " and actor with id: " + actorId + " in version : " + 1 + " does not exist");
        }
        Optional<MotivationActorJunction> motivationActorJunctionOpt =
                motivationActorRepository.fetchByMotivationAndActorAndVersion(motivationId, actorId, 1);

        if (motivationActorJunctionOpt.get().getPublished() == Boolean.TRUE) {
            throw new ForbiddenException("No action is permitted as motivation-actor relation is published");
        }

        var motivation = motivationRepository.findById(motivationId);
        var actor = registryActorRepository.findById(actorId);

        criterionActorRequest.stream().iterator().forEachRemaining(req -> {
            var imperative = imperativeRepository.findById(req.imperativeId);
            var principleCriterionJunction = principleCriterionRepository.findCriterion(req.criterionId, motivation.getId());
            if (principleCriterionJunction.isEmpty()) {
                resultMessages.add("criterion with id :: " + req.criterionId + " is not related to principles");
            } else {

                var criterion = principleCriterionJunction.get().getCriterion();
                if (!criterionActorRepository.existsByMotivationAndActorAndCriterion(motivationId, actorId, req.criterionId, 1)) {

                    generateAutoMetricForCriterion(motivationId, criterion.getId(), userId);
                    actor.addCriterion(motivation, criterion, imperative, motivation.getId(), 1, userId, Timestamp.from(Instant.now()));
                    resultMessages.add("criterion with id :: " + criterion.getId() + " successfully added to actor");
                } else {
                    resultMessages.add("criterion with id :: " + criterion.getId() + " already exists to actor");
                }
            }
        });
        return resultMessages;
    }

    /**
     * Adds  a new Actor to motivation.
     *
     * @param motivationId          The Motivation
     * @param actorId               The Actor.
     * @param criterionActorRequest The CriterionActorRequest to be added.
     * @param userId                The user who requests to add criteria to  the Actor.
     * @return The added relation.
     */
    //@CheckPublishedRelation(permittedStatus = false, type = PublishEntityType.ACTOR)
    // Only proceed if `published` is false
    @Transactional
    public List<String> updateCriterionWithAutoMetricToActor(String motivationId, String actorId, Set<CriterionActorRequest> criterionActorRequest, String userId) {
        var resultMessages = new ArrayList<String>();

        if (!motivationActorRepository.existsByMotivationAndActorAndVersion(motivationId, actorId, 1)) {
            throw new NotFoundException("relation between motivation with id: " + motivationId + " and actor with id: " + actorId + " in version : " + 1 + " does not exist");
        }
        Optional<MotivationActorJunction> motivationActorJunctionOpt =
                motivationActorRepository.fetchByMotivationAndActorAndVersion(motivationId, actorId, 1);

        if (motivationActorJunctionOpt.get().getPublished() == Boolean.TRUE) {
            throw new ForbiddenException("No action is permitted as motivation-actor relation is published");
        }

        var motivation = motivationRepository.findById(motivationId);
        var actor = registryActorRepository.findById(actorId);

        removeCriteriaAndAutoMetric(motivation, actor, criterionActorRequest, resultMessages);

        criterionActorRequest.stream().iterator().forEachRemaining(req -> {
            var imperative = imperativeRepository.findById(req.imperativeId);
            var principleCriterionJunction = principleCriterionRepository.findCriterion(req.criterionId, motivation.getId());

            if (principleCriterionJunction.isEmpty()) {
                resultMessages.add("criterion with id: " + req.criterionId + " is not related to principles");
            } else {

                var criterion = principleCriterionJunction.get().getCriterion();
                var junction = criterionActorRepository.findByMotivationAndActorAndCriterion(motivationId, actorId, req.criterionId, 1);

                if (junction.isPresent()) {

                    var existingJunction = junction.get();

                    if (!existingJunction.getImperative().equals(imperative)) {
                        existingJunction.setImperative(imperative);
                        existingJunction.setLastTouch(Timestamp.from(Instant.now()));
                        existingJunction.setPopulatedBy(userId);
                        resultMessages.add("criterion with id: " + criterion.getId() + " updated with new imperative for actor.");
                    } else {
                        resultMessages.add("criterion with id: " + criterion.getId() + " already exists with the same imperative");
                    }
                } else {
                    generateAutoMetricForCriterion(motivationId, criterion.getId(), userId);
                    actor.addCriterion(motivation, criterion, imperative, motivation.getId(), 1, userId, Timestamp.from(Instant.now()));
                    resultMessages.add("criterion with id: " + criterion.getId() + " successfully added to actor");
                }
            }
        });

        return resultMessages;
    }

    private void generateAutoMetricForCriterion(String motivationId, String criterionId, String userId) {

        boolean alreadyLinked = criterionMetricRepository.existsByMotivationAndCriterionAndVersion(motivationId, criterionId, 1);

        if (alreadyLinked) {
            return; // Skip metric generation
        }

        var createMetric = new MetricRequestDto();
        createMetric.MTR = " ";
        createMetric.labelMetric = " ";
        createMetric.descrMetric = " ";
        createMetric.criterion_id = criterionId;
        createMetric.typeBenchmarkId = "pid_graph:7085006F";
        createMetric.typeAlgorithmId = "pid_graph:AE39C968";
        createMetric.typeMetricId = "pid_graph:8D79984F";
        createMetric.valueBenchmark = "1";

        var metric = motivationService.createMetricForMotivation(motivationId, createMetric, userId);

        var criMtr = new MultipleCriterionMetricRequest();
        criMtr.criterionId = criterionId;
        criMtr.relation = "maintainedBy";
        criMtr.metricId = metric.id;

        criterionMetricService.createNewCriteriaMetricsRelationship(motivationId, Set.of(criMtr), userId);
    }

    private void removeCriteriaAndAutoMetric(Motivation motivation, RegistryActor actor, Set<CriterionActorRequest> request, List<String> resultMessages) {

        var criterionIdsToKeep = request.stream()
                .map(req -> req.criterionId)
                .collect(Collectors.toSet());

        var allLinked = criterionActorRepository.fetchCriteriaByMotivationAndActor(motivation.getId(), actor.getId());

        for (var junction : allLinked) {
            var criterion = junction.getCriterion();
            if (!criterionIdsToKeep.contains(criterion.getId())) {

                // Remove CriterionActorJunction
                criterionActorRepository.delete(junction);
                resultMessages.add("Criterion with id: " + criterion.getId() + " removed from actor");

                // Check for metric linked to this criterion in this motivation
                var cmJunctions = criterionMetricRepository.fetchCriterionMetricByMotivationAndCriterion(motivation.getId(), criterion.getId());

                cmJunctions.ifPresent(cm -> {
                    var metric = cm.getMetric();

                    var expectedLabel = criterion.getLabel() + " Metric";
                    var expectedDesc = "Metric created for \"" + motivation.getLabel() + "\" and used by \"" + criterion.getCri() + "\".";

                    if (expectedLabel.equals(metric.getLabelMetric())
                            && expectedDesc.equals(metric.getDescrMetric())) {

                        criterionMetricRepository.delete(cm);
                        metricRepository.delete(metric);
                        resultMessages.add("Auto-generated metric " + metric.getId() + " removed along with its criterion.");
                    }
                });
            }
        }
    }

    private void removeCriteria(Motivation motivation, RegistryActor actor, Set<CriterionActorRequest> request, List<String> resultMessages) {

        var criterionList = new ArrayList<String>();

        request.iterator().forEachRemaining(req -> criterionList.add(req.criterionId));
        criterionActorRepository.fetchCriteriaByMotivationAndActor(motivation.getId(), actor.getId()).iterator().forEachRemaining(ac -> {
            if (!criterionList.contains(ac.getCriterion().getId())) {
                criterionActorRepository.delete(ac);
                resultMessages.add("criterion with id: " + ac.getCriterion().getId() + " removed from actor");
            }
        });
    }

    public void doesActorWithGivenNameExist(String name) {

        registryActorRepository.fetchActorByName(name).orElseThrow(() -> new NotFoundException("There is no Actor : " + name));
    }
}