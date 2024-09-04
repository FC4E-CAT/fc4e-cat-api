package org.grnet.cat.api.registry;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.registry.RegistryCodelistEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.registry.codelist.TypeCriterionResponse;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest
@TestHTTPEndpoint(RegistryCodelistEndpoint.class)
public class RegistryCodelistEndpointTest extends KeycloakTest {

    @Test
    public void getCodelistNotPermitted() {

        register("alice");

        var error = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .contentType(ContentType.JSON)
                .get("/type-criterion/{id}", "pid_graph:07CA8184")
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("You do not have permission to access this resource.", error.message);
    }

    @Test
    public void getTypeCriterion() {

        register("admin");

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .get("/type-criterion/{id}", "pid_graph:07CA8184")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(TypeCriterionResponse.class);

        assertEquals(response.id, "pid_graph:07CA8184");
    }
}
