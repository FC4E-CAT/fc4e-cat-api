package org.grnet.cat.api.registry;

import com.mysql.cj.log.Log;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.registry.MotivationEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.registry.actor.MotivationActorRequest;
import org.grnet.cat.dtos.registry.codelist.RegistryActorResponse;
import org.grnet.cat.dtos.registry.criterion.CriterionActorRequest;
import org.grnet.cat.dtos.registry.criterion.CriterionRequest;
import org.grnet.cat.dtos.registry.criterion.CriterionResponse;
import org.grnet.cat.dtos.registry.motivation.MotivationRequest;
import org.grnet.cat.dtos.registry.motivation.MotivationResponse;
import org.grnet.cat.dtos.registry.motivation.UpdateMotivationRequest;
import org.grnet.cat.dtos.registry.principle.MotivationPrincipleRequest;
import org.grnet.cat.services.registry.RelationsService;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static io.smallrye.common.constraint.Assert.assertTrue;
import static org.hamcrest.Matchers.equalTo;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
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
        array[0]=motivationActor;

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
        array1[0]=criterionActor;
        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(array1)
                .contentType(ContentType.JSON)
                .post("/{id}/actors/{actor-id}/criteria", motivationResponse.id,"pid_graph:1A718108")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(response.code,  200);
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
        array[0]=motivationActor;

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
        array1[0]=criterionActor;
        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(array1)
                .contentType(ContentType.JSON)
                .post("/{id}/actors/{actor-id}/criteria", motivationResponse.id,"pid_graph:1A718109")
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(response.code,  404);
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
        array[0]=motivationActor;

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
        array1[0]=criterionActor;
        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(array1)
                .contentType(ContentType.JSON)
                .post("/{id}/actors/{actor-id}/criteria", motivationResponse.id,"pid_graph:1A718108")
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(response.code,  404);
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
        array1[0]=criterionActor;
        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(array1)
                .contentType(ContentType.JSON)
                .post("/{id}/actors/{actor-id}/criteria", motivationResponse.id,"pid_graph:1A718108")
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(response.code,  404);
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
        array[0]=motivationActor;

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
        array1[0]=criterionActor;
        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(array1)
                .contentType(ContentType.JSON)
                .put("/{id}/actors/{actor-id}/criteria", motivationResponse.id,"pid_graph:1A718108")
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals ("There is no Imperative  with the following id: notfound", response.message);
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

        assertEquals("Principle with id :: pid_graph:E7C00DBA already exists to motivation.",  duplicate.messages.get(0));
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
}