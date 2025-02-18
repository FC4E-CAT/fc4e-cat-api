package org.grnet.cat.api.registry;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.registry.TypeAlgorithmEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.registry.metric.TypeAlgorithmResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(TypeAlgorithmEndpoint.class)
public class TypeAlgorithmEndpointTest extends KeycloakTest {

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getTypeAlgorithmNotPermitted() {
        var error = getTypeAlgorithmUnauthorized("pid_graph:7A976659");
        assertEquals("You do not have permission to access this resource.", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getTypeAlgorithm() {
        var response = getTypeAlgorithm("pid_graph:7A976659");
        assertNotNull(response);
        assertEquals("pid_graph:7A976659", response.id);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getTypeAlgorithmNotFound() {
        var error = getTypeAlgorithmError("notfound");
        assertEquals("There is no Type Algorithm with the following id: notfound", error.message);
    }

    private TypeAlgorithmResponseDto getTypeAlgorithm(String id) {
        return given()
                .auth()
                .oauth2(adminToken)
                .contentType(ContentType.JSON)
                .get("/{id}", id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(TypeAlgorithmResponseDto.class);
    }

    private InformativeResponse getTypeAlgorithmUnauthorized(String id) {
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

    private InformativeResponse getTypeAlgorithmError(String id) {
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
