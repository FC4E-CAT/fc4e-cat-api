package org.grnet.cat.services.assessment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pivovarit.function.ThrowingFunction;
import io.quarkus.hibernate.orm.panache.Panache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriInfo;
import lombok.SneakyThrows;
import org.grnet.cat.dtos.UserProfileDto;
import org.grnet.cat.dtos.assessment.AdminPartialJsonAssessmentResponse;
import org.grnet.cat.dtos.assessment.UserPartialJsonAssessmentResponse;
import org.grnet.cat.dtos.assessment.registry.JsonRegistryAssessmentRequest;
import org.grnet.cat.dtos.assessment.registry.UserJsonRegistryAssessmentResponse;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.subject.SubjectRequest;
import org.grnet.cat.dtos.template.TemplateAssessmentTypeDto;
import org.grnet.cat.dtos.template.TemplateSubjectDto;
import org.grnet.cat.entities.MotivationAssessment;
import org.grnet.cat.entities.registry.Motivation;
import org.grnet.cat.enums.MailType;
import org.grnet.cat.enums.ShareableEntityType;
import org.grnet.cat.enums.UserType;
import org.grnet.cat.enums.ValidationStatus;
import org.grnet.cat.exceptions.ConflictException;
import org.grnet.cat.mappers.AssessmentMapper;
import org.grnet.cat.mappers.UserMapper;
import org.grnet.cat.repositories.MotivationAssessmentRepository;
import org.grnet.cat.repositories.UserRepository;
import org.grnet.cat.repositories.ValidationRepository;
import org.grnet.cat.services.KeycloakAdminService;
import org.grnet.cat.services.MailerService;
import org.grnet.cat.services.SubjectService;
import org.grnet.cat.services.interceptors.ShareableEntity;
import org.grnet.cat.utils.Utility;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.grnet.cat.services.KeycloakAdminService.ENTITLEMENTS_DELIMITER;

/**
 * The AssessmentService provides operations for managing assessments expressed by a JSON object.
 */
@ApplicationScoped
@Named("json-assessment-service")
public class JsonAssessmentService {

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

    @Inject
    MailerService mailerService;

    @Inject
    MotivationAssessmentRepository motivationAssessmentRepository;

