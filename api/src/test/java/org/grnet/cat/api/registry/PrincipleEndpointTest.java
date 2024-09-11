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

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(PrincipleEndpoint.class)
public class PrincipleEndpointTest extends KeycloakTest {

    @Test
    public void createPrinciple() {

        register("admin");

        var principle = new PrincipleRequestDto();
        principle.description = "principle description";
        principle.pri = "P30";
        principle.label = "Principle 1";

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(principle)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(PrincipleResponseDto.class);

        assertEquals(response.pri, principle.pri);
    }

    @Test
    public void createPrincipleUnauthorized() {

        register("bob");

        var principle = new PrincipleRequestDto();
        principle.description = "principle description";
        principle.pri = "P40";
        principle.label = "Principle 1";

        var response = given()
                .auth()
                .oauth2(getAccessToken("bob"))
                .body(principle)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("You do not have permission to access this resource.", response.message);

    }

    @Test
    public void updatePrinciple() {

        register("admin");

        var principle = new PrincipleRequestDto();
        principle.description = "principle description";
        principle.pri = "P50";
        principle.label = "Principle 1";

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(principle)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(PrincipleResponseDto.class);

        var principleUpdated = new PrincipleRequestDto();
        principleUpdated.description = "principle description updated";
        principleUpdated.pri = "P60";
        principleUpdated.label = "Principle 2";
        var responseUpdated = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(principleUpdated)
                .contentType(ContentType.JSON)
                .patch("/{id}", response.id)
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
    public void deletePrinciple()  {

        register("admin");

        var principle = new PrincipleRequestDto();
        principle.description = "principle description";
        principle.pri = "P70";
        principle.label = "Principle 1";

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(principle)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(PrincipleResponseDto.class);

        var success = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .delete("/{id}", response.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(200, success.code);
    }

    @Test
    public void deletePrincipleNotExists(){

        register("admin");
        var error = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .delete("/{id}", -1)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);
        assertEquals(error.message, "There is no Principle with the following id: -1");
    }
}
