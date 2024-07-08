package org.grnet.cat.services.assessment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;
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
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.statistics.AssessmentStatisticsResponse;
import org.grnet.cat.dtos.subject.SubjectRequest;
import org.grnet.cat.dtos.template.TemplateSubjectDto;
import org.grnet.cat.entities.Assessment;
import org.grnet.cat.services.StatisticsEnum;
import org.grnet.cat.exceptions.InternalServerErrorException;
import org.grnet.cat.mappers.AssessmentMapper;
import org.grnet.cat.mappers.TemplateMapper;
import org.grnet.cat.repositories.AssessmentRepository;
import org.grnet.cat.repositories.TemplateRepository;
import org.grnet.cat.repositories.ValidationRepository;
import org.grnet.cat.services.SubjectService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The AssessmentService provides operations for managing assessments expressed by a JSON object.
 */
@ApplicationScoped
@Named("json-assessment-service")
public class JsonAssessmentService extends JsonAbstractAssessmentService<JsonAssessmentRequest, JsonAssessmentRequest, AssessmentResponse, Assessment> {

    @Inject
    AssessmentRepository assessmentRepository;

    @Inject
    TemplateRepository templateRepository;

    @Inject
    ValidationRepository validationRepository;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    SubjectService subjectService;

    @Override
    @Transactional
    @SneakyThrows
    public JsonAssessmentResponse createAssessment(String userId, JsonAssessmentRequest request) {

        handleSubjectDatabaseId(userId, request);
        var validation = validationRepository.fetchValidationByUserAndActorAndOrganisation(userId, request.assessmentDoc.actor.id, request.assessmentDoc.organisation.id);
        var template = templateRepository.fetchTemplateByActor(request.assessmentDoc.actor.id);

        validateAssessmentAgainstTemplate(objectMapper.writeValueAsString(request.assessmentDoc), objectMapper.writeValueAsString(TemplateMapper.INSTANCE.templateToDto(template).templateDoc));

        var timestamp = Timestamp.from(Instant.now());
        request.assessmentDoc.timestamp = timestamp.toString();
        request.assessmentDoc.organisation.name = validation.getOrganisationName();

        Assessment assessment = new Assessment();
        assessment.setAssessmentDoc(objectMapper.writeValueAsString(request.assessmentDoc));
        assessment.setCreatedOn(timestamp);
        assessment.setTemplate(template);
        assessment.setValidation(validation);
        assessment.setSubject(subjectService.getSubjectById(request.assessmentDoc.subject.dbId));

        assessmentRepository.persist(assessment);

        //assign assessment id to json
        var storedAssessment = assessmentRepository.findById(assessment.getId());

        var assessmentDoc = AssessmentMapper.INSTANCE.templateDocToAssessmentDoc(request.assessmentDoc);
        assessmentDoc.id = assessment.getId();
        storedAssessment.setAssessmentDoc(objectMapper.writeValueAsString(assessmentDoc));

        return AssessmentMapper.INSTANCE.assessmentToJsonAssessment(storedAssessment);
    }

    /**
     * The API should provide flexibility for clients to either use an existing Subject by specifying its id in the db_id property
     * or create a new one by leaving db_id empty and filling in the other three properties (name, type and id).
     * The API should also give precedence to the properties stored in the database. If a valid db_id is provided,
     * its associated Subject should be retrieved, and the values of name, type, and id in the Assessment should be overwritten
     * with the corresponding values from the existing  database Subject.
     */
    private void handleSubjectDatabaseId(String userId, JsonAssessmentRequest request) {

        if (Objects.isNull(request.assessmentDoc.subject.dbId)) {

            var optional = subjectService
                    .getSubjectByNameAndTypeAndSubjectId(request.assessmentDoc.subject.name, request.assessmentDoc.subject.type, request.assessmentDoc.subject.id, userId);

            if (optional.isEmpty()) {

                var newSubject = new SubjectRequest();
                newSubject.name = request.assessmentDoc.subject.name;
                newSubject.type = request.assessmentDoc.subject.type;
                newSubject.id = request.assessmentDoc.subject.id;

                var response = subjectService.createSubject(newSubject, userId);
                request.assessmentDoc.subject.dbId = response.id;
            } else {

                request.assessmentDoc.subject.dbId = optional.get().getId();
            }
        } else {

            var subject = subjectService.getSubjectById(request.assessmentDoc.subject.dbId);

            if (!subject.getCreatedBy().equals(userId)) {
                throw new ForbiddenException("User not authorized to manage subject with ID " + request.assessmentDoc.subject.dbId);
            }

            request.assessmentDoc.subject.name = subject.getName();
            request.assessmentDoc.subject.type = subject.getType();
            request.assessmentDoc.subject.id = subject.getSubjectId();
        }
    }

