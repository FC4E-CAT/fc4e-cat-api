package org.grnet.cat.api.endpoints;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.grnet.cat.api.filters.Registration;
import org.grnet.cat.api.utils.Utility;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.repositories.ActorRepository;
import org.grnet.cat.repositories.AssessmentTypeRepository;
import org.grnet.cat.repositories.ValidationRepository;
import org.grnet.cat.services.TemplateService;



    @Path("/v1/templates")
    @Authenticated
    public class TemplatesEndpoint {

        @Inject
        AssessmentTypeRepository assessmentTypeRepository;

        @Inject
        ActorRepository actorRepository;

        @Inject
        TemplateService templateService;
        @Tag(name = "Template")
        @Operation(
                summary = "Get Template By Actor.",
                description = "This endpoint retrieves the template corresponding to an actor")
        @APIResponse(
                responseCode = "200",
                description = "Actor's Template.",
                content = @Content(schema = @Schema(
                        type = SchemaType.OBJECT,
                        implementation = Response.class)))
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
                responseCode = "500",
                description = "Internal Server Error.",
                content = @Content(schema = @Schema(
                        type = SchemaType.OBJECT,
                        implementation = InformativeResponse.class)))
        @SecurityRequirement(name = "Authentication")
        @GET
        @Path("/types/{type-id}/by-actor/{actor-id}")

        @Produces(MediaType.APPLICATION_JSON)
        @Registration
        public Response get(@Parameter(
                description = "The Actor to retrieve template.",
                required = true,
                example = "1",
                schema = @Schema(type = SchemaType.NUMBER))
                                    @PathParam("actor-id") @Valid @NotFoundEntity(repository = ActorRepository.class, message = "There is no Actor with the following id:") Long actorId,
                            @Parameter(
                                    description = "The Type to retrieve template.",
                                    required = true,
                                    example = "1",
                                    schema = @Schema(type = SchemaType.NUMBER))
                            @PathParam("type-id") @Valid @NotFoundEntity(repository = AssessmentTypeRepository.class, message = "There is no Assessment Type with the following id:")  Long typeId) {

            var template=templateService.getTemplate(actorId,typeId);
            return Response.ok().entity(template).build();
        }
    }
