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
import org.grnet.cat.dtos.template.TemplateResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(AssessmentsEndpoint.class)
public class AssessmentsEndpointTest extends KeycloakTest {

    @Test
    public void createAssessment() throws ParseException {

        register("validated");
        register("admin");

        var validation = makeValidation("validated", 6L);
        var templateDto = fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.validationId = validation.id;
        request.templateId = templateDto.id;
        request.assessmentDoc = makeJsonDoc();

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
    public void createAssessmentNotAuthorizedNotOwner() throws ParseException {

        register("alice");
        register("admin");
        register("validated");

        var validation = makeValidation("alice", 6L);
        var templateDto = fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.validationId = validation.id;
        request.templateId = templateDto.id;
        request.assessmentDoc = makeJsonDoc();

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
    public void createAssessmentNotAuthorizedNotApproved() throws ParseException {

        register("alice");
        register("admin");
        register("validated");

        var validation = makeValidationRequest("alice", 6L);
        var templateDto = fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.validationId = validation.id;
        request.templateId = templateDto.id;
        request.assessmentDoc = makeJsonDoc();

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
    public void createAssessmentEmptyJson() throws ParseException {

        register("validated");
        register("admin");

        var validation = makeValidation("validated", 6L);
        var templateDto = fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.validationId = validation.id;
        request.templateId = templateDto.id;
        request.assessmentDoc = makeEmptyJsonDoc();

        var response = given()
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

        assertEquals(400, response.code);
    }

    @Test
    public void createAssessmentNotExistValidation() throws ParseException {

        register("validated");
        register("admin");

        var templateDto = fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.validationId = 2L;
        request.templateId = templateDto.id;
        request.assessmentDoc = makeJsonDoc();

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
    public void createAssessmentNotExistTemplate() throws ParseException {

        register("validated");
        register("admin");

        var validation = makeValidation("validated", 6L);

        var request = new JsonAssessmentRequest();
        request.validationId = validation.id;
        request.templateId = 2L;
        request.assessmentDoc = makeJsonDoc();

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
    public void createAssessmentMismatch() throws ParseException {

        register("validated");
        register("admin");

        var validation = makeValidation("validated", 1L);
        var templateDto = fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.validationId = validation.id;
        request.templateId = templateDto.id;
        request.assessmentDoc = makeJsonDoc();

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
    public void getAssessment() throws ParseException {

        register("validated");
        register("admin");

        var validation = makeValidation("validated", 6L);
        var templateDto = fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.validationId = validation.id;
        request.templateId = templateDto.id;
        request.assessmentDoc = makeJsonDoc();

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
    public void getAssessments() throws ParseException {

        register("validated");
        register("admin");

        var validation = makeValidation("validated", 6L);
        var templateDto = fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.validationId = validation.id;
        request.templateId = templateDto.id;
        request.assessmentDoc = makeJsonDoc();

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
    public void updateAssessment() throws ParseException {

        register("validated");
        register("admin");

        var validation=makeValidation("validated", 6L);
        var templateDto=fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.validationId = validation.id;
        request.templateId = templateDto.id;
        request.assessmentDoc = makeJsonDoc();

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
        assertEquals(json.toString(),updatedResponse.assessmentDoc.toString());
    }

    @Test
    public void updateAssessmentNotExists() throws ParseException {

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
    public void updateAssessmentNotAuthorizedUser() throws ParseException {

        register("validated");
        register("admin");
        register("alice");

        var validation=makeValidation("validated", 6L);
        var templateDto=fetchTemplateByActorAndType();

        var request = new JsonAssessmentRequest();
        request.validationId = validation.id;
        request.templateId = templateDto.id;
        request.assessmentDoc = makeJsonDoc();

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

    private ValidationResponse makeValidation(String username, Long actorId) {

        var response = makeValidationRequest(username, actorId);
        var approveResponse = approveValidation(response.id);
        return approveResponse;
    }

    private JSONObject makeJsonDoc() throws ParseException {
        String doc = "{\n" +
                "  \"id\": \"9994-9399-9399-0932\",\n" +
                "  \"status\": \"PRIVATE\",\n" +
                "  \"version\": \"1\",\n" +
                "  \"name\": \"first assessment\",\n" +
                "  \"timestamp\": \"2023-03-28T23:23:24Z\",\n" +
                "  \"subject\": {\n" +
                "    \"id\": \"1\",\n" +
                "    \"type\": \"PID POLICY \",\n" +
                "    \"name\": \"services pid policy\"\n" +
                "  },\n" +
                "  \"assessment_type\": \"eosc pid policy\",\n" +
                "  \"actor\": \"owner\",\n" +
                "  \"organisation\": {\n" +
                "    \"id\": \"1\",\n" +
                "    \"name\": \"test\"\n" +
                "  },\n" +
                "  \"result\": {\n" +
                "    \"compliance\": true,\n" +
                "    \"ranking\": 0.6\n" +
                "  },\n" +
                "  \"principles\": [\n" +
                "    {\n" +
                "      \"name\": \"Principle 1\",\n" +
                "      \"criteria\": [\n" +
                "        {\n" +
                "          \"name\": \"Measurement\",\n" +
                "          \"type\": \"optional\",\n" +
                "          \"metric\": {\n" +
                "            \"type\": \"number\",\n" +
                "            \"algorithm\": \"sum\",\n" +
                "            \"benchmark\": {\n" +
                "              \"equal_greater_than\": 1\n" +
                "            },\n" +
                "            \"value\": 2,\n" +
                "            \"result\": 1,\n" +
                "            \"tests\": [\n" +
                "              {\n" +
                "                \"type\": \"binary\",\n" +
                "                \"text\": \"Do you regularly maintain the metadata for your object\",\n" +
                "                \"value\": 1,\n" +
                "                \"evidence_url\": [\"www.in.gr\"]\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        JSONParser parser = new JSONParser();
        JSONObject docObj = (JSONObject) parser.parse(doc);
        return docObj;
    }
    private JSONObject makeJsonDocUpdated() throws ParseException {
        String doc = "{\n" +
                "  \"id\": \"9994-9399-9399-0932\",\n" +
                "  \"status\": \"PUBLICLY VIEWED\",\n" +
                "  \"version\": \"1\",\n" +
                "  \"name\": \"first assessment updated\",\n" +
                "  \"timestamp\": \"2023-03-28T23:23:24Z\",\n" +
                "  \"subject\": {\n" +
                "    \"id\": \"1\",\n" +
                "    \"type\": \"PID POLICY \",\n" +
                "    \"name\": \"services pid policy\"\n" +
                "  },\n" +
                "  \"assessment_type\": \"eosc pid policy\",\n" +
                "  \"actor\": \"owner\",\n" +
                "  \"organisation\": {\n" +
                "    \"id\": \"1\",\n" +
                "    \"name\": \"test\"\n" +
                "  },\n" +
                "  \"result\": {\n" +
                "    \"compliance\": true,\n" +
                "    \"ranking\": 0.6\n" +
                "  },\n" +
                "  \"principles\": [\n" +
                "    {\n" +
                "      \"name\": \"Principle 1\",\n" +
                "      \"criteria\": [\n" +
                "        {\n" +
                "          \"name\": \"Measurement\",\n" +
                "          \"type\": \"optional\",\n" +
                "          \"metric\": {\n" +
                "            \"type\": \"number\",\n" +
                "            \"algorithm\": \"sum\",\n" +
                "            \"benchmark\": {\n" +
                "              \"equal_greater_than\": 1\n" +
                "            },\n" +
                "            \"value\": 2,\n" +
                "            \"result\": 1,\n" +
                "            \"tests\": [\n" +
                "              {\n" +
                "                \"type\": \"binary\",\n" +
                "                \"text\": \"Do you regularly maintain the metadata for your object\",\n" +
                "                \"value\": 1,\n" +
                "                \"evidence_url\": [\"www.in.gr\"]\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        JSONParser parser = new JSONParser();
        JSONObject docObj = (JSONObject) parser.parse(doc);
        return docObj;
    }

    private JSONObject makeEmptyJsonDoc() throws ParseException {
        String doc = "{}";
        JSONParser parser = new JSONParser();
        JSONObject docObj = (JSONObject) parser.parse(doc);
        return docObj;
    }
}

