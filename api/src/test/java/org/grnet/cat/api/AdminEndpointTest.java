package org.grnet.cat.api;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.endpoints.AdminEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.PrincipleRequestDto;
import org.grnet.cat.dtos.PrincipleResponseDto;
import org.grnet.cat.dtos.assessment.JsonAssessmentRequest;
import org.grnet.cat.dtos.assessment.UserJsonAssessmentResponse;
import org.grnet.cat.entities.Principle;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.grnet.cat.dtos.criteria.CriteriaRequestDto;
import org.grnet.cat.dtos.criteria.CriteriaResponseDto;
import org.grnet.cat.dtos.pagination.PageResource;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestHTTPEndpoint(AdminEndpoint.class)
public class AdminEndpointTest extends KeycloakTest {
    @Test
    public void createPrinciple() throws IOException {

        register("admin");

        var principle = new PrincipleRequestDto();
        principle.description = "principle description";
        principle.pri = "P1";
        principle.label = "Principle 1";
        principle.uuid = "869E5028";


        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(principle)
                .contentType(ContentType.JSON)
                .post("/principles")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(PrincipleResponseDto.class);

        assertEquals(response.pri, principle.pri);
    }

    @Test
    public void createPrincipleUnauthorized() throws IOException {

        register("bob");

        var principle = new PrincipleRequestDto();
        principle.description = "principle description";
        principle.pri = "P1";
        principle.label = "Principle 1";
        principle.uuid = "869E5028";


        var response = given()
                .auth()
                .oauth2(getAccessToken("bob"))
                .body(principle)
                .contentType(ContentType.JSON)
                .post("/principles")
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("You do not have permission to access this resource.", response.message);

    }

    @Test
    public void updatePrinciple() throws IOException {

        register("admin");

        var principle = new PrincipleRequestDto();
        principle.description = "principle description";
        principle.pri = "P1";
        principle.label = "Principle 1";
        principle.uuid = "869E5028";


        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(principle)
                .contentType(ContentType.JSON)
                .post("/principles")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(PrincipleResponseDto.class);

        var principleUpdated = new PrincipleRequestDto();
        principleUpdated.description = "principle description updated";
        principleUpdated.pri = "P2";
        principleUpdated.label = "Principle 2";
        principleUpdated.uuid = "869E5029";
        var responseUpdated = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(principleUpdated)
                .contentType(ContentType.JSON)
                .put("/principles/{id}", response.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(PrincipleResponseDto.class);

        assertEquals(responseUpdated.pri, principleUpdated.pri);
        assertEquals(responseUpdated.description, principleUpdated.description);
        assertEquals(responseUpdated.label, principleUpdated.label);
    }

    @Test
    public void deletePrinciple() throws IOException {

        register("admin");

        var principle = new PrincipleRequestDto();
        principle.description = "principle description";
        principle.pri = "P1";
        principle.label = "Principle 1";
        principle.uuid = "869E5028";


        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(principle)
                .contentType(ContentType.JSON)
                .post("/principles")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(PrincipleResponseDto.class);

        var success = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .delete("/principles/{id}", response.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);
    }

    @Test
    public void deletePrincipleNotExists() throws IOException {

        register("admin");
        var error = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .delete("/principles/{id}", -1)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);
        assertEquals(error.message, "Principle with id: -1 does not exist.");
    }


    @Test
    public void createCriteria() {

        register("admin");

        var request = new CriteriaRequestDto();
        request.uuid = "ASEA1C61";
        request.criteriaCode = "C100";
        request.label = "Minimum Operations";
        request.description = "Service providers SHOULD provide a common Application Programming Interface to interact with PIDs, supporting a minimum set of operations (create, resolve and modify PID and PID Kernel Information)";
        request.imperative = "Mandatory";

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post("/criterias")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(CriteriaResponseDto.class);

        assertNotNull(response.id);
        assertEquals(request.uuid, response.uuid);
        assertEquals(request.criteriaCode, response.criteriaCode);
        assertEquals(request.label, response.label);
        assertEquals(request.description, response.description);
        assertEquals(request.imperative, response.imperative);

        given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .delete("/criterias/{id}", response.id)
                .then()
                .assertThat()
                .statusCode(204);
    }


