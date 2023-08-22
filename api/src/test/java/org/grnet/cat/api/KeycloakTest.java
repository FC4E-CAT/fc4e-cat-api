package org.grnet.cat.api;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.keycloak.client.KeycloakTestClient;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.grnet.cat.dtos.*;
import org.grnet.cat.entities.*;
import org.grnet.cat.repositories.*;
import org.grnet.cat.services.assessment.JsonAssessmentService;
import org.grnet.cat.services.UserService;
import org.grnet.cat.services.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
public class KeycloakTest {

    KeycloakTestClient keycloakClient = new KeycloakTestClient();

    @Inject
    UserService userService;

    @Inject
    ValidationService validationService;

    @Inject
    JsonAssessmentService jsonAssessmentService;

    @BeforeEach
    public void setup() {

        jsonAssessmentService.deleteAll();
        validationService.deleteAll();
        userService.deleteAll();
    }
        protected UserProfileDto register(String username) {

        var role = new Role("identidied_id", "identified", "The identified role");

        KeycloakAdminRepository mock = Mockito.mock(KeycloakAdminRepository.class);
        Mockito.when(mock.fetchUserRoles(any())).thenReturn(List.of(role));
        QuarkusMock.installMockForType(mock, KeycloakAdminRepository.class);

        given()
                .auth()
                .oauth2(getAccessToken(username))
                .basePath("/v1/users/")
                .post("/register")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserProfileDto.class);

        var update = new UpdateUserProfileDto();
        update.name = "foo";
        update.surname = "foo";
        update.email = "foo@admin.grnet.gr";


        var profile = given()
                .auth()
                .oauth2(getAccessToken(username))
                .body(update)
                .contentType(ContentType.JSON)
                .basePath("/v1/users/")
                .put("/profile")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(UserProfileDto.class);

        return profile;
    }



    protected String getAccessToken(String userName) {
        return keycloakClient.getAccessToken(userName);
    }

}
