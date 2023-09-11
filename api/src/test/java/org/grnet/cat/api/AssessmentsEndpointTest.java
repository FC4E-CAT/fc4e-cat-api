package org.grnet.cat.api;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.grnet.cat.api.endpoints.AssessmentsEndpoint;
import org.grnet.cat.dtos.*;
import org.grnet.cat.dtos.assessment.JsonAssessmentRequest;
import org.grnet.cat.dtos.assessment.JsonAssessmentResponse;
import org.grnet.cat.dtos.assessment.UpdateJsonAssessmentRequest;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.template.TemplateDto;
import org.grnet.cat.dtos.template.TemplateResponse;
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
        var templateDto = fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.validationId = validation.id;
        request.templateId = templateDto.id;
        request.assessmentDoc = makeJsonDoc(false);

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
                .as(JsonAssessmentResponse.class);

        assertEquals(validation.id, response.validationId);
        assertEquals(templateDto.id, response.templateId);
    }

    @Test
    public void createAssessmentNotAuthorizedNotOwner() throws IOException {

        register("alice");
        register("admin");
        register("validated");

        var validation = makeValidation("alice", 6L);
        var templateDto = fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.validationId = validation.id;
        request.templateId = templateDto.id;
        request.assessmentDoc = makeJsonDoc(false);

        var response = given()
                .auth()
                .oauth2(getAccessToken("validated"))
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
    public void createAssessmentNotAuthorizedNotApproved() throws IOException {

        register("alice");
        register("admin");
        register("validated");

        var validation = makeValidationRequest("alice", 6L);
        var templateDto = fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.validationId = validation.id;
        request.templateId = templateDto.id;
        request.assessmentDoc = makeJsonDoc(false);

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

        var templateDto = fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.validationId = 2L;
        request.templateId = templateDto.id;
        request.assessmentDoc = makeJsonDoc(false);

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

        var validation = makeValidation("validated", 6L);

        var request = new JsonAssessmentRequest();
        request.validationId = validation.id;
        request.templateId = 100L;
        request.assessmentDoc = makeJsonDoc(false);

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

        var validation = makeValidation("validated", 1L);
        var templateDto = fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.validationId = validation.id;
        request.templateId = templateDto.id;
        request.assessmentDoc = makeJsonDoc(false);

        var informativeResponse = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .body(request)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("Actor in Validation Request mismatches actor in Template.", informativeResponse.message);
    }

    @Test
    public void getAssessment() throws IOException {

        register("validated");
        register("admin");

        var validation = makeValidation("validated", 6L);
        var templateDto = fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.validationId = validation.id;
        request.templateId = templateDto.id;
        request.assessmentDoc = makeJsonDoc(false);

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
                .as(JsonAssessmentResponse.class);

        var response = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .get("/{id}", assessment.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(JsonAssessmentResponse.class);

        assertEquals(assessment.id, response.id);

        var error = given()
                .auth()
                .oauth2(getAccessToken("bob"))
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

        var validation = makeValidation("validated", 6L);
        var templateDto = fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.validationId = validation.id;
        request.templateId = templateDto.id;
        request.assessmentDoc = makeJsonDoc(false);

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
                .as(JsonAssessmentResponse.class);

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

        var validation=makeValidation("validated", 6L);
        var templateDto=fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.validationId = validation.id;
        request.templateId = templateDto.id;
        request.assessmentDoc = makeJsonDoc(false);

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
                .as(JsonAssessmentResponse.class);

        assertEquals(validation.id, response.validationId);
        assertEquals(templateDto.id, response.templateId);

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
                .as(JsonAssessmentResponse.class);

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

        var validation=makeValidation("validated", 6L);
        var templateDto=fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.validationId = validation.id;
        request.templateId = templateDto.id;
        request.assessmentDoc = makeJsonDoc(false);

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
                .as(JsonAssessmentResponse.class);

        assertEquals(validation.id, response.validationId);
        assertEquals(templateDto.id, response.templateId);

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

        var validation = makeValidation("validated", 6L);
        var templateDto = fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.validationId = validation.id;
        request.templateId = templateDto.id;
        request.assessmentDoc = makeJsonDoc(true);

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
                .as(JsonAssessmentResponse.class);

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

        var validation = makeValidation("validated", 6L);
        var templateDto = fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.validationId = validation.id;
        request.templateId = templateDto.id;
        request.assessmentDoc = makeJsonDoc(false);

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
                .as(JsonAssessmentResponse.class);

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

        var validation = makeValidation("validated", 6L);
        var templateDto = fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.validationId = validation.id;
        request.templateId = templateDto.id;
        request.assessmentDoc = makeJsonDoc(true);

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
                .as(JsonAssessmentResponse.class);

        var informativeResponse = given()
                .auth()
                .oauth2(getAccessToken("validated"))
                .contentType(ContentType.JSON)
                .delete("/{id}", assessment.id)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("It is not allowed to delete a published assessment.", informativeResponse.message);
    }

    @Test
    public void deleteAssessmentCreatedByOtherUser() throws IOException {

        register("validated");
        register("admin");
        register("evald");

        var validation = makeValidation("validated", 6L);
        var templateDto = fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.validationId = validation.id;
        request.templateId = templateDto.id;
        request.assessmentDoc = makeJsonDoc(true);

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
                .as(JsonAssessmentResponse.class);

        var informativeResponse = given()
                .auth()
                .oauth2(getAccessToken("evald"))
                .contentType(ContentType.JSON)
                .delete("/{id}", assessment.id)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        assertEquals("User not authorized to delete assessment with ID "+assessment.id, informativeResponse.message);
    }

    private ValidationResponse makeValidation(String username, Long actorId) {

        var response = makeValidationRequest(username, actorId);
        return approveValidation(response.id);
    }

    private TemplateDto makeJsonDoc(boolean published) throws IOException {

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
                "      \"id\": 6,\n" +
                "      \"name\": \"PID Owner\"\n" +
                "    },\n" +
                "    \"organisation\": {\n" +
                "      \"id\": \"1\",\n" +
                "      \"name\": \"test\"\n" +
                "    },\n" +
                "    \"result\": {\n" +
                "      \"compliance\": true,\n" +
                "      \"ranking\": 5\n" +
                "    },\n" +
                "    \"principles\": [\n" +
                "      {\n" +
                "        \"id\": \"P1\",\n" +
                "        \"name\": \"Principle 1\",\n" +
                "        \"description\": \"The PID owner SHOULD maintain PID attributes.\",\n" +
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
                "                  \"text\": \"Do you regularly maintain the metadata for your object\",\n" +
                "                  \"value\": false,\n" +
                "                  \"result\": 0,\n" +
                "                  \"guidance\": {\n" +
                "                  \"id\": \"G1\",\n" +
                "                  \"description\": \"In practice, evaluation is very difficult, due to two factors: \\\\n - It requires that a sample of millions of PID owners be evaluated across all services, and \\\\n - Some entities may never have to be maintained and are, despite years of non-maintenance, up to date. \\\\n A measure of the mean update frequency of PIDs across a specific service, and monitoring its change over time against a benchmark, may be the only realistic assessment mechanism.\"\n" +
                "                  },\n" +
                "                  \"evidence_url\": [\n" +
                "                    \"https://www.in.gr\"\n" +
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
                "      \"id\": \"1\",\n" +
                "      \"name\": \"test\"\n" +
                "    },\n" +
                "    \"result\": {\n" +
                "      \"compliance\": true,\n" +
                "      \"ranking\": 5\n" +
                "    },\n" +
                "    \"principles\": [\n" +
                "      {\n" +
                "        \"id\": \"P1\",\n" +
                "        \"name\": \"Principle 1\",\n" +
                "        \"description\": \"The PID owner SHOULD maintain PID attributes.\",\n" +
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
                "                  \"text\": \"Do you regularly maintain the metadata for your object\",\n" +
                "                  \"value\": false,\n" +
                "                  \"result\": 0,\n" +
                "                  \"guidance\": {\n" +
                "                  \"id\": \"G1\",\n" +
                "                  \"description\": \"In practice, evaluation is very difficult, due to two factors: \\\\n - It requires that a sample of millions of PID owners be evaluated across all services, and \\\\n - Some entities may never have to be maintained and are, despite years of non-maintenance, up to date. \\\\n A measure of the mean update frequency of PIDs across a specific service, and monitoring its change over time against a benchmark, may be the only realistic assessment mechanism.\"\n" +
                "                  },\n" +
                "                  \"evidence_url\": [\n" +
                "                    \"https://www.in.gr\"\n" +
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

