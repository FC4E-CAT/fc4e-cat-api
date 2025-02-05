package org.grnet.cat.api.registry;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.registry.MetricTestEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(MetricTestEndpoint.class)
public class MetricTestEndpointTest extends KeycloakTest {

    @Test
    public void getMetricTestNotPermitted() {

        var error = given()
                .auth()
                .oauth2(aliceToken)
                .contentType(ContentType.JSON)
                .get("/")
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("You do not have permission to access this resource.", error.message);
    }
}