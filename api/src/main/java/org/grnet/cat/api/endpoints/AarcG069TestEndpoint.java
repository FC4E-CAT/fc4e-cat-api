package org.grnet.cat.api.endpoints;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.grnet.cat.dtos.AarcG069ValidationResult;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.services.arcc.ArccValidationService;

@Authenticated
@Path("/v1/aarc-g069")
public class AarcG069TestEndpoint {

    @Inject
    ArccValidationService arccValidationService;

    @Tag(name = "Automated Check")
    @Operation(
            summary = "Validate AARC-G069 compliance for a specific AAI provider",
            description = "Fetches metadata from NACO and checks whether the `entitlements` claim is present in both `user_info` and `introspection_info`, and conforms to the URN format defined in AARC-G069."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Validation result for the AARC-G069 compliance check",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = AarcG069ValidationResult.class))),
            @APIResponse(
                    responseCode = "401",
                    description = "User has not been authenticated.",
                    content = @Content(schema = @Schema(
                            type = SchemaType.OBJECT,
                            implementation = InformativeResponse.class))),
            @APIResponse(
                    responseCode = "403",
                    description = "Not permitted.",
                    content = @Content(schema = @Schema(
                            type = SchemaType.OBJECT,
                            implementation = InformativeResponse.class))),
            @APIResponse(
                    responseCode = "404",
                    description = "Entity Not Found.",
                    content = @Content(schema = @Schema(
                            type = SchemaType.OBJECT,
                            implementation = InformativeResponse.class))),
            @APIResponse(
                    responseCode = "500",
                    description = "Internal Server Error.",
                    content = @Content(schema = @Schema(
                            type = SchemaType.OBJECT,
                            implementation = InformativeResponse.class)))
    })
    @SecurityRequirement(name = "Authentication")
    @GET
    @Path("/{aai-provider-id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateProvider(
            @Parameter(
                    description = "The AAI Provider ID to query from NACO.",
                    required = true,
                    example = "egi",
                    schema = @Schema(type = SchemaType.STRING)) @PathParam("aai-provider-id")  String aaiProviderId) {


        var response = arccValidationService.validateAarcG069(aaiProviderId);

        return Response.ok(response).build();
    }
}
