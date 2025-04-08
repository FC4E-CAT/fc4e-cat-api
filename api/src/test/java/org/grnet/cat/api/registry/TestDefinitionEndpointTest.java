package org.grnet.cat.api.registry;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.registry.TestDefinitionEndpoint; // Assumed you have an endpoint for TestDefinition
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.registry.test.TestDefinitionRequestDto;
import org.grnet.cat.dtos.registry.test.TestDefinitionResponseDto;
import org.grnet.cat.dtos.registry.test.TestDefinitionUpdateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

@QuarkusTest
@TestHTTPEndpoint(TestDefinitionEndpoint.class)
public class TestDefinitionEndpointTest extends KeycloakTest {

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getTestDefinitionNotPermitted() {
        var error = getTestDefinitionForbidden("pid_graph:7B428EA4");
        assertEquals("You do not have permission to access this resource.", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void createTestDefinition() {
        var request = createUniqueTestDefinitionRequest();

        var response = createTestDefinition(request);

        assertNotNull(response.id);
        assertEquals("Manual confirmation of user authentication required for access to sensitive metadata.", response.labelTestDefinition);
        assertEquals("onscreen", response.paramType);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getTestDefinition() {
        var request = createUniqueTestDefinitionRequest();
        var createdResponse = createTestDefinition(request);

        var response = getTestDefinition(createdResponse.id);

        assertEquals("Manual confirmation of user authentication required for access to sensitive metadata.", response.labelTestDefinition);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getTestDefinitionNotFound() {
        var error = getTestDefinitionNotFound("non_existent_id");
        assertEquals("There is no Test Definition with the following id: non_existent_id", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void updateTestDefinition() {
        var request = createUniqueTestDefinitionRequest();
        var createdResponse = createTestDefinition(request);

        var updateRequest = createTestDefinitionUpdateRequest(request);

        var updatedResponse = updateTestDefinition(createdResponse.id, updateRequest);

        assertEquals("Updated confirmation of user authentication", updatedResponse.labelTestDefinition);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void deleteTestDefinition() {
        var request = createUniqueTestDefinitionRequest();
        var createdResponse = createTestDefinition(request);

        var success = deleteTestDefinition(createdResponse.id);

        assertEquals(200, success.code);

        var error = getTestDefinitionNotFound(createdResponse.id);
        assertEquals("There is no Test Definition with the following id: " + createdResponse.id, error.message);
    }

    private TestDefinitionRequestDto createUniqueTestDefinitionRequest() {
        var request = new TestDefinitionRequestDto();
        request.testMethodId = "pid_graph:03615660";
        request.labelTestDefinition = "Manual confirmation of user authentication required for access to sensitive metadata.";
        request.paramType = "onscreen";
        request.testParams = "userAuth|evidence";
        request.testQuestion = "Does access to Sensitive PID Kernel Metadata require user authentication?";
        request.toolTip = "A document, web page, or publication describing provisions";
        return request;
    }

    private TestDefinitionUpdateDto createTestDefinitionUpdateRequest(TestDefinitionRequestDto originalRequest) {
        var updateRequest = new TestDefinitionUpdateDto();
        updateRequest.labelTestDefinition = "Updated confirmation of user authentication";
        updateRequest.paramType = "onscreen";
        updateRequest.testParams = "userAuth|updated_evidence";
        updateRequest.testQuestion = "Updated question";
        updateRequest.toolTip = "Updated tooltip";
        return updateRequest;
    }

    private TestDefinitionResponseDto createTestDefinition(TestDefinitionRequestDto request) {
        return given()
                .auth()
                .oauth2(adminToken)
                .body(request)
                .contentType(ContentType.JSON)
                .post("/")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(TestDefinitionResponseDto.class);
    }

    private TestDefinitionResponseDto getTestDefinition(String id) {
        return given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .get("/{id}", id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(TestDefinitionResponseDto.class);
    }

    private InformativeResponse getTestDefinitionForbidden(String id) {
        return given()
                .auth()
                .oauth2(aliceToken)
                .contentType(ContentType.JSON)
                .get("/{id}", id)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);
    }

    private InformativeResponse getTestDefinitionNotFound(String id) {
        return given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .get("/{id}", id)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);
    }

    private TestDefinitionResponseDto updateTestDefinition(String id, TestDefinitionUpdateDto updateRequest) {
        return given()
                .auth()
                .oauth2(adminToken)
                .body(updateRequest)
                .contentType(ContentType.JSON)
                .put("/{id}", id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(TestDefinitionResponseDto.class);
    }

    private InformativeResponse deleteTestDefinition(String testDefinitionId) {
        return given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .delete("/{id}", testDefinitionId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);
    }
}
