package org.grnet.cat.api.endpoints;

import com.networknt.schema.SpecVersion;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
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
import org.grnet.cat.api.utils.Utility;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.assessment.JsonAssessmentRequest;
import org.grnet.cat.dtos.assessment.JsonAssessmentResponse;
import org.grnet.cat.dtos.assessment.PartialJsonAssessmentResponse;
import org.grnet.cat.dtos.assessment.UpdateJsonAssessmentRequest;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.template.TemplateSubjectDto;
import org.grnet.cat.entities.JsonSchema;
import org.grnet.cat.repositories.ActorRepository;
import org.grnet.cat.repositories.AssessmentRepository;
import org.grnet.cat.repositories.AssessmentTypeRepository;
import org.grnet.cat.services.assessment.JsonAssessmentService;

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
                    implementation = JsonAssessmentResponse.class)))
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

        utility.validateTemplateJson(JsonSchema.fetchById("assessment_json_schema").getJsonSchema(), request.assessmentDoc, SpecVersion.VersionFlag.V7);

        var response = assessmentService.createAssessment(utility.getUserUniqueIdentifier(), request);

        var serverInfo = new CatServiceUriInfo(serverUrl.concat(uriInfo.getPath()));
        return Response.created(serverInfo.getAbsolutePathBuilder().path(String.valueOf(response.id)).build()).entity(response).build();
    }

    @Tag(name = "Assessment")
    @Operation(
            summary = "Get Assessment.",
            description = "Returns a specific assessment if it belongs to the user. The published assessment is returned as well.")
    @APIResponse(
            responseCode = "200",
            description = "The corresponding assessment.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = JsonAssessmentResponse.class)))
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

        var validations = assessmentService.getDtoAssessmentIfBelongsToUser(utility.getUserUniqueIdentifier(), id);

        return Response.ok().entity(validations).build();
    }

    @Tag(name = "Assessment")
    @Operation(
            summary = "Update Assessment Json Document.",
            description = "Updates the json document for an assessment.")
    @APIResponse(
            responseCode = "200",
            description = "Assessment's json document updated successfully.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = JsonAssessmentResponse.class)))
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

        var assessment = assessmentService.updatePrivateAssessmentBelongsToUser(id, utility.getUserUniqueIdentifier(), updateJsonAssessmentRequest);

        return Response.ok().entity(assessment).build();
    }

    @Tag(name = "Assessment")
    @Operation(
            summary = "Retrieve assessments for a specific user.",
            description = "This endpoint retrieves the assessments submitted by the specified user." +
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
                    implementation = PageablePartialAssessmentResponse.class)))
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
            description = "Deletes a private assessment if it is not published and belongs to the authenticated user.")
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

        assessmentService.deletePrivateAssessmentBelongsToUser(utility.getUserUniqueIdentifier(), id);

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
    public Response getAssessmentsObjectsByActor(@Parameter(
            description = "The Actor to retrieve assessment objects.",
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
                    implementation = JsonAssessmentResponse.class)))
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

    public static class PageablePartialAssessmentResponse extends PageResource<PartialJsonAssessmentResponse> {

        private List<PartialJsonAssessmentResponse> content;

        @Override
        public List<PartialJsonAssessmentResponse> getContent() {
            return content;
        }

        @Override
        public void setContent(List<PartialJsonAssessmentResponse> content) {
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