    @Transactional
    @SneakyThrows
    public UserJsonRegistryAssessmentResponse createV2Assessment(String userId, JsonRegistryAssessmentRequest request) {

        var validation = validationRepository.fetchValidationByUserAndRegistryActorAndOrganisation(userId, request.assessmentDoc.actor.getId(), request.assessmentDoc.organisation.id);

        if (!validation.getStatus().equals(ValidationStatus.APPROVED)) {

            throw new ForbiddenException("The validation request hasn't been approved yet!");
        }

        handleSubjectDatabaseId(userId, request.assessmentDoc.subject);

        //validateAssessmentAgainstTemplate(objectMapper.writeValueAsString(request.assessmentDoc), objectMapper.writeValueAsString(TemplateMapper.INSTANCE.templateToDto(template.get()).templateDoc));

        var timestamp = Timestamp.from(Instant.now());
        request.assessmentDoc.timestamp = timestamp.toString();
        request.assessmentDoc.organisation.name = validation.getOrganisationName();

        var assessment = new MotivationAssessment();
        assessment.setCreatedOn(timestamp);
        assessment.setValidation(validation);
        assessment.setMotivation(Panache.getEntityManager().getReference(Motivation.class, request.assessmentDoc.motivation.getId()));
        assessment.setSubject(subjectService.getSubjectById(request.assessmentDoc.subject.dbId));
        assessment.setShared(Boolean.FALSE);
        assessment.setPublished(request.assessmentDoc.published);
        assessment.setAssessmentDoc(objectMapper.writeValueAsString(request.assessmentDoc));
        motivationAssessmentRepository.persist(assessment);
        assessment.setParentAssessmentId(assessment.getId());

        var doc = assessment.getAssessmentDoc();
        ObjectNode jsonNode = null;
        try {
            jsonNode = (ObjectNode) objectMapper.readTree(doc);

            jsonNode.put("id", assessment.getId());
            jsonNode.put("version", "v1");
            jsonNode.put("automated_group_test", objectMapper.valueToTree(request.assessmentDoc.automatedGroupTest));
            sortTestsInAssessmentDoc(jsonNode);
            assessment.setAssessmentDoc(objectMapper.writeValueAsString(jsonNode));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        //assessment.setAssessmentDoc(objectMapper.writeValueAsString(request.assessmentDoc));

        keycloakAdminService.addEntitlementsToUser(userId, ShareableEntityType.ASSESSMENT.getValue().concat(ENTITLEMENTS_DELIMITER).concat(assessment.getId()));
        return AssessmentMapper.INSTANCE.userRegistryAssessmentToJsonAssessment(assessment);
    }

    @Transactional
    public UserJsonRegistryAssessmentResponse versionPublishedAssessment(String userId, String assessmentId) {
        var existing = motivationAssessmentRepository.findById(assessmentId);
        if (existing == null) {
            throw new NotFoundException("Assessment not found");
        }

        // Determine the root (original) assessment
        var parentId = existing.getParentAssessmentId() != null ?
                existing.getParentAssessmentId() : existing.getId();

        // Count previous versions
        var countVersions = motivationAssessmentRepository.getAllVersions(parentId).size();
        int nextVersion = countVersions + 1;

        // Parse & update document
        ObjectNode docNode;
        try {
            docNode = (ObjectNode) objectMapper.readTree(existing.getAssessmentDoc());
            docNode.put("version", "v" + nextVersion);
            docNode.put("timestamp", Instant.now().toString());
            docNode.put("published", false);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid assessmentDoc", e);
        }

        // Clean answers
        var cleanedDoc = cleanAssessmentAnswers(docNode);

        // Create versioned assessment
        var newAssessment = new MotivationAssessment();
        newAssessment.setValidation(existing.getValidation());
        newAssessment.setMotivation(existing.getMotivation());
        newAssessment.setSubject(existing.getSubject());
        newAssessment.setCreatedOn(Timestamp.from(Instant.now()));
        newAssessment.setParentAssessmentId(parentId);
        newAssessment.setAssessmentDoc(cleanedDoc.toString());
        newAssessment.setShared(false);
        newAssessment.setPublished(false);


        motivationAssessmentRepository.persist(newAssessment);

        try {
            var docWithId = (ObjectNode) objectMapper.readTree(newAssessment.getAssessmentDoc());
            docWithId.put("id", newAssessment.getId());
            sortTestsInAssessmentDoc(docNode);
            newAssessment.setAssessmentDoc(objectMapper.writeValueAsString(docWithId));
        } catch (Exception e) {
        }

        keycloakAdminService.addEntitlementsToUser(userId,
                ShareableEntityType.ASSESSMENT.getValue() + "|" + newAssessment.getId());

        return AssessmentMapper.INSTANCE.userRegistryAssessmentToJsonAssessment(newAssessment);
    }

    private JsonNode cleanAssessmentAnswers(JsonNode originalDoc) {
        var copy = originalDoc.deepCopy();
        ((ObjectNode) copy.path("result")).putNull("compliance");
        ((ObjectNode) copy.path("result")).putNull("ranking");

        for (var principle : copy.path("principles")) {
            for (var criterion : principle.path("criteria")) {
                var metric = (ObjectNode) criterion.path("metric");
                if (metric != null) {
                    metric.putNull("value");
                    metric.putNull("result");

                    for (var test : metric.path("tests")) {
                        ((ObjectNode) test).putNull("value");
                        ((ObjectNode) test).putNull("result");
                        ((ObjectNode) test).set("evidence_url", objectMapper.createArrayNode());
                    }
                }
            }
        }
        return copy;
    }

    /**
     * The API should provide flexibility for clients to either use an existing Subject by specifying its id in the db_id property
     * or create a new one by leaving db_id empty and filling in the other three properties (name, type and id).
     * The API should also give precedence to the properties stored in the database. If a valid db_id is provided,
     * its associated Subject should be retrieved, and the values of name, type, and id in the Assessment should be overwritten
     * with the corresponding values from the existing  database Subject.
     */
    public void handleSubjectDatabaseId(String userId, TemplateSubjectDto subject) {

        if (Objects.isNull(subject.dbId)) {

            var optional = subjectService
                    .getSubjectByNameAndTypeAndSubjectId(subject.name, subject.type, subject.id, userId);

            if (optional.isEmpty()) {

                var newSubject = new SubjectRequest();
                newSubject.name = subject.name;
                newSubject.type = subject.type;
                newSubject.id = subject.id;

                var response = subjectService.createSubject(newSubject, userId);
                subject.dbId = response.id;
            } else {

                subject.dbId = optional.get().getId();
            }
        } else {

            var dbSubject = subjectService.getSubjectById(subject.dbId);

            if (!dbSubject.getCreatedBy().equals(userId)) {
                throw new ForbiddenException("User not authorized to manage subject with ID " + subject.dbId);
            }

            subject.name = dbSubject.getName();
            subject.type = dbSubject.getType();
            subject.id = dbSubject.getSubjectId();
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

                        if (!actualEntry.getKey().equals("description")) {

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
     * Retrieves a specific registry assessment if it belongs to the user.
     *
     * @param assessmentId The ID of the assessment to retrieve.
     * @return The assessment if it belongs to the user.
     * @throws ForbiddenException If the user is not authorized to access the assessment.
     */
    @ShareableEntity(type = ShareableEntityType.ASSESSMENT, id = String.class)
    public UserJsonRegistryAssessmentResponse getRegistryDtoAssessmentIfBelongsOrSharedToUser(String assessmentId) {

        return getRegistryDtoAssessment(assessmentId);
    }

    public UserJsonRegistryAssessmentResponse getRegistryDtoAssessment(String assessmentId) {

        var assessment = motivationAssessmentRepository.findById(assessmentId);

        try {
            ObjectNode doc = (ObjectNode) objectMapper.readTree(assessment.getAssessmentDoc());
            sortTestsInAssessmentDoc(doc); // Sort tests inside the assessment JSON
            assessment.setAssessmentDoc(objectMapper.writeValueAsString(doc));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to sort tests in assessmentDoc", e);
        }

        return AssessmentMapper.INSTANCE.userRegistryAssessmentToJsonAssessment(assessment);
    }

    /**
     * Deletes a registry assessment if it is not published and belongs to the authenticated user.
     *
     * @param assessmentId The ID of the assessment to be deleted.
     * @throws ForbiddenException If the user does not have permission to delete this assessment (e.g., it's published or doesn't belong to them).
     */
    @ShareableEntity(type = ShareableEntityType.ASSESSMENT, id = String.class)
    @Transactional
    public void deleteRegistryAssessmentBelongsToUser(String assessmentId) {

        delete(assessmentId);
    }

    @Transactional
    public void deleteAll() {

        motivationAssessmentRepository.deleteAll();
    }

    /**
     * Updates the Assessment's json document. This method also handles the subjects that were inserted in the assessment document.
     *
     * @param id      The ID of the assessment whose json doc is being updated.
     * @param request The update request.
     * @return The updated assessment
     */
    @SneakyThrows
    @ShareableEntity(type = ShareableEntityType.ASSESSMENT, id = String.class)
    @Transactional
    public UserJsonRegistryAssessmentResponse updatePrivateAssessment(String id, JsonRegistryAssessmentRequest request) {

        return update(id, request);
    }

    @SneakyThrows
    @Transactional
    public UserJsonRegistryAssessmentResponse update(String id, JsonRegistryAssessmentRequest request) {

        var dbAssessment = motivationAssessmentRepository.findById(id);

        var dbAssessmentToJson = AssessmentMapper.INSTANCE.userRegistryAssessmentToJsonAssessment(dbAssessment);

        request.assessmentDoc.id = dbAssessment.getId();
        request.assessmentDoc.version = dbAssessmentToJson.assessmentDoc.version;
        request.assessmentDoc.status = dbAssessmentToJson.assessmentDoc.status;
        //  request.assessmentDoc.actor = dbAssessmentToJson.assessmentDoc.actor; ---please remove the comment
        request.assessmentDoc.subject = dbAssessmentToJson.assessmentDoc.subject;
        request.assessmentDoc.organisation = dbAssessmentToJson.assessmentDoc.organisation;
        request.assessmentDoc.timestamp = dbAssessmentToJson.assessmentDoc.timestamp;
        // request.assessmentDoc.published = dbAssessmentToJson.assessmentDoc.published;
        dbAssessment.setPublished(request.assessmentDoc.published);
        dbAssessment.setUpdatedBy(utility.getUserUniqueIdentifier());
        dbAssessment.setUpdatedOn(Timestamp.from(Instant.now()));
        dbAssessment.setAssessmentDoc(objectMapper.writeValueAsString(request.assessmentDoc));

        return AssessmentMapper.INSTANCE.userRegistryAssessmentToJsonAssessment(dbAssessment);
    }

    /**
     * Retrieves a page of registry assessments submitted by the specified user.
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
    public PageResource<UserPartialJsonAssessmentResponse> getDtoRegistryAssessmentsByUserAndPage(int page, int size, UriInfo uriInfo, String userID, String subjectName, String subjectType, String actorId) {

        var sharableIds = keycloakAdminService.getUserEntitlements(userID).stream()
                .map(ent -> keycloakAdminService.getLastPartOfEntitlement(ent, ENTITLEMENTS_DELIMITER))
                .collect(Collectors.toList());

        var assessments = motivationAssessmentRepository.fetchRegistryAssessmentsByUserAndPage(
                page, size, userID, subjectName, subjectType, actorId, sharableIds);

        var grouped = assessments.list().stream()
                .collect(Collectors.groupingBy(MotivationAssessment::getParentAssessmentId));

        var latestList = new ArrayList<MotivationAssessment>();
        var latestToPrevious = new HashMap<String, List<MotivationAssessment>>();

        for (var entry : grouped.entrySet()) {
            var versions = entry.getValue().stream()
                    .sorted(Comparator.comparing(MotivationAssessment::getCreatedOn).reversed())
                    .collect(Collectors.toList());

            var latest = versions.get(0);
            latestList.add(latest);

            var previousVersions = versions.subList(1, versions.size());
            latestToPrevious.put(latest.getId(), previousVersions);
        }

        var fullAssessments = latestList.stream()
                .map(latest -> {
                    var dto = AssessmentMapper.INSTANCE.userRegistryAssessmentToJsonAssessment(latest);

                    var prevVersions = latestToPrevious.getOrDefault(latest.getId(), List.of()).stream()
                            .map(AssessmentMapper.INSTANCE::userRegistryAssessmentToJsonAssessment)
                            .collect(Collectors.toList());

                    dto.setUserVersions(prevVersions);
                    return AssessmentMapper.INSTANCE.userRegistryAssessmentToPartialJsonAssessment(dto);
                })
                .collect(Collectors.toList());

        return new PageResource<>(assessments, fullAssessments, uriInfo);
    }

    /**
     * Retrieves a page of published assessments categorized by motivation and actor, created by all users.
     *
     * @param page         The index of the page to retrieve (starting from 0).
     * @param size         The maximum number of assessments to include in a page.
     * @param uriInfo      The Uri Info.
     * @param motivationId The Motivation.
     * @param actorId      The Actor's id.
     * @param subjectName  Subject name to search for.
     * @param subjectType  Subject Type to search for.
     * @return A list of PartialJsonAssessmentResponse objects representing the submitted assessments in the requested page.
     */
    public PageResource<AdminPartialJsonAssessmentResponse> getPublishedAssessmentsByMotivationAndActorAndPage(
            int page, int size, String motivationId, String actorId, UriInfo uriInfo,
            String subjectName, String subjectType) {

        var assessments = motivationAssessmentRepository.fetchPublishedAssessmentsByMotivationAndActorAndPage(
                page, size, motivationId, actorId, subjectName, subjectType);

        var grouped = assessments.list().stream()
                .collect(Collectors.groupingBy(MotivationAssessment::getParentAssessmentId));

        var latestList = new ArrayList<MotivationAssessment>();
        var latestToPrevious = new HashMap<String, List<MotivationAssessment>>();

        for (var entry : grouped.entrySet()) {
            var versions = entry.getValue().stream()
                    .sorted(Comparator.comparing(MotivationAssessment::getCreatedOn).reversed())
                    .collect(Collectors.toList());

            var latest = versions.get(0);
            latestList.add(latest);

            var previousVersions = versions.subList(1, versions.size());
            latestToPrevious.put(latest.getId(), previousVersions);
        }

        var fullAssessments = latestList.stream()
                .map(latest -> {
                    var dto = AssessmentMapper.INSTANCE.adminRegistryAssessmentToJsonAssessment(latest);

                    var prevVersions = latestToPrevious.getOrDefault(latest.getId(), List.of()).stream()
                            .map(AssessmentMapper.INSTANCE::adminRegistryAssessmentToJsonAssessment)
                            .collect(Collectors.toList());

                    dto.setAdminVersions(prevVersions);
                    return AssessmentMapper.INSTANCE.adminRegistryAssessmentToPartialJsonAssessment(dto);
                })
                .collect(Collectors.toList());

        return new PageResource<>(assessments, fullAssessments, uriInfo);
    }

    /**
     * Retrieves a page of published assessments categorized by actor, created by all users.
     *
     * @param page         The index of the page to retrieve (starting from 0).
     * @param size         The maximum number of assessments to include in a page.
     * @param uriInfo      The Uri Info.
     * @param actorId      The Actor's id.
     * @param subjectName  Subject name to search for.
     * @param subjectType  Subject Type to search for.
     * @return A list of PartialJsonAssessmentResponse objects representing the submitted assessments in the requested page.
     */
    public PageResource<AdminPartialJsonAssessmentResponse> getPublishedAssessmentsByActorAndPage(int page, int size, String actorId, UriInfo uriInfo, String subjectName, String subjectType) {

        var assessments = motivationAssessmentRepository.fetchPublishedAssessmentsByActorAndPage(page, size, actorId, subjectName, subjectType);

        var grouped = assessments.list().stream()
                .collect(Collectors.groupingBy(MotivationAssessment::getParentAssessmentId));

        var latestList = new ArrayList<MotivationAssessment>();
        var latestToPrevious = new HashMap<String, List<MotivationAssessment>>();

        for (var entry : grouped.entrySet()) {
            var versions = entry.getValue().stream()
                    .sorted(Comparator.comparing(MotivationAssessment::getCreatedOn).reversed())
                    .collect(Collectors.toList());

            var latest = versions.get(0);
            latestList.add(latest);

            var previousVersions = versions.subList(1, versions.size());
            latestToPrevious.put(latest.getId(), previousVersions);
        }

        var fullAssessments = latestList.stream()
                .map(latest -> {
                    var dto = AssessmentMapper.INSTANCE.adminRegistryAssessmentToJsonAssessment(latest);

                    var prevVersions = latestToPrevious.getOrDefault(latest.getId(), List.of()).stream()
                            .map(AssessmentMapper.INSTANCE::adminRegistryAssessmentToJsonAssessment)
                            .collect(Collectors.toList());

                    dto.setAdminVersions(prevVersions);
                    return AssessmentMapper.INSTANCE.adminRegistryAssessmentToPartialJsonAssessment(dto);
                })
                .collect(Collectors.toList());

        return new PageResource<>(assessments, fullAssessments, uriInfo);
    }

    /**
     * Retrieves a page of public assessment objects by motivation and actor.
     *
     * @param page         The index of the page to retrieve (starting from 0).
     * @param size         The maximum number of assessment objects to include in a page.
     * @param uriInfo      The Uri Info.
     * @param motivationId The ID of the Motivation.
     * @param actorId      The Actor's id.
     * @return A list of TemplateSubjectDto objects representing the public assessment objects in the requested page.
     */
    public PageResource<TemplateSubjectDto> getPublishedAssessmentObjectsByMotivationAndActorAndPage(int page, int size, String motivationId, String actorId, UriInfo uriInfo) {

        var objects = motivationAssessmentRepository.fetchPublishedAssessmentObjectsByMotivationAndActorAndPage(page, size, motivationId, actorId);

        var jsonToObjects = objects
                .list()
                .stream()
                .map(ThrowingFunction.sneaky(json -> objectMapper.readValue(json, TemplateSubjectDto.class)))
                .collect(Collectors.toList());

        return new PageResource<>(objects, jsonToObjects, uriInfo);
    }

    @Transactional
    public void delete(String assessmentId) {

        motivationAssessmentRepository.delete(Panache.getEntityManager().getReference(MotivationAssessment.class, assessmentId));
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
    public PageResource<TemplateSubjectDto> getAssessmentsObjectsByUserAndActor(int page, int size, UriInfo uriInfo, String userID, String actorID) {

        var objects = motivationAssessmentRepository.fetchAssessmentsObjectsByUserAndActor(page, size, userID, actorID);

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
     * @param email        A user's email with whom the assessment will be shared.
     */
    @ShareableEntity(type = ShareableEntityType.ASSESSMENT, id = String.class)
    @Transactional
    public void shareAssessment(String assessmentId, String email) {

        var user = userRepository.fetchActiveUserByEmail(email).orElseThrow(() -> new NotFoundException("There is no user with email : " + email));

        var types = userRepository.findUserTypesById(user.getId());

        if (types.contains(UserType.Deny_Access)) {

            throw new ForbiddenException("You cannot share an assessment with a banned user.");
        } else if (!types.contains(UserType.Validated)) {

            throw new ForbiddenException("You cannot share an assessment with a user who has not been validated.");
        }

        var entitlementToBeAdded = ShareableEntityType.ASSESSMENT.getValue().concat(ENTITLEMENTS_DELIMITER).concat(assessmentId);

        var listOfEntitlements = keycloakAdminService.getUserEntitlements(user.getId());

        if (listOfEntitlements.contains(entitlementToBeAdded)) {

            throw new ConflictException("The assessment has already been shared with the user.");
        }

        keycloakAdminService.addEntitlementsToUser(user.getId(), ShareableEntityType.ASSESSMENT.getValue().concat(ENTITLEMENTS_DELIMITER).concat(assessmentId));

        var assessment = motivationAssessmentRepository.findById(assessmentId);
        var activeUser = userRepository.fetchActiveUserByEmail(email);
        var userName = activeUser.get().getName();
        assessment.setShared(true);

        mailerService.sendMails(assessment, userName, MailType.USER_ALERT_SHARED_ASSESSMENT, Collections.singletonList(email));
    }

    /**
     * Retrieves a list of users to whom the specified assessment has been shared by the current user.
     *
     * @param assessmentId The unique identifier of the assessment.
     * @return A list of {@code UserProfileDto} objects representing the users who have been granted access to the shared assessment.
     **/
    @ShareableEntity(type = ShareableEntityType.ASSESSMENT, id = String.class)
    public List<UserProfileDto> getSharedUsers(String assessmentId) {

        var ids = keycloakAdminService.getIdsOfSharedUsers(ShareableEntityType.ASSESSMENT.getValue().concat(ENTITLEMENTS_DELIMITER).concat(assessmentId));
        var dbUsers = userRepository.fetchUsers(ids);
        return UserMapper.INSTANCE.usersProfileToDto(dbUsers);
    }

    /**
     * Retrieves a specific public assessment.
     *
     * @param assessmentId The ID of the assessment to retrieve.
     * @return The assessment if it's public.
     */
    public UserJsonRegistryAssessmentResponse getPublicDtoRegistryAssessment(String assessmentId) {

        var assessment = motivationAssessmentRepository.findById(assessmentId);

        try {
            ObjectNode doc = (ObjectNode) objectMapper.readTree(assessment.getAssessmentDoc());
            sortTestsInAssessmentDoc(doc);
            assessment.setAssessmentDoc(objectMapper.writeValueAsString(doc));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to sort tests in assessmentDoc for assessment ID: " + assessment.getId(), e);
        }

        var assessmentDto = AssessmentMapper.INSTANCE.publicUserRegistryAssessmentToJsonAssessment(assessment);

        if (!assessment.getPublished()) {
            throw new ForbiddenException("Not Permitted.");
        }

        return assessmentDto;
    }

    /**
     * Retrieves a list of assessment objects.
     *
     * @return The list.
     */
    public PageResource<TemplateSubjectDto> getObjects(int page, int size, UriInfo uriInfo, String userID) {

        var objects = motivationAssessmentRepository.fetchAssessmentObjects(page, size, userID);

        var objectMapper = new ObjectMapper();

        var jsonToObjects = objects
                .list()
                .stream()
                .map(ThrowingFunction.sneaky(json -> objectMapper.readValue(json, TemplateSubjectDto.class)))
                .collect(Collectors.toList());

        return new PageResource<>(objects, jsonToObjects, uriInfo);
    }

    /**
     * Retrieve all assessments.
     *
     * @return List of all assessments
     */
    public PageResource<AdminPartialJsonAssessmentResponse> getAllAssessmentsByPage(int page, int size, String search, UriInfo uriInfo) {

        var assessments = motivationAssessmentRepository.fetchAllAssessmentsByPage(page, size, search);


        var grouped = assessments.list().stream()
                .collect(Collectors.groupingBy(MotivationAssessment::getParentAssessmentId));

        var latestList = new ArrayList<MotivationAssessment>();
        var latestToPrevious = new HashMap<String, List<MotivationAssessment>>();

        for (var entry : grouped.entrySet()) {
            var versions = entry.getValue().stream()
                    .sorted(Comparator.comparing(MotivationAssessment::getCreatedOn).reversed())
                    .collect(Collectors.toList());

            var latest = versions.get(0);
            latestList.add(latest);

            var previousVersions = versions.subList(1, versions.size());
            latestToPrevious.put(latest.getId(), previousVersions);
        }

        var fullAssessments = latestList.stream()
                .map(latest -> {
                    var dto = AssessmentMapper.INSTANCE.adminRegistryAssessmentToJsonAssessment(latest);

                    var prevVersions = latestToPrevious.getOrDefault(latest.getId(), List.of()).stream()
                            .map(AssessmentMapper.INSTANCE::adminRegistryAssessmentToJsonAssessment)
                            .collect(Collectors.toList());

                    dto.setAdminVersions(prevVersions);
                    return AssessmentMapper.INSTANCE.adminRegistryAssessmentToPartialJsonAssessment(dto);
                })
                .collect(Collectors.toList());

        return new PageResource<>(assessments, fullAssessments, uriInfo);
    }

    @SneakyThrows
    @ShareableEntity(type = ShareableEntityType.ASSESSMENT, id = String.class)
    @Transactional
    public String managePublishAssessment(String id, boolean publish) {
        var assessment = motivationAssessmentRepository.findById(id);
        var doc = assessment.getAssessmentDoc();
        ObjectNode jsonNode = null;
        try {
            jsonNode = (ObjectNode) objectMapper.readTree(doc);
            jsonNode.put("published", publish);

            assessment.setAssessmentDoc(objectMapper.writeValueAsString(jsonNode));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        assessment.setPublished(publish);
        return String.format("Assessment is %s successfully", publish ? "published" : "unpublished");

    }

    @Transactional
    public String managePublishAssessmentByAdmin(String id, boolean publish) {
        var assessment = motivationAssessmentRepository.findById(id);
        var doc = assessment.getAssessmentDoc();
        ObjectNode jsonNode = null;
        try {
            jsonNode = (ObjectNode) objectMapper.readTree(doc);
            jsonNode.put("published", publish);
            assessment.setAssessmentDoc(objectMapper.writeValueAsString(jsonNode));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        assessment.setPublished(publish);
        return String.format("Assessment is %s successfully", publish ? "published" : "unpublished");

    }


    private void sortTestsInAssessmentDoc(ObjectNode assessmentDoc) {
        var principles = (ArrayNode) assessmentDoc.get("principles");
        if (principles == null) return;

        for (JsonNode principleNode : principles) {
            var criteria = (ArrayNode) principleNode.get("criteria");
            if (criteria == null) continue;

            for (JsonNode criterionNode : criteria) {
                var metric = (ObjectNode) criterionNode.get("metric");
                if (metric == null) continue;

                var tests = (ArrayNode) metric.get("tests");
                if (tests == null) continue;

                List<JsonNode> sorted = new ArrayList<>();
                tests.forEach(sorted::add);

                sorted.sort(Comparator.comparing(n -> n.get("id").asText()));

                var sortedTests = metric.putArray("tests");
                sorted.forEach(sortedTests::add);
            }
        }
    }
    /**
     * Retrieves a page of assessment objects submitted by the specified user by the specified actor.
     *
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of assessment objects to include in a page.
     * @param uriInfo The Uri Info.
     * @param actorID The actor ID.
     * @return A list of TemplateAssessmetTypeDto objects representing the submitted assessment types in the requested page.
     */
    public PageResource<TemplateAssessmentTypeDto> getAssessmentsTypesByActor(int page, int size, UriInfo uriInfo, String actorID) {

        var types = motivationAssessmentRepository.fetchAssessmentsTypesByActor(page, size,  actorID,Boolean.TRUE);

        var jsonToTypes = types
                .list()
                .stream()
                .map(ThrowingFunction.sneaky(json -> objectMapper.readValue(json, TemplateAssessmentTypeDto.class)))
                .collect(Collectors.toList());

        return new PageResource<>(types, jsonToTypes, uriInfo);
    }

}