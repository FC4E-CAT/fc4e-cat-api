package org.grnet.cat.api.endpoints;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
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
import org.grnet.cat.api.utils.Utility;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.assessment.JsonAssessmentRequest;
import org.grnet.cat.dtos.assessment.JsonAssessmentResponse;
import org.grnet.cat.dtos.assessment.UpdateJsonAssessmentRequest;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.repositories.AssessmentRepository;
import org.grnet.cat.services.assessment.JsonAssessmentService;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

@Path("/v1/assessments")
@Authenticated

public class AssessmentsEndpoint {

    @Inject
    Utility utility;

    @ConfigProperty(name = "server.url")
    String serverUrl;

    @Inject
    JsonAssessmentService assessmentService;

    @Tag(name = "Assessment")
    @Operation(
            summary = "Request to create an assessment.",
            description = "This endpoint allows a validated user to create an assessment for a validation request." +
                    " The validated user should provide the necessary information to support their request.")
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
    @Registration
    public Response create(@Valid @NotNull(message = "The request body is empty.") JsonAssessmentRequest request, @Context UriInfo uriInfo) {

        var response = assessmentService.createAssessment(utility.getUserUniqueIdentifier(), request);

        var serverInfo = new CatServiceUriInfo(serverUrl.concat(uriInfo.getPath()));
        return Response.created(serverInfo.getAbsolutePathBuilder().path(String.valueOf(response.id)).build()).entity(response).build();
    }

    @Tag(name = "Assessment")
    @Operation(
            summary = "Get Assessment.",
            description = "Returns a specific assessment if it belongs to the user.")
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
    public Response getAssessment(@Parameter(
            description = "The ID of the assessment to retrieve.",
            required = true,
            example = "1",
            schema = @Schema(type = SchemaType.NUMBER)) @PathParam("id")
                                  @Valid @NotFoundEntity(repository = AssessmentRepository.class, message = "There is no Assessment with the following id:") Long id) {

        var validations = assessmentService.getAssessment(utility.getUserUniqueIdentifier(), id);

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
            responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @SecurityRequirement(name = "Authentication")
    @Path("/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response updateAssessment(@Parameter(
            description = "The ID of the assessment to update.",
            required = true,
            example = "1",
            schema = @Schema(type = SchemaType.NUMBER))
                                         @PathParam("id")
                                         @Valid @NotFoundEntity(repository = AssessmentRepository.class, message = "There is no assessment with the following id:") Long id,
                                     @Valid @NotNull(message = "The request body is empty.") UpdateJsonAssessmentRequest updateJsonAssessmentRequest) {

        var assessment = assessmentService.updateAssessment(id,utility.getUserUniqueIdentifier(), updateJsonAssessmentRequest);

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
                    implementation = PageableAssessmentResponse.class)))
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
    public Response assessments(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                @Parameter(name = "size", in = QUERY,
                                        description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                @Context UriInfo uriInfo) {

        var assessments = assessmentService.getAssessmentsByUserAndPage(page-1, size, uriInfo, utility.getUserUniqueIdentifier());

        return Response.ok().entity(assessments).build();
    }

    public static class PageableAssessmentResponse extends PageResource<JsonAssessmentResponse> {

        private List<JsonAssessmentResponse> content;

        @Override
        public List<JsonAssessmentResponse> getContent() {
            return content;
        }

        @Override
        public void setContent(List<JsonAssessmentResponse> content) {
            this.content = content;
        }
    }
}