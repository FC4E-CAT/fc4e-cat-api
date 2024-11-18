package org.grnet.cat.api.registry;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.registry.TestEndpoint;
import org.grnet.cat.dtos.AutomatedTestDto;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.registry.test.CheckUrlRequestDto;
import org.grnet.cat.dtos.registry.test.TestRequestDto;
import org.grnet.cat.dtos.registry.test.TestResponseDto;
import org.grnet.cat.dtos.registry.test.TestUpdateDto;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.urlEncodingEnabled;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(TestEndpoint.class)
public class TestEndpointTest extends KeycloakTest {

    @Test
    public void getTestNotPermitted() {

        register("alice");

        var error = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .contentType(ContentType.JSON)
                .get("/{id}", "pid_graph:D8C4E63E")
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("You do not have permission to access this resource.", error.message);
    }

    @Test
    public void createTest() {

        register("admin");

        var request = new TestRequestDto();
        request.TES = "T12";
        request.labelTest = "Performance Test";
        request.descTest = "This Test measures performance.";

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post("/")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(TestResponseDto.class);

        assertEquals("Performance Test", response.labelTest);
        assertEquals("This Test measures performance.", response.descTest);
    }

    @Test
    public void getTest() {

        register("admin");

        var createRequest = new TestRequestDto();
        createRequest.TES = "T12";
        createRequest.labelTest = "Performance Test";
        createRequest.descTest = "This Test measures performance.";

        var createdTest = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(createRequest)
                .contentType(ContentType.JSON)
                .post("/")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(TestResponseDto.class);

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .get("/{id}", createdTest.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(TestResponseDto.class);

        assertEquals(response.TES, "T12");
    }

    @Test
    public void getTestNotFound() {

        register("admin");

        var error = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .get("/{id}", "notfound")
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("There is no Test with the following id: notfound", error.message);
    }

    @Test
    public void updateTest() {

        register("admin");

        var createRequest = new TestRequestDto();
        createRequest.TES = "T12";
        createRequest.labelTest = "Performance Test";
        createRequest.descTest = "This Test measures performance.";

        var createdTest = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(createRequest)
                .contentType(ContentType.JSON)
                .post("/")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(TestResponseDto.class);

        var updateRequest = new TestUpdateDto();
        updateRequest.TES = "T12-Updated";
        updateRequest.labelTest = "Updated Performance Test";
        updateRequest.descTest = "Updated description for performance test.";

        var updatedResponse = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(updateRequest)
                .contentType(ContentType.JSON)
                .put("/{id}", createdTest.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(TestResponseDto.class);

        assertEquals("T12-Updated", updatedResponse.TES);
        assertEquals("Updated Performance Test", updatedResponse.labelTest);
        assertEquals("Updated description for performance test.", updatedResponse.descTest);
    }

    @Test
    public void deleteTest() {

        register("admin");

        var createRequest = new TestRequestDto();
        createRequest.TES = "T12";
        createRequest.labelTest = "Performance Test";
        createRequest.descTest = "This Test measures performance.";

        var createdTest = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(createRequest)
                .contentType(ContentType.JSON)
                .post("/")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(TestResponseDto.class);

        given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .delete("/{id}", createdTest.id)
                .then()
                .assertThat()
                .statusCode(200);

        given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .get("/{id}", createdTest.id)
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void testValidHttpsUrl() {
        register("admin");

        var request=new CheckUrlRequestDto();
        request.url="https://google.com";
        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post("/check-automated-test/https-url")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(AutomatedTestDto.class);

        assertEquals(200,response.code);

    }

    @Test
    public void testInvalidHttpsUrl() {
        // Mock the response for invalid URL
        register("admin");
        var request=new CheckUrlRequestDto();
        request.url="https://google1.com";

        var error = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post("/check-automated-test/https-url")
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .as(InformativeResponse.class);
        assertEquals("Failed to connect to the URL: "+request.url+". IOException: "+request.url.replace("https://",""), error.message);
    }
    @Test
    public void testEmptyUrl() {
        // Mock the response for invalid URL
        register("admin");
        var request=new CheckUrlRequestDto();

        var error = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post("/check-automated-test/https-url")
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .as(InformativeResponse.class);
        assertEquals("url may not be empty.", error.message);
    }

    @Test
    public void testNoHttpsUrl() {
        // Mock the response for invalid URL
        register("admin");
        var request=new CheckUrlRequestDto();
        request.url="http://google.com";

        var error = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post("/check-automated-test/https-url")
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .as(InformativeResponse.class);
        assertEquals("Failed to connect to the URL: "+request.url+". The URL is not a secure HTTPS connection.", error.message);
    }
    @Test
    public void testUnauthenticatedUser() {
        // register("alice");
        var request=new CheckUrlRequestDto();
        request.url="https://google.com";

        var error = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post("/check-automated-test/https-url")
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);
        assertEquals(403, error.code);
    }

    @Test
    public void testForbiddenUser() {
        register("alice");
        var request=new CheckUrlRequestDto();
        request.url="https://google.com";

        var error = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .body(request)
                .post("/check-automated-test/https-url")
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);
        assertEquals(403, error.code);
    }


}