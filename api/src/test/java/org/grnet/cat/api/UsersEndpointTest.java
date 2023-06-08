package org.grnet.cat.api;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.keycloak.client.KeycloakTestClient;
import org.grnet.cat.api.endpoints.UsersEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(UsersEndpoint.class)
public class UsersEndpointTest {

    KeycloakTestClient keycloakClient = new KeycloakTestClient();

    @Test
    public void unauthorizedUser(){

        var notAuthenticatedResponse = given()
                    .auth()
                    .oauth2("invalidToken")
                    .post("/register")
                    .thenReturn();

            assertEquals(401, notAuthenticatedResponse.statusCode());
    }

    @Test
    public void registerUser(){

        var success = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .post("/register")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("User has been successfully registered on Cat Service.", success.message);
    }

    @Test
    public void userAlreadyExistsInTheDatabase(){

        var success = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .post("/register")
                .then()
                .assertThat()
                .statusCode(409)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("User already exists in the database.", success.message);
    }

    protected String getAccessToken(String userName) {
        return keycloakClient.getAccessToken(userName);
    }
}
