// src/test/java/org/grnet/cat/api/KeycloakTest.java
package org.grnet.cat.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.keycloak.client.KeycloakTestClient;
import org.grnet.cat.dtos.UpdateUserProfileDto;
import org.grnet.cat.dtos.UserProfileDto;
import org.grnet.cat.entities.Role;
import org.grnet.cat.repositories.KeycloakAdminRepository;
import org.grnet.cat.repositories.registry.MetricDefinitionRepository;
import org.grnet.cat.repositories.registry.MotivationPrincipleRepository;
import org.grnet.cat.services.CommentService;
import org.grnet.cat.services.KeycloakAdminService;
import org.grnet.cat.services.UserService;
import org.grnet.cat.services.ValidationService;
import org.grnet.cat.services.assessment.JsonAssessmentService;
import org.grnet.cat.services.registry.CriterionService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Base test class for Keycloak-related tests.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Allows non-static @BeforeAll and @AfterAll
@QuarkusTest
public class KeycloakTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeycloakTest.class);

    @TestHTTPResource
    URI baseUri;

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
    CommentService commentService;

    @Inject
    CriterionService criterionService;

    @Inject
    MotivationPrincipleRepository motivationPrincipleRepository;

    @Inject
    MetricDefinitionRepository metricDefinitionRepository;

    //@BeforeEach
    protected String adminToken;
    protected String aliceToken;
    protected String bobToken;
    protected String validatedToken;
    protected String evaldToken;


    @BeforeAll
    public void setup() {
        RestAssured.baseURI = baseUri.toString();

        var mockAdminService = Mockito.mock(KeycloakAdminService.class);
        Mockito.doNothing().when(mockAdminService).addEntitlementsToUser(any(), any());
        Mockito.when(mockAdminService.getUserEntitlements(any())).thenReturn(Collections.emptyList());
        QuarkusMock.installMockForType(mockAdminService, KeycloakAdminService.class);

        var mockAdminRepository = Mockito.mock(KeycloakAdminRepository.class);
        Role role = new Role("identified_id", "identified", "The identified role");
        Mockito.when(mockAdminRepository.fetchUserRoles(any())).thenReturn(List.of(role));
        QuarkusMock.installMockForType(mockAdminRepository, KeycloakAdminRepository.class);

        commentService.deleteAll();
        jsonAssessmentService.deleteAll();
        validationService.deleteAll();
        userService.deleteAll();
        criterionService.deleteAll();
        motivationPrincipleRepository.removeAll();
        metricDefinitionRepository.removeAll();

        register("admin");
        register("alice");
        register("validated");
        register("bob");

        adminToken = getAccessToken("admin");
        aliceToken = getAccessToken("alice");
        validatedToken = getAccessToken("validated");
        evaldToken = getAccessToken("evald");
        bobToken = getAccessToken("bob");
    }

    protected UserProfileDto register(String username) {
        try {
            var registerResponse = given()
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
        } catch (Exception e) {
            if (e.getMessage().contains("409")) {
                return userService.getUserProfile(username);
            } else {
                throw e;
            }
        }
    }

    protected String getAccessToken(String userName) {
        return keycloakClient.getAccessToken(userName);
    }

}
