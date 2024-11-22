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
import org.grnet.cat.dtos.registry.motivation.MotivationRequest;
import org.grnet.cat.dtos.registry.motivation.MotivationResponse;
import org.grnet.cat.dtos.registry.motivation.UpdateMotivationRequest;
import org.grnet.cat.dtos.registry.principle.MotivationPrincipleExtendedRequestDto;
import org.grnet.cat.dtos.registry.principle.MotivationPrincipleRequest;
import org.grnet.cat.dtos.registry.principle.PrincipleRequestDto;
import org.grnet.cat.dtos.registry.principle.PrincipleResponseDto;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest
@TestHTTPEndpoint(MotivationEndpoint.class)
public class MotivationEndpointTest extends KeycloakTest {

    @Test
    public void getMotivationNotPermitted() {

        register("alice");

        var error = given()
                .auth()
                .oauth2(getAccessToken("alice"))
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
    public void motivationTypeIsNotFound() {

        register("admin");

        var request = new MotivationRequest();
        request.mtv = "mtv";
        request.label = "labelMotivation";
        request.description = "decMotivation";
        request.motivationTypeId = "not found";

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
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
    public void getMotivation() {

        register("admin");

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
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
//                .oauth2(getAccessToken("admin"))
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
    public void createMotivation() {

        register("admin");

        var request = new MotivationRequest();
        request.mtv = "mtv";
        request.label = "labelMotivation";
        request.description = "decMotivation";
        request.motivationTypeId = "pid_graph:8882700E";

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
                .as(MotivationResponse.class);

        assertEquals("decMotivation", response.description);
        assertEquals("pid_graph:8882700E", response.motivationType.id);
    }

    @Test
    public void createMotivationAndCopyFromMotivation() {

        register("admin");

        var request = new MotivationRequest();
        request.mtv = "mtv";
        request.label = "labelMotivation";
        request.description = "decMotivation";
        request.motivationTypeId = "pid_graph:8882700E";
        request.basedOn = "pid_graph:3E109BBA";


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
                .as(MotivationResponse.class);

        assertEquals("decMotivation", response.description);
        assertEquals("pid_graph:8882700E", response.motivationType.id);

    }

    @Test
    public void createMotivationCopyNotFound() {

        register("admin");

        var request = new MotivationRequest();
        request.mtv = "mtv";
        request.label = "labelMotivation";
        request.description = "decMotivation";
        request.motivationTypeId = "pid_graph:8882700E";
        request.basedOn = "notfound";


        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
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

        register("admin");

        var request = new MotivationRequest();
        request.mtv = "mtv";
        request.label = "labelMotivation";
        request.description = "decMotivation";
        request.motivationTypeId = "pid_graph:8882700E";

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
                .as(MotivationResponse.class);

        assertNull(response.lodMtvP);

        var update = new UpdateMotivationRequest();
        update.description = "updated_description";
        update.motivationTypeId = "pid_graph:DFE640B9";

        var updated = given()
                .auth()
                .oauth2(getAccessToken("admin"))
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

        register("admin");
        var request = new MotivationRequest();
        request.mtv = "mtv";
        request.label = "labelMotivation";
        request.description = "decMotivation";
        request.motivationTypeId = "pid_graph:8882700E";

        var motivationResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
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

        register("admin");
        var motivationRequest = new MotivationRequest();
        motivationRequest.mtv = "mtv";
        motivationRequest.label = "labelMotivation";
        motivationRequest.description = "decMotivation";
        motivationRequest.motivationTypeId = "pid_graph:8882700E";

        var motivationResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(motivationRequest)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MotivationResponse.class);

        var request = new CriterionRequest();

        request.cri = "C100";
        request.label = "Minimum Operations";
        request.description = "Service providers SHOULD provide a common Application Programming Interface to interact with PIDs, supporting a minimum set of operations (create, resolve and modify PID and PID Kernel Information)";
        request.imperative = "pid_graph:BED209B9";
        request.typeCriterion = "pid_graph:A2719B92";

        var criterionResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
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
//                .oauth2(getAccessToken("admin"))
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
//                .oauth2(getAccessToken("admin"))
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
//                .oauth2(getAccessToken("admin"))
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
    public void addCriterionNoActor() {

        register("admin");
        var motivationRequest = new MotivationRequest();
        motivationRequest.mtv = "mtv";
        motivationRequest.label = "labelMotivation";
        motivationRequest.description = "decMotivation";
        motivationRequest.motivationTypeId = "pid_graph:8882700E";

        var motivationResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(motivationRequest)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MotivationResponse.class);

        var request = new CriterionRequest();

        request.cri = "C100";
        request.label = "Minimum Operations";
        request.description = "Service providers SHOULD provide a common Application Programming Interface to interact with PIDs, supporting a minimum set of operations (create, resolve and modify PID and PID Kernel Information)";
        request.imperative = "pid_graph:BED209B9";
        request.typeCriterion = "pid_graph:A2719B92";

        var criterionResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
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
    public void addCriterionNoImperative() {

        register("admin");
        var motivationRequest = new MotivationRequest();
        motivationRequest.mtv = "mtv";
        motivationRequest.label = "labelMotivation";
        motivationRequest.description = "decMotivation";
        motivationRequest.motivationTypeId = "pid_graph:8882700E";

        var motivationResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(motivationRequest)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MotivationResponse.class);

        var request = new CriterionRequest();

        request.cri = "C100";
        request.label = "Minimum Operations";
        request.description = "Service providers SHOULD provide a common Application Programming Interface to interact with PIDs, supporting a minimum set of operations (create, resolve and modify PID and PID Kernel Information)";
        request.imperative = "pid_graph:BED209B9";
        request.typeCriterion = "pid_graph:A2719B92";

        var criterionResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
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
    public void addCriterionNoMotivationActor() {

        register("admin");
        var motivationRequest = new MotivationRequest();
        motivationRequest.mtv = "mtv";
        motivationRequest.label = "labelMotivation";
        motivationRequest.description = "decMotivation";
        motivationRequest.motivationTypeId = "pid_graph:8882700E";

        var motivationResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(motivationRequest)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MotivationResponse.class);

        var request = new CriterionRequest();

        request.cri = "C100";
        request.label = "Minimum Operations";
        request.description = "Service providers SHOULD provide a common Application Programming Interface to interact with PIDs, supporting a minimum set of operations (create, resolve and modify PID and PID Kernel Information)";
        request.imperative = "pid_graph:BED209B9";
        request.typeCriterion = "pid_graph:A2719B92";

        var criterionResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
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
    public void updateCriterionImperativeNotFound() {

        register("admin");
        var motivationRequest = new MotivationRequest();
        motivationRequest.mtv = "mtv";
        motivationRequest.label = "labelMotivation";
        motivationRequest.description = "decMotivation";
        motivationRequest.motivationTypeId = "pid_graph:8882700E";

        var motivationResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(motivationRequest)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MotivationResponse.class);

        var request = new CriterionRequest();

        request.cri = "C100";
        request.label = "Minimum Operations";
        request.description = "Service providers SHOULD provide a common Application Programming Interface to interact with PIDs, supporting a minimum set of operations (create, resolve and modify PID and PID Kernel Information)";
        request.imperative = "pid_graph:BED209B9";
        request.typeCriterion = "pid_graph:A2719B92";

        var criterionResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
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
    public void addPrinciple() {

        register("admin");
        var motivationRequest = new MotivationRequest();
        motivationRequest.mtv = "mtv";
        motivationRequest.label = "labelMotivation";
        motivationRequest.description = "decMotivation";
        motivationRequest.motivationTypeId = "pid_graph:8882700E";

        var motivationResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post("/{id}/principles", motivationResponse.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(response.code, 200);
        assertEquals("Principle with id :: pid_graph:F9141635 successfully added to motivation.", response.messages.get(0));
    }

    @Test
    public void duplicatePrinciple() {

        register("admin");
        var motivationRequest = new MotivationRequest();
        motivationRequest.mtv = "mtv";
        motivationRequest.label = "labelMotivation";
        motivationRequest.description = "decMotivation";
        motivationRequest.motivationTypeId = "pid_graph:8882700E";

        var motivationResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post("/{id}/principles", motivationResponse.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("Principle with id :: pid_graph:E7C00DBA already exists to motivation.", duplicate.messages.get(0));
    }

    @Test
    public void principleNotFound() {

        register("admin");
        var motivationRequest = new MotivationRequest();
        motivationRequest.mtv = "mtv";
        motivationRequest.label = "labelMotivation";
        motivationRequest.description = "decMotivation";
        motivationRequest.motivationTypeId = "pid_graph:8882700E";

        var motivationResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
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
    public void createPrincipleForMotivationAlreadyExist() {

        register("admin");

        var motivation = new MotivationRequest();
        motivation.mtv = "testMtv";
        motivation.label = "testLabelMotivation";
        motivation.description = "testDecMotivation";
        motivation.motivationTypeId = "pid_graph:8882700E";

        var motivationResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(motivation)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(MotivationResponse.class);

        var principleRequestDto = new PrincipleRequestDto();
        principleRequestDto.pri = "PRITEST";
        principleRequestDto.label = "New Principle";
        principleRequestDto.description = "A test principle for motivation.";

        var request = new MotivationPrincipleExtendedRequestDto();
        request.principleRequestDto = principleRequestDto;
        request.annotationText = "Test Annotation";
        request.annotationUrl = "http://example.com";
        request.relation = "isSupportedBy";

        given()
                .auth()
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post("/{id}/principle", motivationResponse.id)
                .then()
                .assertThat()
                .statusCode(409)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("A principle with the identifier 'PRITEST' already exists.", errorResponse.message);
    }


    @Test
    public void testPublish() {
        register("admin");


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
                .oauth2(getAccessToken("admin"))
                .basePath("/v1/registry/principles")
                .contentType(ContentType.JSON)
                .delete("{id}", principleResponse.id)
                .then()
                .assertThat()
                .statusCode(200);
    }


    private MotivationResponse testCreateMotivation() {

        //    register("admin");

        var request = new MotivationRequest();
        request.mtv = "mtv";
        request.label = "labelMotivation";
        request.description = "decMotivation";
        request.motivationTypeId = "pid_graph:8882700E";

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
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
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
                        oauth2(getAccessToken("admin")).
                        contentType(ContentType.JSON)
                        .put("/{id}/unpublish", "pid_graph:")
                        .then().
                        statusCode(404).
                        extract().
                        as(InformativeResponse.class);

        assertEquals("There is no Motivation with the following id: pid_graph:", response.message);

        var unpublishResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
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
                        oauth2(getAccessToken("admin")).
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
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
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
                .oauth2(getAccessToken("admin"))
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