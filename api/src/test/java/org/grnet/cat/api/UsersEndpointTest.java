package org.grnet.cat.api;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.keycloak.client.KeycloakTestClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.grnet.cat.api.endpoints.UsersEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.UserProfileDto;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(UsersEndpoint.class)
public class UsersEndpointTest {

    @ConfigProperty(name = "oidc.user.unique.id")
    public static String key;

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

    @Test
    public void getUserProfile() {

        var userProfile = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .get("/profile")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(UserProfileDto.class);

        assertEquals("Identified", userProfile.type);
    }

    @Test
    public void nonRegisterUserRequestsTheirUserProfile() {

        var informativeResponse = given()
                .auth()
                .oauth2(getAccessToken("bob"))
                .get("/profile")
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("User has not been registered on CAT service. User registration is a prerequisite for accessing this API resource.", informativeResponse.message);
    }

    protected String getAccessToken(String userName) {
        return keycloakClient.getAccessToken(userName);
    }
}
