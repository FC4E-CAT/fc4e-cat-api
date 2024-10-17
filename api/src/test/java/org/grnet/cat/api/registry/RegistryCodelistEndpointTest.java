package org.grnet.cat.api.registry;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.registry.RegistryCodelistEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.registry.RelationResponse;
import org.grnet.cat.dtos.registry.codelist.*;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(RegistryCodelistEndpoint.class)
public class RegistryCodelistEndpointTest extends KeycloakTest {

    @Test
    public void getTypeCriterionNotPermitted() {

        register("alice");

        var error = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .contentType(ContentType.JSON)
                .get("/criterion-types/{id}", "pid_graph:07CA8184")
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("You do not have permission to access this resource.", error.message);
    }

    @Test
    public void getImperativeNotPermitted() {

        register("alice");

        var error = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .contentType(ContentType.JSON)
                .get("/imperatives/{id}", "pid_graph:293B1DEE")
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("You do not have permission to access this resource.", error.message);
    }

    @Test
    public void getImperative() {

        register("admin");

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .get("/imperatives/{id}", "pid_graph:293B1DEE")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(ImperativeResponse.class);

        assertEquals(response.id, "pid_graph:293B1DEE");
    }
    @Test
    public void getTypeBenchmarkNotPermitted() {

        register("alice");

        var error = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .contentType(ContentType.JSON)
                .get("/benchmark-types/{id}", "pid_graph:0917EC0D")
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("You do not have permission to access this resource.", error.message);
    }

    @Test
    public void getTypeBenchmark() {

        register("admin");

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .get("/benchmark-types/{id}", "pid_graph:0917EC0D")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(TypeBenchmarkResponse.class);

        assertEquals(response.id, "pid_graph:0917EC0D");
    }


    @Test
    public void getRegistryActorNotPermitted() {

        register("alice");

        var error = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .contentType(ContentType.JSON)
                .get("/actors/{id}", "pid_graph:234B60D8")
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("You do not have permission to access this resource.", error.message);
    }

    @Test
    public void getRegistryActor() {

        register("admin");

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .get("/actors/{id}", "pid_graph:234B60D8")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(RegistryActorResponse.class);

        assertEquals(response.id, "pid_graph:234B60D8");
    }

    @Test
    public void getMotivationType() {

        register("admin");

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .get("/motivation-types/{id}", "pid_graph:5AF642D8")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(MotivationTypeResponse.class);

        assertEquals(response.id, "pid_graph:5AF642D8");
    }

    @Test
    public void getMotivationTypeNotPermitted() {

        register("alice");

        var error = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .contentType(ContentType.JSON)
                .get("/motivation-types/{id}", "pid_graph:5AF642D8")
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("You do not have permission to access this resource.", error.message);
    }
    @Test
    public void getRelation() {

        register("admin");

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .get("/relations/{id}", "dcterms:isRequiredBy")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(RelationResponse.class);

        assertEquals(response.id, "dcterms:isRequiredBy");
    }

    @Test
    public void getRelationNotPermitted() {

        register("alice");

        var error = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .contentType(ContentType.JSON)
                .get("/relations/{id}", "dcterms:isRequiredBy")
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("You do not have permission to access this resource.", error.message);
    }
}
