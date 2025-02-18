package org.grnet.cat.api.registry;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.registry.MetricEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.registry.MetricDefinitionExtendedResponse;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.metric.MetricRequestDto;
import org.grnet.cat.dtos.registry.metric.MetricResponseDto;
import org.grnet.cat.dtos.registry.metric.MetricUpdateDto;
import org.grnet.cat.dtos.registry.MetricDefinitionExtendedResponse;
import org.grnet.cat.dtos.pagination.PageResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.UUID;

import java.util.List;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
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
    @Execution(ExecutionMode.CONCURRENT)
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
    @Execution(ExecutionMode.CONCURRENT)
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
    @Execution(ExecutionMode.CONCURRENT)
    public void createMetric() {

        var request = createUniqueMetricRequest();
        var createdMetric = createMetric(request);

        assertNotNull(createdMetric.id);
        assertEquals(request.MTR, createdMetric.MTR);
        assertEquals(request.labelMetric, createdMetric.labelMetric);
        assertEquals(request.descrMetric, createdMetric.descrMetric);
        assertEquals(request.urlMetric, createdMetric.urlMetric);
        assertEquals(request.typeAlgorithmId, createdMetric.typeAlgorithmId);
        assertEquals(request.typeMetricId, createdMetric.typeMetricId);
    }

//    @Test
//    @Execution(ExecutionMode.CONCURRENT)
//    public void getMetric() {
//
//        var request = createUniqueMetricRequest();
//        var createdMetric = createMetric(request);
//        var fetchedMetric = getMetric(createdMetric.id, adminToken);
//
//        assertNotNull(fetchedMetric);
//        assertEquals(createdMetric.id, fetchedMetric.metricId);
//        assertEquals("pid_graph:03615660", fetchedMetric.typeMetricId);
//        assertEquals(createdMetric.MTR, fetchedMetric.metricId);
//        assertEquals(createdMetric.labelMetric, fetchedMetric.metricLabel);
//        assertEquals(createdMetric.descrMetric, fetchedMetric.metricDescription);
//        assertEquals(createdMetric.typeAlgorithmId, fetchedMetric.typeAlgorithmId);
//    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
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

        var updatedMetric = updateMetric(createdMetric.id, updateRequest);

        assertEquals(updateRequest.MTR, updatedMetric.MTR);
        assertEquals(updateRequest.labelMetric, updatedMetric.labelMetric);
        assertEquals(updateRequest.descrMetric, updatedMetric.descrMetric);
        assertEquals(updateRequest.urlMetric, updatedMetric.urlMetric);
        assertEquals(updateRequest.typeAlgorithmId, updatedMetric.typeAlgorithmId);
        assertEquals(updateRequest.typeMetricId, updatedMetric.typeMetricId);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
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
    @Execution(ExecutionMode.CONCURRENT)
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

        var conflictResponse = given()
                .auth()
                .oauth2(adminToken)
                .body(duplicateRequest)
                .contentType(ContentType.JSON)
                .post("/")
                .then()
                .assertThat()
                .statusCode(409) // Assuming 409 Conflict for duplicate MTR
                .extract()
                .as(InformativeResponse.class);

        assertNotNull(conflictResponse);
        assertEquals("The value '" + request.MTR + "' for field 'MTR' is not unique.", conflictResponse.message);

        deleteMetric(createdMetric.id);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void listMetrics() {

        var metrics = given()
                .auth()
                .oauth2(adminToken)
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

    private MetricRequestDto createUniqueMetricRequest() {
        var uniqueMTR = ("MTR" + UUID.randomUUID()).toUpperCase();
        var dto = new MetricRequestDto();
        dto.MTR = uniqueMTR;
        dto.labelMetric = "Performance Metric";
        dto.descrMetric = "This metric measures performance.";
        dto.urlMetric = "http://example.com/metric";
        dto.typeAlgorithmId = "pid_graph:7A976659";
        dto.typeMetricId = "pid_graph:03615660";
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


    private MetricDefinitionExtendedResponse getMetric(String metricId, String token) {
        return given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .get("/{id}", metricId)
                .then()
                .extract()
                .as(MetricDefinitionExtendedResponse.class);
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
