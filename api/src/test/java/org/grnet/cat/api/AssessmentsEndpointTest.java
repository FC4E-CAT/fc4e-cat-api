package org.grnet.cat.api;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.endpoints.AssessmentsEndpoint;
import org.grnet.cat.dtos.*;
import org.grnet.cat.dtos.assessment.*;
import org.grnet.cat.dtos.assessment.registry.JsonRegistryAssessmentRequest;
import org.grnet.cat.dtos.assessment.registry.RegistryAssessmentDto;
import org.grnet.cat.dtos.assessment.registry.UserJsonRegistryAssessmentResponse;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.template.TemplateDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.io.IOException;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@QuarkusTest
@TestHTTPEndpoint(AssessmentsEndpoint.class)
@QuarkusTestResource(DatabaseTestResource.class)
public class AssessmentsEndpointTest extends KeycloakTest {

    private static UserJsonRegistryAssessmentResponse assessment;
    private static UserJsonRegistryAssessmentResponse publicAssessment;

    @BeforeAll
    public void initMakeValidation() throws IOException {
        makeValidation("validated", "pid_graph:B5CC396B");
        assessment = createRegistryAssessment(validatedToken);
        publicAssessment = createRegistryAssessment(validatedToken);
        var response = given()
                .auth()
                .oauth2(validatedToken)
                .basePath("/v2/assessments/")
                .put("/{id}/publish", publicAssessment.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getAssessment() {

        var fetchedAssessment = fetchAssessment(validatedToken, assessment.id);
        assertEquals(assessment.id, fetchedAssessment.id);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void accessAssessmentCreatedByOtherUser() {
        var errorResponse = fetchAssessmentNotValid(getAccessToken("bob"), assessment.id);

        assertEquals("You do not have permission to access this resource.", errorResponse.message);
    }

    @Test
    public void deleteRegistryAssessment() throws IOException {
        var assessment = createRegistryAssessment(validatedToken);
        var response = deleteAssessment(validatedToken, assessment.id, 200);

        assertEquals("Assessment has been successfully deleted.", response.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void createComment() {

        var commentRequest = new CommentRequestDto();
        commentRequest.text = "Create comment" + UUID.randomUUID();

        var commentResponse = addComment(validatedToken, assessment.id, commentRequest);
        assertEquals(commentRequest.text, commentResponse.text);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void createCommentNotAuthorized() {

        var commentRequest = new CommentRequestDto();
        commentRequest.text = "Unauthorized comment.";

        var errorResponse = addCommentNotPermitted(aliceToken, assessment.id, commentRequest);
        assertEquals("You do not have permission to access this resource.", errorResponse.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void getComments() {

        var commentRequest = new CommentRequestDto();
        commentRequest.text = "This is a test comment." + UUID.randomUUID();

        addComment(validatedToken, assessment.id, commentRequest);

        var comments = fetchComments(validatedToken, assessment.id);
        assertFalse(comments.getContent().isEmpty(), "Comments list should not be empty.");
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void updateComment() {

        var commentRequest = new CommentRequestDto();
        commentRequest.text = "Original comment." + UUID.randomUUID();

        var commentResponse = addComment(validatedToken, assessment.id, commentRequest);

        var updatedCommentRequest = new CommentRequestDto();
        updatedCommentRequest.text = "Updated comment.";

        var updatedComment = updateComment(validatedToken, assessment.id, commentResponse.id, updatedCommentRequest);
        assertEquals(updatedCommentRequest.text, updatedComment.text);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void deleteComment() {

        var commentRequest = new CommentRequestDto();
        commentRequest.text = "Comment to delete." + UUID.randomUUID();

        var commentResponse = addComment(validatedToken, assessment.id, commentRequest);

        var response = deleteComment(validatedToken, assessment.id, commentResponse.id, 200);
        assertEquals("Comment has been successfully deleted.", response.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void deleteCommentNotPermitted() {

        var commentRequest = new CommentRequestDto();
        commentRequest.text = "Comment to delete." + UUID.randomUUID();
        ;

        var commentResponse = addComment(validatedToken, assessment.id, commentRequest);

        var errorResponse = deleteComment(aliceToken, assessment.id, commentResponse.id, 403);
        assertEquals("You do not have permission to access this resource.", errorResponse.message);
    }


    private ValidationResponse approveValidation(Long valId) {

        var updateStatus = new UpdateValidationStatus();

        updateStatus.status = "APPROVED";

        return given()
                .auth()
                .oauth2(adminToken)
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

    private ValidationResponse makeValidation(String username, String actorId) {

        var response = makeValidationRequest(username, actorId);
        return approveValidation(response.id);
    }

    private ValidationResponse makeValidationRequest(String username, String actorId) {

        var request = new ValidationRequest();
        request.organisationRole = "Manager";
        request.organisationId = "00tjv0s33";
        request.organisationName = "Keimyung University";
        request.organisationSource = "ROR";
        request.organisationWebsite = "http://www.kmu.ac.kr/main.jsp";
        request.registryActorId = actorId;

        return given()
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
    }

    private UserJsonRegistryAssessmentResponse createRegistryAssessment(String token) throws IOException {
        var request = new JsonRegistryAssessmentRequest();
        request.assessmentDoc = makeRegistryJsonDoc();

        return given()
                .auth()
                .oauth2(token)
                .basePath("/v2/assessments")
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserJsonRegistryAssessmentResponse.class);
    }
    private UserJsonRegistryAssessmentResponse createRegistryPublicAssessment(String token) throws IOException {
        var request = new JsonRegistryAssessmentRequest();
        request.assessmentDoc = makeRegistryJsonDoc();


        var assessment= given()
                .auth()
                .oauth2(token)
                .basePath("/v2/assessments")
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserJsonRegistryAssessmentResponse.class);


        given()
                .auth()
                .oauth2(validatedToken)
                .basePath("/v2/assessments/")
                .put("/{id}/publish", assessment.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        return given()
                .auth()
                .oauth2(token)
                .basePath("/v2/assessments")
                .get("/{id}", assessment.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(UserJsonRegistryAssessmentResponse.class);


    }

    private UserJsonRegistryAssessmentResponse fetchAssessment(String token, String assessmentId) {
        return given()
                .auth()
                .oauth2(token)
                .basePath("/v2/assessments")
                .get("/{id}", assessmentId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(UserJsonRegistryAssessmentResponse.class);
    }

    private InformativeResponse fetchAssessmentNotValid(String token, String assessmentId) {
        return given()
                .auth()
                .oauth2(token)
                .basePath("/v2/assessments")
                .get("/{id}", assessmentId)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);
    }

    private InformativeResponse deleteAssessment(String token, String assessmentId, int expectedStatus) {
        return given()
                .auth()
                .oauth2(token)
                .basePath("/v2/assessments")
                .delete("/{id}", assessmentId)
                .then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract()
                .as(InformativeResponse.class);
    }

    private CommentResponseDto addComment(String token, String assessmentId, CommentRequestDto request) {
        return given()
                .auth()
                .oauth2(token)
                .body(request)
                .contentType(ContentType.JSON)
                .post("/{id}/comments", assessmentId)
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(CommentResponseDto.class);
    }

    private InformativeResponse addCommentNotPermitted(String token, String assessmentId, CommentRequestDto request) {
        return given()
                .auth()
                .oauth2(token)
                .body(request)
                .contentType(ContentType.JSON)
                .post("/{id}/comments", assessmentId)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);
    }

    private PageResource fetchComments(String token, String assessmentId) {
        return given()
                .auth()
                .oauth2(token)
                .get("/{id}/comments", assessmentId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(PageResource.class);
    }

    private CommentResponseDto updateComment(String token, String assessmentId, Long commentId, CommentRequestDto request) {
        return given()
                .auth()
                .oauth2(token)
                .body(request)
                .contentType(ContentType.JSON)
                .put("/{id}/comments/{comment-id}", assessmentId, commentId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(CommentResponseDto.class);
    }

    private InformativeResponse deleteComment(String token, String assessmentId, Long commentId, int expectedStatus) {
        return given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .delete("/{id}/comments/{comment-id}", assessmentId, commentId)
                .then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract()
                .as(InformativeResponse.class);
    }

    private TemplateDto makeJsonDoc(boolean published, Long actor) throws IOException {

        String doc = "{\n" +
                "    \"status\": \"PRIVATE\",\n" +
                "    \"published\": " + published + ",\n" +
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
                "      \"id\": " + actor + ",\n" +
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

    private RegistryAssessmentDto makeRegistryJsonDoc() throws IOException {

        String doc = "{\n" +
                "  \"name\": \"\",\n" +
                "  \"published\": false,\n" +
                "  \"timestamp\": \"\",\n" +
                "  \"status\": \"PRIVATE\",\n" +
                "  \"version\": \"\",\n" +
                "  \"actor\": {\n" +
                "    \"id\": \"pid_graph:B5CC396B\",\n" +
                "    \"name\": \"PID Owner (Role)\"\n" +
                "  },\n" +
                "  \"organisation\": {\n" +
                "    \"id\": \"00tjv0s33\",\n" +
                "    \"name\": \"test\"\n" +
                "  },\n" +
                "  \"subject\": {\n" +
                "    \"id\": \"1\",\n" +
                "    \"type\": \"PID POLICY \",\n" +
                "    \"name\": \"services pid policy\"\n" +
                "  },\n" +
                "  \"result\": {\n" +
                "    \"compliance\": null,\n" +
                "    \"ranking\": null\n" +
                "  },\n" +
                "  \"principles\": [\n" +
                "    {\n" +
                "      \"id\": \"P12\",\n" +
                "      \"name\": \"Viable, Trusted\",\n" +
                "      \"description\": \"The PID Policy concentrates on principles, desired results and governance which are designed to establish a viable, trusted PID infrastructure suitable for the long-term sustainability of the EOSC.\",\n" +
                "      \"criteria\": [\n" +
                "        {\n" +
                "          \"id\": \"C20\",\n" +
                "          \"name\": \"Openly Available\",\n" +
                "          \"description\": \"Services MUST be available to all researchers in the EU.\",\n" +
                "          \"imperative\": \"MUST\",\n" +
                "          \"metric\": {\n" +
                "            \"id\": \"M20\",\n" +
                "            \"name\": \"Openly Available Services\",\n" +
                "            \"type\": \"Binary-Binary\",\n" +
                "            \"benchmark_value\": 1,\n" +
                "            \"value\": null,\n" +
                "            \"result\": null,\n" +
                "            \"tests\": [\n" +
                "              {\n" +
                "                \"id\": \"T20\",\n" +
                "                \"name\": \"Services are Open\",\n" +
                "                \"description\": \"Services (Providers) need to supply public evidence of open availability of services.\",\n" +
                "                \"text\": \"Can you supply public evidence of open availability of your service?|Link to evidence:\",\n" +
                "                \"type\": \"Binary-Manual-Evidence\",\n" +
                "                \"value\": null,\n" +
                "                \"result\": null,\n" +
                "                \"evidence_url\": [" +
                "                 {\n" +
                "                    \"url\": \"http://google.gr\",\n" +
                "                    \"description\": \"search engine\"\n" +
                "                  }],\n" +
                "                \"params\": \"openData|evidence\",\n" +
                "                \"tool_tip\": \"PID kernel metadata should be openly available, except for sensitive elements|A document, web page, or publication describing the plan\"\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"P11\",\n" +
                "      \"name\": \"FAIR\",\n" +
                "      \"description\": \"The PID Policy should enable an environment of research practice, and services that satisfy the FAIR principles as appropriate for the particular domains of use. Central to the realisation of FAIR are FAIR Digital Objects and PIDs are core to the idea of FAIR Digital Objects, as highlighted in the Turning FAIR Into Reality report (FAIR Expert Group, 2018).\",\n" +
                "      \"criteria\": [\n" +
                "        {\n" +
                "          \"id\": \"C4\",\n" +
                "          \"name\": \"Maintenance\",\n" +
                "          \"description\": \"The PID owner SHOULD maintain PID attributes.\",\n" +
                "          \"imperative\": \"SHOULD\",\n" +
                "          \"metric\": {\n" +
                "            \"id\": \"M4\",\n" +
                "            \"name\": \"Owner Maintenance\",\n" +
                "            \"type\": \"Binary-Binary\",\n" +
                "            \"benchmark_value\": 1,\n" +
                "            \"value\": null,\n" +
                "            \"result\": null,\n" +
                "            \"tests\": [\n" +
                "              {\n" +
                "                \"id\": \"T4\",\n" +
                "                \"name\": \"Maintenance\",\n" +
                "                \"description\": \"A test to determine if the entity (PID) attributes are being maintained.\",\n" +
                "                \"text\": \"Do you maintain the metadata for your object as and when required?\",\n" +
                "                \"type\": \"Binary-Manual\",\n" +
                "                \"value\": null,\n" +
                "                \"result\": null,\n" +
                "                \"params\": \"metaMaintain\",\n" +
                "                \"tool_tip\": \"Owners maintain and update metadata as and when required\"\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"P1\",\n" +
                "      \"name\": \"Unambiguous (Ownership and Identification)\",\n" +
                "      \"description\": \"PID application depends on unambiguous ownership, proper maintenance, and unambiguous identification of the entity being referenced.\",\n" +
                "      \"criteria\": [\n" +
                "        {\n" +
                "          \"id\": \"C4\",\n" +
                "          \"name\": \"Maintenance\",\n" +
                "          \"description\": \"The PID owner SHOULD maintain PID attributes.\",\n" +
                "          \"imperative\": \"SHOULD\",\n" +
                "          \"metric\": {\n" +
                "            \"id\": \"M4\",\n" +
                "            \"name\": \"Owner Maintenance\",\n" +
                "            \"type\": \"Binary-Binary\",\n" +
                "            \"benchmark_value\": 1,\n" +
                "            \"value\": null,\n" +
                "            \"result\": null,\n" +
                "            \"tests\": [\n" +
                "              {\n" +
                "                \"id\": \"T4\",\n" +
                "                \"name\": \"Maintenance\",\n" +
                "                \"description\": \"A test to determine if the entity (PID) attributes are being maintained.\",\n" +
                "                \"text\": \"Do you maintain the metadata for your object as and when required?\",\n" +
                "                \"type\": \"Binary-Manual\",\n" +
                "                \"value\": false,\n" +
                "                \"result\": null,\n" +
                "                \"params\": \"metaMaintain\",\n" +
                "                \"tool_tip\": \"Owners maintain and update metadata as and when required\"\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"P8\",\n" +
                "      \"name\": \"Integrated\",\n" +
                "      \"description\": \"Services need to integrate well with European Research Infrastructures, but not at the exclusion of the broader research community.\",\n" +
                "      \"criteria\": [\n" +
                "        {\n" +
                "          \"id\": \"C20\",\n" +
                "          \"name\": \"Openly Available\",\n" +
                "          \"description\": \"Services MUST be available to all researchers in the EU.\",\n" +
                "          \"imperative\": \"MUST\",\n" +
                "          \"metric\": {\n" +
                "            \"id\": \"M20\",\n" +
                "            \"name\": \"Openly Available Services\",\n" +
                "            \"type\": \"Binary-Binary\",\n" +
                "            \"benchmark_value\": 1,\n" +
                "            \"value\": null,\n" +
                "            \"result\": null,\n" +
                "            \"tests\": [\n" +
                "              {\n" +
                "                \"id\": \"T20\",\n" +
                "                \"name\": \"Services are Open\",\n" +
                "                \"description\": \"Services (Providers) need to supply public evidence of open availability of services.\",\n" +
                "                \"text\": \"Can you supply public evidence of open availability of your service?|Link to evidence:\",\n" +
                "                \"type\": \"Binary-Manual-Evidence\",\n" +
                "                \"value\": null,\n" +
                "                \"result\": null,\n" +
                "                \"evidence_url\": [],\n" +
                "                \"params\": \"openData|evidence\",\n" +
                "                \"tool_tip\": \"PID kernel metadata should be openly available, except for sensitive elements|A document, web page, or publication describing the plan\"\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"P20\",\n" +
                "      \"name\": \"Preferred Reference to Entities\",\n" +
                "      \"description\": \"The policy should result in a future where PIDs can be used as the preferred method of referring to its assigned entity, where appropriate, alongside human-readable means e.g. the common name. Multiple PIDs may identify any given entity and users should be able to use whichever they are most comfortable with.\",\n" +
                "      \"criteria\": [\n" +
                "        {\n" +
                "          \"id\": \"C20\",\n" +
                "          \"name\": \"Openly Available\",\n" +
                "          \"description\": \"Services MUST be available to all researchers in the EU.\",\n" +
                "          \"imperative\": \"MUST\",\n" +
                "          \"metric\": {\n" +
                "            \"id\": \"M20\",\n" +
                "            \"name\": \"Openly Available Services\",\n" +
                "            \"type\": \"Binary-Binary\",\n" +
                "            \"benchmark_value\": 1,\n" +
                "            \"value\": null,\n" +
                "            \"result\": null,\n" +
                "            \"tests\": [\n" +
                "              {\n" +
                "                \"id\": \"T20\",\n" +
                "                \"name\": \"Services are Open\",\n" +
                "                \"description\": \"Services (Providers) need to supply public evidence of open availability of services.\",\n" +
                "                \"text\": \"Can you supply public evidence of open availability of your service?|Link to evidence:\",\n" +
                "                \"type\": \"Binary-Manual-Evidence\",\n" +
                "                \"value\": null,\n" +
                "                \"result\": null,\n" +
                "                \"evidence_url\": [],\n" +
                "                \"params\": \"openData|evidence\",\n" +
                "                \"tool_tip\": \"PID kernel metadata should be openly available, except for sensitive elements|A document, web page, or publication describing the plan\"\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ],\n" +
                "  \"assessment_type\": {\n" +
                "    \"id\": \"pid_graph:3E109BBA\",\n" +
                "    \"name\": \"EOSC PID Policy\"\n" +
                "  }\n" +
                "}\n";
        return objectMapper.readValue(doc, RegistryAssessmentDto.class);
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

    @Test
    public void publishUnpublish() throws IOException {

        //register("validated");
        //register("admin");
        //register("evald");

        //makeValidation("validated", "pid_graph:B5CC396B");

        var requestAssessment = new JsonRegistryAssessmentRequest();
        requestAssessment.assessmentDoc = makeRegistryJsonDoc();

        var assessment = given()
                .auth()
                .oauth2(validatedToken)
                .basePath("/v2/assessments")
                .body(requestAssessment)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserJsonRegistryAssessmentResponse.class);

        var response = given()
                .auth()
                .oauth2(validatedToken)
                .basePath("/v2/assessments/")
                .put("/{id}/publish", assessment.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        assertEquals(200, response.code);
        assertEquals("Assessment is published successfully", response.message);

        response = given()
                .auth()
                .oauth2(validatedToken)
                .basePath("/v2/assessments/")
                .put("/{id}/unpublish", assessment.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);
        assertEquals(200, response.code);
        assertEquals("Assessment is unpublished successfully", response.message);

        var error = given()
                .auth()
                .oauth2(adminToken)
                .basePath("/v2/assessments")
                .put("/{id}/publish", assessment.id)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("You do not have permission to access this resource.", error.message);

        error = given()
                .auth()
                .oauth2(adminToken)
                .basePath("/v2/assessments")
                .put("/{id}/unpublish", assessment.id)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("You do not have permission to access this resource.", error.message);

    }
    @Test
    public void getPublicAssessment() {
        var assessment = given()
                .basePath("/v2/assessments")
                .get("/public/{id}", publicAssessment.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(UserJsonRegistryAssessmentResponse.class);
        assertEquals(assessment.id, publicAssessment.id);
    }
}
