package org.grnet.cat.api.endpoints.registry;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
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
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeIn;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.grnet.cat.api.filters.Registration;
import org.grnet.cat.api.utils.CatServiceUriInfo;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.registry.criterion.CriterionRequest;
import org.grnet.cat.dtos.registry.criterion.CriterionResponse;
import org.grnet.cat.dtos.registry.criterion.CriterionUpdate;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.repositories.registry.CriterionRepository;
import org.grnet.cat.services.registry.CriterionService;
import org.grnet.cat.utils.Utility;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

@Path("/v1/registry/criteria")
@Authenticated
@SecurityScheme(securitySchemeName = "Authentication",
        description = "JWT token",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER)
public class CriteriaEndpoint {

    @Inject
    CriterionService criterionService;

    /**
     * Injection point for the Utility service
     */
    @Inject
    Utility utility;

    @ConfigProperty(name = "api.server.url")
    String serverUrl;

    @Tag(name = "Criterion")
    @Operation(
            summary = "Create New Criterion.",
            description = "Creates a new Criterion.")
    @APIResponse(
            responseCode = "201",
            description = "Criterion created.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = CriterionResponse.class)))
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
    @Registration
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCriterion(@Valid @NotNull(message = "The request body is empty.") CriterionRequest criterionRequest,
                                   @Context UriInfo uriInfo) {

        var criteria = criterionService.create(criterionRequest, utility.getUserUniqueIdentifier());

        var serverInfo = new CatServiceUriInfo(serverUrl.concat(uriInfo.getPath()));

        return Response.created(serverInfo.getAbsolutePathBuilder().path(String.valueOf(criteria.id)).build()).entity(criteria).build();
    }

    @Tag(name = "Criterion")
    @Operation(
            summary = "Get Criterion by ID.",
            description = "Retrieves a specific Criterion by ID.")
    @APIResponse(
            responseCode = "200",
            description = "The corresponding Criterion.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = CriterionResponse.class)))
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
    @Registration
    @Produces(MediaType.APPLICATION_JSON)
    public Response findCriterionById(@Parameter(
            description = "The ID of the Criterion item to retrieve.",
            required = true,
            example = "1",
            schema = @Schema(type = SchemaType.STRING))
                                     @PathParam("id")
                                         @Valid @NotFoundEntity(repository = CriterionRepository.class, message = "There is no Criterion with the following id:") String id) {

        var criteria = criterionService.findById(id);

        return Response.ok(criteria).build();
    }

    @Tag(name = "Criterion")
    @Operation(
            summary = "List all criteria items.",
            description = "Retrieves a paginated list of all criteria items.")
    @APIResponse(
            responseCode = "200",
            description = "List of criteria items.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableCriteriaResponse.class)))
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
    @GET
    @Registration
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAllCriteria(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                    @Parameter(name = "size", in = QUERY,
                                            description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                    @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                    @Context UriInfo uriInfo) {

        var criterias = criterionService.listAll(page - 1, size, uriInfo);
        return Response.ok(criterias).build();
    }

    @Tag(name = "Criterion")
    @Operation(
            summary = "Update Criterion.",
            description = "Updates an existing Criterion.")
    @APIResponse(
            responseCode = "200",
            description = "Criterion updated.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = CriterionResponse.class)))
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
    @PATCH
    @Path("/{id}")
    @Registration
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCriteria(@Parameter(
            description = "The ID of the criteria item to update.",
            required = true,
            example = "1",
            schema = @Schema(type = SchemaType.STRING)) @PathParam("id")
                                   @Valid @NotFoundEntity(repository = CriterionRepository.class, message = "There is no Criterion with the following id:") String id,
                                   @Valid @NotNull(message = "The request body is empty.") CriterionUpdate criteriaUpdateDto) {

        var criteria = criterionService.update(id, criteriaUpdateDto, utility.getUserUniqueIdentifier());
        return Response.ok(criteria).build();
    }

    @Tag(name = "Criterion")
    @Operation(
            summary = "Delete Criterion.",
            description = "Deletes a specific Criterion by ID.")
    @APIResponse(
            responseCode = "204",
            description = "Criterion deleted.")
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
    @Registration
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCriteria(@Parameter(
            description = "The ID of the Criterion to delete.",
            required = true,
            example = "1",
            schema = @Schema(type = SchemaType.STRING))
                                   @PathParam("id")
                                       @Valid @NotFoundEntity(repository = CriterionRepository.class, message = "There is no Criterion with the following id:") String id) {

        boolean deleted = criterionService.delete(id);


        InformativeResponse informativeResponse = new InformativeResponse();

        if (!deleted) {

            informativeResponse.code =500;
            informativeResponse.message = "Criterion hasn't been deleted. An error occurred.";
        } else {

            informativeResponse.code = 200;
            informativeResponse.message = "Criterion has been successfully deleted.";
        }

        return Response.ok().entity(informativeResponse).build();
    }

    public static class PageableCriteriaResponse extends PageResource<CriterionResponse> {

        private List<CriterionResponse> content;

        @Override
        public List<CriterionResponse> getContent() {
            return content;
        }

        @Override
        public void setContent(List<CriterionResponse> content) {
            this.content = content;
        }
    }
}
