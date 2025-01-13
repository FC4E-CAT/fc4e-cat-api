package org.grnet.cat.api.endpoints;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
import org.grnet.cat.dtos.*;
import org.grnet.cat.enums.ArccTestType;
import org.grnet.cat.enums.ValidationStatus;
import org.grnet.cat.repositories.MotivationAssessmentRepository;
import org.grnet.cat.services.ArccValidationService;
import org.grnet.cat.services.AutomatedCheckService;

import java.util.Arrays;
import java.util.stream.Collectors;

@Authenticated
@Path("/v1/automated")
public class AutomatedCheckEndpoint {

    @Inject
    AutomatedCheckService automatedCheckService;

    @Inject
    ArccValidationService arccValidationService;

    @Tag(name = "Automated Check")
    @Operation(
            summary = "Validate Automated Check",
            description = "Validates  the https url automated check."
    )
    @APIResponse(
            responseCode = "200",
            description = "URL successfully checked.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = AutomatedCheckResponse.class)))
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
            description = "Entity Not Found.",
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
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Path("/check-url")
    @Authenticated
    @Registration
    public Response checkHttpsUrl(@Valid @NotNull(message = "The request body is empty.") AutomatedCheckRequest request) {

        var response = automatedCheckService.isValidHttpsUrl(request);

        return Response.ok().entity(response).build();
    }


    @Tag(name = "Automated Check")
    @Operation(
            summary = "Validate SAML Metadata",
            description = "Validates the provided SAML Metadata XML URL against the schema and required fields."
    )
    @APIResponse(
            responseCode = "200",
            description = "Metadata validated successfully.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = AutomatedCheckResponse.class)))
    @APIResponse(
            responseCode = "400",
            description = "Validation failed.",
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
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Path("/validate-metadata/{type}")
    @Authenticated
    @Registration
    public Response validateMetadata(
            @Parameter(
                    description = "The type of the test to compare xml attributes.",
                    required = true,
                    example = "MD-1a",
                    schema = @Schema(type = SchemaType.STRING)) @PathParam("type")  String type,
            @Valid @NotNull(message = "The request body is empty.") ArccValidationRequest request) {

        if (type != null && !ArccTestType.getTypes().contains(type)) {
            throw new BadRequestException("The value " + type + " is not a valid test type. Valid type values are: " + Arrays.toString(ArccTestType.values()));
        }
        var response = arccValidationService.validateMetadataByTestId(type,request);

        return Response.ok().entity(response).build();
    }
}