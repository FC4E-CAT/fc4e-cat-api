package org.grnet.cat.api.registry;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.registry.TestMethodEndpoint; // Assuming there is an endpoint for TestMethod
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.registry.test.TestMethodRequestDto;
import org.grnet.cat.dtos.registry.test.TestMethodResponseDto;
import org.grnet.cat.dtos.registry.test.TestMethodUpdateDto;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(TestMethodEndpoint.class)
public class TestMethodEndpointTest extends KeycloakTest {

    @Test
    public void getTestMethodNotPermitted() {

        register("alice");

        var error = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .contentType(ContentType.JSON)
                .get("/{id}", "pid_graph:03615660")
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("You do not have permission to access this resource.", error.message);
    }

    @Test
    public void createTestMethod() {

        register("admin");

        var request = new TestMethodRequestDto();
        request.UUID = "03615660";
        request.labelTestMethod = "String-Auto";
        request.descTestMethod = "PID Persistence - Service - Evidence";
        request.lodTypeValue = "pid_graph:number";
        request.lodTypeProcess = "pid_graph:auto";
        request.numParams = 1;
        request.requestFragment = "{\"name\": \"p1\", \"in\": \"path\", \"description\": \"q1\", \"schema\": {\"type\": \"string\"}}";
        request.codeFragment = "function execTest(p1) { return p1; }";

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
                .as(TestMethodResponseDto.class);

        assertEquals("03615660", response.UUID);
        assertEquals("String-Auto", response.labelTestMethod);
    }

    @Test
    public void getTestMethod() {

        register("admin");

        var createRequest = new TestMethodRequestDto();
        createRequest.UUID = "03615660";
        createRequest.labelTestMethod = "String-Auto";
        createRequest.descTestMethod = "PID Persistence - Service - Evidence";
        createRequest.lodTypeValue = "pid_graph:number";
        createRequest.lodTypeProcess = "pid_graph:auto";
        createRequest.numParams = 1;
        createRequest.requestFragment = "{\"name\": \"p1\", \"in\": \"path\", \"description\": \"q1\", \"schema\": {\"type\": \"string\"}}";
        createRequest.codeFragment = "function execTest(p1) { return p1; }";

        var createdTestMethod = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(createRequest)
                .contentType(ContentType.JSON)
                .post("/")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(TestMethodResponseDto.class);

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .get("/{id}", createdTestMethod.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(TestMethodResponseDto.class);

        assertEquals(response.UUID, "03615660");
    }

    @Test
    public void getTestMethodNotFound() {

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

        assertEquals("There is no Test Method with the following id: notfound", error.message);
    }

    @Test
    public void updateTestMethod() {

        register("admin");

        var createRequest = new TestMethodRequestDto();
        createRequest.UUID = "03615660";
        createRequest.labelTestMethod = "String-Auto";
        createRequest.descTestMethod = "PID Persistence - Service - Evidence";
        createRequest.lodTypeValue = "pid_graph:number";
        createRequest.lodTypeProcess = "pid_graph:auto";
        createRequest.numParams = 1;
        createRequest.requestFragment = "{\"name\": \"p1\", \"in\": \"path\", \"description\": \"q1\", \"schema\": {\"type\": \"string\"}}";
        createRequest.codeFragment = "function execTest(p1) { return p1; }";

        var createdTestMethod = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(createRequest)
                .contentType(ContentType.JSON)
                .post("/")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(TestMethodResponseDto.class);

        var updateRequest = new TestMethodUpdateDto();
        updateRequest.UUID = "03615660-updated";
        updateRequest.labelTestMethod = "String-Auto-Updated";
        updateRequest.descTestMethod = "Updated description";
        updateRequest.lodTypeValue = "pid_graph:string";
        updateRequest.lodTypeProcess = "pid_graph:manual";
        updateRequest.numParams = 1;
        updateRequest.requestFragment = "{\"name\": \"p2\", \"in\": \"path\", \"description\": \"q2\", \"schema\": {\"type\": \"number\"}}";
        updateRequest.codeFragment = "function execTest(p2) { return p2; }";

        var updatedResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(updateRequest)
                .contentType(ContentType.JSON)
                .put("/{id}", createdTestMethod.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(TestMethodResponseDto.class);

        assertEquals("03615660-updated", updatedResponse.UUID);
        assertEquals("String-Auto-Updated", updatedResponse.labelTestMethod);
        assertEquals("Updated description", updatedResponse.descTestMethod);
    }

    @Test
    public void deleteTestMethod() {

        register("admin");

        var createRequest = new TestMethodRequestDto();
        createRequest.UUID = "03615660";
        createRequest.labelTestMethod = "String-Auto";
        createRequest.descTestMethod = "PID Persistence - Service - Evidence";
        createRequest.lodTypeValue = "pid_graph:number";
        createRequest.lodTypeProcess = "pid_graph:auto";
        createRequest.numParams = 1;
        createRequest.requestFragment = "{\"name\": \"p1\", \"in\": \"path\", \"description\": \"q1\", \"schema\": {\"type\": \"string\"}}";
        createRequest.codeFragment = "function execTest(p1) { return p1; }";

        var createdTestMethod = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(createRequest)
                .contentType(ContentType.JSON)
                .post("/")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(TestMethodResponseDto.class);

        given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .delete("/{id}", createdTestMethod.id)
                .then()
                .assertThat()
                .statusCode(200);

        given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .get("/{id}", createdTestMethod.id)
                .then()
                .assertThat()
                .statusCode(404);
    }
}