    private void validateAssessmentAgainstTemplate(String request, String template) {

        boolean isValid;

        var listOfMismatchedFields = new ArrayList<String>();

        try {
            var cmp = new JsonFieldComparator();

            var expected = (ObjectNode) objectMapper.readTree(template);
            var actual = (ObjectNode) objectMapper.readTree(request);

            isValid = equals(cmp, actual, expected, listOfMismatchedFields);
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException("Server Error.", 500);
        }

        if (!isValid) {

            throw new BadRequestException("A field in Assessment Request mismatches a field in Template. Mismatches : "+listOfMismatchedFields);
        }
    }

    private boolean equals(Comparator<JsonNode> comparator, ObjectNode actual, ObjectNode expected, List<String> fields) {

        var actualMap = actual.properties().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        var expectedMap = expected.properties().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        int len = actualMap.size();
        if (expectedMap.size() != len) {
            return false;
        } else {
            var actualIterator = actualMap.entrySet().iterator();

            Map.Entry actualEntry;
            JsonNode expectedKey;
            var continueTheIteration = true;
            do {
                if (!actualIterator.hasNext()) {
                    return true;
                }

                actualEntry = actualIterator.next();
                expectedKey = expectedMap.get(actualEntry.getKey());

                if (expected != null && expectedKey.isObject()) {

                    if (!equals(comparator, (ObjectNode) actualEntry.getValue(), (ObjectNode) expectedKey, fields)) {
                        continueTheIteration = false;
                    }
                } else if (expectedKey != null && expectedKey.isArray()) {

                    if (!equals(comparator, (ArrayNode) actualEntry.getValue(), (ArrayNode) expectedKey, fields)) {
                        continueTheIteration = false;
                    }
                } else {

                    if (!(expectedKey != null && ((JsonNode) actualEntry.getValue()).equals(comparator, expectedKey))) {

                        if(!actualEntry.getKey().equals("description")){

                            fields.add(String.format("FIELD : %s | EXPECTED VALUE : %s | ACTUAL VALUE : %s", actualEntry.getKey(), expectedKey.asText(), ((JsonNode) actualEntry.getValue()).asText()));
                            continueTheIteration = false;
                        }
                    }
                }

            } while (continueTheIteration);

            return false;
        }
    }

