package org.grnet.cat.api.endpoints;

import io.quarkus.arc.properties.IfBuildProperty;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.grnet.cat.api.filters.Registration;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.assessment.zenodo.ZenodoDepositResponse;
import org.grnet.cat.repositories.MotivationAssessmentRepository;
import org.grnet.cat.services.zenodo.ZenodoService;
import org.grnet.cat.utils.Utility;

import java.io.IOException;

@Path("/v2/zenodo")
@IfBuildProperty(name = "zenodo.enabled", stringValue = "true") // Conditional exposure in Swagger
public class ZenodoEndpoint {

    @ConfigProperty(name = "api.server.url")
    String serverUrl;

    @Inject
    ZenodoService zenodoService;

    @Inject
    Utility utility;


    @Tag(name = "Zenodo")
    @Operation(
            summary = "Zenodo publish an assessment.",
            description = "Allows a user to publish an assessment to zenodo.")
    @APIResponse(
            responseCode = "200",
            description = "Request to publish assessment to zenodo submitted  successfully.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "400",
            description = "Invalid request payload.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "401",
            description = "User has not been authenticated.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "403",
            description = "Not permitted.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "404",
            description = "Assessment not found.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @SecurityRequirement(name = "Authentication")
    @POST
    @Path("/publish/assessment/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM) // This consumes binary data

    @Registration
    public Response publishZenodoAssessment(@Parameter(
                                                    description = "The ID of the assessment to publish to zenodo.",
                                                    required = true,
                                                    example = "c242e43f-9869-4fb0-b881-631bc5746ec0",
                                                    schema = @Schema(type = SchemaType.STRING)) @PathParam("id")
                                            @Valid @NotFoundEntity(repository = MotivationAssessmentRepository.class, message = "There is no Assessment with the following id:") String assessmentId,

                                            byte[] pdfFile) {

        if (!isValidPdf(pdfFile)) {
            throw new BadRequestException("Invalid PDF file format.");
        }
        var message = zenodoService.publishAssessment(assessmentId, pdfFile, utility.getUserUniqueIdentifier());
        var response = new InformativeResponse();
        response.code = 202;
        response.message = message;
        return Response.ok(response).build();
    }

    @Tag(name = "Zenodo")
    @Operation(
            summary = "Get a Zenodo  assessment.",
            description = "Allows a user to view information of an assessment in zenodo, as it is stored to CAT service.")
    @APIResponse(
            responseCode = "200",
            description = "Information of zenodo assessment, in CAT service, displayed successfully.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "400",
            description = "Invalid request payload.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "401",
            description = "User has not been authenticated.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "403",
            description = "Not permitted.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "404",
            description = "Assessment not found.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @SecurityRequirement(name = "Authentication")
    @GET
    @Path("/assessment/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON) // This consumes binary data
   // @Registration
    public Response getZenodoAssessment(@Parameter(
            description = "The ID of the assessment uploaded to zenodo.",
            required = true,
            example = "c242e43f-9869-4fb0-b881-631bc5746ec0",
            schema = @Schema(type = SchemaType.STRING)) @PathParam("id")
                                        @Valid @NotFoundEntity(repository = MotivationAssessmentRepository.class, message = "There is no Assessment with the following id:") String assessmentId) throws IOException, InterruptedException {

        var response = zenodoService.getAssessment(assessmentId,utility);
        return Response.ok().entity(response).build();
    }

    @Tag(name = "Zenodo")
    @Operation(
            summary = "Get Info about a deposit in Zenodo.",
            description = "Allows a user to view deposit info  existing in zenodo, as the info exist in the CAT service.")
    @APIResponse(
            responseCode = "200",
            description = "Zenodo deposit information displayed successfully.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = ZenodoDepositResponse.class)))
    @APIResponse(
            responseCode = "400",
            description = "Invalid request payload.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "401",
            description = "User has not been authenticated.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "403",
            description = "Not permitted.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "404",
            description = "Assessment not found.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @SecurityRequirement(name = "Authentication")
    @GET
    @Path("/deposit/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
   // @Registration
    public Response getZenodoDeposit(@Parameter(
            description = "The ID of the deposit existing in zenodo.",
            required = true,
            example = "13467",
            schema = @Schema(type = SchemaType.STRING)) @PathParam("id")
                                     String depositId) {
        var response = zenodoService.getDeposit(depositId, utility);
        return Response.ok().entity(response).build();
    }

    @Tag(name = "Zenodo")
    @Operation(
            summary = "Publish a Zenodo  deposit.",
            description = "Allows a user to publish a specific deposit to Zenodo.")
    @APIResponse(
            responseCode = "200",
            description = "Assessment published successfully.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "400",
            description = "Invalid request payload.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "401",
            description = "User has not been authenticated.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "403",
            description = "Not permitted.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "404",
            description = "Deposit not found.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @SecurityRequirement(name = "Authentication")
    @POST
    @Path("/publish/deposit/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Registration
    public Response publishZenodoDeposit(@Parameter(
            description = "The ID of the deposit to publish to Zenodo",
            required = true,
            example = "183367",
            schema = @Schema(type = SchemaType.STRING)) @PathParam("id") String depositId) {

        zenodoService.publishDepositToZenodo(depositId, utility.getUserUniqueIdentifier());

        var response = new InformativeResponse();
        response.code = 202;
        response.message = "Process to publish Deposit with ID: " + depositId + " is in progress... You will be informed via email when completed.";

        return Response.ok().entity(response).build();
    }

    private boolean isValidPdf(byte[] fileContent) {
        // Check the magic number for PDF files: %PDF- (hex: 0x25 0x50 0x44 0x46)
        if (fileContent.length < 4) {
            return false;
        }
        return (fileContent[0] == 0x25 && fileContent[1] == 0x50 &&
                fileContent[2] == 0x44 && fileContent[3] == 0x46);
    }
}