    @Test
    public void createCriteriaWithInvalidUUID() {

        register("admin");

        var request = new CriteriaRequestDto();
        request.uuid = "INVALID_UUID";
        request.criteriaCode = "C101";
        request.label = "Invalid UUID";
        request.description = "This should fail due to invalid UUID format.";
        request.imperative = "Mandatory";

        given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post("/criterias")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    public void createCriteriaWithNonUniqueUUID() {

        register("admin");

        var request1 = new CriteriaRequestDto();
        request1.uuid = "ASEA1C61";
        request1.criteriaCode = "C102";
        request1.label = "Unique UUID";
        request1.description = "This should succeed.";
        request1.imperative = "Mandatory";

        var response1 = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request1)
                .contentType(ContentType.JSON)
                .post("/criterias")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(CriteriaResponseDto.class);

        var request2 = new CriteriaRequestDto();
        request2.uuid = "ASEA1C61"; // Same UUID
        request2.criteriaCode = "C103";
        request2.label = "Non-Unique UUID";
        request2.description = "This should fail due to non-unique UUID.";
        request2.imperative = "Mandatory";

        given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request2)
                .contentType(ContentType.JSON)
                .post("/criterias")
                .then()
                .assertThat()
                .statusCode(409);

        // Clean up
        given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .delete("/criterias/{id}", response1.id)
                .then()
                .assertThat()
                .statusCode(204);
    }

    @Test
    public void createCriteriaWithNonUniqueCriteriaCode() {

        register("admin");

        var request1 = new CriteriaRequestDto();
        request1.uuid = "ASEA1C61";
        request1.criteriaCode = "C100";
        request1.label = "Unique UUID";
        request1.description = "This should succeed.";
        request1.imperative = "Mandatory";

        var response1 = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request1)
                .contentType(ContentType.JSON)
                .post("/criterias")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(CriteriaResponseDto.class);

        var request2 = new CriteriaRequestDto();
        request2.uuid = "ASEA1C62"; // Same UUID
        request2.criteriaCode = "C100";
        request2.label = "Non-Unique UUID";
        request2.description = "This should fail due to non-unique UUID.";
        request2.imperative = "Mandatory";

        given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request2)
                .contentType(ContentType.JSON)
                .post("/criterias")
                .then()
                .assertThat()
                .statusCode(409);

        // Clean up
        given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .delete("/criterias/{id}", response1.id)
                .then()
                .assertThat()
                .statusCode(204);
    }

    @Test
    public void getCriteria() {

        register("admin");

        var request = new CriteriaRequestDto();
        request.uuid = "ASEA1C61";
        request.criteriaCode = "C100";
        request.label = "Extended Operations";
        request.description = "Service providers MUST provide additional operations for PIDs.";
        request.imperative ="Optional";

        var createdResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post("/criterias")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(CriteriaResponseDto.class);

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .get("/criterias/{id}", createdResponse.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(CriteriaResponseDto.class);

        assertEquals(createdResponse.id, response.id);
        assertEquals(createdResponse.uuid, response.uuid);
        assertEquals(createdResponse.criteriaCode, response.criteriaCode);
        assertEquals(createdResponse.label, response.label);
        assertEquals(createdResponse.description, response.description);
        assertEquals(createdResponse.imperative, response.imperative);

        given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .delete("/criterias/{id}", response.id)
                .then()
                .assertThat()
                .statusCode(204);
    }

    @Test
    public void updateCriteria() {

        register("admin");

        var request = new CriteriaRequestDto();
        request.uuid = "ASEA1C61";
        request.criteriaCode = "C100";
        request.label = "Basic Operations";
        request.description = "Service providers SHOULD provide basic operations for PIDs.";
        request.imperative = "Mandatory";

        var createdResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post("/criterias")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(CriteriaResponseDto.class);

        request.imperative = "Optional";

        var updatedResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .put("/criterias/{id}", createdResponse.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(CriteriaResponseDto.class);

        assertEquals(request.imperative, updatedResponse.imperative);

        given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .delete("/criterias/{id}", updatedResponse.id)
                .then()
                .assertThat()
                .statusCode(204);
    }

    @Test
    public void deleteCriteria() {

        register("admin");

        var request = new CriteriaRequestDto();
        request.uuid = "ASEA1C61";
        request.criteriaCode = "C100";
        request.label = "Advanced Operations";
        request.description = "Service providers MUST provide advanced operations for PIDs.";
        request.imperative = "Mandatory";

        var createdResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post("/criterias")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(CriteriaResponseDto.class);

        given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .delete("/criterias/{id}", createdResponse.id)
                .then()
                .assertThat()
                .statusCode(204);

        given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .get("/criterias/{id}", createdResponse.id)
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void listCriteria() {

        register("admin");

        given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .get("/criterias")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(PageResource.class);
    }



}
