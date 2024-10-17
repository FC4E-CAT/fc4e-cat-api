package org.grnet.cat.services.registry;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.UriInfo;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.principle.PrincipleRequestDto;
import org.grnet.cat.dtos.registry.principle.PrincipleResponseDto;
import org.grnet.cat.dtos.registry.principle.PrincipleUpdateDto;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.exceptions.UniqueConstraintViolationException;
import org.grnet.cat.mappers.registry.PrincipleCriterionMapper;
import org.grnet.cat.mappers.registry.PrincipleMapper;
import org.grnet.cat.repositories.registry.PrincipleRepository;
import org.jboss.logging.Logger;

@ApplicationScoped
public class PrincipleService {

    @Inject
    PrincipleRepository principleRepository;

    private static final Logger LOG = Logger.getLogger(PrincipleService.class);

    /**
     * Retrieves a page of principle items.
     *
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of principle items to include in a page.
     * @param uriInfo The Uri Info.
     * @return A PageResource containing the principle items in the requested page.
     */
    public PageResource<PrincipleResponseDto> listAll(int page, int size, UriInfo uriInfo) {

        var principlePage = principleRepository.fetchPrincipleByPage(page, size);
        var principleDTOs = PrincipleMapper.INSTANCE.principleToDtos(principlePage.list());
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
        return PrincipleMapper.INSTANCE.principleToDto(principle);
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
    @Transactional
    public PrincipleResponseDto update(String id, PrincipleUpdateDto principleUpdateDto, String userId) {

        var principle = principleRepository.findById(id);

        var currentPri = principle.getPri();
        var updatePri = principleUpdateDto.pri.toUpperCase();


        if(!updatePri.equals(currentPri) && StringUtils.isNotEmpty(updatePri)){

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
        return principleRepository.deleteById(id);
    }



    @Transactional
    public PageResource<PrincipleResponseDto> listPrinciplesByMotivation(String motivationId, String search, String sort, String order, int page, int size, UriInfo uriInfo) {

        var principlePage = principleRepository.fetchPrincipleByMotivation(motivationId, search, sort, order, page, size);
        var principleDto = PrincipleCriterionMapper.INSTANCE.principleToDtos(principlePage.list());

        return new PageResource<>(principlePage, principleDto, uriInfo);

    }




    @Transactional
    public void deleteAll() {
        principleRepository.deleteAll();
    }
}
