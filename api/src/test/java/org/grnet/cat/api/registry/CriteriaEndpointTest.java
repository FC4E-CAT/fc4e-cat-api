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

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestHTTPEndpoint(CriteriaEndpoint.class)
public class CriteriaEndpointTest extends KeycloakTest {

    @Test
    public void createCriteria() {

        register("admin");

        var request = new CriterionRequest();
        request.cri = "C100";
        request.label = "Minimum Operations";
        request.description = "Service providers SHOULD provide a common Application Programming Interface to interact with PIDs, supporting a minimum set of operations (create, resolve and modify PID and PID Kernel Information)";
        request.imperative = "pid_graph:BED209B9";
        request.typeCriterion = "pid_graph:A2719B92";

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(CriterionResponse.class);

        assertNotNull(response.id);
        assertEquals(request.label, response.label);
        assertEquals(request.description, response.description);
        assertEquals(request.imperative, response.imperative);
        assertEquals(request.typeCriterion, response.typeCriterion);
    }


    @Test
    public void createCriteriaWithInvalidImperative() {

        register("admin");

        var request = new CriterionRequest();
        request.cri = "C101";
        request.label = "Invalid imperative";
        request.description = "This should fail due to invalid UUID format.";
        request.imperative = "Invalid imperative";
        request.typeCriterion = "pid_graph:A2719B92";

        given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void createCriteriaWithNonUniqueCriteriaCode() {

        register("admin");

        var request1 = new CriterionRequest();
        request1.cri = "C102";
        request1.label = "Unique UUID";
        request1.description = "This should succeed.";
        request1.imperative = "pid_graph:BED209B9";
        request1.typeCriterion = "pid_graph:A2719B92";

        given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request1)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(CriterionResponse.class);

        var request2 = new CriterionRequest();
        request2.cri = "C102";
        request2.label = "Unique UUID";
        request2.description = "This should succeed.";
        request2.imperative = "pid_graph:BED209B9";
        request2.typeCriterion = "pid_graph:A2719B92";

        given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request2)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(409);
    }

    @Test
    public void getCriteria() {

        register("admin");

        var request = new CriterionRequest();
        request.cri = "C100";
        request.label = "Extended Operations";
        request.description = "This should succeed.";
        request.imperative = "pid_graph:BED209B9";
        request.typeCriterion = "pid_graph:A2719B92";

        var createdResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(CriterionResponse.class);

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .get("/{id}", createdResponse.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(CriterionResponse.class);

        assertEquals(createdResponse.id, response.id);
        assertEquals(createdResponse.cri, response.cri);
        assertEquals(createdResponse.label, response.label);
        assertEquals(createdResponse.description, response.description);
        assertEquals(createdResponse.imperative, response.imperative);
    }

    @Test
    public void updateCriteria() {

        register("admin");

        var request = new CriterionRequest();
        request.cri = "C100";
        request.label = "Basic Operations";
        request.description = "Service providers SHOULD provide basic operations for PIDs.";
        request.imperative = "pid_graph:BED209B9";
        request.typeCriterion = "pid_graph:A2719B92";

        var createdResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(CriterionResponse.class);

        var updatedRequest = new CriterionUpdate();

        updatedRequest.imperative = "pid_graph:2981F3DD";

        var updatedResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(updatedRequest)
                .contentType(ContentType.JSON)
                .patch("/{id}", createdResponse.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(CriterionResponse.class);

        assertEquals(updatedRequest.imperative, updatedResponse.imperative);
    }

    @Test
    public void deleteCriteria() {

        register("admin");

        var request = new CriterionRequest();
        request.cri = "C100";
        request.label = "Advanced Operations";
        request.description = "Service providers MUST provide advanced operations for PIDs.";
        request.imperative = "pid_graph:BED209B9";
        request.typeCriterion = "pid_graph:A2719B92";

        var createdResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(CriterionResponse.class);

        var success = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .delete("/{id}", createdResponse.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("Criterion has been successfully deleted.", success.message);

    }
}
