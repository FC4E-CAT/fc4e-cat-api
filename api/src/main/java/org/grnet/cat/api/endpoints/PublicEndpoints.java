package org.grnet.cat.api.endpoints;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.repositories.ActorRepository;
import org.grnet.cat.repositories.AssessmentTypeRepository;
import org.grnet.cat.services.ActorService;
import org.grnet.cat.services.assessment.JsonAssessmentService;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

@Path("/v1/public")
public class PublicEndpoints {

    @Inject
    ActorService actorService;

    @Inject
    JsonAssessmentService assessmentService;

    @Tag(name = "Public Endpoints")
    @Operation(
            summary = "Retrieve a list of available actors.",
            description = "Any unauthenticated user can retrieve the list of actors registered in the service. Each actor object includes basic information such as their id and name. " +
                    " By default, the first page of 10 Actors will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of Actors.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = CodelistEndpoint.PageableActor.class)))
    @APIResponse(
            responseCode = "400",
            description = "Bad Request",
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
    @Path("/actors")
    @Produces(MediaType.APPLICATION_JSON)
    public Response actorsByPage(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                 @Parameter(name = "size", in = QUERY,
                                         description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                 @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                 @Context UriInfo uriInfo) {

        var actors = actorService.getActorsByPage(page-1, size, uriInfo);

        return Response.ok().entity(actors).build();
    }

    @Tag(name = "Public Endpoints")
    @Operation(
            summary = "Get published assessments by type and actor.",
            description = "Any unauthenticated user can retrieve the published assessments categorized by type and actor, created by all users." +
                    "By default, the first page of 10 assessments will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of assessments.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = AssessmentsEndpoint.PageablePartialAssessmentResponse.class)))
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
    @Path("/assessments/by-type/{type-id}/by-actor/{actor-id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response assessmentsByTypeAndActor(@Parameter(
            description = "The Type of Assessment.",
            required = true,
            example = "1",
            schema = @Schema(type = SchemaType.NUMBER))
                                              @PathParam("type-id") @Valid @NotFoundEntity(repository = AssessmentTypeRepository.class, message = "There is no Assessment Type with the following id:") Long typeId, @Parameter(
            description = "The Actor to retrieve template.",
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

        var assessments = assessmentService.getPublishedAssessmentsByTypeAndActorAndPage(page - 1, size, typeId, actorId, uriInfo);

        return Response.ok().entity(assessments).build();
    }
}