package org.grnet.cat.api;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.endpoints.ValidationsEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.PromotionRequest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(ValidationsEndpoint.class)
public class ValidationsEndpointTest extends KeycloakTest {

    @Test
    public void validationRequestBodyIsEmpty() {

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

        var request = new PromotionRequest();
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

        var request = new PromotionRequest();
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

        var request = new PromotionRequest();
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

        var request = new PromotionRequest();
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

        var request = new PromotionRequest();
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
    public void validationActorIsEmpty() {

        var request = new PromotionRequest();
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

        var request = new PromotionRequest();
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
    public void validationSourceNotFound() {

        var request = new PromotionRequest();
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

        var request = new PromotionRequest();
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
    public void validation() {

        var request = new PromotionRequest();
        request.organisationRole = "Manager";
        request.organisationId = "https://ror.org/00tjv0s33";
        request.organisationName = "Keimyung University";
        request.organisationSource = "ROR";
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
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("User promotion request submitted.", response.message);
    }

    @Test
    public void validationAlreadyExists() {

        var request = new PromotionRequest();
        request.organisationRole = "Manager";
        request.organisationId = "https://ror.org/00tjv0s33";
        request.organisationName = "Keimyung University";
        request.organisationSource = "ROR";
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
                .statusCode(409)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("There is a promotion request for this user and organisation.", response.message);
    }
}
