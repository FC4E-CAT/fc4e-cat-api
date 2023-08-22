package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.assessment.AssessmentTypeDto;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.mappers.AssessmentTypeMapper;
import org.grnet.cat.repositories.AssessmentTypeRepository;

/**
 * The AssessmentTypeService provides operations for managing assessment types.
 */
@ApplicationScoped
public class AssessmentTypeService {

    /**
     * Injection point for the AssessmentType Repository
     */
    @Inject
    AssessmentTypeRepository assessmentTypeRepository;

    /**
     * Retrieves a page of assessment types.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of templates to include in a page.
     * @param uriInfo The Uri Info.
     * @return A list of AssessmentTypeDto objects representing the assessment types in the requested page.
     */
    public PageResource<AssessmentTypeDto> getAssessmentTypesByPage(int page, int size, UriInfo uriInfo){

        var types = assessmentTypeRepository.getAssessmentTypesByPage(page, size);

        return new PageResource<>(types, AssessmentTypeMapper.INSTANCE.assessmentTypesToDto(types.list()), uriInfo);
    }
}
