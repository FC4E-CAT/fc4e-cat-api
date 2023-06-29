package org.grnet.cat.api;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.grnet.cat.api.endpoints.ValidationsEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.UpdateValidationStatus;
import org.grnet.cat.dtos.ValidationRequest;
import org.grnet.cat.dtos.ValidationResponse;
import org.grnet.cat.services.UserService;
import org.grnet.cat.services.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(ValidationsEndpoint.class)
public class ValidationsEndpointTest extends KeycloakTest {

    @Inject
    ValidationService validationService;

    @Inject
    UserService userService;


    @BeforeEach
    public void setUp(){

        validationService.deleteAll();
        userService.deleteAll();
    }

    @Test
    public void validationRequestBodyIsEmpty() {

        register("alice");

        var response = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("The request body is empty.", response.message);
    }

    @Test
    public void validationRoleIsEmpty() {

        register("alice");

        var request = new ValidationRequest();
        request.organisationSource = "ROR";
        request.organisationName = "Keimyung University";
        request.organisationId = "https://ror.org/00tjv0s33";
        request.actorId = 5L;

        var response = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("organisation_role may not be empty.", response.message);
    }

    @Test
    public void validationNameIsEmpty() {

        register("alice");

        var request = new ValidationRequest();
        request.organisationRole = "Manager";
        request.organisationSource = "ROR";
        request.organisationId = "https://ror.org/00tjv0s33";
        request.actorId = 5L;

        var response = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("organisation_name may not be empty.", response.message);
    }

    @Test
    public void validationOrgIdIsEmpty() {

        register("alice");

        var request = new ValidationRequest();
        request.organisationRole = "Manager";
        request.organisationSource = "ROR";
        request.organisationName = "Keimyung University";
        request.actorId = 5L;

        var response = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("organisation_id may not be empty.", response.message);
    }

    @Test
    public void validationSourceIsEmpty() {

        register("alice");

        var request = new ValidationRequest();
        request.organisationRole = "Manager";
        request.organisationId = "https://ror.org/00tjv0s33";
        request.organisationName = "Keimyung University";
        request.actorId = 5L;

        var response = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("organisation_source may not be empty.", response.message);
    }

    @Test
    public void validationSourceIsNotValid() {

        register("alice");

        var request = new ValidationRequest();
        request.organisationRole = "Manager";
        request.organisationId = "https://ror.org/00tjv0s33";
        request.organisationName = "Keimyung University";
        request.organisationSource = "NOT_VALID";
        request.actorId = 5L;

        var response = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("The value NOT_VALID is not a valid organisation_source. Valid organisation_source values are: [RE3DATA, ROR, EOSC]", response.message);
    }

    @Test
    public void validationSourceNotFound() {

        register("alice");

        var request = new ValidationRequest();
        request.organisationRole = "Manager";
        request.organisationId = "https://ror.org/00tjv0s33";
        request.organisationName = "Keimyung University";
        request.organisationSource = "EOSC";
        request.organisationWebsite = "http://www.kmu.ac.kr/main.jsp";
        request.actorId = 5L;

        var response = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("Organisation https://ror.org/00tjv0s33, not found in EOSC", response.message);
    }

    @Test
    public void validationSourceNotSupported() {

        register("alice");

        var request = new ValidationRequest();
        request.organisationRole = "Manager";
        request.organisationId = "https://ror.org/00tjv0s33";
        request.organisationName = "Keimyung University";
        request.organisationSource = "RE3DATA";
        request.organisationWebsite = "http://www.kmu.ac.kr/main.jsp";
        request.actorId = 5L;

        var response = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(501)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("Source re3data is not supported.", response.message);
    }

    @Test
    public void validationActorIsEmpty() {

        register("alice");

        var request = new ValidationRequest();
        request.organisationRole = "Manager";
        request.organisationId = "https://ror.org/00tjv0s33";
        request.organisationName = "Keimyung University";
        request.organisationSource = "ROR";
        request.organisationWebsite = "http://www.kmu.ac.kr/main.jsp";

        var response = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("actor_id may not be empty.", response.message);
    }

    @Test
    public void validationActorIsNotFound() {

        register("alice");

        var request = new ValidationRequest();
        request.organisationRole = "Manager";
        request.organisationId = "https://ror.org/00tjv0s33";
        request.organisationName = "Keimyung University";
        request.organisationSource = "ROR";
        request.organisationWebsite = "http://www.kmu.ac.kr/main.jsp";
        request.actorId = 18L;

        var response = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("There is no Actor with the following id: "+18, response.message);
    }

    @Test
    public void validation() {

        register("alice");

        var request = new ValidationRequest();
        request.organisationRole = "Manager";
        request.organisationId = "https://ror.org/00tjv0s33";
        request.organisationName = "Keimyung University";
        request.organisationSource = "ROR";
        request.organisationWebsite = "http://www.kmu.ac.kr/main.jsp";
        request.actorId = 4L;

        var response = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(ValidationResponse.class);

        assertEquals("Manager", response.organisationRole);
    }

    @Test
    public void validationAlreadyExists() {

        register("alice");

        var request = new ValidationRequest();
        request.organisationRole = "Manager";
        request.organisationId = "https://ror.org/00tjv0s33";
        request.organisationName = "Keimyung University";
        request.organisationSource = "ROR";
        request.organisationWebsite = "http://www.kmu.ac.kr/main.jsp";
        request.actorId = 4L;

        given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .body(request)
                .contentType(ContentType.JSON)
                .post();

        var error = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(409)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("There is a promotion request for this user and organisation.", error.message);
    }

    @Test
    public void updateValidationRequestByAdmin() {

        register("alice");
        register("admin");

        var request = new ValidationRequest();
        request.organisationRole = "Manager";
        request.organisationId = "00tjv0s33";
        request.organisationName = "Keimyung University";
        request.organisationSource = "ROR";
        request.organisationWebsite = "http://www.kmu.ac.kr/main.jsp";
        request.actorId = 1L;

        var validation = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(ValidationResponse.class);

        var updateRequest = new ValidationRequest();
        updateRequest.organisationRole = "Project Manager";
        updateRequest.organisationId = "00tjv0s33";
        updateRequest.organisationName = "NTUA";
        updateRequest.organisationSource = "ROR";
        updateRequest.organisationWebsite = "http://www.kmu.ac.kr/main.jsp";
        updateRequest.actorId = 3L;

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .basePath("/v1/admin/validations")
                .body(updateRequest)
                .contentType(ContentType.JSON)
                .put("/{id}", validation.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(ValidationResponse.class);

        assertEquals("Project Manager", response.organisationRole);
        assertEquals("NTUA", response.organisationName);
        assertEquals(3L, response.actorId);
        assertEquals("ROR", updateRequest.organisationSource);
        assertEquals("00tjv0s33", updateRequest.organisationId);
    }

    @Test
    public void updateValidationRequestStatusByAdmin() {

        register("alice");
        register("admin");

        var request = new ValidationRequest();
        request.organisationRole = "Manager";
        request.organisationId = "00tjv0s33";
        request.organisationName = "Keimyung University";
        request.organisationSource = "ROR";
        request.organisationWebsite = "http://www.kmu.ac.kr/main.jsp";
        request.actorId = 2L;

        var validation = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(ValidationResponse.class);

        var updateRequest = new UpdateValidationStatus();
        updateRequest.status = "APPROVED";

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .basePath("/v1/admin/validations")
                .body(updateRequest)
                .contentType(ContentType.JSON)
                .put("/{id}/update-status", validation.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(ValidationResponse.class);

        assertEquals("APPROVED", response.status);
    }
}
