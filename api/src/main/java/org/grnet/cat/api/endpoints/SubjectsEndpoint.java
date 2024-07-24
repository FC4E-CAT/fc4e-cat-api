package org.grnet.cat.api.endpoints;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.DELETE;
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
import org.grnet.cat.services.utils.Utility;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.subject.SubjectRequest;
import org.grnet.cat.dtos.subject.SubjectResponse;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.dtos.subject.UpdateSubjectRequestDto;
import org.grnet.cat.exceptions.ConflictException;
import org.grnet.cat.exceptions.InternalServerErrorException;
import org.grnet.cat.repositories.SubjectRepository;
import org.grnet.cat.services.SubjectService;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

@Path("/v1/subjects")
@Authenticated
public class SubjectsEndpoint {

    /**
     * Injection point for the Subject service
     */
    @Inject
    SubjectService subjectService;

    /**
     * Injection point for the Utility class
     */
    @Inject
    Utility utility;

    @ConfigProperty(name = "api.server.url")
    String serverUrl;

    @Tag(name = "Subject")
    @Operation(
            summary = "Create a new subject.",
            description = "Create a new subject.")
    @APIResponse(
            responseCode = "201",
            description = "Subject created successfully.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = SubjectResponse.class)))
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
            description = "Subject already exists.",
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
    public Response create(@Valid @NotNull(message = "The request body is empty.") SubjectRequest request, @Context UriInfo uriInfo) {

        var subject = subjectService.createSubject(request, utility.getUserUniqueIdentifier());

        var serverInfo = new CatServiceUriInfo(serverUrl.concat(uriInfo.getPath()));

        return Response.created(serverInfo.getAbsolutePathBuilder().path(String.valueOf(subject.id)).build()).entity(subject).build();
    }

    @Tag(name = "Subject")
    @Operation(
            summary = "Delete a subject.",
            description = "Deletes a subject if it belongs to the authenticated user.")
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
    @Registration
    public Response deleteSubject(@Parameter(
            description = "The ID of the subject to be deleted.",
            required = true,
            example = "1",
            schema = @Schema(type = SchemaType.NUMBER)) @PathParam("id")
                                  @Valid @NotFoundEntity(repository = SubjectRepository.class, message = "There is no Subject with the following id:") Long id) {

        subjectService.deleteSubjectBelongsToUser(utility.getUserUniqueIdentifier(), id);

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.message = "Subject has been successfully deleted.";

        return Response.ok().entity(informativeResponse).build();
    }

    @Tag(name = "Subject")
    @Operation(
            summary = "Get list of objects created by a specific user.",
            description = "This endpoint retrieves the Subjects submitted by the specified user." +
                    "By default, the first page of 10 Subjects will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of Subjects.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableSubjectResponse.class)))
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
    public Response getSubjects(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                @Parameter(name = "size", in = QUERY,
                                        description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                @Context UriInfo uriInfo) {

        var subjects = subjectService.getSubjectsByUserAndPage(page - 1, size, uriInfo, utility.getUserUniqueIdentifier());

        return Response.ok().entity(subjects).build();
    }

    @Tag(name = "Subject")
    @Operation(
            summary = "Get specific Subject.",
            description = "Returns a specific Subject.")
    @APIResponse(
            responseCode = "200",
            description = "The corresponding Subject.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = SubjectResponse.class)))
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
    public Response getSubject(@Parameter(
            description = "The ID of the Subject to retrieve.",
            required = true,
            example = "1",
            schema = @Schema(type = SchemaType.NUMBER))
                               @PathParam("id")
                               @Valid @NotFoundEntity(repository = SubjectRepository.class, message = "There is no Subject with the following id:") Long id) {

        var subject = subjectService.getSubjectByUserAndId(id, utility.getUserUniqueIdentifier());

        return Response.ok().entity(subject).build();
    }

    @Tag(name = "Subject")
    @Operation(
            summary = "Updates an existing Subject.",
            description = "To update the resource properties, the body of the request must contain an updated representation of Subject. " +
                    "You can update a part or all attributes of Subject. The empty or null values are ignored.")
    @APIResponse(
            responseCode = "200",
            description = "Subject was updated successfully.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = SubjectResponse.class)))
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
            description = "Subject already exists.",
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
    public Response updateSubject(@Parameter(
            description = "The ID of the Subject to update.",
            required = true,
            example = "1",
            schema = @Schema(type = SchemaType.NUMBER))
                                  @PathParam("id")
                                  @Valid @NotFoundEntity(repository = SubjectRepository.class, message = "There is no Subject with the following id:") Long id,
                                  @Valid @NotNull(message = "The request body is empty.") UpdateSubjectRequestDto request) {

        SubjectResponse subject = null;

        try {
            subject = subjectService.update(id, request, utility.getUserUniqueIdentifier());

        } catch (Exception e) {

            if (e.getCause().getCause() instanceof ConstraintViolationException) {

                throw new ConflictException(String.format("This subject {%s, %s, %s} has already been created.", request.id, request.name, request.type));
            } else {
                throw new InternalServerErrorException("Internal Server Error", 500);
            }
        }

        return Response.ok().entity(subject).build();
    }

    @Tag(name = "Subject")
    @Operation(
            summary = "Get list of Assessments for a specific Subject.",
            description = "Returns a list of Assessments.")
    @APIResponse(
            responseCode = "200",
            description = "The list of Assessments for a Subject.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = AssessmentsEndpoint.PageablePartialAssessmentResponse.class)))
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
    @Path("/{id}/assessments")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response getSubjectAssessments(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                          @Parameter(name = "size", in = QUERY,
                                                  description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                          @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                          @Parameter(
                                                  description = "The ID of the Subject to retrieve.",
                                                  required = true,
                                                  example = "1",
                                                  schema = @Schema(type = SchemaType.NUMBER))
                                          @PathParam("id")
                                          @Valid @NotFoundEntity(repository = SubjectRepository.class, message = "There is no Subject with the following id:") Long id,

                                          @Context UriInfo uriInfo) {

        var assessments = subjectService.getAssessmentsPerSubject(page-1, size, id, uriInfo);

        return Response.ok().entity(assessments).build();
    }

    public static class PageableSubjectResponse extends PageResource<SubjectResponse> {

        private List<SubjectResponse> content;

        @Override
        public List<SubjectResponse> getContent() {
            return content;
        }

        @Override
        public void setContent(List<SubjectResponse> content) {
            this.content = content;
        }
    }
}
