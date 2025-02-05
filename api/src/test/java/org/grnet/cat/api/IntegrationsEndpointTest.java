package org.grnet.cat.api;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.IntegrationsEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.SourceResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(IntegrationsEndpoint.class)
public class IntegrationsEndpointTest extends KeycloakTest {

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void fetchAllIntegrationSources() {
        var response = fetchSources(aliceToken);
        assertEquals(2, response.length);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void fetchOrganisationBySourceAndId() {
        var response = fetchOrganisation(aliceToken, "ROR", "00tjv0s33");
        assertEquals(1, response.getTotalElements());
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void nonRegisterUserRequestsOrganisation() {
        var error = fetchOrganisationUnauthorized("evald", "ROR", "00tjv0s33");
        assertEquals("User has not been registered on CAT service. User registration is a prerequisite for accessing this API resource.", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void fetchOrganisationBySourceAndIdWrongSource() {
        var response = fetchOrganisationInvalidSource(aliceToken, "rorA", "00tjv0s33");
        assertEquals(400, response.statusCode());
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void fetchOrganisationBySourceAndIdNotFound() {
        var response = fetchOrganisationInvalidSource(aliceToken, "ROR", "00tjv0s33A");
        assertEquals(404, response.statusCode());
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void fetchRorOrganisationByNameLessThan2Chars() {
        var error = fetchOrganisationInvalidName(aliceToken, "ROR", "K");
        assertEquals("Value must be at least 2 characters.", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void fetchOrganisationByRE3DATA() {
        var response = fetchOrganisationInvalidSource(aliceToken, "RE3DATA", "00tjv0s33");
        assertEquals(501, response.statusCode());
    }

    private SourceResponseDto[] fetchSources(String token) {
        return given()
                .auth()
                .oauth2(token)
                .get("/organisations")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(SourceResponseDto[].class);
    }

    private PageResource fetchOrganisation(String token, String source, String id) {
        return given()
                .auth()
                .oauth2(token)
                .get("/organisations/{source}/{id}", source, id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(PageResource.class);
    }

    private InformativeResponse fetchOrganisationUnauthorized(String username, String source, String id) {
        return given()
                .auth()
                .oauth2(getAccessToken(username))
                .get("/organisations/{source}/{id}", source, id)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);
    }

    private Response fetchOrganisationInvalidSource(String token, String source, String id) {
        return given()
                .auth()
                .oauth2(token)
                .get("/organisations/{source}/{id}", source, id)
                .thenReturn();
    }

    private InformativeResponse fetchOrganisationInvalidName(String token, String source, String name) {
        return given()
                .auth()
                .oauth2(token)
                .get("/organisations/{source}/{name}", source, name)
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .as(InformativeResponse.class);
    }
}
