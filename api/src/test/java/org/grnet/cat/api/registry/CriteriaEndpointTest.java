package org.grnet.cat.api.registry;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.registry.CriteriaEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.registry.criterion.CriterionRequest;
import org.grnet.cat.dtos.registry.criterion.CriterionResponse;
import org.grnet.cat.dtos.registry.criterion.CriterionUpdate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(CriteriaEndpoint.class)
public class CriteriaEndpointTest extends KeycloakTest {

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void createCriteria() {

        var request = createUniqueCriterionRequest();
        var response = createCriterion(request);

        assertNotNull(response.id);
        assertEquals(request.cri, response.cri);
        assertEquals(request.label, response.label);
        assertEquals(request.description, response.description);
        assertEquals(request.imperative, response.imperative);
        assertEquals(request.typeCriterion, response.typeCriterion);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void createCriteriaWithInvalidImperative() {

        var request = new CriterionRequest();
        request.cri = "C101";
        request.label = "Invalid imperative";
        request.description = "This should fail due to invalid UUID format.";
        request.imperative = "Invalid imperative";
        request.typeCriterion = "pid_graph:A2719B92";

        given()
                .auth()
                .oauth2(adminToken)
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void createCriteriaWithNonUniqueCriteriaCode() {

        var request1 = createUniqueCriterionRequest();
        createCriterion(request1);

        var request2 = new CriterionRequest();
        request2.cri = request1.cri;
        request2.label = "Unique UUID";
        request2.description = "This should fail due to non-unique criteria code.";
        request2.imperative = "pid_graph:BED209B9";
        request2.typeCriterion = "pid_graph:A2719B92";

        var response = createCriterionNotValid(request2,409);

        assertEquals("The value '" + request2.cri + "' for field 'cri' is not unique.", response.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getCriteria() {

        var request = createUniqueCriterionRequest();
        var createdResponse = createCriterion(request);

        var response = getCriterion(createdResponse.id);

        assertEquals(createdResponse.id, response.id);
        assertEquals(createdResponse.cri, response.cri);
        assertEquals(createdResponse.label, response.label);
        assertEquals(createdResponse.description, response.description);
        assertEquals(createdResponse.imperative, response.imperative);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void updateCriteria() {

        var request = createUniqueCriterionRequest();
        var createdResponse = createCriterion(request);

        var updateRequest = new CriterionUpdate();
        updateRequest.cri = request.cri + "-UPDATED";
        updateRequest.imperative = "pid_graph:2981F3DD";

        var updatedResponse = updateCriterion(createdResponse.id, updateRequest);

        assertEquals(updateRequest.imperative, updatedResponse.imperative);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void deleteCriteria() {

        var request = createUniqueCriterionRequest();
        var createdResponse = createCriterion(request);

        var success = deleteCriterion(createdResponse.id);

        assertEquals("Criterion has been successfully deleted.", success.message);
    }

    private CriterionRequest createUniqueCriterionRequest() {
        var uniqueCri = ("CRI" + UUID.randomUUID()).toUpperCase();
        var request = new CriterionRequest();
        request.cri = uniqueCri;
        request.label = "Performance Metric";
        request.description = "This metric measures performance.";
        request.imperative = "pid_graph:BED209B9";
        request.typeCriterion = "pid_graph:A2719B92";
        return request;
    }

    private CriterionResponse createCriterion(CriterionRequest request) {
        return given()
                .auth()
                .oauth2(adminToken)
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(CriterionResponse.class);
    }

    private CriterionResponse getCriterion(String criterionId) {
        return given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .get("/{id}", criterionId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(CriterionResponse.class);
    }

    private CriterionResponse updateCriterion(String criterionId, CriterionUpdate updateRequest) {
        return given()
                .auth()
                .oauth2(adminToken)
                .body(updateRequest)
                .contentType(ContentType.JSON)
                .patch("/{id}", criterionId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(CriterionResponse.class);
    }

    private InformativeResponse deleteCriterion(String criterionId) {
        return given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .delete("/{id}", criterionId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);
    }

    private InformativeResponse createCriterionNotValid(CriterionRequest request, int expectedStatus) {
        return given()
                .auth()
                .oauth2(adminToken)
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract()
                .as(InformativeResponse.class);
    }
}
