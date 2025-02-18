package org.grnet.cat.api;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.KeycloakTest;
import org.grnet.cat.api.endpoints.SubjectsEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.subject.SubjectRequest;
import org.grnet.cat.dtos.subject.SubjectResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(SubjectsEndpoint.class)
public class SubjectsEndpointTest extends KeycloakTest {

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void deleteSubject() {
        var request = createSubjectRequest("id", "name", "type");
        var createdResponse = createSubject(request, validatedToken);

        var error = deleteSubject(String.valueOf(createdResponse.id), adminToken, 403);
        assertEquals("User not authorized to manage subject with ID " + createdResponse.id, error.message);

        var success = deleteSubject(String.valueOf(createdResponse.id), validatedToken, 200);
        assertEquals("Subject has been successfully deleted.", success.message);
    }

    private SubjectRequest createSubjectRequest(String id, String name, String type) {
        var request = new SubjectRequest();
        request.id = id;
        request.name = name;
        request.type = type;
        return request;
    }

    private SubjectResponse createSubject(SubjectRequest request, String token) {
        return given()
                .auth()
                .oauth2(token)
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(SubjectResponse.class);
    }

    private InformativeResponse deleteSubject(String subjectId, String token, int expectedStatus) {
        return given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .delete("/{id}", subjectId)
                .then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract()
                .as(InformativeResponse.class);
    }
}
