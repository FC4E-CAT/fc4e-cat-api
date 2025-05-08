package org.grnet.cat.api;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.grnet.cat.api.endpoints.AutomatedCheckEndpoint;
import org.grnet.cat.dtos.*;
import org.grnet.cat.services.arcc.ArccValidationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestHTTPEndpoint(AutomatedCheckEndpoint.class)
public class AutomatedCheckEndpointTest extends KeycloakTest {

    @Inject
    ArccValidationService arccValidationService;

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void testValidHttpsUrl() {
        var request = createValidHttpsRequest("https://google.com");
        var response = performCheckUrl(request, adminToken, 200, AutomatedTestResponse.class);
        assertEquals(200, response.testStatus.code);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void testInvalidHttpsUrl() {
        var request = createValidHttpsRequest("https://google1.com");
        var response = performCheckUrl(request, adminToken, 400, InformativeResponse.class);
        assertEquals("Failed to connect to the URL: " + request.url + ". IOException: " + request.url.replace("https://", ""), response.message);
        assertEquals(400, response.code);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void testEmptyUrl() {
        var request = new AutomatedCheckRequest();
        var error = performCheckUrl(request, adminToken, 400, InformativeResponse.class);
        assertEquals("url may not be empty.", error.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void testNoHttpsUrl() {
        var request = createValidHttpsRequest("http://google.com");
        var response = performCheckUrl(request, adminToken, 200, AutomatedTestResponse.class);
        assertEquals("Failed to connect to the URL: " + request.url + ". The URL is not a secure HTTPS connection.", response.testStatus.message);
        assertEquals(503, response.testStatus.code);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void testUnauthenticatedUser() {
        var request = createValidHttpsRequest("https://google.com");
        var error = performCheckUrl(request, getAccessToken("evald"), 403, InformativeResponse.class);
        assertEquals(403, error.code);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void testMd1aValid() {
        var request = createArccValidationRequest("https://meta.sram.surf.nl/metadata/proxy_sp.xml");
        var response = performValidation(request, adminToken, "MD-1a", 200);
        Assertions.assertTrue(response.isValid);
        assertEquals(200, response.code);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void testMd1b1Valid() {
        var request = createArccValidationRequest("https://meta.sram.surf.nl/metadata/proxy_sp.xml");
        var response = performValidation(request, adminToken, "MD-1a", 200);
        Assertions.assertTrue(response.isValid);
        assertEquals(200, response.code);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void testMd1b2Invalid_NoTelephoneNumber() {
        var request = createArccValidationRequest("https://meta.sram.surf.nl/metadata/proxy_sp.xml");
        var response = performValidation(request, adminToken, "MD-1b2", 200);
        assertEquals("MD-1b2 validation failed: No operational security TelephoneNumber found.", response.message);
        assertEquals(400, response.code);
    }

    private AutomatedCheckRequest createValidHttpsRequest(String url) {
        var request = new AutomatedCheckRequest();
        request.url = url;
        return request;
    }

    private ArccValidationRequest createArccValidationRequest(String metadataUrl) {
        var request = new ArccValidationRequest();
        request.metadataUrl = metadataUrl;
        return request;
    }

    private <T> T performCheckUrl(AutomatedCheckRequest request, String token, int expectedStatus, Class<T> responseType) {
        return given()
                .auth()
                .oauth2(token)
                .body(request)
                .contentType(ContentType.JSON)
                .post("/check-url")
                .then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract()
                .as(responseType);
    }

    private ArccValidationResponse performValidation(ArccValidationRequest request, String token, String testId, int expectedStatus) {
        return given()
                .auth()
                .oauth2(token)
                .body(request)
                .contentType(ContentType.JSON)
                .post("/validate-metadata/" + testId)
                .then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract()
                .as(ArccValidationResponse.class);
    }
}
