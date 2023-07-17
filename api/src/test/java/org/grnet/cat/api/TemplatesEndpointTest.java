package org.grnet.cat.api;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.grnet.cat.api.endpoints.TemplatesEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.TemplateDto;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(TemplatesEndpoint.class)
public class TemplatesEndpointTest extends KeycloakTest {

@Test
public  void fetchTemplate(){

    register("validated");
    var response = given()
            .auth()
            .oauth2(getAccessToken("validated"))
            .get("/types/{type-id}/by-actor/{actor-id}",1L,6L)
            .then()
            .assertThat()
            .statusCode(200)
            .extract()
            .as(TemplateDto.class);

    assertEquals(6L, response.actor.id);
    assertEquals(1L, response.type.id);
}


    @Test
    public  void fetchTemplateActorNotExists(){

        register("validated");
        var response = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .get("/types/{type-id}/by-actor/{actor-id}",1L,100L)                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(404,response.code );
        }


    @Test
    public  void fetchTemplateTypeNotExists(){

        register("validated");
        var response = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .get("/types/{type-id}/by-actor/{actor-id}",2L,6L)               .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(404,response.code );
    }

}