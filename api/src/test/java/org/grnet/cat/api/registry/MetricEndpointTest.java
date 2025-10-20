package org.grnet.cat.api.registry;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.registry.MetricEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.metric.MetricRequestDto;
import org.grnet.cat.dtos.registry.metric.MetricResponseDto;
import org.grnet.cat.dtos.registry.metric.MetricUpdateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.UUID;


import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestHTTPEndpoint(MetricEndpoint.class)
public class MetricEndpointTest extends KeycloakTest {

    @Test
    //@Execution(ExecutionMode.CONCURRENT)
    public void getMetricForbidden() {

        var metricId = "pid_graph:D8C4E63E";

        var errorResponse = given()
                .auth()
                .oauth2(aliceToken)
                .contentType(ContentType.JSON)
                .get("/{id}", metricId)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertNotNull(errorResponse);
        assertEquals("You do not have permission to access this resource.", errorResponse.message);
    }

    @Test
    //@Execution(ExecutionMode.CONCURRENT)
    public void getMetricNotFound() {

        var metricId = "pid_graph:NON_EXISTENT";

        var notFoundResponse = given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .get("/{id}", metricId)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertNotNull(notFoundResponse);
        assertEquals("There is no Metric with the following id: " + metricId, notFoundResponse.message);
    }

    @Test
    //@Execution(ExecutionMode.CONCURRENT)
    public void createMetric() {

        var request = createUniqueMetricRequest();
        var createdMetric = createMetric(request);

        assertNotNull(createdMetric.id);
        assertEquals(request.MTR, createdMetric.MTR);
        assertEquals(request.labelMetric, createdMetric.labelMetric);
        assertEquals(request.descrMetric, createdMetric.descrMetric);
        assertEquals(request.typeAlgorithmId, createdMetric.typeAlgorithmId);
        assertEquals(request.typeMetricId, createdMetric.typeMetricId);
    }

    @Test
    //@Execution(ExecutionMode.CONCURRENT)
    public void updateMetric() {

        var request = createUniqueMetricRequest();
        var createdMetric = createMetric(request);

        var updateRequest = new MetricUpdateDto();
        updateRequest.MTR = request.MTR + "-UPDATED";
        updateRequest.labelMetric = "Updated Performance Metric";
        updateRequest.descrMetric = "Updated description for performance metric.";
        updateRequest.urlMetric = "http://example.com/metric-updated";
        updateRequest.typeAlgorithmId = request.typeAlgorithmId;
        updateRequest.typeMetricId = request.typeMetricId;
        updateRequest.typeBenchmarkId = request.typeBenchmarkId;
        updateRequest.valueBenchmark = "3";


        var updatedMetric = updateMetric(createdMetric.id, updateRequest);

        assertNotNull(createdMetric.id);
        assertEquals(request.MTR + "-UPDATED", updatedMetric.MTR);
        assertEquals("Updated Performance Metric", updatedMetric.labelMetric);
        assertEquals("Updated description for performance metric.", updatedMetric.descrMetric);
        assertEquals(request.typeAlgorithmId, updatedMetric.typeAlgorithmId);
        assertEquals(request.typeMetricId, updatedMetric.typeMetricId);
    }

    @Test
    //@Execution(ExecutionMode.CONCURRENT)
    public void deleteMetric() {

        var request = createUniqueMetricRequest();
        var createdMetric = createMetric(request);

        var deleteResponse = deleteMetric(createdMetric.id);

        assertNotNull(deleteResponse);
        assertEquals("Metric has been successfully deleted.", deleteResponse.message);

        var notFoundMetric = given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .get("/{id}", createdMetric.id)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertNotNull(notFoundMetric);
        assertEquals("There is no Metric with the following id: " + createdMetric.id, notFoundMetric.message);
    }

    @Test
    //@Execution(ExecutionMode.CONCURRENT)
    public void createMetricWithExistingMTR() {

        var request = createUniqueMetricRequest();
        var createdMetric = createMetric(request);

        var duplicateRequest = new MetricRequestDto();
        duplicateRequest.MTR = request.MTR;
        duplicateRequest.labelMetric = "Duplicate Performance Metric";
        duplicateRequest.descrMetric = "This metric measures duplicate performance.";
        duplicateRequest.urlMetric = "http://example.com/metric-duplicate";
        duplicateRequest.typeAlgorithmId = "pid_graph:7A976659";
        duplicateRequest.typeMetricId = "pid_graph:03615660";
        duplicateRequest.typeBenchmarkId ="pid_graph:7085006F";
        duplicateRequest.valueBenchmark = "2";

        var conflictResponse = given()
                .auth()
                .oauth2(adminToken)
                .body(duplicateRequest)
                .contentType(ContentType.JSON)
                .post("/")
                .then()
                .assertThat()
                .statusCode(409)
                .extract()
                .as(InformativeResponse.class);

        assertNotNull(conflictResponse);
        assertEquals("The value '" + request.MTR + "' for field 'MTR' is not unique.", conflictResponse.message);

        deleteMetric(createdMetric.id);
    }

    @Test
    //@Execution(ExecutionMode.CONCURRENT)
    public void listMetrics() {

        var metrics = given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .get("/")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(PageResource.class);

        assertNotNull(metrics);
        assertNotNull(metrics.getContent());
    }

    private MetricRequestDto createUniqueMetricRequest() {
        var uniqueMTR = ("MTR" + UUID.randomUUID()).toUpperCase();
        var dto = new MetricRequestDto();
        dto.MTR = uniqueMTR;
        dto.labelMetric = "Performance Metric";
        dto.descrMetric = "This metric measures performance.";
        dto.urlMetric = "http://example.com/metric";
        dto.typeAlgorithmId = "pid_graph:7A976659";
        dto.typeMetricId = "pid_graph:03615660";
        dto.typeBenchmarkId = "pid_graph:7085006F";
        dto.valueBenchmark = "2";
        return dto;
    }

    private MetricResponseDto createMetric(MetricRequestDto request) {
        return given()
                .auth()
                .oauth2(adminToken)
                .body(request)
                .contentType(ContentType.JSON)
                .post("/")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MetricResponseDto.class);
    }


    private MetricResponseDto getMetric(String metricId, String token) {
        return given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .get("/{id}", metricId)
                .then()
                .extract()
                .as(MetricResponseDto.class);
    }

    private MetricResponseDto updateMetric(String metricId, MetricUpdateDto updateRequest) {
        return given()
                .auth()
                .oauth2(adminToken)
                .body(updateRequest)
                .contentType(ContentType.JSON)
                .put("/{id}", metricId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(MetricResponseDto.class);
    }

    private InformativeResponse deleteMetric(String metricId) {
        return given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .delete("/{id}", metricId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);
    }
}
