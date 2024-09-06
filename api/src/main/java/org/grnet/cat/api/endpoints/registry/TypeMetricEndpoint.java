package org.grnet.cat.api.endpoints.registry;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
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
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.metric.TypeMetricResponseDto;
import org.grnet.cat.repositories.registry.metric.TypeMetricRepository;
import org.grnet.cat.services.registry.metric.TypeMetricService;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

@Path("/v1/registry/type-metric")
@Authenticated
public class TypeMetricEndpoint {

    @Inject
    TypeMetricService typeMetricService;

    @Tag(name = "Metrics")
    @Operation(
            summary = "Get Type Metric by ID.",
            description = "Retrieves a specific Type Metric item by ID."
    )
    @APIResponse(
            responseCode = "200",
            description = "The corresponding Type Metric item.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = TypeMetricResponseDto.class))
    )
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
    @Path("/{id}")
    public Response getTypeMetricById(
            @Parameter(
                    description = "The ID of the Type Metric to be retrieved.",
                    required = true,
                    example = "pid_graph:3E109BBA",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid @NotFoundEntity(repository = TypeMetricRepository.class, message = "There is no Type Metric with the following id:") String id) {

        var response = typeMetricService.getTypeMetricById(id);

        return Response.ok(response).build();
    }

    @Tag(name = "Metrics")
    @Operation(
            summary = "List all Type Metric items.",
            description = "Retrieves a paginated list of Type Metric items.")
    @APIResponse(
            responseCode = "200",
            description = "List of Type Metric items.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableTypeMetricResponse.class)))
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
    public Response listTypeMetrics(
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

        var typeMetrics = typeMetricService.listAllTypeMetrics(page - 1, size, uriInfo);

        return Response.ok(typeMetrics).build();
    }

    public static class PageableTypeMetricResponse extends PageResource<TypeMetricResponseDto> {

        private List<TypeMetricResponseDto> content;

        @Override
        public List<TypeMetricResponseDto> getContent() {
            return content;
        }

        @Override
        public void setContent(List<TypeMetricResponseDto> content) {
            this.content = content;
        }
    }

}
