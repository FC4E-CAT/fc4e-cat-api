package org.grnet.cat.api;

import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.grnet.cat.api.endpoints.ValidationsEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.UpdateValidationStatus;
import org.grnet.cat.dtos.ValidationRequest;
import org.grnet.cat.dtos.ValidationResponse;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.enums.ValidationStatus;
import org.grnet.cat.services.KeycloakAdminRoleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@QuarkusTest
@TestHTTPEndpoint(ValidationsEndpoint.class)
@QuarkusTestResource(DatabaseTestResource.class)
public class ValidationsEndpointTest extends KeycloakTest {

    @InjectMock
    KeycloakAdminRoleService keycloakAdminRoleService;

    @ConfigProperty(name = "api.cat.validations.approve.auto")
    boolean autoApprove;

//    @BeforeEach
//    public void cleanup() {
//        validationService.deleteAll();
//    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void validationRequestBodyIsEmpty() {
        var response = performValidationRequestNotValid(null, aliceToken, 400);

        assertEquals("The request body is empty.", response.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void validationRoleIsEmpty() {
        var request = createValidationRequest(null, "ROR", "Keimyung University", "https://ror.org/00tjv0s33", "pid_graph:B5CC396B");
        var response = performValidationRequestNotValid(request, aliceToken, 400);

        assertEquals("organisation_role may not be empty.", response.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void validationNameIsEmpty() {
        var request = createValidationRequest("Manager", "ROR", null, "https://ror.org/00tjv0s33", "pid_graph:B5CC396B");
        var response = performValidationRequestNotValid(request, aliceToken, 400);

        assertEquals("organisation_name may not be empty.", response.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void validationActorIsNotFound() {
        var request = createValidationRequest("Manager", "ROR", "Keimyung University", "https://ror.org/00tjv0s33", "non_existent_actor");
        var response = performValidationRequestNotValid(request, aliceToken, 404);

        assertEquals("There is no Registry Actor with the following id: non_existent_actor", response.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void validationOrgIdIsEmpty() {
        var request = createValidationRequest("Manager", "ROR", "Keimyung University", null, "pid_graph:B5CC396B");
        var response = performValidationRequestNotValid(request, aliceToken, 400);

        assertEquals("organisation_id may not be empty.", response.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void validationSourceIsEmpty() {
        var request = createValidationRequest("Manager", null, "Keimyung University", "https://ror.org/00tjv0s33", "pid_graph:B5CC396B");
        var response = performValidationRequestNotValid(request, aliceToken, 400);

        assertEquals("organisation_source may not be empty.", response.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void validationSourceIsNotValid() {
        var request = createValidationRequest("Manager", "NOT_VALID", "Keimyung University", "https://ror.org/00tjv0s33", "pid_graph:B5CC396B");
        var response = performValidationRequestNotValid(request, aliceToken, 400);

        assertEquals("The value NOT_VALID is not a valid organisation_source. Valid organisation_source values are: [RE3DATA, ROR, CUSTOM, EOSC]", response.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void validationSourceNotFound() {
        var request = createValidationRequest("Manager", "EOSC", "Keimyung University", "https://ror.org/00tjv0s33", "pid_graph:B5CC396B");
        var response = performValidationRequestNotValid(request, aliceToken, 501);

        assertEquals("Source EOSC is not supported.", response.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void validationActorIsEmpty() {
        var request = createValidationRequest("Manager", "ROR", "Keimyung University", "https://ror.org/00tjv0s33", null);
        var response = performValidationRequestNotValid(request, aliceToken, 400);

        assertEquals("registry_actor_id may not be empty.", response.message);
    }

//    @Test
//    @Execution(ExecutionMode.CONCURRENT)
//    public void validation() {
//        var request = createValidationRequest("Manager", "ROR", "Keimyung University", "https://ror.org/00tjv0s33", "pid_graph:B5CC396B");
//        var response = performValidationRequest(request, aliceToken);
//
//        assertEquals("Manager", (response).organisationRole);
//    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void validationAlreadyExists() {
        var request = createValidationRequest("Manager", "ROR", "Keimyung University", "https://ror.org/00tjv0s33", "pid_graph:B5CC396B");
        performValidationRequest(request, aliceToken);

        var response = performValidationRequestNotValid(request, aliceToken, 409);

        assertEquals("There is a promotion request for this user and organisation.", response.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void validationSourceNotSupported() {
        var request = createValidationRequest("Manager", "RE3DATA", "Keimyung University", "http://www.kmu.ac.kr/main.jsp", "pid_graph:B5CC396B");
        var response = performValidationRequestNotValid(request, aliceToken, 501);

        assertEquals("Source RE3DATA is not supported.", response.message);
    }
    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getValidation() {
        var request = createValidationRequest("Manager", "ROR", "Keimyung University", "https://ror.org/00tjv0s33", "pid_graph:20A7A125");
        var createdValidation = performValidationRequest(request, aliceToken);

        var validation = given()
                .auth()
                .oauth2(aliceToken)
                .contentType(ContentType.JSON)
                .get("/{id}", createdValidation.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(ValidationResponse.class);

        assertEquals(createdValidation.id, validation.id);
        assertEquals(createdValidation.organisationRole, validation.organisationRole);

        var error = given()
                .auth()
                .oauth2(bobToken)
                .contentType(ContentType.JSON)
                .get("/{id}", createdValidation.id)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("Not Permitted.", error.message);
    }

//    @Test
//    @Execution(ExecutionMode.CONCURRENT)
//    public void getValidationNotPermitted() {
//        var request = createValidationRequest("Manager", "ROR", "Keimyung University", "https://ror.org/00tjv0s33", "pid_graph:20A7A125");
//        var createdValidation = performValidationRequest(request, aliceToken);
//
//        var error = given()
//                .auth()
//                .oauth2(bobToken)
//                .contentType(ContentType.JSON)
//                .get("/{id}", createdValidation.id)
//                .then()
//                .assertThat()
//                .statusCode(403)
//                .extract()
//                .as(InformativeResponse.class);
//
//        assertEquals("Not Permitted.", error.message);
//    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void updateValidationRequestByAdmin() {

        var request = createValidationRequest("Manager", "ROR",  "National Infrastructures for Research and Technology -  GRNET S.A", "https://ror.org/05tcasm11", "pid_graph:0E00C332");
        var createdValidation = performValidationRequest(request, aliceToken);

        var updateRequest = createValidationRequest("Project Manager", "ROR", "NTUA", "https://ror.org/00tjv0s33", "pid_graph:0E00C332");
        var updatedValidation = given()
                .auth()
                .oauth2(adminToken)
                .basePath("/v1/admin/validations")
                .body(updateRequest)
                .contentType(ContentType.JSON)
                .put("/{id}", createdValidation.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(ValidationResponse.class);

        assertEquals("Project Manager", updatedValidation.organisationRole);
        assertEquals("NTUA", updatedValidation.organisationName);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void updateValidationRequestStatusByAdmin() {
        doNothing().when(keycloakAdminRoleService).assignRolesToUser(any(), any());

        var request = createValidationRequest("Manager Alice", "ROR", "Keimyung University", "https://ror.org/00tjv0s33", "pid_graph:0E00C332");
        var createdValidation = performValidationRequest(request, aliceToken);

        var updateStatus = new UpdateValidationStatus();
        updateStatus.status = "APPROVED";

        var response = given()
                .auth()
                .oauth2(adminToken)
                .basePath("/v1/admin/validations")
                .body(updateStatus)
                .contentType(ContentType.JSON)
                .put("/{id}/update-status", createdValidation.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(ValidationResponse.class);

        assertEquals("APPROVED", response.status);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void updateValidationRequestStatusToRejectedByAdmin() {
        doNothing().when(keycloakAdminRoleService).assignRolesToUser(any(), any());

        var request = createValidationRequest("Manager", "ROR", "Keimyung University", "https://ror.org/00tjv0s33", "pid_graph:1A718108");
        var createdValidation = performValidationRequest(request, aliceToken);

        var updateRequest = new UpdateValidationStatus();
        updateRequest.status = "REJECTED";
        updateRequest.rejectionReason = "Rejection reason provided.";

        var response = given()
                .auth()
                .oauth2(adminToken)
                .basePath("/v1/admin/validations")
                .body(updateRequest)
                .contentType(ContentType.JSON)
                .put("/{id}/update-status", createdValidation.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(ValidationResponse.class);

        assertEquals("REJECTED", response.status);
        assertEquals("Rejection reason provided.", response.rejectionReason);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void createValidationInGreek() {
        var request = createValidationRequest("Προϊστάμενος", "ROR", "Keimyung University", "https://ror.org/00tjv0s33", "pid_graph:7835EF43");
        var response = performValidationRequest(request, aliceToken);

        assertEquals("Προϊστάμενος", ((ValidationResponse) response).organisationRole);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void createValidationInPolish() {
        var request = createValidationRequest("Manager", "ROR", "Gdańskie Seminarium Duchowne", "https://ror.org/00tjv0s33", "pid_graph:E92B9B49");
        var response = performValidationRequest(request, aliceToken);

        assertEquals("Gdańskie Seminarium Duchowne", ((ValidationResponse) response).organisationName);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getValidationRequestByAdmin() {
        var request = createValidationRequest("Admin Manager", "ROR", "Keimyung University", "https://ror.org/00tjv0s33", "pid_graph:B5CC396B");
        var createdValidation = performValidationRequest(request, bobToken);

        var response = given()
                .auth()
                .oauth2(adminToken)
                .basePath("/v1/admin/validations")
                .contentType(ContentType.JSON)
                .get("/{id}", createdValidation.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(ValidationResponse.class);

        assertEquals(createdValidation.id, response.id);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getApprovedValidationRequestByAdmin() {

        autoApprove = false;

        var validation1 = createValidationRequest("Manager", "ROR", "Keimyung University", "https://ror.org/00tjv0s33", "pid_graph:234B60D8");
        performValidationRequest(validation1, aliceToken);

        var validation2 = createValidationRequest("Manager", "ROR", "NTUA", "https://ror.org/00tjv0s33", "pid_graph:D42428D7");
        var createdValidation = performValidationRequest(validation2, aliceToken);

        var updateStatus = new UpdateValidationStatus();
        updateStatus.status = "APPROVED";
        performUpdateValidationStatusRequest(String.valueOf(createdValidation.id), updateStatus, adminToken, 200);

        var approvedValidationsAfterUpdate = fetchValidationsWithStatus(ValidationStatus.APPROVED, adminToken);

        assertEquals(2, approvedValidationsAfterUpdate.getTotalElements());
        assertEquals(2, approvedValidationsAfterUpdate.getSizeOfPage());
    }



    private ValidationRequest createValidationRequest(String role, String source, String name, String id, String actorId) {
        var request = new ValidationRequest();
        request.organisationRole = role;
        request.organisationSource = source;
        request.organisationName = name;
        request.organisationId = id;
        request.registryActorId = actorId;
        return request;
    }

    private ValidationResponse performValidationRequest(ValidationRequest request, String token) {
        return given()
                .auth()
                .oauth2(token)
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(ValidationResponse.class);
    }

    private InformativeResponse performValidationRequestNotValid(ValidationRequest request, String token, int expectedStatus) {
        var requestSpecification = given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON);

        if (request != null) {
            requestSpecification.body(request);
        }

        return requestSpecification
                .post()
                .then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract()
                .as(InformativeResponse.class);
    }

    private ValidationResponse performUpdateValidationStatusRequest(String validationId, UpdateValidationStatus updateStatus, String token, int expectedStatus) {
        return given()
                .auth()
                .oauth2(token)
                .body(updateStatus)
                .contentType(ContentType.JSON)
                .basePath("/v1/admin/validations")
                .put("/{id}/update-status", validationId)
                .then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract()
                .as(ValidationResponse.class);
    }

    private PageResource fetchValidationsWithStatus(ValidationStatus status, String token) {
        return given()
                .auth()
                .oauth2(token)
                .basePath("/v1/admin/validations")
                .contentType(ContentType.JSON)
                .queryParam("status", status)
                .get()
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(PageResource.class);
    }

    private PageResource fetchValidations(String token) {
        return given()
                .auth()
                .oauth2(token)
                .basePath("/v1/admin/validations")
                .contentType(ContentType.JSON)
                .get()
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(PageResource.class);
    }
}