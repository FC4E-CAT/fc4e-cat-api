package org.grnet.cat.api.registry;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.registry.TypeReproducibilityEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.registry.metric.TypeReproducibilityResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(TypeReproducibilityEndpoint.class)
public class TypeReproducibilityEndpointTest extends KeycloakTest {

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getTypeReproducibilityNotPermitted() {
        var error = getTypeReproducibilityUnauthorized("pid_graph:1BA2356B");
        assertEquals("You do not have permission to access this resource.", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getTypeReproducibility() {
        var response = getTypeReproducibility("pid_graph:1BA2356B");
        assertNotNull(response);
        assertEquals("pid_graph:1BA2356B", response.id);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getTypeReproducibilityNotFound() {
        var error = getTypeReproducibilityNotFound("notfound");
        assertEquals("There is no Type Reproducibility with the following id: notfound", error.message);
    }

    private TypeReproducibilityResponseDto getTypeReproducibility(String id) {
        return given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .get("/{id}", id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(TypeReproducibilityResponseDto.class);
    }

    private InformativeResponse getTypeReproducibilityUnauthorized(String id) {
        return given()
                .auth()
                .oauth2(aliceToken)
                .contentType(ContentType.JSON)
                .get("/{id}", id)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);
    }

    private InformativeResponse getTypeReproducibilityNotFound(String id) {
        return given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .get("/{id}", id)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);
    }
}
