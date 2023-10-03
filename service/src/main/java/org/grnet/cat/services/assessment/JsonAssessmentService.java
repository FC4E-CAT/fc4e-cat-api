package org.grnet.cat.services.assessment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pivovarit.function.ThrowingFunction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.UriInfo;
import lombok.SneakyThrows;
import org.grnet.cat.dtos.assessment.AssessmentResponse;
import org.grnet.cat.dtos.assessment.JsonAssessmentRequest;
import org.grnet.cat.dtos.assessment.JsonAssessmentResponse;
import org.grnet.cat.dtos.assessment.PartialJsonAssessmentResponse;
import org.grnet.cat.dtos.assessment.UpdateJsonAssessmentRequest;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.template.TemplateSubjectDto;
import org.grnet.cat.entities.Assessment;
import org.grnet.cat.enums.ValidationStatus;
import org.grnet.cat.mappers.AssessmentMapper;
import org.grnet.cat.repositories.AssessmentRepository;
import org.grnet.cat.repositories.TemplateRepository;
import org.grnet.cat.repositories.ValidationRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.stream.Collectors;

/**
 * The AssessmentService provides operations for managing assessments expressed by a JSON object.
 */
@ApplicationScoped
@Named("json-assessment-service")
public class JsonAssessmentService implements AssessmentService<JsonAssessmentRequest, UpdateJsonAssessmentRequest, AssessmentResponse> {

    @Inject
    AssessmentRepository assessmentRepository;

    @Inject
    TemplateRepository templateRepository;

    @Inject
    ValidationRepository validationRepository;

    @Inject
    ObjectMapper objectMapper;

    @Transactional
    @SneakyThrows
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
        assessment.setAssessmentDoc(objectMapper.writeValueAsString(request.assessmentDoc));
        assessment.setCreatedOn(Timestamp.from(Instant.now()));
        assessment.setTemplate(template);
        assessment.setValidation(validation);

        assessmentRepository.persist(assessment);

        //assign assessment id to json
        var storedAssessment = assessmentRepository.findById(assessment.getId());

        var assessmentDoc = AssessmentMapper.INSTANCE.templateDocToAssessmentDoc(request.assessmentDoc);
        assessmentDoc.id = assessment.getId();
        storedAssessment.setAssessmentDoc(objectMapper.writeValueAsString(assessmentDoc));

        return AssessmentMapper.INSTANCE.assessmentToJsonAssessment(storedAssessment);
    }

    /**
     * Retrieves a specific assessment if it belongs to the user.
     *
     * @param userId The ID of the user who requests a specific assessment.
     * @param assessmentId The ID of the assessment to retrieve.
     * @return The assessment if it belongs to the user.
     * @throws ForbiddenException If the user is not authorized to access the assessment.
     */
    public JsonAssessmentResponse getAssessment(String userId, String assessmentId) {

        var assessment = assessmentRepository.findById(assessmentId);

        var assessmentDto = AssessmentMapper.INSTANCE.assessmentToJsonAssessment(assessment);

        if(!assessment.getValidation().getUser().getId().equals(userId) && !assessmentDto.assessmentDoc.published){
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
     * @param userId The ID of the user who requests to update a specific assessment.
     * @param request The update request.
     * @return The updated assessment
     */
    @Transactional
    @SneakyThrows
    public JsonAssessmentResponse updateAssessment(String id, String userId, UpdateJsonAssessmentRequest request) {

        var assessment = assessmentRepository.findById(id);

        if (!assessment.getValidation().getUser().getId().equals(userId)) {
            throw new ForbiddenException("User not authorized to update assessment with ID " + id);
        }

        var assessmentDoc = AssessmentMapper.INSTANCE.templateDocToAssessmentDoc(request.assessmentDoc);
        assessmentDoc.id = assessment.getId();

        assessment.setAssessmentDoc(objectMapper.writeValueAsString(assessmentDoc));
        assessment.setUpdatedOn(Timestamp.from(Instant.now()));

        return AssessmentMapper.INSTANCE.assessmentToJsonAssessment(assessment);
    }

    /**
     * Retrieves a page of assessments submitted by the specified user.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of assessments to include in a page.
     * @param uriInfo The Uri Info.
     * @param userID The ID of the user who requests their assessments.
     * @return A list of PartialJsonAssessmentResponse objects representing the submitted assessments in the requested page.
     */
    public PageResource<PartialJsonAssessmentResponse> getAssessmentsByUserAndPage(int page, int size, UriInfo uriInfo, String userID){

        var assessments = assessmentRepository.fetchAssessmentsByUserAndPage(page, size, userID);

        var fullAssessments = AssessmentMapper.INSTANCE.assessmentsToJsonAssessments(assessments.list());

        return new PageResource<>(assessments, AssessmentMapper.INSTANCE.assessmentsToPartialJsonAssessments(fullAssessments), uriInfo);
    }

    /**
     * Retrieves a page of published assessments categorized by type and actor, created by all users.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of assessments to include in a page.
     * @param uriInfo The Uri Info.
     * @param typeId The ID of the Assessment Type.
     * @param actorId The Actor's id.
     * @return A list of PartialJsonAssessmentResponse objects representing the submitted assessments in the requested page.
     */
    public PageResource<PartialJsonAssessmentResponse> getPublishedAssessmentsByTypeAndActorAndPage(int page, int size, Long typeId, Long actorId, UriInfo uriInfo){

        var assessments = assessmentRepository.fetchPublishedAssessmentsByTypeAndActorAndPage(page, size, typeId, actorId);

        var fullAssessments = AssessmentMapper.INSTANCE.assessmentsToJsonAssessments(assessments.list());

        return new PageResource<>(assessments, AssessmentMapper.INSTANCE.assessmentsToPartialJsonAssessments(fullAssessments), uriInfo);
    }

    /**
     * Deletes a private assessment if it is not published and belongs to the authenticated user.
     *
     * @param userID The ID of the user who requests their assessments.
     * @param assessmentId The ID of the assessment to be deleted.
     * @throws ForbiddenException If the user does not have permission to delete this assessment (e.g., it's published or doesn't belong to them).
     */
    @Transactional
    public void deletePrivateAssessment(String userID, String assessmentId) {

        var assessment = assessmentRepository.findById(assessmentId);

        if (!assessment.getValidation().getUser().getId().equals(userID)) {
            throw new ForbiddenException("User not authorized to delete assessment with ID " + assessmentId);
        }

        if(AssessmentMapper.INSTANCE.assessmentToJsonAssessment(assessment).assessmentDoc.published){
            throw new ForbiddenException("It is not allowed to delete a published assessment.");
        }

        assessmentRepository.delete(assessment);
    }

    /**
     * Retrieves a page of assessment objects submitted by the specified user by the specified actor.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of assessment objects to include in a page.
     * @param uriInfo The Uri Info.
     * @param userID The ID of the user who requests their assessments.
     * @param actorID The actor ID.
     * @return A list of TemplateSubjectDto objects representing the submitted assessment objects in the requested page.
     */
    public PageResource<TemplateSubjectDto> getAssessmentsObjectsByUserAndActor(int page, int size, UriInfo uriInfo, String userID, Long actorID){

        var objects = assessmentRepository.fetchAssessmentsObjectsByUserAndActor(page, size, userID, actorID);

        var jsonToObjects = objects
                .list()
                .stream()
                .map(ThrowingFunction.sneaky(json->objectMapper.readValue(json, TemplateSubjectDto.class)))
                .collect(Collectors.toList());

        return new PageResource<>(objects, jsonToObjects, uriInfo);
    }
}