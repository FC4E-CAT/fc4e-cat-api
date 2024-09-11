package org.grnet.cat.api.registry;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.registry.TypeAlgorithmEndpoint;
import org.grnet.cat.api.endpoints.registry.TypeMetricEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.registry.metric.TypeMetricResponseDto;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(TypeMetricEndpoint.class)
public class TypeMetricEndpointTest extends KeycloakTest {

    @Test
    public void getTypeMetricNotPermitted() {
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
    public void getTypeMetric() {

        register("admin");

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .get("/{id}", "pid_graph:03615660")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(TypeMetricResponseDto.class);

        assertEquals(response.id, "pid_graph:03615660");
    }

    @Test
    public void getTypeMetricNotFound() {

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

        assertEquals("There is no Type Metric with the following id: notfound", error.message);
    }

}
