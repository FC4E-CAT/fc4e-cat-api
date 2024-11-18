package org.grnet.cat.api;

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
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(AssessmentsEndpoint.class)
public class AssessmentsEndpointTest extends KeycloakTest {

    @Test
    public void getAssessment() throws IOException {

        register("validated");
        register("admin");
        register("evald");

        makeValidation("validated", "pid_graph:B5CC396B");

        var requestAssessment = new JsonRegistryAssessmentRequest();
        requestAssessment.assessmentDoc = makeRegistryJsonDoc();

        var assessment = given()
                .auth()
                .oauth2(getAccessToken("validated"))
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
                .oauth2(getAccessToken("validated"))
                .basePath("/v2/assessments")
                .get("/{id}", assessment.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(UserJsonRegistryAssessmentResponse.class);

        assertEquals(assessment.id, response.id);

        var error = given()
                .auth()
                .oauth2(getAccessToken("evald"))
                .basePath("/v2/assessments")
                .get("/{id}", assessment.id)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("You do not have permission to access this resource.", error.message);
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

    private ValidationResponse makeValidationRequest(String username, String actorId) {

        var request = new ValidationRequest();
        request.organisationRole = "Manager";
        request.organisationId = "00tjv0s33";
        request.organisationName = "Keimyung University";
        request.organisationSource = "ROR";
        request.organisationWebsite = "http://www.kmu.ac.kr/main.jsp";
        request.registryActorId = actorId;

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

    @Test
    public void deleteRegistryAssessment() throws IOException {

        register("validated");
        register("admin");

        makeValidation("validated", "pid_graph:B5CC396B");

        var requestAssessment = new JsonRegistryAssessmentRequest();
        requestAssessment.assessmentDoc = makeRegistryJsonDoc();

        var assessment = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .basePath("/v2/assessments")
                .body(requestAssessment)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserJsonRegistryAssessmentResponse.class);

        var informativeResponse = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .contentType(ContentType.JSON)
                .basePath("/v2/assessments")
                .delete("/{id}", assessment.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("Assessment has been successfully deleted.", informativeResponse.message);
    }

    @Test
    public void accessAssessmentCreatedByOtherUser() throws IOException {

        register("validated");
        register("admin");
        register("bob");

        makeValidation("validated", "pid_graph:B5CC396B");

        var requestAssessment = new JsonRegistryAssessmentRequest();
        requestAssessment.assessmentDoc = makeRegistryJsonDoc();

        var assessment = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .basePath("/v2/assessments")
                .body(requestAssessment)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserJsonRegistryAssessmentResponse.class);

        var informativeResponse = given()
                .auth()
                .oauth2(getAccessToken("bob"))
                .contentType(ContentType.JSON)
                .basePath("/v2/assessments")
                .get("/{id}", assessment.id)
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

        makeValidation("validated", "pid_graph:B5CC396B");

        var requestAssessment = new JsonRegistryAssessmentRequest();
        requestAssessment.assessmentDoc = makeRegistryJsonDoc();

        var assessment = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .basePath("/v2/assessments")
                .body(requestAssessment)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserJsonRegistryAssessmentResponse.class);

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

        makeValidation("validated", "pid_graph:B5CC396B");

        var requestAssessment = new JsonRegistryAssessmentRequest();
        requestAssessment.assessmentDoc = makeRegistryJsonDoc();

        var assessment = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .basePath("/v2/assessments")
                .body(requestAssessment)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserJsonRegistryAssessmentResponse.class);

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

        makeValidation("validated", "pid_graph:B5CC396B");

        var requestAssessment = new JsonRegistryAssessmentRequest();
        requestAssessment.assessmentDoc = makeRegistryJsonDoc();

        var assessment = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .basePath("/v2/assessments")
                .body(requestAssessment)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserJsonRegistryAssessmentResponse.class);

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

        makeValidation("validated", "pid_graph:B5CC396B");

        var requestAssessment = new JsonRegistryAssessmentRequest();
        requestAssessment.assessmentDoc = makeRegistryJsonDoc();

        var assessment = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .basePath("/v2/assessments")
                .body(requestAssessment)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserJsonRegistryAssessmentResponse.class);

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

        makeValidation("validated", "pid_graph:B5CC396B");

        var requestAssessment = new JsonRegistryAssessmentRequest();
        requestAssessment.assessmentDoc = makeRegistryJsonDoc();

        var assessment = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .basePath("/v2/assessments")
                .body(requestAssessment)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserJsonRegistryAssessmentResponse.class);

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

        makeValidation("validated", "pid_graph:B5CC396B");

        var requestAssessment = new JsonRegistryAssessmentRequest();
        requestAssessment.assessmentDoc = makeRegistryJsonDoc();

        var assessment = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .basePath("/v2/assessments")
                .body(requestAssessment)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(UserJsonRegistryAssessmentResponse.class);

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

    private ValidationResponse makeValidation(String username, String actorId) {

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
}