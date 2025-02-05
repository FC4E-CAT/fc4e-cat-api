package org.grnet.cat.api;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.UsersEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.UpdateUserProfileDto;
import org.grnet.cat.dtos.UserProfileDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(UsersEndpoint.class)
public class UsersEndpointTest extends KeycloakTest {

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void unauthorizedUser() {
        var error = performRegisterRequest("invalidToken", 401);
        assertEquals(401, error.code);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void userAlreadyExistsInTheDatabase() {
        var error = performRegisterRequest(getAccessToken("alice"), 409);
        assertEquals("User already exists in the database.", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void nonRegisterUserRequestsTheirUserProfile() {
        var error = performGetUserProfileRequest(getAccessToken("evald"), 403);
        assertEquals("User has not been registered on CAT service. User registration is a prerequisite for accessing this API resource.", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void updateMetadataRequestBodyIsEmpty() {
        var error = (InformativeResponse) performUpdateUserProfileRequest(null, aliceToken, 400);
        assertEquals("The request body is empty.", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void updateMetadataNameIsEmpty() {
        var update = createUpdateUserProfileDto(null, "foo", "foo@admin.grnet.gr", null);
        var error = (InformativeResponse) performUpdateUserProfileRequest(update, aliceToken, 400);
        assertEquals("name may not be empty.", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void updateMetadataEmailIsEmpty() {
        var update = createUpdateUserProfileDto("foo", "foo", null, null);
        var error = (InformativeResponse) performUpdateUserProfileRequest(update, aliceToken, 400);
        assertEquals("email may not be empty.", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void updateMetadataEmailIsNotValid() {
        var update = createUpdateUserProfileDto("foo", "foo", "foo.foo", null);
        var error = (InformativeResponse) performUpdateUserProfileRequest(update, aliceToken, 400);
        assertEquals("Please provide a valid email address.", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void updateMetadataSurnameIsEmpty() {
        var update = createUpdateUserProfileDto("foo", null, "foo@admin.grnet.gr", null);
        var error = (InformativeResponse) performUpdateUserProfileRequest(update, bobToken, 400);
        assertEquals("surname may not be empty.", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void updateMetadataNotValidOrcid() {
        var update = createUpdateUserProfileDto("foo", "foo", "foo@admin.grnet.gr", "la-la-la-la");
        var error = (InformativeResponse) performUpdateUserProfileRequest(update, aliceToken, 400);
        assertEquals("Not valid structure of the ORCID Identifier.", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void updateMetadataWithoutOrcid() {
        var update = createUpdateUserProfileDto("foo", "foo", "foo@admin.grnet.gr", null);
        var response = (UserProfileDto) performUpdateUserProfileRequest(update, aliceToken, 200);
        assertEquals("foo@admin.grnet.gr", response.email);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void updateMetadataWithOrcid() {
        var update = createUpdateUserProfileDto("foo", "foo", "foo@admin.grnet.gr", "0000-0002-1825-0097");
        var response = (UserProfileDto) performUpdateUserProfileRequest(update, aliceToken, 200);
        assertEquals("0000-0002-1825-0097", response.orcidId);
    }

    private InformativeResponse performRegisterRequest(String token, int expectedStatus) {
        return given()
                .auth()
                .oauth2(token)
                .post("/register")
                .then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract()
                .as(InformativeResponse.class);
    }

    private InformativeResponse performGetUserProfileRequest(String token, int expectedStatus) {
        return given()
                .auth()
                .oauth2(token)
                .get("/profile")
                .then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract()
                .as(InformativeResponse.class);
    }

    private UpdateUserProfileDto createUpdateUserProfileDto(String name, String surname, String email, String orcidId) {
        var dto = new UpdateUserProfileDto();
        dto.name = name;
        dto.surname = surname;
        dto.email = email;
        dto.orcidId = orcidId;
        return dto;
    }

    private Object performUpdateUserProfileRequest(UpdateUserProfileDto update, String token, int expectedStatus) {
        var requestSpec = given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON);

        if (update != null) {
            requestSpec.body(update);
        }

        var response = requestSpec
                .put("/profile")
                .then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract();

        if (expectedStatus == 200) {
            return response.as(UserProfileDto.class);
        } else {
            return response.as(InformativeResponse.class);
        }
    }

    private Object performUpdateUserProfileRequestNotValid(UpdateUserProfileDto update, String token, int expectedStatus) {
        var requestSpec = given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON);

        if (update != null) {
            requestSpec.body(update);
        }

        var response = requestSpec
                .put("/profile")
                .then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract();

        if (expectedStatus == 200) {
            return response.as(UserProfileDto.class);
        } else {
            return response.as(InformativeResponse.class);
        }
    }

}
