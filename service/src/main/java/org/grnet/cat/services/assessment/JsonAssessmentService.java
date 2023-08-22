package org.grnet.cat.services.assessment;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.assessment.JsonAssessmentRequest;
import org.grnet.cat.dtos.assessment.JsonAssessmentResponse;
import org.grnet.cat.dtos.assessment.UpdateJsonAssessmentRequest;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.entities.Assessment;
import org.grnet.cat.enums.ValidationStatus;
import org.grnet.cat.mappers.AssessmentMapper;
import org.grnet.cat.repositories.AssessmentRepository;
import org.grnet.cat.repositories.TemplateRepository;
import org.grnet.cat.repositories.ValidationRepository;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * The AssessmentService provides operations for managing assessments expressed by a JSON object.
 */
@ApplicationScoped
@Named("json-assessment-service")
public class JsonAssessmentService implements AssessmentService<JsonAssessmentRequest, UpdateJsonAssessmentRequest, JsonAssessmentResponse> {

    @Inject
    AssessmentRepository assessmentRepository;

    @Inject
    TemplateRepository templateRepository;

    @Inject
    ValidationRepository validationRepository;

    @Transactional
    public JsonAssessmentResponse createAssessment(String userId, JsonAssessmentRequest request) {

        var validation = validationRepository.findById(request.validationId);

        if (!validation.getUser().getId().equals(userId)) {
            throw new ForbiddenException("User not authorized to create assessment for Validation with ID " + request.validationId);
        }

        if (!validation.getStatus().equals(ValidationStatus.APPROVED)) {
            throw new ForbiddenException("User not authorized to create assessment for non approved Validation with ID " + request.validationId);
        }

        var template = templateRepository.findById(request.templateId);

        if(!validation.getActor().equals(template.getActor())){
            throw new BadRequestException("Actor in Validation Request mismatches actor in Template.");
        }

        Assessment assessment = new Assessment();
        assessment.setAssessmentDoc(request.assessmentDoc.toString());
        assessment.setCreatedOn(Timestamp.from(Instant.now()));
        assessment.setTemplate(template);
        assessment.setValidation(validation);

        assessmentRepository.persist(assessment);

        return AssessmentMapper.INSTANCE.assessmentToJsonAssessment(assessment);
    }

    /**
     * Retrieves a specific assessment if it belongs to the user.
     *
     * @param userId The ID of the user.
     * @param assessmentId The ID of the assessment to retrieve.
     * @return The assessment if it belongs to the user.
     * @throws ForbiddenException If the user is not authorized to access the assessment.
     */
    public JsonAssessmentResponse getAssessment(String userId, Long assessmentId) {

        var assessment = assessmentRepository.findById(assessmentId);

        if(!assessment.getValidation().getUser().getId().equals(userId)){
            throw new ForbiddenException("Not Permitted.");
        }

        return AssessmentMapper.INSTANCE.assessmentToJsonAssessment(assessment);
    }

    @Transactional
    public void deleteAll() {
        assessmentRepository.deleteAll();
    }

    /**
     * Updates the Assessment's json document.
     *
     * @param id   The ID of the assessment whose json doc is being updated.
     * @param request The update request.
     * @return The updated assessment
     */
    @Transactional
    public JsonAssessmentResponse updateAssessment(Long id, String userId, UpdateJsonAssessmentRequest request) {

        var assessment = assessmentRepository.findById(id);

        if (!assessment.getValidation().getUser().getId().equals(userId)) {
            throw new ForbiddenException("User not authorized to update assessment with ID " + id);
        }

        assessment.setAssessmentDoc(request.assessmentDoc.toString());
        assessment.setUpdatedOn(Timestamp.from(Instant.now()));

        return AssessmentMapper.INSTANCE.assessmentToJsonAssessment(assessment);
    }

    /**
     * Retrieves a page of assessments submitted by the specified user.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of assessments to include in a page.
     * @param uriInfo The Uri Info.
     * @param userID The ID of the user.
     * @return A list of AssessmentResponseDto objects representing the submitted assessments in the requested page.
     */
    public PageResource<JsonAssessmentResponse> getAssessmentsByUserAndPage(int page, int size, UriInfo uriInfo, String userID){

        var assessments = assessmentRepository.fetchAssessmentsByUserAndPage(page, size, userID);

        return new PageResource<>(assessments, AssessmentMapper.INSTANCE.assessmentsToJsonAssessments(assessments.list()), uriInfo);
    }
}