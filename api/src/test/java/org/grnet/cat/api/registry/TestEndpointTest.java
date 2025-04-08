package org.grnet.cat.api.registry;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.registry.TestEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.registry.test.TestRequestDto;
import org.grnet.cat.dtos.registry.test.TestResponseDto;
import org.grnet.cat.dtos.registry.test.TestUpdateDto;
import org.grnet.cat.dtos.AutomatedCheckRequest;
import org.grnet.cat.dtos.registry.test.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestHTTPEndpoint(TestEndpoint.class)
public class TestEndpointTest extends KeycloakTest {

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getTestNotPermitted() {
        var error = given()
                .auth()
                .oauth2(aliceToken)
                .contentType(ContentType.JSON)
                .get("/{id}", "pid_graph:D8C4E63E")
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertNotNull(error);
        assertEquals("You do not have permission to access this resource.", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void createTest() {
        var request = createUniqueTestRequest();
        var response = createTest(request);

        assertEquals(request.getTestRequest().TES, response.testResponse.TES);
        assertEquals(request.getTestRequest().labelTest, response.testResponse.labelTest);
        assertEquals(request.getTestRequest().descTest, response.testResponse.descTest);

    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getTest() {
        var request = createUniqueTestRequest();
        var createdTest = createTest(request);

        var fetchedTest = getTest(createdTest.testResponse.id);

        assertNotNull(fetchedTest);
        assertEquals(request.getTestRequest().TES, fetchedTest.testResponse.TES);
        assertEquals(request.getTestRequest().labelTest, fetchedTest.testResponse.labelTest);
        assertEquals(request.getTestRequest().descTest, fetchedTest.testResponse.descTest);
        assertEquals(request.getTestDefinitionRequest().testMethodId, fetchedTest.testDefinitionResponse.testMethodId);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getTestNotFound() {
        var error = given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .get("/{id}", "notfound")
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertNotNull(error);
        assertEquals("There is no Test with the following id: notfound", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void updateTest() {
        var request = createUniqueTestRequest();
        var createdTest = createTest(request);

        var updateRequest = new TestAndTestDefinitionUpdateRequest();
        var testUpdate = new TestUpdateDto();
        var testDefUpdate = new TestDefinitionUpdateDto();

        testUpdate.labelTest = "Updated Performance Test";
        testUpdate.descTest = "Updated description for performance test.";

        testDefUpdate.labelTestDefinition = "UPDATED Manual confirmation of user authentication required for access to sensitive metadata.\"";
        testDefUpdate.paramType = "UPDATED";

        updateRequest.setTestRequest(testUpdate);
        updateRequest.setTestDefinitionRequest(testDefUpdate);

        var updatedTest = updateTest(createdTest.testResponse.id, updateRequest);

        assertEquals(200, updatedTest.code);

    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void deleteTest() {
        var request = createUniqueTestRequest();
        var createdTest = createTest(request);

        var success = deleteTest(createdTest.testResponse.id);

        assertEquals(200, success.code);

        var error = given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .get("/{id}", createdTest.testResponse.id)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertNotNull(error);
        assertEquals("There is no Test with the following id: " + createdTest.testResponse.id, error.message);
    }

    private TestAndTestDefinitionRequest createUniqueTestRequest() {
        //var uniqueTES = ("TES" + UUID.randomUUID()).toUpperCase();

        var request = new TestAndTestDefinitionRequest();
        var test  = new TestRequestDto();
        var testDef =  new TestDefinitionRequestDto();

        test.TES = ("TES" + UUID.randomUUID()).toUpperCase();
        test.labelTest = "Performance Test";
        test.descTest = "This Test measures performance.";
        testDef.testMethodId = "pid_graph:B733A7D5";
        testDef.labelTestDefinition = "Manual confirmation of user authentication required for access to sensitive metadata.\"";
        testDef.paramType = "onscreen";
        testDef.testParams = "userAuth\"|\"evidence";
        testDef.testQuestion = "\"Does access to Sensitive PID Kernel Metadata require user authentication?\"|\"Provide evidence of this provision via a link to a specification, user guide, or API definition.\"";
        testDef.toolTip = "\"\\\"Users need to be authenticated and requisite permissions must apply for access to sensitive metadata\\\"|\\\"A document, web page, or publication describing provisions\\\"\"";

        request.setTestRequest(test);
        request.setTestDefinitionRequest(testDef);

        return request;
    }

    private TestAndTestDefinitionResponse createTest(TestAndTestDefinitionRequest request) {
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
                .as(TestAndTestDefinitionResponse.class);
    }

    private TestAndTestDefinitionResponse getTest(String testId) {
        return given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .get("/{id}", testId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(TestAndTestDefinitionResponse.class);
    }

    private InformativeResponse updateTest(String testId, TestAndTestDefinitionUpdateRequest updateRequest) {
        return given()
                .auth()
                .oauth2(adminToken)
                .body(updateRequest)
                .contentType(ContentType.JSON)
                .patch("/{id}", testId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);
    }

    private InformativeResponse deleteTest(String testId) {
        return given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .delete("/{id}", testId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);
    }
}