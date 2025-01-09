package org.grnet.cat.api;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.grnet.cat.api.endpoints.AutomatedCheckEndpoint;
import org.grnet.cat.dtos.*;
import org.grnet.cat.services.ArccValidationService;
import org.grnet.cat.validators.XmlMetadataValidator.MetadataValidationFactory;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import static io.restassured.RestAssured.given;
import static io.smallrye.common.constraint.Assert.assertFalse;
import static io.smallrye.common.constraint.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.InputStream;


@QuarkusTest
@TestHTTPEndpoint(AutomatedCheckEndpoint.class)
public class AutomatedCheckEndpointTest extends KeycloakTest {

    @Inject
    ArccValidationService arccValidationService;

    @Test
    public void testValidHttpsUrl() {
        register("admin");

        var request=new AutomatedCheckRequest();
        request.url="https://google.com";
        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post("/check-url")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(AutomatedCheckResponse.class);

        assertEquals(200,response.code);

    }

    @Test
    public void testInvalidHttpsUrl() {
        // Mock the response for invalid URL
        register("admin");
        var request=new AutomatedCheckRequest();
        request.url="https://google1.com";

        var error = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post("/check-url")
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
        var request=new AutomatedCheckRequest();

        var error = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post("/check-url")
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
        var request=new AutomatedCheckRequest();
        request.url="http://google.com";

        var error = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post("/check-url")
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
        var request=new AutomatedCheckRequest();
        request.url="https://google.com";

        var error = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post("/check-url")
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);
        assertEquals(403, error.code);
    }

@Test
public void testMd1aValid() {
    register("admin");

    var request = new ArccValidationRequest();
   // request.testId = "pid_graph:333489E8"; // MD-1a Test ID
    request.metadataUrl = "https://meta.sram.surf.nl/metadata/proxy_sp.xml"; // Replace with actual accessible path or use in-memory XML

    var response = given()
            .auth()
            .oauth2(getAccessToken("admin"))
            .body(request)
            .contentType(ContentType.JSON)
            .post("/validate-metadata/MD-1a")
            .then()
            .assertThat()
            .statusCode(200)
            .extract()
            .as(ArccValidationResponse.class);


    assertEquals(true, response.isValid);
    assertEquals(200, response.code);
  }

    @Test
    public void testMd1b1Valid() {
        register("admin");

        var request = new ArccValidationRequest();
      //  request.testId = "pid_graph:391489E8"; // MD-1b1 Test ID
        request.metadataUrl = "https://meta.sram.surf.nl/metadata/proxy_sp.xml";

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post("/validate-metadata/MD-1a")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(ArccValidationResponse.class);
        assertEquals(true, response.isValid);
        assertEquals(200, response.code);

   }

    @Test
    public void testMd1b2Invalid_NoTelephoneNumber() {
        register("admin");
        var request = new ArccValidationRequest();
        request.metadataUrl = "https://meta.sram.surf.nl/metadata/proxy_sp.xml";

        var response = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .body(request)
                .contentType(ContentType.JSON)
                .post("/validate-metadata/MD-1b2")
                .then()
                .assertThat()
                .statusCode(500)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("MD-1b2 validation failed: No operational security TelephoneNumber found.", response.message);

    }
}