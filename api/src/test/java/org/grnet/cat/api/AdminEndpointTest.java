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
}

