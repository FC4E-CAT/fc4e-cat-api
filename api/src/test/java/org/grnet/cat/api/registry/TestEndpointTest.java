package org.grnet.cat.api.registry;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.registry.TestEndpoint;
import org.grnet.cat.dtos.AutomatedCheckResponse;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.AutomatedCheckRequest;
import org.grnet.cat.dtos.registry.test.*;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(TestEndpoint.class)
public class TestEndpointTest extends KeycloakTest {

    @Test
    public void getTestNotPermitted() {

        register("alice");

        var error = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .contentType(ContentType.JSON)
                .get("/{id}", "pid_graph:D8C4E63E")
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("You do not have permission to access this resource.", error.message);
    }

    @Test
    public void createTest() {

        register("admin");

        var createRequest = new TestAndTestDefinitionRequest();

        var testRequest = new TestRequestDto();
        testRequest.TES = "TCREATE";
        testRequest.labelTest =  "Performance Test";
        testRequest.descTest = "This Test measures performance.";

        var testDefinitionRequest = new TestDefinitionRequestDto();
        testDefinitionRequest.testMethodId = "pid_graph:B733A7D5";
        testDefinitionRequest.labelTestDefinition = "Performance Test Definition";
        testDefinitionRequest.paramType = "onscreen";
        testDefinitionRequest.testParams = "userAuth|evidence";
        testDefinitionRequest.testQuestion = "\"Does access to Sensitive PID Kernel Metadata require user authentication?\"|\"Provide evidence of this provision via a link to a specification, user guide, or API definition.\"";
        testDefinitionRequest.toolTip =  "\"Users need to be authenticated and requisite permissions must apply for access to sensitive metadata\"|\"A document, web page, or publication describing provisions\"";

        createRequest.setTestRequest(testRequest);
        createRequest.setTestDefinitionRequest(testDefinitionRequest);

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(createRequest)
                .contentType(ContentType.JSON)
                .post("/")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(TestAndTestDefinitionResponse.class);

        assertEquals("Performance Test", response.testResponse.labelTest);
        assertEquals("This Test measures performance.", response.testResponse.descTest);
    }

    @Test
    public void getTest() {

        register("admin");

        var createRequest = new TestAndTestDefinitionRequest();

        var testRequest = new TestRequestDto();
        testRequest.TES = "TGET";
        testRequest.labelTest =  "Performance Test";
        testRequest.descTest = "This Test measures performance.";

        var testDefinitionRequest = new TestDefinitionRequestDto();
        testDefinitionRequest.testMethodId = "pid_graph:B733A7D5";
        testDefinitionRequest.labelTestDefinition = "Performance Test Definition";
        testDefinitionRequest.paramType = "onscreen";
        testDefinitionRequest.testParams = "userAuth|evidence";
        testDefinitionRequest.testQuestion = "\"Does access to Sensitive PID Kernel Metadata require user authentication?\"|\"Provide evidence of this provision via a link to a specification, user guide, or API definition.\"";
        testDefinitionRequest.toolTip =  "\"Users need to be authenticated and requisite permissions must apply for access to sensitive metadata\"|\"A document, web page, or publication describing provisions\"";

        createRequest.setTestRequest(testRequest);
        createRequest.setTestDefinitionRequest(testDefinitionRequest);

        var createdTest = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(createRequest)
                .contentType(ContentType.JSON)
                .post("/")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(TestAndTestDefinitionResponse.class);

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .get("/{id}", createdTest.testResponse.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(TestAndTestDefinitionResponse.class);

        assertEquals(response.testResponse.TES, "TGET");
    }

    @Test
    public void getTestNotFound() {

        register("admin");

        var error = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .get("/{id}", "notfound")
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("There is no Test with the following id: notfound", error.message);
    }

    @Test
    public void updateTest() {

        register("admin");

        var createRequest = new TestAndTestDefinitionRequest();

        var testRequest = new TestRequestDto();
        testRequest.TES = "TCREATE1";
        testRequest.labelTest =  "Performance Test";
        testRequest.descTest = "This Test measures performance.";

        var testDefinitionRequest = new TestDefinitionRequestDto();
        testDefinitionRequest.testMethodId = "pid_graph:B733A7D5";
        testDefinitionRequest.labelTestDefinition = "Performance Test Definition";
        testDefinitionRequest.paramType = "onscreen";
        testDefinitionRequest.testParams = "userAuth|evidence";
        testDefinitionRequest.testQuestion = "\"Does access to Sensitive PID Kernel Metadata require user authentication?\"|\"Provide evidence of this provision via a link to a specification, user guide, or API definition.\"";
        testDefinitionRequest.toolTip =  "\"Users need to be authenticated and requisite permissions must apply for access to sensitive metadata\"|\"A document, web page, or publication describing provisions\"";

        createRequest.setTestRequest(testRequest);
        createRequest.setTestDefinitionRequest(testDefinitionRequest);

        var createdTest = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(createRequest)
                .contentType(ContentType.JSON)
                .post("/")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(TestAndTestDefinitionResponse.class);

        var updateRequest = new TestUpdateDto();
        updateRequest.TES = "TCREATE1-Updated";
        updateRequest.labelTest = "Updated Performance Test";
        updateRequest.descTest = "Updated description for performance test.";

        var updatedResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(updateRequest)
                .contentType(ContentType.JSON)
                .put("/{id}", createdTest.testResponse.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(TestResponseDto.class);

        assertEquals("TCREATE1-Updated", updatedResponse.TES);
        assertEquals("Updated Performance Test", updatedResponse.labelTest);
        assertEquals("Updated description for performance test.", updatedResponse.descTest);
    }

    @Test
    public void deleteTest() {

        register("admin");

        var createRequest = new TestAndTestDefinitionRequest();

        var testRequest = new TestRequestDto();
        testRequest.TES = "TDELETE";
        testRequest.labelTest =  "Performance Test";
        testRequest.descTest = "This Test measures performance.";

        var testDefinitionRequest = new TestDefinitionRequestDto();
        testDefinitionRequest.testMethodId = "pid_graph:B733A7D5";
        testDefinitionRequest.labelTestDefinition = "Performance Test Definition";
        testDefinitionRequest.paramType = "onscreen";
        testDefinitionRequest.testParams = "userAuth|evidence";
        testDefinitionRequest.testQuestion = "\"Does access to Sensitive PID Kernel Metadata require user authentication?\"|\"Provide evidence of this provision via a link to a specification, user guide, or API definition.\"";
        testDefinitionRequest.toolTip =  "\"Users need to be authenticated and requisite permissions must apply for access to sensitive metadata\"|\"A document, web page, or publication describing provisions\"";

        createRequest.setTestRequest(testRequest);
        createRequest.setTestDefinitionRequest(testDefinitionRequest);

        var createdTest = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(createRequest)
                .contentType(ContentType.JSON)
                .post("/")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(TestAndTestDefinitionResponse.class);

        given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .delete("/{id}", createdTest.testResponse.id)
                .then()
                .assertThat()
                .statusCode(200);

        given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .get("/{id}", createdTest.testResponse.id)
                .then()
                .assertThat()
                .statusCode(404);
    }

}