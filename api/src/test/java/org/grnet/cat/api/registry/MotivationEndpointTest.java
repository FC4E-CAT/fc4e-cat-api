package org.grnet.cat.api.registry;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.registry.MotivationEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.registry.actor.MotivationActorRequest;
import org.grnet.cat.dtos.registry.motivation.MotivationRequest;
import org.grnet.cat.dtos.registry.motivation.MotivationResponse;
import org.grnet.cat.dtos.registry.motivation.UpdateMotivationRequest;
import org.junit.jupiter.api.Test;

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
                .get("/{id}", "pid_graph:0743BE89")
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
                .get("/{id}", "pid_graph:0743BE89")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(MotivationResponse.class);

        assertEquals(response.motivationType.id, "pid_graph:AD9D854B");
    }

    @Test
    public void motivation() {

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
        update.lodMtvP = "pid_graph:BE36CD9E";

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
        assertEquals("pid_graph:BE36CD9E", updated.lodMtvP);
    }

    @Test
    public void addActor() {

        register("admin");
        var motivationActor = new MotivationActorRequest();
        motivationActor.actorId = "pid_graph:1A718108";
        motivationActor.relation = "dcterms:isRequiredBy";
        MotivationActorRequest[] array = new MotivationActorRequest[1];
        array[0]=motivationActor;
        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(array)
                .contentType(ContentType.JSON)
                .post("/{id}/actors", "pid_graph:C6B2D50E")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(response.code,  200);
    }
}
