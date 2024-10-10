package org.grnet.cat.services.registry;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.codelist.RegistryActorResponse;
import org.grnet.cat.dtos.registry.criterion.CriterionActorRequest;
import org.grnet.cat.dtos.registry.criterion.PrincipleCriterionResponse;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.registry.Motivation;
import org.grnet.cat.entities.registry.RegistryActor;
import org.grnet.cat.mappers.registry.CriterionActorMapper;
import org.grnet.cat.mappers.registry.RegistryActorMapper;
import org.grnet.cat.repositories.registry.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    @Transactional
    public List<String> addCriteria(String motivationId, String actorId, Set<CriterionActorRequest> criterionActorRequest, String userId) {

        var resultMessages = new ArrayList<String>();

        if (!motivationActorRepository.existsByMotivationAndActorAndVersion(motivationId, actorId, 1)) {
            throw new NotFoundException("relation between motivation with id: " + motivationId + " and actor with id: " + actorId + " in version : " + 1 + " does not exist");
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
    public PageResource<PrincipleCriterionResponse> getCriteriaByMotivationActorAndPage(String motivationId, String actorId, int page, int size, UriInfo uriInfo) {

        if (!motivationActorRepository.existsByMotivationAndActorAndVersion(motivationId, actorId, 1)) {
            throw new NotFoundException("relation between motivation with id: " + motivationId + " and actor with id: " + actorId + " in version : " + 1 + " does not exist");
        }

        var criteriaActor = criterionActorRepository.fetchCriteriaByMotivationAndActorAndPage(motivationId, actorId, page, size);

        return new PageResource<>(criteriaActor, CriterionActorMapper.INSTANCE.toPrincipleCriterionResponseList(criteriaActor.list()), uriInfo);
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
    @Transactional
    public List<String> updateCriteria(String motivationId, String actorId, Set<CriterionActorRequest> criterionActorRequest, String userId) {
        var resultMessages = new ArrayList<String>();

        if (!motivationActorRepository.existsByMotivationAndActorAndVersion(motivationId, actorId, 1)) {
            throw new NotFoundException("relation between motivation with id: " + motivationId + " and actor with id: " + actorId + " in version : " + 1 + " does not exist");
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
}