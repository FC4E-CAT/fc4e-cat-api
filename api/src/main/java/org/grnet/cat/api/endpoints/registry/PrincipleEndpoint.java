package org.grnet.cat.api.endpoints.registry;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
import org.grnet.cat.api.utils.CatServiceUriInfo;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.registry.principle.PrincipleRequestDto;
import org.grnet.cat.dtos.registry.principle.PrincipleResponseDto;
import org.grnet.cat.dtos.registry.principle.PrincipleUpdateDto;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.repositories.registry.PrincipleRepository;
import org.grnet.cat.services.registry.PrincipleService;
import org.grnet.cat.utils.Utility;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

@Path("/v1/registry/principles")
@Authenticated
@SecurityScheme(securitySchemeName = "Authentication",
        description = "JWT token",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER)
public class PrincipleEndpoint {

    /**
     * Injection point for the Principle Service
     */
    @Inject
    PrincipleService principleService;

    /**
     * Injection point for the Utility service
     */
    @Inject
    Utility utility;

    @ConfigProperty(name = "api.server.url")
    String serverUrl;

    @Tag(name = "Principle")
    @Operation(
            summary = "Create New Principle Item.",
            description = "Creates a new principle item.")
    @APIResponse(
            responseCode = "201",
            description = "Principle item created.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PrincipleResponseDto.class)))
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPrinciple(@Valid @NotNull(message = "The request body is empty.") PrincipleRequestDto principleRequestDto, @Context UriInfo uriInfo) {

        var principle = principleService.create(principleRequestDto, utility.getUserUniqueIdentifier());

        var serverInfo = new CatServiceUriInfo(serverUrl.concat(uriInfo.getPath()));

        return Response.created(serverInfo.getAbsolutePathBuilder().path(String.valueOf(principle.id)).build()).entity(principle).build();
    }

    @Tag(name = "Principle")
    @Operation(
            summary = "Get Principle by ID.",
            description = "Retrieves a specific principle item by ID.")
    @APIResponse(
            responseCode = "200",
            description = "The corresponding principle item.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PrincipleResponseDto.class)))
    @APIResponse(
            responseCode = "400",
            description = "Invalid UUID: must be a string of letters and numbers",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PrincipleResponseDto.class)))
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPrincipleById(@Parameter(
            description = "The ID of the principle item to retrieve.",
            required = true,
            example = "pid_graph:6279AE43",
            schema = @Schema(type = SchemaType.STRING)) @PathParam("id")
                                          @Valid @NotFoundEntity(repository = PrincipleRepository.class, message = "There is no Principle with the following id:") String id) {

        var principle = principleService.findById(id);

        return Response.ok(principle).build();
    }

    @Tag(name = "Principle")
    @Operation(
            summary = "List all principle items.",
            description = "Retrieves a paginated list of all principle items.")
    @APIResponse(
            responseCode = "200",
            description = "List of principle items.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageablePrincipleResponse.class)))
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
    public Response listAllPrinciples(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                      @Parameter(name = "size", in = QUERY,
                                              description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                      @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                      @Context UriInfo uriInfo) {

        var principles = principleService.listAll(page - 1, size, uriInfo);
        return Response.ok(principles).build();
    }

    @Tag(name = "Principle")
    @Operation(
            summary = "Update Principle Item.",
            description = "Updates an existing principle item.")
    @APIResponse(
            responseCode = "200",
            description = "Principle item updated.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PrincipleResponseDto.class)))
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePrinciple(@Parameter(
            description = "The ID of the principle item to update.",
            required = true,
            example = "pid_graph:7DE44287",
            schema = @Schema(type = SchemaType.STRING)) @PathParam("id")
                                        @Valid @NotFoundEntity(repository = PrincipleRepository.class, message = "There is no Principle with the following id:") String id, @Valid @NotNull(message = "The request body is empty.") PrincipleUpdateDto principleRequestDto) {

        var principle = principleService.update(id, principleRequestDto, utility.getUserUniqueIdentifier());
        return Response.ok(principle).build();
    }

    @Tag(name = "Principle")
    @Operation(
            summary = "Delete Principle Item.",
            description = "Deletes a specific principle item by ID.")
    @APIResponse(
            responseCode = "200",
            description = "Principle item deleted.")
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePrinciple(@Parameter(
            description = "The ID of the principle item to delete.",
            required = true,
            example = "pid_graph:7DE44287",
            schema = @Schema(type = SchemaType.STRING)) @PathParam("id")
                                        @Valid @NotFoundEntity(repository = PrincipleRepository.class, message = "There is no Principle with the following id:") String id) {

        boolean deleted = principleService.delete(id);

        InformativeResponse informativeResponse = new InformativeResponse();

        if (!deleted) {

            informativeResponse.code =500;
            informativeResponse.message = "Principle hasn't been deleted. An error occurred.";
        } else {

            informativeResponse.code = 200;
            informativeResponse.message = "Principle has been successfully deleted.";
        }

        return Response.ok().entity(informativeResponse).build();
    }

    public static class PageablePrincipleResponse extends PageResource<PrincipleResponseDto> {

        private List<PrincipleResponseDto> content;

        @Override
        public List<PrincipleResponseDto> getContent() {
            return content;
        }

        @Override
        public void setContent(List<PrincipleResponseDto> content) {
            this.content = content;
        }
    }
}
