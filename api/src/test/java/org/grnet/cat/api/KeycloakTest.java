package org.grnet.cat.api;

import io.quarkus.test.keycloak.client.KeycloakTestClient;
import org.grnet.cat.dtos.InformativeResponse;

import static io.restassured.RestAssured.given;

public class KeycloakTest {

    KeycloakTestClient keycloakClient = new KeycloakTestClient();

    protected InformativeResponse register(String username){

        return given()
                .auth()
                .oauth2(getAccessToken(username))
                .basePath("/v1/users/")
                .post("/register")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);
    }

    protected String getAccessToken(String userName) {
        return keycloakClient.getAccessToken(userName);
    }
}
