package org.grnet.cat.api.endpoints.registry;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
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
import org.grnet.cat.api.utils.CatServiceUriInfo;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.actor.MotivationActorRequest;
import org.grnet.cat.dtos.registry.actor.MotivationActorResponse;
import org.grnet.cat.dtos.registry.criterion.CriterionActorRequest;
import org.grnet.cat.dtos.registry.criterion.CriterionActorResponse;
import org.grnet.cat.dtos.registry.motivation.MotivationRequest;
import org.grnet.cat.dtos.registry.motivation.MotivationResponse;
import org.grnet.cat.dtos.registry.motivation.UpdateMotivationRequest;
import org.grnet.cat.dtos.registry.principle.MotivationPrincipleRequest;
import org.grnet.cat.repositories.registry.MotivationRepository;
import org.grnet.cat.repositories.registry.RegistryActorRepository;
import org.grnet.cat.services.registry.MotivationService;
import org.grnet.cat.services.registry.RegistryActorService;
import org.grnet.cat.utils.Utility;

import java.util.List;
import java.util.Set;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

@Path("/v1/registry/motivations")
@Authenticated
@SecurityScheme(securitySchemeName = "Authentication",
        description = "JWT token",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER)
public class MotivationEndpoint {

    @Inject
    private MotivationService motivationService;

    @Inject
    private RegistryActorService registryActorService;
    /**
     * Injection point for the Utility class
     */
    @Inject
    Utility utility;

    @ConfigProperty(name = "api.server.url")
    String serverUrl;

