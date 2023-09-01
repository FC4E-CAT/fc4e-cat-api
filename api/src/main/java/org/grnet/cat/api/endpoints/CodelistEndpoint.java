package org.grnet.cat.api.endpoints;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
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
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.grnet.cat.api.filters.Registration;
import org.grnet.cat.dtos.ActorDto;
import org.grnet.cat.dtos.assessment.AssessmentTypeDto;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.services.ActorService;
import org.grnet.cat.services.AssessmentTypeService;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

@Path("/v1/codelist")
@Authenticated
public class CodelistEndpoint {

    @Inject
    ActorService actorService;

    @Inject
    AssessmentTypeService assessmentTypeService;

    @Tag(name = "Codelist")
    @Operation(
            summary = "Retrieve a list of available actors.",
            description = "This endpoint returns a list of actors registered in the service. Each actor object includes basic information such as their id and name. " +
                    " By default, the first page of 10 Actors will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of Actors.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableActor.class)))
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
    @Path("/actors")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response actorsByPage(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                @Parameter(name = "size", in = QUERY,
                                        description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                @Context UriInfo uriInfo) {

        var actors = actorService.getActorsByPage(page-1, size, uriInfo);

        return Response.ok().entity(actors).build();
    }

    @Tag(name = "Codelist")
    @Operation(
            summary = "Get available Assessment Types.",
            description = "This endpoint retrieves a list of available assessment types." +
                    " By default, the first page of 10 Actors will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of Assessment types.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableAssessmentTypes.class)))
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
    @Path("/assessment-types")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response assessmentsByPage(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                 @Parameter(name = "size", in = QUERY,
                                         description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                 @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                 @Context UriInfo uriInfo) {

        var actors = assessmentTypeService.getAssessmentTypesByPage(page-1, size, uriInfo);

        return Response.ok().entity(actors).build();
    }

    public static class PageableActor extends PageResource<ActorDto> {

        private List<ActorDto> content;

        @Override
        public List<ActorDto> getContent() {
            return content;
        }

        @Override
        public void setContent(List<ActorDto> content) {
            this.content = content;
        }
    }

    public static class PageableAssessmentTypes extends PageResource<AssessmentTypeDto> {

        private List<AssessmentTypeDto> content;

        @Override
        public List<AssessmentTypeDto> getContent() {
            return content;
        }

        @Override
        public void setContent(List<AssessmentTypeDto> content) {
            this.content = content;
        }
    }
}