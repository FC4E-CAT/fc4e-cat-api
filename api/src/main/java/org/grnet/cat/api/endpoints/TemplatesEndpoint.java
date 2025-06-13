package org.grnet.cat.api.endpoints;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeIn;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.grnet.cat.api.filters.Registration;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.dtos.registry.template.RegistryTemplateDto;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.repositories.registry.MotivationRepository;
import org.grnet.cat.repositories.registry.RegistryActorRepository;
import org.grnet.cat.services.TemplateService;

@Path("/v1/templates")
@Authenticated
@SecurityScheme(securitySchemeName = "Authentication",
        description = "JWT token",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER)
public class TemplatesEndpoint {

    @Inject
    TemplateService templateService;

    @Tag(name = "Template")
    @Operation(
            summary = "Retrieve registry template for a specific motivation and actor.",
            description = "This endpoint retrieves a registry template for a specific motivation and actor.")
    @APIResponse(
            responseCode = "200",
            description = "List of assessment templates.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = RegistryTemplateDto.class)))
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
    @GET
    @Path("/by-motivation/{motivation-id}/by-actor/{actor-id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response getRegistryTemplate(@Parameter(
            description = "The Motivation to retrieve template.",
            required = true,
            example = "pid_graph:3E109BBA",
            schema = @Schema(type = SchemaType.STRING))
                                        @PathParam("motivation-id") @Valid @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:")
                                        //@CheckPublished(repository = MotivationRepository.class, message = "No action permitted for unpublished Motivation with the following id:", isPublishedPermitted = true)
                                        String motivationId,
                                        @Parameter(
                                                description = "The Actor to retrieve template.",
                                                required = true,
                                                example = "pid_graph:E92B9B49",
                                                schema = @Schema(type = SchemaType.STRING))
                                        @PathParam("actor-id") @Valid @NotFoundEntity(repository = RegistryActorRepository.class, message = "There is no Actor with the following id:") String actorId) {

        var template = templateService.buildTemplate(motivationId, actorId);

        return Response.ok().entity(template).build();
    }
}
