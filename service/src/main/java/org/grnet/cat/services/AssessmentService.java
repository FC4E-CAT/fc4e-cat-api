package org.grnet.cat.services;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import org.grnet.cat.dtos.AssessmentRequest;
import org.grnet.cat.dtos.AssessmentResponseDto;
import org.grnet.cat.dtos.*;
import org.grnet.cat.entities.Assessment;
import org.grnet.cat.enums.ValidationStatus;
import org.grnet.cat.mappers.AssessmentMapper;
import org.grnet.cat.repositories.AssessmentRepository;
import org.grnet.cat.repositories.TemplateRepository;
import org.grnet.cat.repositories.ValidationRepository;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * The AssessmentService provides operations for managing assessments.
 */
@ApplicationScoped
public class AssessmentService {

    @Inject
    AssessmentRepository assessmentRepository;

    @Inject
    TemplateRepository templateRepository;
    @Inject
    ValidationRepository validationRepository;

    @Transactional
    public AssessmentResponseDto createAssessment(String userId, AssessmentRequest request) {

        var validation = validationRepository.findById(request.validationId);

        if (!validation.getUser().getId().equals(userId)) {
            throw new ForbiddenException("User not authorized to create assessment for Validation with ID " + request.validationId);
        }

        if (!validation.getStatus().equals(ValidationStatus.APPROVED)) {
            throw new ForbiddenException("User not authorized to create assessment for non approved Validation with ID " + request.validationId);
        }

        var template = templateRepository.findById(request.templateId);

        Assessment assessment = new Assessment();
        assessment.setAssessmentDoc(request.assessmentDoc.toString());
        assessment.setCreatedOn(Timestamp.from(Instant.now()));
        assessment.setTemplate(template);
        assessment.setValidation(validation);

        assessmentRepository.persist(assessment);

        return AssessmentMapper.INSTANCE.assessmentToResponseDto(assessment);
    }

    /**
     * Retrieves a specific assessment if it belongs to the user.
     *
     * @param userId The ID of the user.
     * @param assessmentId The ID of the assessment to retrieve.
     * @return The assessment if it belongs to the user.
     * @throws ForbiddenException If the user is not authorized to access the assessment.
     */
    public AssessmentResponseDto getAssessment(String userId, Long assessmentId) {

        var assessment = assessmentRepository.findById(assessmentId);

        if(!assessment.getValidation().getUser().getId().equals(userId)){
            throw new ForbiddenException("Not Permitted.");
        }

        return AssessmentMapper.INSTANCE.assessmentToResponseDto(assessment);
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
    public AssessmentResponseDto updateAssessment(Long id, String userId, UpdateAssessmentRequest request) {

        var assessment = assessmentRepository.findById(id);

        if (!assessment.getValidation().getUser().getId().equals(userId)) {
            throw new ForbiddenException("User not authorized to update assessment with ID " + id);
        }
        assessment.setAssessmentDoc(request.assessmentDoc.toString());
        assessment.setUpdatedOn(Timestamp.from(Instant.now()));
        assessmentRepository.persist(assessment);

        return AssessmentMapper.INSTANCE.assessmentToResponseDto(assessment);
    }
}
