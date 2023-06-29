package org.grnet.cat.api;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;
import jakarta.inject.Inject;
import org.grnet.cat.api.endpoints.IntegrationsEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.OrganisationResponseDto;
import org.grnet.cat.dtos.SourceResponseDto;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.grnet.cat.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(IntegrationsEndpoint.class)
public class IntegrationsEndpointTest extends KeycloakTest {

    @Inject
    UserService userService;

    @BeforeEach
    public void setup(){

        userService.deleteAll();
    }

    @Test
    public void fetchAllIntegrationSources() {

        register("alice");

        var response = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .get("/organisations")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(SourceResponseDto[].class);

        assertEquals(3, response.length);
    }

    @Test
    public void fetchOrganisationBySourceAndId() {

        register("alice");

        var response = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .get("/organisations/ROR/00tjv0s33")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(OrganisationResponseDto.class);

        assertEquals("00tjv0s33", response.id);

        assertEquals("Keimyung University", response.name);

        assertEquals("http://www.kmu.ac.kr/main.jsp", response.website);
    }

    @Test
    public void nonRegisterUserRequestsOrganisation() {

        var informativeResponse = given()
                .auth()
                .oauth2(getAccessToken("bob"))
                .get("/organisations/ROR/00tjv0s33")
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("User has not been registered on CAT service. User registration is a prerequisite for accessing this API resource.", informativeResponse.message);
    }

    @Test
    public void fetchOrganisationBySourceAndIdWrongSource() {

        register("alice");

        var response = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .get("/organisations/rorA/00tjv0s33")
                .thenReturn();

        assertEquals(400, response.statusCode());

    }

    @Test
    public void fetchOrganisationBySourceAndIdNotFound() {

        register("alice");

        var response = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .get("/organisations/ROR/00tjv0s33A")
                .thenReturn();

        assertEquals(404, response.statusCode());
    }
}
