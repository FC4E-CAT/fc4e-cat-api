package org.grnet.cat.api.endpoints;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.grnet.cat.api.filters.Registration;
import org.grnet.cat.api.utils.Utility;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.PromotionRequest;
import org.grnet.cat.services.IdentifiedService;

@Path("/v1/validations")
@Authenticated
public class ValidationsEndpoint {

    /**
     * Injection point for the Identified service
     */
    @Inject
    IdentifiedService identifiedService;

    /**
     * Injection point for the Utility class
     */
    @Inject
    Utility utility;


    @Tag(name = "Validation")
    @Operation(
            summary = "Request promotion to validated user.",
            description = "This endpoint allows an identified user to request promotion to become a validated user."+
                    " The identified user should provide the necessary information to support their promotion request, which will be reviewed by the administrators.")
    @APIResponse(
            responseCode = "200",
            description = "User promotion request submitted.",
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
            description = "User has not been registered on CAT service.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "409",
            description = "Promotion request already exists.",
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
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response validate(@Valid @NotNull(message = "The request body is empty.") PromotionRequest request) {

        identifiedService.validate(utility.getUserUniqueIdentifier(), request);

        var response = new InformativeResponse();
        response.code = 200;
        response.message = "User promotion request submitted.";

        return Response.ok().entity(response).build();
    }
}