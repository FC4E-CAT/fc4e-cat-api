package org.grnet.cat.api;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.keycloak.client.KeycloakTestClient;
import jakarta.inject.Inject;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.services.UserService;
import org.grnet.cat.services.ValidationService;
import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class KeycloakTest {

    KeycloakTestClient keycloakClient = new KeycloakTestClient();

    @Inject
    UserService userService;

    @Inject
    ValidationService validationService;

    @BeforeEach
    public void setup(){

        validationService.deleteAll();
        userService.deleteAll();
    }


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
