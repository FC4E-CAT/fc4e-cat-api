package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.PrincipleRequestDto;
import org.grnet.cat.dtos.PrincipleResponseDto;
import org.grnet.cat.dtos.PrincipleUpdateDto;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.entities.Principle;
import org.grnet.cat.exceptions.UniqueConstraintViolationException;
import org.grnet.cat.mappers.GuidanceMapper;
import org.grnet.cat.mappers.PrincipleMapper;
import org.grnet.cat.repositories.PrincipleRepository;
import org.jboss.logging.Logger;

import java.sql.Timestamp;
import java.time.Instant;

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
        var guidanceDTOs = PrincipleMapper.INSTANCE.principleToDtos(principlePage.list());
        return new PageResource<>(principlePage, guidanceDTOs, uriInfo);
    }

    /**
     * Retrieves a specific principle item by ID.
     *
     * @param id The ID of the principle item to retrieve.
     * @return The requested principle item.
     */
    public PrincipleResponseDto findById(Long id) {
        var principle = principleRepository.findById(id);
        if (principle == null) {
            throw new ForbiddenException("Principle not found");
        }
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


        if (principleRepository.notUnique("uuid", principleRequestDto.uuid.toUpperCase())) {
            throw new UniqueConstraintViolationException("uuid", principleRequestDto.uuid.toUpperCase());
        }

        if (principleRepository.notUnique("pri", principleRequestDto.pri.toUpperCase())) {
            throw new UniqueConstraintViolationException("pri", principleRequestDto.pri.toUpperCase());
        }


        var principle = PrincipleMapper.INSTANCE.principleToEntity(principleRequestDto);

        principle.setCreatedOn(Timestamp.from(Instant.now()));
        principle.setCreatedBy(userId);

        principleRepository.persist(principle);

        return PrincipleMapper.INSTANCE.principleToDto(principle);
    }


    /**
     * Updates an existing principle item.
     *
     *  @param id                  The ID of the principle item to update.
     * @param principleUpdateDto  The updated principle item.
     * @param userId              The ID of the user updating the principle.
     * @return The updated principle item.
     */
    @Transactional
    public PrincipleResponseDto update(Long id, PrincipleUpdateDto principleUpdateDto, String userId) {
        var principle = principleRepository.findById(id);

        PrincipleMapper.INSTANCE.updatePrinciple(principleUpdateDto,principle);

        principle.setModifiedOn(Timestamp.from(Instant.now()));
        principle.setModifiedBy(userId);

        return PrincipleMapper.INSTANCE.principleToDto(principle);
}


    /**
     * Deletes a specific principle item by ID.
     * @param id The ID of the principle item to delete.
     * @return True if the principle item was deleted, false otherwise.
     */
    @Transactional
    public boolean delete(Long id) {
        return principleRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        principleRepository.deleteAll();
    }
}
