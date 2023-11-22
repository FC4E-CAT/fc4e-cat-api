package org.grnet.cat.api;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.endpoints.SubjectsEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.subject.SubjectRequest;
import org.grnet.cat.dtos.subject.SubjectResponse;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(SubjectsEndpoint.class)
public class SubjectsEndpointTest extends KeycloakTest {

    @Test
    public void deleteSubject(){

        register("validated");
        register("admin");

        var subject = new SubjectRequest();
        subject.id = "id";
        subject.name = "name";
        subject.type = "type";

        var response = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(subject)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(SubjectResponse.class);

        var error = given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .contentType(ContentType.JSON)
                .delete("/{id}", response.id)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("User not authorized to manage subject with ID " + response.id, error.message);

        var success = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .contentType(ContentType.JSON)
                .delete("/{id}", response.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("Subject has been successfully deleted.", success.message);
    }
}
