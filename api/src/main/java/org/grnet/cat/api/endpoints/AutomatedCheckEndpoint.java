package org.grnet.cat.api.endpoints;


import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
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
import org.grnet.cat.dtos.AutomatedCheckRequest;
import org.grnet.cat.dtos.AutomatedCheckResponse;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.services.AutomatedCheckService;

@Authenticated
@Path("/v1/automated")
public class AutomatedCheckEndpoint {

    @Inject
    AutomatedCheckService automatedCheckService;

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
}