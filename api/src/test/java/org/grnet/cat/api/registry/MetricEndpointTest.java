package org.grnet.cat.api.registry;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.registry.MetricEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.registry.metric.*;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(MetricEndpoint.class)
public class MetricEndpointTest extends KeycloakTest {

    @Test
    public void getMetricNotPermitted() {

        register("alice");

        var error = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .contentType(ContentType.JSON)
                .get("/{id}", "pid_graph:D8C4E63E")
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("You do not have permission to access this resource.", error.message);
    }

    @Test
    public void metric() {

        register("admin");

        var request = new MetricRequestDto();
        request.MTR = "MTR001";
        request.labelMetric = "Performance Metric";
        request.descrMetric = "This metric measures performance.";
        request.urlMetric = "http://example.com/metric";
        request.typeAlgorithmId = "pid_graph:7A976659";
        request.typeMetricId = "pid_graph:03615660";


        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post("/")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MetricResponseDto.class);

        assertEquals("pid_graph:7A976659", response.typeAlgorithmId);
        assertEquals("pid_graph:03615660", response.typeMetricId);
    }

    @Test
    public void getMetric() {

        register("admin");

        var createRequest = new MetricRequestDto();
        createRequest.MTR = "MTR001";
        createRequest.labelMetric = "Performance Metric";
        createRequest.descrMetric = "This metric measures performance.";
        createRequest.urlMetric = "http://example.com/metric";
        createRequest.typeAlgorithmId = "pid_graph:7A976659";
        createRequest.typeMetricId = "pid_graph:03615660";

        var createdMetric = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(createRequest)
                .contentType(ContentType.JSON)
                .post("/")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MetricResponseDto.class);

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .get("/{id}", createdMetric.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(MetricResponseDto.class);

        assertEquals(response.typeMetricId, "pid_graph:03615660");
    }

    @Test
    public void getMetricNotFound() {

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

        assertEquals("There is no Metric with the following id: notfound", error.message);
    }

    @Test
    public void updateMetric() {

        register("admin");

        var createRequest = new MetricRequestDto();
        createRequest.MTR = "MTR001";
        createRequest.labelMetric = "Performance Metric";
        createRequest.descrMetric = "This metric measures performance.";
        createRequest.urlMetric = "http://example.com/metric";
        createRequest.typeAlgorithmId = "pid_graph:7A976659";
        createRequest.typeMetricId = "pid_graph:03615660";

        var createdMetric = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(createRequest)
                .contentType(ContentType.JSON)
                .post("/")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MetricResponseDto.class);

        var updateRequest = new MetricUpdateDto();
        updateRequest.MTR = "MTR001-Updated";
        updateRequest.labelMetric = "Updated Performance Metric";
        updateRequest.descrMetric = "Updated description for performance metric.";
        updateRequest.urlMetric = "http://example.com/metric-updated";
        updateRequest.typeAlgorithmId = "pid_graph:7A976659";
        updateRequest.typeMetricId = "pid_graph:03615660";

        var updatedResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(updateRequest)
                .contentType(ContentType.JSON)
                .put("/{id}", createdMetric.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(MetricResponseDto.class);

        assertEquals("MTR001-Updated", updatedResponse.MTR);
        assertEquals("Updated Performance Metric", updatedResponse.labelMetric);
        assertEquals("Updated description for performance metric.", updatedResponse.descrMetric);
    }

    @Test
    public void deleteMetric() {

        register("admin");

        var createRequest = new MetricRequestDto();
        createRequest.MTR = "MTR001";
        createRequest.labelMetric = "Performance Metric";
        createRequest.descrMetric = "This metric measures performance.";
        createRequest.urlMetric = "http://example.com/metric";
        createRequest.typeAlgorithmId = "pid_graph:7A976659";
        createRequest.typeMetricId = "pid_graph:03615660";

        var createdMetric = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(createRequest)
                .contentType(ContentType.JSON)
                .post("/")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MetricResponseDto.class);

        given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .delete("/{id}", createdMetric.id)
                .then()
                .assertThat()
                .statusCode(200);

        given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .get("/{id}", createdMetric.id)
                .then()
                .assertThat()
                .statusCode(404);
    }

}