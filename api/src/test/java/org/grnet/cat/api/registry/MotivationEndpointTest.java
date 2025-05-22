package org.grnet.cat.api.registry;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.registry.MotivationEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.registry.actor.MotivationActorRequest;
import org.grnet.cat.dtos.registry.criterion.CriterionActorRequest;
import org.grnet.cat.dtos.registry.criterion.CriterionRequest;
import org.grnet.cat.dtos.registry.criterion.CriterionResponse;
import org.grnet.cat.dtos.registry.metric.MetricRequestDto;
import org.grnet.cat.dtos.registry.metric.MotivationMetricExtendedRequest;
import org.grnet.cat.dtos.registry.motivation.MotivationRequest;
import org.grnet.cat.dtos.registry.motivation.MotivationResponse;
import org.grnet.cat.dtos.registry.motivation.MotivationVersionRequest;
import org.grnet.cat.dtos.registry.motivation.UpdateMotivationRequest;
import org.grnet.cat.dtos.registry.principle.MotivationPrincipleExtendedRequestDto;
import org.grnet.cat.dtos.registry.principle.MotivationPrincipleRequest;
import org.grnet.cat.dtos.registry.principle.PrincipleRequestDto;
import org.grnet.cat.dtos.registry.principle.PrincipleResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.UUID;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.params.shadow.com.univocity.parsers.conversions.Conversions.toUpperCase;

