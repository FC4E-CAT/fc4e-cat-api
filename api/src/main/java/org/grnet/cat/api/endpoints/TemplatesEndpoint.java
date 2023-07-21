package org.grnet.cat.api.endpoints;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
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
import org.grnet.cat.api.utils.CatServiceUriInfo;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.dtos.TemplateRequest;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.TemplateDto;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.repositories.ActorRepository;
import org.grnet.cat.repositories.AssessmentTypeRepository;
import org.grnet.cat.repositories.TemplateRepository;
import org.grnet.cat.services.TemplateService;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

@Path("/v1/templates")
@Authenticated
public class TemplatesEndpoint {

    @Inject
    TemplateService templateService;

    @ConfigProperty(name = "server.url")
    String serverUrl;

    @Tag(name = "Template")
    @Operation(
            summary = "Get assessment template By Actor.",
            description = "This endpoint retrieves the assessment template corresponding to an actor")
    @APIResponse(
            responseCode = "200",
            description = "Actor's assessment template.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = TemplateDto.class)))
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
    @Path("/by-type/{type-id}/by-actor/{actor-id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response getAssessmentTemplateByActorAndType(@Parameter(
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
                        @PathParam("type-id") @Valid @NotFoundEntity(repository = AssessmentTypeRepository.class, message = "There is no Assessment Type with the following id:") Long typeId) {

        var template = templateService.getTemplateByActorAndType(actorId, typeId);
        return Response.ok().entity(template).build();
    }

    @Tag(name = "Template")
    @Operation(
            summary = "Create a new assessment template.",
            description = "Create a new assessment template (accessible by admin users only).")
    @APIResponse(
            responseCode = "201",
            description = "Template created successfully.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = TemplateDto.class)))
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
            responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @SecurityRequirement(name = "Authentication")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response create(@Valid @NotNull(message = "The request body is empty.") TemplateRequest request, @Context UriInfo uriInfo) {

        var template = templateService.createAssessmentTemplate(request);
        var serverInfo = new CatServiceUriInfo(serverUrl.concat(uriInfo.getPath()));

        return Response.created(serverInfo.getAbsolutePathBuilder().path(String.valueOf(template.id)).build()).entity(template).build();
    }

    @Tag(name = "Template")
    @Operation(
            summary = "Get assessment template.",
            description = "Returns a specific assessment template.")
    @APIResponse(
            responseCode = "200",
            description = "The corresponding assessment template.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = TemplateDto.class)))
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
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response getAssessmentTemplate(@Parameter(
            description = "The ID of the assessment template to retrieve.",
            required = true,
            example = "1",
            schema = @Schema(type = SchemaType.NUMBER))
                                              @PathParam("id")
                                              @Valid @NotFoundEntity(repository = TemplateRepository.class, message = "There is no assessment template with the following id:") Long id) {

        var template = templateService.getAssessmentTemplate(id);

        return Response.ok().entity(template).build();
    }

    @Tag(name = "Template")
    @Operation(
            summary = "Retrieve assessment templates for a specific type.",
            description = "This endpoint retrieves the assessment templates for a specific type." +
                    "By default, the first page of 10 assessment templates will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of assessment templates.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableTemplate.class)))
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
    @Path("/by-type/{type-id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response getTemplatesByType(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                @Parameter(name = "size", in = QUERY,
                                        description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                @Parameter(
                                        description = "The Type to retrieve templates.",
                                        required = true,
                                        example = "1",
                                        schema = @Schema(type = SchemaType.NUMBER))
                                    @PathParam("type-id") @Valid @NotFoundEntity(repository = AssessmentTypeRepository.class, message = "There is no Assessment Type with the following id:") Long typeId,
                                @Context UriInfo uriInfo) {

        var templates = templateService.getTemplatesByType(page-1, size, typeId, uriInfo);

        return Response.ok().entity(templates).build();
    }

    @Tag(name = "Template")
    @Operation(
            summary = "Retrieve all the assessment templates.",
            description = "This endpoint retrieves all the assessment templates." +
                    "By default, the first page of 10 assessment templates will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of all the assessment templates.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableTemplate.class)))
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
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response getTemplates(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                       @Parameter(name = "size", in = QUERY,
                                               description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                       @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                       @Context UriInfo uriInfo) {

        var templates = templateService.getTemplates(page-1, size, uriInfo);

        return Response.ok().entity(templates).build();
    }

    public class PageableTemplate extends PageResource<TemplateDto> {

        private List<TemplateDto> content;

        @Override
        public List<TemplateDto> getContent() {
            return content;
        }

        @Override
        public void setContent(List<TemplateDto> content) {
            this.content = content;
        }
    }
}
