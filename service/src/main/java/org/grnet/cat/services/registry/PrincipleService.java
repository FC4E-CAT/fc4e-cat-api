package org.grnet.cat.services.registry;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.UriInfo;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.principle.PrincipleRequestDto;
import org.grnet.cat.dtos.registry.principle.PrincipleResponseDto;
import org.grnet.cat.dtos.registry.principle.PrincipleUpdateDto;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.entities.registry.Principle;
import org.grnet.cat.exceptions.UniqueConstraintViolationException;
import org.grnet.cat.mappers.registry.MotivationMapper;
import org.grnet.cat.mappers.registry.PrincipleMapper;
import org.grnet.cat.repositories.registry.MotivationPrincipleRepository;
import org.grnet.cat.repositories.registry.PrincipleCriterionRepository;
import org.grnet.cat.repositories.registry.PrincipleRepository;
import org.jboss.logging.Logger;

import java.util.stream.Collectors;

@ApplicationScoped
public class PrincipleService {

    @Inject
    PrincipleRepository principleRepository;

    @Inject
    PrincipleCriterionRepository principleCriterionRepository;

    @Inject
    MotivationPrincipleRepository motivationPrincipleRepository;


    private static final Logger LOG = Logger.getLogger(PrincipleService.class);

    /**
     * Retrieves a page of principle items.
     *
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of principle items to include in a page.
     * @param uriInfo The Uri Info.
     * @return A PageResource containing the principle items in the requested page.
     */
    public PageResource<PrincipleResponseDto> listAll(String search, String sort, String order,int page, int size, UriInfo uriInfo) {

        var principlePage = principleRepository.fetchPrincipleByPage(search, sort, order, page, size);

        var principleDTOs = principlePage.list().stream()
                .map(this::principleResponseWithMotivations)
                .collect(Collectors.toList());

        return new PageResource<>(principlePage, principleDTOs, uriInfo);
    }

    /**
     * Retrieves a specific principle item by ID.
     *
     * @param id The ID of the principle item to retrieve.
     * @return The requested principle item.
     */
    public PrincipleResponseDto findById(String id) {

        var principle = principleRepository.findById(id);

        return principleResponseWithMotivations(principle);
    }

    /**
     * Creates a new principle item.
     *
     * @param principleRequestDto The principle item to create.
     * @param userId The ID of the user creating the principle.
     * @return The created principle item.
     */
    @Transactional
    public PrincipleResponseDto create(PrincipleRequestDto principleRequestDto, String userId) {

        if (principleRepository.notUnique("pri", principleRequestDto.pri.toUpperCase())) {
            throw new UniqueConstraintViolationException("pri", principleRequestDto.pri.toUpperCase());
        }

        var principle = PrincipleMapper.INSTANCE.principleToEntity(principleRequestDto);
        principle.setPopulatedBy(userId);

        principleRepository.persist(principle);

        return PrincipleMapper.INSTANCE.principleToDto(principle);
    }

    /**
     * Updates an existing principle item.
     *
     * @param id                  The ID of the principle item to update.
     * @param principleUpdateDto  The updated principle item.
     * @param userId              The ID of the user updating the principle.
     * @return The updated principle item.
     */
    //@CheckPublishedRelation(type = PublishEntityType.PRINCIPLE,permittedStatus = false)
    @Transactional
    public PrincipleResponseDto update(String id, PrincipleUpdateDto principleUpdateDto, String userId) {
        if(principleCriterionRepository.existPrincipleInStatus(id, Boolean.TRUE) || motivationPrincipleRepository.existPrincipleInStatus(id, Boolean.TRUE)){
            throw new ForbiddenException("No action permitted , principle exists in a published motivation");
        }

        var principle = principleRepository.findById(id);

        var currentPri = principle.getPri();
        var updatePri = StringUtils.isNotEmpty(principleUpdateDto.pri) ? principleUpdateDto.pri.toUpperCase() : currentPri;


        if(StringUtils.isNotEmpty(updatePri) && !updatePri.equals(currentPri)){

            if (principleRepository.notUnique("pri", updatePri)) {
                throw new UniqueConstraintViolationException("pri", updatePri);
            }
        }

        PrincipleMapper.INSTANCE.updatePrinciple(principleUpdateDto,principle);

        principle.setPopulatedBy(userId);
        return PrincipleMapper.INSTANCE.principleToDto(principle);
    }


    /**
     * Deletes a specific principle item by ID.
     * @param id The ID of the principle item to delete.
     * @return True if the principle item was deleted, false otherwise.
     */
    @Transactional
    public boolean delete(String id) {
        if(principleCriterionRepository.existPrincipleInStatus(id, Boolean.TRUE)){
            throw new ForbiddenException("No action permitted , principle exists in a published motivation");
        }

        if(principleCriterionRepository.existsByPrinciple(id) || motivationPrincipleRepository.existsByPrinciple(id)) {

            throw new ForbiddenException("This Principle cannot be deleted because it is linked to a Motivation.");
        }

        return principleRepository.deleteById(id);
    }



    @Transactional
    public PageResource<PrincipleResponseDto> listPrinciplesByMotivation(String motivationId, String search, String sort, String order, int page, int size, UriInfo uriInfo) {

        var principlePage = principleRepository.fetchPrincipleByMotivation(motivationId, search, sort, order, page, size);
        var principleDto = PrincipleMapper.INSTANCE.principleToDtos(principlePage.list());

        return new PageResource<>(principlePage, principleDto, uriInfo);

    }




    @Transactional
    public void deleteAll() {
        principleRepository.deleteAll();
    }


    /**
     * This method takes a Principle entity, converts it to a PrincipleResponseDto, retrieves and maps
     * any associated motivations, and then sets the motivations in the response.
     *
     * @param principle The Principle entity to be converted and enhanced.
     * @return A PrincipleResponseDto with associated motivations.
     */
    private PrincipleResponseDto principleResponseWithMotivations(Principle principle) {

        var principleResponse = PrincipleMapper.INSTANCE.principleToDto(principle);

        var motivations = principleCriterionRepository.getMotivationIdsByPrinciple(principle.getId());
        var motivationResponses = motivations.stream()
                .map(MotivationMapper.INSTANCE::mapPartialMotivation)
                .collect(Collectors.toList());

        principleResponse.setMotivations(motivationResponses);

        return principleResponse;
    }
}
