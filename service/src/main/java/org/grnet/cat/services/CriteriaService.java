package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.criteria.CriteriaRequestDto;
import org.grnet.cat.dtos.criteria.CriteriaResponseDto;
import org.grnet.cat.dtos.criteria.CriteriaUpdateDto;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.exceptions.UniqueConstraintViolationException;
import org.grnet.cat.mappers.CriteriaMapper;
import org.grnet.cat.repositories.CriteriaRepository;
import org.jboss.logging.Logger;

import java.sql.Timestamp;
import java.time.Instant;

@ApplicationScoped
public class CriteriaService {

    @Inject
    CriteriaRepository criteriaRepository;

    private static final Logger LOG = Logger.getLogger(CriteriaService.class);

    /**
     * Retrieves a page of criteria items.
     *
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of criteria items to include in a page.
     * @param uriInfo The Uri Info.
     * @return A PageResource containing the criteria items in the requested page.
     */
    public PageResource<CriteriaResponseDto> listAll(int page, int size, UriInfo uriInfo) {
        var criteriaPage = criteriaRepository.fetchCriteriaByPage(page, size);
        var criteriaDTOs = CriteriaMapper.INSTANCE.criteriaToDtos(criteriaPage.list());
        return new PageResource<>(criteriaPage, criteriaDTOs, uriInfo);
    }

    /**
     * Retrieves a specific criteria item by ID.
     *
     * @param id The ID of the criteria item to retrieve.
     * @return The requested criteria item.
     */
    public CriteriaResponseDto findById(Long id) {
        var criteria = criteriaRepository.findById(id);
        if (criteria == null) {
            throw new NotFoundException("Criteria not found");
        }
        return CriteriaMapper.INSTANCE.criteriaToDto(criteria);
    }

    /**
     * Creates a new criteria item.
     *
     * @param criteriaRequestDto The criteria item to create.
     * @param userId The ID of the user creating the criteria.
     * @return The created criteria item.
     */
    @Transactional
    public CriteriaResponseDto create(CriteriaRequestDto criteriaRequestDto, String userId) {

        if (criteriaRepository.notUnique("uuid", criteriaRequestDto.uuid.toUpperCase())) {
            throw new UniqueConstraintViolationException("uuid", criteriaRequestDto.uuid.toUpperCase());
        }

        if (criteriaRepository.notUnique("criteriaCode", criteriaRequestDto.criteriaCode.toUpperCase())) {
            throw new UniqueConstraintViolationException("criteriaCode", criteriaRequestDto.criteriaCode.toUpperCase());
        }

        System.out.println("Creating criteria with UUID: " + criteriaRequestDto.uuid + " and criteriaCode: " + criteriaRequestDto.imperative);


        var criteria = CriteriaMapper.INSTANCE.criteriaToEntity(criteriaRequestDto);

        criteria.setCreatedOn(Timestamp.from(Instant.now()));
        criteria.setCreatedBy(userId);

        criteriaRepository.persist(criteria);

        return CriteriaMapper.INSTANCE.criteriaToDto(criteria);
    }


    /**
     * Updates an existing criteria item.
     *
     * @param id                  The ID of the criteria item to update.
     * @param criteriaUpdateDto  The updated criteria item.
     * @param userId              The ID of the user updating the criteria.
     * @return The updated criteria item.
     */
    @Transactional
    public CriteriaResponseDto update(Long id, CriteriaUpdateDto criteriaUpdateDto, String userId) {

        var criteria = criteriaRepository.findById(id);

        CriteriaMapper.INSTANCE.updateCriteria(criteriaUpdateDto, criteria);

        criteria.setModifiedOn(Timestamp.from(Instant.now()));
        criteria.setModifiedBy(userId);

        return CriteriaMapper.INSTANCE.criteriaToDto(criteria);
    }


    /**
     * Deletes a specific criteria item by ID.
     * @param id The ID of the criteria item to delete.
     * @return True if the criteria item was deleted, false otherwise.
     */
    @Transactional
    public boolean delete(Long id) {
        return criteriaRepository.deleteById(id);
    }

}
