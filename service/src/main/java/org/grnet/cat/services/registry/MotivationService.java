package org.grnet.cat.services.registry;

import io.quarkus.hibernate.orm.panache.Panache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.motivation.MotivationRequest;
import org.grnet.cat.dtos.registry.motivation.MotivationResponse;
import org.grnet.cat.dtos.registry.motivation.UpdateMotivationRequest;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.registry.Motivation;
import org.grnet.cat.entities.registry.MotivationType;
import org.grnet.cat.mappers.registry.MotivationMapper;
import org.grnet.cat.repositories.registry.MotivationRepository;
import org.grnet.cat.repositories.registry.MotivationTypeRepository;

import java.util.Objects;

@ApplicationScoped
public class MotivationService {

    @Inject
    private MotivationRepository motivationRepository;

    @Inject
    private MotivationTypeRepository motivationTypeRepository;


    /**
     * Creates a new Motivation.
     *
     * @param request The Motivation to be created.
     * @param userId The user who requests to create the Motivation.
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
     * Retrieves a specific Motivation.
     *
     * @param id The ID of the Motivation to retrieve.
     * @return The corresponding Motivation.
     */
    public MotivationResponse getMotivationById(String id) {

        var motivation = motivationRepository.findById(id);

        return MotivationMapper.INSTANCE.motivationToDto(motivation);
    }

    /**
     * Retrieves a page of Motivations.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Motivations to include in a page.
     * @param uriInfo The Uri Info.
     * @return A list of MotivationResponse objects representing the submitted Motivations in the requested page.
     */
    public PageResource<MotivationResponse> getMotivationsByPage(int page, int size, UriInfo uriInfo){

        PageQuery<Motivation> motivations = motivationRepository.fetchMotivationsByPage(page, size);

        return new PageResource<>(motivations, MotivationMapper.INSTANCE.motivationsToDto(motivations.list()), uriInfo);
    }

    /**
     * This method updates a Motivation.
     *
     * @param id The ID of the Motivation to update.
     * @param request The Motivation attributes to be updated.
     * @param userID The ID of the user who requests to update the Motivation.
     * @return The updated Motivation.
     */
    @Transactional
    public MotivationResponse update(String id, UpdateMotivationRequest request, String userID){

        var motivation = motivationRepository.findById(id);
        motivation.setPopulatedBy(userID);

        MotivationMapper.INSTANCE.updateMotivationFromDto(request, motivation);

        if(!Objects.isNull(request.motivationTypeId)){

            motivationTypeRepository.findByIdOptional(request.motivationTypeId).orElseThrow(()-> new NotFoundException("There is no Motivation Type with the following id: "+request.motivationTypeId));
            motivation.setMotivationType(Panache.getEntityManager().getReference(MotivationType.class, request.motivationTypeId));
        }

        if(!Objects.isNull(request.lodMtvP)){

            var parent = motivationRepository.findByIdOptional(request.lodMtvP).orElseThrow(()-> new NotFoundException("There is no Motivation with the following id: "+request.motivationTypeId));
            motivation.setLodMtvP(parent.getId());
        }

        return MotivationMapper.INSTANCE.motivationToDto(motivation);
    }
}