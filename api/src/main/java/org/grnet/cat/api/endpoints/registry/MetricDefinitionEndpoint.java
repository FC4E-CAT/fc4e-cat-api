package org.grnet.cat.api.endpoints.registry;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.MetricDefinitionResponseDto;
import org.grnet.cat.validators.SortAndOrderValidator;

import org.grnet.cat.services.registry.MetricDefinitionService;
import org.grnet.cat.utils.Utility;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;


@Path("/v1/registry/metric-definitions")
@Authenticated
public class MetricDefinitionEndpoint {

    @Inject
    MetricDefinitionService metricDefinitionService;

    @Inject
    Utility utility;

    @ConfigProperty(name = "api.server.url")
    String serverUrl;


    /**
     * Retrieves a paginated list of MetricDefinitions.
     *
     * @param page    The page number to retrieve.
     * @param size    The number of MetricDefinitions per page.
     * @param uriInfo The Uri Info for pagination.
     * @return A paginated list of Metric Definitions.
     */
    @Tag(name = "Relations")
    @Operation(
            summary = "Retrieve a list of Metric Definitions.",
            description = "Retrieve a list of Metric Definitions.")
    @APIResponse(
            responseCode = "200",
            description = "Metric Definition retrieved successfully.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = MetricDefinitionResponse.class)))
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
            description = "Metric Definition already exists.",
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
    public Response listMetricDefinitionsWithSearch(
            @Parameter(name="Search", in = QUERY,
                    description = "The \"search\" parameter allows clients to search " +
                            "for matches in specific fields in the MetricDefinition entity. " +
                            "The search will be conducted in the following fields: " +
                            "metric ID, type benchmark ID, motivation ID, and motivation description.")
            @QueryParam("search") String search,
            @Parameter(name = "Sort", in = QUERY,
                    schema = @Schema(type = SchemaType.STRING, defaultValue = "lastTouch"),
                    examples = {
                            @ExampleObject(name = "Last Touch", value = "lastTouch"),
                            @ExampleObject(name = "Metric", value = "metric"),
                            @ExampleObject(name = "Type Benchmark", value = "typeBenchmark"),
                            @ExampleObject(name = "Metric Definition", value = "metricDefinition")},
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
        var sortValues = List.of("lastTouch", "metric", "typeBenchmark","metricDefinition");

        SortAndOrderValidator.validateSortAndOrder(sort, order, sortValues, orderValues);

        var pageResource = metricDefinitionService.getMetricDefinitionsWithSearch(search, sort, order,page - 1, size, uriInfo);

        return Response.ok(pageResource).build();
    }

    public static class MetricDefinitionResponse extends PageResource<MetricDefinitionResponseDto> {

        private List<MetricDefinitionResponseDto> content;

        @Override
        public List<MetricDefinitionResponseDto> getContent() {
            return content;
        }

        @Override
        public void setContent(List<MetricDefinitionResponseDto> content) {
            this.content = content;
        }
    }

}