    private boolean equals(Comparator<JsonNode> comparator, ArrayNode actual, ArrayNode expected, List<String> fields) {

        if (expected.size() == 0) {

            return true;
        }

        int actualArraySize = actual.size();

        if (expected.size() != actualArraySize) {

            return false;
        } else {

            var actualList = new ArrayList<JsonNode>();

            for (int i = 0; i < actual.size(); ++i) {

                actualList.add(actual.get(i));
            }

            var expectedList = new ArrayList<JsonNode>();

            for (int i = 0; i < expected.size(); ++i) {

                expectedList.add(expected.get(i));
            }

            for (int i = 0; i < actualList.size(); ++i) {

                if (actualList.get(i).isObject()) {

                    if (!equals(comparator, (ObjectNode) actualList.get(i), (ObjectNode) expectedList.get(i), fields)) {
                        return false;
                    }
                } else if (actualList.get(i).isArray()) {

                    if (!equals(comparator, (ArrayNode) actualList.get(i), (ArrayNode) expectedList.get(i), fields)) {
                        return false;
                    }
                } else {

                    if ((comparator.compare(actualList.get(i), expectedList.get(i)) != 0)) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    /**
     * Retrieves a specific assessment if it belongs to the user.
     *
     * @param userId       The ID of the user who requests a specific assessment.
     * @param assessmentId The ID of the assessment to retrieve.
     * @return The assessment if it belongs to the user.
     * @throws ForbiddenException If the user is not authorized to access the assessment.
     */
    @Override
    public JsonAssessmentResponse getDtoAssessment(String userId, String assessmentId) {

        var assessment = assessmentRepository.findById(assessmentId);

        var assessmentDto = AssessmentMapper.INSTANCE.assessmentToJsonAssessment(assessment);

        if (!assessment.getValidation().getUser().getId().equals(userId) && !assessmentDto.assessmentDoc.published) {
            throw new ForbiddenException("Not Permitted.");
        }

        return AssessmentMapper.INSTANCE.assessmentToJsonAssessment(assessment);
    }

    /**
     * Retrieves a specific public assessment.
     *
     * @param assessmentId The ID of the assessment to retrieve.
     * @return The assessment if it's public.
     */
    @Override
    public JsonAssessmentResponse getPublicDtoAssessment(String assessmentId) {

        var assessment = assessmentRepository.findById(assessmentId);

        var assessmentDto = AssessmentMapper.INSTANCE.assessmentToJsonAssessment(assessment);

        if (!assessmentDto.assessmentDoc.published) {
            throw new ForbiddenException("Not Permitted.");
        }

        return AssessmentMapper.INSTANCE.assessmentToJsonAssessment(assessment);
    }

    @Override
    @Transactional
    public void deleteAll() {
        assessmentRepository.deleteAll();
    }

    /**
     * Updates the Assessment's json document.
     *
     * @param id      The ID of the assessment whose json doc is being updated.
     * @param userId  The ID of the user who requests to update a specific assessment.
     * @param request The update request.
     * @return The updated assessment
     */
    @Override
    @SneakyThrows
    public JsonAssessmentResponse update(String id, String userId, JsonAssessmentRequest request) {

        handleSubjectDatabaseId(userId, request);

        return update(id, request);
    }

    /**
     * Updates the Assessment's json document. This method also handles the subjects that were inserted in the assessment document.
     *
     * @param id      The ID of the assessment whose json doc is being updated.
     * @param request The update request.
     * @return The updated assessment
     */
    @Override
    @SneakyThrows
    public JsonAssessmentResponse update(String id, JsonAssessmentRequest request) {

        var assessmentDoc = AssessmentMapper.INSTANCE.templateDocToAssessmentDoc(request.assessmentDoc);

        assessmentDoc.id = id;

        var assessment = assessmentRepository.updateAssessmentDocById(id, objectMapper.writeValueAsString(assessmentDoc));

        return AssessmentMapper.INSTANCE.assessmentToJsonAssessment(assessment);
    }

    /**
     * Retrieves a page of assessments submitted by the specified user.
     *
     * @param page        The index of the page to retrieve (starting from 0).
     * @param size        The maximum number of assessments to include in a page.
     * @param uriInfo     The Uri Info.
     * @param userID      The ID of the user who requests their assessments.
     * @param subjectName Subject name to search for.
     * @param subjectType Subject Type to search for.
     * @param actorId     Actor ID to search for.
     * @return A list of PartialJsonAssessmentResponse objects representing the submitted assessments in the requested page.
     */
    @Override
    public PageResource<PartialJsonAssessmentResponse> getDtoAssessmentsByUserAndPage(int page, int size, UriInfo uriInfo, String userID, String subjectName, String subjectType, Long actorId) {

        var assessments = assessmentRepository.fetchAssessmentsByUserAndPage(page, size, userID, subjectName, subjectType, actorId);

        var fullAssessments = AssessmentMapper.INSTANCE.assessmentsToJsonAssessments(assessments.list());

        return new PageResource<>(assessments, AssessmentMapper.INSTANCE.assessmentsToPartialJsonAssessments(fullAssessments), uriInfo);
    }

    /**
     * Retrieves a page of published assessments categorized by type and actor, created by all users.
     *
     * @param page        The index of the page to retrieve (starting from 0).
     * @param size        The maximum number of assessments to include in a page.
     * @param uriInfo     The Uri Info.
     * @param typeId      The ID of the Assessment Type.
     * @param actorId     The Actor's id.
     * @param subjectName Subject name to search for.
     * @param subjectType Subject Type to search for.
     * @return A list of PartialJsonAssessmentResponse objects representing the submitted assessments in the requested page.
     */
    @Override
    public PageResource<PartialJsonAssessmentResponse> getPublishedDtoAssessmentsByTypeAndActorAndPage(int page, int size, Long typeId, Long actorId, UriInfo uriInfo, String subjectName, String subjectType) {

        var assessments = assessmentRepository.fetchPublishedAssessmentsByTypeAndActorAndPage(page, size, typeId, actorId, subjectName, subjectType);

        var fullAssessments = AssessmentMapper.INSTANCE.assessmentsToJsonAssessments(assessments.list());

        return new PageResource<>(assessments, AssessmentMapper.INSTANCE.assessmentsToPartialJsonAssessments(fullAssessments), uriInfo);
    }

    /**
     * Retrieves a page of public assessment objects by type and actor.
     *
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of assessment objects to include in a page.
     * @param uriInfo The Uri Info.
     * @param typeId  The ID of the Assessment Type.
     * @param actorId The Actor's id.
     * @return A list of TemplateSubjectDto objects representing the public assessment objects in the requested page.
     */
    public PageResource<TemplateSubjectDto> getPublishedDtoAssessmentObjectsByTypeAndActorAndPage(int page, int size, Long typeId, Long actorId, UriInfo uriInfo) {

        var objects = assessmentRepository.fetchPublishedAssessmentObjectsByTypeAndActorAndPage(page, size, typeId, actorId);

        var jsonToObjects = objects
                .list()
                .stream()
                .map(ThrowingFunction.sneaky(json -> objectMapper.readValue(json, TemplateSubjectDto.class)))
                .collect(Collectors.toList());

        return new PageResource<>(objects, jsonToObjects, uriInfo);
    }

    @Override
    public void assessmentBelongsToUser(String userID, String assessmentId) {

        var assessment = assessmentRepository.findById(assessmentId);

        if (!assessment.getValidation().getUser().getId().equals(userID)) {
            throw new ForbiddenException("User not authorized to manage assessment with ID " + assessmentId);
        }
    }

    @Override
    public Assessment getAssessment(String assessmentId) {

        return assessmentRepository.findById(assessmentId);
    }

    @Override
    public void forbidActionsToPublicAssessment(Assessment assessment) {

        //        if (AssessmentMapper.INSTANCE.assessmentToJsonAssessment(assessment).assessmentDoc.published) {
//            throw new ForbiddenException("It is not allowed to manage a published assessment.");
//        }
    }

    /**
     * Deletes an assessment.
     *
     * @param assessmentId The ID of the assessment to be deleted.
     */
    @Override
    public void delete(String assessmentId) {

        assessmentRepository.deleteAssessmentById(assessmentId);
    }

    /**
     * Retrieves a page of assessment objects submitted by the specified user by the specified actor.
     *
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of assessment objects to include in a page.
     * @param uriInfo The Uri Info.
     * @param userID  The ID of the user who requests their assessments.
     * @param actorID The actor ID.
     * @return A list of TemplateSubjectDto objects representing the submitted assessment objects in the requested page.
     */
    public PageResource<TemplateSubjectDto> getAssessmentsObjectsByUserAndActor(int page, int size, UriInfo uriInfo, String userID, Long actorID) {

        var objects = assessmentRepository.fetchAssessmentsObjectsByUserAndActor(page, size, userID, actorID);

        var jsonToObjects = objects
                .list()
                .stream()
                .map(ThrowingFunction.sneaky(json -> objectMapper.readValue(json, TemplateSubjectDto.class)))
                .collect(Collectors.toList());

        return new PageResource<>(objects, jsonToObjects, uriInfo);
    }

    /**
     * Retrieves a page of assessment objects submitted by the specified user.
     *
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of assessment objects to include in a page.
     * @param uriInfo The Uri Info.
     * @param userID  The ID of the user who requests their assessments.
     * @return A list of TemplateSubjectDto objects representing the submitted assessment objects in the requested page.
     */
    public PageResource<TemplateSubjectDto> getAssessmentsObjectsByUser(int page, int size, UriInfo uriInfo, String userID) {

        var objects = assessmentRepository.fetchAssessmentsObjectsByUser(page, size, userID);

        var jsonToObjects = objects
                .list()
                .stream()
                .map(ThrowingFunction.sneaky(json -> objectMapper.readValue(json, TemplateSubjectDto.class)))
                .collect(Collectors.toList());

        return new PageResource<>(objects, jsonToObjects, uriInfo);
    }
}