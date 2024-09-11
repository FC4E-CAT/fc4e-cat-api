package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.guidance.GuidanceRequestDto;
import org.grnet.cat.dtos.guidance.GuidanceResponseDto;
import org.grnet.cat.dtos.guidance.GuidanceUpdateDto;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.exceptions.UniqueConstraintViolationException;
import org.grnet.cat.mappers.GuidanceMapper;
import org.grnet.cat.repositories.GuidanceRepository;
import org.jboss.logging.Logger;

import java.sql.Timestamp;
import java.time.Instant;

@ApplicationScoped
public class GuidanceService {

    @Inject
    GuidanceRepository guidanceRepository;

    private static final Logger LOG = Logger.getLogger(GuidanceService.class);

    /**
     * Retrieves a page of guidance items.
     *
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of guidance items to include in a page.
     * @param uriInfo The Uri Info.
     * @return A PageResource containing the guidance items in the requested page.
     */
    public PageResource<GuidanceResponseDto> listAll(int page, int size, UriInfo uriInfo) {
        var guidancePage = guidanceRepository.fetchGuidanceByPage(page, size);
        var guidanceDTOs = GuidanceMapper.INSTANCE.guidanceToDtos(guidancePage.list());
        return new PageResource<>(guidancePage, guidanceDTOs, uriInfo);
    }

    /**
     * Retrieves a specific guidance item by ID.
     *
     * @param id The ID of the guidance item to retrieve.
     * @return The requested guidance item.
     */
    public GuidanceResponseDto findById(Long id) {
        var guidance = guidanceRepository.findById(id);
        if (guidance == null) {
            throw new ForbiddenException("Guidance not found");
        }
        return GuidanceMapper.INSTANCE.guidanceToDto(guidance);
    }

    /**
     * Creates a new guidance item.
     *
     * @param guidanceRequestDto The guidance item to create.
     * @param userId The ID of the user creating the guidance.
     * @return The created guidance item.
     */
    @Transactional
    public GuidanceResponseDto create(GuidanceRequestDto guidanceRequestDto, String userId) {

        if (guidanceRepository.notUnique("uuid", guidanceRequestDto.uuid.toUpperCase())) {
            throw new UniqueConstraintViolationException("uuid", guidanceRequestDto.uuid.toUpperCase());
        }

        if (guidanceRepository.notUnique("gdn", guidanceRequestDto.gdn.toUpperCase())) {
            throw new UniqueConstraintViolationException("gdn", guidanceRequestDto.gdn.toUpperCase());
        }


        var guidance = GuidanceMapper.INSTANCE.guidanceToEntity(guidanceRequestDto);

        guidance.setCreatedOn(Timestamp.from(Instant.now()));
        guidance.setCreatedBy(userId);

        guidanceRepository.persist(guidance);

        return GuidanceMapper.INSTANCE.guidanceToDto(guidance);
    }


    /**
    * Updates an existing guidance item.
    *
    * @param id                  The ID of the guidance item to update.
    * @param guidanceUpdateDto  The updated guidance item.
    * @param userId              The ID of the user updating the guidance.
    * @return The updated guidance item.
    */
    @Transactional
    public GuidanceResponseDto update(Long id, GuidanceUpdateDto guidanceUpdateDto, String userId) {

        var guidance = guidanceRepository.findById(id);

        GuidanceMapper.INSTANCE.updateGuidance(guidanceUpdateDto, guidance);

        guidance.setModifiedOn(Timestamp.from(Instant.now()));
        guidance.setModifiedBy(userId);

        return GuidanceMapper.INSTANCE.guidanceToDto(guidance);
    }


    /**
     * Deletes a specific guidance item by ID.
     * @param id The ID of the guidance item to delete.
     * @return True if the guidance item was deleted, false otherwise.
     */
    @Transactional
    public boolean delete(Long id) {
        return guidanceRepository.deleteById(id);
    }

}
