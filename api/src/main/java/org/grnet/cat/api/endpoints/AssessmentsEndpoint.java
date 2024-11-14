package org.grnet.cat.api.endpoints;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
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
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.assessment.*;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.template.TemplateSubjectDto;
import org.grnet.cat.repositories.ActorRepository;
import org.grnet.cat.repositories.AssessmentRepository;
import org.grnet.cat.repositories.AssessmentTypeRepository;
import org.grnet.cat.repositories.CommentRepository;
import org.grnet.cat.repositories.MotivationAssessmentRepository;
import org.grnet.cat.repositories.registry.MotivationRepository;
import org.grnet.cat.repositories.registry.RegistryActorRepository;
import org.grnet.cat.services.CommentService;
import org.grnet.cat.services.assessment.JsonAssessmentService;
import org.grnet.cat.utils.Utility;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

@Path("/v1/assessments")
public class AssessmentsEndpoint {

    @Inject
    Utility utility;

    @ConfigProperty(name = "api.server.url")
    String serverUrl;

    @Inject
    JsonAssessmentService assessmentService;

    @Inject
    CommentService commentService;

    @Tag(name = "Assessment")
    @Operation(
            summary = "Request to create an assessment.",
            description = "This endpoint allows a validated user to create an assessment for a validation request." +
                    " The validated user should provide the necessary information to support their request. " +
                    "The API provides flexibility for clients to either use an existing Subject by specifying its id in the subject.db_id property " +
                    "or create a new one by leaving subject.db_id empty and filling in the other three properties (subject.name, subject.type and subject.id)")
    @APIResponse(
            responseCode = "201",
            description = "Assessment creation successful.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = UserJsonAssessmentResponse.class)))
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
            description = "Entity Not Found.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "409",
            description = "Assessment already exists.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "501",
            description = "Not Implemented.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @SecurityRequirement(name = "Authentication")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @Registration
    public Response create(@Valid @NotNull(message = "The request body is empty.") JsonAssessmentRequest request, @Context UriInfo uriInfo) {

        var response = assessmentService.createAssessment(utility.getUserUniqueIdentifier(), request);

        var serverInfo = new CatServiceUriInfo(serverUrl.concat(uriInfo.getPath()));
        return Response.created(serverInfo.getAbsolutePathBuilder().path(String.valueOf(response.id)).build()).entity(response).build();
    }

    @Tag(name = "Assessment")
    @Operation(
            summary = "Get Assessment.",
            description = "Returns a specific assessment if it belongs or shared to the user.")
    @APIResponse(
            responseCode = "200",
            description = "The corresponding assessment.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = UserJsonAssessmentResponse.class)))
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
    @Authenticated
    @Registration
    public Response getAssessment(@Parameter(
            description = "The ID of the assessment to retrieve.",
            required = true,
            example = "c242e43f-9869-4fb0-b881-631bc5746ec0",
            schema = @Schema(type = SchemaType.STRING)) @PathParam("id")
                                  @Valid @NotFoundEntity(repository = AssessmentRepository.class, message = "There is no Assessment with the following id:") String id) {

        var assessment = assessmentService.getDtoAssessmentIfBelongsOrSharedToUser(id);

        return Response.ok().entity(assessment).build();
    }

    @Tag(name = "Assessment")
    @Operation(
            summary = "Update Assessment Json Document.",
            description = "Updates the assessment json document if it belongs or shared to the authenticated user.")
    @APIResponse(
            responseCode = "200",
            description = "Assessment's json document updated successfully.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = UserJsonAssessmentResponse.class)))
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
    @Path("/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @Registration
    public Response updateAssessment(@Parameter(
            description = "The ID of the assessment to update.",
            required = true,
            example = "c242e43f-9869-4fb0-b881-631bc5746ec0",
            schema = @Schema(type = SchemaType.STRING))
                                     @PathParam("id")
                                     @Valid @NotFoundEntity(repository = AssessmentRepository.class, message = "There is no assessment with the following id:") String id,
                                     @Valid @NotNull(message = "The request body is empty.") JsonAssessmentRequest updateJsonAssessmentRequest) {

        var assessment = assessmentService.updatePrivateAssessmentBelongsToUser(id, updateJsonAssessmentRequest);

        return Response.ok().entity(assessment).build();
    }

    @Tag(name = "Assessment")
    @Operation(
            summary = "Retrieve assessments for a specific user.",
            description = "This endpoint retrieves both the assessments submitted by the specified user and the assessments shared with the specified user." +
                    "By default, the first page of 10 assessments will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of assessments.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageablePartialAssessmentResponse.class)))
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
    @Authenticated
    @Registration
    public Response assessments(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                @Parameter(name = "size", in = QUERY,
                                        description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                @Parameter(name = "subject_name", in = QUERY,
                                        description = "The subject name to filter.") @QueryParam("subject_name") @DefaultValue("") String subjectName,
                                @Parameter(name = "subject_type", in = QUERY,
                                        description = "The subject type to filter.") @QueryParam("subject_type") @DefaultValue("") String subjectType,
                                @Parameter(name = "actor", in = QUERY,
                                        description = "The actor to filter.") @QueryParam("actor")  Long actorId,
                                @Context UriInfo uriInfo) {

        var assessments = assessmentService.getDtoAssessmentsByUserAndPage(page - 1, size, uriInfo, utility.getUserUniqueIdentifier(), subjectName, subjectType, actorId);

        return Response.ok().entity(assessments).build();
    }

    @Tag(name = "Assessment")
    @Operation(
            summary = "Get published assessments by type and actor.",
            description = "This endpoint is public and any unauthenticated user can retrieve published assessments categorized by type and actor, created by all users." +
                    "By default, the first page of 10 assessments will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of assessments.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = AdminEndpoint.PageablePartialAssessmentResponse.class)))
    @APIResponse(
            responseCode = "400",
            description = "Bad Request",
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
    @GET
    @Path("/by-type/{type-id}/by-actor/{actor-id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response assessmentsByTypeAndActor(@Parameter(
            description = "The Type of Assessment.",
            required = true,
            example = "1",
            schema = @Schema(type = SchemaType.NUMBER))
                                              @PathParam("type-id") @Valid @NotFoundEntity(repository = AssessmentTypeRepository.class, message = "There is no Assessment Type with the following id:") Long typeId, @Parameter(
            description = "The Actor to retrieve assessments.",
            required = true,
            example = "6",
            schema = @Schema(type = SchemaType.NUMBER))
                                              @PathParam("actor-id") @Valid @NotFoundEntity(repository = ActorRepository.class, message = "There is no Actor with the following id:") Long actorId,
                                              @Parameter(name = "page", in = QUERY,
                                                      description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                              @Parameter(name = "size", in = QUERY,
                                                      description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                              @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                              @Parameter(name = "subject_name", in = QUERY,
                                                      description = "The subject name to filter.") @QueryParam("subject_name") @DefaultValue("") String subjectName,
                                              @Parameter(name = "subject_type", in = QUERY,
                                                      description = "The subject type to filter.") @QueryParam("subject_type") @DefaultValue("") String subjectType,
                                              @Context UriInfo uriInfo) {

        var assessments = assessmentService.getPublishedDtoAssessmentsByTypeAndActorAndPage(page - 1, size, typeId, actorId, uriInfo, subjectName, subjectType);

        return Response.ok().entity(assessments).build();
    }

    @Tag(name = "Assessment")
    @Operation(
            summary = "Get list of public assessment objects created / used by a specific user by type and actor.",
            description = "This endpoint is public and any unauthenticated user can retrieve published assessment objects categorized by type and actor, created by all users." +
                    "By default, the first page of 10 assessments will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of public assessment objects.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableObjects.class)))
    @APIResponse(
            responseCode = "400",
            description = "Bad Request",
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
    @GET
    @Path("public-objects/by-type/{type-id}/by-actor/{actor-id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Deprecated
    public Response objectsByTypeAndActor(@Parameter(
            description = "The Type of Assessment.",
            required = true,
            example = "1",
            schema = @Schema(type = SchemaType.NUMBER))
                                          @PathParam("type-id") @Valid @NotFoundEntity(repository = AssessmentTypeRepository.class, message = "There is no Assessment Type with the following id:") Long typeId, @Parameter(
            description = "The Actor to retrieve assessments.",
            required = true,
            example = "6",
            schema = @Schema(type = SchemaType.NUMBER))
                                          @PathParam("actor-id") @Valid @NotFoundEntity(repository = ActorRepository.class, message = "There is no Actor with the following id:") Long actorId,
                                          @Parameter(name = "page", in = QUERY,
                                                  description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                          @Parameter(name = "size", in = QUERY,
                                                  description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                          @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                          @Context UriInfo uriInfo) {

        var assessments = assessmentService.getPublishedDtoAssessmentObjectsByTypeAndActorAndPage(page - 1, size, typeId, actorId, uriInfo);

        return Response.ok().entity(assessments).build();
    }

    @Tag(name = "Assessment")
    @Operation(
            summary = "Delete private Assessment.",
            description = "Deletes a private assessment if it belongs or shared to the authenticated user.")
    @APIResponse(
            responseCode = "200",
            description = "Deletion completed.",
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
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @Registration
    public Response deleteAssessment(@Parameter(
            description = "The ID of the assessment to be deleted.",
            required = true,
            example = "c242e43f-9869-4fb0-b881-631bc5746ec0",
            schema = @Schema(type = SchemaType.STRING)) @PathParam("id")
                                     @Valid @NotFoundEntity(repository = AssessmentRepository.class, message = "There is no Assessment with the following id:") String id) {

        assessmentService.deletePrivateAssessmentBelongsToUser(id);

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.message = "Assessment has been successfully deleted.";

        return Response.ok().entity(informativeResponse).build();
    }

    @Tag(name = "Assessment")
    @Operation(
            summary = "Get list of assessment objects created / used by a specific user by a specific actor.",
            description = "This endpoint returns a list of assessment objects created / used by a specific user by a specific actor." +
                    "By default, the first page of 10 assessment objects will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of assessment objects created by a user.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableObjects.class)))
    @APIResponse(
            responseCode = "400",
            description = "Bad Request",
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
    @Path("objects/by-actor/{actor-id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @Registration
    public Response getAssessmentsObjectsByActor(
            @Parameter(
            description = "The Actor to retrieve assessment objects.",
            required = true,
            example = "pid_graph:0E00C332",
            schema = @Schema(type = SchemaType.STRING))
            @PathParam("actor-id")
            @Valid @NotFoundEntity(repository = RegistryActorRepository.class, message = "There is no Actor with the following id:")
            String actorId,
            @Parameter(name = "page", in = QUERY,
                    description = "Indicates the page number. Page number must be >= 1.")
            @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.")
            @QueryParam("page")
            int page,
            @Parameter(name = "size", in = QUERY,
                    description = "The page size.")
            @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.") @Max(value = 100, message = "Page size must be between 1 and 100.")
            @QueryParam("size")
            int size,
            @Context UriInfo uriInfo) {

        var assessments = assessmentService.getAssessmentsObjectsByUserAndActor(page - 1, size, uriInfo, utility.getUserUniqueIdentifier(), actorId);

        return Response.ok().entity(assessments).build();
    }

    @Tag(name = "Assessment")
    @Operation(
            summary = "Get list of assessment objects created / used by a specific user.",
            description = "This endpoint returns a list of assessment objects created / used by a specific user." +
                    "By default, the first page of 10 assessment objects will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of assessment objects created by a user.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableObjects.class)))
    @APIResponse(
            responseCode = "400",
            description = "Bad Request",
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
    @GET
    @Path("objects")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @Registration
    @Deprecated
    public Response getAssessmentsObjects(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                          @Parameter(name = "size", in = QUERY,
                                                  description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                          @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                          @Context UriInfo uriInfo) {

        var assessments = assessmentService.getAssessmentsObjectsByUser(page - 1, size, uriInfo, utility.getUserUniqueIdentifier());

        return Response.ok().entity(assessments).build();
    }

    @Tag(name = "Assessment")
    @Operation(
            summary = "Get public assessment.",
            description = "Returns a specific public assessment.")
    @APIResponse(
            responseCode = "200",
            description = "The corresponding public assessment.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = AdminJsonAssessmentResponse.class)))
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
    @Path("/public/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPublicAssessment(@Parameter(
            description = "The ID of the public assessment to retrieve.",
            required = true,
            example = "c242e43f-9869-4fb0-b881-631bc5746ec0",
            schema = @Schema(type = SchemaType.STRING)) @PathParam("id")
                                        @Valid @NotFoundEntity(repository = AssessmentRepository.class, message = "There is no Assessment with the following id:") String id) {

        var validations = assessmentService.getPublicDtoAssessment(id);

        return Response.ok().entity(validations).build();
    }

    @Tag(name = "Assessment")
    @Operation(
            summary = "Share an assessment with other users.",
            description = "A validated user can share their created assessment with other users.")
    @APIResponse(
            responseCode = "200",
            description = "Assessment shared successfully.",
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
            description = "Not Found.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "409",
            description = "Conflict.",
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
    @Path("/{id}/share")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @Registration
    public Response share(@Parameter(
            description = "The unique identifier of the assessment to be shared.",
            required = true,
            example = "c242e43f-9869-4fb0-b881-631bc5746ec0",
            schema = @Schema(type = SchemaType.STRING)) @PathParam("id")
                          @Valid @NotFoundEntity(repository = MotivationAssessmentRepository.class, message = "There is no Assessment with the following id:") String id,
                          @Valid @NotNull(message = "The request body is empty.") ShareAssessmentRequest request) {

        assessmentService.shareAssessment(id, request.sharedWithUser);

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.message = "Assessment shared successfully";

        return Response.ok().entity(informativeResponse).build();
    }

    @Tag(name = "Assessment")
    @Operation(
            summary = "Get all users with whom an assessment has been shared.",
            description = "Returns a list of users to whom a specific assessment has been shared by the current user.")
    @APIResponse(
            responseCode = "200",
            description = "A list of users to whom the assessment has been shared.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = SharedUsersResponse.class)))
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
    @Path("/{id}/shared-users")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @Registration
    public Response getSharedUsers(@Parameter(
            description = "The ID of the assessment.",
            required = true,
            example = "c242e43f-9869-4fb0-b881-631bc5746ec0",
            schema = @Schema(type = SchemaType.STRING)) @PathParam("id")
                                   @Valid @NotFoundEntity(repository = MotivationAssessmentRepository.class, message = "There is no Assessment with the following id:") String id) {

        var sharedUsers = assessmentService.getSharedUsers(id);

        var response = new SharedUsersResponse();
        response.sharedUsers = sharedUsers;

        return Response.ok().entity(response).build();
    }

    @Tag(name = "Assessment")
    @Operation(
            summary = "Create a comment for a specific assessment",
            description = "A validated user can create a comment for a specified assessment."
    )
    @APIResponse(
            responseCode = "201",
            description = "Assessment comment created successfully.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = CommentResponseDto.class)))
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
    @POST
    @Path("/{id}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @Registration
    public Response createComment(
            @Parameter(
                    description = "The unique identifier of the assessment to create a comment.",
                    required = true,
                    example = "c242e43f-9869-4fb0-b881-631bc5746ec0",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id") @Valid @NotFoundEntity(
                    repository = MotivationAssessmentRepository.class,
                    message = "There is no Assessment with the following id:")
            String id,
            @Valid @NotNull(
                    message = "The request body is empty.")
            CommentRequestDto request, @Context UriInfo uriInfo) {

        var serverInfo = new CatServiceUriInfo(serverUrl.concat(uriInfo.getPath()));
        var response = commentService.addComment(id, utility.getUserUniqueIdentifier(), request);

        return Response.created(serverInfo.getAbsolutePathBuilder().path(String.valueOf(response.id)).build()).entity(response).build();


    }

    @Tag(name = "Assessment")
    @Operation(
            summary = "Retrieve all comments for a specific assessment",
            description = "A validated user can retrieve a list of comments associated with a specific assessment."
    )
    @APIResponse(
            responseCode = "200",
            description = "List of comments.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = CommentResponseDto.class)))
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
    @Path("/{id}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @Registration
    public Response getAllCommentsForAssessment(
            @Parameter(
                    description = "The unique identifier of the assessment to create a comment.",
                    required = true,
                    example = "c242e43f-9869-4fb0-b881-631bc5746ec0",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id") @Valid @NotFoundEntity(
                    repository = MotivationAssessmentRepository.class,
                    message = "There is no Assessment with the following id:")
            String id,
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @Context UriInfo uriInfo) {

        var comments = commentService.listComments(id, page - 1, size, uriInfo);

        return Response.ok(comments).build();
    }

    @Tag(name = "Assessment")
    @Operation(
            summary = "Update a comment for a specific assessment",
            description = "A validated user can update a comment associated with a specific assessment."
    )
    @APIResponse(
            responseCode = "200",
            description = "Comment updated successfully.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = CommentResponseDto.class)))
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
    @PUT
    @Path("{id}/comments/{comment-id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    @Registration
    public Response updateComment(
            @Parameter(
                    description = "The unique identifier of the assessment to create a comment.",
                    required = true,
                    example = "c242e43f-9869-4fb0-b881-631bc5746ec0",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id") @Valid @NotFoundEntity(
                    repository = MotivationAssessmentRepository.class,
                    message = "There is no Assessment with the following id:")
            String assessmentId,
            @Parameter(
                    description = "The unique identifier of the comment.",
                    required = true,
                    example = "22",
                    schema = @Schema(type = SchemaType.NUMBER))
            @PathParam("comment-id") @Valid @NotFoundEntity(
                    repository = CommentRepository.class,
                    message = "Comment with the given ID not found.")
                    Long commentId,
            @Valid @NotNull CommentRequestDto commentRequestDto) {

        var updatedComment = commentService.updateComment(commentId,commentRequestDto, utility.getUserUniqueIdentifier());

        return Response.ok().entity(updatedComment).build();
    }

    @Tag(name = "Assessment")
    @Operation(
            summary = "Delete a comment for a specific assessment",
            description = "A validated user can delete a comment associated with a specific assessment."
    )
    @APIResponse(
            responseCode = "200",
            description = "List of comments.",
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
    @DELETE
    @Path("{id}/comments/{comment-id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @Registration
    public Response deleteComment(
            @Parameter(
                    description = "The unique identifier of the assessment to create a comment.",
                    required = true,
                    example = "c242e43f-9869-4fb0-b881-631bc5746ec0",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id") @Valid @NotFoundEntity(
                    repository = MotivationAssessmentRepository.class,
                    message = "There is no Assessment with the following id:")
                    String assessmentId,
            @Parameter(
                    description = "The unique identifier of the comment.",
                    required = true,
                    example = "22",
                    schema = @Schema(type = SchemaType.NUMBER))
            @PathParam("comment-id") @Valid @NotFoundEntity(
                    repository = CommentRepository.class,
                    message = "Comment with the given ID not found.")
                    Long commentId){

        commentService.deleteComment(commentId, utility.getUserUniqueIdentifier());

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.message = "Comment has been successfully deleted.";

        return Response.ok().entity(informativeResponse).build();
    }

    public static class PageablePartialAssessmentResponse extends PageResource<UserPartialJsonAssessmentResponse> {

        private List<UserPartialJsonAssessmentResponse> content;

        @Override
        public List<UserPartialJsonAssessmentResponse> getContent() {
            return content;
        }

        @Override
        public void setContent(List<UserPartialJsonAssessmentResponse> content) {
            this.content = content;
        }
    }

    public static class PageableObjects extends PageResource<TemplateSubjectDto> {

        private List<TemplateSubjectDto> content;

        @Override
        public List<TemplateSubjectDto> getContent() {
            return content;
        }

        @Override
        public void setContent(List<TemplateSubjectDto> content) {
            this.content = content;
        }
    }
}