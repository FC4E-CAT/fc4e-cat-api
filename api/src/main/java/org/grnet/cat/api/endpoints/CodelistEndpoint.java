package org.grnet.cat.api.endpoints;

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
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.grnet.cat.api.endpoints.registry.RegistryCodelistEndpoint;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.services.registry.RegistryActorService;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

@Path("/v1/codelist")
public class CodelistEndpoint {
    @Inject
    RegistryActorService registryActorService;


    @Tag(name = "Codelist")
    @Operation(
            summary = "Get list of Registry Actors.",
            description = "This endpoint retrieves all Registry Actors" +
                    "By default, the first page of 10 Registry Actor will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of Registry Actors.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = RegistryCodelistEndpoint.PageableRegistryActorResponse.class)))
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
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/registry-actors")
    public Response getRegistryActors(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                 @Parameter(name = "size", in = QUERY,
                                         description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                 @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                 @Context UriInfo uriInfo) {

        var actorList = registryActorService.getActorListByPage(page - 1, size, uriInfo);

        return Response.ok().entity(actorList).build();
    }
}