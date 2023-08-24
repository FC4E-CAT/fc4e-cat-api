package org.grnet.cat.api;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.endpoints.TemplatesEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.template.TemplateResponse;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(TemplatesEndpoint.class)
public class TemplatesEndpointTest extends KeycloakTest {

    @Test
    public void fetchTemplateByActorAndType() {

        register("validated");

        var response = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .get("/by-type/{type-id}/by-actor/{actor-id}", 1L, 6L)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(TemplateResponse.class);

        assertEquals(6L, response.actor.id);
        assertEquals(1L, response.type.id);
    }

    @Test
    public void fetchTemplateActorNotExists() {

        register("validated");

        var response = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .get("/by-type/{type-id}/by-actor/{actor-id}", 1L, 100L)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(404, response.code);
    }


    @Test
    public void fetchTemplateTypeNotExists() {

        register("validated");

        var response = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .get("/by-type/{type-id}/by-actor/{actor-id}", 2L, 6L)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(404, response.code);
    }

    @Test
    public void fetchTemplateNotExists() {

        register("validated");

        var response = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .get("/by-type/{type-id}/by-actor/{actor-id}", 1L, 2L)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("There is no Template.", response.message);
    }

    @Test
    public void createTemplateNotPermitted() {

        register("validated");

        var response = given()
                .auth()
                .oauth2(getAccessToken("validated"))
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
    public void fetchTemplate() {

        register("admin");

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .get("/{id}", 1L)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(TemplateResponse.class);

        assertEquals(6L, response.actor.id);
        assertEquals(1L, response.type.id);
    }
    @Test
    public void fetchTemplatesByActorNotExist() {

        register("validated");

        var response = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .get("/by-actor/{actor-id}", 100L)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(404, response.code);
    }

    @Test
    public void fetchTemplatesByActorForbiddenAccess() {

        register("alice");

        var response = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .get("/by-actor/{actor-id}", 6L)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(403, response.code);
    }
}