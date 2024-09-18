package org.grnet.cat.services.registry;

import io.quarkus.hibernate.orm.panache.Panache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriInfo;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.actor.MotivationActorRequest;
import org.grnet.cat.dtos.registry.actor.MotivationActorResponse;
import org.grnet.cat.dtos.registry.motivation.MotivationRequest;
import org.grnet.cat.dtos.registry.motivation.MotivationResponse;
import org.grnet.cat.dtos.registry.motivation.UpdateMotivationRequest;
import org.grnet.cat.dtos.registry.principle.MotivationPrincipleRequest;
import org.grnet.cat.entities.registry.MotivationType;
import org.grnet.cat.entities.registry.RegistryActor;
import org.grnet.cat.entities.registry.Principle;
import org.grnet.cat.entities.registry.Relation;
import org.grnet.cat.mappers.registry.MotivationActorMapper;
import org.grnet.cat.mappers.registry.MotivationMapper;
import org.grnet.cat.repositories.registry.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@ApplicationScoped
public class MotivationService {

    @Inject
    MotivationActorRepository motivationActorRepository;

    @Inject
    RegistryActorRepository registryActorRepository;

    @Inject
    RelationRepository relationRepository;

    @Inject
    PrincipleRepository principleRepository;

    @Inject
    MotivationRepository motivationRepository;

    @Inject
    MotivationTypeRepository motivationTypeRepository;

    @Inject
    MotivationPrincipleRepository motivationPrincipleRepository;

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
        motivationRepository.persist(motivation);
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

                if(StringUtils.isEmpty(actor.getLodMTV())){

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

    /**
     * Assign multiple principles to a single motivation.
     *
     * @param motivationId           The Motivation to assign Principles.
     * @param request                The MotivationPrincipleRequest to be added.
     * @param userId                 The user who requests to assign Principles to the Motivation.
     * @return The added relation.
     */
    @Transactional
    public List<String> assignPrinciples(String motivationId, Set<MotivationPrincipleRequest> request, String userId) {

        var resultMessages = new ArrayList<String>();

        var motivation = motivationRepository.findById(motivationId);
        request.stream().iterator().forEachRemaining(req -> {

            if (!motivationPrincipleRepository.existsByMotivationAndPrincipleAndVersion(motivationId, req.principleId, 1)) {

                var principle = principleRepository.findById(req.principleId);

                if(StringUtils.isEmpty(principle.getLodMTV())){

                    principle.setLodMTV(motivationId);
                }

                motivation.addPrinciple(Panache.getEntityManager().getReference(Principle.class, req.principleId), req.annotationText, req.annotationUrl, Panache.getEntityManager().getReference(Relation.class, req.relation), motivation.getId(), 1, userId, Timestamp.from(Instant.now()));
                resultMessages.add("Principle with id :: " + req.principleId + " successfully added to motivation.");
            } else {
                resultMessages.add("Principle with id :: " +req.principleId + " already exists to motivation.");
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

        var motivation = motivationRepository.fetchById(id);

        return MotivationMapper.INSTANCE.motivationToDto(motivation);
    }

    /**
     * Retrieves a page of Motivations.
     *
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of Motivations to include in a page.
     * @param uriInfo The Uri Info.
     * @return A list of MotivationResponse objects representing the submitted Motivations in the requested page.
     */
    public PageResource<MotivationResponse> getMotivationsByPage(int page, int size, UriInfo uriInfo) {

        var motivations = motivationRepository.fetchMotivationsByPage(page, size);

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

        if (!Objects.isNull(request.lodMtvP)) {

            var parent = motivationRepository.findByIdOptional(request.lodMtvP).orElseThrow(() -> new NotFoundException("There is no Motivation with the following id: " + request.motivationTypeId));
            motivation.setLodMtvP(parent.getId());
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
}