    @Tag(name = "Motivation")
    @Operation(
            summary = "Create a new Motivation.",
            description = "Create a new Motivation.")
    @APIResponse(
            responseCode = "201",
            description = "Motivation created successfully.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = MotivationResponse.class)))
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
            responseCode = "409",
            description = "Motivation already exists.",
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
    public Response create(@Valid @NotNull(message = "The request body is empty.") MotivationRequest request, @Context UriInfo uriInfo) {

        var motivation = motivationService.createMotivation(request, utility.getUserUniqueIdentifier());

        var serverInfo = new CatServiceUriInfo(serverUrl.concat(uriInfo.getPath()));

        return Response.created(serverInfo.getAbsolutePathBuilder().path(String.valueOf(motivation.id)).build()).entity(motivation).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Get specific Motivation.",
            description = "Returns a specific Motivation.")
    @APIResponse(
            responseCode = "200",
            description = "The corresponding Motivation.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = MotivationResponse.class)))
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
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response getMotivation(@Parameter(
            description = "The ID of the Motivation to retrieve.",
            required = true,
            example = "pid_graph:3E109BBA",
            schema = @Schema(type = SchemaType.STRING))
                                  @PathParam("id")
                                  @Valid @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:") String id) {

        var motivation = motivationService.getMotivationById(id);

        return Response.ok().entity(motivation).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Get list of Motivations.",
            description = "This endpoint retrieves all Motivations." +
                    "By default, the first page of 10 Motivations will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of Motivations.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableMotivationResponse.class)))
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
    public Response getMotivations(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                   @Parameter(name = "size", in = QUERY,
                                           description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                   @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                   @Context UriInfo uriInfo) {

        var subjects = motivationService.getMotivationsByPage(page - 1, size, uriInfo);

        return Response.ok().entity(subjects).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Updates an existing Motivation.",
            description = "To update the resource properties, the body of the request must contain an updated representation of Motivation. " +
                    "You can update a part or all attributes of Motivation. The empty or null values are ignored.")
    @APIResponse(
            responseCode = "200",
            description = "Subject was updated successfully.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = MotivationResponse.class)))
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
    @PATCH
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response updateMotivation(@Parameter(
            description = "The ID of the Subject to update.",
            required = true,
            example = "1",
            schema = @Schema(type = SchemaType.STRING))
                                     @PathParam("id")
                                     @Valid @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:") String id,
                                     @Valid @NotNull(message = "The request body is empty.") UpdateMotivationRequest request) {

        var motivation = motivationService.update(id, request, utility.getUserUniqueIdentifier());

        return Response.ok().entity(motivation).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Get list of Actors of a Motivation.",
            description = "This endpoint retrieves all Actors of a Motivation." +
                    "By default, the first page of 10 Motivations will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of Actors of a Motivation.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableMotivationActorJunctionResponse.class)))
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
    @Path("/{id}/actors")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response getActorsByMotivation(@Parameter(
            description = "The ID of the Motivation to retrieve.",
            required = true,
            example = "pid_graph:3E109BBA",
            schema = @Schema(type = SchemaType.STRING))
                                          @PathParam("id")
                                          @Valid @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:") String id, @Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                          @Parameter(name = "size", in = QUERY,
                                                  description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                          @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                          @Context UriInfo uriInfo) {

        var subjects = motivationService.getActorsByMotivationAndPage(id, page - 1, size, uriInfo);

        return Response.ok().entity(subjects).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Assign Actors to a Motivation.",
            description = "Assign multiple Actors to a single motivation.")
    @APIResponse(
            responseCode = "200",
            description = "Actors successfully assigned to the motivation.",
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
            responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @SecurityRequirement(name = "Authentication")
    @POST
    @Path("/{id}/actors")
    @Produces(MediaType.APPLICATION_JSON)
    public Response assignActorToMotivation(@Parameter(
            description = "The ID of the Motivation to assign actors.",
            required = true,
            example = "pid_graph:3E109BBA",
            schema = @Schema(type = SchemaType.STRING))
                                         @PathParam("id")
                                         @Valid @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:") String id,
                                         @NotEmpty(message = "Actors list can not be empty.") Set<@Valid MotivationActorRequest> request) {

        var messages = motivationService.assignActors(id, request, utility.getUserUniqueIdentifier());

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.messages = messages;

        return Response.ok().entity(informativeResponse).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Assign Principles to a Motivation.",
            description = "Assign multiple Principles to a single motivation.")
    @APIResponse(
            responseCode = "200",
            description = "Principles successfully assigned to the motivation.",
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
            responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @SecurityRequirement(name = "Authentication")
    @POST
    @Path("/{id}/principles")
    @Produces(MediaType.APPLICATION_JSON)
    public Response assignPrinciplesToMotivation(@Parameter(
            description = "The ID of the Motivation to assign Principles.",
            required = true,
            example = "pid_graph:3E109BBA",
            schema = @Schema(type = SchemaType.STRING))
                                         @PathParam("id")
                                         @Valid @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:") String id,
                                         @NotEmpty(message = "Principles list can not be empty.") Set<@Valid MotivationPrincipleRequest> request) {

        var messages = motivationService.assignPrinciples(id, request, utility.getUserUniqueIdentifier());

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.messages = messages;

        return Response.ok().entity(informativeResponse).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Add Criterion Item to Motivation Actor.",
            description = "Adds a new criterion item to motivation actor.")
    @APIResponse(
            responseCode = "201",
            description = "Criterion item added.",
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
            responseCode = "409",
            description = "Unique constraint violation.",
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
    @Path("/{id}/actors/{actor-id}/criteria")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCriterionToMotivationActor(@Parameter(
            description = "The ID of the Motivation to add actors.",
            required = true,
            example = "pid_graph:3E109BBA",
            schema = @Schema(type = SchemaType.STRING))
                                                  @PathParam("id")
                                                  @Valid @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:") String id,
                                                  @Parameter(
                                                          description = "The ID of the Actor to add criterion to.",
                                                          required = true,
                                                          example = "pid_graph:234B60D8",
                                                          schema = @Schema(type = SchemaType.STRING))
                                                  @PathParam("actor-id")
                                                  @Valid @NotFoundEntity(repository = RegistryActorRepository.class, message = "There is no Actor with the following id:") String actorId,
                                                  @NotEmpty(message = "Actors list can not be empty.") Set<@Valid CriterionActorRequest> request,
                                                  @Context UriInfo uriInfo) {

        var messages = registryActorService.addCriteria(id, actorId, request, utility.getUserUniqueIdentifier());

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.messages = messages;

        return Response.ok().entity(informativeResponse).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Get list of Criteria of an Motivation Actor.",
            description = "This endpoint retrieves all Criteria of a Motivation Actor." +
                    "By default, the first page of 10 Motivations will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of Actors of a Motivation.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableCriterionActorJunctionResponse.class)))
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
    @Path("/{id}/actors/{actor-id}/criteria")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response getCriteriaByMotivationActor(@Parameter(
            description = "The ID of the Motivation to add actors.",
            required = true,
            example = "pid_graph:3E109BBA",
            schema = @Schema(type = SchemaType.STRING))
                                                   @PathParam("id")
                                                   @Valid @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:") String id,
                                                   @Parameter(
                                                           description = "The ID of the Actor to add criterion.",
                                                           required = true,
                                                           example = "pid_graph:234B60D8",
                                                           schema = @Schema(type = SchemaType.STRING))
                                                   @PathParam("actor-id")
                                                   @Valid @NotFoundEntity(repository = RegistryActorRepository.class, message = "There is no Actor with the following id:") String actorId, @Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                                   @Parameter(name = "size", in = QUERY,
                                                           description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                                   @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                                   @Context UriInfo uriInfo) {

        var criteria = registryActorService.getCriteriaByMotivationActorAndPage(id,actorId, page - 1, size, uriInfo);

        return Response.ok().entity(criteria).build();
    }

    public static class PageableMotivationResponse extends PageResource<MotivationResponse> {

        private List<MotivationResponse> content;

        @Override
        public List<MotivationResponse> getContent() {
            return content;
        }

        @Override
        public void setContent(List<MotivationResponse> content) {
            this.content = content;
        }
    }

    public static class PageableCriterionActorJunctionResponse extends PageResource<CriterionActorResponse> {

        private List<CriterionActorResponse> content;

        @Override
        public List<CriterionActorResponse> getContent() {
            return content;
        }

        @Override
        public void setContent(List<CriterionActorResponse> content) {
            this.content = content;
        }
    }

    public static class PageableMotivationActorJunctionResponse extends PageResource<MotivationActorResponse> {

        private List<MotivationActorResponse> content;

        @Override
        public List<MotivationActorResponse> getContent() {
            return content;
        }

        @Override
        public void setContent(List<MotivationActorResponse> content) {
            this.content = content;
        }
    }
}