@QuarkusTest
@TestHTTPEndpoint(MotivationEndpoint.class)
public class MotivationEndpointTest extends KeycloakTest {

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getMotivationNotPermitted() {

        var error = given()
                .auth()
                .oauth2(aliceToken)
                .contentType(ContentType.JSON)
                .get("/{id}", "pid_graph:3E109BBA")
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("You do not have permission to access this resource.", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void motivationTypeIsNotFound() {

        var request = new MotivationRequest();
        request.mtv = "mtv";
        request.label = "labelMotivation";
        request.description = "decMotivation";
        request.motivationTypeId = "not found";

        var response = given()
                .auth()
                .oauth2(adminToken)
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("There is no Motivation Type with the following id: not found", response.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getMotivation() {

        var response = given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .get("/{id}", "pid_graph:3E109BBA")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(MotivationResponse.class);

        assertEquals(response.motivationType.id, "pid_graph:5EB0883B");
    }

//    @Test
//    public void testGetMotivationWithPrincipleCriterionDetails() {
//
//        register("admin");
//
//        var response = given()
//                .auth()
//                .oauth2(adminToken)
//                .contentType(ContentType.JSON)
//                .get("/{id}", "pid_graph:3E109BBA")
//                .then()
//                .assertThat()
//                .statusCode(200)
//                .extract()
//                .as(MotivationResponse.class);
//
//        assertEquals("pid_graph:3E109BBA", response.id);
//        assertEquals("pid_graph:5EB0883B", response.motivationType.id);
//
//        response.actors.forEach(actor -> {
//            System.out.println(actor.id);
//            System.out.println(actor.existsPrincipleCriterion);
//            System.out.println(actor.principleCriterionCount);
//
//            if ("pid_graph:566C01F6".equals(actor.id)) {
//                assertEquals(true, actor.existsPrincipleCriterion);
//                assertEquals(51, actor.principleCriterionCount);
//            }
//        });
//    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void createMotivation() {

        var request = new MotivationRequest();
        request.mtv = "mtv";
        request.label = "labelMotivation";
        request.description = "decMotivation";
        request.motivationTypeId = "pid_graph:8882700E";

        var response = given()
                .auth()
                .oauth2(adminToken)
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MotivationResponse.class);

        assertEquals("decMotivation", response.description);
        assertEquals("pid_graph:8882700E", response.motivationType.id);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void createMotivationAndCopyFromMotivation() {

        var request = new MotivationRequest();
        request.mtv = "mtv";
        request.label = "labelMotivation";
        request.description = "decMotivation";
        request.motivationTypeId = "pid_graph:8882700E";
        request.basedOn = "pid_graph:3E109BBA";


        var response = given()
                .auth()
                .oauth2(adminToken)
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MotivationResponse.class);

        assertEquals("decMotivation", response.description);
        assertEquals("pid_graph:8882700E", response.motivationType.id);

    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void createMotivationCopyNotFound() {

        var request = new MotivationRequest();
        request.mtv = "mtv";
        request.label = "labelMotivation";
        request.description = "decMotivation";
        request.motivationTypeId = "pid_graph:8882700E";
        request.basedOn = "notfound";


        var response = given()
                .auth()
                .oauth2(adminToken)
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("There is no Motivation with the following id: notfound", response.message);
    }

    @Test
    public void updateMotivation() {

        var request = new MotivationRequest();
        request.mtv = "mtv";
        request.label = "labelMotivation";
        request.description = "decMotivation";
        request.motivationTypeId = "pid_graph:8882700E";

        var response = given()
                .auth()
                .oauth2(adminToken)
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MotivationResponse.class);

        assertNull(response.lodMtvP);

        var update = new UpdateMotivationRequest();
        update.description = "updated_description";
        update.motivationTypeId = "pid_graph:DFE640B9";

        var updated = given()
                .auth()
                .oauth2(adminToken)
                .body(update)
                .contentType(ContentType.JSON)
                .patch("/{id}", response.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(MotivationResponse.class);

        assertEquals("updated_description", updated.description);
        assertEquals("pid_graph:DFE640B9", updated.motivationType.id);
    }

    @Test
    public void addActor() {

        var request = new MotivationRequest();
        request.mtv = "mtv";
        request.label = "labelMotivation";
        request.description = "decMotivation";
        request.motivationTypeId = "pid_graph:8882700E";

        var motivationResponse = given()
                .auth()
                .oauth2(adminToken)
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MotivationResponse.class);

        var motivationActor = new MotivationActorRequest();
        motivationActor.actorId = "pid_graph:1A718108";
        motivationActor.relation = "dcterms:isRequiredBy";
        MotivationActorRequest[] array = new MotivationActorRequest[1];
        array[0] = motivationActor;

        var response = given()
                .auth()
                .oauth2(adminToken)
                .body(array)
                .contentType(ContentType.JSON)
                .post("/{id}/actors", motivationResponse.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(response.code, 200);
    }

    @Test
    public void addCriterion() {

        var motivationRequest = new MotivationRequest();
        motivationRequest.mtv = "mtv";
        motivationRequest.label = "labelMotivation";
        motivationRequest.description = "decMotivation";
        motivationRequest.motivationTypeId = "pid_graph:8882700E";

        var motivationResponse = given()
                .auth()
                .oauth2(adminToken)
                .body(motivationRequest)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MotivationResponse.class);

        var request = new CriterionRequest();

        request.cri = "C100AC";
        request.label = "Minimum Operations";
        request.description = "Service providers SHOULD provide a common Application Programming Interface to interact with PIDs, supporting a minimum set of operations (create, resolve and modify PID and PID Kernel Information)";
        request.imperative = "pid_graph:BED209B9";
        request.typeCriterion = "pid_graph:A2719B92";

        var criterionResponse = given()
                .auth()
                .oauth2(adminToken)
                .basePath("/v1/registry/criteria")
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(CriterionResponse.class);

        var motivationActor = new MotivationActorRequest();
        motivationActor.actorId = "pid_graph:1A718108";
        motivationActor.relation = "dcterms:isRequiredBy";
        MotivationActorRequest[] array = new MotivationActorRequest[1];
        array[0] = motivationActor;

        given()
                .auth()
                .oauth2(adminToken)
                .body(array)
                .contentType(ContentType.JSON)
                .post("/{id}/actors", motivationResponse.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        var criterionActor = new CriterionActorRequest();
        criterionActor.criterionId = criterionResponse.id;
        criterionActor.imperativeId = "pid_graph:293B1DEE";
        CriterionActorRequest[] array1 = new CriterionActorRequest[1];
        array1[0] = criterionActor;
        var response = given()
                .auth()
                .oauth2(adminToken)
                .body(array1)
                .contentType(ContentType.JSON)
                .post("/{id}/actors/{actor-id}/criteria", motivationResponse.id, "pid_graph:1A718108")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(response.code, 200);
    }

//    @Test
//    public void addCriterionNoMotivation() {
//
//        register("admin");
//        var request = new CriterionRequest();
//
//        request.cri = "C100";
//        request.label = "Minimum Operations";
//        request.description = "Service providers SHOULD provide a common Application Programming Interface to interact with PIDs, supporting a minimum set of operations (create, resolve and modify PID and PID Kernel Information)";
//        request.imperative = "pid_graph:BED209B9";
//        request.typeCriterion = "pid_graph:A2719B92";
//
//        var criterionResponse = given()
//                .auth()
//                .oauth2(adminToken)
//                .basePath("/v1/registry/criteria")
//                .body(request)
//                .contentType(ContentType.JSON)
//                .post()
//                .then()
//                .assertThat()
//                .statusCode(201)
//                .extract()
//                .as(CriterionResponse.class);
//
//
//        var motivationActor = new MotivationActorRequest();
//        motivationActor.actorId = "pid_graph:1A718108";
//        motivationActor.relation = "dcterms:isRequiredBy";
//        MotivationActorRequest[] array = new MotivationActorRequest[1];
//        array[0]=motivationActor;
//
//        given()
//                .auth()
//                .oauth2(adminToken)
//                .body(array)
//                .contentType(ContentType.JSON)
//                .post("/{id}/actors", "pid_graph:C6B2D50E")
//                .then()
//                .assertThat()
//                .statusCode(200)
//                .extract()
//                .as(InformativeResponse.class);
//
//        var criterionActor = new CriterionActorRequest();
//        criterionActor.criterionId = criterionResponse.id;
//        criterionActor.imperativeId = "pid_graph:293B1DEE";
//        CriterionActorRequest[] array1 = new CriterionActorRequest[1];
//        array1[0]=criterionActor;
//        var response = given()
//                .auth()
//                .oauth2(adminToken)
//                .body(array1)
//                .contentType(ContentType.JSON)
//                .post("/{id}/actors/{actor-id}/criteria", "pid_graph:C6B2D50A","pid_graph:1A718108")
//                .then()
//                .assertThat()
//                .statusCode(404)
//                .extract()
//                .as(InformativeResponse.class);
//
//        assertEquals(response.code,  404);
//    }


    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void addCriterionNoActor() {

        var motivationRequest = new MotivationRequest();
        motivationRequest.mtv = "mtvna";
        motivationRequest.label = "labelMotivation";
        motivationRequest.description = "decMotivation";
        motivationRequest.motivationTypeId = "pid_graph:8882700E";

        var motivationResponse = given()
                .auth()
                .oauth2(adminToken)
                .body(motivationRequest)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MotivationResponse.class);

        var request = new CriterionRequest();

        request.cri = "C100NA";
        request.label = "Minimum Operations";
        request.description = "Service providers SHOULD provide a common Application Programming Interface to interact with PIDs, supporting a minimum set of operations (create, resolve and modify PID and PID Kernel Information)";
        request.imperative = "pid_graph:BED209B9";
        request.typeCriterion = "pid_graph:A2719B92";

        var criterionResponse = given()
                .auth()
                .oauth2(adminToken)
                .basePath("/v1/registry/criteria")
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(CriterionResponse.class);

        var motivationActor = new MotivationActorRequest();
        motivationActor.actorId = "pid_graph:1A718108";
        motivationActor.relation = "dcterms:isRequiredBy";
        MotivationActorRequest[] array = new MotivationActorRequest[1];
        array[0] = motivationActor;

        given()
                .auth()
                .oauth2(adminToken)
                .body(array)
                .contentType(ContentType.JSON)
                .post("/{id}/actors", motivationResponse.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        var criterionActor = new CriterionActorRequest();
        criterionActor.criterionId = criterionResponse.id;
        criterionActor.imperativeId = "pid_graph:293B1DEE";
        CriterionActorRequest[] array1 = new CriterionActorRequest[1];
        array1[0] = criterionActor;
        var response = given()
                .auth()
                .oauth2(adminToken)
                .body(array1)
                .contentType(ContentType.JSON)
                .post("/{id}/actors/{actor-id}/criteria", motivationResponse.id, "pid_graph:1A718109")
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(response.code, 404);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void addCriterionNoImperative() {

        var motivationRequest = new MotivationRequest();
        motivationRequest.mtv = "mtv";
        motivationRequest.label = "labelMotivation";
        motivationRequest.description = "decMotivation";
        motivationRequest.motivationTypeId = "pid_graph:8882700E";

        var motivationResponse = given()
                .auth()
                .oauth2(adminToken)
                .body(motivationRequest)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MotivationResponse.class);

        var request = new CriterionRequest();

        request.cri = "C100NI";
        request.label = "Minimum Operations";
        request.description = "Service providers SHOULD provide a common Application Programming Interface to interact with PIDs, supporting a minimum set of operations (create, resolve and modify PID and PID Kernel Information)";
        request.imperative = "pid_graph:BED209B9";
        request.typeCriterion = "pid_graph:A2719B92";

        var criterionResponse = given()
                .auth()
                .oauth2(adminToken)
                .basePath("/v1/registry/criteria")
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(CriterionResponse.class);

        var motivationActor = new MotivationActorRequest();
        motivationActor.actorId = "pid_graph:1A718108";
        motivationActor.relation = "dcterms:isRequiredBy";
        MotivationActorRequest[] array = new MotivationActorRequest[1];
        array[0] = motivationActor;

        given()
                .auth()
                .oauth2(adminToken)
                .body(array)
                .contentType(ContentType.JSON)
                .post("/{id}/actors", motivationResponse.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        var criterionActor = new CriterionActorRequest();
        criterionActor.criterionId = criterionResponse.id;
        criterionActor.imperativeId = "pid_graph:293B1DEEE";
        CriterionActorRequest[] array1 = new CriterionActorRequest[1];
        array1[0] = criterionActor;

        var response = given()
                .auth()
                .oauth2(adminToken)
                .body(array1)
                .contentType(ContentType.JSON)
                .post("/{id}/actors/{actor-id}/criteria", motivationResponse.id, "pid_graph:1A718108")
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(response.code, 404);
    }


    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void addCriterionNoMotivationActor() {

        var motivationRequest = new MotivationRequest();
        motivationRequest.mtv = "mtv";
        motivationRequest.label = "labelMotivation";
        motivationRequest.description = "decMotivation";
        motivationRequest.motivationTypeId = "pid_graph:8882700E";

        var motivationResponse = given()
                .auth()
                .oauth2(adminToken)
                .body(motivationRequest)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MotivationResponse.class);

        var request = new CriterionRequest();

        request.cri = "C100NMA";
        request.label = "Minimum Operations";
        request.description = "Service providers SHOULD provide a common Application Programming Interface to interact with PIDs, supporting a minimum set of operations (create, resolve and modify PID and PID Kernel Information)";
        request.imperative = "pid_graph:2981F3DD";
        request.typeCriterion = "pid_graph:A2719B92";

        var criterionResponse = given()
                .auth()
                .oauth2(adminToken)
                .basePath("/v1/registry/criteria")
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(CriterionResponse.class);

        var criterionActor = new CriterionActorRequest();
        criterionActor.criterionId = criterionResponse.id;
        criterionActor.imperativeId = "pid_graph:293B1DEE";
        CriterionActorRequest[] array1 = new CriterionActorRequest[1];
        array1[0] = criterionActor;
        var response = given()
                .auth()
                .oauth2(adminToken)
                .body(array1)
                .contentType(ContentType.JSON)
                .post("/{id}/actors/{actor-id}/criteria", motivationResponse.id, "pid_graph:1A718108")
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(response.code, 404);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void updateCriterionImperativeNotFound() {

        var motivationRequest = new MotivationRequest();
        motivationRequest.mtv = "mtv";
        motivationRequest.label = "labelMotivation";
        motivationRequest.description = "decMotivation";
        motivationRequest.motivationTypeId = "pid_graph:8882700E";

        var motivationResponse = given()
                .auth()
                .oauth2(adminToken)
                .body(motivationRequest)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MotivationResponse.class);

        var request = new CriterionRequest();

        request.cri = "C100NF";
        request.label = "Minimum Operations";
        request.description = "Service providers SHOULD provide a common Application Programming Interface to interact with PIDs, supporting a minimum set of operations (create, resolve and modify PID and PID Kernel Information)";
        request.imperative = "pid_graph:BED209B9";
        request.typeCriterion = "pid_graph:A2719B92";

        var criterionResponse = given()
                .auth()
                .oauth2(adminToken)
                .basePath("/v1/registry/criteria")
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(CriterionResponse.class);

        var motivationActor = new MotivationActorRequest();
        motivationActor.actorId = "pid_graph:1A718108";
        motivationActor.relation = "dcterms:isRequiredBy";
        MotivationActorRequest[] array = new MotivationActorRequest[1];
        array[0] = motivationActor;

        given()
                .auth()
                .oauth2(adminToken)

                .body(array)
                .contentType(ContentType.JSON)
                .post("/{id}/actors", motivationResponse.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        var criterionActor = new CriterionActorRequest();
        criterionActor.criterionId = criterionResponse.id;
        criterionActor.imperativeId = "notfound";
        CriterionActorRequest[] array1 = new CriterionActorRequest[1];
        array1[0] = criterionActor;
        var response = given()
                .auth()
                .oauth2(adminToken)

                .body(array1)
                .contentType(ContentType.JSON)
                .put("/{id}/actors/{actor-id}/criteria", motivationResponse.id, "pid_graph:1A718108")
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("There is no Imperative  with the following id: notfound", response.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void addPrinciple() {

        var motivationRequest = new MotivationRequest();
        motivationRequest.mtv = "mtv";
        motivationRequest.label = "labelMotivation";
        motivationRequest.description = "decMotivation";
        motivationRequest.motivationTypeId = "pid_graph:8882700E";

        var motivationResponse = given()
                .auth()
                .oauth2(adminToken)
                .body(motivationRequest)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MotivationResponse.class);

        var mpr = new MotivationPrincipleRequest();
        mpr.principleId = "pid_graph:F9141635";
        mpr.relation = "isSupportedBy";

        var request = new HashSet<MotivationPrincipleRequest>();
        request.add(mpr);

        var response = given()
                .auth()
                .oauth2(adminToken)

                .body(request)
                .contentType(ContentType.JSON)
                .post("/{id}/principles", motivationResponse.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(response.code, 200);
        assertEquals("Principle with id :: pid_graph:F9141635 successfully added to motivation.", response.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void duplicatePrinciple() {

        var motivationRequest = new MotivationRequest();
        motivationRequest.mtv = "mtv";
        motivationRequest.label = "labelMotivation";
        motivationRequest.description = "decMotivation";
        motivationRequest.motivationTypeId = "pid_graph:8882700E";

        var motivationResponse = given()
                .auth()
                .oauth2(adminToken)
                .body(motivationRequest)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MotivationResponse.class);

        var mpr = new MotivationPrincipleRequest();
        mpr.principleId = "pid_graph:E7C00DBA";
        mpr.relation = "isSupportedBy";

        var request = new HashSet<MotivationPrincipleRequest>();
        request.add(mpr);

        var response = given()
                .auth()
                .oauth2(adminToken)
                .body(request)
                .contentType(ContentType.JSON)
                .post("/{id}/principles", motivationResponse.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(response.code, 200);

        var duplicate = given()
                .auth()
                .oauth2(adminToken)
                .body(request)
                .contentType(ContentType.JSON)
                .post("/{id}/principles", motivationResponse.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("Principle with id :: pid_graph:E7C00DBA already exists to motivation.",  duplicate.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void principleNotFound() {

        var motivationRequest = new MotivationRequest();
        motivationRequest.mtv = "mtv";
        motivationRequest.label = "labelMotivation";
        motivationRequest.description = "decMotivation";
        motivationRequest.motivationTypeId = "pid_graph:8882700E";

        var motivationResponse = given()
                .auth()
                .oauth2(adminToken)

                .body(motivationRequest)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MotivationResponse.class);

        var mpr = new MotivationPrincipleRequest();
        mpr.principleId = "lalala";
        mpr.relation = "isSupportedBy";

        var request = new HashSet<MotivationPrincipleRequest>();
        request.add(mpr);

        var response = given()
                .auth()
                .oauth2(adminToken)

                .body(request)
                .contentType(ContentType.JSON)
                .post("/{id}/principles", motivationResponse.id)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(response.code, 404);
        assertEquals(response.message, "There is no Principle with the following id: lalala");
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void createPrincipleForMotivationAlreadyExist() {

        var motivation = new MotivationRequest();
        motivation.mtv = "mtv";
        motivation.label = "testLabelMotivation";
        motivation.description = "testDecMotivation";
        motivation.motivationTypeId = "pid_graph:8882700E";

        var motivationResponse = given()
                .auth()
                .oauth2(adminToken)
                .body(motivation)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MotivationResponse.class);

        var principleRequestDto = new PrincipleRequestDto();
        principleRequestDto.pri = ("PRI" + UUID.randomUUID()).toUpperCase();
        principleRequestDto.label = "New Principle";
        principleRequestDto.description = "A test principle for motivation.";

        var request = new MotivationPrincipleExtendedRequestDto();
        request.principleRequestDto = principleRequestDto;
        request.annotationText = "Test Annotation";
        request.annotationUrl = "http://example.com";
        request.relation = "isSupportedBy";

        given()
                .auth()
                .oauth2(adminToken)
                .body(request)
                .contentType(ContentType.JSON)
                .post("/{id}/principle", motivationResponse.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);


        var errorResponse = given()
                .auth()
                .oauth2(adminToken)
                .body(request)
                .contentType(ContentType.JSON)
                .post("/{id}/principle", motivationResponse.id)
                .then()
                .assertThat()
                .statusCode(409)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("A principle with the identifier '" + principleRequestDto.pri + "' already exists.", errorResponse.message);
    }


    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void createMetricDefinitionForMotivationAlreadyExist() {

        //register("admin");

        var motivationRequest = new MotivationRequest();
        motivationRequest.mtv = ("mtv" + UUID.randomUUID()).toUpperCase();
        motivationRequest.label = "testLabelMotivation";
        motivationRequest.description = "testDecMotivation";
        motivationRequest.motivationTypeId = "pid_graph:8882700E";

        var motivationResponse = given()
                .auth()
                .oauth2(adminToken)
                .body(motivationRequest)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MotivationResponse.class);

        var metricDefinitionRequest = new MotivationMetricExtendedRequest();
        metricDefinitionRequest.MTR = ("MTR" + UUID.randomUUID()).toUpperCase();
        metricDefinitionRequest.labelMetric = "Performance Metric";
        metricDefinitionRequest.descrMetric = "This metric measures performance.";
        metricDefinitionRequest.urlMetric = "http://example.com/metric";
        metricDefinitionRequest.typeAlgorithmId = "pid_graph:2050775C";
        metricDefinitionRequest.typeMetricId = "pid_graph:35966E2B";
        metricDefinitionRequest.typeBenchmarkId = "pid_graph:0917EC0D";
        metricDefinitionRequest.valueBenchmark = "3";

        var informativeResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(metricDefinitionRequest)
                .contentType(ContentType.JSON)
                .post("/{id}/metric", motivationResponse.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(200, informativeResponse.code);
        assertEquals("A metric and a Metric Definition successfully created and linked to the specified motivation.", informativeResponse.message);

        var errorResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(metricDefinitionRequest)
                .contentType(ContentType.JSON)
                .post("/{id}/metric", motivationResponse.id)
                .then()
                .assertThat()
                .statusCode(409)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(409, errorResponse.code);
        assertEquals("A metric with the identifier '" + metricDefinitionRequest.MTR + "' already exists.", errorResponse.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void createNewMetricVersion() {

        var motivationRequest = new MotivationRequest();
        motivationRequest.mtv = ("mtv" + UUID.randomUUID()).toUpperCase();
        motivationRequest.label = "Test Motivation";
        motivationRequest.description = "Test Motivation Description";
        motivationRequest.motivationTypeId = "pid_graph:8882700E";

        var motivationResponse = given()
                .auth()
                .oauth2(adminToken)
                .body(motivationRequest)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MotivationResponse.class);

        var metricDefinitionRequest = new MotivationMetricExtendedRequest();
        metricDefinitionRequest.MTR = ("MTR" + UUID.randomUUID()).toUpperCase();
        metricDefinitionRequest.labelMetric = "Performance Metric";
        metricDefinitionRequest.descrMetric = "This metric measures performance.";
        metricDefinitionRequest.urlMetric = "http://example.com/metric";
        metricDefinitionRequest.typeAlgorithmId = "pid_graph:2050775C";
        metricDefinitionRequest.typeMetricId = "pid_graph:35966E2B";
        metricDefinitionRequest.typeBenchmarkId = "pid_graph:0917EC0D";
        metricDefinitionRequest.valueBenchmark = "3";

        var informativeResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(metricDefinitionRequest)
                .contentType(ContentType.JSON)
                .post("/{id}/metric", motivationResponse.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        var metricPage = given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .queryParam("page", 1) // Page number
                .queryParam("size", 10) // Page size
                .get("/{id}/metric-definition", motivationResponse.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(MotivationEndpoint.PageableMetricDefinitionJunctionResponse.class);

        assertTrue(metricPage.getTotalElements() > 0, "There should be at least one metric in the list.");

        var firstMetric = metricPage.getContent().get(0);
        var metricId = firstMetric.metricId;

        var newMotivationRequest = new MotivationRequest();
        motivationRequest.mtv = ("mtv" + UUID.randomUUID()).toUpperCase();
        motivationRequest.label = "Test Motivation";
        motivationRequest.description = "Test Motivation Description";
        motivationRequest.motivationTypeId = "pid_graph:8882700E";

        var newMotivationResponse = given()
                .auth()
                .oauth2(adminToken)
                .body(motivationRequest)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MotivationResponse.class);

        var newVersionResponse = given()
                .auth()
                .oauth2(adminToken)
                .body(metricDefinitionRequest)
                .contentType(ContentType.JSON)
                .post("/{id}/metric/{metric-id}/version-metric", newMotivationResponse.id, metricId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(200, newVersionResponse.code);
        assertEquals("A version of a metric and a Metric Definition successfully created and linked to the specified motivation.", newVersionResponse.message);
    }


    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void testPublish() {

        MotivationResponse motivationResponse = testCreateMotivation();
        CriterionResponse criterionResponse = testCreateCriterion();
        PrincipleResponseDto principleResponse = testCreatePrinciple();

        testPublishMotivation(motivationResponse.id);
        testCreateMotivationPrinciplePublished(motivationResponse.id, principleResponse.id);

        testUnpublishMotivation(motivationResponse.id);

        testCreateMotivationActor(motivationResponse.id);

        testPublishMotivationActor(motivationResponse.id);
        testCreateMotivationActorCriteriaInPublishedActor(motivationResponse.id, "pid_graph:1A718108", criterionResponse.id);
        testUnpublishMotivationActor(motivationResponse.id, "pid_graph:1A718108");
        testCreateMotivationActorCriteriaInUnpublishedActor(motivationResponse.id, "pid_graph:1A718108", criterionResponse.id);

        testPublishMotivation(motivationResponse.id);
        // Attempt to update the published motivation
        testUpdateMotivationPublished(motivationResponse.id);

        testCreateMotivationActorPublishedMotivation(motivationResponse.id, "pid_graph:1A718108");
        testCreateMotivationActorCriteriaPublishedMotivation(motivationResponse.id, "pid_graph:1A718108", criterionResponse.id);

        given()
                .auth()
                .oauth2(adminToken)
                .basePath("/v1/registry/principles")
                .contentType(ContentType.JSON)
                .delete("{id}", principleResponse.id)
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void createNewMotivationVersion() {

        var motivationRequest = new MotivationRequest();
        motivationRequest.mtv = ("mtv" + UUID.randomUUID()).toUpperCase();
        motivationRequest.label = "Test Motivation";
        motivationRequest.description = "Test Motivation Description";
        motivationRequest.motivationTypeId = "pid_graph:8882700E";

        var motivationResponse = given()
                .auth()
                .oauth2(adminToken)
                .body(motivationRequest)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MotivationResponse.class);

        var motivationVersionRequest = new MotivationVersionRequest();
        motivationVersionRequest.label = "Test Motivation";
        motivationVersionRequest.description = "Test Motivation Description";
        motivationVersionRequest.motivationTypeId = "pid_graph:8882700E";
        motivationVersionRequest.versionOf = motivationResponse.id;

        var motivationVersionResponse = given()
                .auth()
                .oauth2(adminToken)
                .body(motivationVersionRequest)
                .contentType(ContentType.JSON)
                .post("/version-motivation")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MotivationResponse.class);

        assertEquals(motivationResponse.mtv, motivationVersionResponse.mtv);
        assertEquals(2, motivationVersionResponse.version);
    }


    private MotivationResponse testCreateMotivation() {

        var request = new MotivationRequest();
        request.mtv = ("mtv"+ UUID.randomUUID()).toUpperCase();;
        request.label = "labelMotivation";
        request.description = "decMotivation";
        request.motivationTypeId = "pid_graph:8882700E";

        var response = given()
                .auth()
                .oauth2(adminToken)
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MotivationResponse.class);

        assertEquals("decMotivation", response.description);
        assertEquals("pid_graph:8882700E", response.motivationType.id);
        return response;
    }

    private CriterionResponse testCreateCriterion() {

        var request = new CriterionRequest();

        request.cri = "C100";
        request.label = "Minimum Operations";
        request.description = "Service providers SHOULD provide a common Application Programming Interface to interact with PIDs, supporting a minimum set of operations (create, resolve and modify PID and PID Kernel Information)";
        request.imperative = "pid_graph:BED209B9";
        request.typeCriterion = "pid_graph:A2719B92";

        var response = given()
                .auth()
                .oauth2(adminToken)
                .basePath("/v1/registry/criteria")
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(CriterionResponse.class);

        assertEquals("pid_graph:A2719B92", response.typeCriterion);
        return response;

    }

    private PrincipleResponseDto testCreatePrinciple() {
        var principle = new PrincipleRequestDto();
        principle.description = "principle description";
        principle.pri = "P30-test";
        principle.label = "Principle 1-test";

        var principleResponse = given()
                .auth()
                .oauth2(adminToken)
                .basePath("/v1/registry/principles")
                .body(principle)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(PrincipleResponseDto.class);


        return principleResponse;
    }

    private void testPublishMotivation(String motivationId) {

        /******* Test UnAuthorized **********/
        var   response = given()
                .auth().oauth2("alice")
                .contentType(ContentType.JSON)
                .put("/{id}/publish", motivationId)
                .then()
                .statusCode(401)
                .extract()
                .as(InformativeResponse.class);
        assertEquals(401, response.code);
//
//        /******* No motivation found *************/
        response = given()
                .auth()
                .oauth2(adminToken)

                .contentType(ContentType.JSON)
                .put("/{id}/publish", "pid_graph:")
                .then()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);
        assertEquals("There is no Motivation with the following id: pid_graph:", response.message);

        /**************/

        var publishResponse = given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .put("/{id}/publish", motivationId)
                .then()
                .assertThat()
                .statusCode(200) // Expect HTTP 200 response
                .extract()
                .as(InformativeResponse.class);
        assertEquals(200, publishResponse.code);
/**** Publish published motivation ****/
        var informativeResponse = given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .put("/{id}/publish", motivationId)
                .then()
                .statusCode(400)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("No action permitted for published Motivation with the following id: " + motivationId, informativeResponse.message);

    }

    private void testUnpublishMotivation(String motivationId) {
        var response =
                given().auth().
                        oauth2(adminToken).
                        contentType(ContentType.JSON)
                        .put("/{id}/unpublish", "pid_graph:")
                        .then().
                        statusCode(404).
                        extract().
                        as(InformativeResponse.class);

        assertEquals("There is no Motivation with the following id: pid_graph:", response.message);

        var unpublishResponse = given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .put("/{id}/unpublish", motivationId)
                .then()
                .assertThat()
                .statusCode(200) // Expect HTTP 200 response
                .extract()
                .as(InformativeResponse.class);

        // Step 4: Validate the response
        assertEquals(200, unpublishResponse.code);
        assertEquals("Successful unpublish", unpublishResponse.message);

        unpublishResponse =
                given().
                        auth().
                        oauth2(adminToken).
                        contentType(ContentType.JSON).
                        put("/{id}/unpublish", motivationId).
                        then().
                        statusCode(400).
                        extract().
                        as(InformativeResponse.class);

        assertEquals("No action permitted for unpublished Motivation with the following id: "+motivationId, unpublishResponse.message);
    }

    private void testCreateMotivationActor(String motivationId) {
        var motivationActor = new MotivationActorRequest();
        motivationActor.actorId = "pid_graph:1A718108";
        motivationActor.relation = "dcterms:isRequiredBy";
        MotivationActorRequest[] array = new MotivationActorRequest[1];
        array[0] = motivationActor;

        var actorResponse = given()
                .auth()
                .oauth2(adminToken)
                .body(array)
                .contentType(ContentType.JSON)
                .post("/{id}/actors", motivationId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);
        assertEquals(200, actorResponse.code);

    }

    private void testPublishMotivationActor(String motivationId) {
        var actorResponse = given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .put("/{id}/actors/{actor-id}/publish", motivationId, "pid_graph:1A718108")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        // Assert
        assertEquals(200, actorResponse.code);

        actorResponse = given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .put("/{id}/actors/{actor-id}/publish", motivationId, "pid_graph:1A718108")
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(403, actorResponse.code);
        assertEquals("Action not permitted, motivation-actor relation is already published", actorResponse.message);

    }

    private void testCreateMotivationActorCriteriaInPublishedActor(String motivationId, String actorId, String criterionId) {

        var criterionActor = new CriterionActorRequest();
        criterionActor.criterionId = criterionId;
        criterionActor.imperativeId = "pid_graph:293B1DEE";
        CriterionActorRequest[] array1 = new CriterionActorRequest[1];
        array1[0] = criterionActor;

        var criterionActorResponse = given()
                .auth()
                .oauth2(adminToken)
                .body(array1)
                .contentType(ContentType.JSON)
                .post("/{id}/actors/{actor-id}/criteria", motivationId, actorId)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("No action is permitted as motivation-actor relation is published", criterionActorResponse.message);

        criterionActorResponse = given()
                .auth()
                .oauth2(adminToken)
                .body(array1)
                .contentType(ContentType.JSON)
                .put("/{id}/actors/{actor-id}/criteria", motivationId, actorId)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("No action is permitted as motivation-actor relation is published", criterionActorResponse.message);


    }

    private void testCreateMotivationActorCriteriaInUnpublishedActor(String motivationId, String actorId, String criterionId) {

        var criterionActor = new CriterionActorRequest();
        criterionActor.criterionId = criterionId;
        criterionActor.imperativeId = "pid_graph:293B1DEE";
        CriterionActorRequest[] array1 = new CriterionActorRequest[1];
        array1[0] = criterionActor;

        var criterionActorResponse = given()
                .auth()
                .oauth2(adminToken)
                .body(array1)
                .contentType(ContentType.JSON)
                .post("/{id}/actors/{actor-id}/criteria", motivationId, actorId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(200, criterionActorResponse.code);

    }


    private void testUnpublishMotivationActor(String motivationId, String actorId) {
        var actorResponse = given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .put("/{id}/actors/{actor-id}/unpublish", motivationId, actorId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        // Assert
        assertEquals(200, actorResponse.code);

    }

    private void testUpdateMotivationPublished(String motivationId) {
        var update = new UpdateMotivationRequest();
        update.description = "updated_description";
        update.motivationTypeId = "pid_graph:DFE640B9";

        var informativeResponse = given()
                .auth()
                .oauth2(adminToken)
                .body(update)
                .contentType(ContentType.JSON)
                .patch("/{id}", motivationId)
                .then()
                .assertThat()
                .statusCode(400) // Expecting 403 Forbidden due to @CheckPublished
                .extract()
                .as(InformativeResponse.class);

        // Assert the failure message
        assertEquals(400, informativeResponse.code);
        assertEquals("No action permitted for published Motivation with the following id: " + motivationId, informativeResponse.message);
    }

    private void testCreateMotivationActorPublishedMotivation(String motivationId, String actorId) {
        var motivationActor = new MotivationActorRequest();
        motivationActor.actorId = actorId;
        motivationActor.relation = "dcterms:isRequiredBy";
        MotivationActorRequest[] array = new MotivationActorRequest[1];
        array[0] = motivationActor;

        var informativeResponse = given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .post("/{id}/actors", motivationId)
                .then()
                .assertThat()
                .statusCode(400) // Expect 403 Forbidden due to @CheckPublished
                .extract()
                .as(InformativeResponse.class);

        // Assert the failure message
        assertEquals(400, informativeResponse.code);
        assertEquals("No action permitted for published Motivation with the following id: " + motivationId,
                informativeResponse.message);

    }

    private void testCreateMotivationActorCriteriaPublishedMotivation(String motivationId, String actorId, String criterionId) {
        var criterionActor = new CriterionActorRequest();
        criterionActor.criterionId = criterionId;
        criterionActor.imperativeId = "pid_graph:293B1DEE";
        CriterionActorRequest[] array = new CriterionActorRequest[1];
        array[0] = criterionActor;

        var informativeResponse = given()
                .auth()
                .oauth2(adminToken)
                .body(array)
                .contentType(ContentType.JSON)
                .post("/{id}/actors/{actor-id}/criteria", motivationId, actorId)
                .then()
                .assertThat()
                .statusCode(400) // Expect 403 Forbidden
                .extract()
                .as(InformativeResponse.class);

        // Step 5: Validate the failure message
        assertEquals(400, informativeResponse.code);
        assertEquals("No action permitted for published Motivation with the following id: " + motivationId,
                informativeResponse.message);

    }

    private void testCreateMotivationPrinciplePublished(String motivationId, String principleId) {
        var mpr = new MotivationPrincipleRequest();
        mpr.principleId = principleId;
        mpr.relation = "isSupportedBy";

        var request = new HashSet<MotivationPrincipleRequest>();
        request.add(mpr);

        var informativeResponse = given()
                .auth()
                .oauth2(adminToken)
                .body(request)
                .contentType(ContentType.JSON)
                .post("/{id}/principles", motivationId)
                .then()
                .assertThat()
                .statusCode(400) // Expect 403 Forbidden
                .extract()
                .as(InformativeResponse.class);

        // Step 5: Validate the failure message
        assertEquals(400, informativeResponse.code);
        assertEquals("No action permitted for published Motivation with the following id: " + motivationId, informativeResponse.message);

    }
}