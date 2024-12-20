package org.grnet.cat.api.endpoints.registry;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeIn;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.grnet.cat.api.filters.Registration;
import org.grnet.cat.api.utils.CatServiceUriInfo;
import org.grnet.cat.constraints.CheckPublished;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.*;
import org.grnet.cat.dtos.registry.actor.MotivationActorRequest;
import org.grnet.cat.dtos.registry.actor.MotivationActorResponse;
import org.grnet.cat.dtos.registry.criterion.CriterionActorRequest;
import org.grnet.cat.dtos.registry.criterion.CriterionActorResponse;
import org.grnet.cat.dtos.registry.criterion.DetailedCriterionDto;
import org.grnet.cat.dtos.registry.criterion.PrincipleCriterionResponse;
import org.grnet.cat.dtos.registry.metric.DetailedMetricDto;
import org.grnet.cat.dtos.registry.metric.MotivationMetricExtendedRequest;
import org.grnet.cat.dtos.registry.motivation.*;
import org.grnet.cat.dtos.registry.principle.MotivationPrincipleExtendedRequestDto;
import org.grnet.cat.dtos.registry.principle.MotivationPrincipleRequest;
import org.grnet.cat.dtos.registry.principle.PrincipleResponseDto;
import org.grnet.cat.dtos.registry.principle.PrincipleUpdateDto;
import org.grnet.cat.dtos.registry.template.RegistryTemplateDto;
import org.grnet.cat.repositories.registry.CriterionRepository;
import org.grnet.cat.repositories.registry.MotivationRepository;
import org.grnet.cat.repositories.registry.PrincipleRepository;
import org.grnet.cat.repositories.registry.RegistryActorRepository;
import org.grnet.cat.repositories.registry.metric.MetricRepository;
import org.grnet.cat.services.TemplateService;
import org.grnet.cat.services.registry.*;
import org.grnet.cat.utils.Utility;
import org.grnet.cat.validators.SortAndOrderValidator;

