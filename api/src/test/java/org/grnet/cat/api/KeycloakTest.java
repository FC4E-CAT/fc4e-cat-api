package org.grnet.cat.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.keycloak.client.KeycloakTestClient;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.grnet.cat.dtos.UpdateUserProfileDto;
import org.grnet.cat.dtos.UserProfileDto;
import org.grnet.cat.entities.Role;
import org.grnet.cat.repositories.CommentRepository;
import org.grnet.cat.repositories.KeycloakAdminRepository;
import org.grnet.cat.services.CommentService;
import org.grnet.cat.services.KeycloakAdminService;
import org.grnet.cat.services.PrincipleService;
import org.grnet.cat.services.UserService;
import org.grnet.cat.services.ValidationService;
import org.grnet.cat.services.assessment.JsonAssessmentService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.util.Collections;
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

    @Inject
    ObjectMapper objectMapper;
    @Inject
    PrincipleService principleService;

    @Inject
    CommentService commentService;

    @BeforeEach
    public void setup() {

        var mock = Mockito.mock(KeycloakAdminService.class);

        Mockito.doNothing().when(mock).addEntitlementsToUser(any(), any());
        Mockito.when(mock.getUserEntitlements(any())).thenReturn(Collections.emptyList());
        QuarkusMock.installMockForType(mock, KeycloakAdminService.class);

        commentService.deleteAll();
        jsonAssessmentService.deleteAll();
        validationService.deleteAll();
        principleService.deleteAll();
        userService.deleteAll();
    }

    protected UserProfileDto register(String username) {

        var role = new Role("identidied_id", "identified", "The identified role");

        var mock = Mockito.mock(KeycloakAdminRepository.class);
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
        update.email = username.concat("@admin.grnet.gr");

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
