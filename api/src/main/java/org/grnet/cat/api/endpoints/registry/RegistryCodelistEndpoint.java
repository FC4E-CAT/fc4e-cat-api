package org.grnet.cat.api.endpoints.registry;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
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
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.grnet.cat.api.filters.Registration;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.codelist.TypeCriterionResponse;
import org.grnet.cat.repositories.registry.TypeCriterionRepository;
import org.grnet.cat.services.registry.TypeCriterionService;
import org.grnet.cat.utils.Utility;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

@Path("/v1/registry/codelist")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RegistryCodelistEndpoint {

    @Inject
    TypeCriterionService typeCriterionService;

    @ConfigProperty(name = "api.server.url")
    String serverUrl;

    @Inject
    Utility utility;

    @Tag(name = "Codelist")
    @Operation(
            summary = "Get specific Type Criterion.",
            description = "Returns a specific Type Criterion.")
    @APIResponse(
            responseCode = "200",
            description = "The corresponding Type Criterion.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = TypeCriterionResponse.class)))
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
    @Path("/type-criterion/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response getTypeCriterion(@Parameter(
            description = "The ID of the Type Criterion to retrieve.",
            required = true,
            example = "pid_graph:3E109BBA",
            schema = @Schema(type = SchemaType.STRING))
                                  @PathParam("id")
                                  @Valid @NotFoundEntity(repository = TypeCriterionRepository.class, message = "There is no Type Criterion with the following id:") String id) {

        var typeCriterion = typeCriterionService.getTypeCriterionById(id);
        return Response.ok().entity(typeCriterion).build();
    }

    @Tag(name = "Codelist")
    @Operation(
            summary = "Get list of Type Criterion.",
            description = "This endpoint retrieves all Type Criterion." +
                    "By default, the first page of 10 Type Criterion will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of Type Criterion.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableTypeCriterionResponse.class)))
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
    @Path("/type-criterion")
    public Response getTypeCriterionList(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                   @Parameter(name = "size", in = QUERY,
                                           description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                   @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                   @Context UriInfo uriInfo) {

        var typeCriterionList = typeCriterionService.getTypeCriterionListByPage(page - 1, size, uriInfo);

        return Response.ok().entity(typeCriterionList).build();
    }

    public static class PageableTypeCriterionResponse extends PageResource<TypeCriterionResponse> {

        private List<TypeCriterionResponse> content;

        @Override
        public List<TypeCriterionResponse> getContent() {
            return content;
        }

        @Override
        public void setContent(List<TypeCriterionResponse> content) {
            this.content = content;
        }
    }


}
