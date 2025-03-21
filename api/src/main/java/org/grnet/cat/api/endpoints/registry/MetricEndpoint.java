package org.grnet.cat.api.endpoints.registry;

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
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.grnet.cat.api.filters.Registration;
import org.grnet.cat.api.utils.CatServiceUriInfo;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.MetricDefinitionExtendedResponse;
import org.grnet.cat.dtos.registry.metric.MetricRequestDto;
import org.grnet.cat.dtos.registry.metric.MetricResponseDto;
import org.grnet.cat.dtos.registry.metric.MetricUpdateDto;
import org.grnet.cat.repositories.registry.metric.MetricRepository;
import org.grnet.cat.services.registry.metric.MetricService;
import org.grnet.cat.utils.Utility;
import org.grnet.cat.validators.SortAndOrderValidator;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

@Path("/v1/registry/metrics")
@Authenticated
public class MetricEndpoint {
    @Inject
    MetricService metricService;

    @Inject
    Utility utility;

    @ConfigProperty(name = "api.server.url")
    String serverUrl;

    @Tag(name = "Metrics")
    @Operation(
            summary = "Get Metric by ID",
            description = "Retrieves a specific Metric item by ID."
    )
    @APIResponse(
            responseCode = "200",
            description = "The corresponding Metric item.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = MetricDefinitionExtendedResponse.class))
    )
    @APIResponse(
            responseCode = "404",
            description = "Metric not found.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class))
    )
    @SecurityRequirement(name = "Authentication")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    @Path("/{id}")
    public Response getMetricById(
            @Parameter(
                    description = "The ID of the Metric to retrieve.",
                    required = true,
                    example = "pid_graph:CF9B6EDF",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid @NotFoundEntity(repository = MetricRepository.class, message = "There is no Metric with the following id:") String id)
    {

        var response = metricService.getMetricById(id);

        return Response.ok(response).build();
    }

    @Tag(name = "Metrics")
    @Operation(
            summary     = "Create new Metric",
            description = "Creates a new Metric item."
    )
    @APIResponse(
            responseCode = "201",
            description = "Metric item created.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = MetricResponseDto.class))
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
            responseCode = "404",
            description = "Entity Not Found.",
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
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response createMetric(
            @Valid @NotNull(message = "The request body is empty.") MetricRequestDto metricRequestDto, @Context UriInfo uriInfo) {

        var metric = metricService.createMetric(utility.getUserUniqueIdentifier(),metricRequestDto);
        var serverInfo = new CatServiceUriInfo(serverUrl.concat(uriInfo.getPath()));

        return Response.created(serverInfo.getAbsolutePathBuilder().path(String.valueOf(metric.id)).build()).entity(metric).build();
    }

    @Tag(name = "Metrics")
    @Operation(
            summary = "Update Metric",
            description = "Updates an existing Metric item."
    )
    @APIResponse(
            responseCode = "200",
            description = "Subject was updated successfully.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = MetricResponseDto.class)))
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
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Registration
    @Path("/{id}")
    public Response updateMetric(
            @Parameter(
                    description = "The ID of the Metric to update.",
                    required = true,
                    example = "pid_graph:3E109BBA",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid @NotFoundEntity(repository = MetricRepository.class, message = "There is no Metric with the following id:") String id,

            @Valid MetricUpdateDto metricUpdateDto) {

        var updatedDto = metricService.updateMetric(id, utility.getUserUniqueIdentifier(), metricUpdateDto);

        return Response.ok(updatedDto).build();
    }

    @Tag(name = "Metrics")
    @Operation(
            summary = "Delete Metric",
            description = "Deletes a specific Metric item by ID.")
    @APIResponse(
            responseCode = "204",
            description = "Metric item deleted.",
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
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    @Path("/{id}")
    public Response deleteMetric(
            @Parameter(
                    description = "The ID of the Metric to be deleted.",
                    required = true,
                    example = "pid_graph:3E109BBA",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid @NotFoundEntity(repository = MetricRepository.class, message = "There is no Metric with the following id:") String id) {

       var deleted =  metricService.deleteMetric(id);

        InformativeResponse informativeResponse = new InformativeResponse();

        if (!deleted) {

            informativeResponse.code =500;
            informativeResponse.message = "Metric hasn't been deleted. An error occurred.";
        } else {

            informativeResponse.code = 200;
            informativeResponse.message = "Metric has been successfully deleted.";
        }

        return Response.ok().entity(informativeResponse).build();

    }

    @Tag(name = "Metrics")
    @Operation(
            summary = "List all Metrics",
            description = "Retrieves a paginated list of Metrics."
    )
    @APIResponse(
            responseCode = "200",
            description = "List of Metrics.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableMetricResponse.class)))
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
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response listMetrics(
            @Parameter(name="Search", in = QUERY,
                    description = "The \"search\" parameter allows clients to search " +
                            "for matches in specific fields in the MetricDefinition entity. " +
                            "The search will be conducted in the following fields: " +
                            "metric ID, metric MTR, metric Label, type benchmark ID, type benchmark Label, motivation ID")
            @QueryParam("search") String search,
            @Parameter(name = "Sort", in = QUERY,
                    schema = @Schema(type = SchemaType.STRING, defaultValue = "lastTouch"),
                    examples = {
                            @ExampleObject(name = "Last Touch", value = "lastTouch"),
                            @ExampleObject(name = "MTR", value = "metric.MTR"),
                            @ExampleObject(name = "Label", value = "metric.metricLabel"),
                            @ExampleObject(name = "Description", value = "metric.descrMetric"),
                            @ExampleObject(name = "Benchmark Value", value = "valueBenchmark")},
                    description = "The \"sort\" parameter allows clients to specify the field by which they want the results to be sorted.")
            @DefaultValue("lastTouch")
            @QueryParam("sort") String sort,
            @Parameter(name = "Order", in = QUERY,
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
        var sortValues = List.of("lastTouch", "metric.MTR","metric.metricLabel","metric.metricDescr", "valueBenchmark");

        SortAndOrderValidator.validateSortAndOrder(sort, order, sortValues, orderValues);

        var metrics = metricService.getMetricListAll(search, sort, order, page - 1, size, uriInfo);

        return Response.ok().entity(metrics).build();
    }

    public static class PageableMetricResponse extends PageResource<MetricDefinitionExtendedResponse> {

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
}
