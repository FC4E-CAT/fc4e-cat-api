package org.grnet.cat.api;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.RolesEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.RoleAssignmentRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(RolesEndpoint.class)
public class RolesEndpointTest extends KeycloakTest {

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void assignRolesNotPermitted() {
        var request = createRoleAssignmentRequest("alice_voperson_id", List.of("validated"));
        var error = assignRoles(request, aliceToken, 403);
        assertEquals("You do not have permission to access this resource.", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void assignRolesRequestBodyIsEmpty() {
        var error = assignRoles(null, adminToken, 400);
        assertEquals("The request body is empty.", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void assignRolesUserIdIsEmpty() {
        var request = createRoleAssignmentRequest(null, List.of("validated"));
        var error = assignRoles(request, adminToken, 400);
        assertEquals("user_id may not be empty.", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void assignRolesIsEmpty() {
        var request = createRoleAssignmentRequest("alice_voperson_id", null);
        var error = assignRoles(request, adminToken, 400);
        assertEquals("roles may not be empty.", error.message);
    }

    private RoleAssignmentRequest createRoleAssignmentRequest(String userId, List<String> roles) {
        var request = new RoleAssignmentRequest();
        request.userId = userId;
        request.roles = roles;
        return request;
    }

    private InformativeResponse assignRoles(RoleAssignmentRequest request, String token, int expectedStatus) {
        var requestSpec = given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON);

        if (request != null) {
            requestSpec.body(request);
        }

        return requestSpec
                .put("/assign-roles")
                .then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract()
                .as(InformativeResponse.class);
    }
}
