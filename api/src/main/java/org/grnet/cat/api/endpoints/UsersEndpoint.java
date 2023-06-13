package org.grnet.cat.api.endpoints;

import io.quarkus.oidc.TokenIntrospection;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeIn;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.services.IdentifiedService;

@Path("/v1/users")
@Authenticated
@SecurityScheme(securitySchemeName = "Authentication",
        description = "JWT token",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER)
public class UsersEndpoint {

    /**
     * Injection point for the Identified Service
     */
    @Inject
    IdentifiedService identifiedService;

    /**
     * Injection point for the Token Introspection
     */
    @Inject
    TokenIntrospection tokenIntrospection;

    @ConfigProperty(name = "oidc.user.unique.id")
    String key;


    @Tag(name = "User")
    @Operation(
            summary = "Registers a user in the CAT database.",
            description = "This endpoint creates a new user entity in the database by extracting the voperson_id from the access token and assigning it to the user.")
    @APIResponse(
            responseCode = "200",
            description = "When a user is successfully registered in the database.",
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
            responseCode = "409",
            description = "User already exists in the database.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))

    @Path("/register")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response register() {

        identifiedService.register(tokenIntrospection.getJsonObject().getString(key));

        var response = new InformativeResponse();
        response.code = 200;
        response.message = "User has been successfully registered on Cat Service.";

        return Response.ok().entity(response).build();
    }
}
