package org.grnet.cat.api;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.endpoints.AssessmentsEndpoint;
import org.grnet.cat.dtos.*;
import org.grnet.cat.dtos.assessment.*;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.template.TemplateDto;
import org.grnet.cat.dtos.template.TemplateResponse;
import org.grnet.cat.entities.Comment;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(AssessmentsEndpoint.class)
public class AssessmentsEndpointTest extends KeycloakTest {

    @Test
    public void createAssessment() throws IOException {

        register("validated");
        register("admin");

        var validation = makeValidation("validated", 6L);

        var request = new JsonAssessmentRequest();
        request.assessmentDoc = makeJsonDoc(false, 6L);

        var response = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserJsonAssessmentResponse.class);

        assertEquals(validation.id, response.validationId);
    }

    @Test
    public void createAssessmentNotAuthorizedNotOwner() throws IOException {

        register("alice");
        register("admin");
        register("validated");

        makeValidation("alice", 6L);

        var request = new JsonAssessmentRequest();
        request.assessmentDoc = makeJsonDoc(false, 6L);

        var response = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(404, response.code);
    }

    @Test
    public void createAssessmentNotAuthorizedNotApproved() throws IOException {

        register("alice");
        register("admin");
        register("validated");

        makeValidationRequest("alice", 6L);

        var request = new JsonAssessmentRequest();
        request.assessmentDoc = makeJsonDoc(false, 6L);

        var response = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(403, response.code);
    }

    @Test
    public void createAssessmentNotExistValidation() throws IOException {

        register("validated");
        register("admin");

        var request = new JsonAssessmentRequest();
        request.assessmentDoc = makeJsonDoc(false, 6L);

        var response = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(404, response.code);
    }

    @Test
    public void createAssessmentNotExistTemplate() throws  IOException {

        register("validated");
        register("admin");

        makeValidation("validated", 6L);

        var request = new JsonAssessmentRequest();
        request.assessmentDoc = makeJsonDoc(false, 200L);

        var response = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(404, response.code);
    }

    @Test
    public void createAssessmentMismatch() throws IOException {

        register("validated");
        register("admin");

        makeValidation("validated", 1L);

        var request = new JsonAssessmentRequest();
        request.assessmentDoc = makeJsonDoc(false, 6L);

        var informativeResponse = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("There is no approved validation request.", informativeResponse.message);
    }

    @Test
    public void getAssessment() throws IOException {

        register("validated");
        register("admin");

        makeValidation("validated", 6L);
        fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.assessmentDoc = makeJsonDoc(false, 6L);

        var assessment = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserJsonAssessmentResponse.class);

        var response = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .get("/{id}", assessment.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(UserJsonAssessmentResponse.class);

        assertEquals(assessment.id, response.id);

        var error = given()
                .auth()
                .oauth2(getAccessToken("evald"))
                .get("/{id}", assessment.id)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("You do not have permission to access this resource.", error.message);
    }

    @Test
    public void getAssessments() throws IOException {

        register("validated");
        register("admin");

        makeValidation("validated", 6L);
        fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.assessmentDoc = makeJsonDoc(false, 6L);

        given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserJsonAssessmentResponse.class);

        var pageResource = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .get()
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(PageResource.class);

        assertEquals(1, pageResource.getTotalElements());
    }

    @Test
    public void updateAssessment() throws IOException {

        register("validated");
        register("admin");

        makeValidation("validated", 6L);

        var request = new JsonAssessmentRequest();
        request.assessmentDoc = makeJsonDoc(false, 6L);

        var response = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserJsonAssessmentResponse.class);

        var updateRequest = new UpdateJsonAssessmentRequest();
        updateRequest.assessmentDoc = makeJsonDocUpdated();

        var updatedResponse = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(updateRequest)
                .contentType(ContentType.JSON)
                .put("/{id}",response.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(UserJsonAssessmentResponse.class);

        var json = makeJsonDocUpdated();
        assertEquals(json.status , updatedResponse.assessmentDoc.status);
    }

    @Test
    public void updateAssessmentNotExists() throws IOException {

        register("validated");
        register("admin");

        var updateRequest = new UpdateJsonAssessmentRequest();
        updateRequest.assessmentDoc =makeJsonDocUpdated();

        var updatedResponse = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(updateRequest)
                .contentType(ContentType.JSON)
                .put("/{id}",1L)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

       assertEquals(404,updatedResponse.code);
    }

    @Test
    public void updateAssessmentNotAuthorizedUser() throws IOException {

        register("validated");
        register("admin");
        register("alice");

        makeValidation("validated", 6L);

        var request = new JsonAssessmentRequest();
        request.assessmentDoc = makeJsonDoc(false, 6L);

        var response = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserJsonAssessmentResponse.class);

        var updateRequest = new UpdateJsonAssessmentRequest();
        updateRequest.assessmentDoc =makeJsonDocUpdated();

        var updatedResponse = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .body(updateRequest)
                .contentType(ContentType.JSON)
                .put("/{id}",response.id)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(403,updatedResponse.code);
    }

    @Test
    public void getAssessmentNotExist() {

        register("validated");

        var response = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .get("/{id}", 8L)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("There is no Assessment with the following id: "+8, response.message);
    }

    private ValidationResponse approveValidation(Long valId) {

        var updateStatus = new UpdateValidationStatus();

        updateStatus.status = "APPROVED";

        return given()
                .auth()
                .oauth2(getAccessToken("admin"))
                .basePath("/v1/admin/validations")
                .contentType(ContentType.JSON)
                .body(updateStatus)
                .put("/{id}/update-status", valId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(ValidationResponse.class);

    }

    private ValidationResponse makeValidationRequest(String username, Long actorId) {

        var request = new ValidationRequest();
        request.organisationRole = "Manager";
        request.organisationId = "00tjv0s33";
        request.organisationName = "Keimyung University";
        request.organisationSource = "ROR";
        request.organisationWebsite = "http://www.kmu.ac.kr/main.jsp";
        request.actorId = actorId;

        var response = given()
                .auth()
                .oauth2(getAccessToken(username))
                .basePath("/v1/validations")
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(ValidationResponse.class);
        return response;
    }

    public TemplateResponse fetchTemplateByActorAndType() {

        var response = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .basePath("/v1/templates")
                .get("/by-type/{type-id}/by-actor/{actor-id}", 1L, 6L)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(TemplateResponse.class);
        return response;
    }

    @Test
    public void getPublishedAssessments() throws IOException {

        register("validated");
        register("admin");

        makeValidation("validated", 6L);
        fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.assessmentDoc = makeJsonDoc(true, 6L);

        given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserJsonAssessmentResponse.class);

        var response = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(request)
                .contentType(ContentType.JSON)
                .get("/by-type/{type-id}/by-actor/{actor-id}", 1L, 6L)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(PageResource.class);

        assertEquals(1, response.getTotalElements());
    }

    @Test
    public void deletePrivateAssessment() throws IOException {

        register("validated");
        register("admin");

        makeValidation("validated", 6L);
        fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.assessmentDoc = makeJsonDoc(false, 6L);

        var assessment = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserJsonAssessmentResponse.class);

        var informativeResponse = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .contentType(ContentType.JSON)
                .delete("/{id}", assessment.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("Assessment has been successfully deleted.", informativeResponse.message);
    }

    @Test
    public void deletePublishedAssessmentIsNotPermitted() throws IOException {

        register("validated");
        register("admin");

        makeValidation("validated", 6L);
        fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.assessmentDoc = makeJsonDoc(true, 6L);

        var assessment = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserJsonAssessmentResponse.class);

       var informativeResponse = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .contentType(ContentType.JSON)
                .delete("/{id}", assessment.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("Assessment has been successfully deleted.", informativeResponse.message);
    }

    @Test
    public void deleteAssessmentCreatedByOtherUser() throws IOException {

        register("validated");
        register("admin");
        register("bob");

        makeValidation("validated", 6L);
        fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.assessmentDoc = makeJsonDoc(true, 6L);

        var assessment = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserJsonAssessmentResponse.class);

        var informativeResponse = given()
                .auth()
                .oauth2(getAccessToken("bob"))
                .contentType(ContentType.JSON)
                .delete("/{id}", assessment.id)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("You do not have permission to access this resource.", informativeResponse.message);
    }
    @Test
    public void createComment() throws IOException {

        register("validated");
        register("admin");

        var validation = makeValidation("validated", 6L);

        var requestAssessment = new JsonAssessmentRequest();
        requestAssessment.assessmentDoc = makeJsonDoc(false, 6L);

        var assessment = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(requestAssessment)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserJsonAssessmentResponse.class);

        var commentRequest = new CommentRequestDto();
        commentRequest.text = "This is a test comment.";

        var responseComment = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(commentRequest)
                .contentType(ContentType.JSON)
                .post("/{id}/comments", assessment.id)
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(CommentResponseDto.class);

        assertEquals(commentRequest.text, responseComment.text);
    }

    @Test
    public void createCommentNotAuthorized() throws IOException {

        register("alice");
        register("admin");
        register("validated");

        var validation = makeValidation("validated", 6L);

        var requestAssessment = new JsonAssessmentRequest();
        requestAssessment.assessmentDoc = makeJsonDoc(false, 6L);

        var assessment = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(requestAssessment)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserJsonAssessmentResponse.class);

        var commentRequest = new CommentRequestDto();
        commentRequest.text = "This is a test comment.";

        var responseComment = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .body(commentRequest)
                .contentType(ContentType.JSON)
                .post("/{id}/comments", assessment.id)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(403, responseComment.code);
    }

    @Test
    public void getComments() throws IOException {

        register("validated");
        register("admin");

        makeValidation("validated", 6L);
        fetchTemplateByActorAndType();

        var requestAssessment = new JsonAssessmentRequest();
        requestAssessment.assessmentDoc = makeJsonDoc(false, 6L);

        var assessment = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(requestAssessment)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserJsonAssessmentResponse.class);


        var commentRequest = new CommentRequestDto();
        commentRequest.text = "This is a test comment.";

        given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(commentRequest)
                .contentType(ContentType.JSON)
                .post("/{id}/comments/", assessment.id)
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(CommentResponseDto.class);

        var pageResource = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .get("/{id}/comments", assessment.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(PageResource.class);

        assertEquals(1, pageResource.getTotalElements());
    }

    @Test
    public void updateComment() throws IOException {

        register("validated");
        register("admin");

        var validation = makeValidation("validated", 6L);

        var requestAssessment = new JsonAssessmentRequest();
        requestAssessment.assessmentDoc = makeJsonDoc(false, 6L);

        var assessment = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(requestAssessment)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserJsonAssessmentResponse.class);

        var commentRequest = new CommentRequestDto();
        commentRequest.text = "This is a test comment.";

        var responseComment = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(commentRequest)
                .contentType(ContentType.JSON)
                .post("/{id}/comments", assessment.id)
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(CommentResponseDto.class);

        var updatedCommentRequest = new CommentRequestDto();
        updatedCommentRequest.text = "This is an updated test comment.";

        var updatedCommentResponse = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(updatedCommentRequest)
                .contentType(ContentType.JSON)
                .put("/{id}/comments/{comment-id}", assessment.id, responseComment.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(CommentResponseDto.class);

        assertEquals(updatedCommentRequest.text, updatedCommentResponse.text);
    }

    @Test
    public void deleteComment() throws IOException {

        register("validated");
        register("admin");

        var validation = makeValidation("validated", 6L);

        var requestAssessment = new JsonAssessmentRequest();
        requestAssessment.assessmentDoc = makeJsonDoc(false, 6L);

        var assessment = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(requestAssessment)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserJsonAssessmentResponse.class);

        var commentRequest = new CommentRequestDto();
        commentRequest.text = "This is a test comment.";

        var responseComment = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(commentRequest)
                .contentType(ContentType.JSON)
                .post("/{id}/comments", assessment.id)
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(CommentResponseDto.class);

        var informativeResponse = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .contentType(ContentType.JSON)
                .delete("/{id}/comments/{comment-id}", assessment.id, responseComment.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("Comment has been successfully deleted.", informativeResponse.message);
    }

    @Test
    public void deleteCommentNotPermitted() throws IOException {

        register("validated");
        register("admin");
        register("alice");

        var validation = makeValidation("validated", 6L);

        var requestAssessment = new JsonAssessmentRequest();
        requestAssessment.assessmentDoc = makeJsonDoc(false, 6L);

        var assessment = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(requestAssessment)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserJsonAssessmentResponse.class);

        var commentRequest = new CommentRequestDto();
        commentRequest.text = "This is a test comment.";

        var responseComment = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(commentRequest)
                .contentType(ContentType.JSON)
                .post("/{id}/comments", assessment.id)
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(CommentResponseDto.class);

        var informativeResponse = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .contentType(ContentType.JSON)
                .delete("/comments/{id}", responseComment.id)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("You do not have permission to access this resource.", informativeResponse.message);
    }

    private ValidationResponse makeValidation(String username, Long actorId) {

        var response = makeValidationRequest(username, actorId);
        return approveValidation(response.id);
    }

    private TemplateDto makeJsonDoc(boolean published, Long actor) throws IOException {

        String doc = "{\n" +
                "    \"status\": \"PRIVATE\",\n" +
                "    \"published\": "+published+",\n" +
                "    \"version\": \"1\",\n" +
                "    \"name\": \"first assessment\",\n" +
                "    \"timestamp\": \"2023-03-28T23:23:24Z\",\n" +
                "    \"subject\": {\n" +
                "      \"id\": \"1\",\n" +
                "      \"type\": \"PID POLICY \",\n" +
                "      \"name\": \"services pid policy\"\n" +
                "    },\n" +
                "    \"assessment_type\": {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"eosc pid policy\"\n" +
                "    },\n" +
                "    \"actor\": {\n" +
                "      \"id\": "+actor+",\n" +
                "      \"name\": \"PID Owner\"\n" +
                "    },\n" +
                "    \"organisation\": {\n" +
                "      \"id\": \"00tjv0s33\",\n" +
                "      \"name\": \"test\"\n" +
                "    },\n" +
                "    \"result\": {\n" +
                "      \"compliance\": true,\n" +
                "      \"ranking\": 5\n" +
                "    },\n" +
                "    \"principles\": [\n" +
                "      {\n" +
                "        \"id\": \"P1\",\n" +
                "        \"name\": \"Application\",\n" +
                "        \"description\": \"PID application depends on unambiguous ownership, proper maintenance, and unambiguous identification of the entity being referenced.\",\n" +
                "        \"criteria\": [\n" +
                "          {\n" +
                "            \"id\": \"C4\",\n" +
                "            \"name\": \"Measurement\",\n" +
                "            \"description\": \"The PID owner SHOULD maintain PID attributes.\",\n" +
                "            \"imperative\": \"should\",\n" +
                "            \"metric\": {\n" +
                "              \"type\": \"number\",\n" +
                "              \"algorithm\": \"sum\",\n" +
                "              \"benchmark\": {\n" +
                "                \"equal_greater_than\": 1\n" +
                "              },\n" +
                "              \"value\": 1,\n" +
                "              \"result\": 1,\n" +
                "              \"tests\": [\n" +
                "                {\n" +
                "                   \"id\": \"T4\",\n" +
                "                  \"type\": \"binary\",\n" +
                "                 \"name\": \"Maintenance\",\n" +
                "                 \"description\": \"A test to determine if the entity (PID) attributes are being maintained.\",\n" +
                "                  \"text\": \"Do you regularly maintain the metadata for your object?\",\n" +
                "                  \"value\": false,\n" +
                "                  \"result\": 0,\n" +
                "                  \"guidance\": {\n" +
                "                  \"id\": \"G4\",\n" +
                "                  \"description\": \"Inventory of public evidence of processes and operations. Subjective evaluation of the completeness of the inventory compared to the infrastructures stated products and services.\"\n" +
                "                  },\n" +
                "                  \"evidence_url\": [\n" +
                "                 {\"url\": \"https://www.in.gr\"}\n" +
                "                  ]\n" +
                "                }\n" +
                "              ]\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }";
        return objectMapper.readValue(doc, TemplateDto.class);
    }

    private TemplateDto makeJsonDocUpdated() throws IOException {
        String doc = "{\n" +
                "    \"status\": \"PRIVATE\",\n" +
                "    \"published\": false,\n" +
                "    \"version\": \"1\",\n" +
                "    \"name\": \"first assessment\",\n" +
                "    \"timestamp\": \"2023-03-28T23:23:24Z\",\n" +
                "    \"subject\": {\n" +
                "      \"id\": \"1\",\n" +
                "      \"type\": \"PID POLICY \",\n" +
                "      \"name\": \"services pid policy\"\n" +
                "    },\n" +
                "    \"assessment_type\": {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"eosc pid policy\"\n" +
                "    },\n" +
                "    \"actor\": {\n" +
                "      \"id\": 6,\n" +
                "      \"name\": \"PID Owner\"\n" +
                "    },\n" +
                "    \"organisation\": {\n" +
                "      \"id\": \"00tjv0s33\",\n" +
                "      \"name\": \"test\"\n" +
                "    },\n" +
                "    \"result\": {\n" +
                "      \"compliance\": true,\n" +
                "      \"ranking\": 5\n" +
                "    },\n" +
                "    \"principles\": [\n" +
                "      {\n" +
                "        \"id\": \"P1\",\n" +
                "        \"name\": \"Application\",\n" +
                "        \"description\": \"The PID owner SHOULD maintain PID attributes.\",\n" +
                "        \"criteria\": [\n" +
                "          {\n" +
                "            \"id\": \"C4\",\n" +
                "            \"name\": \"Measurement\",\n" +
                "            \"description\": \"PID application depends on unambiguous ownership, proper maintenance, and unambiguous identification of the entity being referenced.\",\n" +
                "            \"imperative\": \"should\",\n" +
                "            \"metric\": {\n" +
                "              \"type\": \"number\",\n" +
                "              \"algorithm\": \"sum\",\n" +
                "              \"benchmark\": {\n" +
                "                \"equal_greater_than\": 1\n" +
                "              },\n" +
                "              \"value\": 1,\n" +
                "              \"result\": 1,\n" +
                "              \"tests\": [\n" +
                "                {\n" +
                "                   \"id\": \"T4\",\n" +
                "                  \"type\": \"binary\",\n" +
                "                 \"name\": \"Maintenance\",\n" +
                "                 \"description\": \"A test to determine if the entity (PID) attributes are being maintained.\",\n" +
                "                  \"text\": \"Do you regularly maintain the metadata for your object?\",\n" +
                "                  \"value\": false,\n" +
                "                  \"result\": 0,\n" +
                "                  \"guidance\": {\n" +
                "                  \"id\": \"G4\",\n" +
                "                  \"description\": \"Inventory of public evidence of processes and operations. Subjective evaluation of the completeness of the inventory compared to the infrastructures stated products and services.\"\n" +
                "                  },\n" +
                "                  \"evidence_url\": [\n" +
                "                   {\"url\": \"https://www.in.gr\"}\n" +
                "                  ]\n" +
                "                }\n" +
                "              ]\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }";

        return objectMapper.readValue(doc, TemplateDto.class);
    }
}

