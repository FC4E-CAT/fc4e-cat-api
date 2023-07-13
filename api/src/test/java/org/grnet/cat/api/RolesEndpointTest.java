package org.grnet.cat.api;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.endpoints.RolesEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.RoleAssignmentRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(RolesEndpoint.class)
public class RolesEndpointTest extends KeycloakTest{

    @Test
    public void assignRolesNotPermitted(){

        register("alice");

        var request = new RoleAssignmentRequest();

        var response = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .contentType(ContentType.JSON)
                .body(request)
                .put("/assign-roles")
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("You do not have permission to access this resource.", response.message);
    }

    @Test
    public void assignRolesRequestBodyIsEmpty(){

        register("admin");

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .put("/assign-roles")
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("The request body is empty.", response.message);
    }

    @Test
    public void assignRolesUserIdIsEmpty(){

        register("admin");

        var request = new RoleAssignmentRequest();
        request.roles = List.of("validated");

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .body(request)
                .put("/assign-roles")
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("user_id may not be empty.", response.message);
    }

    @Test
    public void assignRolesIsEmpty(){

        register("admin");
        var alice = register("alice");

        var request = new RoleAssignmentRequest();
        request.userId = alice.id;

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .body(request)
                .put("/assign-roles")
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("roles may not be empty.", response.message);
    }
}
