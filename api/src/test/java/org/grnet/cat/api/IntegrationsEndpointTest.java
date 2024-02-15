package org.grnet.cat.api;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.grnet.cat.api.endpoints.IntegrationsEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.OrganisationResponseDto;
import org.grnet.cat.dtos.SourceResponseDto;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.entities.Organisation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(IntegrationsEndpoint.class)
public class IntegrationsEndpointTest extends KeycloakTest {

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

        assertEquals(2, response.length);
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
                .as(PageResource.class);

        assertEquals(1,response.getTotalElements());
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

    @Test
    public void fetchRorOrganisationByName() {

        register("alice");

        var response = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .get("/organisations/ROR/Keimyung University")
                .thenReturn();

        assertEquals(1, response.body().as(PageResource.class).getNumberOfPage());
    }


    @Test
    public void fetchRorOrganisationByNameLessThan2Chars() {

        register("alice");

        var response = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .get("/organisations/ROR/K")
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("Value must be at least 2 characters.", response.message);
    }
    @Test
    public void fetchOrganisationByRE3DATA() {

        register("alice");

        var response = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .get("/organisations/RE3DATA/00tjv0s33")
                .thenReturn();

        assertEquals(501, response.statusCode());

    }

}
