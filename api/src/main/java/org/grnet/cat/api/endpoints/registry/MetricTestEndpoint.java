package org.grnet.cat.api.endpoints.registry;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
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
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.MetricTestResponseDto;
import org.grnet.cat.dtos.registry.test.TestResponseDto;
import org.grnet.cat.repositories.registry.metric.MetricRepository;
import org.grnet.cat.services.registry.MetricTestService;
import org.grnet.cat.dtos.InformativeResponse;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.grnet.cat.validators.SortAndOrderValidator;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

@Path("/v1/registry/metric-test")
@Tag(name = "Relations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MetricTestEndpoint {

    @Inject
    MetricTestService metricTestService;

    @Operation(
            summary = "List all Metric-Test relations",
            description = "Retrieves a paginated list of Metric-Test relations."
    )
    @APIResponse(
            responseCode = "200",
            description = "List of Metric-Test relations.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableMetricTestResponse.class))
    )
    @APIResponse(
            responseCode = "401",
            description = "User has not been authenticated.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class))
    )
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
                    implementation = InformativeResponse.class))
    )
    @APIResponse(
            responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class))
    )
    @SecurityRequirement(name = "Authentication")
    @GET
    @Path("/")
    @Registration
    public Response listMetricTestsWithSearch(
            @Parameter(name = "Search", in = QUERY,
                    description = "The \"search\" parameter allows clients to search " +
                            "for matches in specific fields in the MetricTest entity. " +
                            "The search will be conducted in the following fields: " +
                            "metric ID, test ID, test definition ID, motivation ID, and motivation description.")
            @QueryParam("search") String search,
            @Parameter(name = "Sort", in = QUERY,
                    schema = @Schema(type = SchemaType.STRING, defaultValue = "lastTouch"),
                    examples = {
                            @ExampleObject(name = "Last Touch", value = "lastTouch"),
                            @ExampleObject(name = "Metric", value = "metric"),
                            @ExampleObject(name = "Test", value = "test"),
                            @ExampleObject(name = "Motivation", value = "motivation")},
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
        var sortValues = List.of("lastTouch", "metric", "test", "motivation");

        SortAndOrderValidator.validateSortAndOrder(sort, order, sortValues, orderValues);

        var metricTests = metricTestService.getMetricTestWithSearch(search, order, sort,page - 1, size, uriInfo);

        return Response.ok().entity(metricTests).build();
    }

    public static class PageableMetricTestResponse extends PageResource<MetricTestResponseDto> {

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

}
