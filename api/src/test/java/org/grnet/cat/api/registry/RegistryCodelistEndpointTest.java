package org.grnet.cat.api.registry;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.registry.RegistryCodelistEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.registry.RelationResponse;
import org.grnet.cat.dtos.registry.codelist.ImperativeResponse;
import org.grnet.cat.dtos.registry.codelist.MotivationTypeResponse;
import org.grnet.cat.dtos.registry.codelist.RegistryActorResponse;
import org.grnet.cat.dtos.registry.codelist.TypeBenchmarkResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(RegistryCodelistEndpoint.class)
public class RegistryCodelistEndpointTest extends KeycloakTest {

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getTypeCriterionNotPermitted() {
        var error = getTypeCriterionForbidden("pid_graph:07CA8184");
        assertEquals("You do not have permission to access this resource.", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getImperativeNotPermitted() {
        var error = getImperativeForbidden("pid_graph:293B1DEE");
        assertEquals("You do not have permission to access this resource.", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getImperative() {
        var response = getImperative("pid_graph:293B1DEE");
        assertEquals("pid_graph:293B1DEE", response.id);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getTypeBenchmarkNotPermitted() {
        var error = getTypeBenchmarkForbidden("pid_graph:0917EC0D");
        assertEquals("You do not have permission to access this resource.", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getTypeBenchmark() {
        var response = getTypeBenchmark("pid_graph:0917EC0D");
        assertEquals("pid_graph:0917EC0D", response.id);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getRegistryActorNotPermitted() {
        var error = getRegistryActorForbidden("pid_graph:234B60D8");
        assertEquals("You do not have permission to access this resource.", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getRegistryActor() {
        var response = getRegistryActor("pid_graph:234B60D8");
        assertEquals("pid_graph:234B60D8", response.id);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getMotivationType() {
        var response = getMotivationType("pid_graph:5AF642D8");
        assertEquals("pid_graph:5AF642D8", response.id);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getMotivationTypeNotPermitted() {
        var error = getMotivationTypeForbidden("pid_graph:5AF642D8");
        assertEquals("You do not have permission to access this resource.", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getRelation() {
        var response = getRelation("dcterms:isRequiredBy");
        assertEquals("dcterms:isRequiredBy", response.id);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getRelationNotPermitted() {
        var error = getRelationForbidden("dcterms:isRequiredBy");
        assertEquals("You do not have permission to access this resource.", error.message);
    }


    private ImperativeResponse getImperative(String id) {
        return given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .get("/imperatives/{id}", id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(ImperativeResponse.class);
    }

    private InformativeResponse getImperativeForbidden(String id) {
        return given()
                .auth()
                .oauth2(aliceToken)
                .contentType(ContentType.JSON)
                .get("/imperatives/{id}", id)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);
    }

    private InformativeResponse getTypeCriterionForbidden(String id) {
        return given()
                .auth()
                .oauth2(aliceToken)
                .contentType(ContentType.JSON)
                .get("/criterion-types/{id}", id)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);
    }

    private TypeBenchmarkResponse getTypeBenchmark(String id) {
        return given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .get("/benchmark-types/{id}", id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(TypeBenchmarkResponse.class);
    }

    private InformativeResponse getTypeBenchmarkForbidden(String id) {
        return given()
                .auth()
                .oauth2(aliceToken)
                .contentType(ContentType.JSON)
                .get("/benchmark-types/{id}", id)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);
    }

    private RegistryActorResponse getRegistryActor(String id) {
        return given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .get("/actors/{id}", id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(RegistryActorResponse.class);
    }

    private InformativeResponse getRegistryActorForbidden(String id) {
        return given()
                .auth()
                .oauth2(aliceToken)
                .contentType(ContentType.JSON)
                .get("/actors/{id}", id)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);
    }

    private MotivationTypeResponse getMotivationType(String id) {
        return given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .get("/motivation-types/{id}", id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(MotivationTypeResponse.class);
    }

    private InformativeResponse getMotivationTypeForbidden(String id) {
        return given()
                .auth()
                .oauth2(aliceToken)
                .contentType(ContentType.JSON)
                .get("/motivation-types/{id}", id)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);
    }

    private RelationResponse getRelation(String id) {
        return given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .get("/relations/{id}", id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(RelationResponse.class);
    }

    private InformativeResponse getRelationForbidden(String id) {
        return given()
                .auth()
                .oauth2(aliceToken)
                .contentType(ContentType.JSON)
                .get("/relations/{id}", id)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);
    }
}
