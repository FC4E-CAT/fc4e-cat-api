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
import org.grnet.cat.dtos.registry.metric.TypeAlgorithmResponseDto;
import org.grnet.cat.repositories.registry.metric.TypeAlgorithmRepository;
import org.grnet.cat.services.registry.metric.TypeAlgorithmService;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

@Path("/v1/registry/type-algorithm")
@Authenticated
public class TypeAlgorithmEndpoint {

    @Inject
    TypeAlgorithmService typeAlgorithmService;

    @Tag(name = "Metrics")
    @Operation(
            summary = "Get Type Algorithm by ID.",
            description = "Retrieves a specific Type Algorithm item by ID.")
    @APIResponse(
            responseCode = "200",
            description = "The corresponding Type Algorithm item.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = TypeAlgorithmResponseDto.class)))
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
    @Path("/{id}")
    public Response getTypeAlgorithmById(
            @Parameter(
                    description = "The ID of the Type Algorithm to be retrieved.",
                    required = true,
                    example = "pid_graph:3E109BBA",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid @NotFoundEntity(repository = TypeAlgorithmRepository.class, message = "There is no Type Algorithm with the following id:") String id) {

        var response = typeAlgorithmService.getTypeAlgorithmById(id);

        return Response.ok(response).build();
    }

    @Tag(name = "Metrics")
    @Operation(
            summary = "List all Type Algorithm items.",
            description = "Retrieves a paginated list of Type Algorithm items.")
    @APIResponse(
            responseCode = "200",
            description = "List of Type Algorithms.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableTypeAlgorithmResponse.class)))
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
    public Response listTypeAlgorithm(
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

        var typeAlgorithm = typeAlgorithmService.listAll(page - 1, size, uriInfo);

        return Response.ok(typeAlgorithm).build();
    }

    public static class PageableTypeAlgorithmResponse extends PageResource<TypeAlgorithmResponseDto> {

        private List<TypeAlgorithmResponseDto> content;

        @Override
        public List<TypeAlgorithmResponseDto> getContent() {
            return content;
        }

        @Override
        public void setContent(List<TypeAlgorithmResponseDto> content) {
            this.content = content;
        }
    }

}