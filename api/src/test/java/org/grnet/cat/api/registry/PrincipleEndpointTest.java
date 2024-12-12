package org.grnet.cat.api.registry;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.registry.PrincipleEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.registry.principle.PrincipleRequestDto;
import org.grnet.cat.dtos.registry.principle.PrincipleResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(PrincipleEndpoint.class)
public class PrincipleEndpointTest extends KeycloakTest {

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void createPrinciple() {

        var principle = createUniquePrincipleRequest();
        var response = createPrinciple(principle);

        assertNotNull(response.id);
        assertEquals(principle.pri, response.pri);
        assertEquals(principle.label, response.label);
        assertEquals(principle.description, response.description);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void createPrincipleUnauthorized() {

        var principle = createUniquePrincipleRequest();
        var response = createPrincipleNotAuthorized(principle.pri);

        assertEquals("You do not have permission to access this resource.", response.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void updatePrinciple() {

        var principle = createUniquePrincipleRequest();
        var createdResponse = createPrinciple(principle);

        var principleUpdated = new PrincipleRequestDto();
        principleUpdated.pri = principle.pri + "-UPDATED";
        principleUpdated.label = "Principle 2";
        principleUpdated.description = "Principle description updated";

        var updatedResponse = updatePrinciple(createdResponse.id, principleUpdated);

        assertEquals(principleUpdated.pri, updatedResponse.pri);
        assertEquals(principleUpdated.label, updatedResponse.label);
        assertEquals(principleUpdated.description, updatedResponse.description);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void deletePrinciple() {

        var principle = createUniquePrincipleRequest();
        var createdResponse = createPrinciple(principle);

        var success = deletePrinciple(createdResponse.id);

        assertEquals(200, success.code);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void deletePrincipleNotExists() {

        var error = given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .delete("/{id}", -1)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("There is no Principle with the following id: -1", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getPrinciple() {

        var principle = createUniquePrincipleRequest();
        var createdResponse = createPrinciple(principle);

        var fetchedPrinciple = getPrinciple(createdResponse.id);

        assertNotNull(fetchedPrinciple);
        assertEquals(createdResponse.id, fetchedPrinciple.id);
        assertEquals(createdResponse.pri, fetchedPrinciple.pri);
        assertEquals(createdResponse.label, fetchedPrinciple.label);
        assertEquals(createdResponse.description, fetchedPrinciple.description);
    }

    private PrincipleRequestDto createUniquePrincipleRequest() {
        var uniquePri = ("PRI" + UUID.randomUUID()).toUpperCase();
        var principle = new PrincipleRequestDto();
        principle.pri = uniquePri;
        principle.label = "Principle " + uniquePri;
        principle.description = "Description for " + uniquePri;
        return principle;
    }

    private PrincipleResponseDto createPrinciple(PrincipleRequestDto principle) {
        return given()
                .auth()
                .oauth2(adminToken)
                .body(principle)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(PrincipleResponseDto.class);
    }

    private PrincipleResponseDto getPrinciple(String principleId) {
        return given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .get("/{id}", principleId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(PrincipleResponseDto.class);
    }

    private PrincipleResponseDto updatePrinciple(String principleId, PrincipleRequestDto principleUpdated) {
        return given()
                .auth()
                .oauth2(adminToken)
                .body(principleUpdated)
                .contentType(ContentType.JSON)
                .patch("/{id}", principleId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(PrincipleResponseDto.class);
    }

    private InformativeResponse deletePrinciple(String principleId) {
        return given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .delete("/{id}", principleId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);
    }

    private InformativeResponse createPrincipleNotAuthorized(String principleId) {
        return given()
                .auth()
                .oauth2(bobToken)
                .body(principleId)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);
    }
}
