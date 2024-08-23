package org.grnet.cat.services.assessment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pivovarit.function.ThrowingFunction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriInfo;
import lombok.SneakyThrows;
import org.grnet.cat.dtos.UserProfileDto;
import org.grnet.cat.dtos.assessment.AdminJsonAssessmentResponse;
import org.grnet.cat.dtos.assessment.AssessmentResponse;
import org.grnet.cat.dtos.assessment.JsonAssessmentRequest;
import org.grnet.cat.dtos.assessment.AdminPartialJsonAssessmentResponse;
import org.grnet.cat.dtos.assessment.UserJsonAssessmentResponse;
import org.grnet.cat.dtos.assessment.UserPartialJsonAssessmentResponse;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.subject.SubjectRequest;
import org.grnet.cat.dtos.template.TemplateSubjectDto;
import org.grnet.cat.entities.Assessment;
import org.grnet.cat.enums.ShareableEntityType;
import org.grnet.cat.enums.UserType;
import org.grnet.cat.enums.ValidationStatus;
import org.grnet.cat.exceptions.ConflictException;
import org.grnet.cat.exceptions.InternalServerErrorException;
import org.grnet.cat.mappers.AssessmentMapper;
import org.grnet.cat.mappers.TemplateMapper;
import org.grnet.cat.mappers.UserMapper;
import org.grnet.cat.repositories.AssessmentRepository;
import org.grnet.cat.repositories.TemplateRepository;
import org.grnet.cat.repositories.UserRepository;
import org.grnet.cat.repositories.ValidationRepository;
import org.grnet.cat.services.KeycloakAdminService;
import org.grnet.cat.services.SubjectService;
import org.grnet.cat.services.interceptors.ShareableEntity;
import org.grnet.cat.utils.Utility;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.grnet.cat.services.KeycloakAdminService.ENTITLEMENTS_DELIMITER;

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
    ObjectMapper objectMapper;

    @Inject
    SubjectService subjectService;

    @Inject
    KeycloakAdminService keycloakAdminService;

    @Inject
    ValidationRepository validationRepository;

    @Inject
    Utility utility;

    @Inject
    UserRepository userRepository;

    @Transactional
    @SneakyThrows
    @Override
    public UserJsonAssessmentResponse createAssessment(String userId, JsonAssessmentRequest request) {

        var validation = validationRepository.fetchValidationByUserAndActorAndOrganisation(userId, request.assessmentDoc.actor.id, request.assessmentDoc.organisation.id);

        if(!validation.getStatus().equals(ValidationStatus.APPROVED)){

            throw new ForbiddenException("The validation request hasn't been approved yet!");
        }

        var template = templateRepository.fetchTemplateByActorAndType(request.assessmentDoc.actor.id, request.assessmentDoc.assessmentType.id);

        if(template.isEmpty()){

            throw new BadRequestException(String.format("Not Found Template for Actor [%s] and Assessment Type [%s].", request.assessmentDoc.actor.name, request.assessmentDoc.assessmentType.name));
        }

        validateTemplateJson(request.assessmentDoc);

        handleSubjectDatabaseId(userId, request);

        validateAssessmentAgainstTemplate(objectMapper.writeValueAsString(request.assessmentDoc), objectMapper.writeValueAsString(TemplateMapper.INSTANCE.templateToDto(template.get()).templateDoc));

        var timestamp = Timestamp.from(Instant.now());
        request.assessmentDoc.timestamp = timestamp.toString();
        request.assessmentDoc.organisation.name = validation.getOrganisationName();

        Assessment assessment = new Assessment();
        assessment.setAssessmentDoc(objectMapper.writeValueAsString(request.assessmentDoc));
        assessment.setCreatedOn(timestamp);
        assessment.setTemplate(template.get());
        assessment.setValidation(validation);
        assessment.setSubject(subjectService.getSubjectById(request.assessmentDoc.subject.dbId));

        assessmentRepository.persist(assessment);

        //assign assessment id to json
        var storedAssessment = assessmentRepository.findById(assessment.getId());

        var assessmentDoc = AssessmentMapper.INSTANCE.templateDocToAssessmentDoc(request.assessmentDoc);
        assessmentDoc.id = assessment.getId();
        storedAssessment.setAssessmentDoc(objectMapper.writeValueAsString(assessmentDoc));

        keycloakAdminService.addEntitlementsToUser(userId, ShareableEntityType.ASSESSMENT.getValue().concat(ENTITLEMENTS_DELIMITER).concat(assessment.getId()));

        return AssessmentMapper.INSTANCE.userAssessmentToJsonAssessment(storedAssessment);
    }

    /**
     * The API should provide flexibility for clients to either use an existing Subject by specifying its id in the db_id property
     * or create a new one by leaving db_id empty and filling in the other three properties (name, type and id).
     * The API should also give precedence to the properties stored in the database. If a valid db_id is provided,
     * its associated Subject should be retrieved, and the values of name, type, and id in the Assessment should be overwritten
     * with the corresponding values from the existing  database Subject.
     */

    @Override
    public void handleSubjectDatabaseId(String userId, JsonAssessmentRequest request) {

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

    @Override
    public void validateAssessmentAgainstTemplate(String request, String template) {

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
     * @param assessmentId The ID of the assessment to retrieve.
     * @return The assessment.
     */
    @Override
    public AdminJsonAssessmentResponse getDtoAssessment(String assessmentId) {

        var assessment = assessmentRepository.findById(assessmentId);

        return AssessmentMapper.INSTANCE.adminAssessmentToJsonAssessment(assessment);
    }

    /**
     * Retrieves a specific assessment if it belongs to the user.
     *
     * @param assessmentId The ID of the assessment to retrieve.
     * @return The assessment if it belongs to the user.
     * @throws ForbiddenException If the user is not authorized to access the assessment.
     */
    @ShareableEntity(type= ShareableEntityType.ASSESSMENT, id = String.class)
    public UserJsonAssessmentResponse getDtoAssessmentIfBelongsOrSharedToUser(String assessmentId) {

        var assessment = assessmentRepository.findById(assessmentId);

        return AssessmentMapper.INSTANCE.userAssessmentToJsonAssessment(assessment);
    }

    /**
     * Retrieves a specific public assessment.
     *
     * @param assessmentId The ID of the assessment to retrieve.
     * @return The assessment if it's public.
     */
    @Override
    public AdminJsonAssessmentResponse getPublicDtoAssessment(String assessmentId) {

        var assessment = assessmentRepository.findById(assessmentId);

        var assessmentDto = AssessmentMapper.INSTANCE.adminAssessmentToJsonAssessment(assessment);

        if (!assessmentDto.assessmentDoc.published) {
            throw new ForbiddenException("Not Permitted.");
        }

        return assessmentDto;
    }

    @Override
    @Transactional
    public void deleteAll() {
        assessmentRepository.deleteAll();
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
    public UserJsonAssessmentResponse update(String id, JsonAssessmentRequest request) {

        validateTemplateJson(request.assessmentDoc);

        var template = templateRepository.fetchTemplateByActorAndType(request.assessmentDoc.actor.id, request.assessmentDoc.assessmentType.id);

        validateAssessmentAgainstTemplate(objectMapper.writeValueAsString(request.assessmentDoc), objectMapper.writeValueAsString(TemplateMapper.INSTANCE.templateToDto(template.get()).templateDoc));

        var dbAssessmentToJson = AssessmentMapper.INSTANCE.userAssessmentToJsonAssessment(assessmentRepository.findById(id));

        var assessmentDoc = AssessmentMapper.INSTANCE.updateAssessmentDocFromTemplateDoc(request.assessmentDoc);

        assessmentDoc.id = id;
        assessmentDoc.version = dbAssessmentToJson.assessmentDoc.version;
        assessmentDoc.assessmentType = dbAssessmentToJson.assessmentDoc.assessmentType;
        assessmentDoc.actor = dbAssessmentToJson.assessmentDoc.actor;
        assessmentDoc.status = dbAssessmentToJson.assessmentDoc.status;
        assessmentDoc.subject = dbAssessmentToJson.assessmentDoc.subject;
        assessmentDoc.organisation = dbAssessmentToJson.assessmentDoc.organisation;
        assessmentDoc.timestamp = dbAssessmentToJson.assessmentDoc.timestamp;

        var assessment = assessmentRepository.updateAssessmentDocById(id, utility.getUserUniqueIdentifier(), objectMapper.writeValueAsString(assessmentDoc));

        return AssessmentMapper.INSTANCE.userAssessmentToJsonAssessment(assessment);
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
    public PageResource<UserPartialJsonAssessmentResponse> getDtoAssessmentsByUserAndPage(int page, int size, UriInfo uriInfo, String userID, String subjectName, String subjectType, Long actorId) {

        var sharableIds = keycloakAdminService
                .getUserEntitlements(userID)
                .stream()
                .map(entitlement->keycloakAdminService.getLastPartOfEntitlement(entitlement, ENTITLEMENTS_DELIMITER))
                .collect(Collectors.toList());

        var assessments = assessmentRepository.fetchAssessmentsByUserAndPage(page, size, userID, subjectName, subjectType, actorId, sharableIds);

        var fullAssessments = AssessmentMapper.INSTANCE.userAssessmentsToJsonAssessments(assessments.list());

        return new PageResource<>(assessments, AssessmentMapper.INSTANCE.userAssessmentsToPartialJsonAssessments(fullAssessments), uriInfo);
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
    public PageResource<AdminPartialJsonAssessmentResponse> getPublishedDtoAssessmentsByTypeAndActorAndPage(int page, int size, Long typeId, Long actorId, UriInfo uriInfo, String subjectName, String subjectType) {

        var assessments = assessmentRepository.fetchPublishedAssessmentsByTypeAndActorAndPage(page, size, typeId, actorId, subjectName, subjectType);

        var fullAssessments = AssessmentMapper.INSTANCE.adminAssessmentsToJsonAssessments(assessments.list());

        return new PageResource<>(assessments, AssessmentMapper.INSTANCE.adminAssessmentsToPartialJsonAssessments(fullAssessments), uriInfo);
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
     * Retrieve all assessments.
     *
     * @return List of all assessments
     */
    @Override
    public PageResource<AdminPartialJsonAssessmentResponse> getAllAssessmentsByPage(int page, int size, String search, UriInfo uriInfo) {

        var assessments = assessmentRepository.fetchAllAssessmentsByPage(page, size, search);
        var fullAssessments = AssessmentMapper.INSTANCE.adminAssessmentsToJsonAssessments(assessments.list());
        var partialAssessments  = AssessmentMapper.INSTANCE.adminAssessmentsToPartialJsonAssessments(fullAssessments);

        return new PageResource<>(assessments, partialAssessments, uriInfo);
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

    /**
     * Shares an assessment with specified users.
     *
     * @param assessmentId The ID of the assessment to be shared.
     * @param email A user's email with whom the assessment will be shared.
     */
    @ShareableEntity(type= ShareableEntityType.ASSESSMENT, id = String.class)
    public void shareAssessment(String assessmentId, String email){

        var user = userRepository.fetchActiveUserByEmail(email).orElseThrow(()-> new NotFoundException("There is no user with email : "+email));

        var type = userRepository.findUserTypeById(user.getId());

        if(!type.equals(UserType.Validated)){

            throw new ForbiddenException("You cannot share an assessment with a user who has not been validated.");
        }

        var entitlementToBeAdded = ShareableEntityType.ASSESSMENT.getValue().concat(ENTITLEMENTS_DELIMITER).concat(assessmentId);

        var listOfEntitlements = keycloakAdminService.getUserEntitlements(user.getId());

        if(listOfEntitlements.contains(entitlementToBeAdded)){

            throw new ConflictException("The assessment has already been shared with the user.");
        }

        keycloakAdminService.addEntitlementsToUser(user.getId(), ShareableEntityType.ASSESSMENT.getValue().concat(ENTITLEMENTS_DELIMITER).concat(assessmentId));
    }

    /**
     * Retrieves a list of users to whom the specified assessment has been shared by the current user.
     *
     * @param assessmentId The unique identifier of the assessment.
     * @return A list of {@code UserProfileDto} objects representing the users who have been granted access to the shared assessment.
     **/
    @ShareableEntity(type= ShareableEntityType.ASSESSMENT, id = String.class)
    public List<UserProfileDto> getSharedUsers(String assessmentId) {

        var ids = keycloakAdminService.getIdsOfSharedUsers(ShareableEntityType.ASSESSMENT.getValue().concat(ENTITLEMENTS_DELIMITER).concat(assessmentId));
        var dbUsers = userRepository.fetchUsers(ids);
        return UserMapper.INSTANCE.usersProfileToDto(dbUsers);
    }
}