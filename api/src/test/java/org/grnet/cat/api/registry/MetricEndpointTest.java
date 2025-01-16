package org.grnet.cat.api.registry;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.registry.MetricEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.registry.MetricDefinitionExtendedResponse;
import org.grnet.cat.dtos.pagination.PageResource;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestHTTPEndpoint(MetricEndpoint.class)
public class MetricEndpointTest extends KeycloakTest {

//    @Test
//    public void getMetricById() {
//        register("admin");
//
//        var metricId = "pid_graph:AE39C968";
//        var metric = given()
//                .auth()
//                .oauth2(getAccessToken("admin"))
//                .contentType(ContentType.JSON)
//                .get("/{id}", metricId)
//                .then()
//                .assertThat()
//                .statusCode(200)
//                .extract()
//                .as(MetricDefinitionExtendedResponse.class);
//
//        assertNotNull(metric);
//        assertEquals(metricId, metric.metricId);
//    }

    @Test
    public void getMetricByIdNotFound() {
        register("admin");

        var metricId = "pid_graph:NON_EXISTENT";
        var error = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .get("/{id}", metricId)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertNotNull(error);
        assertEquals("There is no Metric with the following id: " + metricId, error.message);
    }

    @Test
    public void getMetricByIdForbidden() {
        register("alice");

        var metricId = "pid_graph:AE39C968";
        var error = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .contentType(ContentType.JSON)
                .get("/{id}", metricId)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertNotNull(error);
        assertEquals("You do not have permission to access this resource.", error.message);
    }

    @Test
    public void listMetrics() {
        register("admin");

        var metrics = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .get()
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(PageResource.class);

        assertNotNull(metrics);
        assertNotNull(metrics.getContent());
    }
}
