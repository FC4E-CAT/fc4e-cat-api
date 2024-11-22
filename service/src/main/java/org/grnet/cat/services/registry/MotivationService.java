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
import org.grnet.cat.dtos.registry.PrincipleCriterionResponseDto;
import org.grnet.cat.dtos.registry.actor.MotivationActorRequest;
import org.grnet.cat.dtos.registry.actor.MotivationActorResponse;
import org.grnet.cat.dtos.registry.motivation.MotivationRequest;
import org.grnet.cat.dtos.registry.motivation.MotivationResponse;
import org.grnet.cat.dtos.registry.motivation.PrincipleCriterionRequest;
import org.grnet.cat.dtos.registry.motivation.UpdateMotivationRequest;
import org.grnet.cat.dtos.registry.principle.MotivationPrincipleExtendedRequestDto;
import org.grnet.cat.dtos.registry.principle.MotivationPrincipleRequest;
import org.grnet.cat.dtos.registry.principle.PrincipleRequestDto;
import org.grnet.cat.entities.registry.*;
import org.grnet.cat.mappers.registry.*;
import org.grnet.cat.repositories.registry.*;
import org.keycloak.common.util.DelegatingSerializationFilter;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

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

        var pcList = principleCriterionRepository
                .fetchPrincipleCriterionByMotivation(motivationId);

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
        motivation.getActors().forEach(actor -> actor.setPublished(Boolean.TRUE));
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
        motivation.getActors().forEach(actor -> actor.setPublished(Boolean.FALSE));
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
}
