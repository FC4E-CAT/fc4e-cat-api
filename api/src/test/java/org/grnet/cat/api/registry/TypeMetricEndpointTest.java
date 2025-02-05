package org.grnet.cat.api.registry;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.registry.TypeMetricEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.registry.metric.TypeMetricResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(TypeMetricEndpoint.class)
public class TypeMetricEndpointTest extends KeycloakTest {

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getTypeMetricNotPermitted() {
        var error = getTypeMetricUnauthorized("pid_graph:03615660");
        assertEquals("You do not have permission to access this resource.", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getTypeMetric() {
        var response = getTypeMetric("pid_graph:03615660");
        assertNotNull(response);
        assertEquals("pid_graph:03615660", response.id);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getTypeMetricNotFound() {
        var error = getTypeMetricNotFound("notfound");
        assertEquals("There is no Type Metric with the following id: notfound", error.message);
    }

    private TypeMetricResponseDto getTypeMetric(String id) {
        return given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .get("/{id}", id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(TypeMetricResponseDto.class);
    }

    private InformativeResponse getTypeMetricUnauthorized(String id) {
        return given()
                .auth()
                .oauth2(aliceToken)
                .contentType(ContentType.JSON)
                .get("/{id}", id)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);
    }

    private InformativeResponse getTypeMetricNotFound(String id) {
        return given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .get("/{id}", id)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);
    }
}