import java.util.*;
import java.util.stream.Collectors;

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
    TemplateService templateService;
    @Inject
    private MotivationService motivationService;

    @Inject
    RelationsService relationsService;

    @Inject
    MotivationRepository motivationRepository;

    @Inject
    private RegistryActorService registryActorService;

    @Inject
    private CriterionService criterionService;

    @Inject
    private PrincipleService principleService;

    @Inject
    private CriterionMetricService criterionMetricService;


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
    public Response create(@Valid @NotNull(message = "The request body is empty.") MotivationRequest request,
                           @Context UriInfo uriInfo) {

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
                                   @Parameter(name = "search", in = QUERY,
                                           description = "The \"search\" parameter is a query parameter that allows clients to specify a text string that will be used to search for matches in specific fields in Motivation entity. The search will be conducted in the following fields : MTV, Label.") @QueryParam("search") String search,

                                   @Parameter(name = "actor", in = QUERY,
                                           description = "The Motivation actor to filter.") @QueryParam("actor") @DefaultValue("") String actor,
                                   @Parameter(name = "published",
                                           in = QUERY,
                                           schema = @Schema(type = SchemaType.STRING, defaultValue = ""),
                                           examples = {@ExampleObject(name = "TRUE", value = "TRUE"), @ExampleObject(name = "FALSE", value = "FALSE")},
                                           description = "The \"published\" parameter allows clients to filter the results based on the publication status of the motivation.") @QueryParam("published") String status,

                                   @Parameter(name = "sort", in = QUERY,
                                           schema = @Schema(type = SchemaType.STRING, defaultValue = "lastTouch"),
                                           examples = {@ExampleObject(name = "Last Touch", value = "lastTouch"), @ExampleObject(name = "MTV", value = "mtv"), @ExampleObject(name = "Label", value = "label")},
                                           description = "The \"sort\" parameter allows clients to specify the field by which they want the results to be sorted.") @DefaultValue("lastTouch") @QueryParam("sort") String sort,
                                   @Parameter(name = "order", in = QUERY, schema = @Schema(type = SchemaType.STRING, defaultValue = "DESC"),
                                           examples = {@ExampleObject(name = "Ascending", value = "ASC"), @ExampleObject(name = "Descending", value = "DESC")},
                                           description = "The \"order\" parameter specifies the order in which the sorted results should be returned.") @DefaultValue("DESC") @QueryParam("order") String order,

                                   @Context UriInfo uriInfo) {
        var orderValues = List.of("ASC", "DESC");
        var sortValues = List.of("mtv", "label", "lastTouch");

        if (!orderValues.contains(order)) {

            throw new BadRequestException("The available values of order parameter are : " + orderValues);
        }
        if (!sortValues.contains(sort)) {

            throw new BadRequestException("The available values of sort parameter are : " + sortValues);
        }
        var validStatuses = new String[]{"FALSE", "TRUE"};
        if (status != null && !Arrays.stream(validStatuses).collect(Collectors.toList()).contains(status)) {

            throw new BadRequestException("The value " + status + " is not a valid status. Valid status values are: " + Arrays.toString(validStatuses));
        }
        var subjects = motivationService.getMotivationsByPage(actor, search, status, sort, order, page - 1, size, uriInfo);

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
                                     @Valid @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:")
                                     @CheckPublished(repository = MotivationRepository.class, message = "No action permitted for published Motivation with the following id:", isPublishedPermitted = false) String id,
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
                                                    description = "The ID of the Motivation to update.",
                                                    required = true,
                                                    example = "1",
                                                    schema = @Schema(type = SchemaType.STRING))
                                            @PathParam("id")
                                            @Valid
                                            @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:")
                                            @CheckPublished(repository = MotivationRepository.class, message = "No action permitted for published Motivation with the following id:", isPublishedPermitted = false) String id,
                                            @NotEmpty(message = "Actors list can not be empty.") Set<@Valid MotivationActorRequest> request) {

        var messages = motivationService.assignActors(id, request, utility.getUserUniqueIdentifier());
        String result = String.join("\n", messages);

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.message = result;

        return Response.ok().entity(informativeResponse).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Remove an Actor from a Motivation.",
            description = "Removes an Actor from a specific Motivation and its associated relations.")
    @APIResponse(
            responseCode = "200",
            description = "Actors successfully deleted from the motivation.",
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
            description = "Motivation or Actor not found.",
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
    @DELETE
    @Path("/{id}/actors/{actor-id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response removeActorFromMotivation(@Parameter(
                                                      description = "The ID of the Motivation to update.",
                                                      required = true,
                                                      example = "1",
                                                      schema = @Schema(type = SchemaType.STRING))
                                              @PathParam("id")
                                              @Valid
                                              @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:")
                                              @CheckPublished(repository = MotivationRepository.class, message = "No action permitted for published Motivation with the following id:", isPublishedPermitted = false) String id,
                                              @Parameter(
                                                      description = "The actor to be deleted.",
                                                      required = true,
                                                      example = "pid_graph:0E00C332",
                                                      schema = @Schema(type = SchemaType.STRING))
                                              @PathParam("actor-id")
                                              @Valid @NotFoundEntity(
                                                      repository = RegistryActorRepository.class,
                                                      message = "There is no Actor with the following id: ")
                                              String actorId) {

        var messages = motivationService.deleteActorFromMotivation(id, actorId);
        String result = String.join("\n", messages);

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.message = result;

        return Response.ok().entity(informativeResponse).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Create a Principle for a Motivation.",
            description = "Create a Principle for a single motivation.")
    @APIResponse(
            responseCode = "200",
            description = "Principles successfully created for the motivation.",
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
    @Path("/{id}/principle")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPrinciple(
            @Parameter(
                    description = "The ID of the Motivation to create a principle for.",
                    required = true,
                    example = "pid_graph:3E109BBA",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid
            @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:")
            @CheckPublished(repository = MotivationRepository.class, message = "No action permitted for published Motivation with the following id:", isPublishedPermitted = false)
            String id,
            MotivationPrincipleExtendedRequestDto request) {

        var response = motivationService.createPrincipleForMotivation(id, request, utility.getUserUniqueIdentifier());

        return Response.status(response.code).entity(response).build();

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
                                                         description = "The ID of the Motivation to update.",
                                                         required = true,
                                                         example = "1",
                                                         schema = @Schema(type = SchemaType.STRING))
                                                 @PathParam("id")
                                                 @Valid
                                                 @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:")
                                                 @CheckPublished(repository = MotivationRepository.class, message = "No action permitted for published Motivation with the following id:", isPublishedPermitted = false) String id,
                                                 @NotEmpty(message = "Principles list can not be empty.") Set<@Valid MotivationPrincipleRequest> request) {

        var messages = motivationService.assignPrinciples(id, request, utility.getUserUniqueIdentifier());

        String result = String.join("\n", messages);

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.message = result;

        return Response.ok().entity(informativeResponse).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Get list of Principles of a Motivation.",
            description = "This endpoint retrieves all Principles of a Motivation." +
                    "By default, the first page of 10 Motivations will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of Principles of a Motivation.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageablePrincipleResponse.class)))
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
    @Path("/{id}/principles")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response getPrincipleByMotivation(
            @Parameter(description = "The ID of the Motivation to get principle.",
                    required = true,
                    example = "pid_graph:3E109BBA",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:") String id,
            @Parameter(name = "search", in = QUERY,
                    description = "The \"search\" parameter allows clients to search for matches in specific fields in the MetricTest entity.")
            @QueryParam("search") String search,
            @Parameter(name = "sort", in = QUERY,
                    schema = @Schema(type = SchemaType.STRING, defaultValue = "lastTouch"),
                    examples = {
                            @ExampleObject(name = "Last Touch", value = "lastTouch"),
                            @ExampleObject(name = "Label", value = "label"),
                            @ExampleObject(name = "Pri", value = "pri")},
                    description = "The \"sort\" parameter allows clients to specify the field by which they want the results to be sorted.")
            @DefaultValue("lastTouch")
            @QueryParam("sort") String sort,
            @Parameter(name = "order", in = QUERY,
                    schema = @Schema(type = SchemaType.STRING, defaultValue = "DESC"),
                    examples = {
                            @ExampleObject(name = "Ascending", value = "ASC"),
                            @ExampleObject(name = "Descending", value = "DESC")},
                    description = "The \"order\" parameter specifies the order in which the sorted results should be returned.")
            @DefaultValue("DESC")
            @QueryParam("order") String order,
            @Parameter(name = "page", in = QUERY,
                    description = "Indicates the page number. Page number must be >= 1.")
            @DefaultValue("1")
            @Min(value = 1, message = "Page number must be >= 1.")
            @QueryParam("page") int page,
            @Parameter(name = "size", in = QUERY,
                    description = "The page size.")
            @DefaultValue("10")
            @Min(value = 1, message = "Page size must be between 1 and 100.")
            @Max(value = 100, message = "Page size must be between 1 and 100.")
            @QueryParam("size") int size,
            @Context UriInfo uriInfo) {


        var orderValues = List.of("ASC", "DESC");
        var sortValues = List.of("lastTouch", "label", "pri");

        SortAndOrderValidator.validateSortAndOrder(sort, order, sortValues, orderValues);

        var principle = principleService.listPrinciplesByMotivation(id, search, sort, order, page - 1, size, uriInfo);

        return Response.ok().entity(principle).build();
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
                                                          description = "The ID of the Motivation to update.",
                                                          required = true,
                                                          example = "1",
                                                          schema = @Schema(type = SchemaType.STRING))
                                                  @PathParam("id")
                                                  @Valid
                                                  @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:")
                                                  @CheckPublished(repository = MotivationRepository.class, message = "No action permitted for published Motivation with the following id:", isPublishedPermitted = false) String id,
                                                  @Parameter(
                                                          description = "The ID of the Actor to add criterion to.",
                                                          required = true,
                                                          example = "pid_graph:234B60D8",
                                                          schema = @Schema(type = SchemaType.STRING))
                                                  @PathParam("actor-id")
                                                  @Valid @NotFoundEntity(repository = RegistryActorRepository.class, message = "There is no Actor with the following id:") String actorId,
                                                  @NotEmpty(message = "List of criteria can not be empty.") Set<@Valid CriterionActorRequest> request,
                                                  @Context UriInfo uriInfo) {

        var messages = registryActorService.addCriteria(id, actorId, request, utility.getUserUniqueIdentifier());

        String result = String.join("\n", messages);

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.message = result;

        return Response.ok().entity(informativeResponse).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Get list of Criteria of an Motivation Actor.",
            description = "This endpoint retrieves all Criteria of a Motivation Actor." +
                    "By default, the first page of 10 Actors will be returned. You can tune the default values by using the query parameters page and size.")
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
                                                         description = "The ID of the Motivation to add criteria.",
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

        var criteria = registryActorService.getCriteriaByMotivationActorAndPage(id, actorId, page - 1, size, uriInfo);

        return Response.ok().entity(criteria).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Get list of Criteria of an Motivation.",
            description = "This endpoint retrieves all Criteria of a Motivation." +
                    "By default, the first page of 10 Motivations will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of Criteria of a Motivation.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageablePrincipleCriteriaJunctionResponse.class)))
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
    @Path("/{id}/criteria")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response getCriteriaByMotivation(
            @Parameter(description = "The ID of the Motivation to get criteria.",
                    required = true,
                    example = "pid_graph:3E109BBA",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:") String id,
            @Parameter(name = "page", in = QUERY,
                    description = "Indicates the page number. Page number must be >= 1.")
            @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.")
            @QueryParam("page") int page,
            @Parameter(name = "size", in = QUERY,
                    description = "The page size.")
            @DefaultValue("10")
            @Min(value = 1, message = "Page size must be between 1 and 100.")
            @Max(value = 100, message = "Page size must be between 1 and 100.")
            @QueryParam("size") int size,
            @Context UriInfo uriInfo) {

        var criteria = criterionService.listCriteriaByMotivation(id, page - 1, size, uriInfo);

        return Response.ok().entity(criteria).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Get list of relations of a Motivation.",
            description = "This endpoint retrieves all Criteria of a Motivation Actor." +
                    "By default, the first page of 10 Motivations will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of relations of a Motivation.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableRelationsResponse.class)))
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
    @Path("/relations/{motivation-id}")
    @Registration
    public Response getRelationsByMotivation(
            @Parameter(description = "The ID of the Motivation to retrieve the associated relations.",
                    required = true,
                    example = "pid_graph:3E109BBA",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("motivation-id")
            @Valid @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:")
            String motivationId,
            @Parameter(name = "page",
                    description = "Page number. Must be >= 1.")
            @DefaultValue("1")
            @Min(value = 1, message = "Page number must be >= 1.")
            @QueryParam("page") int page,
            @Parameter(name = "size",
                    description = "Page size. Must be between 1 and 100.")
            @DefaultValue("10")
            @Min(value = 1, message = "Page size must be between 1 and 100.")
            @Max(value = 100, message = "Page size must be between 1 and 100.")
            @QueryParam("size") int size,
            @Context UriInfo uriInfo) {

        var relations = relationsService.getRelationsByMotivation(motivationId, page - 1, size, uriInfo);

        return Response.ok(relations).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Update Criterion Item to Motivation Actor.",
            description = "Updates the criterion list to motivation actor.")
    @APIResponse(
            responseCode = "201",
            description = "Criterion items updated.",
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
    @PUT
    @Path("/{id}/actors/{actor-id}/criteria")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCriterionToMotivationActor(@Parameter(
                                                             description = "The ID of the Motivation to update.",
                                                             required = true,
                                                             example = "pid_graph:3E109BBA",
                                                             schema = @Schema(type = SchemaType.STRING))
                                                     @PathParam("id")
                                                     @Valid
                                                     @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:")
                                                     @CheckPublished(repository = MotivationRepository.class, message = "No action permitted for published Motivation with the following id:", isPublishedPermitted = false) String id,
                                                     @Parameter(description = "The ID of the Actor to add criterion to.",
                                                             required = true,
                                                             example = "pid_graph:234B60D8",
                                                             schema = @Schema(type = SchemaType.STRING))
                                                     @PathParam("actor-id")
                                                     @Valid @NotFoundEntity(repository = RegistryActorRepository.class, message = "There is no Actor with the following id:") String actorId,
                                                     Set<@Valid CriterionActorRequest> request) {

        if (Objects.isNull(request)) {

            request = new HashSet<>();
        }

        var messages = registryActorService.updateCriteria(id, actorId, request, utility.getUserUniqueIdentifier());
        String result = String.join("\n", messages);

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.message = result;

        return Response.ok().entity(informativeResponse).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Create a new relationship between motivation, principles and criteria.",
            description = "Create a new relationship between motivation, principles and criteria.")
    @APIResponse(
            responseCode = "200",
            description = "Relationship created successfully.",
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
    @Path("/{id}/principles-criteria")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewPrinciplesCriteriaRelationship(@Parameter(
                                                                    description = "The ID of the Motivation to update.",
                                                                    required = true,
                                                                    example = "pid_graph:3E109BBA",
                                                                    schema = @Schema(type = SchemaType.STRING))
                                                            @PathParam("id")
                                                            @Valid
                                                            @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:")
                                                            @CheckPublished(repository = MotivationRepository.class, message = "No action permitted for published Motivation with the following id:", isPublishedPermitted = false) String id,
                                                            @NotEmpty(message = "Principles-Criteria list can not be empty.") Set<@Valid PrincipleCriterionRequest> request) {

        var messages = motivationService.createNewPrinciplesCriteriaRelationship(id, request, utility.getUserUniqueIdentifier());
        String result = String.join("\n", messages);

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.message = result;

        return Response.ok().entity(informativeResponse).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Update an existing relationship between motivation, principles and criteria.",
            description = "Update an existing relationship between motivation, principles and criteria.")
    @APIResponse(
            responseCode = "200",
            description = "Relationship updated successfully.",
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
    @PUT
    @Path("/{id}/principles-criteria")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePrinciplesCriteriaRelationship(@Parameter(
                                                                 description = "The ID of the Motivation to update.",
                                                                 required = true,
                                                                 example = "pid_graph:3E109BBA",
                                                                 schema = @Schema(type = SchemaType.STRING))
                                                         @PathParam("id")
                                                         @Valid
                                                         @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:")
                                                         @CheckPublished(repository = MotivationRepository.class, message = "No action permitted for published Motivation with the following id:", isPublishedPermitted = false) String id,
                                                         Set<@Valid PrincipleCriterionRequest> request) {

        if (Objects.isNull(request)) {

            request = new HashSet<>();
        }

        var messages = motivationService.updatePrinciplesCriteriaRelationship(id, request, utility.getUserUniqueIdentifier());

        String result = String.join("\n", messages);

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.message = result;

        return Response.ok().entity(informativeResponse).build();
    }

    @Tag(name = "Motivation")
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
    @Path("/{id}/by-actor/{actor-id}/template")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response getRegistryTemplate(@Parameter(
                                                description = "The Motivation to retrieve template.",
                                                required = true,
                                                example = "pid_graph:3E109BBA",
                                                schema = @Schema(type = SchemaType.STRING))
                                        @PathParam("id") @Valid @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:")
                                        String id,


                                        @Parameter(
                                                description = "The Actor to retrieve template.",
                                                required = true,
                                                example = "pid_graph:566C01F6",
                                                schema = @Schema(type = SchemaType.STRING))
                                        @PathParam("actor-id") @Valid @NotFoundEntity(repository = RegistryActorRepository.class, message = "There is no Actor with the following id:") String actorId) {

        var template = templateService.buildTemplateForAdmin(id, actorId);

        return Response.ok().entity(template).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Get a list of relationships between motivation, principles and criteria.",
            description = "Get a list of relationships between motivation, principles and criteria.")
    @APIResponse(
            responseCode = "200",
            description = "A list of relationships between motivation, principles and criteria.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageablePrincipleCriteriaJunctionResponse.class)))
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
    @Path("/{id}/principles-criteria")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrinciplesCriteriaRelationship(@Parameter(
                                                              description = "The ID of the Motivation to assign Principles.",
                                                              required = true,
                                                              example = "pid_graph:3E109BBA",
                                                              schema = @Schema(type = SchemaType.STRING))
                                                      @PathParam("id")
                                                      @Valid @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:") String id,
                                                      @Parameter(name = "page", in = QUERY,
                                                              description = "Indicates the page number. Page number must be >= 1.")
                                                      @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.")
                                                      @QueryParam("page") int page,
                                                      @Parameter(name = "size", in = QUERY,
                                                              description = "The page size.")
                                                      @DefaultValue("10")
                                                      @Min(value = 1, message = "Page size must be between 1 and 100.")
                                                      @Max(value = 100, message = "Page size must be between 1 and 100.")
                                                      @QueryParam("size") int size,
                                                      @Context UriInfo uriInfo) {


        var response = motivationService.getPrinciplesCriteriaRelationship(id, page - 1, size, uriInfo);

        return Response.ok().entity(response).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Get the Metrics and associated Metric Tests of a Criterion.",
            description = "Returns the Metrics and associated Metric Tests of a Criterion.")
    @APIResponse(
            responseCode = "200",
            description = "the Metrics and associated Metric Tests of a Criterion.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = DetailedCriterionDto.class)))
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
    @Path("/{id}/criteria/{criterion-id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response getCriterionByMotivation(
            @Parameter(description = "The ID of the Motivation to get Criterion.",
                    required = true,
                    example = "pid_graph:3E109BBA",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:") String id,
            @Parameter(description = "The ID of the Criterion.",
                    required = true,
                    example = "pid_graph:1F4D6BEF",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("criterion-id")
            @Valid @NotFoundEntity(repository = CriterionRepository.class, message = "There is no Criterion with the following id:") String criterionId) {

        var response = criterionService.getMotivationCriterion(id, criterionId);

        return Response.ok().entity(response).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Publish Motivation",
            description = "Publish the motivation")
    @APIResponse(
            responseCode = "201",
            description = "Motivation is Published.",
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
    @PUT
    @Path("/{id}/publish")
    @Produces(MediaType.APPLICATION_JSON)
    public Response publishMotivation(
            @Parameter(description = "The ID of the Motivation to publish",
                    required = true,
                    example = "pid_graph:3E109BBA",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:")
            @CheckPublished(repository = MotivationRepository.class, message = "No action permitted for published Motivation with the following id:", isPublishedPermitted = false) String id
    ) {

        motivationService.publish(id);

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.message = "Successful publish";

        return Response.ok().entity(informativeResponse).build();
    }


    @Tag(name = "Motivation")
    @Operation(
            summary = "Unpublish Motivation",
            description = "Unpublish the motivation")
    @APIResponse(
            responseCode = "201",
            description = "Motivation is Unpublished.",
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
    @PUT
    @Path("/{id}/unpublish")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unpublishMotivation(
            @Parameter(description = "The ID of the Motivation to publish",
                    required = true,
                    example = "pid_graph:3E109BBA",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:")
            @CheckPublished(repository = MotivationRepository.class, message = "No action permitted for unpublished Motivation with the following id:", isPublishedPermitted = true) String id
    ) {

        motivationService.unpublish(id);

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.message = "Successful unpublish";

        return Response.ok().entity(informativeResponse).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Publish Motivation Actor relationship",
            description = "Publish the motivation actor relationship")
    @APIResponse(
            responseCode = "201",
            description = "Motivation is Published.",
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
    @PUT
    @Path("/{id}/actors/{actor-id}/publish")
    @Produces(MediaType.APPLICATION_JSON)
    public Response publishMotivationActor(
            @Parameter(description = "The ID of the Motivation to publish",
                    required = true,
                    example = "pid_graph:3E109BBA",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:")
            String id, @Parameter(
                    description = "The ID of the Actor to publish.",
                    required = true,
                    example = "pid_graph:234B60D8",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("actor-id")

            @Valid @NotFoundEntity(repository = RegistryActorRepository.class, message = "There is no Actor with the following id:")
            String actorId
    ) {

        motivationService.publishActor(id, actorId);

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.message = "Successful publish";

        return Response.ok().entity(informativeResponse).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Unpublish Motivation Actor relationship",
            description = "Unpublish the motivation actor relationship")
    @APIResponse(
            responseCode = "201",
            description = "Motivation is Published.",
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
    @PUT
    @Path("/{id}/actors/{actor-id}/unpublish")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unpublishMotivationActor(
            @Parameter(description = "The ID of the Motivation to publish",
                    required = true,
                    example = "pid_graph:3E109BBA",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:")
            String id, @Parameter(
                    description = "The ID of the Actor to publish.",
                    required = true,
                    example = "pid_graph:234B60D8",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("actor-id")
            @Valid @NotFoundEntity(repository = RegistryActorRepository.class, message = "There is no Actor with the following id:") String actorId) {

        motivationService.unpublishActor(id, actorId);

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.message = "Successful unpublish";

        return Response.ok().entity(informativeResponse).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Create a new relationship between motivation, principles and criteria.",
            description = "Create a new relationship between motivation, principles and criteria.")
    @APIResponse(
            responseCode = "200",
            description = "Relationship created successfully.",
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
    @Path("/{id}/criteria-metrics")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewCriteriaMetricsRelationship(@Parameter(
                                                                 description = "The ID of the Motivation to create a relationship.",
                                                                 required = true,
                                                                 example = "pid_graph:3E109BBA",
                                                                 schema = @Schema(type = SchemaType.STRING))
                                                         @PathParam("id")
                                                         @Valid
                                                         @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:")
                                                         @CheckPublished(repository = MotivationRepository.class, message = "No action permitted for published Motivation with the following id:", isPublishedPermitted = false) String id,
                                                         @NotEmpty(message = "Criteria-Metrics list can not be empty.") Set<@Valid CriterionMetricRequest> request) {

        var messages = criterionMetricService.createNewCriteriaMetricsRelationship(id, request, utility.getUserUniqueIdentifier());
        String result = String.join(StringUtils.LF, messages);

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.message = result;

        return Response.ok().entity(informativeResponse).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Update an existing relationship between motivation, criteria and metrics.",
            description = "Update an existing relationship between motivation, criteria and metrics.")
    @APIResponse(
            responseCode = "200",
            description = "Relationship updated successfully.",
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
    @PUT
    @Path("/{id}/criteria-metrics")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCriteriaMetricsRelationship(@Parameter(
                                                              description = "The ID of the Motivation to update a relationship.",
                                                              required = true,
                                                              example = "pid_graph:3E109BBA",
                                                              schema = @Schema(type = SchemaType.STRING))
                                                      @PathParam("id")
                                                      @Valid
                                                      @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:")
                                                      @CheckPublished(repository = MotivationRepository.class, message = "No action permitted for published Motivation with the following id:", isPublishedPermitted = false) String id,
                                                      Set<@Valid CriterionMetricRequest> request) {

        if (Objects.isNull(request)) {

            request = new HashSet<>();
        }

        var messages = criterionMetricService.updateCriteriaMetricsRelationship(id, request, utility.getUserUniqueIdentifier());

        String result = String.join(StringUtils.LF, messages);

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.message = result;

        return Response.ok().entity(informativeResponse).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Get a list of relationships between motivation, criteria and metrics.",
            description = "Get a list of relationships between motivation, criteria and metrics.")
    @APIResponse(
            responseCode = "200",
            description = "A list of relationships between motivation, criteria and metrics.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableCriteriaMetricJunctionResponse.class)))
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
    @Path("/{id}/criteria-metrics")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCriteriaMetricsRelationship(@Parameter(
                                                           description = "The ID of the Motivation to assign Principles.",
                                                           required = true,
                                                           example = "pid_graph:3E109BBA",
                                                           schema = @Schema(type = SchemaType.STRING))
                                                   @PathParam("id")
                                                   @Valid @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:") String id,
                                                   @Parameter(name = "page", in = QUERY,
                                                           description = "Indicates the page number. Page number must be >= 1.")
                                                   @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.")
                                                   @QueryParam("page") int page,
                                                   @Parameter(name = "size", in = QUERY,
                                                           description = "The page size.")
                                                   @DefaultValue("10")
                                                   @Min(value = 1, message = "Page size must be between 1 and 100.")
                                                   @Max(value = 100, message = "Page size must be between 1 and 100.")
                                                   @QueryParam("size") int size,
                                                   @Context UriInfo uriInfo) {


        var response = criterionMetricService.getCriteriaMetricsRelationship(id, page - 1, size, uriInfo);

        return Response.ok().entity(response).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Create a new relations between motivation, metric and tests.",
            description = "Create a new relations between motivation, metric and tests.")
    @APIResponse(
            responseCode = "200",
            description = "Relations created successfully.",
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
    @Path("/{id}/metrics/{metric-id}/tests")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMetricTestRelation(
            @Parameter(
                    description = "The ID of the Motivation to update.",
                    required = true,
                    example = "pid_graph:3E109BBA",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid
            @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:")
            //@CheckPublished(repository = MotivationRepository.class, message = "No action permitted for published Motivation with the following id:", isPublishedPermitted = false)
            String id,

            @Parameter(
                    description = "The ID of the Metric to update.",
                    required = true,
                    example = "pid_graph:EBCEBED1",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("metric-id")
            @Valid
            @NotFoundEntity(repository = MetricRepository.class, message = "There is no Metric with the following id:") String metricId,

            @NotEmpty(message = "Metric-Test list can not be empty.")
            Set<@Valid MetricTestRequest> request) {

        var messages = motivationService.createMetricTestRelation(id, metricId,request, utility.getUserUniqueIdentifier());

        String result = String.join("\n", messages);

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.message = result;

        return Response.ok().entity(informativeResponse).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Get a list of relations between motivation, metric and tests.",
            description = "Get a list of relations between motivation, metric and tests.")
    @APIResponse(
            responseCode = "200",
            description = "A list of relations between motivation, metric and tests.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = DetailedMetricDto.class)))
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
    @Path("/{id}/metrics/{metric-id}/test")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMetricTestsRelation(
            @Parameter(description = "The ID of the Motivation to get Metric-Test relation.",
                    required = true,
                    example = "pid_graph:3E109BBA",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid
            @NotFoundEntity(repository = MotivationRepository.class,
                    message = "There is no Motivation with the following id:")
            String id,
            @Parameter(
                    description = "The ID of the Metric to get Metric-Tests.",
                    required = true,
                    example = "pid_graph:EBCEBED1",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("metric-id")
            @Valid
            @NotFoundEntity(repository = MetricRepository.class, message = "There is no Metric with the following id:") String metricId,

            @Parameter(name = "page", in = QUERY,
                    description = "Indicates the page number. Page number must be >= 1.")
            @DefaultValue("1")
            @Min(value = 1, message = "Page number must be >= 1.")
            @QueryParam("page") int page,
            @Parameter(name = "size", in = QUERY,
                    description = "The page size.")
            @DefaultValue("10")
            @Min(value = 1, message = "Page size must be between 1 and 100.")
            @Max(value = 100, message = "Page size must be between 1 and 100.")
            @QueryParam("size") int size,
            @Context UriInfo uriInfo) {

        var response = motivationService.getMetricTestRelation(id,metricId, page - 1, size, uriInfo);

        return Response.ok().entity(response).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Update an existing relationship between motivation, metric and tests.",
            description = "Update an existing relationship between motivation, metric and tests.")
    @APIResponse(
            responseCode = "200",
            description = "Relationship updated successfully.",
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
    @PUT
    @Path("/{id}/metrics/{metric-id}/tests")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMetricTestsRelation(
            @Parameter(description = "The ID of the Motivation to update.",
                    required = true,
                    example = "pid_graph:3E109BBA",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid
            @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:")
            @CheckPublished(repository = MotivationRepository.class, message = "No action permitted for published Motivation with the following id:", isPublishedPermitted = false)
            String id,
            @Parameter(
                    description = "The ID of the Metric to update.",
                    required = true,
                    example = "pid_graph:EBCEBED1",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("metric-id")
            @Valid
            @NotFoundEntity(repository = MetricRepository.class, message = "There is no Metric with the following id:") String metricId,

            List<@Valid MetricTestRequest> request) {

        if (Objects.isNull(request)) {
            request = new ArrayList<>();
        }

        var messages = motivationService.updateTestMetricRelation(id, metricId, request, utility.getUserUniqueIdentifier());

        String result = String.join("\n", messages);

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.message = result;

        return Response.ok().entity(informativeResponse).build();
    }


    @Tag(name = "Motivation")
    @Operation(
            summary = "Create a Metric - Definition relation for a Motivation.",
            description = "Create a Metric Definition for a single motivation.")
    @APIResponse(
            responseCode = "200",
            description = "Metric successfully created for the motivation.",
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
    @Path("/{id}/metric-definition")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMetric(
            @Parameter(
                    description = "The ID of the Motivation to create a metric for.",
                    required = true,
                    example = "pid_graph:3E109BBA",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid
            @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:")
            @CheckPublished(repository = MotivationRepository.class, message = "No action permitted for published Motivation with the following id:", isPublishedPermitted = false)
            String id,
            MotivationMetricExtendedRequest request) {

        var response = motivationService.createMetricDefinitionForMotivation(id, request, utility.getUserUniqueIdentifier());

        return Response.status(response.code).entity(response).build();
    }
    @Tag(name = "Motivation")
    @Operation(
            summary = "Get a list of relations between motivation, metric and definition.",
            description = "Get a list of relations between motivation, metric and definition.")
    @APIResponse(
            responseCode = "200",
            description = "A list of relations between motivation, metric and definition.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableMetricDefinitionJunctionResponse.class)))
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
    @Path("/{id}/metric-definition")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMetricDefinitionRelation(
            @Parameter(description = "The ID of the Motivation to assign Metric-Definition relation.",
                    required = true,
                    example = "pid_graph:3E109BBA",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid
            @NotFoundEntity(repository = MotivationRepository.class,
                    message = "There is no Motivation with the following id:")
            String id,
            @Parameter(name = "page", in = QUERY,
                    description = "Indicates the page number. Page number must be >= 1.")
            @DefaultValue("1")
            @Min(value = 1, message = "Page number must be >= 1.")
            @QueryParam("page") int page,
            @Parameter(name = "size", in = QUERY,
                    description = "The page size.")
            @DefaultValue("10")
            @Min(value = 1, message = "Page size must be between 1 and 100.")
            @Max(value = 100, message = "Page size must be between 1 and 100.")
            @QueryParam("size") int size,
            @Context UriInfo uriInfo) {

        var response = motivationService.getMetricDefinitionRelation(id, page - 1, size, uriInfo);

        return Response.ok().entity(response).build();
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Update an existing relationship between motivation, metric and type benchmark.",
            description = "Update an existing relationship between motivation, metric and type benchmark.")
    @APIResponse(
            responseCode = "200",
            description = "Relationship updated successfully.",
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
    @PUT
    @Path("/{id}/metric-definition")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMetricDefinitionRelation(
            @Parameter(description = "The ID of the Motivation to update.",
                    required = true,
                    example = "pid_graph:3E109BBA",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid
            @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:")
            @CheckPublished(repository = MotivationRepository.class, message = "No action permitted for published Motivation with the following id:", isPublishedPermitted = false) String id,
            Set<@Valid MetricDefinitionRequest> request) {

        if (Objects.isNull(request)) {
            request = new HashSet<>();
        }

        var messages = motivationService.updateMetricDefinitionRelation(id, request, utility.getUserUniqueIdentifier());

        String result = String.join("\n", messages);

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.message = result;

        return Response.ok().entity(informativeResponse).build();
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

    public static class PageableRelationsResponse extends PageResource<RelationsResponseDto> {

        private List<RelationsResponseDto> content;

        @Override
        public List<RelationsResponseDto> getContent() {
            return content;
        }

        @Override
        public void setContent(List<RelationsResponseDto> content) {
            this.content = content;
        }
    }

    public static class PageablePrincipleCriteriaJunctionResponse extends PageResource<PrincipleCriterionResponse> {

        private List<PrincipleCriterionResponse> content;

        @Override
        public List<PrincipleCriterionResponse> getContent() {
            return content;
        }

        @Override
        public void setContent(List<PrincipleCriterionResponse> content) {
            this.content = content;
        }
    }

    public static class PageablePrincipleResponse extends PageResource<PrincipleResponseDto> {

        private List<PrincipleResponseDto> content;

        @Override
        public List<PrincipleResponseDto> getContent() {
            return content;
        }

        @Override
        public void setContent(List<PrincipleResponseDto> content) {
            this.content = content;
        }
    }

    public static class PageableCriteriaMetricJunctionResponse extends PageResource<PrincipleCriterionResponseDto> {

        private List<PrincipleCriterionResponseDto> content;

        @Override
        public List<PrincipleCriterionResponseDto> getContent() {
            return content;
        }

        @Override
        public void setContent(List<PrincipleCriterionResponseDto> content) {
            this.content = content;
        }
    }

    public static class PageableMetricDefinitionJunctionResponse extends PageResource<MetricDefinitionExtendedResponse> {

        private List<MetricDefinitionExtendedResponse> content;

        @Override
        public List<MetricDefinitionExtendedResponse> getContent() {
            return content;
        }

        @Override
        public void setContent(List<MetricDefinitionExtendedResponse> content) {
            this.content = content;
        }
    }


    public static class PageableMetricTestJunctionResponse extends PageResource<MetricTestResponseDto> {

        private List<MetricTestResponseDto> content;

        @Override
        public List<MetricTestResponseDto> getContent() {
            return content;
        }

        @Override
        public void setContent(List<MetricTestResponseDto> content) {
            this.content = content;
        }
    }

    @Tag(name = "Motivation")
    @Operation(
            summary = "Remove a Principle from a Motivation.",
            description = "Removes  a principle from a specific Motivation and its associated relations.")
    @APIResponse(
            responseCode = "200",
            description = "Principle successfully deleted from the motivation.",
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
            description = "Motivation or Principle not found.",
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
    @DELETE
    @Path("/{id}/principles/{principle-id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response removePrincipleFromMotivation(@Parameter(
                                                          description = "The ID of the Motivation to update.",
                                                          required = true,
                                                          example = "1",
                                                          schema = @Schema(type = SchemaType.STRING))
                                                  @PathParam("id")
                                                  @Valid
                                                  @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:")
                                                  @CheckPublished(repository = MotivationRepository.class, message = "No action permitted for published Motivation with the following id:", isPublishedPermitted = false) String id,
                                                  @Parameter(
                                                          description = "The principle to be deleted.",
                                                          required = true,
                                                          example = "pid_graph:0E00C332",
                                                          schema = @Schema(type = SchemaType.STRING))
                                                  @PathParam("principle-id")
                                                  @Valid @NotFoundEntity(
                                                          repository = PrincipleRepository.class,
                                                          message = "There is no Principle with the following id: ")
                                                  String principleId) {

        var messages = motivationService.deletePrincipleFromMotivation(id, principleId);
        String result = String.join("\n", messages);

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.message = result;

        return Response.ok().entity(informativeResponse).build();
    }


    @Tag(name = "Motivation")
    @Operation(
            summary = "Update a Principle from a Motivation.",
            description = "Update  a principle from a specific Motivation and its associated relations.")
    @APIResponse(
            responseCode = "200",
            description = "Principle successfully updated from the motivation.",
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
            description = "Motivation or Principle not found.",
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
    @PUT
    @Path("/{id}/principles/{principle-id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response updatePrincipleFromMotivation(@Parameter(
                                                          description = "The ID of the Motivation to update.",
                                                          required = true,
                                                          example = "1",
                                                          schema = @Schema(type = SchemaType.STRING))
                                                  @PathParam("id")
                                                  @Valid
                                                  @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:")
                                                  @CheckPublished(repository = MotivationRepository.class, message = "No action permitted for published Motivation with the following id:", isPublishedPermitted = false) String id,
                                                  @Parameter(
                                                          description = "The principle to be deleted.",
                                                          required = true,
                                                          example = "pid_graph:0E00C332",
                                                          schema = @Schema(type = SchemaType.STRING))
                                                  @PathParam("principle-id")
                                                  @Valid @NotFoundEntity(
                                                          repository = PrincipleRepository.class,
                                                          message = "There is no Principle with the following id: ")
                                                  String principleId, @Valid @NotNull(message = "The request body is empty.") PrincipleUpdateDto principleRequestDto
    ) {

        var principle = motivationService.updatePrincipleFromMotivation(id, principleId, principleRequestDto);


        return Response.ok().entity(principle).build();
    }
}
