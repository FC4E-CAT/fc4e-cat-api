package org.grnet.cat.api.registry;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.registry.TestMethodEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.registry.test.TestMethodRequestDto;
import org.grnet.cat.dtos.registry.test.TestMethodResponseDto;
import org.grnet.cat.dtos.registry.test.TestMethodUpdateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(TestMethodEndpoint.class)
public class TestMethodEndpointTest extends KeycloakTest {

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getTestMethodNotPermitted() {
        var error = getTestMethodWithUnauthorized("pid_graph:03615660");
        assertEquals("You do not have permission to access this resource.", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void createTestMethod() {
        var request = createUniqueTestMethodRequest();
        var response = createTestMethod(request);

        assertNotNull(response.id);
        assertEquals(request.UUID, response.UUID);
        assertEquals(request.labelTestMethod, response.labelTestMethod);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getTestMethod() {
        var request = createUniqueTestMethodRequest();
        var createdResponse = createTestMethod(request);

        var response = getTestMethod(createdResponse.id);

        assertNotNull(response);
        assertEquals(createdResponse.id, response.id);
        assertEquals(createdResponse.UUID, response.UUID);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getTestMethodNotFound() {
        var error = getTestMethodNotFound("non_existent_id");
        assertEquals("There is no Test Method with the following id: non_existent_id", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void updateTestMethod() {
        var request = createUniqueTestMethodRequest();
        var createdResponse = createTestMethod(request);

        var updateRequest = new TestMethodUpdateDto();
        updateRequest.UUID = request.UUID + "-UPDATED";
        updateRequest.labelTestMethod = "Updated Test Method";
        updateRequest.descTestMethod = "Updated description";
        updateRequest.lodTypeValue = "pid_graph:string";
        updateRequest.lodTypeProcess = "pid_graph:manual";
        updateRequest.numParams = 2;
        updateRequest.requestFragment = "{\"name\": \"p2\", \"in\": \"path\", \"description\": \"Updated Q\", \"schema\": {\"type\": \"number\"}}";
        updateRequest.codeFragment = "function updatedExecTest(p2) { return p2; }";

        var updatedResponse = updateTestMethod(createdResponse.id, updateRequest);

        assertEquals(updateRequest.UUID, updatedResponse.UUID);
        assertEquals(updateRequest.labelTestMethod, updatedResponse.labelTestMethod);
        assertEquals(updateRequest.descTestMethod, updatedResponse.descTestMethod);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void deleteTestMethod() {
        var request = createUniqueTestMethodRequest();
        var createdResponse = createTestMethod(request);

        var success = deleteTestMethod(createdResponse.id);
        assertEquals(200, success.code);

        var error = getTestMethodNotFound(createdResponse.id);
        assertEquals("There is no Test Method with the following id: " + createdResponse.id, error.message);
    }

    private TestMethodRequestDto createUniqueTestMethodRequest() {
        var uniqueUUID = ("UUID" + UUID.randomUUID()).toUpperCase();
        var request = new TestMethodRequestDto();
        request.UUID = uniqueUUID;
        request.labelTestMethod = "String-Auto";
        request.descTestMethod = "PID Persistence - Service - Evidence";
        request.lodTypeValue = "pid_graph:number";
        request.lodTypeProcess = "pid_graph:auto";
        request.numParams = 1;
        request.requestFragment = "{\"name\": \"p1\", \"in\": \"path\", \"description\": \"Q1\", \"schema\": {\"type\": \"string\"}}";
        request.codeFragment = "function execTest(p1) { return p1; }";
        return request;
    }

    private TestMethodResponseDto createTestMethod(TestMethodRequestDto request) {
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
                .as(TestMethodResponseDto.class);
    }

    private TestMethodResponseDto getTestMethod(String id) {
        return given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .get("/{id}", id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(TestMethodResponseDto.class);
    }

    private InformativeResponse getTestMethodWithUnauthorized(String id) {
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

    private InformativeResponse getTestMethodNotFound(String id) {
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

    private TestMethodResponseDto updateTestMethod(String id, TestMethodUpdateDto updateRequest) {
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
                .as(TestMethodResponseDto.class);
    }

    private InformativeResponse deleteTestMethod(String id) {
        return given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .delete("/{id}", id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);
    }
}
