package org.grnet.cat.api;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.grnet.cat.api.endpoints.ZenodoEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.UpdateValidationStatus;
import org.grnet.cat.dtos.ValidationRequest;
import org.grnet.cat.dtos.ValidationResponse;
import org.grnet.cat.dtos.assessment.ZenodoAssessmentInfoResponse;
import org.grnet.cat.dtos.assessment.registry.JsonRegistryAssessmentRequest;
import org.grnet.cat.dtos.assessment.registry.RegistryAssessmentDto;
import org.grnet.cat.dtos.assessment.registry.UserJsonRegistryAssessmentResponse;
import org.grnet.cat.enums.ZenodoState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(ZenodoEndpoint.class)
@QuarkusTestResource(DatabaseTestResource.class)
public class ZenodoEndpointTest extends KeycloakTest {

   private static UserJsonRegistryAssessmentResponse assessment;
    private static UserJsonRegistryAssessmentResponse publicAssessment;

    @BeforeAll
    public void initMakeValidation() throws IOException {
        makeValidation("validated", "pid_graph:B5CC396B");
        assessment = createRegistryAssessment(validatedToken);
        publicAssessment = createRegistryPublicAssessment(validatedToken);
    }

    @BeforeEach
    public void removeZenodoAssessments() {
        zenodoAssessmentInfoRepository.removeAll();
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


        var assessment = given()
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

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void testPublishZenodoAssessment_Success() throws IOException, InterruptedException {

        //  zenodoAssessmentInfoRepository.removeAll();
        //register(validatedToken);
     //   assessment = createRegistryPublicAssessment(validatedToken);
      //  assessment.published = true;
        var pdf = generateValidPdf();
        var response = given()
                .auth()
                .oauth2(validatedToken)
                .body(pdf)
                .contentType("application/octet-stream")
                .post("/publish/assessment/{id}", publicAssessment.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);
        //  String expectedMessage = "Process of uploading assessment with ID: " + assessment.id + " has started...";
        var zenodAssessmentInfo = given()
                .auth()
                .oauth2(validatedToken)
                .get("/assessment/{id}", publicAssessment.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(ZenodoAssessmentInfoResponse.class);


        assertEquals(ZenodoState.PROCESS_COMPLETED.getType(), zenodAssessmentInfo.getZenodoState());
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void testPublishZenodoAssessment_notPublished() throws IOException {

        //zenodoAssessmentInfoRepository.removeAll();
        //register(validatedToken);
       // var assessment = createRegistryAssessment(validatedToken);
        //assessment.published = true;

        var response = given()
                .auth()
                .oauth2(validatedToken)
                .body(generateValidPdf())
                .contentType("application/octet-stream")
                .post("/publish/assessment/{id}", assessment.id)
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .as(InformativeResponse.class);

        String expectedMessage = "Action not permitted for assessment:  " + assessment.id;
        assertEquals(expectedMessage, response.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void testPublishZenodoAssessment_ForbiddenAccess() throws IOException {

        //zenodoAssessmentInfoRepository.removeAll();
        //register(validatedToken);
       // var assessment = createRegistryPublicAssessment(validatedToken);
        //assessment.published = true;

        var response = given()
                .auth()
                .oauth2(adminToken)
                .body(generateValidPdf())
                .contentType("application/octet-stream")
                .post("/publish/assessment/{id}", publicAssessment.id)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .as(InformativeResponse.class);

        String expectedMessage = "You do not have permission to access this resource.";
        assertEquals(expectedMessage, response.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void testPublishZenodoAssessment_AlreadyPublishedInZenodo() throws IOException {

        //zenodoAssessmentInfoRepository.removeAll();
        // motivationAssessmentRepository.removeAll();
        //register(validatedToken);
      //  var assessment = createRegistryPublicAssessment(validatedToken);
       // assessment.published = true;

        var response = given()
                .auth()
                .oauth2(validatedToken)
                .body(generateValidPdf())
                .contentType("application/octet-stream")
                .post("/publish/assessment/{id}", publicAssessment.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(InformativeResponse.class);

        var zenodoAssessment = given()
                .auth()
                .oauth2(validatedToken)
                .contentType(ContentType.JSON)
                .get("/assessment/{id}", publicAssessment.id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(ZenodoAssessmentInfoResponse.class);


        var response2 = given()
                .auth()
                .oauth2(validatedToken)
                .body(generateValidPdf())
                .contentType("application/octet-stream")
                .post("/publish/assessment/{id}", publicAssessment.id)
                .then()
                .assertThat()
                .statusCode(500)
                .extract()
                .as(InformativeResponse.class);
        String isPublished = zenodoAssessment.getIsPublished() ? "PUBLISHED" : "DRAFT";

        String expectedMessage = "Assessment with ID: " + publicAssessment.id + " is already in Zenodo under deposit with ID: " + zenodoAssessment.getDepositId() + " and it's publication status is: " + isPublished;

        assertEquals(expectedMessage, response2.message);
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void testPublishZenodoAssessment_notValidPDF() throws IOException {
        //zenodoAssessmentInfoRepository.removeAll();

        //register(validatedToken);
       // var assessment = createRegistryPublicAssessment(validatedToken);
       // assessment.published = true;
        var invalidPdf = new byte[]{0x00, 0x01, 0x02};
        var response = given()
                .auth()
                .oauth2(validatedToken)
                .body(invalidPdf)
                .contentType("application/octet-stream")
                .post("/publish/assessment/{id}", publicAssessment.id)
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .as(InformativeResponse.class);

        String expectedMessage = "Invalid PDF file format.";
        assertEquals(expectedMessage, response.message);
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

    public static byte[] generateValidPdf() throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.newLineAtOffset(100, 700);
        contentStream.showText("This is a valid PDF file for testing.");
        contentStream.endText();
        contentStream.close();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.save(byteArrayOutputStream);
        document.close();

        return byteArrayOutputStream.toByteArray();
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


}

