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

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(TestDefinitionEndpoint.class)
public class TestDefinitionEndpointTest extends KeycloakTest {

    @Test
    public void getTestDefinitionNotPermitted() {

        register("alice");

        var error = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .contentType(ContentType.JSON)
                .get("/{id}", "pid_graph:7B428EA4")
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("You do not have permission to access this resource.", error.message);
    }

    @Test
    public void createTestDefinition() {

        register("admin");

        var request = new TestDefinitionRequestDto();
        request.testMethodId = "pid_graph:03615660";
        request.labelTestDefinition = "Manual confirmation of user authentication required for access to sensitive metadata.";
        request.paramType = "onscreen";
        request.testParams = "userAuth|evidence";
        request.testQuestion = "Does access to Sensitive PID Kernel Metadata require user authentication?";
        request.toolTip = "A document, web page, or publication describing provisions";

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post("/")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(TestDefinitionResponseDto.class);

        assertEquals("Manual confirmation of user authentication required for access to sensitive metadata.", response.labelTestDefinition);
        assertEquals("onscreen", response.paramType);
    }

    @Test
    public void getTestDefinition() {

        register("admin");

        var createRequest = new TestDefinitionRequestDto();
        createRequest.testMethodId = "pid_graph:03615660";
        createRequest.labelTestDefinition = "Manual confirmation of user authentication required for access to sensitive metadata.";
        createRequest.paramType = "onscreen";
        createRequest.testParams = "userAuth|evidence";
        createRequest.testQuestion = "Does access to Sensitive PID Kernel Metadata require user authentication?";
        createRequest.toolTip = "A document, web page, or publication describing provisions";

        var createdTestDefinition = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(createRequest)
                .contentType(ContentType.JSON)
                .post("/")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(TestDefinitionResponseDto.class);

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .get("/{id}", createdTestDefinition.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(TestDefinitionResponseDto.class);

        assertEquals(response.labelTestDefinition , "Manual confirmation of user authentication required for access to sensitive metadata.");
    }

    @Test
    public void getTestDefinitionNotFound() {

        register("admin");

        var error = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .get("/{id}", "non_existent_id")
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("There is no Test Definition with the following id: non_existent_id", error.message);
    }

    @Test
    public void updateTestDefinition() {

        register("admin");

        var createRequest = new TestDefinitionRequestDto();
        createRequest.testMethodId = "pid_graph:03615660";
        createRequest.labelTestDefinition = "Manual confirmation of user authentication required for access to sensitive metadata.";
        createRequest.paramType = "onscreen";
        createRequest.testParams = "userAuth|evidence";
        createRequest.testQuestion = "Does access to Sensitive PID Kernel Metadata require user authentication?";
        createRequest.toolTip = "A document, web page, or publication describing provisions";

        var createdTestDefinition = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(createRequest)
                .contentType(ContentType.JSON)
                .post("/")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(TestDefinitionResponseDto.class);

        var updateRequest = new TestDefinitionUpdateDto();
        updateRequest.testMethodId = "pid_graph:03615660";
        updateRequest.labelTestDefinition = "Updated confirmation of user authentication";
        updateRequest.paramType = "onscreen";
        updateRequest.testParams = "userAuth|updated_evidence";
        updateRequest.testQuestion = "Updated question";
        updateRequest.toolTip = "Updated tooltip";

        var updatedResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(updateRequest)
                .contentType(ContentType.JSON)
                .put("/{id}", createdTestDefinition.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(TestDefinitionResponseDto.class);

        assertEquals("Updated confirmation of user authentication", updatedResponse.labelTestDefinition);
    }

    @Test
    public void deleteTestDefinition() {

        register("admin");

        var createRequest = new TestDefinitionRequestDto();
        createRequest.testMethodId = "pid_graph:03615660";
        createRequest.labelTestDefinition = "Manual confirmation of user authentication required for access to sensitive metadata.";
        createRequest.paramType = "onscreen";
        createRequest.testParams = "userAuth|evidence";
        createRequest.testQuestion = "Does access to Sensitive PID Kernel Metadata require user authentication?";
        createRequest.toolTip = "A document, web page, or publication describing provisions";

        var createdTestDefinition = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(createRequest)
                .contentType(ContentType.JSON)
                .post("/")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(TestDefinitionResponseDto.class);

        given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .delete("/{id}", createdTestDefinition.id)
                .then()
                .assertThat()
                .statusCode(200);

        given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .get("/{id}", createdTestDefinition.id)
                .then()
                .assertThat()
                .statusCode(404);
    }
}
